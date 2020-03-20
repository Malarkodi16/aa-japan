package com.nexware.aajapan.repositories.custom.impl;

import java.util.Arrays;
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

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.StockDocumentsDto;
import com.nexware.aajapan.repositories.custom.TDocumentReceivedRepositoryCustom;
import com.nexware.aajapan.utils.AppUtil;

public class TDocumentReceivedRepositoryCustomImpl implements TDocumentReceivedRepositoryCustom {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<StockDocumentsDto> findAllDocumentReceivedList(Integer status) {
		Criteria criteria = new Criteria().andOperator(
				Criteria.where("documentStatus").is(Constants.STOCK_DOCUMENT_RECEIVED),
				Criteria.where("rikujiStatus").is(status));
		MatchOperation matchOperation = Aggregation.match(criteria);

		LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stockDetails");

		ProjectionOperation project = Aggregation.project()
				.andInclude("stockNo", "documentType", "documentReceivedDate", "documentConvertedDate",
						"documentConvertTo", "createdDate")
				.and("stockDetails.chassisNo").as("chassisNo").and("stockDetails.firstRegDate").as("firstRegDate")
				.and("stockDetails.oldNumberPlate").as("oldNumberPlate").and("stockDetails.plateNoReceivedDate")
				.as("plateNoReceivedDate").and("stockDetails.purchaseInfo.date").as("purchaseInfoDate")
				.and("stockDetails.purchaseInfo.type").as("purchaseType").and("stockDetails.purchaseInfo.supplier")
				.as("supplier").and("stockDetails.purchaseInfo.auctionInfo.lotNo").as("shuppinNo")
				.and("stockDetails.documentFob").as("documentFob").and("supplierDetails.supplierCode")
				.as("supplierCode").and("supplierDetails.company").as("supplierName").and("auctionHouse._id")
				.as("auctionHouseId").and("auctionHouse.auctionHouse").as("auctionHouse");

		LookupOperation lookupSupplier = LookupOperation.newLookup().from("m_spplr")
				.localField("stockDetails.purchaseInfo.supplier").foreignField("supplierCode").as("supplierDetails");
		AggregationOperation addAuctionHouse = context -> new Document("$addFields",
				new Document("auctionHouse",
						new Document("$filter",
								new Document("input", "$supplierDetails.supplierLocations").append("as", "result")
										.append("cond", new Document("$eq", Arrays.asList("$$result._id",
												"$stockDetails.purchaseInfo.auctionInfo.auctionHouse"))))));
		SortOperation sort = Aggregation.sort(Direction.DESC, "createdDate");
		Aggregation aggregation = Aggregation.newAggregation(matchOperation, lookupStock,
				Aggregation.unwind("$stockDetails", true), lookupSupplier, Aggregation.unwind("$supplierDetails", true),
				addAuctionHouse, Aggregation.unwind("$auctionHouse", true), project, sort);

		AggregationResults<StockDocumentsDto> result = this.mongoTemplate.aggregate(aggregation, "t_doc_recvd",
				StockDocumentsDto.class);
		return result.getMappedResults();
	}

	@Override
	public StockDocumentsDto findOneDocumentReceivedStockDetails(Integer status, String stockNo) {
		Criteria criteria = new Criteria().andOperator(
				Criteria.where("documentStatus").is(Constants.STOCK_DOCUMENT_RECEIVED),
				Criteria.where("rikujiStatus").is(status), Criteria.where("stockNo").is(stockNo));
		MatchOperation matchOperation = Aggregation.match(criteria);
		LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stockDetails");

		ProjectionOperation project = Aggregation.project()
				.andInclude("stockNo", "documentType", "documentReceivedDate", "documentConvertedDate",
						"documentConvertTo")
				.and("stockDetails.chassisNo").as("chassisNo").and("stockDetails.firstRegDate").as("firstRegDate")
				.and("stockDetails.oldNumberPlate").as("oldNumberPlate").and("stockDetails.plateNoReceivedDate")
				.as("plateNoReceivedDate").and("stockDetails.purchaseInfo.date").as("purchaseInfoDate")
				.and("stockDetails.purchaseInfo.type").as("purchaseType").and("stockDetails.documentFob")
				.as("documentFob").and("stockDetails.purchaseInfo.supplier").as("supplier")
				.and("stockDetails.purchaseInfo.auctionInfo.lotNo").as("shuppinNo").and("supplierDetails.supplierCode")
				.as("supplierCode").and("supplierDetails.company").as("supplierName").and("auctionHouse._id")
				.as("auctionHouseId").and("auctionHouse.auctionHouse").as("auctionHouse");

		LookupOperation lookupSupplier = LookupOperation.newLookup().from("m_spplr")
				.localField("stockDetails.purchaseInfo.supplier").foreignField("supplierCode").as("supplierDetails");
		AggregationOperation addAuctionHouse = context -> new Document("$addFields",
				new Document("auctionHouse",
						new Document("$filter",
								new Document("input", "$supplierDetails.supplierLocations").append("as", "result")
										.append("cond", new Document("$eq", Arrays.asList("$$result._id",
												"$stockDetails.purchaseInfo.auctionInfo.auctionHouse"))))));
		Aggregation aggregation = Aggregation.newAggregation(matchOperation, lookupStock,
				Aggregation.unwind("$stockDetails", true), lookupSupplier, Aggregation.unwind("$supplierDetails", true),
				addAuctionHouse, Aggregation.unwind("$auctionHouse", true), project);
		AggregationResults<StockDocumentsDto> result = this.mongoTemplate.aggregate(aggregation, "t_doc_recvd",
				StockDocumentsDto.class);
		return result.getUniqueMappedResult();
	}

	@Override
	public Long findAllByDocumentStatusReceivedCount() {
		Criteria criteria = new Criteria().andOperator(
				Criteria.where("documentStatus").is(Constants.STOCK_DOCUMENT_RECEIVED),
				Criteria.where("rikujiStatus").is(Constants.STOCK_RIKUJI_STATUS_0));
		MatchOperation match = Aggregation.match(criteria);
		CountOperation count = Aggregation.count().as("count");
		Aggregation aggregation = Aggregation.newAggregation(match, count);
		AggregationResults<Map> result = this.mongoTemplate.aggregate(aggregation, "t_doc_recvd", Map.class);
		return Long.valueOf(!AppUtil.isObjectEmpty(result.getUniqueMappedResult())
				? result.getUniqueMappedResult().get("count").toString()
				: "0");
	}

	@Override
	public Long findAllByDocumentStatusReceivedRikujiCount() {
		Criteria criteria = new Criteria().andOperator(
				Criteria.where("documentStatus").is(Constants.STOCK_DOCUMENT_RECEIVED),
				Criteria.where("rikujiStatus").is(Constants.STOCK_RIKUJI_STATUS_1));
		MatchOperation match = Aggregation.match(criteria);
		CountOperation count = Aggregation.count().as("count");
		Aggregation aggregation = Aggregation.newAggregation(match, count);
		AggregationResults<Map> result = this.mongoTemplate.aggregate(aggregation, "t_doc_recvd", Map.class);
		return Long.valueOf(!AppUtil.isObjectEmpty(result.getUniqueMappedResult())
				? result.getUniqueMappedResult().get("count").toString()
				: "0");
	}

}
