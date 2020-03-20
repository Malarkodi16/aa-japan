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
import com.nexware.aajapan.models.MAuctionGradesExterior;
import com.nexware.aajapan.models.MForwarder;
import com.nexware.aajapan.models.MGeneralSupplier;
import com.nexware.aajapan.repositories.custom.MGeneralSupplierRepositoryCustom;

public class MGeneralSupplierRepositoryCustomImpl implements MGeneralSupplierRepositoryCustom {

	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private MongoOperations mongoOperations;
	
	@Override
	public List<MGeneralSupplier> getAllUnDeletedGeneralSupplier() {
		MatchOperation match = Aggregation.match(Criteria.where("deleteFlag").is(Constants.DELETE_FLAG_0));
		Aggregation aggregation = Aggregation.newAggregation(match);
		AggregationResults<MGeneralSupplier> result = this.mongoTemplate.aggregate(aggregation, "m_gen_supplier",
				MGeneralSupplier.class);
		return result.getMappedResults();
	}
	
	
	/*
	 * @Override public boolean existsByCodeAndName(String code, String name) {
	 * final Criteria criteria = new Criteria();
	 * criteria.andOperator(Criteria.where("code").is(code),
	 * Criteria.where("name").is(name)); return
	 * mongoOperations.exists(Query.query(criteria), MGeneralSupplier.class); }
	 * 
	 * @Override public boolean existsByName(String name) { final Criteria criteria
	 * = new Criteria();
	 * criteria.andOperator(Criteria.where("deleteFlag").is(Constants.DELETE_FLAG_0)
	 * , Criteria.where("name").is(name)); return
	 * mongoOperations.exists(Query.query(criteria), MGeneralSupplier.class); }
	 */

	@Override
	public boolean existsByCodeAndName(String code, String name) {
		final Criteria criteria = new Criteria();
		criteria.andOperator(Criteria.where("deleteFlag").is(Constants.DELETE_FLAG_0), Criteria.where("code").is(code), Criteria.where("name").regex("(?sim)^" + name + "$"));
		return mongoOperations.exists(Query.query(criteria), MGeneralSupplier.class);
	}

	@Override
	public boolean existsByName(String name) {
		final Criteria criteria = new Criteria();
		criteria.andOperator(Criteria.where("deleteFlag").is(Constants.DELETE_FLAG_0), Criteria.where("name").is(name)); 
		return mongoOperations.exists(Query.query(Criteria.where("name").regex("(?sim)^" + name + "$")),
				MGeneralSupplier.class);
	}

	
}
