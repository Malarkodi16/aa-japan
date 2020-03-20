package com.nexware.aajapan.controllers.master;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nexware.aajapan.models.MCountryPort;
import com.nexware.aajapan.models.YardDetails;
import com.nexware.aajapan.payload.DatatableResponse;
import com.nexware.aajapan.payload.Response;
import com.nexware.aajapan.repositories.MasterCountryPortRepository;

@Controller
@RequestMapping("/master/country/port")
public class MasterCountryPortController {

	@Autowired
	private MasterCountryPortRepository CountryPortRepository;

	@GetMapping("/list")
	public ModelAndView listPort() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("shipping.master.countryPort.list");
		return modelAndView;
	}

	@GetMapping("/list-data")
	@ResponseBody
	public DatatableResponse getListData() {
		return new DatatableResponse(this.CountryPortRepository.findAll());
	}

	@PostMapping("/saveMapped")
	@ResponseBody
	@Transactional
	public ResponseEntity<Response> saveCountryModelMap(@RequestBody List<String> data,
			@RequestParam("country") String country) {
		MCountryPort countryport = this.CountryPortRepository.findOneByCountry(country);
		countryport.setPort(data);
		this.CountryPortRepository.save(countryport);
		return new ResponseEntity<>(new Response("success", this.CountryPortRepository.findOneByCountry(country)),
				HttpStatus.OK);
	}

}
