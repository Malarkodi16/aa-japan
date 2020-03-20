package com.nexware.aajapan.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("shipping/document")
public class ShippingDocumentController {

	@GetMapping("/dashboard")
	public ModelAndView documentTracking(ModelAndView modelAndView) {
		modelAndView.setViewName("document.dashboard");
		return modelAndView;
	}

	@GetMapping("/not-received")
	public ModelAndView documentTrackingNotReceived(ModelAndView modelAndView) {
		modelAndView.setViewName("shipping.document.notreceived");
		return modelAndView;
	}

	@GetMapping("/received")
	public ModelAndView documentTrackingReceived(ModelAndView modelAndView) {
		modelAndView.setViewName("shipping.document.received");
		return modelAndView;
	}

	@GetMapping("/export-certificates")
	public ModelAndView documentTrackingExportCerficates(ModelAndView modelAndView) {
		modelAndView.setViewName("shipping.document.exportcerficates");
		return modelAndView;
	}

	@GetMapping("/name-transfer")
	public ModelAndView documentTrackingNameTransfer(ModelAndView modelAndView) {
		modelAndView.setViewName("shipping.document.nametransfer");
		return modelAndView;
	}

}
