package com.nexware.aajapan.repositories.custom.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;

import com.nexware.aajapan.dto.TForwardBookingDto;
import com.nexware.aajapan.repositories.custom.TForwardBookingRepositoryCustom;

public class TForwardBookingRepositoryCustomImpl implements TForwardBookingRepositoryCustom {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<TForwardBookingDto> fetchForwardBookingList() {

		LookupOperation lookupMasterCurrency = LookupOperation.newLookup().from("m_currency").localField("currency")
				.foreignField("currencySeq").as("currencyDetail");

		LookupOperation lookupbank = LookupOperation.newLookup().from("m_bank").localField("bank")
				.foreignField("bankSeq").as("bankDetail");

		ProjectionOperation project = Aggregation.project()
				.andInclude("id", "bookingDate", "closingDate", "amount", "currentExchangeRate", "bookingExchangeRate")
				.and("bank").as("bankId").and("currency").as("currencySeq").and("$currencyDetail.currency")
				.as("currency").and("$currencyDetail.symbol").as("symbol").and("$bankDetail.bankName").as("bank");

		Aggregation aggregation = Aggregation.newAggregation(lookupMasterCurrency,
				Aggregation.unwind("$currencyDetail", true), lookupbank, Aggregation.unwind("$bankDetail", true),
				project);

		AggregationResults<TForwardBookingDto> result = this.mongoTemplate.aggregate(aggregation, "t_frwd_bkng",
				TForwardBookingDto.class);
		return result.getMappedResults();
	}

}
