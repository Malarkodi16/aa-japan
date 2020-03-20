package com.nexware.aajapan.repositories.custom.impl;

import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.BasicDBObject;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.MVechicleMakerDto;
import com.nexware.aajapan.dto.MVechicleMakerExportExcelDto;
import com.nexware.aajapan.models.MVechicleMaker;
import com.nexware.aajapan.models.Model;
import com.nexware.aajapan.repositories.custom.MVechicleMakerRepositoryCustom;

public class MVechicleMakerRepositoryCustomImpl implements MVechicleMakerRepositoryCustom {

	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private MongoOperations mongoOperations;

	@Override
	public List<MVechicleMakerDto> findAllModel() {

		final ProjectionOperation project = Aggregation.project().andInclude("id", "code", "name")
				.and("$models.modelName").as("models");
		final Aggregation aggregation = Aggregation.newAggregation(project);

		final AggregationResults<MVechicleMakerDto> result = mongoTemplate.aggregate(aggregation, MVechicleMaker.class,
				MVechicleMakerDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<MVechicleMaker> getListWithoutDelete() {
		final MatchOperation match = Aggregation.match(Criteria.where("deleteFlag").ne(Constants.DELETE_FLAG_1));
		final AggregationOperation unwindModel = Aggregation.unwind("models", true);
		final SortOperation sortModels = Aggregation.sort(Direction.ASC, "$models.modelName");
		final GroupOperation groupOperation = Aggregation.group("$_id").first("code").as("code").first("name")
				.as("name").first("deleteFlag").as("deleteFlag")
				.push(new BasicDBObject("modelName", "$models.modelName").append("category", "$models.category")
						.append("transportCategory", "$models.transportCategory")
						.append("subcategory", "$models.subcategory").append("modelId", "$models.modelId")
						.append("length", "$models.length").append("width", "$models.width")
						.append("height", "$models.height").append("m3", "$models.m3"))
				.as("models");
		final SortOperation sortMaker = Aggregation.sort(Direction.ASC, "name");
		final Aggregation aggregation = Aggregation.newAggregation(match, unwindModel, sortModels, groupOperation,
				sortMaker);
		final AggregationResults<MVechicleMaker> result = mongoTemplate.aggregate(aggregation, "m_vchcl_mkr",
				MVechicleMaker.class);
		return result.getMappedResults();
	}

	@Override
	public MVechicleMaker getListByCodeWithoutDelete(String code) {
		final MatchOperation match = Aggregation.match(
				Criteria.where("deleteFlag").ne(Constants.DELETE_FLAG_1).andOperator(Criteria.where("code").is(code)));

		final Aggregation aggregation = Aggregation.newAggregation(match);
		final AggregationResults<MVechicleMaker> result = mongoTemplate.aggregate(aggregation, "m_vchcl_mkr",
				MVechicleMaker.class);
		return result.getUniqueMappedResult();
	}

	@Override
	public List<MVechicleMakerExportExcelDto> exportExcelFormatting() {
		final MatchOperation match = Aggregation.match(Criteria.where("deleteFlag").ne(Constants.DELETE_FLAG_1));

		final AggregationOperation unwindModel = Aggregation.unwind("models", true);

		final ProjectionOperation project = Aggregation.project().andInclude("name").and("models.modelName")
				.as("modelName").and("models.category").as("category").and("models.subcategory").as("subcategory")
				.and("models.width").as("width").and("models.m3").as("m3").and("models.length").as("length")
				.and("models.height").as("height").and("models.subModel.subModelName").as("subModelName");

		final Aggregation aggregation = Aggregation.newAggregation(match, unwindModel, project);
		final AggregationResults<MVechicleMakerExportExcelDto> result = mongoTemplate.aggregate(aggregation,
				"m_vchcl_mkr", MVechicleMakerExportExcelDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<MVechicleMakerExportExcelDto> exportExcelFormattingRow(String code) {
		final MatchOperation match = Aggregation.match(Criteria.where("code").is(code));

		final AggregationOperation unwindModel = Aggregation.unwind("models", true);

		final ProjectionOperation project = Aggregation.project().andInclude("name").and("models.modelName")
				.as("modelName").and("models.category").as("category").and("models.subcategory").as("subcategory")
				.and("models.width").as("width").and("models.m3").as("m3").and("models.length").as("length")
				.and("models.height").as("height").and("models.subModel.subModelName").as("subModelName");

		final Aggregation aggregation = Aggregation.newAggregation(match, unwindModel, project);
		final AggregationResults<MVechicleMakerExportExcelDto> result = mongoTemplate.aggregate(aggregation,
				"m_vchcl_mkr", MVechicleMakerExportExcelDto.class);
		return result.getMappedResults();
	}

	@Override
	public Model getModelData(String maker, String modelId) {
		final MatchOperation matchOperation = Aggregation.match(Criteria.where("name").is(maker));

		final AggregationOperation filterModel = context -> new Document("$addFields",
				new Document("model", new Document("$filter", new Document("input", "$models").append("as", "result")
						.append("cond", new Document("$eq", Arrays.asList("$$result.modelId", modelId))))));

		final ProjectionOperation project = Aggregation.project().andInclude().and("model.modelName").as("modelName")
				.and("model.category").as("category").and("model.subcategory").as("subcategory")
				.and("model.transportCategory").as("transportCategory").and("model.width").as("width").and("model.m3").as("m3")
				.and("model.length").as("length").and("model.height").as("height").and("model.subModel").as("subModel")
				.and("model.modelId").as("modelId");

		final Aggregation aggregation = Aggregation.newAggregation(matchOperation, filterModel,
				Aggregation.unwind("$model", true), project);
		final AggregationResults<Model> result = mongoTemplate.aggregate(aggregation, "m_vchcl_mkr", Model.class);
		return result.getUniqueMappedResult();

	}

	@Override
	public boolean existsByCodeAndName(String code, String name) {
		final Criteria criteria = new Criteria();
		criteria.andOperator(Criteria.where("code").is(code), Criteria.where("name").regex("(?sim)^" + name + "$"));
		return mongoOperations.exists(Query.query(criteria), MVechicleMaker.class);
	}

	@Override
	public boolean existsByName(String name) {
		return mongoOperations.exists(Query.query(Criteria.where("name").regex("(?sim)^" + name + "$")),
				MVechicleMaker.class);
	}

	@Override
	public Model getModelDataName(String maker, String modelName, String category) {
		final MatchOperation matchOperation = Aggregation.match(Criteria.where("name").is(maker));

		final AggregationOperation filterModel = context -> new Document("$addFields",
				new Document("model",
						new Document("$filter",
								new Document("input", "$models").append("as", "result").append("cond",
										new Document("$and", Arrays.asList(
												new Document("$eq", Arrays.asList("$$result.modelName", modelName)),
												new Document("eq", Arrays.asList("$$result.category", category))))))));

		final ProjectionOperation project = Aggregation.project().andInclude().and("model.modelName").as("modelName")
				.and("model.category").as("category").and("model.subcategory").as("subcategory").and("model.width")
				.as("width").and("model.m3").as("m3").and("model.length").as("length").and("model.height").as("height")
				.and("model.subModel").as("subModel").and("model.modelId").as("modelId");

		final Aggregation aggregation = Aggregation.newAggregation(matchOperation, filterModel,
				Aggregation.unwind("$model", true), project);
		final AggregationResults<Model> result = mongoTemplate.aggregate(aggregation, "m_vchcl_mkr", Model.class);
		return result.getUniqueMappedResult();

	}

}
