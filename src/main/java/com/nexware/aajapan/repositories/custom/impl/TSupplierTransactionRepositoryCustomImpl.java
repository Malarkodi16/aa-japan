package com.nexware.aajapan.repositories.custom.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.TSupplierTransactionDto;
import com.nexware.aajapan.models.TSupplierTransaction;
import com.nexware.aajapan.repositories.custom.TSupplierTransactionRepositoryCustom;
import com.nexware.aajapan.utils.AppUtil;

@Repository
public class TSupplierTransactionRepositoryCustomImpl implements TSupplierTransactionRepositoryCustom {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public Double getCreditBalanceAmount(String supplierCode) {
		MatchOperation matchOperation = Aggregation.match(Criteria.where("supplierCode").is(supplierCode));
		AggregationExpression finalBalance = context -> {
			Document query = new Document();
			query.put("$cond",
					new Document("if",
							new Document("$eq", Arrays.<Object>asList("$transactionType", Constants.TRANSACTION_DEBIT)))
									.append("then", "$amount")
									.append("else", new Document("$multiply", Arrays.<Object>asList("$amount", -1))));

			return query;
		};
		GroupOperation groupOperationTotal = Aggregation.group("supplierCode").sum(finalBalance).as("final_balance");
		Aggregation aggregation = Aggregation.newAggregation(matchOperation, groupOperationTotal);
		AggregationResults<Document> result = this.mongoTemplate.aggregate(aggregation, "t_supplr_trsnsctn",
				Document.class);
		return Double.valueOf(!AppUtil.isObjectEmpty(result.getUniqueMappedResult())
				? result.getUniqueMappedResult().get("final_balance").toString()
				: "0.0");
	}

	@Override
	public List<TSupplierTransactionDto> findTransactionsBySupplierAndDate(String supplier, Date from, Date to) {
		List<Criteria> criterias = new ArrayList<>();
		criterias.add(Criteria.where("supplierCode").is(supplier));
		criterias.add(Criteria.where("createdDate").gte(AppUtil.atStartOfDay(from)).lte(AppUtil.atEndOfDay(to)));
		MatchOperation matchOperation = Aggregation
				.match(new Criteria().andOperator(criterias.toArray(new Criteria[0])));
		LookupOperation lookupOperation = Aggregation.lookup("t_stck", "stockNo", "stockNo", "stockDetails");
		ProjectionOperation projectionOperation = Aggregation
				.project("stockNo", "supplierCode", "invoiceNo", "transactionType", "amount", "balance",
						"closingBalance")
				.and("stockDetails.chassisNo").as("chassisNo").and("stockDetails.maker").as("maker")
				.and("stockDetails.model").as("model").and("$createdDate").as("date");
		SortOperation sortOperation = Aggregation.sort(Sort.Direction.ASC, "date");
		Aggregation aggregation = Aggregation.newAggregation(matchOperation, lookupOperation,
				Aggregation.unwind("stockDetails", true), projectionOperation, sortOperation);

		AggregationResults<TSupplierTransactionDto> result = this.mongoTemplate.aggregate(aggregation,
				TSupplierTransaction.class, TSupplierTransactionDto.class);
		return result.getMappedResults();
	}

}
