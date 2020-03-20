package com.nexware.aajapan.repositories.custom.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AccumulatorOperators;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.TDayBookDto;
import com.nexware.aajapan.models.TDayBook;
import com.nexware.aajapan.models.TDayBookTransaction;
import com.nexware.aajapan.models.TSalesInvoice;
import com.nexware.aajapan.repositories.custom.TTAllocationRepositoryCustom;

public class TTAllocationRepositoryCustomImpl implements TTAllocationRepositoryCustom {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public TSalesInvoice getSalesinvoiceById(String id) {
		Criteria criteria = new Criteria();
		criteria.andOperator(Criteria.where("_id").is(id), Criteria.where("status")
				.in(Constants.SALES_INV_PAYMENT_NOT_RECEIVED, Constants.SALES_INV_PAYMENT_RECEIVED_PARTIAL));
		MatchOperation match = Aggregation.match(criteria);
		SortOperation sort = Aggregation.sort(Direction.ASC, "createdDate");
		Aggregation aggregation = Aggregation.newAggregation(match, sort);
		AggregationResults<TSalesInvoice> result = this.mongoTemplate.aggregate(aggregation, TSalesInvoice.class,
				TSalesInvoice.class);
		return result.getUniqueMappedResult();
	}

	@Override
	public List<TDayBookDto> getEntryByStatus() {
		MatchOperation match = Aggregation.match(Criteria.where("status").is(Constants.DAYBOOK_ENTRY_APPROVED));
		LookupOperation currencyLookup = Aggregation.lookup("m_currency", "currency", "currencySeq", "currencyDetails");
		LookupOperation bankLookup = Aggregation.lookup("m_bank", "bank", "bankSeq", "bankDetails");
		LookupOperation remitTypeLookup = Aggregation.lookup("m_remit_type", "remitType", "remitSeq", "remitDetails");
		ProjectionOperation projectionOperation = Aggregation
				.project("daybookId", "remitDate", "remitType", "remitter", "bank", "currency", "amount", "bankCharges",
						"remarks", "ownedAmount", "exchangeRate", "salesExchangeRate", "SpecialExchangeRate",
						"customer", "billOfExchange", "lcNo", "salesPerson")
				.and("$currencyDetails.symbol").as("currencySymbol").and("$bankDetails.bankName").as("bankName")
				.and("$remitDetails.remitType").as("remitTypeName").and("amount").minus("ownedAmount")
				.as("balanceAmount");
		MatchOperation matchBal = Aggregation.match(Criteria.where("balanceAmount").ne(0));

		Aggregation aggregation = Aggregation.newAggregation(match, currencyLookup,
				Aggregation.unwind("currencyDetails", true), bankLookup, Aggregation.unwind("bankDetails", true),
				remitTypeLookup, Aggregation.unwind("remitDetails", true), projectionOperation, matchBal);
		AggregationResults<TDayBookDto> result = this.mongoTemplate.aggregate(aggregation, TDayBook.class,
				TDayBookDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<TDayBookTransaction> getInvoiceBasedReceivedAmount(String dayBookId, String inviceNo, String stockId) {
		Criteria criteria = new Criteria();
		criteria.andOperator(Criteria.where("daybookId").is(dayBookId), Criteria.where("salesInvoiceId").is(inviceNo),
				Criteria.where("stockId").is(stockId));
		MatchOperation match = Aggregation.match(criteria);
		Aggregation aggregation = Aggregation.newAggregation(match);
		AggregationResults<TDayBookTransaction> result = this.mongoTemplate.aggregate(aggregation, "t_dybk_trnsctn",
				TDayBookTransaction.class);
		return result.getMappedResults();
	}

}
