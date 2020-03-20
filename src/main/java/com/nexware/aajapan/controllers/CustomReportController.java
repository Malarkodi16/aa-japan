package com.nexware.aajapan.controllers;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.CustomReportDto;
import com.nexware.aajapan.dto.CustomReportResponseDto;
import com.nexware.aajapan.dto.MLoginDto;
import com.nexware.aajapan.models.MCustomListFields;
import com.nexware.aajapan.models.TCustomReport;
import com.nexware.aajapan.payload.DatatableResponse;
import com.nexware.aajapan.payload.Response;
import com.nexware.aajapan.repositories.MCustomListFieldsRepository;
import com.nexware.aajapan.repositories.TCustomReportRepository;
import com.nexware.aajapan.services.SecurityService;
import com.nexware.aajapan.services.SequenceService;
import com.nexware.aajapan.utils.AppUtil;

@Controller

@RequestMapping("report")
public class CustomReportController {

	@Autowired
	TCustomReportRepository customReportRepository;
	@Autowired
	MCustomListFieldsRepository customListFieldsRepository;
	@Autowired
	private SecurityService securityService;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private SequenceService sequenceService;

	@PostMapping(path = "/save")
	public ResponseEntity<Response> transporterFeeSave(@RequestBody TCustomReport report,
			RedirectAttributes redirectAttributes, ModelAndView modelAndView) {
		final MLoginDto loggedInUser = securityService.findLoggedInUser();
		TCustomReport customReport;
		if (!AppUtil.isObjectEmpty(report.getCode())) {
			customReport = customReportRepository.findOneByCode(report.getCode());
		} else {
			customReport = new TCustomReport();
			customReport.setCode(sequenceService.getNextSequence(Constants.SEQUENCE_KEY_RPRT));
			customReport.setCreateBy(loggedInUser.getUserId());
		}
		customReport.setName(report.getName());
		customReport.setFields(report.getFields());
		customReport.setPeriod(report.getPeriod());
		customReport.setFrom(report.getFrom());
		customReport.setTo(report.getTo());
		customReportRepository.save(customReport);
		return new ResponseEntity<>(new Response("success", findAllReports()), HttpStatus.OK);
	}

	@GetMapping("/dynamicReport/data-source")
	public ResponseEntity<CustomReportResponseDto> dynamicReport(
			@RequestParam(value = "customLists", required = false) String customLists) throws JsonProcessingException {
		CustomReportResponseDto response = new CustomReportResponseDto();
		if (AppUtil.isObjectEmpty(customLists)) {
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		TCustomReport report = customReportRepository.findOneByCode(customLists);
		List<MCustomListFields> customField = customListFieldsRepository.findByFieldIdIn(report.getFields());
		List<Integer> fields = report.getFields();
		List<CustomReportDto> data = customReportRepository.findByCustomField(customField, report.getPeriod(),
				report.getFrom(), report.getTo());
		// sort by selected order
		Collections.sort(customField, Comparator.comparing(item -> fields.indexOf(item.getFieldId())));
		response.setColumnMetaData(customField);
		response.setData(new DatatableResponse(data));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/list")
	public ResponseEntity<List<TCustomReport>> findAllReports() {
		final MLoginDto loggedInUser = securityService.findLoggedInUser();
		return new ResponseEntity<List<TCustomReport>>(
				customReportRepository.findAllByCreateBy(loggedInUser.getUserId()), HttpStatus.OK);
	}

	@GetMapping("/find")
	public ResponseEntity<TCustomReport> findOneReportsByCode(@RequestParam("code") String code) {

		return new ResponseEntity<TCustomReport>(customReportRepository.findOneByCode(code), HttpStatus.OK);
	}
}
