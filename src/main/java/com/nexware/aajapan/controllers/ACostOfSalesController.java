package com.nexware.aajapan.controllers;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nexware.aajapan.payload.DatatableResponse;
import com.nexware.aajapan.repositories.AccountsTransactionRepository;
import com.nexware.aajapan.repositories.TSalesInvoiceRepository;
import com.nexware.aajapan.utils.AppUtil;

@Controller
@RequestMapping("/costofsales")
public class ACostOfSalesController {

	@Autowired
	private TSalesInvoiceRepository tsalesInvoiceRepository;

	@Autowired
	private AccountsTransactionRepository accountsTransactionRepository;

	@GetMapping("/viewcostofsales")
	public ModelAndView viewCostOfSales() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.viewcostofsales");
		return modelAndView;
	}

	@GetMapping(path = "/sales-converted-list")
	@ResponseBody
	public DatatableResponse listDataSource() {
		return new DatatableResponse(this.tsalesInvoiceRepository.getListWithStockDetails());
	}

	@GetMapping("/details/data")
	@ResponseBody
	public DatatableResponse getStockDetails(
			@RequestParam(value = "fromDate", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date fromDate,
			@RequestParam(value = "toDate", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date toDate,
			String code) {
		Long codeValue = Long.parseLong(code);

		return new DatatableResponse(this.accountsTransactionRepository.findAllByCode(codeValue,
				AppUtil.ifNull(fromDate, AppUtil.addMonths(-1)), AppUtil.ifNull(toDate, new Date())));

	}
}
