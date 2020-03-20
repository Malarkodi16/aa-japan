package com.nexware.aajapan.repositories.custom.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.models.MForeignBank;
import com.nexware.aajapan.repositories.custom.MForeignBankRepositoryCustom;

public class MForeignBankRepositoryCustomImpl implements MForeignBankRepositoryCustom {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private MongoOperations mongoOperations;


	@Override
	public List<MForeignBank> getAllUnDeletedForeignBank() {
		MatchOperation match = Aggregation.match(Criteria.where("deleteFlag").is(Constants.DELETE_FLAG_0));
		Aggregation aggregation = Aggregation.newAggregation(match);
		AggregationResults<MForeignBank> result = this.mongoTemplate.aggregate(aggregation, "m_frgn_bnks",
				MForeignBank.class);
		return result.getMappedResults();
	}

}
