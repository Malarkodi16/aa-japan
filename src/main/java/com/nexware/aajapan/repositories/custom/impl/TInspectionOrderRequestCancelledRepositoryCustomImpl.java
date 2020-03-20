package com.nexware.aajapan.repositories.custom.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import com.nexware.aajapan.dto.TStockMakerModelDto;
import com.nexware.aajapan.models.TInspectionOrderRequestCancelled;
import com.nexware.aajapan.repositories.custom.TInspectionOrderRequestCancelledRepositoryCustom;

public class TInspectionOrderRequestCancelledRepositoryCustomImpl
		implements TInspectionOrderRequestCancelledRepositoryCustom {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<TStockMakerModelDto> searchStockForReInspection(String search) {
//		final Criteria autionCriteria = Criteria.where("supplierId").is(auctionCompany);
//		final Criteria autionHouse = Criteria.where("auctionHouseId").is(new ObjectId(auctionHouse));

		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");

		GroupOperation group = Aggregation.group("$stockNo").first("$stockNo").as("stockNo").first("$stock.chassisNo")
				.as("chassisNo").first("$stock.maker").as("maker").first("$stock.model").as("model");

		final Criteria searchCriteria = new Criteria().orOperator(
				Criteria.where("stockNo").regex(".*" + search + ".*", "i"),
				Criteria.where("chassisNo").regex(".*" + search + ".*", "i"));
		final Criteria criteria = new Criteria().andOperator(searchCriteria);
		final MatchOperation match = Aggregation.match(criteria);
		final ProjectionOperation project = Aggregation.project().and("stockNo").as("stockNo").and("chassisNo")
				.as("chassisNo").and("maker").as("maker").and("model").as("model");

		final Aggregation aggregation = Aggregation.newAggregation(lookupStock, Aggregation.unwind("$stock", true),
				group, match, project);
		final AggregationResults<TStockMakerModelDto> result = mongoTemplate.aggregate(aggregation,
				"t_inspctn_odr_rqst_cancld", TStockMakerModelDto.class);

		return result.getMappedResults();
	}
}
