package com.nexware.aajapan.controllers.master;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.models.MForwarder;
import com.nexware.aajapan.models.MSupplier;
import com.nexware.aajapan.models.TStockModelType;
import com.nexware.aajapan.payload.DatatableResponse;
import com.nexware.aajapan.payload.Response;
import com.nexware.aajapan.repositories.MForwarderRepository;
import com.nexware.aajapan.repositories.TStockModelTypeRepository;
import com.nexware.aajapan.services.SequenceService;
import com.nexware.aajapan.utils.AppUtil;

@Controller
@RequestMapping("/master/stockType")
public class MasterStockModelTypeController {

	@Autowired
	private TStockModelTypeRepository tStockModelTypeRepository;
	@Autowired
	private SequenceService sequenceService;

	@GetMapping("/list")
	public ModelAndView listForwarderPage() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("shipping.master.stock.model.type.list");
		return modelAndView;
	}

	@GetMapping("/list-data")
	@ResponseBody
	public DatatableResponse getListData() {
		return new DatatableResponse(this.tStockModelTypeRepository.findAll());
	}

	@GetMapping("/edit/{code}")
	public ModelAndView supplierCreate(@PathVariable("code") String code, ModelAndView modelAndView,
			RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("code", code);
		modelAndView.setViewName("shipping.master.stock.model.type.edit");
		return modelAndView;
	}

	@PutMapping(path = "/edit")
	public ResponseEntity<Response> modelStockTypeEdit(@RequestBody TStockModelType tStockModelType) {
		TStockModelType modelToEdit = tStockModelTypeRepository.findOneByCode(tStockModelType.getCode());
		modelToEdit.setCategory(tStockModelType.getCategory());
		modelToEdit.setCc(tStockModelType.getCc());
		modelToEdit.setDriven(tStockModelType.getDriven());
		modelToEdit.setModelType(tStockModelType.getModelType());
		modelToEdit.setMaker(tStockModelType.getMaker());
		modelToEdit.setModel(tStockModelType.getModel());
		modelToEdit.setUnit(tStockModelType.getUnit());
		modelToEdit.setSubcategory(tStockModelType.getSubcategory());
		modelToEdit.setTransmission(tStockModelType.getTransmission());
		modelToEdit.setManualTypes(tStockModelType.getManualTypes());
		modelToEdit.setFuel(tStockModelType.getFuel());
		modelToEdit.setEquipment(tStockModelType.getEquipment());
		tStockModelTypeRepository.save(modelToEdit);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);

	}

	@GetMapping("/delete/{code}")
	public ResponseEntity<Response> deleteStockType(@PathVariable("code") String code, ModelAndView modelAndView,
			RedirectAttributes redirectAttributes) {
		this.tStockModelTypeRepository.deleteOneByCode(code);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@GetMapping("/info/{code}.json")
	public ResponseEntity<Response> activeCompanyInfo(@PathVariable("code") String code) {
		return new ResponseEntity<Response>(new Response("success", this.tStockModelTypeRepository.findOneByCode(code)),
				HttpStatus.OK);
	}

}
