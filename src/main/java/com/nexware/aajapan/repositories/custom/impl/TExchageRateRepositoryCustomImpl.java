package com.nexware.aajapan.repositories.custom.impl;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import com.mongodb.BasicDBObject;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.TExchangeRateDto;
import com.nexware.aajapan.dto.TLoanRepaymentDto;
import com.nexware.aajapan.models.TExchangeRate;
import com.nexware.aajapan.repositories.custom.TExchageRateRepositoryCustom;
import com.nexware.aajapan.utils.AppUtil;

public class TExchageRateRepositoryCustomImpl implements TExchageRateRepositoryCustom {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<TExchangeRateDto> getAllList() {

		final LookupOperation lookupCurrency = LookupOperation.newLookup().from("m_currency").localField("currency")
				.foreignField("currencySeq").as("currencyDetails");

//		final GroupOperation groupOperation = Aggregation.group("createdDate").first("createdDate").as("createdDate")
//				.first("createdBy").as("createdBy")
//				.push(new BasicDBObject("currency", "$currencyDetails.currency")
//						.append("symbol", "$currencyDetails.symbol").append("exchangeRate", "$exchangeRate")
//						.append("salesExchangeRate", "$salesExchangeRate")
//						.append("specialExchangeRate", "$specialExchangeRate"))
//				.as("groupItems");

		final Map<String, Object> query = new LinkedHashMap<>();
		final Document groupBy = new Document("createdDate",
				new Document("$dateToString", new Document("format", "%Y-%m-%d").append("date", "$createdDate")));
		final Document groupItems = new Document("$push",
				new BasicDBObject("currency", "$currencyDetails.currency").append("symbol", "$currencyDetails.symbol")
						.append("exchangeRate", "$exchangeRate").append("salesExchangeRate", "$salesExchangeRate")
						.append("specialExchangeRate", "$specialExchangeRate"));
		query.put("_id", groupBy);
		query.put("groupItems", groupItems);
		query.put("createdDate", new Document("$first", "$createdDate"));
		query.put("createdBy", new Document("$first", "$createdBy"));

		final AggregationOperation groupAggregationOperation = context -> new Document("$group", new Document(query));

		Aggregation aggregation = Aggregation.newAggregation(lookupCurrency,
				Aggregation.unwind("currencyDetails", true), groupAggregationOperation);

		AggregationResults<TExchangeRateDto> result = this.mongoTemplate.aggregate(aggregation, "t_exhg_rate",
				TExchangeRateDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<TExchangeRate> findTop3ByOrderByCreatedDate(Date remitDate) {
		MatchOperation match = Aggregation.match(
				Criteria.where("createdDate").gte(AppUtil.atStartOfDay(remitDate)).lt(AppUtil.atEndOfDay(remitDate)));
		SortOperation sort = Aggregation.sort(Direction.DESC, "createdDate");
		LimitOperation limit = Aggregation.limit(3);
		ProjectionOperation project = Aggregation.project().andInclude("id", "currency", "exchangeRate",
				"salesExchangeRate", "specialExchangeRate", "createdDate");
		Aggregation aggregation = Aggregation.newAggregation(match, project, sort, limit);
		AggregationResults<TExchangeRate> result = this.mongoTemplate.aggregate(aggregation, "t_exhg_rate",
				TExchangeRate.class);
		return result.getMappedResults();
	}

	@Override
	public TExchangeRate findTopOneByOrderByCreatedDate(Date remitDate, Integer currency) {
		MatchOperation match = Aggregation.match(new Criteria().andOperator(
				Criteria.where("createdDate").gte(AppUtil.atStartOfDay(remitDate)).lt(AppUtil.atEndOfDay(remitDate)),
				Criteria.where("currency").is(currency)));
		SortOperation sort = Aggregation.sort(Direction.DESC, "createdDate");
		LimitOperation limit = Aggregation.limit(1);
		ProjectionOperation project = Aggregation.project().andInclude("id", "currency", "exchangeRate",
				"salesExchangeRate", "specialExchangeRate", "createdDate");
		Aggregation aggregation = Aggregation.newAggregation(match, project, sort, limit);
		AggregationResults<TExchangeRate> result = this.mongoTemplate.aggregate(aggregation, "t_exhg_rate",
				TExchangeRate.class);
		return result.getUniqueMappedResult();
	}

}
