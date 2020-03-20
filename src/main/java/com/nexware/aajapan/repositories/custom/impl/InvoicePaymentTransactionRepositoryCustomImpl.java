package com.nexware.aajapan.repositories.custom.impl;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.TInvoicePaymentTransactionDto;
import com.nexware.aajapan.repositories.custom.InvoicePaymentTransactionRepositoryCustom;
import com.nexware.aajapan.utils.AppUtil;

public class InvoicePaymentTransactionRepositoryCustomImpl implements InvoicePaymentTransactionRepositoryCustom {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<TInvoicePaymentTransactionDto> findAllTransactionDetailsByInvoiceNoAndStatus(String invoiceNo) {
		MatchOperation matchOperation = Aggregation.match(Criteria.where("invoiceNo").is(invoiceNo).and("status")
				.is(Constants.INVOICE_PAYMENT_TRANSACTION_NOT_CANCELLED));
		LookupOperation lookupBank = LookupOperation.newLookup().from("m_bank").localField("bankId")
				.foreignField("bankSeq").as("bankDetails");
		ProjectionOperation project = Aggregation.project()
				.andInclude("invoiceType", "code", "invoiceNo", "bankId", "amount", "approvedDate", "paymentVoucherNo",
						"bankStatementAttachmentFilename", "bankStatementAttachmentDiskFilename")
				.and("bankDetails.bankName").as("bankName").and("attachmentDirectory").as("attachmentDirectory")
				.andExpression("approvedDate").dateAsFormattedString("%d-%m-%Y").as("sApprovedDate");
		Aggregation aggregation = Aggregation.newAggregation(matchOperation, lookupBank,
				Aggregation.unwind("$bankDetails", true), project);
		AggregationResults<TInvoicePaymentTransactionDto> result = this.mongoTemplate.aggregate(aggregation,
				"t_invc_pymnt_trnsctn", TInvoicePaymentTransactionDto.class);
		return result.getMappedResults();

	}

	@Override
	public Double findTotalByInvoiceNoAndStatus(String invoiceNo) {

		MatchOperation matchOperation = Aggregation.match(Criteria.where("invoiceNo").is(invoiceNo).and("status")
				.is(Constants.INVOICE_PAYMENT_TRANSACTION_NOT_CANCELLED));
		GroupOperation groupOperationTotal = Aggregation.group("invoiceType", "invoiceNo").sum("amount").as("total");
		Aggregation aggregation = Aggregation.newAggregation(matchOperation, groupOperationTotal);
		AggregationResults<Document> result = this.mongoTemplate.aggregate(aggregation, "t_invc_pymnt_trnsctn",
				Document.class);

		return Double.valueOf(!AppUtil.isObjectEmpty(result.getUniqueMappedResult())
				? result.getUniqueMappedResult().get("total").toString()
				: "0.0");
	}

	@Override
	public Double findTotalByInvoiceTypeAndInvoiceNoAndStatus(String invoiceType, String invoiceNo) {

		MatchOperation matchOperation = Aggregation
				.match(Criteria.where("invoiceType").is(invoiceType).andOperator(Criteria.where("invoiceNo")
						.is(invoiceNo).and("status").is(Constants.INVOICE_PAYMENT_TRANSACTION_NOT_CANCELLED)));
		GroupOperation groupOperationTotal = Aggregation.group("invoiceType", "invoiceNo").sum("amount").as("total");
		Aggregation aggregation = Aggregation.newAggregation(matchOperation, groupOperationTotal);
		AggregationResults<Document> result = this.mongoTemplate.aggregate(aggregation, "t_invc_pymnt_trnsctn",
				Document.class);

		return Double.valueOf(!AppUtil.isObjectEmpty(result.getUniqueMappedResult())
				? result.getUniqueMappedResult().get("total").toString()
				: "0.0");
	}

	@Override
	public Double findTotalUsdByInvoiceTypeAndInvoiceNoAndStatus(String invoiceType, String invoiceNo) {
		MatchOperation matchOperation = Aggregation
				.match(Criteria.where("invoiceType").is(invoiceType).andOperator(Criteria.where("invoiceNo")
						.is(invoiceNo).and("status").is(Constants.INVOICE_PAYMENT_TRANSACTION_NOT_CANCELLED)));
		GroupOperation groupOperationTotal = Aggregation.group("invoiceType", "invoiceNo").sum("amount").as("total");
		Aggregation aggregation = Aggregation.newAggregation(matchOperation, groupOperationTotal);
		AggregationResults<Document> result = this.mongoTemplate.aggregate(aggregation, "t_invc_pymnt_trnsctn",
				Document.class);

		return Double.valueOf(!AppUtil.isObjectEmpty(result.getUniqueMappedResult())
				? result.getUniqueMappedResult().get("total").toString()
				: "0.0");
	}
}
