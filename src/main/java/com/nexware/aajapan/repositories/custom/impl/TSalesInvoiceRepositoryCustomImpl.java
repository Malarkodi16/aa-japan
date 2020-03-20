package com.nexware.aajapan.repositories.custom.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AccumulatorOperators;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationExpression;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;
import org.springframework.data.mongodb.core.aggregation.CountOperation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import com.mongodb.BasicDBObject;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.ARAgingSummaryDto;
import com.nexware.aajapan.dto.BranchSalesInvoiceDto;
import com.nexware.aajapan.dto.BranchSalesOrderListDto;
import com.nexware.aajapan.dto.CostofSalesDto;
import com.nexware.aajapan.dto.CustomerTransactionDto;
import com.nexware.aajapan.dto.CustomerTransactionItemsDto;
import com.nexware.aajapan.dto.CustomerTransactionReportDto;
import com.nexware.aajapan.dto.IncomeByCustomerDto;
import com.nexware.aajapan.dto.MHSCodeDto;
import com.nexware.aajapan.dto.ReceivableAmountDto;
import com.nexware.aajapan.dto.RecentSalesDto;
import com.nexware.aajapan.dto.SalesSummaryDto;
import com.nexware.aajapan.dto.StockSalesReportDto;
import com.nexware.aajapan.dto.TCustomerAccountsTransactionDto;
import com.nexware.aajapan.dto.TSalesInvoiceDto;
import com.nexware.aajapan.dto.TSalesInvoiceItemDto;
import com.nexware.aajapan.dto.TTUnitAllocationDto;
import com.nexware.aajapan.models.TSalesInvoice;
import com.nexware.aajapan.repositories.custom.TSalesInvoiceRepositoryCustom;
import com.nexware.aajapan.utils.AppUtil;

public class TSalesInvoiceRepositoryCustomImpl implements TSalesInvoiceRepositoryCustom {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<TSalesInvoiceDto> getListWithCustomerDetails(List<String> salesPersonIds) {
		final List<Criteria> criterias = new ArrayList<>();
		if (!AppUtil.isObjectEmpty(salesPersonIds)) {
			criterias.add(Criteria.where("salesPerson").in(salesPersonIds));
		}
		criterias.add(Criteria.where("status").ne(Constants.SALES_INV_CANCEL));
		final MatchOperation matchOperation = Aggregation
				.match(new Criteria().andOperator(criterias.toArray(new Criteria[0])));
		final LookupOperation lookupCustomer = LookupOperation.newLookup().from("t_cstmr").localField("customerId")
				.foreignField("code").as("customer");

		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stockDtls");
		final LookupOperation lookupCurrency = LookupOperation.newLookup().from("m_currency")
				.localField("customer.currencyType").foreignField("currencySeq").as("currencyDtls");
		final LookupOperation lookupLogin = LookupOperation.newLookup().from("m_lgn").localField("salesPerson")
				.foreignField("userId").as("userDetails");

		final AggregationOperation addConsignee = (context) -> new Document("$addFields", new Document("consignee",
				new Document("$filter", new Document("input", "$customer.consigneeNotifyparties").append("as", "result")
						.append("cond", new Document("$eq", Arrays.asList("$$result._id", "$consigneeId"))))));

		final AggregationOperation addNotifyParty = (context) -> new Document("$addFields",
				new Document("notifyparty",
						new Document("$filter",
								new Document("input", "$customer.consigneeNotifyparties").append("as", "result").append(
										"cond",
										new Document("$eq", Arrays.asList("$$result._id", "$notifypartyId"))))));

		final GroupOperation group = Aggregation.group("$invoiceNo")
				.push(new BasicDBObject("stockNo", "$stockDtls.stockNo").append("chassisNo", "$stockDtls.chassisNo")
						.append("shippingInstructionStatus", "$stockDtls.shippingInstructionStatus")
						.append("firstRegDate", "$stockDtls.firstRegDate").append("maker", "$stockDtls.maker")
						.append("model", "$stockDtls.model").append("fob", "$fob").append("insurance", "$insurance")
						.append("shipping", "$shipping").append("freight", "$freight").append("total", "$total")
						.append("status", "$status").append("currencySymbol", "$currencyDtls.symbol"))
				.as("salesInvoiceDetails").first("customer.firstName").as("customerFN").first("customer.lastName")
				.as("customerLN").first("customer.companyName").as("companyName").first("customer.nickName")
				.as("nickName").first("consignee.cFirstName").as("cFirstName").first("consignee.cLastName")
				.as("cLastName").first("notifyparty.npFirstName").as("npFirstName").first("notifyparty.npLastName")
				.as("npLastName").first("invoiceNo").as("invoiceNo").first("customer.paymentType").as("paymentType")
				.first("customerId").as("customerId").first("consignee._id").as("consigneeId").first("notifyparty._id")
				.as("notifypartyId").first("createdDate").as("createdDate").first("userDetails.username")
				.as("orderedBy").first("$currencyDtls.symbol").as("currencySymbol").first("currencyType")
				.as("currencyType").first("salesPerson").as("salesPerson");

		SortOperation sort = Aggregation.sort(Direction.DESC, "createdDate");

		final ProjectionOperation project = Aggregation.project()
				.andInclude("salesInvoiceDetails", "companyName", "customerFN", "customerLN", "nickName", "cFirstName",
						"npFirstName", "invoiceNo", "paymentType", "customerId", "createdDate", "orderedBy",
						"currencySymbol", "consigneeId", "notifypartyId", "currencyType", "salesPerson")
				.and(AccumulatorOperators.Sum.sumOf("salesInvoiceDetails.total")).as("allTtotal");

		final Aggregation aggregation = Aggregation.newAggregation(matchOperation, lookupCustomer, lookupLogin,
				Aggregation.unwind("$customer", true), lookupStock, Aggregation.unwind("$stockDtls", true),
				lookupCurrency, Aggregation.unwind("$currencyDtls", true), addConsignee,
				Aggregation.unwind("consignee", true), addNotifyParty, Aggregation.unwind("$notifyparty", true), group,
				project, sort);

		final AggregationResults<TSalesInvoiceDto> result = mongoTemplate.aggregate(aggregation, "t_sls_inv",
				TSalesInvoiceDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<TSalesInvoiceItemDto> getListWithStockDetails(List<String> salesPersonIds) {
		final List<Criteria> criterias = new ArrayList<>();
		if (!AppUtil.isObjectEmpty(salesPersonIds)) {
			criterias.add(Criteria.where("salesPerson").in(salesPersonIds));
		}
		criterias.add(Criteria.where("status").ne(Constants.SALES_INV_CANCEL));
		final MatchOperation matchOperation = Aggregation
				.match(new Criteria().andOperator(criterias.toArray(new Criteria[0])));

		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stockDtls");
		final LookupOperation lookupCustomer = LookupOperation.newLookup().from("t_cstmr").localField("customerId")
				.foreignField("code").as("customer");
		final LookupOperation lookupCurrency = LookupOperation.newLookup().from("m_currency")
				.localField("customer.currencyType").foreignField("currencySeq").as("currencyDtls");
		final LookupOperation lookupLogin = LookupOperation.newLookup().from("m_lgn").localField("salesPerson")
				.foreignField("userId").as("userDetails");
		LookupOperation lookupShippingRequest = LookupOperation.newLookup().from("t_shppng_rqust").localField("stockNo")
				.foreignField("stockNo").as("shippingRequest");

		final LookupOperation lookupShipmentSchedule = LookupOperation.newLookup().from("t_shpmnt_schdl")
				.localField("shippingRequest.scheduleId").foreignField("scheduleId").as("schedule");
		final LookupOperation lookupShip = LookupOperation.newLookup().from("m_ship").localField("schedule.shipId")
				.foreignField("shipId").as("ship");
		final LookupOperation lookupShippingCompany = LookupOperation.newLookup().from("m_shppng_cmpny")
				.localField("schedule.shippingCompanyNo").foreignField("shippingCompanyNo").as("shippingCompany");

		final AggregationOperation addConsignee = (context) -> new Document("$addFields", new Document("consignee",
				new Document("$filter", new Document("input", "$customer.consigneeNotifyparties").append("as", "result")
						.append("cond", new Document("$eq", Arrays.asList("$$result._id", "$consigneeId"))))));

		final AggregationOperation addNotifyParty = (context) -> new Document("$addFields",
				new Document("notifyparty",
						new Document("$filter",
								new Document("input", "$customer.consigneeNotifyparties").append("as", "result").append(
										"cond",
										new Document("$eq", Arrays.asList("$$result._id", "$notifypartyId"))))));

		final AggregationOperation addETDField = (
				context) -> new Document("$addFields",
						new Document("etd",
								new Document("$filter",
										new Document("input", "$schedule.schedule").append("as", "result")
												.append("cond", new Document("$and", Arrays.asList(
														new Document("$eq",
																Arrays.asList("$$result.portName",
																		"$shippingRequest.orginPort")),
														new Document("$eq",
																Arrays.asList("$$result.portFlag", "loading"))))))));
		final AggregationOperation addETAField = context -> new Document("$addFields", new Document("eta", new Document(
				"$filter",
				new Document("input", "$schedule.schedule").append("as", "result").append("cond", new Document("$and",
						Arrays.asList(
								new Document("$eq", Arrays.asList("$$result.portName", "$shippingRequest.destPort")),
								new Document("$eq", Arrays.asList("$$result.portFlag", "destination"))))))));

		final ProjectionOperation project = Aggregation.project()
				.andInclude("invoiceNo", "stockNo", "total", "status", "createdDate", "customerId")
				.and("$stockDtls.maker").as("maker").and("$stockDtls.model").as("model").and("$stockDtls.chassisNo")
				.as("chassisNo").and("$stockDtls.shippingInstructionStatus").as("shippingInstructionStatus")
				.and("$stockDtls.firstRegDate").as("firstRegDate").and("$currencyDtls.symbol").as("currencySymbol")
				.and("$currencyDtls.currencySeq").as("currencyType").and("$customer.firstName").as("customerFN")
				.and("$customer.lastName").as("customerLN").and("$customer.companyName").as("companyName")
				.and("$consignee.cFirstName").as("cFirstName").and("$consignee.cLastName").as("cLastName")
				.and("$notifyparty.npFirstName").as("npFirstName").and("$notifyparty.npLastName").as("npLastName")
				.and("$userDetails.username").as("orderedBy").and("$stockDtls.shippingStatus").as("shippingStatus")
				.and("$etd.date").as("etd").and("$eta.date").as("eta").and("$ship.name").as("shipName")
				.and("$shippingCompany.name").as("shippingCompanyName");

		Aggregation aggregation = Aggregation.newAggregation(matchOperation, lookupStock,
				Aggregation.unwind("$stockDtls", true), lookupCustomer, Aggregation.unwind("$customer", true),
				lookupCurrency, Aggregation.unwind("$currencyDtls", true), lookupLogin, lookupShippingRequest,
				Aggregation.unwind("$shippingRequest", true), lookupShipmentSchedule,
				Aggregation.unwind("$schedule", true), addConsignee, addNotifyParty,
				Aggregation.unwind("$consignee", true), Aggregation.unwind("$notifyparty", true), addETDField,
				Aggregation.unwind("$etd", true), addETAField, Aggregation.unwind("$eta", true), lookupShip,
				Aggregation.unwind("$ship", true), lookupShippingCompany, Aggregation.unwind("$shippingCompany", true),
				project);
		AggregationResults<TSalesInvoiceItemDto> result = this.mongoTemplate.aggregate(aggregation, "t_sls_inv",
				TSalesInvoiceItemDto.class);
		return result.getMappedResults();
	}

	@Override
	public Long getListWithCustomerDetailsCount(List<String> salesPersonIds) {
		final List<Criteria> criterias = new ArrayList<>();
		if (!AppUtil.isObjectEmpty(salesPersonIds)) {
			criterias.add(Criteria.where("salesPerson").in(salesPersonIds));
		}
		criterias.add(Criteria.where("status").ne(Constants.SALES_INV_CANCEL));
		final MatchOperation matchOperation = Aggregation
				.match(new Criteria().andOperator(criterias.toArray(new Criteria[0])));
		final GroupOperation group = Aggregation.group("$invoiceNo");
		final CountOperation count = Aggregation.count().as("count");
		final Aggregation aggregation = Aggregation.newAggregation(matchOperation, group, count);
		final AggregationResults<Document> result = mongoTemplate.aggregate(aggregation, "t_sls_inv", Document.class);
		return Long.valueOf(
				result.getUniqueMappedResult() != null ? result.getUniqueMappedResult().getInteger("count", 0) : 0);

	}

	@Override
	public List<CustomerTransactionDto> findAllCustomerTransactions(String customerId, Date minDate, Date maxDate) {
		final List<Criteria> criterias = new ArrayList<>();
		if (!AppUtil.isObjectEmpty(customerId)) {
			criterias.add(Criteria.where("reservedInfo.customerId").is(customerId));
		}
		if (!AppUtil.isObjectEmpty(minDate) && !AppUtil.isObjectEmpty(maxDate)) {
			criterias.add(Criteria.where("reservedInfo.date").gte(minDate).lte(maxDate));
		}
		if (criterias.isEmpty()) {
			return new ArrayList<>();
		}
		criterias.add(Criteria.where("reserve").is(Constants.RESERVED));
		final Criteria matchCriteria = new Criteria();
		matchCriteria.andOperator(criterias.toArray(new Criteria[0]));

		final MatchOperation match = Aggregation.match(matchCriteria);

		final LookupOperation lookupCustomer = LookupOperation.newLookup().from("t_cstmr")
				.localField("reservedInfo.customerId").foreignField("code").as("customer");

		final LookupOperation lookupCurrency = LookupOperation.newLookup().from("m_currency")
				.localField("customer.currencyType").foreignField("currencySeq").as("currencyDtls");

		final AggregationOperation group = (context) -> new Document("$group",
				new Document("_id", new Document("$month", "$reservedInfo.date"))
						.append("date", new Document("$first", "$reservedInfo.date"))
						.append("customerId", new Document("$first", "$reservedInfo.customerId"))
						.append("currencySymbol", new Document("$first", "$currencyDtls.symbol"))
						.append("currency", new Document("$first", "$currencyDtls.currencySeq")));

		final SortOperation sort = Aggregation.sort(Direction.ASC, "date");

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupCustomer,
				Aggregation.unwind("$customer", true), lookupCurrency, Aggregation.unwind("$currencyDtls", true), group,
				sort);

		final AggregationResults<CustomerTransactionDto> result = mongoTemplate.aggregate(aggregation, "t_stck",
				CustomerTransactionDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<CustomerTransactionItemsDto> findAllCustomerTransactionsOnExpand(String customerId, Date date) {
		final List<Criteria> criterias = new ArrayList<>();
		if (!AppUtil.isObjectEmpty(date) && !AppUtil.isObjectEmpty(customerId)) {
			criterias.add(Criteria.where("reservedInfo.date").gte(AppUtil.startDateOfMonth(date))
					.lte(AppUtil.endDateOfMonth(date)));
			criterias.add(Criteria.where("reservedInfo.customerId").is(customerId));

		}
		criterias.add(Criteria.where("reserve").is(Constants.RESERVED));
		final Criteria matchCriteria = new Criteria();
		matchCriteria.andOperator(criterias.toArray(new Criteria[0]));

		final MatchOperation match = Aggregation.match(matchCriteria);

		final LookupOperation lookupCustomer = LookupOperation.newLookup().from("t_cstmr")
				.localField("reservedInfo.customerId").foreignField("code").as("customer");

		final LookupOperation lookupSalesInvoice = LookupOperation.newLookup().from("t_sls_inv")
				.localField("salesInvoiceId").foreignField("_id").as("salesInvoice");

		final LookupOperation lookupPurchaseInvoice = LookupOperation.newLookup().from("t_prchs_invc")
				.localField("stockNo").foreignField("stockNo").as("purchaseDetails");

		final LookupOperation lookupMSupplier = LookupOperation.newLookup().from("m_spplr")
				.localField("purchaseDetails.supplierId").foreignField("supplierCode").as("supplier");

		final LookupOperation lookupLcInvoice = LookupOperation.newLookup().from("t_lc_invc").localField("stockNo")
				.foreignField("stockNo").as("lcInvoice");

		final LookupOperation lookupLcInvoiceDtls = LookupOperation.newLookup().from("t_lc_dtls")
				.localField("lcInvoice.lcDtlId").foreignField("_id").as("lcInvoiceDtls");

		final LookupOperation lookupShippingRequest = LookupOperation.newLookup().from("t_shppng_rqust")
				.localField("shipmentRequestId").foreignField("shipmentRequestId").as("shippingRequest");

		final LookupOperation lookupShip = LookupOperation.newLookup().from("m_ship").localField("schedule.shipId")
				.foreignField("shipId").as("m_ship");

		final LookupOperation lookupShipmentSchedule = LookupOperation.newLookup().from("t_shpmnt_schdl")
				.localField("shippingRequest.scheduleId").foreignField("scheduleId").as("schedule");

		final LookupOperation lookupCustStock = LookupOperation.newLookup().from("t_cstmr_stck_trnsctn")
				.localField("stockNo").foreignField("stockNo").as("custStockDetails");

		final AggregationOperation addETDField = (context) -> new Document("$addFields", new Document("etd",
				new Document("$filter", new Document("input", "$schedule.schedule").append("as", "result").append(
						"cond",
						new Document("$eq", Arrays.asList("$$result.portName", "$shippingRequest.orginPort"))))));

		final AggregationOperation addETAField = (context) -> new Document("$addFields", new Document("eta",
				new Document("$filter", new Document("input", "$schedule.schedule").append("as", "result").append(
						"cond",
						new Document("$eq", Arrays.asList("$$result.portName", "$shippingRequest.destPort"))))));

		final AggregationOperation project = context -> new Document("$project", new Document("id", "$_id")
				.append("invoiceNo", "$salesInvoice.invoiceNo").append("paymentType", "$salesInvoice.paymentType")
				.append("totalPrice", "$salesInvoice.total").append("date", "$reservedInfo.date")// .append("date",
																									// "$reservedInfo.date")
				.append("customerId", "$reservedInfo.customerId").append("firstName", "$customer.firstName")
				.append("lastName", "$customer.lastName").append("lcNumber", "$lcInvoiceDtls.lcNo")
				.append("stockNo", "$stockNo").append("chassisNo", "$chassisNo")
				.append("commision", "$purchaseDetails.commision").append("price", "$reservedInfo.price")
				.append("recycleCost", "$purchaseDetails.recycle").append("roadTax", "$purchaseDetails.roadTax")
				.append("lotNo", "$purchaseInfo.auctionInfo.lotNo").append("auction", "$supplier.company")
				.append("otherCharges", "$purchaseDetails.otherCharges")
				.append("purchaseCost", "$purchaseDetails.purchaseCost").append("dhlNo", "$shippingRequest.dhlNo")
				.append("additionalCharges",
						new Document("$sum",
								Arrays.asList("$purchaseDetails.recycle", "$purchaseDetails.roadTax",
										"$purchaseDetails.otherCharges")))
				.append("shippingStatus", "$shippingInstructionStatus").append("vesselId", "$m_ship.shippingCompanyNo")
				.append("vesselName", "$m_ship.shippingCompanyName").append("containerNo", "$shippingStatus")
				.append("etd", "$etd.date").append("eta", "$eta.date"));

		AggregationExpression receivedmount = context -> {
			Document query = new Document();
			query.put("$cond", new Document("if",
					new Document("$eq",
							Arrays.<Object>asList("$custStockDetails.transactionType", Constants.TRANSACTION_CREDIT)))
									.append("then", "$custStockDetails.amount").append("else", "0.0"));

			return query;
		};

		final GroupOperation group = Aggregation.group("$stockNo")
				.sum(ConditionalOperators
						.when(Criteria.where("custStockDetails.transactionType").ne(Constants.TRANSACTION_DEBIT))
						.thenValueOf("$custStockDetails.amount").otherwise("0.0"))
				.as("receivedAmount").first("invoiceNo").as("invoiceNo").first("paymentType").as("paymentType")
				.first("totalPrice").as("totalPrice").first("date").as("date").first("customerId").as("customerId")
				.first("firstName").as("firstName").first("lastName").as("lastName").first("lcNumber").as("lcNumber")
				.first("stockNo").as("stockNo").first("chassisNo").as("chassisNo").first("commision").as("commision")
				.first("price").as("price").first("lotNo").as("lotNo").first("auction").as("auction")
				.first("purchaseCost").as("purchaseCost").first("dhlNo").as("dhlNo").first("additionalCharges")
				.as("additionalCharges").first("shippingStatus").as("shippingStatus").first("vesselId").as("vesselId")
				.first("vesselName").as("vesselName").first("containerNo").as("containerNo").first("etd").as("etd")
				.first("eta").as("eta");

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupCustomer,
				Aggregation.unwind("$customer", true), lookupSalesInvoice, Aggregation.unwind("$salesInvoice", true),
				lookupPurchaseInvoice, Aggregation.unwind("$purchaseDetails", true), lookupMSupplier,
				Aggregation.unwind("$supplier", true), lookupLcInvoice, Aggregation.unwind("$lcInvoice", true),
				lookupLcInvoiceDtls, Aggregation.unwind("$lcInvoiceDtls", true), lookupShippingRequest,
				Aggregation.unwind("$shippingRequest", true), lookupShipmentSchedule,
				Aggregation.unwind("$schedule", true), lookupShip, Aggregation.unwind("$m_ship", true), addETDField,
				addETAField, Aggregation.unwind("$etd", true), Aggregation.unwind("$eta", true), project,
				lookupCustStock, Aggregation.unwind("$custStockDetails", true), group);

		final AggregationResults<CustomerTransactionItemsDto> result = mongoTemplate.aggregate(aggregation, "t_stck",
				CustomerTransactionItemsDto.class);
		return result.getMappedResults();
	}

	@Override
	public TSalesInvoiceDto getOneByInvoiceNo(String invoiceNo) {
		final MatchOperation match = Aggregation.match(Criteria.where("invoiceNo").is(invoiceNo));
		final LookupOperation lookupCustomer = LookupOperation.newLookup().from("t_cstmr").localField("customerId")
				.foreignField("code").as("customer");
		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stockDtls");
		final AggregationOperation addConsignee = (context) -> new Document("$addFields", new Document("consignee",
				new Document("$filter", new Document("input", "$customer.consigneeNotifyparties").append("as", "result")
						.append("cond", new Document("$eq", Arrays.asList("$$result._id", "$consigneeId"))))));

		final AggregationOperation addNotifyParty = (context) -> new Document("$addFields",
				new Document("notifyparty",
						new Document("$filter",
								new Document("input", "$customer.consigneeNotifyparties").append("as", "result").append(
										"cond",
										new Document("$eq", Arrays.asList("$$result._id", "$notifypartyId"))))));
		final LookupOperation Currency = LookupOperation.newLookup().from("m_currency").localField("currencyType")
				.foreignField("currencySeq").as("currencyDtls");
		final GroupOperation group = Aggregation.group("$invoiceNo")
				.push(new BasicDBObject("stockNo", "$stockDtls.stockNo").append("chassisNo", "$stockDtls.chassisNo")
						.append("maker", "$stockDtls.maker").append("model", "$stockDtls.model")
						.append("transmission", "$stockDtls.transmission").append("fuel", "$stockDtls.fuel")
						.append("driven", "$stockDtls.driven").append("cc", "$stockDtls.cc")
						.append("type", "$stockDtls.purchaseInfo.type")
						.append("firstRegDate", "$stockDtls.firstRegDate").append("total", "$total")
						.append("currencySymbol", "$currencyDtls.symbol").append("status", "$status"))
				.as("salesInvoiceDetails").first("customer.firstName").as("customerFN").first("customer.lastName")
				.as("customerLN").first("customer.isLcCustomer").as("isLcCustomer").first("customer.port").as("port")
				.first("customer.country").as("country").first("createdDate").as("createdDate")
				.first("customer.paymentType").as("paymentType").first("consignee.cFirstName").as("cFirstName")
				.first("consignee.cLastName").as("cLastName").first("consignee.cAddress").as("cAddress")
				.first("consignee.cMobileNo").as("cMobileNo").first("notifyparty.npFirstName").as("npFirstName")
				.first("notifyparty.npLastName").as("npLastName").first("notifyparty.npAddress").as("npAddress")
				.first("notifyparty.npMobileNo").as("npMobileNo").first("invoiceNo").as("invoiceNo").first("fobTotal")
				.as("fobTotal").first("freightTotal").as("freightTotal").first("shippingTotal").as("shippingTotal")
				.first("insuranceTotal").as("insuranceTotal").first("customerId").as("customerId").first("createdDate")
				.as("issueDate").first("currencyDtls.symbol").as("currencySymbol");
		final ProjectionOperation project = Aggregation.project()
				.andInclude("salesInvoiceDetails", "customerFN", "customerLN", "cFirstName", "cLastName", "npFirstName",
						"npLastName", "invoiceNo", "createdDate", "paymentType", "isLcCustomer", "cAddress",
						"cMobileNo", "npAddress", "npMobileNo", "issueDate", "port", "country", "currencySymbol")
				.and(AccumulatorOperators.Sum.sumOf("salesInvoiceDetails.total")).as("allTtotal");
		// .and(AccumulatorOperators.Sum.sumOf("salesInvoiceDetails.total")).as("total");

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupCustomer,
				Aggregation.unwind("$customer"), lookupStock, addConsignee, Aggregation.unwind("consignee", true),
				addNotifyParty, Aggregation.unwind("$notifyparty", true), Aggregation.unwind("$stockDtls", true),
				Currency, Aggregation.unwind("$currencyDtls", true), group, project);
		final AggregationResults<TSalesInvoiceDto> result = mongoTemplate.aggregate(aggregation, "t_sls_inv",
				TSalesInvoiceDto.class);

		return result.getUniqueMappedResult();
	}

	@Override
	public List<StockSalesReportDto> getStockSalesReport(Date fromDate, Date toDate, String maker, String model) {

		// filter purchase date
		Criteria dateRangeMatchCriteria = new Criteria();
		if (!AppUtil.isObjectEmpty(fromDate) && !AppUtil.isObjectEmpty(toDate)) {
			dateRangeMatchCriteria = Criteria.where("createdDate").gte(AppUtil.atStartOfDay(fromDate))
					.andOperator(Criteria.where("createdDate").lte(AppUtil.atEndOfDay(toDate)));
		}
		final MatchOperation dateRangeMatch = Aggregation.match(dateRangeMatchCriteria);
		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");
		final List<Criteria> andCriteria = new ArrayList<>();
		if (!AppUtil.isObjectEmpty(maker)) {
			andCriteria.add(Criteria.where("stock.maker").is(maker));
			if (!AppUtil.isObjectEmpty(model)) {
				andCriteria.add(Criteria.where("stock.model").is(model));
			}
		}
		final Criteria criteria = new Criteria();
		if (!andCriteria.isEmpty()) {
			criteria.andOperator(andCriteria.toArray(new Criteria[0]));
		}
		final MatchOperation match = Aggregation.match(criteria);

		final LookupOperation lookupPurchaseInvoice = LookupOperation.newLookup().from("t_prchs_invc")
				.localField("stockNo").foreignField("stockNo").as("purchaseInvoice");

		final ProjectionOperation project = Aggregation.project().andInclude("stockNo", "createdDate", "exchangeRate")
				.and("purchaseInvoice.purchaseCost").as("purchaseCost").and("purchaseInvoice.commision").as("commision")
				.and("total").as("sTotal").and("purchaseInvoice.otherCharges").as("otherCharges");
		final Aggregation aggregation = Aggregation.newAggregation(dateRangeMatch, lookupStock,
				Aggregation.unwind("stock", true), match, lookupPurchaseInvoice,
				Aggregation.unwind("purchaseInvoice", true), project);
		final AggregationResults<StockSalesReportDto> result = mongoTemplate.aggregate(aggregation, TSalesInvoice.class,
				StockSalesReportDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<TCustomerAccountsTransactionDto> findAllCustomerTransactions() {

		final LookupOperation lookupLcInvoice = LookupOperation.newLookup().from("t_lc_invc").localField("stockNo")
				.foreignField("stockNo").as("lcInvoice");
		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");
		final LookupOperation lookupCustomer = LookupOperation.newLookup().from("t_cstmr").localField("customerId")
				.foreignField("code").as("customer");
		final LookupOperation lookupUser = LookupOperation.newLookup().from("m_usr").localField("salesPerson")
				.foreignField("code").as("userDetails");
		final LookupOperation lookupLcInvoiceDetails = LookupOperation.newLookup().from("t_lc_dtls")
				.localField("$lcInvoice.lcDtlId").foreignField("_id").as("lcInvoiceDetails");
		final AggregationOperation project = context -> new Document("$project",
				new Document("invoiceNo", "$invoiceNo").append("customerId", "$customerId")
						.append("amountReceived", "$amountReceived").append("stockNo", "$stockNo")
						.append("customerName", "$customer.firstName").append("chassisNo", "$stock.chassisNo")
						.append("createdBy", "$createdBy").append("salesPersonName", "$userDetails.fullname")
						.append("date", "$createdDate").append("lcAmount", "$lcInvoice.amount")
						.append("lcNo", "$lcInvoiceDetails.lcNo").append("invoiceAmount",
								new Document("$add",
										Arrays.asList(new Document("$ifNull", Arrays.asList("$freight", 0)),
												new Document("$ifNull", Arrays.asList("$fob", 0)),
												new Document("$ifNull", Arrays.asList("$shipping", 0)),
												new Document("$ifNull", Arrays.asList("$insurance", 0))))));

		final Aggregation aggregation = Aggregation.newAggregation(lookupLcInvoice,
				Aggregation.unwind("$lcInvoice", true), lookupCustomer, Aggregation.unwind("$lookupCustomer", true),
				lookupUser, Aggregation.unwind("$lookupCustomer", true), lookupLcInvoiceDetails,
				Aggregation.unwind("$lcInvoiceDetails", true), lookupStock, Aggregation.unwind("$stock", true),
				project);
		final AggregationResults<TCustomerAccountsTransactionDto> result = mongoTemplate.aggregate(aggregation,
				"t_sls_inv", TCustomerAccountsTransactionDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<CustomerTransactionReportDto> findAllCustomerTransactionsReport(String customerId, Date minDate,
			Date maxDate) {
		final List<Criteria> criterias = new ArrayList<>();
		if (!AppUtil.isObjectEmpty(customerId)) {
			criterias.add(Criteria.where("reservedInfo.customerId").is(customerId));
		}
		if (!AppUtil.isObjectEmpty(minDate) && !AppUtil.isObjectEmpty(maxDate)) {
			criterias.add(Criteria.where("reservedInfo.date").gte(minDate).lte(maxDate));
		}
		if (criterias.isEmpty()) {
			return new ArrayList<>();
		}
		criterias.add(Criteria.where("reserve").is(Constants.RESERVED));
		final Criteria matchCriteria = new Criteria();
		matchCriteria.andOperator(criterias.toArray(new Criteria[0]));

		final MatchOperation match = Aggregation.match(matchCriteria);

		final LookupOperation lookupCustomer = LookupOperation.newLookup().from("t_cstmr")
				.localField("reservedInfo.customerId").foreignField("code").as("customer");

		final LookupOperation lookupSalesInvoice = LookupOperation.newLookup().from("t_sls_inv").localField("stockNo")
				.foreignField("stockNo").as("salesInvoice");

		final LookupOperation lookupPurchaseInvoice = LookupOperation.newLookup().from("t_prchs_invc")
				.localField("stockNo").foreignField("stockNo").as("purchaseDetails");

		final LookupOperation lookupLcInvoice = LookupOperation.newLookup().from("t_lc_invc").localField("stockNo")
				.foreignField("stockNo").as("lcInvoice");

		final LookupOperation lookupLcInvoiceDtls = LookupOperation.newLookup().from("t_lc_dtls")
				.localField("lcInvoice.lcDtlId").foreignField("_id").as("lcInvoiceDtls");

		final LookupOperation lookupShippingRequest = LookupOperation.newLookup().from("t_shppng_rqust")
				.localField("shipmentRequestId").foreignField("shipmentRequestId").as("shippingRequest");

		final LookupOperation lookupShip = LookupOperation.newLookup().from("m_ship")
				.localField("shippingRequest.vesselId").foreignField("shipId").as("m_ship");

		final LookupOperation lookupShipmentSchedule = LookupOperation.newLookup().from("t_shpmnt_schdl")
				.localField("shippingRequest.scheduleId").foreignField("scheduleId").as("schedule");

		final AggregationOperation addETDField = (context) -> new Document("$addFields", new Document("etd",
				new Document("$filter", new Document("input", "$schedule.schedule").append("as", "result").append(
						"cond",
						new Document("$eq", Arrays.asList("$$result.portName", "$shippingRequest.orginPort"))))));

		final AggregationOperation addETAField = (context) -> new Document("$addFields", new Document("eta",
				new Document("$filter", new Document("input", "$schedule.schedule").append("as", "result").append(
						"cond",
						new Document("$eq", Arrays.asList("$$result.portName", "$shippingRequest.destPort"))))));

		final AggregationOperation project = (context) -> new Document("$project", new Document("id", "$_id")
				.append("invoiceNo", "$salesInvoice.invoiceNo").append("paymentType", "$salesInvoice.paymentType")
				.append("fobTotal", "$salesInvoice.fob").append("shippingTotal", "$salesInvoice.shipping")
				.append("freightTotal", "$salesInvoice.freight").append("insuranceTotal", "$salesInvoice.insurance")
				.append("date", "$reservedInfo.date").append("customerId", "$reservedInfo.customerId")
				.append("firstName", "$customer.firstName").append("lastName", "$customer.lastName")
				.append("lcNumber", "$lcInvoiceDtls.lcNo").append("stockNo", "$stockNo")
				.append("chassisNo", "$chassisNo").append("commision", "$purchaseDetails.commision")
				.append("price", "$reservedInfo.price").append("recycleCost", "$purchaseDetails.recycle")
				.append("roadTax", "$purchaseDetails.roadTax").append("lotNo", "$purchaseInfo.auctionInfo.lotNo")
				.append("auction", "$purchaseInfo.supplier").append("otherCharges", "$purchaseDetails.otherCharges")
				.append("purchaseCost", "$purchaseDetails.purchaseCost").append("dhlNo", "$shippingRequest.dhlNo")
				.append("additionalCharges",
						new Document("$sum",
								Arrays.asList("$purchaseDetails.recycle", "$purchaseDetails.roadTax",
										"$purchaseDetails.otherCharges")))
				.append("shippingStatus", "$shippingInstructionStatus").append("vesselId", "$m_ship.shippingCompanyNo")
				.append("vesselName", "$m_ship.shippingCompanyName").append("containerNo", "$shippingStatus")
				.append("etd", "$etd.date").append("eta", "$eta.date"));

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupCustomer,
				Aggregation.unwind("$customer", true), lookupSalesInvoice, Aggregation.unwind("$salesInvoice", true),
				lookupPurchaseInvoice, Aggregation.unwind("$purchaseDetails", true), lookupLcInvoice,
				Aggregation.unwind("$lcInvoice", true), lookupLcInvoiceDtls, Aggregation.unwind("$lcInvoiceDtls", true),
				lookupShippingRequest, Aggregation.unwind("$shippingRequest", true), lookupShip,
				Aggregation.unwind("$m_ship", true), lookupShipmentSchedule, Aggregation.unwind("$schedule", true),
				addETDField, addETAField, Aggregation.unwind("$etd", true), Aggregation.unwind("$eta", true), project);

		final AggregationResults<CustomerTransactionReportDto> result = mongoTemplate.aggregate(aggregation, "t_stck",
				CustomerTransactionReportDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<CostofSalesDto> getListWithStockDetails() {

		final LookupOperation lookupCustomer = LookupOperation.newLookup().from("t_cstmr").localField("customerId")
				.foreignField("code").as("customer");
		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");
		final LookupOperation lookupPurchaseInvoice = LookupOperation.newLookup().from("t_prchs_invc")
				.localField("stock.purchaseInvoiceCode").foreignField("code").as("purchaseInvoice");
		final LookupOperation lookupExchange = LookupOperation.newLookup().from("t_exhg_rate").localField("createdDate")
				.foreignField("date").as("exchange");
		final LookupOperation lookupCurrency = LookupOperation.newLookup().from("m_currency")
				.localField("customer.currencyType").foreignField("currencySeq").as("currencyDtls");

		final AggregationOperation addConsignee = (context) -> new Document("$addFields", new Document("consignee",
				new Document("$filter", new Document("input", "$customer.consigneeNotifyparties").append("as", "result")
						.append("cond", new Document("$eq", Arrays.asList("$$result._id", "$consigneeId"))))));

		final AggregationOperation addNotifyParty = (context) -> new Document("$addFields",
				new Document("notifyparty",
						new Document("$filter",
								new Document("input", "$customer.consigneeNotifyparties").append("as", "result").append(
										"cond",
										new Document("$eq", Arrays.asList("$$result._id", "$notifypartyId"))))));

		final ProjectionOperation project = Aggregation.project()
				.andInclude("id", "invoiceNo", "stockNo", "customerId", "fob", "insurance", "shipping", "freight",
						"exchangeRate")
				.and("stock.chassisNo").as("chassisNo").and("stock.purchaseInfo.date").as("purchaseDate")
				.and("stock.purchaseInfo.type").as("type").and("currencyDtls.symbol").as("currencySymbol")
				.and("purchaseInvoice.purchaseCost").as("purchaseCost").and("purchaseInvoice.commision").as("commision")
				.and("purchaseInvoice.otherCharges").as("otherCharge").and("purchaseInvoice.roadTax").as("roadTax")
				.and("purchaseInvoice.recycle").as("recycle").and("createdDate").as("salesDate");

		final Aggregation aggregation = Aggregation.newAggregation(lookupCustomer,
				Aggregation.unwind("$customer", true), lookupCurrency, Aggregation.unwind("$currencyDtls", true),
				lookupStock, Aggregation.unwind("$stock", true), lookupPurchaseInvoice,
				Aggregation.unwind("$purchaseInvoice", true), lookupExchange, Aggregation.unwind("$exchange", true),
				addConsignee, Aggregation.unwind("$consignee", true), addNotifyParty,
				Aggregation.unwind("$notifyparty", true), project);
		final AggregationResults<CostofSalesDto> result = mongoTemplate.aggregate(aggregation, "t_sls_inv",
				CostofSalesDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<SalesSummaryDto> getSalesSummaryReport() {
		final LookupOperation lookupCustomer = LookupOperation.newLookup().from("t_cstmr").localField("customerId")
				.foreignField("code").as("customer");
		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");
		final LookupOperation lookupUser = LookupOperation.newLookup().from("m_usr").localField("salesPerson")
				.foreignField("code").as("userDetails");
		final LookupOperation lookupLogin = LookupOperation.newLookup().from("m_lgn").localField("salesPerson")
				.foreignField("userId").as("loginDetails");
		final LookupOperation lookupCurrency = LookupOperation.newLookup().from("m_currency").localField("currencyType")
				.foreignField("currencySeq").as("currencyDtls");
		final LookupOperation lookupLocation = LookupOperation.newLookup().from("m_office_lctn")
				.localField("loginDetails.location").foreignField("code").as("locationDetails");
		final LookupOperation lookupInventoryCost = LookupOperation.newLookup().from("t_invtry_cst")
				.localField("stockNo").foreignField("stockNo").as("inventoryCost");
		final LookupOperation lookupShippingRequest = LookupOperation.newLookup().from("t_shppng_rqust")
				.localField("stockNo").foreignField("stockNo").as("shippingRequest");
		final LookupOperation lookupShipmentSchedule = LookupOperation.newLookup().from("t_shpmnt_schdl")
				.localField("shippingRequest.scheduleId").foreignField("scheduleId").as("schedule");
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

		final AggregationOperation addETAField = (context) -> new Document("$addFields",
				new Document("eta",
						new Document("$filter",
								new Document("input", "$schedule.schedule").append("as", "result").append("cond",
										new Document("$and", Arrays.asList(
												new Document("$eq",
														Arrays.asList("$$result.portName",
																"$shippingRequest.destPort")),
												new Document("$eq",
														Arrays.asList("$$result.portFlag", "destination"))))))));
		final ProjectionOperation project = Aggregation.project()
				.andInclude("id", "invoiceNo", "stockNo", "customerId", "total", "status", "salesPerson",
						"exchangeRate")
				.and("currencyDtls.symbol").as("currencySymbol").and("stock.chassisNo").as("chassisNo")
				.and("stock.purchaseInfo.date").as("purchaseDate").and("createdDate").as("soldDate")
				.and("customer.firstName").as("customerName").and("stock.purchaseInfo.type").as("type")
				.and("stock.maker").as("maker").and("stock.model").as("model").and("stock.destinationCountry")
				.as("destinationCountry").and("stock.destinationPort").as("destinationPort").and("stock.isBidding")
				.as("isBidding").and("fob").as("marginPercentage").and("userDetails.fullname").as("salesPersonName")
				.and("costOfGoods").as("costOfGoods").and("locationDetails.location").as("location")
				.and("locationDetails.code").as("locationId").and("etd.date").as("etd").and("eta.date").as("eta");
		final AggregationOperation costOfGoods = (context) -> new Document("$addFields",
				new Document("costOfGoods", new Document("$sum", "$inventoryCost.amount")));
		final SortOperation sort = Aggregation.sort(Direction.DESC, "soldDate");
		final Aggregation aggregation = Aggregation.newAggregation(lookupCustomer,
				Aggregation.unwind("$customer", true), lookupCurrency, Aggregation.unwind("$currencyDtls", true),
				lookupInventoryCost, lookupStock, Aggregation.unwind("$stock", true), lookupUser, lookupLogin,
				Aggregation.unwind("$userDetails", true), Aggregation.unwind("$loginDetails", true), lookupLocation,
				Aggregation.unwind("$locationDetails", true), costOfGoods, lookupShippingRequest,
				Aggregation.unwind("$shippingRequest", true), lookupShipmentSchedule,
				Aggregation.unwind("$schedule", true), lookupMLocation, Aggregation.unwind("$location_dtls", true),
				addETDField, Aggregation.unwind("$etd", true), addETAField, Aggregation.unwind("$eta", true), project,
				sort);
		final AggregationResults<SalesSummaryDto> result = mongoTemplate.aggregate(aggregation, "t_sls_inv",
				SalesSummaryDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<RecentSalesDto> recentSalesOrder() {
		// TODO Auto-generated method stub
		final Date currentDate = new Date();

		final Calendar aCalendar = Calendar.getInstance();
		aCalendar.add(Calendar.DATE, -5);
		final Date fromDate = aCalendar.getTime();

		Criteria criteria = new Criteria();
		criteria = Criteria.where("createdDate").gte(fromDate)
				.andOperator(Criteria.where("createdDate").lte(currentDate));

		final MatchOperation matchOperation = Aggregation.match(criteria);
		final LookupOperation lookupCustomer = LookupOperation.newLookup().from("t_cstmr").localField("customerId")
				.foreignField("code").as("customer");
		final LookupOperation lookupSalesPerson = LookupOperation.newLookup().from("m_usr").localField("salesPerson")
				.foreignField("code").as("sp");

		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stockDtls");
		final LookupOperation lookupCurrency = LookupOperation.newLookup().from("m_currency")
				.localField("customer.currencyType").foreignField("currencySeq").as("currencyDtls");
		final LookupOperation lookupInventoryCost = LookupOperation.newLookup().from("t_invtry_cst")
				.localField("stockNo").foreignField("stockNo").as("invntryCst");

		final AggregationOperation addInventoryCost = (context) -> new Document("$addFields", new Document(
				"invntryCstAmount",
				new Document("$filter", new Document("input", "$invntryCst").append("as", "result").append("cond",
						new Document("$eq", Arrays.asList("$$result.type", Constants.COST_OF_GOODS_TYPE_PURCHASE))))));

		final AggregationOperation addConsignee = (context) -> new Document("$addFields", new Document("consignee",
				new Document("$filter", new Document("input", "$customer.consigneeNotifyparties").append("as", "result")
						.append("cond", new Document("$eq", Arrays.asList("$$result._id", "$consigneeId"))))));

		final AggregationOperation addNotifyParty = (context) -> new Document("$addFields",
				new Document("notifyparty",
						new Document("$filter",
								new Document("input", "$customer.consigneeNotifyparties").append("as", "result").append(
										"cond",
										new Document("$eq", Arrays.asList("$$result._id", "$notifypartyId"))))));

		final ProjectionOperation project = Aggregation.project()
				.andInclude("invoiceNo", "createdDate", "customerId", "stockNo").and("customer.firstName")
				.as("fCustomerName").and("salesPerson").as("salesPersonId").and("sp.fullname").as("salesPerson")
				.and("stockDtls.maker").as("maker").and("stockDtls.model").as("model").and("consignee.cFirstName")
				.as("fConsigneeName").and(AccumulatorOperators.Sum.sumOf("$invntryCstAmount.amount"))
				.as("purchaseCostTotal").and("total").as("salesAmount").and("currencyDtls.symbol").as("currencySymbol");

		final SortOperation sort = Aggregation.sort(Direction.DESC, "createdDate");

		final Aggregation aggregation = Aggregation.newAggregation(matchOperation, lookupCustomer,
				Aggregation.unwind("$customer", true), lookupStock, Aggregation.unwind("$stockDtls", true),
				lookupSalesPerson, Aggregation.unwind("$sp", true), lookupCurrency,
				Aggregation.unwind("$currencyDtls", true), addConsignee, Aggregation.unwind("consignee", true),
				addNotifyParty, Aggregation.unwind("$notifyparty", true), lookupInventoryCost, addInventoryCost,
				project, sort);

		final AggregationResults<RecentSalesDto> result = mongoTemplate.aggregate(aggregation, "t_sls_inv",
				RecentSalesDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<ReceivableAmountDto> getReceivableAmountForCustomer() {
		final MatchOperation match = Aggregation.match(Criteria.where("status")
				.in(Constants.SALES_INV_PAYMENT_NOT_RECEIVED, Constants.SALES_INV_PAYMENT_RECEIVED_PARTIAL));
		final LookupOperation lookupCustomer = LookupOperation.newLookup().from("t_cstmr").localField("customerId")
				.foreignField("code").as("customer");
		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stock");
		final LookupOperation lookupMasterCurrency = LookupOperation.newLookup().from("m_currency")
				.localField("customer.currencyType").foreignField("currencySeq").as("currencyDetails");

		final AggregationOperation groupOperation = (
				context) -> new Document("$group",
						new Document("_id", "$customerId")
								.append("salesDetails",
										new Document("$push", new BasicDBObject("chassisNo", "$stock.chassisNo")
												.append("stockNo", "$stock.stockNo").append("fob", "$fob")
												.append("insurance", "$insurance").append("shipping", "$shipping")
												.append("freight", "$freight").append("maker", "$stock.maker")
												.append("invoiceNo", "$invoiceNo")
												.append("amountReceived", "$amountReceived")
												.append("model", "$stock.model").append("total",
														new Document("$add",
																Arrays.asList("$fob", "$insurance", "$shipping",
																		"$freight")))))

								.append("customerId", new Document("$first", "$customerId")));

		final AggregationOperation finalProject = (context) -> new Document("$project",
				new Document("id", "$_id").append("salesDetails", "$salesDetails").append("invoiceNo", "$invoiceNo")
						.append("stockNo", "$stockNo").append("code", "$customerId")
						.append("firstName", "$customer.firstName").append("lastName", "$customer.lastName")
						.append("creditBalance", "$customer.creditBalance").append("balance", "$customer.balance")
						.append("total", new Document("$sum", "$salesDetails.total"))
						.append("amountReceived", new Document("$sum", "$salesDetails.amountReceived"))
						.append("currencyDetails", "$currencyDetails"));

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupStock,
				Aggregation.unwind("$stock", true), groupOperation, lookupCustomer,
				Aggregation.unwind("$customer", true), lookupMasterCurrency,
				Aggregation.unwind("$currencyDetails", true), finalProject);
		final AggregationResults<ReceivableAmountDto> result = mongoTemplate.aggregate(aggregation, "t_sls_inv",
				ReceivableAmountDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<ARAgingSummaryDto> getAgingSummary() throws ParseException {
		final MatchOperation match = Aggregation.match(Criteria.where("status")
				.in(Constants.SALES_INV_PAYMENT_NOT_RECEIVED, Constants.SALES_INV_PAYMENT_RECEIVED_PARTIAL));
		final LookupOperation lookupCustomer = LookupOperation.newLookup().from("t_cstmr").localField("_id")
				.foreignField("code").as("customer");
		final Document invoiceTotal = new Document("$add",
				Arrays.asList("$fob", "$insurance", "$shipping", "$freight"));
		final Document total = new Document("$subtract", Arrays.asList(invoiceTotal, "$amountReceived"));
		final AggregationOperation groupOperation = context -> new Document("$group",
				new Document("_id", "$customerId").append("code", new Document("$first", "$invoiceNo")).append(
						"salesDetails",
						new Document("$push", new BasicDBObject("chassisNo", "$chassisNo").append("stockNo", "$stockNo")
								.append("fob", "$fob").append("insurance", "$insurance").append("shipping", "$shipping")
								.append("freight", "$freight").append("maker", "$maker").append("model", "$model")
								.append("createdDate", "$createdDate").append("amountReceived", "$amountReceived")
								.append("total", new Document("$multiply", Arrays.asList(total, "$exchangeRate"))))));
		final Date oneDayBefore = AppUtil.subtractDays(-1);
		final Date last30Days = AppUtil.subtractDays(-30);
		final Date last60Days = AppUtil.subtractDays(-60);
		final Date last90Days = AppUtil.subtractDays(-90);

		final AggregationOperation addCurrentDate = (
				context) -> new Document("$addFields",
						new Document("current",
								new Document("$filter",
										new Document("input", "$$ROOT.salesDetails").append("as", "result")
												.append("cond",
														new Document("$and",
																Arrays.asList(
																		new Document("$gte",
																				Arrays.asList("$$result.createdDate",
																						AppUtil.atStartOfDay(
																								new Date()))),
																		new Document("$lte", Arrays.asList(
																				"$$result.createdDate",
																				AppUtil.atEndOfDay(new Date())))))))));
		final AggregationOperation add30Days = context -> new Document("$addFields", new Document("aged30",
				new Document("$filter", new Document("input", "$$ROOT.salesDetails").append("as", "result")
						.append("cond", new Document("$and", Arrays.asList(
								new Document("$gt",
										Arrays.asList("$$result.createdDate", AppUtil.atStartOfDay(last30Days))),
								new Document("$lt",
										Arrays.asList("$$result.createdDate", AppUtil.atEndOfDay(oneDayBefore)))))))));
		final AggregationOperation add60Days = (
				context) -> new Document("$addFields",
						new Document("aged60",
								new Document("$filter",
										new Document("input", "$$ROOT.salesDetails").append("as", "result")
												.append("cond",
														new Document("$and",
																Arrays.asList(
																		new Document("$gt",
																				Arrays.asList("$$result.createdDate",
																						AppUtil.atStartOfDay(
																								last60Days))),
																		new Document("$lt", Arrays.asList(
																				"$$result.createdDate",
																				AppUtil.atEndOfDay(last30Days)))))))));
		final AggregationOperation add90Days = (
				context) -> new Document("$addFields",
						new Document("aged90",
								new Document("$filter",
										new Document("input", "$$ROOT.salesDetails").append("as", "result")
												.append("cond",
														new Document("$and",
																Arrays.asList(
																		new Document("$gt",
																				Arrays.asList("$$result.createdDate",
																						AppUtil.atStartOfDay(
																								last90Days))),
																		new Document("$lt", Arrays.asList(
																				"$$result.createdDate",
																				AppUtil.atEndOfDay(last60Days)))))))));

		final AggregationOperation addGreater90Days = (context) -> new Document("$addFields", new Document(
				"agedAbove90",
				new Document("$filter", new Document("input", "$$ROOT.salesDetails").append("as", "result").append(
						"cond",
						new Document("$lte", Arrays.asList("$$result.createdDate", AppUtil.atEndOfDay(last90Days)))))));

		final ProjectionOperation project = Aggregation.project().andInclude("customerId")
				.and(AccumulatorOperators.Sum.sumOf("$current.total")).as("current")
				.and(AccumulatorOperators.Sum.sumOf("$aged30.total")).as("aged30")
				.and(AccumulatorOperators.Sum.sumOf("$aged60.total")).as("aged60")
				.and(AccumulatorOperators.Sum.sumOf("$aged90.total")).as("aged90")
				.and(AccumulatorOperators.Sum.sumOf("$agedAbove90.total")).as("agedAbove90")
				.and(AccumulatorOperators.Sum.sumOf("$amountReceived")).as("amountReceived").and("$customer.firstName")
				.as("customerName").and("$customer.code").as("customerId");

		final Aggregation aggregation = Aggregation.newAggregation(match, groupOperation, addCurrentDate, add30Days,
				add60Days, add90Days, addGreater90Days, lookupCustomer, Aggregation.unwind("$customer", true), project);
		final AggregationResults<ARAgingSummaryDto> result = mongoTemplate.aggregate(aggregation, "t_sls_inv",
				ARAgingSummaryDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<TSalesInvoice> fifoSalesInvoice(String custId) {
		final Criteria criteria = new Criteria();
		criteria.andOperator(Criteria.where("customerId").is(custId), Criteria.where("status")
				.in(Constants.SALES_INV_PAYMENT_NOT_RECEIVED, Constants.SALES_INV_PAYMENT_RECEIVED_PARTIAL));
		final MatchOperation match = Aggregation.match(criteria);
		final SortOperation sort = Aggregation.sort(Direction.ASC, "createdDate");
		final Aggregation aggregation = Aggregation.newAggregation(match, sort);
		final AggregationResults<TSalesInvoice> result = mongoTemplate.aggregate(aggregation, TSalesInvoice.class,
				TSalesInvoice.class);
		return result.getMappedResults();
	}

	@Override
	public List<TTUnitAllocationDto> findAllInvoiceByCustomerAndPaymentStatus(String custId) {

		final Criteria criteria = new Criteria();
		criteria.andOperator(Criteria.where("customerId").is(custId), Criteria.where("status")
				.in(Constants.SALES_INV_PAYMENT_NOT_RECEIVED, Constants.SALES_INV_PAYMENT_RECEIVED_PARTIAL));
		final MatchOperation match = Aggregation.match(criteria);

		final LookupOperation lookupstockDetail = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stockDetail");
		final LookupOperation lookupshippingDetail = LookupOperation.newLookup().from("t_shppng_rqust")
				.localField("stockNo").foreignField("stockNo").as("shippingDetail");
		final LookupOperation lookupShipmentSchedule = LookupOperation.newLookup().from("t_shpmnt_schdl")
				.localField("shippingDetail.scheduleId").foreignField("scheduleId").as("schedule");
		final LookupOperation lookupMLocation = LookupOperation.newLookup().from("m_lctn")
				.localField("stockDetail.lastTransportLocation").foreignField("code").as("location_dtls");
		final LookupOperation currencyDetail = LookupOperation.newLookup().from("m_currency").localField("currencyType")
				.foreignField("currencySeq").as("currencyDetail");
		final AggregationOperation addETDField = (context) -> new Document("$addFields",
				new Document("etd",
						new Document("$filter", new Document("input", "$schedule.schedule").append("as", "result")
								.append("cond", new Document("$and", Arrays.asList(
										new Document("$eq",
												Arrays.asList("$$result.portName",
														"$location_dtls.shipmentOriginPort")),
										new Document("$eq", Arrays.asList("$$result.portFlag", "loading"))))))));
		final ProjectionOperation projectionOperation = Aggregation
				.project("invoiceNo", "stockNo", "total", "currencyType").and("$stockDetail.chassisNo").as("chassisNo")
				.and("amountAllocatted").as("received").and("$currencyDetail.symbol").as("currencySymbol")
				.and("etd.date").as("etd").andExpression("$amountAllocatted<$total").as("condition");

		MatchOperation matchStock = Aggregation.match(Criteria.where("condition").is(true));

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupstockDetail,
				Aggregation.unwind("$stockDetail", true), lookupshippingDetail,
				Aggregation.unwind("$shippingDetail", true), lookupShipmentSchedule,
				Aggregation.unwind("$schedule", true), lookupMLocation, Aggregation.unwind("$location_dtls", true),
				currencyDetail, Aggregation.unwind("$currencyDetail", true), addETDField,
				Aggregation.unwind("etd", true), projectionOperation, matchStock);

		final AggregationResults<TTUnitAllocationDto> result = mongoTemplate.aggregate(aggregation, "t_sls_inv",
				TTUnitAllocationDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<TTUnitAllocationDto> findAllFifoInvoiceByCustomerAndPaymentStatus(String custId) {

		final Criteria criteria = new Criteria();
		criteria.andOperator(Criteria.where("customerId").is(custId), Criteria.where("status")
				.in(Constants.SALES_INV_PAYMENT_NOT_RECEIVED, Constants.SALES_INV_PAYMENT_RECEIVED_PARTIAL));

		MatchOperation match = Aggregation.match(criteria);

		final LookupOperation lookupstockDetail = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stockDetail");
		final LookupOperation currencyDetail = LookupOperation.newLookup().from("m_currency").localField("currencyType")
				.foreignField("currencySeq").as("currencyDetail");
		final ProjectionOperation projectionOperation = Aggregation
				.project("invoiceNo", "stockNo", "total", "currencyType", "createdDate").and("$stockDetail.chassisNo")
				.as("chassisNo").and("amountAllocatted").as("received").and("$currencyDetail.symbol")
				.as("currencySymbol").andExpression("$amountAllocatted<$total").as("condition");

		MatchOperation matchStock = Aggregation.match(Criteria.where("condition").is(true));
		final SortOperation sort = Aggregation.sort(Direction.ASC, "createdDate");

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupstockDetail,
				Aggregation.unwind("$stockDetail", true), currencyDetail, Aggregation.unwind("$currencyDetail", true),
				projectionOperation, matchStock, sort);

		final AggregationResults<TTUnitAllocationDto> result = mongoTemplate.aggregate(aggregation, "t_sls_inv",
				TTUnitAllocationDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<IncomeByCustomerDto> getIncomeByCustomerList() {

		final LookupOperation lookupCustomer = LookupOperation.newLookup().from("t_cstmr").localField("customerId")
				.foreignField("code").as("customer");
		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stockDtls");
		final LookupOperation lookupPurchaseInvoice = LookupOperation.newLookup().from("t_prchs_invc")
				.localField("stockDtls.purchaseInvoiceCode").foreignField("code").as("purchaseInvoice");

		final Document purchaseCostTax = new Document("$multiply", Arrays.asList("$purchaseInvoice.purchaseCost",
				new Document("$divide", Arrays.asList("$purchaseInvoice.purchaseCostTax", 100))));
		final Document commisionTax = new Document("$multiply", Arrays.asList("$purchaseInvoice.commision",
				new Document("$divide", Arrays.asList("$purchaseInvoice.commisionTax", 100))));

		final GroupOperation group = Aggregation
				.group("$customerId").push(
						new BasicDBObject("stockNo", "$stockDtls.stockNo").append("chassisNo", "$stockDtls.chassisNo")
								.append("maker", "$stockDtls.maker").append("model", "$stockDtls.model")
								.append("salesDate", "$createdDate").append("status", "$status")
								.append("exchangeRate", "$exchangeRate")
								.append("currencySymbol", "$currencyDtls.symbol")
								.append("sellingPrice",
										new Document("$multiply", Arrays.asList("$exchangeRate",
												new Document("$add",
														Arrays.asList("$fob", "$insurance", "$shipping", "$freight")))))
								.append("purchasePrice", new Document("$add",
										Arrays.asList("$purchaseInvoice.recycle", "$purchaseInvoice.purchaseCost",
												"$purchaseInvoice.otherCharges", "$purchaseInvoice.roadTax",
												"$purchaseInvoice.commision", purchaseCostTax, commisionTax)))
								.append("margin", "$margin"))
				.as("stockDetails").first("customer.firstName").as("customerName").first("customerId").as("customerId")
				.first("createdDate").as("salesDate");

		final ProjectionOperation project = Aggregation.project()
				.andInclude("stockDetails", "customerName", "customerId")
				.and(AccumulatorOperators.Sum.sumOf("$stockDetails.purchasePrice")).as("totalPurchasePrice")
				.and(AccumulatorOperators.Sum.sumOf("$stockDetails.sellingPrice")).as("totalSellingPrice");
		final Aggregation aggregation = Aggregation.newAggregation(lookupCustomer,
				Aggregation.unwind("$customer", true), lookupStock, Aggregation.unwind("$stockDtls", true),
				lookupPurchaseInvoice, Aggregation.unwind("$purchaseInvoice", true), group, project);
		final AggregationResults<IncomeByCustomerDto> result = mongoTemplate.aggregate(aggregation, "t_sls_inv",
				IncomeByCustomerDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<BranchSalesOrderListDto> branchSalesOrderList() {
		final MatchOperation match = Aggregation
				.match(Criteria.where("customerFlag").is(Constants.CUSTOMER_FLAG_BRANCH));

		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stockDtls");
		final LookupOperation lookupCustomer = LookupOperation.newLookup().from("t_cstmr").localField("customerId")
				.foreignField("code").as("customer");
		final LookupOperation currencyDetail = LookupOperation.newLookup().from("m_currency")
				.localField("customer.currencyType").foreignField("currencySeq").as("currencyDetail");

		final GroupOperation group = Aggregation.group("$customerId")
				.push(new BasicDBObject("stockNo", "$stockDtls.stockNo").append("chassisNo", "$stockDtls.chassisNo")
						.append("maker", "$stockDtls.maker").append("model", "$stockDtls.model")
						.append("salesDate", "$createdDate").append("status", "$status")
						.append("exchangeRate", "$exchangeRate").append("currencySymbol", "$currencyDtls.symbol")
						.append("fob", "$fob").append("insurance", "$insurance").append("shipping", "$shipping")
						.append("freight", "$freight").append("currencyDetail", "$currencyDetail")
						.append("total", "$total"))
				.as("salesDetails").first("customer.firstName").as("fName").first("customer.lastName").as("lName")
				.first("customerId").as("customerId").first("customer.companyName").as("companyName")
				.first("customer.email").as("email").first("customer.mobileNo").as("mobileNo").first("customer.country")
				.as("country").first("customer.port").as("port").first("invoiceNo").as("invoiceNo");

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupStock,
				Aggregation.unwind("$stockDtls", true), lookupCustomer, Aggregation.unwind("$customer", true),
				currencyDetail, Aggregation.unwind("$currencyDetail", true), group);
		final AggregationResults<BranchSalesOrderListDto> result = mongoTemplate.aggregate(aggregation, "t_sls_inv",
				BranchSalesOrderListDto.class);
		return result.getMappedResults();
	}

	@Override
	public BranchSalesInvoiceDto getOneByBranchInvoiceNo(String invoiceNo) {
		final MatchOperation match = Aggregation.match(Criteria.where("invoiceNo").is(invoiceNo));
		final LookupOperation lookupCustomer = LookupOperation.newLookup().from("t_cstmr").localField("customerId")
				.foreignField("code").as("customer");
		final LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockNo")
				.foreignField("stockNo").as("stockDtls");
		final AggregationOperation addConsignee = context -> new Document("$addFields", new Document("consignee",
				new Document("$filter", new Document("input", "$customer.consigneeNotifyparties").append("as", "result")
						.append("cond", new Document("$eq", Arrays.asList("$$result._id", "$consigneeId"))))));

		final AggregationOperation addNotifyParty = context -> new Document("$addFields",
				new Document("notifyparty",
						new Document("$filter",
								new Document("input", "$customer.consigneeNotifyparties").append("as", "result").append(
										"cond",
										new Document("$eq", Arrays.asList("$$result._id", "$notifypartyId"))))));
		final GroupOperation group = Aggregation.group("$invoiceNo")
				.push(new BasicDBObject("stockNo", "$stockDtls.stockNo").append("chassisNo", "$stockDtls.chassisNo")
						.append("maker", "$stockDtls.maker").append("model", "$stockDtls.model")
						.append("transmission", "$stockDtls.transmission").append("fuel", "$stockDtls.fuel")
						.append("driven", "$stockDtls.driven").append("cc", "$stockDtls.cc")
						.append("type", "$stockDtls.purchaseInfo.type")
						.append("firstRegDate", "$stockDtls.firstRegDate").append("fob", "$fob")
						.append("insurance", "$insurance").append("freight", "$freight").append("shipping", "$shipping")
						.append("status", "$status"))
				.as("salesInvoiceDetails").first("customer.firstName").as("customerFN").first("customer.isLcCustomer")
				.as("isLcCustomer").first("customer.port").as("port").first("customer.country").as("country")
				.first("customer.companyName").as("cCompanyName").first("createdDate").as("createdDate")
				.first("customer.paymentType").as("paymentType").first("customer.address").as("cAddress")
				.first("customer.mobileNo").as("cMobileNo").first("customer.city").as("cCity").first("invoiceNo")
				.as("invoiceNo").first("fobTotal").as("fobTotal").first("freightTotal").as("freightTotal")
				.first("shippingTotal").as("shippingTotal").first("total").as("total").first("insuranceTotal")
				.as("insuranceTotal").first("customerId").as("customerId").first("createdDate").as("issueDate");
		final ProjectionOperation project = Aggregation.project()
				.andInclude("salesInvoiceDetails", "customerFN", "cCompanyName", "cCity", "invoiceNo", "createdDate",
						"paymentType", "isLcCustomer", "cAddress", "cMobileNo", "issueDate", "port", "country")
				.and(AccumulatorOperators.Sum.sumOf("salesInvoiceDetails.fob")).as("fobTotal")
				.and(AccumulatorOperators.Sum.sumOf("salesInvoiceDetails.insurance")).as("insuranceTotal")
				.and(AccumulatorOperators.Sum.sumOf("salesInvoiceDetails.shipping")).as("shippingTotal")
				.and(AccumulatorOperators.Sum.sumOf("salesInvoiceDetails.freight")).as("freightTotal")
				.and(AccumulatorOperators.Sum.sumOf("salesInvoiceDetails.total")).as("total");

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupCustomer,
				Aggregation.unwind("$customer"), lookupStock, addConsignee, Aggregation.unwind("consignee", true),
				addNotifyParty, Aggregation.unwind("$notifyparty", true), Aggregation.unwind("$stockDtls", true), group,
				project);
		final AggregationResults<BranchSalesInvoiceDto> result = mongoTemplate.aggregate(aggregation, "t_sls_inv",
				BranchSalesInvoiceDto.class);

		return result.getUniqueMappedResult();
	}

	@Override
	public Double getPreviousMonthReservedPrice(String customerId, Date date) {
		final List<Criteria> criterias = new ArrayList<>();
		if (!AppUtil.isObjectEmpty(date) && !AppUtil.isObjectEmpty(customerId)) {
			criterias.add(Criteria.where("reservedInfo.date").lt(AppUtil.startDateOfMonth(date))
					.lte(AppUtil.endDateOfMonth(date)));
			criterias.add(Criteria.where("reservedInfo.customerId").is(customerId));

		}
		criterias.add(Criteria.where("reserve").is(Constants.RESERVED));
		final Criteria matchCriteria = new Criteria();
		matchCriteria.andOperator(criterias.toArray(new Criteria[0]));

		final MatchOperation match = Aggregation.match(matchCriteria);
		GroupOperation groupOperation = Aggregation.group("customerId").sum("reservedInfo.price").as("amount");
		Aggregation aggregation = Aggregation.newAggregation(match, groupOperation);
		AggregationResults<Document> result = this.mongoTemplate.aggregate(aggregation, "t_stck", Document.class);
		return Double.valueOf(!AppUtil.isObjectEmpty(result.getUniqueMappedResult())
				? result.getUniqueMappedResult().get("amount").toString()
				: "0.0");
	}

	@Override
	public List<CustomerTransactionDto> findAllCustomerTransactionsInExcel(String customerId, Date minDate,
			Date maxDate) {
		final List<Criteria> criterias = new ArrayList<>();
		if (!AppUtil.isObjectEmpty(customerId)) {
			criterias.add(Criteria.where("reservedInfo.customerId").is(customerId));
		}
		if (!AppUtil.isObjectEmpty(minDate) && !AppUtil.isObjectEmpty(maxDate)) {
			criterias.add(Criteria.where("reservedInfo.date").gte(minDate).lte(maxDate));
		}
		if (criterias.isEmpty()) {
			return new ArrayList<>();
		}
		criterias.add(Criteria.where("reserve").is(Constants.RESERVED));
		final Criteria matchCriteria = new Criteria();
		matchCriteria.andOperator(criterias.toArray(new Criteria[0]));

		final MatchOperation match = Aggregation.match(matchCriteria);

		final LookupOperation lookupCustomer = LookupOperation.newLookup().from("t_cstmr")
				.localField("reservedInfo.customerId").foreignField("code").as("customer");

		final LookupOperation lookupCurrency = LookupOperation.newLookup().from("m_currency")
				.localField("customer.currencyType").foreignField("currencySeq").as("currencyDtls");

		final ProjectionOperation project = Aggregation.project().andExpression("month(reservedInfo.date)").as("month")
				.and("reservedInfo.date").as("reservedDate").and("reservedInfo.customerId").as("customerId")
				.and("currencyDtls.symbol").as("currencySymbol");

		final GroupOperation group = Aggregation.group("month").first("reservedDate").as("reservedDate")
				.first("customerId").as("customerId").first("currencySymbol").as("currencySymbol");

		final SortOperation sort = Aggregation.sort(Direction.ASC, "_id");

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupCustomer,
				Aggregation.unwind("$customer", true), lookupCurrency, Aggregation.unwind("$currencyDtls", true),
				project, group, sort);

		final AggregationResults<CustomerTransactionDto> result = mongoTemplate.aggregate(aggregation, "t_stck",
				CustomerTransactionDto.class);
		return result.getMappedResults();
	}

}
