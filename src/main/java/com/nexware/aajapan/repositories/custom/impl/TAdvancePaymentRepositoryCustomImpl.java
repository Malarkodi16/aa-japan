package com.nexware.aajapan.repositories.custom.impl;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.CountOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import com.nexware.aajapan.repositories.custom.TAdvancePaymentRepositoryCustom;
import com.nexware.aajapan.utils.AppUtil;

public class TAdvancePaymentRepositoryCustomImpl implements TAdvancePaymentRepositoryCustom {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public Long getCountOnAdvanceBooking(Integer paymentApprove) {
		MatchOperation match = Aggregation.match(Criteria.where("paymentApprove").is(paymentApprove));
		CountOperation count = Aggregation.count().as("count");
		Aggregation aggregation = Aggregation.newAggregation(match, count);
		AggregationResults<Document> result = this.mongoTemplate.aggregate(aggregation, "t_adv_pymnt", Document.class);
		return Long.valueOf(!AppUtil.isObjectEmpty(result.getUniqueMappedResult())
				? result.getUniqueMappedResult().get("count").toString()
				: "0");
	}

}
