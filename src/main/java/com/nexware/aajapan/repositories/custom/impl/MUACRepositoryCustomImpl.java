package com.nexware.aajapan.repositories.custom.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import com.nexware.aajapan.dto.MUACDto;
import com.nexware.aajapan.repositories.custom.MUACRepositoryCustom;

public class MUACRepositoryCustomImpl implements MUACRepositoryCustom {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<MUACDto> findAllByDepartment(int dept) {
		MatchOperation match = Aggregation.match(Criteria.where("department").is(dept));
		ProjectionOperation project = Aggregation.project().and("accessId").as("_id").and("accessId").as("id")
				.and("name").as("text").and("parentId").as("parentId");
		Aggregation aggregation = Aggregation.newAggregation(match, project);
		AggregationResults<MUACDto> result = this.mongoTemplate.aggregate(aggregation, "m_uac", MUACDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<MUACDto> findAllUAC() {
		ProjectionOperation project = Aggregation.project().and("accessId").as("_id").and("accessId").as("id")
				.and("name").as("text").and("parentId").as("parentId");
		Aggregation aggregation = Aggregation.newAggregation(project);
		AggregationResults<MUACDto> result = this.mongoTemplate.aggregate(aggregation, "m_uac", MUACDto.class);
		return result.getMappedResults();
	}
}
