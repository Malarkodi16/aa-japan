package com.nexware.aajapan.repositories.custom.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AccumulatorOperators;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
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
import org.springframework.transaction.annotation.Transactional;

import com.mongodb.BasicDBObject;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.InspectionApplicationDto;
import com.nexware.aajapan.dto.TInspectionApprovalDataDto;
import com.nexware.aajapan.dto.TInspectionBookingDataDto;
import com.nexware.aajapan.dto.TInspectionCancelledDto;
import com.nexware.aajapan.dto.TInspectionOrderRequestDto;
import com.nexware.aajapan.dto.TInspectionOrderRequestItemDto;
import com.nexware.aajapan.models.TInspectionOrderRequest;
import com.nexware.aajapan.repositories.custom.TInspectionOrderRequestRepositoryCustom;
import com.nexware.aajapan.utils.AppUtil;

public class TInspectionOrderRequestRepositoryCustomImpl implements TInspectionOrderRequestRepositoryCustom {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<TInspectionOrderRequestDto> findAllInspectionOrderRequestByStatus(Integer... status) {
		MatchOperation matchOperation = Aggregation.match(Criteria.where("status").in(status));
		LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");
		LookupOperation locationDetails = LookupOperation.newLookup().from("m_lctn")
				.localField("$stock.lastTransportLocation").foreignField("code").as("locationDetails");
		LookupOperation transportDetails = LookupOperation.newLookup().from("m_trnsprtr")
				.localField("$stock.transportInfo.transporter").foreignField("code").as("transportDetails");
		final LookupOperation customerDetails = LookupOperation.newLookup().from("t_cstmr")
				.localField("$stock.reservedInfo.customerId").foreignField("code").as("customerDetails");
		final LookupOperation userDetails = LookupOperation.newLookup().from("m_lgn")
				.localField("$stock.reservedInfo.salesPersonId").foreignField("userId").as("userDetails");
		LookupOperation lookupForwarder = LookupOperation.newLookup().from("m_frwrdr").localField("forwarder")
				.foreignField("code").as("forwarder");
		LookupOperation lookupInspectionCompany = LookupOperation.newLookup().from("m_inspctn_cmpny")
				.localField("inspectionCompany").foreignField("code").as("inspection_company");
		LookupOperation lookupSupplier = LookupOperation.newLookup().from("m_spplr")
				.localField("$stock.purchaseInfo.supplier").foreignField("supplierCode").as("supplierDetails");
		final LookupOperation shippingInstruction = LookupOperation.newLookup().from("t_shppng_instructn")
				.localField("$stock.shippingInstructionInfo.shippingInstructionId")
				.foreignField("shippingInstructionId").as("shippingInstruction");
		ProjectionOperation project = Aggregation.project()
				.andInclude("_id", "code", "country", "forwarder", "status", "documentSentDate", "dateOfIssue",
						"certificateNo", "doumentSentStatus", "cancelRemark", "inspectionCompanyFlag", "location",
						"inspectionDate")
				.and("stock.stockNo").as("stockNo").and("stock.chassisNo").as("chassisNo").and("stock.engineNo")
				.as("engineNo").and("stock.category").as("category").and("stock.destinationCountry")
				.as("destinationCountry").and("stock.destinationPort").as("destinationPort").and("stock.subcategory")
				.as("subcategory").and("stock.maker").as("maker").and("stock.model").as("model").and("stock.cc")
				.as("cc").and("stock.firstRegDate").as("firstRegDate").and("stock.sFirstRegDate").as("sFirstRegDate")
				.and("stock.mileage").as("mileage").and("stock.remarks").as("remarks").and("stock.color").as("color")
				.and("stock.equipment").as("equipments").and("stock.transportInfo").as("transportInfo")
				.and("stock.purchaseInfo").as("purchaseInfo").and("stock.transportInfo.charge").as("charge")
				.and("stock.mashoCopyReceivedDate").as("mashoCopyReceivedDate").and("forwarder.name").as("forwarder")
				.and("forwarder.code").as("forwarderId").and("stock.isPhotoUploaded").as("isPhotoUploaded")
				.and("$stock.transportationStatus").as("transportationStatus").and("$stock.lastTransportLocation")
				.as("lastTransportLocation").and("$stock.lastTransportLocationCustom").as("lastTransportLocationCustom")
				.and("createdDate").as("inspectionSentDate").and("inspection_company.code").as("inspectionCompanyId")
				.and("locationDetails.displayName").as("sLastTransportLocation").and("transportDetails.name")
				.as("transporterName").and("customerDetails.firstName").as("customerName").and("userDetails.username")
				.as("salesPerson").and("inspection_company.name").as("inspectionCompany").and("$location.locationId")
				.as("locationId").and("supplierDetails.company").as("supplierName");
		Document groupBy = new Document("yearMonthDay",
				new Document("$dateToString", new Document("format", "%Y-%m-%d").append("date", "$inspectionSentDate")))
						.append("country", "$country").append("inspectionCompanyFlag", "$inspectionCompanyFlag")
						.append("inspectionCompanyId", "$inspectionCompanyId").append("forwarderId", "$forwarderId")
						.append("locationId", "$locationId");
		Document pushItem = new Document("$push", new BasicDBObject("inspectionId", "$_id")
				.append("inspectionCode", "$code").append("stockNo", "$stockNo").append("chassisNo", "$chassisNo")
				.append("model", "$model").append("sLastTransportLocation", "$sLastTransportLocation")
				.append("transporterName", "$transporterName").append("maker", "$maker").append("country", "$country")
				.append("cc", "$cc").append("firstRegDate", "$firstRegDate").append("sFirstRegDate", "$sFirstRegDate")
				.append("mileage", "$mileage").append("remarks", "$remarks")
				.append("destinationCountry", "$destinationCountry").append("destinationPort", "$destinationPort")
				.append("forwarder", "$forwarder").append("category", "$category").append("subcategory", "$subcategory")
				.append("status", "$status").append("date", "$date").append("transportInfo", "$transportInfo")
				.append("dropLocation", "$dropLocation").append("documentSentDate", "$documentSentDate")
				.append("sDocumentSentDate", "$sDocumentSentDate").append("inspectionSentDate", "$inspectionSentDate")
				.append("inspectedDate", "$dateOfIssue").append("inspectionDate", "$inspectionDate")
				.append("inspectionRcvdDate", "$inspectionRcvdDate").append("engineNo", "$engineNo")
				.append("color", "$color").append("equipments", "$equipments")
				.append("transportationStatus", "$transportationStatus").append("isPhotoUploaded", "$isPhotoUploaded")
				.append("lastTransportLocation", "$lastTransportLocation")
				.append("lastTransportLocationCustom", "$lastTransportLocationCustom")
				.append("estimatedDeparture", "$shippingInstruction.estimatedDeparture")
				.append("shippingInstructionStatus", "$shippingInstruction.scheduleType")
				.append("purchaseInfo", "$purchaseInfo").append("charge", "$charge")
				.append("doumentSentStatus", "$doumentSentStatus").append("cancelRemark", "$cancelRemark")
				.append("dateOfIssue", "$dateOfIssue").append("certificateNo", "$certificateNo")
				.append("inspectionCompanyFlag", "$inspectionCompanyFlag").append("bookingDetails", "$bookingDetails")
				.append("inspectionCompany", "$inspectionCompany").append("inspectionCompanyId", "$inspectionCompanyId")
				.append("customerName", "$customerName").append("salesPerson", "$salesPerson")
				.append("supplierName", "$supplierName").append("forwarderId", "$forwarderId")
				.append("mashoCopyReceivedDate", "$mashoCopyReceivedDate"));
		Map<String, Object> query = new LinkedHashMap<>();
		query.put("_id", groupBy);
		query.put("items", pushItem);
		query.put("country", new Document("$first", "$country"));
		query.put("inspectionSentDate", new Document("$first", "$inspectionSentDate"));
		query.put("inspectionDate", new Document("$first", "$inspectionDate"));
		query.put("inspectionCompanyFlag", new Document("$first", "$inspectionCompanyFlag"));
		query.put("inspectionCompany", new Document("$first", "$inspectionCompany"));
		query.put("inspectionCompanyId", new Document("$first", "$inspectionCompanyId"));
		query.put("forwarder", new Document("$first", "$forwarder"));
		query.put("forwarderId", new Document("$first", "$forwarderId"));
		query.put("location", new Document("$first", "$location"));
		AggregationOperation groupAggregationOperation = context -> new Document("$group", new Document(query));

		SortOperation sort = Aggregation.sort(Direction.DESC, "inspectionSentDate");
		Aggregation aggregation = Aggregation.newAggregation(matchOperation, lookupStock,
				Aggregation.unwind("$stock", true), locationDetails, Aggregation.unwind("$locationDetails", true),
				transportDetails, Aggregation.unwind("$transportDetails", true), customerDetails,
				Aggregation.unwind("$customerDetails", true), userDetails, Aggregation.unwind("$userDetails", true),
				lookupForwarder, Aggregation.unwind("$forwarder", true), lookupInspectionCompany,
				Aggregation.unwind("$inspection_company", true), lookupSupplier,
				Aggregation.unwind("$supplierDetails", true), shippingInstruction,
				Aggregation.unwind("$shippingInstruction", true), project, groupAggregationOperation, sort);
		AggregationResults<TInspectionOrderRequestDto> result = this.mongoTemplate.aggregate(aggregation,
				"t_inspctn_odr_rqst", TInspectionOrderRequestDto.class);

		return result.getMappedResults();
	}

	@Override
	public List<TInspectionOrderRequestDto> findAllInspectionOrderRequestByTransportNotComplete(Integer... status) {
		MatchOperation matchOperation = Aggregation.match(Criteria.where("status").in(status));
		LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");
		LookupOperation locationDetails = LookupOperation.newLookup().from("m_lctn")
				.localField("$stock.lastTransportLocation").foreignField("code").as("locationDetails");
		LookupOperation transportDetails = LookupOperation.newLookup().from("m_trnsprtr")
				.localField("$stock.transportInfo.transporter").foreignField("code").as("transportDetails");
		final LookupOperation customerDetails = LookupOperation.newLookup().from("t_cstmr")
				.localField("$stock.reservedInfo.customerId").foreignField("code").as("customerDetails");
		final LookupOperation userDetails = LookupOperation.newLookup().from("m_lgn")
				.localField("$stock.reservedInfo.salesPersonId").foreignField("userId").as("userDetails");
		LookupOperation lookupForwarder = LookupOperation.newLookup().from("m_frwrdr").localField("forwarder")
				.foreignField("code").as("forwarder");
		LookupOperation lookupInspectionCompany = LookupOperation.newLookup().from("m_inspctn_cmpny")
				.localField("inspectionCompany").foreignField("code").as("inspection_company");
		LookupOperation lookupSupplier = LookupOperation.newLookup().from("m_spplr")
				.localField("$stock.purchaseInfo.supplier").foreignField("supplierCode").as("supplierDetails");
		final LookupOperation shippingInstruction = LookupOperation.newLookup().from("t_shppng_instructn")
				.localField("$stock.shippingInstructionInfo.shippingInstructionId")
				.foreignField("shippingInstructionId").as("shippingInstruction");
		MatchOperation matchTransportNotComplete = Aggregation
				.match(Criteria.where("stock.transportationStatus").ne(Constants.TRANSPORT_COMPLETED));
		ProjectionOperation project = Aggregation.project()
				.andInclude("_id", "code", "country", "forwarder", "status", "documentSentDate", "dateOfIssue",
						"certificateNo", "doumentSentStatus", "cancelRemark", "inspectionCompanyFlag", "location",
						"inspectionDate")
				.and("stock.stockNo").as("stockNo").and("stock.chassisNo").as("chassisNo").and("stock.engineNo")
				.as("engineNo").and("stock.category").as("category").and("stock.destinationCountry")
				.as("destinationCountry").and("stock.destinationPort").as("destinationPort").and("stock.subcategory")
				.as("subcategory").and("stock.maker").as("maker").and("stock.model").as("model").and("stock.cc")
				.as("cc").and("stock.firstRegDate").as("firstRegDate").and("stock.sFirstRegDate").as("sFirstRegDate")
				.and("stock.mileage").as("mileage").and("stock.remarks").as("remarks").and("stock.color").as("color")
				.and("stock.equipment").as("equipments").and("stock.transportInfo").as("transportInfo")
				.and("stock.purchaseInfo").as("purchaseInfo").and("stock.transportInfo.charge").as("charge")
				.and("forwarder.name").as("forwarder").and("forwarder.code").as("forwarderId")
				.and("stock.isPhotoUploaded").as("isPhotoUploaded").and("$stock.transportationStatus")
				.as("transportationStatus").and("$stock.lastTransportLocation").as("lastTransportLocation")
				.and("$stock.lastTransportLocationCustom").as("lastTransportLocationCustom").and("createdDate")
				.as("inspectionSentDate").and("inspection_company.code").as("inspectionCompanyId")
				.and("locationDetails.displayName").as("sLastTransportLocation").and("transportDetails.name")
				.as("transporterName").and("customerDetails.firstName").as("customerName").and("userDetails.username")
				.as("salesPerson").and("inspection_company.name").as("inspectionCompany").and("$location.locationId")
				.as("locationId").and("supplierDetails.company").as("supplierName");
		Document groupBy = new Document("yearMonthDay",
				new Document("$dateToString", new Document("format", "%Y-%m-%d").append("date", "$inspectionDate")))
						.append("country", "$country").append("inspectionCompanyFlag", "$inspectionCompanyFlag")
						.append("inspectionCompanyId", "$inspectionCompanyId").append("forwarderId", "$forwarderId")
						.append("locationId", "$locationId");
		Document pushItem = new Document("$push", new BasicDBObject("inspectionId", "$_id")
				.append("inspectionCode", "$code").append("stockNo", "$stockNo").append("chassisNo", "$chassisNo")
				.append("model", "$model").append("sLastTransportLocation", "$sLastTransportLocation")
				.append("transporterName", "$transporterName").append("maker", "$maker").append("country", "$country")
				.append("cc", "$cc").append("firstRegDate", "$firstRegDate").append("sFirstRegDate", "$sFirstRegDate")
				.append("mileage", "$mileage").append("remarks", "$remarks")
				.append("destinationCountry", "$destinationCountry").append("destinationPort", "$destinationPort")
				.append("forwarder", "$forwarder").append("category", "$category").append("subcategory", "$subcategory")
				.append("status", "$status").append("date", "$date").append("transportInfo", "$transportInfo")
				.append("dropLocation", "$dropLocation").append("documentSentDate", "$documentSentDate")
				.append("sDocumentSentDate", "$sDocumentSentDate").append("inspectionSentDate", "$inspectionSentDate")
				.append("inspectedDate", "$dateOfIssue").append("inspectionRcvdDate", "$inspectionRcvdDate")
				.append("engineNo", "$engineNo").append("color", "$color").append("equipments", "$equipments")
				.append("transportationStatus", "$transportationStatus").append("isPhotoUploaded", "$isPhotoUploaded")
				.append("lastTransportLocation", "$lastTransportLocation")
				.append("lastTransportLocationCustom", "$lastTransportLocationCustom")
				.append("estimatedDeparture", "$shippingInstruction.estimatedDeparture")
				.append("shippingInstructionStatus", "$shippingInstruction.scheduleType")
				.append("purchaseInfo", "$purchaseInfo").append("charge", "$charge")
				.append("doumentSentStatus", "$doumentSentStatus").append("cancelRemark", "$cancelRemark")
				.append("dateOfIssue", "$dateOfIssue").append("certificateNo", "$certificateNo")
				.append("inspectionCompanyFlag", "$inspectionCompanyFlag").append("bookingDetails", "$bookingDetails")
				.append("inspectionCompany", "$inspectionCompany").append("inspectionCompanyId", "$inspectionCompanyId")
				.append("customerName", "$customerName").append("salesPerson", "$salesPerson")
				.append("supplierName", "$supplierName").append("forwarderId", "$forwarderId"));
		Map<String, Object> query = new LinkedHashMap<>();
		query.put("_id", groupBy);
		query.put("items", pushItem);
		query.put("country", new Document("$first", "$country"));
		query.put("inspectionSentDate", new Document("$first", "$inspectionSentDate"));
		query.put("inspectionDate", new Document("$first", "$inspectionDate"));
		query.put("inspectionCompanyFlag", new Document("$first", "$inspectionCompanyFlag"));
		query.put("inspectionCompany", new Document("$first", "$inspectionCompany"));
		query.put("inspectionCompanyId", new Document("$first", "$inspectionCompanyId"));
		query.put("forwarder", new Document("$first", "$forwarder"));
		query.put("forwarderId", new Document("$first", "$forwarderId"));
		query.put("location", new Document("$first", "$location"));
		AggregationOperation groupAggregationOperation = context -> new Document("$group", new Document(query));

		SortOperation sort = Aggregation.sort(Direction.DESC, "inspectionSentDate");
		Aggregation aggregation = Aggregation.newAggregation(matchOperation, lookupStock,
				Aggregation.unwind("$stock", true), matchTransportNotComplete, locationDetails,
				Aggregation.unwind("$locationDetails", true), transportDetails,
				Aggregation.unwind("$transportDetails", true), customerDetails,
				Aggregation.unwind("$customerDetails", true), userDetails, Aggregation.unwind("$userDetails", true),
				lookupForwarder, Aggregation.unwind("$forwarder", true), lookupInspectionCompany,
				Aggregation.unwind("$inspection_company", true), lookupSupplier,
				Aggregation.unwind("$supplierDetails", true), shippingInstruction,
				Aggregation.unwind("$shippingInstruction", true), project, groupAggregationOperation, sort);
		AggregationResults<TInspectionOrderRequestDto> result = this.mongoTemplate.aggregate(aggregation,
				"t_inspctn_odr_rqst", TInspectionOrderRequestDto.class);

		return result.getMappedResults();
	}

	@Override
	public List<TInspectionOrderRequestItemDto> findAllInspectionCompletedByStatus(Integer status) {
		MatchOperation matchOperation = Aggregation.match(Criteria.where("status").is(status));
		LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");
		LookupOperation locationDetails = LookupOperation.newLookup().from("m_lctn")
				.localField("$stock.lastTransportLocation").foreignField("code").as("locationDetails");
		LookupOperation transportDetails = LookupOperation.newLookup().from("m_trnsprtr")
				.localField("$stock.transportInfo.transporter").foreignField("code").as("transportDetails");
		final LookupOperation customerDetails = LookupOperation.newLookup().from("t_cstmr")
				.localField("$stock.reservedInfo.customerId").foreignField("code").as("customerDetails");
		final LookupOperation userDetails = LookupOperation.newLookup().from("m_lgn")
				.localField("$stock.reservedInfo.salesPersonId").foreignField("userId").as("userDetails");
		LookupOperation lookupForwarder = LookupOperation.newLookup().from("m_frwrdr").localField("forwarder")
				.foreignField("code").as("forwarder");
		LookupOperation lookupInspectionCompany = LookupOperation.newLookup().from("m_inspctn_cmpny")
				.localField("inspectionCompany").foreignField("code").as("inspection_company");
		LookupOperation lookupSupplier = LookupOperation.newLookup().from("m_spplr")
				.localField("$stock.purchaseInfo.supplier").foreignField("supplierCode").as("supplierDetails");
		final LookupOperation shippingInstruction = LookupOperation.newLookup().from("t_shppng_instructn")
				.localField("$stock.shippingInstructionInfo.shippingInstructionId")
				.foreignField("shippingInstructionId").as("shippingInstruction");
		ProjectionOperation project = Aggregation.project()
				.andInclude("_id", "country", "forwarder", "status", "documentSentDate", "inspectionSentDate",
						"dateOfIssue", "certificateNo", "doumentSentStatus", "cancelRemark", "inspectionCompanyFlag",
						"location", "inspectionDate")
				.and("code").as("inspectionCode").and("stock.stockNo").as("stockNo").and("stock.chassisNo")
				.as("chassisNo").and("stock.engineNo").as("engineNo").and("stock.category").as("category")
				.and("stock.destinationCountry").as("destinationCountry").and("stock.destinationPort")
				.as("destinationPort").and("stock.subcategory").as("subcategory").and("stock.maker").as("maker")
				.and("stock.model").as("model").and("stock.cc").as("cc").and("stock.firstRegDate").as("firstRegDate")
				.and("stock.sFirstRegDate").as("sFirstRegDate").and("stock.mileage").as("mileage").and("stock.remarks")
				.as("remarks").and("stock.color").as("color").and("stock.equipment").as("equipments")
				.and("stock.transportInfo").as("transportInfo").and("stock.purchaseInfo").as("purchaseInfo")
				.and("stock.transportInfo.charge").as("charge").and("forwarder.name").as("forwarder")
				.and("forwarder.code").as("forwarderId").and("stock.isPhotoUploaded").as("isPhotoUploaded")
				.and("$stock.transportationStatus").as("transportationStatus").and("$stock.lastTransportLocation")
				.as("lastTransportLocation").and("$stock.lastTransportLocationCustom").as("lastTransportLocationCustom")
				.and("createdDate").as("inspectionSentDate").and("inspection_company.code").as("inspectionCompanyId")
				.and("locationDetails.displayName").as("sLastTransportLocation").and("transportDetails.name")
				.as("transporterName").and("customerDetails.firstName").as("customerName").and("userDetails.username")
				.as("salesPerson").and("inspection_company.name").as("inspectionCompany").and("$location.locationId")
				.as("locationId").and("supplierDetails.company").as("supplierName").and("_id").as("inspectionId")
				.and("shippingInstruction.estimatedDeparture").as("estimatedDeparture")
				.and("shippingInstruction.scheduleType").as("shippingInstructionStatus");
//		Document groupBy = new Document("yearMonthDay",
//				new Document("$dateToString", new Document("format", "%Y-%m-%d").append("date", "$inspectionSentDate")))
//						.append("country", "$country").append("inspectionCompanyFlag", "$inspectionCompanyFlag")
//						.append("inspectionCompanyId", "$inspectionCompanyId").append("forwarderId", "$forwarderId")
//						.append("locationId", "$locationId");
//		Document pushItem = new Document("$push", new BasicDBObject("inspectionId", "$_id")
//				.append("stockNo", "$stockNo").append("chassisNo", "$chassisNo").append("model", "$model")
//				.append("sLastTransportLocation", "$sLastTransportLocation")
//				.append("transporterName", "$transporterName").append("maker", "$maker").append("country", "$country")
//				.append("cc", "$cc").append("firstRegDate", "$firstRegDate").append("sFirstRegDate", "$sFirstRegDate")
//				.append("mileage", "$mileage").append("remarks", "$remarks")
//				.append("destinationCountry", "$destinationCountry").append("destinationPort", "$destinationPort")
//				.append("forwarder", "$forwarder").append("category", "$category").append("subcategory", "$subcategory")
//				.append("status", "$status").append("date", "$date").append("transportInfo", "$transportInfo")
//				.append("dropLocation", "$dropLocation").append("documentSentDate", "$documentSentDate")
//				.append("sDocumentSentDate", "$sDocumentSentDate").append("inspectionSentDate", "$inspectionSentDate")
//				.append("inspectedDate", "$dateOfIssue").append("inspectionRcvdDate", "$inspectionRcvdDate")
//				.append("engineNo", "$engineNo").append("color", "$color").append("equipments", "$equipments")
//				.append("transportationStatus", "$transportationStatus").append("isPhotoUploaded", "$isPhotoUploaded")
//				.append("lastTransportLocation", "$lastTransportLocation")
//				.append("lastTransportLocationCustom", "$lastTransportLocationCustom")
//				.append("purchaseInfo", "$purchaseInfo").append("charge", "$charge")
//				.append("doumentSentStatus", "$doumentSentStatus").append("cancelRemark", "$cancelRemark")
//				.append("dateOfIssue", "$dateOfIssue").append("certificateNo", "$certificateNo")
//				.append("inspectionCompanyFlag", "$inspectionCompanyFlag").append("bookingDetails", "$bookingDetails")
//				.append("inspectionCompany", "$inspectionCompany").append("inspectionCompanyId", "$inspectionCompanyId")
//				.append("customerName", "$customerName").append("salesPerson", "$salesPerson")
//				.append("supplierName", "$supplierName").append("forwarderId", "$forwarderId"));
//		Map<String, Object> query = new LinkedHashMap<>();
//		query.put("_id", groupBy);
//		query.put("items", pushItem);
//		query.put("country", new Document("$first", "$country"));
//		query.put("inspectionSentDate", new Document("$first", "$inspectionSentDate"));
//		query.put("inspectionCompanyFlag", new Document("$first", "$inspectionCompanyFlag"));
//		query.put("inspectionCompany", new Document("$first", "$inspectionCompany"));
//		query.put("inspectionCompanyId", new Document("$first", "$inspectionCompanyId"));
//		query.put("forwarder", new Document("$first", "$forwarder"));
//		query.put("forwarderId", new Document("$first", "$forwarderId"));
//		query.put("location", new Document("$first", "$location"));
//		AggregationOperation groupAggregationOperation = context -> new Document("$group", new Document(query));

		SortOperation sort = Aggregation.sort(Direction.DESC, "inspectionSentDate");
		Aggregation aggregation = Aggregation.newAggregation(matchOperation, lookupStock,
				Aggregation.unwind("$stock", true), locationDetails, Aggregation.unwind("$locationDetails", true),
				transportDetails, Aggregation.unwind("$transportDetails", true), customerDetails,
				Aggregation.unwind("$customerDetails", true), userDetails, Aggregation.unwind("$userDetails", true),
				lookupForwarder, Aggregation.unwind("$forwarder", true), lookupInspectionCompany,
				Aggregation.unwind("$inspection_company", true), lookupSupplier,
				Aggregation.unwind("$supplierDetails", true), shippingInstruction,
				Aggregation.unwind("$shippingInstruction", true), project, sort);
		AggregationResults<TInspectionOrderRequestItemDto> result = this.mongoTemplate.aggregate(aggregation,
				"t_inspctn_odr_rqst", TInspectionOrderRequestItemDto.class);

		return result.getMappedResults();
	}

	@Override
	public TInspectionOrderRequestItemDto findByInspectionOrderRequestId(String id) {
		MatchOperation match = Aggregation.match(Criteria.where("_id").is(id));
		ProjectionOperation project = Aggregation.project()
				.andInclude("id", "country", "forwarder", "status", "documentSentDate", "inspectionSentDate",
						"dateOfIssue", "certificateNo", "doumentSentStatus", "cancelRemark")
				.and("stock.stockNo").as("stockNo").and("stock.chassisNo").as("chassisNo").and("stock.engineNo")
				.as("engineNo").and("stock.category").as("category").and("stock.destinationCountry")
				.as("destinationCountry").and("stock.destinationPort").as("destinationPort").and("stock.subcategory")
				.as("subcategory").and("stock.maker").as("maker").and("stock.model").as("model").and("stock.cc")
				.as("cc").and("stock.firstRegDate").as("firstRegDate").and("stock.sFirstRegDate").as("sFirstRegDate")
				.and("stock.mileage").as("mileage").and("stock.remarks").as("remarks").and("stock.color").as("color")
				.and("stock.equipment").as("equipments").and("stock.transportInfo").as("transportInfo")
				.and("stock.purchaseInfo").as("purchaseInfo").and("stock.transportInfo.charge").as("charge")
				.and("forwarder.name").as("forwarder").and("$stock.transportationStatus").as("transportationStatus")
				.and("$stock.lastTransportLocation").as("lastTransportLocation")
				.and("$stock.lastTransportLocationCustom").as("lastTransportLocationCustom").and("createdDate")
				.as("inspectionSentDate");
		LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");
		LookupOperation lookupForwarder = LookupOperation.newLookup().from("m_frwrdr").localField("forwarder")
				.foreignField("code").as("forwarder");

		Aggregation aggregation = Aggregation.newAggregation(lookupStock, Aggregation.unwind("$stock", true),
				lookupForwarder, Aggregation.unwind("$forwarder", true), match, project);
		AggregationResults<TInspectionOrderRequestItemDto> result = this.mongoTemplate.aggregate(aggregation,
				TInspectionOrderRequest.class, TInspectionOrderRequestItemDto.class);

		return result.getUniqueMappedResult();
	}

	@Override
	@Transactional
	public void updateInspectionDocumentSentStatus(String id, int status, Date date) {
		Update update = new Update().set("doumentSentStatus", status).set("documentSentDate", date);
		this.mongoTemplate.updateMulti(Query.query(Criteria.where("id").in(id)), update, TInspectionOrderRequest.class);
	}

	@Override
	public void updateInspectionOrderRequestDate(String id, Date inspectionSentDate, Date inspectedDate,
			Date inspectionRcvdDate) {
		Update update = new Update().set("inspectionSentDate", inspectionSentDate).set("inspectedDate", inspectedDate)
				.set("inspectionRcvdDate", inspectionRcvdDate);

		this.mongoTemplate.updateMulti(Query.query(Criteria.where("id").is(id)), update, TInspectionOrderRequest.class);

	}

	@Override
	public Integer findCountOfInspectionRequested() {
		final MatchOperation match = Aggregation
				.match(Criteria.where("status").is(Constants.INSPECTION_INSTRUCTION_GIVEN));
		CountOperation count = Aggregation.count().as("count");
		Aggregation aggregation = Aggregation.newAggregation(match, count);
		AggregationResults<Document> result = this.mongoTemplate.aggregate(aggregation, "t_inspctn_instructn",
				Document.class);
		return !AppUtil.isObjectEmpty(result.getUniqueMappedResult())
				? result.getUniqueMappedResult().getInteger("count")
				: 0;
	}

	@Override
	public InspectionApplicationDto findAllInspectionOrderRequestByParams(Date inspectionDate, String country,
			String inspectionCompanyId, String forwarderId, String locationId) {

		List<Criteria> criterias = new ArrayList<>();
		criterias.add(Criteria.where("inspectionDate").gte(AppUtil.atStartOfDay(inspectionDate))
				.lt(AppUtil.atEndOfDay(inspectionDate)));
		criterias.add(Criteria.where("country").is(country));
		if (!AppUtil.isObjectEmpty(inspectionCompanyId)) {
			criterias.add(Criteria.where("inspectionCompany").is(inspectionCompanyId));
			criterias.add(Criteria.where("location.locationId").is(locationId));
		}
		if (!AppUtil.isObjectEmpty(forwarderId)) {
			criterias.add(Criteria.where("forwarder").is(forwarderId));
		}
		criterias.add(Criteria.where("status").in(Constants.INSPECTION_ORDER_REQUEST_INITIATED,
				Constants.INSPECTION_ORDER_REQUEST_PASSED));
		Criteria criteria = new Criteria().andOperator(criterias.toArray(new Criteria[0]));
		MatchOperation matchOperation = Aggregation.match(criteria);
		LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");

		Document groupBy = new Document("yearMonthDay",
				new Document("$dateToString", new Document("format", "%Y-%m-%d").append("date", "$inspectionDate")))
						.append("country", "$country").append("inspectionCompanyFlag", "$inspectionCompanyFlag")
						.append("inspectionCompany", "$inspectionCompany").append("locationId", "$location.locationId")
						.append("forwarderId", "$forwarder");
		Document pushItem = new Document("$push",
				new BasicDBObject("inspectionId", "$_id").append("stockNo", "$stock.stockNo")
						.append("chassisNo", "$stock.chassisNo").append("model", "$stock.model")
						.append("color", "$stock.color").append("maker", "$stock.maker")
						.append("country", "$stock.country").append("firstRegDate", "$stock.firstRegDate"));
		Map<String, Object> query = new LinkedHashMap<>();
		query.put("_id", groupBy);
		query.put("items", pushItem);
		query.put("country", new Document("$first", "$country"));
		query.put("createdDate", new Document("$first", "$inspectionDate"));
		query.put("inspectionCompanyFlag", new Document("$first", "$inspectionCompanyFlag"));
		query.put("forwarder", new Document("$first", "$forwarder"));
		query.put("inspectionCompany", new Document("$first", "$inspectionCompany"));
		query.put("inspectionCompanylocation", new Document("$first", "$location"));
		query.put("locationNameJrxml", new Document("$first", "$location.locationName"));
		query.put("count", new Document("$sum", 1));
		AggregationOperation groupAggregationOperation = context -> new Document("$group", new Document(query));
		LookupOperation lookupForwarder = LookupOperation.newLookup().from("m_frwrdr").localField("forwarder")
				.foreignField("code").as("forwarderDetails");
		LookupOperation lookupInspectionCompany = LookupOperation.newLookup().from("m_inspctn_cmpny")
				.localField("inspectionCompany").foreignField("code").as("inspection_company");
		ProjectionOperation project = Aggregation.project()
				.andInclude("items", "inspectionCompanylocation", "locationNameJrxml", "country",
						"inspectionCompanyFlag", "createdDate")
				.and("$forwarderDetails.name").as("forwarderName").and("$forwarder").as("forwarderId")
				.and("$inspectionCompany").as("inspectionCompanyId").and("$inspection_company.name")
				.as("inspectionCompanyName").and("$count").as("totalCount");
		Aggregation aggregation = Aggregation.newAggregation(matchOperation, lookupStock,
				Aggregation.unwind("$stock", true), groupAggregationOperation, lookupForwarder,
				Aggregation.unwind("$forwarderDetails", true), lookupInspectionCompany,
				Aggregation.unwind("$inspection_company", true), project);
		AggregationResults<InspectionApplicationDto> result = this.mongoTemplate.aggregate(aggregation,
				"t_inspctn_odr_rqst", InspectionApplicationDto.class);

		return result.getUniqueMappedResult();
	}

	@Override
	public List<TInspectionCancelledDto> getCancelledOrderByStatus(Integer status) {
		MatchOperation matchOperation = Aggregation.match(Criteria.where("status").is(status));
		LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");
		LookupOperation lookupForwarder = LookupOperation.newLookup().from("m_frwrdr").localField("forwarder")
				.foreignField("code").as("forwarder");
		LookupOperation lookupInspectionCompany = LookupOperation.newLookup().from("m_inspctn_cmpny")
				.localField("inspectionCompany").foreignField("code").as("inspection_company");
		final LookupOperation shippingInstruction = LookupOperation.newLookup().from("t_shppng_instructn")
				.localField("$stock.shippingInstructionInfo.shippingInstructionId")
				.foreignField("shippingInstructionId").as("shippingInstruction");
		final LookupOperation customerDetails = LookupOperation.newLookup().from("t_cstmr")
				.localField("$stock.reservedInfo.customerId").foreignField("code").as("customerDetails");
		final LookupOperation userDetails = LookupOperation.newLookup().from("m_lgn")
				.localField("$stock.reservedInfo.salesPersonId").foreignField("userId").as("userDetails");
		LookupOperation lookupStockLocation = LookupOperation.newLookup().from("m_lctn")
				.localField("stock.lastTransportLocation").foreignField("code").as("locationDetail");
		ProjectionOperation project = Aggregation.project()
				.andInclude("stockNo", "code", "cancelRemark", "status", "inspectionCompanyFlag", "location",
						"instructionId")
				.and("stock.stockNo").as("stockNo").and("stock.chassisNo").as("chassisNo").and("country")
				.as("destinationCountry").and("stock.maker").as("maker").and("stock.model").as("model")
				.and("forwarder.name").as("forwarder").and("forwarder.code").as("forwarderId").and("inspectionDate")
				.as("inspectionSentDate").and("inspection_company.name").as("inspectionCompany")
				.and("inspectionCompany").as("inspectionCompanyId").and("remark").as("remark")
				.and("shippingInstruction.estimatedDeparture").as("estimatedDeparture")
				.and("shippingInstruction.scheduleType").as("shippingInstructionStatus")
				.and("stock.lastTransportLocation").as("lastTransportLocationId")
				.and("stock.lastTransportLocationCustom").as("lastTransportLocationCustom")
				.and("locationDetail.displayName").as("lastTransportLocationName").and("customerDetails.firstName")
				.as("customerName").and("userDetails.username").as("salesPerson");

		Aggregation aggregation = Aggregation.newAggregation(matchOperation, lookupStock,
				Aggregation.unwind("$stock", true), lookupStockLocation, Aggregation.unwind("$locationDetail", true),
				lookupForwarder, Aggregation.unwind("$forwarder", true), lookupInspectionCompany,
				Aggregation.unwind("$inspection_company", true), shippingInstruction,
				Aggregation.unwind("$shippingInstruction", true), customerDetails,
				Aggregation.unwind("$customerDetails", true), userDetails, Aggregation.unwind("$userDetails", true),
				project);
		AggregationResults<TInspectionCancelledDto> result = this.mongoTemplate.aggregate(aggregation,
				"t_inspctn_odr_rqst", TInspectionCancelledDto.class);

		return result.getMappedResults();
	}

	@Override
	public List<TInspectionBookingDataDto> getAllInspectionBookingData() {
		MatchOperation matchOperation = Aggregation
				.match(Criteria.where("status").is(Constants.INSPECTION_ORDER_REQUEST_COMPLETE).andOperator(
						Criteria.where("inspectionCompanyFlag").is(0),
						Criteria.where("bookingStatus").in(Constants.INVOICE_FOR_INSPECTION_ORDER_NOT_BOOKED)));
		LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");
		LookupOperation lookupInspectionCompany = LookupOperation.newLookup().from("m_inspctn_cmpny")
				.localField("inspectionCompany").foreignField("code").as("inspection_company");
		ProjectionOperation project = Aggregation.project()
				.andInclude("_id", "code", "instructionId", "comment", "status", "inspectionDate", "documentSentDate",
						"doumentSentStatus", "inspectionCompanyFlag", "dateOfIssue", "certificateNo")
				.and("stock.stockNo").as("stockNo").and("stock.chassisNo").as("chassisNo")
				.and("stock.destinationCountry").as("destinationCountry").and("stock.destinationPort")
				.as("destinationPort").and("stock.maker").as("maker").and("stock.model").as("model")
				.and("stock.purchaseInfo.date").as("purchaseDate").and("createdDate").as("inspectionSentDate")
				.and("inspection_company.code").as("inspectionCompanyId").and("inspection_company.name")
				.as("inspectionCompany");

		SortOperation sort = Aggregation.sort(Direction.DESC, "inspectionSentDate");
		Aggregation aggregation = Aggregation.newAggregation(matchOperation, lookupStock,
				Aggregation.unwind("$stock", true), lookupInspectionCompany,
				Aggregation.unwind("$inspection_company", true), project, sort);
		AggregationResults<TInspectionBookingDataDto> result = this.mongoTemplate.aggregate(aggregation,
				"t_inspctn_odr_rqst", TInspectionBookingDataDto.class);

		return result.getMappedResults();
	}

	@Override
	public List<TInspectionApprovalDataDto> getAllInspectionApprovalData() {
		MatchOperation matchOperation = Aggregation
				.match(Criteria.where("paymentStatus").is(Constants.INSPECTION_PAYMENT_INVOICE_BOOKING_APPROVED));
		LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");

		GroupOperation group = Aggregation.group("$invoiceRefNo")
				.push(new BasicDBObject("stockNo", "$stock.stockNo").append("code", "$code")
						.append("chassisNo", "$stock.chassisNo").append("maker", "$stock.maker")
						.append("model", "$stock.model").append("amount", "$amount").append("taxAmount", "$taxAmount")
						.append("totalTaxIncluded", "$totalTaxIncluded"))
				.as("items").first("invoiceNo").as("invoiceNo").first("refNo").as("refNo").first("inspectionCompanyId")
				.as("inspectionCompanyId").first("dueDate").as("dueDate").first("invoiceAttachmentFilename")
				.as("invoiceAttachmentFilename").first("invoiceAttachmentDiskFilename")
				.as("invoiceAttachmentDiskFilename").first("invoiceUpload").as("invoiceUpload")
				.first("attachmentViewed").as("attachmentViewed")
				.first(LiteralOperators.Literal.asLiteral(Constants.INVOICE_TYPE_INSPECTION)).as("type");

		LookupOperation lookupInspectionCompany = LookupOperation.newLookup().from("m_inspctn_cmpny")
				.localField("inspectionCompanyId").foreignField("code").as("inspection_company");

		ProjectionOperation project = Aggregation.project()
				.andInclude("refNo", "dueDate", "items", "invoiceAttachmentFilename", "invoiceAttachmentDiskFilename",
						"invoiceUpload", "attachmentViewed", "type", "invoiceNo")
				.and("inspection_company.name").as("company").and("inspectionCompanyId").as("companyId")
				.and(AccumulatorOperators.Sum.sumOf("$items.totalTaxIncluded")).as("totalAmount");
		SortOperation sort = Aggregation.sort(Direction.DESC, "dueDate");
		Aggregation aggregation = Aggregation.newAggregation(matchOperation, lookupStock,
				Aggregation.unwind("$stock", true), group, lookupInspectionCompany,
				Aggregation.unwind("$inspection_company", true), project, sort);
		AggregationResults<TInspectionApprovalDataDto> result = this.mongoTemplate.aggregate(aggregation,
				"t_inspctn_invc", TInspectionApprovalDataDto.class);

		return result.getMappedResults();
	}

	@Override
	public Integer findCountOfInspectionAvailable() {
		final MatchOperation match1 = Aggregation
				.match(Criteria.where("inspectionStatus").is(Constants.STOCK_AVAILABLE_FOR_INSPECTION)
						.andOperator(Criteria.where("status").ne(Constants.STOCK_STATUS_CANCEL)));
		final LookupOperation lookupCountry = LookupOperation.newLookup().from("m_cntry_prt")
				.localField("destinationCountry").foreignField("country").as("countryDetails");
		final MatchOperation match2 = Aggregation
				.match(Criteria.where("countryDetails.inspectionFlag").is(Constants.COUNTRY_INSPECTION_NEED));
//		Criteria.where("countryDetails.inspectionFlag").is(Constants.COUNTRY_INSPECTION_NEED)
		CountOperation count = Aggregation.count().as("count");
		Aggregation aggregation = Aggregation.newAggregation(match1, lookupCountry,
				Aggregation.unwind("$countryDetails", true), match2, count);
		AggregationResults<Document> result = this.mongoTemplate.aggregate(aggregation, "t_stck", Document.class);
		return !AppUtil.isObjectEmpty(result.getUniqueMappedResult())
				? result.getUniqueMappedResult().getInteger("count")
				: 0;
	}

}
