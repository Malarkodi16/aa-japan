package com.nexware.aajapan.repositories.custom.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.MHSCodeDto;
import com.nexware.aajapan.models.MHSCode;
import com.nexware.aajapan.repositories.custom.MHSCodeRepositoryCustom;

public class MHSCodeRepositoryCustomImpl implements MHSCodeRepositoryCustom {

	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private MongoOperations mongoOperations;

	@Override
	public boolean existsByCcAndCategoryAndSubCategoryAndHsCode(String cc, String category, String subCategory, String hsCode) {
		// TODO Auto-generated method stub
		final Criteria criteria = new Criteria();
		criteria.andOperator(Criteria.where("deleteFlag").is(Constants.DELETE_FLAG_0), Criteria.where("cc").is(Double.parseDouble(cc)),
				Criteria.where("category").is(category), Criteria.where("subCategory").is(subCategory), Criteria.where("hsCode").is(hsCode));
		return mongoOperations.exists(Query.query(criteria), MHSCode.class);
	}

	@Override
	public List<MHSCode> getAllUnDeletedhsCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MHSCodeDto> findAllData() {
		MatchOperation matchCategory = Aggregation
				.match(Criteria.where("deleteFlag").is(Constants.DELETE_FLAG_0));

		LookupOperation lookupCategory  = LookupOperation.newLookup().from("m_vchcl_ctgry").localField("category")
				.foreignField("code").as("vchl_category");
		
		ProjectionOperation project = Aggregation.project()
				.andInclude("id","cc","code","category","categoryName","subCategory","hsCode")
				.and("$vchl_category.name").as("categoryName");
		
		Aggregation aggregation = Aggregation.newAggregation(matchCategory, lookupCategory, Aggregation.unwind("$vchl_category", true), project);
		AggregationResults<MHSCodeDto> result = this.mongoTemplate.aggregate(aggregation, "m_hs_code",
				MHSCodeDto.class);
		return (result.getMappedResults());
	}

}
