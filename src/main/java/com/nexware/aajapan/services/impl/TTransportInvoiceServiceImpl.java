package com.nexware.aajapan.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexware.aajapan.core.AccountTransactionConstants;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.models.MBank;
import com.nexware.aajapan.models.MTransporter;
import com.nexware.aajapan.models.TAccountsTransaction;
import com.nexware.aajapan.models.TCancelledInvoices;
import com.nexware.aajapan.models.TInventoryCost;
import com.nexware.aajapan.models.TInvoicePaymentTransaction;
import com.nexware.aajapan.models.TPurchaseInvoice;
import com.nexware.aajapan.models.TReAuction;
import com.nexware.aajapan.models.TStock;
import com.nexware.aajapan.models.TTransportInvoice;
import com.nexware.aajapan.repositories.InvoicePaymentTransactionRepository;
import com.nexware.aajapan.repositories.MasterBankRepository;
import com.nexware.aajapan.repositories.TCancelledInvoiceRepo;
import com.nexware.aajapan.repositories.TTransportInvoiceRepository;
import com.nexware.aajapan.repositories.TransportersRepository;
import com.nexware.aajapan.services.COATransactionService;
import com.nexware.aajapan.services.InvoicePaymentTransactionService;
import com.nexware.aajapan.services.TAccountsTransactionService;
import com.nexware.aajapan.services.TInventoryCostService;
import com.nexware.aajapan.services.TTransportInvoiceService;
import com.nexware.aajapan.utils.AppUtil;

@Service
@Transactional
public class TTransportInvoiceServiceImpl implements TTransportInvoiceService {

	@Autowired
	private TAccountsTransactionService accountsTransactionService;
	@Autowired
	private TransportersRepository transportersRepository;
	@Autowired
	private InvoicePaymentTransactionService invoicePaymentTransactionService;
	@Autowired
	private TTransportInvoiceRepository transportInvoiceRepository;
	@Autowired
	private MasterBankRepository masterBankRepository;
	@Autowired
	private COATransactionService coaTransactionService;
	@Autowired
	private TInventoryCostService inventoryCostService;
	@Autowired
	private InvoicePaymentTransactionRepository invoicePaymentTransactionRepository;
	@Autowired
	private TCancelledInvoiceRepo cancelledinvoiceRepo;

	@Override
	public void transportInvoicePaymentApproveTransactions(List<TTransportInvoice> transportInvoices) {
		this.transportInvoiceRepository.saveAll(transportInvoices);
		TTransportInvoice transportInvoice = transportInvoices.get(0);
		// create account transaction entry - Amount - credit
		Double totalAmount = transportInvoices.stream().mapToDouble(TTransportInvoice::getInvoiceTotal).sum();
		MTransporter mTransporter = this.transportersRepository.findOneByCode(transportInvoice.getTransporterId());

		transportInvoices.stream().forEach(invoice -> {

			// create account transaction entry - Amount - debit
			if (!AppUtil.isObjectEmpty(invoice.getStockNo())) {
				this.accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(invoice.getInvoiceNo(),
						mTransporter.getName(),
						this.coaTransactionService.checkStockInventoryStatusAndGetCoaCode(invoice.getStockNo(),
								AccountTransactionConstants.TRANSPORT_COST_OF_GOOD_SOLD_RIKUSO),
						Constants.CURRENCY_YEN, Constants.TRANSACTION_DEBIT, invoice.getAmount(),
						AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_TRANSPORT, invoice.getInvoiceDate()));
			} else {
				String[] arrOfStr = invoice.getDropLocationCustom().split("- ", 0);
				this.accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(invoice.getInvoiceNo(),
						mTransporter.getName(), Long.parseLong(arrOfStr[1]), Constants.CURRENCY_YEN,
						Constants.TRANSACTION_DEBIT, invoice.getAmount(),
						AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_TRANSPORT, invoice.getInvoiceDate()));
			}

			this.accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(invoice.getInvoiceNo(),
					mTransporter.getName(), AccountTransactionConstants.CONSUMPTION_TAX, Constants.CURRENCY_YEN,
					Constants.TRANSACTION_DEBIT, invoice.getTaxAmount(),
					AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_TRANSPORT, invoice.getInvoiceDate()));
		});

		String invoiceNo = transportInvoice.getInvoiceNo();

		this.accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(invoiceNo,
				mTransporter.getName(), AccountTransactionConstants.TRANSPORT_RIKUSO_PAYABLE, Constants.CURRENCY_YEN,
				Constants.TRANSACTION_CREDIT, totalAmount,
				AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_TRANSPORT, transportInvoice.getInvoiceDate()));

		// Inventory Cost List Save
		List<TInventoryCost> inventoryCosts = new ArrayList<>();
		for (TTransportInvoice tTransportInvoice : transportInvoices) {
			TInventoryCost cost = new TInventoryCost(Constants.COST_OF_GOODS_TYPE_TRANSPORT,
					tTransportInvoice.getStockNo(), tTransportInvoice.getInvoiceTotal(),
					tTransportInvoice.getInvoiceNo());
			inventoryCosts.add(cost);
		}
		this.inventoryCostService.saveInventoryCost(inventoryCosts);

	}

	@Override
	public void completePayment(TTransportInvoice invoice) {
		String invoiceNo = invoice.getInvoiceRefNo();
		String remitter = invoice.getTransporterName();
		final List<TInvoicePaymentTransaction> transactions = invoicePaymentTransactionRepository
				.findAllByInvoiceNoAndStatus(invoiceNo, Constants.INVOICE_PAYMENT_TRANSACTION_NOT_CANCELLED);
		for (TInvoicePaymentTransaction transaction : transactions) {
			final MBank bank = masterBankRepository.findOneByBankSeq(transaction.getBankId());
			this.accountsTransactionService.accountTransactionEntry(
					new TAccountsTransaction(invoiceNo, remitter, AccountTransactionConstants.TRANSPORT_RIKUSO_PAYABLE,
							Constants.CURRENCY_YEN, Constants.TRANSACTION_DEBIT, transaction.getAmount(),
							AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_TRANSPORT, transaction.getCode(),
							invoice.getInvoiceDate()));
			// create account transaction entry - Amount - credit
			this.accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(invoiceNo, remitter,
					bank.getCoaCode(), Constants.CURRENCY_YEN, Constants.TRANSACTION_CREDIT, transaction.getAmount(),
					AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_TRANSPORT, transaction.getCode(),
					invoice.getInvoiceDate()));

		}

	}
	
	@Override
	public void revertTransportInvoice(String invoiceRefNo,String cancelledRemarks) {
		final List<TTransportInvoice> invoices = transportInvoiceRepository.findAllByInvoiceRefNo(invoiceRefNo);

		// Cancelled Invoice Entry History
		TCancelledInvoices cancelinvoice = new TCancelledInvoices();
		cancelinvoice.setTransporterCode(invoices.stream().findFirst().get().getTransporterId());
		cancelinvoice.setInvoiceNo(invoices.stream().findFirst().get().getInvoiceRefNo());
		cancelinvoice.setRefNo(invoices.stream().findFirst().get().getRefNo());
		cancelinvoice.setInvoiceDate(invoices.stream().findFirst().get().getInvoiceDate());
		double invCost = invoices.stream().mapToDouble(TTransportInvoice::getTotalTaxIncluded).sum();
		cancelinvoice.setInvoiceType(Constants.INVOICE_TYPE_TRANSPORT);
		cancelinvoice.setInvoiceAmount(invCost);
		cancelinvoice.setCancelledDate(new Date());
		cancelinvoice.setCancellationRemarks(cancelledRemarks);
		cancelledinvoiceRepo.save(cancelinvoice);
		// Cancelled Invoice History End

		for (final TTransportInvoice transportInvoice : invoices) {
			transportInvoice.setStatus(Constants.TRANSPORT_INVOICE_NOT_BOOKED);
			transportInvoice.setPaymentApprove(Constants.PAYMENT_NOT_APPROVED);
			transportInvoiceRepository.save(transportInvoice);
			
		}

	}

	@Override
	public void revertPaymentTransaction(String invoiceNo, Double amount) {

		final List<TTransportInvoice> invoices = transportInvoiceRepository.findAllByInvoiceRefNo(invoiceNo);

		// calculate invoice total
		final Double invoiceTotal = invoices.stream().mapToDouble(TTransportInvoice::getInvoiceTotal).sum();

		// get total paid amount
		final Double totalPaidAmount = invoicePaymentTransactionService
				.getTotalTransactionAmount(Constants.INVOICE_TYPE_TRANSPORT, invoiceNo);

		final Double amountAfterTransaction = totalPaidAmount - amount;
		Integer paymentStatus;
		if (amountAfterTransaction >= invoiceTotal) {
			paymentStatus = Constants.PAYMENT_COMPLETED;
		} else if (amountAfterTransaction > 0) {
			paymentStatus = Constants.PAYMENT_PARTIAL;
		} else {
			paymentStatus = Constants.PAYMENT_APPROVED;
		}
		for (final TTransportInvoice invoice : invoices) {
			invoice.setPaymentApprove(paymentStatus);
			invoice.setInvoiceAmountReceived(amountAfterTransaction);
		}
		transportInvoiceRepository.saveAll(invoices);

	}
}
