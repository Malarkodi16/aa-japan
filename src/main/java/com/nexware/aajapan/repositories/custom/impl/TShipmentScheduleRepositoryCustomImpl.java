package com.nexware.aajapan.repositories.custom.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.BasicDBObject;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.VessalDto;
import com.nexware.aajapan.dto.ViewShipmentScheduleDto;
import com.nexware.aajapan.models.TShipmentSchedule;
import com.nexware.aajapan.repositories.custom.TShipmentScheduleRepositoryCustom;
import com.nexware.aajapan.utils.AppUtil;

public class TShipmentScheduleRepositoryCustomImpl implements TShipmentScheduleRepositoryCustom {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<ViewShipmentScheduleDto> findAllShippingScheduleDetails(String orginPort, String destPort) {
		final MatchOperation matchOperation = Aggregation
				.match(Criteria.where("deleteFlag").is(Constants.DELETE_FLAG_0));

		final LookupOperation lookupShippingCompany = LookupOperation.newLookup().from("m_shppng_cmpny")
				.localField("shippingCompanyNo").foreignField("shippingCompanyNo").as("companyName");
		final LookupOperation lookupShip = LookupOperation.newLookup().from("m_ship").localField("shipId")
				.foreignField("shipId").as("ship");
		final LookupOperation lookupContinent = LookupOperation.newLookup().from("m_cntnnt")
				.localField("schedule.continent").foreignField("code").as("continent");
		// m_cntnnt

		final AggregationOperation sort = Aggregation.sort(Sort.Direction.ASC, "schedule.date");

		final GroupOperation group = Aggregation.group("$scheduleId")
				.push(new BasicDBObject("_id", "$schedule._id").append("date", "$schedule.date")
						.append("soCutDate", "$schedule.soCutDate").append("portName", "$schedule.portName")
						.append("subVessel", "$schedule.subVessel").append("portFlag", "$schedule.portFlag"))
				.as("schedule").first("companyName").as("companyName").first("ship").as("ship").first("schedule")
				.as("first").last("schedule").as("last").first("shippingCompanyNo").as("shippingCompanyNo")
				.first("shipId").as("shipId").first("deckHeight").as("deckHeight").first("voyageNo").as("voyageNo")
				.first("destinationCountry").as("destinationCountry").first("destinationPort").as("destinationPort")
				.first("scheduleId").as("scheduleId").first("scheduleId").as("scheduleId");
		final ProjectionOperation project = Aggregation.project()
				.andInclude("voyageNo", "deckHeight", "schedule", "scheduleId").and("companyName.name")
				.as("shipmentCompany").and("ship.name").as("vesselName").and("first.country").as("startCountry")
				.and("first.portName").as("startPort").and("first.date").as("startDate").and("last.country")
				.as("destinationCountry").and("last.portName").as("destinationPort").and("last.date").as("endDate");

		final List<Document> criteriaList = new ArrayList<>();

		if (!AppUtil.isObjectEmpty(orginPort)) {
			criteriaList
					.add(new Document("$elemMatch", new Document("portName", orginPort).append("portFlag", "loading")));
		}

		if (!AppUtil.isObjectEmpty(destPort)) {
			criteriaList.add(
					new Document("$elemMatch", new Document("portName", destPort).append("portFlag", "destination")));
		}

		Aggregation aggregation;
		if (!criteriaList.isEmpty()) {
			final AggregationOperation match = context -> new Document("$match",
					new Document("schedule", new Document("$all", criteriaList)));
			aggregation = Aggregation.newAggregation(matchOperation, lookupShip, Aggregation.unwind("ship", true),
					Aggregation.unwind("schedule", true), lookupContinent, Aggregation.unwind("continent", true),
					lookupShippingCompany, Aggregation.unwind("companyName", true), sort, group, project, match);
		} else {
			aggregation = Aggregation.newAggregation(matchOperation, lookupShip, Aggregation.unwind("ship", true),
					Aggregation.unwind("schedule", true), lookupContinent, Aggregation.unwind("continent", true),
					lookupShippingCompany, Aggregation.unwind("companyName", true), sort, group, project);
		}

		final AggregationResults<ViewShipmentScheduleDto> result = mongoTemplate.aggregate(aggregation,
				"t_shpmnt_schdl", ViewShipmentScheduleDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<VessalDto> findAllVessal(String destPort) {

		final AggregationOperation matchOperation = context -> new Document("$match",
				new Document("deleteFlag", Constants.DELETE_FLAG_0).append("schedule",
						new Document("$elemMatch", new Document("portName", destPort))));

		// final AggregationOperation addOrgin = context -> new Document("$addFields",
		// new Document("orgin", new Document("$filter", new Document("input",
		// "$schedule").append("as", "result")
		// .append("cond", new Document("$eq", Arrays.asList("$$result.portName",
		// orginPort))))));
		final AggregationOperation addDestination = context -> new Document("$addFields",
				new Document("destination",
						new Document("$filter", new Document("input", "$schedule").append("as", "result").append("cond",
								new Document("$eq", Arrays.asList("$$result.portName", destPort))))));

		// final AggregationOperation matchValid = context -> new Document("$match",
		// new Document("$expr", new Document("$lt", Arrays.asList("$orgin.route",
		// "$destination.route"))));
		final LookupOperation lookupShip = LookupOperation.newLookup().from("m_ship").localField("shipId")
				.foreignField("shipId").as("ship");
		final LookupOperation lookupShippingCompany = LookupOperation.newLookup().from("m_shppng_cmpny")
				.localField("shippingCompanyNo").foreignField("shippingCompanyNo").as("shippingCompany");
		final ProjectionOperation project = Aggregation.project().andInclude("voyageNo", "scheduleId")
				.and("ship.shipId").as("shipId").and("ship.name").as("shipName")
				.and("shippingCompany.shippingCompanyNo").as("shippingCompanyNo").and("shippingCompany.name")
				.as("shippingCompanyName").and("destination.date").as("eta");
		final Aggregation aggregation = Aggregation.newAggregation(matchOperation, addDestination, lookupShip,
				Aggregation.unwind("ship", true), lookupShippingCompany, Aggregation.unwind("shippingCompany", true),
				project);
		final AggregationResults<VessalDto> result = mongoTemplate.aggregate(aggregation, "t_shpmnt_schdl",
				VessalDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<VessalDto> findAllVessalByOrigin(String originPort, String destPort) {

		final Criteria criteria = new Criteria();
		final ArrayList<Criteria> andOperators = new ArrayList<>();
		andOperators.add(Criteria.where("deleteFlag").is(Constants.DELETE_FLAG_0));

		if (!AppUtil.isObjectEmpty(originPort)) {
			andOperators.add(Criteria.where("schedule.portName").is(originPort));

		}
		andOperators.add(Criteria.where("schedule.portName").is(destPort));
		criteria.andOperator(andOperators.toArray(new Criteria[0]));
		final MatchOperation matchOperation = Aggregation.match(criteria);

		// final AggregationOperation addOrgin = context -> new Document("$addFields",
		// new Document("orgin", new Document("$filter", new Document("input",
		// "$schedule").append("as", "result")
		// .append("cond", new Document("$eq", Arrays.asList("$$result.portName",
		// orginPort))))));
		final AggregationOperation addDestination = context -> new Document("$addFields",
				new Document("destination",
						new Document("$filter", new Document("input", "$schedule").append("as", "result").append("cond",
								new Document("$eq", Arrays.asList("$$result.portName", destPort))))));

		// final AggregationOperation matchValid = context -> new Document("$match",
		// new Document("$expr", new Document("$lt", Arrays.asList("$orgin.route",
		// "$destination.route"))));
		final LookupOperation lookupShip = LookupOperation.newLookup().from("m_ship").localField("shipId")
				.foreignField("shipId").as("ship");
		final LookupOperation lookupShippingCompany = LookupOperation.newLookup().from("m_shppng_cmpny")
				.localField("shippingCompanyNo").foreignField("shippingCompanyNo").as("shippingCompany");
		final ProjectionOperation project = Aggregation.project().andInclude("voyageNo", "scheduleId")
				.and("ship.shipId").as("shipId").and("ship.name").as("shipName")
				.and("shippingCompany.shippingCompanyNo").as("shippingCompanyNo").and("shippingCompany.name")
				.as("shippingCompanyName").and("destination.date").as("eta");
		final Aggregation aggregation = Aggregation.newAggregation(matchOperation, addDestination, lookupShip,
				Aggregation.unwind("ship", true), lookupShippingCompany, Aggregation.unwind("shippingCompany", true),
				project);
		final AggregationResults<VessalDto> result = mongoTemplate.aggregate(aggregation, "t_shpmnt_schdl",
				VessalDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<VessalDto> findVessalsAndVoyageNo() {
		final LookupOperation lookupShip = LookupOperation.newLookup().from("m_ship").localField("shipId")
				.foreignField("shipId").as("ship");
		final LookupOperation lookupShippingCompany = LookupOperation.newLookup().from("m_shppng_cmpny")
				.localField("shippingCompanyNo").foreignField("shippingCompanyNo").as("shippingCompany");
		final ProjectionOperation project = Aggregation.project().andInclude("voyageNo", "scheduleId")
				.and("ship.shipId").as("shipId").and("ship.name").as("shipName")
				.and("shippingCompany.shippingCompanyNo").as("shippingCompanyNo").and("shippingCompany.name")
				.as("shippingCompanyName");
		final Aggregation aggregation = Aggregation.newAggregation(lookupShip, Aggregation.unwind("ship", true),
				lookupShippingCompany, Aggregation.unwind("shippingCompany", true), project);
		final AggregationResults<VessalDto> result = mongoTemplate.aggregate(aggregation, TShipmentSchedule.class,
				VessalDto.class);
		return result.getMappedResults();
	}

	@Override
	public boolean isExistsByVoyageNo(String shipId, String voyageno, String shippingCompanyNo, Integer deleteFlag) {
		Criteria criteria = new Criteria().andOperator(Criteria.where("shipId").is(shipId),
				Criteria.where("voyageNo").regex("(?sim)^" + voyageno + "$"),
				Criteria.where("shippingCompanyNo").is(shippingCompanyNo), Criteria.where("deleteFlag").is(deleteFlag));
		return mongoTemplate.exists(Query.query(criteria), TShipmentSchedule.class);
	}

	@Override
	public boolean isExistsByScheduleIdAndVoyageno(String shippingNo, String shipId, String voyageno,
			Integer deleteFlag) {
		Criteria criteria = new Criteria().andOperator(Criteria.where("shipId").is(shipId),
				Criteria.where("voyageNo").regex("(?sim)^" + voyageno + "$"),
				Criteria.where("shippingCompanyNo").is(shippingNo), Criteria.where("deleteFlag").is(deleteFlag));
		return mongoTemplate.exists(Query.query(criteria), TShipmentSchedule.class);
	}

	@Override
	public List<VessalDto> findAllScheduleById(String[] shipmentRequestIds) {

		final MatchOperation matchOperation = Aggregation
				.match(new Criteria().andOperator(Criteria.where("shipmentRequestId").in(shipmentRequestIds)));
		final LookupOperation lookupSchedule = LookupOperation.newLookup().from("t_shpmnt_schdl")
				.localField("scheduleId").foreignField("scheduleId").as("schedule");
		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");
		final LookupOperation lookupLocation = LookupOperation.newLookup().from("m_lctn")
				.localField("stock.lastTransportLocation").foreignField("code").as("location");
		final LookupOperation lookupShip = LookupOperation.newLookup().from("m_ship").localField("schedule.shipId")
				.foreignField("shipId").as("ship");
		final LookupOperation lookupShippingCompany = LookupOperation.newLookup().from("m_shppng_cmpny")
				.localField("schedule.shippingCompanyNo").foreignField("shippingCompanyNo").as("shippingCompany");

		final AggregationOperation etd = (context) -> new Document("$addFields", new Document("etd", new Document(
				"$filter",
				new Document("input", "$schedule.schedule").append("as", "result").append("cond", new Document("$and",
						Arrays.asList(
								new Document("$eq", Arrays.asList("$$result.portName", "$location.shipmentOriginPort")),
								new Document("$eq", Arrays.asList("$$result.portFlag", "loading"))))))));

		final ProjectionOperation project = Aggregation.project().andInclude("scheduleId").and("schedule.voyageNo")
				.as("voyageNo").and("ship.shipId").as("shipId").and("ship.name").as("shipName")
				.and("shippingCompany.shippingCompanyNo").as("shippingCompanyNo").and("shippingCompany.name")
				.as("shippingCompanyName").and("etd.date").as("etd");
		final Aggregation aggregation = Aggregation.newAggregation(matchOperation, lookupSchedule,
				Aggregation.unwind("schedule", true), lookupStock, Aggregation.unwind("stock", true), lookupLocation,
				Aggregation.unwind("location", true), lookupShip, Aggregation.unwind("ship", true),
				lookupShippingCompany, Aggregation.unwind("shippingCompany", true), etd,
				Aggregation.unwind("etd", true), project);
		final AggregationResults<VessalDto> result = mongoTemplate.aggregate(aggregation, "t_shppng_rqust",
				VessalDto.class);
		return result.getMappedResults();
	}

	@Override
	public VessalDto findOneVesselDetailsByShippmentRequestId(String shipmentRequestId) {
		final MatchOperation matchOperation = Aggregation
				.match(Criteria.where("shipmentRequestId").is(shipmentRequestId));
		final LookupOperation lookupSchedule = LookupOperation.newLookup().from("t_shpmnt_schdl")
				.localField("scheduleId").foreignField("scheduleId").as("schedule");
		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");
		final LookupOperation lookupLocation = LookupOperation.newLookup().from("m_lctn")
				.localField("stock.lastTransportLocation").foreignField("code").as("location");
		final LookupOperation lookupShip = LookupOperation.newLookup().from("m_ship").localField("schedule.shipId")
				.foreignField("shipId").as("ship");
		final LookupOperation lookupShippingCompany = LookupOperation.newLookup().from("m_shppng_cmpny")
				.localField("schedule.shippingCompanyNo").foreignField("shippingCompanyNo").as("shippingCompany");

		final AggregationOperation etd = (context) -> new Document("$addFields", new Document("etd", new Document(
				"$filter",
				new Document("input", "$schedule.schedule").append("as", "result").append("cond", new Document("$and",
						Arrays.asList(
								new Document("$eq", Arrays.asList("$$result.portName", "$location.shipmentOriginPort")),
								new Document("$eq", Arrays.asList("$$result.portFlag", "loading"))))))));

		final ProjectionOperation project = Aggregation.project().andInclude("scheduleId").and("schedule.voyageNo")
				.as("voyageNo").and("ship.shipId").as("shipId").and("ship.name").as("shipName")
				.and("shippingCompany.shippingCompanyNo").as("shippingCompanyNo").and("shippingCompany.name")
				.as("shippingCompanyName").and("etd.date").as("etd").and("location.shipmentOriginPort").as("orginPort")
				.and("location.shipmentOriginCountry").as("orginCountry").and("destCountry").as("destinationCountry")
				.and("destPort").as("destinationPort");
		final Aggregation aggregation = Aggregation.newAggregation(matchOperation, lookupSchedule,
				Aggregation.unwind("schedule", true), lookupStock, Aggregation.unwind("stock", true), lookupLocation,
				Aggregation.unwind("location", true), lookupShip, Aggregation.unwind("ship", true),
				lookupShippingCompany, Aggregation.unwind("shippingCompany", true), etd,
				Aggregation.unwind("etd", true), project);
		final AggregationResults<VessalDto> result = mongoTemplate.aggregate(aggregation, "t_shppng_rqust",
				VessalDto.class);
		return result.getUniqueMappedResult();
	}

}
