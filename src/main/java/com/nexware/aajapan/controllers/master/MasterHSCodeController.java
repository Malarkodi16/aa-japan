package com.nexware.aajapan.controllers.master;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.models.MHSCode;
import com.nexware.aajapan.models.MVechicleCategory;
import com.nexware.aajapan.models.SubCategory;
import com.nexware.aajapan.payload.DatatableResponse;
import com.nexware.aajapan.payload.Response;
import com.nexware.aajapan.repositories.MHSCodeRepository;
import com.nexware.aajapan.repositories.MasterVechicleCategoryRepository;
import com.nexware.aajapan.services.SequenceService;
import com.nexware.aajapan.utils.AppUtil;

@Controller
@RequestMapping("/master/hsCode")
public class MasterHSCodeController {

	@Autowired
	private MHSCodeRepository hsCodeRepository;
	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private MasterVechicleCategoryRepository masterVehicleCategoryRepository;

	@GetMapping("/list")
	public ModelAndView listHSCode() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.master.hsCode.list");
		return modelAndView;
	}

	@GetMapping("/list-data")
	@ResponseBody
	public DatatableResponse getListData() {
		return new DatatableResponse(this.hsCodeRepository.findAllByDeleteFlag(Constants.DELETE_FLAG_0));
	}

	@PostMapping("/save")
	@ResponseBody
	@Transactional
	public ResponseEntity<Response> create(@RequestBody MHSCode hsCode) {
		boolean isNewEntry = (hsCode.getCode() == null || hsCode.getCode().isEmpty());
		MHSCode hsCodeToSave = null;
		if (isNewEntry) {
			hsCodeToSave = hsCode;
			hsCodeToSave.setCode(sequenceService.getNextSequence(Constants.SEQUENCE_KEY_HSCODE));
			hsCodeToSave.setDeleteFlag(Constants.DELETE_FLAG_0);
		} else {
			hsCodeToSave = this.hsCodeRepository.findOneByCode(hsCode.getCode());
			hsCodeToSave.setCc(hsCode.getCc());
			hsCodeToSave.setCategory(hsCode.getCategory());
			hsCodeToSave.setSubCategory(hsCode.getSubCategory());
			hsCodeToSave.setHsCode(hsCode.getHsCode());
		}

		this.hsCodeRepository.save(hsCodeToSave);
		return new ResponseEntity<>(new Response("success", hsCodeToSave), HttpStatus.OK);
	}

	@GetMapping("/delete/{code}")
	public ResponseEntity<Response> deleteHsCode(@PathVariable("code") String code) {
		MHSCode hsCode = this.hsCodeRepository.findOneByCode(code);
		hsCode.setDeleteFlag(Constants.DELETE_FLAG_1);
		this.hsCodeRepository.save(hsCode);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@GetMapping("/validHsCode")
	public ResponseEntity<Boolean> validhsCode(@RequestParam("cc") String cc, @RequestParam("category") String category,
			@RequestParam("hsCode") String hsCode, @RequestParam("subCategory") String subCategory) {
		boolean isExists = false;

		if (!AppUtil.isObjectEmpty(cc) && !AppUtil.isObjectEmpty(category) && !AppUtil.isObjectEmpty(hsCode)
				&& !AppUtil.isObjectEmpty(subCategory)) {
			boolean hSCode = this.hsCodeRepository.existsByCcAndCategoryAndSubCategoryAndHsCode(cc, category,
					subCategory, hsCode);
			if (hSCode) {
				isExists = true;
			}
		}
		return new ResponseEntity<>(isExists, HttpStatus.OK);
	}

	@PostMapping("/upload/xls")
	@Transactional
	public ResponseEntity<Response> createHSCodeUsingExcel(@RequestParam("excelFile") final MultipartFile file,
			HttpServletRequest request) throws IOException {
		try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
			Sheet sheet = workbook.getSheetAt(0);
			for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
				Row row = sheet.getRow(i);
				if (row.getCell(0) == null || row.getCell(1) == null || row.getCell(3) == null) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return new ResponseEntity<>(new Response("Failure"), HttpStatus.OK);
				}
				MVechicleCategory category = masterVehicleCategoryRepository
						.findOneByName(row.getCell(1).toString().toUpperCase());
				if (AppUtil.isObjectEmpty(category)) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return new ResponseEntity<>(new Response("Category Not Found"), HttpStatus.OK);
				}
				List<SubCategory> cat = new ArrayList<SubCategory>();
				if (!(row.getCell(2) == null || row.getCell(2).getCellTypeEnum() == CellType.BLANK)) {
					cat = category.getSubCategories().stream()
							.filter(subCategory -> subCategory.getName().equalsIgnoreCase(row.getCell(2).toString()))
							.collect(Collectors.toList());
					if (AppUtil.isObjectEmpty(cat)) {
						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
						return new ResponseEntity<>(new Response("Sub Category Not Found"), HttpStatus.OK);
					}
				} 
				final String code = sequenceService.getNextSequence(Constants.SEQUENCE_KEY_HSCODE);
				MHSCode hsCode = new MHSCode();
				hsCode.setCode(code);
				hsCode.setCc(row.getCell(0).getNumericCellValue());
				hsCode.setCategory(category.getCode());
				hsCode.setSubCategory(AppUtil.isObjectEmpty(cat) ? "" : cat.get(0).getCode());
				hsCode.setHsCode(row.getCell(3).toString().toUpperCase());
				hsCode.setDeleteFlag(Constants.DELETE_FLAG_0);
				hsCodeRepository.insert(hsCode);
			}
		} catch (IOException exception) {
			return new ResponseEntity<>(new Response("failure", exception.getMessage()), HttpStatus.OK);
		}
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

}
