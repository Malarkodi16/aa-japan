package com.nexware.aajapan.services;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

public interface AccountReportService {
	public void exportBalanceSheetReport(Date date, HttpServletResponse response) throws IOException;

	public void exportGenaralExpensesReport(HttpServletResponse response) throws IOException;

	public void exportProfitAndLossReport(Integer flag, Date fromDate, Date toDate, HttpServletResponse response);

	public void exportGlReport(Date fromDate, Date toDate, HttpServletResponse response);

	public void exportPaymentTrackingExcel(Integer type, String supplier, Date invoiceDateFrom, Date invoiceDateTo,
			HttpServletResponse response);
	
	public void exportPaymentTrackingFreightANdShippingExcel(Integer type, String supplier, Date invoiceDateFrom, Date invoiceDateTo,
			HttpServletResponse response);
}
