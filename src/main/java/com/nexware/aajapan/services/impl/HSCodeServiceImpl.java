package com.nexware.aajapan.services.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.exceptions.AAJRuntimeException;
import com.nexware.aajapan.models.MHSCode;
import com.nexware.aajapan.models.MVechicleCategory;
import com.nexware.aajapan.models.MVechicleMaker;
import com.nexware.aajapan.models.Model;
import com.nexware.aajapan.repositories.MHSCodeRepository;
import com.nexware.aajapan.repositories.MasterVechicleCategoryRepository;
import com.nexware.aajapan.services.HSCodeService;
import com.nexware.aajapan.services.SequenceService;
import com.nexware.aajapan.utils.ExcelUtil;

@Service
@Transactional
public class HSCodeServiceImpl implements HSCodeService{
	
	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private MHSCodeRepository hsCodeRepository;
	@Autowired
	private MasterVechicleCategoryRepository masterVehicleCategoryRepository;
	
	@Override
	public void uploadExcelFile(MultipartFile file) throws IOException {
		try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
			Sheet sheet = workbook.getSheetAt(0);
		for(int i = 1; i<sheet.getPhysicalNumberOfRows(); i++) {
			Row row = sheet.getRow(i);
			final String code = sequenceService.getNextSequence(Constants.SEQUENCE_KEY_HSCODE);
			MVechicleCategory category = masterVehicleCategoryRepository.findOneByName(row.getCell(1).toString().toUpperCase());
			MHSCode hsCode = new MHSCode();
			hsCode.setCode(code);
			hsCode.setCc(row.getCell(0).getNumericCellValue());
			hsCode.setCategory(category.getCode());
			hsCode.setHsCode(row.getCell(2).toString().toUpperCase());
			hsCode.setDeleteFlag(Constants.DELETE_FLAG_0);
			hsCodeRepository.insert(hsCode);
		}	
		}catch(IOException exception) {
			throw new AAJRuntimeException("Could not find the CSV file: " + exception);
		}
		
	}
	
	
}
