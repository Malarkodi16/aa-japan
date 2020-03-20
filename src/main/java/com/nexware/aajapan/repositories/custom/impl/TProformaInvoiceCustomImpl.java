package com.nexware.aajapan.repositories.custom.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AccumulatorOperators;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators.Filter;
import org.springframework.data.mongodb.core.aggregation.ComparisonOperators.Eq;
import org.springframework.data.mongodb.core.aggregation.CountOperation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import com.mongodb.BasicDBObject;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.ProformaInvoiceDto;
import com.nexware.aajapan.models.ProformaInvoiceItem;
import com.nexware.aajapan.repositories.custom.TProformaInvoiceCustom;
import com.nexware.aajapan.utils.AppUtil;

public class TProformaInvoiceCustomImpl implements TProformaInvoiceCustom {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public ProformaInvoiceDto findByInvoiceNo(String invoiceNo) {
		MatchOperation match = Aggregation.match(Criteria.where("invoiceNo").is(invoiceNo));
		LookupOperation lookupCustomer = LookupOperation.newLookup().from("t_cstmr").localField("customerId")
				.foreignField("code").as("customer");
		LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("items.stockNo")
				.foreignField("stockNo").as("stock");
		LookupOperation lookupLCInVoice = LookupOperation.newLookup().from("t_lc_invc").localField("invoiceNo")
				.foreignField("proformaInvoiceId").as("lcInvoiceList");
		LookupOperation lookupLCDtl = LookupOperation.newLookup().from("t_lc_dtls").localField("lcInvoice.lcDtlId")
				.foreignField("_id").as("lcDtl");
		LookupOperation lookupLCCustomer = LookupOperation.newLookup().from("t_cstmr")
				.localField("lcInvoice.customerId").foreignField("code").as("lcCustomer");
		LookupOperation lookupLogin = LookupOperation.newLookup().from("m_lgn").localField("salesPerson")
				.foreignField("userId").as("userDetails");

		AggregationOperation addFieldsOperation = context -> new Document("$addFields", new Document("lcInvoice",
				new Document("$filter", new Document("input", "$lcInvoiceList").append("as", "result").append("cond",
						new Document("$eq", Arrays.asList("$$result.stockNo", "$stock.stockNo"))))));

		GroupOperation groupOperation = Aggregation.group("$_id")
				.push(new BasicDBObject("fob", "$items.fob").append("insurance", "$items.insurance")
						.append("shipping", "$items.shipping").append("freight", "$items.freight")
						.append("total", "$items.total").append("stockNo", "$stock.stockNo")
						.append("chassisNo", "$stock.chassisNo").append("maker", "$stock.maker")
						.append("model", "$stock.model").append("hsCode", "$stock.hsCode")
						.append("firstRegDate", "$stock.firstRegDate").append("transmission", "$stock.transmission")
						.append("fuel", "$stock.fuel").append("driven", "$stock.driven").append("cc", "$stock.cc")
						.append("type", "$stock.purchaseInfo.type").append("reserve", "$stock.reserve")
						.append("status", "$stock.status").append("lcNo", "$lcDtl.lcNo")
						.append("lcCustomerId", "$lcCustomer.code").append("lcCustFirstName", "$lcCustomer.firstName")
						.append("lcCustLastName", "$lcCustomer.lastName").append("lcAmount", "$lcInvoice.amount"))
				.as("items").first("invoiceNo").as("invoiceNo").first("date").as("date").first("paymentType")
				.as("paymentType").first("fobTotal").as("fobTotal").first("freightTotal").as("freightTotal")
				.first("shippingTotal").as("shippingTotal").first("insuranceTotal").as("insuranceTotal").first("total")
				.as("total").first("paymentType").as("paymentType").first("customer.code").as("customerId")
				.first("customer.port").as("port").first("customer.address").as("customerAddress")
				.first("customer.country").as("country").first("customer.isLcCustomer").as("isLcCustomer")
				.first("customer.firstName").as("firstName").first("customer.mobileNo").as("mobileNo")
				.first("customer.lastName").as("lastName").first("createdDate").as("issueDate").first("currencyType")
				.as("currencyType")
				.first(Filter.filter("customer.consigneeNotifyparties").as("consignee")
						.by(Eq.valueOf("consignee._id").equalToValue("$consigneeId")))
				.as("consignee")
				.first(Filter.filter("$customer.consigneeNotifyparties").as("notifyparty")
						.by(Eq.valueOf("notifyparty._id").equalToValue("$notifypartyId")))
				.as("notifyparty").first("userDetails.username").as("invoiceBy");
		ProjectionOperation project = Aggregation.project()
				.andInclude("invoiceNo", "date", "paymentType", "fobTotal", "freightTotal", "insuranceTotal", "total",
						"shippingTotal", "items", "customerId", "firstName", "lastName", "mobileNo", "isLcCustomer",
						"issueDate", "port", "country", "currencyType", "invoiceBy", "customerAddress")
				.and("consignee._id").as("consigneeId").and("consignee.cFirstName").as("cFirstName")
				.and("consignee.cLastName").as("cLastName").and("consignee.cAddress").as("cAddress")
				.and("consignee.cMobileNo").as("cMobileNo").and("notifyparty._id").as("notifypartyId")
				.and("notifyparty.npFirstName").as("npFirstName").and("notifyparty.npLastName").as("npLastName")
				.and("notifyparty.npAddress").as("npAddress").and("notifyparty.npMobileNo").as("npMobileNo");//

		Aggregation aggregation = Aggregation.newAggregation(match, lookupCustomer, Aggregation.unwind("$customer"),
				Aggregation.unwind("$items", true), lookupStock, Aggregation.unwind("$stock", true), lookupLCInVoice,
				addFieldsOperation, Aggregation.unwind("$lcInvoice", true), lookupLCDtl,
				Aggregation.unwind("$lcDtl", true), lookupLCCustomer, Aggregation.unwind("$lcCustomer", true),
				lookupLogin, Aggregation.unwind("$userDetails", true), groupOperation,
				Aggregation.unwind("$consignee", true), Aggregation.unwind("$notifyparty", true), project

		);
		AggregationResults<ProformaInvoiceDto> result = this.mongoTemplate.aggregate(aggregation, "t_prfrm_invc",
				ProformaInvoiceDto.class);

		return result.getUniqueMappedResult();

	}

	@Override
	public Optional<List<ProformaInvoiceDto>> findAllProformaInvoiceDetailsByCustId(String custId) {

		MatchOperation match = Aggregation.match(Criteria.where("customerId").is(custId));

		LookupOperation lookupCustomer = LookupOperation.newLookup().from("t_cstmr").localField("customerId")
				.foreignField("code").as("customer");
		LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("items.stockNo")
				.foreignField("stockNo").as("stock");
		LookupOperation lookupLCInVoice = LookupOperation.newLookup().from("t_lc_invc").localField("invoiceNo")
				.foreignField("proformaInvoiceId").as("lcInvoiceList");
		LookupOperation lookupLCDtl = LookupOperation.newLookup().from("t_lc_dtls").localField("lcInvoice.lcDtlId")
				.foreignField("_id").as("lcDtl");
		LookupOperation lookupLCCustomer = LookupOperation.newLookup().from("t_cstmr")
				.localField("lcInvoice.customerId").foreignField("code").as("lcCustomer");

		AggregationOperation addFieldsOperation = context -> new Document("$addFields", new Document("lcInvoice",
				new Document("$filter", new Document("input", "$lcInvoiceList").append("as", "result").append("cond",
						new Document("$eq", Arrays.asList("$$result.stockNo", "$stock.stockNo"))))));
		MatchOperation matchLcStatus = Aggregation.match(Criteria.where("stock.lcStatus")
				.is(Constants.STOCK_LC_NOT_APPLIED).andOperator(Criteria.where("stock.reserve").is(Constants.RESERVED),
						Criteria.where("stock.reservedInfo.customerId").is(custId)));
		GroupOperation groupOperation = Aggregation.group("$_id")
				.push(new BasicDBObject("fob", "$items.fob").append("shipping", "$items.shipping")
						.append("insurance", "$items.insurance").append("freight", "$items.freight")
						.append("total", "$items.total").append("stockNo", "$stock.stockNo")
						.append("chassisNo", "$stock.chassisNo").append("maker", "$stock.maker")
						.append("model", "$stock.model").append("hsCode", "$stock.hsCode")
						.append("year", "$stock.firstRegDate").append("reserve", "$stock.reserve")
						.append("reservedInfo", "$stock.reservedInfo").append("status", "$stock.status")
						.append("lcNo", "$lcDtl.lcNo").append("lcCustomerId", "$lcCustomer.code")
						.append("lcCustFirstName", "$lcCustomer.firstName")
						.append("lcCustLastName", "$lcCustomer.lastName").append("lcStatus", "$stock.lcStatus")
						.append("lcAmount", "$lcInvoice.amount"))
				.as("items").first("invoiceNo").as("invoiceNo").first("date").as("date").first("paymentType")
				.as("paymentType").first("createdDate").as("issueDate").first("fobTotal").as("fobTotal")
				.first("freightTotal").as("freightTotal").first("insuranceTotal").as("insuranceTotal")
				.first("shippingTotal").as("shippingTotal").first("$items.total").as("total").first("paymentType")
				.as("paymentType").first("customer.code").as("customerId").first("customer.firstName").as("firstName")
				.first("customer.lastName").as("lastName")
				.first(Filter.filter("customer.consigneeNotifyparties").as("consignee")
						.by(Eq.valueOf("consignee._id").equalToValue("$consigneeId")))
				.as("consignee").first(Filter.filter("$customer.consigneeNotifyparties").as("notifyparty")
						.by(Eq.valueOf("notifyparty._id").equalToValue("$notifypartyId")))
				.as("notifyparty");
		ProjectionOperation project = Aggregation.project()
				.andInclude("invoiceNo", "date", "issueDate", "paymentType", "fobTotal", "freightTotal",
						"insuranceTotal", "items", "customerId", "firstName", "lastName")
				.and(AccumulatorOperators.Sum.sumOf("$items.total")).as("total").and("consignee._id").as("consigneeId")
				.and("consignee.cFirstName").as("cFirstName").and("consignee.cLastName").as("cLastName")
				.and("consignee.cAddress").as("cAddress").and("notifyparty._id").as("notifypartyId")
				.and("notifyparty.npFirstName").as("npFirstName").and("notifyparty.npLastName").as("npLastName")
				.and("notifyparty.npAddress").as("npAddress");

		Aggregation aggregation = Aggregation.newAggregation(match, lookupCustomer, Aggregation.unwind("$customer"),
				Aggregation.unwind("$items", true), lookupStock, Aggregation.unwind("$stock", true), lookupLCInVoice,
				addFieldsOperation, Aggregation.unwind("$lcInvoice", true), lookupLCDtl,
				Aggregation.unwind("$lcDtl", true), lookupLCCustomer, Aggregation.unwind("$lcCustomer", true),
				matchLcStatus, groupOperation, Aggregation.unwind("$consignee", true),
				Aggregation.unwind("$notifyparty", true), project);
		AggregationResults<ProformaInvoiceDto> result = this.mongoTemplate.aggregate(aggregation, "t_prfrm_invc",
				ProformaInvoiceDto.class);

		return Optional.of(result.getMappedResults());

	}

	@Override
	public Optional<List<ProformaInvoiceDto>> findAllProformaInvoiceBySalesPerson(List<String> salesPersonIds) {
		Criteria criteria = new Criteria();
		if (!AppUtil.isObjectEmpty(salesPersonIds)) {
			criteria = Criteria.where("salesPerson").in(salesPersonIds);
		}
		MatchOperation match = Aggregation.match(criteria);

		LookupOperation lookupCustomer = LookupOperation.newLookup().from("t_cstmr").localField("customerId")
				.foreignField("code").as("customer");
		LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("items.stockNo")
				.foreignField("stockNo").as("stock");
		LookupOperation lookupLogin = LookupOperation.newLookup().from("m_lgn").localField("salesPerson")
				.foreignField("userId").as("userDetails");
		final LookupOperation lookupHsCode = LookupOperation.newLookup().from("m_hs_code").localField("stock.category")
				.foreignField("category").as("hsCode");
		GroupOperation groupOperation = Aggregation.group("$_id")
				.push(new BasicDBObject("fob", "$items.fob").append("insurance", "$items.insurance")
						.append("freight", "$items.freight").append("shipping", "$items.shipping")
						.append("total", "$items.total").append("stockNo", "$stock.stockNo")
						.append("shippingInstructionStatus", "$stock.shippingInstructionStatus")
						.append("chassisNo", "$stock.chassisNo").append("maker", "$stock.maker")
						.append("model", "$stock.model").append("hsCode", "$items.hsCode")
						.append("firstRegDate", "$stock.firstRegDate").append("reserve", "$stock.reserve")
						.append("status", "$stock.status").append("cc", "$stock.cc").append("hsCode", "$hsCode.hsCode"))
				.as("items").first("invoiceNo").as("invoiceNo").first("date").as("date").first("createdDate")
				.as("issueDate").first("paymentType").as("paymentType").first("fobTotal").as("fobTotal")
				.first("freightTotal").as("freightTotal").first("insuranceTotal").as("insuranceTotal").first("total")
				.as("total").first("paymentType").as("paymentType").first("customerId").as("customerId")
				.first("consigneeId").as("consigneeId").first("notifypartyId").as("notifypartyId").first("currencyType")
				.as("currencyType").first("userDetails.username").as("invoiceBy");

		ProjectionOperation project1 = Aggregation.project()
				.andInclude("invoiceNo", "date", "issueDate", "paymentType", "items").and("customerId").as("customerId")
				.and("customer.firstName").as("firstName").and("customer.lastName").as("lastName").and("consigneeId")
				.as("consigneeId").and("notifypartyId").as("notifypartyId")
				.and(Filter.filter("customer.consigneeNotifyparties").as("consignee")
						.by(Eq.valueOf("consignee._id").equalToValue("$consigneeId")))
				.as("consignee")
				.and(Filter.filter("customer.consigneeNotifyparties").as("consignee")
						.by(Eq.valueOf("consignee._id").equalToValue("$notifypartyId")))
				.as("notifyparty").and("$currency").as("currencyDetails")
				.and(AccumulatorOperators.Sum.sumOf("$items.fob")).as("fobTotal")
				.and(AccumulatorOperators.Sum.sumOf("$items.insurance")).as("insuranceTotal")
				.and(AccumulatorOperators.Sum.sumOf("$items.shipping")).as("shippingTotal")
				.and(AccumulatorOperators.Sum.sumOf("$items.freight")).as("freightTotal").and("invoiceBy")
				.as("invoiceBy");

		LookupOperation lookupCurrency = LookupOperation.newLookup().from("m_currency").localField("currencyType")
				.foreignField("currencySeq").as("currency");
		ProjectionOperation project2 = Aggregation.project()
				.andInclude("invoiceNo", "date", "issueDate", "paymentType", "fobTotal", "freightTotal",
						"insuranceTotal", "shippingTotal", "items", "customerId", "firstName", "lastName",
						"consigneeId", "notifypartyId", "currencyDetails")
				.and("$consignee.cFirstName").as("cFirstName").and("$consignee.cLastName").as("cLastName")
				.and("$notifyparty.npFirstName").as("npFirstName").and("$notifyparty.npLastName").as("npLastName")
				.and(ArithmeticOperators.Add.valueOf("fobTotal").add("freightTotal").add("insuranceTotal")
						.add("shippingTotal"))
				.as("total").and("invoiceBy").as("invoiceBy");
		SortOperation sort = Aggregation.sort(Direction.DESC, "date");
		Aggregation aggregation = Aggregation.newAggregation(match, Aggregation.unwind("$items", true), lookupStock,
				lookupLogin, Aggregation.unwind("$stock", true), lookupHsCode, Aggregation.unwind("$hsCode", true),
				groupOperation, lookupCustomer, Aggregation.unwind("$customer", true), lookupCurrency,
				Aggregation.unwind("$currency", true), project1, Aggregation.unwind("$consignee", true),
				Aggregation.unwind("$notifyparty", true), project2, sort);
		AggregationResults<ProformaInvoiceDto> result = this.mongoTemplate.aggregate(aggregation, "t_prfrm_invc",
				ProformaInvoiceDto.class);

		return Optional.of(result.getMappedResults());

	}

	@Override
	public long getCountBySalesPersonId(List<String> salesPersonIds) {
		Criteria criteria = new Criteria();
		if (!AppUtil.isObjectEmpty(salesPersonIds)) {
			criteria = Criteria.where("salesPerson").in(salesPersonIds);
		}
		MatchOperation match = Aggregation.match(criteria);

		CountOperation count = Aggregation.count().as("count");
		Aggregation aggregation = Aggregation.newAggregation(match, count);
		AggregationResults<Map> result = this.mongoTemplate.aggregate(aggregation, "t_prfrm_invc", Map.class);
		return Long.valueOf(!AppUtil.isObjectEmpty(result.getUniqueMappedResult())
				? result.getUniqueMappedResult().get("count").toString()
				: "0");
	}

	@Override
	public Document findByProformaInvoiceNo(String invoiceNo) {
		MatchOperation match = Aggregation.match(Criteria.where("invoiceNo").is(invoiceNo));
		LookupOperation lookupCustomer = LookupOperation.newLookup().from("t_cstmr").localField("customerId")
				.foreignField("code").as("customer");
		LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("items.stockNo")
				.foreignField("stockNo").as("stock");
		LookupOperation lookupSales = LookupOperation.newLookup().from("m_usr").localField("salesPerson")
				.foreignField("code").as("salesPerson");
		GroupOperation groupOperation = Aggregation.group("$_id").push(new BasicDBObject("stockNo", "$stock.stockNo"))
				.as("items").first("invoiceNo").as("invoiceNo").first("createdDate").as("createdDate")
				.first("customer.code").as("customerId").first("customer.firstName").as("firstName")
				.first("salesPerson.fullname").as("salesName");
		ProjectionOperation project = Aggregation.project().andInclude("invoiceNo", "createdDate", "items",
				"customerId", "firstName", "salesName");
		Aggregation aggregation = Aggregation.newAggregation(match, lookupCustomer, Aggregation.unwind("$customer"),
				lookupSales, Aggregation.unwind("$salesPerson", true), lookupStock, Aggregation.unwind("$stock", true),
				groupOperation, project);
		AggregationResults<Document> document = this.mongoTemplate.aggregate(aggregation, "t_prfrm_invc",
				Document.class);
		return document.getUniqueMappedResult();
	}

	@Override
	public List<ProformaInvoiceItem> findAllProformaInvoiceByCustId(String custId) {
		MatchOperation match = Aggregation.match(Criteria.where("customerId").is(custId));
		LookupOperation lookupCustomer = LookupOperation.newLookup().from("t_cstmr").localField("customerId")
				.foreignField("code").as("customerDetails");

		AggregationOperation addConsignee = context -> new Document("$addFields", new Document("consigneeDetails",
				new Document("$filter",
						new Document("input", "$customerDetails.consigneeNotifyparties").append("as", "result")
								.append("cond", new Document("$eq", Arrays.asList("$$result._id", "$consigneeId"))))));
		AggregationOperation addNotifyparty = context -> new Document("$addFields",
				new Document("notifypartyDetails",
						new Document("$filter", new Document("input", "$customerDetails.consigneeNotifyparties")
								.append("as", "result").append("cond",
										new Document("$eq", Arrays.asList("$$result._id", "$notifypartyId"))))));

		LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("items.stockNo")
				.foreignField("stockNo").as("stock");

		MatchOperation matchStock = Aggregation.match(Criteria.where("stock.shippingStatus")
				.ne(Constants.STOCK_SHIPPING_STATUS_IDLE).and("stock.lcStatus").ne(Constants.STOCK_LC_APPLIED));

		LookupOperation shippingLookup = LookupOperation.newLookup().from("t_shppng_rqust")
				.localField("stock.shipmentRequestId").foreignField("shipmentRequestId").as("shipmentRequestDetails");
		LookupOperation lookupExportCertificate = LookupOperation.newLookup().from("t_exprt_crtfct")
				.localField("stock.stockNo").foreignField("stockNo").as("exportCertificate");
		final AggregationOperation filterInspectionDetails = (context) -> new Document("$addFields", new Document(
				"inspectionDetails",
				new Document("$filter", new Document("input", "$stock.inspectionDetails").append("as", "result").append(
						"cond",
						new Document("$eq", Arrays.asList("$$result.country", "$stock.destinationCountry"))))));
		LookupOperation lookupInspection = LookupOperation.newLookup().from("t_inspctn_odr_rqst")
				.localField("inspectionDetails.inspectionRequestId").foreignField("code").as("inspectionOrderDetails");
		ProjectionOperation project = Aggregation.project().andInclude("invoiceNo", "consigneeId", "notifypartyId")
				.and("$items.fob").as("fob").and("$items.shipping").as("shipping").and("$items.insurance")
				.as("insurance").and("$items.freight").as("freight").and("$items.total").as("total")
				.and("$items.chassisNo").as("chassisNo").and("$items.maker").as("maker").and("$items.model").as("model")
				.and("$items.hsCode").as("hsCode").and("$items.firstRegDate").as("firstRegDate").and("$stock.reserve")
				.as("reserve").and("$stock.stockNo").as("stockNo").and("$stock.lcStatus").as("lcStatus")
				.and("$stock.reservedInfo").as("reservedInfo").and("$customerDetails.code").as("lcCustomerId")
				.and("$customerDetails.firstName").as("lcCustFirstName").and("$customerDetails.nickName")
				.as("lcCustNickName").and("$consigneeDetails.cFirstName").as("consigneeName")
				.and("$consigneeDetails.cAddress").as("consigneeAddress").and("$notifypartyDetails.npFirstName")
				.as("notifypartyName").and("$notifypartyDetails.npAddress").as("notifypartyAddress")
				.and("$stock.status").as("status").and("$customerDetails.consigneeNotifyparties")
				.as("consigneeNotifyparties").and("$shipmentRequestDetails.destCountry").as("destCountry")
				.and("$shipmentRequestDetails.destPort").as("destPort").and("$shipmentRequestDetails.scheduleId")
				.as("scheduleId").and("$shipmentRequestDetails.shipmentRequestId").as("shipmentRequestId")
				.and("etd.date").as("etd").and("$ship.name").as("shipName").and("inspectionOrderDetails.certificateNo").as("inspectionCertificateNo")
				.and("$exportCertificate.serialNo").as("exportCertificateNo");

		Aggregation aggregation = Aggregation.newAggregation(match, lookupCustomer,
				Aggregation.unwind("$customerDetails"), addConsignee, Aggregation.unwind("$consigneeDetails", true),
				addNotifyparty, Aggregation.unwind("$notifypartyDetails", true), Aggregation.unwind("$items", true),
				lookupStock, Aggregation.unwind("$stock", true), matchStock, shippingLookup,
				Aggregation.unwind("shipmentRequestDetails", true),filterInspectionDetails,
				Aggregation.unwind("$inspectionDetails", true), lookupInspection,
				Aggregation.unwind("$inspectionOrderDetails", true), lookupExportCertificate,
				Aggregation.unwind("$exportCertificate", true), project);
		AggregationResults<ProformaInvoiceItem> result = this.mongoTemplate.aggregate(aggregation, "t_prfrm_invc",
				ProformaInvoiceItem.class);

		return result.getMappedResults();

	}
}
