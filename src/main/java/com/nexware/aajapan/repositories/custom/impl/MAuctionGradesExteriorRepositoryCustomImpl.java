package com.nexware.aajapan.repositories.custom.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.models.MAuctionGradesExterior;
import com.nexware.aajapan.models.MAuctionGradesInterior;
import com.nexware.aajapan.models.MForwarder;
import com.nexware.aajapan.repositories.MAuctionGradesExteriorRepository;
import com.nexware.aajapan.repositories.custom.MAuctionGradesExteriorRepositoryCustom;

public class MAuctionGradesExteriorRepositoryCustomImpl implements MAuctionGradesExteriorRepositoryCustom {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private MongoOperations mongoOperations;

	@Override
	public List<MAuctionGradesExterior> getAllUnDeletedExteriorGrade() {
		MatchOperation match = Aggregation.match(Criteria.where("deleteFlag").is(Constants.DELETE_FLAG_0));
		Aggregation aggregation = Aggregation.newAggregation(match);
		AggregationResults<MAuctionGradesExterior> result = this.mongoTemplate.aggregate(aggregation, "m_actn_grd_ext",
				MAuctionGradesExterior.class);
		return result.getMappedResults();
	}

	@Override
	public boolean existsByIdAndGrade(String id, String grade) {
		// TODO Auto-generated method stub
		final Criteria criteria = new Criteria();
		criteria.andOperator(Criteria.where("id").is(id), Criteria.where("grade").is(grade));
		return mongoOperations.exists(Query.query(criteria), MAuctionGradesExterior.class);
		
	}

	@Override
	public boolean existsByGrade(String grade) {
		// TODO Auto-generated method stub
		final Criteria criteria = new Criteria();
		criteria.andOperator(Criteria.where("deleteFlag").is(Constants.DELETE_FLAG_0), Criteria.where("grade").is(grade));
		return mongoOperations.exists(Query.query(criteria),
				MAuctionGradesExterior.class);
	}

}
