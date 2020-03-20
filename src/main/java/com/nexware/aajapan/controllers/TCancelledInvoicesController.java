package com.nexware.aajapan.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nexware.aajapan.payload.DatatableResponse;
import com.nexware.aajapan.repositories.MHSCodeRepository;
import com.nexware.aajapan.repositories.TCancelledInvoicesRepository;

@Controller
@RequestMapping("/accounts/cancelledInvoices")
public class TCancelledInvoicesController {
	//
	@Autowired
	private TCancelledInvoicesRepository  cancelledInvoicesRepository ;
	
	@GetMapping("/list")
	public ModelAndView listCancelledInvoicesPage() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.cancelledInvoices.list");
		return modelAndView;
	}

	@GetMapping("/list-data")
	@ResponseBody
	public DatatableResponse getListData() {
		return new DatatableResponse(this.cancelledInvoicesRepository.findAllData());
	}
}
