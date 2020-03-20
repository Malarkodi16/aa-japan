package com.nexware.aajapan.repositories.custom.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.models.MForwarder;
import com.nexware.aajapan.models.MVechicleMaker;
import com.nexware.aajapan.repositories.custom.MForwarderRepositoryCustom;

public class MForwarderRepositoryCustomImpl implements MForwarderRepositoryCustom {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private MongoOperations mongoOperations;


	@Override
	public List<MForwarder> getAllUnDeletedForwarders() {
		MatchOperation match = Aggregation.match(Criteria.where("deleteFlag").is(Constants.DELETE_FLAG_0));
		Aggregation aggregation = Aggregation.newAggregation(match);
		AggregationResults<MForwarder> result = this.mongoTemplate.aggregate(aggregation, "m_frwrdr",
				MForwarder.class);
		return result.getMappedResults();
	}
	
	@Override
	public boolean existsByCodeAndName(String code, String name) {
		final Criteria criteria = new Criteria();
		criteria.andOperator(Criteria.where("code").is(code), Criteria.where("name").regex("(?sim)^" + name + "$"));
		return mongoOperations.exists(Query.query(criteria), MForwarder.class);
	}

	@Override
	public boolean existsByName(String name) {
		return mongoOperations.exists(Query.query(Criteria.where("name").regex("(?sim)^" + name + "$")),
				MForwarder.class);
	}

}
