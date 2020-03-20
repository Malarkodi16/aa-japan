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
import com.nexware.aajapan.models.MShippingCompany;
import com.nexware.aajapan.repositories.custom.MShippingCompanyRepositoryCustom;

public class MShippingCompanyRepositoryCustomImpl implements MShippingCompanyRepositoryCustom {

	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private MongoOperations mongoOperations;
	
	

	@Override
	public boolean existsByName(String name) {
		final Criteria criteria = new Criteria();
		criteria.andOperator(Criteria.where("deleteFlag").is(Constants.DELETE_FLAG_0), Criteria.where("name").is(name));
		return mongoOperations.exists(Query.query(criteria), MShippingCompany.class);
	}



	@Override
	public boolean existByNameAndShippingCompanyNo(String name, String shippingCompanyNo) {
		final Criteria criteria = new Criteria();
		criteria.andOperator(Criteria.where("name").is(name), Criteria.where("shippingCompanyNo").is(shippingCompanyNo));
		return mongoOperations.exists(Query.query(criteria), MShippingCompany.class);
	}



	@Override
	public List<MShippingCompany> getAllUnDeletedShippingCompany() {
		MatchOperation match = Aggregation.match(Criteria.where("deleteFlag").is(Constants.DELETE_FLAG_0));
		Aggregation aggregation = Aggregation.newAggregation(match);
		AggregationResults<MShippingCompany> result = this.mongoTemplate.aggregate(aggregation, "m_shppng_cmpny",
				MShippingCompany.class);
		return result.getMappedResults();
	}

}
