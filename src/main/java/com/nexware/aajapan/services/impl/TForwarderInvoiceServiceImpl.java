package com.nexware.aajapan.services.impl;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexware.aajapan.core.AccountTransactionConstants;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.TForwarderInvoiceCsvDto;
import com.nexware.aajapan.exceptions.AAJRuntimeException;
import com.nexware.aajapan.models.MBank;
import com.nexware.aajapan.models.TAccountsTransaction;
import com.nexware.aajapan.models.TFwdrInvoice;
import com.nexware.aajapan.models.TInvoicePaymentTransaction;
import com.nexware.aajapan.repositories.InvoicePaymentTransactionRepository;
import com.nexware.aajapan.repositories.MasterBankRepository;
import com.nexware.aajapan.repositories.TFwdrInvoiceRepository;
import com.nexware.aajapan.services.InvoicePaymentTransactionService;
import com.nexware.aajapan.services.TAccountsTransactionService;
import com.nexware.aajapan.services.TForwarderInvoiceService;
import com.nexware.aajapan.utils.AppUtil;

@Service
@Transactional
public class TForwarderInvoiceServiceImpl implements TForwarderInvoiceService {

	@Autowired
	private TAccountsTransactionService accountsTransactionService;
	@Autowired
	private TFwdrInvoiceRepository tFwdrInvoiceRepository;
	@Autowired
	private InvoicePaymentTransactionRepository invoicePaymentTransactionRepository;
	@Autowired
	private TFwdrInvoiceRepository fwdrInvoiceRepository;
	@Autowired
	private MasterBankRepository masterBankRepository;
	@Autowired
	private InvoicePaymentTransactionService invoicePaymentTransactionService;

	@Override
	public void forwarderInvoicePaymentApproveTransactions(List<TFwdrInvoice> forwarderInvoices) {
		this.tFwdrInvoiceRepository.saveAll(forwarderInvoices);
	}

	@Override
	public void completePayment(TFwdrInvoice invoice) {
		final List<TInvoicePaymentTransaction> transactions = invoicePaymentTransactionRepository
				.findAllByInvoiceNoAndStatus(invoice.getInvoiceNo(),
						Constants.INVOICE_PAYMENT_TRANSACTION_NOT_CANCELLED);
		for (TInvoicePaymentTransaction transaction : transactions) {
			final MBank bank = masterBankRepository.findOneByBankSeq(transaction.getBankId());
			this.accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(invoice.getInvoiceNo(),
					invoice.getForwarderName(), bank.getCoaCode(), Constants.CURRENCY_YEN, Constants.TRANSACTION_CREDIT,
					(invoice.getCurrency() != 1) ? (transaction.getAmount() * invoice.getExchangeRateValue())
							: transaction.getAmount(),
					AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_STORAGE_PHOTOS, transaction.getCode(),
					invoice.getInvoiceDate()));

			// create account transaction entry - Photos - Debit
			this.accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(invoice.getInvoiceNo(),
					invoice.getForwarderName(), AccountTransactionConstants.STORAGE_PHOTOS_FREIGHT_SHIPPING_PAYABLE,
					Constants.CURRENCY_YEN, Constants.TRANSACTION_DEBIT,
					(invoice.getCurrency() != 1) ? (transaction.getAmount() * invoice.getExchangeRateValue())
							: transaction.getAmount(),
					AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_STORAGE_PHOTOS, transaction.getCode(),
					invoice.getInvoiceDate()));

		}

	}

	public Boolean isEntryValid(List<TFwdrInvoice> invoices, String paymentFor) {
		boolean isPaymentValid = false;
		for (TFwdrInvoice invoice : invoices) {
			if (paymentFor.equals("STORAGE")) {
				isPaymentValid = AppUtil.isObjectEmpty(invoice.getAmount());
			} else if (paymentFor.equals("PHOTO")) {
				isPaymentValid = AppUtil.isObjectEmpty(invoice.getPhotoCharges());
			} else if (paymentFor.equals("BL AMENDCOMBINE")) {
				isPaymentValid = AppUtil.isObjectEmpty(invoice.getBlAmendCombineCharges());
			} else if (paymentFor.equals("RADIATION")) {
				isPaymentValid = AppUtil.isObjectEmpty(invoice.getRadiationCharges());
			} else if (paymentFor.equals("INSPECTION")) {
				isPaymentValid = AppUtil.isObjectEmpty(invoice.getInspectionCharges());
			} else if (paymentFor.equals("YARD HANDLING CHARGES")) {
				isPaymentValid = AppUtil.isObjectEmpty(invoice.getYardHandlingCharges());
			}
			if (!isPaymentValid) {
				return isPaymentValid;
			}
		}
		return isPaymentValid;
	}

	@Override
	public void saveCsvData(String invoiceNo, HttpServletRequest request, TForwarderInvoiceCsvDto csvData,
			String stockNo) {
		String refNo = request.getParameter("refNo");
		String invoiceDateString = request.getParameter("invoiceDate");
		String dueDateString = request.getParameter("dueDate");
		String remitter = request.getParameter("remitter");
		Integer currency = Integer.valueOf(request.getParameter("currency"));
		String paymentFor = request.getParameter("paymentFor");
		String taxCheckString = request.getParameter("taxCheck");
		Integer taxCheck = Integer.valueOf(taxCheckString);
		TFwdrInvoice tFwdrInvoice = new TFwdrInvoice();
		try {
			tFwdrInvoice.setInvoiceDate(new SimpleDateFormat("dd-MM-yyyy").parse(invoiceDateString));
			tFwdrInvoice.setDueDate(new SimpleDateFormat("dd-MM-yyyy").parse(dueDateString));
		} catch (Exception e) {
			throw new AAJRuntimeException("Date exception" + e);
		}
		tFwdrInvoice.setRefNo(refNo);
		tFwdrInvoice.setStockNo(stockNo);
		tFwdrInvoice.setRemitter(remitter);
		tFwdrInvoice.setCurrency(currency);
		if (paymentFor.equals("STORAGE")) {
			tFwdrInvoice.setAmount(csvData.getAmount());
			if (taxCheck.equals(1)) {
				tFwdrInvoice.setStorageTax(csvData.getAmount() * Constants.COMMON_TAX_PECENTAGE);
			}
		} else if (paymentFor.equals("SHIPPING")) {
			tFwdrInvoice.setShippingCharges(csvData.getAmount());
			if (taxCheck.equals(1)) {
				tFwdrInvoice.setShippingTax(csvData.getAmount() * Constants.COMMON_TAX_PECENTAGE);
			}
		} else if (paymentFor.equals("PHOTO")) {
			tFwdrInvoice.setPhotoCharges(csvData.getAmount());
			if (taxCheck.equals(1)) {
				tFwdrInvoice.setPhotoTax(csvData.getAmount() * Constants.COMMON_TAX_PECENTAGE);
			}
		} else if (paymentFor.equals("BL AMENDCOMBINE")) {
			tFwdrInvoice.setBlAmendCombineCharges(csvData.getAmount());
			if (taxCheck.equals(1)) {
				tFwdrInvoice.setBlAmendCombineTax(csvData.getAmount() * Constants.COMMON_TAX_PECENTAGE);
			}
		} else if (paymentFor.equals("RADIATION")) {
			tFwdrInvoice.setRadiationCharges(csvData.getAmount());
			if (taxCheck.equals(1)) {
				tFwdrInvoice.setRadiationTax(csvData.getAmount() * Constants.COMMON_TAX_PECENTAGE);
			}
		} else if (paymentFor.equals("REPAIR")) {
			tFwdrInvoice.setRepairCharges(csvData.getAmount());
			if (taxCheck.equals(1)) {
				tFwdrInvoice.setRepairTax(csvData.getAmount() * Constants.COMMON_TAX_PECENTAGE);
			}
		} else if (paymentFor.equals("YARD HANDLING CHARGES")) {
			tFwdrInvoice.setYardHandlingCharges(csvData.getAmount());
			if (taxCheck.equals(1)) {
				tFwdrInvoice.setYardHandlingTax(csvData.getAmount() * Constants.COMMON_TAX_PECENTAGE);
			}
		} else if (paymentFor.equals("INSPECTION")) {
			tFwdrInvoice.setInspectionCharges(csvData.getAmount());
			if (taxCheck.equals(1)) {
				tFwdrInvoice.setInspectionTax(csvData.getAmount() * Constants.COMMON_TAX_PECENTAGE);
			}
		} else if (paymentFor.equals("TRANSPORT")) {
			tFwdrInvoice.setTransportCharges(csvData.getAmount());
			if (taxCheck.equals(1)) {
				tFwdrInvoice.setTransportTax(csvData.getAmount() * Constants.COMMON_TAX_PECENTAGE);
			}
		} else if (paymentFor.equals("FREIGHT")) {
			tFwdrInvoice.setFreightCharges(csvData.getAmount());
			if (taxCheck.equals(1)) {
				tFwdrInvoice.setFreightTax(csvData.getAmount() * Constants.COMMON_TAX_PECENTAGE);
			}
		}
		tFwdrInvoice.setRemarks(csvData.getRemarks());
		tFwdrInvoice.setInvoiceNo(invoiceNo);
		tFwdrInvoice.setInvoiceUpload(Constants.INVOICE_NOT_UPLOADED);
		this.fwdrInvoiceRepository.save(tFwdrInvoice);
	}

	@Override
	public void revertPaymentTransaction(String invoiceNo, Double amount) {
		final List<TFwdrInvoice> invoices = tFwdrInvoiceRepository.findAllByInvoiceNo(invoiceNo);

		// calculate invoice total
		final Double invoiceTotal = invoices.stream().mapToDouble(TFwdrInvoice::getSumOfCharges).sum();

		// get total paid amount
		final Double totalPaidAmount = invoicePaymentTransactionService
				.getTotalTransactionAmount(Constants.INVOICE_TYPE_FORWARDER, invoiceNo);

		final Double amountAfterTransaction = totalPaidAmount - amount;
		Integer paymentStatus;
		if (amountAfterTransaction >= invoiceTotal) {
			paymentStatus = Constants.PAYMENT_COMPLETED;
		} else if (amountAfterTransaction > 0) {
			paymentStatus = Constants.PAYMENT_PARTIAL;
		} else {
			paymentStatus = Constants.PAYMENT_APPROVED;
		}
		for (final TFwdrInvoice invoice : invoices) {
			invoice.setPaymentApprove(paymentStatus);
			invoice.setInvoiceAmountReceived(amountAfterTransaction);
		}
		tFwdrInvoiceRepository.saveAll(invoices);

	}

}
