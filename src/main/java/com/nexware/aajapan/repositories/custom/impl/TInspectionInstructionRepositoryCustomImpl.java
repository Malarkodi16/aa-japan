package com.nexware.aajapan.repositories.custom.impl;

import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.TInspectionInstructionDto;
import com.nexware.aajapan.repositories.custom.TInspectionInstructionRepositoryCustom;

public class TInspectionInstructionRepositoryCustomImpl implements TInspectionInstructionRepositoryCustom {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<TInspectionInstructionDto> getAllGivenInstruction() {
		final MatchOperation match = Aggregation
				.match(Criteria.where("status").is(Constants.INSPECTION_INSTRUCTION_GIVEN));
		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stockDetails");
		final LookupOperation locationDetails = LookupOperation.newLookup().from("m_lctn")
				.localField("stockDetails.lastTransportLocation").foreignField("code").as("locationDetails");
		final LookupOperation supplierDetails = LookupOperation.newLookup().from("m_spplr")
				.localField("stockDetails.purchaseInfo.supplier").foreignField("supplierCode").as("supplierDetails");
		final LookupOperation customerDetails = LookupOperation.newLookup().from("t_cstmr")
				.localField("stockDetails.reservedInfo.customerId").foreignField("code").as("customerDetails");
		final LookupOperation userDetails = LookupOperation.newLookup().from("m_lgn")
				.localField("stockDetails.reservedInfo.salesPersonId").foreignField("userId").as("userDetails");
		final LookupOperation transportDetails = LookupOperation.newLookup().from("m_trnsprtr")
				.localField("stockDetails.transportInfo.transporter").foreignField("code").as("transportDetails");
		final LookupOperation shippingInstruction = LookupOperation.newLookup().from("t_shppng_instructn")
				.localField("stockDetails.shippingInstructionInfo.shippingInstructionId")
				.foreignField("shippingInstructionId").as("shippingInstruction");
		final LookupOperation shippingRequest = LookupOperation.newLookup().from("t_shppng_rqust")
				.localField("stockDetails.shipmentRequestId").foreignField("shipmentRequestId").as("shippingRequest");
		final LookupOperation mLocation = LookupOperation.newLookup().from("m_lctn")
				.localField("stockDetails.lastTransportLocation").foreignField("code").as("mLocation");
		final LookupOperation shippingSchedule = LookupOperation.newLookup().from("t_shpmnt_schdl")
				.localField("shippingRequest.scheduleId").foreignField("scheduleId").as("shippingSchedule");
		final LookupOperation lookupShip = LookupOperation.newLookup().from("m_ship")
				.localField("shippingSchedule.shipId").foreignField("shipId").as("ship");
		final LookupOperation lookupShippingCompany = LookupOperation.newLookup().from("m_shppng_cmpny")
				.localField("shippingSchedule.shippingCompanyNo").foreignField("shippingCompanyNo")
				.as("shippingCompany");
		final LookupOperation lookupTransportOrderItems = LookupOperation.newLookup().from("trnsprt_ordr_items")
				.localField("stockNo").foreignField("stockNo").as("trnsprt_ordr_items");
		final AggregationOperation addTransportItem = (
				context) -> new Document("$addFields",
						new Document("trnsprt_ordr_item",
								new Document("$filter",
										new Document("input", "$trnsprt_ordr_items").append("as", "result")
												.append("cond", new Document("$and", Arrays.asList(
														new Document("$ne",
																Arrays.asList("$$result.status",
																		Constants.TRANSPORT_ITEM_CANCELED)),
														new Document("$ne", Arrays.asList("$$result.status",
																Constants.TRANSPORT_ITEM_REARRANGE))))))));
		final AggregationOperation addRecentTransportItem = (context) -> new Document("$addFields",
				new Document("trnsprt_ordr_item",
						new Document("$arrayElemAt",
								Arrays.asList("$trnsprt_ordr_item",
										new Document("$indexOfArray", Arrays.asList("$trnsprt_ordr_item.createdDate",
												new Document("$max", "trnsprt_ordr_item.createdDate")))))));
		final AggregationOperation addETDField = (context) -> new Document("$addFields",
				new Document("etdValue",
						new Document("$filter",
								new Document("input", "$shippingSchedule.schedule").append("as", "result")
										.append("cond", new Document("$and", Arrays.asList(
												// new Document("$ifNull", Arrays.asList("$$result", null)),
												new Document("$eq",
														Arrays.asList("$$result.portName",
																"$mLocation.shipmentOriginPort")),
												new Document("$eq",
														Arrays.asList("$$result.portFlag", "loading"))))))));

		final ProjectionOperation project = Aggregation.project().andInclude("stockNo", "code").and("destCountry")
				.as("destinationCountry").and("stockDetails.chassisNo").as("chassisNo").and("stockDetails.model")
				.as("model").and("stockDetails.destinationPort").as("destinationPort")
				.and("stockDetails.lastTransportLocationCustom").as("lastTransportLocationCustom")
				.and("stockDetails.firstRegDate").as("firstRegDate").and("stockDetails.color").as("color")
				.and("stockDetails.inspectionDetails").as("inspectionDetails").and("stockDetails.isPhotoUploaded")
				.as("isPhotoUploaded").and("stockDetails.model").as("model").and("stockDetails.model").as("model")
				.and("stockDetails.model").as("model").and("stockDetails.mashoCopyReceivedDate")
				.as("documentReceivedDate").and("locationDetails.displayName").as("sLastTransportLocation")
				.and("stockDetails.purchaseInfo.date").as("purchaseDate").and("stockDetails.purchaseInfo.supplier")
				.as("supplier").and("stockDetails.lastTransportLocation").as("lastTransportLocation")
				.and("locationDetails.displayName").as("sLastTransportLocation").and("purchaseInfo.date")
				.as("purchaseDate").and("purchaseInfo.supplier").as("supplier").and("supplierDetails.company")
				.as("supplierName").and("stockDetails.purchaseInfo.auctionInfo.lotNo").as("lotNo")
				// .and("transportOrderItems.etd").as("etd") // addETDField,
				// Aggregation.unwind("$etd", true),
				.and("customerDetails.firstName").as("customerName").and("userDetails.username").as("salesPerson")
				.and("etdValue.date").as("shippingDate").and("transportDetails.name").as("transporterName")
				.and("shippingSchedule.voyageNo").as("voyageNo").and("ship.shipId").as("shipId").and("ship.name")
				.as("shipName").and("shippingCompany.shippingCompanyNo").as("shippingCompanyNo")
				.and("shippingCompany.name").as("shippingCompanyName").and("shippingRequest.status")
				.as("shippingStatus").and("shippingInstruction.scheduleType").as("shippingInstructionStatus")
				.and("shippingInstruction.estimatedDeparture").as("estimatedDeparture")
				.and("trnsprt_ordr_item.status")
				.as("transportStatus").and("trnsprt_ordr_item.etd")
				.as("transportDeliveryDate");

		Aggregation aggregation = Aggregation.newAggregation(match, lookupStock,
				Aggregation.unwind("$stockDetails", true), locationDetails,
				Aggregation.unwind("$locationDetails", true), supplierDetails,
				Aggregation.unwind("$supplierDetails", true), customerDetails,
				Aggregation.unwind("$customerDetails", true), userDetails, Aggregation.unwind("$userDetails", true),
				// transportInvoice, Aggregation.unwind("$invoiceDetails", true),
				// transportOrderItems,Aggregation.unwind("$transportOrderItems", true)
				transportDetails, Aggregation.unwind("$transportDetails", true), shippingInstruction,
				Aggregation.unwind("$shippingInstruction", true), shippingRequest,
				Aggregation.unwind("$shippingRequest", true), mLocation, Aggregation.unwind("$mLocation", true),
				shippingSchedule, Aggregation.unwind("$shippingSchedule", true), addETDField,
				Aggregation.unwind("$etdValue", true), lookupShip, Aggregation.unwind("ship", true),
				lookupShippingCompany, Aggregation.unwind("shippingCompany", true), lookupTransportOrderItems,
				addTransportItem, addRecentTransportItem, project);

		AggregationResults<TInspectionInstructionDto> result = this.mongoTemplate.aggregate(aggregation,
				"t_inspctn_instructn", TInspectionInstructionDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<TInspectionInstructionDto> getAllGivenInstructionByStockNo(List<String> stockNo) {
		final MatchOperation match = Aggregation.match(new Criteria().andOperator(Criteria.where("stockNo").in(stockNo),
				Criteria.where("status").is(Constants.INSPECTION_INSTRUCTION_GIVEN)));
		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stockDetails");
		final LookupOperation locationDetails = LookupOperation.newLookup().from("m_lctn")
				.localField("stockDetails.lastTransportLocation").foreignField("code").as("locationDetails");

		final LookupOperation supplierDetails = LookupOperation.newLookup().from("m_spplr")
				.localField("stockDetails.purchaseInfo.supplier").foreignField("supplierCode").as("supplierDetails");
		final LookupOperation customerDetails = LookupOperation.newLookup().from("t_cstmr")
				.localField("stockDetails.reservedInfo.customerId").foreignField("code").as("customerDetails");
		final LookupOperation userDetails = LookupOperation.newLookup().from("m_lgn")
				.localField("stockDetails.reservedInfo.salesPersonId").foreignField("userId").as("userDetails");
		final LookupOperation transportDetails = LookupOperation.newLookup().from("m_trnsprtr")
				.localField("stockDetails.transportInfo.transporter").foreignField("code").as("transportDetails");

		final LookupOperation shippingRequest = LookupOperation.newLookup().from("t_shppng_rqust")
				.localField("stockDetails.shipmentRequestId").foreignField("shipmentRequestId").as("shippingRequest");
		final LookupOperation mLocation = LookupOperation.newLookup().from("m_lctn")
				.localField("stockDetails.lastTransportLocation").foreignField("code").as("mLocation");
		final LookupOperation shippingSchedule = LookupOperation.newLookup().from("t_shpmnt_schdl")
				.localField("shippingRequest.scheduleId").foreignField("scheduleId").as("shippingSchedule");
		final LookupOperation lookupShip = LookupOperation.newLookup().from("m_ship")
				.localField("shippingSchedule.shipId").foreignField("shipId").as("ship");
		final LookupOperation lookupShippingCompany = LookupOperation.newLookup().from("m_shppng_cmpny")
				.localField("shippingSchedule.shippingCompanyNo").foreignField("shippingCompanyNo")
				.as("shippingCompany");

		final AggregationOperation addETDField = (context) -> new Document("$addFields",
				new Document("etdValue",
						new Document("$filter",
								new Document("input", "$shippingSchedule.schedule").append("as", "result")
										.append("cond", new Document("$and", Arrays.asList(
												// new Document("$ifNull", Arrays.asList("$$result", null)),
												new Document("$eq",
														Arrays.asList("$$result.portName",
																"$mLocation.shipmentOriginPort")),
												new Document("$eq",
														Arrays.asList("$$result.portFlag", "loading"))))))));

		final ProjectionOperation project = Aggregation.project().andInclude("stockNo", "code").and("destCountry")
				.as("destinationCountry").and("stockDetails.chassisNo").as("chassisNo").and("stockDetails.model")
				.as("model").and("stockDetails.destinationPort").as("destinationPort")
				.and("stockDetails.lastTransportLocationCustom").as("lastTransportLocationCustom")
				.and("stockDetails.firstRegDate").as("firstRegDate").and("stockDetails.color").as("color")
				.and("stockDetails.inspectionDetails").as("inspectionDetails").and("stockDetails.isPhotoUploaded")
				.as("isPhotoUploaded").and("stockDetails.model").as("model").and("stockDetails.model").as("model")
				.and("stockDetails.model").as("model").and("stockDetails.mashoCopyReceivedDate")
				.as("documentReceivedDate").and("locationDetails.displayName").as("sLastTransportLocation")
				.and("stockDetails.purchaseInfo.date").as("purchaseDate").and("stockDetails.purchaseInfo.supplier")
				.as("supplier").and("stockDetails.lastTransportLocation").as("lastTransportLocation")
				.and("locationDetails.displayName").as("sLastTransportLocation").and("purchaseInfo.date")
				.as("purchaseDate").and("purchaseInfo.supplier").as("supplier").and("supplierDetails.company")
				.as("supplierName").and("stockDetails.purchaseInfo.auctionInfo.lotNo").as("lotNo")
				// .and("transportOrderItems.etd").as("etd") // addETDField,
				// Aggregation.unwind("$etd", true),
				.and("customerDetails.firstName").as("customerName").and("userDetails.username").as("salesPerson")
				.and("etdValue.date").as("shippingDate").and("transportDetails.name").as("transporterName")
				.and("shippingSchedule.voyageNo").as("voyageNo").and("ship.shipId").as("shipId").and("ship.name")
				.as("shipName").and("shippingCompany.shippingCompanyNo").as("shippingCompanyNo")
				.and("shippingCompany.name").as("shippingCompanyName").and("shippingRequest.status")
				.as("shippingStatus");

		Aggregation aggregation = Aggregation.newAggregation(match, lookupStock,
				Aggregation.unwind("$stockDetails", true), locationDetails,
				Aggregation.unwind("$locationDetails", true), supplierDetails,
				Aggregation.unwind("$supplierDetails", true), customerDetails,
				Aggregation.unwind("$customerDetails", true), userDetails, Aggregation.unwind("$userDetails", true),
				// transportInvoice, Aggregation.unwind("$invoiceDetails", true),
				// transportOrderItems,Aggregation.unwind("$transportOrderItems", true)
				transportDetails, Aggregation.unwind("$transportDetails", true), shippingRequest,
				Aggregation.unwind("$shippingRequest", true), mLocation, Aggregation.unwind("$mLocation", true),
				shippingSchedule, Aggregation.unwind("$shippingSchedule", true), addETDField,
				Aggregation.unwind("$etdValue", true), lookupShip, Aggregation.unwind("ship", true),
				lookupShippingCompany, Aggregation.unwind("shippingCompany", true), project);

		AggregationResults<TInspectionInstructionDto> result = this.mongoTemplate.aggregate(aggregation,
				"t_inspctn_instructn", TInspectionInstructionDto.class);
		return result.getMappedResults();
	}

}
