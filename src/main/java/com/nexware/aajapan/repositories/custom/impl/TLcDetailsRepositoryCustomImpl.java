package com.nexware.aajapan.repositories.custom.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AccumulatorOperators;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators.Filter;
import org.springframework.data.mongodb.core.aggregation.ComparisonOperators.Eq;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.LcInvoiceDto;
import com.nexware.aajapan.dto.TLcInvoiceDto;
import com.nexware.aajapan.repositories.custom.TLcDetailsRepositoryCustom;

@Repository
public class TLcDetailsRepositoryCustomImpl implements TLcDetailsRepositoryCustom {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public Optional<List<LcInvoiceDto>> findAllLcInvoice() {
		MatchOperation match = Aggregation.match(Criteria.where("billOfExchangeStatus")
				.in(Constants.BILL_OF_EXCHANGE_NOT_CREATED, Constants.BILL_OF_EXCHANGE_PARTIALLY_CREATED));
		LookupOperation lookupInvoice = LookupOperation.newLookup().from("t_lc_invc").localField("lcInvoiceNo")
				.foreignField("lcInvoiceNo").as("invoice");
		MatchOperation matchInvoice = Aggregation
				.match(new Criteria().andOperator(Criteria.where("invoice.status").ne(Constants.LC_INVOICE_CANCELLED),
						Criteria.where("invoice.isBolUpdated").is(Constants.BILL_OF_EXCHANGE_NOT_UPDATED)));

		LookupOperation lookupBank = LookupOperation.newLookup().from("m_frgn_bnks").localField("bankId")
				.foreignField("bankId").as("bank");
		LookupOperation lookupProforma = LookupOperation.newLookup().from("t_prfrm_invc")
				.localField("invoice.proformaInvoiceId").foreignField("invoiceNo").as("proformaDetails");

		LookupOperation lookupInvoiceCustomer = LookupOperation.newLookup().from("t_cstmr")
				.localField("proformaDetails.customerId").foreignField("code").as("InvCustDetails");

		LookupOperation shippingLookup = LookupOperation.newLookup().from("t_shppng_rqust")
				.localField("invoice.stockNo").foreignField("stockNo").as("shippingStock");

		LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("invoice.stockNo")
				.foreignField("stockNo").as("stock");
		LookupOperation lookupExportCertificate = LookupOperation.newLookup().from("t_exprt_crtfct")
				.localField("$invoice.stockNo").foreignField("stockNo").as("exportCertificate");
		final AggregationOperation filterInspectionDetails = (context) -> new Document("$addFields",
				new Document("inspectionDetails", new Document("$filter",
						new Document("input", "$stock.inspectionDetails").append("as", "result").append("cond",
								new Document("$eq", Arrays.asList("$$result.country", "$stock.destinationCountry"))))));
		LookupOperation lookupInspection = LookupOperation.newLookup().from("t_inspctn_odr_rqst")
				.localField("inspectionDetails.inspectionRequestId").foreignField("code").as("inspectionOrderDetails");

		final AggregationOperation shippingFilter = (context) -> new Document("$addFields",
				new Document("shippingFilterResult",
						new Document("$filter",
								new Document("input", "$shippingStock").append("as", "result").append("cond",
										new Document("$and", Arrays.asList(

												new Document("$ne", Arrays.asList("$$result.scheduleId", null)),
												new Document("$ne", Arrays.asList("$$result.shippingStatus", 0))))))));

		GroupOperation group = Aggregation.group("$_id").push(new BasicDBObject("stockNo", "$invoice.stockNo")
				.append("chassisNo", "$invoice.chassisNo").append("invoiceId", "$invoice._id")
				.append("maker", "$invoice.maker").append("model", "$invoice.model").append("hsCode", "$invoice.hsCode")

				.append("fob", "$invoice.fob").append("insurance", "$invoice.insurance")
				.append("freight", "$invoice.freight").append("amount", "$invoice.amount")
				.append("schedule", "$invoice.schedule").append("to", "$invoice.to").append("from", "$invoice.from")
				.append("from", "$invoice.from").append("sailingDate", "$invoice.sailingDate")
				.append("bankSentDate", "$invoice.bankSentDate").append("shippingMarks", "$invoice.shippingMarks")
				.append("shippingMarksId", "$invoice.shippingMarksId").append("perVessel", "$invoice.perVessel")
				.append("customerId", "$InvCustDetails.code").append("customerName", "$InvCustDetails.firstName")
				.append("scheduleId", "$shippingFilterResult.scheduleId")
				.append("proConsigneeId", "$proformaDetails.consigneeId")
				.append("proNotifypartyId", "$proformaDetails.notifypartyId")
				.append("consigneeName", "$consigneeDetails.cFirstName")
				.append("notifypartyName", "$notifypartyDetails.npFirstName")
				.append("consigneeAddress", "$consigneeDetails.cAddress")
				.append("notifypartyAddress", "$notifypartyDetails.npAddress").append("hsCode", "$invoice.hsCode")
				.append("proformaInvoiceId", "$invoice.proformaInvoiceId")
				.append("proformaInvoiceNo", "$invoice.proformaInvoiceNo")
				.append("inspectionCertificateNo", "$inspectionOrderDetails.certificateNo")
				.append("exportCertificateNo", "$exportCertificate.serialNo")
	
				
				.append("shipmentRequestId", "$shippingFilterResult.shipmentRequestId")).as("items")
				.first("lcInvoiceNo").as("lcInvoiceNo").first("lcNo").as("lcNo").first("issueDate").as("issueDate")
				.first("expiryDate").as("expiryDate").first("bank.bank").as("bank")//
				.first("proformaInvoiceId").as("proformaInvoiceId").first("billOfExchangeNo").as("billOfExchangeNo")
				.first("consigneeName").as("consignee").first("InvCustDetails.firstName").as("customerName")
				.first("cAddress").as("cAddress").first("npAddress").as("npAddress").first("notifyPartyName")
				.as("notifyParty").first("perVessel").as("perVessel").first("from").as("from").first("to").as("to")
				.first("sailingDate").as("sailingDate").first("bankSentDate").as("bankSentDate").first("shippingMarks")
				.as("shippingMarks").first("shippingTerms").as("shippingTerms").first("shippingTermsName")
				.as("shippingTermsName").first("beneficiaryCertify").as("beneficiaryCertify").first("licenseDoc")
				.as("licenseDoc").first("InvCustDetails.consigneeNotifyparties").as("consigneeNotifyparties")
				.first("consignee").as("consigneeId").first("notifyParty").as("notifypartyId").first("bankId")
				.as("bankId").first("customerId").as("customerId").first("amount").as("amount");
		ProjectionOperation project = Aggregation.project().andInclude("items", "lcInvoiceNo", "lcNo", "issueDate",
				"expiryDate", "bank", "proformaInvoiceId", "amount", "billOfExchangeNo", "consignee", "notifyParty",
				"customerName", "cAddress", "npAddress", "perVessel", "from", "to", "sailingDate", "bankSentDate",
				"shippingMarks", "shippingTerms", "shippingTermsName", "consigneeNotifyparties", "consigneeId",
				"notifypartyId", "beneficiaryCertify", "licenseDoc", "bankId", "customerId");

		Aggregation aggregation = Aggregation.newAggregation(match, lookupInvoice, Aggregation.unwind("$invoice", true),
				matchInvoice, lookupBank, Aggregation.unwind("$bank", true), lookupProforma,
				Aggregation.unwind("$proformaDetails", true), lookupInvoiceCustomer,
				Aggregation.unwind("$InvCustDetails", true), shippingLookup, shippingFilter,
				Aggregation.unwind("shippingFilterResult", true), lookupStock, Aggregation.unwind("$stock", true),
				filterInspectionDetails, Aggregation.unwind("$inspectionDetails", true), lookupInspection,
				Aggregation.unwind("$inspectionOrderDetails", true), lookupExportCertificate,
				Aggregation.unwind("$exportCertificate", true), group, project);
		AggregationResults<LcInvoiceDto> result = this.mongoTemplate.aggregate(aggregation, "t_lc_dtls",
				LcInvoiceDto.class);
		return Optional.of(result.getMappedResults());
	}

	@Override
	public Optional<List<LcInvoiceDto>> findOneLcInvoiceByLcNO(String lcNo) {

		MatchOperation match = Aggregation.match(
				new Criteria().andOperator(Criteria.where("lcNo").is(lcNo), Criteria.where("billOfExchangeStatus")
						.in(Constants.BILL_OF_EXCHANGE_NOT_CREATED, Constants.BILL_OF_EXCHANGE_PARTIALLY_CREATED)));
		LookupOperation lookupInvoice = LookupOperation.newLookup().from("t_lc_invc").localField("lcInvoiceNo")
				.foreignField("lcInvoiceNo").as("invoice");
		MatchOperation matchInvoice = Aggregation
				.match(new Criteria().andOperator(Criteria.where("invoice.status").ne(Constants.LC_INVOICE_CANCELLED),
						Criteria.where("invoice.isBolUpdated").is(Constants.BILL_OF_EXCHANGE_NOT_UPDATED)));

		LookupOperation lookupBank = LookupOperation.newLookup().from("m_frgn_bnks").localField("bankId")
				.foreignField("bankId").as("bank");
		LookupOperation lookupProforma = LookupOperation.newLookup().from("t_prfrm_invc")
				.localField("invoice.proformaInvoiceId").foreignField("invoiceNo").as("proformaDetails");

		LookupOperation lookupInvoiceCustomer = LookupOperation.newLookup().from("t_cstmr")
				.localField("proformaDetails.customerId").foreignField("code").as("InvCustDetails");

		LookupOperation shippingLookup = LookupOperation.newLookup().from("t_shppng_rqust")
				.localField("invoice.stockNo").foreignField("stockNo").as("shippingStock");

		final AggregationOperation shippingFilter = (context) -> new Document("$addFields",
				new Document("shippingFilterResult",
						new Document("$filter",
								new Document("input", "$shippingStock").append("as", "result").append("cond",
										new Document("$and", Arrays.asList(
												// new Document("$ifNull", Arrays.asList("$$result", null)),
												new Document("$ne", Arrays.asList("$$result.scheduleId", null)),
												new Document("$ne", Arrays.asList("$$result.shippingStatus", 0))))))));

		GroupOperation group = Aggregation.group("$_id").push(new BasicDBObject("stockNo", "$invoice.stockNo")
				.append("chassisNo", "$invoice.chassisNo").append("invoiceId", "$invoice._id")
				.append("maker", "$invoice.maker").append("model", "$invoice.model").append("hsCode", "$invoice.hsCode")
				.append("fob", "$invoice.fob").append("insurance", "$invoice.insurance")
				.append("freight", "$invoice.freight").append("amount", "$invoice.amount")
				.append("schedule", "$invoice.schedule").append("to", "$invoice.to").append("from", "$invoice.from")
				.append("from", "$invoice.from").append("sailingDate", "$invoice.sailingDate")
				.append("bankSentDate", "$invoice.bankSentDate").append("shippingMarks", "$invoice.shippingMarks")
				.append("shippingMarksId", "$invoice.shippingMarksId").append("perVessel", "$invoice.perVessel")
				.append("customerId", "$InvCustDetails.code").append("customerName", "$InvCustDetails.firstName")
				.append("scheduleId", "$shippingFilterResult.scheduleId")
				.append("proConsigneeId", "$proformaDetails.consigneeId")
				.append("proNotifypartyId", "$proformaDetails.notifypartyId")
				.append("consigneeName", "$consigneeDetails.cFirstName")
				.append("notifypartyName", "$notifypartyDetails.npFirstName")
				.append("consigneeAddress", "$consigneeDetails.cAddress")
				.append("notifypartyAddress", "$notifypartyDetails.npAddress")
				.append("proformaInvoiceId", "$invoice.proformaInvoiceId")
				.append("proformaInvoiceNo", "$invoice.proformaInvoiceNo")
				.append("shipmentRequestId", "$shippingFilterResult.shipmentRequestId")).as("items")
				.first("lcInvoiceNo").as("lcInvoiceNo").first("lcNo").as("lcNo").first("issueDate").as("issueDate")
				.first("expiryDate").as("expiryDate").first("bank.bank").as("bank")//
				.first("proformaInvoiceId").as("proformaInvoiceId").first("billOfExchangeNo").as("billOfExchangeNo")
				.first("consigneeName").as("consignee").first("InvCustDetails.firstName").as("customerName")
				.first("cAddress").as("cAddress").first("npAddress").as("npAddress").first("notifyPartyName")
				.as("notifyParty").first("perVessel").as("perVessel").first("from").as("from").first("to").as("to")
				.first("sailingDate").as("sailingDate").first("bankSentDate").as("bankSentDate").first("shippingMarks")
				.as("shippingMarks").first("shippingTerms").as("shippingTerms").first("shippingTermsName")
				.as("shippingTermsName").first("beneficiaryCertify").as("beneficiaryCertify").first("licenseDoc")
				.as("licenseDoc").first("InvCustDetails.consigneeNotifyparties").as("consigneeNotifyparties")
				.first("consignee").as("consigneeId").first("notifyParty").as("notifypartyId").first("bankId")
				.as("bankId").first("customerId").as("customerId").first("amount").as("amount");
		ProjectionOperation project = Aggregation.project().andInclude("items", "lcInvoiceNo", "lcNo", "issueDate",
				"expiryDate", "bank", "proformaInvoiceId", "amount", "billOfExchangeNo", "consignee", "notifyParty",
				"customerName", "cAddress", "npAddress", "perVessel", "from", "to", "sailingDate", "bankSentDate",
				"shippingMarks", "shippingTerms", "shippingTermsName", "consigneeNotifyparties", "consigneeId",
				"notifypartyId", "beneficiaryCertify", "licenseDoc", "bankId", "customerId");
		Aggregation aggregation = Aggregation.newAggregation(match, lookupInvoice, Aggregation.unwind("$invoice", true),
				matchInvoice, lookupBank, Aggregation.unwind("$bank", true), lookupProforma,
				Aggregation.unwind("$proformaDetails", true), lookupInvoiceCustomer,
				Aggregation.unwind("$InvCustDetails", true), shippingLookup, shippingFilter,
				Aggregation.unwind("shippingFilterResult", true), group, project);
		AggregationResults<LcInvoiceDto> result = this.mongoTemplate.aggregate(aggregation, "t_lc_dtls",
				LcInvoiceDto.class);
		return Optional.of(result.getMappedResults());
	}

	@Override
	public Optional<LcInvoiceDto> findOneLcInvoiceById(String lcInvoiceNo) {
		MatchOperation match = Aggregation.match(Criteria.where("lcInvoiceNo").is(lcInvoiceNo));
		LookupOperation lookupInvoice = LookupOperation.newLookup().from("t_lc_invc").localField("lcInvoiceNo")
				.foreignField("lcInvoiceNo").as("invoice");
		LookupOperation lookupBank = LookupOperation.newLookup().from("m_frgn_bnks").localField("bankId")
				.foreignField("bankId").as("bank");
		LookupOperation lookupProforma = LookupOperation.newLookup().from("t_prfrm_invc")
				.localField("proformaInvoiceId").foreignField("invoiceNo").as("proformaDetails");
		LookupOperation lookupProformaCustomer = LookupOperation.newLookup().from("t_cstmr")
				.localField("proformaDetails.customerId").foreignField("code").as("ProCustDetails");
		LookupOperation lookupInvoiceCustomer = LookupOperation.newLookup().from("t_cstmr")
				.localField("proformaDetails.customerId").foreignField("code").as("InvCustDetails");

		AggregationOperation addConsignee = context -> new Document("$addFields", new Document("consigneeDetails",
				new Document("$filter",
						new Document("input", "$InvCustDetails.consigneeNotifyparties").append("as", "result")
								.append("cond", new Document("$eq", Arrays.asList("$$result._id", "$consignee"))))));
		AggregationOperation addNotifyparty = context -> new Document("$addFields", new Document("notifypartyDetails",
				new Document("$filter",
						new Document("input", "$InvCustDetails.consigneeNotifyparties").append("as", "result")
								.append("cond", new Document("$eq", Arrays.asList("$$result._id", "$notifyParty"))))));

		GroupOperation group = Aggregation.group("$_id")
				.push(new BasicDBObject("stockNo", "$invoice.stockNo").append("chassisNo", "$invoice.chassisNo")
						.append("invoiceId", "$invoice._id").append("maker", "$invoice.maker")
						.append("model", "$invoice.model").append("hsCode", "$invoice.hsCode")
						.append("amount", "$invoice.amount").append("customerId", "$InvCustDetails.firstName"))
				.as("items").first("lcNo").as("lcNo").first("validity").as("validity").first("bank.bank").as("bank")
				.first("invoice.amount").as("amount").first("proformaInvoiceId").as("proformaInvoiceId")
				.first("billOfExchangeNo").as("billOfExchangeNo").first("consigneeDetails.cFirstName").as("consignee")
				.first("ProCustDetails.firstName").as("customerName").first("cAddress").as("cAddress")
				.first("notifypartyDetails.npFirstName").as("notifyParty").first("npAddress").as("npAddress")
				.first("perVessel").as("perVessel").first("from").as("from").first("to").as("to").first("sailingDate")
				.as("sailingDate").first("bankSentDate").as("bankSentDate").first("shippingMarks").as("shippingMarks")
				.first("shippingTerms").as("shippingTerms").first("bankId").as("bankId");
		Aggregation aggregation = Aggregation.newAggregation(match, lookupInvoice, Aggregation.unwind("$invoice", true),
				lookupBank, Aggregation.unwind("$bank", true), lookupProforma,
				Aggregation.unwind("$proformaDetails", true), lookupProformaCustomer,
				Aggregation.unwind("$ProCustDetails", true), lookupInvoiceCustomer, lookupProformaCustomer,
				Aggregation.unwind("$InvCustDetails", true), addConsignee,
				Aggregation.unwind("$consigneeDetails", true), addNotifyparty,
				Aggregation.unwind("$notifypartyDetails", true), group);
		AggregationResults<LcInvoiceDto> result = this.mongoTemplate.aggregate(aggregation, "t_lc_dtls",
				LcInvoiceDto.class);
		return Optional.of(result.getUniqueMappedResult());
	}

	@Override
	public List<TLcInvoiceDto> findOneLcDetailsId(String lcInvoiceNo) {
		MatchOperation match = Aggregation.match(Criteria.where("lcInvoiceNo").is(lcInvoiceNo));
		LookupOperation lookupInvoice = LookupOperation.newLookup().from("t_lc_invc").localField("lcInvoiceNo")
				.foreignField("lcInvoiceNo").as("invoice");
		LookupOperation lookupBank = LookupOperation.newLookup().from("m_frgn_bnks").localField("bankId")
				.foreignField("bankId").as("bank");
		LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("invoice.stockNo")
				.foreignField("stockNo").as("stock");
		LookupOperation lookupShippingRequest = LookupOperation.newLookup().from("t_shppng_rqust")
				.localField("stock.shipmentRequestId").foreignField("shipmentRequestId").as("shippingRequest");
		LookupOperation lookupProformainvoice = LookupOperation.newLookup().from("t_prfrm_invc")
				.localField("invoice.proformaInvoiceId").foreignField("invoiceNo").as("proformaInvoice");
		LookupOperation lookupInvoiceCustomer = LookupOperation.newLookup().from("t_cstmr")
				.localField("proformaInvoice.customerId").foreignField("code").as("InvCustDetails");
		LookupOperation lookupExportCertificate = LookupOperation.newLookup().from("t_exprt_crtfct")
				.localField("stock.stockNo").foreignField("stockNo").as("exportCertificate");
		final AggregationOperation filterInspectionDetails = (context) -> new Document("$addFields", new Document(
				"inspectionDetails",
				new Document("$filter", new Document("input", "$stock.inspectionDetails").append("as", "result").append(
						"cond",
						new Document("$eq", Arrays.asList("$$result.country", "$shippingRequest.destCountry"))))));
		LookupOperation lookupInspection = LookupOperation.newLookup().from("t_inspctn_odr_rqst")
				.localField("inspectionDetails.inspectionRequestId").foreignField("code").as("inspectionOrderDetails");
		ProjectionOperation project = Aggregation.project()
				.andInclude("npAddress", "lcNo", "proformaInvoiceId", "lcInvoiceNo", "createdDate", "shippingTerms",
						"sailingDate", "bankSentDate", "issueDate", "expiryDate", "bankId", "billOfExchangeNo")
				.and("invoice.from").as("from").and("invoice.to").as("to").and("invoice.perVessel").as("perVessel")
				.and("invoice.shippingMarks").as("shippingMarks").and("invoice.sailingDate").as("sailingDate")
				.and("_id").as("lcDtlId").and("invoice.maker").as("maker").and("invoice.model").as("model")
				.and("stock.modelType").as("type").and("stock.chassisNo").as("chassisNo").and("stock.stockNo")
				.as("stockNo").and("invoice.amount").as("stockAmount").and("stock.fuel").as("fuel")
				.and("stock.equipment").as("equipment").and("stock.firstRegDate").as("firstRegDate")
				.and("invoice.proformaInvoiceNo").as("proformaInvoiceNo").and("proformaInvoice.createdDate")
				.as("proformaInvoiceDate").and("invoice.hsCode").as("hsCode").and("validity").as("lcExpiryDate")
				.and(Filter.filter("proformaInvoice.items").as("result")
						.by(Eq.valueOf("result.stockNo").equalToValue("$stock.stockNo")))
				.as("proformaStockData").and("notifyPartyName").as("notifyParty").and("bank.bank").as("bank")
				.and("beneficiaryCertify").as("beneficiaryCertify").and("licenseDoc").as("licenseDoc")
				.and("inspectionOrderDetails.certificateNo").as("inspectionCertificateNo")
				.and("$exportCertificate.serialNo").as("exportCertificateNo").and("invoice.fob").as("fob")
				.and("invoice.freight").as("freight").and("invoice.insurance").as("insurance").and("invoice.amount")
				.as("total");

		ProjectionOperation project1 = Aggregation
				.project("issueDate", "expiryDate", "fob", "freight", "insurance", "total").and("$notifyParty")
				.as("notifyParty").and("$npAddress").as("npAddress").and("$proformaInvoiceId").as("proformaInvoiceId")
				.and("$lcNo").as("lcNo").and("$createdDate").as("createdDate").and("$lcInvoiceNo").as("lcInvoiceNo")
				.and("$lcDtlId").as("lcDtlId").and("$shippingMarks").as("shippingMarks").and("$shippingTerms")
				.as("shippingTerms").and("$billOfExchangeNo").as("billOfExchangeNo")

				.and("$perVessel").as("perVessel").and("$from").as("from").and("$to").as("to").and("$sailingDate")
				.as("sailingDate").and("$bankSentDate").as("bankSentDate").and("$maker").as("maker").and("$model")
				.as("model").and("$type").as("type").and("$chassisNo").as("chassisNo").and("$stockNo").as("stockNo")
				.and("$stockAmount").as("stockAmount").and("$fuel").as("fuel").and("$equipment").as("equipment")
				.and("$firstRegDate").as("firstRegDate").and("$proformaInvoiceNo").as("proformaInvoiceNo")
				.and("$proformaInvoiceDate").as("proformaInvoiceDate").and("$hsCode").as("hsCode").and("$lcExpiryDate")
				.as("lcExpiryDate").and("$bank").as("bank").and("$beneficiaryCertify").as("beneficiaryCertify")
				.and("$licenseDoc").as("licenseDoc")
//				.and("$proformaStockData.fob").as("fob")
//				.and("$proformaStockData.insurance").as("insurance")
//				.and("$proformaStockData.freight").as("freight")
//				.and("$proformaStockData.shipping").as("inspection")
//				.and("$proformaStockData.total").as("total")
				.and("$inspectionCertificateNo").as("inspectionCertificateNo").and("$exportCertificateNo")
				.as("exportCertificateNo");

		Aggregation aggregation = Aggregation.newAggregation(match, lookupInvoice, Aggregation.unwind("$invoice", true),
				lookupBank, Aggregation.unwind("$bank", true), lookupStock, Aggregation.unwind("$stock", true),
				lookupShippingRequest, Aggregation.unwind("shippingRequest", true), filterInspectionDetails,
				Aggregation.unwind("$inspectionDetails", true), lookupInspection,
				Aggregation.unwind("$inspectionOrderDetails", true), lookupExportCertificate,
				Aggregation.unwind("$exportCertificate", true), lookupProformainvoice,
				Aggregation.unwind("$proformaInvoice", true), lookupInvoiceCustomer,
				Aggregation.unwind("$InvCustDetails", true), project, Aggregation.unwind("$proformaStockData", true),

				project1);
		AggregationResults<TLcInvoiceDto> result = this.mongoTemplate.aggregate(aggregation, "t_lc_dtls",
				TLcInvoiceDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<Document> getBillOfExchangeDetails() {
		return new ArrayList<>();
	}

	@Override
	public List<Document> updateFields() {

		MatchOperation matchOperation = Aggregation.match(Criteria.where("billOfExchangeStatus").is(2));
		LookupOperation lookupInvoice = LookupOperation.newLookup().from("t_lc_invc").localField("lcInvoiceNo")
				.foreignField("lcInvoiceNo").as("invoiceDetails");

		LookupOperation lookupUser = LookupOperation.newLookup().from("m_usr").localField("salesPerson")
				.foreignField("code").as("userDetails");

		LookupOperation lookupCustomer = LookupOperation.newLookup().from("t_cstmr")
				.localField("$invoiceDetails.customerId").foreignField("code").as("customerDetails");

		GroupOperation group = Aggregation.group("$lcInvoiceNo").first("$lcNo").as("lcNo").first("$lcInvoiceNo")
				.as("lcInvoiceNo").first("$bankId").as("bankId").first("$validity").as("validity").first("$consignee")
				.as("consignee").first("$notifyParty").as("notifyParty").first("$cAddress").as("cAddress")
				.first("$npAddress").as("npAddress").first("$amount").as("amount").first("$proformaInvoiceId")
				.as("proformaInvoiceId").first("$billOfExchangeNo").as("billOfExchangeNo").first("$createdDate")
				.as("createdDate").first("$createdBy").as("salesPerson").first("$customerDetails.firstName")
				.as("customerName").first("$customerDetails.code").as("customerId");
		Aggregation aggregation = Aggregation.newAggregation(matchOperation, lookupInvoice,
				Aggregation.unwind("$invoiceDetails", true), lookupUser, Aggregation.unwind("$userDetails", true),
				lookupCustomer, Aggregation.unwind("$customerDetails", true), group);
		AggregationResults<Document> result = this.mongoTemplate.aggregate(aggregation, "t_lc_dtls", Document.class);

		return result.getMappedResults();

	}

	@Override
	public List<LcInvoiceDto> findAllBillOfExchangeUpdated() {
		MatchOperation match = Aggregation.match(Criteria.where("billOfExchangeStatus")
				.in(Constants.BILL_OF_EXCHANGE_PARTIALLY_CREATED, Constants.BILL_OF_EXCHANGE_CREATED));
		LookupOperation lookupInvoice = LookupOperation.newLookup().from("t_lc_invc").localField("lcInvoiceNo")
				.foreignField("lcInvoiceNo").as("invoice");
		MatchOperation matchInvoice = Aggregation
				.match(new Criteria().andOperator(Criteria.where("invoice.status").ne(Constants.LC_INVOICE_CANCELLED),
						Criteria.where("invoice.isBolUpdated").is(Constants.BILL_OF_EXCHANGE_UPDATED)));

		LookupOperation lookupBank = LookupOperation.newLookup().from("m_frgn_bnks").localField("bankId")
				.foreignField("bankId").as("bank");
		LookupOperation lookupProforma = LookupOperation.newLookup().from("t_prfrm_invc")
				.localField("invoice.proformaInvoiceId").foreignField("invoiceNo").as("proformaDetails");

		LookupOperation lookupInvoiceCustomer = LookupOperation.newLookup().from("t_cstmr")
				.localField("proformaDetails.customerId").foreignField("code").as("InvCustDetails");

		LookupOperation shippingLookup = LookupOperation.newLookup().from("t_shppng_rqust")
				.localField("invoice.stockNo").foreignField("stockNo").as("shippingStock");

		final AggregationOperation shippingFilter = (context) -> new Document("$addFields",
				new Document("shippingFilterResult",
						new Document("$filter",
								new Document("input", "$shippingStock").append("as", "result").append("cond",
										new Document("$and", Arrays.asList(
												// new Document("$ifNull", Arrays.asList("$$result", null)),
												new Document("$ne", Arrays.asList("$$result.scheduleId", null)),
												new Document("$ne", Arrays.asList("$$result.shippingStatus", 0))))))));

		GroupOperation group = Aggregation.group("$_id").push(new BasicDBObject("stockNo", "$invoice.stockNo")
				.append("chassisNo", "$invoice.chassisNo").append("invoiceId", "$invoice._id")
				.append("maker", "$invoice.maker").append("model", "$invoice.model").append("hsCode", "$invoice.hsCode")
				.append("fob", "$invoice.fob").append("insurance", "$invoice.insurance")
				.append("freight", "$invoice.freight").append("amount", "$invoice.amount")
				.append("schedule", "$invoice.schedule").append("to", "$invoice.to").append("from", "$invoice.from")
				.append("from", "$invoice.from").append("sailingDate", "$invoice.sailingDate")
				.append("bankSentDate", "$invoice.bankSentDate").append("shippingMarks", "$invoice.shippingMarks")
				.append("shippingMarksId", "$invoice.shippingMarksId").append("perVessel", "$invoice.perVessel")
				.append("customerId", "$InvCustDetails.code").append("customerName", "$InvCustDetails.firstName")
				.append("scheduleId", "$shippingFilterResult.scheduleId")
				.append("proConsigneeId", "$proformaDetails.consigneeId")
				.append("proNotifypartyId", "$proformaDetails.notifypartyId")
				.append("consigneeName", "$consigneeDetails.cFirstName")
				.append("notifypartyName", "$notifypartyDetails.npFirstName")
				.append("consigneeAddress", "$consigneeDetails.cAddress")
				.append("notifypartyAddress", "$notifypartyDetails.npAddress")
				.append("proformaInvoiceId", "$invoice.proformaInvoiceId")
				.append("proformaInvoiceNo", "$invoice.proformaInvoiceNo")
				.append("shipmentRequestId", "$shippingFilterResult.shipmentRequestId")).as("items")
				.first("lcInvoiceNo").as("lcInvoiceNo").first("lcNo").as("lcNo").first("issueDate").as("issueDate")
				.first("expiryDate").as("expiryDate").first("bank.bank").as("bank")//
				.first("proformaInvoiceId").as("proformaInvoiceId").first("billOfExchangeNo").as("billOfExchangeNo")
				.first("consigneeName").as("consignee").first("InvCustDetails.firstName").as("customerName")
				.first("cAddress").as("cAddress").first("npAddress").as("npAddress").first("notifyPartyName")
				.as("notifyParty").first("perVessel").as("perVessel").first("from").as("from").first("to").as("to")
				.first("sailingDate").as("sailingDate").first("bankSentDate").as("bankSentDate").first("shippingMarks")
				.as("shippingMarks").first("shippingTerms").as("shippingTerms").first("shippingTermsName")
				.as("shippingTermsName").first("beneficiaryCertify").as("beneficiaryCertify").first("licenseDoc")
				.as("licenseDoc").first("InvCustDetails.consigneeNotifyparties").as("consigneeNotifyparties")
				.first("consignee").as("consigneeId").first("notifyParty").as("notifypartyId").first("bankId")
				.as("bankId").first("customerId").as("customerId").first("amount").as("amount");
		ProjectionOperation project = Aggregation.project().andInclude("items", "lcInvoiceNo", "lcNo", "issueDate",
				"expiryDate", "bank", "proformaInvoiceId", "amount", "billOfExchangeNo", "consignee", "notifyParty",
				"customerName", "cAddress", "npAddress", "perVessel", "from", "to", "sailingDate", "bankSentDate",
				"shippingMarks", "shippingTerms", "shippingTermsName", "consigneeNotifyparties", "consigneeId",
				"notifypartyId", "beneficiaryCertify", "licenseDoc", "bankId", "customerId");
		Aggregation aggregation = Aggregation.newAggregation(match, lookupInvoice, Aggregation.unwind("$invoice", true),
				matchInvoice, lookupBank, Aggregation.unwind("$bank", true), lookupProforma,
				Aggregation.unwind("$proformaDetails", true), lookupInvoiceCustomer,
				Aggregation.unwind("$InvCustDetails", true), shippingLookup, shippingFilter,
				Aggregation.unwind("shippingFilterResult", true), group, project);
		AggregationResults<LcInvoiceDto> result = this.mongoTemplate.aggregate(aggregation, "t_lc_dtls",
				LcInvoiceDto.class);
		return result.getMappedResults();
	}

}
