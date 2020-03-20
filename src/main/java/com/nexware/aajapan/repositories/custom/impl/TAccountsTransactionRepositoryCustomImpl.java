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
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.TAccountsTransactionDto;
import com.nexware.aajapan.repositories.custom.TAccountsTransactionRepositoryCustom;
import com.nexware.aajapan.utils.AppUtil;

public class TAccountsTransactionRepositoryCustomImpl implements TAccountsTransactionRepositoryCustom {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<TAccountsTransactionDto> getTrailBalanceAccountsTransactionList(Date fromDate, Date toDate,
			Long subAccount) {

		List<Criteria> criterias = new ArrayList<>();
		if (!AppUtil.isObjectEmpty(subAccount)) {
			criterias.add(Criteria.where("code").is(subAccount));
		}
		if (!AppUtil.isObjectEmpty(fromDate) && !AppUtil.isObjectEmpty(toDate)) {
			criterias.add(
					Criteria.where("createdDate").gte(AppUtil.atStartOfDay(fromDate)).lte(AppUtil.atEndOfDay(toDate)));
		}
		if (criterias.isEmpty()) {
			criterias.add(Criteria.where("createdDate").gte(AppUtil.addDays(new Date(), -30)));
		}
		Criteria criteria = new Criteria();
		if (!criterias.isEmpty()) {
			criteria.andOperator(criterias.toArray(new Criteria[0]));
		}
		MatchOperation dateRangeMatch = Aggregation.match(criteria);
		LookupOperation lookupMCoa = LookupOperation.newLookup().from("m_coa").localField("code").foreignField("code")
				.as("mCoa");
		SortOperation sort = Aggregation.sort(Direction.ASC, "createdDate");
		ProjectionOperation project = Aggregation.project()
				.andInclude("code", "createdDate", "type", "amount", "balance", "description", "refInvoiceNo",
						"remitTo", "transactionId")
				.and("$mCoa.account").as("accountName").and("$mCoa.subAccount").as("subAccount");
		Aggregation aggregation = Aggregation.newAggregation(dateRangeMatch, lookupMCoa,
				Aggregation.unwind("$mCoa", true), sort, project);

		AggregationResults<TAccountsTransactionDto> result = this.mongoTemplate.aggregate(aggregation, "t_acnt_trnsctn",
				TAccountsTransactionDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<TAccountsTransactionDto> findAllByCode(Long code, Date fromDate, Date toDate) {

		Criteria subAccount = new Criteria();

		if (!AppUtil.isObjectEmpty(code)) {
			subAccount = new Criteria().andOperator(Criteria.where("code").is(code),
					Criteria.where("createdDate").gte(AppUtil.atStartOfDay(fromDate)),
					Criteria.where("createdDate").lte(AppUtil.atEndOfDay(toDate)));
		}

		MatchOperation subAccountMatch = Aggregation.match(subAccount);

		LookupOperation lookupMCoa = LookupOperation.newLookup().from("m_coa").localField("code").foreignField("code")
				.as("mCoa");
		ProjectionOperation project = Aggregation.project()
				.andInclude("id", "code", "createdDate", "type", "amount", "balance", "source", "currency",
						"closingBalance", "exchangeRate", "category", "transactionId", "refInvoiceNo")
				.and("$mCoa.account").as("accountName").and("$mCoa.subAccount").as("subAccount");
		SortOperation sort = Aggregation.sort(Direction.ASC, "createdDate");
		Aggregation aggregation = Aggregation.newAggregation(subAccountMatch, lookupMCoa,
				Aggregation.unwind("$mCoa", true), sort, project);

		AggregationResults<TAccountsTransactionDto> result = this.mongoTemplate.aggregate(aggregation, "t_acnt_trnsctn",
				TAccountsTransactionDto.class);
		return result.getMappedResults();

	}

	@Override
	public List<Document> getTrailBalance(Date toDate) {
		Date fromDate = AppUtil.startDateOfMonth(toDate);
		MatchOperation match = Aggregation.match(Criteria.where("createdDate").gte(AppUtil.atStartOfDay(fromDate))
				.andOperator(Criteria.where("createdDate").lte(AppUtil.atEndOfDay(toDate))));
		Document groupQuery = new Document();
		Document totalDebit = new Document("$sum", new Document("$cond", Arrays.<Object>asList(
				new Document("$eq", Arrays.<Object>asList("$type", Constants.TRANSACTION_DEBIT)), "$amount", 0)));
		Document totalCredit = new Document("$sum", new Document("$cond", Arrays.<Object>asList(
				new Document("$eq", Arrays.<Object>asList("$type", Constants.TRANSACTION_CREDIT)), "$amount", 0)));

		groupQuery.put("$group",
				new Document("_id", "$code").append("openingBalance", new Document("$first", "$closingBalance"))
						.append("totalDebit", totalDebit).append("totalCredit", totalCredit));
		AggregationOperation group = context -> groupQuery;
		Document projectQuery = new Document();
		Document obDebit = new Document("$cond",
				Arrays.<Object>asList(new Document("$lt", Arrays.<Object>asList("$openingBalance", 0)),
						new Document("$multiply", Arrays.<Object>asList("$openingBalance", -1)), 0));
		Document obCredit = new Document("$cond", Arrays.<Object>asList(
				new Document("$gt", Arrays.<Object>asList("$openingBalance", 0)), "$openingBalance", 0));
		Document debitTotal = new Document("$add", Arrays.<Object>asList(obDebit, "$totalDebit"));
		Document creditTotal = new Document("$add", Arrays.<Object>asList(obCredit, "$totalCredit"));
		Document closingBalance = new Document("$subtract", Arrays.<Object>asList(debitTotal, creditTotal));
		Document cbCredit = new Document("$cond",
				Arrays.<Object>asList(new Document("$lt", Arrays.<Object>asList(closingBalance, 0)),
						new Document("$multiply", Arrays.<Object>asList(closingBalance, -1)), 0));
		Document cbDebit = new Document("$cond", Arrays
				.<Object>asList(new Document("$gt", Arrays.<Object>asList(closingBalance, 0)), closingBalance, 0));
		projectQuery.put("$project",
				new Document("accountNo", "$_id").append("openingBalance", "$openingBalance").append("obDebit", obDebit)
						.append("obCredit", obCredit).append("debit", "$totalDebit").append("credit", "$totalCredit")
						.append("cbDebit", cbDebit).append("cbCredit", cbCredit)
						.append("closingBalance", closingBalance).append("description", "$mCoa.subAccount"));
		LookupOperation lookupMCoa = LookupOperation.newLookup().from("m_coa").localField("_id").foreignField("code")
				.as("mCoa");
		AggregationOperation projectOperation = context -> projectQuery;
		SortOperation sort = Aggregation.sort(Direction.ASC, "accountNo");
		Aggregation aggregation = Aggregation.newAggregation(match, group, lookupMCoa,
				Aggregation.unwind("$mCoa", true), projectOperation, sort);
		AggregationResults<Document> result = this.mongoTemplate.aggregate(aggregation, "t_acnt_trnsctn",
				Document.class);
		return result.getMappedResults();
	}

}
