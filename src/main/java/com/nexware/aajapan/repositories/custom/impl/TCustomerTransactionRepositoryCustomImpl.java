package com.nexware.aajapan.repositories.custom.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationExpression;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.CustomerTransactionInMonthDto;
import com.nexware.aajapan.repositories.custom.TCustomerTransactionRepositoryCustom;
import com.nexware.aajapan.utils.AppUtil;

public class TCustomerTransactionRepositoryCustomImpl implements TCustomerTransactionRepositoryCustom {
	@Autowired
	MongoTemplate mongoTemplate;

	@Override
	public Double getAmountReceivedForStockByCustomer(String customerId, String stockNo) {
		MatchOperation matchOperation = Aggregation
				.match(Criteria.where("customerId").is(customerId).and("stockNo").is(stockNo));
		AggregationExpression amount = context -> {
			Document query = new Document();
			query.put("$cond",
					new Document("if",
							new Document("$eq",
									Arrays.<Object>asList("$transactionType", Constants.TRANSACTION_CREDIT)))
											.append("then", "$amount").append("else",
													new Document("$multiply", Arrays.<Object>asList("$amount", -1))));

			return query;
		};
		GroupOperation groupOperation = Aggregation.group("customerId", "stockNo").sum(amount).as("amount");
		Aggregation aggregation = Aggregation.newAggregation(matchOperation, groupOperation);
		AggregationResults<Document> result = this.mongoTemplate.aggregate(aggregation, "t_cstmr_stck_trnsctn",
				Document.class);
		return Double.valueOf(!AppUtil.isObjectEmpty(result.getUniqueMappedResult())
				? result.getUniqueMappedResult().get("amount").toString()
				: "0.0");
	}

	@Override
	public Double getBroughtForwardAmountForCustomer(String customerId, Date date) {
		MatchOperation matchOperation = Aggregation.match(Criteria.where("customerId").is(customerId).andOperator(
				Criteria.where("createdDate").lt(AppUtil.startDateOfMonth(date)),
				Criteria.where("transactionType").is(Constants.TRANSACTION_CREDIT)));
		AggregationExpression amount = context -> {
			Document query = new Document();
			query.put("$cond",
					new Document("if",
							new Document("$eq", Arrays.<Object>asList("$transactionType", Constants.TRANSACTION_DEBIT)))
									.append("then", "$amount")
									.append("else", new Document("$multiply", Arrays.<Object>asList("$amount", -1))));

			return query;
		};
		GroupOperation groupOperation = Aggregation.group("customerId").sum(amount).as("amount");
		Aggregation aggregation = Aggregation.newAggregation(matchOperation, groupOperation);
		AggregationResults<Document> result = this.mongoTemplate.aggregate(aggregation, "t_cstmr_stck_trnsctn",
				Document.class);
		return Double.valueOf(!AppUtil.isObjectEmpty(result.getUniqueMappedResult())
				? result.getUniqueMappedResult().get("amount").toString()
				: "0.0");
	}

	@Override
	public Double getpaymentAmountForCustomer(String customerId, Date date) {
		MatchOperation matchOperation = Aggregation.match(Criteria.where("customerId").is(customerId).andOperator(
				Criteria.where("createdDate").gte(AppUtil.startDateOfMonth(date)).lte(AppUtil.endDateOfMonth(date)),
				Criteria.where("transactionType").is(Constants.TRANSACTION_CREDIT)));
		AggregationExpression amount = context -> {
			Document query = new Document();
			query.put("$cond",
					new Document("if",
							new Document("$eq", Arrays.<Object>asList("$transactionType", Constants.TRANSACTION_DEBIT)))
									.append("then", "$amount")
									.append("else", new Document("$multiply", Arrays.<Object>asList("$amount", -1))));

			return query;
		};
		GroupOperation groupOperation = Aggregation.group("customerId").sum(amount).as("amount");
		Aggregation aggregation = Aggregation.newAggregation(matchOperation, groupOperation);
		AggregationResults<Document> result = this.mongoTemplate.aggregate(aggregation, "t_cstmr_stck_trnsctn",
				Document.class);
		return Double.valueOf(!AppUtil.isObjectEmpty(result.getUniqueMappedResult())
				? result.getUniqueMappedResult().get("amount").toString()
				: "0.0");
	}

	@Override
	public List<CustomerTransactionInMonthDto> getMonthlyTransaction(String customerId, Date date) {
		MatchOperation matchOperation = Aggregation.match(Criteria.where("customerId").is(customerId).andOperator(
				Criteria.where("createdDate").gte(AppUtil.startDateOfMonth(date)).lte(AppUtil.endDateOfMonth(date)),
				Criteria.where("transactionType").is(Constants.TRANSACTION_CREDIT)));

		final LookupOperation lookupCurrency = LookupOperation.newLookup().from("m_currency").localField("currency")
				.foreignField("currencySeq").as("currencyDtls");

		ProjectionOperation project = Aggregation.project().andInclude("createdDate", "amount")
				.and("currencyDtls.symbol").as("currencySymbol");
		Aggregation aggregation = Aggregation.newAggregation(matchOperation, lookupCurrency,
				Aggregation.unwind("$currencyDtls", true), project);
		AggregationResults<CustomerTransactionInMonthDto> result = this.mongoTemplate.aggregate(aggregation,
				"t_cstmr_stck_trnsctn", CustomerTransactionInMonthDto.class);
		return result.getMappedResults();
	}
}
