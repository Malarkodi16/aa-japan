package com.nexware.aajapan.repositories.custom.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;

import com.nexware.aajapan.dto.MContinentDto;
import com.nexware.aajapan.repositories.custom.MasterContinentRepositoryCustom;

public class MasterContinentRepositoryCustomImpl implements MasterContinentRepositoryCustom {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<MContinentDto> getCountryWithContinent() {
		LookupOperation lookupCountryPort = LookupOperation.newLookup().from("m_cntry_prt").localField("code")
				.foreignField("continent").as("items");
		Aggregation aggregation = Aggregation.newAggregation(lookupCountryPort);
		AggregationResults<MContinentDto> result = this.mongoTemplate.aggregate(aggregation, "m_cntnnt",
				MContinentDto.class);
		return result.getMappedResults();
	}

}
