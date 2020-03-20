package com.nexware.aajapan.repositories.custom.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationExpression;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.TBankTransactionDto;
import com.nexware.aajapan.repositories.custom.TBankTransactionRepositoryCustom;
import com.nexware.aajapan.utils.AppUtil;

public class TBankTransactionRepositoryCustomImpl implements TBankTransactionRepositoryCustom {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public Double bankCurrentBalanceByBankIdAndCurrency(String bankid, int currency) {
		MatchOperation matchOperation = Aggregation
				.match(Criteria.where("bankId").is(bankid).and("currency").is(currency));
		AggregationExpression finalBalance = context -> {
			Document query = new Document();
			query.put("$cond",
					new Document("if",
							new Document("$eq", Arrays.<Object>asList("$transactionType", Constants.TRANSACTION_DEBIT)))
									.append("then", "$amount")
									.append("else", new Document("$multiply", Arrays.<Object>asList("$amount", -1))));

			return query;
		};
		GroupOperation groupOperationTotal = Aggregation.group("bankId", "currency").sum(finalBalance)
				.as("final_balance");
		Aggregation aggregation = Aggregation.newAggregation(matchOperation, groupOperationTotal);
		AggregationResults<Document> result = this.mongoTemplate.aggregate(aggregation, "t_bnk_trsnsctn",
				Document.class);
		return Double.valueOf(!AppUtil.isObjectEmpty(result.getUniqueMappedResult())
				? result.getUniqueMappedResult().get("final_balance").toString()
				: "0.0");

	}

	@Override
	public List<TBankTransactionDto> getBankBalanceAccountsTransactionList(String bank, Date fromDate, Date toDate) {
		List<Criteria> criterias = new ArrayList<>();
		if (!AppUtil.isObjectEmpty(bank)) {
			criterias.add(Criteria.where("bankId").is(bank));
		}
		if (!AppUtil.isObjectEmpty(fromDate) && !AppUtil.isObjectEmpty(toDate)) {
			criterias.add(Criteria.where("createdDate").gte(AppUtil.atStartOfDay(fromDate))
					.andOperator(Criteria.where("createdDate").lte(AppUtil.atEndOfDay(toDate))));
		}
		if (criterias.isEmpty()) {
			return new ArrayList<>();
		}
		Criteria matchCriteria = new Criteria();
		matchCriteria.andOperator(criterias.toArray(new Criteria[0]));

		MatchOperation match = Aggregation.match(matchCriteria);

		LookupOperation lookupMBank = LookupOperation.newLookup().from("m_bank").localField("bankId")
				.foreignField("bankSeq").as("mBank");
		SortOperation sort = Aggregation.sort(Direction.ASC, "createdDate");
		ProjectionOperation project = Aggregation.project().andInclude("createdDate", "transactionType", "amount",
				"balance", "clearingAccount", "closingBalance", "description").and("$mBank.bankName").as("bankName");
		Aggregation aggregation = Aggregation.newAggregation(match, lookupMBank, Aggregation.unwind("$mBank", true),
				sort, project);

		AggregationResults<TBankTransactionDto> result = this.mongoTemplate.aggregate(aggregation, "t_bnk_trsnsctn",
				TBankTransactionDto.class);
		return result.getMappedResults();
	}

	@Override
	public Double bankCurrentBankBalanceByBankIdAndCurrency(String bankid, int currency) {
		MatchOperation matchOperation = Aggregation
				.match(Criteria.where("bankId").is(bankid).and("currency").is(currency));

		GroupOperation groupOperationTotal = Aggregation.group("bankSeq", "yenBalance");
		Aggregation aggregation = Aggregation.newAggregation(matchOperation, groupOperationTotal);
		AggregationResults<Document> result = this.mongoTemplate.aggregate(aggregation, "m_bank", Document.class);
		return Double.valueOf(!AppUtil.isObjectEmpty(result.getUniqueMappedResult())
				? result.getUniqueMappedResult().get("yenBalance").toString()
				: "0.0");
	}

}
