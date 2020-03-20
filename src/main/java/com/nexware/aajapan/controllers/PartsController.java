package com.nexware.aajapan.controllers;

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
import org.springframework.web.servlet.ModelAndView;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.models.TPartsPurchase;
import com.nexware.aajapan.payload.Response;
import com.nexware.aajapan.repositories.TPartsPurchaseRepository;
import com.nexware.aajapan.services.SequenceService;

@Controller
@RequestMapping("/parts/")
public class PartsController {

	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private TPartsPurchaseRepository partsPurchaseRepository;

	@GetMapping("create/page")
	public ModelAndView createPartsPurchasePage() {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.pp.create");
		return modelAndView;
	}

	@GetMapping("view/page")
	public ModelAndView viewPartsPurchasePage() {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.pp.list");
		return modelAndView;
	}

	@Transactional
	@PostMapping("/save-purchased-parts")
	public ResponseEntity<Response> savePurchasedParts(@RequestBody List<TPartsPurchase> purchasedParts) {
		final String code = sequenceService.getNextSequence(Constants.SEQUENCE_KEY_PARTS_PURCHASED);

		purchasedParts.stream().forEach(invoice -> {
			invoice.setCode(code);
			if (invoice.isTaxInclusive()) {
				invoice.setTaxPercentage(Constants.COMMON_TAX);
			} else {
				invoice.setTaxPercentage(0.0);
			}
		});

		partsPurchaseRepository.saveAll(purchasedParts);

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

}
