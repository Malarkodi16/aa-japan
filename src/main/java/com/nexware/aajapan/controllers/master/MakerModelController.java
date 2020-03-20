
package com.nexware.aajapan.controllers.master;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.BulkOperations.BulkMode;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.MModelDto;
import com.nexware.aajapan.models.MVechicleMaker;
import com.nexware.aajapan.models.Model;
import com.nexware.aajapan.payload.DatatableResponse;
import com.nexware.aajapan.payload.Response;
import com.nexware.aajapan.repositories.MasterVechicleMakerRepository;
import com.nexware.aajapan.services.MVechicleMakerService;
import com.nexware.aajapan.services.SequenceService;
import com.nexware.aajapan.utils.AppUtil;

@Controller
@RequestMapping("/master")
public class MakerModelController {
	@Autowired
	private MasterVechicleMakerRepository masterVechicleMakerRepository;
	@Autowired
	private MongoOperations mongoOperation;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private MVechicleMakerService vehicleMakerService;
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private MVechicleMakerService mVechicleMakerService;

	@GetMapping("/list")
	public ModelAndView viewloan() {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("shipping.master.maker/model.list");
		return modelAndView;
	}

	@GetMapping("/list-data")
	@ResponseBody
	public DatatableResponse getListData() {
		return new DatatableResponse(masterVechicleMakerRepository.getListWithoutDelete());
	}

	@GetMapping("/maker")
	public ModelAndView dashBoard(HttpServletRequest request) {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("shipping.master.maker");

		return modelAndView;
	}

	@PostMapping("/save-maker")
	@ResponseBody
	public ResponseEntity<Response> saveMakerModel(@RequestBody MVechicleMaker mVehicleMaker) {
		// boolean isNewEntry = mVehicleMaker.getCode().trim().isEmpty();
		// MVechicleMaker mVehicleMakerToSave = null;
		// if (isNewEntry) {

		mVehicleMaker.setDeleteFlag(Constants.DELETE_FLAG_0);
		mVehicleMaker.setCode(sequenceService.getNextSequence(Constants.SEQUENCE_KEY_MKR));
		mVehicleMaker.getModels()
				.forEach(model -> model.setModelId(sequenceService.getNextSequence(Constants.SEQUENCE_KEY_MODEL)));

		// } else {
		// mVehicleMakerToSave =
		// this.masterVechicleMakerRepository.findOneByCode(mVehicleMaker.getCode());
		// mVehicleMakerToSave.setModels(mVehicleMaker.getModels());
		// mVehicleMakerToSave.setDeleteFlag(Constants.DELETE_FLAG_0);
		// }
		masterVechicleMakerRepository.insert(mVehicleMaker);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PostMapping("/save-model")
	@ResponseBody
	@Transactional
	public ResponseEntity<Response> saveModel(@RequestBody MModelDto modelDto) {
		final MVechicleMaker mVehicleMakerToSave = masterVechicleMakerRepository.findOneByCode(modelDto.getCode());

		if (!AppUtil.isObjectEmpty(mVehicleMakerToSave)) {
			final Model model = modelMapper.map(modelDto, Model.class);
			model.setModelId(sequenceService.getNextSequence(Constants.SEQUENCE_KEY_MODEL));
			final Update update = new Update().pull("models",
					Query.query(new Criteria().andOperator((Criteria.where("modelName").is(model.getModelName())),
							Criteria.where("category").is(model.getCategory()),
							Criteria.where("subcategory").is(model.getSubcategory()))));
			mongoOperation.updateMulti(new Query(Criteria.where("code").is(modelDto.getCode())), update,
					MVechicleMaker.class);
			final Update pushUpdate = new Update().push("models", model);
			mongoOperation.updateFirst(new Query(Criteria.where("code").is(modelDto.getCode())), pushUpdate,
					MVechicleMaker.class);
		}
		return new ResponseEntity<>(
				new Response("success", masterVechicleMakerRepository.getListByCodeWithoutDelete(modelDto.getCode())),
				HttpStatus.OK);
	}

	@GetMapping("/model/delete")
	@Transactional
	public ResponseEntity<Response> deleteFile(@RequestParam("code") String code,
			@RequestParam("modelName") String modelName) {

		final MVechicleMaker makerModel = masterVechicleMakerRepository.findOneByCode(code);
		if (!AppUtil.isObjectEmpty(makerModel)) {

			final Update update = new Update().pull("models", Query.query(Criteria.where("modelName").is(modelName)));
			mongoOperation.updateMulti(new Query(Criteria.where("code").is(code)), update, MVechicleMaker.class);
		}
		return new ResponseEntity<>(
				new Response("success", masterVechicleMakerRepository.getListByCodeWithoutDelete(code)), HttpStatus.OK);
	}

	@GetMapping("/isMakerExists")
	public ResponseEntity<Boolean> isMakerExists(@RequestParam(value = "id", required = false) String code,
			String maker) {
		boolean isValid;

		if (code.isEmpty()) {
			isValid = masterVechicleMakerRepository.existsByName(maker);
		} else {
			isValid = masterVechicleMakerRepository.existsByCodeAndName(code, maker);
			if (!isValid) {
				isValid = masterVechicleMakerRepository.existsByName(maker);
			} else {
				isValid = false;
			}
		}
		return new ResponseEntity<>(isValid, HttpStatus.OK);
	}

	@GetMapping("/maker/edit/{makerCode}")
	public ModelAndView redirectCreate(@PathVariable("makerCode") String makerCode, ModelAndView modelAndView,
			RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("code", makerCode);
		modelAndView.setViewName("redirect:/master/maker");
		return modelAndView;
	}

	@GetMapping("/maker/getInfo/{code}.json")
	public ResponseEntity<Response> getMakerInfo(@PathVariable("code") String code) {
		return new ResponseEntity<>(new Response("success", masterVechicleMakerRepository.findOneByCode(code)),
				HttpStatus.OK);
	}

	@GetMapping("/delete/{code}")
	public ModelAndView deleteSupplier(@PathVariable("code") String code, ModelAndView modelAndView,
			RedirectAttributes redirectAttributes) {
		final MVechicleMaker mVehicleMaker = masterVechicleMakerRepository.findOneByCode(code);
		mVehicleMaker.setDeleteFlag(Constants.DELETE_FLAG_1);
		masterVechicleMakerRepository.save(mVehicleMaker);
		redirectAttributes.addFlashAttribute("message", "deleted Successfully");
		modelAndView.setViewName("redirect:/master/list");
		return modelAndView;
	}

	@Transactional
	@PostMapping("/makermodel/xls")
	public ResponseEntity<Response> createMakerModelUsingCsv(@RequestParam("excelFile") final MultipartFile file,
			HttpServletRequest request) throws IOException {
		BulkOperations ops = this.mongoTemplate.bulkOps(BulkMode.UNORDERED, MVechicleMaker.class);
		mVechicleMakerService.uploadExcelFile(file);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@GetMapping("/export-excel")
	@ResponseBody
	public DatatableResponse exportExcelFormatting() {
		return new DatatableResponse(masterVechicleMakerRepository.exportExcelFormatting());
	}

	@GetMapping("/export-excel-row")
	@ResponseBody
	public DatatableResponse exportExcelFormattingRow(@RequestParam String code) {
		return new DatatableResponse(masterVechicleMakerRepository.exportExcelFormattingRow(code));
	}

	@PutMapping("/edit-model")
	@ResponseBody
	@Transactional
	public ResponseEntity<Response> editModel(@RequestParam("makerName") String makerName,
			@RequestBody Map<String, Object> data) {

		final String modelId = AppUtil.isObjectEmpty(data.get("modelId")) ? "" : data.get("modelId").toString();
		final String subcategory = AppUtil.isObjectEmpty(data.get("subcategory")) ? ""
				: data.get("subcategory").toString();
		final String category = AppUtil.isObjectEmpty(data.get("category")) ? "" : data.get("category").toString();
		final Double m3 = AppUtil.isObjectEmpty(data.get("m3")) ? 0 : Double.parseDouble(data.get("m3").toString());
		final Model editModel = vehicleMakerService.getModelData(makerName, modelId);
		editModel.setCategory(category);
		editModel.setM3(m3);
		editModel.setSubcategory(subcategory);
		editModel.setTransportCategory(data.get("transportCategory").toString());
		editModel.setLength(
				Double.parseDouble(AppUtil.isObjectEmpty(data.get("length")) ? "0.0" : data.get("length").toString()));
		editModel.setWidth(
				Double.parseDouble(AppUtil.isObjectEmpty(data.get("width")) ? "0.0" : data.get("width").toString()));
		editModel.setHeight(
				Double.parseDouble(AppUtil.isObjectEmpty(data.get("height")) ? "0.0" : data.get("height").toString()));

		final MVechicleMaker mVehicleMakerToSave = masterVechicleMakerRepository.findOneByName(makerName);

		if (!AppUtil.isObjectEmpty(mVehicleMakerToSave)) {
			final Model model = modelMapper.map(editModel, Model.class);
			final Update update = new Update().pull("models",
					Query.query(Criteria.where("modelId").is(model.getModelId())));
			mongoOperation.updateMulti(new Query(Criteria.where("code").is(mVehicleMakerToSave.getCode())), update,
					MVechicleMaker.class);
			final Update pushUpdate = new Update().push("models", model);
			mongoOperation.updateFirst(new Query(Criteria.where("code").is(mVehicleMakerToSave.getCode())), pushUpdate,
					MVechicleMaker.class);
		}
		return new ResponseEntity<>(
				new Response("success",
						masterVechicleMakerRepository.getListByCodeWithoutDelete(mVehicleMakerToSave.getCode())),
				HttpStatus.OK);
	}

	@GetMapping("/model/check/DuplicateSubCategory")
	public ResponseEntity<Boolean> checkDuplicateSubCategory(@RequestParam("makerName") String makerName,
			@RequestParam("modelName") String modelName, @RequestParam("category") String category,
			@RequestParam("subcategory") String subcategory) {
		boolean isValid = false;

		final MVechicleMaker makerModel = masterVechicleMakerRepository.findOneByName(makerName);
		for (final Model model : makerModel.getModels()) {
			if (model.getModelName().equals(modelName) && model.getCategory().equals(category)
					&& model.getSubcategory().equals(subcategory)) {
				isValid = true;
			}
		}
		return new ResponseEntity<>(isValid, HttpStatus.OK);
	}
}
