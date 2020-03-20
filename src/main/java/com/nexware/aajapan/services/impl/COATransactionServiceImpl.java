package com.nexware.aajapan.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexware.aajapan.core.AccountTransactionConstants;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.exceptions.AAJRuntimeException;
import com.nexware.aajapan.models.MCOA;
import com.nexware.aajapan.services.COATransactionService;
import com.nexware.aajapan.services.TStockService;
import com.nexware.aajapan.utils.AppUtil;

@Service
@Transactional
public class COATransactionServiceImpl implements COATransactionService {
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private TStockService stockService;

	@Override
	public MCOA modifyAndGetCurrentCOA(Long coaCode, Double amount, int transactionType) {
		Query query = new Query(Criteria.where("code").is(coaCode));
		// update balance
		Update update = new Update();
		if (transactionType == Constants.TRANSACTION_CREDIT) {
			update.inc("balance", (amount * -1));
		} else if (transactionType == Constants.TRANSACTION_DEBIT) {
			update.inc("balance", amount);
		}
		// return new balance
		FindAndModifyOptions options = new FindAndModifyOptions();
		options.returnNew(false);
		MCOA updated = this.mongoTemplate.findAndModify(query, update, options, MCOA.class);
		if (AppUtil.isObjectEmpty(updated)) {
			throw new AAJRuntimeException("Exception while update balance : " + coaCode);
		}
		return updated;
	}

	@Override
	public Long checkStockInventoryStatusAndGetCoaCode(String stockNo, Long coaCode) {

		Integer inventoryStatus = this.stockService.getStockInventoryStatus(stockNo);
		if (AppUtil.isObjectEmpty(inventoryStatus)) {
			return coaCode;
		}
		if (inventoryStatus == Constants.STOCK_INVENTORY_STATUS_GENERAL) {
			return AccountTransactionConstants.INVENTORY_GENERAL;
		} else if (inventoryStatus == Constants.STOCK_INVENTORY_STATUS_STAGING) {
			return AccountTransactionConstants.INVENTORY_STAGING;
		} else if (inventoryStatus == Constants.STOCK_INVENTORY_STATUS_IN_TRANSIT) {
			return AccountTransactionConstants.INVENTORY_IN_TRANSIT;
		} else if (inventoryStatus == Constants.STOCK_INVENTORY_STATUS_IN_CONSIGNMENT) {
			return AccountTransactionConstants.INVENTORY_CONSIGNMENT;
		}
		return coaCode;

	}

}
