package com.nexware.aajapan.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nexware.aajapan.services.TShippingRoroService;

@Controller
@RequestMapping("shipping/roro")
public class ShippingRoroController {

	@Autowired
	private TShippingRoroService shippingRoroService;

	@GetMapping(path = "/confirmed/export/excel")
	@ResponseBody
	public ResponseEntity<HttpStatus> roroConfirmExcelExport(@RequestParam("scheduleId") String scheduleId,
			@RequestParam("destCountry") String destCountry, HttpServletResponse response) throws IOException {
		this.shippingRoroService.roroConfirmedExcelReport(scheduleId, destCountry, response);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping(path = "/arranged/export/excel")
	public void roroArrangedExcel(@RequestParam("forwarderId") String forwarderId,
			@RequestParam("destCountry") String destCountry, @RequestParam("allocationId") String allocationId,
			@RequestParam("status") Integer status,
			@RequestParam(value = "originPort", required = false) String originPort, HttpServletResponse response)
			throws IOException {
		this.shippingRoroService.roroArrangedExcelReport(forwarderId, destCountry, allocationId, status, originPort,
				response);
	}

	@GetMapping(path = "/export/excel")
	@ResponseBody
	public ResponseEntity<HttpStatus> balanceSheetReportConfirmed(@RequestParam("scheduleId") String scheduleId,
			HttpServletResponse response) throws IOException {
		this.shippingRoroService.roroConfirmedExportExcel(scheduleId, response);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
