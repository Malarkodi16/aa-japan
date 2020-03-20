package com.nexware.aajapan.repositories.custom.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AccumulatorOperators;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationExpression;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LiteralOperators;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import com.mongodb.BasicDBObject;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.ApprovePaymentsDto;
import com.nexware.aajapan.dto.PaymentTrackingDto;
import com.nexware.aajapan.dto.PaymentTrackingReportDto;
import com.nexware.aajapan.dto.TInspectionInvoiceDto;
import com.nexware.aajapan.repositories.custom.TInspectionInvoiceRepositoryCustom;
import com.nexware.aajapan.utils.AppUtil;

public class TInspectionInvoiceRepositoryCustomImpl implements TInspectionInvoiceRepositoryCustom {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<ApprovePaymentsDto> findAllByPaymentStatus(List<Integer> paymentProcessing) {
		final MatchOperation match = Aggregation
				.match(new Criteria().andOperator(Criteria.where("paymentStatus").in(paymentProcessing),
						Criteria.where("deleteFlag").is(Constants.DELETE_FLAG_0)));
		final LookupOperation lookupInspectionCompany = LookupOperation.newLookup().from("m_inspctn_cmpny")
				.localField("inspectionCompanyId").foreignField("code").as("inspectionCompany");
		LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stockDetails");

		final AggregationOperation groupOperation = context -> new Document("$group", new Document("_id", "$invoiceNo")
				.append("approvePaymentItems",
						new Document("$push", new BasicDBObject("code", "$code").append("stockNo", "$stockNo")
								.append("chassisNo", "$stockDetails.chassisNo").append("maker", "$stockDetails.maker")
								.append("model", "$stockDetails.model").append("taxAmount", "$taxAmount")
								.append("totalTaxIncluded", "$totalTaxIncluded").append("amount", "$amount")))
				.append("createdDate", new Document("$first", "$createdDate"))
				.append("dueDate", new Document("$first", "$dueDate")).append("refNo", new Document("$first", "$refNo"))
				.append("type", new Document("$first", Constants.INVOICE_TYPE_INSPECTION))
				.append("invoiceNo", new Document("$first", "$invoiceNo"))
				.append("invoiceName", new Document("$first", "$inspectionCompany.name"))
				.append("invoiceAttachmentFilename", new Document("$first", "$invoiceAttachmentFilename"))
				.append("invoiceAttachmentDiskFilename", new Document("$first", "$invoiceAttachmentDiskFilename"))
				.append("invoiceAmountReceived", new Document("$first", "$invoiceAmountReceived"))
				.append("supplierId", new Document("$first", "$inspectionCompanyId")));

		ProjectionOperation project = Aggregation.project()
				.andInclude("approvePaymentItems", "createdDate", "type", "invoiceNo", "refNo", "invoiceName",
						"dueDate", "invoiceAttachmentFilename", "invoiceAttachmentDiskFilename",
						"invoiceAmountReceived", "supplierId")
				.and(AccumulatorOperators.Sum.sumOf("approvePaymentItems.totalTaxIncluded")).as("totalAmount");

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupInspectionCompany,
				Aggregation.unwind("$inspectionCompany", true), lookupStock, Aggregation.unwind("$stockDetails", true),
				groupOperation, project);
		final AggregationResults<ApprovePaymentsDto> result = mongoTemplate.aggregate(aggregation, "t_inspctn_invc",
				ApprovePaymentsDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<TInspectionInvoiceDto> findAllInspectionPaymentApprovalInvoice() {
		MatchOperation match = Aggregation.match(Criteria.where("paymentStatus").in(
				Constants.INSPECTION_PAYMENT_INVOICE_APPROVAL, Constants.INSPECTION_PAYMENT_INVOICE_PROCESSING_PARTIAL,
				Constants.INSPECTION_PAYMENT_INVOICE_PAYMENT_CANCELLED));
		final LookupOperation lookupInspectionCompany = LookupOperation.newLookup().from("m_inspctn_cmpny")
				.localField("inspectionCompanyId").foreignField("code").as("inspectionCompany");
		LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stockDetail");

		ProjectionOperation project = Aggregation.project()
				.andInclude("id", "invoiceNo", "refNo", "stockNo", "amount", "dueDate", "tax",
						"invoiceAttachmentFilename", "invoiceAttachmentDiskFilename", "inspectionCompanyId",
						"createdDate", "taxAmount", "totalTaxIncluded")
				.and("$inspectionCompany.name").as("inspectionCompany").and("$stockDetail.chassisNo").as("chassisNo")
				.and("$stockDetail.maker").as("maker").and("$stockDetail.model").as("model").and("$code")
				.as("inspectionInvoiceId").and("$paymentStatus").as("paymentStatus");
		GroupOperation groupOperation = Aggregation.group("$invoiceNo")
				.push(new BasicDBObject("stockNo", "$stockNo").append("maker", "$maker").append("model", "$model")
						.append("chassisNo", "$chassisNo").append("amount", "$amount").append("taxAmount", "$taxAmount")
						.append("totalTaxIncluded", "$totalTaxIncluded")
						.append("inspectionInvoiceId", "$inspectionInvoiceId"))
				.as("items").first("invoiceNo").as("invoiceNo").first("inspectionInvoiceId").as("inspectionInvoiceId")
				.first("paymentStatus").as("paymentStatus").first("refNo").as("refNo").first("inspectionCompanyId")
				.as("inspectionCompanyId").first("dueDate").as("dueDate").first("inspectionCompany")
				.as("inspectionCompany").first("invoiceAttachmentFilename").as("invoiceAttachmentFilename")
				.first("invoiceAttachmentDiskFilename").as("invoiceAttachmentDiskFilename").first("createdDate")
				.as("invoiceDate").first(LiteralOperators.Literal.asLiteral(Constants.INVOICE_TYPE_INSPECTION))
				.as("type");

		final LookupOperation lookupInvoiceTransaction = LookupOperation.newLookup().from("t_invc_pymnt_trnsctn")
				.localField("invoiceNo").foreignField("invoiceNo").as("invoiceTransaction");

		final AggregationOperation filterTransaction = context -> new Document("$addFields",
				new Document("invoiceTransaction",
						new Document("$filter",
								new Document("input", "$invoiceTransaction").append("as", "result").append("cond",
										new Document("$eq", Arrays.asList("$$result.status",
												Constants.INVOICE_PAYMENT_TRANSACTION_NOT_CANCELLED))))));

		final AggregationOperation paymentVoucherNo = context -> new Document("$addFields",
				new Document("paymentVoucherNo", new Document("$reduce",
						new Document("input", "$invoiceTransaction").append("initialValue", "").append("in",
								new Document("$concat", Arrays.asList("$$value", "$$this.paymentVoucherNo", " | "))))));

		AggregationOperation invoiceTotal = context -> new Document("$addFields",
				new Document("invoiceTotal", new Document("$sum", "$items.totalTaxIncluded")));

		SortOperation sort = Aggregation.sort(Direction.ASC, "dueDate");

		Aggregation aggregation = Aggregation.newAggregation(match, lookupInspectionCompany,
				Aggregation.unwind("$inspectionCompany", true), lookupStock, Aggregation.unwind("$stockDetail", true),
				project, groupOperation, lookupInvoiceTransaction, filterTransaction, paymentVoucherNo, invoiceTotal,
				sort);
		AggregationResults<TInspectionInvoiceDto> result = this.mongoTemplate.aggregate(aggregation, "t_inspctn_invc",
				TInspectionInvoiceDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<TInspectionInvoiceDto> findAllInspectionPaymentCompletedInvoice() {
		MatchOperation match = Aggregation
				.match(Criteria.where("paymentStatus").is(Constants.INSPECTION_PAYMENT_INVOICE_PAYMENT_COMPLETED));
		final LookupOperation lookupInspectionCompany = LookupOperation.newLookup().from("m_inspctn_cmpny")
				.localField("inspectionCompanyId").foreignField("code").as("inspectionCompany");
		LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stockDetail");

		ProjectionOperation project = Aggregation.project()
				.andInclude("id", "invoiceNo", "refNo", "stockNo", "amount", "dueDate", "tax",
						"invoiceAttachmentFilename", "invoiceAttachmentDiskFilename", "inspectionCompanyId",
						"createdDate", "taxAmount", "totalTaxIncluded")
				.and("$inspectionCompany.name").as("inspectionCompany").and("$stockDetail.chassisNo").as("chassisNo")
				.and("$stockDetail.maker").as("maker").and("$stockDetail.model").as("model").and("$code")
				.as("inspectionInvoiceId").and("$paymentStatus").as("paymentStatus");
		GroupOperation groupOperation = Aggregation.group("$invoiceNo")
				.push(new BasicDBObject("stockNo", "$stockNo").append("maker", "$maker").append("model", "$model")
						.append("chassisNo", "$chassisNo").append("amount", "$amount").append("taxAmount", "$taxAmount")
						.append("totalTaxIncluded", "$totalTaxIncluded")
						.append("inspectionInvoiceId", "$inspectionInvoiceId"))
				.as("items").first("invoiceNo").as("invoiceNo").first("inspectionInvoiceId").as("inspectionInvoiceId")
				.first("paymentStatus").as("paymentStatus").first("refNo").as("refNo").first("inspectionCompanyId")
				.as("inspectionCompanyId").first("dueDate").as("dueDate").first("inspectionCompany")
				.as("inspectionCompany").first("invoiceAttachmentFilename").as("invoiceAttachmentFilename")
				.first("invoiceAttachmentDiskFilename").as("invoiceAttachmentDiskFilename").first("createdDate")
				.as("invoiceDate").first(LiteralOperators.Literal.asLiteral(Constants.INVOICE_TYPE_INSPECTION))
				.as("type");

		final LookupOperation lookupInvoiceTransaction = LookupOperation.newLookup().from("t_invc_pymnt_trnsctn")
				.localField("invoiceNo").foreignField("invoiceNo").as("invoiceTransaction");

		final AggregationOperation filterTransaction = context -> new Document("$addFields",
				new Document("invoiceTransaction",
						new Document("$filter",
								new Document("input", "$invoiceTransaction").append("as", "result").append("cond",
										new Document("$eq", Arrays.asList("$$result.status",
												Constants.INVOICE_PAYMENT_TRANSACTION_NOT_CANCELLED))))));

		final AggregationOperation paymentVoucherNo = context -> new Document("$addFields",
				new Document("paymentVoucherNo", new Document("$reduce",
						new Document("input", "$invoiceTransaction").append("initialValue", "").append("in",
								new Document("$concat", Arrays.asList("$$value", "$$this.paymentVoucherNo", " | "))))));

		AggregationOperation invoiceTotal = context -> new Document("$addFields",
				new Document("invoiceTotal", new Document("$sum", "$items.totalTaxIncluded")));

		SortOperation sort = Aggregation.sort(Direction.ASC, "dueDate");

		Aggregation aggregation = Aggregation.newAggregation(match, lookupInspectionCompany,
				Aggregation.unwind("$inspectionCompany", true), lookupStock, Aggregation.unwind("$stockDetail", true),
				project, groupOperation, lookupInvoiceTransaction, filterTransaction, paymentVoucherNo, invoiceTotal,
				sort);
		AggregationResults<TInspectionInvoiceDto> result = this.mongoTemplate.aggregate(aggregation, "t_inspctn_invc",
				TInspectionInvoiceDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<PaymentTrackingDto> purchasepaymentTracking(String remitter, Date fromDate, Date toDate) {
		final List<Criteria> andCriterias = new ArrayList<>();

		boolean isValid = false;
		if (!AppUtil.isObjectEmpty(remitter)) {
			isValid = true;
			andCriterias.add(Criteria.where("inspectionCompanyId").is(remitter));
		}

		if (!AppUtil.isObjectEmpty(fromDate)) {
			isValid = true;
			andCriterias
					.add(new Criteria().andOperator(Criteria.where("createdDate").gte(AppUtil.atStartOfDay(fromDate)),
							Criteria.where("createdDate").lte(AppUtil.atEndOfDay(toDate))));
		}
		if (!isValid) {
			return new ArrayList<>();
		}
		final Criteria matchCriteria = new Criteria();
		matchCriteria.andOperator(andCriterias.toArray(new Criteria[0]));
		final MatchOperation match = Aggregation.match(matchCriteria);

		LookupOperation lookupInspectionCompany = LookupOperation.newLookup().from("m_inspctn_cmpny")
				.localField("inspectionCompanyId").foreignField("code").as("inspectionCompany");
		LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stockDetails");

		AggregationOperation groupOperation = context -> new Document("$group", new Document("_id", "$invoiceNo")
				.append("approvePaymentItems",
						new Document("$push", new BasicDBObject("invoiceNo", "$code").append("stockNo", "$stockNo")
								.append("chassisNo", "$stockDetails.chassisNo").append("maker", "$stockDetails.maker")
								.append("model", "$stockDetails.model").append("taxamount", "$taxAmount")
								.append("totalTaxIncluded", "$totalTaxIncluded").append("amount", "$amount")))
				.append("invoiceUpload", new Document("$first", "$invoiceUpload"))
				.append("invoiceNo", new Document("$first", "$invoiceNo"))
				.append("refNo", new Document("$first", "$refNo"))
				.append("paymentApproveStatus", new Document("$first", "$paymentStatus"))
				.append("invoiceName", new Document("$first", "$inspectionCompany.name"))
				.append("type", new Document("$first", Constants.INVOICE_TYPE_INSPECTION))
				.append("approvedDate", new Document("$first", "$approvedDate"))
				.append("invoiceDate", new Document("$first", "$createdDate"))
				.append("approvedBy", new Document("$first", "$approvedBy"))
				.append("paidAmount", new Document("$first", "$invoiceAmountReceived"))
				.append("invoiceAttachmentFilename", new Document("$first", "$invoiceAttachmentFilename"))
				.append("invoiceAttachmentDiskFilename", new Document("$first", "$invoiceAttachmentDiskFilename")));
		LookupOperation lookupUser = LookupOperation.newLookup().from("m_usr").localField("approvedBy")
				.foreignField("code").as("userDetails");
		ProjectionOperation project = Aggregation.project()
				.andInclude("approvePaymentItems", "type", "invoiceNo", "invoiceUpload", "invoiceName",
						"paymentApproveStatus", "approvedDate", "invoiceDate", "paidAmount",
						"invoiceAttachmentFilename", "invoiceAttachmentDiskFilename", "refNo")
				.and(AccumulatorOperators.Sum.sumOf("approvePaymentItems.totalTaxIncluded")).as("totalAmount")
				.and("$userDetails.fullname").as("approvedBy");

		Aggregation aggregation = Aggregation.newAggregation(match, lookupInspectionCompany, lookupStock,
				Aggregation.unwind("$stockDetails", true), Aggregation.unwind("$inspectionCompany", true),
				groupOperation, lookupUser, Aggregation.unwind("$userDetails", true), project);
		AggregationResults<PaymentTrackingDto> result = this.mongoTemplate.aggregate(aggregation, "t_inspctn_invc",
				PaymentTrackingDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<PaymentTrackingReportDto> purchasepaymentTrackingReport(String remitter, Date fromDate, Date toDate) {

		final List<Criteria> andCriterias = new ArrayList<>();

		boolean isValid = false;
		if (!AppUtil.isObjectEmpty(remitter)) {
			isValid = true;
			andCriterias.add(Criteria.where("inspectionCompanyId").is(remitter));
		}

		if (!AppUtil.isObjectEmpty(fromDate)) {
			isValid = true;
			andCriterias
					.add(new Criteria().andOperator(Criteria.where("createdDate").gte(AppUtil.atStartOfDay(fromDate)),
							Criteria.where("createdDate").lte(AppUtil.atEndOfDay(toDate))));
		}
		if (!isValid) {
			return new ArrayList<>();
		}
		final Criteria matchCriteria = new Criteria();
		matchCriteria.andOperator(andCriterias.toArray(new Criteria[0]));
		final MatchOperation match = Aggregation.match(matchCriteria);

		LookupOperation lookupInspectionCompany = LookupOperation.newLookup().from("m_inspctn_cmpny")
				.localField("inspectionCompanyId").foreignField("code").as("inspectionCompany");
		LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stockDetails");
		AggregationOperation groupOperation = context -> new Document("$group", new Document("_id", "$invoiceNo")
				.append("approvePaymentItems",
						new Document("$push", new BasicDBObject("invoiceNo", "$invoiceNo").append("stockNo", "$stockNo")
								.append("chassisNo", "$stockDetails.chassisNo").append("maker", "$stockDetails.maker")
								.append("model", "$stockDetails.model").append("taxamount", "$taxAmount")
								.append("totalTaxIncluded", "$totalTaxIncluded").append("amount", "$amount")))
				.append("invoiceNo", new Document("$first", "$invoiceNo"))
				.append("paymentApproveStatus", new Document("$first", "$paymentStatus"))
				.append("inspectionCompanyId", new Document("$first", "$inspectionCompanyId"))
				.append("invoiceName", new Document("$first", "$inspectionCompany.name"))
				.append("approvedDate", new Document("$first", "$approvedDate"))
				.append("invoiceDate", new Document("$first", "$createdDate"))
				.append("approvedBy", new Document("$first", "$approvedBy"))
				.append("paidAmount", new Document("$first", "$invoiceAmountReceived")));

		final AggregationExpression totalAmount = contexts -> new Document("$sum",
				"$approvePaymentItems.totalTaxIncluded");
		final AggregationExpression balanceAmount = contexts -> new Document("$subtract",
				Arrays.asList(new Document("$sum", "$approvePaymentItems.totalTaxIncluded"), "$paidAmount"));
		ProjectionOperation project1 = Aggregation.project()
				.andInclude("invoiceNo", "paymentApproveStatus", "invoiceDate", "paidAmount", "invoiceName")
				.and(totalAmount).as("totalAmount").and(balanceAmount).as("balance");

		final LookupOperation lookupTransaction = LookupOperation.newLookup().from("t_invc_pymnt_trnsctn")
				.localField("invoiceNo").foreignField("invoiceNo").as("transaction");
		final AggregationOperation filterTransactions = contexts -> new Document("$addFields",
				new Document("transaction",
						new Document("$filter",
								new Document("input", "$transaction").append("as", "result").append("cond",
										new Document("$eq", Arrays.asList("$$result.status",
												Constants.INVOICE_PAYMENT_TRANSACTION_NOT_CANCELLED))))));

		final ProjectionOperation project2 = Aggregation.project()
				.andInclude("invoiceDate", "invoiceNo", "paymentApproveStatus", "paidAmount", "invoiceName",
						"totalAmount", "balance")
				.and("$transaction.code").as("paymentVoucherNo").and("$transaction.approvedDate").as("approvedDate")
				.and("$transaction.amount").as("paymentAmount");
		Aggregation aggregation = Aggregation.newAggregation(match, lookupInspectionCompany,
				Aggregation.unwind("$inspectionCompany", true), lookupStock, Aggregation.unwind("$stockDetails", true),
				groupOperation, project1, lookupTransaction, filterTransactions,
				Aggregation.unwind("$transaction", true), project2);
		AggregationResults<PaymentTrackingReportDto> result = this.mongoTemplate.aggregate(aggregation,
				"t_inspctn_invc", PaymentTrackingReportDto.class);
		return result.getMappedResults();

	}

}
