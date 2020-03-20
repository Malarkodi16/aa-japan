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
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;

import com.nexware.aajapan.dto.TCancelledInvoicesDto;
import com.nexware.aajapan.repositories.custom.TCancelledInvoicesRepositoryCustom;

public class TCancelledInvoicesRepositoryCustomImpl implements TCancelledInvoicesRepositoryCustom {

	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private MongoOperations mongoOperations;

	@Override
	public List<TCancelledInvoicesDto> findAllData() {

		LookupOperation lookupSupplier = LookupOperation.newLookup().from("m_spplr").localField("supplierCode")
				.foreignField("supplierCode").as("company");
		LookupOperation lookupMasterTransport = LookupOperation.newLookup().from("m_trnsprtr")
				.localField("transporterCode").foreignField("code").as("transporterDetail");

		AggregationOperation addAuctionHouse = contexts -> new Document("$addFields",
				new Document("auctionHouseDetails",
						new Document("$filter",
								new Document("input", "$company.supplierLocations").append("as", "result").append(
										"cond", new Document("$eq", Arrays.asList("$$result._id", "$auctionHouse"))))));

		ProjectionOperation projectSupplier = Aggregation.project()
				.andInclude("id", "invoiceNo", "refNo", "invoiceDate", "supplierCode", "auctionHouse", "invoiceAmount",
						"remarks", "cancelledDate", "cancellationRemarks", "invoiceType")
				.and("$company.company").as("company").and("$auctionHouseDetails.auctionHouse").as("auctionHouseName")
				.and("$transporterDetail.name").as("transporter");

		SortOperation sort = Aggregation.sort(Direction.DESC, "cancelledDate");

		Aggregation aggregation = Aggregation.newAggregation(lookupSupplier, Aggregation.unwind("$company", true),
				addAuctionHouse, Aggregation.unwind("$auctionHouseDetails", true), lookupMasterTransport,
				Aggregation.unwind("$transporterDetail", true), projectSupplier, sort);
		AggregationResults<TCancelledInvoicesDto> result = this.mongoTemplate.aggregate(aggregation,
				"t_cancelled_Invoices", TCancelledInvoicesDto.class);
		return (result.getMappedResults());
	}

}
