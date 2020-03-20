package com.nexware.aajapan.repositories.custom.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
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
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.BasicDBObject;
import com.mongodb.client.result.UpdateResult;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.ApprovePaymentsDto;
import com.nexware.aajapan.dto.CancelledStockDto;
import com.nexware.aajapan.dto.PayableAmountDto;
import com.nexware.aajapan.dto.PaymentTrackingDto;
import com.nexware.aajapan.dto.PaymentTrackingReportDto;
import com.nexware.aajapan.dto.TInvoiceDto;
import com.nexware.aajapan.dto.TPurchaseInvoiceCarTaxDto;
import com.nexware.aajapan.dto.TPurchaseInvoiceDocumentationRecylceDto;
import com.nexware.aajapan.dto.TPurchaseInvoiceRecylceDto;
import com.nexware.aajapan.dto.TPurchaseInvoiceTaxDto;
import com.nexware.aajapan.models.TPurchaseInvoice;
import com.nexware.aajapan.models.TStock;
import com.nexware.aajapan.repositories.custom.TPurchaseInvoiceRepositoryCustom;
import com.nexware.aajapan.utils.AppUtil;

public class TPurchaseInvoiceRepositoryCustomImpl implements TPurchaseInvoiceRepositoryCustom {
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private MongoOperations mongoOperations;

	@Override
	public List<ApprovePaymentsDto> findAllByPaymentApprove(List<Integer> invoiceStatus, List<Integer> paymentStatus) {
		final MatchOperation match = Aggregation.match(new Criteria().andOperator(
				Criteria.where("status").in(invoiceStatus), Criteria.where("paymentApprove").in(paymentStatus)));
		final LookupOperation lookupSupplier = LookupOperation.newLookup().from("m_spplr").localField("supplierId")
				.foreignField("supplierCode").as("supplier_details");
		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");

		final Document purchaseCostTax = new Document("$multiply",
				Arrays.asList("$purchaseCost", new Document("$divide", Arrays.asList("$purchaseCostTax", 100))));
		final Document commisionTax = new Document("$multiply",
				Arrays.asList("$commision", new Document("$divide", Arrays.asList("$commisionTax", 100))));

		final AggregationOperation groupOperation = context -> new Document("$group", new Document("_id", "$invoiceNo")
				.append("approvePaymentItems", new Document("$push", new BasicDBObject("invoiceNo", "$invoiceNo")
						.append("stockNo", "$stockNo").append("chassisNo", "$chassisNo")
						.append("lotNo", "$stock.purchaseInfo.auctionInfo.lotNo")
						.append("purchaseCost", "$purchaseCost").append("paymentType", "$paymentType")
						.append("roadTax", "$roadTax").append("commision", "$commision")
						.append("otherCharges", "$otherCharges").append("recycle", "$recycle")
						.append("status", "$status").append("type", "$type")
						.append("purchaseCostTaxAmount",
								new Document("$ifNull", Arrays.asList("$purchaseCostTaxAmount", 0.0)))
						.append("commisionTaxAmount",
								new Document("$ifNull", Arrays.asList("$commisionTaxAmount", 0.0)))
						.append("othersCostTaxAmount",
								new Document("$ifNull", Arrays.asList("$othersCostTaxAmount", 0.0)))
						.append("totalAmount", new Document("$add",
								Arrays.asList("$recycle", "$purchaseCost", "$otherCharges", "$roadTax", "$commision",
										new Document("$ifNull", Arrays.asList("$purchaseCostTaxAmount", 0.0)),
										new Document("$ifNull", Arrays.asList("$commisionTaxAmount", 0.0)),
										new Document("$ifNull", Arrays.asList("$othersCostTaxAmount", 0.0)))))))
				.append("createdDate", new Document("$first", "$createdDate"))
				.append("dueDate", new Document("$first", "$dueDate"))
				.append("invoiceNo", new Document("$first", "$invoiceNo"))
				.append("supplierId", new Document("$first", "$supplierId"))
				.append("auctionHouseId", new Document("$first", "$auctionHouseId"))
				.append("remitter", new Document("$first", "$supplierId"))
				.append("invoiceDate", new Document("$first", "$invoiceDate"))
				.append("invoiceUpload", new Document("$first", "$invoiceUpload"))
				.append("attachementViewed", new Document("$first", "$attachementViewed"))
				.append("auctionRefNo", new Document("$first", "$auctionRefNo"))
				.append("remarks", new Document("$first", "$remarks"))
				.append("invoiceAttachmentFilename", new Document("$first", "$invoiceAttachmentFilename"))
				.append("invoiceAttachmentDiskFilename", new Document("$first", "$invoiceAttachmentDiskFilename"))
				.append("invoiceAmountReceived", new Document("$first", "$invoiceAmountReceived"))

		);
		final AggregationOperation addAuctionHouse = contexts -> new Document("$addFields",
				new Document("auctionHouseDetails",
						new Document("$filter", new Document("input", "$supplier_details.supplierLocations")
								.append("as", "result").append("cond",
										new Document("$eq", Arrays.asList("$$result._id", "$auctionHouseId"))))));
		final ProjectionOperation project = Aggregation.project()
				.andInclude("approvePaymentItems", "createdDate", "dueDate", "invoiceNo", "invoiceName", "supplierId",
						"auctionHouseId", "invoiceDate", "invoiceAttachmentFilename", "invoiceAttachmentDiskFilename",
						"invoiceAmountReceived", "invoiceUpload", "attachementViewed", "auctionRefNo", "remarks")
				.and("$supplier_details.company").as("supplierName").and("$auctionHouseDetails.auctionHouse")
				.as("auctionHouseName").and(AccumulatorOperators.Sum.sumOf("approvePaymentItems.totalAmount"))
				.as("totalAmount").and("supplier_details.company").as("invoiceName")
				.and(LiteralOperators.Literal.asLiteral(Constants.PURCHASE_INVOICE_ITEM_TYPE_PURCHASE)).as("type");
		// sort
		final SortOperation sort = Aggregation.sort(Direction.DESC, "invoiceDate");

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupStock,
				Aggregation.unwind("$stock", true), groupOperation, lookupSupplier,
				Aggregation.unwind("$supplier_details", true), addAuctionHouse,
				Aggregation.unwind("$auctionHouseDetails", true), project, sort);
		final AggregationResults<ApprovePaymentsDto> result = mongoTemplate.aggregate(aggregation, "t_prchs_invc",
				ApprovePaymentsDto.class);
		return result.getMappedResults();
	}

	@Override
	public Long getApprovalCountAuctionData(List<Integer> invoiceStatus, List<Integer> paymentStatus) {
		final MatchOperation match = Aggregation.match(new Criteria().andOperator(
				Criteria.where("status").in(invoiceStatus), Criteria.where("paymentApprove").in(paymentStatus)));
		final GroupOperation groupOperation = Aggregation.group("$invoiceNo");
		final CountOperation count = Aggregation.count().as("count");
		final Aggregation aggregation = Aggregation.newAggregation(match, groupOperation, count);
		final AggregationResults<Map> result = mongoTemplate.aggregate(aggregation, "t_prchs_invc", Map.class);
		return Long.valueOf(!AppUtil.isObjectEmpty(result.getUniqueMappedResult())
				? result.getUniqueMappedResult().get("count").toString()
				: "0");
	}

	@Override
	public void updatePurchaseStatusByStockNo(Integer statusList, String stockNo) {
		final Update update = new Update().set("status", statusList);
		mongoTemplate.updateMulti(Query.query(Criteria.where("stockNo").in(stockNo)), update, TPurchaseInvoice.class);
	}

	@Override
	public List<CancelledStockDto> findAllCancelledInvoice() {
		final MatchOperation match = Aggregation.match(Criteria.where("status").is(Constants.INV_STATUS_CANCEL));
		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");
		final LookupOperation lookupSupplier = LookupOperation.newLookup().from("m_spplr")
				.localField("stock.purchaseInfo.supplier").foreignField("supplierCode").as("supplier");
		final AggregationOperation addAuctionHouse = context -> new Document("$addFields", new Document("auctionHouse",
				new Document("$filter", new Document("input", "$supplier.supplierLocations").append("as", "result")
						.append("cond", new Document("$eq",
								Arrays.asList("$$result._id", "$stock.purchaseInfo.auctionInfo.auctionHouse"))))));
		final ProjectionOperation project = Aggregation.project().andInclude("id", "stockNo", "cancellationCharge")
				.and("stock.chassisNo").as("chassisNo").and("stock.purchaseInfo.date").as("purchaseDate")
				.and("supplier.supplierCode").as("supplierId").and("supplier.company").as("supplierName").and("status")
				.as("status").and("$auctionHouse._id").as("auctionHouseId").and("$auctionHouse.auctionHouse")
				.as("auctionHouseName");
		SortOperation sort = Aggregation.sort(Direction.DESC, "purchaseDate");
		final Aggregation aggregation = Aggregation.newAggregation(match, lookupStock,
				Aggregation.unwind("$stock", true), lookupSupplier, Aggregation.unwind("$supplier", true),
				addAuctionHouse, Aggregation.unwind("$auctionHouse", true), project, sort);
		final AggregationResults<CancelledStockDto> result = mongoTemplate.aggregate(aggregation, "t_prchs_invc",
				CancelledStockDto.class);
		return result.getMappedResults();
	}

	@Override
	public UpdateResult updateById(String id, Update update) {
		return mongoTemplate.updateMulti(Query.query(Criteria.where("id").is(id)), update, TPurchaseInvoice.class);
	}

	@Override
	public CancelledStockDto findCancelledInvoiceById(String id) {
		final MatchOperation match = Aggregation.match(Criteria.where("_id").is(id));
		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");
		final LookupOperation lookupSupplier = LookupOperation.newLookup().from("m_spplr")
				.localField("stock.purchaseInfo.supplier").foreignField("supplierCode").as("supplier");
		final AggregationOperation addAuctionHouse = context -> new Document("$addFields", new Document("auctionHouse",
				new Document("$filter", new Document("input", "$supplier.supplierLocations").append("as", "result")
						.append("cond", new Document("$eq",
								Arrays.asList("$$result.id", "$stock.purchaseInfo.auctionInfo.auctionHouse"))))));
		final ProjectionOperation project = Aggregation.project().andInclude("id", "stockNo", "cancellationCharge")
				.and("stock.chassisNo").as("chassisNo").and("supplier.supplierCode").as("supplierId")
				.and("supplier.company").as("supplierName").and("status").as("status").and("$auctionHouse._id")
				.as("auctionHouseId").and("$auctionHouse.auctionHouse").as("auctionHouseName");
		final Aggregation aggregation = Aggregation.newAggregation(match, lookupStock,
				Aggregation.unwind("$stock", true), lookupSupplier, Aggregation.unwind("$supplier", true),
				addAuctionHouse, Aggregation.unwind("$auctionHouse", true), project);
		final AggregationResults<CancelledStockDto> result = mongoTemplate.aggregate(aggregation, "t_prchs_invc",
				CancelledStockDto.class);
		return result.getUniqueMappedResult();
	}

	@Override
	public List<ApprovePaymentsDto> getAllPaymentStatusDayBook() {
		final LookupOperation lookupSupplier = LookupOperation.newLookup().from("m_spplr").localField("invoiceName")
				.foreignField("supplierCode").as("supplierDetails");
		final LookupOperation lookupBank = LookupOperation.newLookup().from("m_bank")
				.localField("approvePaymentDetails.bank").foreignField("bankSeq").as("bank");
		final AggregationOperation groupOperation = context -> new Document("$group",
				new Document("_id", "$invoiceNo")
						.append("approvePaymentItems",
								new Document("$push", new BasicDBObject("invoiceNo", "$invoiceNo")
										.append("stockNo", "$stockNo").append("purchaseCost", "$purchaseCost")
										.append("paymentType", "$paymentType").append("roadTax", "$roadTax")
										.append("commision", "$commision").append("otherCharges", "$otherCharges")
										.append("recycle", "$recycle").append("totalAmount",
												new Document("$sum",
														Arrays.asList("$recycle", "$purchaseCost", "$otherCharges",
																"roadTax", "commision")))))
						.append("createdDate", new Document("$first", "$createdDate"))
						.append("dueDate", new Document("$first", "$dueDate"))
						.append("invoiceNo", new Document("$first", "$invoiceNo"))
						.append("invoiceName", new Document("$first", "$supplierDetails.company"))
						.append("type", new Document("$first", "$type"))
						.append("bank", new Document("$first", "$bank.bankName")));
		final ProjectionOperation project = Aggregation.project()
				.andInclude("approvePaymentItems", "createdDate", "dueDate", "invoiceNo", "invoiceName", "type", "bank")
				.and(AccumulatorOperators.Sum.sumOf("approvePaymentItems.totalAmount")).as("totalAmount");

		final Aggregation aggregation = Aggregation.newAggregation(lookupSupplier,
				Aggregation.unwind("$supplierDetails", true), lookupBank, Aggregation.unwind("$bank", true),
				groupOperation, project);
		final AggregationResults<ApprovePaymentsDto> result = mongoTemplate.aggregate(aggregation, "t_prchs_invc",
				ApprovePaymentsDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<ApprovePaymentsDto> getAllDayBookTransportStatusAccountData() {

		final LookupOperation lookupBank = LookupOperation.newLookup().from("m_bank")
				.localField("approvePaymentDetails.bank").foreignField("bankSeq").as("bank");
		final LookupOperation lookupTransporter = LookupOperation.newLookup().from("m_trnsprtr")
				.localField("transporterId").foreignField("code").as("transporterDetails");
		final AggregationOperation groupOperation = context -> new Document("$group",
				new Document("_id", "$invoiceNo")
						.append("approvePaymentItems",
								new Document("$push",
										new BasicDBObject("invoiceNo", "$invoiceNo").append("stockNo", "$stockNo")
												.append("amount", "$amount")
												.append("amount", new Document("$sum", Arrays.asList("$amount")))))
						.append("createdDate", new Document("$first", "$createdDate"))
						.append("invoiceNo", new Document("$first", "$invoiceNo"))
						.append("invoiceName", new Document("$first", "$transporterDetails.name"))
						.append("type", new Document("$first", Constants.INVOICE_TYPE_TRANSPORT))
						.append("bank", new Document("$first", "$bank.bankName")));
		final ProjectionOperation project = Aggregation.project()
				.andInclude("approvePaymentItems", "createdDate", "invoiceNo", "invoiceName", "type", "bank")
				.and(AccumulatorOperators.Sum.sumOf("approvePaymentItems.amount")).as("totalAmount");

		final Aggregation aggregation = Aggregation.newAggregation(lookupTransporter,
				Aggregation.unwind("$transporterDetails", true), lookupBank, Aggregation.unwind("$bank", true),
				groupOperation, project);
		final AggregationResults<ApprovePaymentsDto> result = mongoTemplate.aggregate(aggregation, "trnsprt_invc",
				ApprovePaymentsDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<ApprovePaymentsDto> getAllDayBookTinvStatusAccountData() {

		final LookupOperation lookupBank = LookupOperation.newLookup().from("m_bank")
				.localField("approvePaymentDetails.bank").foreignField("bankSeq").as("bank");
		final LookupOperation lookupSupplier = LookupOperation.newLookup().from("m_spplr").localField("remitter")
				.foreignField("supplierCode").as("supplierDetails");
		final AggregationOperation groupOperation = context -> new Document("$group",
				new Document("_id", "$invoiceNo")
						.append("approvePaymentItems",
								new Document("$push",
										new BasicDBObject("invoiceNo", "$invoiceNo").append("stockNo", "$stockNo")
												.append("amount", "$amount")
												.append("amount", new Document("$sum", Arrays.asList("$amount")))))
						.append("createdDate", new Document("$first", "$createdDate"))
						.append("dueDate", new Document("$first", "$dueDate"))
						.append("invoiceNo", new Document("$first", "$invoiceNo"))
						.append("invoiceName", new Document("$first", "$supplierDetails.company"))
						.append("type", new Document("$first", Constants.INVOICE_TYPE_PAYMENT))
						.append("bank", new Document("$first", "$bank.bankName")));
		final ProjectionOperation project = Aggregation.project()
				.andInclude("approvePaymentItems", "createdDate", "dueDate", "invoiceNo", "invoiceName", "type", "bank")
				.and(AccumulatorOperators.Sum.sumOf("approvePaymentItems.amount")).as("totalAmount");

		final Aggregation aggregation = Aggregation.newAggregation(lookupSupplier,
				Aggregation.unwind("$transporterDetails", true), lookupBank, Aggregation.unwind("$bank", true),
				groupOperation, project);
		final AggregationResults<ApprovePaymentsDto> result = mongoTemplate.aggregate(aggregation, "t_invc",
				ApprovePaymentsDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<ApprovePaymentsDto> getAllDayBookForwarderStatusAccountData() {

		final LookupOperation lookupBank = LookupOperation.newLookup().from("m_bank")
				.localField("approvePaymentDetails.bank").foreignField("bankSeq").as("bank");
		final LookupOperation lookupForwarder = LookupOperation.newLookup().from("m_frwrdr").localField("remitter")
				.foreignField("code").as("forwarderDetails");
		final AggregationOperation groupOperation = context -> new Document("$group",
				new Document("_id", "$invoiceNo")
						.append("approvePaymentItems",
								new Document("$push",
										new BasicDBObject("invoiceNo", "$invoiceNo").append("amount", "$amount")
												.append("stockNo", "$stockNo")
												.append("amount", new Document("$sum", Arrays.asList("$amount")))))
						.append("createdDate", new Document("$first", "$createdDate"))
						.append("dueDate", new Document("$first", "$dueDate"))
						.append("invoiceNo", new Document("$first", "$invoiceNo"))
						.append("invoiceName", new Document("$first", "$forwarderDetails.name"))
						.append("type", new Document("$first", Constants.INVOICE_TYPE_FORWARDER))
						.append("bank", new Document("$first", "$bank.bankName")));
		final ProjectionOperation project = Aggregation.project()
				.andInclude("approvePaymentItems", "createdDate", "dueDate", "invoiceNo", "invoiceName", "type", "bank")
				.and(AccumulatorOperators.Sum.sumOf("approvePaymentItems.amount")).as("totalAmount");

		final Aggregation aggregation = Aggregation.newAggregation(lookupForwarder,
				Aggregation.unwind("$forwarderDetails", true), lookupBank, Aggregation.unwind("$bank", true),
				groupOperation, project);
		final AggregationResults<ApprovePaymentsDto> result = mongoTemplate.aggregate(aggregation, "t_frwrdr_invc",
				ApprovePaymentsDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<ApprovePaymentsDto> getAllDayBookFreightShippingStatusAccountData() {

		final LookupOperation lookupBank = LookupOperation.newLookup().from("m_bank")
				.localField("approvePaymentDetails.bank").foreignField("bankSeq").as("bank");
		final AggregationOperation groupOperation = context -> new Document("$group", new Document("_id", "$invoiceNo")
				.append("approvePaymentItems", new Document("$push",
						new BasicDBObject("invoiceNo", "$invoiceNo").append("stockNo", "$stockNo")
								.append("freightCharge", "$freightCharge").append("shippingCharge", "$shippingCharge")
								.append("inspectionCharge", "$inspectionCharge")
								.append("radiationCharge", "$radiationCharge").append("otherCharges", "$otherCharges")
								.append("amount",
										new Document("$sum",
												Arrays.asList("$freightCharge", "$shippingCharge", "$inspectionCharge",
														"radiationCharge", "otherCharges")))))
				.append("invoiceNo", new Document("$first", "$invoiceNo"))
				.append("createdDate", new Document("$first", "$createdDate"))
				.append("type", new Document("$first", Constants.INVOICE_TYPE_FREIGHT))
				.append("bank", new Document("$first", "$bank.bankName")));
		final ProjectionOperation project = Aggregation.project()
				.andInclude("approvePaymentItems", "invoiceNo", "createdDate", "type", "bank")
				.and(AccumulatorOperators.Sum.sumOf("approvePaymentItems.amount")).as("totalAmount");

		final Aggregation aggregation = Aggregation.newAggregation(lookupBank, Aggregation.unwind("$bank", true),
				groupOperation, project);
		final AggregationResults<ApprovePaymentsDto> result = mongoTemplate.aggregate(aggregation, "t_frght_shpng_invc",
				ApprovePaymentsDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<TPurchaseInvoiceRecylceDto> getAllNotClaimedRecycleClaimStatus() {

		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");
		final ProjectionOperation project = Aggregation.project()
				.andInclude("id", "stockNo", "recycleClaimStatus", "recycle", "recycleClaimReceivedAmount")
				.and("stock.purchaseInfo.date").as("date").and("stock.chassisNo").as("chassisNo");
		final Aggregation aggregation = Aggregation.newAggregation(lookupStock, Aggregation.unwind("$stock", true),
				project);
		final AggregationResults<TPurchaseInvoiceRecylceDto> result = mongoTemplate.aggregate(aggregation,
				"t_prchs_invc", TPurchaseInvoiceRecylceDto.class);
		return result.getMappedResults();
	}

	@Override
	public TPurchaseInvoiceRecylceDto getNotClaimedRecycleClaimById(String id) {
		final MatchOperation match = Aggregation.match(Criteria.where("_id").is(id));
		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");
		final ProjectionOperation project = Aggregation.project()
				.andInclude("id", "stockNo", "recycleClaimStatus", "recycle").and("stock.purchaseInfo.date").as("date")
				.and("stock.chassisNo").as("chassisNo");
		final Aggregation aggregation = Aggregation.newAggregation(match, lookupStock,
				Aggregation.unwind("$stock", true), project);
		final AggregationResults<TPurchaseInvoiceRecylceDto> result = mongoTemplate.aggregate(aggregation,
				"t_prchs_invc", TPurchaseInvoiceRecylceDto.class);
		return result.getUniqueMappedResult();
	}

	@Override
	public List<TPurchaseInvoiceCarTaxDto> getAllNotClaimedCarTaxClaimStatus() {
		final MatchOperation matchPurchase = Aggregation.match(Criteria.where("type")
				.in(Constants.PURCHASE_INVOICE_ITEM_TYPE_PURCHASE, Constants.PURCHASE_INVOICE_ITEM_TYPE_REAUCTION)
				.and("roadTax").gt(0));
		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");
		final MatchOperation matchStock = Aggregation.match(Criteria.where("stock.numberPlate").is("yes"));
		final ProjectionOperation project = Aggregation.project()
				.andInclude("id", "stockNo", "carTaxClaimStatus", "roadTax", "carTaxClaimReceivedAmount")
				.and("stock.purchaseInfo.date").as("purchaseDate").and("stock.chassisNo").as("chassisNo")
				.and("stock.purchaseInfo.type").as("purchaseInfoType").and("supplierId").as("supplierCode")
				.and("auctionHouseId").as("auctionHouseId").and("stock.purchaseInfo.auctionInfo.posNo")
				.as("auctionInfoPosNo").and("stock.purchaseInfo.auctionInfo.lotNo").as("auctionInfoLotNo")
				.and("carTaxReceivedDate").as("receivedDate");
		final Aggregation aggregation = Aggregation.newAggregation(matchPurchase, lookupStock,
				Aggregation.unwind("$stock", true), matchStock, project);
		final AggregationResults<TPurchaseInvoiceCarTaxDto> result = mongoTemplate.aggregate(aggregation,
				"t_prchs_invc", TPurchaseInvoiceCarTaxDto.class);
		return result.getMappedResults();
	}

	@Override
	public TPurchaseInvoiceCarTaxDto getNotClaimedCarTaxClaimById(String id) {
		final MatchOperation match = Aggregation.match(Criteria.where("_id").is(id));
		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");
		final ProjectionOperation project = Aggregation.project()
				.andInclude("id", "stockNo", "carTaxClaimStatus", "roadTax", "carTaxClaimReceivedAmount")
				.and("stock.purchaseInfo.date").as("date").and("stock.chassisNo").as("chassisNo");
		final Aggregation aggregation = Aggregation.newAggregation(match, lookupStock,
				Aggregation.unwind("$stock", true), project);
		final AggregationResults<TPurchaseInvoiceCarTaxDto> result = mongoTemplate.aggregate(aggregation,
				"t_prchs_invc", TPurchaseInvoiceCarTaxDto.class);
		return result.getUniqueMappedResult();
	}

	@Override
	public boolean isExistsByInvoiceNo(String invoiceNo) {
		return mongoOperations.exists(Query.query(Criteria.where("code").regex("(?sim)^" + invoiceNo + "$")),
				TPurchaseInvoice.class);
	}

	@Override
	public String findInvoiceNoByPurchaseDateAndSupplier(Date purchaseDate, String supplierCode) {
		final MatchOperation match = Aggregation
				.match(Criteria.where("purchaseInfo.date").gte(AppUtil.atStartOfDay(purchaseDate))
						.lt(AppUtil.atEndOfDay(purchaseDate)).and("purchaseInfo.supplier").is(supplierCode));
		final LookupOperation lookupInvoice = LookupOperation.newLookup().from("t_prchs_invc").localField("stockNo")
				.foreignField("stockNo").as("invoice");
		final MatchOperation matchInvoiceNo = Aggregation
				.match(Criteria.where("invoice.invoiceNo").exists(true).not().size(0));
		final GroupOperation groupOperation = Aggregation.group().first("invoice.invoiceNo").as("invoiceNo");
		final Aggregation aggregation = Aggregation.newAggregation(match, lookupInvoice,
				Aggregation.unwind("$invoice", true), matchInvoiceNo, groupOperation);
		final AggregationResults<Document> document = mongoTemplate.aggregate(aggregation, TStock.class,
				Document.class);
		final Document result = document.getUniqueMappedResult();
		return AppUtil.isObjectEmpty(result) ? "" : result.getString("invoiceNo");
	}

	@Override
	public List<TPurchaseInvoiceDocumentationRecylceDto> getRecycleClaimByRecycleClaimStatus(
			Integer recycleClaimStatus) {
		final MatchOperation match = Aggregation.match(Criteria.where("status").is(Constants.INV_STATUS_VERIFIED)
				.and("recycleClaimStatus").is(recycleClaimStatus).and("recycle").gt(0));
		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");
		final ProjectionOperation project = Aggregation.project()
				.andInclude("id", "stockNo", "recycleClaimStatus", "recycle", "recycleClaimReceivedAmount",
						"recycleClaimAppliedDate", "recycleClaimReceivedDate", "recycleClaimCharge",
						"recycleClaimInterest", "createdDate")
				.and("stock.purchaseInfo.date").as("purchaseDate").and("stock.chassisNo").as("chassisNo")
				.and("stock.purchaseInfo.type").as("purchaseInfoType").and("supplierId").as("supplierCode")
				.and("auctionHouseId").as("auctionHouseId").and("stock.purchaseInfo.auctionInfo.posNo")
				.as("auctionInfoPosNo").and("stock.destinationCountry").as("destCountry").and("stock.category")
				.as("vehicleType").and("stock.destinationPort").as("destPort")
				.and("stock.purchaseInfo.auctionInfo.lotNo").as("auctionInfoLotNo");

		final SortOperation sort = Aggregation.sort(Direction.DESC, "createdDate");
		final Aggregation aggregation = Aggregation.newAggregation(match, lookupStock,
				Aggregation.unwind("$stock", true), project, sort);
		final AggregationResults<TPurchaseInvoiceDocumentationRecylceDto> result = mongoTemplate.aggregate(aggregation,
				"t_prchs_invc", TPurchaseInvoiceDocumentationRecylceDto.class);
		return result.getMappedResults();
	}

	@Override
	public Long getCountAuctionData(Integer paymentApprove) {
		final MatchOperation match = Aggregation
				.match(new Criteria().andOperator(Criteria.where("status").is(Constants.INV_STATUS_NEW),
						Criteria.where("type").is(Constants.PURCHASE_INVOICE_ITEM_TYPE_PURCHASE)));
		final CountOperation count = Aggregation.count().as("count");
		final Aggregation aggregation = Aggregation.newAggregation(match, count);
		final AggregationResults<Map> result = mongoTemplate.aggregate(aggregation, "t_prchs_invc", Map.class);
		return Long.valueOf(!AppUtil.isObjectEmpty(result.getUniqueMappedResult())
				? result.getUniqueMappedResult().get("count").toString()
				: "0");
	}

	@Override
	public Long getAllPurchasedDataCountShipping() {
		final Criteria criteria = new Criteria();
		final ArrayList<Criteria> andOperators = new ArrayList<>();
		andOperators.add(Criteria.where("status").is(Constants.INV_STATUS_NEW));
		andOperators.add(Criteria.where("type").is(Constants.PURCHASE_INVOICE_ITEM_TYPE_PURCHASE));
		andOperators.add(new Criteria().andOperator(Criteria.where("purchaseCostFlag").ne(Constants.FLAG_YES),
				Criteria.where("CommissionFlag").ne(Constants.FLAG_YES),
				Criteria.where("recycleFlag").ne(Constants.FLAG_YES),
				Criteria.where("roadTaxFlag").ne(Constants.FLAG_YES)));
		criteria.andOperator(andOperators.toArray(new Criteria[0]));
		final MatchOperation matchOperation = Aggregation.match(criteria);

		final CountOperation count = Aggregation.count().as("count");
		final Aggregation aggregation = Aggregation.newAggregation(matchOperation, count);
		final AggregationResults<Map> result = mongoTemplate.aggregate(aggregation, "t_prchs_invc", Map.class);
		return Long.valueOf(!AppUtil.isObjectEmpty(result.getUniqueMappedResult())
				? result.getUniqueMappedResult().get("count").toString()
				: "0");
	}

	@Override
	public Long getAllPurchasedDataCountAccounts() {
		final MatchOperation matchOperation = Aggregation.match(Criteria.where("status").is(Constants.INV_STATUS_NEW));

		final CountOperation count = Aggregation.count().as("count");
		final Aggregation aggregation = Aggregation.newAggregation(matchOperation, count);
		final AggregationResults<Map> result = mongoTemplate.aggregate(aggregation, "t_prchs_invc", Map.class);
		return Long.valueOf(!AppUtil.isObjectEmpty(result.getUniqueMappedResult())
				? result.getUniqueMappedResult().get("count").toString()
				: "0");
	}

	@Override
	public List<ApprovePaymentsDto> findAllByPaymentApproveCompleted() {
		final MatchOperation match = Aggregation.match(new Criteria().andOperator(
				Criteria.where("status").is(Constants.INV_STATUS_VERIFIED), Criteria.where("paymentApprove")
						.in(Constants.PAYMENT_COMPLETED, Constants.PAYMENT_PARTIAL, Constants.PAYMENT_CANCELED)));
		final LookupOperation lookupSupplier = LookupOperation.newLookup().from("m_spplr").localField("supplierId")
				.foreignField("supplierCode").as("supplier_details");

		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");

		final AggregationOperation groupOperation = context -> new Document("$group", new Document("_id", "$invoiceNo")
				.append("approvePaymentItems", new Document("$push", new BasicDBObject("invoiceNo", "$invoiceNo")
						.append("stockNo", "$stockNo").append("chassisNo", "$stock.chassisNo")
						.append("purchaseCost", "$purchaseCost").append("paymentType", "$paymentType")
						.append("roadTax", "$roadTax").append("commision", "$commision")
						.append("otherCharges", "$otherCharges").append("recycle", "$recycle").append("type", "$type")
						.append("purchaseCostTaxAmount",
								new Document("$ifNull", Arrays.asList("$purchaseCostTaxAmount", 0.0)))
						.append("commisionTaxAmount",
								new Document("$ifNull", Arrays.asList("$commisionTaxAmount", 0.0)))
						.append("othersCostTaxAmount",
								new Document("$ifNull", Arrays.asList("$othersCostTaxAmount", 0.0)))
						.append("totalAmount", new Document("$add",
								Arrays.asList("$recycle", "$purchaseCost", "$otherCharges", "$roadTax", "$commision",
										new Document("$ifNull", Arrays.asList("$purchaseCostTaxAmount", 0.0)),
										new Document("$ifNull", Arrays.asList("$commisionTaxAmount", 0.0)),
										new Document("$ifNull", Arrays.asList("$othersCostTaxAmount", 0.0)))))))
				.append("createdDate", new Document("$first", "$createdDate"))
				.append("dueDate", new Document("$first", "$dueDate"))
				.append("invoiceNo", new Document("$first", "$invoiceNo"))
				.append("invoiceName", new Document("$first", "$supplier_details.company"))
				.append("supplierId", new Document("$first", "$supplierId"))
				.append("auctionHouseId", new Document("$first", "$auctionHouseId"))
				.append("auctionRefNo", new Document("$first", "$auctionRefNo"))
				.append("remarks", new Document("$first", "$remarks")).append("type", new Document("$first", "$type"))
				.append("approvePaymentStatus", new Document("$first", "$paymentApprove"))
				.append("invoiceDate", new Document("$first", "$invoiceDate"))
				.append("invoiceAttachmentFilename", new Document("$first", "$invoiceAttachmentFilename"))
				.append("invoiceAttachmentDiskFilename", new Document("$first", "$invoiceAttachmentDiskFilename"))
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
		final AggregationOperation addAuctionHouse = context -> new Document("$addFields",
				new Document("auctionHouseDetails",
						new Document("$filter", new Document("input", "$supplier_details.supplierLocations")
								.append("as", "result").append("cond",
										new Document("$eq", Arrays.asList("$$result._id", "$auctionHouseId"))))));

		final ProjectionOperation project = Aggregation.project()
				.andInclude("approvePaymentItems", "createdDate", "dueDate", "invoiceNo", "invoiceName", "supplierId",
						"paymentVoucherNo", "auctionHouseId", "invoiceDate", "approvePaymentStatus", "auctionRefNo",
						"remarks", "invoiceTransaction", "invoiceAttachmentFilename", "invoiceAttachmentDiskFilename",
						"invoiceAmountReceived")
				.and("$supplier_details.company").as("supplierName").and("$auctionHouseDetails.auctionHouse")
				.as("auctionHouseName").and(AccumulatorOperators.Sum.sumOf("approvePaymentItems.totalAmount"))
				.as("totalAmount").and("supplier_details.company").as("supplier")
				.and("$auctionHouseDetails.auctionHouse").as("auctionHouse")
				.and(LiteralOperators.Literal.asLiteral(Constants.PURCHASE_INVOICE_ITEM_TYPE_PURCHASE)).as("type");
		// sort
		final SortOperation sort = Aggregation.sort(Direction.DESC, "invoiceDate");

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupStock,
				Aggregation.unwind("$stock", true), groupOperation, lookupInvoiceTransaction, filterTransaction,
				paymentVoucherNo, lookupSupplier, Aggregation.unwind("$supplier_details", true), addAuctionHouse,
				Aggregation.unwind("$auctionHouseDetails", true), project, sort);
		final AggregationResults<ApprovePaymentsDto> result = mongoTemplate.aggregate(aggregation, "t_prchs_invc",
				ApprovePaymentsDto.class);
		return result.getMappedResults();
	}

	@Override
	public Long getPaymentApprovalCountAuctionData() {
		final MatchOperation match = Aggregation.match(new Criteria().andOperator(
				Criteria.where("status").is(Constants.INV_STATUS_VERIFIED), Criteria.where("paymentApprove")
						.in(Constants.PAYMENT_COMPLETED, Constants.PAYMENT_PARTIAL, Constants.PAYMENT_CANCELED)));
		final GroupOperation groupOperation = Aggregation.group("$invoiceNo");
		final CountOperation count = Aggregation.count().as("count");
		final Aggregation aggregation = Aggregation.newAggregation(match, groupOperation, count);
		final AggregationResults<Map> result = mongoTemplate.aggregate(aggregation, "t_prchs_invc", Map.class);
		return Long.valueOf(!AppUtil.isObjectEmpty(result.getUniqueMappedResult())
				? result.getUniqueMappedResult().get("count").toString()
				: "0");
	}

	@Override
	public List<ApprovePaymentsDto> findAllByPaymentApproveFreezed(Integer paymentApprove) {
		final MatchOperation match = Aggregation
				.match(new Criteria().andOperator(Criteria.where("status").is(Constants.INV_STATUS_VERIFIED),
						Criteria.where("paymentApprove").is(paymentApprove)));
		final LookupOperation lookupSupplier = LookupOperation.newLookup().from("m_spplr").localField("supplierId")
				.foreignField("supplierCode").as("supplier_details");
		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stockDetails");
		final Document purchaseCostTax = new Document("$multiply",
				Arrays.asList("$purchaseCost", new Document("$divide", Arrays.asList("$purchaseCostTax", 100))));
		final Document commisionTax = new Document("$multiply",
				Arrays.asList("$commision", new Document("$divide", Arrays.asList("$commisionTax", 100))));

		final AggregationOperation groupOperation = context -> new Document("$group", new Document("_id", "$invoiceNo")
				.append("approvePaymentItems", new Document("$push", new BasicDBObject("invoiceNo", "$invoiceNo")
						.append("stockNo", "$stockNo").append("chassisNo", "$stockDetails.chassisNo")
						.append("purchaseCost", "$purchaseCost").append("paymentType", "$paymentType")
						.append("roadTax", "$roadTax").append("commision", "$commision")
						.append("otherCharges", "$otherCharges").append("recycle", "$recycle").append("type", "$type")
						.append("purchaseCostTaxAmount",
								new Document("$ifNull", Arrays.asList("$purchaseCostTaxAmount", 0.0)))
						.append("commisionTaxAmount",
								new Document("$ifNull", Arrays.asList("$commisionTaxAmount", 0.0)))
						.append("othersCostTaxAmount",
								new Document("$ifNull", Arrays.asList("$othersCostTaxAmount", 0.0)))
						.append("totalAmount", new Document("$add",
								Arrays.asList("$recycle", "$purchaseCost", "$otherCharges", "$roadTax", "$commision",
										new Document("$ifNull", Arrays.asList("$purchaseCostTaxAmount", 0.0)),
										new Document("$ifNull", Arrays.asList("$commisionTaxAmount", 0.0)),
										new Document("$ifNull", Arrays.asList("$othersCostTaxAmount", 0.0)))))))
				.append("createdDate", new Document("$first", "$createdDate"))
				.append("dueDate", new Document("$first", "$dueDate"))
				.append("invoiceNo", new Document("$first", "$invoiceNo"))
				.append("invoiceUpload", new Document("$first", "$invoiceUpload"))
				.append("supplierId", new Document("$first", "$supplierId"))
				.append("auctionHouseId", new Document("$first", "$auctionHouseId"))
				.append("remarks", new Document("$first", "$remarks")).append("type", new Document("$first", "$type"))
				.append("invoiceDate", new Document("$first", "$invoiceDate"))
				.append("invoiceAttachmentFilename", new Document("$first", "$invoiceAttachmentFilename"))
				.append("invoiceAttachmentDiskFilename", new Document("$first", "$invoiceAttachmentDiskFilename"))

		);
		final AggregationOperation addAuctionHouse = context -> new Document("$addFields",
				new Document("auctionHouseDetails",
						new Document("$filter", new Document("input", "$supplier_details.supplierLocations")
								.append("as", "result").append("cond",
										new Document("$eq", Arrays.asList("$$result._id", "$auctionHouseId"))))));
		final ProjectionOperation project = Aggregation.project()
				.andInclude("approvePaymentItems", "createdDate", "dueDate", "invoiceNo", "invoiceUpload",
						"invoiceName", "supplierId", "auctionHouseId", "remarks", "invoiceDate",
						"invoiceAttachmentFilename", "invoiceAttachmentDiskFilename")
				.and("$supplier_details.company").as("supplierName").and("$auctionHouseDetails.auctionHouse")
				.as("auctionHouseName").and(AccumulatorOperators.Sum.sumOf("approvePaymentItems.totalAmount"))
				.as("totalAmount").and("supplier_details.company").as("supplier")
				.and("$auctionHouseDetails.auctionHouse").as("auctionHouse")
				.and(LiteralOperators.Literal.asLiteral(Constants.PURCHASE_INVOICE_ITEM_TYPE_PURCHASE)).as("type");

		// sort
		final SortOperation sort = Aggregation.sort(Direction.DESC, "invoiceDate");

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupStock,
				Aggregation.unwind("$stockDetails", true), groupOperation, lookupSupplier,
				Aggregation.unwind("$supplier_details", true), addAuctionHouse,
				Aggregation.unwind("$auctionHouseDetails", true), project, sort);
		final AggregationResults<ApprovePaymentsDto> result = mongoTemplate.aggregate(aggregation, "t_prchs_invc",
				ApprovePaymentsDto.class);
		return result.getMappedResults();
	}

	@Override
	public Long getFreezedCountAuctionData() {
		final MatchOperation match = Aggregation
				.match(new Criteria().andOperator(Criteria.where("status").is(Constants.INV_STATUS_VERIFIED),
						Criteria.where("paymentApprove").is(Constants.PAYMENT_FREEZE)));
		final GroupOperation groupOperation = Aggregation.group("$invoiceNo");
		final CountOperation count = Aggregation.count().as("count");
		final Aggregation aggregation = Aggregation.newAggregation(match, groupOperation, count);
		final AggregationResults<Map> result = mongoTemplate.aggregate(aggregation, "t_prchs_invc", Map.class);
		return Long.valueOf(!AppUtil.isObjectEmpty(result.getUniqueMappedResult())
				? result.getUniqueMappedResult().get("count").toString()
				: "0");
	}

	@Override
	public Long countByRecycleClaimStatus(Integer recycleClaimStatus) {
		final MatchOperation match = Aggregation
				.match(new Criteria().andOperator(Criteria.where("status").is(Constants.INV_STATUS_VERIFIED),
						Criteria.where("recycleClaimStatus").is(recycleClaimStatus), Criteria.where("recycle").gt(0)));
		final CountOperation count = Aggregation.count().as("count");
		final Aggregation aggregation = Aggregation.newAggregation(match, count);
		final AggregationResults<Map> result = mongoTemplate.aggregate(aggregation, "t_prchs_invc", Map.class);
		return Long.valueOf(!AppUtil.isObjectEmpty(result.getUniqueMappedResult())
				? result.getUniqueMappedResult().get("count").toString()
				: "0");
	}

	@Override
	public Long countByPurchaseTaxClaimStatus(Integer purchaseTaxStatus) {
		final MatchOperation match = Aggregation.match(new Criteria().andOperator(
				Criteria.where("purchaseTaxClaimStatus").is(purchaseTaxStatus),
				new Criteria().orOperator(Criteria.where("purchaseCost").exists(true),
						Criteria.where("commision").exists(true), Criteria.where("purchaseCostTaxAmount").exists(true),
						Criteria.where("commisionTaxAmount").exists(true)),
				new Criteria().orOperator(Criteria.where("purchaseCost").gt(0),
						Criteria.where("commision").exists(true).gt(0), Criteria.where("purchaseCostTaxAmount").gt(0),
						Criteria.where("commisionTaxAmount").gt(0))));
		final CountOperation count = Aggregation.count().as("count");
		final Aggregation aggregation = Aggregation.newAggregation(match, count);
		final AggregationResults<Map> result = mongoTemplate.aggregate(aggregation, "t_prchs_invc", Map.class);
		return Long.valueOf(!AppUtil.isObjectEmpty(result.getUniqueMappedResult())
				? result.getUniqueMappedResult().get("count").toString()
				: "0");
	}

	@Override
	public Long countByCommisionTaxClaimStatus(Integer commisionTaxStatus) {
		final MatchOperation match = Aggregation.match(new Criteria().andOperator(
				Criteria.where("commisionTaxClaimStatus").is(commisionTaxStatus),
				new Criteria().orOperator(Criteria.where("purchaseCost").exists(true),
						Criteria.where("commision").exists(true), Criteria.where("purchaseCostTaxAmount").exists(true),
						Criteria.where("commisionTaxAmount").exists(true)),
				new Criteria().orOperator(Criteria.where("purchaseCost").gt(0),
						Criteria.where("commision").exists(true).gt(0), Criteria.where("purchaseCostTaxAmount").gt(0),
						Criteria.where("commisionTaxAmount").gt(0))));
		final CountOperation count = Aggregation.count().as("count");
		final Aggregation aggregation = Aggregation.newAggregation(match, count);
		final AggregationResults<Map> result = mongoTemplate.aggregate(aggregation, "t_prchs_invc", Map.class);
		return Long.valueOf(!AppUtil.isObjectEmpty(result.getUniqueMappedResult())
				? result.getUniqueMappedResult().get("count").toString()
				: "0");
	}

	@Override
	public Long countByCarTaxClaimStatus(Integer carTaxStatus) {
		final MatchOperation match = Aggregation.match(new Criteria().andOperator(
				Criteria.where("type")
						.in(Constants.PURCHASE_INVOICE_ITEM_TYPE_PURCHASE,
								Constants.PURCHASE_INVOICE_ITEM_TYPE_REAUCTION)
						.and("roadTax").gt(0),
				Criteria.where("carTaxClaimStatus").is(carTaxStatus)));
		final CountOperation count = Aggregation.count().as("count");
		final Aggregation aggregation = Aggregation.newAggregation(match, count);
		final AggregationResults<Map> result = mongoTemplate.aggregate(aggregation, "t_prchs_invc", Map.class);
		return Long.valueOf(!AppUtil.isObjectEmpty(result.getUniqueMappedResult())
				? result.getUniqueMappedResult().get("count").toString()
				: "0");
	}

	@Override
	public List<TInvoiceDto> findAllByPaymentOthersCompleted() {
		final MatchOperation match = Aggregation.match(new Criteria().andOperator(Criteria.where("paymentApprove")
				.in(Constants.PAYMENT_COMPLETED, Constants.PAYMENT_PARTIAL, Constants.PAYMENT_CANCELED),
				Criteria.where("deleteFlag").is(Constants.DELETE_FLAG_0)));

		final LookupOperation lookupSupplier = LookupOperation.newLookup().from("m_gen_supplier").localField("remitter")
				.foreignField("code").as("remitterDetails");

		final LookupOperation paymentCategory = LookupOperation.newLookup().from("m_pymnt_ctgry").localField("category")
				.foreignField("categoryCode").as("categoryDetails");
		final Document taxIncluded = new Document("$add", Arrays.asList("$amountInYen", "$taxAmount"));
		final AggregationOperation groupOperation = context -> new Document("$group", new Document("_id", "$invoiceNo")
				.append("approvePaymentItems",
						new Document("$push", new BasicDBObject("invoiceNo", "$invoiceNo")
								.append("invoiceType", "$invoiceType").append("category", "$categoryDetails.category")
								.append("tkcCode", "$categoryDetails.tkcCode")
								.append("tkcDescription", "$categoryDetails.tkcDescription")
								.append("categoryOthers", "$categoryOthers").append("sourceCurrency", "$sourceCurrency")
								.append("amount", "$amount").append("taxAmount", "$taxAmount")
								.append("exchangeRate", "$exchangeRate").append("description", "$description")
								.append("amountInYen", "$amountInYen").append("totalWithTax", taxIncluded)))
				.append("createdDate", new Document("$first", "$createdDate"))
				.append("remitterName", new Document("$first", "$remitterDetails.name"))
				.append("remitter", new Document("$first", "$remitter"))
				.append("remitterOthers", new Document("$first", "$remitterOthers"))
				.append("invoiceNo", new Document("$first", "$invoiceNo"))
				.append("refNo", new Document("$first", "$refNo"))
				.append("invoiceType", new Document("$first", "$invoiceType"))
				.append("invoiceUpload", new Document("$first", "$invoiceUpload"))
				.append("type", new Document("$first", Constants.INVOICE_TYPE_OTHERS))
				.append("amount", new Document("$first", "$amount"))
				.append("dueDate", new Document("$first", "$dueDate"))
				.append("invoiceAttachmentFilename", new Document("$first", "$invoiceAttachmentFilename"))
				.append("invoiceAttachmentDiskFilename", new Document("$first", "$invoiceAttachmentDiskFilename"))
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
				.andInclude("approvePaymentItems", "remitter", "createdDate", "type", "invoiceAttachmentFilename",
						"invoiceAttachmentDiskFilename", "invoiceNo", "invoiceType", "invoiceUpload", "dueDate",
						"paymentApprove", "remitterName", "remitterOthers", "paymentVoucherNo", "refNo")
				.and(AccumulatorOperators.Sum.sumOf("approvePaymentItems.totalWithTax")).as("totalAmount");

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupSupplier,
				Aggregation.unwind("$supplierDetails", true), paymentCategory, groupOperation, lookupInvoiceTransaction,
				filterTransaction, paymentVoucherNo, project);
		final AggregationResults<TInvoiceDto> result = mongoTemplate.aggregate(aggregation, "t_invc",
				TInvoiceDto.class);

		return result.getMappedResults();
	}

	@Override
	public Long getPaymentApprovalCountOthersData() {
		final MatchOperation match = Aggregation.match(Criteria.where("paymentApprove").in(Constants.PAYMENT_COMPLETED,
				Constants.PAYMENT_PARTIAL, Constants.PAYMENT_CANCELED));
		final GroupOperation groupOperation = Aggregation.group("$invoiceNo");
		final CountOperation count = Aggregation.count().as("count");
		final Aggregation aggregation = Aggregation.newAggregation(match, groupOperation, count);
		final AggregationResults<Map> result = mongoTemplate.aggregate(aggregation, "t_invc", Map.class);
		return Long.valueOf(!AppUtil.isObjectEmpty(result.getUniqueMappedResult())
				? result.getUniqueMappedResult().get("count").toString()
				: "0");
	}

	@Override
	public List<PayableAmountDto> getPayableAmountsForRemitters(List<Integer> invoiceStatus) {

		final MatchOperation match = Aggregation.match(
				new Criteria().andOperator(Criteria.where("status").in(invoiceStatus), Criteria.where("paymentApprove")
						.in(Constants.PAYMENT_NOT_APPROVED, Constants.PAYMENT_PARTIAL, Constants.PAYMENT_APPROVED)));

		final Document purchaseCostTax = new Document("$multiply",
				Arrays.asList("$purchaseCost", new Document("$divide", Arrays.asList("$purchaseCostTax", 100))));
		final Document commisionTax = new Document("$multiply",
				Arrays.asList("$commision", new Document("$divide", Arrays.asList("$commisionTax", 100))));
		final Document purchaseTotal = new Document().append("$add", Arrays.asList("$recycle", "$purchaseCost",
				"$otherCharges", "$roadTax", "$commision", purchaseCostTax, commisionTax));

		final AggregationOperation group1 = context -> new Document().append("$group",
				new Document().append("_id", "$invoiceNo")
						.append("supplierId", new Document().append("$first", "$supplierId"))
						.append("invoiceAmountReceived", new Document().append("$first", "$invoiceAmountReceived"))
						.append("total", new Document().append("$sum", purchaseTotal)));
		final AggregationOperation group2 = context -> new Document().append("$group",
				new Document().append("_id", "$supplierId")
						.append("sequenceId", new Document().append("$first", "$supplierId"))

						.append("grandTotal", new Document().append("$sum", "$total"))
						.append("invoiceAmountReceived", new Document().append("$sum", "$invoiceAmountReceived")));
		final LookupOperation lookupSupplier = LookupOperation.newLookup().from("m_spplr").localField("sequenceId")
				.foreignField("supplierCode").as("supplier_details");
		final ProjectionOperation project = Aggregation
				.project("_id", "sequenceId", "grandTotal", "invoiceAmountReceived").and("$supplier_details.company")
				.as("remitter");
		final Aggregation aggregation = Aggregation.newAggregation(match, group1, group2, lookupSupplier,
				Aggregation.unwind("supplier_details", true), project);
		final AggregationResults<PayableAmountDto> result = mongoTemplate.aggregate(aggregation, "t_prchs_invc",
				PayableAmountDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<Document> findBySearchDto(String search) {

		final MatchOperation matchCancelledStock = Aggregation
				.match(Criteria.where("status").is(Constants.INV_STATUS_CANCEL));
		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");

		final ProjectionOperation project = Aggregation.project().andInclude("stockNo", "code").and("$stock.chassisNo")
				.as("chassisNo");

		final Criteria criteria = new Criteria();
		criteria.orOperator(Criteria.where("stockNo").regex(".*" + search + ".*", "i"),
				Criteria.where("chassisNo").regex(".*" + search + ".*", "i"));
		final MatchOperation matchSearch = Aggregation.match(criteria);

		final Aggregation aggregation = Aggregation.newAggregation(matchCancelledStock, lookupStock,
				Aggregation.unwind("$stock", true), project, matchSearch);
		final AggregationResults<Document> result = mongoTemplate.aggregate(aggregation, TPurchaseInvoice.class,
				Document.class);

		return result.getMappedResults();
	}

	@Override
	public List<TPurchaseInvoiceTaxDto> getAllCarTaxOnStatus(Integer commissionStatus, Integer purchaseStatus) {
		final MatchOperation match = Aggregation.match(new Criteria().andOperator(
				Criteria.where("commisionTaxClaimStatus").is(commissionStatus),
				Criteria.where("purchaseTaxClaimStatus").is(purchaseStatus),
				new Criteria().orOperator(Criteria.where("purchaseCost").exists(true),
						Criteria.where("commision").exists(true), Criteria.where("purchaseCostTaxAmount").exists(true),
						Criteria.where("commisionTaxAmount").exists(true)),
				new Criteria().orOperator(Criteria.where("purchaseCost").gt(0),
						Criteria.where("commision").exists(true).gt(0), Criteria.where("purchaseCostTaxAmount").gt(0),
						Criteria.where("commisionTaxAmount").gt(0))));
		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");
		final ProjectionOperation project = Aggregation.project()
				.andInclude("code", "stockNo", "purchaseTaxClaimStatus", "commisionTaxClaimStatus", "purchaseCost",
						"commision")
				.and("stock.purchaseInfo.date").as("purchaseDate").and("stock.chassisNo").as("chassisNo")
				.and("purchaseType").as("purchaseInfoType").and("supplierId").as("supplierCode").and("auctionHouseId")
				.as("auctionHouseId").and("stock.purchaseInfo.auctionInfo.posNo").as("auctionInfoPosNo")
				.and("stock.purchaseInfo.auctionInfo.lotNo").as("auctionInfoLotNo").and("purchaseCostTaxAmount")
				.as("purchaseCostTax").and("commisionTaxAmount").as("commisionTax");
		final Aggregation aggregation = Aggregation.newAggregation(match, lookupStock,
				Aggregation.unwind("$stock", true), project);
		final AggregationResults<TPurchaseInvoiceTaxDto> result = mongoTemplate.aggregate(aggregation, "t_prchs_invc",
				TPurchaseInvoiceTaxDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<TPurchaseInvoiceTaxDto> getAllCarTaxReceivable(Integer commissionStatus, Integer purchaseStatus) {
		final MatchOperation match = Aggregation.match(new Criteria().andOperator(
				new Criteria().orOperator(Criteria.where("commisionTaxClaimStatus").is(commissionStatus),
						Criteria.where("purchaseTaxClaimStatus").is(purchaseStatus)),
				new Criteria().orOperator(Criteria.where("purchaseCost").exists(true),
						Criteria.where("commision").exists(true), Criteria.where("purchaseCostTaxAmount").exists(true),
						Criteria.where("commisionTaxAmount").exists(true)),
				new Criteria().orOperator(Criteria.where("purchaseCost").gt(0),
						Criteria.where("commision").exists(true).gt(0), Criteria.where("purchaseCostTaxAmount").gt(0),
						Criteria.where("commisionTaxAmount").gt(0))));
		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");
		final ProjectionOperation project = Aggregation.project()
				.andInclude("code", "stockNo", "purchaseTaxClaimStatus", "commisionTaxClaimStatus", "purchaseCost",
						"commision")
				.and("stock.purchaseInfo.date").as("purchaseDate").and("stock.chassisNo").as("chassisNo")
				.and("purchaseType").as("purchaseInfoType").and("supplierId").as("supplierCode").and("auctionHouseId")
				.as("auctionHouseId").and("stock.purchaseInfo.auctionInfo.posNo").as("auctionInfoPosNo")
				.and("stock.purchaseInfo.auctionInfo.lotNo").as("auctionInfoLotNo").and("purchaseCostTaxAmount")
				.as("purchaseCostTax").and("commisionTaxAmount").as("commisionTax");
		final Aggregation aggregation = Aggregation.newAggregation(match, lookupStock,
				Aggregation.unwind("$stock", true), project);
		final AggregationResults<TPurchaseInvoiceTaxDto> result = mongoTemplate.aggregate(aggregation, "t_prchs_invc",
				TPurchaseInvoiceTaxDto.class);
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
			andCriterias.add(Criteria.where("supplierId").is(remitter));
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

		final LookupOperation lookupSupplier = LookupOperation.newLookup().from("m_spplr").localField("supplierId")
				.foreignField("supplierCode").as("supplier_details");

		final Document purchaseCostTax = new Document("$multiply",
				Arrays.asList("$purchaseCost", new Document("$divide", Arrays.asList("$purchaseCostTax", 100))));
		final Document commisionTax = new Document("$multiply",
				Arrays.asList("$commision", new Document("$divide", Arrays.asList("$commisionTax", 100))));

		final AggregationOperation groupOperation = context -> new Document("$group", new Document("_id", "$invoiceNo")
				.append("approvePaymentItems", new Document("$push", new BasicDBObject("invoiceDate", "$purDate")
						.append("stockNo", "$stockNo").append("chassisNo", "$chassisNo")
						.append("purchaseCost", "$purchaseCost").append("paymentType", "$paymentType")
						.append("roadTax", "$roadTax").append("commision", "$commision")
						.append("otherCharges", "$otherCharges").append("recycle", "$recycle")
						.append("status", "$status").append("type", "$type")
						.append("purchaseCostTaxAmount",
								new Document("$ifNull", Arrays.asList("$purchaseCostTaxAmount", 0.0)))
						.append("commisionTaxAmount",
								new Document("$ifNull", Arrays.asList("$commisionTaxAmount", 0.0)))
						.append("othersCostTaxAmount",
								new Document("$ifNull", Arrays.asList("$othersCostTaxAmount", 0.0)))
						.append("totalAmount", new Document("$add",
								Arrays.asList("$recycle", "$purchaseCost", "$otherCharges", "$roadTax", "$commision",
										new Document("$ifNull", Arrays.asList("$purchaseCostTaxAmount", 0.0)),
										new Document("$ifNull", Arrays.asList("$commisionTaxAmount", 0.0)),
										new Document("$ifNull", Arrays.asList("$othersCostTaxAmount", 0.0)))))))

				.append("invoiceNo", new Document("$first", "$invoiceNo"))
				.append("refNo", new Document("$first", "$auctionRefNo"))
				.append("invoiceUpload", new Document("$first", "$invoiceUpload"))
				.append("paymentApproveStatus", new Document("$first", "$paymentApprove"))
				.append("supplierId", new Document("$first", "$supplierId"))
				.append("auctionHouseId", new Document("$first", "$auctionHouseId"))
				.append("remarks", new Document("$first", "$remarks"))
				.append("remitter", new Document("$first", "$supplierId"))
				.append("type", new Document("$first", "$type"))
				.append("approvedDate", new Document("$first", "$approvedDate"))
				.append("invoiceDate", new Document("$first", "$invoiceDate"))
				.append("paidAmount", new Document("$first", "$invoiceAmountReceived"))
				.append("approvedBy", new Document("$first", "$approvedBy"))
				.append("invoiceAttachmentFilename", new Document("$first", "$invoiceAttachmentFilename"))
				.append("invoiceAttachmentDiskFilename", new Document("$first", "$invoiceAttachmentDiskFilename"))

		);
		final LookupOperation lookupUser = LookupOperation.newLookup().from("m_usr").localField("approvedBy")
				.foreignField("code").as("userDetails");

		final AggregationOperation addAuctionHouse = contexts -> new Document("$addFields",
				new Document("auctionHouseDetails",
						new Document("$filter", new Document("input", "$supplier_details.supplierLocations")
								.append("as", "result").append("cond",
										new Document("$eq", Arrays.asList("$$result._id", "$auctionHouseId"))))));
		final ProjectionOperation project = Aggregation.project()
				.andInclude("approvePaymentItems", "invoiceNo", "invoiceUpload", "paymentApproveStatus", "supplierId",
						"auctionHouseId", "remarks", "type", "approvedDate", "invoiceDate", "paidAmount",
						"invoiceAttachmentFilename", "invoiceAttachmentDiskFilename","refNo")
				.and("$supplier_details.company").as("supplierName").and("$auctionHouseDetails.auctionHouse")
				.as("auctionHouseName").and(AccumulatorOperators.Sum.sumOf("approvePaymentItems.totalAmount"))
				.as("totalAmount").and("$userDetails.fullname").as("approvedBy");

		final Aggregation aggregation = Aggregation.newAggregation(match, groupOperation, lookupSupplier,
				Aggregation.unwind("$supplier_details", true), addAuctionHouse,
				Aggregation.unwind("$auctionHouseDetails", true), lookupUser, Aggregation.unwind("$userDetails", true),
				project);
		final AggregationResults<PaymentTrackingDto> result = mongoTemplate.aggregate(aggregation, "t_prchs_invc",
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
			andCriterias.add(Criteria.where("supplierId").is(remitter));
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

		final Document purchaseCostTax = new Document("$multiply",
				Arrays.asList("$purchaseCost", new Document("$divide", Arrays.asList("$purchaseCostTax", 100))));
		final Document commisionTax = new Document("$multiply",
				Arrays.asList("$commision", new Document("$divide", Arrays.asList("$commisionTax", 100))));

		final AggregationOperation groupOperation = context -> new Document("$group", new Document("_id", "$invoiceNo")
				.append("approvePaymentItems",
						new Document("$push",
								new BasicDBObject("invoiceDate", "$purDate").append("stockNo", "$stockNo")
										.append("chassisNo", "$chassisNo").append("purchaseCost", "$purchaseCost")
										.append("paymentType", "$paymentType").append("roadTax", "$roadTax")
										.append("commision", "$commision").append("otherCharges", "$otherCharges")
										.append("recycle", "$recycle").append("status", "$status")
										.append("type", "$type").append("purchaseCostTaxAmount", purchaseCostTax)
										.append("commisionTaxAmount", commisionTax)
										.append("totalAmount", new Document("$add",
												Arrays.asList("$recycle", "$purchaseCost", "$otherCharges", "$roadTax",
														"$commision", purchaseCostTax, commisionTax)))))

				.append("invoiceNo", new Document("$first", "$invoiceNo"))
				.append("paymentApproveStatus", new Document("$first", "$paymentApprove"))
				.append("supplierId", new Document("$first", "$supplierId"))
				.append("auctionHouseId", new Document("$first", "$auctionHouseId"))
				.append("remitter", new Document("$first", "$supplierId"))
				.append("type", new Document("$first", "$type"))
				.append("invoiceDate", new Document("$first", "$invoiceDate"))

				.append("paidAmount", new Document("$first", "$invoiceAmountReceived"))

		);
//		final LookupOperation lookupUser = LookupOperation.newLookup().from("m_usr").localField("approvedBy")
//				.foreignField("code").as("userDetails");
//
		final LookupOperation lookupSupplier = LookupOperation.newLookup().from("m_spplr").localField("supplierId")
				.foreignField("supplierCode").as("supplier_details");
		final AggregationOperation addAuctionHouse = contexts -> new Document("$addFields",
				new Document("auctionHouseDetails",
						new Document("$filter", new Document("input", "$supplier_details.supplierLocations")
								.append("as", "result").append("cond",
										new Document("$eq", Arrays.asList("$$result._id", "$auctionHouseId"))))));
		final AggregationExpression totalAmount = contexts -> new Document("$sum", "$approvePaymentItems.totalAmount");
		final AggregationExpression balanceAmount = contexts -> new Document("$subtract",
				Arrays.asList(new Document("$sum", "$approvePaymentItems.totalAmount"), "$paidAmount"));
		final ProjectionOperation project1 = Aggregation.project()
				.andInclude("invoiceDate", "invoiceNo", "paymentApproveStatus", "paidAmount", "auctionHouseId")
				.and("$supplier_details.company").as("supplierName").and("$auctionHouseDetails.auctionHouse")
				.as("auctionHouseName").and(totalAmount).as("totalAmount").and(balanceAmount).as("balance");
		final LookupOperation lookupTransaction = LookupOperation.newLookup().from("t_invc_pymnt_trnsctn")
				.localField("invoiceNo").foreignField("invoiceNo").as("transaction");
		final AggregationOperation filterTransactions = contexts -> new Document("$addFields",
				new Document("transaction",
						new Document("$filter",
								new Document("input", "$transaction").append("as", "result").append("cond",
										new Document("$eq", Arrays.asList("$$result.status",
												Constants.INVOICE_PAYMENT_TRANSACTION_NOT_CANCELLED))))));
//		AggregationExpression formatApprovedDate = contexts -> new Document("$dateToString",
//				new Document("date", "$transaction.approvedDate").append("format", "%d-%m-%Y").append("timezone",
//						"+05:30"));
//		AggregationExpression formatInvoiceDate = contexts -> new Document("$dateToString",
//				new Document("date", "$invoiceDate").append("format", "%d-%m-%Y").append("timezone", "+05:30"));
		final ProjectionOperation project2 = Aggregation.project()
				.andInclude("invoiceDate", "invoiceNo", "paymentApproveStatus", "paidAmount", "auctionHouseId",
						"supplierName", "auctionHouseName", "totalAmount", "balance")
				.and("$transaction.code").as("paymentVoucherNo").and("$transaction.approvedDate").as("approvedDate")
				.and("$transaction.amount").as("paymentAmount");
		final Aggregation aggregation = Aggregation.newAggregation(match, groupOperation, lookupSupplier,
				Aggregation.unwind("$supplier_details", true), addAuctionHouse,
				Aggregation.unwind("$auctionHouseDetails", true), project1, lookupTransaction, filterTransactions,
				Aggregation.unwind("$transaction", true), project2);
		final AggregationResults<PaymentTrackingReportDto> result = mongoTemplate.aggregate(aggregation, "t_prchs_invc",
				PaymentTrackingReportDto.class);
		return result.getMappedResults();
	}

	@Override
	public TPurchaseInvoice findOnePurchaseInvoice(String stockNo, String type) {
		final MatchOperation matchOperation = Aggregation
				.match(Criteria.where("stockNo").is(stockNo).and("type").is(type));
		GroupOperation groupOperationTotal = Aggregation.group("stockNo", "chassisNo").sum("purchaseCost")
				.as("purchaseCost").sum("purchaseCostTaxAmount").as("purchaseCostTaxAmount").sum("commision")
				.as("commision").sum("commisionTaxAmount").as("commisionTaxAmount").first("purchaseCostTax")
				.as("purchaseCostTax").first("commisionTax").as("commisionTax").first("otherChargesTax")
				.as("otherChargesTax").sum("otherCharges").as("otherCharges").sum("othersCostTaxAmount")
				.as("othersCostTaxAmount").sum("roadTax").as("roadTax").sum("recycle").as("recycle");
		final Aggregation aggregation = Aggregation.newAggregation(matchOperation, groupOperationTotal);
		final AggregationResults<TPurchaseInvoice> result = mongoTemplate.aggregate(aggregation, "t_prchs_invc",
				TPurchaseInvoice.class);
		return result.getUniqueMappedResult();
	}

}
