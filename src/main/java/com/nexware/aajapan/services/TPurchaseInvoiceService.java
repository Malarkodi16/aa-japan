package com.nexware.aajapan.services;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.nexware.aajapan.models.TPurchaseInvoice;

public interface TPurchaseInvoiceService {

	void paymentApproveTransactions(List<TPurchaseInvoice> auctionInvoices);

	Double getBalanceToPayAmount(String invoiceNo, List<Integer> paymentStatus, List<Integer> invoiceStatus);

	void purchaseInvoicePaymentCompleteTransactions(List<TPurchaseInvoice> invoices);

	void completePayment(List<TPurchaseInvoice> invoices, String bank, Double amount, String remarks,
			Date approvedDate);

	void addAuctionCancellationCharge(Map<String, Object> data);

	void carTaxReceived(String id, Double amoun, Date receivedDate, String chassisNo);

	String generateInvoiceNo(String invoiceId);

	void revertPurchaseConfirm(String invoiceNo,String cancelledRemarks);

	void revertPaymentTransaction(String invoiceNo, Double amount);
}
