package com.nexware.aajapan.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.models.TBankTransaction;
import com.nexware.aajapan.models.TInvoicePaymentTransaction;
import com.nexware.aajapan.repositories.InvoicePaymentTransactionRepository;
import com.nexware.aajapan.repositories.TBankTransactionRepository;
import com.nexware.aajapan.repositories.TPurchaseInvoiceRepository;
import com.nexware.aajapan.services.BankTransactionService;
import com.nexware.aajapan.services.InvoicePaymentTransactionService;
import com.nexware.aajapan.services.SequenceService;
import com.nexware.aajapan.services.TForwarderInvoiceService;
import com.nexware.aajapan.services.TFreightInvoiceService;
import com.nexware.aajapan.services.TInspectionInvoiceService;
import com.nexware.aajapan.services.TInvoiceService;
import com.nexware.aajapan.services.TPurchaseInvoiceService;
import com.nexware.aajapan.services.TTransportInvoiceService;
import com.nexware.aajapan.utils.AppUtil;

@Service
@Transactional
public class InvoicePaymentTransactionServiceImpl implements InvoicePaymentTransactionService {

	@Autowired
	private InvoicePaymentTransactionRepository invoicePaymentTransactionRepository;
	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private TPurchaseInvoiceRepository purchaseInvoiceRepository;
	@Autowired
	private BankTransactionService bankTransactionService;
	@Autowired
	private TBankTransactionRepository bankTransactionRepository;
	@Autowired
	private TPurchaseInvoiceService purchaseInvoiceService;
	@Autowired
	private TInvoiceService invoiceService;
	@Autowired
	private TTransportInvoiceService transportInvoiceService;
	@Autowired
	private TForwarderInvoiceService forwarderInvoiceService;
	@Autowired
	private TFreightInvoiceService freightInvoiceService;
	@Autowired
	private TInspectionInvoiceService inspectionInvoiceService;

	@Override
	public void saveInvoicePaymentTransaction(TInvoicePaymentTransaction invoicePaymentTransaction) {
		invoicePaymentTransaction
				.setCode(this.sequenceService.getNextSequence(Constants.SEQUENCE_KEY_PAYMENT_TRANSACTION_NO));
		this.invoicePaymentTransactionRepository.insert(invoicePaymentTransaction);
	}

	@Override
	public Double getTotalTransactionAmountForAuction(String invoiceNo) {
		return this.invoicePaymentTransactionRepository.findTotalByInvoiceNoAndStatus(invoiceNo);

	}

	@Override
	public Double getTotalTransactionAmount(String invoiceType, String invoiceNo) {
		return this.invoicePaymentTransactionRepository.findTotalByInvoiceTypeAndInvoiceNoAndStatus(invoiceType,
				invoiceNo);

	}

	@Override
	public Double getTotalTransactionAmountUsd(String invoiceType, String invoiceNo) {
		return this.invoicePaymentTransactionRepository.findTotalUsdByInvoiceTypeAndInvoiceNoAndStatus(invoiceType,
				invoiceNo);
	}

	@Override
	public void deleteTransaction(String paymentVoucherNo) {
		List<TInvoicePaymentTransaction> paymentTransactions = invoicePaymentTransactionRepository
				.findAllByPaymentVoucherNo(paymentVoucherNo);
		for (TInvoicePaymentTransaction paymentTransaction : paymentTransactions) {
			String invoiceNo = paymentTransaction.getInvoiceNo();
			Double amount = paymentTransaction.getAmount();
			if (paymentTransaction.getInvoiceType().equals(Constants.INVOICE_TYPE_PURCHASE)) {
				purchaseInvoiceService.revertPaymentTransaction(invoiceNo, amount);
			} else if (paymentTransaction.getInvoiceType().equals(Constants.INVOICE_TYPE_OTHERS)) {
				invoiceService.revertPaymentTransaction(invoiceNo, amount);
			} else if (paymentTransaction.getInvoiceType().equals(Constants.INVOICE_TYPE_TRANSPORT)) {
				transportInvoiceService.revertPaymentTransaction(invoiceNo, amount);
			} else if (paymentTransaction.getInvoiceType().equals(Constants.INVOICE_TYPE_FORWARDER)) {
				forwarderInvoiceService.revertPaymentTransaction(invoiceNo, amount);
			} else if (paymentTransaction.getInvoiceType().equals(Constants.INVOICE_TYPE_INSPECTION)) {
				inspectionInvoiceService.revertPaymentTransaction(invoiceNo, amount);
			} else {
				return;
			}
			paymentTransaction.setStatus(Constants.INVOICE_PAYMENT_TRANSACTION_CANCELLED);
			invoicePaymentTransactionRepository.save(paymentTransaction);
		}
		// bank transaction
		TBankTransaction bankTransaction = bankTransactionRepository.findOneByPaymentVoucherNo(paymentVoucherNo);
		if (!AppUtil.isObjectEmpty(bankTransaction)) {
			bankTransactionService.bankTransactionEntry(bankTransaction.getBankId(), bankTransaction.getAmount(),
					Constants.CURRENCY_YEN, Constants.TRANSACTION_DEBIT, "payment cancelled", paymentVoucherNo);
		}

	}

}
