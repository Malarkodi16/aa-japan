package com.nexware.aajapan.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.models.TStock;
import com.nexware.aajapan.models.TStockModelType;
import com.nexware.aajapan.repositories.TStockModelTypeRepository;
import com.nexware.aajapan.services.SequenceService;
import com.nexware.aajapan.services.TStockModelTypeService;
import com.nexware.aajapan.utils.AppUtil;

@Service
@Transactional
public class TStockModelTypeServiceImpl implements TStockModelTypeService {

	@Autowired
	private TStockModelTypeRepository stockModelTypeRepository;
	@Autowired
	private SequenceService sequenceService;

	@Override
	public void saveStockModelType(TStock stock) {
		TStockModelType modelTypeObject = stockModelTypeRepository.findOneByModelType(stock.getModelType());
		if (AppUtil.isObjectEmpty(modelTypeObject)) {
			modelTypeObject = new TStockModelType();
			modelTypeObject.setModelType(stock.getModelType());
			modelTypeObject.setCode(sequenceService.getNextSequence(Constants.SEQUENCE_KEY_STOCK_MODEL_TYPE));
		} else {
			modelTypeObject.setModelType(modelTypeObject.getModelType());
			modelTypeObject.setCode(modelTypeObject.getCode());
		}
		modelTypeObject.setMaker(stock.getMaker());
		modelTypeObject.setModel(stock.getModel());
		modelTypeObject.setCategory(stock.getCategory());
		modelTypeObject.setSubcategory(stock.getSubcategory());
		modelTypeObject.setTransmission(stock.getTransmission());
		modelTypeObject.setManualTypes(stock.getManualTypes());

		modelTypeObject.setFuel(stock.getFuel());
		modelTypeObject.setDriven(stock.getDriven());
		modelTypeObject.setCc(stock.getCc());
		modelTypeObject.setUnit(stock.getUnit());

		if (!AppUtil.isObjectEmpty(modelTypeObject.getModelType())) {
			stockModelTypeRepository.save(modelTypeObject);
		}
	}

}
