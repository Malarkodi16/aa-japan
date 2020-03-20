package com.nexware.aajapan.repositories.custom.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AccumulatorOperators;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.CountOperation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.InventoryDto;
import com.nexware.aajapan.dto.ProfitLossDto;
import com.nexware.aajapan.dto.TReAuctionDto;
import com.nexware.aajapan.repositories.custom.TReAuctionRepositoryCustom;
import com.nexware.aajapan.utils.AppUtil;

public class TReAuctionRepositoryCustomImpl implements TReAuctionRepositoryCustom {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<TReAuctionDto> getTReAuctionInitiatedList() {

		final LookupOperation lookupSupplier = LookupOperation.newLookup().from("m_spplr").localField("auctionCompany")
				.foreignField("supplierCode").as("supplier");

		final AggregationOperation projectAuctionHouse = context -> new Document("$project",
				new Document("id", "$id").append("stockNo", "$stockNo").append("chassisNo", "$chassisNo")
						.append("reauctionDate", "$reauctionDate").append("auctionCompanyId", "$auctionCompany")
						.append("auctionHouseId", "$auctionHouse").append("auctionCompany", "$supplier.company")
						.append("createdDate", "$createdDate").append("status", "$status")
						.append("recycleAmount", "$recycleAmount").append("auctionHouse",
								new Document("$filter", new Document("input", "$supplier.supplierLocations")
										.append("as", "result").append("cond",
												new Document("$eq", Arrays.asList("$$result._id", "$auctionHouse"))))));

		final ProjectionOperation project = Aggregation.project()
				.andInclude("$id", "$stockNo", "$chassisNo", "$reauctionDate", "$auctionCompanyId", "$auctionHouseId",
						"$auctionCompany", "$status", "$recycleAmount")
				.and("$auctionHouse.auctionHouse").as("auctionHouse");
		final SortOperation sort = Aggregation.sort(Direction.DESC, "reauctionDate");
		final Aggregation aggregation = Aggregation.newAggregation(sort, lookupSupplier,
				Aggregation.unwind("$supplier", true), projectAuctionHouse, Aggregation.unwind("$auctionHouse", true),
				project);

		final AggregationResults<TReAuctionDto> result = mongoTemplate.aggregate(aggregation, "t_re_actn",
				TReAuctionDto.class);

		return result.getMappedResults();
	}

	@Override
	public TReAuctionDto getTReAuctionInitiatedListById(String id) {
		final MatchOperation match = Aggregation.match(Criteria.where("_id").is(new ObjectId(id)));

		final LookupOperation lookupSupplier = LookupOperation.newLookup().from("m_spplr").localField("auctionCompany")
				.foreignField("supplierCode").as("supplier");

		final AggregationOperation projectAuctionHouse = context -> new Document("$project",
				new Document("id", "$id").append("stockNo", "$stockNo").append("chassisNo", "$chassisNo")
						.append("reauctionDate", "$reauctionDate").append("auctionCompanyId", "$auctionCompany")
						.append("auctionHouseId", "$auctionHouse").append("auctionCompany", "$supplier.company")
						.append("status", "$status").append("soldPrice", "$soldPrice").append("tax", "$tax")
						.append("recycleAmount", "$recycleAmount").append("commission", "$commission")
						.append("auctionHouse", new Document("$filter",
								new Document("input", "$supplier.supplierLocations").append("as", "result").append(
										"cond", new Document("$eq", Arrays.asList("$$result._id", "$auctionHouse")))))
						.append("nagareCharge", "$nagareCharge")
						.append("auctionShippingCharge", "$auctionShippingCharge"));

		final ProjectionOperation project = Aggregation.project().and("$id").as("id").and("$stockNo").as("stockNo")
				.and("$chassisNo").as("chassisNo").and("$reauctionDate").as("reauctionDate").and("$auctionCompanyId")
				.as("auctionCompanyId").and("$auctionHouseId").as("auctionHouseId").and("$auctionCompany")
				.as("auctionCompany").and("$auctionHouse.auctionHouse").as("auctionHouse").and("$status").as("status")
				.and("$soldPrice").as("soldPrice").and("$tax").as("tax").and("$commission").as("commission")
				.and("$recycleAmount").as("recycleAmount").and("$nagareCharge").as("nagareCharge")
				.and("$auctionShippingCharge").as("auctionShippingCharge");

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupSupplier,
				Aggregation.unwind("$supplier", true), projectAuctionHouse, Aggregation.unwind("$auctionHouse", true),
				project);

		final AggregationResults<TReAuctionDto> result = mongoTemplate.aggregate(aggregation, "t_re_actn",
				TReAuctionDto.class);

		return result.getUniqueMappedResult();
	}

	@Override
	public Long getTReAuctionInitiatedListCount() {
		final MatchOperation match = Aggregation.match(Criteria.where("status").in(Constants.REAUCTION_STATUS_INITIATED,
				Constants.REAUCTION_STATUS_INVOICE_GENARATED));
		final CountOperation count = Aggregation.count().as("count");
		final Aggregation aggregation = Aggregation.newAggregation(match, count);
		final AggregationResults<Map> result = mongoTemplate.aggregate(aggregation, "t_re_actn", Map.class);
		return Long.valueOf(!AppUtil.isObjectEmpty(result.getUniqueMappedResult())
				? result.getUniqueMappedResult().get("count").toString()
				: "0");
	}

	@Override
	public List<ProfitLossDto> getProfitLosslist() {
		
		final MatchOperation match = Aggregation
				.match(Criteria.where("status").ne(Constants.REAUCTION_STATUS_CANCELLED));
		final LookupOperation lookupPurchaseInvoice = LookupOperation.newLookup().from("t_prchs_invc")
				.localField("stockNo").foreignField("stockNo").as("purchaseInvoiceDetails");
		final LookupOperation lookupTransportInvoice = LookupOperation.newLookup().from("trnsprt_invc")
				.localField("stockNo").foreignField("stockNo").as("transportInvoice");
		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stockDetails");
		final LookupOperation lookupCustomer = LookupOperation.newLookup().from("t_cstmr")
				.localField("stockDetails.reservedInfo.customerId").foreignField("code").as("customer");
		final LookupOperation lookupForwarderInvoice = LookupOperation.newLookup().from("t_frwrdr_invc")
				.localField("stockNo").foreignField("stockNo").as("forwarderInvoice");

		GroupOperation groupOperationTotal = Aggregation.group("$stockNo").first("stockNo").as("stockNo")
				.first("chassisNo").as("chassisNo").sum("$purchaseItem.purchaseCost").as("purchaseCost")
				.sum("$purchaseItem.purchaseCostTaxAmount").as("purchaseCostTaxAmount").sum("$purchaseItem.commision")
				.as("commision").sum("$purchaseItem.commisionTaxAmount").as("commisionTaxAmount")
				.sum("$purchaseItem.otherCharges").as("otherCharges").sum("$purchaseItem.othersCostTaxAmount")
				.as("othersCostTaxAmount").sum("$purchaseItem.roadTax").as("roadTax").sum("$purchaseItem.recycle")
				.as("recycle").first("status").as("status").first("soldPrice").as("soldPrice").first("soldDate")
				.as("soldDate").first("shuppinCommission").as("shuppinCommission").first("shuppinTax").as("shuppinTax")
				.first("soldCommission").as("soldCommission").first("soldCommTax").as("soldCommTax").first("tax").as("tax")
				.first("taxAmount").as("taxAmount").first("shuppinTaxAmount").as("shuppinTaxAmount")
				.first("soldTaxAmount").as("soldTaxAmount").first("recycleAmount").as("recycleAmount")
				.first("invoiceDate").as("soldDate").first("$stockDetails.isBidding").as("stockType")
				.first("$customer.firstName").as("firstName").first("$customer.lastName").as("lastName")
				.first("$transportInvoice.totalTaxIncluded").as("totalTaxIncluded").first("$rikusoItem.otherCharges")
				.as("rikusoCharge").first("$forwarderInvoice.sumOfCharges").as("sumOfCharges").first("others")
				.as("others");

		AggregationOperation purchaseVehicles = context -> new Document("$addFields",
				new Document("purchaseItem",
						new Document("$filter",
								new Document("input", "$purchaseInvoiceDetails").append("as", "result").append("cond",
										new Document("$eq", Arrays.asList("$$result.type",
												Constants.PURCHASE_INVOICE_ITEM_TYPE_PURCHASE))))));

		AggregationOperation rikusoVehicles = context -> new Document("$addFields",
				new Document("rikusoItem",
						new Document("$filter",
								new Document("input", "$purchaseInvoiceDetails").append("as", "result").append("cond",
										new Document("$eq", Arrays.asList("$$result.type",
												Constants.PURCHASE_INVOICE_ITEM_TYPE_RIKUSO_PAYMENT))))));

		final AggregationOperation othersTotal = context -> new Document("$addFields",
				new Document("others", new Document("$sum", Arrays.asList("$totalTaxIncluded",
						"$sumOfCharges", "$rikusoCharge"))));

		final ProjectionOperation project = Aggregation.project()
				.andInclude("stockNo", "chassisNo", "status", "soldPrice", "shuppinCommission", "shuppinTax",
						"soldCommission", "soldCommTax", "tax", "taxAmount", "shuppinTaxAmount", "soldTaxAmount")
				.and("recycleAmount").as("recycleClaimed").and("purchaseCost").as("purchaseCost")
				.and("purchaseCostTaxAmount").as("purchaseCostTaxAmount").and("commision").as("commision")
				.and("commisionTaxAmount").as("commisionTaxAmount").and("roadTax").as("roadTax").and("otherCharges")
				.as("otherCharges").and("othersCostTaxAmount").as("othersCostTaxAmount").and("recycle").as("recycle")
				.and("soldDate").as("soldDate").and("stockType").as("stockType").and("firstName").as("firstName")
				.and("lastName").as("lastName").and("others").as("others");

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupPurchaseInvoice, purchaseVehicles,
				Aggregation.unwind("$purchaseItem", true), rikusoVehicles, Aggregation.unwind("$rikusoItem", true),
				lookupStock, Aggregation.unwind("$stockDetails", true), lookupCustomer,
				Aggregation.unwind("$customer", true), lookupTransportInvoice,
				Aggregation.unwind("$transportInvoice", true), lookupForwarderInvoice,
				Aggregation.unwind("$forwarderInvoice", true), groupOperationTotal, othersTotal, project);
		final AggregationResults<ProfitLossDto> result = mongoTemplate.aggregate(aggregation, "t_re_actn",
				ProfitLossDto.class);
		return result.getMappedResults();

	}

}
