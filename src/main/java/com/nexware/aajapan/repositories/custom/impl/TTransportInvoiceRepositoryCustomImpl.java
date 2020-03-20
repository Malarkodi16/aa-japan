package com.nexware.aajapan.repositories.custom.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AccumulatorOperators;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationExpression;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.CountOperation;
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
import com.nexware.aajapan.dto.PayableAmountDto;
import com.nexware.aajapan.dto.PaymentTrackingDto;
import com.nexware.aajapan.dto.PaymentTrackingReportDto;
import com.nexware.aajapan.dto.TTransportInvoiceDto;
import com.nexware.aajapan.dto.TTransportInvoiceStockDto;
import com.nexware.aajapan.repositories.custom.TTransportInvoiceRepositoryCustom;
import com.nexware.aajapan.utils.AppUtil;

public class TTransportInvoiceRepositoryCustomImpl implements TTransportInvoiceRepositoryCustom {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<TTransportInvoiceDto> findAllTranporterInvoice() {
		MatchOperation match = Aggregation.match(Criteria.where("status")
				.in(Constants.TRANSPORT_INVOICE_BOOKED, Constants.TRANSPORT_INVOICE_MISMATCH_AMOUNT,
						Constants.TRANSPORT_INVOICE_MISMATCH_AMOUNT_APPROVED)
				.andOperator(Criteria.where("paymentApprove").is(Constants.PAYMENT_NOT_APPROVED)));

		LookupOperation lookupMasterTransport = LookupOperation.newLookup().from("m_trnsprtr")
				.localField("transporterId").foreignField("code").as("transporterDetail");

		LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stockDetail");

		ProjectionOperation project = Aggregation.project()
				.andInclude("id", "invoiceRefNo", "invoiceNo", "refNo", "orderId", "transporterId", "stockNo", "etd",
						"amount", "dueDate", "tax", "taxAmount", "totalTaxIncluded", "invoiceAttachmentFilename",
						"invoiceAttachmentDiskFilename", "invoiceUpload", "attachementViewed", "status", "invoiceDate",
						"dropLocation", "dropLocationCustom", "pickupLocation", "pickupLocationCustom")
				.and("$transporterDetail.name").as("transporterName").and("$stockDetail.chassisNo").as("chassisNo")
				.and("$stockDetail.maker").as("maker").and("$stockDetail.model").as("model").and("$_id")
				.as("transportInvoiceId");
		LookupOperation lookupMasterPickupLocation = LookupOperation.newLookup().from("m_lctn")
				.localField("pickupLocation").foreignField("code").as("pickupLocationDetails");
		LookupOperation lookupMasterDropLocation = LookupOperation.newLookup().from("m_lctn").localField("dropLocation")
				.foreignField("code").as("dropLocationDetails");
		GroupOperation groupOperation = Aggregation.group("$invoiceRefNo", "$refNo")
				.push(new BasicDBObject("stockNo", "$stockNo").append("maker", "$maker").append("model", "$model")
						.append("chassisNo", "$chassisNo").append("amount", "$amount").append("tax", "$tax")
						.append("taxAmount", "$taxAmount").append("totalAmount", "$totalTaxIncluded")
						.append("status", "$status").append("pickupLocationName", "$pickupLocationDetails.displayName")
						.append("dropLocationName", "$dropLocationDetails.displayName")
						.append("pickupLocationCustom", "$pickupLocationCustom")
						.append("dropLocationCustom", "$dropLocationCustom").append("pickupLocation", "$pickupLocation")
						.append("dropLocation", "$dropLocation"))
				.as("items").first("invoiceNo").as("invoiceNo").first("transportInvoiceId").as("transportInvoiceId")
				.first("refNo").as("refNo").first("invoiceRefNo").as("invoiceRefNo").first("invoiceDate")
				.as("invoiceDate").first("orderId").as("orderId").first("transporterId").as("transporterId")
				.first("etd").as("etd").first("dueDate").as("dueDate").first("transporterName").as("transporterName")
				.first("invoiceAttachmentFilename").as("invoiceAttachmentFilename")
				.first("invoiceAttachmentDiskFilename").as("invoiceAttachmentDiskFilename").first("invoiceUpload")
				.as("invoiceUpload").first("attachementViewed").as("attachementViewed")
				.first(LiteralOperators.Literal.asLiteral(Constants.INVOICE_TYPE_TRANSPORT)).as("type");

		AggregationOperation invoiceTotal = context -> new Document("$addFields",
				new Document("invoiceTotal", new Document("$sum", "$items.totalAmount")));
		SortOperation sort = Aggregation.sort(Direction.ASC, "dueDate");
		Aggregation aggregation = Aggregation.newAggregation(match, lookupMasterTransport,
				Aggregation.unwind("$transporterDetail", true), lookupStock, Aggregation.unwind("$stockDetail", true),
				project, lookupMasterPickupLocation, Aggregation.unwind("$pickupLocationDetails", true),
				lookupMasterDropLocation, Aggregation.unwind("$dropLocationDetails", true), groupOperation,
				invoiceTotal, sort);

		AggregationResults<TTransportInvoiceDto> result = this.mongoTemplate.aggregate(aggregation, "trnsprt_invc",
				TTransportInvoiceDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<TTransportInvoiceDto> findAllTranporterMismatchInvoice() {
		MatchOperation match = Aggregation
				.match(Criteria.where("status").is(Constants.TRANSPORT_INVOICE_MISMATCH_AMOUNT)
						.andOperator(Criteria.where("paymentApprove").is(Constants.PAYMENT_NOT_APPROVED)));

		LookupOperation lookupMasterTransport = LookupOperation.newLookup().from("m_trnsprtr")
				.localField("transporterId").foreignField("code").as("transporterDetail");

		LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stockDetail");

		ProjectionOperation project = Aggregation.project()
				.andInclude("id", "invoiceRefNo", "invoiceNo", "refNo", "orderId", "transporterId", "stockNo", "etd",
						"amount", "dueDate", "tax", "taxAmount", "totalTaxIncluded", "invoiceAttachmentFilename",
						"invoiceAttachmentDiskFilename", "invoiceUpload", "attachementViewed", "status", "invoiceDate",
						"dropLocation", "dropLocationCustom", "pickupLocation", "pickupLocationCustom")
				.and("$transporterDetail.name").as("transporterName").and("$stockDetail.chassisNo").as("chassisNo")
				.and("$stockDetail.maker").as("maker").and("$stockDetail.model").as("model").and("$_id")
				.as("transportInvoiceId");
		LookupOperation lookupMasterPickupLocation = LookupOperation.newLookup().from("m_lctn")
				.localField("pickupLocation").foreignField("code").as("pickupLocationDetails");
		LookupOperation lookupMasterDropLocation = LookupOperation.newLookup().from("m_lctn").localField("dropLocation")
				.foreignField("code").as("dropLocationDetails");
		GroupOperation groupOperation = Aggregation.group("$invoiceRefNo", "$refNo")
				.push(new BasicDBObject("stockNo", "$stockNo").append("maker", "$maker").append("model", "$model")
						.append("chassisNo", "$chassisNo").append("amount", "$amount").append("tax", "$tax")
						.append("taxAmount", "$taxAmount").append("totalAmount", "$totalTaxIncluded")
						.append("status", "$status").append("pickupLocationName", "$pickupLocationDetails.displayName")
						.append("dropLocationName", "$dropLocationDetails.displayName")
						.append("pickupLocationCustom", "$pickupLocationCustom")
						.append("dropLocationCustom", "$dropLocationCustom").append("pickupLocation", "$pickupLocation")
						.append("dropLocation", "$dropLocation"))
				.as("items").first("invoiceNo").as("invoiceNo").first("transportInvoiceId").as("transportInvoiceId")
				.first("refNo").as("refNo").first("invoiceDate").as("invoiceDate").first("orderId").as("orderId")
				.first("transporterId").as("transporterId").first("etd").as("etd").first("dueDate").as("dueDate")
				.first("transporterName").as("transporterName").first("invoiceAttachmentFilename")
				.as("invoiceAttachmentFilename").first("invoiceAttachmentDiskFilename")
				.as("invoiceAttachmentDiskFilename").first("invoiceUpload").as("invoiceUpload")
				.first("attachementViewed").as("attachementViewed")
				.first(LiteralOperators.Literal.asLiteral(Constants.INVOICE_TYPE_TRANSPORT)).as("type");

		AggregationOperation invoiceTotal = context -> new Document("$addFields",
				new Document("invoiceTotal", new Document("$sum", "$items.totalAmount")));
		SortOperation sort = Aggregation.sort(Direction.ASC, "dueDate");
		Aggregation aggregation = Aggregation.newAggregation(match, lookupMasterTransport,
				Aggregation.unwind("$transporterDetail", true), lookupStock, Aggregation.unwind("$stockDetail", true),
				project, lookupMasterPickupLocation, Aggregation.unwind("$pickupLocationDetails", true),
				lookupMasterDropLocation, Aggregation.unwind("$dropLocationDetails", true), groupOperation,
				invoiceTotal, sort);

		AggregationResults<TTransportInvoiceDto> result = this.mongoTemplate.aggregate(aggregation, "trnsprt_invc",
				TTransportInvoiceDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<ApprovePaymentsDto> findAllByPaymentApprove(List<Integer> paymentApprove) {
		MatchOperation match = Aggregation.match(Criteria.where("paymentApprove").in(paymentApprove));

		LookupOperation lookupTransporter = LookupOperation.newLookup().from("m_trnsprtr").localField("transporterId")
				.foreignField("code").as("transporterDetails");
		LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stockDetails");
		Document taxamount = new Document("$multiply",
				Arrays.asList("$amount", new Document("$divide", Arrays.asList("$tax", 100))));
		AggregationOperation groupOperation = context -> new Document("$group",
				new Document("_id", "$invoiceRefNo")
						.append("approvePaymentItems",
								new Document("$push",
										new BasicDBObject("invoiceNo", "$invoiceNo").append("stockNo", "$stockNo")
												.append("chassisNo", "$stockDetails.chassisNo")
												.append("maker", "$stockDetails.maker")
												.append("model", "$stockDetails.model").append("taxamount", taxamount)
												.append("taxtotalamount",
														new Document("$add", Arrays.asList("$amount", taxamount)))
												.append("amount", "$amount")))
						.append("createdDate", new Document("$first", "$invoiceDate"))
						.append("invoiceNo", new Document("$first", "$invoiceRefNo"))
						.append("refNo", new Document("$first", "$refNo"))
						.append("type", new Document("$first", Constants.INVOICE_TYPE_TRANSPORT))
						.append("dueDate", new Document("$first", "$dueDate"))
						.append("supplierId", new Document("$first", "$transporterDetails.code"))
						.append("invoiceAttachmentFilename", new Document("$first", "$invoiceAttachmentFilename"))
						.append("invoiceAttachmentDiskFilename",
								new Document("$first", "$invoiceAttachmentDiskFilename"))
						.append("invoiceName", new Document("$first", "$transporterDetails.name"))
						.append("invoiceAmountReceived", new Document("$first", "$invoiceAmountReceived")));
		ProjectionOperation project = Aggregation.project()
				.andInclude("approvePaymentItems", "createdDate", "type", "invoiceNo", "refNo", "invoiceName",
						"dueDate", "invoiceAttachmentFilename", "invoiceAttachmentDiskFilename",
						"invoiceAmountReceived", "supplierId")
				.and(AccumulatorOperators.Sum.sumOf("approvePaymentItems.taxtotalamount")).as("totalAmount");

		Aggregation aggregation = Aggregation.newAggregation(match, lookupTransporter, lookupStock,
				Aggregation.unwind("$stockDetails", true), Aggregation.unwind("$transporterDetails", true),
				groupOperation, project);
		AggregationResults<ApprovePaymentsDto> result = this.mongoTemplate.aggregate(aggregation, "trnsprt_invc",
				ApprovePaymentsDto.class);
		return result.getMappedResults();
	}

	@Override
	public Long getCountTransportData(Integer paymentApprove) {
		MatchOperation match = Aggregation
				.match(new Criteria().andOperator(Criteria.where("status").is(Constants.TRANSPORT_INVOICE_NOT_BOOKED),
						Criteria.where("paymentApprove").is(paymentApprove)));

		CountOperation count = Aggregation.count().as("count");
		Aggregation aggregation = Aggregation.newAggregation(match, count);
		AggregationResults<Map> result = this.mongoTemplate.aggregate(aggregation, "trnsprt_invc", Map.class);
		return Long.valueOf(!AppUtil.isObjectEmpty(result.getUniqueMappedResult())
				? result.getUniqueMappedResult().get("count").toString()
				: "0");
	}

	@Override
	public Long getApprovalCountTransportData() {

		MatchOperation match = Aggregation.match(Criteria.where("status").is(Constants.TRANSPORT_INVOICE_BOOKED)
				.andOperator(Criteria.where("paymentApprove").is(Constants.PAYMENT_NOT_APPROVED)));

		CountOperation count = Aggregation.count().as("count");
		GroupOperation groupOperation = Aggregation.group("$invoiceRefNo");
		Aggregation aggregation = Aggregation.newAggregation(match, groupOperation, count);
		AggregationResults<Map> result = this.mongoTemplate.aggregate(aggregation, "trnsprt_invc", Map.class);
		return Long.valueOf(!AppUtil.isObjectEmpty(result.getUniqueMappedResult())
				? result.getUniqueMappedResult().get("count").toString()
				: "0");
	}

	@Override
	public List<TTransportInvoiceStockDto> findAllTransportCompletedInvoice() {
		MatchOperation matchOperation = Aggregation
				.match(Criteria.where("status").is(Constants.TRANSPORT_INVOICE_NOT_BOOKED));
		LookupOperation lookupMasterTransport = LookupOperation.newLookup().from("m_trnsprtr")
				.localField("transporterId").foreignField("code").as("transporterDetail");
		LookupOperation lookupMasterPickupLocation = LookupOperation.newLookup().from("m_lctn")
				.localField("pickupLocation").foreignField("code").as("pickupLocationDetails");
		LookupOperation lookupMasterDropLocation = LookupOperation.newLookup().from("m_lctn").localField("dropLocation")
				.foreignField("code").as("dropLocationDetails");
		LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stockDetail");
		LookupOperation lookupTransportOrder = LookupOperation.newLookup().from("trnsprt_ordr_items")
				.localField("orderId").foreignField("invoiceNo").as("transportOrder");
		ProjectionOperation project = Aggregation.project()
				.andInclude("transporterId", "stockNo", "amount", "dropLocation", "dropLocationCustom",
						"pickupLocation", "pickupLocationCustom", "tax", "taxAmount")
				.and("totalTaxIncluded").as("totalAmount").and("$transporterDetail.name").as("transporterName")
				.and("$stockDetail.chassisNo").as("chassisNo").and("$stockDetail.purchaseInfo.date").as("purchaseDate")
				.and("$stockDetail.maker").as("maker").and("$stockDetail.model").as("model")
				.and("$pickupLocationDetails.displayName").as("pickupLocationName")
				.and("$dropLocationDetails.displayName").as("dropLocationName").and("$transportOrder.etd")
				.as("arrivalDate").and("$_id").as("invoiceId").and("transportOrder.createdDate").as("createdDate");

		GroupOperation group = Aggregation.group("$stockNo").first("transporterId").as("transporterId").first("amount")
				.as("amount").first("createdDate").as("createdDate").first("dropLocation").as("dropLocation")
				.first("dropLocationCustom").as("dropLocationCustom").first("pickupLocation").as("pickupLocation")
				.first("pickupLocationCustom").as("pickupLocationCustom").first("tax").as("tax").first("taxAmount")
				.as("taxAmount").first("totalAmount").as("totalAmount").first("transporterName").as("transporterName")
				.first("chassisNo").as("chassisNo").first("purchaseDate").as("purchaseDate").first("maker").as("maker")
				.first("model").as("model").first("pickupLocationName").as("pickupLocationName")
				.first("dropLocationName").as("dropLocationName").first("arrivalDate").as("arrivalDate")
				.first("invoiceId").as("invoiceId");
		Aggregation aggregation = Aggregation.newAggregation(matchOperation, lookupMasterTransport,
				Aggregation.unwind("$transporterDetail", true), lookupStock, Aggregation.unwind("$stockDetail", true),
				lookupMasterPickupLocation, Aggregation.unwind("$pickupLocationDetails", true),
				lookupMasterDropLocation, Aggregation.unwind("$dropLocationDetails", true), lookupTransportOrder,
				Aggregation.unwind("$transportOrder", true), project, group);
		AggregationResults<TTransportInvoiceStockDto> result = this.mongoTemplate.aggregate(aggregation, "trnsprt_invc",
				TTransportInvoiceStockDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<ApprovePaymentsDto> findAllByPaymentApproveFreezed(Integer paymentApprove) {
		MatchOperation match = Aggregation.match(Criteria.where("paymentApprove").is(paymentApprove));

		LookupOperation lookupTransporter = LookupOperation.newLookup().from("m_trnsprtr").localField("transporterId")
				.foreignField("code").as("transporterDetails");
		LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stockDetail");
		Document taxamount = new Document("$multiply",
				Arrays.asList("$amount", new Document("$divide", Arrays.asList("$tax", 100))));
		AggregationOperation groupOperation = context -> new Document("$group", new Document("_id", "$invoiceRefNo")
				.append("approvePaymentItems",
						new Document("$push", new BasicDBObject("invoiceNo", "$invoiceNo")
								.append("stockNo", "$stockDetail.stockNo").append("chassisNo", "$stockDetail.chassisNo")
								.append("maker", "$stockDetail.maker").append("model", "$stockDetail.model")
								.append("amount", "$amount").append("tax", "$tax").append("taxAmount", taxamount)
								.append("amount", new Document("$sum", Arrays.asList("$amount")))))
				.append("invoiceDate", new Document("$first", "$createdDate"))
				.append("invoiceUpload", new Document("$first", "$invoiceUpload"))
				.append("invoiceNo", new Document("$first", "$invoiceRefNo"))
				.append("dueDate", new Document("$first", "$dueDate")).append("refNo", new Document("$first", "$refNo"))
				.append("transporterId", new Document("$first", "$transporterDetails.code"))
				.append("invoiceName", new Document("$first", "$transporterDetails.name"))
				.append("invoiceAttachmentFilename", new Document("$first", "$invoiceAttachmentFilename"))
				.append("invoiceAttachmentDiskFilename", new Document("$first", "$invoiceAttachmentDiskFilename"))
				.append("type", new Document("$first", Constants.INVOICE_TYPE_TRANSPORT)));
		ProjectionOperation project = Aggregation.project()
				.andInclude("approvePaymentItems", "invoiceDate", "invoiceNo", "refNo", "invoiceUpload", "invoiceName",
						"dueDate", "type", "invoiceAttachmentFilename", "invoiceAttachmentDiskFilename",
						"transporterId")
				.and(AccumulatorOperators.Sum.sumOf("approvePaymentItems.amount")).as("totalAmount");

		Aggregation aggregation = Aggregation.newAggregation(match, lookupTransporter, lookupStock,
				Aggregation.unwind("$stockDetail", true), Aggregation.unwind("$transporterDetails", true),
				groupOperation, project);
		AggregationResults<ApprovePaymentsDto> result = this.mongoTemplate.aggregate(aggregation, "trnsprt_invc",
				ApprovePaymentsDto.class);
		return result.getMappedResults();
	}

	@Override
	public Long getFreezedCountTransportData(List<Integer> paymentApprove) {

		MatchOperation match = Aggregation.match(Criteria.where("paymentApprove").in(paymentApprove));

		CountOperation count = Aggregation.count().as("count");
		GroupOperation groupOperation = Aggregation.group("$invoiceRefNo");
		Aggregation aggregation = Aggregation.newAggregation(match, groupOperation, count);
		AggregationResults<Map> result = this.mongoTemplate.aggregate(aggregation, "trnsprt_invc", Map.class);
		return Long.valueOf(!AppUtil.isObjectEmpty(result.getUniqueMappedResult())
				? result.getUniqueMappedResult().get("count").toString()
				: "0");
	}

	@Override
	public List<TTransportInvoiceDto> findAllTransportPaymentCompletedInvoice() {
		MatchOperation match = Aggregation.match(Criteria.where("paymentApprove").in(Constants.PAYMENT_COMPLETED,
				Constants.PAYMENT_PARTIAL, Constants.PAYMENT_CANCELED));

		LookupOperation lookupMasterTransport = LookupOperation.newLookup().from("m_trnsprtr")
				.localField("transporterId").foreignField("code").as("transporterDetail");

		LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stockDetail");

		ProjectionOperation project = Aggregation.project()
				.andInclude("id", "invoiceRefNo", "invoiceNo", "refNo", "orderId", "transporterId", "stockNo", "etd",
						"amount", "dueDate", "tax", "invoiceAttachmentFilename", "invoiceAttachmentDiskFilename",
						"invoiceDate")
				.and("$transporterDetail.name").as("transporterName").and("$stockDetail.chassisNo").as("chassisNo")
				.and("$stockDetail.maker").as("maker").and("$stockDetail.model").as("model").and("$_id")
				.as("transportInvoiceId").and("$paymentApprove").as("paymentApprove");
		Document taxamount = new Document("$multiply",
				Arrays.asList("$amount", new Document("$divide", Arrays.asList("$tax", 100))));
		GroupOperation groupOperation = Aggregation.group("$invoiceRefNo")
				.push(new BasicDBObject("stockNo", "$stockNo").append("maker", "$maker").append("model", "$model")
						.append("chassisNo", "$chassisNo").append("amount", "$amount").append("tax", "$tax")
						.append("taxAmount", taxamount)
						.append("totalAmount", new Document("$add", Arrays.asList("$amount", taxamount)))

				).as("items").first("invoiceNo").as("invoiceNo").first("invoiceRefNo").as("invoiceRefNo")
				.first("transportInvoiceId").as("transportInvoiceId").first("paymentApprove").as("paymentApprove")
				.first("refNo").as("refNo").first("orderId").as("orderId").first("transporterId").as("transporterId")
				.first("etd").as("etd").first("dueDate").as("dueDate").first("transporterName").as("transporterName")
				.first("invoiceAttachmentFilename").as("invoiceAttachmentFilename")
				.first("invoiceAttachmentDiskFilename").as("invoiceAttachmentDiskFilename").first("invoiceDate")
				.as("invoiceDate").first(LiteralOperators.Literal.asLiteral(Constants.INVOICE_TYPE_TRANSPORT))
				.as("type");

		final LookupOperation lookupInvoiceTransaction = LookupOperation.newLookup().from("t_invc_pymnt_trnsctn")
				.localField("invoiceRefNo").foreignField("invoiceNo").as("invoiceTransaction");

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
				new Document("invoiceTotal", new Document("$sum", "$items.totalAmount")));

		SortOperation sort = Aggregation.sort(Direction.ASC, "dueDate");
		Aggregation aggregation = Aggregation.newAggregation(match, lookupMasterTransport,
				Aggregation.unwind("$transporterDetail", true), lookupStock, Aggregation.unwind("$stockDetail", true),
				project, groupOperation, lookupInvoiceTransaction, filterTransaction, paymentVoucherNo, invoiceTotal,
				sort);

		AggregationResults<TTransportInvoiceDto> result = this.mongoTemplate.aggregate(aggregation, "trnsprt_invc",
				TTransportInvoiceDto.class);
		return result.getMappedResults();
	}

	@Override
	public Long getPaymentApprovalCountTransportData() {

		MatchOperation match = Aggregation.match(Criteria.where("paymentApprove").in(Constants.PAYMENT_COMPLETED,
				Constants.PAYMENT_PARTIAL, Constants.PAYMENT_CANCELED));

		CountOperation count = Aggregation.count().as("count");
		GroupOperation groupOperation = Aggregation.group("$invoiceRefNo");
		Aggregation aggregation = Aggregation.newAggregation(match, groupOperation, count);
		AggregationResults<Map> result = this.mongoTemplate.aggregate(aggregation, "trnsprt_invc", Map.class);
		return Long.valueOf(!AppUtil.isObjectEmpty(result.getUniqueMappedResult())
				? result.getUniqueMappedResult().get("count").toString()
				: "0");
	}

	@Override
	public List<PayableAmountDto> getPayableAmountsForRemitters() {
		MatchOperation match = Aggregation
				.match(Criteria.where("paymentApprove").in(Constants.PAYMENT_NOT_APPROVED, Constants.PAYMENT_APPROVED));

		Document taxamount = new Document("$multiply",
				Arrays.asList("$amount", new Document("$divide", Arrays.asList("$tax", 100))));

		AggregationOperation groupOperation = context -> new Document("$group", new Document("_id", "$transporterId")
				.append("paymentItems",
						new Document("$push",
								new BasicDBObject("stockNo", "$stockNo").append("amount", "$amount")
										.append("tax", "$tax").append("taxAmount", taxamount).append("totalAmount",
												new Document("$add", Arrays.asList("$amount", taxamount)))))
				.append("transporterId", new Document("$first", "$transporterId")));

		LookupOperation lookupMasterTransport = LookupOperation.newLookup().from("m_trnsprtr")
				.localField("transporterId").foreignField("code").as("transporterDetail");

		ProjectionOperation project = Aggregation.project().andInclude("paymentItems").and("$transporterDetail.name")
				.as("remitter").and("$transporterId").as("sequenceId")
				.and(AccumulatorOperators.Sum.sumOf("paymentItems.totalAmount")).as("grandTotal");

		Aggregation aggregation = Aggregation.newAggregation(match, groupOperation, lookupMasterTransport,
				Aggregation.unwind("$transporterDetail", true), project);
		AggregationResults<PayableAmountDto> result = this.mongoTemplate.aggregate(aggregation, "trnsprt_invc",
				PayableAmountDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<PaymentTrackingDto> purchasepaymentTracking(List<Integer> invoiceStatus, String remitter, Date fromDate,
			Date toDate) {
		final List<Criteria> andCriterias = new ArrayList<>();
		andCriterias.add(Criteria.where("status").in(invoiceStatus));
		boolean isValid = false;
		if (!AppUtil.isObjectEmpty(remitter)) {
			isValid = true;
			andCriterias.add(Criteria.where("transporterId").is(remitter));
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

		LookupOperation lookupTransporter = LookupOperation.newLookup().from("m_trnsprtr").localField("transporterId")
				.foreignField("code").as("transporterDetails");
		LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stockDetails");
		Document taxamount = new Document("$multiply",
				Arrays.asList("$amount", new Document("$divide", Arrays.asList("$tax", 100))));
		AggregationOperation groupOperation = context -> new Document("$group",
				new Document("_id", "$invoiceRefNo")
						.append("approvePaymentItems",
								new Document("$push",
										new BasicDBObject("invoiceNo", "$invoiceNo").append("stockNo", "$stockNo")
												.append("chassisNo", "$stockDetails.chassisNo")
												.append("maker", "$stockDetails.maker")
												.append("model", "$stockDetails.model").append("taxamount", taxamount)
												.append("taxtotalamount",
														new Document("$add", Arrays.asList("$amount", taxamount)))
												.append("amount", "$amount")))
						.append("invoiceUpload", new Document("$first", "$invoiceUpload"))
						.append("invoiceNo", new Document("$first", "$invoiceRefNo"))
						.append("refNo", new Document("$first", "$refNo"))
						.append("paymentApproveStatus", new Document("$first", "$paymentApprove"))
						.append("invoiceName", new Document("$first", "$transporterDetails.name"))
						.append("type", new Document("$first", Constants.INVOICE_TYPE_TRANSPORT))
						.append("approvedDate", new Document("$first", "$approvedDate"))
						.append("invoiceDate", new Document("$first", "$createdDate"))
						.append("approvedBy", new Document("$first", "$approvedBy"))
						.append("paidAmount", new Document("$first", "$invoiceAmountReceived"))
						.append("invoiceAttachmentFilename", new Document("$first", "$invoiceAttachmentFilename"))
						.append("invoiceAttachmentDiskFilename",
								new Document("$first", "$invoiceAttachmentDiskFilename")));
		LookupOperation lookupUser = LookupOperation.newLookup().from("m_usr").localField("approvedBy")
				.foreignField("code").as("userDetails");
		ProjectionOperation project = Aggregation.project()
				.andInclude("approvePaymentItems", "type", "invoiceNo", "invoiceUpload", "invoiceName",
						"paymentApproveStatus", "approvedDate", "invoiceDate", "paidAmount",
						"invoiceAttachmentFilename", "invoiceAttachmentDiskFilename", "refNo")
				.and(AccumulatorOperators.Sum.sumOf("approvePaymentItems.taxtotalamount")).as("totalAmount")
				.and("$userDetails.fullname").as("approvedBy");

		Aggregation aggregation = Aggregation.newAggregation(match, lookupTransporter, lookupStock,
				Aggregation.unwind("$stockDetails", true), Aggregation.unwind("$transporterDetails", true),
				groupOperation, lookupUser, Aggregation.unwind("$userDetails", true), project);
		AggregationResults<PaymentTrackingDto> result = this.mongoTemplate.aggregate(aggregation, "trnsprt_invc",
				PaymentTrackingDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<PaymentTrackingReportDto> purchasepaymentTrackingReport(List<Integer> invoiceStatus, String remitter,
			Date fromDate, Date toDate) {
		final List<Criteria> andCriterias = new ArrayList<>();
		andCriterias.add(Criteria.where("status").in(invoiceStatus));
		boolean isValid = false;
		if (!AppUtil.isObjectEmpty(remitter)) {
			isValid = true;
			andCriterias.add(Criteria.where("transporterId").is(remitter));
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

		LookupOperation lookupTransporter = LookupOperation.newLookup().from("m_trnsprtr").localField("transporterId")
				.foreignField("code").as("transporterDetails");
		LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stockDetails");
		Document taxamount = new Document("$multiply",
				Arrays.asList("$amount", new Document("$divide", Arrays.asList("$tax", 100))));
		AggregationOperation groupOperation = context -> new Document("$group",
				new Document("_id", "$invoiceRefNo")
						.append("approvePaymentItems",
								new Document("$push",
										new BasicDBObject("invoiceNo", "$invoiceNo").append("stockNo", "$stockNo")
												.append("chassisNo", "$stockDetails.chassisNo")
												.append("maker", "$stockDetails.maker")
												.append("model", "$stockDetails.model").append("taxamount", taxamount)
												.append("taxtotalamount",
														new Document("$add", Arrays.asList("$amount", taxamount)))
												.append("amount", "$amount")))
						.append("invoiceNo", new Document("$first", "$invoiceRefNo"))
						.append("paymentApproveStatus", new Document("$first", "$paymentApprove"))
						.append("transporterId", new Document("$first", "$transporterId"))
						.append("invoiceName", new Document("$first", "$transporterDetails.name"))
						.append("approvedDate", new Document("$first", "$approvedDate"))
						.append("invoiceDate", new Document("$first", "$createdDate"))
						.append("approvedBy", new Document("$first", "$approvedBy"))
						.append("paidAmount", new Document("$first", "$invoiceAmountReceived")));

		final AggregationExpression totalAmount = contexts -> new Document("$sum",
				"$approvePaymentItems.taxtotalamount");
		final AggregationExpression balanceAmount = contexts -> new Document("$subtract",
				Arrays.asList(new Document("$sum", "$approvePaymentItems.taxtotalamount"), "$paidAmount"));
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
		Aggregation aggregation = Aggregation.newAggregation(match, lookupTransporter,
				Aggregation.unwind("$transporterDetails", true), groupOperation, project1, lookupTransaction,
				filterTransactions, Aggregation.unwind("$transaction", true), project2);
		AggregationResults<PaymentTrackingReportDto> result = this.mongoTemplate.aggregate(aggregation, "trnsprt_invc",
				PaymentTrackingReportDto.class);
		return result.getMappedResults();
	}
}
