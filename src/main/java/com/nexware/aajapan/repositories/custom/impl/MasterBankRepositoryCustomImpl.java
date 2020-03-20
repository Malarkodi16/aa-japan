package com.nexware.aajapan.repositories.custom.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AccumulatorOperators;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import com.mongodb.BasicDBObject;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.MBankDto;
import com.nexware.aajapan.dto.MCurrencyDto;
import com.nexware.aajapan.dto.MForeignBankDto;
import com.nexware.aajapan.repositories.custom.MasterBankRepositoryCustom;
import com.nexware.aajapan.repositories.custom.MasterContinentRepositoryCustom;

public class MasterBankRepositoryCustomImpl implements MasterBankRepositoryCustom {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<MCurrencyDto> findAllByAccountType() {

		LookupOperation lookupBank = LookupOperation.newLookup().from("m_bank").localField("currencySeq")
				.foreignField("currencyType").as("bank");

		MatchOperation match = Aggregation
				.match(Criteria.where("bank.accountType").is(Constants.TYPE_FOREIGN_BANK_ACCOUNT));

		GroupOperation group = Aggregation.group("$currencySeq")
				.push(new BasicDBObject("bankSeq", "$bank.bankSeq").append("bankName", "$bank.bankName")
						.append("yenBalance", "$bank.yenBalance").append("coaCode", "$bank.coaCode")
						.append("clearingBalance", "$bank.clearingBalance").append("currencyType", "$bank.currencyType")
						.append("id", "$bank.id"))
				.as("bankDetails").first("symbol").as("symbol").first("currencySeq").as("currencySeq").first("currency")
				.as("currency");

		ProjectionOperation project = Aggregation.project()
				.andInclude("bankDetails", "symbol", "currencySeq", "currency")
				.and(AccumulatorOperators.Sum.sumOf("bankDetails.yenBalance")).as("totalAmount");

		Aggregation aggregation = Aggregation.newAggregation(lookupBank, Aggregation.unwind("$bank", true), match,
				group, project);
		AggregationResults<MCurrencyDto> result = this.mongoTemplate.aggregate(aggregation, "m_currency",
				MCurrencyDto.class);
		return result.getMappedResults();
	}
	
	@Override
	public List<MBankDto> findAllByAccountTypeBank() {

		MatchOperation match = Aggregation.match(Criteria.where("accountType").is(Constants.TYPE_FOREIGN_BANK_ACCOUNT));
		LookupOperation lookupBank = LookupOperation.newLookup().from("m_currency").localField("currencyType")
				.foreignField("currencySeq").as("currency");

		ProjectionOperation project = Aggregation.project()
				.andInclude("id", "bankSeq", "bankName", "currencyType", "yenBalance", "coaCode", "clearingBalance")
				.and("currency.symbol").as("currency");

		Aggregation aggregation = Aggregation.newAggregation(match, lookupBank, Aggregation.unwind("$currency", true),
				project);
		AggregationResults<MBankDto> result = this.mongoTemplate.aggregate(aggregation, "m_bank", MBankDto.class);
		return result.getMappedResults();
	}
	
	

}
