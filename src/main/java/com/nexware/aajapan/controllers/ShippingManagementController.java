package com.nexware.aajapan.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.models.TShippingRequest;
import com.nexware.aajapan.payload.Response;
import com.nexware.aajapan.repositories.TShippingRequestRepository;
import com.nexware.aajapan.services.ShippingRequestService;

@Controller
@RequestMapping("shipping/management")
public class ShippingManagementController {
	@Autowired
	private TShippingRequestRepository shippingRequestRepository;
	@Autowired
	private ShippingRequestService shippingRequestService;

	@GetMapping("/available")
	public ModelAndView availableForShipping(ModelAndView modelAndView) {
		modelAndView.setViewName("shipping.shipping-management.available");
		return modelAndView;
	}
	//

	@GetMapping("/requested")
	public ModelAndView requestedForShipping(ModelAndView modelAndView) {
		modelAndView.setViewName("shipping.shipping-management.requested");
		return modelAndView;
	}

	@GetMapping("/container")
	public ModelAndView containerShipping(ModelAndView modelAndView) {
		modelAndView.setViewName("shipping.shipping-management.container");
		return modelAndView;
	}

	@GetMapping("/roro")
	public ModelAndView roroShipping(ModelAndView modelAndView) {
		modelAndView.setViewName("shipping.shipping-management.roro");
		return modelAndView;
	}

	@PutMapping("/roro/update/blNo")
	@Transactional
	public ResponseEntity<Response> updateBLNo(@RequestBody List<Document> data) {
		data.forEach(d -> {
			final TShippingRequest shippingRequest = shippingRequestRepository
					.findOneByShipmentRequestId(d.getString("shipmentRequestId"));
			shippingRequest.setBlNo(d.getString("blNo"));
			shippingRequest.setStatus(Constants.SHIPIING_REQUEST_RORO_UPDATED_BL_AND_SHIPPED);
			shippingRequestRepository.save(shippingRequest);

		});
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);

	}

	@GetMapping(path = "/container/request/excel")
	public void balanceSheetReport(@RequestParam("allocationId") String allocationId, HttpServletResponse response)
			throws IOException {
		shippingRequestService.exportContainerRequestExcel(allocationId, response);

	}
}
