package com.nexware.aajapan.controllers.master;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.models.MPort;
import com.nexware.aajapan.payload.DatatableResponse;
import com.nexware.aajapan.payload.Response;
import com.nexware.aajapan.repositories.MPortRepository;
import com.nexware.aajapan.services.SequenceService;
import com.nexware.aajapan.utils.AppUtil;

@Controller
@RequestMapping("/master/port")
public class MasterPortController {

	@Autowired
	private MPortRepository portRepository;
	@Autowired
	private SequenceService sequenceService;

	@GetMapping("/list")
	public ModelAndView listPort() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("shipping.master.port.list");
		return modelAndView;
	}

	@GetMapping("/list-data")
	@ResponseBody
	public DatatableResponse getListData() {
		return new DatatableResponse(this.portRepository.findAll());
	}

	@GetMapping("/create")
	public ModelAndView createPort(ModelAndView modelAndView) {
		modelAndView.setViewName("shipping.master.location.create");
		return modelAndView;
	}

	@PostMapping("save")
	@Transactional
	public ResponseEntity<Response> create(MPort port) {
		port.setPortId(Integer.valueOf(sequenceService.getNextSequence(Constants.SEQUENCE_KEY_MPORT)));
		this.portRepository.save(port);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@GetMapping("/validate/portName")
	public ResponseEntity<Boolean> validatePortname(
			@RequestParam(value = "portName", required = false) String portName) {
		boolean validate = false;
		MPort port = this.portRepository.findOneByPortName(portName.toUpperCase());
		if (!AppUtil.isObjectEmpty(port)) {
			validate = true;
		}
		return new ResponseEntity<>(validate, HttpStatus.OK);
	}

}
