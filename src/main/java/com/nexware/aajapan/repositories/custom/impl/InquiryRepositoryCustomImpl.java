package com.nexware.aajapan.repositories.custom.impl;

import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.CountOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import com.nexware.aajapan.dto.TInquiryDto;
import com.nexware.aajapan.repositories.custom.InquiryRepositoryCustom;
import com.nexware.aajapan.utils.AppUtil;

public class InquiryRepositoryCustomImpl implements InquiryRepositoryCustom {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<TInquiryDto> findAllInquiriesBySalesPersonIn(List<String> salesPersonIds) {
		Criteria criteria = new Criteria();
		if (!AppUtil.isObjectEmpty(salesPersonIds)) {
			criteria = Criteria.where("salesPerson").in(salesPersonIds);
		}
		MatchOperation match = Aggregation.match(criteria);
		ProjectionOperation project = Aggregation.project()
				.andInclude("id", "customerCode", "mobile", "customerName", "companyName", "skypeId", "createdDate")
				.and("inquiryVehicles._id").as("inquiryVehicleId").and("inquiryVehicles.category").as("category")
				.and("inquiryVehicles.subCategory").as("subCategory").and("inquiryVehicles.maker").as("maker")
				.and("inquiryVehicles.model").as("model").and("inquiryVehicles.subModel").as("subModel")
				.and("inquiryVehicles.country").as("country").and("inquiryVehicles.port").as("port");
		SortOperation sort = Aggregation.sort(Direction.DESC, "createdDate");
		Aggregation aggregation = Aggregation.newAggregation(match, Aggregation.unwind("$inquiryVehicles", true),
				project, sort);

		AggregationResults<TInquiryDto> result = this.mongoTemplate.aggregate(aggregation, "t_inqry",
				TInquiryDto.class);

		return result.getMappedResults();
	}

	@Override
	public TInquiryDto findOneInquiryById(String inquiryId, String itemId) {
		MatchOperation match = Aggregation.match(Criteria.where("_id").is(inquiryId));

		AggregationOperation addNotifyParty = context -> new Document("$addFields", new Document("item",
				new Document("$filter", new Document("input", "$inquiryVehicles").append("as", "result").append("cond",
						new Document("$eq", Arrays.asList("$$result._id", new ObjectId(itemId)))))));
		ProjectionOperation project2 = Aggregation.project()
				.andInclude("_id", "customerCode", "mobile", "customerName", "companyName", "skypeId", "createdDate")
				.and("$item._id").as("inquiryVehicleId").and("$item.category").as("category").and("$item.subCategory")
				.as("subCategory").and("$item.maker").as("maker").and("$item.model").as("model").and("$item.subModel")
				.as("subModel").and("$item.country").as("country").and("$item.port").as("port");

		Aggregation aggregation = Aggregation.newAggregation(match, addNotifyParty, Aggregation.unwind("$item", true),
				project2);

		AggregationResults<TInquiryDto> result = this.mongoTemplate.aggregate(aggregation, "t_inqry",
				TInquiryDto.class);

		return result.getUniqueMappedResult();
	}

	@Override
	public long getCountBySalesPersonId(List<String> salesPersonIds) {
		Criteria criteria = new Criteria();
		if (!AppUtil.isObjectEmpty(salesPersonIds)) {
			criteria = Criteria.where("salesPerson").in(salesPersonIds);
		}
		MatchOperation match = Aggregation.match(criteria);
		CountOperation count = Aggregation.count().as("count");
		Aggregation aggregation = Aggregation.newAggregation(match, Aggregation.unwind("$inquiryVehicles", true),
				count);
		AggregationResults<Document> result = this.mongoTemplate.aggregate(aggregation, "t_inqry", Document.class);
		return !AppUtil.isObjectEmpty(result.getUniqueMappedResult())
				? result.getUniqueMappedResult().getInteger("count")
				: 0;
	}
}
