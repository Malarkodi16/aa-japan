package com.nexware.aajapan.repositories.custom.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.TTUnitAllocationDto;
import com.nexware.aajapan.repositories.custom.TLcInvoiceRepositoryCustom;

public class TLcInvoiceRepositoryCustomImpl implements TLcInvoiceRepositoryCustom {
	@Autowired
	private MongoTemplate mongoTemplate;

//	@Override
//	public void updateInvoiceById(String id, Map<String, String> data) {
//		String 
//		Update update = new Update().set("maker", AppUtil.ifNull(data.get("maker"), ""))
//				.set("model", AppUtil.ifNull(data.get("model"), ""))
//				.set("hsCode", AppUtil.ifNull(data.get("hsCode"), ""))
//				.set("schedule", AppUtil.ifNull(data.get("schedule"), ""));
//
//		this.mongoTemplate.updateFirst(Query.query(Criteria.where("id").is(id)), update, TLcInvoice.class);
//	}

	@Override
	public List<TTUnitAllocationDto> findAllInvoiceByLcInvoiceNo(String lcInvocieNo, String customerId) {

		final Criteria criteria = new Criteria();
		criteria.andOperator(Criteria.where("lcInvoiceNo").is(lcInvocieNo),
				Criteria.where("customerId").is(customerId));

		MatchOperation match = Aggregation.match(criteria);

		final LookupOperation lookupstockDetail = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stockDetail");
//		final LookupOperation currencyDetail = LookupOperation.newLookup().from("m_currency").localField("currencyType")
//				.foreignField("currencySeq").as("currencyDetail");
		final ProjectionOperation projectionOperation = Aggregation.project("stockNo", "createdDate")
				.and("$stockDetail.chassisNo").as("chassisNo").and("amount").as("total").and("amountAllocatted")
				.as("received").and("amount").minus("amountAllocatted").as("lcBalance");

//		final SortOperation sort = Aggregation.sort(Direction.ASC, "createdDate");

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupstockDetail,
				Aggregation.unwind("$stockDetail", true), projectionOperation);

		final AggregationResults<TTUnitAllocationDto> result = mongoTemplate.aggregate(aggregation, "t_lc_invc",
				TTUnitAllocationDto.class);
		return result.getMappedResults();
	}
}
