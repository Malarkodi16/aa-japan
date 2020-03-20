package com.nexware.aajapan.services;

import java.util.Date;
import java.util.List;

import com.nexware.aajapan.models.TFreightShippingInvoice;

public interface TFreightInvoiceService {
	void approveRoroFreightInvoicePayment(final List<TFreightShippingInvoice> invoiceNos, String bank,
			Date approvedDate, String remarks, Double amount, Integer paymentCurrency);

	void approveRoroInvoicePayment(final List<TFreightShippingInvoice> invoiceNos, String bank, Date approvedDate,
			String remarks, Double amount);

	void approveOneRoroInvoicePayment(final TFreightShippingInvoice invoice, String bank, Date approvedDate,
			String remarks, Double amount);

	void freightInvoicePaymentApproveTransactions(List<TFreightShippingInvoice> tFreightShippingInvoices);

	void freightInvoicePaymentCompleteTransactions(TFreightShippingInvoice invoice);

	void completePayment(List<TFreightShippingInvoice> invoices, String bank, Double amount, String remarks,
			Date approvedDate);

	void completePaymentFreightInvoice(List<TFreightShippingInvoice> invoices, String bank, Double amount,
			String remarks, Date approvedDate, Double exchangeRate, Integer currency);

	void revertPaymentTransaction(String invoiceNo, Double amount);
}
