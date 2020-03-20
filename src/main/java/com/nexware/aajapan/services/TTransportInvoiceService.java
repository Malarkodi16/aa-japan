package com.nexware.aajapan.services;

import java.util.List;

import com.nexware.aajapan.models.TTransportInvoice;

public interface TTransportInvoiceService {

	void transportInvoicePaymentApproveTransactions(List<TTransportInvoice> transportInvoices);

	void completePayment(TTransportInvoice invoice);

	void revertPaymentTransaction(String invoiceNo, Double amount);
	
	void revertTransportInvoice(String invoiceRefNo,String cancelledRemarks);
}
