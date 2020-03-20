package com.nexware.aajapan.services;

import java.util.List;

import com.nexware.aajapan.models.TInvoice;

public interface TInvoiceService {

	void othersInvoicePaymentApproveTransactions(List<TInvoice> otherInvoices);

	void completePayment(TInvoice invoice);

	void editGeneralExpenses(List<TInvoice> tInvoice);

	void deleteGeneralExpenses(List<TInvoice> tInvoice);

	void revertPaymentTransaction(String invoiceNo, Double amount);
}
