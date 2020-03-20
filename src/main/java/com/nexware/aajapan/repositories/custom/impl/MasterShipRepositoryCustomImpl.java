package com.nexware.aajapan.repositories.custom.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.BasicDBObject;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.MShipDto;
import com.nexware.aajapan.models.MAuctionGradesExterior;
import com.nexware.aajapan.models.MShip;
import com.nexware.aajapan.models.MShippingCompany;
import com.nexware.aajapan.repositories.custom.MasterShipRepositoryCustom;

public class MasterShipRepositoryCustomImpl implements MasterShipRepositoryCustom {
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private MongoOperations mongoOperations;

	@Override
	public List<MShipDto> getShipsWithCompanyName() {

		GroupOperation groupOperation = Aggregation.group("shippingCompanyNo")
				.push(new BasicDBObject("name", "$name").append("shipId", "$shipId")).as("items")
				.first("shippingCompanyName").as("shippingCompanyName").first("shippingCompanyNo")
				.as("shippingCompanyNo");
		Aggregation aggregation = Aggregation.newAggregation(groupOperation);

		AggregationResults<MShipDto> result = this.mongoTemplate.aggregate(aggregation, "m_ship", MShipDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<MShip> getAllUnDeletedShip() {
		MatchOperation match = Aggregation.match(Criteria.where("deleteFlag").is(Constants.DELETE_FLAG_0));
		Aggregation aggregation = Aggregation.newAggregation(match);
		AggregationResults<MShip> result = this.mongoTemplate.aggregate(aggregation, "m_ship",
				MShip.class);
		return result.getMappedResults();
	}

	@Override
	public boolean existsByShipIdAndName(String shipId, String name) {
		final Criteria criteria = new Criteria();
		criteria.andOperator(Criteria.where("name").is(name), Criteria.where("shipId").is(shipId));
		return mongoOperations.exists(Query.query(criteria), MShippingCompany.class);
	}

	@Override
	public boolean existsByName(String name) {
		final Criteria criteria = new Criteria();
		criteria.andOperator(Criteria.where("deleteFlag").is(Constants.DELETE_FLAG_0), Criteria.where("name").is(name));
		return mongoOperations.exists(Query.query(criteria), MShip.class);
	}

}
