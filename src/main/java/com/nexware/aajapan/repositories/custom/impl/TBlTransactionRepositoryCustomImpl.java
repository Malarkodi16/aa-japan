package com.nexware.aajapan.repositories.custom.impl;

import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import com.nexware.aajapan.dto.BlTransactionListDto;
import com.nexware.aajapan.repositories.custom.TBlTransactionRepositoryCustom;

public class TBlTransactionRepositoryCustomImpl implements TBlTransactionRepositoryCustom {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<BlTransactionListDto> getBLListTransaction(String shippingInstructionId) {
		MatchOperation match = Aggregation.match(Criteria.where("shippingInstructionId").is(shippingInstructionId));
		final LookupOperation lookupCustomer = LookupOperation.newLookup().from("t_cstmr").localField("customerId")
				.foreignField("code").as("customerInfo");

		final AggregationOperation addConsigneeField = context -> new Document("$addFields", new Document("consignee",
				new Document("$filter",
						new Document("input", "$customerInfo.consigneeNotifyparties").append("as", "result")
								.append("cond", new Document("$eq", Arrays.asList("$$result._id", "$consigneeId"))))));
		final ProjectionOperation project = Aggregation.project()
				.andInclude("shippingInstructionId", "createdDate", "createdBy").and("customerInfo.firstName")
				.as("customer").and("consignee.cFirstName").as("consignee");
		final Aggregation aggregation = Aggregation.newAggregation(match, lookupCustomer,
				Aggregation.unwind("$customerInfo", true), addConsigneeField, Aggregation.unwind("$consignee", true),
				project);// Aggregation.match(Criteria.where("status").is(Constants.STOCK_STATUS_SOLD)),
		final AggregationResults<BlTransactionListDto> result = mongoTemplate.aggregate(aggregation, "t_bl_trnsctn",
				BlTransactionListDto.class);
		return result.getMappedResults();
	}

}
