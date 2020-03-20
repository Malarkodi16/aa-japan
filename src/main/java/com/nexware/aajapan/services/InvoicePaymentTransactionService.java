package com.nexware.aajapan.services;

import com.nexware.aajapan.models.TInvoicePaymentTransaction;

public interface InvoicePaymentTransactionService {

	void saveInvoicePaymentTransaction(TInvoicePaymentTransaction invoicePaymentTransaction);

	void deleteTransaction(String paymentVoucherNo);

	Double getTotalTransactionAmountForAuction(String invoiceNo);

	Double getTotalTransactionAmount(String invoiceType, String invoiceNo);

	Double getTotalTransactionAmountUsd(String invoiceType, String invoiceNo);

}
