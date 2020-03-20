package com.nexware.aajapan.controllers.master;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.models.InspectionCompanyLocation;
import com.nexware.aajapan.models.MInspectionCompany;
import com.nexware.aajapan.models.MLocation;
import com.nexware.aajapan.models.MSupplier;
import com.nexware.aajapan.models.MTransporter;
import com.nexware.aajapan.models.MVechicleMaker;
import com.nexware.aajapan.models.SupplierLocation;
import com.nexware.aajapan.payload.DatatableResponse;
import com.nexware.aajapan.payload.Response;
import com.nexware.aajapan.repositories.MInspectionCompanyRepository;
import com.nexware.aajapan.services.SequenceService;
import com.nexware.aajapan.utils.AppUtil;

@Controller
@RequestMapping("/master/inspection")
public class InspectionCompanyMaster {
	@Autowired
	private MInspectionCompanyRepository inspectionRepo;
	@Autowired
	private SequenceService sequenceService;

	@GetMapping("/list")
	public ModelAndView listTransporterPage() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("shipping.master.inspection.company.list");
		return modelAndView;
	}

	@GetMapping("/list-data")
	@ResponseBody
	public DatatableResponse getListData() {
		return new DatatableResponse(this.inspectionRepo.getAllUnDeletedInspectionCompany());
	}

	@GetMapping("/inspectionCompany")
	public ModelAndView createInspectionCompany(HttpServletRequest request) {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("shipping.master.inspection.company");

		return modelAndView;
	}

	@GetMapping("/delete/{code}")
	public ResponseEntity<Response> deleteInspectionCompany(@PathVariable("code") String code,
			ModelAndView modelAndView, RedirectAttributes redirectAttributes) {
		MInspectionCompany mInspectionCompany = this.inspectionRepo.findOneByCode(code);
		mInspectionCompany.setDeleteFlag(Constants.DELETE_FLAG_1);
		this.inspectionRepo.save(mInspectionCompany);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PostMapping("/create")
	@ResponseBody
	@Transactional
	public ResponseEntity<Response> saveInspectionCompany(@RequestBody MInspectionCompany mInspectionCompany) {
		if (AppUtil.isObjectEmpty(mInspectionCompany.getCode())) {
			mInspectionCompany.setDeleteFlag(Constants.DELETE_FLAG_0);
			mInspectionCompany.setCode(sequenceService.getNextSequence(Constants.SEQUENCE_KEY_INSPECTION_COMPANY));
			mInspectionCompany.getLocations().forEach(location -> {
				location.setLocationId(sequenceService.getNextSequence(Constants.SEQUENCE_KEY_LOCATION));
				location.setDeleteStatus(Constants.DELETE_FLAG_0);
			});

			this.inspectionRepo.insert(mInspectionCompany);
		} else {
			MInspectionCompany updateInspectionCompany = this.inspectionRepo.findOneByCode(mInspectionCompany.getCode());
			updateInspectionCompany.setName(mInspectionCompany.getName());
			mInspectionCompany.getLocations().forEach(location -> {
				if(AppUtil.isObjectEmpty(location.getLocationId())){
					location.setDeleteStatus(Constants.DELETE_FLAG_0);
					location.setLocationId(sequenceService.getNextSequence(Constants.SEQUENCE_KEY_LOCATION));
				}
			});
			updateInspectionCompany.setLocations(mInspectionCompany.getLocations());
			this.inspectionRepo.save(updateInspectionCompany);
		}

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@GetMapping("/isCompanyExists")
	public ResponseEntity<Boolean> isCompanyExists(@RequestParam(value = "id", required = false) String code,
			String company) {
		boolean isValid;

		if (code.isEmpty()) {
			isValid = inspectionRepo.existsByName(company);
		} else {
			isValid = inspectionRepo.existsByCodeAndName(code, company);
			if (!isValid) {
				isValid = inspectionRepo.existsByName(company);
			} else {
				isValid = false;
			}
		}
		return new ResponseEntity<>(isValid, HttpStatus.OK);
	}

	@GetMapping("/location/delete")
	@Transactional
	public ResponseEntity<HttpStatus> deleteSupplierLocation(@RequestParam("locationId") String locationId,
			@RequestParam("code") String code) {
		MInspectionCompany updateInspectionCompany = this.inspectionRepo.findOneByCode(code);
		Optional<InspectionCompanyLocation> inspectionCompanyLocation = updateInspectionCompany.getLocations().stream()
				.filter(loc -> loc.getLocationId().equalsIgnoreCase(locationId)).findFirst();
		if (inspectionCompanyLocation.isPresent()) {
			inspectionCompanyLocation.get().setDeleteStatus(Constants.DELETE_FLAG_1);
		}
		this.inspectionRepo.save(updateInspectionCompany);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("/company/edit/{code}")
	public ModelAndView supplierCreate(@PathVariable("code") String code, ModelAndView modelAndView,
			RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("code", code);
		modelAndView.setViewName("redirect:/master/inspection/inspectionCompany");
		return modelAndView;
	}

	@GetMapping("/company/info/{code}.json")
	public ResponseEntity<Response> activeCompanyInfo(@PathVariable("code") String code) {
		return new ResponseEntity<Response>(new Response("success", this.inspectionRepo.getActiveInspectionCompany(code)),
				HttpStatus.OK);
	}
}
