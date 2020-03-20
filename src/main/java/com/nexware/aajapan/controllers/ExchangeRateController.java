package com.nexware.aajapan.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexware.aajapan.models.TExchangeRate;
import com.nexware.aajapan.payload.DatatableResponse;
import com.nexware.aajapan.repositories.TExchageRateRepository;

@Controller
@RequestMapping("exchange")
public class ExchangeRateController {
	@Autowired
	private TExchageRateRepository exchageRate;
	@Autowired
	ObjectMapper objectMapper;
	@Autowired
	private TExchageRateRepository exchageRateRepository;

	@GetMapping(value = { "/exchangeRate" })
	public ModelAndView exchange(ModelAndView modelAndView, TExchangeRate tExchangeRate) {

		modelAndView.setViewName("accounts.exchange");
		return modelAndView;

	}

	@GetMapping("/findByDate")
	@ResponseBody
	public DatatableResponse getByDate(ModelAndView modelAndView, @RequestParam("date") String date) {
		if (date.isEmpty()) {
			return new DatatableResponse(new ArrayList<>());
		}
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");

		return new DatatableResponse(this.exchageRate.findAllByDate(LocalDate.parse(date, dtf)));

	}

	@GetMapping("/list-page")
	public ModelAndView viewExchangeRates() {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.exchange.list");
		return modelAndView;
	}

	@GetMapping("/list-page/data-source")
	@ResponseBody
	public DatatableResponse getBLList() {

		return new DatatableResponse(this.exchageRateRepository.getAllList());
	}

}
