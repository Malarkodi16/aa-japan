package com.nexware.aajapan.repositories.custom.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators.Filter;
import org.springframework.data.mongodb.core.aggregation.ComparisonOperators.Eq;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.TTransporterChargeDto;
import com.nexware.aajapan.dto.TTransporterFeeDto;
import com.nexware.aajapan.models.TTransporterFee;
import com.nexware.aajapan.repositories.custom.TTransporterFeeRepositoryCustom;

public class TTransporterFeeRepositoryCustomImpl implements TTransporterFeeRepositoryCustom {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<TTransporterChargeDto> findByLocation(String from, String transportCategory, String to) {
		MatchOperation match = Aggregation.match(new Criteria().andOperator(Criteria.where("from").is(from),
				Criteria.where("to").is(to), Criteria.where("transportCategory").in(Arrays.asList(transportCategory)),
				Criteria.where("deleteFlag").is(Constants.DELETE_FLAG_0)));
		LookupOperation lookupTransporter = LookupOperation.newLookup().from("m_trnsprtr").localField("transporter")
				.foreignField("code").as("transport");

		ProjectionOperation project = Aggregation.project().andInclude("_id", "amount").and("$transport.code")
				.as("code").and("$transport.name").as("name");
		SortOperation sort = Aggregation.sort(Direction.ASC, "amount");
		Aggregation aggregation = Aggregation.newAggregation(match, lookupTransporter,
				Aggregation.unwind("$transport", true), project, sort);
		AggregationResults<TTransporterChargeDto> result = this.mongoTemplate.aggregate(aggregation, "t_trnsprtr_fee",
				TTransporterChargeDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<TTransporterFeeDto> getListBeforeDeleteAndWithoutCode() {
		MatchOperation matchOperation = Aggregation
				.match(new Criteria().andOperator((Criteria.where("deleteFlag").is(Constants.DELETE_FLAG_0))));
		LookupOperation lookupMTransporter = LookupOperation.newLookup().from("m_trnsprtr").localField("transporter")
				.foreignField("code").as("masterTransporter");
		LookupOperation lookupFromMLocation = LookupOperation.newLookup().from("m_lctn").localField("from")
				.foreignField("code").as("masterFromLocation");
		LookupOperation lookupToMLocation = LookupOperation.newLookup().from("m_lctn").localField("to")
				.foreignField("code").as("masterToLocation");
		ProjectionOperation project = Aggregation.project()
				.andInclude("id", "amount", "categories", "deleteFlag", "transportCategory").and("transporter")
				.as("transporterCode").and("from").as("fromCode").and("to").as("toCode").and("$masterTransporter.name")
				.as("transporter")
				.and(Filter.filter("$masterFromLocation").as("result")
						.by(Eq.valueOf("$$result.code").equalToValue("$from")))
				.as("fromLocation").and(Filter.filter("$masterToLocation").as("result")
						.by(Eq.valueOf("$$result.code").equalToValue("$to")))
				.as("toLocation");
		ProjectionOperation projectAfterFilter = Aggregation.project().and("$id").as("id").and("$amount").as("amount")
				.and("$deleteFlag").as("deleteFlag").and("$transportCategory").as("transportCategory")
				.and("$transporterCode").as("transporterCode").and("$fromCode").as("fromCode").and("$toCode")
				.as("toCode").and("$transporter").as("transporter").and("$categories").as("categories")
				.and("$fromLocation.displayName").as("from").and("$toLocation.displayName").as("to");

		Aggregation aggregation = Aggregation.newAggregation(matchOperation, lookupMTransporter,
				Aggregation.unwind("$masterTransporter", true), lookupFromMLocation, lookupToMLocation, project,
				projectAfterFilter);
		AggregationResults<TTransporterFeeDto> result = this.mongoTemplate.aggregate(aggregation, "t_trnsprtr_fee",
				TTransporterFeeDto.class);
		return result.getMappedResults();
	}

	@Override
	public boolean isExist(String transporter, String from, String to, List<String> categories, int deleteFlag) {
		Criteria criteria = new Criteria().andOperator(Criteria.where("transporter").is(transporter),
				Criteria.where("from").is(from), Criteria.where("to").is(to),
				Criteria.where("deleteFlag").is(deleteFlag), Criteria.where("categories").in(categories));
		return this.mongoTemplate.exists(Query.query(criteria), TTransporterFee.class);
	}

	@Override
	public boolean isExist(String id, String transporter, String from, String to, List<String> categories,
			int deleteFlag) {
		Criteria criteria = new Criteria().andOperator(Criteria.where("id").is(id),
				Criteria.where("transporter").is(transporter), Criteria.where("from").is(from),
				Criteria.where("to").is(to), Criteria.where("deleteFlag").is(deleteFlag),
				Criteria.where("categories").in(categories));
		return this.mongoTemplate.exists(Query.query(criteria), TTransporterFee.class);
	}
}
