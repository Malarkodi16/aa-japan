package com.nexware.aajapan.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nexware.aajapan.dto.CustomerTransactionDto;
import com.nexware.aajapan.dto.CustomerTransactionInMonthDto;
import com.nexware.aajapan.dto.CustomerTransactionItemsDto;
import com.nexware.aajapan.dto.CustomerTransactionReportDto;
import com.nexware.aajapan.payload.DatatableResponse;
import com.nexware.aajapan.payload.Response;
import com.nexware.aajapan.repositories.TCustomerTransactionRepository;
import com.nexware.aajapan.repositories.TSalesInvoiceRepository;
import com.nexware.aajapan.services.TCustomerTransactionService;
import com.nexware.aajapan.utils.AppUtil;

@Controller

@RequestMapping("sales/")
public class CustomerTransactionController {

	@Autowired
	private TSalesInvoiceRepository tSalesInvoiceRepository;
	@Autowired
	private TCustomerTransactionRepository customerTransactionRepository;
	@Autowired
	private TCustomerTransactionService customerTransactionService;

	@GetMapping("/customer-transaction")
	public ModelAndView draftView() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("customer.tansaction");
		return modelAndView;
	}

	@GetMapping("customertransaction/search")
	@ResponseBody
	public DatatableResponse customerTransactionDatasource(@RequestParam("customerId") String customerId,
			@RequestParam("min_date") @DateTimeFormat(pattern = "dd-MM-yyyy") Date minDate,
			@RequestParam("max_date") @DateTimeFormat(pattern = "dd-MM-yyyy") Date maxDate) {
		List<CustomerTransactionDto> list = this.tSalesInvoiceRepository.findAllCustomerTransactions(customerId,
				minDate, maxDate);
		List<CustomerTransactionDto> finalList = new ArrayList<CustomerTransactionDto>();

		if (!list.isEmpty()) {
			String custId = list.stream().findFirst().get().getCustomerId();
			String currencySymbol = list.stream().findFirst().get().getCurrencySymbol();
			Integer currency = list.stream().findFirst().get().getCurrency();
			Date startDate = AppUtil.isObjectEmpty(minDate) ? list.stream().findFirst().get().getDate() : minDate;
			Date lastDate = AppUtil.isObjectEmpty(maxDate) ? new Date() : maxDate;
			// list.get(list.size() - 1).getDate(); to find last record date
			List<Date> dateArray = AppUtil.getArrayOfMonthsBetweenDates(startDate, lastDate);
			dateArray.stream().forEach(date -> {
				CustomerTransactionDto obj = new CustomerTransactionDto();
				obj.setCustomerId(custId);
				obj.setDate(date);
				obj.setCurrencySymbol(currencySymbol);
				obj.setCurrency(currency);
				finalList.add(obj);
			});
		}

		return new DatatableResponse(!finalList.isEmpty() ? finalList : new ArrayList<>());
	}

	@GetMapping("customertransaction/search/onExpand")
	@ResponseBody
	public DatatableResponse customerTransactionDatasourceOnExpand(@RequestParam("customerId") String customerId,
			@RequestParam("date") @DateTimeFormat(pattern = "MMMM yyyy") Date date) {
		List<CustomerTransactionItemsDto> list = this.tSalesInvoiceRepository
				.findAllCustomerTransactionsOnExpand(customerId, date);
		return new DatatableResponse(!list.isEmpty() ? list : new ArrayList<>());

	}

	@GetMapping("customertransaction/broughtForward")
	public ResponseEntity<Response> broughtForward(@RequestParam("customerId") String customerId,
			@RequestParam("date") @DateTimeFormat(pattern = "MMMM yyyy") Date date) {
		Double allPaidAmount = this.customerTransactionRepository.getBroughtForwardAmountForCustomer(customerId, date);
		Double totalReservedPrice = this.tSalesInvoiceRepository.getPreviousMonthReservedPrice(customerId, date);
		final Double broughtForward = totalReservedPrice + allPaidAmount;

		return new ResponseEntity<>(new Response("success", broughtForward), HttpStatus.OK);
	}

	@GetMapping("customertransaction/export/excel/new")
	public void exportExcelNew(@RequestParam("customerId") String customerId,
			@RequestParam(value = "min_date", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date minDate,
			@RequestParam(value = "max_date", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date maxDate,
			HttpServletResponse response) throws IOException {
		this.customerTransactionService.exportAllCustomerTransactions(customerId, minDate, maxDate, response);
	}

	@GetMapping("customertransaction/export/excel")
	public ResponseEntity<Response> exportExcel(@RequestParam("customerId") Optional<String> customerId,
			@RequestParam("min_date") @DateTimeFormat(pattern = "dd-MM-yyyy") Optional<Date> minDate,
			@RequestParam("max_date") @DateTimeFormat(pattern = "dd-MM-yyyy") Optional<Date> maxDate,
			HttpServletResponse response) {
		List<CustomerTransactionReportDto> list = this.tSalesInvoiceRepository.findAllCustomerTransactionsReport(
				customerId.isPresent() ? customerId.get() : "", minDate.isPresent() ? minDate.get() : null,
				maxDate.isPresent() ? maxDate.get() : null);
		return new ResponseEntity<>(new Response("success", list), HttpStatus.OK);
	}

	@GetMapping("customertransaction/paymentAmount")
	public ResponseEntity<Response> paymentAmount(@RequestParam("customerId") String customerId,
			@RequestParam("date") @DateTimeFormat(pattern = "MMMM yyyy") Date date) {
		Double paidAmount = this.customerTransactionRepository.getpaymentAmountForCustomer(customerId, date);
		return new ResponseEntity<>(new Response("success", paidAmount), HttpStatus.OK);
	}

	@GetMapping("customertransaction/onMonth")
	public ResponseEntity<Response> onMonth(@RequestParam("customerId") String customerId,
			@RequestParam("date") @DateTimeFormat(pattern = "MMMM yyyy") Date date) {
		List<CustomerTransactionInMonthDto> transactionList = this.customerTransactionRepository
				.getMonthlyTransaction(customerId, date);
		return new ResponseEntity<>(new Response("success", transactionList), HttpStatus.OK);
	}
}
