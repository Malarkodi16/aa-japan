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
import com.nexware.aajapan.models.MForwarder;
import com.nexware.aajapan.payload.DatatableResponse;
import com.nexware.aajapan.payload.Response;
import com.nexware.aajapan.repositories.MForwarderRepository;
import com.nexware.aajapan.services.SequenceService;

@Controller
@RequestMapping("/master/forwarder")
public class MasterForwarderController {

	@Autowired
	private MForwarderRepository forwarderRepository;
	@Autowired
	private SequenceService sequenceService;

	@GetMapping("/list")
	public ModelAndView listForwarderPage() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("shipping.master.forwarder.list");
		return modelAndView;
	}

	@GetMapping("/list-data")
	@ResponseBody
	public DatatableResponse getListData() {
		return new DatatableResponse(this.forwarderRepository.getAllUnDeletedForwarders());
	}

	@PostMapping("save")
	@ResponseBody
	@Transactional
	public ResponseEntity<Response> create(@RequestBody MForwarder forwarder) {
		boolean isNewEntry = forwarder.getCode().trim().isEmpty();
		MForwarder forwarderToSave = null;
		if (isNewEntry) {
			forwarderToSave = forwarder;
			forwarderToSave.setDeleteFlag(Constants.DELETE_FLAG_0);
			forwarderToSave.setCode(sequenceService.getNextSequence(Constants.SEQUENCE_KEY_FRWDR));
		} else {
			forwarderToSave = this.forwarderRepository.findOneByCode(forwarder.getCode());
			forwarderToSave.setName(forwarder.getName());
		}
		this.forwarderRepository.save(forwarderToSave);
		return new ResponseEntity<>(
				new Response("success", this.forwarderRepository.findOneByCode(forwarderToSave.getCode())),
				HttpStatus.OK);
	}

	@GetMapping("/delete/{code}")
	public ResponseEntity<Response> deleteSupplier(@PathVariable("code") String code, ModelAndView modelAndView,
			RedirectAttributes redirectAttributes) {
		MForwarder forwarder = this.forwarderRepository.findOneByCode(code);
		forwarder.setDeleteFlag(Constants.DELETE_FLAG_1);
		this.forwarderRepository.save(forwarder);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

}
