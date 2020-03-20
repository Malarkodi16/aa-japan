package com.nexware.aajapan.repositories.custom.impl;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.exceptions.AAJRuntimeException;
import com.nexware.aajapan.models.MLocation;
import com.nexware.aajapan.models.MSupplier;
import com.nexware.aajapan.repositories.custom.MasterSupplierRepositoryCustom;

public class MasterSupplierRepositoryCustomImpl implements MasterSupplierRepositoryCustom {
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private MongoOperations mongoOperations;

	@Override
	public List<MSupplier> getSupplierListBySearchTerm(String search, String criteria) {
		ProjectionOperation projectStage = Aggregation.project("supplierCode", "company");
		MatchOperation matchOperation = Aggregation
				.match(new Criteria().andOperator(Criteria.where("type").is(criteria),
						Criteria.where("company").regex(Pattern.compile("(?sim).*" + search + ".*"))));
		AggregationResults<MSupplier> result = this.mongoTemplate
				.aggregate(Aggregation.newAggregation(matchOperation, projectStage), MSupplier.class, MSupplier.class);
		return result.getMappedResults();
	}

	@Override
	public List<String> getPOSNos(String supplier, String auctionHouse) {
		Query query = new Query(new Criteria().andOperator(Criteria.where("company").is(supplier),
				Criteria.where("supplierLocations.auctionHouse").is(auctionHouse)));
		query.fields().include("supplierLocations.$");

		MSupplier supplierResult = this.mongoTemplate.findOne(query, MSupplier.class);
		if ((supplierResult.getSupplierLocations() == null) || supplierResult.getSupplierLocations().isEmpty()) {
			throw new AAJRuntimeException("Unable to find supplier: " + supplier);
		} else if (supplierResult.getSupplierLocations().size() > 1) {
			throw new AAJRuntimeException("Duplication auction house found for : " + supplier);
		} else {
			return supplierResult.getSupplierLocations().get(0).getPosNos();

		}

	}

	@Override
	public MSupplier getSupplierByName(String supplier) {
		return this.mongoTemplate.findOne(Query.query(Criteria.where("company").is(supplier)), MSupplier.class);
	}

	@Override
	public List<MSupplier> getListWithoutDeletedSuppliers() {
		MatchOperation matchOperation = Aggregation.match(new Criteria()
				.orOperator(Criteria.where("deleteStatus").is(Constants.MLOCATION_DELETE_STATUS_INITIALLY)));
		AggregationOperation project = context -> new Document("$project",
				new Document("id", "$id").append("supplierCode", "$supplierCode").append("type", "$type")
						.append("company", "$company").append("deleteStatus", "$deleteStatus")
						.append("maxCreditAmount", "$maxCreditAmount").append("supplierLocations",
								new Document("$filter",
										new Document("input", "$supplierLocations").append("as", "result")
												.append("cond", new Document("$eq", Arrays.asList(
														"$$result.deleteStatus",
														Constants.MLOCATION_SUPPLIER_DELETE_STATUS_INITIALLY))))));
		Aggregation aggregation = Aggregation.newAggregation(matchOperation, project);
		AggregationResults<MSupplier> result = this.mongoTemplate.aggregate(aggregation, MSupplier.class,
				MSupplier.class);
		return result.getMappedResults();
	}

	@Override
	public MSupplier findOneActiveSupplier(String supplierCode) {

		Criteria criteria = new Criteria();
		criteria.andOperator(Criteria.where("deleteStatus").is(Constants.MLOCATION_DELETE_STATUS_INITIALLY),
				Criteria.where("supplierCode").is(supplierCode));
		MatchOperation matchOperation = Aggregation.match(criteria);
		AggregationOperation project = context -> new Document("$project",
				new Document("id", "$id").append("supplierCode", "$supplierCode").append("type", "$type")
						.append("company", "$company").append("createdDate", "$createdDate")
						.append("createdBy", "$createdBy").append("maxDueDays", "$maxDueDays")
						.append("maxCreditAmount", "$maxCreditAmount").append("deleteStatus", "$deleteStatus")
						.append("supplierLocations",
								new Document("$filter",
										new Document("input", "$supplierLocations").append("as", "result")
												.append("cond", new Document("$eq", Arrays.asList(
														"$$result.deleteStatus",
														Constants.MLOCATION_SUPPLIER_DELETE_STATUS_INITIALLY))))));
		Aggregation aggregation = Aggregation.newAggregation(matchOperation, project);
		AggregationResults<MSupplier> result = this.mongoTemplate.aggregate(aggregation, MSupplier.class,
				MSupplier.class);
		return result.getUniqueMappedResult();
	}

	@Override
	public boolean isExistsByCompany(String company, Integer deleteFlag) {
		return this.mongoOperations.exists(Query.query(Criteria.where("company").regex("(?sim)^" + company + "$")
				.andOperator(Criteria.where("deleteStatus").is(deleteFlag))), MSupplier.class);
	}

	@Override
	public boolean isExistsBySupplierCodeAndCompany(String supplierCode, String company, Integer deleteFlag) {
		Criteria criteria = new Criteria();
		criteria.andOperator(Criteria.where("supplierCode").is(supplierCode),
				Criteria.where("company").regex("(?sim)^" + company + "$"),
				Criteria.where("deleteStatus").is(deleteFlag));
		return this.mongoOperations.exists(Query.query(criteria), MSupplier.class);
	}

	@Override
	public void updateBySupplierCode(String supplierCode, Integer deleteFlag) {
		Update update = new Update();
		update.set("deleteFlag", deleteFlag);
		this.mongoTemplate.updateMulti(Query.query(Criteria.where("supplierCode").is(supplierCode)), update,
				MLocation.class);
	}

}
