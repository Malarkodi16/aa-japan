package com.nexware.aajapan.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexware.aajapan.core.AccountTransactionConstants;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.exceptions.AAJRuntimeException;
import com.nexware.aajapan.models.ApprovePaymentDetails;
import com.nexware.aajapan.models.MBank;
import com.nexware.aajapan.models.MForwarder;
import com.nexware.aajapan.models.TAccountsTransaction;
import com.nexware.aajapan.models.TFreightShippingInvoice;
import com.nexware.aajapan.models.TInventoryCost;
import com.nexware.aajapan.models.TInvoicePaymentTransaction;
import com.nexware.aajapan.repositories.InvoicePaymentTransactionRepository;
import com.nexware.aajapan.repositories.MForwarderRepository;
import com.nexware.aajapan.repositories.MasterBankRepository;
import com.nexware.aajapan.repositories.TFreightShippingInvoiceRepository;
import com.nexware.aajapan.services.BankTransactionService;
import com.nexware.aajapan.services.COATransactionService;
import com.nexware.aajapan.services.InvoicePaymentTransactionService;
import com.nexware.aajapan.services.TAccountsTransactionService;
import com.nexware.aajapan.services.TFreightInvoiceService;
import com.nexware.aajapan.services.TInventoryCostService;
import com.nexware.aajapan.utils.AppUtil;

@Service
@Transactional
public class TFreightInvoiceServiceImpl implements TFreightInvoiceService {

	@Autowired
	private TAccountsTransactionService accountsTransactionService;

	@Autowired
	private MForwarderRepository mForwarderRepository;
	@Autowired
	private MasterBankRepository masterBankRepository;
	@Autowired
	private InvoicePaymentTransactionService invoicePaymentTransactionService;
	@Autowired
	private TFreightShippingInvoiceRepository freightShippingInvoiceRepository;
	@Autowired
	private COATransactionService coaTransactionService;
	@Autowired
	private TInventoryCostService inventoryCostService;
	@Autowired
	private BankTransactionService bankTransactionService;
	@Autowired
	private InvoicePaymentTransactionRepository invoicePaymentTransactionRepository;

	@Override
	public void freightInvoicePaymentApproveTransactions(List<TFreightShippingInvoice> tFreightShippingInvoices) {
		this.freightShippingInvoiceRepository.saveAll(tFreightShippingInvoices);
		TFreightShippingInvoice freightShippingInvoice = tFreightShippingInvoices.get(0);
		MForwarder mForwarder = this.mForwarderRepository.findOneByCode(freightShippingInvoice.getForwarder());
		String invoiceNo = freightShippingInvoice.getInvoiceNo();
		Date invoiceDate = freightShippingInvoice.getDate();
		tFreightShippingInvoices.stream().forEach(frght -> {
			// create account transaction entry - Freight - Debit
			if (!AppUtil.isObjectEmpty(frght.getFreightCharge())) {
				this.accountsTransactionService
						.accountTransactionEntry(new TAccountsTransaction(frght.getInvoiceNo(), mForwarder.getName(),
								this.coaTransactionService.checkStockInventoryStatusAndGetCoaCode(frght.getStockNo(),
										AccountTransactionConstants.FREIGHT_COST_OF_GOODS_SOLD),
								Constants.CURRENCY_YEN, Constants.TRANSACTION_DEBIT, frght.getFreightCharge(),
								AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_FREIGHT, invoiceDate));
			}
			// create account transaction entry - Shipping - Debit
			if (!AppUtil.isObjectEmpty(frght.getShippingCharge())) {
				this.accountsTransactionService
						.accountTransactionEntry(new TAccountsTransaction(frght.getInvoiceNo(), mForwarder.getName(),
								this.coaTransactionService.checkStockInventoryStatusAndGetCoaCode(frght.getStockNo(),
										AccountTransactionConstants.SHIPPING_COST_OF_GOODS_SOLD),
								Constants.CURRENCY_YEN, Constants.TRANSACTION_DEBIT, frght.getShippingCharge(),
								AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_SHIPPING, invoiceDate));
			}
			// create account transaction entry - Inspection - Debit
			if (!AppUtil.isObjectEmpty(frght.getInspectionCharge())) {
				this.accountsTransactionService
						.accountTransactionEntry(new TAccountsTransaction(frght.getInvoiceNo(), mForwarder.getName(),
								this.coaTransactionService.checkStockInventoryStatusAndGetCoaCode(frght.getStockNo(),
										AccountTransactionConstants.INSPECTION_COST_OF_GOODS_SOLD),
								Constants.CURRENCY_YEN, Constants.TRANSACTION_DEBIT, frght.getInspectionCharge(),
								AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_INSPECTION, invoiceDate));
			}
			// create account transaction entry - Radiation - Debit
			if (!AppUtil.isObjectEmpty(frght.getRadiationCharge())) {
				this.accountsTransactionService
						.accountTransactionEntry(new TAccountsTransaction(frght.getInvoiceNo(), mForwarder.getName(),
								this.coaTransactionService.checkStockInventoryStatusAndGetCoaCode(frght.getStockNo(),
										AccountTransactionConstants.RADIATION_COST_OF_GOODS_SOLD),
								Constants.CURRENCY_YEN, Constants.TRANSACTION_DEBIT, frght.getRadiationCharge(),
								AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_RADIATION, invoiceDate));
			}
		});
		Double totalAmount = tFreightShippingInvoices.stream()
				.mapToDouble(frght -> AppUtil.ifNull(frght.getFreightCharge(), 0.0)
						+ AppUtil.ifNull(frght.getInspectionCharge(), 0.0)
						+ AppUtil.ifNull(frght.getShippingCharge(), 0.0)
						+ AppUtil.ifNull(frght.getRadiationCharge(), 0.0))
				.sum();

		// create account transaction entry - Storage & Photos - Credit
		this.accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(invoiceNo,
				mForwarder.getName(), AccountTransactionConstants.FREIGHT_SHIPPING_PAYABLE, Constants.CURRENCY_YEN,
				Constants.TRANSACTION_CREDIT, totalAmount,
				AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_FREIGHT_SHIPPING, invoiceDate));

		// Inventory Cost List Save
		List<TInventoryCost> inventoryCosts = new ArrayList<>();
		for (TFreightShippingInvoice tFreightShippingInvoice : tFreightShippingInvoices) {
			TInventoryCost cost = null;
			if (!AppUtil.isObjectEmpty(tFreightShippingInvoice.getFreightCharge())
					&& (tFreightShippingInvoice.getFreightCharge() > 0)) {
				cost = new TInventoryCost(Constants.COST_OF_GOODS_TYPE_FREIGHT, tFreightShippingInvoice.getStockNo(),
						tFreightShippingInvoice.getFreightCharge(), tFreightShippingInvoice.getInvoiceNo());
				inventoryCosts.add(cost);
			}
			if (!AppUtil.isObjectEmpty(tFreightShippingInvoice.getShippingCharge())
					&& (tFreightShippingInvoice.getShippingCharge() > 0)) {
				cost = new TInventoryCost(Constants.COST_OF_GOODS_TYPE_SHIPPING, tFreightShippingInvoice.getStockNo(),
						tFreightShippingInvoice.getShippingCharge(), tFreightShippingInvoice.getInvoiceNo());
				inventoryCosts.add(cost);
			}
			if (!AppUtil.isObjectEmpty(tFreightShippingInvoice.getInspectionCharge())
					&& (tFreightShippingInvoice.getInspectionCharge() > 0)) {
				cost = new TInventoryCost(Constants.COST_OF_GOODS_TYPE_INSPECTION, tFreightShippingInvoice.getStockNo(),
						tFreightShippingInvoice.getInspectionCharge(), tFreightShippingInvoice.getInvoiceNo());
				inventoryCosts.add(cost);
			}
			if (!AppUtil.isObjectEmpty(tFreightShippingInvoice.getRadiationCharge())
					&& (tFreightShippingInvoice.getRadiationCharge() > 0)) {
				cost = new TInventoryCost(Constants.COST_OF_GOODS_TYPE_RADIATION, tFreightShippingInvoice.getStockNo(),
						tFreightShippingInvoice.getRadiationCharge(), tFreightShippingInvoice.getInvoiceNo());
				inventoryCosts.add(cost);
			}

		}
		this.inventoryCostService.saveInventoryCost(inventoryCosts);

	}

	@Override
	public void freightInvoicePaymentCompleteTransactions(TFreightShippingInvoice invoice) {

		final List<TInvoicePaymentTransaction> transactions = invoicePaymentTransactionRepository
				.findAllByInvoiceNoAndStatus(invoice.getInvoiceNo(),
						Constants.INVOICE_PAYMENT_TRANSACTION_NOT_CANCELLED);
		for (TInvoicePaymentTransaction transaction : transactions) {
			final MBank bank = masterBankRepository.findOneByBankSeq(transaction.getBankId());
			// create account transaction entry - Photos - Credit
			this.accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(invoice.getInvoiceNo(),
					invoice.getForwarder(), bank.getCoaCode(), Constants.CURRENCY_YEN, Constants.TRANSACTION_CREDIT,
					transaction.getAmount(), AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_FREIGHT_SHIPPING,
					transaction.getInvoiceNo(), invoice.getDate()));

			// create account transaction entry - Photos - Debit
			this.accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(invoice.getInvoiceNo(),
					invoice.getForwarder(), AccountTransactionConstants.FREIGHT_SHIPPING_PAYABLE,
					Constants.CURRENCY_YEN, Constants.TRANSACTION_DEBIT, transaction.getAmount(),
					AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_FREIGHT_SHIPPING, transaction.getInvoiceNo(),
					invoice.getDate()));
		}
	}

	@Override
	public void completePayment(List<TFreightShippingInvoice> invoices, String bank, Double amount, String remarks,
			Date approvedDate) {
		if (AppUtil.isObjectEmpty(invoices) || invoices.isEmpty()) {
			throw new AAJRuntimeException("Exception while completing payment");
		}
		TFreightShippingInvoice freightShippingInvoice = invoices.get(0);
		this.freightShippingInvoiceRepository.saveAll(invoices);
		final String paymentVoucherNo = bankTransactionService.bankTransactionEntry(bank, amount,
				Constants.CURRENCY_YEN, Constants.TRANSACTION_CREDIT, "invoice payment",
				freightShippingInvoice.getInvoiceNo());
		// invoice payment transaction
		TInvoicePaymentTransaction invoicePaymentTransaction = new TInvoicePaymentTransaction(
				Constants.INVOICE_TYPE_FREIGHT, freightShippingInvoice.getInvoiceNo(), bank, amount, approvedDate,
				remarks, Constants.INVOICE_PAYMENT_TRANSACTION_NOT_CANCELLED);
		invoicePaymentTransaction.setPaymentVoucherNo(paymentVoucherNo);
		this.invoicePaymentTransactionService.saveInvoicePaymentTransaction(invoicePaymentTransaction);

	}

	@Override
	public void approveRoroInvoicePayment(final List<TFreightShippingInvoice> invoices, String bank, Date approvedDate,
			String remarks, Double amount) {
		final Double amountAfterTransaction;
		Integer paymentStatus;
		TFreightShippingInvoice invoice = invoices.get(0);
		// calculate invoice total
		Double invoiceTotal = invoices.stream().mapToDouble(TFreightShippingInvoice::getTotalAmount).sum();
		invoiceTotal = (double) Math.round(invoiceTotal);
		// get total paid amount
		final Double totalPaidAmount = invoicePaymentTransactionService
				.getTotalTransactionAmount(Constants.INVOICE_TYPE_FREIGHT, invoice.getInvoiceNo());
		final Double balanceToPay = invoiceTotal - totalPaidAmount;

		if (amount > Math.round(balanceToPay)) {
			throw new AAJRuntimeException("Amount is greater than balance.");
		}
		amountAfterTransaction = totalPaidAmount + (double) Math.round(amount);
		if (amountAfterTransaction >= invoiceTotal) {
			paymentStatus = Constants.PAYMENT_COMPLETED;
		} else {
			paymentStatus = Constants.PAYMENT_PARTIAL;
		}
		for (final TFreightShippingInvoice tFreightShippingInvoice : invoices) {
			final ApprovePaymentDetails approvePaymentDetails = new ApprovePaymentDetails(bank, approvedDate, remarks);
			tFreightShippingInvoice.setApprovePaymentDetails(approvePaymentDetails);
			tFreightShippingInvoice.setPaymentApprove(paymentStatus);
			tFreightShippingInvoice.setInvoiceAmountReceived(amountAfterTransaction);
		}

		completePayment(invoices, bank, amount, remarks, approvedDate);
	}

	@Override
	public void approveRoroFreightInvoicePayment(final List<TFreightShippingInvoice> invoices, String bank,
			Date approvedDate, String remarks, Double amount, Integer paymentCurrency) {

		TFreightShippingInvoice invoice = invoices.get(0);

		Double amountInYen;
		if (paymentCurrency.equals(Constants.CURRENCY_YEN)) {
			amountInYen = amount;
		} else {
			amountInYen = amount * invoice.getExchangeRate();
		}
		final Double amountAfterTransactionInYen;

		Integer paymentStatus;

		// calculate invoice total
		final Double invoiceTotalInYen = invoices.stream().mapToDouble(TFreightShippingInvoice::getTotalAmount).sum();
		// get total paid amount
		final Double totalPaidAmountInYen = invoicePaymentTransactionService
				.getTotalTransactionAmount(Constants.INVOICE_TYPE_FREIGHT, invoice.getInvoiceNo());
		final Double balanceToPayInYen = invoiceTotalInYen - totalPaidAmountInYen;

		if (amountInYen > Math.round(balanceToPayInYen)) {
			throw new AAJRuntimeException("Amount is greater than balance.");
		}
		amountAfterTransactionInYen = totalPaidAmountInYen + amountInYen;
		if (amountAfterTransactionInYen >= invoiceTotalInYen) {
			paymentStatus = Constants.PAYMENT_COMPLETED;
		} else {
			paymentStatus = Constants.PAYMENT_PARTIAL;
		}
		for (final TFreightShippingInvoice tFreightShippingInvoice : invoices) {
			final ApprovePaymentDetails approvePaymentDetails = new ApprovePaymentDetails(bank, approvedDate, remarks);
			tFreightShippingInvoice.setApprovePaymentDetails(approvePaymentDetails);
			tFreightShippingInvoice.setPaymentApprove(paymentStatus);
			tFreightShippingInvoice.setInvoiceAmountReceived(amountAfterTransactionInYen);
			tFreightShippingInvoice
					.setInvoiceAmountReceivedUsd(amountAfterTransactionInYen / invoice.getExchangeRate());
		}

		completePaymentFreightInvoice(invoices, bank, amount, remarks, approvedDate, invoice.getExchangeRate(),
				paymentCurrency);
	}

	@Override
	public void completePaymentFreightInvoice(List<TFreightShippingInvoice> invoices, String bank, Double amount,
			String remarks, Date approvedDate, Double exchangeRate, Integer currency) {
		if (AppUtil.isObjectEmpty(invoices) || invoices.isEmpty()) {
			throw new AAJRuntimeException("Exception while completing payment");
		}
		Double amountInYen = amount;
		if (currency != Constants.CURRENCY_YEN) {
			amountInYen = amount * exchangeRate;
		}
		String invoiceNo = invoices.get(0).getInvoiceNo();
		// invoice payment transaction
		TInvoicePaymentTransaction invoicePaymentTransaction = new TInvoicePaymentTransaction(
				Constants.INVOICE_TYPE_FREIGHT, invoiceNo, bank, amountInYen, approvedDate, remarks,
				Constants.INVOICE_PAYMENT_TRANSACTION_NOT_CANCELLED);
		// bank transaction
		final String paymentVoucherNo = bankTransactionService.bankTransactionEntry(bank, amount, currency,
				Constants.TRANSACTION_CREDIT, "invoice payment", invoiceNo);
		invoicePaymentTransaction.setPaymentVoucherNo(paymentVoucherNo);
		this.invoicePaymentTransactionService.saveInvoicePaymentTransaction(invoicePaymentTransaction);
		this.freightShippingInvoiceRepository.saveAll(invoices);

	}

	@Override
	public void approveOneRoroInvoicePayment(TFreightShippingInvoice invoice, String bank, Date approvedDate,
			String remarks, Double amount) {

	}

	@Override
	public void revertPaymentTransaction(String invoiceNo, Double amount) {
		final List<TFreightShippingInvoice> invoices = freightShippingInvoiceRepository.findAllByinvoiceNo(invoiceNo);

		// calculate invoice total
		final Double invoiceTotal = invoices.stream().mapToDouble(TFreightShippingInvoice::getTotalAmount).sum();

		// get total paid amount
		final Double totalPaidAmount = invoicePaymentTransactionService
				.getTotalTransactionAmount(Constants.INVOICE_TYPE_FREIGHT, invoiceNo);

		final Double amountAfterTransaction = totalPaidAmount - amount;
		Integer paymentStatus;
		if (amountAfterTransaction >= invoiceTotal) {
			paymentStatus = Constants.PAYMENT_COMPLETED;
		} else if (amountAfterTransaction > 0) {
			paymentStatus = Constants.PAYMENT_PARTIAL;
		} else {
			paymentStatus = Constants.PAYMENT_APPROVED;
		}
		for (final TFreightShippingInvoice invoice : invoices) {
			invoice.setPaymentApprove(paymentStatus);
			invoice.setInvoiceAmountReceived(amountAfterTransaction);
		}
		freightShippingInvoiceRepository.saveAll(invoices);

	}
}
