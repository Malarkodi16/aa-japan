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
import com.nexware.aajapan.dto.InventoryDto;
import com.nexware.aajapan.repositories.custom.InventoryRepositoryCustom;

public class InventoryRepositoryCustomImpl implements InventoryRepositoryCustom {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<InventoryDto> findAllInventoryStockDetails() {

		final MatchOperation match = Aggregation.match(Criteria.where("status").is(Constants.STOCK_STATUS_SOLD));
		final LookupOperation lookupShpmntRequest = LookupOperation.newLookup().from("t_shppng_rqust")
				.localField("shipmentRequestId").foreignField("shipmentRequestId").as("shipping_reqst");
		final LookupOperation lookupSchedule = LookupOperation.newLookup().from("t_shpmnt_schdl")
				.localField("shipping_reqst.scheduleId").foreignField("scheduleId").as("shipping_schedule");
		final LookupOperation lookupSalesInv = LookupOperation.newLookup().from("t_sls_inv")
				.localField("slaesInvoiceId").foreignField("_id").as("salesInv");

		final AggregationOperation addETD = context -> new Document("$addFields", new Document("etd", new Document(
				"$filter", new Document("input", "$shipping_schedule.schedule").append("as", "result").append("cond",
						new Document("$eq", Arrays.asList("$$result.portName", "$shipping_reqst.orginPort"))))));

		final AggregationOperation addETA = context -> new Document("$addFields",
				new Document("eta", new Document("$filter",
						new Document("input", "$shipping_schedule.schedule").append("as", "result").append("cond",
								new Document("$eq", Arrays.asList("$$result.portName", "$shipping_reqst.destPort"))))));

		final ProjectionOperation project = Aggregation.project().andInclude("stockNo", "chassisNo")
				.and("shipping_reqst.status").as("shippingStatus").and("etd.date").as("etd").and("eta.date").as("eta")
				.and("salesInv.fob").as("sellingPrice").and("salesInv.paymentType").as("paymentType")
				.and("salesInv.status").as("paymentStatus");

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupShpmntRequest,
				Aggregation.unwind("$shipping_reqst", true), lookupSchedule,
				Aggregation.unwind("$shipping_schedule", true), lookupSalesInv, Aggregation.unwind("$salesInv", true),
				addETD, Aggregation.unwind("$etd", true), addETA, Aggregation.unwind("$eta", true), project);
		final AggregationResults<InventoryDto> result = mongoTemplate.aggregate(aggregation, "t_stck",
				InventoryDto.class);
		return result.getMappedResults();

	}

}
