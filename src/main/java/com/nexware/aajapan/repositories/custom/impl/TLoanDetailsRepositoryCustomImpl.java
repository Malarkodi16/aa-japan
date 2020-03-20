package com.nexware.aajapan.repositories.custom.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AccumulatorOperators;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.BasicDBObject;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.TLoanDetailsDto;
import com.nexware.aajapan.dto.TLoanListDto;
import com.nexware.aajapan.dto.TLoanRepaymentDto;
import com.nexware.aajapan.models.MHSCode;
import com.nexware.aajapan.models.TLoanDetails;
import com.nexware.aajapan.repositories.custom.TLoanDetailsRepositoryCustom;

public class TLoanDetailsRepositoryCustomImpl implements TLoanDetailsRepositoryCustom {
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private MongoOperations mongoOperations;

	@Override
	public TLoanRepaymentDto findLoanWithMinDateAndNotPaid(String loanId) {
		MatchOperation match = Aggregation.match(new Criteria().andOperator(Criteria.where("loanId").is(loanId),
				Criteria.where("status").is(Constants.LOAN_REPAYMENT_NOT_PAID)));
		SortOperation sort = Aggregation.sort(Direction.ASC, "createdDate");
		LimitOperation limit = Aggregation.limit(1);
		LookupOperation lookupLoan = LookupOperation.newLookup().from("t_loan").localField("loanId")
				.foreignField("loanId").as("loan");
		ProjectionOperation project = Aggregation.project().andInclude("loanDtlId", "loanId").and("amount")
				.as("installmentAmount").and("loan.bank").as("bank").and("loan.loanType").as("loanType")
				.and("loan.savingAccount").as("savingAccount").and("loan.savingsAccountAmount")
				.as("savingsAccountAmount").and("loan.savingsBankAccount").as("savingsBankAccount").and("date")
				.as("paymentDate");
		Aggregation aggregation = Aggregation.newAggregation(match, sort, limit, lookupLoan,
				Aggregation.unwind("$loan", true), project);
		AggregationResults<TLoanRepaymentDto> result = this.mongoTemplate.aggregate(aggregation, "t_ln_dtls",
				TLoanRepaymentDto.class);
		return result.getUniqueMappedResult();
	}

	@Override
	public TLoanRepaymentDto preCloseLoan(String loanId) {
		MatchOperation match = Aggregation.match(new Criteria().andOperator(Criteria.where("loanId").is(loanId),
				Criteria.where("status").is(Constants.LOAN_REPAYMENT_NOT_PAID)));
		LookupOperation lookupLoan = LookupOperation.newLookup().from("t_loan").localField("loanId")
				.foreignField("loanId").as("loan");
		ProjectionOperation project = Aggregation.project().andInclude("loanId")
				.and(AccumulatorOperators.Sum.sumOf("loan.closingBalance")).as("installmentAmount").and("loan.bank")
				.as("bank").and("loan.loanType").as("loanType").and("loan.savingAccount").as("savingAccount")
				.and("loan.savingsAccountAmount").as("savingsAccountAmount").and("loan.savingsBankAccount")
				.as("savingsBankAccount").and("date").as("paymentDate");
		LimitOperation limit = Aggregation.limit(1);
		Aggregation aggregation = Aggregation.newAggregation(match, lookupLoan, Aggregation.unwind("$loan", true),
				project, limit);
		AggregationResults<TLoanRepaymentDto> result = this.mongoTemplate.aggregate(aggregation, "t_ln_dtls",
				TLoanRepaymentDto.class);
		return result.getUniqueMappedResult();
	}

	@Override
	public List<TLoanDetailsDto> getEditLoanDetail(String loanId) {
		MatchOperation match = Aggregation.match(new Criteria().andOperator(Criteria.where("loanId").is(loanId)));
		ProjectionOperation project = Aggregation.project().andInclude("loanId", "loanDtlId", "principalAmt",
				"interestAmount", "amount", "openingBalance", "closingBalance", "status").and("date").as("dueDate");
		SortOperation sort = Aggregation.sort(Direction.ASC, "dueDate");
		Aggregation aggregation = Aggregation.newAggregation(match, project, sort);

		AggregationResults<TLoanDetailsDto> result = this.mongoTemplate.aggregate(aggregation, "t_ln_dtls",
				TLoanDetailsDto.class);

		return result.getMappedResults();
	}
	
	@Override
	public boolean existsByStatus(String loanId) {
		// TODO Auto-generated method stub
		final Criteria criteria = new Criteria();
		criteria.andOperator(Criteria.where("status").is(Constants.LOAN_REPAYMENT_PAID), Criteria.where("loanId").is(loanId));
		return mongoOperations.exists(Query.query(criteria), TLoanDetails.class);
	}

}
