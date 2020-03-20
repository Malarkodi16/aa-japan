package com.nexware.aajapan.repositories.custom.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.CountOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.client.result.UpdateResult;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.TTransportOrderInvoiceDto;
import com.nexware.aajapan.dto.TTransportOrderItemDto;
import com.nexware.aajapan.dto.TTransportOrderListDto;
import com.nexware.aajapan.models.TTransportOrderItem;
import com.nexware.aajapan.repositories.custom.TransporterOrderItemRepositoryCustom;
import com.nexware.aajapan.utils.AppUtil;

@Service
public class TransporterOrderItemRepositoryCustomImpl implements TransporterOrderItemRepositoryCustom {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public UpdateResult updateStatusById(String id, int status) {
		final Update update = new Update().set("status", status);
		return mongoTemplate.updateMulti(new Query(Criteria.where("id").is(id)), update, TTransportOrderItem.class);

	}

	@Override
	public UpdateResult updateStatusByOrderNo(String invoiceNo, int status) {
		final Update update = new Update().set("status", status);
		return mongoTemplate.updateMulti(new Query(Criteria.where("invoiceNo").is(invoiceNo)), update,
				TTransportOrderItem.class);
	}

	@Override
	public UpdateResult updateStatusByOrderNoAndStockNos(String invoiceNo, List<String> stockNos, int status) {
		final Update update = new Update().set("status", status);
		return mongoTemplate.updateMulti(new Query(new Criteria().andOperator(Criteria.where("invoiceNo").is(invoiceNo),
				Criteria.where("stockNo").in(stockNos))), update, TTransportOrderItem.class);
	}

	@Override
	public UpdateResult cancelItemById(String id, String reason) {
		final Update update = new Update().set("status", Constants.TRANSPORT_ITEM_CANCELED).set("reasonForCancel",
				reason);
		return mongoTemplate.updateMulti(new Query(Criteria.where("id").is(id)), update, TTransportOrderItem.class);

	}

	@Override
	public List<TTransportOrderListDto> findAllTransportOrders() {
//		final MatchOperation match = Aggregation.match(
//				Criteria.where("status").in(Constants.TRANSPORT_ITEM_INITIATED, Constants.TRANSPORT_ITEM_CONFIRMED));
		final LookupOperation lookupTransporter = LookupOperation.newLookup().from("m_trnsprtr")
				.localField("transporter").foreignField("code").as("transporter");
		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");
		final LookupOperation lookupSupplier = LookupOperation.newLookup().from("m_spplr")
				.localField("$stock.purchaseInfo.supplier").foreignField("supplierCode").as("supplierDetails");
		final MatchOperation matchStock = Aggregation.match(new Criteria().orOperator(
				new Criteria().andOperator(Criteria.where("stock.status").is(Constants.STOCK_STATUS_NEW),
						Criteria.where("status").is(Constants.TRANSPORT_ITEM_CONFIRMED)),
				Criteria.where("status").is(Constants.TRANSPORT_ITEM_INITIATED)));
		final LookupOperation pickupLocationDetails = LookupOperation.newLookup().from("m_lctn")
				.localField("pickupLocation").foreignField("code").as("pickupLocationDetails");
		final LookupOperation dropLocationDetails = LookupOperation.newLookup().from("m_lctn")
				.localField("dropLocation").foreignField("code").as("dropLocationDetails");

		final Map<String, Object> query = new LinkedHashMap<>();
		final Document groupBy = new Document("invoiceNo", "$invoiceNo").append("transporterCode", "$transporter._id")
				.append("supplierCode", "$supplierDetails.supplierCode")
				.append("yearMonthDay", new Document("$dateToString",
						new Document("format", "%Y-%m-%d").append("date", "$stock.purchaseInfo.date")));
		final Document pushOrderItem = new Document("$push", new BasicDBObject("invoiceId", "$_id")
				.append("invoiceNo", "$invoiceNo").append("stockNo", "$stockNo").append("dueDate", "$dueDate")
				.append("pickupDate", "$pickupDate").append("pickupTime", "$pickupTime")
				.append("deliveryDate", "$deliveryDate").append("deliveryTime", "$deliveryTime")
				.append("numberPlate", "$numberPlate").append("purchaseDate", "$stock.purchaseInfo.date")
				.append("destinationCountry", "$destinationCountry").append("destinationPort", "$destinationPort")
				.append("pickupLocation", "$pickupLocation").append("supplierName", "$supplierDetails.company")
				.append("supplierCode", "$supplierDetails.supplierCode")
				.append("auctionHouseId", "$auctionHouseDetails._id")
				.append("auctionHouse", "$auctionHouseDetails.auctionHouse")
				.append("auctionInfoPosNo", "$stock.purchaseInfo.auctionInfo.posNo")
				.append("posNos",
						new Document("$ifNull",
								Arrays.asList("$auctionHouseDetails.posNos", "$stock.purchaseInfo.auctionInfo.posNo")))
				.append("sPickupLocation", "$pickupLocationDetails.displayName")
				.append("pickupLocationCustom", "$pickupLocationCustom").append("dropLocation", "$dropLocation")
				.append("sDropLocation", "$dropLocationDetails.displayName").append("remarks", "$remarks")
				.append("comment", "$comment").append("dropLocationCustom", "$dropLocationCustom")
				.append("chassisNo", "$stock.chassisNo").append("transportationCount", "$stock.transportationCount")
				.append("finalDestination", "$stock.destinationCountry")
				.append("lotNo", new Document("$cond",
						new Document("if", new Document("$ne", Arrays.asList("$stock.transportationCount", 0)))
								.append("then", "$lotNo").append("else", "$stock.purchaseInfo.auctionInfo.lotNo")))
				.append("maker", "$stock.maker").append("model", "$stock.model").append("status", "$status")
				.append("stockStatus", "$stock.status").append("transporter", "$transporter.code")
				.append("createdDate", "$createdDate").append("charge", "$charge").append("etd", "$etd")
				.append("category", "$stock.category").append("subcategory", "$stock.subcategory")
				.append("scheduleType", "$scheduleType").append("forwarder", "$stock.forwarder")
				.append("transportCategory", "$stock.transportCategory").append("selectedDate", "$selectedDate"));
		query.put("_id", groupBy);
		query.put("orderItem", pushOrderItem);
		query.put("transporterCode", new Document("$first", "$transporter.code"));
		query.put("transporterName", new Document("$first", "$transporter.name"));
		query.put("createdDate", new Document("$first", "$createdDate"));
		query.put("invoiceNo", new Document("$first", "$invoiceNo"));
		query.put("purchaseDate", new Document("$first", "$stock.purchaseInfo.date"));
		query.put("sPickupLocation", new Document("$first", "$pickupLocationDetails.displayName"));
		query.put("pickupLocation", new Document("$first", "$pickupLocation"));
		query.put("sPickupLocationCustom", new Document("$first", "$pickupLocationCustom"));
		query.put("supplierName", new Document("$first", "$supplierDetails.company"));
		query.put("supplierCode", new Document("$first", "$supplierDetails.supplierCode"));

		final SortOperation sort = Aggregation.sort(Direction.DESC, "createdDate");

		final AggregationOperation addAuctionHouse = context -> new Document("$addFields", new Document(
				"auctionHouseDetails",
				new Document("$filter", new Document("input", "$supplierDetails.supplierLocations")
						.append("as", "result").append("cond", new Document("$eq",
								Arrays.asList("$$result._id", "$stock.purchaseInfo.auctionInfo.auctionHouse"))))));

		final AggregationOperation groupAggregationOperation = context -> new Document("$group", new Document(query));

		final Aggregation aggregation = Aggregation.newAggregation(lookupTransporter,
				Aggregation.unwind("$transporter", true), pickupLocationDetails,
				Aggregation.unwind("$pickupLocationDetails", true), dropLocationDetails,
				Aggregation.unwind("$dropLocationDetails", true), lookupStock, Aggregation.unwind("$stock", true),
				lookupSupplier, Aggregation.unwind("$supplierDetails", true), addAuctionHouse,
				Aggregation.unwind("$auctionHouseDetails", true), matchStock, groupAggregationOperation, sort);
		final AggregationResults<TTransportOrderListDto> result = mongoTemplate.aggregate(aggregation,
				"trnsprt_ordr_items", TTransportOrderListDto.class);
		return result.getMappedResults();
	}

	@Override
	public TTransportOrderListDto findOneTransportOrdersById(String invoiceId) {
		final MatchOperation match = Aggregation.match(Criteria.where("invoiceNo").is(invoiceId)
				.andOperator(Criteria.where("status").ne(Constants.TRANSPORT_ITEM_CANCELED)));
		final LookupOperation lookupTransporter = LookupOperation.newLookup().from("m_trnsprtr")
				.localField("transporter").foreignField("code").as("transporter");
		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");
		final LookupOperation pickupLocationDetails = LookupOperation.newLookup().from("m_lctn")
				.localField("pickupLocation").foreignField("code").as("pickupLocationDetails");
		final LookupOperation dropLocationDetails = LookupOperation.newLookup().from("m_lctn")
				.localField("dropLocation").foreignField("code").as("dropLocationDetails");
		final Map<String, Object> query = new LinkedHashMap<>();
		final Document groupBy = new Document("code", "$invoiceNo");
		final Document pushOrderItem = new Document("$push",
				new BasicDBObject("invoiceId", "$_id").append("invoiceNo", "$invoiceNo").append("stockNo", "$stockNo")
						.append("dueDate", "$dueDate").append("pickupDate", "$pickupDate")
						.append("pickupTime", "$pickupTime").append("deliveryDate", "$deliveryDate")
						.append("deliveryTime", "$deliveryTime").append("numberPlate", "$numberPlate")
						.append("destinationCountry", "$destinationCountry").append("pickupLocation", "$pickupLocation")
						.append("sPickupLocation", "$pickupLocationDetails.displayName")
						.append("pickupLocationCustom", "$pickupLocationCustom").append("dropLocation", "$dropLocation")
						.append("sDropLocation", "$dropLocationDetails.displayName")
						.append("dropLocationCustom", "$dropLocationCustom").append("chassisNo", "$stock.chassisNo")
						.append("status", "$status").append("transporter", "$transporter.code")
						.append("createdDate", "$createdDate").append("charge", "$charge").append("etd", "$etd")
						.append("chassisNo", "$stock.chassisNo"));
		query.put("_id", groupBy);
		query.put("orderItem", pushOrderItem);
		query.put("transporterCode", new Document("$first", "$transporter.code"));
		query.put("transporterName", new Document("$first", "$transporter.name"));
		query.put("createdDate", new Document("$first", "$createdDate"));
		query.put("invoiceNo", new Document("$first", "$invoiceNo"));

		final AggregationOperation groupAggregationOperation = context -> new Document("$group", new Document(query));

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupTransporter,
				Aggregation.unwind("$transporter", true), pickupLocationDetails,
				Aggregation.unwind("$pickupLocationDetails", true), dropLocationDetails,
				Aggregation.unwind("$dropLocationDetails", true), lookupStock, Aggregation.unwind("$stock", true),
				groupAggregationOperation);
		final AggregationResults<TTransportOrderListDto> result = mongoTemplate.aggregate(aggregation,
				"trnsprt_ordr_items", TTransportOrderListDto.class);
		return result.getUniqueMappedResult();
	}

	@Override
	public TTransportOrderInvoiceDto findOneTransportOrdersInvoiceByInvoiceID(String invoiceNo, Date purchaseDate,
			String transporter, List<Integer> matchTransStatus) {

		final MatchOperation match = Aggregation.match(Criteria.where("invoiceNo").is(invoiceNo).andOperator(
				Criteria.where("status").in(matchTransStatus), Criteria.where("transporter").is(transporter)));

		final LookupOperation lookupTransporter = LookupOperation.newLookup().from("m_trnsprtr")
				.localField("transporter").foreignField("code").as("transporter");
		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");

		final MatchOperation matchStock = Aggregation.match(Criteria.where("stock.purchaseInfo.date")
				.gte(AppUtil.atStartOfDay(purchaseDate)).lt(AppUtil.atEndOfDay(purchaseDate)));

		final LookupOperation lookupPickupLocation = LookupOperation.newLookup().from("m_lctn")
				.localField("pickupLocation").foreignField("code").as("pickupLocationDetails");
		final LookupOperation lookupDropLocation = LookupOperation.newLookup().from("m_lctn").localField("dropLocation")
				.foreignField("code").as("dropLocationDetails");
		final LookupOperation lookupPurchaseInvoice = LookupOperation.newLookup().from("t_prchs_invc")
				.localField("stockNo").foreignField("stockNo").as("purchsInvoice");

		final LookupOperation lookupSupplier = LookupOperation.newLookup().from("m_spplr")
				.localField("stock.purchaseInfo.supplier").foreignField("supplierCode").as("supplier_details");

		final AggregationOperation addAuctionHouse = contexts -> new Document("$addFields", new Document(
				"auctionHouseDetails",
				new Document("$filter", new Document("input", "$supplier_details.supplierLocations")
						.append("as", "result").append("cond", new Document("$eq",
								Arrays.asList("$$result._id", "$stock.purchaseInfo.auctionInfo.auctionHouse"))))));
		final Map<String, Object> query = new LinkedHashMap<>();

		final Document groupBy = new Document("invoiceNo", "$invoiceNo").append("transporterCode", "$transporter._id")
				.append("supplierCode", "$supplierDetails.supplierCode")
				.append("yearMonthDay", new Document("$dateToString",
						new Document("format", "%Y-%m-%d").append("date", "$stock.purchaseInfo.date")));

		final Document pushOrderItem = new Document("$push", new BasicDBObject("stockNo", "$stockNo")
				.append("chassisNo", "$stock.chassisNo").append("maker", "$stock.maker").append("model", "$stock.model")
				.append("lotNo", new Document("$cond",
						new Document("if", new Document("$ne", Arrays.asList("$stock.transportationCount", 0)))
								.append("then", "$lotNo").append("else", "$stock.purchaseInfo.auctionInfo.lotNo")))
				.append("posNo", "$stock.purchaseInfo.auctionInfo.posNo").append("chassisNo", "$stock.chassisNo")
				.append("numberPlate", "$stock.oldNumberPlate").append("auctionDate", "$stock.purchaseInfo.date")
				.append("company", "$supplier_details.company")
				.append("auctionHouse", "$auctionHouseDetails.auctionHouse").append("deliveryDate", "$deliveryDate")
				.append("destinationCountry", "$destinationCountry").append("pickupLocation", "$pickupLocation")
				.append("pickupLocationName", "$pickupLocationDetails.displayName")
				.append("pickupLocationCustom", "$pickupLocationCustom").append("dropLocation", "$dropLocation")
				.append("dropLocationName", "$dropLocationDetails.displayName")
				.append("dropLocationCustom", "$dropLocationCustom").append("remarks", "$remarks"));
		// scheduleType
		query.put("_id", groupBy);
		query.put("orderItem", pushOrderItem);
		query.put("transporterCode", new Document("$first", "$transporter.code"));
		query.put("transporterName", new Document("$first", "$transporter.name"));
		query.put("auctionDate", new Document("$first", "$stock.purchaseInfo.date"));
//		query.put("dueDate",
//				new Document("$first", new Document("$arrayElemAt", Arrays.asList("$purchsInvoice.dueDate", 0))));
		query.put("createdDate", new Document("$first", "$createdDate"));
		query.put("scheduleType", new Document("$first", "$scheduleType"));
		query.put("invoiceNo", new Document("$first", "$invoiceNo"));
		query.put("pickupDate", new Document("$first", "$pickupDate"));
		query.put("dueDate", new Document("$first", "$deliveryDate"));
		query.put("deliveryDate", new Document("$first", "$deliveryDate"));
		query.put("pickupTime", new Document("$first", "$pickupTime"));
		query.put("deliveryTime", new Document("$first", "$deliveryTime"));
		query.put("selectedDate", new Document("$first", "$selectedDate"));
		query.put("comment", new Document("$first", "$comment"));
		final AggregationOperation groupAggregationOperation = context -> new Document("$group", new Document(query));

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupTransporter,
				Aggregation.unwind("$transporter", true), lookupStock, Aggregation.unwind("$stock", true), matchStock,
				lookupPickupLocation, Aggregation.unwind("$pickupLocationDetails", true), lookupDropLocation,
				Aggregation.unwind("$dropLocationDetails", true), lookupPurchaseInvoice, lookupSupplier,
				Aggregation.unwind("$supplier_details", true), addAuctionHouse,
				Aggregation.unwind("$auctionHouseDetails", true), groupAggregationOperation);
		final AggregationResults<TTransportOrderInvoiceDto> result = mongoTemplate.aggregate(aggregation,
				"trnsprt_ordr_items", TTransportOrderInvoiceDto.class);
		return result.getUniqueMappedResult();
	}

	@Override
	public Long findTransportInitiatedCount() {

		final LookupOperation lookupTransporter = LookupOperation.newLookup().from("m_trnsprtr")
				.localField("transporter").foreignField("code").as("transporter");
		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");
		final LookupOperation lookupSupplier = LookupOperation.newLookup().from("m_spplr")
				.localField("$stock.purchaseInfo.supplier").foreignField("supplierCode").as("supplierDetails");
		final MatchOperation matchStock = Aggregation.match(new Criteria().orOperator(
				new Criteria().andOperator(Criteria.where("stock.status").is(Constants.STOCK_STATUS_NEW),
						Criteria.where("status").is(Constants.TRANSPORT_ITEM_CONFIRMED)),
				Criteria.where("status").is(Constants.TRANSPORT_ITEM_INITIATED)));

		final Map<String, Object> query = new LinkedHashMap<>();

		final Document groupBy = new Document("invoiceNo", "$invoiceNo").append("transporterCode", "$transporter._id")
				.append("supplierCode", "$supplierDetails.supplierCode")
				.append("yearMonthDay", new Document("$dateToString",
						new Document("format", "%Y-%m-%d").append("date", "$stock.purchaseInfo.date")));

		final Document pushOrderItem = new Document("$push", new BasicDBObject("status", "$status"));
		query.put("_id", groupBy);
		query.put("orderItem", pushOrderItem);

		final AggregationOperation groupAggregationOperation = context -> new Document("$group", new Document(query));

		final MatchOperation finalMatch = Aggregation
				.match(Criteria.where("orderItem.status").ne(Constants.TRANSPORT_ITEM_CONFIRMED));
		final CountOperation count = Aggregation.count().as("count");

		final Aggregation aggregation = Aggregation.newAggregation(lookupTransporter,
				Aggregation.unwind("$transporter", true), lookupStock, Aggregation.unwind("$stock", true),
				lookupSupplier, Aggregation.unwind("$supplierDetails", true), matchStock, groupAggregationOperation,
				finalMatch, count);
		final AggregationResults<Map> result = mongoTemplate.aggregate(aggregation, "trnsprt_ordr_items", Map.class);
		return Long.valueOf(!AppUtil.isObjectEmpty(result.getUniqueMappedResult())
				? result.getUniqueMappedResult().get("count").toString()
				: "0");
	}

	@Override
	public List<TTransportOrderListDto> findAllConfirmTransportOrders() {
		final MatchOperation match = Aggregation.match(Criteria.where("status").is(Constants.TRANSPORT_ITEM_CONFIRMED));
		final LookupOperation lookupTransporter = LookupOperation.newLookup().from("m_trnsprtr")
				.localField("transporter").foreignField("code").as("transporter");
		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");
		final LookupOperation lookupSupplier = LookupOperation.newLookup().from("m_spplr")
				.localField("$stock.purchaseInfo.supplier").foreignField("supplierCode").as("supplierDetails");
		final MatchOperation matchStock = Aggregation
				.match(new Criteria().andOperator(Criteria.where("stock.status").ne(Constants.STOCK_STATUS_NEW),
						Criteria.where("stock.transportInfo.status").is(Constants.TRANSPORT_ITEM_CONFIRMED)));
		final LookupOperation pickupLocationDetails = LookupOperation.newLookup().from("m_lctn")
				.localField("pickupLocation").foreignField("code").as("pickupLocationDetails");
		final LookupOperation dropLocationDetails = LookupOperation.newLookup().from("m_lctn")
				.localField("dropLocation").foreignField("code").as("dropLocationDetails");
		final Map<String, Object> query = new LinkedHashMap<>();
		final Document groupBy = new Document("invoiceNo", "$invoiceNo").append("transporterCode", "$transporter._id")
				.append("supplierCode", "$supplierDetails.supplierCode")
				.append("yearMonthDay", new Document("$dateToString",
						new Document("format", "%Y-%m-%d").append("date", "$stock.purchaseInfo.date")));
		final Document pushOrderItem = new Document("$push", new BasicDBObject("invoiceId", "$_id")
				.append("invoiceNo", "$invoiceNo").append("stockNo", "$stockNo").append("dueDate", "$dueDate")
				.append("pickupDate", "$pickupDate").append("pickupTime", "$pickupTime")
				.append("deliveryDate", "$deliveryDate").append("deliveryTime", "$deliveryTime")
				.append("numberPlate", "$numberPlate").append("destinationCountry", "$destinationCountry")
				.append("destinationPort", "$destinationPort").append("pickupLocation", "$pickupLocation")
				.append("sPickupLocation", "$pickupLocationDetails.displayName")
				.append("supplierName", "$supplierDetails.company")
				.append("supplierCode", "$supplierDetails.supplierCode")
				.append("auctionHouseId", "$auctionHouseDetails._id")
				.append("auctionHouse", "$auctionHouseDetails.auctionHouse")
				.append("auctionInfoPosNo", "$stock.purchaseInfo.auctionInfo.posNo")
				.append("posNos",
						new Document("$ifNull",
								Arrays.asList("$auctionHouseDetails.posNos", "$stock.purchaseInfo.auctionInfo.posNo")))
				.append("pickupLocationCustom", "$pickupLocationCustom").append("dropLocation", "$dropLocation")
				.append("sDropLocation", "$dropLocationDetails.displayName").append("remarks", "$remarks")
				.append("comment", "$comment").append("dropLocationCustom", "$dropLocationCustom")
				.append("chassisNo", "$stock.chassisNo").append("transportationCount", "$stock.transportationCount")
				.append("purchaseDate", "$stock.purchaseInfo.date")
				.append("finalDestination", "$stock.destinationCountry")
				.append("lotNo", new Document("$cond",
						new Document("if", new Document("$ne", Arrays.asList("$stock.transportationCount", 0)))
								.append("then", "$lotNo").append("else", "$stock.purchaseInfo.auctionInfo.lotNo")))
				.append("maker", "$stock.maker").append("model", "$stock.model").append("status", "$status")
				.append("stockStatus", "$stock.status").append("transporter", "$transporter.code")
				.append("createdDate", "$createdDate").append("charge", "$charge").append("etd", "$etd")
				.append("category", "$stock.category").append("subcategory", "$stock.subcategory")
				.append("scheduleType", "$scheduleType").append("forwarder", "$stock.forwarder")
				.append("transportCategory", "$stock.transportCategory").append("selectedDate", "$selectedDate"));
		query.put("_id", groupBy);
		query.put("orderItem", pushOrderItem);
		query.put("transporterCode", new Document("$first", "$transporter.code"));
		query.put("transporterName", new Document("$first", "$transporter.name"));
		query.put("createdDate", new Document("$first", "$createdDate"));
		query.put("invoiceNo", new Document("$first", "$invoiceNo"));
		query.put("purchaseDate", new Document("$first", "$stock.purchaseInfo.date"));
		query.put("sPickupLocation", new Document("$first", "$pickupLocationDetails.displayName"));
		query.put("pickupLocation", new Document("$first", "$pickupLocation"));
		query.put("sPickupLocationCustom", new Document("$first", "$pickupLocationCustom"));
		query.put("supplierCode", new Document("$first", "$supplierDetails.supplierCode"));
		query.put("supplierName", new Document("$first", "$supplierDetails.company"));

		final SortOperation sort = Aggregation.sort(Direction.DESC, "createdDate");

		final AggregationOperation addAuctionHouse = context -> new Document("$addFields", new Document(
				"auctionHouseDetails",
				new Document("$filter", new Document("input", "$supplierDetails.supplierLocations")
						.append("as", "result").append("cond", new Document("$eq",
								Arrays.asList("$$result._id", "$stock.purchaseInfo.auctionInfo.auctionHouse"))))));
		final AggregationOperation groupAggregationOperation = context -> new Document("$group", new Document(query));

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupTransporter,
				Aggregation.unwind("$transporter", true), pickupLocationDetails,
				Aggregation.unwind("$pickupLocationDetails", true), dropLocationDetails,
				Aggregation.unwind("$dropLocationDetails", true), lookupStock, Aggregation.unwind("$stock", true),
				lookupSupplier, Aggregation.unwind("$supplierDetails", true), addAuctionHouse,
				Aggregation.unwind("$auctionHouseDetails", true), matchStock, groupAggregationOperation, sort);
		final AggregationResults<TTransportOrderListDto> result = mongoTemplate.aggregate(aggregation,
				"trnsprt_ordr_items", TTransportOrderListDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<TTransportOrderListDto> findAllCompletedTransportOrders() {
		final MatchOperation match = Aggregation.match(
				new Criteria().andOperator(Criteria.where("status").is(Constants.TRANSPORT_ITEM_DELIVERY_CONFIRMED),
						Criteria.where("etd").gte(new Date())));
		final LookupOperation lookupTransporter = LookupOperation.newLookup().from("m_trnsprtr")
				.localField("transporter").foreignField("code").as("transporter");
		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");
		final LookupOperation lookupSupplier = LookupOperation.newLookup().from("m_spplr")
				.localField("$stock.purchaseInfo.supplier").foreignField("supplierCode").as("supplierDetails");
		final LookupOperation pickupLocationDetails = LookupOperation.newLookup().from("m_lctn")
				.localField("pickupLocation").foreignField("code").as("pickupLocationDetails");
		final LookupOperation dropLocationDetails = LookupOperation.newLookup().from("m_lctn")
				.localField("dropLocation").foreignField("code").as("dropLocationDetails");
		final Map<String, Object> query = new LinkedHashMap<>();
		final Document groupBy = new Document("invoiceNo", "$invoiceNo").append("transporterCode", "$transporter._id")
				.append("supplierCode", "$supplierDetails.supplierCode")
				.append("yearMonthDay", new Document("$dateToString",
						new Document("format", "%Y-%m-%d").append("date", "$stock.purchaseInfo.date")));
		final Document pushOrderItem = new Document("$push", new BasicDBObject("invoiceId", "$_id")
				.append("invoiceNo", "$invoiceNo").append("stockNo", "$stockNo").append("dueDate", "$dueDate")
				.append("pickupDate", "$pickupDate").append("pickupTime", "$pickupTime")
				.append("deliveryDate", "$deliveryDate").append("deliveryTime", "$deliveryTime")
				.append("numberPlate", "$numberPlate").append("destinationCountry", "$destinationCountry")
				.append("destinationPort", "$destinationPort").append("pickupLocation", "$pickupLocation")
				.append("sPickupLocation", "$pickupLocationDetails.displayName")
				.append("supplierName", "$supplierDetails.company")
				.append("supplierCode", "$supplierDetails.supplierCode")
				.append("auctionHouseId", "$auctionHouseDetails._id")
				.append("auctionHouse", "$auctionHouseDetails.auctionHouse")
				.append("auctionInfoPosNo", "$stock.purchaseInfo.auctionInfo.posNo")
				.append("posNos",
						new Document("$ifNull",
								Arrays.asList("$auctionHouseDetails.posNos", "$stock.purchaseInfo.auctionInfo.posNo")))
				// .append("purchaseDate", "$stock.purchaseInfo.date")
				.append("pickupLocationCustom", "$pickupLocationCustom").append("dropLocation", "$dropLocation")
				.append("sDropLocation", "$dropLocationDetails.displayName").append("remarks", "$remarks")
				.append("comment", "$comment").append("dropLocationCustom", "$dropLocationCustom")
				.append("chassisNo", "$stock.chassisNo").append("transportationCount", "$stock.transportationCount")
				.append("purchaseDate", "$stock.purchaseInfo.date")
				.append("finalDestination", "$stock.destinationCountry")
				.append("lotNo", new Document("$cond",
						new Document("if", new Document("$ne", Arrays.asList("$stock.transportationCount", 0)))
								.append("then", "$lotNo").append("else", "$stock.purchaseInfo.auctionInfo.lotNo")))
				.append("maker", "$stock.maker").append("model", "$stock.model").append("status", "$status")
				.append("stockStatus", "$stock.status").append("transporter", "$transporter.code")
				.append("createdDate", "$createdDate").append("charge", "$charge").append("etd", "$etd")
				.append("category", "$stock.category").append("subcategory", "$stock.subcategory")
				.append("scheduleType", "$scheduleType").append("forwarder", "$stock.forwarder")
				.append("transportCategory", "$stock.transportCategory").append("selectedDate", "$selectedDate"));
		query.put("_id", groupBy);
		query.put("orderItem", pushOrderItem);
		query.put("transporterCode", new Document("$first", "$transporter.code"));
		query.put("transporterName", new Document("$first", "$transporter.name"));
		query.put("createdDate", new Document("$first", "$createdDate"));
		query.put("invoiceNo", new Document("$first", "$invoiceNo"));
		query.put("purchaseDate", new Document("$first", "$stock.purchaseInfo.date"));
		query.put("sPickupLocation", new Document("$first", "$pickupLocationDetails.displayName"));
		query.put("pickupLocation", new Document("$first", "$pickupLocation"));
		query.put("sPickupLocationCustom", new Document("$first", "$pickupLocationCustom"));
		query.put("supplierCode", new Document("$first", "$supplierDetails.supplierCode"));
		query.put("supplierName", new Document("$first", "$supplierDetails.company"));

		final SortOperation sort = Aggregation.sort(Direction.DESC, "createdDate");
		final AggregationOperation addAuctionHouse = context -> new Document("$addFields", new Document(
				"auctionHouseDetails",
				new Document("$filter", new Document("input", "$supplierDetails.supplierLocations")
						.append("as", "result").append("cond", new Document("$eq",
								Arrays.asList("$$result._id", "$stock.purchaseInfo.auctionInfo.auctionHouse"))))));
		final AggregationOperation groupAggregationOperation = context -> new Document("$group", new Document(query));

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupTransporter,
				Aggregation.unwind("$transporter", true), pickupLocationDetails,
				Aggregation.unwind("$pickupLocationDetails", true), dropLocationDetails,
				Aggregation.unwind("$dropLocationDetails", true), lookupStock, Aggregation.unwind("$stock", true),
				lookupSupplier, Aggregation.unwind("$supplierDetails", true), addAuctionHouse,
				Aggregation.unwind("$auctionHouseDetails", true), groupAggregationOperation, sort);
		final AggregationResults<TTransportOrderListDto> result = mongoTemplate.aggregate(aggregation,
				"trnsprt_ordr_items", TTransportOrderListDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<TTransportOrderListDto> findAllDeliveredTransportOrders() {
		final MatchOperation match = Aggregation.match(Criteria.where("status").is(Constants.TRANSPORT_ITEM_DELIVERED));
		final LookupOperation lookupTransporter = LookupOperation.newLookup().from("m_trnsprtr")
				.localField("transporter").foreignField("code").as("transporter");
		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");
		final LookupOperation lookupSupplier = LookupOperation.newLookup().from("m_spplr")
				.localField("$stock.purchaseInfo.supplier").foreignField("supplierCode").as("supplierDetails");
		final LookupOperation pickupLocationDetails = LookupOperation.newLookup().from("m_lctn")
				.localField("pickupLocation").foreignField("code").as("pickupLocationDetails");
		final LookupOperation dropLocationDetails = LookupOperation.newLookup().from("m_lctn")
				.localField("dropLocation").foreignField("code").as("dropLocationDetails");
		final Map<String, Object> query = new LinkedHashMap<>();
		final Document groupBy = new Document("invoiceNo", "$invoiceNo").append("transporterCode", "$transporter._id")
				.append("supplierCode", "$supplierDetails.supplierCode")
				.append("yearMonthDay", new Document("$dateToString",
						new Document("format", "%Y-%m-%d").append("date", "$stock.purchaseInfo.date")));
		final Document pushOrderItem = new Document("$push", new BasicDBObject("invoiceId", "$_id")
				.append("invoiceNo", "$invoiceNo").append("stockNo", "$stockNo").append("dueDate", "$dueDate")
				.append("pickupDate", "$pickupDate").append("pickupTime", "$pickupTime")
				.append("deliveryDate", "$deliveryDate").append("deliveryTime", "$deliveryTime")
				.append("numberPlate", "$numberPlate").append("destinationCountry", "$destinationCountry")
				.append("pickupLocation", "$pickupLocation")
				.append("sPickupLocation", "$pickupLocationDetails.displayName")
				.append("supplierName", "$supplierDetails.company")
				.append("supplierCode", "$supplierDetails.supplierCode")
				.append("auctionHouseId", "$auctionHouseDetails._id")
				.append("auctionHouse", "$auctionHouseDetails.auctionHouse")
				.append("auctionInfoPosNo", "$stock.purchaseInfo.auctionInfo.posNo")
				.append("posNos",
						new Document("$ifNull",
								Arrays.asList("$auctionHouseDetails.posNos", "$stock.purchaseInfo.auctionInfo.posNo")))
				.append("purchaseDate", "$stock.purchaseInfo.date")
				.append("pickupLocationCustom", "$pickupLocationCustom").append("dropLocation", "$dropLocation")
				.append("sDropLocation", "$dropLocationDetails.displayName").append("remarks", "$remarks")
				.append("dropLocationCustom", "$dropLocationCustom").append("chassisNo", "$stock.chassisNo")
				.append("finalDestination", "$stock.destinationCountry")
				.append("lotNo", new Document("$cond",
						new Document("if", new Document("$ne", Arrays.asList("$stock.transportationCount", 0)))
								.append("then", "$lotNo").append("else", "$stock.purchaseInfo.auctionInfo.lotNo")))
				.append("maker", "$stock.maker").append("model", "$stock.model").append("status", "$status")
				.append("stockStatus", "$stock.status").append("transporter", "$transporter.code")
				.append("createdDate", "$createdDate").append("charge", "$charge").append("etd", "$etd")
				.append("chassisNo", "$stock.chassisNo"));
		query.put("_id", groupBy);
		query.put("orderItem", pushOrderItem);
		query.put("transporterCode", new Document("$first", "$transporter.code"));
		query.put("transporterName", new Document("$first", "$transporter.name"));
		query.put("createdDate", new Document("$first", "$createdDate"));
		query.put("invoiceNo", new Document("$first", "$invoiceNo"));
		query.put("purchaseDate", new Document("$first", "$stock.purchaseInfo.date"));
		query.put("sPickupLocation", new Document("$first", "$pickupLocationDetails.displayName"));
		query.put("pickupLocation", new Document("$first", "$pickupLocation"));
		query.put("sPickupLocationCustom", new Document("$first", "$pickupLocationCustom"));
		query.put("supplierCode", new Document("$first", "$supplierDetails.supplierCode"));
		query.put("supplierName", new Document("$first", "$supplierDetails.company"));

		final SortOperation sort = Aggregation.sort(Direction.DESC, "createdDate");
		final AggregationOperation addAuctionHouse = context -> new Document("$addFields", new Document(
				"auctionHouseDetails",
				new Document("$filter", new Document("input", "$supplierDetails.supplierLocations")
						.append("as", "result").append("cond", new Document("$eq",
								Arrays.asList("$$result._id", "$stock.purchaseInfo.auctionInfo.auctionHouse"))))));
		final AggregationOperation groupAggregationOperation = context -> new Document("$group", new Document(query));

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupTransporter,
				Aggregation.unwind("$transporter", true), pickupLocationDetails,
				Aggregation.unwind("$pickupLocationDetails", true), dropLocationDetails,
				Aggregation.unwind("$dropLocationDetails", true), lookupStock, Aggregation.unwind("$stock", true),
				lookupSupplier, Aggregation.unwind("$supplierDetails", true), addAuctionHouse,
				Aggregation.unwind("$auctionHouseDetails", true), groupAggregationOperation, sort);
		final AggregationResults<TTransportOrderListDto> result = mongoTemplate.aggregate(aggregation,
				"trnsprt_ordr_items", TTransportOrderListDto.class);
		return result.getMappedResults();
	}

	@Override
	public TTransportOrderInvoiceDto findOneTransportOrdersInvoiceByInvoiceID(String[] ids) {
		final MatchOperation match = Aggregation.match(Criteria.where("_id").in(ids));
		final LookupOperation lookupTransporter = LookupOperation.newLookup().from("m_trnsprtr")
				.localField("transporter").foreignField("code").as("transporter");
		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");

		final LookupOperation lookupPickupLocation = LookupOperation.newLookup().from("m_lctn")
				.localField("pickupLocation").foreignField("code").as("pickupLocationDetails");
		final LookupOperation lookupDropLocation = LookupOperation.newLookup().from("m_lctn").localField("dropLocation")
				.foreignField("code").as("dropLocationDetails");
		final LookupOperation lookupPurchaseInvoice = LookupOperation.newLookup().from("t_prchs_invc")
				.localField("stockNo").foreignField("stockNo").as("purchsInvoice");

		final LookupOperation lookupSupplier = LookupOperation.newLookup().from("m_spplr")
				.localField("stock.purchaseInfo.supplier").foreignField("supplierCode").as("supplier_details");

		final AggregationOperation addAuctionHouse = contexts -> new Document("$addFields", new Document(
				"auctionHouseDetails",
				new Document("$filter", new Document("input", "$supplier_details.supplierLocations")
						.append("as", "result").append("cond", new Document("$eq",
								Arrays.asList("$$result._id", "$stock.purchaseInfo.auctionInfo.auctionHouse"))))));

		final Map<String, Object> query = new LinkedHashMap<>();

		final Document groupBy = new Document("transporterCode", "$transporter");

		final Document pushOrderItem = new Document("$push", new BasicDBObject("stockNo", "$stockNo")
				.append("chassisNo", "$stock.chassisNo").append("maker", "$stock.maker").append("model", "$stock.model")
				.append("lotNo", "$stock.purchaseInfo.auctionInfo.lotNo")
				.append("posNo", "$stock.purchaseInfo.auctionInfo.posNo").append("chassisNo", "$stock.chassisNo")
				.append("numberPlate", "$stock.oldNumberPlate").append("auctionDate", "$stock.purchaseInfo.date")
				.append("company", "$supplier_details.company")
				.append("auctionHouse", "$auctionHouseDetails.auctionHouse")
				.append("destinationCountry", "$destinationCountry").append("pickupLocation", "$pickupLocation")
				.append("pickupLocationName", "$pickupLocationDetails.displayName")
				.append("pickupLocationCustom", "$pickupLocationCustom").append("dropLocation", "$dropLocation")
				.append("dropLocationName", "$dropLocationDetails.displayName")
				.append("dropLocationCustom", "$dropLocationCustom").append("remarks", "$remarks"));
		// scheduleType
		query.put("_id", groupBy);
		query.put("orderItem", pushOrderItem);
		query.put("transporterCode", new Document("$first", "$transporter.code"));
		query.put("transporterName", new Document("$first", "$transporter.name"));
		query.put("auctionDate", new Document("$first", "$stock.purchaseInfo.date"));
		query.put("dueDate",
				new Document("$first", new Document("$arrayElemAt", Arrays.asList("$purchsInvoice.dueDate", 0))));
		query.put("createdDate", new Document("$first", "$createdDate"));
		query.put("scheduleType", new Document("$first", "$scheduleType"));
		query.put("invoiceNo", new Document("$first", "$invoiceNo"));
		query.put("pickupDate", new Document("$first", "$pickupDate"));
		query.put("deliveryDate", new Document("$first", "$deliveryDate"));
		query.put("pickupTime", new Document("$first", "$pickupTime"));
		query.put("deliveryTime", new Document("$first", "$deliveryTime"));
		query.put("selectedDate", new Document("$first", "$selectedDate"));
		query.put("comment", new Document("$addToSet", new Document("$concat", "$comment")));
		final AggregationOperation groupAggregationOperation = context -> new Document("$group", new Document(query));

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupTransporter,
				Aggregation.unwind("$transporter", true), lookupStock, Aggregation.unwind("$stock", true),
				lookupPickupLocation, Aggregation.unwind("$pickupLocationDetails", true), lookupDropLocation,
				Aggregation.unwind("$dropLocationDetails", true), lookupPurchaseInvoice, lookupSupplier,
				Aggregation.unwind("$supplier_details", true), addAuctionHouse,
				Aggregation.unwind("$auctionHouseDetails", true), groupAggregationOperation);
		final AggregationResults<TTransportOrderInvoiceDto> result = mongoTemplate.aggregate(aggregation,
				"trnsprt_ordr_items", TTransportOrderInvoiceDto.class);
		return result.getUniqueMappedResult();
	}

	@Override
	public List<TTransportOrderItemDto> findAllDeliveryConfirmedTransportOrders() {
		final MatchOperation match = Aggregation.match(
				new Criteria().andOperator(Criteria.where("status").is(Constants.TRANSPORT_ITEM_DELIVERY_CONFIRMED),
						Criteria.where("etd").lt(new Date())));
		final LookupOperation lookupTransporter = LookupOperation.newLookup().from("m_trnsprtr")
				.localField("transporter").foreignField("code").as("transporter");
		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");
		final LookupOperation lookupSupplier = LookupOperation.newLookup().from("m_spplr")
				.localField("$stock.purchaseInfo.supplier").foreignField("supplierCode").as("supplierDetails");
		final LookupOperation pickupLocationDetails = LookupOperation.newLookup().from("m_lctn")
				.localField("pickupLocation").foreignField("code").as("pickupLocationDetails");
		final LookupOperation dropLocationDetails = LookupOperation.newLookup().from("m_lctn")
				.localField("dropLocation").foreignField("code").as("dropLocationDetails");

		final SortOperation sort = Aggregation.sort(Direction.DESC, "createdDate");
		final AggregationOperation addAuctionHouse = context -> new Document("$addFields", new Document(
				"auctionHouseDetails",
				new Document("$filter", new Document("input", "$supplierDetails.supplierLocations")
						.append("as", "result").append("cond", new Document("$eq",
								Arrays.asList("$$result._id", "$stock.purchaseInfo.auctionInfo.auctionHouse"))))));
		ProjectionOperation projectionOperation = Aggregation
				.project("invoiceNo", "stockNo", "dueDate", "pickupDate", "pickupTime", "deliveryDate", "deliveryTime",
						"numberPlate", "destinationCountry", "destinationPort", "pickupLocation",
						"pickupLocationCustom", "dropLocation", "remarks", "comment", "dropLocationCustom")
				.and("$pickupLocationDetails.displayName").as("sPickupLocation").and("$supplierDetails.company")
				.as("supplierName").and("$supplierDetails.supplierCode").as("supplierCode")
				.and("$auctionHouseDetails.auctionHouse").as("auctionHouse")
				.and("$stock.purchaseInfo.auctionInfo.posNo").as("auctionInfoPosNo")
				.and("$dropLocationDetails.displayName").as("sDropLocation").and("$stock.chassisNo").as("chassisNo")
				.and("$stock.transportationCount").as("transportationCount").and("$stock.purchaseInfo.date")
				.as("purchaseDate").and("$stock.destinationCountry").as("finalDestination")
				.and("$stock.purchaseInfo.auctionInfo.lotNo").as("lotNo").and("$stock.maker").as("maker")
				.and("$stock.model").as("model").and("$status").as("status").and("$stock.status").as("stockStatus")
				.and("$transporter.code").as("transporter").and("$createdDate").as("createdDate").and("$charge")
				.as("charge").and("$etd").as("etd").and("$stock.category").as("category").and("$stock.subcategory")
				.as("subcategory").and("$scheduleType").as("scheduleType").and("$stock.forwarder").as("forwarder")
				.and("$stock.transportCategory").as("transportCategory").and("$selectedDate").as("selectedDate")
				.and("$transporter.code").as("transporterCode").and("$transporter.name").as("transporterName")
				.and("$createdDate").as("createdDate").and("$invoiceNo").as("invoiceNo").and("$stock.purchaseInfo.date")
				.as("purchaseDate").and("$pickupLocationDetails.displayName").as("sPickupLocation")
				.and("$pickupLocation").as("pickupLocation").and("$pickupLocationCustom").as("sPickupLocationCustom")
				.and("$supplierDetails.supplierCode").as("supplierCode").and("$supplierDetails.company")
				.as("supplierName");

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupTransporter,
				Aggregation.unwind("$transporter", true), pickupLocationDetails,
				Aggregation.unwind("$pickupLocationDetails", true), dropLocationDetails,
				Aggregation.unwind("$dropLocationDetails", true), lookupStock, Aggregation.unwind("$stock", true),
				lookupSupplier, Aggregation.unwind("$supplierDetails", true), addAuctionHouse,
				Aggregation.unwind("$auctionHouseDetails", true), projectionOperation, sort);
		final AggregationResults<TTransportOrderItemDto> result = mongoTemplate.aggregate(aggregation,
				"trnsprt_ordr_items", TTransportOrderItemDto.class);
		return result.getMappedResults();
	}

	@Override
	public Integer findCountOfTransportRequested() {

		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");

		final MatchOperation matchStock = Aggregation.match(new Criteria().orOperator(
				new Criteria().andOperator(Criteria.where("stock.status").is(Constants.STOCK_STATUS_NEW),
						Criteria.where("status").is(Constants.TRANSPORT_ITEM_CONFIRMED)),
				Criteria.where("status").is(Constants.TRANSPORT_ITEM_INITIATED)));
		CountOperation count = Aggregation.count().as("count");
		Aggregation aggregation = Aggregation.newAggregation(lookupStock, matchStock,
				Aggregation.unwind("$stock", true), count);
		AggregationResults<Document> result = this.mongoTemplate.aggregate(aggregation, "trnsprt_ordr_items",
				Document.class);
		return !AppUtil.isObjectEmpty(result.getUniqueMappedResult())
				? result.getUniqueMappedResult().getInteger("count")
				: 0;

	}

	@Override
	public Integer findCountOfTransportConfirmed() {
		final MatchOperation match = Aggregation.match(Criteria.where("status").is(Constants.TRANSPORT_ITEM_CONFIRMED));
		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");

		final MatchOperation matchStock = Aggregation
				.match(new Criteria().andOperator(Criteria.where("stock.status").ne(Constants.STOCK_STATUS_NEW),
						Criteria.where("stock.transportInfo.status").is(Constants.TRANSPORT_ITEM_CONFIRMED)));
		CountOperation count = Aggregation.count().as("count");
		Aggregation aggregation = Aggregation.newAggregation(match, lookupStock, Aggregation.unwind("$stock", true),
				matchStock, count);
		AggregationResults<Document> result = this.mongoTemplate.aggregate(aggregation, "trnsprt_ordr_items",
				Document.class);
		return !AppUtil.isObjectEmpty(result.getUniqueMappedResult())
				? result.getUniqueMappedResult().getInteger("count")
				: 0;
	}
}
