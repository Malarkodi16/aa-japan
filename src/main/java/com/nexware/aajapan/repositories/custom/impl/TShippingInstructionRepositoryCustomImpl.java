package com.nexware.aajapan.repositories.custom.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationExpression;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;
import org.springframework.data.mongodb.core.aggregation.CountOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.mongodb.client.result.UpdateResult;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.TShippingInstructionDto;
import com.nexware.aajapan.dto.TShippingInstructionFromSalesDto;
import com.nexware.aajapan.models.TShippingInstruction;
import com.nexware.aajapan.repositories.custom.TShippingInstructionRepositoryCustom;
import com.nexware.aajapan.utils.AppUtil;

@Service
public class TShippingInstructionRepositoryCustomImpl implements TShippingInstructionRepositoryCustom {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<TShippingInstructionFromSalesDto> findAllShippingInstructionFromSales() {
		final ProjectionOperation project = Aggregation.project()
				.andInclude("id", "customerId", "consigneeId", "estimatedDeparture", "estimatedArrival", "remarks",
						"scheduleType", "createdDate", "status")
				.and("notifypartyId").as("notifyPartyId").and("destCountry").as("destinationCountry").and("yard")
				.as("yard").and("destPort").as("destinationPort").and("stock.stockNo").as("stockNo").and("stock.maker")
				.as("maker").and("stock.model").as("model").and("stock.m3").as("m3").and("stock.length").as("length")
				.and("stock.width").as("width").and("stock.height").as("height").and("stock.chassisNo").as("chassisNo")
				.and("customer.firstName").as("customerFN").and("customer.lastName").as("customerLN")
				.and("customer.lastName").as("customerLN").and("consignee.cFirstName").as("consigneeFN")
				.and("consignee.cLastName").as("consigneeLN").and("notifyParty.npFirstName").as("notifyPartyFN")
				.and("notifyParty.npLastName").as("notifyPartyLN").and("user.fullname").as("salesPersonName")
				.and("user.code").as("salesPersonId").and("stock.lastTransportLocation").as("lastTransportLocation")
				.and("stock.lastTransportLocationCustom").as("lastTransportLocationCustom").and("stock.shipmentType")
				.as("shipmentType").and("locationDetails.displayName").as("lastTransportLocationDisplayname")
				.and("locationDetails.shipmentOriginCountry").as("originCountry")
				.and("locationDetails.shipmentOriginPort").as("originPort").and("stock.maker").as("maker")
				.and("stock.model").as("model").and("stock.length").as("length").and("stock.width").as("width")
				.and("stock.height").as("height").and("stock.forwarder").as("forwarder").and("stock.firstRegDate")
				.as("firstRegDate").and("forwarderDetails.name").as("forwarderName").and("stock.purchaseInfo.date")
				.as("purchaseDate").and(ConditionalOperators.ifNull("exportCertificate.convertedDate")
						.thenValueOf("docRecDetails.documentConvertedDate"))
				.as("documentConvertedDate");

		final SortOperation sort = Aggregation.sort(Direction.ASC, "scheduleType");

		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");
		final LookupOperation lookupCustomer = LookupOperation.newLookup().from("t_cstmr").localField("customerId")
				.foreignField("code").as("customer");
		final LookupOperation lookupUser = LookupOperation.newLookup().from("m_usr").localField("salesPersonId")
				.foreignField("code").as("user");
		final LookupOperation locationDetails = LookupOperation.newLookup().from("m_lctn")
				.localField("stock.lastTransportLocation").foreignField("code").as("locationDetails");
		final LookupOperation lookupMForwarder = LookupOperation.newLookup().from("m_frwrdr")
				.localField("stock.forwarder").foreignField("code").as("forwarderDetails");
		final AggregationOperation addConsignee = context -> new Document("$addFields", new Document("consignee",
				new Document("$filter", new Document("input", "$customer.consigneeNotifyparties").append("as", "result")
						.append("cond", new Document("$eq", Arrays.asList("$$result._id", "$consigneeId"))))));
		final LookupOperation lookupDocReceived = LookupOperation.newLookup().from("t_doc_recvd").localField("stockNo")
				.foreignField("stockNo").as("docRecDetails");
		final LookupOperation lookupExportCertificate = LookupOperation.newLookup().from("t_exprt_crtfct")
				.localField("stockNo").foreignField("stockNo").as("exportCertificate");
		final AggregationOperation addNotifyParty = context -> new Document("$addFields",
				new Document("notifyParty",
						new Document("$filter",
								new Document("input", "$customer.consigneeNotifyparties").append("as", "result").append(
										"cond",
										new Document("$eq", Arrays.asList("$$result._id", "$notifypartyId"))))));
		final MatchOperation match = Aggregation
				.match(new Criteria().andOperator(Criteria.where("status").is(Constants.SHIPPING_INSTRUCTION_GIVEN),
						Criteria.where("deleteStatus").ne(Constants.DELETE_FLAG_1)));
		final Aggregation aggregation = Aggregation.newAggregation(match, lookupStock,
				Aggregation.unwind("$stock", true), lookupCustomer, Aggregation.unwind("$customer", true), lookupUser,
				Aggregation.unwind("$user", true), locationDetails, Aggregation.unwind("$locationDetails", true),
				addConsignee, addNotifyParty, Aggregation.unwind("$consignee", true),
				Aggregation.unwind("$notifyParty", true), lookupMForwarder,
				Aggregation.unwind("$forwarderDetails", true), lookupDocReceived,
				Aggregation.unwind("$docRecDetails", true), lookupExportCertificate,
				Aggregation.unwind("$exportCertificate", true), project, sort);
		final AggregationResults<TShippingInstructionFromSalesDto> result = mongoTemplate.aggregate(aggregation,
				"t_shppng_instructn", TShippingInstructionFromSalesDto.class);
		return result.getMappedResults();
	}

	@Override
	public Optional<List<TShippingInstructionDto>> findBySalesPersonShippingData(List<String> salesPersonIds) {
		final List<Criteria> andCriteria = new ArrayList<>();
		andCriteria.add(Criteria.where("status").is(Constants.SHIPPING_INSTRUCTION_GIVEN));
		andCriteria.add(Criteria.where("deleteStatus").ne(Constants.DELETE_FLAG_1));
		if (!AppUtil.isObjectEmpty(salesPersonIds)) {
			andCriteria.add(Criteria.where("salesPersonId").in(salesPersonIds));
		}
		final MatchOperation match = Aggregation
				.match(new Criteria().andOperator(andCriteria.toArray(new Criteria[0])));
		final LookupOperation lookupCustomer = LookupOperation.newLookup().from("t_cstmr").localField("customerId")
				.foreignField("code").as("customer");
		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");
		final LookupOperation lookupShippingRequest = LookupOperation.newLookup().from("t_shppng_rqust")
				.localField("stock.shipmentRequestId").foreignField("shipmentRequestId").as("shipmentRequestId");
		final LookupOperation lookupShipmentSchedule = LookupOperation.newLookup().from("t_shpmnt_schdl")
				.localField("shipmentRequestId.scheduleId").foreignField("scheduleId").as("schedule");
		final LookupOperation lookupShip = LookupOperation.newLookup().from("m_ship")
				.localField("shipmentRequestId.vesselId").foreignField("shipId").as("ship");
		final LookupOperation lookupLcInvoice = LookupOperation.newLookup().from("t_lc_invc").localField("stockNo")
				.foreignField("stockNo").as("lcInvoice");
		final LookupOperation lookupLcInvoiceDtls = LookupOperation.newLookup().from("t_lc_dtls")
				.localField("lcInvoice.lcDtlId").foreignField("_id").as("lcInvoiceDtls");
		final LookupOperation lookupLogin = LookupOperation.newLookup().from("m_lgn").localField("salesPersonId")
				.foreignField("userId").as("userDetails");

		final AggregationOperation addETDField = context -> new Document("$addFields", new Document("etd",
				new Document("$filter", new Document("input", "$schedule.schedule").append("as", "result").append(
						"cond",
						new Document("$eq", Arrays.asList("$$result.portName", "$shipmentRequestId.orginPort"))))));
		final AggregationOperation addETAField = context -> new Document("$addFields", new Document("eta",
				new Document("$filter", new Document("input", "$schedule.schedule").append("as", "result").append(
						"cond",
						new Document("$eq", Arrays.asList("$$result.portName", "$shipmentRequestId.destPort"))))));

		final AggregationOperation addConsignee = context -> new Document("$addFields", new Document("consignee",
				new Document("$filter", new Document("input", "$customer.consigneeNotifyparties").append("as", "result")
						.append("cond", new Document("$eq", Arrays.asList("$$result._id", "$consigneeId"))))));
		final AggregationOperation addNotify = context -> new Document("$addFields",
				new Document("notifyparty",
						new Document("$filter",
								new Document("input", "$customer.consigneeNotifyparties").append("as", "result").append(
										"cond",
										new Document("$eq", Arrays.asList("$$result._id", "$notifypartyId"))))));
		final ProjectionOperation project = Aggregation.project()
				.andInclude("stockNo", "date", "customerFN", "customerLN", "destCountry", "destPort", "status")
				.and("stock.chassisNo").as("chassisNo").and("customer.code").as("customerId").and("customer.firstName")
				.as("customerFN").and("customer.lastName").as("customerLN").and("consignee._id").as("consigneeId")
				.and("consignee.cFirstName").as("consigneeFN").and("consignee.cLastName").as("consigneeLN")
				.and("notifyparty._id").as("notifypartyId").and("notifyparty.npFirstName").as("notifyPartyFN")
				.and("notifyparty.npLastName").as("notifyPartyLN").and("shipmentRequestId.dhlNo").as("dhlNo")
				.and("ship.name").as("vesselId").and("etd.date").as("etd").and("eta.date").as("eta")
				.and("lcInvoiceDtls.lcNo").as("lcNo").and("stock.reservedInfo.date").as("reserveDate")
				.and("stock.shippingUser").as("shippingUser").and("stock.shippingId").as("shippingId")
				.and("stock.shippingTel").as("shippingTel").and("stock.hsCode").as("hsCode").and("userDetails.username")
				.as("instructedBy");

		SortOperation sort = Aggregation.sort(Direction.DESC, "date");

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupCustomer, lookupStock, lookupLogin,
				Aggregation.unwind("$stock", true), Aggregation.unwind("$customer", true), lookupShippingRequest,
				Aggregation.unwind("$shipmentRequestId", true), lookupShipmentSchedule,
				Aggregation.unwind("$schedule", true), lookupShip, Aggregation.unwind("ship", true), lookupLcInvoice,
				Aggregation.unwind("lcInvoice", true), lookupLcInvoiceDtls, Aggregation.unwind("lcInvoiceDtls", true),
				addConsignee, addNotify, addETDField, addETAField, Aggregation.unwind("$etd", true),
				Aggregation.unwind("$eta", true), project, sort);
		final AggregationResults<TShippingInstructionDto> result = mongoTemplate.aggregate(aggregation,
				"t_shppng_instructn", TShippingInstructionDto.class);

		return Optional.of(result.getMappedResults());
	}

	@Override
	public void updateStatusByShippingInstructionIds(Integer status, List<String> ids) {
		final Update update = new Update().set("status", status);
		mongoTemplate.updateMulti(Query.query(Criteria.where("shippingInstructionId").in(ids)), update,
				TShippingInstruction.class);

	}

	@Override
	public Optional<List<TShippingInstructionDto>> findBySalesPersonShippingStatus(List<String> salesPersonIds) {

		final List<Criteria> andCriteria = new ArrayList<>();
		andCriteria.add(Criteria.where("status").in(Constants.SHIPPING_INSTRUCTION_INITIATED,
				Constants.SHIPPING_INSTRUCTION_GIVEN));
		andCriteria.add(Criteria.where("deleteStatus").ne(Constants.DELETE_FLAG_1));
		if (!AppUtil.isObjectEmpty(salesPersonIds)) {
			andCriteria.add(Criteria.where("salesPersonId").in(salesPersonIds));
		}
		final MatchOperation match = Aggregation
				.match(new Criteria().andOperator(andCriteria.toArray(new Criteria[0])));

		final LookupOperation lookupCustomer = LookupOperation.newLookup().from("t_cstmr").localField("customerId")
				.foreignField("code").as("customer");
		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");
		final LookupOperation lookupShippingRequest = LookupOperation.newLookup().from("t_shppng_rqust")
				.localField("stock.shipmentRequestId").foreignField("shipmentRequestId").as("shipmentRequestId");
		final LookupOperation lookupShipmentSchedule = LookupOperation.newLookup().from("t_shpmnt_schdl")
				.localField("shipmentRequestId.scheduleId").foreignField("scheduleId").as("schedule");
		final LookupOperation lookupShip = LookupOperation.newLookup().from("m_ship").localField("schedule.shipId")
				.foreignField("shipId").as("ship");
		final LookupOperation lookupLcInvoice = LookupOperation.newLookup().from("t_lc_invc").localField("stockNo")
				.foreignField("stockNo").as("lcInvoice");
		final LookupOperation lookupLcInvoiceDtls = LookupOperation.newLookup().from("t_lc_dtls")
				.localField("lcInvoice.lcDtlId").foreignField("_id").as("lcInvoiceDtls");
		final LookupOperation lookupLogin = LookupOperation.newLookup().from("m_lgn").localField("salesPersonId")
				.foreignField("userId").as("userDetails");
		final LookupOperation lookupPurchaseInvoice = LookupOperation.newLookup().from("t_prchs_invc")
				.localField("stockNo").foreignField("stockNo").as("purchaseInvoiceDetails");

		final AggregationOperation addETDField = context -> new Document("$addFields", new Document("etd",
				new Document("$filter", new Document("input", "$schedule.schedule").append("as", "result").append(
						"cond",
						new Document("$eq", Arrays.asList("$$result.portName", "$shipmentRequestId.orginPort"))))));
		final AggregationOperation addETAField = context -> new Document("$addFields", new Document("eta",
				new Document("$filter", new Document("input", "$schedule.schedule").append("as", "result").append(
						"cond",
						new Document("$eq", Arrays.asList("$$result.portName", "$shipmentRequestId.destPort"))))));

		final AggregationExpression purchasePrice = contexts -> new Document("$sum",
				Arrays.asList("$purchaseInvoiceDetails.purchaseCost", "$purchaseInvoiceDetails.commision",
						"$purchaseInvoiceDetails.otherCharges", "$purchaseInvoiceDetails.purchaseCostTaxAmount",
						"$purchaseInvoiceDetails.commisionTaxAmount", "$purchaseInvoiceDetails.roadTax",
						"$purchaseInvoiceDetails.recycle"));

		final AggregationOperation addConsignee = context -> new Document("$addFields", new Document("consignee",
				new Document("$filter", new Document("input", "$customer.consigneeNotifyparties").append("as", "result")
						.append("cond", new Document("$eq", Arrays.asList("$$result._id", "$consigneeId"))))));
		final AggregationOperation addNotify = context -> new Document("$addFields",
				new Document("notifyparty",
						new Document("$filter",
								new Document("input", "$customer.consigneeNotifyparties").append("as", "result").append(
										"cond",
										new Document("$eq", Arrays.asList("$$result._id", "$notifypartyId"))))));
		final ProjectionOperation project = Aggregation.project()
				.andInclude("stockNo", "date", "customerFN", "customerLN", "destCountry", "destPort", "status",
						"remarks", "createdDate", "shippingInstructionId", "salesPersonId")
				.and("stock.chassisNo").as("chassisNo").and("customer.code").as("customerId").and("customer.firstName")
				.as("customerFN").and("consignee._id").as("consigneeId").and("consignee.cFirstName").as("consigneeFN")
				.and("consignee.cLastName").as("consigneeLN").and("notifyparty._id").as("notifypartyId")
				.and("notifyparty.npFirstName").as("notifyPartyFN").and("notifyparty.npLastName").as("notifyPartyLN")
				.and("shipmentRequestId.dhlNo").as("dhlNo").and("ship.name").as("vesselId").and("etd.date").as("etd")
				.and("eta.date").as("eta").and("lcInvoiceDtls.lcNo").as("lcNo").and("stock.reservedInfo.date")
				.as("reserveDate").and("stock.shippingUser").as("shippingUser").and("stock.shippingId").as("shippingId")
				.and("stock.shippingTel").as("shippingTel").and("stock.hsCode").as("hsCode").and("userDetails.username")
				.as("instructedBy").and("shippingInstructionId").as("shippingInstructionId").and("stock.inspectionFlag")
				.as("inspectionFlag").and("paymentType").as("paymentType").and("yard").as("yard").and("scheduleType")
				.as("scheduleType").and("estimatedArrival").as("estimatedArrival").and("estimatedDeparture")
				.as("estimatedDeparture").and("bookingDetails").as("bookingDetails").and(purchasePrice)
				.as("purchasePrice");

		final SortOperation sort = Aggregation.sort(Direction.DESC, "createdDate");

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupCustomer, lookupStock,
				Aggregation.unwind("$stock", true), Aggregation.unwind("$customer", true), lookupShippingRequest,
				Aggregation.unwind("$shipmentRequestId", true), lookupShipmentSchedule,
				Aggregation.unwind("$schedule", true), lookupShip, Aggregation.unwind("ship", true), lookupLcInvoice,
				Aggregation.unwind("lcInvoice", true), lookupLcInvoiceDtls, Aggregation.unwind("lcInvoiceDtls", true),
				addConsignee, addNotify, addETDField, addETAField, Aggregation.unwind("$etd", true),
				Aggregation.unwind("$eta", true), lookupLogin, lookupPurchaseInvoice,
				Aggregation.unwind("$purchaseInvoiceDetails", true), project, sort);
		final AggregationResults<TShippingInstructionDto> result = mongoTemplate.aggregate(aggregation,
				"t_shppng_instructn", TShippingInstructionDto.class);

		return Optional.of(result.getMappedResults());

	}

	@Override
	public Long findByShippingInstructionCount(List<String> salesPersonIds) {
		final List<Criteria> andCriteria = new ArrayList<>();
		andCriteria.add(Criteria.where("status").is(Constants.SHIPPING_INSTRUCTION_GIVEN));
		andCriteria.add(Criteria.where("deleteStatus").ne(Constants.DELETE_FLAG_1));
		if (!AppUtil.isObjectEmpty(salesPersonIds)) {
			andCriteria.add(Criteria.where("salesPersonId").in(salesPersonIds));
		}
		final MatchOperation match = Aggregation
				.match(new Criteria().andOperator(andCriteria.toArray(new Criteria[0])));
		final CountOperation count = Aggregation.count().as("count");
		final Aggregation aggregation = Aggregation.newAggregation(match, count);
		final AggregationResults<Document> result = mongoTemplate.aggregate(aggregation, "t_shppng_instructn",
				Document.class);
		return Long.valueOf(!AppUtil.isObjectEmpty(result.getUniqueMappedResult())
				? result.getUniqueMappedResult().getInteger("count")
				: 0);
	}

	@Override
	public Long findByShippingInstructionStatusCount(List<String> salesPersonIds) {
		final List<Criteria> andCriteria = new ArrayList<>();
		andCriteria.add(Criteria.where("status").in(Constants.SHIPPING_INSTRUCTION_INITIATED,
				Constants.SHIPPING_INSTRUCTION_GIVEN));
		andCriteria.add(Criteria.where("deleteStatus").ne(Constants.DELETE_FLAG_1));
		if (!AppUtil.isObjectEmpty(salesPersonIds)) {
			andCriteria.add(Criteria.where("salesPersonId").in(salesPersonIds));
		}
		final MatchOperation match = Aggregation
				.match(new Criteria().andOperator(andCriteria.toArray(new Criteria[0])));

		final CountOperation count = Aggregation.count().as("count");
		final Aggregation aggregation = Aggregation.newAggregation(match, count);
		final AggregationResults<Document> result = mongoTemplate.aggregate(aggregation, "t_shppng_instructn",
				Document.class);
		return Long.valueOf(!AppUtil.isObjectEmpty(result.getUniqueMappedResult())
				? result.getUniqueMappedResult().getInteger("count")
				: 0);
	}

	@Override
	public UpdateResult updateById(String id, Update update) {
		return mongoTemplate.updateMulti(Query.query(Criteria.where("id").is(id)), update, TShippingInstruction.class);
	}

	@Override
	public TShippingInstructionFromSalesDto findOneShippingInstructionFromSales(String id) {

		final ProjectionOperation project = Aggregation.project()
				.andInclude("id", "customerId", "consigneeId", "estimatedDeparture", "estimatedArrival", "remarks",
						"scheduleType", "createdDate", "status")
				.and("notifypartyId").as("notifyPartyId").and("destCountry").as("destinationCountry").and("yard")
				.as("yard").and("destPort").as("destinationPort").and("stock.stockNo").as("stockNo").and("stock.maker")
				.as("maker").and("stock.model").as("model").and("stock.m3").as("m3").and("stock.length").as("length")
				.and("stock.width").as("width").and("stock.height").as("height").and("stock.chassisNo").as("chassisNo")
				.and("customer.firstName").as("customerFN").and("customer.lastName").as("customerLN")
				.and("customer.lastName").as("customerLN").and("consignee.cFirstName").as("consigneeFN")
				.and("consignee.cLastName").as("consigneeLN").and("notifyParty.npFirstName").as("notifyPartyFN")
				.and("notifyParty.npLastName").as("notifyPartyLN").and("user.fullname").as("salesPersonName")
				.and("user.code").as("salesPersonId").and("stock.lastTransportLocation").as("lastTransportLocation")
				.and("stock.lastTransportLocationCustom").as("lastTransportLocationCustom").and("stock.shipmentType")
				.as("shipmentType").and("locationDetails.displayName").as("lastTransportLocationDisplayname")
				.and("locationDetails.shipmentOriginCountry").as("originCountry")
				.and("locationDetails.shipmentOriginPort").as("originPort").and("stock.maker").as("maker")
				.and("stock.model").as("model").and("stock.length").as("length").and("stock.width").as("width")
				.and("stock.height").as("height").and("stock.forwarder").as("forwarder").and("stock.firstRegDate")
				.as("firstRegDate").and("forwarderDetails.name").as("forwarderName").and("stock.purchaseInfo.date")
				.as("purchaseDate");

		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");
		final LookupOperation lookupCustomer = LookupOperation.newLookup().from("t_cstmr").localField("customerId")
				.foreignField("code").as("customer");
		final LookupOperation lookupUser = LookupOperation.newLookup().from("m_usr").localField("salesPersonId")
				.foreignField("code").as("user");
		final LookupOperation locationDetails = LookupOperation.newLookup().from("m_lctn")
				.localField("stock.lastTransportLocation").foreignField("code").as("locationDetails");
		final LookupOperation lookupMForwarder = LookupOperation.newLookup().from("m_frwrdr")
				.localField("stock.forwarder").foreignField("code").as("forwarderDetails");
		final AggregationOperation addConsignee = context -> new Document("$addFields", new Document("consignee",
				new Document("$filter", new Document("input", "$customer.consigneeNotifyparties").append("as", "result")
						.append("cond", new Document("$eq", Arrays.asList("$$result._id", "$consigneeId"))))));
		final AggregationOperation addNotifyParty = context -> new Document("$addFields",
				new Document("notifyParty",
						new Document("$filter",
								new Document("input", "$customer.consigneeNotifyparties").append("as", "result").append(
										"cond",
										new Document("$eq", Arrays.asList("$$result._id", "$notifypartyId"))))));
		final MatchOperation match = Aggregation.match(Criteria.where("status").is(Constants.SHIPPING_INSTRUCTION_GIVEN)
				.andOperator(Criteria.where("_id").is(id), Criteria.where("deleteStatus").ne(Constants.DELETE_FLAG_1)));
		final Aggregation aggregation = Aggregation.newAggregation(match, lookupStock,
				Aggregation.unwind("$stock", true), lookupCustomer, Aggregation.unwind("$customer", true), lookupUser,
				Aggregation.unwind("$user", true), locationDetails, Aggregation.unwind("$locationDetails", true),
				addConsignee, addNotifyParty, Aggregation.unwind("$consignee", true),
				Aggregation.unwind("$notifyParty", true), lookupMForwarder,
				Aggregation.unwind("$forwarderDetails", true), project);
		final AggregationResults<TShippingInstructionFromSalesDto> result = mongoTemplate.aggregate(aggregation,
				"t_shppng_instructn", TShippingInstructionFromSalesDto.class);
		return result.getUniqueMappedResult();
	}
}
