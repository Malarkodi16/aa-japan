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
import com.nexware.aajapan.models.MVechicleMaker;
import com.nexware.aajapan.models.Model;
import com.nexware.aajapan.repositories.MasterVechicleMakerRepository;
import com.nexware.aajapan.services.MVechicleMakerService;
import com.nexware.aajapan.services.SequenceService;
import com.nexware.aajapan.utils.ExcelUtil;

@Service
@Transactional
public class MVechicleMakerServiceImpl implements MVechicleMakerService {

	@Autowired
	private MasterVechicleMakerRepository makerModelRepository;
	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private MasterVechicleMakerRepository masterVechicleMakerRepository;

	@Override
	public Model getModelData(String maker, String modelId) {
		return this.makerModelRepository.getModelData(maker, modelId);
	}
	
	@Override
	public void uploadExcelFile(MultipartFile file) throws IOException {
		try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
			Sheet sheet = workbook.getSheetAt(0);
			MVechicleMaker mVechicleMaker = null;
			List<Model> modelList = new ArrayList<Model>();
		for(int i = 1; i<=sheet.getPhysicalNumberOfRows()-1; i++) {
			if(i<=sheet.getPhysicalNumberOfRows()-2) {
			Row row = sheet.getRow(i);
			Row row1 = sheet.getRow(i+1);
			String maker = ExcelUtil.getCellValue(row, 0, String.class);
			String maker1 = ExcelUtil.getCellValue(row1, 0, String.class);			
			if(maker.equalsIgnoreCase(maker1)) {				
				if(i==sheet.getPhysicalNumberOfRows()-2) {
					modelList.add(settingModel(row));
					modelList.add(settingModel(row1));
					settingMaker(modelList, maker);
				}
				else {
					modelList.add(settingModel(row));
				}
			}
			else {
				if(i==sheet.getPhysicalNumberOfRows()-2) {				
					modelList.add(settingModel(row));
					settingMaker(modelList, maker);
					modelList.clear();
					modelList.add(settingModel(row1));
					settingMaker(modelList, maker1);
				}
				else {
				modelList.add(settingModel(row));
				settingMaker(modelList, maker);
				modelList.clear();
				}
			}
}
		}	
		}
		
	}
	
	public Model settingModel(Row row) {
		final String modelsId = sequenceService.getNextSequence(Constants.SEQUENCE_KEY_MODEL);
		Model model = new Model();
		model.setModelId(modelsId);
		model.setModelName(row.getCell(1).toString().toUpperCase());
		model.setCategory(row.getCell(2).toString().toUpperCase());
		model.setM3(Double.valueOf(row.getCell(3).toString()));
		model.setLength(Double.valueOf(row.getCell(4)==null || row.getCell(4).getCellTypeEnum()==CellType.BLANK?"0.0":row.getCell(4).toString()));
		model.setWidth(Double.valueOf(row.getCell(5)==null || row.getCell(5).getCellTypeEnum()==CellType.BLANK?"0.0":row.getCell(5).toString()));
		model.setHeight(Double.valueOf(row.getCell(6)==null || row.getCell(6).getCellTypeEnum()==CellType.BLANK?"0.0":row.getCell(6).toString()));
		return model;
		
	}
	
	public void settingMaker(List<Model> modelList,String maker) {
		final String makerId = sequenceService.getNextSequence(Constants.SEQUENCE_KEY_MKR);
		MVechicleMaker mVechicleMaker = new MVechicleMaker();
		mVechicleMaker.setCode(makerId);
		mVechicleMaker.setName(maker.toUpperCase());
		mVechicleMaker.setModels(modelList);
		masterVechicleMakerRepository.insert(mVechicleMaker);
		
	}

}
