package com.nexware.aajapan.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("invoice")
public class InvoiceManagementController {

	@GetMapping(value = { "/dashboard" })
	public ModelAndView invoiceEntry(ModelAndView modelAndView) {

		modelAndView.setViewName("sales.invoice.dashboard");
		return modelAndView;
	}

	@GetMapping(value = { "/porforma-invoice-management" })
	public ModelAndView porformaInvoiceMangement() {

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("porforma-invoice-management");
		return modelAndView;
	}

	@GetMapping(value = { "/sales-invoice-management" })
	public ModelAndView salesInvoiceMangement() {

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("sales-invoice-management");
		return modelAndView;
	}

}
