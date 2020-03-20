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
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.BasicDBObject;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.ApprovePaymentsDto;
import com.nexware.aajapan.dto.PaymentTrackingDto;
import com.nexware.aajapan.dto.PaymentTrackingReportDto;
import com.nexware.aajapan.dto.TFreightShippingContainerInvoiceDto;
import com.nexware.aajapan.dto.TFreightShippingInvoiceDto;
import com.nexware.aajapan.dto.TFreightShippingRadiationDto;
import com.nexware.aajapan.models.TFreightShippingInvoice;
import com.nexware.aajapan.repositories.custom.TFreightShippingInvoiceRepositoryCustom;
import com.nexware.aajapan.utils.AppUtil;

public class TFreightShippingInvoiceRepositoryCustomImpl implements TFreightShippingInvoiceRepositoryCustom {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void updateFreightExchange(String invoiceNo, Date invoiceDate, String exchangeRate,
			List<String> shipmentRequestId) {
		final Update update = new Update().set("invoiceNo", invoiceNo).set("invoiceDate", invoiceDate)
				.set("exchangeRate", exchangeRate);

		mongoTemplate.updateMulti(Query.query(Criteria.where("shipmentRequestId").in(shipmentRequestId)), update,
				TFreightShippingInvoice.class);
	}

	@Override
	public List<ApprovePaymentsDto> findAllByPaymentApprove(List<Integer> paymentApprove) {
		final MatchOperation match = Aggregation.match(Criteria.where("paymentApprove").in(paymentApprove));
		final LookupOperation lookupForwarder = LookupOperation.newLookup().from("m_frwrdr").localField("forwarder")
				.foreignField("code").as("forwarder");
		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");
		final LookupOperation lookupInvoiceItems = LookupOperation.newLookup().from("t_shipping_invoice_items")
				.localField("invoiceNo").foreignField("invoiceNo").as("invoiceItems");
		final AggregationOperation groupOperation = context -> new Document("$group", new Document("_id", "$invoiceNo")
				.append("approvePaymentItems", new Document("$push",
						new BasicDBObject("invoiceNo", "$invoiceNo").append("stockNo", "$stockNo").append("id", "$_id")
								.append("chassisNo", "$stock.chassisNo").append("amount", "$totalAmount")
								.append("freightCharge", "$freightCharge").append("shippingCharge", "$shippingCharge")
								.append("inspectionCharge", "$inspectionCharge")
								.append("radiationCharge", "$radiationCharge").append("otherCharges", "$otherCharges")
								.append("freightChargeUsd", "$freightChargeUsd").append("exchangeRate", "exchangeRate")
								.append("freightPaymentStatus", "$freightPaymentStatus")
								.append("totalWithFreightInYen",
										new Document("$sum",
												Arrays.asList("$freightCharge", "$shippingCharge", "$inspectionCharge",
														"$radiationCharge", "$otherCharges")))
								.append("totalWithOutFreightInYen",
										new Document("$sum",
												Arrays.asList("$shippingCharge", "$inspectionCharge",
														"$radiationCharge", "$otherCharges")))))
				.append("invoiceNo", new Document("$first", "$invoiceNo"))
				.append("createdDate", new Document("$first", "$createdDate"))
				.append("dueDate", new Document("$first", "$dueDate"))
				.append("supplierId", new Document("$first", "$forwarder.code"))
				.append("invoiceAmountReceived", new Document("$first", "$invoiceAmountReceived"))
				.append("invoiceAttachmentDiskFilename", new Document("$first", "$invoiceAttachmentDiskFilename"))
				.append("attachmentDirectory", new Document("$first", "$attachmentDirectory"))
				.append("type", new Document("$first", Constants.INVOICE_TYPE_FREIGHT))

				.append("invoiceName", new Document("$first", "$forwarder.name"))
				.append("currencyType", new Document("$first", "$currencyType"))
				.append("exchangeRate", new Document("$first", "$exchangeRate"))
				.append("paymentType", new Document("$first", "$paymentType"))
				.append("invoiceType", new Document("$first", "$invoiceType"))
				.append("invoiceAmountReceivedUsd", new Document("$first", "$invoiceAmountReceivedUsd")));
		final ProjectionOperation project = Aggregation.project()
				.andInclude("approvePaymentItems", "invoiceType", "invoiceNo", "createdDate", "invoiceName", "dueDate",
						"supplierId", "invoiceAmountReceived", "invoiceAmountReceivedUsd",
						"invoiceAttachmentDiskFilename", "attachmentDirectory", "type", "paymentType", "currencyType",
						"exchangeRate", "invoiceItems")
				.and(AccumulatorOperators.Sum.sumOf("approvePaymentItems.amount")).as("totalAmount")
				.and(AccumulatorOperators.Sum.sumOf("approvePaymentItems.freightChargeUsd")).as("totalAmountUsd")
				.and(AccumulatorOperators.Sum.sumOf("invoiceItems.usd")).as("containerUsd");

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupForwarder,
				Aggregation.unwind("$forwarder", true), lookupStock, Aggregation.unwind("$stock", true), groupOperation,
				lookupInvoiceItems, project);
		final AggregationResults<ApprovePaymentsDto> result = mongoTemplate.aggregate(aggregation, "t_frght_shpng_invc",
				ApprovePaymentsDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<ApprovePaymentsDto> findAllRoroInvoicesGroup() {
		final MatchOperation match = Aggregation
				.match(Criteria.where("paymentApprove").is(Constants.PAYMENT_NOT_APPROVED)
						.andOperator(Criteria.where("invoiceType").is(Constants.SHIPPING_INVOICE_TYPE_RORO)));
		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");
		final LookupOperation lookupForwarder = LookupOperation.newLookup().from("m_frwrdr").localField("forwarder")
				.foreignField("code").as("forwarder");
		final LookupOperation lookupShippingRequest = LookupOperation.newLookup().from("t_shppng_rqust")
				.localField("shipmentRequestId").foreignField("shipmentRequestId").as("shipping");

		final AggregationOperation groupOperation = context -> new Document("$group", new Document("_id", "$invoiceNo")
				.append("approvePaymentItems",
						new Document("$push", new BasicDBObject("invoiceNo", "$invoiceNo")
								.append("chassisNo", "$stock.chassisNo").append("amount", "$totalAmount")
								.append("freightCharge", "$freightCharge").append("shippingCharge", "$shippingCharge")
								.append("inspectionCharge", "$inspectionCharge")
								.append("radiationCharge", "$radiationCharge").append("otherCharges", "$otherCharges")
								.append("freightChargeUsd", "$freightChargeUsd").append("otherCharge",
										new Document("$sum",
												Arrays.asList("$shippingCharge", "$inspectionCharge",
														"$radiationCharge", "$otherCharges")))))
				.append("invoiceNo", new Document("$first", "$invoiceNo"))
				.append("createdDate", new Document("$first", "$createdDate"))
				.append("dueDate", new Document("$first", "$dueDate"))
				.append("supplierId", new Document("$first", "$forwarder.code"))
				.append("invoiceUpload", new Document("$first", "$invoiceUpload"))
				.append("attachementViewed", new Document("$first", "$attachementViewed"))
				.append("invoiceAttachmentDiskFilename", new Document("$first", "$invoiceAttachmentDiskFilename"))
				.append("type", new Document("$first", Constants.INVOICE_TYPE_FREIGHT))
				.append("totalAmountUsd", new Document("$first", "$totalAmountUsd"))
				.append("invoiceName", new Document("$first", "$forwarder.name")));

		final ProjectionOperation project = Aggregation.project()
				.andInclude("approvePaymentItems", "invoiceNo", "createdDate", "invoiceName", "dueDate", "supplierId",
						"invoiceUpload", "type", "invoiceAttachmentDiskFilename", "attachementViewed")
				.and(AccumulatorOperators.Sum.sumOf("approvePaymentItems.amount")).as("totalAmount")
				.and(AccumulatorOperators.Sum.sumOf("approvePaymentItems.freightChargeUsd")).as("totalAmountUsd")
				.and(AccumulatorOperators.Sum.sumOf("approvePaymentItems.freightCharge")).as("totalFreightAmount")
				.and(AccumulatorOperators.Sum.sumOf("approvePaymentItems.otherCharge")).as("otherTotalAmount");

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupStock,
				Aggregation.unwind("$stock", true), lookupShippingRequest, Aggregation.unwind("$shipping", true),
				lookupForwarder, Aggregation.unwind("$forwarder", true), groupOperation, project);
		final AggregationResults<ApprovePaymentsDto> result = mongoTemplate.aggregate(aggregation, "t_frght_shpng_invc",
				ApprovePaymentsDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<TFreightShippingInvoiceDto> findAllRoroPaymentCompleted() {
		final MatchOperation match = Aggregation
				.match(Criteria.where("paymentApprove").in(Constants.PAYMENT_COMPLETED, Constants.PAYMENT_CANCELED));
		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");
		final LookupOperation lookupForwarder = LookupOperation.newLookup().from("m_frwrdr").localField("forwarder")
				.foreignField("code").as("forwarder");
		final LookupOperation lookupInvoiceItems = LookupOperation.newLookup().from("t_shipping_invoice_items")
				.localField("invoiceNo").foreignField("invoiceNo").as("invoiceItems");
		final AggregationOperation groupOperation = context -> new Document("$group", new Document("_id", "$invoiceNo")
				.append("items", new Document("$push",
						new BasicDBObject("stockNo", "$stockNo").append("chassisNo", "$stock.chassisNo")
								.append("freightCharge", "$freightCharge").append("shippingCharge", "$shippingCharge")
								.append("inspectionCharge", "$inspectionCharge")
								.append("radiationCharge", "$radiationCharge").append("otherCharges", "$otherCharges")
								.append("freightChargeUsd", "$freightChargeUsd").append("amount", "$totalAmount")))
				.append("invoiceNo", new Document("$first", "$invoiceNo"))
				.append("invoiceType", new Document("$first", "$invoiceType"))
				.append("createdDate", new Document("$first", "$createdDate"))
				.append("dueDate", new Document("$first", "$dueDate"))
				.append("invoiceAttachmentDiskFilename", new Document("$first", "$invoiceAttachmentDiskFilename"))
				.append("attachmentDirectory", new Document("$first", "$attachmentDirectory"))
				.append("invoiceName", new Document("$first", "$forwarder.name"))
				.append("supplierId", new Document("$first", "$forwarder.code"))
				.append("paymentApprove", new Document("$first", "$paymentApprove")));

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
		final ProjectionOperation project = Aggregation.project()
				.andInclude("invoiceNo", "createdDate", "invoiceName", "dueDate", "supplierId", "paymentApprove",
						"invoiceItems", "invoiceType", "invoiceAttachmentDiskFilename", "attachmentDirectory",
						"paymentVoucherNo")
				.and("items").as("items").and(AccumulatorOperators.Sum.sumOf("items.amount")).as("totalAmount")
				.and(AccumulatorOperators.Sum.sumOf("items.freightChargeUsd")).as("totalAmountUsd");

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupStock,
				Aggregation.unwind("$stock", true), lookupForwarder, Aggregation.unwind("$forwarder", true),
				groupOperation, lookupInvoiceItems, lookupInvoiceTransaction, filterTransaction, paymentVoucherNo,
				project);
		final AggregationResults<TFreightShippingInvoiceDto> result = mongoTemplate.aggregate(aggregation,
				"t_frght_shpng_invc", TFreightShippingInvoiceDto.class);
		return result.getMappedResults();
	}

	@Override
	public TFreightShippingInvoiceDto findOneRoroPaymentCompleted(String invoiceNo) {
		final MatchOperation match = Aggregation.match(Criteria.where("invoiceNo").in(invoiceNo));
		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");
		final LookupOperation lookupForwarder = LookupOperation.newLookup().from("m_frwrdr").localField("forwarder")
				.foreignField("code").as("forwarder");
		final AggregationOperation groupOperation = context -> new Document("$group", new Document("_id", "$invoiceNo")
				.append("items", new Document("$push",
						new BasicDBObject("stockNo", "$stockNo").append("chassisNo", "$stock.chassisNo")
								.append("maker", "$stock.maker").append("model", "$stock.model")
								.append("freightCharge", "$freightCharge").append("shippingCharge", "$shippingCharge")
								.append("inspectionCharge", "$inspectionCharge")
								.append("radiationCharge", "$radiationCharge").append("otherCharges", "$otherCharges")
								.append("amount",
										new Document("$sum",
												Arrays.asList("$freightCharge", "$shippingCharge", "$inspectionCharge",
														"$radiationCharge", "$otherCharges")))))
				.append("invoiceNo", new Document("$first", "$invoiceNo"))
				.append("createdDate", new Document("$first", "$createdDate"))
				.append("dueDate", new Document("$first", "$dueDate"))
				.append("invoiceName", new Document("$first", "$forwarder.name"))
				.append("supplierId", new Document("$first", "$forwarder.code"))
				.append("paymentApprove", new Document("$first", "$paymentApprove")));
		final ProjectionOperation project = Aggregation.project()
				.andInclude("invoiceNo", "createdDate", "invoiceName", "dueDate", "supplierId", "paymentApprove")
				.and("items").as("items").and(AccumulatorOperators.Sum.sumOf("items.amount")).as("totalAmount");

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupStock,
				Aggregation.unwind("$stock", true), lookupForwarder, Aggregation.unwind("$forwarder", true),
				groupOperation, project);
		final AggregationResults<TFreightShippingInvoiceDto> result = mongoTemplate.aggregate(aggregation,
				"t_frght_shpng_invc", TFreightShippingInvoiceDto.class);
		return result.getUniqueMappedResult();
	}

	@Override
	public List<ApprovePaymentsDto> findAllByPaymentApproveFreezed(Integer paymentApprove) {
		final MatchOperation match = Aggregation.match(Criteria.where("paymentApprove").is(paymentApprove));
		final LookupOperation lookupForwarder = LookupOperation.newLookup().from("m_frwrdr").localField("forwarder")
				.foreignField("code").as("forwarder");
		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");
		final AggregationOperation groupOperation = context -> new Document("$group", new Document("_id", "$invoiceNo")
				.append("approvePaymentItems", new Document("$push",
						new BasicDBObject("invoiceNo", "$invoiceNo").append("chassisNo", "$stock.chassisNo")
								.append("freightCharge", "$freightCharge").append("shippingCharge", "$shippingCharge")
								.append("inspectionCharge", "$inspectionCharge")
								.append("radiationCharge", "$radiationCharge").append("otherCharges", "$otherCharges")
								.append("freightChargeUsd", "$freightChargeUsd").append("amount", "$totalAmount")))
				.append("invoiceNo", new Document("$first", "$invoiceNo"))
				.append("createdDate", new Document("$first", "$createdDate"))
				.append("dueDate", new Document("$first", "$dueDate"))
				.append("invoiceName", new Document("$first", "$forwarder.name"))
				.append("supplierId", new Document("$first", "$forwarder.code")));
		final ProjectionOperation project = Aggregation.project()
				.andInclude("approvePaymentItems", "invoiceNo", "createdDate", "invoiceName", "dueDate", "supplierId")
				.and(AccumulatorOperators.Sum.sumOf("approvePaymentItems.amount")).as("totalAmount")
				.and(AccumulatorOperators.Sum.sumOf("approvePaymentItems.freightChargeUsd")).as("totalAmountUsd");

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupForwarder,
				Aggregation.unwind("$forwarder", true), lookupStock, Aggregation.unwind("$stock", true), groupOperation,
				project);
		final AggregationResults<ApprovePaymentsDto> result = mongoTemplate.aggregate(aggregation, "t_frght_shpng_invc",
				ApprovePaymentsDto.class);
		return result.getMappedResults();
	}

	@Override
	public Long getCountStorageData(List<Integer> paymentApprove) {
		final MatchOperation match = Aggregation.match(Criteria.where("paymentApprove").in(paymentApprove));
		final GroupOperation groupOperation = Aggregation.group("$invoiceNo");
		final CountOperation count = Aggregation.count().as("count");
		final Aggregation aggregation = Aggregation.newAggregation(match, groupOperation, count);
		final AggregationResults<Document> result = mongoTemplate.aggregate(aggregation, "t_frght_shpng_invc",
				Document.class);
		return Long.valueOf(!AppUtil.isObjectEmpty(result.getUniqueMappedResult())
				? result.getUniqueMappedResult().get("count").toString()
				: "0");
	}

	@Override
	public List<TFreightShippingRadiationDto> getAllNotClaimedRadiationStatus() {
		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");
		final LookupOperation lookupForwarder = LookupOperation.newLookup().from("m_frwrdr").localField("forwarder")
				.foreignField("code").as("forwarderDetails");
		final LookupOperation lookupShippingReq = LookupOperation.newLookup().from("t_shppng_rqust")
				.localField("shipmentRequestId").foreignField("shipmentRequestId").as("shippingRequest");
		final LookupOperation lookupShippingSchedule = LookupOperation.newLookup().from("t_shpmnt_schdl")
				.localField("shippingRequest.scheduleId").foreignField("scheduleId").as("shippingSchedule");
		final LookupOperation lookupShippingCompany = LookupOperation.newLookup().from("m_shppng_cmpny")
				.localField("shippingSchedule.shippingCompanyNo").foreignField("shippingCompanyNo")
				.as("shippingCompany");
		final ProjectionOperation project = Aggregation.project()
				.andInclude("id", "stockNo", "createdDate", "radiationCharge", "radiationClaimReceivedAmount")
				.and("forwarderDetails.name").as("forwarder").and("stock.chassisNo").as("chassisNo")
				.and("shippingCompany.name").as("shippingCompany");
		final Aggregation aggregation = Aggregation.newAggregation(lookupStock, Aggregation.unwind("$stock", true),
				lookupForwarder, Aggregation.unwind("$forwarderDetails", true), lookupShippingReq,
				Aggregation.unwind("$shippingRequest", true), lookupShippingSchedule,
				Aggregation.unwind("$shippingSchedule", true), lookupShippingCompany,
				Aggregation.unwind("$shippingCompany", true), project);
		final AggregationResults<TFreightShippingRadiationDto> result = mongoTemplate.aggregate(aggregation,
				"t_frght_shpng_invc", TFreightShippingRadiationDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<PaymentTrackingDto> purchasepaymentTracking(String remitter, Date fromDate, Date toDate) {
		final List<Criteria> andCriterias = new ArrayList<>();
		boolean isValid = false;
		if (!AppUtil.isObjectEmpty(remitter)) {
			isValid = true;
			andCriterias.add(Criteria.where("forwarder").is(remitter));
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
		final LookupOperation lookupForwarder = LookupOperation.newLookup().from("m_frwrdr").localField("forwarder")
				.foreignField("code").as("forwarder");
		final AggregationOperation groupOperation = context -> new Document("$group", new Document("_id", "$invoiceNo")
				.append("approvePaymentItems",
						new Document("$push", new BasicDBObject("invoiceNo", "$invoiceNo").append("stockNo", "$stockNo")
								.append("freightCharge", "$freightCharge").append("shippingCharge", "$shippingCharge")
								.append("inspectionCharge", "$inspectionCharge")
								.append("radiationCharge", "$radiationCharge").append("otherCharges", "$otherCharges")
								.append("freightChargeUsd", "$freightChargeUsd").append("amount",
										new Document("$sum",
												Arrays.asList("$freightCharge", "$shippingCharge", "$inspectionCharge",
														"$radiationCharge", "$otherCharges")))))
				.append("invoiceNo", new Document("$first", "$invoiceNo"))
				.append("invoiceUpload", new Document("$first", "$invoiceUpload"))
				.append("invoiceName", new Document("$first", "$forwarder.name"))
				.append("paymentApproveStatus", new Document("$first", "$paymentApprove"))
				.append("approvedDate", new Document("$first", "$approvedDate"))
				.append("invoiceDate", new Document("$first", "$createdDate"))
				.append("approvedBy", new Document("$first", "$approvedBy"))
				.append("paidAmount", new Document("$first", "$invoiceAmountReceived"))
				.append("paidAmountUsd", new Document("$first", "$invoiceAmountReceivedUsd"))
				.append("invoiceAttachmentFilename", new Document("$first", "$invoiceAttachmentFilename"))
				.append("invoiceAttachmentDiskFilename", new Document("$first", "$invoiceAttachmentDiskFilename"))
				.append("type", new Document("$first", Constants.INVOICE_TYPE_FREIGHT)));
		LookupOperation lookupUser = LookupOperation.newLookup().from("m_usr").localField("approvedBy")
				.foreignField("code").as("userDetails");
		final ProjectionOperation project = Aggregation.project()
				.andInclude("approvePaymentItems", "invoiceNo", "invoiceUpload", "invoiceName", "type",
						"paymentApproveStatus", "approvedDate", "invoiceDate", "paidAmount", "paidAmountUsd",
						"invoiceAttachmentFilename", "invoiceAttachmentDiskFilename")
				.and(AccumulatorOperators.Sum.sumOf("approvePaymentItems.amount")).as("totalAmount")
				.and(AccumulatorOperators.Sum.sumOf("approvePaymentItems.freightChargeUsd")).as("totalAmountUsd")
				.and("$userDetails.fullname").as("approvedBy");

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupForwarder,
				Aggregation.unwind("$forwarder", true), groupOperation, lookupUser,
				Aggregation.unwind("$userDetails", true), project);
		final AggregationResults<PaymentTrackingDto> result = mongoTemplate.aggregate(aggregation, "t_frght_shpng_invc",
				PaymentTrackingDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<PaymentTrackingReportDto> purchasepaymentTrackingReport(String remitter, Date fromDate, Date toDate) {
		final List<Criteria> andCriterias = new ArrayList<>();
		boolean isValid = false;
		if (!AppUtil.isObjectEmpty(remitter)) {
			isValid = true;
			andCriterias.add(Criteria.where("forwarder").is(remitter));
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
		final LookupOperation lookupForwarder = LookupOperation.newLookup().from("m_frwrdr").localField("forwarder")
				.foreignField("code").as("forwarder");
		final AggregationOperation groupOperation = context -> new Document("$group", new Document("_id", "$invoiceNo")
				.append("approvePaymentItems",
						new Document("$push", new BasicDBObject("invoiceNo", "$invoiceNo").append("stockNo", "$stockNo")
								.append("freightCharge", "$freightCharge").append("shippingCharge", "$shippingCharge")
								.append("inspectionCharge", "$inspectionCharge")
								.append("radiationCharge", "$radiationCharge").append("otherCharges", "$otherCharges")
								.append("freightChargeUsd", "$freightChargeUsd").append("amount",
										new Document("$sum",
												Arrays.asList("$freightCharge", "$shippingCharge", "$inspectionCharge",
														"$radiationCharge", "$otherCharges")))))
				.append("invoiceNo", new Document("$first", "$invoiceNo"))
				.append("invoiceName", new Document("$first", "$forwarder.name"))
				.append("paymentApproveStatus", new Document("$first", "$paymentApprove"))
				.append("approvedDate", new Document("$first", "$approvedDate"))
				.append("invoiceDate", new Document("$first", "$createdDate"))
				.append("approvedBy", new Document("$first", "$approvedBy"))
				.append("paidAmount", new Document("$first", "$invoiceAmountReceived"))
				.append("paidAmountUsd", new Document("$first", "$invoiceAmountReceivedUsd")));

		final AggregationExpression totalAmount = contexts -> new Document("$sum", "$approvePaymentItems.amount");
		final AggregationExpression totalAmountUsd = contexts -> new Document("$sum",
				"$approvePaymentItems.freightChargeUsd");
		final AggregationExpression balanceAmount = contexts -> new Document("$subtract",
				Arrays.asList(new Document("$sum", "$approvePaymentItems.amount"), "$paidAmountUsd"));
		final AggregationExpression balanceAmountUsd = contexts -> new Document("$subtract",
				Arrays.asList(new Document("$sum", "$approvePaymentItems.freightChargeUsd"), "$paidAmount"));
		
		final ProjectionOperation project = Aggregation.project()
				.andInclude("invoiceNo", "invoiceName", "paymentApproveStatus", "invoiceDate", "paidAmount",
						"paidAmountUsd")
				.and(totalAmount).as("totalAmount").and(balanceAmount).as("balance").and(totalAmountUsd)
				.as("totalAmountUsd").and(balanceAmountUsd).as("balanceAmountUsd");

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
						"totalAmount", "balance", "paidAmountUsd", "totalAmountUsd", "balanceAmountUsd")
				.and("$transaction.code").as("paymentVoucherNo").and("$transaction.approvedDate").as("approvedDate")
				.and("$transaction.amount").as("paymentAmount");

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupForwarder,
				Aggregation.unwind("$forwarder", true), groupOperation, project, lookupTransaction, filterTransactions,
				Aggregation.unwind("$transaction", true), project2);
		final AggregationResults<PaymentTrackingReportDto> result = mongoTemplate.aggregate(aggregation,
				"t_frght_shpng_invc", PaymentTrackingReportDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<TFreightShippingContainerInvoiceDto> findAllShippingContainerInvoice() {

		final MatchOperation match = Aggregation
				.match(Criteria.where("paymentApprove").is(Constants.PAYMENT_NOT_APPROVED)
						.andOperator(Criteria.where("invoiceType").is(Constants.SHIPPING_INVOICE_TYPE_CONTAINER)));

		final AggregationOperation groupOperation = context -> new Document("$group",
				new Document("_id", "$invoiceNo").append("invoiceNo", new Document("$first", "$invoiceNo"))
						.append("createdDate", new Document("$first", "$createdDate"))
						.append("forwarderId", new Document("$first", "$forwarder"))
						.append("dueDate", new Document("$first", "$dueDate"))
						.append("total", new Document("$sum", "$totalAmount"))
						.append("invoiceUpload", new Document("$first", "$invoiceUpload"))
						.append("attachementViewed", new Document("$first", "$attachementViewed"))
						.append("invoiceAttachmentDiskFilename",
								new Document("$first", "$invoiceAttachmentDiskFilename"))
						.append("type", new Document("$first", Constants.INVOICE_TYPE_FREIGHT)));

		final LookupOperation lookupForwarder = LookupOperation.newLookup().from("m_frwrdr").localField("forwarderId")
				.foreignField("code").as("forwarder");
		final LookupOperation lookupInvoiceItems = LookupOperation.newLookup().from("t_shipping_invoice_items")
				.localField("invoiceNo").foreignField("invoiceNo").as("items");
		final ProjectionOperation project = Aggregation.project("invoiceNo", "createdDate", "dueDate", "items", "total",
				"invoiceUpload", "attachementViewed", "invoiceAttachmentDiskFilename", "type", "forwarderId")
				.and("forwarder.name").as("remitTo");

		final Aggregation aggregation = Aggregation.newAggregation(match, groupOperation, lookupForwarder,
				lookupInvoiceItems, Aggregation.unwind("$forwarder", true), project);
		final AggregationResults<TFreightShippingContainerInvoiceDto> result = mongoTemplate.aggregate(aggregation,
				"t_frght_shpng_invc", TFreightShippingContainerInvoiceDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<Document> findAllStockByinvoiceNo(String invoiceNo) {
		final MatchOperation match = Aggregation.match(Criteria.where("invoiceNo").is(invoiceNo));
		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");

		final ProjectionOperation project = Aggregation.project().and("stock.chassisNo").as("chassisNo")
				.and("stock.maker").as("maker").and("stock.model").as("model").and("stock.m3").as("m3")
				.and("totalAmount").as("amount");

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupStock,
				Aggregation.unwind("$stock", true), project);
		final AggregationResults<Document> result = mongoTemplate.aggregate(aggregation, "t_frght_shpng_invc",
				Document.class);
		return result.getMappedResults();
	}
}
