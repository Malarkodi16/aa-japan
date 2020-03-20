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
import com.nexware.aajapan.models.MAuctionGradesExterior;
import com.nexware.aajapan.models.MHSCode;
import com.nexware.aajapan.models.MShippingCompany;
import com.nexware.aajapan.payload.DatatableResponse;
import com.nexware.aajapan.payload.Response;
import com.nexware.aajapan.repositories.MShippingCompanyRepository;
import com.nexware.aajapan.services.SequenceService;
import com.nexware.aajapan.utils.AppUtil;

@Controller
@RequestMapping("/master/shippingCompany")
public class MasterShippingCompanyController {

	@Autowired
	private MShippingCompanyRepository shippingCompanyRepository;

	@Autowired
	private SequenceService sequenceService;

	@GetMapping("/list")
	public ModelAndView listShippingCompanypage() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("shipping.master.shippingCompany.list");
		return modelAndView;
	}

	@GetMapping("/list-data")

	@ResponseBody
	public DatatableResponse getListData() {
		return new DatatableResponse(this.shippingCompanyRepository.getAllUnDeletedShippingCompany());
	}

	@PostMapping("/save")
	@ResponseBody
	@Transactional
	public ResponseEntity<Response> create(@RequestBody MShippingCompany shippingCompany) {
		boolean isNewEntry = (shippingCompany.getShippingCompanyNo() == null
				|| shippingCompany.getShippingCompanyNo().isEmpty());
		MShippingCompany shippingCompanyToSave = null;
		if (isNewEntry) {
			shippingCompanyToSave = shippingCompany;
			shippingCompanyToSave
					.setShippingCompanyNo(sequenceService.getNextSequence(Constants.SEQUENCE_KEY_SHIPPING_COMP));
			shippingCompanyToSave.setDeleteFlag(Constants.DELETE_FLAG_0);
		} else {
			shippingCompanyToSave = this.shippingCompanyRepository
					.findOneByShippingCompanyNo(shippingCompany.getShippingCompanyNo());
			shippingCompanyToSave.setName(shippingCompany.getName());
			shippingCompanyToSave.setShipCompAddr(shippingCompany.getShipCompAddr());
			shippingCompanyToSave.setShipCompMail(shippingCompany.getShipCompMail());
			shippingCompanyToSave.setMobileNo(shippingCompany.getMobileNo());
		}

		this.shippingCompanyRepository.save(shippingCompanyToSave);
		return new ResponseEntity<>(new Response("success", shippingCompanyToSave), HttpStatus.OK);
	}

	@GetMapping("/delete/{shippingCompanyNo}")
	public ResponseEntity<Response> deleteshippingCompany(@PathVariable("shippingCompanyNo") String shippingCompanyNo,
			ModelAndView modelAndView, RedirectAttributes redirectAttributes) {
		MShippingCompany shippingCompany = this.shippingCompanyRepository.findOneByShippingCompanyNo(shippingCompanyNo);
		shippingCompany.setDeleteFlag(Constants.DELETE_FLAG_1);
		this.shippingCompanyRepository.save(shippingCompany);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@GetMapping("/validShippingCompany")
	public ResponseEntity<Boolean> shippingCompanyExists(
			@RequestParam(value = "shippingCompanyNo", required = false) String shippingCompanyNo,
			@RequestParam("name") String name) {
		boolean isValid = false;

		if (AppUtil.isObjectEmpty(shippingCompanyNo)) {
			isValid = shippingCompanyRepository.existsByName(name);
		} else {
			isValid = shippingCompanyRepository.existByNameAndShippingCompanyNo(name, shippingCompanyNo);
			if (!isValid) {
				isValid = shippingCompanyRepository.existsByName(name);
			} else {
				isValid = false;
			}
		}
		return new ResponseEntity<>(isValid, HttpStatus.OK);
	}

}
