package com.nexware.aajapan.services.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.nexware.aajapan.exceptions.AAJRuntimeException;
import com.nexware.aajapan.models.MSequence;
import com.nexware.aajapan.repositories.TProformaInvoiceRepository;
import com.nexware.aajapan.services.SequenceService;
import com.nexware.aajapan.utils.AppUtil;

@Service
@Transactional
public class SequenceServiceImpl implements SequenceService {
	@Autowired
	private MongoOperations mongoOperation;
	@Autowired
	private TProformaInvoiceRepository proformaInvoiceOrderRepository;

	@Override
	public String getNextSequence(String key) {
		// get sequence id
		Query query = new Query(Criteria.where("type").is(key));

		// increase sequence id by 1
		Update update = new Update();
		update.inc("sequence", 1);

		// return new increased id
		FindAndModifyOptions options = new FindAndModifyOptions();
		options.returnNew(true);

		MSequence sequence = this.mongoOperation.findAndModify(query, update, options, MSequence.class);

		// if no id, throws SequenceException
		// optional, just a way to tell user when the sequence id is failed to generate.

		if (AppUtil.isObjectEmpty(sequence)) {
			throw new AAJRuntimeException("Unable to get sequence id for key : " + key);
		}

		return AppUtil.ifNull(sequence.getPrefix(), "") + sequence.getSequence();

	}

	@Override
	public String getNextPurchaseInvoiceNo(String key, Date date, String sequenceId) {
		// get sequence id
		Query query = new Query(Criteria.where("type").is(key));

		// increase sequence id by 1
		Update update = new Update();
		update.inc("sequence", 1);
		// return new increased id
		FindAndModifyOptions options = new FindAndModifyOptions();
		options.returnNew(true);
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyy-");
		String date1 = dateFormat.format(date);
		String supplierName = null;
		String supplierName1 = sequenceId.replaceAll("\\s+", "");

		if (supplierName1.length() <= 3) {
			supplierName = supplierName1.toUpperCase();
		} else {
			supplierName = sequenceId.substring(0, 3).toUpperCase();
		}

		MSequence sequence = this.mongoOperation.findAndModify(query, update, options, MSequence.class);

		// if no id, throws SequenceException
		// optional, just a way to tell user when the sequence id is failed to generate.

		if (AppUtil.isObjectEmpty(sequence)) {
			throw new AAJRuntimeException("Unable to get sequence id for key : " + key);
		}

		return supplierName + date1 + sequence.getSequence();
	}

	@Override
	public String generateSalesCustomerSeqProformaInvioceId(String proformaId, long count) {

		Document document = this.proformaInvoiceOrderRepository.findByProformaInvoiceNo(proformaId);
		String salesPersonName = (String) document.get("salesName");
		String customerName = (String) document.get("firstName");
		Date date = (Date) document.get("createdDate");
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM-");
		String date1 = dateFormat.format(date);
		// @SuppressWarnings("unchecked")
		// List<Document> items = (List<Document>) document.get("items");
		// long count = items.size();
		if (salesPersonName.length() <= 3) {
			salesPersonName = salesPersonName.toUpperCase();
		} else {
			salesPersonName = salesPersonName.substring(0, 3).toUpperCase();
		}

		if (customerName.length() <= 3) {
			customerName = customerName.toUpperCase();
		} else {
			customerName = customerName.substring(0, 3).toUpperCase();
		}
		return salesPersonName + customerName + date1 + count;
	}
}
