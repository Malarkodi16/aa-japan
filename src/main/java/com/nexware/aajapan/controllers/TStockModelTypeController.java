package com.nexware.aajapan.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nexware.aajapan.models.TStockModelType;
import com.nexware.aajapan.payload.Response;
import com.nexware.aajapan.repositories.TStockModelTypeRepository;

@Controller
@RequestMapping("stockModelType")
public class TStockModelTypeController {

	@Autowired
	private TStockModelTypeRepository stockModelTypeRepository;

	@GetMapping("/details")
	public ResponseEntity<Response> getPrice(@RequestParam("modelType") String modelType) {
		final String convertedString = modelType.toUpperCase();
		final TStockModelType response = stockModelTypeRepository.findOneByModelType(convertedString);
		return new ResponseEntity<>(new Response("success", response), HttpStatus.OK);
	}

}
