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
import com.nexware.aajapan.models.TStockModelType;
import com.nexware.aajapan.repositories.custom.TStockModelTypeRepositoryCustom;

public class TStockModelTypeRepositoryCustomImpl implements TStockModelTypeRepositoryCustom{
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private MongoOperations mongoOperations;


	@Override
	public List<TStockModelType> getAllUnDeletedModel() {
		MatchOperation match = Aggregation.match(Criteria.where("deleteFlag").is(Constants.DELETE_FLAG_0));
		Aggregation aggregation = Aggregation.newAggregation(match);
		AggregationResults<TStockModelType> result = this.mongoTemplate.aggregate(aggregation, "t_stck_mdl_type",
				TStockModelType.class);
		return result.getMappedResults();
	}
	
	@Override
	public boolean existsByCodeAndModelType(String code, String modelType) {
		final Criteria criteria = new Criteria();
		criteria.andOperator(Criteria.where("code").is(code), Criteria.where("modelType").regex("(?sim)^" + modelType + "$"));
		return mongoOperations.exists(Query.query(criteria), TStockModelType.class);
	}
	
	@Override
	public boolean existsByModelType(String modelType) {
		return mongoOperations.exists(Query.query(Criteria.where("modelType").regex("(?sim)^" + modelType + "$")),
				TStockModelType.class);
	}

}
