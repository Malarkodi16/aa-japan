package com.nexware.aajapan.services;

import java.util.List;

import org.bson.Document;

import com.nexware.aajapan.models.TSalesInvoice;

public interface SalesOrderService {

	void salesOrderTransaction(List<TSalesInvoice> salesInvoiceList);

	void cancelSalesOrderTransaction(List<TSalesInvoice> salesInvoiceList);

	void editSalesOrderTransaction(List<TSalesInvoice> editedSalesList);

	Double getActualPrice(String stockNo);

	void createSalesOrder(List<TSalesInvoice> salesInvoice);

	void cancelSalesOrder(String invoiceNo);

	void fifoAllocationByCustomerAndDaybook(String advanceAmount, String exchangeRate1, String exchangeRate2,
			String customerId, String daybookId);

	void fifoAllocationByCustomerAndDaybookReAllocate(String customerId, String daybookId, String transId);

	void unitAllocationByCustomerAndDaybook(String advanceAmount, String exchangeRate1, String exchangeRate2,
			String customerId, String daybookId, List<Document> allocation);

	void lcAllocationByCustomerAndDaybook(String advanceAmount, String customerId, String daybookId,
			List<Document> allocation);

	void unitAllocationByCustomerAndDaybookReAllocate(String customerId, String daybookId, List<Document> allocation,
			String transId);

	void depositOrAdvanceByCustomerAndDaybook(String advanceAmount, String exchangeRate1, String exchangeRate2,
			String customerId, String daybookId, Integer allocationType);

	void updateReceivedAmount(String invoiceNo, String stockNo, Double amount, Integer transactionType);

//	void updateAllocatedAmount(String invoiceNo, String stockNo, Double amount, Integer transactionType);

	void allocattedAmountForInvoice(String invoiceNo, String stockNo, Double amount, Integer transactionType);

	void allocattedAmountForLcInvoice(String id, Double amount, Integer transactionType);

	Integer findSalesInvoiceStatus(TSalesInvoice invoice);

	void checkAndUpdateReceivedAmount(List<TSalesInvoice> invoice);

	Double getBalanceAmountReceivedForStockByCustomer(String invoiceNo, String stockNo, String customerId);
}
