package com.nexware.aajapan.controllers.master;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.models.MShip;
import com.nexware.aajapan.models.MShippingCompany;
import com.nexware.aajapan.payload.DatatableResponse;
import com.nexware.aajapan.payload.Response;
import com.nexware.aajapan.repositories.MShippingCompanyRepository;
import com.nexware.aajapan.repositories.MasterShipRepository;
import com.nexware.aajapan.services.SequenceService;
import com.nexware.aajapan.utils.AppUtil;

@Controller
@RequestMapping("/master/ship")
public class MasterShipController {

	@Autowired
	private MasterShipRepository shipRepository;

	@Autowired
	private SequenceService sequenceService;
	
	@GetMapping("/list")
	public ModelAndView listShipPage() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("shipping.master.ship.list");
		return modelAndView;
	}
	
	@GetMapping("/list-data")

	@ResponseBody
	public DatatableResponse getListData() {
		return new DatatableResponse(this.shipRepository.getAllUnDeletedShip());
	}
	
	@PostMapping("/save")
	@ResponseBody
	@Transactional
	public ResponseEntity<Response> create(@RequestBody MShip ship) {
		boolean isNewEntry = (ship.getShipId() == null || ship.getShipId().isEmpty());
		MShip shipToSave = null;
		if (isNewEntry) {
			shipToSave = ship;
			shipToSave.setShipId(sequenceService.getNextSequence(Constants.SEQUENCE_KEY_MSHIP));
			shipToSave.setDeleteFlag(Constants.DELETE_FLAG_0);
		} else {
			shipToSave = this.shipRepository.findOneByShipId(ship.getShipId());
			shipToSave.setName(ship.getName());
		}

		this.shipRepository.save(shipToSave);
		return new ResponseEntity<>(new Response("success", shipToSave),
				HttpStatus.OK);
	}

	
	@GetMapping("/delete/{shipId}")
	public ResponseEntity<Response> deleteship(@PathVariable("shipId") String shipId, ModelAndView modelAndView,
			RedirectAttributes redirectAttributes) {
		MShip ship = this.shipRepository.findOneByShipId(shipId);
		ship.setDeleteFlag(Constants.DELETE_FLAG_1);
		this.shipRepository.save(ship);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}
	
	
	
	@GetMapping("/validShip")
	public ResponseEntity<Boolean> shipExists(@RequestParam(value = "shipId", required = false) String shipId,
			@RequestParam("name") String name) {
		boolean isValid = false;

		if (AppUtil.isObjectEmpty(shipId)) {
			isValid = shipRepository.existsByName(name);
		} else {
			isValid = shipRepository.existsByShipIdAndName(shipId, name);
			if (!isValid) {
				isValid = shipRepository.existsByName(name);
			} else {
				isValid = false;
			}
		}
		return new ResponseEntity<>(isValid, HttpStatus.OK);
	}


}
