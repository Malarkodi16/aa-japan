package com.nexware.aajapan.controllers.master;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.nexware.aajapan.models.MSpecialExchangeRate;
import com.nexware.aajapan.payload.Response;
import com.nexware.aajapan.repositories.MasterSpecialExchangeRateRepository;
import com.nexware.aajapan.utils.AppUtil;

@Controller
@RequestMapping("/accounts/master/specialExchageRate")
public class MasterSpecialExchageRateController {
	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	private MasterSpecialExchangeRateRepository specialExchangeRateRepository;

	@GetMapping("/list")
	public ModelAndView listSpecialExchangeRate() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.master.specialExchageRate.list");
		return modelAndView;
	}

	@GetMapping("/create")
	public ModelAndView exchangeRateCreate() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.master.specialExchageRate.create");
		return modelAndView;
	}

	@PostMapping("/save")
	public ResponseEntity<Response> saveExchangeRate(@RequestBody MSpecialExchangeRate specialExchangeRate) {

		List<MSpecialExchangeRate> mSpecialExchangeRate = specialExchangeRateRepository.findAll();
		if (!AppUtil.isObjectEmpty(mSpecialExchangeRate)) {
			specialExchangeRateRepository.deleteAll();
		}
		this.specialExchangeRateRepository.save(specialExchangeRate);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

}
