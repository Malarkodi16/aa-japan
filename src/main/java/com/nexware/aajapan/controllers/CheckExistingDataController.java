package com.nexware.aajapan.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nexware.aajapan.repositories.MAuctionGradesExteriorRepository;
import com.nexware.aajapan.repositories.MAuctionGradesInteriorRepository;
import com.nexware.aajapan.repositories.MForwarderRepository;
import com.nexware.aajapan.repositories.MGeneralSupplierRepository;
import com.nexware.aajapan.repositories.MasterShippingCompanyRepository;
import com.nexware.aajapan.repositories.TInvoiceRepository;
import com.nexware.aajapan.repositories.TStockModelTypeRepository;
import com.nexware.aajapan.utils.AppUtil;

@Controller
@RequestMapping("/check/existing/in/")
public class CheckExistingDataController {

	@Autowired
	private MGeneralSupplierRepository masterGeneralSupplierRepository;
	@Autowired
	private TInvoiceRepository tInvoiceRepository;
	@Autowired
	private MForwarderRepository forwarderRepository;
	@Autowired
	private TStockModelTypeRepository tStockModelTypeRepository;
	@Autowired
	private MAuctionGradesExteriorRepository auctionGradesExteriorRepository;
	@Autowired
	private MAuctionGradesInteriorRepository auctionGradesInteriorRepository;
	@Autowired
	private MasterShippingCompanyRepository shippingCompanyRepository;
	
	@GetMapping("generalSupplier")
	public ResponseEntity<Boolean> isSameSupplierExists(@RequestParam("name") String name) {
		boolean isExists = false;
		if (!AppUtil.isObjectEmpty(name)) {
			isExists = masterGeneralSupplierRepository.existsByName(name);
		}
		return new ResponseEntity<>(isExists, HttpStatus.OK);
	}

	@GetMapping("generalExpense/invoiceNo")
	public ResponseEntity<Boolean> isSameInvoiceNoExistsInGeneralSupplier(@RequestParam("refNo") String refNo,
			@RequestParam(value = "invoiceNo", required = false) String invoiceNo) {
		boolean isExists = false;
		if (AppUtil.isObjectEmpty(invoiceNo)) {
			isExists = tInvoiceRepository.isSameRefNoExistsInGeneralSupplier(refNo);
		} else {
			isExists = tInvoiceRepository.isSameRefNoExistsWithInvoiceNo(invoiceNo, refNo);
			if (!isExists) {
				isExists = tInvoiceRepository.isSameRefNoExistsInGeneralSupplier(refNo);
			} else {
				isExists = false;
			}
		}
		return new ResponseEntity<>(isExists, HttpStatus.OK);
	}

	@GetMapping("isForwarderExists")
	public ResponseEntity<Boolean> isForwarderExists(@RequestParam(value = "id", required = false) String code,
			@RequestParam("name") String name) {
		boolean isValid = false;

		if (AppUtil.isObjectEmpty(code)) {
			isValid = forwarderRepository.existsByName(name);
		} else {
			isValid = forwarderRepository.existsByCodeAndName(code, name);
			if (!isValid) {
				isValid = forwarderRepository.existsByName(name);
			} else {
				isValid = false;
			}
		}
		return new ResponseEntity<>(isValid, HttpStatus.OK);
	}

	@GetMapping("GradeExteriorExists")
	public ResponseEntity<Boolean> GradeExteriorExists(@RequestParam(value = "id", required = false) String id,
			@RequestParam("grade") String grade) {
		boolean isValid = false;

		if (AppUtil.isObjectEmpty(id)) {
			isValid = auctionGradesExteriorRepository.existsByGrade(grade);
		} else {
			isValid = auctionGradesExteriorRepository.existsByIdAndGrade(id, grade);
			if (!isValid) {
				isValid = auctionGradesExteriorRepository.existsByGrade(grade);
			} else {
				isValid = false;
			}
		}
		return new ResponseEntity<>(isValid, HttpStatus.OK);
	}

	@GetMapping("GradeInteriorExists")
	public ResponseEntity<Boolean> GradeInteriorExists(@RequestParam(value = "id", required = false) String id,
			@RequestParam("grade") String grade) {
		boolean isValid = false;

		if (AppUtil.isObjectEmpty(id)) {
			isValid = auctionGradesInteriorRepository.existsByGrade(grade);
		} else {
			isValid = auctionGradesInteriorRepository.existsByIdAndGrade(id, grade);
			if (!isValid) {
				isValid = auctionGradesInteriorRepository.existsByGrade(grade);
			} else {
				isValid = false;
			}
		}
		return new ResponseEntity<>(isValid, HttpStatus.OK);
	}

	@GetMapping("generalSupplierCreate")

	public ResponseEntity<Boolean> generalSupplier(@RequestParam(value = "code", required = false) String code,
			@RequestParam("name") String name) {
		boolean isValid = false;

		if (AppUtil.isObjectEmpty(code)) {
			isValid = masterGeneralSupplierRepository.existsByName(name);
		} else {
			isValid = masterGeneralSupplierRepository.existsByCodeAndName(code, name);
			if (!isValid) {
				isValid = masterGeneralSupplierRepository.existsByName(name);
			} else {
				isValid = false;
			}
		}
		return new ResponseEntity<>(isValid, HttpStatus.OK);
	}
	
	@GetMapping("validShippingComp")
	public ResponseEntity<Boolean> shippingCompanyExists(@RequestParam(value = "shippingCompanyNo", required = false) String shippingCompanyNo,
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
	
	@GetMapping("isModelTypeExists")
	public ResponseEntity<Boolean> isModelTypeExists(@RequestParam(value = "code", required = false) String code,
			@RequestParam("modelType") String modelType) {
		boolean isValid = false;

		if (AppUtil.isObjectEmpty(code)) {
			isValid = tStockModelTypeRepository.existsByModelType(modelType);
		} else {
			isValid = tStockModelTypeRepository.existsByCodeAndModelType(code, modelType);
			if (!isValid) {
				isValid = tStockModelTypeRepository.existsByModelType(modelType);
			} else {
				isValid = false;
			}
		}
		return new ResponseEntity<>(isValid, HttpStatus.OK);
	}

}
