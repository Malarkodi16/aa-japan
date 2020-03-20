package com.nexware.aajapan.repositories.custom.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import com.mongodb.BasicDBObject;
import com.nexware.aajapan.dto.LoanSearchDto;
import com.nexware.aajapan.dto.TLoanListDto;
import com.nexware.aajapan.models.TLoan;
import com.nexware.aajapan.repositories.custom.TLoanRepositoryCustom;

public class TLoanRepositoryCustomImpl implements TLoanRepositoryCustom {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<TLoanListDto> getLoanDetails(String bank, String reference, String sequence) {
		if ((bank.equals("")) || (reference.equals("")) || (sequence.equals(""))) {
			return new ArrayList<>();
		} else {

			Criteria criteria = new Criteria();
			criteria.andOperator(Criteria.where("bank").is(bank), Criteria.where("reference").is(reference),
					Criteria.where("sequence").is(sequence));
			MatchOperation match = Aggregation.match(criteria);
			LookupOperation lookupLoanDetails = LookupOperation.newLookup().from("t_ln_dtls").localField("sequence")
					.foreignField("sequence").as("salesInvoiceDetails");

			ProjectionOperation project = Aggregation.project().andExpression("year(salesInvoiceDetails.loanDate)")
					.as("year").and("salesInvoiceDetails.loanId").as("loanId").and("salesInvoiceDetails.principal")
					.as("principal").and("salesInvoiceDetails.interest").as("interest")
					.and("salesInvoiceDetails.totalPayment").as("totalPayment").and("salesInvoiceDetails.balance")
					.as("balance").and("salesInvoiceDetails.loanDate").as("dateAsMonth")
					.and("salesInvoiceDetails.sequence").as("sequence");

			GroupOperation groupOperation = Aggregation.group("year").sum("principal").as("principalTotal")
					.sum("interest").as("interestTotal").sum("totalPayment").as("totalPaymentTotal").sum("balance")
					.as("balanceTotal")
					.push(new BasicDBObject("month", "$dateAsMonth").append("principal", "$principal")
							.append("interest", "$interest").append("totalPayment", "$totalPayment")
							.append("balance", "$balance"))
					.as("month").first("sequence").as("sequence").first("loanId").as("loanId");

			Aggregation aggregation = Aggregation.newAggregation(match, lookupLoanDetails,
					Aggregation.unwind("$salesInvoiceDetails", true), project, groupOperation);

			AggregationResults<TLoanListDto> result = this.mongoTemplate.aggregate(aggregation, "t_createloan",
					TLoanListDto.class);

			return result.getMappedResults();
		}
	}

	@Override
	public List<TLoanListDto> getAllLoanDetails() {
		LookupOperation lookupLoanDetails = LookupOperation.newLookup().from("t_ln_dtls").localField("loanId")
				.foreignField("loanId").as("loanDetails");
		LookupOperation lookupBankDetails = LookupOperation.newLookup().from("m_bank").localField("bank")
				.foreignField("bankSeq").as("bankDetails");

		GroupOperation groupOperation = Aggregation.group("loanId")
				.push(new BasicDBObject("dueDate", "$loanDetails.date")
						.append("principalAmt", "$loanDetails.principalAmt")
						.append("interestAmount", "$loanDetails.interestAmount").
						append("amount", "$loanDetails.amount")
						.append("openingBalance", "$loanDetails.openingBalance")
						.append("closingBalance", "$loanDetails.closingBalance")
						.append("status", "$loanDetails.status"))
				.as("loanDetails").first("bankDetails.bankName").as("bankName").first("loanId").as("loanId")
				.first("date").as("date").first("bank").as("bank").first("reference").as("reference").first("loanTerm")
				.as("loanTerm").first("loanAmount").as("loanAmount").first("firstPaymentDate").as("firstPaymentDate")
				.first("rateOfInterest").as("rateOfInterest").first("totalPayable").as("totalPayable")
				.first("closingBalance").as("closingBalance").first("loanType").as("loanType").first("description")
				.as("description");

		Aggregation aggregation = Aggregation.newAggregation(lookupLoanDetails,
				Aggregation.unwind("$loanDetails", true), lookupBankDetails, Aggregation.unwind("$bankDetails", true),
				groupOperation);

		AggregationResults<TLoanListDto> result = this.mongoTemplate.aggregate(aggregation, "t_loan",
				TLoanListDto.class);

		return result.getMappedResults();
	}

	@Override
	public List<LoanSearchDto> findBySearchDto(String search) {
		Criteria criteria = new Criteria();
		criteria.orOperator(Criteria.where("loanId").regex(".*" + search + ".*", "i"),
				Criteria.where("reference").regex(".*" + search + ".*", "i"));
		MatchOperation match = Aggregation.match(criteria);

		ProjectionOperation project = Aggregation.project().andInclude("loanId", "reference");

		Aggregation aggregation = Aggregation.newAggregation(match, project);
		AggregationResults<LoanSearchDto> result = this.mongoTemplate.aggregate(aggregation, TLoan.class,
				LoanSearchDto.class);

		return result.getMappedResults();
	}
}
