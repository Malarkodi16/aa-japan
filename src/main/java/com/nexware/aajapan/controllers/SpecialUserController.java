package com.nexware.aajapan.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nexware.aajapan.payload.Response;
import com.nexware.aajapan.repositories.StockRepository;

@Controller
@RequestMapping("specialuser")
public class SpecialUserController {

	@Autowired
	private StockRepository stockRepository;

	@GetMapping("/details/data/{stockNo}")
	public ResponseEntity<Response> getStockDetails(@PathVariable("stockNo") String stockNo) {

		return new ResponseEntity<>(new Response("success", this.stockRepository.findOneByStockNo(stockNo)),
				HttpStatus.OK);
	}

}
