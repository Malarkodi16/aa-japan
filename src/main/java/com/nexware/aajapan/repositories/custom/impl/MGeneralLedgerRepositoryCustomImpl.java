package com.nexware.aajapan.repositories.custom.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AccumulatorOperators;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationExpression;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.BasicDBObject;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.GlReportDetails;
import com.nexware.aajapan.exceptions.AAJRuntimeException;
import com.nexware.aajapan.models.MGeneralLedger;
import com.nexware.aajapan.repositories.custom.MGeneralLedgerRepositoryCustom;
import com.nexware.aajapan.utils.AppUtil;

public class MGeneralLedgerRepositoryCustomImpl implements MGeneralLedgerRepositoryCustom {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public MGeneralLedger getNextSequenceById(int id) {
		// get sequence id
		Query query = new Query(Criteria.where("ledgerId").is(id));
		// increase sequence id by 1
		Update update = new Update();
		update.inc("sequence", 1);
		// return new increased id
		FindAndModifyOptions options = new FindAndModifyOptions();
		options.returnNew(true);

		MGeneralLedger generalLedger = this.mongoTemplate.findAndModify(query, update, options, MGeneralLedger.class);
		// if no id, throws SequenceException
		// optional, just a way to tell user when the sequence id is failed to generate.
		if (AppUtil.isObjectEmpty(generalLedger)) {
			throw new AAJRuntimeException("Unable to get sequence id for key(General Ledger) : " + id);
		}
		return generalLedger;
	}

	@Override
	public List<GlReportDetails> getTransactions() {
		LookupOperation lookupOperation = Aggregation.lookup("m_coa", "ledgerId", "generalLedger", "coa_details");
		ProjectionOperation projectionOperation = Aggregation.project("name", "order", "coa_details")
				.and(AccumulatorOperators.Sum.sumOf("coa_details.balance")).as("total");
		SortOperation sortOperation = Aggregation.sort(Direction.ASC, "order");
		Aggregation aggregation = Aggregation.newAggregation(lookupOperation, projectionOperation, sortOperation);
		AggregationResults<GlReportDetails> result = this.mongoTemplate.aggregate(aggregation, MGeneralLedger.class,
				GlReportDetails.class);
		return result.getMappedResults();
	}

	@Override
	public List<GlReportDetails> getTransactions(Date from, Date to) {
		MatchOperation matchOperation = Aggregation
				.match(Criteria.where("createdDate").gte(AppUtil.atStartOfDay(from)).lte(AppUtil.atEndOfDay(to)));
		AggregationExpression finalBalance = context -> {
			Document query = new Document();
			query.put("$cond",
					new Document("if", new Document("$eq", Arrays.<Object>asList("$type", Constants.TRANSACTION_DEBIT)))
							.append("then", "$amount")
							.append("else", new Document("$multiply", Arrays.<Object>asList("$amount", -1))));

			return query;
		};
		GroupOperation groupOperationBalance = Aggregation.group("code").first("code").as("code").sum(finalBalance)
				.as("finalBalance");
		LookupOperation lookupOperation = Aggregation.lookup("m_coa", "code", "code", "coa");
		GroupOperation groupOperationGenaralLedger = Aggregation.group("$coa.generalLedger").first("coa.generalLedger")
				.as("ledgerId")
				.push(new BasicDBObject("reportingCategory", "$coa.reportingCategory").append("account", "$coa.account")
						.append("code", "$coa.code").append("subAccount", "$coa.subAccount")
						.append("balance", "$finalBalance"))
				.as("coa_details");
		LookupOperation lookupOperationGenaralLedger = Aggregation.lookup("m_gnrl_ldgr", "ledgerId", "ledgerId",
				"genaral_ledger");
		ProjectionOperation projectionOperation = Aggregation.project("coa_details").and("genaral_ledger.name")
				.as("name").and("genaral_ledger.order").as("order")
				.and(AccumulatorOperators.Sum.sumOf("coa_details.balance")).as("total");
		SortOperation sortOperation = Aggregation.sort(Direction.ASC, "order");
		Aggregation aggregation = Aggregation.newAggregation(matchOperation, groupOperationBalance, lookupOperation,
				Aggregation.unwind("$coa", true), groupOperationGenaralLedger, lookupOperationGenaralLedger,
				Aggregation.unwind("$genaral_ledger", true), projectionOperation, sortOperation);
		AggregationResults<GlReportDetails> result = this.mongoTemplate.aggregate(aggregation, "t_acnt_trnsctn",
				GlReportDetails.class);

		return result.getMappedResults();
	}

}
