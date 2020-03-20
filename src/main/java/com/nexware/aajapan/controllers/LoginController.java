package com.nexware.aajapan.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

	@GetMapping(value = { "/", "/login" })
	public ModelAndView login(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
		String error = request.getParameter("error");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		modelAndView.addObject("error", error == null ? "" : error);
		modelAndView.setViewName("login");
		return modelAndView;
	}

	@GetMapping(value = "/registration")
	public ModelAndView registration() {
		ModelAndView modelAndView = new ModelAndView();

		modelAndView.setViewName("registration");
		return modelAndView;
	}

}
