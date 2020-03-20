package com.nexware.aajapan.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.models.MLocation;
import com.nexware.aajapan.models.TYearOfManufacture;
import com.nexware.aajapan.payload.DatatableResponse;
import com.nexware.aajapan.payload.Response;
import com.nexware.aajapan.repositories.YearOfManufactureRepository;
import com.nexware.aajapan.services.YearOfManufactureService;
import com.nexware.aajapan.utils.AppUtil;

@Controller
@RequestMapping("/documents/year-of-manufacture")
public class DocumentsYearOfManufactureController {

	@Autowired
	private YearOfManufactureService yearOfManufactureService;

	@Autowired
	private YearOfManufactureRepository yearOfManufactureRepository;

	@GetMapping("/view")
	public ModelAndView manufactureListView() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("document.year.of.manufacture.list");
		return modelAndView;
	}

	@GetMapping("/list")
	@ResponseBody
	public DatatableResponse getManufactureList() {

		return new DatatableResponse(this.yearOfManufactureService.getListWithoutDelete());
	}

	@GetMapping("/create")
	public ModelAndView manufactureCreate() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("document.year.of.manufacture.create");
		return modelAndView;
	}

	@PostMapping("/save")
	public ResponseEntity<Response> saveYearOfManufacture(@RequestBody TYearOfManufacture tYearOfManufacture) {
		if (!AppUtil.isObjectEmpty(tYearOfManufacture.getFormatedSerialNoFrom())) {
			tYearOfManufacture.setSerialNoFrom(Long.parseLong(tYearOfManufacture.getFormatedSerialNoFrom()));
		}
		if (!AppUtil.isObjectEmpty(tYearOfManufacture.getFormatedSerialNoTo())) {
			tYearOfManufacture.setSerialNoTo(Long.parseLong(tYearOfManufacture.getFormatedSerialNoTo()));
		}

		this.yearOfManufactureService.saveManufactureYear(tYearOfManufacture);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}
	
	@GetMapping("/delete/{id}")
	public ModelAndView deleteYearOfManufacture(@PathVariable("id") String id, ModelAndView modelAndView,
			RedirectAttributes redirectAttributes) {
		TYearOfManufacture yearOfManufacture = this.yearOfManufactureRepository.findOneById(id);
		yearOfManufacture.setDeleteFlag(Constants.DELETE_FLAG_1);
		this.yearOfManufactureRepository.save(yearOfManufacture);
		redirectAttributes.addFlashAttribute("message", "deleted Successfully");
		modelAndView.setViewName("redirect:/documents/year-of-manufacture/list");
		return modelAndView;
	}
	
	@GetMapping("/edit/{code}")
	public ModelAndView ManufactureYearCreate(@PathVariable("code") String code, ModelAndView modelAndView,
			RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("code", code);
		modelAndView.setViewName("redirect:/documents/year-of-manufacture/create");
		return modelAndView;
	}
	
	
	
	@GetMapping("manufactureDate/infoActive/{code}.json")
	public ResponseEntity<Response> yearOfManufactureInfo(@PathVariable("code") String code) {
		return new ResponseEntity<>(
				new Response("success", this.yearOfManufactureRepository.findOneByCode(code)), HttpStatus.OK);
	}
}
