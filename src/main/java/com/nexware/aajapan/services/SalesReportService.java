package com.nexware.aajapan.services;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;

import com.nexware.aajapan.dto.CustomerTransactionDto;

public interface SalesReportService {
	Workbook exportCustomerTransaction(List<CustomerTransactionDto> list);

	void exportAllSalesOrders(HttpServletResponse response) throws IOException;
}
