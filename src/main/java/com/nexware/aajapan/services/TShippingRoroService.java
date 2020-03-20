package com.nexware.aajapan.services;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

public interface TShippingRoroService {

	void roroConfirmedExcelReport(String scheduleId, String destCountry, HttpServletResponse response)
			throws IOException;

	void roroArrangedExcelReport(String forwarder, String destCountry, String allocationId, Integer status,
			String originPort, HttpServletResponse response) throws IOException;

	void roroConfirmedExportExcel(String scheduleId, HttpServletResponse response) throws IOException;

}
