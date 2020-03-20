package com.nexware.aajapan.repositories.custom.impl;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AccumulatorOperators;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import com.mongodb.BasicDBObject;
import com.nexware.aajapan.dto.SalesSumaryResultDto;
import com.nexware.aajapan.dto.SalesSummaryDto;
import com.nexware.aajapan.models.TInventoryCost;
import com.nexware.aajapan.repositories.custom.TInventoryCostRepositoryCustom;

public class TInventoryCostRepositoryCustomImpl implements TInventoryCostRepositoryCustom {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<TInventoryCost> getInventoryAmountsForStock(String stockNo) {
		MatchOperation match = Aggregation.match(Criteria.where("stockNo").is(stockNo));
		AggregationOperation groupOperation = context -> new Document("$group",
				new Document("_id", "$type").append("invoiceNo", new Document("$first", "$invoiceNo"))
						.append("stockNo", new Document("$first", "$stockNo"))
						.append("amount", new Document("$sum", "$amount"))
						.append("type", new Document("$first", "$type")));

		Aggregation aggregation = Aggregation.newAggregation(match, groupOperation);

		AggregationResults<TInventoryCost> result = this.mongoTemplate.aggregate(aggregation, "t_invtry_cst",
				TInventoryCost.class);

		return result.getMappedResults();
	}

	@Override
	public List<SalesSumaryResultDto> getInventoryByStockAndType(String stockNo) {
		MatchOperation match = Aggregation.match(Criteria.where("stockNo").is(stockNo));

//		GroupOperation groupStock = Aggregation.group("$stockNo").first("$stockNo").as("stockNo");

//		final LookupOperation lookupPurchase = LookupOperation.newLookup().from("t_invtry_cst").localField("stockNo")
//				.foreignField("stockNo").as("inventoryCost");
		

		GroupOperation group = Aggregation.group("$stockNo","$type")
				.push(new BasicDBObject("amount", "$amount"))
				.as("items").first("type").as("type");

		ProjectionOperation project = Aggregation.project().andInclude("stockNo").and("items.amount")
				.as("amount").and("type").as("type");

		Aggregation aggregation = Aggregation.newAggregation(match, group, project);

		AggregationResults<SalesSumaryResultDto> result = this.mongoTemplate.aggregate(aggregation, "t_invtry_cst",
				SalesSumaryResultDto.class);

		return result.getMappedResults();
	}

}
