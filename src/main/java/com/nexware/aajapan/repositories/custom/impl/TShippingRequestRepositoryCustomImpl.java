package com.nexware.aajapan.repositories.custom.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AccumulatorOperators;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationExpression;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators.Cond;
import org.springframework.data.mongodb.core.aggregation.CountOperation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.BillOfLandingDto;
import com.nexware.aajapan.dto.DocumentStatusDto;
import com.nexware.aajapan.dto.FreightVesselDto;
import com.nexware.aajapan.dto.ShippingRequestedItemsDto;
import com.nexware.aajapan.dto.TShippingRequestContainerDto;
import com.nexware.aajapan.dto.TShippingRequestDto;
import com.nexware.aajapan.dto.TShippingRequestEtaEtd;
import com.nexware.aajapan.dto.TShippingRequestedContainerExcelDto;
import com.nexware.aajapan.dto.TShippingRequestedDto;
import com.nexware.aajapan.dto.TShippingRoroExcelDto;
import com.nexware.aajapan.models.TFreightShippingInvoice;
import com.nexware.aajapan.models.TShippingRequest;
import com.nexware.aajapan.repositories.custom.TShippingRequestRepositoryCustom;
import com.nexware.aajapan.utils.AppUtil;

@Service
public class TShippingRequestRepositoryCustomImpl implements TShippingRequestRepositoryCustom {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<TShippingRequestDto> findAllFreightItems() {
		final ProjectionOperation project = Aggregation.project()
				.andInclude("id", "customer", "stock", "invoiceId", "fright", "usd", "jpy", "shipping", "inspection",
						"radiation", "others", "blNo")
				.and("stock.chassisNo").as("chassisNo").and("customer.firstName").as("customerId");
		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");
		final LookupOperation lookupCustomer = LookupOperation.newLookup().from("t_cstmr").localField("customerId")
				.foreignField("code").as("customer");

		final AggregationResults<TShippingRequestDto> result = mongoTemplate
				.aggregate(
						Aggregation.newAggregation(lookupStock, lookupCustomer, Aggregation.unwind("$customer"),
								Aggregation.unwind("$stock"), project),
						TShippingRequest.class, TShippingRequestDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<FreightVesselDto> findBySearch(String forwarder, List<String> vessel, List<String> voyageNo) {
		final LookupOperation lookupCustomer = LookupOperation.newLookup().from("t_cstmr").localField("customerId")
				.foreignField("code").as("customer");
		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");
		final LookupOperation lookupShipmentSchedule = LookupOperation.newLookup().from("t_shpmnt_schdl")
				.localField("voyageNo").foreignField("voyageNo").as("schedule");
		final LookupOperation lookupShip = LookupOperation.newLookup().from("m_ship").localField("vesselId")
				.foreignField("shipId").as("ship");
		final LookupOperation lookupVehicleMaker = LookupOperation.newLookup().from("m_vchcl_mkr")
				.localField("$stock.maker").foreignField("name").as("vehicle");
		final LookupOperation lookupForwarder = LookupOperation.newLookup().from("m_frwrdr").localField("$forwarderId")
				.foreignField("code").as("forwarder");

		final AggregationOperation addETDField = context -> new Document("$addFields",
				new Document("etd",
						new Document("$filter",
								new Document("input", "$schedule.schedule").append("as", "result").append("cond",
										new Document("$eq", Arrays.asList("$$result.portName", "$orginPort"))))));
		final AggregationOperation addETAField = context -> new Document("$addFields",
				new Document("eta",
						new Document("$filter",
								new Document("input", "$schedule.schedule").append("as", "result").append("cond",
										new Document("$eq", Arrays.asList("$$result.portName", "$destPort"))))));
		final AggregationOperation addModel = context -> new Document("$addFields", new Document("model", new Document(
				"$filter",
				new Document("input", "$vehicle.models").append("as", "result").append("cond", new Document("$and",
						Arrays.asList(new Document("$eq", Arrays.asList("$$result.modelName", "$stock.model"))))))));

		final List<Criteria> andCriterias = new ArrayList<>();
		boolean isValid = false;
		andCriterias.add(Criteria.where("status").is(Constants.FREIGHT_NOT_UPDATED));
		if (!AppUtil.isObjectEmpty(forwarder)) {
			isValid = true;
			andCriterias.add(Criteria.where("forwarderId").is(forwarder));
		}

		if (!AppUtil.isObjectEmpty(vessel)) {
			isValid = true;
			andCriterias.add(Criteria.where("vesselId").in(vessel));
		}

		if (!AppUtil.isObjectEmpty(voyageNo)) {
			isValid = true;
			andCriterias.add(Criteria.where("voyageNo").in(voyageNo));
		}

		if (!isValid) {
			return new ArrayList<>();
		}

		final Criteria matchCriteria = new Criteria();
		matchCriteria.andOperator(andCriterias.toArray(new Criteria[0]));
		final MatchOperation match = Aggregation.match(matchCriteria);
		final GroupOperation groupOperation = Aggregation.group("$vesselId")
				.push(new BasicDBObject("stockNo", "$stock.stockNo").append("shipmentRequestId", "$shipmentRequestId")
						.append("chassisNo", "$stock.chassisNo").append("m3", "$model.m3")
						.append("length", "$model.length").append("width", "$model.width")
						.append("height", "$model.height").append("customerFirstName", "$customer.firstName")
						.append("customerLastName", "$customer.lastName").append("freightUSD", "$forwarder.perM3Usd")
						.append("freightCharge", "$freightCharge").append("shippingCharge", "$shippingCharge")
						.append("inspectionCharge", "$inspectionCharge").append("radiationCharge", "$radiationCharge")
						.append("orginPort", "$orginPort").append("destPort", "$destPort")
						.append("orginCountry", "$orginCountry").append("destCountry", "$destCountry")
						.append("etd", "$etd.date").append("eta", "$eta.date"))
				.as("items").first("ship.name").as("vessel");

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupCustomer, lookupShip,
				Aggregation.unwind("$ship", true), Aggregation.unwind("$customer", true), lookupStock,
				Aggregation.unwind("$stock", true), lookupVehicleMaker, Aggregation.unwind("$vehicle", true),
				lookupShipmentSchedule, Aggregation.unwind("$schedule", true), lookupForwarder,
				Aggregation.unwind("$forwarder", true), addETDField, addETAField, Aggregation.unwind("$etd", true),
				addModel, Aggregation.unwind("$model", true), Aggregation.unwind("$eta", true), groupOperation);
		final AggregationResults<FreightVesselDto> result = mongoTemplate.aggregate(aggregation, "t_shppng_rqust",
				FreightVesselDto.class);
		return result.getMappedResults();
	}

	@Override
	public void updateFreightExchange(String invoiceNo, Date invoiceDate, String exchangeRate,
			List<String> shipmentRequestId) {
		final Update update = new Update().set("invoiceNo", invoiceNo).set("invoiceDate", invoiceDate)
				.set("exchangeRate", exchangeRate);

		mongoTemplate.updateMulti(Query.query(Criteria.where("shipmentRequestId").in(shipmentRequestId)), update,
				TFreightShippingInvoice.class);
	}

	@Override
	public void updateFreightStatus(int status, List<String> shippingRequestId) {
		final Update update = new Update().set("status", status);
		mongoTemplate.updateMulti(Query.query(Criteria.where("shipmentRequestId").in(shippingRequestId)), update,
				TShippingRequest.class);
	}

	@Override
	public List<DocumentStatusDto> findAllBlCustomers() {
		final ProjectionOperation project = Aggregation.project()
				.andInclude("id", "shipmentRequestId", "stockNo", "customerId", "forwarderId", "orginCountry",
						"orginPort", "destCountry", "destPort", "voyageNo", "fName", "blDraftStatus")
				.and("customer.firstName").as("firstName").and("customer.lastName").as("lastName");
		final LookupOperation lookupCustomer = LookupOperation.newLookup().from("t_cstmr").localField("customerId")
				.foreignField("code").as("customer");
		final LookupOperation lookupShippingRequest = LookupOperation.newLookup().from("t_shppng_rqust")
				.localField("shipmentRequestId").foreignField("shipmentRequestId").as("shipmentRequest");
		final LookupOperation lookupShipmentSchedule = LookupOperation.newLookup().from("t_shpmnt_schdl")
				.localField("voyageNo").foreignField("voyageNo").as("shipping_schedule");
		final LookupOperation lookupShip = LookupOperation.newLookup().from("m_ship").localField("vesselId")
				.foreignField("shipId").as("ship");

		final Aggregation aggregation = Aggregation.newAggregation(lookupCustomer,
				Aggregation.unwind("$customer", true), lookupShip, Aggregation.unwind("$ship", true),
				lookupShippingRequest, Aggregation.unwind("$shipmentRequest", true), lookupShipmentSchedule,
				Aggregation.unwind("$shipping_schedule", true), project);
		final AggregationResults<DocumentStatusDto> result = mongoTemplate.aggregate(aggregation, "t_shppng_rqust",
				DocumentStatusDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<DocumentStatusDto> findAllLesserEtd() {

		final ProjectionOperation project = Aggregation.project()
				.andInclude("id", "shipmentRequestId", "stockNo", "customerId", "forwarderId", "orginCountry",
						"orginPort", "destCountry", "destPort", "voyageNo", "fName", "blDraftStatus")
				.and("customer.firstName").as("firstName").and("customer.lastName").as("lastName").and("ship.name")
				.as("vesselId").and("etd.date").as("etd").and("eta.date").as("eta");

		final LookupOperation lookupCustomer = LookupOperation.newLookup().from("t_cstmr").localField("customerId")
				.foreignField("code").as("customer");
		final LookupOperation lookupShippingRequest = LookupOperation.newLookup().from("t_shppng_rqust")
				.localField("shipmentRequestId").foreignField("shipmentRequestId").as("shipmentRequest");
		final LookupOperation lookupShipmentSchedule = LookupOperation.newLookup().from("t_shpmnt_schdl")
				.localField("scheduleId").foreignField("scheduleId").as("shipping_schedule");
		final LookupOperation lookupShip = LookupOperation.newLookup().from("m_ship").localField("vesselId")
				.foreignField("shipId").as("ship");

		final AggregationOperation addETDField = context -> new Document("$addFields", new Document("etd", new Document(
				"$filter", new Document("input", "$shipping_schedule.schedule").append("as", "result").append("cond",
						new Document("$eq", Arrays.asList("$$result.portName", "$shipmentRequest.orginPort"))))));

		final AggregationOperation addETAField = context -> new Document("$addFields", new Document("eta", new Document(
				"$filter", new Document("input", "$shipping_schedule.schedule").append("as", "result").append("cond",
						new Document("$eq", Arrays.asList("$$result.portName", "$shipmentRequest.destPort"))))));
		final Date startDate = new Date();

		final MatchOperation match = Aggregation.match(Criteria.where("etd").lt(startDate));
		// .andOperator(Criteria.where("eta").gt(startDate))
		final Aggregation aggregation = Aggregation.newAggregation(lookupCustomer,
				Aggregation.unwind("$customer", true), lookupShip, Aggregation.unwind("$ship", true),
				lookupShippingRequest, Aggregation.unwind("$shipmentRequest", true), lookupShipmentSchedule,
				Aggregation.unwind("$shipping_schedule", true), addETDField, Aggregation.unwind("$etd", true),
				addETAField, Aggregation.unwind("$eta", true), project, match);
		final AggregationResults<DocumentStatusDto> result = mongoTemplate.aggregate(aggregation, "t_shppng_rqust",
				DocumentStatusDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<DocumentStatusDto> findAllLesserEta() {
		final ProjectionOperation project = Aggregation.project()
				.andInclude("id", "shipmentRequestId", "stockNo", "customerId", "forwarderId", "orginCountry",
						"orginPort", "destCountry", "destPort", "voyageNo", "fName", "blOriginalStatus")
				.and("customer.firstName").as("firstName").and("customer.lastName").as("lastName").and("ship.name")
				.as("vesselId").and("etd.date").as("etd").and("eta.date").as("eta");
		final LookupOperation lookupCustomer = LookupOperation.newLookup().from("t_cstmr").localField("customerId")
				.foreignField("code").as("customer");
		final LookupOperation lookupShippingRequest = LookupOperation.newLookup().from("t_shppng_rqust")
				.localField("shipmentRequestId").foreignField("shipmentRequestId").as("shipmentRequest");
		final LookupOperation lookupShipmentSchedule = LookupOperation.newLookup().from("t_shpmnt_schdl")
				.localField("scheduleId").foreignField("scheduleId").as("shipping_schedule");
		final LookupOperation lookupShip = LookupOperation.newLookup().from("m_ship").localField("vesselId")
				.foreignField("shipId").as("ship");

		final AggregationOperation addETDField = context -> new Document("$addFields", new Document("etd", new Document(
				"$filter", new Document("input", "$shipping_schedule.schedule").append("as", "result").append("cond",
						new Document("$eq", Arrays.asList("$$result.portName", "$shipmentRequest.orginPort"))))));

		final AggregationOperation addETAField = context -> new Document("$addFields", new Document("eta", new Document(
				"$filter", new Document("input", "$shipping_schedule.schedule").append("as", "result").append("cond",
						new Document("$eq", Arrays.asList("$$result.portName", "$shipmentRequest.destPort"))))));
		final Date startDate = new Date();

		final MatchOperation match = Aggregation.match(Criteria.where("eta").lt(startDate));

		final Aggregation aggregation = Aggregation.newAggregation(lookupCustomer,
				Aggregation.unwind("$customer", true), lookupShip, Aggregation.unwind("$ship", true),
				lookupShippingRequest, Aggregation.unwind("$shipmentRequest", true), lookupShipmentSchedule,
				Aggregation.unwind("$shipping_schedule", true), addETDField, Aggregation.unwind("$etd", true),
				addETAField, Aggregation.unwind("$eta", true), project, match);
		final AggregationResults<DocumentStatusDto> result = mongoTemplate.aggregate(aggregation, "t_shppng_rqust",
				DocumentStatusDto.class);
		return result.getMappedResults();
	}

	@Override
	public void updateDraftStatus(int status, List<String> shippingRequestId) {
		final Update update = new Update().set("blDraftStatus", status);
		mongoTemplate.updateMulti(Query.query(Criteria.where("id").in(shippingRequestId)), update,
				TShippingRequest.class);
	}

	@Override
	public void updateOriginalStatus(int status, List<String> shippingRequestId) {
		final Update update = new Update().set("blOriginalStatus", status);
		mongoTemplate.updateMulti(Query.query(Criteria.where("id").in(shippingRequestId)), update,
				TShippingRequest.class);
	}

	@Override
	public List<TShippingRequestedDto> findAllShippingRequestedStock(Integer status) {
		final MatchOperation match = Aggregation.match(Criteria.where("status").is(status)
				.andOperator(Criteria.where("shippingType").is(Constants.STOCK_SHIPPING_TYPE_RORO)));
		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");
		final LookupOperation lookupFwd = LookupOperation.newLookup().from("m_frwrdr").localField("forwarderId")
				.foreignField("code").as("forwarderDetails");
		final LookupOperation lookupSchedule = LookupOperation.newLookup().from("t_shpmnt_schdl")
				.localField("scheduleId").foreignField("scheduleId").as("shipping_schedule");
		final LookupOperation lookupVessal = LookupOperation.newLookup().from("m_ship")
				.localField("shipping_schedule.shipId").foreignField("shipId").as("ship_dtls");
		final LookupOperation lookupMLocation = LookupOperation.newLookup().from("m_lctn")
				.localField("stock.lastTransportLocation").foreignField("code").as("location_dtls");
		LookupOperation lookupDocReceived = LookupOperation.newLookup().from("t_doc_recvd").localField("stockNo")
				.foreignField("stockNo").as("docRecDetails");
		LookupOperation lookupExportCertificate = LookupOperation.newLookup().from("t_exprt_crtfct")
				.localField("stockNo").foreignField("stockNo").as("exportCertificate");
		final LookupOperation lookupInspectionOrder = Aggregation.lookup("t_inspctn_odr_rqst", "stockNo", "stockNo",
				"inspectionDetails");
		final AggregationOperation inspectionRequestDetails = context -> new Document("$addFields",
				new Document("inspectionRequestDetails",
						new Document("$filter",
								new Document("input", "$inspectionDetails").append("as", "result").append("cond",
										new Document("$and", Arrays.asList(new Document("$eq",
												Arrays.asList("$$result.country", "$stock.destinationCountry"))))))));

		final AggregationOperation addRecentInspectionRequestDetails = (context) -> new Document("$addFields",
				new Document("inspectionDetails",
						new Document("$arrayElemAt",
								Arrays.asList("$inspectionRequestDetails",
										new Document("$indexOfArray", Arrays.asList(
												"$inspectionRequestDetails.createdDate",
												new Document("$max", "inspectionRequestDetails.createdDate")))))));

		final AggregationOperation addETD = (context) -> new Document("$addFields",
				new Document("etd", new Document("$filter",
						new Document("input", "$shipping_schedule.schedule").append("as", "result").append("cond",
								new Document("$and", Arrays.asList(
										new Document("$eq", Arrays.asList("$$result.portName", "$orginPort")),
										new Document("$eq", Arrays.asList("$$result.portFlag", "loading"))))))));

		final AggregationOperation addETA = context -> new Document("$addFields",
				new Document("eta",
						new Document("$filter",
								new Document("input", "$shipping_schedule.schedule").append("as", "result").append(
										"cond",
										new Document("$eq", Arrays.asList("$$result.portName", "$destPort"))))));

		Document convertedDate = new Document("$cond",
				Arrays.<Object>asList(
						new Document("$eq", Arrays.<Object>asList("$exportCertificate.convertedDate", null)),
						"$docRecDetails.documentConvertedDate", "$exportCertificate.convertedDate"));

		final GroupOperation groupOperation = Aggregation.group("$scheduleId", "$destCountry")
				.push(new BasicDBObject("stockNo", "$stock.stockNo").append("chassisNo", "$stock.chassisNo")
						.append("purchaseDate", "$stock.purchaseInfo.date")
						.append("shipmentRequestId", "$shipmentRequestId").append("shippingType", "$shippingType")
						.append("forwarderId", "$forwarderId").append("forwarder", "$forwarder.name")
						.append("vesselId", "$vesselId").append("scheduleId", "$scheduleId")
						.append("customerId", "$customerId").append("consigneeId", "$consigneeId")
						.append("notifypartyId", "$notifypartyId").append("orginCountry", "$orginCountry")
						.append("orginPort", "$orginPort").append("destCountry", "$destCountry")
						.append("destPort", "$destPort").append("yard", "$yard").append("etd", "$etd.date")
						.append("eta", "$eta.date").append("status", "$status")
						.append("voyageNo", "$shipping_schedule.voyageNo").append("shipId", "$ship_dtls.shipId")
						.append("vessel", "$ship_dtls.name")
						.append("shippingCompanyName", "$ship_dtls.shippingCompanyName")
						.append("shippingCompanyNo", "$ship_dtls.shippingCompanyNo").append("blNo", "$blNo")
						.append("allocationId", "$allocationId").append("shippingType", "$shippingType")
						.append("scheduleId", "$scheduleId").append("remarks", "$remarks")
						.append("locationName", "$location_dtls.displayName")
						.append("inspectionDate", "$inspectionDetails.inspectionDate")
						.append("inspectionStatus", "$inspectionDetails.status")
						.append("inspectionDateOfIssue", "$inspectionDetails.dateOfIssue")
						.append("locationId", "$location_dtls.code").append("documentConvertedDate", convertedDate))
				.as("items").first("forwarderId").as("forwarderId").first("status").as("status").first("$destCountry")
				.as("destCountry").first("$destPort").as("destPort").first("$createdDate").as("allocationDate")
				.first("$ship_dtls.name").as("vessel").first("$orginPort").as("orginPort")
				.first("$shipping_schedule.voyageNo").as("voyageNo").first("$allocationId").as("allocationId")
				.first("$scheduleId").as("scheduleId");
		final AggregationExpression itemCount = context -> new Document("$size", "$items");
		final ProjectionOperation projectionOperation = Aggregation
				.project("forwarderId", "status", "items", "destCountry", "destPort", "allocationDate", "vessel",
						"orginPort", "voyageNo", "allocationId", "scheduleId")
				.and("$forwarderDetails.name").as("forwarderName").and(itemCount).as("itemCount");
		final Aggregation aggregation = Aggregation.newAggregation(match, lookupSchedule,
				Aggregation.unwind("$shipping_schedule", true), lookupVessal, Aggregation.unwind("$ship_dtls", true),
				lookupStock, Aggregation.unwind("$stock", true), lookupMLocation,
				Aggregation.unwind("$location_dtls", true), addETD, Aggregation.unwind("$etd", true), addETA,
				Aggregation.unwind("$eta", true), lookupDocReceived, Aggregation.unwind("$docRecDetails", true),
				lookupExportCertificate, Aggregation.unwind("$exportCertificate", true), lookupInspectionOrder,
				inspectionRequestDetails, addRecentInspectionRequestDetails, groupOperation, lookupFwd,
				Aggregation.unwind("$forwarderDetails", true), projectionOperation);
		final AggregationResults<TShippingRequestedDto> result = mongoTemplate.aggregate(aggregation, "t_shppng_rqust",
				TShippingRequestedDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<TShippingRequestedDto> findAllShippingArrangedRequestedStock(Integer status) {
		final MatchOperation match = Aggregation.match(Criteria.where("status").is(status)
				.andOperator(Criteria.where("shippingType").is(Constants.STOCK_SHIPPING_TYPE_RORO)));
		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");
		final LookupOperation lookupFwd = LookupOperation.newLookup().from("m_frwrdr").localField("forwarderId")
				.foreignField("code").as("forwarderDetails");
		final LookupOperation lookupSchedule = LookupOperation.newLookup().from("t_shpmnt_schdl")
				.localField("scheduleId").foreignField("scheduleId").as("shipping_schedule");
		final LookupOperation lookupVessal = LookupOperation.newLookup().from("m_ship")
				.localField("shipping_schedule.shipId").foreignField("shipId").as("ship_dtls");
		final LookupOperation lookupMLocation = LookupOperation.newLookup().from("m_lctn")
				.localField("stock.lastTransportLocation").foreignField("code").as("location_dtls");
		LookupOperation lookupDocReceived = LookupOperation.newLookup().from("t_doc_recvd").localField("stockNo")
				.foreignField("stockNo").as("docRecDetails");
		LookupOperation lookupExportCertificate = LookupOperation.newLookup().from("t_exprt_crtfct")
				.localField("stockNo").foreignField("stockNo").as("exportCertificate");
		final LookupOperation lookupInspectionOrder = Aggregation.lookup("t_inspctn_odr_rqst", "stockNo", "stockNo",
				"inspectionDetails");
		final AggregationOperation inspectionRequestDetails = context -> new Document("$addFields",
				new Document("inspectionRequestDetails",
						new Document("$filter",
								new Document("input", "$inspectionDetails").append("as", "result").append("cond",
										new Document("$and", Arrays.asList(new Document("$eq",
												Arrays.asList("$$result.country", "$stock.destinationCountry"))))))));

		final AggregationOperation addRecentInspectionRequestDetails = (context) -> new Document("$addFields",
				new Document("inspectionDetails",
						new Document("$arrayElemAt",
								Arrays.asList("$inspectionRequestDetails",
										new Document("$indexOfArray", Arrays.asList(
												"$inspectionRequestDetails.createdDate",
												new Document("$max", "inspectionRequestDetails.createdDate")))))));
		final AggregationOperation addETD = (context) -> new Document("$addFields",
				new Document("etd", new Document("$filter",
						new Document("input", "$shipping_schedule.schedule").append("as", "result").append("cond",
								new Document("$and", Arrays.asList(
										new Document("$eq",
												Arrays.asList("$$result.portName",
														"$location_dtls.shipmentOriginPort")),
										new Document("$eq", Arrays.asList("$$result.portFlag", "loading"))))))));

		final AggregationOperation addETA = context -> new Document("$addFields",
				new Document("eta",
						new Document("$filter",
								new Document("input", "$shipping_schedule.schedule").append("as", "result").append(
										"cond",
										new Document("$eq", Arrays.asList("$$result.portName", "$destPort"))))));

		Document convertedDate = new Document("$cond",
				Arrays.<Object>asList(
						new Document("$eq", Arrays.<Object>asList("$exportCertificate.convertedDate", null)),
						"$docRecDetails.documentConvertedDate", "$exportCertificate.convertedDate"));

		final GroupOperation groupOperation = Aggregation.group("$forwarderId", "$destCountry", "$allocationId")
				.push(new BasicDBObject("stockNo", "$stock.stockNo").append("chassisNo", "$stock.chassisNo")
						.append("purchaseDate", "$stock.purchaseInfo.date")
						.append("shipmentRequestId", "$shipmentRequestId").append("shippingType", "$shippingType")
						.append("forwarderId", "$forwarderId").append("forwarder", "$forwarder.name")
						.append("vesselId", "$vesselId").append("scheduleId", "$scheduleId")
						.append("customerId", "$customerId").append("consigneeId", "$consigneeId")
						.append("notifypartyId", "$notifypartyId").append("orginCountry", "$orginCountry")
						.append("orginPort", "$orginPort").append("destCountry", "$destCountry")
						.append("destPort", "$destPort").append("yard", "$yard").append("etd", "$etd.date")
						.append("eta", "$eta.date").append("status", "$status")
						.append("voyageNo", "$shipping_schedule.voyageNo").append("shipId", "$ship_dtls.shipId")
						.append("vessel", "$ship_dtls.name")
						.append("shippingCompanyName", "$ship_dtls.shippingCompanyName")
						.append("shippingCompanyNo", "$ship_dtls.shippingCompanyNo").append("blNo", "$blNo")
						.append("allocationId", "$allocationId").append("shippingType", "$shippingType")
						.append("scheduleId", "$scheduleId").append("remarks", "$remarks")
						.append("locationName", "$location_dtls.displayName")
						.append("inspectionDate", "$inspectionDetails.inspectionDate")
						.append("inspectionStatus", "$inspectionDetails.status")
						.append("inspectionDateOfIssue", "$inspectionDetails.dateOfIssue")
						.append("locationId", "$location_dtls.code").append("documentConvertedDate", convertedDate))
				.as("items").first("forwarderId").as("forwarderId").first("status").as("status").first("$destCountry")
				.as("destCountry").first("$destPort").as("destPort").first("$createdDate").as("allocationDate")
				.first("$ship_dtls.name").as("vessel").first("$orginPort").as("orginPort").first("$allocationId")
				.as("allocationId").first("$shipping_schedule.voyageNo").as("voyageNo");
		final AggregationExpression itemCount = context -> new Document("$size", "$items");
		final ProjectionOperation projectionOperation = Aggregation
				.project("forwarderId", "status", "items", "destCountry", "destPort", "allocationDate", "vessel",
						"orginPort", "allocationId", "voyageNo")
				.and("$forwarderDetails.name").as("forwarderName").and(itemCount).as("itemCount");
		final SortOperation sortContainer = Aggregation.sort(Direction.DESC, "allocationDate");
		final Aggregation aggregation = Aggregation.newAggregation(match, lookupSchedule,
				Aggregation.unwind("$shipping_schedule", true), lookupVessal, Aggregation.unwind("$ship_dtls", true),
				lookupStock, Aggregation.unwind("$stock", true), lookupMLocation,
				Aggregation.unwind("$location_dtls", true), addETD, Aggregation.unwind("$etd", true), addETA,
				Aggregation.unwind("$eta", true), lookupDocReceived, Aggregation.unwind("$docRecDetails", true),
				lookupExportCertificate, Aggregation.unwind("$exportCertificate", true), lookupInspectionOrder,
				inspectionRequestDetails, addRecentInspectionRequestDetails, groupOperation, lookupFwd,
				Aggregation.unwind("$forwarderDetails", true), projectionOperation, sortContainer);
		final AggregationResults<TShippingRequestedDto> result = mongoTemplate.aggregate(aggregation, "t_shppng_rqust",
				TShippingRequestedDto.class);
		return result.getMappedResults();
	}

	@Override
	public Long getCountByShipmentType(Integer type) {
		final MatchOperation match = Aggregation
				.match(new Criteria().andOperator(Criteria.where("status").is(Constants.SHIPPING_INSTRUCTION_GIVEN),
						Criteria.where("deleteStatus").ne(Constants.DELETE_FLAG_1)));
		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");

		final MatchOperation matchStock = Aggregation.match(Criteria.where("stock.shipmentType").is(type));
		final CountOperation count = Aggregation.count().as("count");
		final Aggregation aggregation = Aggregation.newAggregation(match, lookupStock,
				Aggregation.unwind("$stock", true), matchStock, count);
		final AggregationResults<Map> result = mongoTemplate.aggregate(aggregation, "t_shppng_instructn", Map.class);
		return Long.valueOf(!AppUtil.isObjectEmpty(result.getUniqueMappedResult())
				? result.getUniqueMappedResult().get("count").toString()
				: "0");
	}

	@Override
	public TShippingRequestedDto findShippingRequestedStockByScheduleId(String allocationId, Integer status) {
		final MatchOperation match = Aggregation.match(Criteria.where("status").is(status).andOperator(
				Criteria.where("shippingType").is(Constants.STOCK_SHIPPING_TYPE_RORO),
				Criteria.where("allocationId").is(allocationId)));
		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");
		final LookupOperation lookupFwd = LookupOperation.newLookup().from("m_frwrdr").localField("forwarderId")
				.foreignField("code").as("forwarderDetails");
		final LookupOperation lookupSchedule = LookupOperation.newLookup().from("t_shpmnt_schdl")
				.localField("scheduleId").foreignField("scheduleId").as("shipping_schedule");
		final LookupOperation lookupVessal = LookupOperation.newLookup().from("m_ship")
				.localField("shipping_schedule.shipId").foreignField("shipId").as("ship_dtls");

		final AggregationOperation addETD = context -> new Document("$addFields",
				new Document("etd",
						new Document("$filter",
								new Document("input", "$shipping_schedule.schedule").append("as", "result").append(
										"cond",
										new Document("$eq", Arrays.asList("$$result.portName", "$orginPort"))))));

		final AggregationOperation addETA = context -> new Document("$addFields",
				new Document("eta",
						new Document("$filter",
								new Document("input", "$shipping_schedule.schedule").append("as", "result").append(
										"cond",
										new Document("$eq", Arrays.asList("$$result.portName", "$destPort"))))));

		final GroupOperation groupOperation = Aggregation.group("$allocationId")
				.push(new BasicDBObject("stockNo", "$stock.stockNo").append("chassisNo", "$stock.chassisNo")
						.append("shipmentRequestId", "$shipmentRequestId").append("shippingType", "$shippingType")
						.append("forwarderId", "$forwarderId").append("forwarder", "$forwarder.name")
						.append("vesselId", "$vesselId").append("scheduleId", "$scheduleId")
						.append("customerId", "$customerId").append("consigneeId", "$consigneeId")
						.append("notifypartyId", "$notifypartyId").append("orginCountry", "$orginCountry")
						.append("orginPort", "$orginPort").append("destCountry", "$destCountry")
						.append("destPort", "$destPort").append("yard", "$yard").append("etd", "$etd.date")
						.append("eta", "$eta.date").append("status", "$status")
						.append("voyageNo", "$shipping_schedule.voyageNo").append("shipId", "$ship_dtls.shipId")
						.append("vessel", "$ship_dtls.name")
						.append("shippingCompanyName", "$ship_dtls.shippingCompanyName")
						.append("shippingCompanyNo", "$ship_dtls.shippingCompanyNo").append("blNo", "$blNo")
						.append("allocationId", "$allocationId").append("shippingType", "$shippingType")
						.append("scheduleId", "$scheduleId"))
				.as("items").first("forwarderId").as("forwarderId").first("status").as("status").first("$destCountry")
				.as("destCountry").first("$destPort").as("destPort").first("$createdDate").as("allocationDate");
		final AggregationExpression itemCount = context -> new Document("$size", "$items");
		final ProjectionOperation projectionOperation = Aggregation
				.project("forwarderId", "status", "items", "destCountry", "destPort", "allocationDate")
				.and("$forwarderDetails.name").as("forwarderName").and(itemCount).as("itemCount").and("$_id")
				.as("allocationId");
		final Aggregation aggregation = Aggregation.newAggregation(match, lookupSchedule,
				Aggregation.unwind("$shipping_schedule", true), lookupVessal, Aggregation.unwind("$ship_dtls", true),
				lookupStock, Aggregation.unwind("$stock", true), addETD, Aggregation.unwind("$etd", true), addETA,
				Aggregation.unwind("$eta", true), groupOperation, lookupFwd,
				Aggregation.unwind("$forwarderDetails", true), projectionOperation);
		final AggregationResults<TShippingRequestedDto> result = mongoTemplate.aggregate(aggregation, "t_shppng_rqust",
				TShippingRequestedDto.class);

		return result.getUniqueMappedResult();
	}

	@Override
	public Optional<List<TShippingRequestedDto>> findAllShippingRequestedStatusStock(String forwarderFilter) {
		final MatchOperation match = Aggregation.match(Criteria.where("forwarderId").is(forwarderFilter));
		final LookupOperation lookupVessal = LookupOperation.newLookup().from("m_ship").localField("vesselId")
				.foreignField("shipId").as("ship_dtls");
		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");
		final LookupOperation lookupFwd = LookupOperation.newLookup().from("m_frwrdr").localField("forwarderId")
				.foreignField("code").as("forwarder");
		final LookupOperation lookupSchedule = LookupOperation.newLookup().from("t_shpmnt_schdl").localField("voyageNo")
				.foreignField("voyageNo").as("shipping_schedule");
		final AggregationOperation addETD = context -> new Document("$addFields",
				new Document("etd",
						new Document("$filter",
								new Document("input", "$shipping_schedule.schedule").append("as", "result").append(
										"cond",
										new Document("$eq", Arrays.asList("$$result.portName", "$orginPort"))))));

		final AggregationOperation addETA = context -> new Document("$addFields",
				new Document("eta",
						new Document("$filter",
								new Document("input", "$shipping_schedule.schedule").append("as", "result").append(
										"cond",
										new Document("$eq", Arrays.asList("$$result.portName", "$destPort"))))));

		final GroupOperation groupOperation = Aggregation.group("$vesselId")
				.push(new BasicDBObject("stockNo", "$stock.stockNo").append("chassisNo", "$stock.chassisNo")
						.append("shipmentRequestId", "$shipmentRequestId").append("shippingType", "$shippingType")
						.append("forwarder", "$forwarder.name").append("forwarder", "$forwarder.name")
						.append("orginCountry", "$orginCountry").append("orginPort", "$orginPort")
						.append("destCountry", "$destCountry").append("destPort", "$destPort")
						.append("etd", "$etd.date").append("eta", "$eta.date").append("status", "$status"))
				.as("items").first("voyageNo").as("voyageNo").first("ship_dtls.shipId").as("shipId")
				.first("ship_dtls.name").as("vessel").first("ship_dtls.shippingCompanyName").as("shippingCompanyName");

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupVessal,
				Aggregation.unwind("$ship_dtls", true), lookupStock, Aggregation.unwind("$stock", true), lookupFwd,
				Aggregation.unwind("$forwarder", true), lookupSchedule, Aggregation.unwind("$shipping_schedule", true),
				addETD, Aggregation.unwind("$etd", true), addETA, Aggregation.unwind("$eta", true), groupOperation);
		final AggregationResults<TShippingRequestedDto> result = mongoTemplate.aggregate(aggregation, "t_shppng_rqust",
				TShippingRequestedDto.class);
		return Optional.of(result.getMappedResults());
	}

	@Override
	public void updateFieldsByShippingRequestId(Map<String, Object> data, String id) {
		final Update update = new Update();
		data.forEach((key, value) -> update.set(key, value));
		mongoTemplate.updateMulti(Query.query(Criteria.where("shipmentRequestId").in(id)), update,
				TShippingRequest.class);
	}

	@Override
	public Optional<List<TShippingRequestedDto>> findAllShippingRequestedStatusStock(String forwarderFilter,
			String countryFilter, String portFilter, String shipmentTypeFilter) {
		final List<Criteria> andCriterias = new ArrayList<>();
		boolean isValid = false;
		andCriterias.add(Criteria.where("status").nin(Constants.SHIPIING_REQUEST_RESCHEDULED,
				Constants.SHIPIING_REQUEST_CANCELLED, Constants.SHIPIING_REQUEST_INITIATED));
		if (!AppUtil.isObjectEmpty(forwarderFilter)) {
			isValid = true;
			andCriterias.add(Criteria.where("forwarderId").is(forwarderFilter));
		}
		if (!AppUtil.isObjectEmpty(countryFilter)) {
			isValid = true;
			andCriterias.add(Criteria.where("destCountry").is(countryFilter));
		}

		if (!AppUtil.isObjectEmpty(portFilter)) {
			isValid = true;
			andCriterias.add(Criteria.where("destPort").in(portFilter));
		}

		if (!AppUtil.isObjectEmpty(shipmentTypeFilter)) {
			isValid = true;
			andCriterias.add(Criteria.where("shippingType").in(shipmentTypeFilter));
		}

		if (!isValid) {
			return Optional.of(new ArrayList<>());
		}

		final Criteria matchCriteria = new Criteria();
		matchCriteria.andOperator(andCriterias.toArray(new Criteria[0]));
		final MatchOperation match = Aggregation.match(matchCriteria);

		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");
		final LookupOperation lookupFwd = LookupOperation.newLookup().from("m_frwrdr").localField("forwarderId")
				.foreignField("code").as("forwarder");
		final LookupOperation lookupSchedule = LookupOperation.newLookup().from("t_shpmnt_schdl")
				.localField("scheduleId").foreignField("scheduleId").as("shipping_schedule");
		final LookupOperation lookupVessal = LookupOperation.newLookup().from("m_ship")
				.localField("shipping_schedule.shipId").foreignField("shipId").as("ship_dtls");
		final AggregationOperation addETD = context -> new Document("$addFields",
				new Document("etd",
						new Document("$filter",
								new Document("input", "$shipping_schedule.schedule").append("as", "result").append(
										"cond",
										new Document("$eq", Arrays.asList("$$result.portName", "$orginPort"))))));

		final AggregationOperation addETA = context -> new Document("$addFields",
				new Document("eta",
						new Document("$filter",
								new Document("input", "$shipping_schedule.schedule").append("as", "result").append(
										"cond",
										new Document("$eq", Arrays.asList("$$result.portName", "$destPort"))))));

		final GroupOperation groupOperation = Aggregation.group("$scheduleId")
				.push(new BasicDBObject("stockNo", "$stock.stockNo").append("chassisNo", "$stock.chassisNo")
						.append("shipmentRequestId", "$shipmentRequestId").append("shippingType", "$shippingType")
						.append("forwarder", "$forwarder.name").append("forwarder", "$forwarder.name")
						.append("orginCountry", "$orginCountry").append("orginPort", "$orginPort")
						.append("destCountry", "$destCountry").append("destPort", "$destPort")
						.append("etd", "$etd.date").append("eta", "$eta.date").append("status", "$status"))
				.as("items").first("$shipping_schedule.voyageNo").as("voyageNo").first("ship_dtls.shipId").as("shipId")
				.first("ship_dtls.name").as("vessel").first("ship_dtls.shippingCompanyName").as("shippingCompanyName")
				.first("ship_dtls.shippingCompanyNo").as("shippingCompanyNo");

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupStock,
				Aggregation.unwind("$stock", true), lookupFwd, Aggregation.unwind("$forwarder", true), lookupSchedule,
				Aggregation.unwind("$shipping_schedule", true), lookupVessal, Aggregation.unwind("$ship_dtls", true),
				addETD, Aggregation.unwind("$etd", true), addETA, Aggregation.unwind("$eta", true), groupOperation);
		final AggregationResults<TShippingRequestedDto> result = mongoTemplate.aggregate(aggregation, "t_shppng_rqust",
				TShippingRequestedDto.class);
		return Optional.of(result.getMappedResults());
	}

	@Override
	public List<TShippingRequestedDto> findAllShippingRequestedStockByStatus(Integer status) {
		final MatchOperation match = Aggregation
				.match(Criteria.where("status").nin(Constants.SHIPIING_REQUEST_RESCHEDULED,
						Constants.SHIPIING_REQUEST_CANCELLED, Constants.SHIPIING_REQUEST_INITIATED));

		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");
		final LookupOperation lookupFwd = LookupOperation.newLookup().from("m_frwrdr").localField("forwarderId")
				.foreignField("code").as("forwarder");
		final LookupOperation lookupSchedule = LookupOperation.newLookup().from("t_shpmnt_schdl")
				.localField("scheduleId").foreignField("scheduleId").as("shipping_schedule");
		final LookupOperation lookupVessal = LookupOperation.newLookup().from("m_ship")
				.localField("shipping_schedule.shipId").foreignField("shipId").as("ship_dtls");
		final AggregationOperation addETD = context -> new Document("$addFields",
				new Document("etd",
						new Document("$filter",
								new Document("input", "$shipping_schedule.schedule").append("as", "result").append(
										"cond",
										new Document("$eq", Arrays.asList("$$result.portName", "$orginPort"))))));

		final AggregationOperation addETA = context -> new Document("$addFields",
				new Document("eta",
						new Document("$filter",
								new Document("input", "$shipping_schedule.schedule").append("as", "result").append(
										"cond",
										new Document("$eq", Arrays.asList("$$result.portName", "$destPort"))))));

		final GroupOperation groupOperation = Aggregation.group("$scheduleId")
				.push(new BasicDBObject("stockNo", "$stock.stockNo").append("chassisNo", "$stock.chassisNo")
						.append("shipmentRequestId", "$shipmentRequestId").append("shippingType", "$shippingType")
						.append("forwarder", "$forwarder.name").append("forwarder", "$forwarder.name")
						.append("orginCountry", "$orginCountry").append("orginPort", "$orginPort")
						.append("destCountry", "$destCountry").append("destPort", "$destPort")
						.append("etd", "$etd.date").append("eta", "$eta.date").append("status", "$status"))
				.as("items").first("$shipping_schedule.voyageNo").as("voyageNo").first("ship_dtls.shipId").as("shipId")
				.first("ship_dtls.name").as("vessel").first("ship_dtls.shippingCompanyName").as("shippingCompanyName")
				.first("ship_dtls.shippingCompanyNo").as("shippingCompanyNo");

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupStock,
				Aggregation.unwind("$stock", true), lookupFwd, Aggregation.unwind("$forwarder", true), lookupSchedule,
				Aggregation.unwind("$shipping_schedule", true), lookupVessal, Aggregation.unwind("$ship_dtls", true),
				addETD, Aggregation.unwind("$etd", true), addETA, Aggregation.unwind("$eta", true), groupOperation);
		final AggregationResults<TShippingRequestedDto> result = mongoTemplate.aggregate(aggregation, "t_shppng_rqust",
				TShippingRequestedDto.class);
		return result.getMappedResults();
	}

	@Override
	public Long findAllShippingAcceptedStockCount() {

		final List<Criteria> andCriteria = new ArrayList<>();
		andCriteria.add(Criteria.where("status").in(Constants.SHIPPING_INSTRUCTION_INITIATED,
				Constants.SHIPPING_INSTRUCTION_GIVEN));
		andCriteria.add(Criteria.where("deleteStatus").ne(Constants.DELETE_FLAG_1));

		final MatchOperation match = Aggregation
				.match(new Criteria().andOperator(andCriteria.toArray(new Criteria[0])));

		final CountOperation countOperation = Aggregation.count().as("count");
		final Aggregation aggregation = Aggregation.newAggregation(match, countOperation);
		final AggregationResults<Map> result = mongoTemplate.aggregate(aggregation, "t_shppng_instructn", Map.class);

		return Long.valueOf(!AppUtil.isObjectEmpty(result.getUniqueMappedResult())
				? result.getUniqueMappedResult().get("count").toString()
				: "0");
	}

	@Override
	public List<FreightVesselDto> findAllContainerShippingRequestBySearch(Optional<String> forwarder,
			String[] scheduleId) {
		final LookupOperation lookupCustomer = LookupOperation.newLookup().from("t_cstmr").localField("customerId")
				.foreignField("code").as("customer");
		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");
		final LookupOperation lookupShipmentSchedule = LookupOperation.newLookup().from("t_shpmnt_schdl")
				.localField("scheduleId").foreignField("scheduleId").as("schedule");
		final LookupOperation lookupFreightShippingInvoice = LookupOperation.newLookup().from("t_frght_shpng_invc")
				.localField("shipmentRequestId").foreignField("shipmentRequestId").as("freightShipping");

		final AggregationOperation freightShippingFilter = context -> new Document("$addFields", new Document(
				"freightShippingData",
				new Document("$filter", new Document("input", "$freightShipping").append("as", "result").append("cond",
						new Document("$eq", Arrays.asList("$$result.shipmentRequestId", "$shipmentRequestId"))))));

		final AggregationOperation addETDField = context -> new Document("$addFields",
				new Document("etd",
						new Document("$filter",
								new Document("input", "$schedule.schedule").append("as", "result").append("cond",
										new Document("$eq", Arrays.asList("$$result.portName", "$orginPort"))))));
		final AggregationOperation addETAField = context -> new Document("$addFields",
				new Document("eta",
						new Document("$filter",
								new Document("input", "$schedule.schedule").append("as", "result").append("cond",
										new Document("$eq", Arrays.asList("$$result.portName", "$destPort"))))));

		final List<Criteria> andCriterias = new ArrayList<>();
		boolean isValid = false;
		// andCriterias.add(Criteria.where("status").is(Constants.FREIGHT_NOT_UPDATED))
		andCriterias.add(Criteria.where("shippingType").is(Constants.STOCK_SHIPPING_TYPE_RORO));
		andCriterias.add(Criteria.where("invoiceStatus").is(Constants.SHIPIING_REQUEST_INVOICE_NOT_CREATED));
		andCriterias.add(Criteria.where("status").ne(0));
		if (forwarder.isPresent()) {
			isValid = true;
			andCriterias.add(Criteria.where("forwarderId").is(forwarder.get()));
		}

		if (!AppUtil.isObjectEmpty(scheduleId)) {
			isValid = true;
			andCriterias.add(Criteria.where("scheduleId").in(scheduleId));
		}

		if (!isValid) {
			return new ArrayList<>();
		}

		final Criteria matchCriteria = new Criteria();
		matchCriteria.andOperator(andCriterias.toArray(new Criteria[0]));
		final MatchOperation match = Aggregation.match(matchCriteria);
		final GroupOperation groupOperation = Aggregation.group("$scheduleId")
				.push(new BasicDBObject("stockNo", "$stock.stockNo").append("shipmentRequestId", "$shipmentRequestId")
						.append("chassisNo", "$stock.chassisNo").append("m3", "$stock.m3")
						.append("length", "$stock.length").append("width", "$stock.width")
						.append("height", "$stock.height").append("customerFirstName", "$customer.firstName")
						.append("customerLastName", "$customer.lastName").append("freightUSD", "$freightUSD")
						.append("freightCharge", "$freightShipping.freightCharge")
						.append("shippingCharge", "$freightShipping.shippingCharge")
						.append("inspectionCharge", "$freightShipping.inspectionCharge")
						.append("radiationCharge", "$freightShipping.radiationCharge")
						.append("status", "$freightShipping.status").append("orginPort", "$orginPort")
						.append("destPort", "$destPort").append("orginCountry", "$orginCountry")
						.append("destCountry", "$destCountry").append("etd", "$etd.date").append("eta", "$eta.date")
						.append("forwarderId", "$forwarderId"))
				.as("items").first("schedule.shipId").as("shipId").first("schedule.shippingCompanyNo")
				.as("shippingCompanyNo").first("schedule.voyageNo").as("voyageNo");

		final LookupOperation lookupShip = LookupOperation.newLookup().from("m_ship").localField("shipId")
				.foreignField("shipId").as("ship_details");
		final LookupOperation lookupShippingCompany = LookupOperation.newLookup().from("m_shppng_cmpny")
				.localField("shippingCompanyNo").foreignField("shippingCompanyNo").as("shippingCompany");

		final ProjectionOperation project = Aggregation.project().andInclude("items", "voyageNo")
				.and("ship_details.shipId").as("shipId").and("ship_details.name").as("shipName")
				.and("shippingCompanyNo").as("shippingCompanyNo").and("shippingCompany.name").as("shippingCompanyName");

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupCustomer,
				Aggregation.unwind("$customer", true), lookupStock, Aggregation.unwind("$stock", true),
				lookupShipmentSchedule, Aggregation.unwind("$schedule", true), lookupFreightShippingInvoice,
				freightShippingFilter, addETDField, addETAField, Aggregation.unwind("$etd", true),
				Aggregation.unwind("$eta", true), groupOperation, lookupShip, Aggregation.unwind("$ship_details", true),
				lookupShippingCompany, Aggregation.unwind("$shippingCompany", true), project);
		final AggregationResults<FreightVesselDto> result = mongoTemplate.aggregate(aggregation, "t_shppng_rqust",
				FreightVesselDto.class);
		return result.getMappedResults();
	}

	@Override
	public TShippingRequestedDto findByShipId(String shipId) {
		final MatchOperation match = Aggregation
				.match(Criteria.where("status").is(Constants.SHIPIING_REQUEST_INITIATED));
		final LookupOperation lookupVessal = LookupOperation.newLookup().from("m_ship").localField("vesselId")
				.foreignField("shipId").as("ship_dtls");
		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");
		final LookupOperation lookupFwd = LookupOperation.newLookup().from("m_frwrdr").localField("forwarderId")
				.foreignField("code").as("forwarder");
		final LookupOperation lookupSchedule = LookupOperation.newLookup().from("t_shpmnt_schdl").localField("voyageNo")
				.foreignField("voyageNo").as("shipping_schedule");
		final AggregationOperation addETD = context -> new Document("$addFields",
				new Document("etd",
						new Document("$filter",
								new Document("input", "$shipping_schedule.schedule").append("as", "result").append(
										"cond",
										new Document("$eq", Arrays.asList("$$result.portName", "$orginPort"))))));

		final AggregationOperation addETA = context -> new Document("$addFields",
				new Document("eta",
						new Document("$filter",
								new Document("input", "$shipping_schedule.schedule").append("as", "result").append(
										"cond",
										new Document("$eq", Arrays.asList("$$result.portName", "$destPort"))))));

		final GroupOperation groupOperation = Aggregation.group("$vesselId")
				.push(new BasicDBObject("stockNo", "$stock.stockNo").append("chassisNo", "$stock.chassisNo")
						.append("shipmentRequestId", "$shipmentRequestId").append("shippingType", "$shippingType")
						.append("forwarder", "$forwarder.name").append("forwarder", "$forwarder.name")
						.append("orginCountry", "$orginCountry").append("orginPort", "$orginPort")
						.append("destCountry", "$destCountry").append("destPort", "$destPort")
						.append("etd", "$etd.date").append("eta", "$eta.date").append("status", "$status"))
				.as("items").first("voyageNo").as("voyageNo").first("ship_dtls.shipId").as("shipId")
				.first("ship_dtls.name").as("vessel").first("ship_dtls.shippingCompanyName").as("shippingCompanyName")
				.first("ship_dtls.shippingCompanyNo").as("shippingCompanyNo");

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupVessal,
				Aggregation.unwind("$ship_dtls", true), lookupStock, Aggregation.unwind("$stock", true), lookupFwd,
				Aggregation.unwind("$forwarder", true), lookupSchedule, Aggregation.unwind("$shipping_schedule", true),
				addETD, Aggregation.unwind("$etd", true), addETA, Aggregation.unwind("$eta", true), groupOperation);
		final AggregationResults<TShippingRequestedDto> result = mongoTemplate.aggregate(aggregation, "t_shppng_rqust",
				TShippingRequestedDto.class);
		return result.getUniqueMappedResult();
	}

	@Override
	public Long findDraftBlCount() {

		final ProjectionOperation project = Aggregation.project()
				.andInclude("id", "shipmentRequestId", "stockNo", "customerId", "forwarderId", "orginCountry",
						"orginPort", "destCountry", "destPort", "voyageNo", "fName", "blDraftStatus")
				.and("customer.firstName").as("firstName").and("customer.lastName").as("lastName").and("ship.name")
				.as("vesselId").and("etd.date").as("etd").and("eta.date").as("eta");

		final LookupOperation lookupCustomer = LookupOperation.newLookup().from("t_cstmr").localField("customerId")
				.foreignField("code").as("customer");
		final LookupOperation lookupShippingRequest = LookupOperation.newLookup().from("t_shppng_rqust")
				.localField("shipmentRequestId").foreignField("shipmentRequestId").as("shipmentRequest");
		final LookupOperation lookupShipmentSchedule = LookupOperation.newLookup().from("t_shpmnt_schdl")
				.localField("scheduleId").foreignField("scheduleId").as("shipping_schedule");
		final LookupOperation lookupShip = LookupOperation.newLookup().from("m_ship").localField("vesselId")
				.foreignField("shipId").as("ship");

		final AggregationOperation addETDField = context -> new Document("$addFields", new Document("etd", new Document(
				"$filter", new Document("input", "$shipping_schedule.schedule").append("as", "result").append("cond",
						new Document("$eq", Arrays.asList("$$result.portName", "$shipmentRequest.orginPort"))))));

		final AggregationOperation addETAField = context -> new Document("$addFields", new Document("eta", new Document(
				"$filter", new Document("input", "$shipping_schedule.schedule").append("as", "result").append("cond",
						new Document("$eq", Arrays.asList("$$result.portName", "$shipmentRequest.destPort"))))));
		final Date startDate = new Date();

		final MatchOperation match = Aggregation.match(Criteria.where("etd").lt(startDate));
		// .andOperator(Criteria.where("eta").gt(startDate))
		final CountOperation count = Aggregation.count().as("count");
		final Aggregation aggregation = Aggregation.newAggregation(lookupCustomer,
				Aggregation.unwind("$customer", true), lookupShip, Aggregation.unwind("$ship", true),
				lookupShippingRequest, Aggregation.unwind("$shipmentRequest", true), lookupShipmentSchedule,
				Aggregation.unwind("$shipping_schedule", true), addETDField, Aggregation.unwind("$etd", true),
				addETAField, Aggregation.unwind("$eta", true), project, match, count);
		final AggregationResults<Map> result = mongoTemplate.aggregate(aggregation, "t_shppng_rqust", Map.class);
		return Long.valueOf(!AppUtil.isObjectEmpty(result.getUniqueMappedResult())
				? result.getUniqueMappedResult().get("count").toString()
				: "0");
	}

	@Override
	public Long findOriginalBlCount() {
		final ProjectionOperation project = Aggregation.project()
				.andInclude("id", "shipmentRequestId", "stockNo", "customerId", "forwarderId", "orginCountry",
						"orginPort", "destCountry", "destPort", "voyageNo", "fName", "blDraftStatus")
				.and("customer.firstName").as("firstName").and("customer.lastName").as("lastName").and("ship.name")
				.as("vesselId").and("etd.date").as("etd").and("eta.date").as("eta");

		final LookupOperation lookupCustomer = LookupOperation.newLookup().from("t_cstmr").localField("customerId")
				.foreignField("code").as("customer");
		final LookupOperation lookupShippingRequest = LookupOperation.newLookup().from("t_shppng_rqust")
				.localField("shipmentRequestId").foreignField("shipmentRequestId").as("shipmentRequest");
		final LookupOperation lookupShipmentSchedule = LookupOperation.newLookup().from("t_shpmnt_schdl")
				.localField("scheduleId").foreignField("scheduleId").as("shipping_schedule");
		final LookupOperation lookupShip = LookupOperation.newLookup().from("m_ship").localField("vesselId")
				.foreignField("shipId").as("ship");

		final AggregationOperation addETDField = context -> new Document("$addFields", new Document("etd", new Document(
				"$filter", new Document("input", "$shipping_schedule.schedule").append("as", "result").append("cond",
						new Document("$eq", Arrays.asList("$$result.portName", "$shipmentRequest.orginPort"))))));

		final AggregationOperation addETAField = context -> new Document("$addFields", new Document("eta", new Document(
				"$filter", new Document("input", "$shipping_schedule.schedule").append("as", "result").append("cond",
						new Document("$eq", Arrays.asList("$$result.portName", "$shipmentRequest.destPort"))))));
		final Date startDate = new Date();

		final MatchOperation match = Aggregation.match(Criteria.where("eta").lt(startDate));
		// .andOperator(Criteria.where("eta").gt(startDate))
		final CountOperation count = Aggregation.count().as("count");
		final Aggregation aggregation = Aggregation.newAggregation(lookupCustomer,
				Aggregation.unwind("$customer", true), lookupShip, Aggregation.unwind("$ship", true),
				lookupShippingRequest, Aggregation.unwind("$shipmentRequest", true), lookupShipmentSchedule,
				Aggregation.unwind("$shipping_schedule", true), addETDField, Aggregation.unwind("$etd", true),
				addETAField, Aggregation.unwind("$eta", true), project, match, count);
		final AggregationResults<Map> result = mongoTemplate.aggregate(aggregation, "t_shppng_rqust", Map.class);
		return Long.valueOf(!AppUtil.isObjectEmpty(result.getUniqueMappedResult())
				? result.getUniqueMappedResult().get("count").toString()
				: "0");
	}

	@Override
	public List<TShippingRequestContainerDto> findAllShippingContainerStock(Integer status) {
		final MatchOperation match = Aggregation.match(Criteria.where("status").is(status)
				.andOperator(Criteria.where("shippingType").is(Constants.STOCK_SHIPPING_TYPE_CONTAINER)));
		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");
		final LookupOperation lookupFwd = LookupOperation.newLookup().from("m_frwrdr").localField("forwarderId")
				.foreignField("code").as("forwarder");
		final LookupOperation lookupSchedule = LookupOperation.newLookup().from("t_shpmnt_schdl")
				.localField("scheduleId").foreignField("scheduleId").as("shipping_schedule");
		final LookupOperation lookupVessal = LookupOperation.newLookup().from("m_ship")
				.localField("shipping_schedule.shipId").foreignField("shipId").as("ship_dtls");
		final LookupOperation lookupDocReceived = LookupOperation.newLookup().from("t_doc_recvd").localField("stockNo")
				.foreignField("stockNo").as("docRecDetails");
		final LookupOperation lookupExportCertificate = LookupOperation.newLookup().from("t_exprt_crtfct")
				.localField("stockNo").foreignField("stockNo").as("exportCertificate");

		final AggregationOperation addETD = context -> new Document("$addFields",
				new Document("etd",
						new Document("$filter",
								new Document("input", "$shipping_schedule.schedule").append("as", "result").append(
										"cond",
										new Document("$eq", Arrays.asList("$$result.portName", "$orginPort"))))));

		final AggregationOperation addETA = context -> new Document("$addFields",
				new Document("eta",
						new Document("$filter",
								new Document("input", "$shipping_schedule.schedule").append("as", "result").append(
										"cond",
										new Document("$eq", Arrays.asList("$$result.portName", "$destPort"))))));

		Document convertedDate = new Document("$cond",
				Arrays.<Object>asList(
						new Document("$eq", Arrays.<Object>asList("$exportCertificate.convertedDate", null)),
						"$docRecDetails.documentConvertedDate", "$exportCertificate.convertedDate"));

		final AggregationOperation groupOperation = context -> new Document("$group",
				new Document("_id", new Document("allocationId", "$allocationId").append("containerNo", "$containerNo"))
						.append("items", new Document("$push",
								new Document("stockNo", "$stock.stockNo").append("chassisNo", "$stock.chassisNo")
										.append("purchaseDate", "$stock.purchaseInfo.date")
										.append("maker", "$stock.maker").append("model", "$stock.model")
										.append("shipmentRequestId", "$shipmentRequestId").append("remarks", "$remarks")
										.append("shippingType", "$shippingType").append("forwarderId", "$forwarderId")
										.append("forwarder", "$forwarder.name").append("vesselId", "$vesselId")
										.append("scheduleId", "$scheduleId").append("customerId", "$customerId")
										.append("consigneeId", "$consigneeId").append("notifypartyId", "$notifypartyId")
										.append("orginCountry", "$orginCountry").append("orginPort", "$orginPort")
										.append("destCountry", "$destCountry").append("destPort", "$destPort")
										.append("yard", "$yard").append("etd", "$etd.date").append("eta", "$eta.date")
										.append("status", "$status").append("containerNo", "$containerNo")
										.append("documentConvertedDate", convertedDate)))
						.append("scheduleId", new Document("$first", "$scheduleId"))
						.append("voyageNo", new Document("$first", "$shipping_schedule.voyageNo"))
						.append("shipId", new Document("$first", "$ship_dtls.shipId"))
						.append("vessel", new Document("$first", "$ship_dtls.name"))
						.append("containerNo", new Document("$first", "$containerNo"))
						.append("allocationId", new Document("$first", "$allocationId"))
						.append("orginCountry", new Document("$first", "$orginCountry"))
						.append("orginPort", new Document("$first", "$orginPort"))
						.append("destCountry", new Document("$first", "$destCountry"))
						.append("destPort", new Document("$first", "$destPort"))
						.append("status", new Document("$first", "$status"))
						.append("shippingStatus", new Document("$first", "$shippingStatus"))
						.append("containerName", new Document("$first", "$containerName"))
						.append("createdDate", new Document("$first", "$createdDate"))
						.append("forwarder", new Document("$first", "$forwarder"))
						.append("blNo", new Document("$first", "$blNo"))
						.append("createdDate", new Document("$first", "$createdDate"))
						.append("shippingCompanyName", new Document("$first", "$ship_dtls.shippingCompanyName")));
		final AggregationOperation groupOperation2 = context -> new Document("$group",
				new Document("_id", new Document("allocationId", "$allocationId").append("scheduleId", "$scheduleId"))
						.append("items", new Document("$push", "$$ROOT"))
						.append("stockCount", new Document("$sum", new Document("$size", "$$ROOT.items")))
						.append("scheduleId", new Document("$first", "$scheduleId"))
						.append("voyageNo", new Document("$first", "$voyageNo"))
						.append("shipId", new Document("$first", "$shipId"))
						.append("vessel", new Document("$first", "$vessel"))
						.append("shippingCompanyName", new Document("$first", "$shippingCompanyName"))
						.append("allocationId", new Document("$first", "$allocationId"))
						.append("orginCountry", new Document("$first", "$orginCountry"))
						.append("orginPort", new Document("$first", "$orginPort"))
						.append("destCountry", new Document("$first", "$destCountry"))
						.append("destPort", new Document("$first", "$destPort"))
						.append("createdDate", new Document("$first", "$createdDate"))
						.append("status", new Document("$first", "$status"))
						.append("shippingStatus", new Document("$first", "$shippingStatus"))
						.append("containerName", new Document("$first", "$containerName"))
						.append("forwarder", new Document("$first", "$forwarder.code"))
						.append("forwarderName", new Document("$first", "$forwarder.name"))
						.append("createdDate", new Document("$first", "$createdDate")));
		final SortOperation sortContainer = Aggregation.sort(Direction.ASC, "items.containerNo");
		final SortOperation sortDate = Aggregation.sort(Direction.DESC, "createdDate");
		final Aggregation aggregation = Aggregation.newAggregation(match, lookupSchedule,
				Aggregation.unwind("$shipping_schedule", true), lookupVessal, Aggregation.unwind("$ship_dtls", true),
				lookupStock, Aggregation.unwind("$stock", true), lookupFwd, Aggregation.unwind("$forwarder", true),
				addETD, Aggregation.unwind("$etd", true), addETA, Aggregation.unwind("$eta", true), lookupDocReceived,
				Aggregation.unwind("$docRecDetails", true), lookupExportCertificate,
				Aggregation.unwind("$exportCertificate", true), groupOperation, sortContainer, groupOperation2,
				sortDate);
		final AggregationResults<TShippingRequestContainerDto> result = mongoTemplate.aggregate(aggregation,
				"t_shppng_rqust", TShippingRequestContainerDto.class);
		return result.getMappedResults();
	}

	@Override
	public TShippingRequestEtaEtd findEtaEtdByShipmentRequestId(String shipmentRequestId) {
		final MatchOperation match = Aggregation.match(Criteria.where("shipmentRequestId").is(shipmentRequestId));
		final LookupOperation lookupSchedule = LookupOperation.newLookup().from("t_shpmnt_schdl")
				.localField("scheduleId").foreignField("scheduleId").as("schedule_details");
		final LookupOperation stockDetails = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stockDetails");
		final LookupOperation mLocation = LookupOperation.newLookup().from("m_lctn")
				.localField("stockDetails.lastTransportLocation").foreignField("code").as("mLocation");

		final AggregationOperation addOrgin = (context) -> new Document("$addFields",
				new Document("orgin",
						new Document("$filter",
								new Document("input", "$schedule_details.schedule").append("as", "result")
										.append("cond", new Document("$and", Arrays.asList(
												// new Document("$ifNull", Arrays.asList("$$result", null)),
												new Document("$eq",
														Arrays.asList("$$result.portName",
																"$mLocation.shipmentOriginPort")),
												new Document("$eq",
														Arrays.asList("$$result.portFlag", "loading"))))))));

		final AggregationOperation addDestination = context -> new Document("$addFields",
				new Document("destination", new Document("$filter",
						new Document("input", "$schedule_details.schedule").append("as", "result").append("cond",
								new Document("$and", Arrays.asList(
										new Document("$eq", Arrays.asList("$$result.portName", "$destPort")),
										new Document("$eq", Arrays.asList("$$result.portFlag", "destination"))))))));

		final AggregationOperation matchValid = context -> new Document("$match",
				new Document("$expr", new Document("$lt", Arrays.asList("$orgin.route", "$destination.route"))));
		final ProjectionOperation project = Aggregation.project("shipmentRequestId").and("orgin.date").as("etd")
				.and("destination.date").as("eta");
		final Aggregation aggregation = Aggregation.newAggregation(match, lookupSchedule,
				Aggregation.unwind("$schedule_details", true), stockDetails, Aggregation.unwind("$stockDetails", true),
				mLocation, Aggregation.unwind("$mLocation", true), addOrgin, addDestination, matchValid, project);
		final AggregationResults<TShippingRequestEtaEtd> result = mongoTemplate.aggregate(aggregation, "t_shppng_rqust",
				TShippingRequestEtaEtd.class);
		return result.getUniqueMappedResult();
	}

	@Override
	public List<ShippingRequestedItemsDto> findAllContainerShippingRequestedItems(List<Criteria> criterias) {
		final MatchOperation match = Aggregation.match(new Criteria().andOperator(criterias.toArray(new Criteria[0])));
		final LookupOperation lookupStock = Aggregation.lookup("t_stck", "stockNo", "stockNo", "stockDetails");
		final LookupOperation lookupShippingInstruction = Aggregation.lookup("t_shppng_instructn",
				"shippingInstructionId", "shippingInstructionId", "shippingInstructionDetails");
		final LookupOperation lookupUser = Aggregation.lookup("m_usr", "shippingInstructionDetails.salesPersonId",
				"code", "userDetails");
		final ProjectionOperation projectionOperation = Aggregation.project("shipmentRequestId").and("destCountry")
				.as("destinationCountry").and("containerNo").as("contName").and("stockDetails.stockNo").as("stockNo")
				.and("stockDetails.chassisNo").as("chassisNo").and("stockDetails.maker").as("maker")
				.and("stockDetails.model").as("model").and("stockDetails.model").as("model").and("stockDetails.m3")
				.as("m3").and("userDetails.fullname").as("salesPerson");
		final Aggregation aggregation = Aggregation.newAggregation(match, lookupShippingInstruction,
				Aggregation.unwind("shippingInstructionDetails", true), lookupUser,
				Aggregation.unwind("userDetails", true), lookupStock, Aggregation.unwind("stockDetails", true),
				projectionOperation);
		final AggregationResults<ShippingRequestedItemsDto> result = mongoTemplate.aggregate(aggregation,
				"t_shppng_rqust", ShippingRequestedItemsDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<TShippingRoroExcelDto> roroConfirmedExcelReport(String scheduleId, String destCountry) {
		MatchOperation match = Aggregation.match(new Criteria().andOperator(Criteria.where("scheduleId").is(scheduleId),
				Criteria.where("status").is(Constants.SHIPIING_REQUEST_ACCEPTED),
				Criteria.where("destCountry").is(destCountry),
				Criteria.where("shippingType").is(Constants.STOCK_SHIPPING_TYPE_RORO)));
		final LookupOperation lookupStock = Aggregation.lookup("t_stck", "stockNo", "stockNo", "stockDetails");
		final LookupOperation lookupShippingInstruction = Aggregation.lookup("t_shppng_instructn",
				"shippingInstructionId", "shippingInstructionId", "shippingInsDetails");
		final LookupOperation lookupUser = Aggregation.lookup("m_usr", "shippingInsDetails.salesPersonId", "code",
				"UserDetails");
		final LookupOperation lookupInspectionOrder = Aggregation.lookup("t_inspctn_odr_rqst", "stockNo", "stockNo",
				"inspectionDetails");
		final AggregationOperation inspectionRequestDetails = context -> new Document("$addFields",
				new Document("inspectionRequestDetails",
						new Document("$filter", new Document("input", "$inspectionDetails").append("as", "result")
								.append("cond", new Document("$and", Arrays.asList(new Document("$eq",
										Arrays.asList("$$result.country", "$stockDetails.destinationCountry"))))))));

		final AggregationOperation addRecentInspectionRequestDetails = (context) -> new Document("$addFields",
				new Document("inspectionDetails",
						new Document("$arrayElemAt",
								Arrays.asList("$inspectionRequestDetails",
										new Document("$indexOfArray", Arrays.asList(
												"$inspectionRequestDetails.createdDate",
												new Document("$max", "inspectionRequestDetails.createdDate")))))));

		final LookupOperation lookupPurchaseInvoice = Aggregation.lookup("t_prchs_invc", "stockNo", "stockNo",
				"purchaseInvoice");
		final LookupOperation lookupInspectionCompany = Aggregation.lookup("m_inspctn_cmpny",
				"inspectionDetails.inspectionCompany", "code", "inspectionCompany");
		final LookupOperation lookupDocConversion = Aggregation.lookup("t_dcmnt_cnvrsn", "stockNo", "stockNo",
				"docDetails");
		final LookupOperation lookupLastLapVehicles = LookupOperation.newLookup().from("m_lt_lp_vhcls")
				.localField("destCountry").foreignField("destinationCountry").as("lastLapVehicles");
		final LookupOperation lookupMLocation = LookupOperation.newLookup().from("m_lctn")
				.localField("stockDetails.lastTransportLocation").foreignField("code").as("mLocation");
		final LookupOperation lookupCustomer = LookupOperation.newLookup().from("t_cstmr")
				.localField("$shippingInsDetails.customerId").foreignField("code").as("customerDetails");
		final LookupOperation lookupExportCertificate = LookupOperation.newLookup().from("t_exprt_crtfct")
				.localField("stockNo").foreignField("stockNo").as("exportCertificateDetails");
		final LookupOperation lookupShpmntSchdl = LookupOperation.newLookup().from("t_shpmnt_schdl")
				.localField("scheduleId").foreignField("scheduleId").as("shippingSchedule");
		final LookupOperation lookupShip = LookupOperation.newLookup().from("m_ship")
				.localField("shippingSchedule.shipId").foreignField("shipId").as("shipDetails");
		final AggregationOperation addConsigneeDtls = context -> new Document("$addFields",
				new Document("consigneeDtls",
						new Document("$filter",
								new Document("input", "$customerDetails.consigneeNotifyparties").append("as", "result")
										.append("cond", new Document("$eq",
												Arrays.asList("$$result._id", "$shippingInsDetails.consigneeId"))))));

		final AggregationOperation addNotifyPartyDtls = context -> new Document("$addFields",
				new Document("notifyPartyDtls",
						new Document("$filter",
								new Document("input", "$customerDetails.consigneeNotifyparties").append("as", "result")
										.append("cond", new Document("$eq",
												Arrays.asList("$$result._id", "$shippingInsDetails.notifypartyId"))))));

		final ProjectionOperation projectionOperation = Aggregation.project().and("stockDetails.firstRegDate")
				.plus("lastLapVehicles.expiryMilliSeconds").as("expiryDate").and("stockDetails.lastTransportLocation")
				.as("originPort").and("stockDetails.stockNo").as("stockNo").and("shipmentRequestId")
				.as("shipmentRequestId").and("stockDetails.chassisNo").as("chassisNo").and("stockDetails.m3").as("m3")
				.and("exportCertificateDetails.length").as("length").and("exportCertificateDetails.width").as("width")
				.and("exportCertificateDetails.height").as("height").and("destCountry").as("destCountry")
				.and("destPort").as("destPort").and("inspectionDetails.inspectionDate").as("createdDate")
				.and("inspectionDetails.status").as("inspectionStatus").and("inspectionDetails.dateOfIssue")
				.as("inspectionDateOfIssue").and("inspectionCompany.name").as("inspCompany").and("stockDetails.maker")
				.as("maker").and("stockDetails.model").as("model").and("UserDetails.fullname")
				.as("shippingInstructionGivenBy").and("stockDetails.sFirstRegDate").as("sFirstRegDate")
				.and("docDetails.docSendDate").as("docSendDate").and("docDetails.docOriginalSent").as("docOriginalSent")
				.and("docDetails.docEmailSent").as("docEmailSent").and("mLocation.shipmentOriginPort")
				.as("originPortName").and("mLocation.displayName").as("locationName").and("mLocation.code")
				.as("locationId").and("consigneeDtls.cAddress").as("consigneeAddress").and("consigneeDtls.cFirstName")
				.as("consigneeName").and("exportCertificateDetails.weight").as("weight")
				.and("notifyPartyDtls.npFirstName").as("notifypartyName").and("notifyPartyDtls.npAddress")
				.as("notifypartyAddress").and("yard").as("yard").and("shippingInsDetails.paymentType").as("paymentType")
				.and("shippingSchedule.voyageNo").as("voyageNo").and("shipDetails.name").as("shipName")
				.and(AccumulatorOperators.Sum.sumOf("purchaseInvoice.purchaseCost")).as("purchaseValue");

		final SortOperation sortOperation = Aggregation.sort(Direction.ASC, "consigneeName");

		final GroupOperation groupOperation = Aggregation.group("$originPortName")
				.push(new BasicDBObject("stockNo", "$stockNo").append("shipmentRequestId", "$shipmentRequestId")
						.append("expiryDate", "$expiryDate").append("chassisNo", "$chassisNo")
						.append("length", "$length").append("width", "$width").append("height", "$height")
						.append("m3", "$m3").append("destCountry", "$destCountry").append("destPort", "$destPort")
						.append("inspCreatedDate", "$createdDate").append("inspCompany", "$inspCompany")
						.append("maker", "$maker").append("model", "$model")
						.append("shippingInstructionGivenBy", "$shippingInstructionGivenBy")
						.append("sFirstRegDate", "$sFirstRegDate").append("docSendDate", "$docSendDate")
						.append("docOriginalSent", "$docOriginalSent").append("docEmailSent", "$docEmailSent")
						.append("consigneeAddress", "$consigneeAddress").append("consigneeName", "$consigneeName")
						.append("weight", "$weight").append("locationName", "$locationName")
						.append("locationId", "$locationId").append("notifypartyName", "$notifypartyName")
						.append("notifypartyAddress", "$notifypartyAddress").append("yard", "$yard")
						.append("paymentType", "$paymentType").append("voyageNo", "$voyageNo")
						.append("inspectionStatus", "$inspectionStatus")
						.append("inspectionDateOfIssue", "$inspectionDateOfIssue").append("shipName", "$shipName")
						.append("purchaseValue", "$purchaseValue"))
				.as("items").first("originPort").as("originPort").first("originPortName").as("originPortName");

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupStock,
				Aggregation.unwind("$stockDetails", true), lookupShippingInstruction,
				Aggregation.unwind("$shippingInsDetails", true), lookupUser, Aggregation.unwind("$UserDetails", true),
				lookupInspectionOrder, inspectionRequestDetails, addRecentInspectionRequestDetails,
				lookupInspectionCompany, Aggregation.unwind("$inspectionCompany", true), lookupDocConversion,
				Aggregation.unwind("$docDetails", true), lookupLastLapVehicles,
				Aggregation.unwind("$lastLapVehicles", true), lookupMLocation, Aggregation.unwind("$mLocation", true),
				lookupCustomer, Aggregation.unwind("$customerDetails", true), addConsigneeDtls, addNotifyPartyDtls,
				Aggregation.unwind("$consigneeDtls", true), Aggregation.unwind("$notifyPartyDtls", true),
				lookupExportCertificate, Aggregation.unwind("$exportCertificateDetails", true), lookupShpmntSchdl,
				Aggregation.unwind("$shippingSchedule", true), lookupShip, Aggregation.unwind("$shipDetails", true),
				lookupPurchaseInvoice, projectionOperation, sortOperation, groupOperation);
		final AggregationResults<TShippingRoroExcelDto> result = mongoTemplate.aggregate(aggregation, "t_shppng_rqust",
				TShippingRoroExcelDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<TShippingRoroExcelDto> roroExcelReport(String scheduleId) {
		final MatchOperation match = Aggregation.match(Criteria.where("scheduleId").is(scheduleId)
				.andOperator(Criteria.where("status").is(Constants.SHIPIING_REQUEST_ACCEPTED)));
		final LookupOperation lookupStock = Aggregation.lookup("t_stck", "stockNo", "stockNo", "stockDetails");
		final LookupOperation lookupShippingInstruction = Aggregation.lookup("t_shppng_instructn",
				"shippingInstructionId", "shippingInstructionId", "shippingInsDetails");
		final LookupOperation lookupUser = Aggregation.lookup("m_usr", "shippingInsDetails.salesPersonId", "code",
				"UserDetails");

		final AggregationOperation addInspectionRequestId = context -> new Document("$addFields",
				new Document("inspectionRequestDetails",
						new Document("$filter", new Document("input", "$stockDetails.inspectionDetails")
								.append("as", "result").append("cond", new Document("$eq",
										Arrays.asList("$$result.country", "$stockDetails.destinationCountry"))))));

		final LookupOperation lookupInspectionOrder = Aggregation.lookup("t_inspctn_odr_rqst",
				"inspectionRequestDetails.inspectionRequestId", "code", "inspectionDetails");
		final LookupOperation lookupInspectionCompany = Aggregation.lookup("m_inspctn_cmpny",
				"inspectionDetails.inspectionCompany", "code", "inspectionCompany");
		final LookupOperation lookupDocConversion = Aggregation.lookup("t_dcmnt_cnvrsn", "stockNo", "stockNo",
				"docDetails");
		final LookupOperation lookupLastLapVehicles = LookupOperation.newLookup().from("m_lt_lp_vhcls")
				.localField("destCountry").foreignField("destinationCountry").as("lastLapVehicles");
		final LookupOperation lookupMLocation = LookupOperation.newLookup().from("m_lctn")
				.localField("stockDetails.lastTransportLocation").foreignField("code").as("mLocation");
		final LookupOperation lookupCustomer = LookupOperation.newLookup().from("t_cstmr")
				.localField("$shippingInsDetails.customerId").foreignField("code").as("customerDetails");
		final LookupOperation lookupExportCertificate = LookupOperation.newLookup().from("t_exprt_crtfct")
				.localField("stockNo").foreignField("stockNo").as("exportCertificateDetails");

		final AggregationOperation addConsigneeDtls = context -> new Document("$addFields",
				new Document("consigneeDtls",
						new Document("$filter",
								new Document("input", "$customerDetails.consigneeNotifyparties").append("as", "result")
										.append("cond", new Document("$eq",
												Arrays.asList("$$result._id", "$shippingInsDetails.consigneeId"))))));

		final ProjectionOperation projectionOperation = Aggregation.project().and("stockDetails.firstRegDate")
				.plus("lastLapVehicles.expiryMilliSeconds").as("expiryDate").and("stockDetails.lastTransportLocation")
				.as("originPort").and("stockDetails.stockNo").as("stockNo").and("shipmentRequestId")
				.as("shipmentRequestId").and("stockDetails.chassisNo").as("chassisNo").and("stockDetails.m3").as("m3")
				.and("stockDetails.length").as("length").and("stockDetails.width").as("width")
				.and("stockDetails.height").as("height").and("destCountry").as("destCountry").and("destPort")
				.as("destPort").and("inspectionDetails.createdDate").as("createdDate").and("inspectionCompany.name")
				.as("inspCompany").and("stockDetails.maker").as("maker").and("stockDetails.model").as("model")
				.and("UserDetails.fullname").as("shippingInstructionGivenBy").and("stockDetails.sFirstRegDate")
				.as("sFirstRegDate").and("docDetails.docSendDate").as("docSendDate").and("docDetails.docOriginalSent")
				.as("docOriginalSent").and("docDetails.docEmailSent").as("docEmailSent").and("orginPort")
				.as("originPortName").and("consigneeDtls.cAddress").as("consigneeAddress")
				.and("consigneeDtls.cFirstName").as("consigneeName").and("exportCertificateDetails.grossWeight")
				.as("weight");

		final SortOperation sortOperation = Aggregation.sort(Direction.ASC, "consigneeName");

		final GroupOperation groupOperation = Aggregation.group("$originPortName")
				.push(new BasicDBObject("stockNo", "$stockNo").append("shipmentRequestId", "$shipmentRequestId")
						.append("expiryDate", "$expiryDate").append("chassisNo", "$chassisNo")
						.append("length", "$length").append("width", "$width").append("height", "$height")
						.append("m3", "$m3").append("destCountry", "$destCountry").append("destPort", "$destPort")
						.append("inspCreatedDate", "$createdDate").append("inspCompany", "$inspCompany")
						.append("maker", "$maker").append("model", "$model")
						.append("shippingInstructionGivenBy", "$shippingInstructionGivenBy")
						.append("sFirstRegDate", "$sFirstRegDate").append("docSendDate", "$docSendDate")
						.append("docOriginalSent", "$docOriginalSent").append("docEmailSent", "$docEmailSent")
						.append("consigneeAddress", "$consigneeAddress").append("consigneeName", "$consigneeName")
						.append("weight", "$weight"))
				.as("items").first("originPort").as("originPort").first("originPortName").as("originPortName");

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupStock,
				Aggregation.unwind("$stockDetails", true), lookupShippingInstruction,
				Aggregation.unwind("$shippingInsDetails", true), lookupUser, Aggregation.unwind("$UserDetails", true),
				addInspectionRequestId, Aggregation.unwind("$inspectionRequestDetails", true), lookupInspectionOrder,
				Aggregation.unwind("$inspectionDetails", true), lookupInspectionCompany,
				Aggregation.unwind("$inspectionCompany", true), lookupDocConversion,
				Aggregation.unwind("$docDetails", true), lookupLastLapVehicles,
				Aggregation.unwind("$lastLapVehicles", true), lookupMLocation, Aggregation.unwind("$mLocation", true),
				lookupCustomer, Aggregation.unwind("$customerDetails", true), addConsigneeDtls,
				Aggregation.unwind("$consigneeDtls", true), lookupExportCertificate,
				Aggregation.unwind("$exportCertificateDetails", true), projectionOperation, sortOperation,
				groupOperation);
		final AggregationResults<TShippingRoroExcelDto> result = mongoTemplate.aggregate(aggregation, "t_shppng_rqust",
				TShippingRoroExcelDto.class);
		return result.getMappedResults();
	}

	@Override
	public TShippingRequestedContainerExcelDto findAllShippingContainerExcelData(String allocationId, Integer status) {

		final MatchOperation match = Aggregation.match(new Criteria()
				.andOperator(Criteria.where("allocationId").is(allocationId), Criteria.where("status").is(status)));

		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");
		final LookupOperation lookupLocation = LookupOperation.newLookup().from("m_lctn")
				.localField("$stock.lastTransportLocation").foreignField("code").as("locationDetails");
		final LookupOperation lookupSchedule = LookupOperation.newLookup().from("t_shpmnt_schdl")
				.localField("scheduleId").foreignField("scheduleId").as("shipping_schedule");
		final LookupOperation lookupShippingInstruction = LookupOperation.newLookup().from("t_shppng_instructn")
				.localField("shippingInstructionId").foreignField("shippingInstructionId")
				.as("shippingInstructionDetails");
		final LookupOperation lookupSalesPerson = LookupOperation.newLookup().from("m_usr")
				.localField("$shippingInstructionDetails.salesPersonId").foreignField("code").as("salesPersonDetails");
		final LookupOperation lookupExportCertificate = LookupOperation.newLookup().from("t_exprt_crtfct")
				.localField("stockNo").foreignField("stockNo").as("exportCertificateDetails");

		final LookupOperation lookupCustomer = LookupOperation.newLookup().from("t_cstmr")
				.localField("shippingInstructionDetails.customerId").foreignField("code").as("customerInfo");

		final AggregationOperation addConsigneeField = context -> new Document("$addFields",
				new Document("consignee",
						new Document("$filter", new Document("input", "$customerInfo.consigneeNotifyparties")
								.append("as", "result").append("cond", new Document("$eq",
										Arrays.asList("$$result._id", "$shippingInstructionDetails.consigneeId"))))));

		final AggregationOperation addETD = context -> new Document("$addFields",
				new Document("etd",
						new Document("$filter", new Document("input", "$shipping_schedule.schedule")
								.append("as", "result").append("cond", new Document("$eq",
										Arrays.asList("$$result.portName", "$locationDetails.shipmentOriginPort"))))));

		final AggregationOperation addETA = context -> new Document("$addFields",
				new Document("eta",
						new Document("$filter",
								new Document("input", "$shipping_schedule.schedule").append("as", "result").append(
										"cond",
										new Document("$eq", Arrays.asList("$$result.portName", "$destPort"))))));

		final SortOperation sortOperation = Aggregation.sort(Direction.ASC, "containerNo");

		final AggregationOperation groupOperation = context -> new Document("$group",
				new Document("_id", new Document("allocationId", "$allocationId"))
						.append("items", new Document("$push",
								new Document("stockNo", "$stock.stockNo").append("chassisNo", "$stock.chassisNo")
										.append("maker", "$stock.maker").append("etd", "$etd.date")
										.append("eta", "$eta.date").append("ctm", "$salesPersonDetails.fullname")
										.append("user", "$stock.shippingUser").append("shippingId", "$stock.shippingId")
										.append("tel", "$stock.shippingTel").append("hsCode", "$stock.hsCode")
										.append("cr", "$exportCertificateDetails.convertedDate")
										.append("consignee", "$consignee.cFirstName")
										.append("customer", "$customerInfo.firstName")
										.append("year", "$stock.firstRegDate").append("model", "$stock.model")
										.append("containerNo", "$containerNo")))
						.append("destCountry", new Document("$first", "$destCountry"))
						.append("destPort", new Document("$first", "$destPort"))
						.append("allocationDate", new Document("$first", "$createdDate")));

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupStock,
				Aggregation.unwind("$stock", true), lookupLocation, Aggregation.unwind("$locationDetails", true),
				lookupSchedule, Aggregation.unwind("$shipping_schedule", true), addETD,
				Aggregation.unwind("$etd", true), addETA, Aggregation.unwind("$eta", true), lookupShippingInstruction,
				Aggregation.unwind("$shippingInstructionDetails", true), lookupCustomer,
				Aggregation.unwind("$customerInfo", true), addConsigneeField, Aggregation.unwind("$consignee", true),
				lookupSalesPerson, Aggregation.unwind("$salesPersonDetails", true), lookupExportCertificate,
				Aggregation.unwind("$exportCertificateDetails", true), sortOperation, groupOperation);
		final AggregationResults<TShippingRequestedContainerExcelDto> result = mongoTemplate.aggregate(aggregation,
				"t_shppng_rqust", TShippingRequestedContainerExcelDto.class);
		return result.getUniqueMappedResult();
	}

	@Override
	public List<TShippingRequest> findAllBlNo() {
		// Projection Operation
		final ProjectionOperation project = Aggregation.project().andInclude("id", "shipmentRequestId", "blNo");

		// Match to remove null or empty
		final MatchOperation match = Aggregation.match(Criteria.where("blNo").nin(null, ""));

		// Aggregating the grouping data
		final Aggregation aggregation = Aggregation.newAggregation(match, project);

		// Aggregating the result to project
		final AggregationResults<TShippingRequest> result = mongoTemplate.aggregate(aggregation, "t_shppng_rqust",
				TShippingRequest.class);

		return result.getMappedResults();
	}

	@Override
	public List<BillOfLandingDto> findByBlNo(String blNo) {

		// Match blNo
		final MatchOperation match = Aggregation.match(Criteria.where("blNo").in(blNo));

		// Lookup Operation for fetching data
		final LookupOperation lookupShippingInstruction = LookupOperation.newLookup().from("t_shppng_instructn")
				.localField("shippingInstructionId").foreignField("shippingInstructionId").as("shippingInstruction");

		final LookupOperation lookupCustomer = LookupOperation.newLookup().from("t_cstmr")
				.localField("shippingInstruction.customerId").foreignField("code").as("customer");

		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");

		final LookupOperation lookupLocation = LookupOperation.newLookup().from("m_lctn")
				.localField("stock.transportInfo.pickupLocation").foreignField("code").as("location");

		final LookupOperation lookupShipSchedule = LookupOperation.newLookup().from("t_shpmnt_schdl")
				.localField("scheduleId").foreignField("scheduleId").as("shipSchedule");

		// Projection Data
		final ProjectionOperation project = Aggregation.project().andInclude("id", "shipmentRequestId", "blNo")
				.and("consignee.cFirstName").as("consignee").and("notify.npFirstName").as("notifyParty").and("blNo")
				.as("billOfLandingNo").and("location.shipmentOriginPort").as("portOfDischarge")
				.and("location.shipmentOriginPort").as("portOfLoading").and("shippingInstruction.destPort")
				.as("portOfDelivery").and("shipSchedule.voyageNo").as("vesselVoyage").and("destCountry")
				.as("finalDestination");

		// Adding Consignee
		final AggregationOperation addConsigneeField = context -> new Document("$addFields",
				new Document("consignee",
						new Document("$filter",
								new Document("input", "$customer.consigneeNotifyparties").append("as", "result")
										.append("cond", new Document("$eq",
												Arrays.asList("$$result._id", "$shippingInstruction.consigneeId"))))));

		// Adding Notifyparty
		final AggregationOperation addNotifyField = context -> new Document("$addFields",
				new Document("notify",
						new Document("$filter", new Document("input", "$customer.consigneeNotifyparties")
								.append("as", "result").append("cond", new Document("$eq",
										Arrays.asList("$$result._id", "$shippingInstruction.notifypartyId"))))));

		// Aggregating the grouping data
		final Aggregation aggregation = Aggregation.newAggregation(match, lookupShippingInstruction,
				Aggregation.unwind("$shippingInstruction", true), lookupStock, Aggregation.unwind("$stock", true),
				lookupCustomer, Aggregation.unwind("$customer", true), lookupLocation,
				Aggregation.unwind("$location", true), addConsigneeField, Aggregation.unwind("$consignee", true),
				addNotifyField, Aggregation.unwind("$notify", true), lookupShipSchedule,
				Aggregation.unwind("$shipSchedule", true), project);

		// Aggregating the result to project
		final AggregationResults<BillOfLandingDto> result = mongoTemplate.aggregate(aggregation, "t_shppng_rqust",
				BillOfLandingDto.class);

		return result.getMappedResults();
	}

	@Override
	public List<TShippingRoroExcelDto> roroArrangedExcelReport(String forwarderId, String destCountry,
			String allocationId, Integer status, String originPort) {

		MatchOperation match = null;
		if (!AppUtil.isObjectEmpty(originPort)) {
			match = Aggregation.match(new Criteria().andOperator(Criteria.where("allocationId").is(allocationId),
					Criteria.where("status").is(Constants.SHIPIING_REQUEST_INITIATED),
					Criteria.where("shippingType").is(Constants.STOCK_SHIPPING_TYPE_RORO),
					Criteria.where("forwarderId").is(forwarderId), Criteria.where("destCountry").is(destCountry),
					Criteria.where("orginPort").is(originPort)));
		} else {
			match = Aggregation.match(new Criteria().andOperator(Criteria.where("allocationId").is(allocationId),
					Criteria.where("status").is(Constants.SHIPIING_REQUEST_INITIATED),
					Criteria.where("shippingType").is(Constants.STOCK_SHIPPING_TYPE_RORO),
					Criteria.where("forwarderId").is(forwarderId), Criteria.where("destCountry").is(destCountry)));
		}

		final LookupOperation lookupStock = Aggregation.lookup("t_stck", "stockNo", "stockNo", "stockDetails");
		final LookupOperation lookupShippingInstruction = Aggregation.lookup("t_shppng_instructn",
				"shippingInstructionId", "shippingInstructionId", "shippingInsDetails");
		final LookupOperation lookupUser = Aggregation.lookup("m_usr", "shippingInsDetails.salesPersonId", "code",
				"UserDetails");
		final LookupOperation lookupInspectionOrder = Aggregation.lookup("t_inspctn_odr_rqst", "stockNo", "stockNo",
				"inspectionDetails");
		final AggregationOperation inspectionRequestDetails = context -> new Document("$addFields",
				new Document("inspectionRequestDetails",
						new Document("$filter", new Document("input", "$inspectionDetails").append("as", "result")
								.append("cond", new Document("$and", Arrays.asList(new Document("$eq",
										Arrays.asList("$$result.country", "$stockDetails.destinationCountry"))))))));

		final AggregationOperation addRecentInspectionRequestDetails = (context) -> new Document("$addFields",
				new Document("inspectionDetails",
						new Document("$arrayElemAt",
								Arrays.asList("$inspectionRequestDetails",
										new Document("$indexOfArray", Arrays.asList(
												"$inspectionRequestDetails.createdDate",
												new Document("$max", "inspectionRequestDetails.createdDate")))))));

		final LookupOperation lookupPurchaseInvoice = Aggregation.lookup("t_prchs_invc", "stockNo", "stockNo",
				"purchaseInvoice");
		final LookupOperation lookupInspectionCompany = Aggregation.lookup("m_inspctn_cmpny",
				"inspectionDetails.inspectionCompany", "code", "inspectionCompany");
		final LookupOperation lookupDocConversion = Aggregation.lookup("t_dcmnt_cnvrsn", "stockNo", "stockNo",
				"docDetails");
		final LookupOperation lookupLastLapVehicles = LookupOperation.newLookup().from("m_lt_lp_vhcls")
				.localField("destCountry").foreignField("destinationCountry").as("lastLapVehicles");
		final LookupOperation lookupMLocation = LookupOperation.newLookup().from("m_lctn")
				.localField("stockDetails.lastTransportLocation").foreignField("code").as("mLocation");
		final LookupOperation lookupCustomer = LookupOperation.newLookup().from("t_cstmr")
				.localField("$shippingInsDetails.customerId").foreignField("code").as("customerDetails");
		final LookupOperation lookupExportCertificate = LookupOperation.newLookup().from("t_exprt_crtfct")
				.localField("stockNo").foreignField("stockNo").as("exportCertificateDetails");
		final LookupOperation lookupShpmntSchdl = LookupOperation.newLookup().from("t_shpmnt_schdl")
				.localField("scheduleId").foreignField("scheduleId").as("shippingSchedule");
		final LookupOperation lookupShip = LookupOperation.newLookup().from("m_ship")
				.localField("shippingSchedule.shipId").foreignField("shipId").as("shipDetails");
		final AggregationOperation addConsigneeDtls = context -> new Document("$addFields",
				new Document("consigneeDtls",
						new Document("$filter",
								new Document("input", "$customerDetails.consigneeNotifyparties").append("as", "result")
										.append("cond", new Document("$eq",
												Arrays.asList("$$result._id", "$shippingInsDetails.consigneeId"))))));

		final AggregationOperation addNotifyPartyDtls = context -> new Document("$addFields",
				new Document("notifyPartyDtls",
						new Document("$filter",
								new Document("input", "$customerDetails.consigneeNotifyparties").append("as", "result")
										.append("cond", new Document("$eq",
												Arrays.asList("$$result._id", "$shippingInsDetails.notifypartyId"))))));

		final ProjectionOperation projectionOperation = Aggregation.project().and("stockDetails.firstRegDate")
				.plus("lastLapVehicles.expiryMilliSeconds").as("expiryDate").and("stockDetails.lastTransportLocation")
				.as("originPort").and("stockDetails.stockNo").as("stockNo").and("shipmentRequestId")
				.as("shipmentRequestId").and("stockDetails.chassisNo").as("chassisNo").and("stockDetails.m3").as("m3")
				.and("exportCertificateDetails.length").as("length").and("exportCertificateDetails.width").as("width")
				.and("exportCertificateDetails.height").as("height").and("destCountry").as("destCountry")
				.and("destPort").as("destPort").and("inspectionDetails.inspectionDate").as("createdDate")
				.and("inspectionDetails.status").as("inspectionStatus").and("inspectionDetails.dateOfIssue")
				.as("inspectionDateOfIssue").and("inspectionCompany.name").as("inspCompany").and("stockDetails.maker")
				.as("maker").and("stockDetails.model").as("model").and("UserDetails.fullname")
				.as("shippingInstructionGivenBy").and("stockDetails.sFirstRegDate").as("sFirstRegDate")
				.and("docDetails.docSendDate").as("docSendDate").and("docDetails.docOriginalSent").as("docOriginalSent")
				.and("docDetails.docEmailSent").as("docEmailSent")
				.and(ConditionalOperators.ifNull("orginPort").thenValueOf("mLocation.shipmentOriginPort"))
				.as("originPortName").and("orginPort").as("orginPort").and("mLocation.displayName").as("locationName")
				.and("mLocation.code").as("locationId").and("consigneeDtls.cAddress").as("consigneeAddress")
				.and("consigneeDtls.cFirstName").as("consigneeName").and("exportCertificateDetails.weight").as("weight")
				.and("notifyPartyDtls.npFirstName").as("notifypartyName").and("notifyPartyDtls.npAddress")
				.as("notifypartyAddress").and("yard").as("yard").and("shippingInsDetails.paymentType").as("paymentType")
				.and("shippingSchedule.voyageNo").as("voyageNo").and("shipDetails.name").as("shipName")
				.and(AccumulatorOperators.Sum.sumOf("purchaseInvoice.purchaseCost")).as("purchaseValue");

		final SortOperation sortOperation = Aggregation.sort(Direction.ASC, "consigneeName");

		final GroupOperation groupOperation = Aggregation.group("$originPortName")
				.push(new BasicDBObject("stockNo", "$stockNo").append("shipmentRequestId", "$shipmentRequestId")
						.append("expiryDate", "$expiryDate").append("chassisNo", "$chassisNo")
						.append("length", "$length").append("width", "$width").append("height", "$height")
						.append("m3", "$m3").append("destCountry", "$destCountry").append("destPort", "$destPort")
						.append("inspCreatedDate", "$createdDate").append("inspCompany", "$inspCompany")
						.append("maker", "$maker").append("model", "$model")
						.append("shippingInstructionGivenBy", "$shippingInstructionGivenBy")
						.append("sFirstRegDate", "$sFirstRegDate").append("docSendDate", "$docSendDate")
						.append("docOriginalSent", "$docOriginalSent").append("docEmailSent", "$docEmailSent")
						.append("consigneeAddress", "$consigneeAddress").append("consigneeName", "$consigneeName")
						.append("weight", "$weight").append("locationName", "$locationName")
						.append("locationId", "$locationId").append("notifypartyName", "$notifypartyName")
						.append("notifypartyAddress", "$notifypartyAddress").append("yard", "$yard")
						.append("paymentType", "$paymentType").append("voyageNo", "$voyageNo")
						.append("inspectionStatus", "$inspectionStatus")
						.append("inspectionDateOfIssue", "$inspectionDateOfIssue").append("shipName", "$shipName")
						.append("purchaseValue", "$purchaseValue"))
				.as("items").first("originPort").as("originPort").first("originPortName").as("originPortName");

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupStock,
				Aggregation.unwind("$stockDetails", true), lookupShippingInstruction,
				Aggregation.unwind("$shippingInsDetails", true), lookupUser, Aggregation.unwind("$UserDetails", true),
				lookupInspectionOrder, inspectionRequestDetails, addRecentInspectionRequestDetails,
				lookupInspectionCompany, Aggregation.unwind("$inspectionCompany", true), lookupDocConversion,
				Aggregation.unwind("$docDetails", true), lookupLastLapVehicles,
				Aggregation.unwind("$lastLapVehicles", true), lookupMLocation, Aggregation.unwind("$mLocation", true),
				lookupCustomer, Aggregation.unwind("$customerDetails", true), addConsigneeDtls, addNotifyPartyDtls,
				Aggregation.unwind("$consigneeDtls", true), Aggregation.unwind("$notifyPartyDtls", true),
				lookupExportCertificate, Aggregation.unwind("$exportCertificateDetails", true), lookupShpmntSchdl,
				Aggregation.unwind("$shippingSchedule", true), lookupShip, Aggregation.unwind("$shipDetails", true),
				lookupPurchaseInvoice, projectionOperation, sortOperation, groupOperation);
		final AggregationResults<TShippingRoroExcelDto> result = mongoTemplate.aggregate(aggregation, "t_shppng_rqust",
				TShippingRoroExcelDto.class);
		return result.getMappedResults();
	}
}
