package com.nexware.aajapan.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.TNotificationDto;
import com.nexware.aajapan.models.TNotification;
import com.nexware.aajapan.payload.DatatableResponse;
import com.nexware.aajapan.payload.Response;
import com.nexware.aajapan.repositories.TNotificationRepository;

@Controller
@RequestMapping("notifications")
public class TNotificationController {
	@Autowired
	private TNotificationRepository tnotificationRepository;

	@GetMapping("/listOnLoad")
	@ResponseBody
	public DatatableResponse notifications() {
		return new DatatableResponse(this.tnotificationRepository.getListOnLoad());
	}

	@GetMapping("/getCounts")
	public ResponseEntity<Response> notificationsGetCount() {
		Map<String, Object> response = new HashMap<>();
		List<TNotificationDto> notificaton = this.tnotificationRepository.getListOnLoad();
		long count = notificaton.stream().filter(n -> n.getStatus() == Constants.TNOTIFICATION_CREATED).count();

		response.put("count", count);
		response.put("notifications", notificaton);

		return new ResponseEntity<>(new Response("success", response), HttpStatus.OK);
	}

	@PutMapping("/update")
	public ResponseEntity<Response> inquiryeditsave(@RequestParam("id") String id) {
		Optional<TNotification> notification = this.tnotificationRepository.findById(id);
		if (notification.isPresent()) {
			notification.get().setStatus(Constants.TNOTIFICATION_OPENED);
			this.tnotificationRepository.save(notification.get());
		}
		return new ResponseEntity<>(new Response("success", this.tnotificationRepository.getById(id)), HttpStatus.OK);
	}

}
