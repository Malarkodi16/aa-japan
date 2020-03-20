package com.nexware.aajapan.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.nexware.aajapan.payload.Response;

@Controller
public class MyErrorController implements ErrorController {
	private static final Logger log = LogManager.getLogger(MyErrorController.class);

	@RequestMapping("/error")
	public String handleError(HttpServletRequest request, HttpServletResponse response) {
//		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
//		Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");

//		log.error(exception.getMessage());
		if (isAjax(request)) {
			return "redirect:/error/handle/ajax/request";
		} else {
			return "redirect:/error/handle/http/request";
		}

	}

	@Override
	public String getErrorPath() {
		return "/error";
	}

	private boolean isAjax(HttpServletRequest request) {
		String requestedWithHeader = request.getHeader("X-Requested-With");
		return "XMLHttpRequest".equals(requestedWithHeader);
	}

	@GetMapping(value = "/error/handle/ajax/request")
	public String handleAjaxRequest(HttpServletRequest request, HttpServletResponse response) {

		if (response.getStatus() == HttpStatus.UNAUTHORIZED.value()) {
			return "redirect:/error/data/401";
		} else {
			return "redirect:/error/data/500";
		}
	}

	@GetMapping(value = { "/error/handle/http/request" })
	public String handleHttpRequest(HttpServletRequest request, HttpServletResponse response) {

		if (response.getStatus() == HttpStatus.UNAUTHORIZED.value()) {
			return "redirect:/error/access-denied";
		} else {
			return "redirect:/error/500";
		}
	}

	@GetMapping(value = "/error/access-denied")
	public ModelAndView accessDenied(ModelAndView modelAndView) {

		modelAndView.setViewName("access-denied");
		return modelAndView;
	}

	@GetMapping(value = { "/error/500" })
	public ModelAndView errorInfo(ModelAndView modelAndView) {
		modelAndView.setViewName("error");
		return modelAndView;
	}

	@GetMapping(value = { "/error/data/401" })
	public ResponseEntity<Response> handle401(HttpServletRequest request) {
		return new ResponseEntity<Response>(HttpStatus.UNAUTHORIZED);
	}

	@GetMapping(value = { "/error/data/500" })
	public ResponseEntity<Response> handle500(HttpServletRequest request) {
		return new ResponseEntity<Response>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
}