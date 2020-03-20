package com.nexware.aajapan.repositories.custom.impl;

import static java.util.stream.Collectors.toMap;

import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AccumulatorOperators;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators.Filter;
import org.springframework.data.mongodb.core.aggregation.ComparisonOperators.Eq;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;
import org.springframework.data.mongodb.core.aggregation.CountOperation;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.client.result.UpdateResult;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.datatable.DataTableColumnSpecs;
import com.nexware.aajapan.datatable.DataTableRequest;
import com.nexware.aajapan.datatable.PaginationCriteria;
import com.nexware.aajapan.dto.BLDto;
import com.nexware.aajapan.dto.BranchSalesOrderDto;
import com.nexware.aajapan.dto.GLDto;
import com.nexware.aajapan.dto.InspectionDto;
import com.nexware.aajapan.dto.InventoryValueDto;
import com.nexware.aajapan.dto.MLoginDto;
import com.nexware.aajapan.dto.PurchasedDto;
import com.nexware.aajapan.dto.SalesStockSearchDto;
import com.nexware.aajapan.dto.ShippingAvailableStockDto;
import com.nexware.aajapan.dto.ShippingStockSearchDto;
import com.nexware.aajapan.dto.SpecialUserDto;
import com.nexware.aajapan.dto.SpecialUserStockSearchDto;
import com.nexware.aajapan.dto.StockDetailsDto;
import com.nexware.aajapan.dto.StockDocumentsDto;
import com.nexware.aajapan.dto.StockFilter;
import com.nexware.aajapan.dto.StockReserveTableShowDto;
import com.nexware.aajapan.dto.StockSearchDto;
import com.nexware.aajapan.dto.TLastLapVehiclesDto;
import com.nexware.aajapan.dto.TStockDto;
import com.nexware.aajapan.dto.TStockMakerModelDto;
import com.nexware.aajapan.dto.TStockShippingListDto;
import com.nexware.aajapan.dto.TransportRearrangeDto;
import com.nexware.aajapan.models.FBFeedDto;
import com.nexware.aajapan.models.MLogin;
import com.nexware.aajapan.models.ReservedInfo;
import com.nexware.aajapan.models.TPurchaseInvoice;
import com.nexware.aajapan.models.TReAuction;
import com.nexware.aajapan.models.TStock;
import com.nexware.aajapan.models.TransportInfo;
import com.nexware.aajapan.repositories.custom.StockRepositoryCustom;
import com.nexware.aajapan.services.SecurityService;
import com.nexware.aajapan.utils.AppUtil;

@Service
public class StockRepositoryCustomImpl implements StockRepositoryCustom {
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private MongoOperations mongoOperations;
	@Autowired
	private SecurityService securityService;

	@Override
	public List<PurchasedDto> getAllPurchasedData(String invoiceItemType) {

		final Criteria criteria = new Criteria();
		final ArrayList<Criteria> andOperators = new ArrayList<>();
		andOperators.add(Criteria.where("status").is(Constants.INV_STATUS_NEW));

		if (!invoiceItemType.equals("accounts")) {
			andOperators.add(Criteria.where("type").is(Constants.PURCHASE_INVOICE_ITEM_TYPE_PURCHASE));
			andOperators.add(new Criteria().andOperator(Criteria.where("purchaseCostFlag").ne(Constants.FLAG_YES),
					Criteria.where("CommissionFlag").ne(Constants.FLAG_YES),
					Criteria.where("recycleFlag").ne(Constants.FLAG_YES),
					Criteria.where("roadTaxFlag").ne(Constants.FLAG_YES)));
		}
		criteria.andOperator(andOperators.toArray(new Criteria[0]));
		final MatchOperation matchOperation = Aggregation.match(criteria);
		final ProjectionOperation project = Aggregation.project()
				.andInclude("code", "invoiceNo", "type", "purchaseCost", "purchaseCostTax", "purchaseCostTaxAmount",
						"recycle", "commision", "commisionTax", "commisionTaxAmount", "roadTax", "otherCharges",
						"otherChargesTax", "othersCostTaxAmount", "createdDate", "purchaseCostFlag", "CommissionFlag",
						"recycleFlag", "roadTaxFlag")
				.and("stock.stockNo").as("stockNo").and("stock.chassisNo").as("chassisNo").and("stock.recycle")
				.as("sRecycle").and("purchaseType").as("purchaseInfoType").and("stock.purchaseInfo.date")
				.as("purchaseInfoDate").and("stock.purchaseInfo.supplier").as("purchaseInfoSupplier")
				.and("stock.purchaseInfo.auctionInfo.auctionHouse").as("auctionInfoAuctionHouse")
				.and("stock.purchaseInfo.auctionInfo.lotNo").as("auctionInfoLotNo")
				.and("stock.purchaseInfo.auctionInfo.posNo").as("auctionInfoPosNo").and("stock.numberPlate")
				.as("numberPlate").and("stock.inspectionFlag").as("inspectionFlag").and("supplierId").as("supplierCode")
				.and("supplierDetails.company").as("supplierName").and("auctionHouseId").as("auctionHouseId")
				.and("auctionHouseDetails.auctionHouse").as("auctionHouse").and("stock.transportInfo")
				.as("transportInfo").and("stock.maker").as("maker").and("stock.model").as("model").and("stock.category")
				.as("category").and("stock.subcategory").as("subcategory").and("stock.transportInfo.charge")
				.as("charge").and("stock.destinationCountry").as("destinationCountry").and("stock.destinationPort")
				.as("destinationPort").and("stock.transportationStatus").as("transportationStatus")
				.and("stock.lastTransportLocation").as("lastTransportLocation").and("stock.lastTransportLocationCustom")
				.as("lastTransportLocationCustom").and("invoiceDate").as("purchaseDate")
				.and(ConditionalOperators.ifNull("auctionHouseDetails.posNos")
						.thenValueOf("stock.purchaseInfo.auctionInfo.posNo"))
				.as("posNos").and("stock.transportCategory").as("transportCategory");
		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");
		final LookupOperation lookupSupplier = LookupOperation.newLookup().from("m_spplr").localField("supplierId")
				.foreignField("supplierCode").as("supplierDetails");
		final AggregationOperation addAuctionHouse = context -> new Document("$addFields",
				new Document("auctionHouseDetails",
						new Document("$filter", new Document("input", "$supplierDetails.supplierLocations")
								.append("as", "result").append("cond",
										new Document("$eq", Arrays.asList("$$result._id", "$auctionHouseId"))))));
		// sort
		final SortOperation sort = Aggregation.sort(Direction.DESC, "createdDate");
		final Aggregation aggregation = Aggregation.newAggregation(matchOperation, lookupStock,
				Aggregation.unwind("$stock", true), lookupSupplier, Aggregation.unwind("$supplierDetails", true),
				addAuctionHouse, Aggregation.unwind("$auctionHouseDetails", true), project, sort);
		final AggregationResults<PurchasedDto> result = mongoTemplate.aggregate(aggregation, "t_prchs_invc",
				PurchasedDto.class);
		return result.getMappedResults();

	}

	@Override
	public List<TStockDto> getAllPurchaseconfirmedData() {
		final LookupOperation lookupSupplier = LookupOperation.newLookup().from("m_spplr")
				.localField("purchaseInfo.supplier").foreignField("supplierCode").as("supplier");
		final LookupOperation lookupPickupLocation = LookupOperation.newLookup().from("m_lctn")
				.localField("transportInfo.pickupLocation").foreignField("code").as("pickuplocation");
		final LookupOperation lookupDropLocation = LookupOperation.newLookup().from("m_lctn")
				.localField("transportInfo.dropLocation").foreignField("code").as("dropLocation");
		final LookupOperation lookupTransportOrderItems = LookupOperation.newLookup().from("trnsprt_ordr_items")
				.localField("stockNo").foreignField("stockNo").as("transportOrderDetails");

		final AggregationOperation addTransportInfo = context -> new Document("$addFields",
				new Document("transport_info",
						new Document("$filter", new Document("input", "$transportOrderDetails").append("as", "result")
								.append("cond", new Document(new Document("$ne",
										Arrays.asList("$$result.status", Constants.TRANSPORT_ITEM_DELIVERED)))))));

		final ProjectionOperation project = Aggregation.project()
				.andInclude("id", "stockNo", "chassisNo", "model", "maker", "category", "subcategory", "oldNumberPlate",
						"destinationCountry", "destinationPort", "transportationCount", "transportCategory",
						"inspectionFlag")
				.and("purchaseInfo.date").as("purchaseInfoDate").and("purchaseInfo.supplier").as("prchsInfoSupplier")
				.and("supplier.company").as("prchsInfoSSupplier").and("transportInfo.status").as("trnsprtInfostatus")
				.and("transportInfo.charge").as("charge").and("purchaseInfo.auctionInfo.lotNo").as("prchsInfoLotNo")
				.and("transport_info.etd").as("etd").and("purchaseInfo.auctionInfo.posNo").as("prchsInfoPosNo")
				.and("purchaseInfo.auctionInfo.auctionHouse").as("prchsInfoAuctionHouse").and("purchaseInfo.type")
				.as("prchsInfoType").and("purchaseInfo.auctionInfo.auctionHouse").as("prchsInfoSAuctionHouse")
				.and("transportInfo.pickupLocation").as("trnsprtInfoPickupLocation")
				.and("transportInfo.pickupLocationCustom").as("trnsprtInfoPickupLocationCustom")
				.and("pickuplocation.displayName").as("trnsprtInfoPickupLocationName").and("transportInfo.dropLocation")
				.as("trnsprtInfoDropLocation").and("transportInfo.dropLocationCustom")
				.as("trnsprtInfoDropLocationCustom").and("transportInfo.transporter").as("transporter")
				.and("dropLocation.displayName").as("trnsprtInfoDropLocationName")

				.and("transportationStatus").as("$transportationStatus").and("lastTransportLocation")
				.as("lastTransportLocation").and("lastTransportLocationCustom").as("$lastTransportLocationCustom")
				.and(ConditionalOperators.ifNull("auctionHouseDetails.posNos")
						.thenValueOf("purchaseInfo.auctionInfo.posNo"))
				.as("posNos");
		/*
		 * .and(ConditionalOperators.when(Criteria.where("transportationCount").ne(0))
		 * .thenValueOf("$transportOrderDetails.lotNo").otherwise(
		 * "$purchaseInfo.auctionInfo.lotNo")) .as("prchsInfoLotNo");
		 */

		final AggregationOperation addAuctionHouse = context -> new Document("$addFields",
				new Document("auctionHouseDetails",
						new Document("$filter", new Document("input", "$supplier.supplierLocations")
								.append("as", "result").append("cond", new Document("$eq",
										Arrays.asList("$$result._id", "$purchaseInfo.auctionInfo.auctionHouse"))))));
		final Criteria criteria = new Criteria();
		criteria.andOperator(
				Criteria.where("status").in(Constants.STOCK_STATUS_PURCHASED_CONFIRMED, Constants.STOCK_STATUS_SOLD));
		MatchOperation matchStock = Aggregation.match(Criteria.where("status").ne(Constants.TRANSPORT_ITEM_DELIVERED));
		final Aggregation aggregation = Aggregation.newAggregation((Aggregation.match(criteria)), lookupSupplier,
				Aggregation.unwind("$supplier", true), addAuctionHouse,
				Aggregation.unwind("$auctionHouseDetails", true), lookupPickupLocation,
				Aggregation.unwind("$pickuplocation", true), lookupDropLocation,
				Aggregation.unwind("$dropLocation", true), lookupTransportOrderItems, addTransportInfo,
				Aggregation.unwind("$transport_info", true), project);
		final AggregationResults<TStockDto> result = mongoTemplate.aggregate(aggregation, "t_stck", TStockDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<TStockDto> getAllMonthWisePurchaseconfirmedData(String period, Date from, Date to) {
		final LookupOperation lookupSupplier = LookupOperation.newLookup().from("m_spplr")
				.localField("purchaseInfo.supplier").foreignField("supplierCode").as("supplier");
		final LookupOperation lookupPickupLocation = LookupOperation.newLookup().from("m_lctn")
				.localField("transportInfo.pickupLocation").foreignField("code").as("pickuplocation");
		final LookupOperation lookupDropLocation = LookupOperation.newLookup().from("m_lctn")
				.localField("transportInfo.dropLocation").foreignField("code").as("dropLocation");
		final LookupOperation lookupTransportOrderItems = LookupOperation.newLookup().from("trnsprt_ordr_items")
				.localField("stockNo").foreignField("stockNo").as("transportOrderDetails");

		final AggregationOperation addTransportInfo = context -> new Document("$addFields",
				new Document("transport_info",
						new Document("$filter", new Document("input", "$transportOrderDetails").append("as", "result")
								.append("cond", new Document(new Document("$ne",
										Arrays.asList("$$result.status", Constants.TRANSPORT_ITEM_DELIVERED)))))));

		final ProjectionOperation project = Aggregation.project()
				.andInclude("id", "stockNo", "chassisNo", "model", "maker", "category", "subcategory", "oldNumberPlate",
						"destinationCountry", "destinationPort", "transportationCount", "transportCategory",
						"inspectionFlag")
				.and("purchaseInfo.date").as("purchaseInfoDate").and("purchaseInfo.supplier").as("prchsInfoSupplier")
				.and("supplier.company").as("prchsInfoSSupplier").and("transportInfo.status").as("trnsprtInfostatus")
				.and("transportOrderDetails.etd").as("etd").and("transportInfo.charge").as("charge")
				.and("purchaseInfo.auctionInfo.lotNo").as("prchsInfoLotNo").and("purchaseInfo.auctionInfo.posNo")
				.as("prchsInfoPosNo").and("purchaseInfo.auctionInfo.auctionHouse").as("prchsInfoAuctionHouse")
				.and("purchaseInfo.type").as("prchsInfoType").and("purchaseInfo.auctionInfo.auctionHouse")
				.as("prchsInfoSAuctionHouse").and("transportInfo.pickupLocation").as("trnsprtInfoPickupLocation")
				.and("transportInfo.pickupLocationCustom").as("trnsprtInfoPickupLocationCustom")
				.and("pickuplocation.displayName").as("trnsprtInfoPickupLocationName").and("transportInfo.dropLocation")
				.as("trnsprtInfoDropLocation").and("transportInfo.dropLocationCustom")
				.as("trnsprtInfoDropLocationCustom").and("transportInfo.transporter").as("transporter")
				.and("dropLocation.displayName").as("trnsprtInfoDropLocationName")

				.and("transportationStatus").as("$transportationStatus").and("lastTransportLocation")
				.as("lastTransportLocation").and("lastTransportLocationCustom").as("$lastTransportLocationCustom")
				.and(ConditionalOperators.ifNull("auctionHouseDetails.posNos")
						.thenValueOf("purchaseInfo.auctionInfo.posNo"))
				.as("posNos");

		final AggregationOperation addAuctionHouse = context -> new Document("$addFields",
				new Document("auctionHouseDetails",
						new Document("$filter", new Document("input", "$supplier.supplierLocations")
								.append("as", "result").append("cond", new Document("$eq",
										Arrays.asList("$$result._id", "$purchaseInfo.auctionInfo.auctionHouse"))))));
		final Criteria criteria = new Criteria();
		criteria.andOperator(
				Criteria.where("status").in(Constants.STOCK_STATUS_PURCHASED_CONFIRMED, Constants.STOCK_STATUS_SOLD),
				Criteria.where("purchaseInfo.date").gte(from).lte(to));
		final Aggregation aggregation = Aggregation.newAggregation((Aggregation.match(criteria)), lookupSupplier,
				Aggregation.unwind("$supplier", true), addAuctionHouse,
				Aggregation.unwind("$auctionHouseDetails", true), lookupPickupLocation,
				Aggregation.unwind("$pickuplocation", true), lookupDropLocation,
				Aggregation.unwind("$dropLocation", true), lookupTransportOrderItems, addTransportInfo,
				Aggregation.unwind("$transport_info", true), project);
		final AggregationResults<TStockDto> result = mongoTemplate.aggregate(aggregation, "t_stck", TStockDto.class);
		return result.getMappedResults();
	}

	@Override
	public void updateStatusByStockNo(Integer status, String stockNo) {
		final Update update = new Update().set("status", status);
		mongoTemplate.updateMulti(Query.query(Criteria.where("stockNo").in(stockNo)), update, TStock.class);

	}

	@Override
	public List<StockReserveTableShowDto> findByReserveStock(List<String> salesPersonIds, Integer isBidding) {
		final Criteria criteria = new Criteria();
		final ArrayList<Criteria> andOperators = new ArrayList<>();
		if (isBidding != -1) {
			andOperators.add(Criteria.where("isBidding").is(isBidding));
		}

		andOperators.add(Criteria.where("reserve").is(Constants.RESERVED));
		andOperators.add(Criteria.where("status").is(Constants.STOCK_STATUS_PURCHASED_CONFIRMED));
		if (!AppUtil.isObjectEmpty(salesPersonIds)) {
			andOperators.add(Criteria.where("reservedInfo.salesPersonId").in(salesPersonIds));
		}
		final LookupOperation lookupCustomer = LookupOperation.newLookup().from("t_cstmr")
				.localField("reservedInfo.customerId").foreignField("code").as("customer");
		final MatchOperation matchOperation = Aggregation.match(criteria);
		criteria.andOperator(andOperators.toArray(new Criteria[0]));
		ProjectionOperation project = null;
		final LookupOperation lookupSalesPersonDetails = LookupOperation.newLookup().from("m_usr")
				.localField("reservedInfo.salesPersonId").foreignField("code").as("salesPersonDetails");
		final LookupOperation lookupHsCode = LookupOperation.newLookup().from("m_hs_code").localField("category")
				.foreignField("category").as("hsCode");
		final LookupOperation lookupVehicleCategory = LookupOperation.newLookup().from("m_vchcl_ctgry")
				.localField("category").foreignField("name").as("vehicleCategory");
		project = Aggregation.project()
				.andInclude("stockNo", "chassisNo", "model", "fob", "category", "maker", "firstRegDate", "hsCode",
						"shippingInstructionStatus", "minSellingPriceInDollar", "inspectionFlag", "destinationCountry",
						"destinationPort", "isBidding", "cc")
				.and("firstRegDate").plus("lastLapVehicles.expiryMilliSeconds").as("expiryDate").and("firstRegDate")
				.plus("lastLapVehicles.warningDaysMilliSeconds").as("warningDate").and("reservedInfo.price").as("price")
				.and("reservedInfo.date").as("date").and("reservedInfo.exchangeRate").as("exchangeRate")
				.and("purchaseInfo.date").as("purchaseDate").and("customer.code").as("customerId")
				.and("customer.firstName").as("firstName").and("customer.lastName").as("lastName")
				.and("customer.nickName").as("nickName").and("customer.companyName").as("companyName")
				.and("$currency_details.symbol").as("currencySymbol").and("$customer.currencyType").as("currency")
				.and("salesPersonDetails.fullname").as("reserveBy").and("$hsCode.hsCode").as("hsCode")
				.and("$vehicleCategory.code").as("vehicleCategoryCode");

		final LookupOperation lookupCurrency = LookupOperation.newLookup().from("m_currency")
				.localField("customer.currencyType").foreignField("currencySeq").as("currency_details");
		final LookupOperation lookupLastLapVehicles = LookupOperation.newLookup().from("m_lt_lp_vhcls")
				.localField("destinationCountry").foreignField("destinationCountry").as("lastLapVehicles");

		SortOperation sort = Aggregation.sort(Direction.DESC, "date");

		final Aggregation aggregation = Aggregation.newAggregation(matchOperation, lookupCustomer,
				Aggregation.unwind("$customer", true), lookupCurrency, Aggregation.unwind("$currency_details", true),
				lookupLastLapVehicles, Aggregation.unwind("$lastLapVehicles", true), lookupSalesPersonDetails,
				Aggregation.unwind("$salesPersonDetails", true), lookupHsCode, Aggregation.unwind("$hsCode", true),
				lookupVehicleCategory, Aggregation.unwind("$vehicleCategory", true), project, sort);
		final AggregationResults<StockReserveTableShowDto> result = mongoTemplate.aggregate(aggregation, TStock.class,
				StockReserveTableShowDto.class);
		return result.getMappedResults();

	}

	@Override
	public Long findByReserveStockCount(List<String> salesPersonIds) {

		final ArrayList<Criteria> andOperators = new ArrayList<>();
		andOperators.add(Criteria.where("reserve").is(Constants.RESERVED));
		andOperators.add(Criteria.where("status").is(Constants.STOCK_STATUS_PURCHASED_CONFIRMED));
		if (!AppUtil.isObjectEmpty(salesPersonIds)) {
			andOperators.add(Criteria.where("reservedInfo.salesPersonId").in(salesPersonIds));
		}
		final MatchOperation matchOperation = Aggregation
				.match(new Criteria().andOperator(andOperators.toArray(new Criteria[0])));

		final CountOperation count = Aggregation.count().as("count");
		final Aggregation aggregation = Aggregation.newAggregation(matchOperation, count);
		final AggregationResults<Document> result = mongoTemplate.aggregate(aggregation, "t_stck", Document.class);
		return Long.valueOf(!AppUtil.isObjectEmpty(result.getUniqueMappedResult())
				? result.getUniqueMappedResult().getInteger("count")
				: 0);
	}

	@Override
	public void updateStatusByStockNos(int status, List<String> stockNos) {
		final Update update = new Update().set("status", status);
		mongoTemplate.updateMulti(Query.query(Criteria.where("stockNo").in(stockNos)), update, TStock.class);
	}

	@Override
	public void updateTransportStatusByStockNos(int status, List<String> stockNos) {
		final Update update = new Update().set("transportInfo.status", status);
		mongoTemplate.updateMulti(Query.query(Criteria.where("stockNo").in(stockNos)), update, TStock.class);

	}

	@Override
	public void updateTransportStatusByStockNo(int status, String stockNo) {
		final Update update = new Update().set("transportInfo.status", status);
		mongoTemplate.updateFirst(Query.query(Criteria.where("stockNo").is(stockNo)), update, TStock.class);
	}

	@Override
	public ShippingStockSearchDto getStockShippingList(DataTableRequest<TStockShippingListDto> dataTableInRQ,
			StockFilter filter) {

		// global search
		final Criteria globalSearch = new Criteria();
		final List<Criteria> criterias = new ArrayList<>();
		if (!dataTableInRQ.getSearch().isEmpty()) {
			dataTableInRQ.getColumns().stream().filter(DataTableColumnSpecs::isSearchable)
					.forEach(column -> criterias.add(Criteria.where(column.getName())
							.regex(Pattern.compile("(?sim).*" + dataTableInRQ.getSearch() + ".*")))

					);
		}
		final Criteria matchCriteria = new Criteria();
		final Criteria matchCriteriaPurchase = new Criteria();
		final List<Criteria> andCriteriasPurchase = new ArrayList<>();
		final List<Criteria> andCriterias = new ArrayList<>();

		if (!criterias.isEmpty()) {
			globalSearch.orOperator(criterias.toArray(new Criteria[0]));
			andCriterias.add(globalSearch);
		}

		if (!AppUtil.isObjectEmpty(filter.getFlag())) {
			if (filter.getFlag() == 0) {
				andCriterias.add(Criteria.where("status").is(Constants.STOCK_STATUS_PURCHASED_CONFIRMED));
			} else if (filter.getFlag() == 1) {
				andCriterias.add(Criteria.where("reserve").is(Constants.RESERVED));
			} else if (filter.getFlag() == 2) {
				andCriterias.add(Criteria.where("status").is(Constants.STOCK_STATUS_SOLD));
			}
		}
		if (!AppUtil.isObjectEmpty(filter.getAccount())) {
			if (filter.getAccount() == Constants.ACCOUNT_AAJ) {
				andCriterias.add(Criteria.where("account").is(Constants.ACCOUNT_AAJ));
			} else if (filter.getAccount() == Constants.ACCOUNT_SOMO) {
				andCriterias.add(Criteria.where("account").is(Constants.ACCOUNT_SOMO));
			}
		}

		// filter driven
		if (!AppUtil.isObjectEmpty(filter.getDriven())) {
			andCriterias.add(Criteria.where("driven").is(filter.getDriven()));
		}

		// filter by transmission
		if (!AppUtil.isObjectEmpty(filter.getTransmissions())) {
			andCriterias.add(Criteria.where("transmission").in(Arrays.asList(filter.getTransmissions())));
		}
		// filter by makers
		if (!AppUtil.isObjectEmpty(filter.getMakers())) {
			andCriterias.add(Criteria.where("maker").in(Arrays.asList(filter.getMakers())));
		}
		// filter by models
		if (!AppUtil.isObjectEmpty(filter.getModels())) {
			andCriterias.add(Criteria.where("model").in(Arrays.asList(filter.getModels())));
		}
		// filter by submodel
		/*
		 * if (!AppUtil.isObjectEmpty(filter.getSubModels())) {
		 * andCriterias.add(Criteria.where("subModel").in(Arrays.asList(filter.
		 * getSubModels()))); }
		 */
		// filter by Mileage
		if (!AppUtil.isObjectEmpty(filter.getMileageMin())) {
			andCriterias.add(Criteria.where("mileage").gte(filter.getMileageMin()));
		}
		if (!AppUtil.isObjectEmpty(filter.getMileageMax())) {
			andCriterias.add(Criteria.where("mileage").lte(filter.getMileageMax()));
		}
		if (!AppUtil.isObjectEmpty(filter.getPriceMin())) {
			andCriteriasPurchase.add(Criteria.where("buyingPrice").gte(filter.getPriceMin()));
		}
		if (!AppUtil.isObjectEmpty(filter.getPriceMax())) {
			andCriteriasPurchase.add(Criteria.where("buyingPrice").lte(filter.getPriceMax()));
		}
		if (!AppUtil.isObjectEmpty(filter.getFobMin())) {
			andCriterias.add(Criteria.where("fob").gte(filter.getFobMin()));
		}
		if (!AppUtil.isObjectEmpty(filter.getFobMax())) {
			andCriterias.add(Criteria.where("fob").lte(filter.getFobMax()));
		}
		// filter by cc
		if (!AppUtil.isObjectEmpty(filter.getCcMin())) {
			andCriterias.add(Criteria.where("cc").gte(filter.getCcMin()));
		}
		if (!AppUtil.isObjectEmpty(filter.getCcMax())) {
			andCriterias.add(Criteria.where("cc").lte(filter.getCcMax()));
		}
		// filter by lot no
		if (!AppUtil.isObjectEmpty(filter.getLotNos())) {
			andCriterias.add(Criteria.where("purchaseInfo.auctionInfo.lotNo").in(Arrays.asList(filter.getLotNos())));
		}

		if (!AppUtil.isObjectEmpty(filter.getOptions())) {
			andCriterias.add(Criteria.where("equipment").in(Arrays.asList(filter.getOptions())));
		}
		// filter by modelType
		if (!AppUtil.isObjectEmpty(filter.getModelTypes())) {
			andCriterias.add(Criteria.where("modelType").in(Arrays.asList(filter.getModelTypes())));
		}
		if (!AppUtil.isObjectEmpty(filter.getDestinationCountry())) {
			andCriterias.add(Criteria.where("destinationCountry").is(filter.getDestinationCountry()));
		}
		// filter by colors
		if (!AppUtil.isObjectEmpty(filter.getColors())) {
			andCriterias.add(Criteria.where("color").in(Arrays.asList(filter.getColors())));
		}
		// filter by stockNo
		if (!AppUtil.isObjectEmpty(filter.getStockNos())) {
			andCriterias.add(Criteria.where("stockNo").in(Arrays.asList(filter.getStockNos())));
		}
		// filter by grade
		if (!AppUtil.isObjectEmpty(filter.getGrades())) {
			andCriterias.add(Criteria.where("grade").in(Arrays.asList(filter.getGrades())));
		}

		// filter by auction grade
		if (!AppUtil.isObjectEmpty(filter.getAuctionGrades())) {
			andCriterias.add(Criteria.where("auctionGrade").in(Arrays.asList(filter.getAuctionGrades())));
		}
		// filter purchase date
		if (!AppUtil.isObjectEmpty(filter.getPurchaseDateFrom())) {
			andCriterias.add(Criteria.where("purchaseInfo.date").lte(filter.getPurchaseDateTo()));

		}
		if (!AppUtil.isObjectEmpty(filter.getPurchaseDateTo())) {
			andCriterias.add(Criteria.where("purchaseInfo.date").gte(filter.getPurchaseDateFrom()));
		}
		// vehicle categories
		if (!AppUtil.isObjectEmpty(filter.getVehicleCategories())) {
			andCriterias.add(Criteria.where("subcategory").in(Arrays.asList(filter.getVehicleCategories())));
		}
		// year filter

		if (!AppUtil.isObjectEmpty(filter.getYearMax())) {
			andCriterias.add(Criteria.where("firstRegDate").lte(filter.getYearMax()));

		}
		if (!AppUtil.isObjectEmpty(filter.getYearMin())) {
			andCriterias.add(Criteria.where("firstRegDate").gte(filter.getYearMin()));
		}
		matchCriteria.andOperator(andCriterias.toArray(new Criteria[0]));
		final MatchOperation match = Aggregation.match(matchCriteria);
		final MatchOperation matchPrice = Aggregation.match(matchCriteriaPurchase);

		// look up purchase invoice
		final LookupOperation lookupPurchaseInvoice = LookupOperation.newLookup().from("t_prchs_invc")
				.localField("stockNo").foreignField("stockNo").as("purchaseInvoice");
		// match purchase type
		final MatchOperation matchPurchase = Aggregation
				.match(Criteria.where("purchaseInvoice.type").is(Constants.PURCHASE_INVOICE_ITEM_TYPE_PURCHASE));
		// project stock values
		final Fields fields = Fields.fields("id", "stockNo", "reserve", "chassisNo", "firstRegDate", "model",
				"category", "subcategory", "maker", "modelType", "grade", "auctionGrade", "transmission", "noOfDoors",
				"noOfSeat", "fuel", "driven", "mileage", "color", "orgin", "cc", "recycle", "numberPlate",
				"oldNumberPlate", "optionDescription", "remarks", "equipment", "extraAccessories", "status",
				"destinationCountry", "reservedInfo", "purchaseInfo", "isLocked", "lockedBy", "fob", "buyingPrice");
		final ProjectionOperation project = Aggregation.project().andInclude(fields).and("purchaseInvoice.purchaseCost")
				.as("purchaseCost").and("purchaseInvoice.commisionTax").as("commisionTax")
				.and("purchaseInvoice.recycle").as("recycleAmount").and("purchaseInvoice.purchaseCostTax")
				.as("purchaseCostTax").and("purchaseInvoice.commision").as("commision")
				.and("purchaseInvoice.otherCharges").as("otherCharges").and("purchaseInvoice.roadTax").as("roadTax")
				.and("purchaseInvoice.code").as("code");
		// group by purchase invoice code
		final GroupOperation groupPurchaseInvoice = Aggregation.group("code").first("stockNo").as("stockNo")
				.first("chassisNo").as("chassisNo").first("purchaseCost").as("purchaseCost").first("commisionTax")
				.as("commisionTax").first("recycleAmount").as("recycleAmount").first("purchaseCostTax")
				.as("purchaseCostTax").first("commision").as("commision").first("otherCharges").as("otherCharges")
				.first("roadTax").as("roadTax").first("id").as("id").first("reserve").as("reserve")
				.first("firstRegDate").as("firstRegDate").first("model").as("model").first("category").as("category")
				.first("subcategory").as("subcategory").first("maker").as("maker").first("modelType").as("modelType")
				.first("grade").as("grade").first("auctionGrade").as("auctionGrade").first("transmission")
				.as("transmission").first("noOfDoors").as("noOfDoors").first("noOfSeat").as("noOfSeat").first("fuel")
				.as("fuel").first("driven").as("driven").first("mileage").as("mileage").first("color").as("color")
				.first("orgin").as("orgin").first("cc").as("cc").first("recycle").as("recycle").first("numberPlate")
				.as("numberPlate").first("oldNumberPlate").as("oldNumberPlate").first("optionDescription")
				.as("optionDescription").first("remarks").as("remarks").first("equipment").as("equipment")
				.first("extraAccessories").as("extraAccessories").first("status").as("status")
				.first("destinationCountry").as("destinationCountry").first("reservedInfo").as("reservedInfo")
				.first("purchaseInfo").as("purchaseInfo").first("isLocked").as("isLocked").first("lockedBy")
				.as("lockedBy").first("fob").as("fob").first("buyingPrice").as("buyingPrice");
		// calculate buying price
		final AggregationOperation buyingPrice = context -> new Document("$addFields",
				new Document("buyingPrice", new Document("$add", Arrays.asList(
						new Document("$add",
								Arrays.asList(new Document("$multiply",
										Arrays.asList(new Document("$divide", Arrays.asList("$purchaseCostTax", 100)),
												"$purchaseCost")))),
						"$purchaseCost",
						new Document("$add",
								Arrays.asList(new Document("$add", Arrays.asList(new Document("$multiply",
										Arrays.asList(new Document("$divide", Arrays.asList("$commisionTax", 100)),
												"$commision")))),
										"$commision")),
						"$roadTax", "$recycleAmount", "$otherCharges"))));

		// get pagination request parameters
		final PaginationCriteria pagination = dataTableInRQ.getPaginationRequest();

		// sort
		final SortOperation sort = Aggregation.sort(Direction.DESC, "purchaseInfo.date");
		final GroupOperation groupByStock = Aggregation.group("$stockNo")
				.sum(AccumulatorOperators.Sum.sumOf("buyingPrice")).as("buyingPrice").first("stockNo").as("stockNo")
				.first("chassisNo").as("chassisNo").first("purchaseCost").as("purchaseCost").first("commisionTax")
				.as("commisionTax").sum(AccumulatorOperators.Sum.sumOf("recycleAmount")).as("recycleAmount")
				.first("purchaseCostTax").as("purchaseCostTax").first("commision").as("commision").first("otherCharges")
				.as("otherCharges").first("roadTax").as("roadTax").first("id").as("id").first("reserve").as("reserve")
				.first("firstRegDate").as("firstRegDate").first("model").as("model").first("category").as("category")
				.first("subcategory").as("subcategory").first("maker").as("maker").first("modelType").as("modelType")
				.first("grade").as("grade").first("auctionGrade").as("auctionGrade").first("transmission")
				.as("transmission").first("noOfDoors").as("noOfDoors").first("noOfSeat").as("noOfSeat").first("fuel")
				.as("fuel").first("driven").as("driven").first("mileage").as("mileage").first("color").as("color")
				.first("orgin").as("orgin").first("cc").as("cc").first("recycle").as("recycle").first("numberPlate")
				.as("numberPlate").first("oldNumberPlate").as("oldNumberPlate").first("optionDescription")
				.as("optionDescription").first("remarks").as("remarks").first("equipment").as("equipment")
				.first("extraAccessories").as("extraAccessories").first("status").as("status")
				.first("destinationCountry").as("destinationCountry").first("reservedInfo").as("reservedInfo")
				.first("purchaseInfo").as("purchaseInfo").first("isLocked").as("isLocked").first("lockedBy")
				.as("lockedBy").first("fob").as("fob");

		// for total count
		final AggregationOperation group = context -> new Document("$group",
				new Document("_id", null).append("recordsTotal", new Document("$sum", 1)).append("listOfDataObjects",
						new Document("$push", "$$ROOT")));

		final AggregationOperation finalproject = context -> new Document("$project",
				new Document("_id", 0).append("recordsTotal", 1).append("listOfDataObjects", new Document("$slice",
						Arrays.asList("$listOfDataObjects", pagination.getPageNumber(), pagination.getPageSize()))));

		Aggregation aggregation;
		if (!andCriteriasPurchase.isEmpty()) {
			matchCriteriaPurchase.andOperator(andCriteriasPurchase.toArray(new Criteria[0]));
			aggregation = Aggregation.newAggregation(match, lookupPurchaseInvoice,
					Aggregation.unwind("$purchaseInvoice", true), matchPurchase, project, groupPurchaseInvoice,
					buyingPrice, Aggregation.unwind("$buyingPrice", true), matchPrice, sort, groupByStock, group,
					finalproject);
		} else {
			aggregation = Aggregation.newAggregation(match, lookupPurchaseInvoice,
					Aggregation.unwind("$purchaseInvoice", true), matchPurchase, project, groupPurchaseInvoice,
					buyingPrice, Aggregation.unwind("$buyingPrice", true), matchPrice, sort, groupByStock, group,
					finalproject);
		}
		final AggregationResults<ShippingStockSearchDto> result = mongoTemplate.aggregate(aggregation, TStock.class,
				ShippingStockSearchDto.class);
		return result.getUniqueMappedResult();
	}

	@Override
	public SalesStockSearchDto getSalesStockSearchList(StockFilter filter) {
		final Fields fields = Fields.fields("id", "stockNo", "reserve", "chassisNo", "firstRegDate", "sFirstRegDate",
				"model", "category", "subcategory", "maker", "modelType", "grade", "auctionGrade", "transmission",
				"noOfDoors", "noOfSeat", "fuel", "driven", "mileage", "color", "orgin", "cc", "recycle", "numberPlate",
				"oldNumberPlate", "optionDescription", "remarks", "equipment", "extraAccessories", "status",
				"destinationCountry", "reservedInfo", "purchaseInfo", "isLocked", "lockedBy", "lockedBySalesPersonName",
				"fob", "thresholdRange", "buyingPrice", "shipmentType");
		final ProjectionOperation project = Aggregation.project(fields);
//				.and("purchaseInvoice.purchaseCost")
//				.as("purchaseCost").and("purchaseInvoice.commisionTax").as("commisionTax")
//				.and("purchaseInvoice.recycle").as("recycleAmount").and("purchaseInvoice.purchaseCostTax")
//				.as("purchaseCostTax").and("purchaseInvoice.commision").as("commision")
//				.and("purchaseInvoice.otherCharges").as("otherCharges").and("purchaseInvoice.roadTax").as("roadTax")
//				.and("location.shipmentType").as("shipmentType");
//		final LookupOperation lookupPurchaseInvoice = LookupOperation.newLookup().from("t_prchs_invc")
//				.localField("stockNo").foreignField("stockNo").as("purchaseInvoice");
//		final LookupOperation lookupLocation = LookupOperation.newLookup().from("m_lctn")
//				.localField("lastTransportLocation").foreignField("code").as("location");
//		final AggregationOperation buyingPrice = context -> new Document("$addFields",
//				new Document("buyingPrice",
//						new Document("$add",
//								Arrays.asList(
//										new Document("$add",
//												Arrays.asList(new Document("$multiply",
//														Arrays.asList(new Document("$divide",
//																Arrays.asList("$purchaseInvoice.purchaseCostTax", 100)),
//																"$purchaseInvoice.purchaseCost")))),
//										"$purchaseInvoice.purchaseCost",
//										new Document("$add",
//												Arrays.asList(
//														new Document("$add",
//																Arrays.asList(new Document("$multiply", Arrays.asList(
//																		new Document("$divide", Arrays.asList(
//																				"$purchaseInvoice.commisionTax", 100)),
//																		"$purchaseInvoice.commision")))),
//														"$purchaseInvoice.commision"))))));

		// get pagination request parameters
//		final PaginationCriteria pagination = dataTableInRQ.getPaginationRequest();
		// global search
		final Criteria globalSearch = new Criteria();
		final List<Criteria> criterias = new ArrayList<>();
//		if (!dataTableInRQ.getSearch().isEmpty()) {
//			dataTableInRQ.getColumns().stream().filter(DataTableColumnSpecs::isSearchable)
//					.forEach(column -> criterias.add(Criteria.where(column.getName())
//							.regex(Pattern.compile("(?sim).*" + dataTableInRQ.getSearch() + ".*")))
//
//					);
//		}
		final Criteria matchCriteria = new Criteria();
		final Criteria matchCriteriaPurchase = new Criteria();
		final List<Criteria> andCriteriasPurchase = new ArrayList<>();
		final List<Criteria> andCriterias = new ArrayList<>();

		if (!criterias.isEmpty()) {
			globalSearch.orOperator(criterias.toArray(new Criteria[0]));
			andCriterias.add(globalSearch);
		}

		if (!AppUtil.isObjectEmpty(filter.getFlag())) {
			if (filter.getFlag() == 0) {
				andCriterias.add(Criteria.where("reserve").is(Constants.NOT_RESERVED).andOperator(
						Criteria.where("status").nin(Constants.STOCK_STATUS_NEW, Constants.STOCK_STATUS_SOLD)));
				andCriterias.add(Criteria.where("showForSales").is(Constants.SHOW_FOR_SALES));
			} else if (filter.getFlag() == 1) {
				andCriterias.add(Criteria.where("reserve").is(Constants.RESERVED).andOperator(
						Criteria.where("status").nin(Constants.STOCK_STATUS_NEW, Constants.STOCK_STATUS_SOLD)));
				andCriterias.add(Criteria.where("showForSales").is(Constants.SHOW_FOR_SALES));
			} else if (filter.getFlag() == 2) {
				andCriterias.add(Criteria.where("status").is(Constants.STOCK_STATUS_SOLD));
				andCriterias.add(Criteria.where("showForSales").is(Constants.SHOW_FOR_SALES));
			} else if (filter.getFlag() == 3) {
				andCriterias.add(new Criteria().orOperator(Criteria.where("status").is(Constants.STOCK_STATUS_SOLD),
						Criteria.where("reserve").is(Constants.RESERVED)));
				andCriterias.add(Criteria.where("showForSales").is(Constants.SHOW_FOR_SALES));
			} else if (filter.getFlag() == 4) {
				andCriterias.add(new Criteria().orOperator(Criteria.where("status").is(Constants.STOCK_STATUS_NEW)));
			}
		}
		if (!AppUtil.isObjectEmpty(filter.getAccount())) {
			if (filter.getAccount() == Constants.ACCOUNT_AAJ) {
				andCriterias.add(Criteria.where("account").is(Constants.ACCOUNT_AAJ));
			} else if (filter.getAccount() == Constants.ACCOUNT_SOMO) {
				andCriterias.add(Criteria.where("account").is(Constants.ACCOUNT_SOMO));
			}
		}
		// show only stock approved
		// filter driven
		if (!AppUtil.isObjectEmpty(filter.getDriven())) {
			andCriterias.add(Criteria.where("driven").is(filter.getDriven()));
		}

		// filter by transmission
		if (!AppUtil.isObjectEmpty(filter.getTransmissions())) {
			andCriterias.add(Criteria.where("transmission").in(Arrays.asList(filter.getTransmissions())));
		}
		// filter by makers
		if (!AppUtil.isObjectEmpty(filter.getMakers())) {
			andCriterias.add(Criteria.where("maker").in(Arrays.asList(filter.getMakers())));
		}
		// filter by models
		if (!AppUtil.isObjectEmpty(filter.getModels())) {
			andCriterias.add(Criteria.where("model").in(Arrays.asList(filter.getModels())));
		}
		// filter by subModel
		/*
		 * if (!AppUtil.isObjectEmpty(filter.getSubModels())) {
		 * andCriterias.add(Criteria.where("subModel").in(Arrays.asList(filter.
		 * getSubModels()))); }
		 */
		// filter by Mileage
		if (!AppUtil.isObjectEmpty(filter.getMileageMin())) {
			andCriterias.add(Criteria.where("mileage").gte(filter.getMileageMin()));
		}
		if (!AppUtil.isObjectEmpty(filter.getMileageMax())) {
			andCriterias.add(Criteria.where("mileage").lte(filter.getMileageMax()));
		}
		if (!AppUtil.isObjectEmpty(filter.getPriceMin())) {
			andCriteriasPurchase.add(Criteria.where("buyingPrice").gte(filter.getPriceMin()));
		}
		if (!AppUtil.isObjectEmpty(filter.getPriceMax())) {
			andCriteriasPurchase.add(Criteria.where("buyingPrice").lte(filter.getPriceMax()));
		}
		if (!AppUtil.isObjectEmpty(filter.getFobMin())) {
			andCriterias.add(Criteria.where("fob").gte(filter.getFobMin()));
		}
		if (!AppUtil.isObjectEmpty(filter.getFobMax())) {
			andCriterias.add(Criteria.where("fob").lte(filter.getFobMax()));
		}
		// filter by cc
		if (!AppUtil.isObjectEmpty(filter.getCcMin())) {
			andCriterias.add(Criteria.where("cc").gte(filter.getCcMin()));
		}
		if (!AppUtil.isObjectEmpty(filter.getCcMax())) {
			andCriterias.add(Criteria.where("cc").lte(filter.getCcMax()));
		}
		// filter by lot no
		if (!AppUtil.isObjectEmpty(filter.getLotNos())) {
			andCriterias.add(Criteria.where("purchaseInfo.auctionInfo.lotNo").in(Arrays.asList(filter.getLotNos())));
		}

		if (!AppUtil.isObjectEmpty(filter.getOptions())) {
			andCriterias.add(Criteria.where("equipment").in(Arrays.asList(filter.getOptions())));
		}
		// filter by modelType
		if (!AppUtil.isObjectEmpty(filter.getModelTypes())) {
			andCriterias.add(Criteria.where("modelType").in(Arrays.asList(filter.getModelTypes())));
		}
		if (!AppUtil.isObjectEmpty(filter.getDestinationCountry())) {
			andCriterias.add(Criteria.where("destinationCountry").is(filter.getDestinationCountry()));
		}
		// filter by colors
		if (!AppUtil.isObjectEmpty(filter.getColors())) {
			andCriterias.add(Criteria.where("color").in(Arrays.asList(filter.getColors())));
		}
		// filter by grade
		if (!AppUtil.isObjectEmpty(filter.getGrades())) {
			andCriterias.add(Criteria.where("grade").in(Arrays.asList(filter.getGrades())));
		}
		// filter by auction grade
		if (!AppUtil.isObjectEmpty(filter.getAuctionGrades())) {
			andCriterias.add(Criteria.where("auctionGrade").in(Arrays.asList(filter.getAuctionGrades())));
		}
		// filter by StockNo
		if (!AppUtil.isObjectEmpty(filter.getStockNos())) {
			andCriterias.add(Criteria.where("stockNo").in(Arrays.asList(filter.getStockNos())));
		}

		// filter purchase date
		if (!AppUtil.isObjectEmpty(filter.getPurchaseDateFrom())) {
			andCriterias.add(Criteria.where("purchaseInfo.date").lte(filter.getPurchaseDateTo()));

		}
		if (!AppUtil.isObjectEmpty(filter.getPurchaseDateTo())) {
			andCriterias.add(Criteria.where("purchaseInfo.date").gte(filter.getPurchaseDateFrom()));
		}
		// vehicle categories
		if (!AppUtil.isObjectEmpty(filter.getVehicleCategories())) {
			andCriterias.add(Criteria.where("subcategory").in(Arrays.asList(filter.getVehicleCategories())));
		}
		// year filter

		if (!AppUtil.isObjectEmpty(filter.getYearMax())) {
			andCriterias.add(Criteria.where("firstRegDate").lte(filter.getYearMax()));

		}
		if (!AppUtil.isObjectEmpty(filter.getYearMin())) {
			andCriterias.add(Criteria.where("firstRegDate").gte(filter.getYearMin()));
		}
		matchCriteria.andOperator(andCriterias.toArray(new Criteria[0]));

		final MatchOperation match = Aggregation.match(matchCriteria);
		final MatchOperation matchPrice = Aggregation.match(matchCriteriaPurchase);
		// sort
		final SortOperation sort = Aggregation.sort(Direction.DESC, "purchaseInfo.date");

		// lookup Purchase invoice
		final LookupOperation lookupCustomer = LookupOperation.newLookup().from("t_cstmr")
				.localField("reservedInfo.customerId").foreignField("code").as("customer");
		final LookupOperation lookupSalesPersonDetails = LookupOperation.newLookup().from("m_usr")
				.localField("reservedInfo.salesPersonId").foreignField("code").as("salesPersonDetails");
		// group by stock no
		final AggregationOperation groupPurchaseInvoice = context -> new Document("$group",
				new Document("_id", "$stockNo").append("stockNo", new Document("$first", "$stockNo"))
						.append("stockNo", new Document("$first", "$stockNo"))
						.append("reserve", new Document("$first", "$reserve"))
						.append("chassisNo", new Document("$first", "$chassisNo"))
						.append("firstRegDate", new Document("$first", "$firstRegDate"))
						.append("sFirstRegDate", new Document("$first", "$sFirstRegDate"))
						.append("model", new Document("$first", "$model"))
						.append("category", new Document("$first", "$category"))
						.append("subcategory", new Document("$first", "$subcategory"))
						.append("maker", new Document("$first", "$maker"))
						.append("modelType", new Document("$first", "$modelType"))
						.append("grade", new Document("$first", "$grade"))
						.append("auctionGrade", new Document("$first", "$auctionGrade"))
						.append("transmission", new Document("$first", "$transmission"))
						.append("noOfDoors", new Document("$first", "$noOfDoors"))
						.append("noOfSeat", new Document("$first", "$noOfSeat"))
						.append("fuel", new Document("$first", "$fuel"))
						.append("driven", new Document("$first", "$driven"))
						.append("mileage", new Document("$first", "$mileage"))
						.append("color", new Document("$first", "$color"))
						.append("orgin", new Document("$first", "$orgin")).append("cc", new Document("$first", "$cc"))
						.append("recycle", new Document("$first", "$recycle"))
						.append("numberPlate", new Document("$first", "$numberPlate"))
						.append("oldNumberPlate", new Document("$first", "$oldNumberPlate"))
						.append("optionDescription", new Document("$first", "$optionDescription"))
						.append("remarks", new Document("$first", "$remarks"))
						.append("equipment", new Document("$first", "$equipment"))
						.append("extraAccessories", new Document("$first", "$extraAccessories"))
						.append("status", new Document("$first", "$status"))
						.append("destinationCountry", new Document("$first", "$destinationCountry"))
						.append("reservedInfo", new Document("$first", "$reservedInfo"))
						.append("purchaseInfo", new Document("$first", "$purchaseInfo"))
						.append("isLocked", new Document("$first", "$isLocked"))
						.append("lockedBy", new Document("$first", "$lockedBy"))
						.append("lockedBySalesPersonName", new Document("$first", "$lockedBySalesPersonName"))
						.append("fob", new Document("$first", "$fob"))
						.append("thresholdRange", new Document("$first", "$thresholdRange"))
						.append("customerName", new Document("$first", "$customer.firstName"))
						.append("shipmentType", new Document("$first", "$shipmentType"))
						.append("userName", new Document("$first", "$salesPersonDetails.fullname")));
//						.append("buyingPrice", new Document("$first", "$buyingPrice"))
//						.append("purchaseCost", new Document("$sum", "$purchaseCost"))
//						.append("commisionTax", new Document("$first", "$commisionTax"))
//						.append("recycleAmount", new Document("$sum", "$recycleAmount"))
//						.append("purchaseCostTax", new Document("$first", "$purchaseCostTax"))
//						.append("commision", new Document("$sum", "$commision"))
//						.append("otherCharges", new Document("$sum", "$otherCharges"))
//						.append("roadTax", new Document("$sum", "$roadTax"))

		// for total count
		final AggregationOperation group = context -> new Document("$group",
				new Document("_id", null).append("recordsTotal", new Document("$sum", 1)).append("listOfDataObjects",
						new Document("$push", "$$ROOT")));

		final AggregationOperation finalproject = context -> new Document("$project",
				new Document("_id", 0).append("recordsTotal", 1).append("listOfDataObjects", 1));

		Aggregation aggregation;
		if (!andCriteriasPurchase.isEmpty()) {
			matchCriteriaPurchase.andOperator(andCriteriasPurchase.toArray(new Criteria[0]));
			aggregation = Aggregation.newAggregation(match, project, matchPrice, lookupCustomer,
					Aggregation.unwind("$customer", true), lookupSalesPersonDetails,
					Aggregation.unwind("$salesPersonDetails", true), groupPurchaseInvoice, sort, group, finalproject);
//			lookupPurchaseInvoice, Aggregation.unwind("$purchaseInvoice", true), buyingPrice, Aggregation.unwind("$buyingPrice", true), 
//			lookupLocation, Aggregation.unwind("$location", true),
		} else {
			aggregation = Aggregation.newAggregation(match, project, lookupCustomer,
					Aggregation.unwind("$customer", true), lookupSalesPersonDetails,
					Aggregation.unwind("$salesPersonDetails", true), groupPurchaseInvoice, sort, group, finalproject);
		}

		final AggregationResults<SalesStockSearchDto> result = mongoTemplate.aggregate(aggregation, TStock.class,
				SalesStockSearchDto.class);
		return result.getUniqueMappedResult();
	}

	@Override
	public SpecialUserDto findOneSpecialUserByStockNo(String stockNo) {
		final LookupOperation lookupPurchaseInvoice = LookupOperation.newLookup().from("t_prchs_invc")
				.localField("stockNo").foreignField("stockNo").as("purchaseInvoice");
		final LookupOperation lookupTransportInvoice = LookupOperation.newLookup().from("trnsprt_invc")
				.localField("stockNo").foreignField("stockNo").as("transportInvoice");

		final Fields fields = Fields.fields("id", "stockNo", "reserve", "chassisNo", "firstRegDate", "model",
				"category", "subcategory", "maker", "modelType", "grade", "auctionGrade", "transmission", "noOfDoors",
				"noOfSeat", "fuel", "driven", "mileage", "color", "orgin", "cc", "recycle", "numberPlate", "account",
				"oldNumberPlate", "optionDescription", "remarks", "equipment", "extraAccessories", "status",
				"destinationCountry", "destinationPort", "reservedInfo", "purchaseInfo", "isLocked", "lockedBy", "fob",
				"thresholdRange", "buyingPrice", "offerPrice");
		final ProjectionOperation project = Aggregation.project(fields).and("$purchaseInvoice.purchaseCost")
				.as("purchaseCost").and("$purchaseInvoice.purchaseCostTax").as("purchaseCostTax")
				.and("$purchaseInvoice.purchaseCostTaxAmount").as("purchaseCostTaxAmount")
				.and("$purchaseInvoice.commision").as("commision").and("$purchaseInvoice.commisionTax")
				.as("commisionTax").and("$purchaseInvoice.commisionTaxAmount").as("commisionTaxAmount")
				.and("$purchaseInvoice.recycle").as("recycleAmount").and("$purchaseInvoice.roadTax").as("roadTax")
				.and("$purchaseInvoice.otherCharges").as("otherCharges").and("$purchaseInvoice.otherChargesTax")
				.as("otherChargesTax").and("$purchaseInvoice.othersCostTaxAmount").as("othersCostTaxAmount")

				.and(AccumulatorOperators.Sum.sumOf("$transportInvoice.amount")).as("transportCharge");

		final MatchOperation match = Aggregation.match(Criteria.where("stockNo").is(stockNo));
		// sort
		final SortOperation sort = Aggregation.sort(Direction.DESC, "purchaseInfo.date");
		final AggregationOperation groupPurchaseInvoice = context -> new Document("$group",
				new Document("_id", "$stockNo").append("stockNo", new Document("$first", "$stockNo"))
						.append("reserve", new Document("$first", "$reserve"))
						.append("chassisNo", new Document("$first", "$chassisNo"))
						.append("firstRegDate", new Document("$first", "$firstRegDate"))
						.append("model", new Document("$first", "$model"))
						.append("category", new Document("$first", "$category"))
						.append("subcategory", new Document("$first", "$subcategory"))
						.append("maker", new Document("$first", "$maker"))
						.append("modelType", new Document("$first", "$modelType"))
						.append("grade", new Document("$first", "$grade"))
						.append("auctionGrade", new Document("$first", "$auctionGrade"))
						.append("transmission", new Document("$first", "$transmission"))
						.append("noOfDoors", new Document("$first", "$noOfDoors"))
						.append("noOfSeat", new Document("$first", "$noOfSeat"))
						.append("fuel", new Document("$first", "$fuel"))
						.append("driven", new Document("$first", "$driven"))
						.append("mileage", new Document("$first", "$mileage"))
						.append("color", new Document("$first", "$color"))
						.append("orgin", new Document("$first", "$orgin")).append("cc", new Document("$first", "$cc"))
						.append("recycle", new Document("$first", "$recycle"))
						.append("numberPlate", new Document("$first", "$numberPlate"))
						.append("account", new Document("$first", "$account"))
						.append("oldNumberPlate", new Document("$first", "$oldNumberPlate"))
						.append("optionDescription", new Document("$first", "$optionDescription"))
						.append("remarks", new Document("$first", "$remarks"))
						.append("equipment", new Document("$first", "$equipment"))
						.append("extraAccessories", new Document("$first", "$extraAccessories"))
						.append("status", new Document("$first", "$status"))
						.append("destinationCountry", new Document("$first", "$destinationCountry"))
						.append("destinationPort", new Document("$first", "$destinationPort"))
						.append("reservedInfo", new Document("$first", "$reservedInfo"))
						.append("purchaseInfo", new Document("$first", "$purchaseInfo"))
						.append("isLocked", new Document("$first", "$isLocked"))
						.append("lockedBy", new Document("$first", "$lockedBy"))
						.append("fob", new Document("$first", "$fob"))
						.append("thresholdRange", new Document("$first", "$thresholdRange"))
						.append("buyingPrice", new Document("$first", "$buyingPrice"))
						.append("purchaseCost", new Document("$sum", "$purchaseCost"))
						.append("purchaseCostTax", new Document("$first", "$purchaseCostTax"))
						.append("purchaseCostTaxAmount", new Document("$sum", "$purchaseCostTaxAmount"))
						.append("commision", new Document("$sum", "$commision"))
						.append("commisionTax", new Document("$first", "$commisionTax"))
						.append("commisionTaxAmount", new Document("$sum", "$commisionTaxAmount"))
						.append("recycleAmount", new Document("$sum", "$recycleAmount"))
						.append("roadTax", new Document("$sum", "$roadTax"))
						.append("otherCharges", new Document("$sum", "$otherCharges"))
						.append("otherChargesTax", new Document("$first", "$otherChargesTax"))
						.append("othersCostTaxAmount", new Document("$sum", "$othersCostTaxAmount"))
						.append("transportCharge", new Document("$first", "$transportCharge")));

		Aggregation aggregation = Aggregation.newAggregation(match, lookupPurchaseInvoice, lookupTransportInvoice,
				Aggregation.unwind("$purchaseInvoice", true), project, groupPurchaseInvoice, sort);

		final AggregationResults<SpecialUserDto> result = mongoTemplate.aggregate(aggregation, TStock.class,
				SpecialUserDto.class);
		return result.getUniqueMappedResult();
	}

	@Override
	public SpecialUserStockSearchDto getSpecialUserStockList(StockFilter filter) {
//		final LookupOperation lookupPurchaseInvoice = LookupOperation.newLookup().from("t_prchs_invc")
//				.localField("stockNo").foreignField("stockNo").as("purchaseInvoice");
//		final LookupOperation lookupTransportInvoice = LookupOperation.newLookup().from("trnsprt_invc")
//				.localField("stockNo").foreignField("stockNo").as("transportInvoice");

		final Fields fields = Fields.fields("id", "stockNo", "reserve", "chassisNo", "firstRegDate", "model",
				"category", "subcategory", "maker", "modelType", "grade", "auctionGrade", "transmission", "noOfDoors",
				"noOfSeat", "fuel", "driven", "mileage", "color", "orgin", "cc", "recycle", "numberPlate", "account",
				"oldNumberPlate", "optionDescription", "remarks", "equipment", "extraAccessories", "status",
				"destinationCountry", "destinationPort", "reservedInfo", "purchaseInfo", "isLocked", "lockedBy", "fob",
				"thresholdRange", "buyingPrice", "offerPrice");
		final ProjectionOperation project = Aggregation.project(fields);
//				.and("$purchaseInvoice.purchaseCost")
//				.as("purchaseCost").and("$purchaseInvoice.purchaseCostTax").as("purchaseCostTax")
//				.and("$purchaseInvoice.purchaseCostTaxAmount").as("purchaseCostTaxAmount")
//				.and("$purchaseInvoice.commision").as("commision").and("$purchaseInvoice.commisionTax")
//				.as("commisionTax").and("$purchaseInvoice.commisionTaxAmount").as("commisionTaxAmount")
//				.and("$purchaseInvoice.recycle").as("recycleAmount").and("$purchaseInvoice.roadTax").as("roadTax")
//				.and("$purchaseInvoice.otherCharges").as("otherCharges").and("$purchaseInvoice.otherChargesTax")
//				.as("otherChargesTax").and("$purchaseInvoice.othersCostTaxAmount").as("othersCostTaxAmount")
//				.and(AccumulatorOperators.Sum.sumOf("$transportInvoice.amount")).as("transportCharge");

		// get pagination request parameters
//		final PaginationCriteria pagination = dataTableInRQ.getPaginationRequest();
		// global search
		final Criteria globalSearch = new Criteria();
		final List<Criteria> criterias = new ArrayList<>();
//		if (!dataTableInRQ.getSearch().isEmpty()) {
//			dataTableInRQ.getColumns().stream().filter(DataTableColumnSpecs::isSearchable)
//					.forEach(column -> criterias.add(Criteria.where(column.getName())
//							.regex(Pattern.compile("(?sim).*" + dataTableInRQ.getSearch() + ".*")))
//
//					);
//		}
		final Criteria matchCriteria = new Criteria();
		final Criteria matchCriteriaPurchase = new Criteria();
		final List<Criteria> andCriteriasPurchase = new ArrayList<>();
		final List<Criteria> andCriterias = new ArrayList<>();

		if (!criterias.isEmpty()) {
			globalSearch.orOperator(criterias.toArray(new Criteria[0]));
			andCriterias.add(globalSearch);
		}
		if (!AppUtil.isObjectEmpty(filter.getFlag())) {
			if (filter.getFlag() == 0) {

				andCriterias.add(Criteria.where("showForSales").is(Constants.NOT_SHOW_FOR_SALES).andOperator(
						Criteria.where("status").is(Constants.STOCK_STATUS_PURCHASED_CONFIRMED),
						Criteria.where("isBidding").is(Constants.IS_NOT_BIDDING)));

			} else if (filter.getFlag() == 1) {
				andCriterias.add(new Criteria().andOperator(
						Criteria.where("status").is(Constants.STOCK_STATUS_PURCHASED_CONFIRMED),
						Criteria.where("showForSales").is(Constants.SHOW_FOR_SALES)));
			}
		}
		if (!AppUtil.isObjectEmpty(filter.getAccount())) {
			if (filter.getAccount().equals(Constants.ACCOUNT_AAJ)) {
				andCriterias.add(Criteria.where("account").is(Constants.ACCOUNT_AAJ));
			} else if (filter.getAccount().equals(Constants.ACCOUNT_SOMO)) {
				andCriterias.add(Criteria.where("account").is(Constants.ACCOUNT_SOMO));
			}
		}

		// filter driven
		if (!AppUtil.isObjectEmpty(filter.getDriven())) {
			andCriterias.add(Criteria.where("driven").is(filter.getDriven()));
		}

		// filter by transmission
		if (!AppUtil.isObjectEmpty(filter.getTransmissions())) {
			andCriterias.add(Criteria.where("transmission").in(Arrays.asList(filter.getTransmissions())));
		}
		// filter by makers
		if (!AppUtil.isObjectEmpty(filter.getMakers())) {
			andCriterias.add(Criteria.where("maker").in(Arrays.asList(filter.getMakers())));
		}
		// filter by models
		if (!AppUtil.isObjectEmpty(filter.getModels())) {
			andCriterias.add(Criteria.where("model").in(Arrays.asList(filter.getModels())));
		}
		// filter by submodel

		// filter by Mileage
		if (!AppUtil.isObjectEmpty(filter.getMileageMin())) {
			andCriterias.add(Criteria.where("mileage").gte(filter.getMileageMin()));
		}
		if (!AppUtil.isObjectEmpty(filter.getMileageMax())) {
			andCriterias.add(Criteria.where("mileage").lte(filter.getMileageMax()));
		}
		if (!AppUtil.isObjectEmpty(filter.getPriceMin())) {
			andCriteriasPurchase.add(Criteria.where("buyingPrice").gte(filter.getPriceMin()));
		}
		if (!AppUtil.isObjectEmpty(filter.getPriceMax())) {
			andCriteriasPurchase.add(Criteria.where("buyingPrice").lte(filter.getPriceMax()));
		}
		if (!AppUtil.isObjectEmpty(filter.getFobMin())) {
			andCriterias.add(Criteria.where("fob").gte(filter.getFobMin()));
		}
		if (!AppUtil.isObjectEmpty(filter.getFobMax())) {
			andCriterias.add(Criteria.where("fob").lte(filter.getFobMax()));
		}
		// filter by cc
		if (!AppUtil.isObjectEmpty(filter.getCcMin())) {
			andCriterias.add(Criteria.where("cc").gte(filter.getCcMin()));
		}
		if (!AppUtil.isObjectEmpty(filter.getCcMax())) {
			andCriterias.add(Criteria.where("cc").lte(filter.getCcMax()));
		}
		// filter by lot no
		if (!AppUtil.isObjectEmpty(filter.getLotNos())) {
			andCriterias.add(Criteria.where("purchaseInfo.auctionInfo.lotNo").in(Arrays.asList(filter.getLotNos())));
		}

		if (!AppUtil.isObjectEmpty(filter.getOptions())) {
			andCriterias.add(Criteria.where("equipment").in(Arrays.asList(filter.getOptions())));
		}
		// filter by modelType
		if (!AppUtil.isObjectEmpty(filter.getModelTypes())) {
			andCriterias.add(Criteria.where("modelType").in(Arrays.asList(filter.getModelTypes())));
		}
		if (!AppUtil.isObjectEmpty(filter.getDestinationCountry())) {
			andCriterias.add(Criteria.where("destinationCountry").is(filter.getDestinationCountry()));
		}
		// filter by colors
		if (!AppUtil.isObjectEmpty(filter.getColors())) {
			andCriterias.add(Criteria.where("color").in(Arrays.asList(filter.getColors())));
		}
		// filter by stockNo
		if (!AppUtil.isObjectEmpty(filter.getStockNos())) {
			andCriterias.add(Criteria.where("stockNo").in(Arrays.asList(filter.getStockNos())));
		}
		// filter by grade
		if (!AppUtil.isObjectEmpty(filter.getGrades())) {
			andCriterias.add(Criteria.where("grade").in(Arrays.asList(filter.getGrades())));
		}

		// filter by auction grade
		if (!AppUtil.isObjectEmpty(filter.getAuctionGrades())) {
			andCriterias.add(Criteria.where("auctionGrade").in(Arrays.asList(filter.getAuctionGrades())));
		}
		// filter purchase date
		if (!AppUtil.isObjectEmpty(filter.getPurchaseDateFrom())) {
			andCriterias.add(Criteria.where("purchaseInfo.date").lte(filter.getPurchaseDateTo()));

		}
		if (!AppUtil.isObjectEmpty(filter.getPurchaseDateTo())) {
			andCriterias.add(Criteria.where("purchaseInfo.date").gte(filter.getPurchaseDateFrom()));
		}
		// vehicle categories
		if (!AppUtil.isObjectEmpty(filter.getVehicleCategories())) {
			andCriterias.add(Criteria.where("subcategory").in(Arrays.asList(filter.getVehicleCategories())));
		}
		// year filter

		if (!AppUtil.isObjectEmpty(filter.getYearMax())) {
			andCriterias.add(Criteria.where("firstRegDate").lte(filter.getYearMax()));

		}
		if (!AppUtil.isObjectEmpty(filter.getYearMin())) {
			andCriterias.add(Criteria.where("firstRegDate").gte(filter.getYearMin()));
		}
		matchCriteria.andOperator(andCriterias.toArray(new Criteria[0]));
		final MatchOperation match = Aggregation.match(matchCriteria);
		final MatchOperation matchPrice = Aggregation.match(matchCriteriaPurchase);
		// sort
		final SortOperation sort = Aggregation.sort(Direction.DESC, "purchaseInfo.date");
		final AggregationOperation groupPurchaseInvoice = context -> new Document("$group",
				new Document("_id", "$stockNo").append("stockNo", new Document("$first", "$stockNo"))
						.append("reserve", new Document("$first", "$reserve"))
						.append("chassisNo", new Document("$first", "$chassisNo"))
						.append("firstRegDate", new Document("$first", "$firstRegDate"))
						.append("model", new Document("$first", "$model"))
						.append("category", new Document("$first", "$category"))
						.append("subcategory", new Document("$first", "$subcategory"))
						.append("maker", new Document("$first", "$maker"))
						.append("modelType", new Document("$first", "$modelType"))
						.append("grade", new Document("$first", "$grade"))
						.append("auctionGrade", new Document("$first", "$auctionGrade"))
						.append("transmission", new Document("$first", "$transmission"))
						.append("noOfDoors", new Document("$first", "$noOfDoors"))
						.append("noOfSeat", new Document("$first", "$noOfSeat"))
						.append("fuel", new Document("$first", "$fuel"))
						.append("driven", new Document("$first", "$driven"))
						.append("mileage", new Document("$first", "$mileage"))
						.append("color", new Document("$first", "$color"))
						.append("orgin", new Document("$first", "$orgin")).append("cc", new Document("$first", "$cc"))
						.append("recycle", new Document("$first", "$recycle"))
						.append("numberPlate", new Document("$first", "$numberPlate"))
						.append("account", new Document("$first", "$account"))
						.append("oldNumberPlate", new Document("$first", "$oldNumberPlate"))
						.append("optionDescription", new Document("$first", "$optionDescription"))
						.append("remarks", new Document("$first", "$remarks"))
						.append("equipment", new Document("$first", "$equipment"))
						.append("extraAccessories", new Document("$first", "$extraAccessories"))
						.append("status", new Document("$first", "$status"))
						.append("destinationCountry", new Document("$first", "$destinationCountry"))
						.append("destinationPort", new Document("$first", "$destinationPort"))
						.append("reservedInfo", new Document("$first", "$reservedInfo"))
						.append("purchaseInfo", new Document("$first", "$purchaseInfo"))
						.append("isLocked", new Document("$first", "$isLocked"))
						.append("lockedBy", new Document("$first", "$lockedBy"))
						.append("fob", new Document("$first", "$fob"))
						.append("thresholdRange", new Document("$first", "$thresholdRange"))
						.append("buyingPrice", new Document("$first", "$buyingPrice")));
//						.append("purchaseCost", new Document("$sum", "$purchaseCost"))
//						.append("purchaseCostTax", new Document("$first", "$purchaseCostTax"))
//						.append("purchaseCostTaxAmount", new Document("$sum", "$purchaseCostTaxAmount"))
//						.append("commision", new Document("$sum", "$commision"))
//						.append("commisionTax", new Document("$first", "$commisionTax"))
//						.append("commisionTaxAmount", new Document("$sum", "$commisionTaxAmount"))
//						.append("recycleAmount", new Document("$sum", "$recycleAmount"))
//						.append("roadTax", new Document("$sum", "$roadTax"))
//						.append("otherCharges", new Document("$sum", "$otherCharges"))
//						.append("otherChargesTax", new Document("$first", "$otherChargesTax"))
//						.append("othersCostTaxAmount", new Document("$sum", "$othersCostTaxAmount"))
//						.append("transportCharge", new Document("$first", "$transportCharge")));
		// for total count
		final AggregationOperation group = context -> new Document("$group",
				new Document("_id", null).append("recordsTotal", new Document("$sum", 1)).append("listOfDataObjects",
						new Document("$push", "$$ROOT")));

		final AggregationOperation finalproject = context -> new Document("$project",
				new Document("_id", 0).append("recordsTotal", 1).append("listOfDataObjects", 1));
		Aggregation aggregation;
		if (!andCriteriasPurchase.isEmpty()) {
			matchCriteriaPurchase.andOperator(andCriteriasPurchase.toArray(new Criteria[0]));
			aggregation = Aggregation.newAggregation(match, project, matchPrice, groupPurchaseInvoice, sort, group,
					finalproject);
//			lookupPurchaseInvoice, lookupTransportInvoice, Aggregation.unwind("$purchaseInvoice", true),
		} else {
			aggregation = Aggregation.newAggregation(match, project, groupPurchaseInvoice, sort, group, finalproject);
		}

		final AggregationResults<SpecialUserStockSearchDto> result = mongoTemplate.aggregate(aggregation, TStock.class,
				SpecialUserStockSearchDto.class);
		return result.getUniqueMappedResult();
	}

	@Override
	public void updateTransportInfo(String stockNo, TransportInfo transportInfo) {
		final Update update = new Update().set("transportInfo", transportInfo);
		mongoTemplate.updateMulti(Query.query(Criteria.where("stockNo").is(stockNo)), update, TStock.class);
	}

	@Override
	public List<TransportRearrangeDto> findAllRearrangeItems() {

		final LookupOperation lookupPickupLocation = LookupOperation.newLookup().from("m_lctn")
				.localField("transportInfo.pickupLocation").foreignField("code").as("pickupLocation");
		final LookupOperation lookupDropLocation = LookupOperation.newLookup().from("m_lctn")
				.localField("transportInfo.dropLocation").foreignField("code").as("dropLocation");
		final LookupOperation lookupSupplier = LookupOperation.newLookup().from("m_spplr")
				.localField("purchaseInfo.supplier").foreignField("supplierCode").as("supplierDetails");
		final LookupOperation lookupTransportOrderItems = LookupOperation.newLookup().from("trnsprt_ordr_items")
				.localField("stockNo").foreignField("stockNo").as("transportOrderDetails");
		final ProjectionOperation project = Aggregation.project()
				.andInclude("stockNo", "chassisNo", "category", "subcategory", "numberPlate", "model", "maker",
						"destinationCountry", "destinationPort", "lastModifiedDate", "transportationCount")
				.and(ConditionalOperators.when(Criteria.where("transportationCount").ne(0))
						.thenValueOf("$transportOrderDetails.lotNo").otherwise("$purchaseInfo.auctionInfo.lotNo"))
				.as("lotNo").and("purchaseInfo.auctionInfo.posNo").as("auctionInfoPosNo")
				.and("transportInfo.pickupLocation").as("pickupLocation").and("$pickupLocation.displayName")
				.as("pickupLocationName").and("$dropLocation.displayName").as("dropLocationName")
				.and("transportInfo.pickupLocationCustom").as("pickupLocationCustom").and("transportInfo.dropLocation")
				.as("dropLocation").and("transportInfo.dropLocationCustom").as("dropLocationCustom")
				.and("transportInfo.transporter").as("transporter").and("transportInfo.status").as("status")
				.and("transportInfo.reasonForCancel").as("reason").and("transportInfo.charge").as("charge")
				.and("purchaseInfo.date").as("purchaseDate").and("purchaseInfo.supplier").as("supplierCode")
				.and("$auctionHouseDetails.auctionHouse").as("auctionHouse").and("$auctionHouseDetails._id")
				.as("auctionHouseId").and(ConditionalOperators.ifNull("$auctionHouseDetails.posNos")
						.thenValueOf("purchaseInfo.auctionInfo.posNo"))
				.as("posNos");

		final SortOperation sort = Aggregation.sort(Direction.DESC, "lastModifiedDate");
		final AggregationOperation addAuctionHouse = context -> new Document("$addFields",
				new Document("auctionHouseDetails",
						new Document("$filter", new Document("input", "$supplierDetails.supplierLocations")
								.append("as", "result").append("cond", new Document("$eq",
										Arrays.asList("$$result._id", "$purchaseInfo.auctionInfo.auctionHouse"))))));
		final Aggregation aggregation = Aggregation.newAggregation(
				Aggregation.match(Criteria.where("transportInfo.status").is(Constants.TRANSPORT_ITEM_REARRANGE)),
				lookupPickupLocation, Aggregation.unwind("$pickupLocation", true), lookupDropLocation,
				Aggregation.unwind("$dropLocation", true), lookupSupplier, Aggregation.unwind("$supplierDetails", true),
				addAuctionHouse, Aggregation.unwind("$auctionHouseDetails", true), lookupTransportOrderItems,
				Aggregation.unwind("$transportOrderDetails", true), project, sort);
		final AggregationResults<TransportRearrangeDto> result = mongoTemplate.aggregate(aggregation, "t_stck",
				TransportRearrangeDto.class);
		return result.getMappedResults();
	}

	@Override
	public void updateTransportCancelInfo(String reasonForCancel, int status, String stockNo) {
		final Update update = new Update().set("transportInfo.reasonForCancel", reasonForCancel)
				.set("transportInfo.status", status).set("transportationStatus", Constants.TRANSPORT_IDLE)
				.set("lastModifiedDate", new Date());
		mongoTemplate.updateMulti(Query.query(Criteria.where("stockNo").is(stockNo)), update, TStock.class);
	}

	@Override
	public void updateTransportCancelInfo(String reasonForCancel, int status, List<String> stockNos) {
		final Update update = new Update().set("transportInfo.reasonForCancel", reasonForCancel)
				.set("transportInfo.status", status);
		mongoTemplate.updateMulti(Query.query(Criteria.where("stockNo").in(stockNos)), update, TStock.class);
	}

	@Override
	public void lockStocks(List<String> stockNos, String lockedBy, String lockedBySalesPersonName) {
		final Update update = new Update().set("isLocked", 1).set("lockedBy", lockedBy).set("lockedBySalesPersonName",
				lockedBySalesPersonName);
		mongoTemplate.updateMulti(Query.query(Criteria.where("stockNo").in(stockNos)), update, TStock.class);

	}

	@Override
	public void unLockStocks(List<String> stockNos, String lockedBy, String lockedBySalesPersonName) {
		final Update update = new Update().set("isLocked", 0);
		mongoTemplate.updateMulti(Query.query(Criteria.where("stockNo").in(stockNos)), update, TStock.class);

	}

	@Override
	public List<TStock> findBySearchTerms(String searchterm) {
		return mongoOperations.find(Query.query(Criteria.where("stockNo").regex(".*" + searchterm + ".*", "i")),
				TStock.class);
	}

	@Override
	public List<BLDto> getBLList() {

		MatchOperation match = Aggregation.match(new Criteria().andOperator(Criteria.where("scheduleId").ne(null),
				Criteria.where("status").ne(Constants.SHIPIING_REQUEST_INITIATED),
				Criteria.where("destCountry").nin("AUSTRALIA", "RUSSIA", "GEORGIA", "NEW ZEALAND")));

		final LookupOperation lookupShipmentSchedule = LookupOperation.newLookup().from("t_shpmnt_schdl")
				.localField("scheduleId").foreignField("scheduleId").as("schedule");

		LookupOperation lookupShippingInstruction = LookupOperation.newLookup().from("t_shppng_instructn")
				.localField("shippingInstructionId").foreignField("shippingInstructionId").as("shippingInstruction");

		LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");

		final LookupOperation lookupMLocation = LookupOperation.newLookup().from("m_lctn")
				.localField("stock.lastTransportLocation").foreignField("code").as("location_dtls");

		final AggregationOperation addETDField = (context) -> new Document("$addFields",
				new Document("etd",
						new Document("$filter", new Document("input", "$schedule.schedule").append("as", "result")
								.append("cond", new Document("$and", Arrays.asList(
										new Document("$eq",
												Arrays.asList("$$result.portName",
														"$location_dtls.shipmentOriginPort")),
										new Document("$eq", Arrays.asList("$$result.portFlag", "loading"))))))));

		MatchOperation matchEtd = Aggregation.match(Criteria.where("etd.date").lte(AppUtil.atEndOfDay(new Date())));

		final AggregationOperation addETAField = context -> new Document("$addFields",
				new Document("eta",
						new Document("$filter",
								new Document("input", "$schedule.schedule").append("as", "result").append("cond",
										new Document("$eq", Arrays.asList("$$result.portName", "$destPort"))))));

		LookupOperation lookupUserDetails = LookupOperation.newLookup().from("m_usr")
				.localField("stock.shippingInstructionInfo.salesPersonId").foreignField("code").as("userDetails");

		final LookupOperation lookupsalesInvoice = LookupOperation.newLookup().from("t_sls_inv").localField("stockNo")
				.foreignField("stockNo").as("soldPrice");

		final LookupOperation lookupShip = LookupOperation.newLookup().from("m_ship").localField("schedule.shipId")
				.foreignField("shipId").as("ship");

		final LookupOperation lookupCustomer = LookupOperation.newLookup().from("t_cstmr")
				.localField("shippingInstruction.customerId").foreignField("code").as("customerInfo");

		final AggregationOperation addConsigneeField = context -> new Document("$addFields",
				new Document("consignee",
						new Document("$filter",
								new Document("input", "$customerInfo.consigneeNotifyparties").append("as", "result")
										.append("cond", new Document("$eq",
												Arrays.asList("$$result._id", "$shippingInstruction.consigneeId"))))));

		final LookupOperation lookupDayBookTransaction = LookupOperation.newLookup().from("t_dybk_trnsctn")
				.localField("stockNo").foreignField("stockId").as("dyBkTransaction");

		final AggregationOperation addTransField = (context) -> new Document("$addFields",
				new Document("transaction",
						new Document("$filter",
								new Document("input", "$dyBkTransaction").append("as", "result").append("cond",
										new Document("$and", Arrays.asList(
												// new Document("$ifNull", Arrays.asList("$$result", null)),
												new Document("$eq",
														Arrays.asList("$$result.transactionType",
																Constants.TRANSACTION_CREDIT)),
												new Document("$eq", Arrays.asList("$$result.paymentApprove",
														Constants.DAYBOOK_TRANSACTION_APPROVED))))))));

//		Arrays.asList(new Document("$eq",
//				Arrays.<Object>asList("$dyBkTransaction.paymentApprove", Constants.DAYBOOK_TRANSACTION_APPROVED)))

		final GroupOperation group = Aggregation.group("$stockNo")
				.push(new BasicDBObject("daybookId", "$transaction.daybookId").append("amount", "$transaction.amount"))
				.as("items").first("stock.chassisNo").as("chassisNo").first("customerInfo.firstName").as("customer")
				.first("consignee.cFirstName").as("consignee").first("userDetails.code").as("staffId")
				.first("userDetails.fullname").as("staff").first("soldPrice.total").as("soldAmount").first("ship.name")
				.as("vessalName").first("schedule.voyageNo").as("voyageNo").first("destPort").as("destPort")
				.first("stock.purchaseInfo.auctionInfo.lotNo").as("lotNo").first("status").as("status")
				.first("etd.date").as("etd").first("eta.date").as("eta").first("blNo").as("blNo")
				.first("shippingInstruction.shippingInstructionId").as("shippingInstructionId")
				.first("shippingInstruction.customerId").as("customerId").first("shippingInstruction.consigneeId")
				.as("consigneeId").first("shipmentRequestId").as("shipmentRequestId").first("destCountry")
				.as("destinationCountry").first("code").as("blTransactionNo").first("recSurStatus").as("recSurStatus")
				.first("blDocumentStatus").as("blDocumentStatus");

		final ProjectionOperation project = Aggregation.project().and("chassisNo").as("chassisNo").and("consignee")
				.concat(" ( ", "$customer", " )").as("customer").and("staff").as("staff").and("soldAmount")
				.as("soldAmount").and("vessalName").as("vessalName").and("destinationCountry").as("destinationCountry")
				.and("voyageNo").as("vessalNo").and("destPort").as("destinationPortName").and("lotNo").as("lotNo")
				.and("blNo").as("blNo").and("status").as("status").and("etd").as("etd").and("eta").as("eta")
				.and(AccumulatorOperators.Sum.sumOf("items.amount")).as("amountReceived").and("shippingInstructionId")
				.as("shippingInstructionId").and("customerId").as("customerId").and("consigneeId").as("consigneeId")
				.and("shipmentRequestId").as("shipmentRequestId").and("staffId").as("staffId").and("blTransactionNo")
				.as("blTransactionNo").and("recSurStatus").as("recSurStatus").and("blDocumentStatus")
				.as("blDocumentStatus");

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupShipmentSchedule,
				Aggregation.unwind("$schedule", true), lookupShippingInstruction,
				Aggregation.unwind("$shippingInstruction", true), lookupStock, Aggregation.unwind("$stock", true),
				lookupMLocation, Aggregation.unwind("$location_dtls", true), addETDField,
				Aggregation.unwind("etd", true), matchEtd, addETAField, Aggregation.unwind("$eta", true),
				lookupUserDetails, Aggregation.unwind("$userDetails", true), lookupsalesInvoice,
				Aggregation.unwind("$soldPrice", true), lookupShip, Aggregation.unwind("ship", true), lookupCustomer,
				Aggregation.unwind("$customerInfo", true), addConsigneeField, Aggregation.unwind("$consignee", true),
				lookupDayBookTransaction, addTransField, Aggregation.unwind("transaction", true), group, project);// Aggregation.match(Criteria.where("status").is(Constants.STOCK_STATUS_SOLD)),
		final AggregationResults<BLDto> result = mongoTemplate.aggregate(aggregation, "t_shppng_rqust", BLDto.class);
		return result.getMappedResults();
	}

	// ------GL Reconciliation-----

	@Override
	public List<GLDto> getExecuteGLList() {

		final ProjectionOperation project = Aggregation.project().andInclude("stockNo", "items");

		final LookupOperation lookupsalesInvoice = LookupOperation.newLookup().from("t_acnt_trnsctn")
				.localField("stockNo").foreignField("stockNo").as("items");
		final MatchOperation match = Aggregation.match(Criteria.where("status").is(Constants.STOCK_STATUS_SOLD));
		final Aggregation aggregation = Aggregation.newAggregation(match, lookupsalesInvoice, project);// Aggregation.match(Criteria.where("status").is(Constants.STOCK_STATUS_SOLD)),
		final AggregationResults<GLDto> result = mongoTemplate.aggregate(aggregation, "t_stck", GLDto.class);
		return result.getMappedResults();
	}

	@Override
	public void updateInspectionStatusByStockNo(int status, List<String> stockNo) {
		final Update update = new Update().set("inspectionStatus", status);
		mongoTemplate.updateMulti(Query.query(Criteria.where("stockNo").in(stockNo)), update, TStock.class);
	}

	@Override
	public void stockUnreserve(List<String> stockNo) {
		final Update update = new Update().set("reserve", Constants.NOT_RESERVED).set("reservedInfo",
				new ReservedInfo());

		mongoTemplate.updateMulti(Query.query(Criteria.where("stockNo").in(stockNo)), update, TStock.class);

	}

	@Override
	public List<TLastLapVehiclesDto> getAllLastLapVehiclesList() {
		final MatchOperation initialMatch = Aggregation
				.match(Criteria.where("status").nin(Constants.STOCK_STATUS_SOLD, Constants.STOCK_STATUS_CANCEL)
						.and("destinationCountry").exists(true).not().size(0));
		final LookupOperation lookupLastLapVehicles = LookupOperation.newLookup().from("m_lt_lp_vhcls")
				.localField("destinationCountry").foreignField("destinationCountry").as("lastLapVehicles");

		final ProjectionOperation project = Aggregation.project().andExpression("month(firstRegDate)").as("month")
				.andExpression("year(firstRegDate)").as("year").and("firstRegDate")
				.plus("lastLapVehicles.expiryMilliSeconds").as("expiryDate").and("firstRegDate")
				.plus("lastLapVehicles.warningDaysMilliSeconds").as("warningDate")
				.andInclude("destinationCountry", "stockNo", "chassisNo", "firstRegDate", "model", "hsCode", "maker",
						"inspectionDetails")
				.and("lastLapVehicles.destinationCountry").as("lpDestinationCountry").and("lastLapVehicles.noOfDays")
				.as("noOfDays").and("shippingStatus").as("shippingStatus");

		final MatchOperation match = Aggregation.match(Criteria.where("warningDate").lte(new Date()));

		final Aggregation aggregation = Aggregation.newAggregation(initialMatch, lookupLastLapVehicles,
				Aggregation.unwind("$lastLapVehicles", true), project, match);

		final AggregationResults<TLastLapVehiclesDto> result = mongoTemplate.aggregate(aggregation, TStock.class,
				TLastLapVehiclesDto.class);

		return result.getMappedResults();
	}

	@Override
	public List<ShippingAvailableStockDto> findAllAvailableStocksForShipping() {
		final MatchOperation match = Aggregation
				.match(Criteria.where("shippingStatus").is(Constants.STOCK_SHIPPING_STATUS_IDLE).andOperator(
						Criteria.where("status").nin(Constants.STOCK_STATUS_CANCEL, Constants.STOCK_STATUS_NEW,
								Constants.STOCK_STATUS_RE_AUCTION),
						Criteria.where("shippingInstructionStatus")
								.ne(Constants.STOCK_SHIPPING_INSTRUCTION_STATUS_ARRANGED)));

		final LookupOperation lookupMaker = LookupOperation.newLookup().from("m_vchcl_mkr").localField("maker")
				.foreignField("name").as("makerDetails");

		final LookupOperation lookupLastLapVehicles = LookupOperation.newLookup().from("m_lt_lp_vhcls")
				.localField("destinationCountry").foreignField("destinationCountry").as("lastLapVehicles");

		final LookupOperation lookupMLocation = LookupOperation.newLookup().from("m_lctn")
				.localField("lastTransportLocation").foreignField("code").as("locationDetails");
		final LookupOperation lookupSupplier = LookupOperation.newLookup().from("m_spplr")
				.localField("purchaseInfo.supplier").foreignField("supplierCode").as("supplierDetails");

		final LookupOperation lookupMForwarder = LookupOperation.newLookup().from("m_frwrdr").localField("forwarder")
				.foreignField("code").as("forwarderDetails");
		LookupOperation lookupDocReceived = LookupOperation.newLookup().from("t_doc_recvd").localField("stockNo")
				.foreignField("stockNo").as("docRecDetails");
		LookupOperation lookupExportCertificate = LookupOperation.newLookup().from("t_exprt_crtfct")
				.localField("stockNo").foreignField("stockNo").as("exportCertificate");
		final LookupOperation lookupCustomer = LookupOperation.newLookup().from("t_cstmr")
				.localField("reservedInfo.customerId").foreignField("code").as("customer");
		final LookupOperation lookupLogin = LookupOperation.newLookup().from("m_lgn")
				.localField("reservedInfo.salesPersonId").foreignField("userId").as("userDetails");
		final AggregationOperation model = context -> new Document("$addFields",
				new Document("addmodel",
						new Document("$filter", new Document("input", "$makerDetails.models").append("as", "result")
								.append("cond", new Document("$eq", Arrays.asList("$$result.modelName", "$model"))))));

		final ProjectionOperation project = Aggregation.project().andExpression("month(firstRegDate)").as("month")
				.andExpression("year(firstRegDate)").as("year").and("firstRegDate")
				.plus("lastLapVehicles.expiryMilliSeconds").as("expiryDate").and("firstRegDate")
				.plus("lastLapVehicles.warningDaysMilliSeconds").as("warningDate")
				.andInclude("stockNo", "chassisNo", "firstRegDate", "model", "maker", "destinationCountry",
						"destinationPort", "category", "subcategory", "inspectionDetails", "inspectionStatus",
						"forwarder", "transportCategory", "inspectionFlag")
				.and("purchaseInfo.date").as("purchaseDate").and("$addmodel.length").as("length").and("$addmodel.width")
				.as("width").and("$addmodel.height").as("height").and("transportInfo.transporter").as("transporter")
				.and("transportInfo.pickupLocation").as("pickupLocation").and("transportInfo.pickupLocationCustom")
				.as("pickupLocationCustom").and("transportInfo.dropLocation").as("dropLocation")
				.and("transportInfo.charge").as("charge").and("transportInfo.dropLocationCustom")
				.as("dropLocationCustom").and("$transportationStatus").as("transportationStatus")
				.and("$purchaseInfo.auctionInfo.lotNo").as("lotNo").and("$purchaseInfo.auctionInfo.posNo")
				.as("auctionInfoPosNo").and("$lastTransportLocation").as("lastTransportLocation")
				.and("$lastTransportLocationCustom").as("lastTransportLocationCustom").and("$_id").as("id")
				.and("locationDetails.displayName").as("currentLocation").and("locationDetails.shipmentOriginCountry")
				.as("shipmentOriginCountry").and("locationDetails.shipmentOriginPort").as("shipmentOriginPort")
				.and("shipmentType").as("shipmentType").and("forwarderDetails.name").as("forwarder")
				.and("$auctionHouseDetails.auctionHouse").as("auctionHouse").and("$auctionHouseDetails._id")
				.as("auctionHouseId").and("customer.firstName").as("customerFN").and("userDetails.username")
				.as("instructedBy").and("bookingDetails").as("bookingDetails").and("reservedInfo.salesPersonId")
				.as("salesPersonId")
				.and(ConditionalOperators.ifNull("$auctionHouseDetails.posNos")
						.thenValueOf("purchaseInfo.auctionInfo.posNo"))
				.as("posNos").and(ConditionalOperators.ifNull("exportCertificate.convertedDate")
						.thenValueOf("docRecDetails.documentConvertedDate"))
				.as("documentConvertedDate");

		final SortOperation sort = Aggregation.sort(Direction.DESC, "purchaseDate");

		final AggregationOperation addAuctionHouse = context -> new Document("$addFields",
				new Document("auctionHouseDetails",
						new Document("$filter", new Document("input", "$supplierDetails.supplierLocations")
								.append("as", "result").append("cond", new Document("$eq",
										Arrays.asList("$$result._id", "$purchaseInfo.auctionInfo.auctionHouse"))))));

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupMaker,
				Aggregation.unwind("$makerDetails", true),
//				Aggregation.unwind("$destinationCountryInspection", true),
				model, Aggregation.unwind("$addmodel", true), lookupLastLapVehicles,
				Aggregation.unwind("$lastLapVehicles", true), lookupMLocation,
				Aggregation.unwind("$locationDetails", true), lookupMForwarder,
				Aggregation.unwind("$forwarderDetails", true), lookupSupplier,
				Aggregation.unwind("$supplierDetails", true), addAuctionHouse,
				Aggregation.unwind("$auctionHouseDetails", true), lookupDocReceived,
				Aggregation.unwind("$docRecDetails", true), lookupExportCertificate,
				Aggregation.unwind("$customer", true), lookupCustomer, Aggregation.unwind("$userDetails", true),
				lookupLogin, Aggregation.unwind("$exportCertificate", true), project, sort);
		final AggregationResults<ShippingAvailableStockDto> result = mongoTemplate.aggregate(aggregation, "t_stck",
				ShippingAvailableStockDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<TStock> findBySearch(String search) {
		final Criteria criteria = new Criteria();
		criteria.orOperator(Criteria.where("stockNo").regex(".*" + search + ".*", "i"),
				Criteria.where("chassisNo").regex(".*" + search + ".*", "i"));
		return mongoOperations.find(Query.query(criteria), TStock.class);
	}

	@Override
	public List<StockSearchDto> findBySearchDto(String search) {
		final Criteria criteria = new Criteria();
		criteria.orOperator(Criteria.where("stockNo").regex(".*" + search + ".*", "i"),
				Criteria.where("chassisNo").regex(".*" + search + ".*", "i"));
		final MatchOperation match = Aggregation.match(criteria);
		final ProjectionOperation project = Aggregation.project().andInclude("stockNo", "chassisNo");

		final Aggregation aggregation = Aggregation.newAggregation(match, project);
		final AggregationResults<StockSearchDto> result = mongoTemplate.aggregate(aggregation, TStock.class,
				StockSearchDto.class);

		return result.getMappedResults();
	}

	@Override
	public List<StockSearchDto> findCancelledStock(String search, Date purchaseDate) {
		final Criteria dateCriteria = Criteria.where("invoiceDate").gte(AppUtil.atStartOfDay(purchaseDate))
				.lte(AppUtil.atEndOfDay(purchaseDate));
		final Criteria statusCriteria = Criteria.where("status").is(Constants.INV_STATUS_CANCEL);
//		final Criteria autionCriteria = Criteria.where("supplierId").is(auctionCompany);
//		final Criteria autionHouse = Criteria.where("auctionHouseId").is(new ObjectId(auctionHouse));
		final Criteria searchCriteria = new Criteria().orOperator(
				Criteria.where("stockNo").regex(".*" + search + ".*", "i"),
				Criteria.where("chassisNo").regex(".*" + search + ".*", "i"));
		final Criteria criteria = new Criteria().andOperator(dateCriteria, statusCriteria, searchCriteria);
		final MatchOperation match = Aggregation.match(criteria);
		final ProjectionOperation project = Aggregation.project().andInclude("stockNo", "chassisNo");

		final Aggregation aggregation = Aggregation.newAggregation(match, project);
		final AggregationResults<StockSearchDto> result = mongoTemplate.aggregate(aggregation, TPurchaseInvoice.class,
				StockSearchDto.class);

		return result.getMappedResults();
	}

	@Override
	public void updateInspection(List<String> equipment, String stockNo, String engineNo, String chassisNo,
			String color) {
		final Update update = new Update().set("equipment", equipment).set("engineNo", engineNo)
				.set("chassisNo", chassisNo).set("color", color);

		mongoTemplate.updateMulti(Query.query(Criteria.where("stockNo").in(stockNo)), update, TStock.class);
	}

	@Override
	public List<InspectionDto> findAllByInspectionStatus() {
		final LookupOperation lookupCountry = LookupOperation.newLookup().from("m_cntry_prt")
				.localField("destinationCountry").foreignField("country").as("countryDetails");
		final LookupOperation locationDetails = LookupOperation.newLookup().from("m_lctn")
				.localField("lastTransportLocation").foreignField("code").as("locationDetails");
		final LookupOperation supplierDetails = LookupOperation.newLookup().from("m_spplr")
				.localField("purchaseInfo.supplier").foreignField("supplierCode").as("supplierDetails");
		final LookupOperation customerDetails = LookupOperation.newLookup().from("t_cstmr")
				.localField("reservedInfo.customerId").foreignField("code").as("customerDetails");
		final LookupOperation userDetails = LookupOperation.newLookup().from("m_lgn")
				.localField("reservedInfo.salesPersonId").foreignField("userId").as("userDetails");
		final LookupOperation transportDetails = LookupOperation.newLookup().from("m_trnsprtr")
				.localField("transportInfo.transporter").foreignField("code").as("transportDetails");
		final LookupOperation shippingInstruction = LookupOperation.newLookup().from("t_shppng_instructn")
				.localField("shippingInstructionInfo.shippingInstructionId").foreignField("shippingInstructionId")
				.as("shippingInstruction");
		final LookupOperation shippingRequest = LookupOperation.newLookup().from("t_shppng_rqust")
				.localField("shipmentRequestId").foreignField("shipmentRequestId").as("shippingRequest");
		final LookupOperation mLocation = LookupOperation.newLookup().from("m_lctn").localField("lastTransportLocation")
				.foreignField("code").as("mLocation");
		final LookupOperation shippingSchedule = LookupOperation.newLookup().from("t_shpmnt_schdl")
				.localField("shippingRequest.scheduleId").foreignField("scheduleId").as("shippingSchedule");
		final LookupOperation lookupLastLapVehicles = LookupOperation.newLookup().from("m_lt_lp_vhcls")
				.localField("destinationCountry").foreignField("destinationCountry").as("lastLapVehicles");
		final LookupOperation lookupShip = LookupOperation.newLookup().from("m_ship")
				.localField("shippingSchedule.shipId").foreignField("shipId").as("ship");
		final LookupOperation lookupShippingCompany = LookupOperation.newLookup().from("m_shppng_cmpny")
				.localField("shippingSchedule.shippingCompanyNo").foreignField("shippingCompanyNo")
				.as("shippingCompany");
		final LookupOperation lookupTransportOrderItems = LookupOperation.newLookup().from("trnsprt_ordr_items")
				.localField("stockNo").foreignField("stockNo").as("trnsprt_ordr_items");

		final AggregationOperation addTransportItem = (context) -> new Document("$addFields", new Document(
				"trnsprt_ordr_item",
				new Document("$filter", new Document("input", "$trnsprt_ordr_items").append("as", "result")
						.append("cond", new Document("$and", Arrays.asList(
								new Document("$ne",
										Arrays.asList("$$result.status", Constants.TRANSPORT_ITEM_CANCELED)),
								new Document("$ne",
										Arrays.asList("$$result.status", Constants.TRANSPORT_ITEM_REARRANGE))))))));

		/*
		 * final AggregationOperation addTransportItem = ( context) -> new
		 * Document("$addFields", new Document("trnsprt_ordr_item", new
		 * Document("$filter", new Document("input", "$trnsprt_ordr_items").append("as",
		 * "result") .append("cond", new Document("$result.statuss", new
		 * Document("$nin", Arrays.asList(Constants.TRANSPORT_ITEM_CANCELED,Constants.
		 * TRANSPORT_ITEM_REARRANGE)))))));
		 */
		final AggregationOperation addRecentTransportItem = (context) -> new Document("$addFields",
				new Document("trnsprt_ordr_item",
						new Document("$arrayElemAt",
								Arrays.asList("$trnsprt_ordr_item",
										new Document("$indexOfArray", Arrays.asList("$trnsprt_ordr_item.createdDate",
												new Document("$max", "trnsprt_ordr_item.createdDate")))))));
		final MatchOperation match1 = Aggregation
				.match(Criteria.where("inspectionStatus").is(Constants.STOCK_AVAILABLE_FOR_INSPECTION)
						.andOperator(Criteria.where("status").ne(Constants.STOCK_STATUS_CANCEL)));
		final MatchOperation match2 = Aggregation
				.match(Criteria.where("countryDetails.inspectionFlag").is(Constants.COUNTRY_INSPECTION_NEED));

		final AggregationOperation addETDField = (context) -> new Document("$addFields",
				new Document("etdValue",
						new Document("$filter",
								new Document("input", "$shippingSchedule.schedule").append("as", "result")
										.append("cond", new Document("$and", Arrays.asList(
												// new Document("$ifNull", Arrays.asList("$$result", null)),
												new Document("$eq",
														Arrays.asList("$$result.portName",
																"$mLocation.shipmentOriginPort")),
												new Document("$eq",
														Arrays.asList("$$result.portFlag", "loading"))))))));

		final ProjectionOperation project = Aggregation.project()
				.andInclude("stockNo", "chassisNo", "model", "firstRegDate", "color", "destinationCountry",
						"destinationPort", "lastTransportLocationCustom", "inspectionDetails", "lastTransportLocation",
						"isPhotoUploaded")
				.and("firstRegDate").plus("lastLapVehicles.expiryMilliSeconds").as("expiryDate").and("firstRegDate")
				.plus("lastLapVehicles.warningDaysMilliSeconds").as("warningDate").and("locationDetails.displayName")
				.as("sLastTransportLocation").and("purchaseInfo.date").as("purchaseDate").and("purchaseInfo.supplier")
				.as("supplier").and("supplierDetails.company").as("supplierName").and("purchaseInfo.auctionInfo.lotNo")
				.as("lotNo").and("customerDetails.firstName").as("customerName").and("userDetails.username")
				.as("salesPerson").and("etdValue.date").as("shippingDate").and("mashoCopyReceivedDate")
				.as("documentReceivedDate").and("transportDetails.name").as("transporterName")
				.and("shippingSchedule.voyageNo").as("voyageNo").and("ship.shipId").as("shipId").and("ship.name")
				.as("shipName").and("shippingCompany.shippingCompanyNo").as("shippingCompanyNo")
				.and("shippingCompany.name").as("shippingCompanyName").and("shippingRequest.status")
				.as("shippingStatus").and("shippingInstruction.scheduleType").as("shippingInstructionStatus")
				.and("shippingInstruction.estimatedDeparture").as("estimatedDeparture")
				.and("shippingInstruction.estimatedArrival").as("estimatedArrival").and("trnsprt_ordr_item.status")
				.as("transportStatus").and("trnsprt_ordr_item.etd").as("transportDeliveryDate");

		final SortOperation sort = Aggregation.sort(Direction.DESC, "purchaseDate");
		final Aggregation aggregation = Aggregation.newAggregation(match1, lookupCountry,
				Aggregation.unwind("$countryDetails", true), match2, locationDetails,
				Aggregation.unwind("$locationDetails", true), supplierDetails,
				Aggregation.unwind("$supplierDetails", true), customerDetails,
				Aggregation.unwind("$customerDetails", true), userDetails, Aggregation.unwind("$userDetails", true),
				lookupTransportOrderItems, addTransportItem, addRecentTransportItem,

				transportDetails, Aggregation.unwind("$transportDetails", true), shippingInstruction,
				Aggregation.unwind("$shippingInstruction", true), shippingRequest,
				Aggregation.unwind("$shippingRequest", true), mLocation, Aggregation.unwind("$mLocation", true),
				shippingSchedule, Aggregation.unwind("$shippingSchedule", true), addETDField,
				Aggregation.unwind("$etdValue", true), lookupLastLapVehicles,
				Aggregation.unwind("$lastLapVehicles", true), lookupShip, Aggregation.unwind("ship", true),
				lookupShippingCompany, Aggregation.unwind("shippingCompany", true), project, sort);
		final AggregationResults<InspectionDto> result = mongoTemplate.aggregate(aggregation, "t_stck",
				InspectionDto.class);
		return result.getMappedResults();
	}

	@Override
	public UpdateResult updateByStockNos(List<String> stockNos, Update update) {

		return mongoTemplate.updateMulti(Query.query(Criteria.where("stockNo").in(stockNos)), update, TStock.class);

	}

	@Override
	public UpdateResult updateByStockNo(String stockNo, Update update) {
		return mongoTemplate.updateMulti(Query.query(Criteria.where("stockNo").is(stockNo)), update, TStock.class);

	}

	@Override
	public boolean isExistsByChassisNo(String chassisNo) {
		return mongoOperations.exists(Query.query(Criteria.where("chassisNo").regex("(?sim)^" + chassisNo + "$")),
				TStock.class);
	}

	@Override
	public boolean isExistsByStockNoAndChassisNo(String stockNo, String chassisNo) {
		return mongoOperations.exists(Query.query(Criteria.where("stockNo").is(stockNo)
				.andOperator(Criteria.where("chassisNo").regex("(?sim)^" + chassisNo + "$"))), TStock.class);
	}

	@Override
	public StockDetailsDto findOneStockDetailsByStockNo(String stockNo) {
		final MatchOperation match = Aggregation.match(Criteria.where("stockNo").is(stockNo));
		final LookupOperation lookupsupplier = LookupOperation.newLookup().from("m_spplr")
				.localField("purchaseInfo.supplier").foreignField("supplierCode").as("supplier");
		final LookupOperation lookupSalesPerson = LookupOperation.newLookup().from("m_usr")
				.localField("reservedInfo.salesPersonId").foreignField("code").as("salesPerson");
		final LookupOperation lookupCustomer = LookupOperation.newLookup().from("t_cstmr")
				.localField("reservedInfo.customerId").foreignField("code").as("customer");
		final LookupOperation lookuppurchaseinvoice = LookupOperation.newLookup().from("t_prchs_invc")
				.localField("stockNo").foreignField("stockNo").as("purchase_invoice_arr");
		final LookupOperation lookupExportCertificate = LookupOperation.newLookup().from("t_exprt_crtfct")
				.localField("stockNo").foreignField("stockNo").as("export_certificate");
		final AggregationOperation addPurchaseInvoice = (context) -> new Document("$addFields", new Document(
				"purchase_invoice", new Document("$arrayElemAt", Arrays.asList("$purchase_invoice_arr", 0))));

		final LookupOperation lookupTransportInfo = LookupOperation.newLookup().from("trnsprt_ordr_items")
				.localField("stockNo").foreignField("stockNo").as("transport_info_all");
		final AggregationOperation addTransportInfo = context -> new Document("$addFields", new Document(
				"transport_info",
				new Document("$filter", new Document("input", "$transport_info_all").append("as", "result")
						.append("cond", new Document("$and", Arrays.asList(
								new Document("$ne",
										Arrays.asList("$$result.status", Constants.TRANSPORT_ITEM_CANCELED)),
								new Document("$ne",
										Arrays.asList("$$result.status", Constants.TRANSPORT_ITEM_REARRANGE))))))));
		final AggregationOperation addAuctionHouse = context -> new Document("$addFields",
				new Document("auctionHouseDetails",
						new Document("$filter", new Document("input", "$supplier.supplierLocations")
								.append("as", "result").append("cond", new Document("$eq",
										Arrays.asList("$$result._id", "$purchaseInfo.auctionInfo.auctionHouse"))))));

		final LookupOperation lookupPickupLocation = LookupOperation.newLookup().from("m_lctn")
				.localField("transport_info.pickupLocation").foreignField("code").as("pickupLocation");
		final LookupOperation lookupDropLocation = LookupOperation.newLookup().from("m_lctn")
				.localField("transport_info.dropLocation").foreignField("code").as("dropLocation");

		final LookupOperation lookupTransporter = LookupOperation.newLookup().from("m_trnsprtr")
				.localField("transport_info.transporter").foreignField("code").as("transporter");

		final GroupOperation groupOperation1 = Aggregation.group("_id").first("stockNo").as("stockNo")
				.first("shipmentType").as("shipmentType").first("chassisNo").as("chassisNo").first("model").as("model")
				.first("auctionRemarks").as("auctionRemarks").first("remarks").as("remarks").first("maker").as("maker")
				.first("destinationCountry").as("destinationCountry").first("destinationPort").as("destinationPort")
				.first("firstRegDate").as("firstRegDate").first("sFirstRegDate").as("sFirstRegDate")
				.first("transmission").as("transmission").first("manualTypes").as("manualTypes").first("noOfDoors")
				.as("noOfDoors").first("color").as("color").first("noOfSeat").as("noOfSeat").first("grade").as("grade")
				.first("auctionGrade").as("auctionGrade").first("auctionGradeExt").as("auctionGradeExt").first("fuel")
				.as("fuel").first("driven").as("driven").first("mileage").as("mileage").first("cc").as("cc")
				.first("recycle").as("recycle").first("numberPlate").as("numberPlate").first("oldNumberPlate")
				.as("oldNumberPlate").first("equipment").as("equipment").first("extraAccessories")
				.as("extraAccessories").first("purchaseInfo.date").as("purchasedDate")
				.first("purchaseInfo.auctionInfo.lotNo").as("shuppinNo").first("supplier.supplierCode")
				.as("supplierCode").first("supplier.company").as("supplierName").first("reservedInfo.date")
				.as("reservedDate").first("salesPerson.fullname").as("reservedByName")
				.first("reservedInfo.salesPersonId").as("reservedById").first("reservedInfo.customerId")
				.as("reservedCustomerID").first("customer.firstName").as("reservedCustomerName")
				.first("reservedInfo.price").as("reservedPrice").first("fob").as("minSellPrice")
				.first("purchase_invoice.recycle").as("recycleAmount").first("purchase_invoice.purchaseCost")
				.as("purchaseCost").first("purchase_invoice.purchaseCostTax").as("purchaseCostTax")
				.first("purchase_invoice.commision").as("commision").first("purchase_invoice.commisionTax")
				.as("commisionTax").first("purchase_invoice.roadTax").as("roadTax")
				.first("purchase_invoice.otherCharges").as("otherCharges").first("attachments").as("attachments")
				.first("isBidding").as("isBidding").first("export_certificate.serialNo").as("exportSerial")
				.first("export_certificate.referenceNo").as("exportReference").first("auctionHouseDetails.auctionHouse")
				.as("auctionHouse").first("auctionHouseDetails._id").as("auctionHouseId").first("manufactureYear")
				.as("manufactureYear").last("$dropLocation.displayName").as("currentLocation")
				.push(new BasicDBObject("pickupLocation", "$transport_info.pickupLocation")
						.append("pickupLocationCustom", "$transport_info.pickupLocationCustom")
						.append("sPickupLocation", "$pickupLocation.displayName")
						.append("dropLocation", "$transport_info.dropLocation")
						.append("sDropLocation", "$dropLocation.displayName")
						.append("dropLocationCustom", "$transport_info.dropLocationCustom")
						.append("transporter", "$transporter.name").append("charge", "$transport_info.charge")
						.append("etd", "$transport_info.etd"))
				.as("transportInfos");
		final LookupOperation lookupInspectionDetails = LookupOperation.newLookup().from("t_inspctn_odr_rqst")
				.localField("stockNo").foreignField("stockNo").as("inspection_info");

		final LookupOperation lookupInspectionForworder = LookupOperation.newLookup().from("m_frwrdr")
				.localField("inspection_info.forwarder").foreignField("code").as("inspection_forwarder");
		final LookupOperation lookupInspectionCompany = LookupOperation.newLookup().from("m_inspctn_cmpny")
				.localField("inspection_info.inspectionCompany").foreignField("code").as("inspection_company");

		final ProjectionOperation project = Aggregation.project("_id", "stockNo", "chassisNo", "maker", "model",
				"supplierCode", "supplierName", "shipmentType", "firstRegDate", "sFirstRegDate", "transmission",
				"manualTypes", "noOfDoors", "noOfSeat", "grade", "auctionGrade", "auctionGradeExt", "fuel", "driven",
				"mileage", "color", "cc", "recycle", "numberPlate", "oldNumberPlate", "equipment", "extraAccessories",
				"attachments", "purchasedDate", "shuppinNo", "destinationCountry", "destinationPort", "reservedById",
				"reservedByName", "reservedDate", "reservedCustomerID", "reservedCustomerName", "reservedPrice",
				"minSellPrice", "purchaseCost", "purchaseCostTax", "commision", "commisionTax", "roadTax",
				"recycleAmount", "otherCharges", "isBidding", "exportSerial", "exportReference", "transportInfos",
				"currentLocation", "manufactureYear", "auctionHouse", "auctionHouseId", "auctionRemarks", "remarks")
				.and(Filter.filter("$inspection_info").as("result")
						.by(Eq.valueOf("$result.status").equalToValue(Constants.INSPECTION_ORDER_REQUEST_COMPLETE)))
				.as("inspection_info");
		final GroupOperation groupOperation2 = Aggregation.group("_id").first("stockNo").as("stockNo")
				.first("shipmentType").as("shipmentType").first("chassisNo").as("chassisNo").first("model").as("model")
				.first("maker").as("maker").first("destinationCountry").as("destinationCountry").first("auctionRemarks")
				.as("auctionRemarks").first("remarks").as("remarks").first("destinationPort").as("destinationPort")
				.first("firstRegDate").as("firstRegDate").first("sFirstRegDate").as("sFirstRegDate")
				.first("transmission").as("transmission").first("manualTypes").as("manualTypes").first("noOfDoors")
				.as("noOfDoors").first("noOfSeat").as("noOfSeat").first("grade").as("grade").first("auctionGrade")
				.as("auctionGrade").first("color").as("color").first("auctionGradeExt").as("auctionGradeExt")
				.first("fuel").as("fuel").first("driven").as("driven").first("mileage").as("mileage").first("cc")
				.as("cc").first("recycle").as("recycle").first("numberPlate").as("numberPlate").first("oldNumberPlate")
				.as("oldNumberPlate").first("equipment").as("equipment").first("extraAccessories")
				.as("extraAccessories").first("purchasedDate").as("purchasedDate").first("shuppinNo").as("shuppinNo")
				.first("supplierCode").as("supplierCode").first("supplierName").as("supplierName").first("reservedDate")
				.as("reservedDate").first("reservedByName").as("reservedByName").first("reservedById")
				.as("reservedById").first("reservedCustomerID").as("reservedCustomerID").first("reservedCustomerName")
				.as("reservedCustomerName").first("reservedPrice").as("reservedPrice").first("minSellPrice")
				.as("minSellPrice").first("recycleAmount").as("recycleAmount").first("purchaseCost").as("purchaseCost")
				.first("purchaseCostTax").as("purchaseCostTax").first("commision").as("commision").first("commisionTax")
				.as("commisionTax").first("roadTax").as("roadTax").first("otherCharges").as("otherCharges")
				.first("isBidding").as("isBidding").first("attachments").as("attachments").first("transportInfos")
				.as("transportInfos").first("currentLocation").as("currentLocation").first("exportSerial")
				.as("exportSerial").first("exportReference").as("exportReference").first("auctionHouse")
				.as("auctionHouse").first("auctionHouseId").as("auctionHouseId").first("manufactureYear")
				.as("manufactureYear")
				.push(new BasicDBObject("country", "$inspection_info.country")
						.append("dateOfIssue", "$inspection_info.dateOfIssue")
						.append("forwarder", "$inspection_forwarder.name")
						.append("inspectionCompany", "$inspection_company.name")
						.append("inspectionCompanyFlag", "$inspection_info.inspectionCompanyFlag")
						.append("inspectionSentDate", "$inspection_info.createdDate"))
				.as("inspectionInfos");

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupsupplier,
				Aggregation.unwind("$supplier", true), lookupSalesPerson, Aggregation.unwind("$salesPerson", true),
				lookupExportCertificate, Aggregation.unwind("$export_certificate", true), lookupCustomer,
				Aggregation.unwind("$customer", true), lookuppurchaseinvoice, addPurchaseInvoice, lookupTransportInfo,
				addTransportInfo, Aggregation.unwind("$transport_info", true), addAuctionHouse,
				Aggregation.unwind("$auctionHouseDetails", true), lookupPickupLocation,
				Aggregation.unwind("$pickupLocation", true), lookupDropLocation,
				Aggregation.unwind("$dropLocation", true), lookupTransporter, Aggregation.unwind("$transporter", true),
				groupOperation1, lookupInspectionDetails, project, Aggregation.unwind("$inspection_info", true),
				lookupInspectionForworder, Aggregation.unwind("$inspection_forwarder", true), lookupInspectionCompany,
				Aggregation.unwind("$inspection_company", true), groupOperation2);
		final AggregationResults<StockDetailsDto> result = mongoTemplate.aggregate(aggregation, "t_stck",
				StockDetailsDto.class);

		return result.getUniqueMappedResult();
	}

	@Override
	public Long findAllByDocumentStatusCount() {
		final MatchOperation match = Aggregation.match(Criteria.where("documentStatus")
				.in(Constants.STOCK_DOCUMENT_NOT_RECEIVED, Constants.STOCK_DOCUMENT_RECEIVED_AND_CANCELLED)
				.and("status").nin(Constants.STOCK_STATUS_CANCEL));
		final CountOperation count = Aggregation.count().as("count");
		final Aggregation aggregation = Aggregation.newAggregation(match, count);
		final AggregationResults<Map> result = mongoTemplate.aggregate(aggregation, "t_stck", Map.class);
		return Long.valueOf(!AppUtil.isObjectEmpty(result.getUniqueMappedResult())
				? result.getUniqueMappedResult().get("count").toString()
				: "0");
	}

	@Override
	public Long findAllByDocumentCertificateStatusCount() {
		final MatchOperation match = Aggregation.match(
				Criteria.where("documentConvertTo").is(Constants.STOCK_DOCUMENT_TYPE_CONVERT_TO_EXPORT_CERTIFICATE));
		final CountOperation count = Aggregation.count().as("count");
		final Aggregation aggregation = Aggregation.newAggregation(match, count);
		final AggregationResults<Map> result = mongoTemplate.aggregate(aggregation, "t_stck", Map.class);
		return Long.valueOf(!AppUtil.isObjectEmpty(result.getUniqueMappedResult())
				? result.getUniqueMappedResult().get("count").toString()
				: "0");
	}

	@Override
	public Long findAllByDocumentNameTransferStatusCount() {
		final MatchOperation match = Aggregation
				.match(Criteria.where("documentConvertTo").is(Constants.STOCK_DOCUMENT_TYPE_CONVERT_TO_NAME_TRANSFER));
		final CountOperation count = Aggregation.count().as("count");
		final Aggregation aggregation = Aggregation.newAggregation(match, count);
		final AggregationResults<Map> result = mongoTemplate.aggregate(aggregation, "t_stck", Map.class);
		return Long.valueOf(!AppUtil.isObjectEmpty(result.getUniqueMappedResult())
				? result.getUniqueMappedResult().get("count").toString()
				: "0");
	}

	@Override
	public Long findAllByDocumentDomesticStatusCount() {
		final MatchOperation match = Aggregation
				.match(Criteria.where("documentConvertTo").is(Constants.STOCK_DOCUMENT_TYPE_CONVERT_TO_DOMESTIC));
		final CountOperation count = Aggregation.count().as("count");
		final Aggregation aggregation = Aggregation.newAggregation(match, count);
		final AggregationResults<Map> result = mongoTemplate.aggregate(aggregation, "t_stck", Map.class);
		return Long.valueOf(!AppUtil.isObjectEmpty(result.getUniqueMappedResult())
				? result.getUniqueMappedResult().get("count").toString()
				: "0");
	}

	@Override
	public List<StockDocumentsDto> findAllDocumentNotReceivedList() {

		final MatchOperation match = Aggregation.match(Criteria.where("documentStatus")
				.in(Constants.STOCK_DOCUMENT_NOT_RECEIVED, Constants.STOCK_DOCUMENT_RECEIVED_AND_CANCELLED)
				.and("status").nin(Constants.STOCK_STATUS_CANCEL));

		final SortOperation sort = Aggregation.sort(Direction.DESC, "documentStatus");
		final ProjectionOperation project = Aggregation.project()
				.andInclude("stockNo", "chassisNo", "firstRegDate", "sFirstRegDate", "oldNumberPlate", "documentStatus",
						"documentRemarks", "numberPlate", "plateNoReceivedDate", "mileage", "destinationCountry",
						"destinationPort", "documentReceivedDate")
				.and("purchaseInfo.date").as("purchaseInfoDate").and("purchaseInfo.type").as("purchaseType")
				.and("purchaseInfo.auctionInfo.lotNo").as("shuppinNo").and("supplier.supplierCode").as("supplierCode")
				.and("supplier.company").as("supplierName").and("auctionHouse._id").as("auctionHouseId")
				.and("auctionHouse.auctionHouse").as("auctionHouse");

		final LookupOperation lookupSupplier = LookupOperation.newLookup().from("m_spplr")
				.localField("purchaseInfo.supplier").foreignField("supplierCode").as("supplier");
		final AggregationOperation addAuctionHouse = context -> new Document("$addFields",
				new Document("auctionHouse",
						new Document("$filter", new Document("input", "$supplier.supplierLocations")
								.append("as", "result").append("cond", new Document("$eq",
										Arrays.asList("$$result._id", "$purchaseInfo.auctionInfo.auctionHouse"))))));
		final Aggregation aggregation = Aggregation.newAggregation(match, lookupSupplier,
				Aggregation.unwind("$supplier", true), addAuctionHouse, Aggregation.unwind("$auctionHouse", true),
				project, sort);
		final AggregationResults<StockDocumentsDto> result = mongoTemplate.aggregate(aggregation, "t_stck",
				StockDocumentsDto.class);
		return result.getMappedResults();
	}

	@Override
	public StockDocumentsDto findOneDocumentNotReceivedStockDetails(String stockNo) {

		final MatchOperation match = Aggregation
				.match(new Criteria().andOperator(
						Criteria.where("documentStatus").in(Constants.STOCK_DOCUMENT_NOT_RECEIVED,
								Constants.STOCK_DOCUMENT_RECEIVED_AND_CANCELLED),
						Criteria.where("stockNo").is(stockNo)));

		final SortOperation sort = Aggregation.sort(Direction.DESC, "documentStatus");
		final ProjectionOperation project = Aggregation.project()
				.andInclude("stockNo", "chassisNo", "firstRegDate", "oldNumberPlate", "documentStatus",
						"documentRemarks", "numberPlate", "plateNoReceivedDate")
				.and("purchaseInfo.date").as("purchaseInfoDate").and("purchaseInfo.type").as("purchaseType")
				.and("purchaseInfo.auctionInfo.lotNo").as("shuppinNo").and("supplier.supplierCode").as("supplierCode")
				.and("supplier.company").as("supplierName").and("auctionHouse._id").as("auctionHouseId")
				.and("auctionHouse.auctionHouse").as("auctionHouse");

		final LookupOperation lookupSupplier = LookupOperation.newLookup().from("m_spplr")
				.localField("purchaseInfo.supplier").foreignField("supplierCode").as("supplier");
		final AggregationOperation addAuctionHouse = context -> new Document("$addFields",
				new Document("auctionHouse",
						new Document("$filter", new Document("input", "$supplier.supplierLocations")
								.append("as", "result").append("cond", new Document("$eq",
										Arrays.asList("$$result._id", "$purchaseInfo.auctionInfo.auctionHouse"))))));
		final Aggregation aggregation = Aggregation.newAggregation(match, lookupSupplier,
				Aggregation.unwind("$supplier", true), addAuctionHouse, Aggregation.unwind("$auctionHouse", true),
				project, sort);
		final AggregationResults<StockDocumentsDto> result = mongoTemplate.aggregate(aggregation, "t_stck",
				StockDocumentsDto.class);
		return result.getUniqueMappedResult();
	}

	@Override
	public List<StockDocumentsDto> findAllDocumentNameTransferList() {
		final ProjectionOperation project = Aggregation.project()
				.andInclude("stockNo", "chassisNo", "firstRegDate", "documentType", "documentReceivedDate",
						"oldNumberPlate", "documentConvertedDate", "documentConvertTo")
				.and("purchaseInfo.date").as("purchaseInfoDate").and("purchaseInfo.type").as("purchaseType")
				.and("purchaseInfo.supplier").as("supplier").and("purchaseInfo.auctionInfo.lotNo").as("shuppinNo")
				.and("supplier.supplierCode").as("supplierCode").and("supplier.company").as("supplierName")
				.and("auctionHouse._id").as("auctionHouseId").and("auctionHouse.auctionHouse").as("auctionHouse");

		final LookupOperation lookupSupplier = LookupOperation.newLookup().from("m_spplr")
				.localField("purchaseInfo.supplier").foreignField("supplierCode").as("supplier");
		final AggregationOperation addAuctionHouse = context -> new Document("$addFields",
				new Document("auctionHouse",
						new Document("$filter", new Document("input", "$supplier.supplierLocations")
								.append("as", "result").append("cond", new Document("$eq",
										Arrays.asList("$$result._id", "$purchaseInfo.auctionInfo.auctionHouse"))))));
		final Aggregation aggregation = Aggregation.newAggregation(
				Aggregation.match(
						Criteria.where("documentConvertTo").is(Constants.STOCK_DOCUMENT_TYPE_CONVERT_TO_NAME_TRANSFER)),
				lookupSupplier, Aggregation.unwind("$supplier", true), addAuctionHouse,
				Aggregation.unwind("$auctionHouse", true), project);
		final AggregationResults<StockDocumentsDto> result = mongoTemplate.aggregate(aggregation, "t_stck",
				StockDocumentsDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<StockDocumentsDto> findAllDocumentDomesticList() {
		final ProjectionOperation project = Aggregation.project()
				.andInclude("stockNo", "chassisNo", "firstRegDate", "documentType", "documentReceivedDate",
						"oldNumberPlate", "documentConvertedDate", "documentConvertTo")
				.and("purchaseInfo.date").as("purchaseInfoDate").and("purchaseInfo.type").as("purchaseType")
				.and("purchaseInfo.supplier").as("supplier").and("purchaseInfo.auctionInfo.lotNo").as("shuppinNo")
				.and("supplier.supplierCode").as("supplierCode").and("supplier.company").as("supplierName")
				.and("auctionHouse._id").as("auctionHouseId").and("auctionHouse.auctionHouse").as("auctionHouse");

		final LookupOperation lookupSupplier = LookupOperation.newLookup().from("m_spplr")
				.localField("purchaseInfo.supplier").foreignField("supplierCode").as("supplier");
		final AggregationOperation addAuctionHouse = context -> new Document("$addFields",
				new Document("auctionHouse",
						new Document("$filter", new Document("input", "$supplier.supplierLocations")
								.append("as", "result").append("cond", new Document("$eq",
										Arrays.asList("$$result._id", "$purchaseInfo.auctionInfo.auctionHouse"))))));
		final Aggregation aggregation = Aggregation.newAggregation(
				Aggregation.match(
						Criteria.where("documentConvertTo").is(Constants.STOCK_DOCUMENT_TYPE_CONVERT_TO_DOMESTIC)),
				lookupSupplier, Aggregation.unwind("$supplier", true), addAuctionHouse,
				Aggregation.unwind("$auctionHouse", true), project);
		final AggregationResults<StockDocumentsDto> result = mongoTemplate.aggregate(aggregation, "t_stck",
				StockDocumentsDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<StockDocumentsDto> findAllExportTrackingList() {

		final MatchOperation match = Aggregation.match(Criteria.where("documentStatus")
				.in(Constants.STOCK_DOCUMENT_RECEIVED, Constants.STOCK_DOCUMENT_CONVERT));

		final SortOperation sort = Aggregation.sort(Direction.DESC, "documentStatus");
		final ProjectionOperation project = Aggregation.project()
				.andInclude("stockNo", "chassisNo", "firstRegDate", "oldNumberPlate", "documentStatus",
						"documentRemarks", "numberPlate", "plateNoReceivedDate", "mileage", "destinationCountry",
						"handoverTo")
				.and("purchaseInfo.date").as("purchaseInfoDate").and("purchaseInfo.type").as("purchaseType")
				.and("purchaseInfo.auctionInfo.lotNo").as("shuppinNo").and("supplier.supplierCode").as("supplierCode")
				.and("supplier.company").as("supplierName").and("auctionHouse._id").as("auctionHouseId")
				.and("auctionHouse.auctionHouse").as("auctionHouse").and("$user.fullname").as("handOverPerson");

		final LookupOperation lookupSupplier = LookupOperation.newLookup().from("m_spplr")
				.localField("purchaseInfo.supplier").foreignField("supplierCode").as("supplier");
		final LookupOperation lookupUser = LookupOperation.newLookup().from("m_usr").localField("handoverToUserId")
				.foreignField("code").as("user");
		final AggregationOperation addAuctionHouse = context -> new Document("$addFields",
				new Document("auctionHouse",
						new Document("$filter", new Document("input", "$supplier.supplierLocations")
								.append("as", "result").append("cond", new Document("$eq",
										Arrays.asList("$$result._id", "$purchaseInfo.auctionInfo.auctionHouse"))))));
		final Aggregation aggregation = Aggregation.newAggregation(match, lookupSupplier,
				Aggregation.unwind("$supplier", true), lookupUser, Aggregation.unwind("$user", true), addAuctionHouse,
				addAuctionHouse, Aggregation.unwind("$auctionHouse", true), project, sort);
		final AggregationResults<StockDocumentsDto> result = mongoTemplate.aggregate(aggregation, "t_stck",
				StockDocumentsDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<InventoryValueDto> getInventoryValueReport() {
		final MatchOperation match = Aggregation.match(Criteria.where("status")
				.in(Constants.STOCK_STATUS_PURCHASED_CONFIRMED, Constants.STOCK_STATUS_RE_AUCTION));
		final LookupOperation lookupPurchase = LookupOperation.newLookup().from("t_prchs_invc").localField("stockNo")
				.foreignField("stockNo").as("purchaseInvoice");
		final LookupOperation lookupSalesPersonId = LookupOperation.newLookup().from("m_usr")
				.localField("reservedInfo.salesPersonId").foreignField("code").as("salesPerson");
//		final LookupOperation lookupInventoryCost = LookupOperation.newLookup().from("t_invtry_cst")
//				.localField("stockNo").foreignField("stockNo").as("inventoryCost");
		final LookupOperation lookupLastLocation = LookupOperation.newLookup().from("m_lctn")
				.localField("lastTransportLocation").foreignField("code").as("lastLocation");
		final ProjectionOperation project = Aggregation.project()
				.andInclude("stockNo", "chassisNo", "maker", "model", "reserve").and("$purchaseInfo.date")
				.as("purchaseDate").and(AccumulatorOperators.Sum.sumOf("$purchaseInvoice.purchaseCost"))
				.as("purchaseCost").and(AccumulatorOperators.Sum.sumOf("$purchaseInvoice.commision"))
				.as("commisionCost").and(AccumulatorOperators.Sum.sumOf("$purchaseInvoice.otherCharges"))
				.as("otherCharges").and("$lastLocation.displayName").as("lastLocation").and("$salesPerson.code")
				.as("reservedPersonId").and("$salesPerson.fullname").as("reservedPersonName");
		final Aggregation aggregation = Aggregation.newAggregation(match, lookupPurchase, lookupSalesPersonId,
				Aggregation.unwind("$salesPerson", true), lookupLastLocation, Aggregation.unwind("$lastLocation", true),
				project);
		final AggregationResults<InventoryValueDto> result = mongoTemplate.aggregate(aggregation, "t_stck",
				InventoryValueDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<BranchSalesOrderDto> getShippedStockWithoutSalesOrder() {
		final Criteria criteria = new Criteria();
		final ArrayList<Criteria> andOperators = new ArrayList<>();
		andOperators.add(Criteria.where("shippingStatus").is(Constants.STOCK_SHIPPING_STATUS_SHIPPINGARRANGED));
		andOperators.add(Criteria.where("status").ne(Constants.STOCK_STATUS_SOLD));

		final MatchOperation matchOperation = Aggregation.match(criteria);
		criteria.andOperator(andOperators.toArray(new Criteria[0]));

		final LookupOperation lookupShipping = LookupOperation.newLookup().from("t_shppng_rqust")
				.localField("shipmentRequestId").foreignField("shipmentRequestId").as("shippingRequest");
		final LookupOperation lookupShippingInstruction = LookupOperation.newLookup().from("t_shppng_instructn")
				.localField("shippingRequest.shippingInstructionId").foreignField("shippingInstructionId")
				.as("instruction");
		final LookupOperation lookupCustomer = LookupOperation.newLookup().from("t_cstmr")
				.localField("instruction.customerId").foreignField("code").as("customer");

		final MatchOperation matchCustomer = Aggregation
				.match(Criteria.where("customer.flag").is(Constants.CUSTOMER_FLAG_BRANCH));
		final LookupOperation lookupCurrency = LookupOperation.newLookup().from("m_currency")
				.localField("customer.currencyType").foreignField("currencySeq").as("currency_details");

		final ProjectionOperation project = Aggregation.project()
				.andInclude("stockNo", "chassisNo", "model", "category", "maker", "firstRegDate",
						"shippingInstructionStatus")
				.and("shippingRequest.destCountry").as("destCountry").and("shippingRequest.destPort").as("destPort")
				.and("customer.code").as("customerId").and("customer.firstName").as("firstName")
				.and("customer.lastName").as("lastName").and("customer.nickName").as("nickName")
				.and("$currency_details.symbol").as("currencySymbol").and("$customer.currencyType").as("currency");

		final Aggregation aggregation = Aggregation.newAggregation(matchOperation, lookupShipping,
				Aggregation.unwind("$shippingRequest", true), lookupShippingInstruction,
				Aggregation.unwind("$instruction", true), lookupCustomer, Aggregation.unwind("$customer", true),
				matchCustomer, lookupCurrency, Aggregation.unwind("$currency_details", true), project);
		final AggregationResults<BranchSalesOrderDto> result = mongoTemplate.aggregate(aggregation, TStock.class,
				BranchSalesOrderDto.class);
		return result.getMappedResults();
	}

	@Override
	public Long countByStatusPurchaseConfirmedAndSold() {
		final MatchOperation match = Aggregation.match(
				Criteria.where("status").in(Constants.STOCK_STATUS_PURCHASED_CONFIRMED, Constants.STOCK_STATUS_SOLD));
		final CountOperation count = Aggregation.count().as("count");
		final Aggregation aggregation = Aggregation.newAggregation(match, count);
		final AggregationResults<Map> result = mongoTemplate.aggregate(aggregation, TStock.class, Map.class);
		return Long.valueOf(!AppUtil.isObjectEmpty(result.getUniqueMappedResult())
				? result.getUniqueMappedResult().get("count").toString()
				: "0");
	}

	@Override
	public List<FBFeedDto> findStockForFbFeed() {
		final Criteria criteria = new Criteria();

		final ArrayList<Criteria> andOperators = new ArrayList<>();
		andOperators.add(Criteria.where("status").nin(Constants.STOCK_STATUS_SOLD));
		andOperators.add(Criteria.where("showForSales").is(Constants.SHOW_FOR_SALES));

		final MatchOperation match = Aggregation.match(criteria);
		criteria.andOperator(andOperators.toArray(new Criteria[0]));

		final ProjectionOperation project = Aggregation.project().andInclude("modelType", "grade").and("equipment")
				.as("extraAccessories").and("auctionRemarks").as("remarks").and("stockNo").as("vehicle_id")
				.and("attachments").size().as("attachCount").and("maker").as("url").and("maker").as("make").and("model")
				.as("model").and("firstRegDate").as("year").and("mileage").as("mileage_value").and("maker")
				.as("mileage_unit").and("transmission").as("transmission").and("fuel").as("fuel_type")
				.and("subcategory").as("body_style").and("offerPrice").as("price").and("color").as("exterior_color")
				.and("fob").as("sale_price").and("chassisNo").as("vin").and("recycle").as("state_vehicle");

		final Aggregation aggregation = Aggregation.newAggregation(match, project);
		final AggregationResults<FBFeedDto> result = mongoTemplate.aggregate(aggregation, TStock.class,
				FBFeedDto.class);
		return result.getMappedResults();

	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Integer> getMonthWiseSalesData() {
		final AggregationResults<TStockDto> result = getMonthWiseData();
		final Map<String, Integer> monthlySales = new HashMap<>();
		final ArrayList<Document> docs = (ArrayList<Document>) result.getRawResults().get("results");
		for (final Document monthDocument : docs) {
			monthlySales.put(Month.of((int) monthDocument.get("month")).toString(),
					(Integer) monthDocument.get("totalRecords"));
		}
		return monthlySales;

	}

	private AggregationResults<TStockDto> getMonthWiseData() {

		final List<String> salesPersonIds = getSalesPersonIds();
		final ArrayList<Criteria> andOperators = new ArrayList<>();
		andOperators.add(Criteria.where("reserve").is(Constants.RESERVED));
		andOperators.add(Criteria.where("shippingInstructionInfo.salesPersonId").in(salesPersonIds));

		final MatchOperation matchOperation = Aggregation
				.match(new Criteria().andOperator(andOperators.toArray(new Criteria[0])));

		final ProjectionOperation project = Aggregation.project().andExpression("month(createdDate)").as("month")
				.and("reservedInfo.price").as("price");
		final GroupOperation groupOperation = Aggregation.group("month").count().as("totalRecords").first("month")
				.as("month");
		final ProjectionOperation project2 = Aggregation.project().and("month").as("month").and("totalRecords")
				.as("totalRecords");

		final Aggregation aggregation = Aggregation.newAggregation(matchOperation, project, groupOperation, project2);
		final AggregationResults<TStockDto> result = mongoTemplate.aggregate(aggregation, TStock.class,
				TStockDto.class);
		return result;
	}

	/**
	 * @return list of the sales person Ids
	 */
	private List<String> getSalesPersonIds() {
		final List<String> salesPersonList = new ArrayList<>();
		final MLoginDto loggedInUser = securityService.findLoggedInUser();
		if (securityService.findLoggedInUser().getRole().equalsIgnoreCase(Constants.ROLE_SALES_ADMIN)
				|| securityService.findLoggedInUser().getRole().equalsIgnoreCase(Constants.ROLE_SALES_MANAGER)) {

			final ArrayList<Criteria> andOperators = new ArrayList<>();
			andOperators.add(Criteria.where("reportTo").is(loggedInUser.getId()));

			final MatchOperation matchOperation = Aggregation
					.match(new Criteria().andOperator(andOperators.toArray(new Criteria[0])));

			final ProjectionOperation project2 = Aggregation.project().andInclude("userId");

			final Aggregation aggregation = Aggregation.newAggregation(matchOperation, project2);

			final AggregationResults<MLoginDto> result = mongoTemplate.aggregate(aggregation, MLogin.class,
					MLoginDto.class);

			result.forEach((loginDto) -> salesPersonList.add(loginDto.getId()));

		} else {
			// salesPersonList.add(loggedInUser.getId());

			final ArrayList<Criteria> andOperators = new ArrayList<>();
			andOperators.add(Criteria.where("reportTo").is(loggedInUser.getId()));

			final MatchOperation matchOperation = Aggregation
					.match(new Criteria().andOperator(andOperators.toArray(new Criteria[0])));

			final ProjectionOperation project2 = Aggregation.project().andInclude("userId");

			final Aggregation aggregation = Aggregation.newAggregation(matchOperation, project2);

			final AggregationResults<MLoginDto> result = mongoTemplate.aggregate(aggregation, MLogin.class,
					MLoginDto.class);

			result.forEach((loginDto) -> salesPersonList.add(loginDto.getUserId()));

		}
		return salesPersonList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Integer> getTopCustomerData() {
		final ArrayList<Criteria> andOperators = new ArrayList<>();
		andOperators.add(Criteria.where("reserve").is(Constants.RESERVED));
		andOperators.add(Criteria.where("status").is(Constants.STOCK_STATUS_PURCHASED_CONFIRMED));

		final LookupOperation lookupCustomer = LookupOperation.newLookup().from("t_cstmr")
				.localField("reservedInfo.customerId").foreignField("code").as("customer");

		final MatchOperation matchOperation = Aggregation
				.match(new Criteria().andOperator(andOperators.toArray(new Criteria[0])));

		final GroupOperation groupOperation = Aggregation.group("$reservedInfo.customerId").count().as("customerCount")
				.first("$customer.firstName").as("customerName").first("$reservedInfo.customerId").as("customerId")
				.push(new BasicDBObject("price", "$reservedInfo.price")).as("price");

		final ProjectionOperation project = Aggregation.project()
				.andInclude("customerId", "customerCount", "customerName")
				.and(AccumulatorOperators.Sum.sumOf("$price.price")).as("totalPrice");

		final SortOperation sort = Aggregation.sort(Direction.DESC, "customerCount");

		final Aggregation aggregation = Aggregation.newAggregation(matchOperation, lookupCustomer,
				Aggregation.unwind("$customer", true), groupOperation, project, sort);

		final AggregationResults<Document> result = mongoTemplate.aggregate(aggregation, "t_stck", Document.class);

		final Map<String, Integer> topCustomers = new LinkedHashMap<>();
		final ArrayList<Document> customerDocs = (ArrayList<Document>) result.getRawResults().get("results");
		for (final Document monthDocument : customerDocs) {
			final String customerName = (String) monthDocument.get("customerName");
			if (null != customerName) {
				topCustomers.put(customerName, (Integer) monthDocument.get("customerCount"));
			} else
				continue;
		}

		return topCustomers.entrySet().stream().sorted(Map.Entry.comparingByValue())
				.collect(toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2, LinkedHashMap::new));

	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Integer> getTopModelData() {
		final ArrayList<Criteria> andOperators = new ArrayList<>();
		andOperators.add(Criteria.where("reserve").is(Constants.RESERVED));
		andOperators.add(Criteria.where("status").is(Constants.STOCK_STATUS_PURCHASED_CONFIRMED));

		final MatchOperation matchOperation = Aggregation
				.match(new Criteria().andOperator(andOperators.toArray(new Criteria[0])));

		final GroupOperation groupOperation = Aggregation.group("$maker").count().as("makerCount").first("$maker")
				.as("maker");

		final SortOperation sort = Aggregation.sort(Direction.DESC, "makerCount");

		final Aggregation aggregation = Aggregation.newAggregation(matchOperation, groupOperation, sort);

		final AggregationResults<Document> result = mongoTemplate.aggregate(aggregation, "t_stck", Document.class);

		final Map<String, Integer> topModel = new LinkedHashMap<>();

		final ArrayList<Document> customerDocs = (ArrayList<Document>) result.getRawResults().get("results");

		for (final Document monthDocument : customerDocs) {
			final String customerId = monthDocument.get("maker").toString();
			// for incorrect data customerId was returned as null so a check is essential.
			// We need the top makers hence the restriction for the Map size.
			if (customerId.length() > 1 && topModel.size() < 4) {
				topModel.put(monthDocument.get("maker").toString(), (Integer) monthDocument.get("makerCount"));
			} else
				continue;
		}

		return topModel;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Integer getYearlySalesTotal() {
		final AggregationResults<TStockDto> monthWise = getMonthWiseData();
		Integer total = 0;
		final ArrayList<Document> resultDocs = (ArrayList<Document>) monthWise.getRawResults().get("results");
		for (final Document monthDocument : resultDocs) {
			total += (Integer) monthDocument.get("totalRecords");
		}
		return total;
	}

	@Override
	public List<StockSearchDto> findReauctionStock(String search, Date purchaseDate) {
		// TODO Auto-generated method stub

		final Criteria statusCriteria = Criteria.where("status").is(Constants.INV_STATUS_REAUCTION);
//		final Criteria autionCriteria = Criteria.where("auctionCompany").is(auctionCompany);
//		final Criteria autionHouse = Criteria.where("auctionHouse").is(new ObjectId(auctionHouse));
		final Criteria searchCriteria = new Criteria().orOperator(
				Criteria.where("stockNo").regex(".*" + search + ".*", "i"),
				Criteria.where("chassisNo").regex(".*" + search + ".*", "i"));
		final Criteria criteria = new Criteria().andOperator(searchCriteria);
		final MatchOperation match = Aggregation.match(criteria);
		final ProjectionOperation project = Aggregation.project().andInclude("stockNo", "chassisNo");

		final Aggregation aggregation = Aggregation.newAggregation(match, project);
		final AggregationResults<StockSearchDto> result = mongoTemplate.aggregate(aggregation, TReAuction.class,
				StockSearchDto.class);

		return result.getMappedResults();
	}

	@Override
	public List<StockSearchDto> searchRikusoStock(String search) {

		final Criteria dateCriteria = Criteria.where("invoiceDate").gte(AppUtil.subtractDays(-180))
				.lte(AppUtil.atEndOfDay(new Date()));

		// TODO Auto-generated method stub
//		final Criteria autionCriteria = Criteria.where("supplierId").is(auctionCompany);
//		final Criteria autionHouse = Criteria.where("auctionHouseId").is(new ObjectId(auctionHouse));
		final Criteria searchCriteria = new Criteria().orOperator(
				Criteria.where("stockNo").regex(".*" + search + ".*", "i"),
				Criteria.where("chassisNo").regex(".*" + search + ".*", "i"));
		final Criteria criteria = new Criteria().andOperator(dateCriteria, searchCriteria);
		final MatchOperation match = Aggregation.match(criteria);
		final ProjectionOperation project = Aggregation.project().andInclude("stockNo", "chassisNo");

		GroupOperation group = Aggregation.group("$stockNo").first("stockNo").as("stockNo").first("chassisNo")
				.as("chassisNo");

		final Aggregation aggregation = Aggregation.newAggregation(match, project, group);
		final AggregationResults<StockSearchDto> result = mongoTemplate.aggregate(aggregation, TPurchaseInvoice.class,
				StockSearchDto.class);

		return result.getMappedResults();
	}

	@Override
	public List<StockSearchDto> searchTakeOutStock(String search, String auctionCompany, String auctionHouse) {

		final Criteria dateCriteria = Criteria.where("invoiceDate").gte(AppUtil.subtractDays(-30))
				.lte(AppUtil.atEndOfDay(new Date()));

		// TODO Auto-generated method stub
		final Criteria autionCriteria = Criteria.where("supplierId").is(auctionCompany);
		final Criteria autionHouse = Criteria.where("auctionHouseId").is(new ObjectId(auctionHouse));
		final Criteria searchCriteria = new Criteria().orOperator(
				Criteria.where("stockNo").regex(".*" + search + ".*", "i"),
				Criteria.where("chassisNo").regex(".*" + search + ".*", "i"));
		final Criteria criteria = new Criteria().andOperator(dateCriteria, searchCriteria, autionCriteria, autionHouse);
		final MatchOperation match = Aggregation.match(criteria);
		final ProjectionOperation project = Aggregation.project().andInclude("stockNo", "chassisNo");

		final Aggregation aggregation = Aggregation.newAggregation(match, project);
		final AggregationResults<StockSearchDto> result = mongoTemplate.aggregate(aggregation, TPurchaseInvoice.class,
				StockSearchDto.class);

		return result.getMappedResults();
	}

	@Override
	public void updateSalesPersonByStockNos(String salesPersonId, List<String> stockNos) {
		final Update update = new Update().set("reservedInfo.salesPersonId", salesPersonId);
		mongoTemplate.updateMulti(Query.query(Criteria.where("stockNo").in(stockNos)), update, TStock.class);
	}

	@Override
	public List<InspectionDto> findAllByInspectionStatusAndStockNos(List<String> stockNos) {
		MatchOperation matchOperation = Aggregation.match(Criteria.where("stockNo").in(stockNos));
		final LookupOperation lookupCountry = LookupOperation.newLookup().from("m_cntry_prt")
				.localField("destinationCountry").foreignField("country").as("countryDetails");
		final LookupOperation locationDetails = LookupOperation.newLookup().from("m_lctn")
				.localField("lastTransportLocation").foreignField("code").as("locationDetails");
		final LookupOperation supplierDetails = LookupOperation.newLookup().from("m_spplr")
				.localField("purchaseInfo.supplier").foreignField("supplierCode").as("supplierDetails");
		final LookupOperation customerDetails = LookupOperation.newLookup().from("t_cstmr")
				.localField("reservedInfo.customerId").foreignField("code").as("customerDetails");
		final LookupOperation userDetails = LookupOperation.newLookup().from("m_lgn")
				.localField("reservedInfo.salesPersonId").foreignField("userId").as("userDetails");
		final LookupOperation transportDetails = LookupOperation.newLookup().from("m_trnsprtr")
				.localField("transportInfo.transporter").foreignField("code").as("transportDetails");
		final LookupOperation shippingInstruction = LookupOperation.newLookup().from("t_shppng_instructn")
				.localField("shippingInstructionInfo.shippingInstructionId").foreignField("shippingInstructionId")
				.as("shippingInstruction");
		final LookupOperation shippingRequest = LookupOperation.newLookup().from("t_shppng_rqust")
				.localField("shipmentRequestId").foreignField("shipmentRequestId").as("shippingRequest");
		final LookupOperation mLocation = LookupOperation.newLookup().from("m_lctn").localField("lastTransportLocation")
				.foreignField("code").as("mLocation");
		final LookupOperation shippingSchedule = LookupOperation.newLookup().from("t_shpmnt_schdl")
				.localField("shippingRequest.scheduleId").foreignField("scheduleId").as("shippingSchedule");
		final LookupOperation lookupLastLapVehicles = LookupOperation.newLookup().from("m_lt_lp_vhcls")
				.localField("destinationCountry").foreignField("destinationCountry").as("lastLapVehicles");
		final LookupOperation lookupShip = LookupOperation.newLookup().from("m_ship")
				.localField("shippingSchedule.shipId").foreignField("shipId").as("ship");
		final LookupOperation lookupShippingCompany = LookupOperation.newLookup().from("m_shppng_cmpny")
				.localField("shippingSchedule.shippingCompanyNo").foreignField("shippingCompanyNo")
				.as("shippingCompany");
		final LookupOperation lookupTransportOrderItems = LookupOperation.newLookup().from("trnsprt_ordr_items")
				.localField("stockNo").foreignField("stockNo").as("trnsprt_ordr_items");
		final AggregationOperation addTransportItem = (
				context) -> new Document("$addFields",
						new Document("trnsprt_ordr_item",
								new Document("$filter",
										new Document("input", "$trnsprt_ordr_items").append("as", "result")
												.append("cond", new Document("$or", Arrays.asList(
														new Document("$eq",
																Arrays.asList("$$result.status",
																		Constants.TRANSPORT_ITEM_CONFIRMED)),
														new Document("$eq", Arrays.asList("$$result.status",
																Constants.TRANSPORT_ITEM_DELIVERY_CONFIRMED))))))));
		final AggregationOperation addRecentTransportItem = (context) -> new Document("$addFields",
				new Document("trnsprt_ordr_item",
						new Document("$arrayElemAt",
								Arrays.asList("$trnsprt_ordr_item",
										new Document("$indexOfArray", Arrays.asList("$trnsprt_ordr_item.createdDate",
												new Document("$max", "trnsprt_ordr_item.createdDate")))))));

		final MatchOperation match = Aggregation
				.match(Criteria.where("inspectionStatus").is(Constants.STOCK_AVAILABLE_FOR_INSPECTION).andOperator(
						Criteria.where("countryDetails.inspectionFlag").is(Constants.COUNTRY_INSPECTION_NEED),
						Criteria.where("status").ne(Constants.STOCK_STATUS_CANCEL)));

		final AggregationOperation addETDField = (context) -> new Document("$addFields",
				new Document("etdValue",
						new Document("$filter",
								new Document("input", "$shippingSchedule.schedule").append("as", "result")
										.append("cond", new Document("$and", Arrays.asList(
												// new Document("$ifNull", Arrays.asList("$$result", null)),
												new Document("$eq",
														Arrays.asList("$$result.portName",
																"$mLocation.shipmentOriginPort")),
												new Document("$eq",
														Arrays.asList("$$result.portFlag", "loading"))))))));

		final ProjectionOperation project = Aggregation.project()
				.andInclude("stockNo", "chassisNo", "model", "firstRegDate", "color", "destinationCountry",
						"destinationPort", "lastTransportLocationCustom", "inspectionDetails", "lastTransportLocation",
						"isPhotoUploaded")
				.and("firstRegDate").plus("lastLapVehicles.expiryMilliSeconds").as("expiryDate").and("firstRegDate")
				.plus("lastLapVehicles.warningDaysMilliSeconds").as("warningDate").and("locationDetails.displayName")
				.as("sLastTransportLocation").and("purchaseInfo.date").as("purchaseDate").and("purchaseInfo.supplier")
				.as("supplier").and("supplierDetails.company").as("supplierName").and("purchaseInfo.auctionInfo.lotNo")
				.as("lotNo").and("customerDetails.firstName").as("customerName").and("userDetails.username")
				.as("salesPerson").and("etdValue.date").as("shippingDate").and("mashoCopyReceivedDate")
				.as("documentReceivedDate").and("transportDetails.name").as("transporterName")
				.and("shippingSchedule.voyageNo").as("voyageNo").and("ship.shipId").as("shipId").and("ship.name")
				.as("shipName").and("shippingCompany.shippingCompanyNo").as("shippingCompanyNo")
				.and("shippingCompany.name").as("shippingCompanyName").and("shippingRequest.status")
				.as("shippingStatus").and("shippingInstruction.status").as("shippingInstructionStatus")
				.and("shippingInstruction.estimatedDeparture").as("estimatedDeparture")
				.and("shippingInstruction.estimatedArrival").as("estimatedArrival").and("trnsprt_ordr_item.etd")
				.as("transportDeliveryDate");

		final SortOperation sort = Aggregation.sort(Direction.DESC, "purchaseDate");
		final Aggregation aggregation = Aggregation.newAggregation(matchOperation, lookupCountry,
				Aggregation.unwind("$countryDetails", true), locationDetails,
				Aggregation.unwind("$locationDetails", true), supplierDetails,
				Aggregation.unwind("$supplierDetails", true), customerDetails,
				Aggregation.unwind("$customerDetails", true), userDetails, Aggregation.unwind("$userDetails", true),
				lookupTransportOrderItems, addTransportItem, addRecentTransportItem,

				transportDetails, Aggregation.unwind("$transportDetails", true), shippingInstruction,
				Aggregation.unwind("$shippingInstruction", true), shippingRequest,
				Aggregation.unwind("$shippingRequest", true), mLocation, Aggregation.unwind("$mLocation", true),
				shippingSchedule, Aggregation.unwind("$shippingSchedule", true), match, addETDField,
				Aggregation.unwind("$etdValue", true), lookupLastLapVehicles,
				Aggregation.unwind("$lastLapVehicles", true), lookupShip, Aggregation.unwind("ship", true),
				lookupShippingCompany, Aggregation.unwind("shippingCompany", true), project, sort);
		final AggregationResults<InspectionDto> result = mongoTemplate.aggregate(aggregation, "t_stck",
				InspectionDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<StockSearchDto> findShippingAvailableStock(String search) {

		final Criteria orCriteria = new Criteria();
		orCriteria.orOperator(Criteria.where("stockNo").regex(".*" + search + ".*", "i"),
				Criteria.where("chassisNo").regex(".*" + search + ".*", "i"));
		final Criteria criteria = new Criteria().andOperator(
				Criteria.where("shippingStatus").is(Constants.STOCK_SHIPPING_STATUS_IDLE),
				Criteria.where("status").nin(Constants.STOCK_STATUS_CANCEL, Constants.STOCK_STATUS_NEW,
						Constants.STOCK_STATUS_RE_AUCTION),
				Criteria.where("shippingInstructionStatus").ne(Constants.STOCK_SHIPPING_INSTRUCTION_STATUS_ARRANGED),
				orCriteria);

		final MatchOperation match = Aggregation.match(criteria);
		final ProjectionOperation project = Aggregation.project().andInclude("stockNo", "chassisNo");

		final Aggregation aggregation = Aggregation.newAggregation(match, project);
		final AggregationResults<StockSearchDto> result = mongoTemplate.aggregate(aggregation, TStock.class,
				StockSearchDto.class);

		return result.getMappedResults();
	}

	@Override
	public void updateStock(Update update, String id) {
		mongoTemplate.updateFirst(Query.query(Criteria.where("id").is(id)), update, TStock.class);

	}

}
