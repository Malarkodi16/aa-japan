package com.nexware.aajapan.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.MongoRegexCreator;
import org.springframework.data.mongodb.core.query.MongoRegexCreator.MatchMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexware.aajapan.dto.CustomerEmailDto;
import com.nexware.aajapan.models.TCustomer;
import com.nexware.aajapan.services.CustomerService;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<CustomerEmailDto> getCustomerEmailList() {
		ProjectionOperation projectStage = Aggregation.project("id", "email");
		AggregationResults<CustomerEmailDto> result = this.mongoTemplate
				.aggregate(Aggregation.newAggregation(projectStage), "customer", CustomerEmailDto.class);
		return result.getMappedResults();

	}

	@Override
	public List<CustomerEmailDto> getCustomerEmailListBySearchTerm(String search) {
		ProjectionOperation projectStage = Aggregation.project("code", "email");
		MatchOperation matchOperation = Aggregation.match(
				Criteria.where("email").regex(MongoRegexCreator.INSTANCE.toRegularExpression(search, MatchMode.LIKE)));
		AggregationResults<CustomerEmailDto> result = this.mongoTemplate.aggregate(
				Aggregation.newAggregation(matchOperation, projectStage), TCustomer.class, CustomerEmailDto.class);
		return result.getMappedResults();
	}

}
