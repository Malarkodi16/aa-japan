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
import com.nexware.aajapan.models.MForwarder;
import com.nexware.aajapan.models.MGeneralSupplier;
import com.nexware.aajapan.models.MTransporter;
import com.nexware.aajapan.payload.DatatableResponse;
import com.nexware.aajapan.payload.Response;
import com.nexware.aajapan.repositories.TransportersRepository;
import com.nexware.aajapan.services.SequenceService;

@Controller
@RequestMapping("/master/trn")
public class TransporterMaster {
	@Autowired
	private TransportersRepository trnRepo;
	@Autowired
	private SequenceService sequenceService;
	
	@PostMapping(value = "create")
	@Transactional
	public ResponseEntity<Response> create(@RequestBody final MTransporter data) {
		final String code = sequenceService.getNextSequence(Constants.SEQUENCE_TRANSPORTER);
		trnRepo.save(new MTransporter(code, data.getName()));
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);

	}
	
	@GetMapping("/list")
	public ModelAndView listTransporterPage() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("shipping.master.transporter.list");
		return modelAndView;
	}
	
	@GetMapping("/list-data")
	@ResponseBody
	public DatatableResponse getListData() {
		return new DatatableResponse(this.trnRepo.getAllUnDeletedTransporters());
	}
	
	@PostMapping("save")
	@ResponseBody
	@Transactional
	public ResponseEntity<Response> save(@RequestBody MTransporter transporter) {
		boolean isNewEntry = transporter.getCode().trim().isEmpty();
		MTransporter transporterToSave = null;
		if (isNewEntry) {
			transporterToSave = transporter;
			transporterToSave.setDeleteFlag(Constants.DELETE_FLAG_0);
			transporterToSave.setCode(sequenceService.getNextSequence(Constants.SEQUENCE_TRANSPORTER));
		} else {
			transporterToSave = this.trnRepo.findOneByCode(transporter.getCode());
			transporterToSave.setName(transporter.getName());
		}
		this.trnRepo.save(transporterToSave);
		return new ResponseEntity<>(
				new Response("success", this.trnRepo.findOneByCode(transporterToSave.getCode())),
				HttpStatus.OK);
	}
	
	@GetMapping("/delete/{code}")
	public ResponseEntity<Response> deleteSupplier(@PathVariable("code") String code, ModelAndView modelAndView,
			RedirectAttributes redirectAttributes) {
		MTransporter transporter = this.trnRepo.findOneByCode(code);
		transporter.setDeleteFlag(Constants.DELETE_FLAG_1);
		this.trnRepo.save(transporter);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}
	
}
