package com.nexware.aajapan.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.DocumentStatusCountDto;
import com.nexware.aajapan.dto.DocumentStatusDto;
import com.nexware.aajapan.payload.DatatableResponse;
import com.nexware.aajapan.payload.Response;
import com.nexware.aajapan.repositories.TShippingRequestRepository;

@Controller
@RequestMapping("shipping/bl")
public class DocumentStatusController {

	@Autowired
	TShippingRequestRepository tShippingRequestRepository;

	@GetMapping("/document-draft")
	public ModelAndView draftView() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("shipping.document.draft");
		return modelAndView;
	}

	@GetMapping("/document-original")
	public ModelAndView originalview() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("shipping.document.original");
		return modelAndView;
	}

	@GetMapping("/customers.json")
	@ResponseBody
	public List<DocumentStatusDto> findAllCountries() {
		return this.tShippingRequestRepository.findAllBlCustomers();
	}

	@GetMapping("/draft-list")
	@ResponseBody
	public DatatableResponse getDraftList() {

		return new DatatableResponse(this.tShippingRequestRepository.findAllLesserEtd());
	}

	@GetMapping("/original-list")
	@ResponseBody
	public DatatableResponse getOriginalList() {

		return new DatatableResponse(this.tShippingRequestRepository.findAllLesserEta());
	}

	@PutMapping("/update-draft")
	public ResponseEntity<Response> updateDraftStatus(@RequestBody Map<String, Object> data) {
		Integer status = Integer.parseInt(data.get("status").toString());
		@SuppressWarnings("unchecked")
		List<String> ids = (List<String>) data.get("ids");
		if (status == 0) {
			this.tShippingRequestRepository.updateDraftStatus(Constants.BL_DRAFT_STATUS_NOT_RECEIVED, ids);
		} else {
			this.tShippingRequestRepository.updateDraftStatus(Constants.BL_DRAFT_STATUS_RECEIVED, ids);
		}
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PutMapping("/update-original")
	public ResponseEntity<Response> updateOriginalStatus(@RequestBody Map<String, Object> data) {
		Integer status = Integer.parseInt(data.get("status").toString());
		@SuppressWarnings("unchecked")
		List<String> ids = (List<String>) data.get("ids");
		if (status == 0) {
			this.tShippingRequestRepository.updateOriginalStatus(Constants.BL_ORIGINAL_STATUS_NOT_RECEIVED, ids);
		} else {
			this.tShippingRequestRepository.updateOriginalStatus(Constants.BL_ORIGINAL_STATUS_RECEIVED, ids);
		}
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@GetMapping("/document-dashboard/status-count")
	public ResponseEntity<Response> getDocumentDraftDashboardCount() {
		DocumentStatusCountDto documentStatusCountDto = new DocumentStatusCountDto();
		documentStatusCountDto.setDraftBL(this.tShippingRequestRepository.findDraftBlCount());
		documentStatusCountDto.setOriginalBL(this.tShippingRequestRepository.findOriginalBlCount());
		return new ResponseEntity<>(new Response("success", documentStatusCountDto), HttpStatus.OK);
	}

}
