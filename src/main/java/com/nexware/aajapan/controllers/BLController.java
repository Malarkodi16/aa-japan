package com.nexware.aajapan.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.payload.DatatableResponse;
import com.nexware.aajapan.payload.Response;
import com.nexware.aajapan.repositories.StockRepository;
import com.nexware.aajapan.services.TBlTransactionService;

@Controller
@RequestMapping("accountsBL")
public class BLController {

	@Autowired
	private StockRepository stockRepository;
	@Autowired
	private TBlTransactionService blTransactionService;

	@GetMapping(value = { "/page" })
	public ModelAndView viewBl() {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.bl");
		return modelAndView;
	}

	@GetMapping("/bl/list")
	@ResponseBody
	public DatatableResponse getBLList() {

		return new DatatableResponse(this.stockRepository.getBLList());
	}

	@PostMapping(path = "/update-single/blAmend")
	@ResponseBody
	public ResponseEntity<Response> updateSingleBlAmend(@RequestBody Map<String, Object> data) {

		String shippingInstructionId = data.get("shippingInstructionId").toString();
		String customerId = data.get("customerId").toString();
		String consigneeId = data.get("consigneeId").toString();

		// save bl transaction value
		blTransactionService.updateSingleBlTransaction(shippingInstructionId, customerId, consigneeId);

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PostMapping(path = "/update-multiple/blAmend")
	@ResponseBody
	public ResponseEntity<Response> updateMultipleBlAmend(@RequestBody List<Map<String, Object>> data) {

		// save bl transaction value
		blTransactionService.updateMultipleBlTransaction(data);

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PostMapping(path = "/update-single/blNo")
	@ResponseBody
	public ResponseEntity<Response> updateSingleBlNo(@RequestBody Map<String, Object> data) {

		String blNo = data.get("blNo").toString();
		String shipmentRequestId = data.get("shipmentRequestId").toString();

		// save bl transaction value
		blTransactionService.updateSingleBlTransactionNo(blNo, shipmentRequestId);

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PostMapping(path = "/update-multiple/blNo")
	@ResponseBody
	public ResponseEntity<Response> updateMultipleBlNo(@RequestBody List<Map<String, Object>> data) {

		// save bl transaction value
		blTransactionService.updateMultipleBlTransactionNo(data);

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@GetMapping("/list/transaction/data-source")
	@ResponseBody
	public DatatableResponse getBLListTransaction(@RequestParam String shippingInstructionId) {

		return new DatatableResponse(blTransactionService.getBLListTransaction(shippingInstructionId));
	}

	@PostMapping(path = "/update-rec-sur/status")
	@ResponseBody
	public ResponseEntity<Response> updateReceiveOrSurrenderStatus(@RequestBody Map<String, Object> data) {

		String shipmentRequestId = data.get("shipmentRequestId").toString();
		Integer recSurStatus = Integer.parseInt(data.get("recSurStatus").toString());

		// save receive or surrender status
		blTransactionService.updateReceiveOrSurrenderStatus(shipmentRequestId, recSurStatus);

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PostMapping(path = "/update-docStatus/toReceived")
	@ResponseBody
	public ResponseEntity<Response> updateBlDocStatusReceived(@RequestParam String shipmentRequestId) {

		// save receive or surrender status
		blTransactionService.updateBlDocStatus(shipmentRequestId, Constants.SHIPIING_REQUEST_BL_DOC_STATUS_RECEIVE);

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PostMapping(path = "/update-docStatus/toIssued")
	@ResponseBody
	public ResponseEntity<Response> updateBlDocStatusIssued(@RequestParam String shipmentRequestId) {

		// save receive or surrender status
		blTransactionService.updateBlDocStatus(shipmentRequestId, Constants.SHIPIING_REQUEST_BL_DOC_STATUS_ISSUED);

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PostMapping(path = "/update-docStatus/toDispatched")
	@ResponseBody
	public ResponseEntity<Response> updateBlDocStatusDispatched(@RequestParam String shipmentRequestId) {

		// save receive or surrender status
		blTransactionService.updateBlDocStatus(shipmentRequestId, Constants.SHIPIING_REQUEST_BL_DOC_STATUS_DISPATCHED);

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@GetMapping(value = { "/cr-management/page" })
	public ModelAndView viewCrManagement() {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.cr.management");
		return modelAndView;
	}

	@GetMapping("/cr-management/list")
	@ResponseBody
	public DatatableResponse getCrList() {

		return new DatatableResponse(this.blTransactionService.getCrList());
	}

	@PostMapping(path = "/update-single/crAmend")
	@ResponseBody
	public ResponseEntity<Response> updateSingleCrAmend(@RequestBody Map<String, Object> data) {

		String shippingInstructionId = data.get("shippingInstructionId").toString();
		String customerId = data.get("customerId").toString();
		String consigneeId = data.get("consigneeId").toString();

		// save bl transaction value
		blTransactionService.updateSingleCrTransaction(shippingInstructionId, customerId, consigneeId);

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PostMapping(path = "/update-multiple/crAmend")
	@ResponseBody
	public ResponseEntity<Response> updateMultipleCrAmend(@RequestBody List<Map<String, Object>> data) {

		// save bl transaction value
		blTransactionService.updateMultipleCrTransaction(data);

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}
	
	@GetMapping(value = { "/page/sales" })
	public ModelAndView viewSalesBl() {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("sales.bl");
		return modelAndView;
	}
	
	@GetMapping(value = { "/cr-management/page/sales" })
	public ModelAndView viewCrManagementSales() {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("sales.cr.management");
		return modelAndView;
	}

}
