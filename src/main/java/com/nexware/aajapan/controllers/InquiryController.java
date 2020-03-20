package com.nexware.aajapan.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexware.aajapan.dto.MLoginDto;
import com.nexware.aajapan.models.InquiryVehicle;
import com.nexware.aajapan.models.TInquiry;
import com.nexware.aajapan.payload.DatatableResponse;
import com.nexware.aajapan.repositories.InquiryRepository;
import com.nexware.aajapan.repositories.TCustomerRepository;
import com.nexware.aajapan.services.MLoginService;
import com.nexware.aajapan.services.SecurityService;

@Controller

@RequestMapping("inquiry")

public class InquiryController {

	@Autowired
	ObjectMapper mapper;
	@Autowired
	TCustomerRepository customerRepository;
	@Autowired
	private SecurityService securityService;
	@Autowired
	private InquiryRepository inquiryRepository;
	@Autowired
	private MLoginService loginService;

	@GetMapping("/create")
	public ModelAndView create(Model model, ModelAndView modelAndView) {
		TInquiry inquiry = new TInquiry();
		List<InquiryVehicle> inquiries = new ArrayList<>();
		inquiries.add(new InquiryVehicle());
		inquiry.setInquiryVehicles(inquiries);

		modelAndView.addObject("inquiryForm", inquiry);
		modelAndView.setViewName("sales.inquiry");
		return modelAndView;

	}

	@PostMapping("/save")
	public ModelAndView create(@ModelAttribute("inquiryForm") TInquiry inquiry, ModelAndView modelAndView,
			@RequestParam String action, RedirectAttributes redirectAttributes) {
		MLoginDto loginDto = this.securityService.findLoggedInUser();
		inquiry.setSalesPerson(loginDto.getUserId());
		this.inquiryRepository.save(inquiry);
		redirectAttributes.addFlashAttribute("message", "Inquiry created successfully.");
		if (action.equalsIgnoreCase("save")) {
			modelAndView.setViewName("redirect:/inquiry/listview");

		} else {
			modelAndView.setViewName("redirect:/inquiry/create");
		}
		return modelAndView;
	}

	@GetMapping("/listview")
	public ModelAndView viewinquiryList(ModelAndView modelAndView) {
		modelAndView.setViewName("sales.viewinquirydetails");
		return modelAndView;

	}

	@GetMapping("/inquirylist-data")
	@ResponseBody
	public DatatableResponse getInquirylistData(String salesPersonId) {
		// get sales person id and sales person under logged in sales person
		List<String> salesPersonIds = this.loginService.getSalesPersonIdsByHierarchyLevel();
		return new DatatableResponse(this.inquiryRepository.findAllInquiriesBySalesPersonIn(salesPersonIds));
	}

	@PostMapping("/saveagain")
	public ModelAndView editsave(@ModelAttribute("inquiryForms") TInquiry inquiry, ModelAndView modelAndView,
			RedirectAttributes redirectAttributes) {
		this.inquiryRepository.save(inquiry);
		redirectAttributes.addFlashAttribute("message", "Inquiry created successfully.");
		modelAndView.setViewName("redirect:/inquiry/create");
		return modelAndView;
	}

}
