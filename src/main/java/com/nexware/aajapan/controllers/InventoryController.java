package com.nexware.aajapan.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nexware.aajapan.payload.DatatableResponse;
import com.nexware.aajapan.repositories.InventoryRepository;

@Controller

@RequestMapping("inventory")

public class InventoryController {
	@Autowired
	private InventoryRepository inventoryRepository;

	@GetMapping("/inventory")
	public ModelAndView inventory(ModelAndView modelAndView) {

		modelAndView.setViewName("accounts.inventory");
		return modelAndView;

	}

	@GetMapping("/inventorylist-data")
	@ResponseBody
	public DatatableResponse getInventoryData() {

		return new DatatableResponse(this.inventoryRepository.findAllInventoryStockDetails());
	}
}
