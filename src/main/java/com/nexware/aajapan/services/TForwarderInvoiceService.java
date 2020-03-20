package com.nexware.aajapan.services;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.nexware.aajapan.dto.TForwarderInvoiceCsvDto;
import com.nexware.aajapan.models.TFwdrInvoice;

public interface TForwarderInvoiceService {

	void forwarderInvoicePaymentApproveTransactions(List<TFwdrInvoice> forwarderInvoices);

	void completePayment(TFwdrInvoice invoice);

	Boolean isEntryValid(List<TFwdrInvoice> invoices, String paymentFor);

	void saveCsvData(String invoiceNo, HttpServletRequest request, TForwarderInvoiceCsvDto csvData, String stockNo);

	void revertPaymentTransaction(String invoiceNo, Double amount);
}
