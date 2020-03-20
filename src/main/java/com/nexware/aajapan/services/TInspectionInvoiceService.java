package com.nexware.aajapan.services;

public interface TInspectionInvoiceService {
	
	void revertPaymentTransaction(String invoiceNo, Double amount);

}
