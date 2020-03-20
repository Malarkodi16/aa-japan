package com.nexware.aajapan.repositories.custom.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AccumulatorOperators;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationExpression;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.CountOperation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import com.mongodb.BasicDBObject;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.PayableAmountDto;
import com.nexware.aajapan.dto.PaymentTrackingDto;
import com.nexware.aajapan.dto.PaymentTrackingReportDto;
import com.nexware.aajapan.dto.TStoragePhotosApprovalDto;
import com.nexware.aajapan.repositories.custom.TFwdrInvoiceRepositoryCustom;
import com.nexware.aajapan.utils.AppUtil;

public class TFwdrInvoiceRepositoryCustomImpl implements TFwdrInvoiceRepositoryCustom {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<TStoragePhotosApprovalDto> getListOnInitiatedStatus() {
		MatchOperation match = Aggregation.match(Criteria.where("paymentApprove").is(Constants.PAYMENT_NOT_APPROVED));
		LookupOperation lookupForwarder = LookupOperation.newLookup().from("m_frwrdr").localField("remitter")
				.foreignField("code").as("forwarderDetails");
		LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stockDetails");
		LookupOperation lookupCurrency = LookupOperation.newLookup().from("m_currency").localField("currency")
				.foreignField("currencySeq").as("currencyDetails");
		AggregationOperation groupOperation = context -> new Document("$group",
				new Document("_id", "$invoiceNo").append("approvePaymentItems",
						new Document("$push",
								new BasicDBObject("invoiceNo", "$invoiceNo").append("stockNo", "$stockNo")
										.append("chassisNo", "$stockDetails.chassisNo").append("remarks", "$remarks")
										.append("currencySymbol", "$currencyDetails.symbol")
										.append("chargesList", "$chargesList").append("metaData", "$metaData")
//						.append("blAmendCombineCharges",
//								new Document("$sum", Arrays.asList("$blAmendCombineCharges", "$blAmendCombineTax")))
//						.append("radiationCharges",
//								new Document("$sum", Arrays.asList("$radiationCharges", "$radiationTax")))
//						.append("repairCharges", new Document("$sum", Arrays.asList("$repairCharges", "$repairTax")))
//						.append("yardHandlingCharges",
//								new Document("$sum", Arrays.asList("$yardHandlingCharges", "$yardHandlingTax")))
//						.append("inspectionCharges",
//								new Document("$sum", Arrays.asList("$inspectionCharges", "$inspectionTax")))
//						.append("transportCharges",
//								new Document("$sum", Arrays.asList("$transportCharges", "$transportTax")))
//						.append("freightCharges", new Document("$sum", Arrays.asList("$freightCharges", "$freightTax")))
//						.append("totalAmount",
//								new Document("$sum", Arrays.asList("$amount", "$storageTax", "$shippingCharges",
//										"$shippingTax", "$photoCharges", "$photoTax", "$blAmendCombineCharges",
//										"$blAmendCombineTax", "$radiationCharges", "$radiationTax", "$repairCharges",
//										"$repairTax", "$yardHandlingCharges", "$yardHandlingTax", "$inspectionCharges",
//										"$inspectionTax", "$transportCharges", "$transportTax", "$freightCharges",
//										"$freightTax")))
						)).append("invoiceDate", new Document("$first", "$invoiceDate"))
						.append("currency", new Document("$first", "$currency"))
						.append("currencySymbol", new Document("$first", "$currencyDetails.symbol"))
						.append("forwarderName", new Document("$first", "$forwarderName"))
						.append("remitter", new Document("$first", "$remitter"))
						.append("invoiceNo", new Document("$first", "$invoiceNo"))
						.append("refNo", new Document("$first", "$refNo"))
						.append("remarks", new Document("$first", "$remarks"))
						.append("invoiceAttachmentFilename", new Document("$first", "$invoiceAttachmentFilename"))
						.append("invoiceAttachmentDiskFilename",
								new Document("$first", "$invoiceAttachmentDiskFilename"))
						.append("invoiceUpload", new Document("$first", "$invoiceUpload"))
						.append("attachementViewed", new Document("$first", "$attachementViewed"))
						.append("type", new Document("$first", Constants.INVOICE_TYPE_FORWARDER))
						.append("amount", new Document("$first", "$amount"))
						.append("dueDate", new Document("$first", "$dueDate"))
						.append("paymentApprove", new Document("$first", "$paymentApprove"))
						.append("metaData", new Document("$first", "$metaData"))
						.append("totalAmount", new Document("$first", "$totalAmount"))

		);
		ProjectionOperation project = Aggregation.project().andInclude("approvePaymentItems", "invoiceNo", "type",
				"invoiceAttachmentFilename", "invoiceAttachmentDiskFilename", "forwarderName", "remitter", "refNo",
				"invoiceDate", "dueDate", "status", "currency", "paymentApprove", "invoiceUpload", "attachementViewed",
				"remarks", "currencySymbol", "metaData", "totalAmount");

		Aggregation aggregation = Aggregation.newAggregation(match, lookupForwarder, lookupStock,
				Aggregation.unwind("$forwarderDetails", true), Aggregation.unwind("$stockDetails", true),
				lookupCurrency, Aggregation.unwind("$currencyDetails", true), groupOperation, project);

		AggregationResults<TStoragePhotosApprovalDto> result = this.mongoTemplate.aggregate(aggregation,
				"t_frwrdr_invc", TStoragePhotosApprovalDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<TStoragePhotosApprovalDto> findAllByPaymentApprove(List<Integer> paymentApprove) {
		MatchOperation match = Aggregation.match(Criteria.where("paymentApprove").in(paymentApprove));
		LookupOperation lookupForwarder = LookupOperation.newLookup().from("m_frwrdr").localField("remitter")
				.foreignField("code").as("forwarderDetails");
		LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stockDetails");
		LookupOperation lookupCurrency = LookupOperation.newLookup().from("m_currency").localField("currency")
				.foreignField("currencySeq").as("currencyDetails");
		AggregationOperation groupOperation = context -> new Document("$group", new Document("_id", "$invoiceNo")
				.append("approvePaymentItems",
						new Document("$push",
								new BasicDBObject("invoiceNo", "$invoiceNo").append("stockNo", "$stockNo")
										.append("chassisNo", "$stockDetails.chassisNo").append("remarks", "$remarks")
										.append("currencySymbol", "$currencyDetails.symbol")
										.append("chargesList", "$chargesList").append("metaData", "$metaData")))
				.append("createdDate", new Document("$first", "$createdDate"))
				.append("dueDate", new Document("$first", "$dueDate"))
				.append("currency", new Document("$first", "$currency"))
				.append("currencySymbol", new Document("$first", "$currencyDetails.symbol"))
				.append("type", new Document("$first", Constants.INVOICE_TYPE_FORWARDER))
				.append("invoiceAttachmentFilename", new Document("$first", "$invoiceAttachmentFilename"))
				.append("invoiceAttachmentDiskFilename", new Document("$first", "$invoiceAttachmentDiskFilename"))
				.append("invoiceNo", new Document("$first", "$invoiceNo"))
				.append("invoiceAmountReceived", new Document("$first", "$invoiceAmountReceived"))
				.append("metaData", new Document("$first", "$metaData"))
				.append("totalAmount", new Document("$first", "$totalAmount"))
				.append("supplierId", new Document("$first", "$remitter"))
				.append("invoiceName", new Document("$first", "$forwarderName")));

		ProjectionOperation project = Aggregation.project().andInclude("approvePaymentItems", "createdDate", "dueDate",
				"type", "invoiceNo", "invoiceName", "supplierId", "invoiceAttachmentFilename",
				"invoiceAttachmentDiskFilename", "invoiceAmountReceived", "metaData", "totalAmount", "currency",
				"currencySymbol");

		Aggregation aggregation = Aggregation.newAggregation(match, lookupForwarder, lookupStock,
				Aggregation.unwind("$forwarderDetails", true), lookupCurrency,
				Aggregation.unwind("$currencyDetails", true), groupOperation, project);
		AggregationResults<TStoragePhotosApprovalDto> result = this.mongoTemplate.aggregate(aggregation,
				"t_frwrdr_invc", TStoragePhotosApprovalDto.class);
		return result.getMappedResults();
	}

	@Override
	public Long getCountStorageData(List<Integer> paymentApprove) {
		MatchOperation match = Aggregation.match(Criteria.where("paymentApprove").in(paymentApprove));
		GroupOperation groupOperation = Aggregation.group("$invoiceNo");
		CountOperation count = Aggregation.count().as("count");
		Aggregation aggregation = Aggregation.newAggregation(match, groupOperation, count);
		AggregationResults<Document> result = this.mongoTemplate.aggregate(aggregation, "t_frwrdr_invc",
				Document.class);
		return Long.valueOf(!AppUtil.isObjectEmpty(result.getUniqueMappedResult())
				? result.getUniqueMappedResult().get("count").toString()
				: "0");
	}

	@Override
	public List<TStoragePhotosApprovalDto> findAllByPaymentApproveFreezed(Integer paymentApprove) {
		MatchOperation match = Aggregation.match(Criteria.where("paymentApprove").is(paymentApprove));
		LookupOperation lookupForwarder = LookupOperation.newLookup().from("m_frwrdr").localField("remitter")
				.foreignField("code").as("forwarderDetails");
		LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stockDetails");
		LookupOperation lookupCurrency = LookupOperation.newLookup().from("m_currency").localField("currency")
				.foreignField("currencySeq").as("currencyDetails");
		AggregationOperation groupOperation = context -> new Document("$group", new Document("_id", "$invoiceNo")

				.append("approvePaymentItems",
						new Document("$push",
								new BasicDBObject("invoiceNo", "$invoiceNo").append("stockNo", "$stockNo")
										.append("chassisNo", "$stockDetails.chassisNo").append("remarks", "$remarks")
										.append("currencySymbol", "$currencyDetails.symbol")
										.append("chargesList", "$chargesList").append("metaData", "$metaData")))
				.append("invoiceDate", new Document("$first", "$invoiceDate"))
				.append("invoiceUpload", new Document("$first", "$invoiceUpload"))
				.append("currency", new Document("$first", "$currency"))
				.append("currencySymbol", new Document("$first", "$currencyDetails.symbol"))
				.append("forwarderName", new Document("$first", "$forwarderName"))
				.append("remitter", new Document("$first", "$remitter"))
				.append("invoiceNo", new Document("$first", "$invoiceNo"))
				.append("amount", new Document("$first", "$amount"))
				.append("dueDate", new Document("$first", "$dueDate"))
				// .append("approvePaymentStatus", new Document("$first", "$paymentApprove"))
				.append("invoiceAttachmentFilename", new Document("$first", "$invoiceAttachmentFilename"))
				.append("invoiceAttachmentDiskFilename", new Document("$first", "$invoiceAttachmentDiskFilename"))
				.append("type", new Document("$first", Constants.INVOICE_TYPE_FORWARDER))
				.append("metaData", new Document("$first", "$metaData"))
				.append("totalAmount", new Document("$first", "$totalAmount"))

		);
		ProjectionOperation project = Aggregation.project().andInclude("approvePaymentItems", "invoiceNo",
				"invoiceUpload", "forwarderName", "remitter", "refNo", "invoiceDate", "dueDate", "chassisNo", "amount",
				"remarks", "status", "currency", "yardIn", "yardOut", "paymentApprove", "approvePaymentDetails", "type",
				"invoiceAttachmentFilename", "invoiceAttachmentDiskFilename", "currencySymbol", "metaData",
				"totalAmount");

		Aggregation aggregation = Aggregation.newAggregation(match, lookupForwarder, lookupStock,
				Aggregation.unwind("$stockDetails", true), lookupCurrency, Aggregation.unwind("$currencyDetails", true),
				Aggregation.unwind("$forwarderDetails", true), groupOperation, project);

		AggregationResults<TStoragePhotosApprovalDto> result = this.mongoTemplate.aggregate(aggregation,
				"t_frwrdr_invc", TStoragePhotosApprovalDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<TStoragePhotosApprovalDto> findAllByPaymentStorageCompleted() {
		MatchOperation match = Aggregation.match(Criteria.where("paymentApprove").in(Constants.PAYMENT_COMPLETED,
				Constants.PAYMENT_PARTIAL, Constants.PAYMENT_CANCELED));
		LookupOperation lookupForwarder = LookupOperation.newLookup().from("m_frwrdr").localField("remitter")
				.foreignField("code").as("forwarderDetails");
		LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stockDetails");
		LookupOperation lookupCurrency = LookupOperation.newLookup().from("m_currency").localField("currency")
				.foreignField("currencySeq").as("currencyDetails");
		AggregationOperation groupOperation = context -> new Document("$group", new Document("_id", "$invoiceNo")
				.append("approvePaymentItems",
						new Document("$push",
								new BasicDBObject("invoiceNo", "$invoiceNo").append("stockNo", "$stockNo")
										.append("chassisNo", "$stockDetails.chassisNo").append("remarks", "$remarks")
										.append("currencySymbol", "$currencyDetails.symbol")
										.append("chargesList", "$chargesList").append("metaData", "$metaData")))
				.append("invoiceDate", new Document("$first", "$invoiceDate"))
				.append("invoiceUpload", new Document("$first", "$invoiceUpload"))
				.append("currency", new Document("$first", "$currency"))
				.append("currencySymbol", new Document("$first", "$currencyDetails.symbol"))
				.append("forwarderName", new Document("$first", "$forwarderName"))
				.append("remitter", new Document("$first", "$remitter"))
				.append("invoiceNo", new Document("$first", "$invoiceNo"))
				.append("refNo", new Document("$first", "$refNo"))
				.append("category", new Document("$first", "$category"))
				.append("metaData", new Document("$first", "$metaData"))
				.append("totalAmount", new Document("$first", "$totalAmount"))
				.append("amount", new Document("$first", "$amount"))
				.append("dueDate", new Document("$first", "$dueDate"))
				.append("approvePaymentStatus", new Document("$first", "$paymentApprove"))
				.append("invoiceAttachmentFilename", new Document("$first", "$invoiceAttachmentFilename"))
				.append("invoiceAttachmentDiskFilename", new Document("$first", "$invoiceAttachmentDiskFilename"))
				.append("type", new Document("$first", Constants.INVOICE_TYPE_FORWARDER))
				.append("invoiceAmountReceived", new Document("$first", "$invoiceAmountReceived"))

		);
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
		ProjectionOperation project = Aggregation.project().andInclude("approvePaymentItems", "invoiceNo",
				"paymentVoucherNo", "invoiceUpload", "forwarderName", "remitter", "category", "refNo", "invoiceDate",
				"dueDate", "stockNo", "remarks", "status", "currency", "yardIn", "yardOut", "approvePaymentStatus",
				"approvePaymentDetails", "invoiceAttachmentFilename", "invoiceAttachmentDiskFilename", "type",
				"invoiceAmountReceived", "metaData", "totalAmount", "currencySymbol");

		Aggregation aggregation = Aggregation.newAggregation(match, lookupForwarder,
				Aggregation.unwind("$forwarderDetails", true), lookupStock, Aggregation.unwind("$stockDetails", true),
				lookupCurrency, Aggregation.unwind("$currencyDetails", true), groupOperation, lookupInvoiceTransaction,
				filterTransaction, paymentVoucherNo, project);

		AggregationResults<TStoragePhotosApprovalDto> result = this.mongoTemplate.aggregate(aggregation,
				"t_frwrdr_invc", TStoragePhotosApprovalDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<PayableAmountDto> getPayableAmountsForRemitters() {
		MatchOperation match = Aggregation
				.match(Criteria.where("paymentApprove").in(Constants.PAYMENT_NOT_APPROVED, Constants.PAYMENT_APPROVED));
		LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stockDetails");
		AggregationOperation groupOperation = context -> new Document("$group", new Document("_id", "$remitter")
				.append("paymentItems",
						new Document("$push",
								new BasicDBObject("stockNo", "$stockNo").append("chassisNo", "$stockDetails.chassisNo")
										.append("category", "$category").append("amount", "$amount")))
				.append("remitter", new Document("$first", "$remitter")));

		LookupOperation lookupForwarder = LookupOperation.newLookup().from("m_frwrdr").localField("remitter")
				.foreignField("code").as("forwarder");

		ProjectionOperation project = Aggregation.project().andInclude("paymentItems").and("$forwarder.name")
				.as("remitter").and("$remitter").as("sequenceId")
				.and(AccumulatorOperators.Sum.sumOf("paymentItems.amount")).as("grandTotal");

		Aggregation aggregation = Aggregation.newAggregation(match, lookupStock,
				Aggregation.unwind("$stockDetails", true), groupOperation, lookupForwarder,
				Aggregation.unwind("$forwarder", true), project);
		AggregationResults<PayableAmountDto> result = this.mongoTemplate.aggregate(aggregation, "t_frwrdr_invc",
				PayableAmountDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<PaymentTrackingDto> purchasepaymentTracking(String remitter, Date fromDate, Date toDate) {
		final List<Criteria> andCriterias = new ArrayList<>();
		boolean isValid = false;
		if (!AppUtil.isObjectEmpty(remitter)) {
			isValid = true;
			andCriterias.add(Criteria.where("remitter").is(remitter));
		}

		if (!AppUtil.isObjectEmpty(fromDate)) {
			isValid = true;
			andCriterias
					.add(new Criteria().andOperator(Criteria.where("invoiceDate").gte(AppUtil.atStartOfDay(fromDate)),
							Criteria.where("invoiceDate").lte(AppUtil.atEndOfDay(toDate))));
		}
		if (!isValid) {
			return new ArrayList<>();
		}
		final Criteria matchCriteria = new Criteria();
		matchCriteria.andOperator(andCriterias.toArray(new Criteria[0]));
		final MatchOperation match = Aggregation.match(matchCriteria);
		LookupOperation lookupForwarder = LookupOperation.newLookup().from("m_frwrdr").localField("remitter")
				.foreignField("code").as("forwarderDetails");
		LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stockDetails");
		LookupOperation lookupCurrency = LookupOperation.newLookup().from("m_currency").localField("currency")
				.foreignField("currencySeq").as("currencyDetails");
		AggregationOperation groupOperation = context -> new Document("$group", new Document("_id", "$invoiceNo")
				.append("approvePaymentItems",
						new Document("$push",
								new BasicDBObject("invoiceNo", "$invoiceNo").append("stockNo", "$stockNo")
										.append("chassisNo", "$stockDetails.chassisNo").append("remarks", "$remarks")
										.append("currencySymbol", "$currencyDetails.symbol")))
				.append("type", new Document("$first", Constants.INVOICE_TYPE_FORWARDER))
				.append("invoiceUpload", new Document("$first", "$invoiceUpload"))
				.append("invoiceNo", new Document("$first", "$invoiceNo"))
				.append("refNo", new Document("$first", "$refNo"))
				.append("currency", new Document("$first", "$currency"))
				.append("currencySymbol", new Document("$first", "$currencyDetails.symbol"))
				.append("invoiceName", new Document("$first", "$forwarderName"))
				.append("paymentApproveStatus", new Document("$first", "$paymentApprove"))
				.append("approvedDate", new Document("$first", "$approvedDate"))
				.append("invoiceDate", new Document("$first", "$invoiceDate"))
				.append("approvedBy", new Document("$first", "$approvedBy"))
				.append("paidAmount", new Document("$first", "$invoiceAmountReceived"))
				.append("totalAmount", new Document("$first", "$totalAmount"))
				.append("invoiceAttachmentFilename", new Document("$first", "$invoiceAttachmentFilename"))
				.append("invoiceAttachmentDiskFilename", new Document("$first", "$invoiceAttachmentDiskFilename")));
		LookupOperation lookupUser = LookupOperation.newLookup().from("m_usr").localField("approvedBy")
				.foreignField("code").as("userDetails");
		ProjectionOperation project = Aggregation.project()
				.andInclude("approvePaymentItems", "type", "invoiceNo", "invoiceUpload", "invoiceName",
						"paymentApproveStatus", "approvedDate", "invoiceDate", "paidAmount",
						"invoiceAttachmentFilename", "invoiceAttachmentDiskFilename", "refNo", "totalAmount",
						"currency", "currencySymbol")

				.and("$userDetails.fullname").as("approvedBy");

		Aggregation aggregation = Aggregation.newAggregation(match, lookupForwarder, lookupStock,
				Aggregation.unwind("$forwarderDetails", true), lookupCurrency,
				Aggregation.unwind("$currencyDetails", true), groupOperation, lookupUser,
				Aggregation.unwind("$userDetails", true), project);
		AggregationResults<PaymentTrackingDto> result = this.mongoTemplate.aggregate(aggregation, "t_frwrdr_invc",
				PaymentTrackingDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<PaymentTrackingReportDto> purchasepaymentTrackingReport(String remitter, Date fromDate, Date toDate) {
		final List<Criteria> andCriterias = new ArrayList<>();
		boolean isValid = false;
		if (!AppUtil.isObjectEmpty(remitter)) {
			isValid = true;
			andCriterias.add(Criteria.where("remitter").is(remitter));
		}

		if (!AppUtil.isObjectEmpty(fromDate)) {
			isValid = true;
			andCriterias
					.add(new Criteria().andOperator(Criteria.where("invoiceDate").gte(AppUtil.atStartOfDay(fromDate)),
							Criteria.where("invoiceDate").lte(AppUtil.atEndOfDay(toDate))));
		}
		if (!isValid) {
			return new ArrayList<>();
		}
		final Criteria matchCriteria = new Criteria();
		matchCriteria.andOperator(andCriterias.toArray(new Criteria[0]));
		final MatchOperation match = Aggregation.match(matchCriteria);
		LookupOperation lookupForwarder = LookupOperation.newLookup().from("m_frwrdr").localField("remitter")
				.foreignField("code").as("forwarderDetails");
		LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stockDetails");
		AggregationOperation groupOperation = context -> new Document("$group", new Document("_id", "$invoiceNo")
				.append("approvePaymentItems", new Document("$push", new BasicDBObject("invoiceNo", "$invoiceNo")
						.append("stockNo", "$stockNo").append("chassisNo", "$stockDetails.chassisNo")
						.append("remarks", "$remarks")
						.append("amount", new Document("$sum", Arrays.asList("$amount", "$storageTax")))
						.append("shippingCharges",
								new Document("$sum", Arrays.asList("$shippingCharges", "$shippingTax")))
						.append("photoCharges", new Document("$sum", Arrays.asList("$photoCharges", "$photoTax")))
						.append("blAmendCombineCharges",
								new Document("$sum", Arrays.asList("$blAmendCombineCharges", "$blAmendCombineTax")))
						.append("radiationCharges",
								new Document("$sum", Arrays.asList("$radiationCharges", "$radiationTax")))
						.append("repairCharges", new Document("$sum", Arrays.asList("$repairCharges", "$repairTax")))
						.append("yardHandlingCharges",
								new Document("$sum", Arrays.asList("$yardHandlingCharges", "$yardHandlingTax")))
						.append("inspectionCharges",
								new Document("$sum", Arrays.asList("$inspectionCharges", "$inspectionTax")))
						.append("transportCharges",
								new Document("$sum", Arrays.asList("$transportCharges", "$transportTax")))
						.append("freightCharges", new Document("$sum", Arrays.asList("$freightCharges", "$freightTax")))
						.append("totalAmount",
								new Document("$sum", Arrays.asList("$amount", "$storageTax", "$shippingCharges",
										"$shippingTax", "$photoCharges", "$photoTax", "$blAmendCombineCharges",
										"$blAmendCombineTax", "$radiationCharges", "$radiationTax", "$repairCharges",
										"$repairTax", "$yardHandlingCharges", "$yardHandlingTax", "$inspectionCharges",
										"$inspectionTax", "$transportCharges", "$transportTax", "$freightCharges",
										"$freightTax")))))
				.append("invoiceNo", new Document("$first", "$invoiceNo"))
				.append("invoiceName", new Document("$first", "$forwarderDetails.name"))
				.append("paymentApproveStatus", new Document("$first", "$paymentApprove"))
				.append("approvedDate", new Document("$first", "$approvedDate"))
				.append("invoiceDate", new Document("$first", "$invoiceDate"))
				.append("approvedBy", new Document("$first", "$approvedBy"))
				.append("paidAmount", new Document("$first", "$invoiceAmountReceived")));

		final AggregationExpression totalAmount = contexts -> new Document("$sum", "$approvePaymentItems.totalAmount");
		final AggregationExpression balanceAmount = contexts -> new Document("$subtract",
				Arrays.asList(new Document("$sum", "$approvePaymentItems.totalAmount"), "$paidAmount"));
		ProjectionOperation project = Aggregation.project()
				.andInclude("invoiceNo", "invoiceName", "paymentApproveStatus", "invoiceDate", "paidAmount")
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

		Aggregation aggregation = Aggregation.newAggregation(match, lookupForwarder, lookupStock,
				Aggregation.unwind("$forwarderDetails", true), groupOperation, project, lookupTransaction,
				filterTransactions, Aggregation.unwind("$transaction", true), project2);
		AggregationResults<PaymentTrackingReportDto> result = this.mongoTemplate.aggregate(aggregation, "t_frwrdr_invc",
				PaymentTrackingReportDto.class);
		return result.getMappedResults();
	}

	@Override
	public TStoragePhotosApprovalDto findStorageAndPhotosDateByInvoiceNo(String invoiceNo) {
		MatchOperation match = Aggregation.match(Criteria.where("invoiceNo").is(invoiceNo));
		LookupOperation lookupForwarder = LookupOperation.newLookup().from("m_frwrdr").localField("remitter")
				.foreignField("code").as("forwarderDetails");
		LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stockDetails");
		LookupOperation lookupCurrency = LookupOperation.newLookup().from("m_currency").localField("currency")
				.foreignField("currencySeq").as("currencyDetails");
		AggregationOperation groupOperation = context -> new Document("$group", new Document("_id", "$invoiceNo")

				.append("approvePaymentItems",
						new Document("$push",
								new BasicDBObject("invoiceNo", "$invoiceNo").append("stockNo", "$stockNo")
										.append("chassisNo", "$stockDetails.chassisNo").append("remarks", "$remarks")
										.append("currencySymbol", "$currencyDetails.symbol")
										.append("chargesList", "$chargesList").append("metaData", "$metaData")))
				.append("invoiceDate", new Document("$first", "$invoiceDate"))
				.append("invoiceUpload", new Document("$first", "$invoiceUpload"))
				.append("currencySymbol", new Document("$first", "$currencyDetails.symbol"))
				.append("forwarderName", new Document("$first", "$forwarderName"))
				.append("remitter", new Document("$first", "$remitter"))
				.append("invoiceType", new Document("$first", "$invoiceType"))
				.append("invoiceNo", new Document("$first", "$invoiceNo"))
				.append("amount", new Document("$first", "$amount"))
				.append("metaData", new Document("$first", "$metaData"))
				.append("totalAmount", new Document("$first", "$totalAmount"))
				.append("exchangeRate", new Document("$first", "$exchangeRate"))
				.append("dueDate", new Document("$first", "$dueDate"))
				.append("currency", new Document("$first", "$currency"))
				.append("refNo", new Document("$first", "$refNo"))
				.append("invoiceAttachmentFilename", new Document("$first", "$invoiceAttachmentFilename"))
				.append("invoiceAttachmentDiskFilename", new Document("$first", "$invoiceAttachmentDiskFilename"))
				.append("type", new Document("$first", Constants.INVOICE_TYPE_FORWARDER))

		);
		ProjectionOperation project = Aggregation.project().andInclude("approvePaymentItems", "invoiceNo",
				"invoiceUpload", "forwarderName", "remitter", "refNo", "invoiceDate", "dueDate", "chassisNo", "amount",
				"remarks", "status", "currency", "yardIn", "yardOut", "paymentApprove", "approvePaymentDetails", "type",
				"invoiceAttachmentFilename", "invoiceAttachmentDiskFilename", "metaData", "totalAmount",
				"currencySymbol", "invoiceType", "exchangeRate");

		Aggregation aggregation = Aggregation.newAggregation(match, lookupForwarder, lookupStock,
				Aggregation.unwind("$stockDetails", true), lookupCurrency, Aggregation.unwind("$currencyDetails", true),
				Aggregation.unwind("$forwarderDetails", true), groupOperation, project);

		AggregationResults<TStoragePhotosApprovalDto> result = this.mongoTemplate.aggregate(aggregation,
				"t_frwrdr_invc", TStoragePhotosApprovalDto.class);
		return result.getUniqueMappedResult();
	}
}
