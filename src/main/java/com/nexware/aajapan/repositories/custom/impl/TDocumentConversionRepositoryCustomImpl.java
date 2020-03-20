package com.nexware.aajapan.repositories.custom.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;
import org.springframework.data.mongodb.core.aggregation.CountOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.TDocumentConversionDto;
import com.nexware.aajapan.repositories.custom.TDocumentConversionRepositoryCustom;
import com.nexware.aajapan.utils.AppUtil;

public class TDocumentConversionRepositoryCustomImpl implements TDocumentConversionRepositoryCustom {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<TDocumentConversionDto> findAllByStatus(Integer docType) {
		MatchOperation match = Aggregation.match(Criteria.where("docConvertTo").is(docType));
		ProjectionOperation project = Aggregation.project("reauctionDate")
				.andInclude("stockNo", "docConvertTo", "exportCertificateStatus", "docSentStatus", "status")
				.and("stockDetails.chassisNo").as("chassisNo").and("stockDetails.firstRegDate").as("firstRegDate")
				.and("stockDetails.documentType").as("documentType").and("stockReceived.documentReceivedDate")
				.as("documentReceivedDate").and("stockDetails.oldNumberPlate").as("oldNumberPlate")
				.and("stockReceived.documentConvertedDate").as("documentConvertedDate")
				.and("stockDetails.documentConvertTo").as("documentConvertTo")

				.and("stockDetails.purchaseInfo.date").as("purchaseInfoDate").and("stockDetails.purchaseInfo.type")
				.as("purchaseType").and("stockDetails.purchaseInfo.supplier").as("supplierCode")
				.and("stockDetails.purchaseInfo.auctionInfo.lotNo").as("shuppinNo").and("supplierDetails.company")
				.as("supplierName").and("auctionHouse._id").as("auctionHouseId").and("auctionHouse.auctionHouse")
				.as("auctionHouse");
		LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stockDetails");
		LookupOperation lookupDocReceived = LookupOperation.newLookup().from("t_doc_recvd").localField("stockNo")
				.foreignField("stockNo").as("stockReceived");
		LookupOperation lookupSupplier = LookupOperation.newLookup().from("m_spplr").localField("supplier")
				.foreignField("supplierCode").as("supplierDetails");
		AggregationOperation addAuctionHouse = context -> new Document("$addFields", new Document("auctionHouse",
				new Document("$filter",
						new Document("input", "$supplierDetails.supplierLocations").append("as", "result")
								.append("cond", new Document("$eq", Arrays.asList("$$result._id", "$auctionHouse"))))));
		Aggregation aggregation = Aggregation.newAggregation(match, lookupStock, lookupDocReceived,
				Aggregation.unwind("$stockDetails", true), Aggregation.unwind("$stockReceived", true), lookupSupplier,
				Aggregation.unwind("$supplierDetails", true), addAuctionHouse,
				Aggregation.unwind("$auctionHouse", true), project);
		AggregationResults<TDocumentConversionDto> result = this.mongoTemplate.aggregate(aggregation, "t_dcmnt_cnvrsn",
				TDocumentConversionDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<TDocumentConversionDto> findAllExportCertificatesByExportCertificateStatusAndDocSentStatus(
			Integer exportCertificateStatus, Integer docSentStatus) {
		List<Criteria> criteria = new ArrayList<>();
		criteria.add(Criteria.where("docConvertTo").is(Constants.STOCK_DOCUMENT_TYPE_CONVERT_TO_EXPORT_CERTIFICATE));
		criteria.add(Criteria.where("docReceivedStatus").is(Constants.EXPORT_CERTIFICATE_ORIGINAL_NOT_RECEIVED));
		if (exportCertificateStatus.equals(Constants.EXPORT_CERTIFICATE_INALAIN)) {
			criteria.add(Criteria.where("exportCertificateStatus").in(Constants.EXPORT_CERTIFICATE_INALAIN,
					Constants.EXPORT_CERTIFICATE_SHIPPING_COMPANY));
			criteria.add(Criteria.where("docOriginalSent").is(Constants.EXPORT_CERTIFICATE_DOCUMENT_ORIGINAL_NOT_SENT));
		} else if (exportCertificateStatus.equals(Constants.EXPORT_CERTIFICATE_INSPECTION_COMPANY)) {
			criteria.add(Criteria.where("exportCertificateStatus").is(Constants.EXPORT_CERTIFICATE_INSPECTION_COMPANY));
		} else if ((exportCertificateStatus.equals(Constants.EXPORT_CERTIFICATE_SHIPPING_COMPANY))
				&& (docSentStatus == 0)) {
			criteria.add(Criteria.where("exportCertificateStatus").is(Constants.EXPORT_CERTIFICATE_SHIPPING_COMPANY));
			criteria.add(Criteria.where("docOriginalSent").is(Constants.EXPORT_CERTIFICATE_DOCUMENT_ORIGINAL_SENT));
		} else if ((exportCertificateStatus.equals(Constants.EXPORT_CERTIFICATE_SHIPPING_COMPANY))
				&& (docSentStatus == 1)) {
			criteria.add(Criteria.where("exportCertificateStatus").is(Constants.EXPORT_CERTIFICATE_SHIPPING_COMPANY));
			criteria.add(Criteria.where("docEmailSent").is(Constants.EXPORT_CERTIFICATE_DOCUMENT_EMAIL_SENT));
		}

		MatchOperation match = Aggregation.match(new Criteria().andOperator(criteria.toArray(new Criteria[0])));

		LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stockDetails");

		LookupOperation lookupSupplier = LookupOperation.newLookup().from("m_spplr")
				.localField("stockDetails.purchaseInfo.supplier").foreignField("supplierCode").as("supplierDetails");
		AggregationOperation addAuctionHouse = context -> new Document("$addFields",
				new Document("auctionHouse",
						new Document("$filter",
								new Document("input", "$supplierDetails.supplierLocations").append("as", "result")
										.append("cond", new Document("$eq", Arrays.asList("$$result._id",
												"$stockDetails.purchaseInfo.auctionInfo.auctionHouse"))))));
		LookupOperation lookupDocReceived = LookupOperation.newLookup().from("t_doc_recvd").localField("stockNo")
				.foreignField("stockNo").as("docRecDetails");
		LookupOperation lookupExportCertificate = LookupOperation.newLookup().from("t_exprt_crtfct")
				.localField("stockNo").foreignField("stockNo").as("exportCertificate");
		ProjectionOperation project = Aggregation.project()
				.andInclude("stockNo", "docConvertTo", "exportCertificateStatus", "docSentStatus", "status")
				.and("stockDetails.chassisNo").as("chassisNo").and("stockDetails.firstRegDate").as("firstRegDate")
				.and("stockDetails.documentType").as("documentType").and("docRecDetails.documentReceivedDate")
				.as("documentReceivedDate").and("stockDetails.oldNumberPlate").as("oldNumberPlate")
				.and(ConditionalOperators.ifNull("exportCertificate.convertedDate")
						.thenValueOf("docRecDetails.documentConvertedDate"))
				.as("documentConvertedDate").and("docRecDetails.documentConvertTo").as("documentConvertTo")
				.and("stockDetails.documentFob").as("documentFob").and("stockDetails.destinationPort")
				.as("destinationPort").and("stockDetails.plateNoReceivedDate").as("plateNoReceivedDate")
				.and("stockDetails.destinationCountry").as("destinationCountry").and("stockDetails.documentStatus")
				.as("documentStatus").and("stockDetails.purchaseInfo.date").as("purchaseInfoDate")
				.and("stockDetails.purchaseInfo.type").as("purchaseType").and("stockDetails.purchaseInfo.supplier")
				.as("supplierCode").and("stockDetails.purchaseInfo.auctionInfo.lotNo").as("shuppinNo")
				.and("supplierDetails.company").as("supplierName").and("auctionHouse._id").as("auctionHouseId")
				.and("auctionHouse.auctionHouse").as("auctionHouse");
		Aggregation aggregation = Aggregation.newAggregation(match, lookupStock,
				Aggregation.unwind("$stockDetails", true), lookupSupplier, Aggregation.unwind("$supplierDetails", true),
				addAuctionHouse, Aggregation.unwind("$auctionHouse", true), lookupDocReceived,
				Aggregation.unwind("$docRecDetails", true), lookupExportCertificate,
				Aggregation.unwind("$exportCertificate", true), project);
		AggregationResults<TDocumentConversionDto> result = this.mongoTemplate.aggregate(aggregation, "t_dcmnt_cnvrsn",
				TDocumentConversionDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<TDocumentConversionDto> findAllCrReceivedDocument() {
		MatchOperation match = Aggregation.match(new Criteria().andOperator(
				Criteria.where("docConvertTo").is(Constants.STOCK_DOCUMENT_TYPE_CONVERT_TO_EXPORT_CERTIFICATE),
				Criteria.where("docReceivedStatus").is(Constants.EXPORT_CERTIFICATE_ORIGINAL_RECEIVED),
				Criteria.where("handoverStatus").is(Constants.EXPORT_CERTIFICATE_NOT_HANDOVER)));
		ProjectionOperation project = Aggregation.project()
				.andInclude("stockNo", "docConvertTo", "exportCertificateStatus", "docSentStatus", "status")
				.and("stockDetails.chassisNo").as("chassisNo").and("stockDetails.firstRegDate").as("firstRegDate")
				.and("docDetails.documentType").as("docType").and("docDetails.documentReceivedDate")
				.as("documentReceivedDate").and("stockDetails.oldNumberPlate").as("oldNumberPlate")
				.and("stockDetails.documentConvertedDate").as("documentConvertedDate")
				.and("stockDetails.documentConvertTo").as("documentConvertTo")

				.and("stockDetails.purchaseInfo.date").as("purchaseInfoDate").and("stockDetails.purchaseInfo.type")
				.as("purchaseType").and("stockDetails.purchaseInfo.supplier").as("supplierCode")
				.and("stockDetails.purchaseInfo.auctionInfo.lotNo").as("shuppinNo").and("supplierDetails.company")
				.as("supplierName").and("auctionHouse._id").as("auctionHouseId").and("auctionHouse.auctionHouse")
				.as("auctionHouse");
		LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stockDetails");
		LookupOperation lookupDocumentRecieved = LookupOperation.newLookup().from("t_doc_recvd").localField("stockNo")
				.foreignField("stockNo").as("docDetails");
		LookupOperation lookupSupplier = LookupOperation.newLookup().from("m_spplr")
				.localField("stockDetails.purchaseInfo.supplier").foreignField("supplierCode").as("supplierDetails");
		AggregationOperation addAuctionHouse = context -> new Document("$addFields",
				new Document("auctionHouse",
						new Document("$filter",
								new Document("input", "$supplierDetails.supplierLocations").append("as", "result")
										.append("cond", new Document("$eq", Arrays.asList("$$result._id",
												"$stockDetails.purchaseInfo.auctionInfo.auctionHouse"))))));
		Aggregation aggregation = Aggregation.newAggregation(match, lookupStock,
				Aggregation.unwind("$stockDetails", true), lookupDocumentRecieved,
				Aggregation.unwind("$docDetails", true), lookupSupplier, Aggregation.unwind("$supplierDetails", true),
				addAuctionHouse, Aggregation.unwind("$auctionHouse", true), project);
		AggregationResults<TDocumentConversionDto> result = this.mongoTemplate.aggregate(aggregation, "t_dcmnt_cnvrsn",
				TDocumentConversionDto.class);
		return result.getMappedResults();
	}

	@Override
	public Long findAllByExportCertificateCount() {
		List<Criteria> criteria = new ArrayList<>();
		criteria.add(Criteria.where("docConvertTo").is(Constants.STOCK_DOCUMENT_TYPE_CONVERT_TO_EXPORT_CERTIFICATE));
		criteria.add(Criteria.where("docReceivedStatus").is(Constants.EXPORT_CERTIFICATE_ORIGINAL_NOT_RECEIVED));
		criteria.add(Criteria.where("exportCertificateStatus").in(Constants.EXPORT_CERTIFICATE_INALAIN,
				Constants.EXPORT_CERTIFICATE_SHIPPING_COMPANY));
		criteria.add(Criteria.where("docOriginalSent").is(Constants.EXPORT_CERTIFICATE_DOCUMENT_ORIGINAL_NOT_SENT));
		MatchOperation match = Aggregation.match(new Criteria().andOperator(criteria.toArray(new Criteria[0])));
		CountOperation count = Aggregation.count().as("count");
		Aggregation aggregation = Aggregation.newAggregation(match, count);
		AggregationResults<Map> result = this.mongoTemplate.aggregate(aggregation, "t_dcmnt_cnvrsn", Map.class);
		return Long.valueOf(!AppUtil.isObjectEmpty(result.getUniqueMappedResult())
				? result.getUniqueMappedResult().get("count").toString()
				: "0");
	}
}
