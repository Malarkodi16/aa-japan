package com.nexware.aajapan.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nexware.aajapan.models.TForwardBooking;
import com.nexware.aajapan.payload.DatatableResponse;
import com.nexware.aajapan.payload.Response;
import com.nexware.aajapan.repositories.TForwardBookingRepository;

@Controller
@RequestMapping("/accounts/forward/booking")
public class AccountsForwardBookingController {
	@Autowired
	private TForwardBookingRepository forwardBookingRepository;

	@GetMapping("/view")
	public ModelAndView viewAccountOthers() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.forward.booking");
		return modelAndView;
	}

	@PostMapping("/save")
	public ResponseEntity<Response> createForwardBooking(@RequestBody TForwardBooking booking) {
		this.forwardBookingRepository.save(booking);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@GetMapping("/list/data-source")
	@ResponseBody
	public DatatableResponse fetchForwardBookingList() {
		return new DatatableResponse(this.forwardBookingRepository.fetchForwardBookingList());
	}

}
