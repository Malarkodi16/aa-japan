package com.nexware.aajapan.repositories.custom;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import com.nexware.aajapan.dto.ARAgingSummaryDto;
import com.nexware.aajapan.dto.BranchSalesInvoiceDto;
import com.nexware.aajapan.dto.BranchSalesOrderListDto;
import com.nexware.aajapan.dto.CostofSalesDto;
import com.nexware.aajapan.dto.CustomerTransactionDto;
import com.nexware.aajapan.dto.CustomerTransactionItemsDto;
import com.nexware.aajapan.dto.CustomerTransactionReportDto;
import com.nexware.aajapan.dto.IncomeByCustomerDto;
import com.nexware.aajapan.dto.ReceivableAmountDto;
import com.nexware.aajapan.dto.RecentSalesDto;
import com.nexware.aajapan.dto.SalesSummaryDto;
import com.nexware.aajapan.dto.StockSalesReportDto;
import com.nexware.aajapan.dto.TCustomerAccountsTransactionDto;
import com.nexware.aajapan.dto.TSalesInvoiceDto;
import com.nexware.aajapan.dto.TSalesInvoiceItemDto;
import com.nexware.aajapan.dto.TTUnitAllocationDto;
import com.nexware.aajapan.models.TSalesInvoice;

public interface TSalesInvoiceRepositoryCustom {

	List<TSalesInvoiceItemDto> getListWithStockDetails(List<String> salesPersonIds);
	
	List<TSalesInvoiceDto> getListWithCustomerDetails(List<String> salesPersonIds);

	TSalesInvoiceDto getOneByInvoiceNo(String invoiceNo);

	BranchSalesInvoiceDto getOneByBranchInvoiceNo(String invoiceNo);

	List<CustomerTransactionDto> findAllCustomerTransactions(String customerId, Date minDate, Date maxDate);

	List<CustomerTransactionItemsDto> findAllCustomerTransactionsOnExpand(String customerId, Date date);

	Double getPreviousMonthReservedPrice(String customerId, Date date);

	List<CustomerTransactionReportDto> findAllCustomerTransactionsReport(String customerId, Date minDate, Date maxDate);

	Long getListWithCustomerDetailsCount(List<String> salesPersonIds);

	List<StockSalesReportDto> getStockSalesReport(Date fromDate, Date toDate, String maker, String model);

	List<TCustomerAccountsTransactionDto> findAllCustomerTransactions();

	List<CostofSalesDto> getListWithStockDetails();

	List<IncomeByCustomerDto> getIncomeByCustomerList();

	List<SalesSummaryDto> getSalesSummaryReport();

	List<RecentSalesDto> recentSalesOrder();

	List<ReceivableAmountDto> getReceivableAmountForCustomer();

	List<ARAgingSummaryDto> getAgingSummary() throws ParseException;

	List<TSalesInvoice> fifoSalesInvoice(String custId);

	List<TTUnitAllocationDto> findAllInvoiceByCustomerAndPaymentStatus(String custId);
	
	List<TTUnitAllocationDto> findAllFifoInvoiceByCustomerAndPaymentStatus(String custId);

	List<BranchSalesOrderListDto> branchSalesOrderList();

	List<CustomerTransactionDto> findAllCustomerTransactionsInExcel(String customerId, Date minDate, Date maxDate);
}
