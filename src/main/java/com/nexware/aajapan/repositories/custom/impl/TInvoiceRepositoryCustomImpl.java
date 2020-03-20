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
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.BasicDBObject;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.ApprovePaymentsDto;
import com.nexware.aajapan.dto.GenaralExpensesDto;
import com.nexware.aajapan.dto.PayableAmountDto;
import com.nexware.aajapan.dto.PaymentTrackingDto;
import com.nexware.aajapan.dto.PaymentTrackingReportDto;
import com.nexware.aajapan.dto.TInvoiceDto;
import com.nexware.aajapan.models.TInvoice;
import com.nexware.aajapan.repositories.custom.TInvoiceRepositoryCustom;
import com.nexware.aajapan.utils.AppUtil;

public class TInvoiceRepositoryCustomImpl implements TInvoiceRepositoryCustom {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<ApprovePaymentsDto> findAllByPaymentApprove(List<Integer> paymentApprove) {
		final MatchOperation match = Aggregation
				.match(new Criteria().andOperator(Criteria.where("paymentApprove").in(paymentApprove),
						Criteria.where("deleteFlag").is(Constants.DELETE_FLAG_0)));

		final LookupOperation lookupSupplier = LookupOperation.newLookup().from("m_gen_supplier").localField("remitter")
				.foreignField("code").as("supplierDetails");
		final LookupOperation lookupCategory = LookupOperation.newLookup().from("m_pymnt_ctgry").localField("category")
				.foreignField("categoryCode").as("categoryDetails");
		Document taxIncluded = new Document("$add", Arrays.asList("$amountInYen", "$taxAmount"));
		final AggregationOperation groupOperation = context -> new Document("$group", new Document("_id", "$invoiceNo")
				.append("approvePaymentItems",
						new Document("$push", new BasicDBObject("invoiceNo", "$invoiceNo")
								.append("invoiceType", "$invoiceType").append("category", "$categoryDetails.category")
								.append("tkcCode", "$categoryDetails.tkcCode")
								.append("tkcDescription", "$categoryDetails.tkcDescription")
								.append("categoryOthers", "$categoryOthers").append("sourceCurrency", "$sourceCurrency")
								.append("amount", "$amount").append("exchangeRate", "$exchangeRate")
								.append("description", "$description").append("amountInYen", "$amountInYen")
								.append("taxAmount", "$taxAmount").append("totalWithTax", taxIncluded)))
				.append("createdDate", new Document("$first", "$createdDate"))
				.append("dueDate", new Document("$first", "$dueDate"))
				.append("type", new Document("$first", Constants.INVOICE_TYPE_OTHERS))
				.append("invoiceNo", new Document("$first", "$invoiceNo"))
				.append("invoiceName", new Document("$first", "$supplierDetails.name"))
				.append("invoiceAttachmentFilename", new Document("$first", "$invoiceAttachmentFilename"))
				.append("invoiceAttachmentDiskFilename", new Document("$first", "$invoiceAttachmentDiskFilename"))
				.append("remitterOthers", new Document("$first", "$remitterOthers"))
				.append("invoiceAmountReceived", new Document("$first", "$invoiceAmountReceived"))
				.append("remitter", new Document("$first", "$remitter"))
				.append("supplierId", new Document("$first", "$remitter")));
		final ProjectionOperation project = Aggregation.project()
				.andInclude("approvePaymentItems", "createdDate", "dueDate", "remitter", "supplierId", "type",
						"invoiceNo", "invoiceName", "invoiceAttachmentFilename", "invoiceAttachmentDiskFilename",
						"remitterOthers", "invoiceAmountReceived")
				.and(AccumulatorOperators.Sum.sumOf("approvePaymentItems.totalWithTax")).as("totalAmount");

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupSupplier,
				Aggregation.unwind("$transporterDetails", true), lookupCategory,
				Aggregation.unwind("$categoryDetails", true), groupOperation, project);
		final AggregationResults<ApprovePaymentsDto> result = mongoTemplate.aggregate(aggregation, "t_invc",
				ApprovePaymentsDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<TInvoiceDto> findAllOtherPaymentNotApproved() {
		final MatchOperation match = Aggregation
				.match(new Criteria().andOperator(Criteria.where("paymentApprove").is(Constants.PAYMENT_NOT_APPROVED),
						Criteria.where("deleteFlag").is(Constants.DELETE_FLAG_0)));
		final LookupOperation lookupSupplier = LookupOperation.newLookup().from("m_gen_supplier").localField("remitter")
				.foreignField("code").as("supplierDetails");

		final LookupOperation paymentCategory = LookupOperation.newLookup().from("m_pymnt_ctgry").localField("category")
				.foreignField("categoryCode").as("categoryDetails");
		Document taxIncluded = new Document("$add", Arrays.asList("$amountInYen", "$taxAmount"));
		final AggregationOperation groupOperation = context -> new Document("$group", new Document("_id", "$invoiceNo")
				.append("approvePaymentItems", new Document("$push", new BasicDBObject("invoiceNo", "$invoiceNo")
						.append("id", "$_id").append("invoiceType", "$invoiceType").append("categoryCode", "$category")
						.append("category", "$categoryDetails.category").append("tkcCode", "$categoryDetails.tkcCode")
						.append("tkcDescription", "$categoryDetails.tkcDescription")
						.append("categoryOthers", "$categoryOthers").append("sourceCurrency", "$sourceCurrency")
						.append("amount", "$amount").append("exchangeRate", "$exchangeRate")
						.append("description", "$description").append("taxAmount", "$taxAmount")
						.append("taxPercentage", "$taxPercentage").append("amountInYen", "$amountInYen")
						.append("taxInclusive", "$taxInclusive").append("taxIncludedAmount", "$taxIncludedAmount")
						.append("totalWithTax", "$taxIncludedAmount")))
				.append("createdDate", new Document("$first", "$createdDate"))
				.append("remitterName", new Document("$first", "$supplierDetails.name"))
				.append("remitter", new Document("$first", "$remitter"))
				.append("invoiceAttachmentFilename", new Document("$first", "$invoiceAttachmentFilename"))
				.append("invoiceAttachmentDiskFilename", new Document("$first", "$invoiceAttachmentDiskFilename"))
				.append("invoiceUpload", new Document("$first", "$invoiceUpload"))
				.append("attachementViewed", new Document("$first", "$attachementViewed"))
				.append("type", new Document("$first", Constants.INVOICE_TYPE_OTHERS))
				.append("remitterOthers", new Document("$first", "$remitterOthers"))
				.append("invoiceNo", new Document("$first", "$invoiceNo"))
				.append("amount", new Document("$first", "$amount"))
				.append("dueDate", new Document("$first", "$dueDate")).append("refNo", new Document("$first", "$refNo"))
				.append("invoiceDate", new Document("$first", "$invoiceDate"))
				.append("paymentApprove", new Document("$first", "$paymentApprove")));

		final ProjectionOperation project = Aggregation.project()
				.andInclude("approvePaymentItems", "remitter", "type", "invoiceAttachmentFilename",
						"invoiceAttachmentDiskFilename", "invoiceUpload", "attachementViewed", "remitterOthers",
						"remitterName", "createdDate", "invoiceNo", "invoiceType", "dueDate", "paymentApprove", "refNo",
						"invoiceDate")
				.and(AccumulatorOperators.Sum.sumOf("approvePaymentItems.totalWithTax")).as("totalAmount");

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupSupplier, paymentCategory,
				Aggregation.unwind("$supplierDetails", true), Aggregation.unwind("$categoryDetails", true),
				groupOperation, project);
		final AggregationResults<TInvoiceDto> result = mongoTemplate.aggregate(aggregation, "t_invc",
				TInvoiceDto.class);
		return result.getMappedResults();
	}

	@Override
	public TInvoiceDto findOneOtherPaymentByInvoiceNo(String invoiceNo) {
		final MatchOperation match = Aggregation.match(Criteria.where("invoiceNo").is(invoiceNo));
		final LookupOperation lookupSupplier = LookupOperation.newLookup().from("m_gen_supplier").localField("remitter")
				.foreignField("code").as("supplierDetails");
		// LookupOperation lookupStock =
		// LookupOperation.newLookup().from("t_stck").localField("stockNo")
		//
		// .foreignField("stockNo").as("stockDetails");
		final LookupOperation paymentCategory = LookupOperation.newLookup().from("m_pymnt_ctgry").localField("category")
				.foreignField("categoryCode").as("categoryDetails");
		Document taxIncluded = new Document("$add", Arrays.asList("$amountInYen", "$taxAmount"));
		final AggregationOperation groupOperation = context -> new Document("$group", new Document("_id", "$invoiceNo")
				.append("approvePaymentItems", new Document("$push", new BasicDBObject("invoiceNo", "$invoiceNo")
						.append("id", "$_id").append("invoiceType", "$invoiceType").append("categoryCode", "$category")
						.append("category", "$categoryDetails.category").append("tkcCode", "$categoryDetails.tkcCode")
						.append("tkcDescription", "$categoryDetails.tkcDescription")
						.append("categoryOthers", "$categoryOthers").append("sourceCurrency", "$sourceCurrency")
						.append("amount", "$amount").append("exchangeRate", "$exchangeRate")
						.append("description", "$description").append("taxAmount", "$taxAmount")
						.append("taxPercentage", "$taxPercentage").append("amountInYen", "$amountInYen")
						.append("taxInclusive", "$taxInclusive").append("taxIncludedAmount", "$taxIncludedAmount")
						.append("totalWithTax", "$taxIncludedAmount")))
				.append("createdDate", new Document("$first", "$createdDate"))
				.append("remitterName", new Document("$first", "$supplierDetails.name"))
				.append("remitter", new Document("$first", "$remitter"))
				.append("invoiceAttachmentFilename", new Document("$first", "$invoiceAttachmentFilename"))
				.append("invoiceAttachmentDiskFilename", new Document("$first", "$invoiceAttachmentDiskFilename"))
				.append("invoiceUpload", new Document("$first", "$invoiceUpload"))
				.append("attachementViewed", new Document("$first", "$attachementViewed"))
				.append("type", new Document("$first", Constants.INVOICE_TYPE_OTHERS))
				.append("remitterOthers", new Document("$first", "$remitterOthers"))
				.append("invoiceNo", new Document("$first", "$invoiceNo"))
				.append("amount", new Document("$first", "$amount"))
				.append("dueDate", new Document("$first", "$dueDate")).append("refNo", new Document("$first", "$refNo"))
				.append("invoiceDate", new Document("$first", "$invoiceDate"))
				.append("paymentApprove", new Document("$first", "$paymentApprove")));

		final ProjectionOperation project = Aggregation.project()
				.andInclude("approvePaymentItems", "remitter", "type", "invoiceAttachmentFilename",
						"invoiceAttachmentDiskFilename", "invoiceUpload", "attachementViewed", "remitterOthers",
						"remitterName", "createdDate", "invoiceNo", "invoiceType", "dueDate", "paymentApprove", "refNo",
						"invoiceDate")
				.and(AccumulatorOperators.Sum.sumOf("approvePaymentItems.totalWithTax")).as("totalAmount");

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupSupplier, paymentCategory,
				Aggregation.unwind("$supplierDetails", true), Aggregation.unwind("$categoryDetails", true),
				groupOperation, project);
		final AggregationResults<TInvoiceDto> result = mongoTemplate.aggregate(aggregation, "t_invc",
				TInvoiceDto.class);
		return result.getUniqueMappedResult();
	}

	@Override
	public Long getCountOthersData(List<Integer> paymentApprove) {
		final MatchOperation match = Aggregation.match(Criteria.where("paymentApprove").in(paymentApprove));
		final GroupOperation groupOperation = Aggregation.group("$invoiceNo");
		final CountOperation count = Aggregation.count().as("count");
		final Aggregation aggregation = Aggregation.newAggregation(match, groupOperation, count);
		final AggregationResults<Map> result = mongoTemplate.aggregate(aggregation, "t_invc", Map.class);
		return Long.valueOf(!AppUtil.isObjectEmpty(result.getUniqueMappedResult())
				? result.getUniqueMappedResult().get("count").toString()
				: "0");
	}

	@Override
	public List<TInvoiceDto> findAllByPaymentApproveFreezed(Integer paymentApprove) {
		final MatchOperation match = Aggregation
				.match(new Criteria().andOperator(Criteria.where("paymentApprove").is(paymentApprove),
						Criteria.where("deleteFlag").is(Constants.DELETE_FLAG_0)));
		final LookupOperation lookupCategory = LookupOperation.newLookup().from("m_pymnt_ctgry").localField("category")
				.foreignField("categoryCode").as("categoryDetails");
		Document taxIncluded = new Document("$add", Arrays.asList("$amountInYen", "$taxAmount"));
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
				.append("remitter", new Document("$first", "$remitter"))
				// .append("remitterOthers", new Document("$first", "$remitterOthers"))
				// .append("remitter", new Document("$first", "$supplierDetails.name"))
				.append("invoiceNo", new Document("$first", "$invoiceNo"))
				.append("invoiceUpload", new Document("$first", "$invoiceUpload"))
				.append("invoiceType", new Document("$first", "$invoiceType"))
				.append("totalAmount", new Document("$first", "$amount"))
				.append("dueDate", new Document("$first", "$dueDate"))
				.append("invoiceDate", new Document("$first", "$invoiceDate"))
				.append("invoiceAttachmentFilename", new Document("$first", "$invoiceAttachmentFilename"))
				.append("invoiceAttachmentDiskFilename", new Document("$first", "$invoiceAttachmentDiskFilename"))
				.append("type", new Document("$first", Constants.INVOICE_TYPE_OTHERS)));
		// final AggregationOperation addAuctionHouse = context -> new
		// Document("$addFields",
		// new Document("auctionHouseDetails",
		// new Document("$filter", new Document("input",
		// "$supplier_details.supplierLocations")
		// .append("as", "result").append("cond",
		// new Document("$eq", Arrays.asList("$$result._id", "$auctionHouseId"))))));

		final LookupOperation lookupSupplier = LookupOperation.newLookup().from("m_gen_supplier").localField("remitter")
				.foreignField("code").as("supplier_details");
		final ProjectionOperation project = Aggregation.project()
				.andInclude("approvePaymentItems", "totalAmount", "createdDate", "invoiceNo", "invoiceType", "dueDate",
						"invoiceDate", "invoiceUpload", "type", "invoiceAttachmentFilename",
						"invoiceAttachmentDiskFilename", "remitter")
				.and("$supplier_details.name").as("remitterName")
				.and(AccumulatorOperators.Sum.sumOf("$approvePaymentItems.totalWithTax")).as("totalAmount");

		// sort
		final SortOperation sort = Aggregation.sort(Direction.DESC, "invoiceDate");

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupCategory,
				Aggregation.unwind("$categoryDetails", true), groupOperation, lookupSupplier,
				Aggregation.unwind("$supplier_details", true), project, sort);
		final AggregationResults<TInvoiceDto> result = mongoTemplate.aggregate(aggregation, "t_invc",
				TInvoiceDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<PayableAmountDto> getPayableAmountsForRemitters() {

		final MatchOperation match = Aggregation
				.match(Criteria.where("paymentApprove").in(Constants.PAYMENT_NOT_APPROVED, Constants.PAYMENT_APPROVED));
		final LookupOperation paymentCategory = LookupOperation.newLookup().from("m_pymnt_ctgry").localField("category")
				.foreignField("categoryCode").as("categoryDetails");
		final AggregationOperation groupOperation = context -> new Document("$group", new Document("_id", "$remitter")
				.append("paymentItems",
						new Document("$push",
								new BasicDBObject("invoiceNo", "$invoiceNo").append("invoiceType", "$category")
										.append("category", "$categoryDetails.category")
										.append("categoryOthers", "$categoryOthers").append("amount", "$amount")))
				.append("remitter", new Document("$first", "$remitter")));

		final LookupOperation lookupForwarder = LookupOperation.newLookup().from("m_frwrdr").localField("remitter")
				.foreignField("code").as("forwarder");

		final ProjectionOperation project = Aggregation.project().andInclude("paymentItems").and("$forwarder.name")
				.as("remitter").and("$remitter").as("sequenceId")
				.and(AccumulatorOperators.Sum.sumOf("paymentItems.amount")).as("grandTotal");

		final Aggregation aggregation = Aggregation.newAggregation(match, paymentCategory,
				Aggregation.unwind("$categoryDetails", true), groupOperation, lookupForwarder,
				Aggregation.unwind("$forwarder", true), project);
		final AggregationResults<PayableAmountDto> result = mongoTemplate.aggregate(aggregation, "t_invc",
				PayableAmountDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<PaymentTrackingDto> purchasepaymentTracking(List<Integer> invoiceStatus, String remitter, Date fromDate,
			Date toDate) {
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
							Criteria.where("invoiceDate").lte(AppUtil.atEndOfDay(toDate)),
							Criteria.where("deleteFlag").is(Constants.DELETE_FLAG_0)));
		}
		if (!isValid) {
			return new ArrayList<>();
		}
		final Criteria matchCriteria = new Criteria();
		matchCriteria.andOperator(andCriterias.toArray(new Criteria[0]));
		final MatchOperation match = Aggregation.match(matchCriteria);
		final LookupOperation lookupSupplier = LookupOperation.newLookup().from("m_gen_supplier").localField("remitter")
				.foreignField("code").as("supplierDetails");
		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stockDetails");
		final LookupOperation lookupCategory = LookupOperation.newLookup().from("m_pymnt_ctgry").localField("category")
				.foreignField("categoryCode").as("categoryDetails");
		Document taxIncluded = new Document("$add", Arrays.asList("$amountInYen", "$taxAmount"));
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
				.append("type", new Document("$first", Constants.INVOICE_TYPE_OTHERS))
				.append("invoiceUpload", new Document("$first", "$invoiceUpload"))
				.append("invoiceNo", new Document("$first", "$invoiceNo"))
				.append("refNo", new Document("$first", "$refNo"))
				.append("invoiceName", new Document("$first", "$supplierDetails.name"))
				.append("remitterOthers", new Document("$first", "$remitterOthers"))
				.append("remitter", new Document("$first", "$remitter"))
				.append("paymentApproveStatus", new Document("$first", "$paymentApprove"))
				.append("approvedDate", new Document("$first", "$approvedDate"))
				.append("invoiceDate", new Document("$first", "$invoiceDate"))
				.append("approvedBy", new Document("$first", "$approvedBy"))
				.append("paidAmount", new Document("$first", "$invoiceAmountReceived"))
				.append("invoiceAttachmentFilename", new Document("$first", "$invoiceAttachmentFilename"))
				.append("invoiceAttachmentDiskFilename", new Document("$first", "$invoiceAttachmentDiskFilename")));
		final LookupOperation lookupUser = LookupOperation.newLookup().from("m_usr").localField("approvedBy")
				.foreignField("code").as("userDetails");
		final ProjectionOperation project = Aggregation.project()
				.andInclude("approvePaymentItems", "remitter", "type", "invoiceNo", "invoiceUpload", "invoiceName",
						"remitterOthers", "paymentApproveStatus", "approvedDate", "invoiceDate", "paidAmount",
						"invoiceAttachmentFilename", "invoiceAttachmentDiskFilename", "refNo")
				.and(AccumulatorOperators.Sum.sumOf("approvePaymentItems.amount")).as("totalAmount")
				.and("$userDetails.fullname").as("approvedBy");

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupSupplier, lookupStock,
				Aggregation.unwind("$stockDetails", true), Aggregation.unwind("$supplierDetails", true), lookupCategory,
				Aggregation.unwind("$categoryDetails", true), groupOperation, lookupUser,
				Aggregation.unwind("$userDetails", true), project);
		final AggregationResults<PaymentTrackingDto> result = mongoTemplate.aggregate(aggregation, "t_invc",
				PaymentTrackingDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<PaymentTrackingReportDto> purchasepaymentTrackingReport(List<Integer> invoiceStatus, String remitter,
			Date fromDate, Date toDate) {
		final List<Criteria> andCriterias = new ArrayList<>();
		boolean isValid = false;
		// if (!AppUtil.isObjectEmpty(remitter)) {
		// isValid = true;
		// andCriterias.add(Criteria.where("remitter").is(remitter));
		// }

		if (!AppUtil.isObjectEmpty(fromDate)) {
			isValid = true;
			andCriterias
					.add(new Criteria().andOperator(Criteria.where("invoiceDate").gte(AppUtil.atStartOfDay(fromDate)),
							Criteria.where("invoiceDate").lte(AppUtil.atEndOfDay(toDate)),
							Criteria.where("deleteFlag").is(Constants.DELETE_FLAG_0)));
		}
		if (!isValid) {
			return new ArrayList<>();
		}
		final Criteria matchCriteria = new Criteria();
		matchCriteria.andOperator(andCriterias.toArray(new Criteria[0]));
		final MatchOperation match = Aggregation.match(matchCriteria);
		final LookupOperation lookupSupplier = LookupOperation.newLookup().from("m_gen_supplier").localField("remitter")
				.foreignField("code").as("supplierDetails");
		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stockDetails");
		final LookupOperation lookupCategory = LookupOperation.newLookup().from("m_pymnt_ctgry").localField("category")
				.foreignField("categoryCode").as("categoryDetails");
		Document taxIncluded = new Document("$add", Arrays.asList("$amountInYen", "$taxAmount"));
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
				.append("invoiceNo", new Document("$first", "$invoiceNo"))
				.append("invoiceName", new Document("$first", "$supplierDetails.name"))
				.append("remitterOthers", new Document("$first", "$remitterOthers"))
				.append("remitter", new Document("$first", "$remitter"))
				.append("paymentApproveStatus", new Document("$first", "$paymentApprove"))
				.append("approvedDate", new Document("$first", "$approvedDate"))
				.append("invoiceDate", new Document("$first", "$invoiceDate"))
				.append("approvedBy", new Document("$first", "$approvedBy"))
				.append("paidAmount", new Document("$first", "$invoiceAmountReceived")));
		final LookupOperation lookupUser = LookupOperation.newLookup().from("m_usr").localField("approvedBy")
				.foreignField("code").as("userDetails");
		final AggregationExpression totalAmount = contexts -> new Document("$sum", "$approvePaymentItems.totalWithTax");
		final AggregationExpression balanceAmount = contexts -> new Document("$subtract",
				Arrays.asList(new Document("$sum", "$approvePaymentItems.totalWithTax"), "$paidAmount"));
		final ProjectionOperation project = Aggregation.project()
				.andInclude("remitter", "invoiceNo", "invoiceName", "remitterOthers", "paymentApproveStatus",
						"invoiceDate", "paidAmount")
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
						"totalAmount", "balance", "remitter", "remitterOthers")
				.and("$transaction.code").as("paymentVoucherNo").and("$transaction.approvedDate").as("approvedDate")
				.and("$transaction.amount").as("paymentAmount");

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupSupplier, lookupStock,
				Aggregation.unwind("$stockDetails", true), Aggregation.unwind("$supplierDetails", true), groupOperation,
				project, lookupTransaction, filterTransactions, Aggregation.unwind("$transaction", true), project2);
		final AggregationResults<PaymentTrackingReportDto> result = mongoTemplate.aggregate(aggregation, "t_invc",
				PaymentTrackingReportDto.class);
		return result.getMappedResults();
	}

	@Override
	public boolean isSameRefNoExistsInGeneralSupplier(String refNo) {
		Criteria criteria = new Criteria().andOperator(Criteria.where("refNo").regex("(?sim)^" + refNo + "$"));
		return mongoTemplate.exists(Query.query(criteria), TInvoice.class);
	}

	@Override
	public boolean isSameRefNoExistsWithInvoiceNo(String invoiceNo, String refNo) {
		Criteria criteria = new Criteria();
		criteria.andOperator(Criteria.where("invoiceNo").is(invoiceNo),
				Criteria.where("refNo").regex("(?sim)^" + refNo + "$"));
		return mongoTemplate.exists(Query.query(criteria), TInvoice.class);
	}

	@Override
	public List<GenaralExpensesDto> findAllGenaralExpensesNotApproved() {
		final MatchOperation match = Aggregation
				.match(new Criteria().andOperator(Criteria.where("paymentApprove").is(Constants.PAYMENT_NOT_APPROVED),
						Criteria.where("deleteFlag").is(Constants.DELETE_FLAG_0)));
		final LookupOperation lookupSupplier = LookupOperation.newLookup().from("m_gen_supplier").localField("remitter")
				.foreignField("code").as("supplierDetails");

		final LookupOperation paymentCategory = LookupOperation.newLookup().from("m_pymnt_ctgry").localField("category")
				.foreignField("categoryCode").as("categoryDetails");

		/*
		 * final AggregationOperation groupOperation = context -> new Document("$group",
		 * new Document("_id", "$invoiceNo") .append("approvePaymentItems", new
		 * Document("$push", new BasicDBObject("invoiceNo", "$invoiceNo") .append("id",
		 * "$_id").append("invoiceType", "$invoiceType").append("categoryCode",
		 * "$category") .append("category",
		 * "$categoryDetails.category").append("tkcCode", "$categoryDetails.tkcCode")
		 * .append("tkcDescription", "$categoryDetails.tkcDescription")
		 * .append("categoryOthers", "$categoryOthers").append("sourceCurrency",
		 * "$sourceCurrency") .append("amount", "$amount").append("exchangeRate",
		 * "$exchangeRate") .append("description", "$description").append("taxAmount",
		 * "$taxAmount") .append("taxPercentage",
		 * "$taxPercentage").append("amountInYen", "$amountInYen")
		 * .append("taxInclusive", "$taxInclusive").append("taxIncludedAmount",
		 * "$taxIncludedAmount") .append("totalWithTax", "$taxIncludedAmount")))
		 * .append("createdDate", new Document("$first", "$createdDate"))
		 * .append("remitterName", new Document("$first", "$supplierDetails.name"))
		 * .append("remitter", new Document("$first", "$remitter"))
		 * .append("invoiceAttachmentFilename", new Document("$first",
		 * "$invoiceAttachmentFilename")) .append("invoiceAttachmentDiskFilename", new
		 * Document("$first", "$invoiceAttachmentDiskFilename"))
		 * .append("invoiceUpload", new Document("$first", "$invoiceUpload"))
		 * .append("attachementViewed", new Document("$first", "$attachementViewed"))
		 * .append("type", new Document("$first", Constants.INVOICE_TYPE_OTHERS))
		 * .append("remitterOthers", new Document("$first", "$remitterOthers"))
		 * .append("invoiceNo", new Document("$first", "$invoiceNo")) .append("amount",
		 * new Document("$first", "$amount")) .append("dueDate", new Document("$first",
		 * "$dueDate")).append("refNo", new Document("$first", "$refNo"))
		 * .append("invoiceDate", new Document("$first", "$invoiceDate"))
		 * .append("paymentApprove", new Document("$first", "$paymentApprove")));
		 */

		final ProjectionOperation project = Aggregation.project().and("invoiceNo").as("invoiceNo").and("createdDate")
				.as("date").and("supplierDetails.name").as("remitTo").and("dueDate").as("dueDate")
				.and("categoryDetails.category").as("category").and("categoryDetails.tkcCode").as("tkcCode")
				.and("categoryDetails.tkcDescription").as("tkcDescription")

				.and("description").as("description").and("amountInYen").as("amountInYen").and("taxAmount")
				.as("taxAmount").and("taxIncludedAmount").as("totalAmount").and("sourceCurrency").as("sourceCurrency")

				.and("amount").as("amount").and("exchangeRate").as("exchangeRate");

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupSupplier, paymentCategory,
				Aggregation.unwind("$supplierDetails", true), Aggregation.unwind("$categoryDetails", true), project);
		final AggregationResults<GenaralExpensesDto> result = mongoTemplate.aggregate(aggregation, "t_invc",
				GenaralExpensesDto.class);
		return result.getMappedResults();
	}
}
//