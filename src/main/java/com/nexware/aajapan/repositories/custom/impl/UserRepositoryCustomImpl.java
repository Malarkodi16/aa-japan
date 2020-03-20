package com.nexware.aajapan.repositories.custom.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.nexware.aajapan.dto.MUserDto;
import com.nexware.aajapan.dto.MUserInfoDto;
import com.nexware.aajapan.models.MUser;
import com.nexware.aajapan.repositories.custom.UserRepositoryCustom;

@Service
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

	@Autowired
	private MongoOperations mongoTemplate;

	@Override
	public List<MUser> findAllByRole() {

		return mongoTemplate.find(Query.query(Criteria.where("role").in("SALES")), MUser.class);
	}

	@Override
	public List<MUserInfoDto> getListByDepartment(String department) {

		final MatchOperation match = Aggregation
				.match(new Criteria().andOperator(Criteria.where("department").is(department)));
		final ProjectionOperation project = Aggregation.project().andInclude("userId", "username", "fullname",
				"department");
		final Aggregation aggregation = Aggregation.newAggregation(match, project);
		final AggregationResults<MUserInfoDto> result = mongoTemplate.aggregate(aggregation, "m_lgn",
				MUserInfoDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<MUserDto> findAllUsers() {
		final LookupOperation lookupOperation = Aggregation.lookup("m_lgn", "code", "userId", "userInfo");
		final LookupOperation lookupReportTo = Aggregation.lookup("m_usr", "userInfo.reportTo", "code", "reportToInfo");
		final LookupOperation lookupLocation = Aggregation.lookup("m_office_lctn", "userInfo.location", "code",
				"locationInfo");
		final ProjectionOperation project = Aggregation.project("code", "fullname", "dob", "mobileno")
				.and("userInfo.department").as("department").and("userInfo.username").as("username")
				.and("userInfo.userId").as("userId").and("userInfo.role").as("role").and("reportToInfo.code")
				.as("reportTo").and("reportToInfo.fullname").as("reportToName").and("locationInfo.code").as("location")
				.and("locationInfo.location").as("locationName");
		final Aggregation aggregation = Aggregation.newAggregation(lookupOperation,
				Aggregation.unwind("lookupOperation", true), lookupReportTo, Aggregation.unwind("reportToInfo", true),
				lookupLocation, Aggregation.unwind("locationInfo", true), project);
		final AggregationResults<MUserDto> result = mongoTemplate.aggregate(aggregation, "m_usr", MUserDto.class);
		return result.getMappedResults();
	}

}
