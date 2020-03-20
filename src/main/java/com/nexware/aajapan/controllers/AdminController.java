package com.nexware.aajapan.controllers;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.models.MLocation;
import com.nexware.aajapan.models.MSupplier;
import com.nexware.aajapan.models.SupplierLocation;
import com.nexware.aajapan.payload.DatatableResponse;
import com.nexware.aajapan.payload.Response;
import com.nexware.aajapan.repositories.LocationRepository;
import com.nexware.aajapan.repositories.MasterSupplierRepository;
import com.nexware.aajapan.services.MSupplierService;
import com.nexware.aajapan.services.SequenceService;
import com.nexware.aajapan.utils.AppUtil;

@Controller
@RequestMapping("a")
public class AdminController {
	@Autowired
	private MasterSupplierRepository supplierRepository;
	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private LocationRepository locationRepository;
	@Autowired
	private MSupplierService supplierService;

	@GetMapping("/dashboard")
	public ModelAndView dashboard(ModelAndView modelAndView) {
		modelAndView.setViewName("admin.dashboard");
		return modelAndView;
	}

	@GetMapping("/supplier/create")
	public ModelAndView supplierCreate(HttpServletRequest request, ModelAndView modelAndView) {
		modelAndView.setViewName("admin.supplier.create");
		return modelAndView;
	}

	@GetMapping("/supplier/edit/{supplierCode}")
	public ModelAndView supplierCreate(@PathVariable("supplierCode") String supplierCode, ModelAndView modelAndView,
			RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("supplierCode", supplierCode);
		modelAndView.setViewName("redirect:/a/supplier/create");
		return modelAndView;
	}

	@GetMapping("/supplier/info/{supplierCode}.json")
	public ResponseEntity<Response> supplierInfo(@PathVariable("supplierCode") String supplierCode) {
		return new ResponseEntity<>(
				new Response("success", this.supplierRepository.findOneBySupplierCode(supplierCode)), HttpStatus.OK);
	}

	@GetMapping("/supplier/infoActive/{supplierCode}.json")
	public ResponseEntity<Response> activeSupplierInfo(@PathVariable("supplierCode") String supplierCode) {
		return new ResponseEntity<>(
				new Response("success", this.supplierRepository.findOneActiveSupplier(supplierCode)), HttpStatus.OK);
	}

	@PostMapping(path = "/supplier/create")
	@Transactional
	public ModelAndView supplierCreate(@RequestParam("supplierCode") String supplierCode, MSupplier supplier,
			ModelAndView modelAndView) {
		supplier.getSupplierLocations().removeIf(location -> AppUtil.isObjectEmpty(location.getAuctionHouse()));
		if (AppUtil.isObjectEmpty(supplier.getSupplierCode())) {
			supplierService.createSupplier(supplier);
		} else {
			supplierService.editSupplier(supplierCode, supplier);
		}
		modelAndView.setViewName("redirect:/a/supplier/list");
		return modelAndView;

	}

	@GetMapping("/supplier/list")
	public ModelAndView supplierList(ModelAndView modelAndView) {
		modelAndView.setViewName("admin.supplier-list");
		return modelAndView;
	}

	@GetMapping("/supplier/list/datasource")
	@ResponseBody
	public DatatableResponse getSupplierList() {
		return new DatatableResponse(this.supplierRepository.getListWithoutDeletedSuppliers());
	}

	@GetMapping("/supplier/location/delete")
	@Transactional
	public ResponseEntity<HttpStatus> deleteSupplierLocation(@RequestParam("itemId") String itemId,
			@RequestParam("supplierCode") String supplierCode) {
		MSupplier updateSupplier = this.supplierRepository.findOneBySupplierCode(supplierCode);
		Optional<SupplierLocation> supplierLocation = updateSupplier.getSupplierLocations().stream()
				.filter(loc -> loc.getId().equalsIgnoreCase(itemId)).findFirst();
		if (supplierLocation.isPresent()) {
			supplierLocation.get().setDeleteStatus(Constants.DELETE_FLAG_1);
		}
		MLocation location = this.locationRepository.findOneByAuctionHouseId(new ObjectId(itemId));
		location.setDeleteFlag(Constants.DELETE_FLAG_1);
		this.locationRepository.save(location);
		this.supplierRepository.save(updateSupplier);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("/supplier/delete/{supplierCode}")
	@Transactional
	public ModelAndView deleteSupplier(@PathVariable("supplierCode") String supplierCode, ModelAndView modelAndView,
			RedirectAttributes redirectAttributes) {
		MSupplier updateSupplier = this.supplierRepository.findOneBySupplierCode(supplierCode);
		updateSupplier.setSupplierCode(supplierCode);
		updateSupplier.setType(updateSupplier.getType());
		updateSupplier.setCompany(updateSupplier.getCompany());
		updateSupplier.setMaxCreditAmount(updateSupplier.getMaxCreditAmount());
		updateSupplier.setMaxDueDays(updateSupplier.getMaxDueDays());
		updateSupplier.setDeleteStatus(Constants.MLOCATION_DELETE_STATUS_AFTER);
		updateSupplier.setSupplierLocations(updateSupplier.getSupplierLocations());
		this.supplierRepository.save(updateSupplier);
		this.supplierRepository.updateBySupplierCode(supplierCode, Constants.DELETE_FLAG_1);

		redirectAttributes.addFlashAttribute("status", HttpStatus.OK);
		modelAndView.setViewName("redirect:/a/supplier/list");
		return modelAndView;
	}
}
