package com.nexware.aajapan.repositories.custom.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.client.result.UpdateResult;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.models.MLocation;
import com.nexware.aajapan.repositories.custom.MLocationRepositoryCustom;

public class MLocationRepositoryCustomImpl implements MLocationRepositoryCustom {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<MLocation> getListWithoutDelete() {
		MatchOperation match = Aggregation
				.match(Criteria.where("deleteFlag").ne(Constants.MLOCATION_DELETE_STATUS_AFTER));
		SortOperation sort = Aggregation.sort(Direction.DESC, "createdDate");
		Aggregation aggregation = Aggregation.newAggregation(match, sort);
		AggregationResults<MLocation> result = this.mongoTemplate.aggregate(aggregation, "m_lctn", MLocation.class);
		return result.getMappedResults(); 
	}

	@Override
	public UpdateResult updateById(String id, Update update) {

		return this.mongoTemplate.updateMulti(Query.query(Criteria.where("id").is(id)), update, MLocation.class);
	}

}
