package com.nexware.aajapan.repositories.custom.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationExpression;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.nexware.aajapan.dto.MLoginDto;
import com.nexware.aajapan.models.MLogin;
import com.nexware.aajapan.repositories.custom.LoginRepositoryCustom;

@Service
public class LoginRepositoryCustomImpl implements LoginRepositoryCustom {

	@Autowired
	private MongoOperations mongoTemplate;

	@Override
	public MLoginDto findOneByUsername(String username) {
		MatchOperation match = Aggregation.match(Criteria.where("username").is(username));
		LookupOperation lookupUser = LookupOperation.newLookup().from("m_usr").localField("userId").foreignField("code")
				.as("user");
		LookupOperation lookupDept = LookupOperation.newLookup().from("m_role").localField("department")
				.foreignField("department").as("roleDetails");
		AggregationExpression filterrole = context -> new Document("$filter", new Document("input", "$roleDetails")
				.append("as", "result").append("cond", new Document("$eq", Arrays.asList("$$result.role", "$role"))));
		ProjectionOperation project1 = Aggregation.project("username", "password", "department", "role", "reportTo",
				"userId", "user", "access", "ip", "fromTime", "toTime").and("user.fullname").as("fullname")
				.and(filterrole).as("roleDetail");
		ProjectionOperation project2 = Aggregation.project("username", "password", "department", "role", "reportTo",
				"userId", "user", "fullname", "access", "ip", "fromTime", "toTime").and("$roleDetail.hierarchy")
				.as("hierarchy");

		Aggregation aggregation = Aggregation.newAggregation(match, lookupUser, Aggregation.unwind("$user"), lookupDept,
				project1, Aggregation.unwind("$roleDetail"), project2);

		AggregationResults<MLoginDto> result = this.mongoTemplate.aggregate(aggregation, MLogin.class, MLoginDto.class);
		return result.getUniqueMappedResult();
	}

	@Override
	public List<Document> findAllByDepartmentAndRole(String department, String role) {
		MatchOperation match = Aggregation.match(Criteria.where("department").is(department).and("role").is(role));
		LookupOperation lookupUser = LookupOperation.newLookup().from("m_usr").localField("userId").foreignField("code")
				.as("user");
		ProjectionOperation project = Aggregation.project().andInclude("userId").and("user.fullname").as("fullname");
		Aggregation aggregation = Aggregation.newAggregation(match, lookupUser, Aggregation.unwind("$user"), project);
		AggregationResults<Document> result = this.mongoTemplate.aggregate(aggregation, MLogin.class, Document.class);
		return result.getMappedResults();
	}

	@Override
	public List<MLoginDto> findAllByRole() {
		MatchOperation match = Aggregation.match(new Criteria().orOperator(Criteria.where("role").is("ADMIN_ADMIN"),
				Criteria.where("department").is("SALES")));
		LookupOperation lookupUser = LookupOperation.newLookup().from("m_usr").localField("userId").foreignField("code")
				.as("user");
		LookupOperation lookupLogin = LookupOperation.newLookup().from("m_lgn").localField("userId")
				.foreignField("code").as("login");
		ProjectionOperation project = Aggregation.project().andInclude("userId", "user").and("user.fullname")
				.as("username").andInclude("username").and("login.username").as("loginName");

		Aggregation aggregation = Aggregation.newAggregation(match, lookupUser, lookupLogin,
				Aggregation.unwind("$user"), project);
		AggregationResults<MLoginDto> result = this.mongoTemplate.aggregate(aggregation, MLogin.class, MLoginDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<MLoginDto> findAllSalesPerson() {
//		MatchOperation match = Aggregation.match(new Criteria().orOperator(Criteria.where("role").is("ADMIN_ADMIN"),
//				Criteria.where("department").is("SALES")));
//		LookupOperation lookupUser = LookupOperation.newLookup().from("m_usr").localField("userId").foreignField("code")
//				.as("user");
		
		ProjectionOperation project = Aggregation.project().andInclude("userId", "user", "username");

		Aggregation aggregation = Aggregation.newAggregation(project);
		AggregationResults<MLoginDto> result = this.mongoTemplate.aggregate(aggregation, "m_lgn", MLoginDto.class);
		return result.getMappedResults();
	}

	@Override
	public Document findUserIdByDepartmentAndHierarchyLevelIsLessThanOrEqual(String userId) {
		MatchOperation match = Aggregation.match(Criteria.where("userId").is(userId));
		Map<String, String> graphLookupQuery = new HashMap<>();
		graphLookupQuery.put("from", "m_lgn");
		graphLookupQuery.put("startWith", "$userId");
		graphLookupQuery.put("connectFromField", "userId");
		graphLookupQuery.put("connectToField", "reportTo");
		graphLookupQuery.put("as", "reportingHierarchy");
		AggregationOperation graphLookup = context -> new Document("$graphLookup", graphLookupQuery);
		Aggregation aggregation = Aggregation.newAggregation(match, graphLookup);
		AggregationResults<Document> result = this.mongoTemplate.aggregate(aggregation, MLogin.class, Document.class);
		return result.getUniqueMappedResult();
	}
}
