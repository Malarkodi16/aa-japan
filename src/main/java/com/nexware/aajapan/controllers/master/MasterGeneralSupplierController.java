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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.models.MGeneralSupplier;
import com.nexware.aajapan.payload.DatatableResponse;
import com.nexware.aajapan.payload.Response;
import com.nexware.aajapan.repositories.MGeneralSupplierRepository;
import com.nexware.aajapan.services.SequenceService;

@Controller
@RequestMapping("/accounts/master/generalSupplier")
public class MasterGeneralSupplierController {

	/*@Autowired
	private SequenceService sequenceService;
	@Autowired
	private MGeneralSupplierRepository masterGeneralSupplierRepository;

	@PostMapping(value = "create")
	@Transactional
	public ResponseEntity<Response> create(@RequestBody final MGeneralSupplier data) {
		final String code = sequenceService.getNextSequence(Constants.SEQUENCE_KEY_GENERAL_SUPPLIER);
		masterGeneralSupplierRepository.save(new MGeneralSupplier(code, data.getName(), Constants.DELETE_FLAG_0));
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
*/
	
	@Autowired
	private MGeneralSupplierRepository masterGeneralSupplierRepository;

	@Autowired
	private SequenceService sequenceService;

	@GetMapping("/list")
	public ModelAndView listGeneralSupplier() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.master.generalSupplier.list");
		return modelAndView;
	}

	@GetMapping("/list-data")

	@ResponseBody
	public DatatableResponse getListData() {
		return new DatatableResponse(this.masterGeneralSupplierRepository.getAllUnDeletedGeneralSupplier());
	}

	@PostMapping("create")

	@ResponseBody

	@Transactional
	public ResponseEntity<Response> create(@RequestBody MGeneralSupplier generalSupplier) {
		boolean isNewEntry = (generalSupplier.getCode() == null || generalSupplier.getCode().isEmpty());
		MGeneralSupplier generalSupplierToSave = null;
		if (isNewEntry) {
			generalSupplierToSave = generalSupplier;
			generalSupplierToSave.setDeleteFlag(Constants.DELETE_FLAG_0);
			generalSupplierToSave.setCode(sequenceService.getNextSequence(Constants.SEQUENCE_KEY_GENERAL_SUPPLIER));
		} else {
			generalSupplierToSave = this.masterGeneralSupplierRepository.findOneByCode(generalSupplier.getCode());
			generalSupplierToSave.setName(generalSupplier.getName());
		}
		this.masterGeneralSupplierRepository.save(generalSupplierToSave);
		return new ResponseEntity<>(
				new Response("success", this.masterGeneralSupplierRepository.findOneByCode(generalSupplierToSave.getCode())),
				HttpStatus.OK);
	}

	@GetMapping("/delete/{code}")
	public ResponseEntity<Response> deleteSupplier(@PathVariable("code") String code, ModelAndView modelAndView,
			RedirectAttributes redirectAttributes) {
		MGeneralSupplier generalSupplier = this.masterGeneralSupplierRepository.findOneByCode(code);
		generalSupplier.setDeleteFlag(Constants.DELETE_FLAG_1);
		this.masterGeneralSupplierRepository.save(generalSupplier);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

}
