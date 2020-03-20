package com.nexware.aajapan.repositories.custom.impl;

import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.models.MInspectionCompany;
import com.nexware.aajapan.models.MTransporter;
import com.nexware.aajapan.models.MVechicleMaker;
import com.nexware.aajapan.repositories.custom.MInspectionCompanyRepositoryCustom;
import com.nexware.aajapan.repositories.custom.MTransporterRepositoryCustom;

public class MInspectionCompanyRepositoryCustomImpl implements MInspectionCompanyRepositoryCustom {

	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private MongoOperations mongoOperations;

	@Override
	public List<MInspectionCompany> getAllUnDeletedInspectionCompany() {
		MatchOperation match = Aggregation.match(Criteria.where("deleteFlag").is(Constants.DELETE_FLAG_0));
		AggregationOperation project = context -> new Document("$project", new Document("id", "$id")
				.append("code", "$code").append("name", "$name").append("deleteFlag", "$deleteFlag")
				.append("locations", new Document("$filter",
						new Document("input", "$locations").append("as", "result").append("cond", new Document("$eq",
								Arrays.asList("$$result.deleteStatus", Constants.DELETE_FLAG_0))))));
		Aggregation aggregation = Aggregation.newAggregation(match, project);
		AggregationResults<MInspectionCompany> result = this.mongoTemplate.aggregate(aggregation, "m_inspctn_cmpny",
				MInspectionCompany.class);
		return result.getMappedResults();
	}

	@Override
	public MInspectionCompany getActiveInspectionCompany(String code) {
		MatchOperation match = Aggregation.match(new Criteria().andOperator(
				Criteria.where("deleteFlag").is(Constants.DELETE_FLAG_0), Criteria.where("code").is(code)));
		AggregationOperation project = context -> new Document("$project", new Document("id", "$id")
				.append("code", "$code").append("name", "$name").append("deleteFlag", "$deleteFlag")
				.append("locations", new Document("$filter",
						new Document("input", "$locations").append("as", "result").append("cond", new Document("$eq",
								Arrays.asList("$$result.deleteStatus", Constants.DELETE_FLAG_0))))));
		Aggregation aggregation = Aggregation.newAggregation(match, project);
		AggregationResults<MInspectionCompany> result = this.mongoTemplate.aggregate(aggregation, "m_inspctn_cmpny",
				MInspectionCompany.class);
		return result.getUniqueMappedResult();
	}

	@Override
	public boolean existsByCodeAndName(String code, String name) {
		final Criteria criteria = new Criteria();
		criteria.andOperator(Criteria.where("code").is(code), Criteria.where("name").regex("(?sim)^" + name + "$"));
		return mongoOperations.exists(Query.query(criteria), MInspectionCompany.class);
	}

	@Override
	public boolean existsByName(String name) {
		return mongoOperations.exists(Query.query(Criteria.where("name").regex("(?sim)^" + name + "$")),
				MInspectionCompany.class);
	}

}
