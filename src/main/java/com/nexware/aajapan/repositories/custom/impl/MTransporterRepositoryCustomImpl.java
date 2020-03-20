package com.nexware.aajapan.repositories.custom.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.models.MForwarder;
import com.nexware.aajapan.models.MTransporter;
import com.nexware.aajapan.repositories.custom.MForwarderRepositoryCustom;
import com.nexware.aajapan.repositories.custom.MTransporterRepositoryCustom;

public class MTransporterRepositoryCustomImpl implements MTransporterRepositoryCustom {
	
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<MTransporter> getAllUnDeletedTransporters() {
		MatchOperation match = Aggregation.match(Criteria.where("deleteFlag").is(Constants.DELETE_FLAG_0));
		Aggregation aggregation = Aggregation.newAggregation(match);
		AggregationResults<MTransporter> result = this.mongoTemplate.aggregate(aggregation, "m_trnsprtr",
				MTransporter.class);
		return result.getMappedResults();
	}

}
