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

import com.nexware.aajapan.dto.MForwarderDetailDto;
import com.nexware.aajapan.models.MForwarderDetail;
import com.nexware.aajapan.repositories.custom.MForwarderDetailRepositoryCustom;

public class MForwarderDetailRepositoryCustomImpl implements MForwarderDetailRepositoryCustom {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<MForwarderDetailDto> findAllFwdrByOrginAndDestination(String orginPort, String destPort) {
		final MatchOperation match = Aggregation.match(new Criteria()
				.andOperator(Criteria.where("orginPort").is(orginPort), Criteria.where("destPort").is(destPort)));
		final LookupOperation lookupForwarder = LookupOperation.newLookup().from("m_frwrdr").localField("forwarderId")
				.foreignField("code").as("forwarder");
		final ProjectionOperation project = Aggregation.project()
				.andInclude("id", "forwarderId", "orginCountry", "orginPort", "destCountry", "destPort", "freightUSD",
						"freightCharge", "shippingCharge", "inspectionCharge", "radiationCharge")
				.and("forwarder.name").as("forwarderName");
		final Aggregation aggregation = Aggregation.newAggregation(match, lookupForwarder,
				Aggregation.unwind("$forwarder", true), project);
		final AggregationResults<MForwarderDetailDto> result = mongoTemplate.aggregate(aggregation,
				MForwarderDetail.class, MForwarderDetailDto.class);
		return result.getMappedResults();

	}

}
