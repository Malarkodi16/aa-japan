package com.nexware.aajapan.repositories.custom.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import com.nexware.aajapan.dto.TStockEditHistoryDto;
import com.nexware.aajapan.repositories.custom.TStockEditHistoryRepositoryCustom;

public class TStockEditHistoryRepositoryCustomImpl implements TStockEditHistoryRepositoryCustom {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<TStockEditHistoryDto> findAllByStockNo(String stockNo) {
		final MatchOperation matchOperation = Aggregation.match(Criteria.where("stockNo").is(stockNo));

		final LookupOperation lookupStockDisplayName = LookupOperation.newLookup().from("m_stck_dsply_name")
				.localField("columnName").foreignField("columnName").as("displayName");

		final ProjectionOperation project = Aggregation.project()
				.andInclude("columnName", "originalValue", "newValue", "createdDate", "createdBy")
				.and("displayName.displayName").as("displayName");

		final Aggregation aggregation = Aggregation.newAggregation(matchOperation, lookupStockDisplayName,
				Aggregation.unwind("$displayName", true), project);
		final AggregationResults<TStockEditHistoryDto> result = mongoTemplate.aggregate(aggregation, "t_stck_edt_hstry",
				TStockEditHistoryDto.class);
		return result.getMappedResults();
	}

}
