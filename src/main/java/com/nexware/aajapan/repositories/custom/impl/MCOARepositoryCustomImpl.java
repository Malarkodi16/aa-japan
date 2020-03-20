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
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import com.mongodb.BasicDBObject;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.BalanceAndPlDto;
import com.nexware.aajapan.dto.MCOABalanceStatementDto;
import com.nexware.aajapan.dto.MCOADto;
import com.nexware.aajapan.dto.MCOAProfitLossDto;
import com.nexware.aajapan.models.MCOA;
import com.nexware.aajapan.repositories.custom.MCOARepositoryCustom;
import com.nexware.aajapan.utils.AppUtil;

public class MCOARepositoryCustomImpl implements MCOARepositoryCustom {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<MCOA> findSubAccountTypes(String reportingCategory) {
		MatchOperation matchOperation = Aggregation
				.match(new Criteria().andOperator((Criteria.where("reportingCategory").is(reportingCategory))));
		GroupOperation group = Aggregation.group("$account").first("account").as("account").max("code").as("code");
		SortOperation sort = Aggregation.sort(Direction.ASC, "code");
		ProjectionOperation project = Aggregation.project().andInclude("account", "code");
		Aggregation aggregation = Aggregation.newAggregation(matchOperation, group, project, sort);
		AggregationResults<MCOA> result = this.mongoTemplate.aggregate(aggregation, "m_coa", MCOA.class);
		return result.getMappedResults();
	}

	@Override
	public List<MCOA> findAllByStatus(Integer status) {
		MatchOperation matchOperation = Aggregation
				.match(new Criteria().andOperator((Criteria.where("status").is(status))));
		ProjectionOperation project = Aggregation.project().andInclude("subAccount", "code", "account");
		Aggregation aggregation = Aggregation.newAggregation(matchOperation, project);
		AggregationResults<MCOA> result = this.mongoTemplate.aggregate(aggregation, "m_coa", MCOA.class);
		return result.getMappedResults();
	}

	@Override
	public List<MCOABalanceStatementDto> getBalanceStatementList(Date toDate) {
		Date fromDate = AppUtil.startDateOfMonth(toDate);
		List<Criteria> criterias = new ArrayList<>();
		if (!AppUtil.isObjectEmpty(toDate)) {
			criterias.add(
					Criteria.where("createdDate").gte(AppUtil.atStartOfDay(fromDate)).lte(AppUtil.atEndOfDay(toDate)));
		}
		Criteria criteria = new Criteria();
		if (!criterias.isEmpty()) {
			criteria.andOperator(criterias.toArray(new Criteria[0]));
		}
		MatchOperation dateRangeMatch = Aggregation.match(criteria);
		LookupOperation lookupMCoa = LookupOperation.newLookup().from("m_coa").localField("code").foreignField("code")
				.as("mCoa");
		MatchOperation matchOperation = Aggregation.match(new Criteria()
				.andOperator((Criteria.where("mCoa.reportFlag").is(Constants.BALANCE_SHEET_REPORT_FLAG))));

		List<MCOAProfitLossDto> plData = this.getProfitLossList(AppUtil.startDateOfMonth(toDate), toDate);
		Double plPtdAmount = plData.stream().mapToDouble(MCOAProfitLossDto::getPtdAmount).sum();
		Double plYtdAmount = plData.stream().mapToDouble(MCOAProfitLossDto::getYtdAmount).sum();

		AggregationOperation groupSubAccount = context -> new Document("$group",
				new Document("_id", "$mCoa.subAccount")
						.append("ptdAmount",
								new Document("$sum",
										new Document("$cond",
												new Document("if", new Document("$eq",
														Arrays.<Object>asList("$type", Constants.TRANSACTION_DEBIT)))
																.append("then", "$amount").append("else",
																		new Document("$multiply",
																				Arrays.<Object>asList("$amount",
																						-1))))))
						.append("account", new Document("$first", "$mCoa.account"))
						.append("subAccount", new Document("$first", "$mCoa.subAccount"))
						.append("code", new Document("$first", "$mCoa.code"))
						.append("ytdAmount", new Document("$first", "$mCoa.balance"))
						.append("balanceSheet", new Document("$first", "$mCoa.balanceSheet"))
						.append("plPtdAmount", new Document("$first", plPtdAmount * -1))
						.append("plYtdAmount", new Document("$first", plYtdAmount * -1)));

		GroupOperation groupAccount = Aggregation.group("$account")
				.push(new BasicDBObject("subAccount", "$subAccount").append("code", "$code")
						.append("ptdAmount", "$ptdAmount").append("ytdAmount", "$ytdAmount"))
				.as("items").first("account").as("account").first("code").as("code").first("balanceSheet")
				.as("balanceSheet").sum("ptdAmount").as("ptdAmount").sum("ytdAmount").as("ytdAmount")
				.first("plPtdAmount").as("plPtdAmount").first("plYtdAmount").as("plYtdAmount");

		SortOperation sortOperation = Aggregation.sort(Direction.ASC, "code", "balanceSheet");

		Aggregation aggregation = Aggregation.newAggregation(dateRangeMatch, lookupMCoa,
				Aggregation.unwind("$mCoa", true), matchOperation, groupSubAccount, groupAccount, sortOperation);

		AggregationResults<MCOABalanceStatementDto> result = this.mongoTemplate.aggregate(aggregation, "t_acnt_trnsctn",
				MCOABalanceStatementDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<MCOAProfitLossDto> getProfitLossList(Date fromDate, Date toDate) {
		List<Criteria> criterias = new ArrayList<>();
		if (!AppUtil.isObjectEmpty(fromDate) && !AppUtil.isObjectEmpty(toDate)) {
			criterias.add(
					Criteria.where("createdDate").gte(AppUtil.atStartOfDay(fromDate)).lte(AppUtil.atEndOfDay(toDate)));
		}
		Criteria criteria = new Criteria();
		if (!criterias.isEmpty()) {
			criteria.andOperator(criterias.toArray(new Criteria[0]));
		}
		MatchOperation dateRangeMatch = Aggregation.match(criteria);
		LookupOperation lookupMCoa = LookupOperation.newLookup().from("m_coa").localField("code").foreignField("code")
				.as("mCoa");

		AggregationExpression ptdBalance = context -> {
			Document query = new Document();
			query.put("$cond",
					new Document("if", new Document("$eq", Arrays.<Object>asList("$type", Constants.TRANSACTION_DEBIT)))
							.append("then", "$amount")
							.append("else", new Document("$multiply", Arrays.<Object>asList("$amount", -1))));

			return query;
		};
		MatchOperation matchOperation = Aggregation.match(
				new Criteria().andOperator((Criteria.where("mCoa.reportFlag").is(Constants.PROFIT_LOSS_REPORT_FLAG))));

		GroupOperation groupSubAccount = Aggregation.group("$mCoa.subAccount").sum(ptdBalance).as("ptdAmount")
				.first("$mCoa.account").as("account").first("$mCoa.subAccount").as("subAccount").first("$mCoa.code")
				.as("code").first("$mCoa.balance").as("ytdAmount").first("$mCoa.plOrder").as("plOrder");

		GroupOperation groupAccount = Aggregation.group("$account")
				.push(new BasicDBObject("subAccount", "$subAccount").append("code", "$code")
						.append("ptdAmount", "$ptdAmount").append("ytdAmount", "$ytdAmount"))
				.as("items").first("account").as("account").first("code").as("code").first("plOrder").as("plOrder")
				.sum("ptdAmount").as("ptdAmount").sum("ytdAmount").as("ytdAmount");

		SortOperation sortOperation = Aggregation.sort(Direction.ASC, "code", "plOrder");

		Aggregation aggregation = Aggregation.newAggregation(dateRangeMatch, lookupMCoa,
				Aggregation.unwind("$mCoa", true), matchOperation, groupSubAccount, groupAccount, sortOperation);

		AggregationResults<MCOAProfitLossDto> result = this.mongoTemplate.aggregate(aggregation, "t_acnt_trnsctn",
				MCOAProfitLossDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<MCOADto> findAllByReportFlag() {
		ProjectionOperation project = Aggregation.project().andInclude("id", "code", "account", "subAccount",
				"reportingCategory", "balanceSheet", "plOrder", "reportFlag");
		SortOperation sortOperation = Aggregation.sort(Direction.ASC, "code");
		Aggregation aggregation = Aggregation.newAggregation(project, sortOperation);

		AggregationResults<MCOADto> result = this.mongoTemplate.aggregate(aggregation, "m_coa", MCOADto.class);
		return result.getMappedResults();
	}

	@Override
	public List<BalanceAndPlDto> balanceStatementsearchData(Date toDate) {
		Date fromDate = AppUtil.startDateOfMonth(toDate);
		List<Criteria> criterias = new ArrayList<>();
		if (!AppUtil.isObjectEmpty(toDate)) {
			criterias.add(
					Criteria.where("createdDate").gte(AppUtil.atStartOfDay(fromDate)).lte(AppUtil.atEndOfDay(toDate)));
		}
		Criteria criteria = new Criteria();
		if (!criterias.isEmpty()) {
			criteria.andOperator(criterias.toArray(new Criteria[0]));
		}
		MatchOperation dateRangeMatch = Aggregation.match(criteria);
		LookupOperation lookupMCoa = LookupOperation.newLookup().from("m_coa").localField("code").foreignField("code")
				.as("mCoa");

		MatchOperation matchOperation = Aggregation.match(new Criteria()
				.andOperator((Criteria.where("mCoa.reportFlag").is(Constants.BALANCE_SHEET_REPORT_FLAG))));

		List<MCOAProfitLossDto> plData = this.getProfitLossList(AppUtil.startDateOfMonth(toDate), toDate);
		Double plYtdAmount = plData.stream().mapToDouble(MCOAProfitLossDto::getYtdAmount).sum();

		AggregationOperation groupSubAccount = context -> new Document("$group",
				new Document("_id", "$mCoa.subAccount").append("account", new Document("$first", "$mCoa.account"))
						.append("subAccount", new Document("$first", "$mCoa.subAccount"))
						.append("code", new Document("$first", "$mCoa.code"))
						.append("ytdAmount", new Document("$first", "$mCoa.balance"))
						.append("balanceSheet", new Document("$first", "$mCoa.balanceSheet"))
						.append("plYtdAmount", new Document("$first", plYtdAmount * -1)));

		GroupOperation groupAccount = Aggregation.group("$account")
				.push(new BasicDBObject("subAccount", "$subAccount").append("code", "$code").append("ytdAmount",
						"$ytdAmount"))
				.as("items").first("account").as("account").first("code").as("code").first("balanceSheet")
				.as("balanceSheet").first("plYtdAmount").as("plYtdAmount");

		ProjectionOperation project = Aggregation.project().andInclude("account", "plYtdAmount").and("items.code")
				.as("code").and("items.subAccount").as("subAccount").and("items.ytdAmount").as("ytdAmount");

		Aggregation aggregation = Aggregation.newAggregation(dateRangeMatch, lookupMCoa,
				Aggregation.unwind("$mCoa", true), matchOperation, groupSubAccount, groupAccount,
				Aggregation.unwind("$items", true), project);

		AggregationResults<BalanceAndPlDto> result = this.mongoTemplate.aggregate(aggregation, "t_acnt_trnsctn",
				BalanceAndPlDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<BalanceAndPlDto> profitAndLossSearchData(Date fromDate, Date toDate) {
		List<Criteria> criterias = new ArrayList<>();
		if (!AppUtil.isObjectEmpty(fromDate) && !AppUtil.isObjectEmpty(toDate)) {
			criterias.add(
					Criteria.where("createdDate").gte(AppUtil.atStartOfDay(fromDate)).lte(AppUtil.atEndOfDay(toDate)));
		}
		Criteria criteria = new Criteria();
		if (!criterias.isEmpty()) {
			criteria.andOperator(criterias.toArray(new Criteria[0]));
		}
		MatchOperation dateRangeMatch = Aggregation.match(criteria);
		LookupOperation lookupMCoa = LookupOperation.newLookup().from("m_coa").localField("code").foreignField("code")
				.as("mCoa");

		AggregationExpression ptdBalance = context -> {
			Document query = new Document();
			query.put("$cond",
					new Document("if", new Document("$eq", Arrays.<Object>asList("$type", Constants.TRANSACTION_DEBIT)))
							.append("then", "$amount")
							.append("else", new Document("$multiply", Arrays.<Object>asList("$amount", -1))));

			return query;
		};

		GroupOperation groupSubAccount = Aggregation.group("$mCoa.subAccount").sum(ptdBalance).as("ptdAmount")
				.first("$mCoa.account").as("account").first("$mCoa.subAccount").as("subAccount").first("$mCoa.code")
				.as("code").first("$mCoa.balance").as("ytdAmount").first("$mCoa.plOrder").as("plOrder");

		GroupOperation groupAccount = Aggregation.group("$account")
				.push(new BasicDBObject("subAccount", "$subAccount").append("code", "$code")
						.append("ptdAmount", "$ptdAmount").append("ytdAmount", "$ytdAmount"))
				.as("items").first("account").as("account").first("code").as("code").first("plOrder").as("plOrder")
				.sum("ptdAmount").as("ptdAmount").sum("ytdAmount").as("ytdAmount");

		ProjectionOperation project = Aggregation.project().andInclude("account").and("items.code").as("code")
				.and("items.subAccount").as("subAccount").and("items.ptdAmount").as("ptdAmount").and("items.ytdAmount")
				.as("ytdAmount").and("ptdAmount").as("totalPtdAmount").and("ytdAmount").as("totalYtdAmount");

		Aggregation aggregation = Aggregation.newAggregation(dateRangeMatch, lookupMCoa,
				Aggregation.unwind("$mCoa", true), groupSubAccount, groupAccount, Aggregation.unwind("$items", true),
				project);

		AggregationResults<BalanceAndPlDto> result = this.mongoTemplate.aggregate(aggregation, "t_acnt_trnsctn",
				BalanceAndPlDto.class);
		return result.getMappedResults();
	}

	@Override
	public Document getBalanceStatementByReportingType(String reportingCategory, Date toDate) {
		Date fromDate = AppUtil.startDateOfMonth(toDate);
		List<Criteria> criterias = new ArrayList<>();
		if (!AppUtil.isObjectEmpty(toDate)) {
			criterias.add(
					Criteria.where("createdDate").gte(AppUtil.atStartOfDay(fromDate)).lte(AppUtil.atEndOfDay(toDate)));
		}
		Criteria criteria = new Criteria();
		if (!criterias.isEmpty()) {
			criteria.andOperator(criterias.toArray(new Criteria[0]));
		}
		MatchOperation dateRangeMatch = Aggregation.match(criteria);
		LookupOperation lookupMCoa = LookupOperation.newLookup().from("m_coa").localField("code").foreignField("code")
				.as("mCoa");
		MatchOperation matchOperation = Aggregation.match(
				new Criteria().andOperator(Criteria.where("mCoa.reportFlag").is(Constants.BALANCE_SHEET_REPORT_FLAG),
						Criteria.where("mCoa.reportingCategory").is(reportingCategory)));

		GroupOperation group1 = Aggregation.group("$code").first("$mCoa.balance").as("amount").first("$mCoa.account")
				.as("account").first("$mCoa.subAccount").as("subAccount").first("$mCoa.code").as("code")
				.first("$mCoa.reportingCategory").as("reportingCategory");
		GroupOperation group2 = Aggregation.group("$reportingCategory")
				.push(new BasicDBObject("subAccount", "$subAccount").append("code", "$code")
						.append("account", "$account").append("amount", "$amount"))
				.as("items").sum("amount").as("total").first("reportingCategory").as("reportingCategory");

		Aggregation aggregation = Aggregation.newAggregation(dateRangeMatch, lookupMCoa,
				Aggregation.unwind("$mCoa", true), matchOperation, group1, group2);

		AggregationResults<Document> result = this.mongoTemplate.aggregate(aggregation, "t_acnt_trnsctn",
				Document.class);
		return result.getUniqueMappedResult();

	}

}
