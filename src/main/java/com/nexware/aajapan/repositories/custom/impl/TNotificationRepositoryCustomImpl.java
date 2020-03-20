package com.nexware.aajapan.repositories.custom.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import com.nexware.aajapan.dto.MLoginDto;
import com.nexware.aajapan.dto.TNotificationDto;
import com.nexware.aajapan.repositories.custom.TNotificationRepositoryCustom;
import com.nexware.aajapan.services.SecurityService;

public class TNotificationRepositoryCustomImpl implements TNotificationRepositoryCustom {
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private SecurityService securityService;

	@Override
	public List<TNotificationDto> getListOnLoad() {
		MLoginDto login = this.securityService.findLoggedInUser();
		MatchOperation match = Aggregation.match(Criteria.where("toUserCode").is(login.getUserId()));
		LookupOperation lookupFromUser = LookupOperation.newLookup().from("m_usr").localField("fromUserCode")
				.foreignField("code").as("fromUser");
		LookupOperation lookupToUser = LookupOperation.newLookup().from("m_usr").localField("toUserCode")
				.foreignField("code").as("toUser");

		ProjectionOperation project = Aggregation.project()
				.andInclude("id", "message", "fromUserCode", "toUserCode", "status", "actionUrl", "createdDate")
				.and("$fromUser.username").as("from").and("$fromUser.fullname").as("toUserFullname")
				.and("$toUser.username").as("to");

		Aggregation aggregation = Aggregation.newAggregation(match, lookupFromUser,
				Aggregation.unwind("$fromUser", true), lookupToUser, Aggregation.unwind("$toUser", true), project);

		AggregationResults<TNotificationDto> result = this.mongoTemplate.aggregate(aggregation, "t_ntfcn",
				TNotificationDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<TNotificationDto> getNotificationsByIds(List<String> ids) {

		MatchOperation match = Aggregation.match(Criteria.where("_id").in(ids));
		LookupOperation lookupFromUser = LookupOperation.newLookup().from("m_usr").localField("fromUserCode")
				.foreignField("code").as("fromUser");
		LookupOperation lookupToUser = LookupOperation.newLookup().from("m_usr").localField("toUserCode")
				.foreignField("code").as("toUser");
		ProjectionOperation project = Aggregation.project()
				.andInclude("id", "message", "fromUserCode", "toUserCode", "status", "actionUrl", "createdDate")
				.and("$fromUser.username").as("from").and("$fromUser.fullname").as("toUserFullname")
				.and("$toUser.username").as("to");

		Aggregation aggregation = Aggregation.newAggregation(match, lookupFromUser,
				Aggregation.unwind("$fromUser", true), lookupToUser, Aggregation.unwind("$toUser", true), project);

		AggregationResults<TNotificationDto> result = this.mongoTemplate.aggregate(aggregation, "t_ntfcn",
				TNotificationDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<TNotificationDto> getListBasedOnLogin(List<String> toUserCodes) {
		MatchOperation match = Aggregation.match(Criteria.where("toUserCode").in(toUserCodes));
		Aggregation aggregation = Aggregation.newAggregation(match);

		AggregationResults<TNotificationDto> result = this.mongoTemplate.aggregate(aggregation, "t_ntfcn",
				TNotificationDto.class);
		return result.getMappedResults();
	}

	@Override
	public TNotificationDto getById(String id) {
		MatchOperation match = Aggregation.match(Criteria.where("_id").is(id));
		LookupOperation lookupFromUser = LookupOperation.newLookup().from("m_usr").localField("fromUserCode")
				.foreignField("code").as("fromUser");
		LookupOperation lookupToUser = LookupOperation.newLookup().from("m_usr").localField("toUserCode")
				.foreignField("code").as("toUser");
		ProjectionOperation project = Aggregation.project()
				.andInclude("id", "message", "fromUserCode", "toUserCode", "status", "actionUrl", "createdDate")
				.and("$fromUser.username").as("from").and("$fromUser.fullname").as("toUserFullname")
				.and("$toUser.username").as("to");

		Aggregation aggregation = Aggregation.newAggregation(match, lookupFromUser,
				Aggregation.unwind("$fromUser", true), lookupToUser, Aggregation.unwind("$toUser", true), project);

		AggregationResults<TNotificationDto> result = this.mongoTemplate.aggregate(aggregation, "t_ntfcn",
				TNotificationDto.class);
		return result.getUniqueMappedResult();
	}
}
