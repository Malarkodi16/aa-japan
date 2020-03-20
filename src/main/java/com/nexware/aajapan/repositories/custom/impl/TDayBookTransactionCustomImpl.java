package com.nexware.aajapan.repositories.custom.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.aspectj.weaver.patterns.IScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import com.mongodb.BasicDBObject;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.DayBookListDto;
import com.nexware.aajapan.dto.OwnedTransactionTTDto;
import com.nexware.aajapan.dto.TTApproveDto;
import com.nexware.aajapan.repositories.custom.TDayBookTransactionCustom;
import com.nexware.aajapan.utils.AppUtil;

public class TDayBookTransactionCustomImpl implements TDayBookTransactionCustom {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<TTApproveDto> fetchDayBookTransactionToApprove() {
		MatchOperation match = Aggregation
				.match(Criteria.where("paymentApprove").is(Constants.DAYBOOK_TRANSACTION_NOT_APPROVED));

		LookupOperation lookupDayBook = LookupOperation.newLookup().from("t_day_book").localField("daybookId")
				.foreignField("daybookId").as("dayBookDetail");
		LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockId")
				.foreignField("stockNo").as("stockDetails");

		LookupOperation lookupCustomer = LookupOperation.newLookup().from("t_cstmr").localField("customerId")
				.foreignField("code").as("customerDetails");

		LookupOperation lookupUser = LookupOperation.newLookup().from("m_usr").localField("salesPersonId")
				.foreignField("code").as("userDetails");

		LookupOperation lookupBank = LookupOperation.newLookup().from("m_bank").localField("$dayBookDetail.bank")
				.foreignField("bankSeq").as("bankDetails");

		LookupOperation lookupCurrency = LookupOperation.newLookup().from("m_currency")
				.localField("$dayBookDetail.currency").foreignField("currencySeq").as("currency_details");

		LookupOperation lookupCustomerCurrency = LookupOperation.newLookup().from("m_currency")
				.localField("$customerDetails.currencyType").foreignField("currencySeq")
				.as("customer_currency_details");

		ProjectionOperation project = Aggregation.project()
				.andInclude("_id", "daybookId", "salesInvoiceId", "amount", "allocatedAmount", "allocationType",
						"createdDate", "salesPersonId", "customerId")
				.and("$customerDetails.firstName").as("customerFn").and("$stockDetails.stockNo").as("stockNo")
				.and("$stockDetails.chassisNo").as("chassisNo").and("$dayBookDetail.coaNo").as("coaCode")
				.and("$bankDetails.bankSeq").as("bankSeq").and("$bankDetails.bankName").as("bank")
				.and("$dayBookDetail.remitter").as("remitter")// .and("$dayBookDetail.customer").as("customerId")
				.and("$userDetails.fullname").as("salesPerson").and("$currency_details.currencySeq").as("currency")
				.and("$currency_details.symbol").as("currencySymbol").and("$customer_currency_details.symbol")
				.as("customerCurrencySymbol").and("$customerDetails.currencyType").as("customerCurrency");

		Aggregation aggregation = Aggregation.newAggregation(match, lookupDayBook,
				Aggregation.unwind("$dayBookDetail", true), lookupStock, Aggregation.unwind("$stockDetails", true),
				lookupCustomer, Aggregation.unwind("$customerDetails", true), lookupUser,
				Aggregation.unwind("$userDetails", true), lookupBank, Aggregation.unwind("$bankDetails", true),
				lookupCurrency, Aggregation.unwind("$currency_details", true), lookupCustomerCurrency,
				Aggregation.unwind("$customer_currency_details", true), project);

		AggregationResults<TTApproveDto> result = this.mongoTemplate.aggregate(aggregation, "t_dybk_trnsctn",
				TTApproveDto.class);
		return result.getMappedResults();
	}

	@Override
	public TTApproveDto fetchOneDayBookTransactionToApprove(String id) {
		MatchOperation match = Aggregation.match(Criteria.where("paymentApprove")
				.is(Constants.DAYBOOK_TRANSACTION_NOT_APPROVED).andOperator(Criteria.where("_id").is(id)));

		LookupOperation lookupSales = LookupOperation.newLookup().from("t_sls_inv").localField("salesInvoiceId")
				.foreignField("_id").as("salesDetail");

		LookupOperation lookupDayBook = LookupOperation.newLookup().from("t_day_book").localField("daybookId")
				.foreignField("_id").as("dayBookDetail");

		LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockId")
				.foreignField("stockNo").as("stockDetails");

		LookupOperation lookupCustomer = LookupOperation.newLookup().from("t_cstmr")
				.localField("$salesDetail.customerId").foreignField("code").as("customerDetails");

		LookupOperation lookupUser = LookupOperation.newLookup().from("m_usr").localField("$salesDetail.createdBy")
				.foreignField("username").as("userDetails");

		ProjectionOperation project = Aggregation.project()
				.andInclude("_id", "daybookId", "salesInvoiceId", "amount", "allocationType", "createdDate")
				.and("$customerDetails.firstName").as("customerFn").and("$customerDetails.lastName").as("customerLn")
				.and("$stockDetails.stockNo").as("stockNo").and("$stockDetails.chassisNo").as("chassisNo")
				.and("$dayBookDetail.coaNo").as("coaCode").and("$dayBookDetail.bank").as("bank")
				.and("$dayBookDetail.remitter").as("remitter").and("$salesDetail.customerId").as("customerId")
				.and("$userDetails.fullname").as("salesPerson").and("$dayBookDetail.currency").as("currency")
				.and("$dayBookDetail.clearingAccount").as("clearingAccount").and("$dayBookDetail.exchangeRate")
				.as("exchangeRate");

		Aggregation aggregation = Aggregation.newAggregation(match, lookupSales,
				Aggregation.unwind("$salesDetail", true), lookupDayBook, Aggregation.unwind("$dayBookDetail", true),
				lookupStock, Aggregation.unwind("$stockDetails", true), lookupCustomer,
				Aggregation.unwind("$customerDetails", true), lookupUser, Aggregation.unwind("$userDetails", true),
				project);

		AggregationResults<TTApproveDto> result = this.mongoTemplate.aggregate(aggregation, "t_dybk_trnsctn",
				TTApproveDto.class);
		return result.getUniqueMappedResult();
	}

	@Override
	public List<DayBookListDto> listDayBookEntry() {
		MatchOperation match = Aggregation.match(Criteria.where("status").is(Constants.DAYBOOK_ENTRY_CREATED));

		LookupOperation lookupBank = LookupOperation.newLookup().from("m_bank").localField("bank")
				.foreignField("bankSeq").as("bankDetails");
		LookupOperation lookupCurrency = LookupOperation.newLookup().from("m_currency").localField("currency")
				.foreignField("currencySeq").as("currency_details");
		LookupOperation lookupCustomer = LookupOperation.newLookup().from("t_cstmr").localField("customer")
				.foreignField("code").as("customer_details");
		LookupOperation lookupRemitType = LookupOperation.newLookup().from("m_remit_type").localField("remitType")
				.foreignField("remitSeq").as("remit_type");
		ProjectionOperation project = Aggregation.project()
				.andInclude("remitDate", "daybookId", "remitter", "bank", "coaNo", "transactionType", "amount",
						"amountWithOutBankCharge", "currency", "bankCharges", "billOfExchange", "lcNo", "remarks", "id",
						"status", "clearingAccount", "exchangeRate", "isCustomerBankCharge", "attachmentFilename",
						"slipUpload", "attachementViewed", "attachmentDiskFilename")
				.and("$bankDetails.bankName").as("bankName").and("$currency_details.symbol").as("currencySymbol")
				.and("customer_details.firstName").as("customer").and("customer_details.code").as("customerId")
				.and("$remit_type.remitType").as("remitType").and("$remit_type.remitSeq").as("remitTypeId")
				.and("salesPerson").as("staff");

		Aggregation aggregation = Aggregation.newAggregation(match, lookupBank,
				Aggregation.unwind("$bankDetails", true), lookupCurrency, Aggregation.unwind("$currency_details", true),
				lookupRemitType, Aggregation.unwind("$remit_type", true), lookupCustomer,
				Aggregation.unwind("$customer_details", true), project);

		AggregationResults<DayBookListDto> result = this.mongoTemplate.aggregate(aggregation, "t_day_book",
				DayBookListDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<DayBookListDto> listDayBookEntryApproved(Date fromDate, Date toDate) {

		final List<Criteria> andCriterias = new ArrayList<>();
		andCriterias.add(Criteria.where("status").is(Constants.DAYBOOK_ENTRY_APPROVED));
		boolean isValid = false;
		if (!AppUtil.isObjectEmpty(fromDate)) {
			isValid = true;
			andCriterias.add(new Criteria().andOperator(Criteria.where("remitDate").gte(AppUtil.atStartOfDay(fromDate)),
					Criteria.where("remitDate").lte(AppUtil.atEndOfDay(toDate))));
		}

		if (!isValid) {
			return new ArrayList<>();
		}

		final Criteria matchCriteria = new Criteria();
		matchCriteria.andOperator(andCriterias.toArray(new Criteria[0]));
		final MatchOperation match = Aggregation.match(matchCriteria);

		LookupOperation lookupBank = LookupOperation.newLookup().from("m_bank").localField("bank")
				.foreignField("bankSeq").as("bankDetails");
		LookupOperation lookupCurrency = LookupOperation.newLookup().from("m_currency").localField("currency")
				.foreignField("currencySeq").as("currency_details");
		LookupOperation lookupCustomer = LookupOperation.newLookup().from("t_cstmr").localField("customer")
				.foreignField("code").as("customer_details");
		LookupOperation lookupRemitType = LookupOperation.newLookup().from("m_remit_type").localField("remitType")
				.foreignField("remitSeq").as("remit_type");
		ProjectionOperation project = Aggregation.project()
				.andInclude("remitDate", "daybookId", "remitter", "bank", "coaNo", "transactionType", "amount",
						"amountWithOutBankCharge", "currency", "bankCharges", "billOfExchange", "lcNo", "staff",
						"remarks", "id", "status", "clearingAccount", "exchangeRate")
				.and("$bankDetails.bankName").as("bankName").and("$currency_details.symbol").as("currencySymbol")
				.and("customer_details.firstName").as("customer").and("$remit_type.remitType").as("remitType")
				.and("$remit_type.remitSeq").as("remitTypeId");
		Aggregation aggregation = Aggregation.newAggregation(match, lookupBank,
				Aggregation.unwind("$bankDetails", true), lookupCurrency, Aggregation.unwind("$currency_details", true),
				lookupRemitType, Aggregation.unwind("$remit_type", true), lookupCustomer,
				Aggregation.unwind("$customer_details", true), project);

		AggregationResults<DayBookListDto> result = this.mongoTemplate.aggregate(aggregation, "t_day_book",
				DayBookListDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<OwnedTransactionTTDto> getOwnedTransactionList(String salesPersonId) {
		MatchOperation match = Aggregation.match(Criteria.where("salesPersonId").is(salesPersonId));
		LookupOperation lookupDayBook = LookupOperation.newLookup().from("t_day_book").localField("daybookId")
				.foreignField("daybookId").as("dayBook");
		LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockId")
				.foreignField("stockNo").as("stock");
		LookupOperation lookupSalesInvoice = LookupOperation.newLookup().from("t_sls_inv").localField("salesInvoiceId")
				.foreignField("invoiceNo").as("salesInvoice");
		LookupOperation lookupCustomer = LookupOperation.newLookup().from("t_cstmr").localField("customerId")
				.foreignField("code").as("customer");
		LookupOperation lookupBank = LookupOperation.newLookup().from("m_bank").localField("dayBook.bank")
				.foreignField("bankSeq").as("bankDetails");
		LookupOperation lookupCurrency = LookupOperation.newLookup().from("m_currency").localField("dayBook.currency")
				.foreignField("currencySeq").as("currency_details");

		GroupOperation groupOperation = Aggregation.group("$daybookId")
				.push(new BasicDBObject("dayBookTransId", "$_id").append("stockNo", "$stock.stockNo")
						.append("chassisNo", "$stock.chassisNo").append("invoiceNo", "$salesInvoice.invoiceNo")
						.append("code", "$customer.code").append("firstName", "$customer.firstName")
						.append("lastName", "$customer.lastName").append("nickName", "$customer.nickName")
						.append("amount", "$amount").append("currency", "$currency_details.symbol"))
				.as("items").first("$dayBook.amount").as("amount").first("$salesInvoice._id").as("salesInvoiceId")
				.first("$stock._id").as("stockId").first("$dayBook.remitDate").as("remitDate")
				.first("$dayBook.remitter").as("remitter").first("$bankDetails.bankName").as("bank")
				.first("$currency_details.symbol").as("currency").first("$dayBook.remarks").as("remarks")
				.first("$dayBook.amount").as("amount");

		Aggregation aggregation = Aggregation.newAggregation(match, lookupDayBook, Aggregation.unwind("$dayBook", true),
				lookupStock, Aggregation.unwind("$stock", true), lookupSalesInvoice,
				Aggregation.unwind("$salesInvoice", true), lookupCustomer, Aggregation.unwind("$customer", true),
				lookupBank, Aggregation.unwind("$bankDetails", true), lookupCurrency,
				Aggregation.unwind("$currency_details", true), groupOperation);
		AggregationResults<OwnedTransactionTTDto> result = this.mongoTemplate.aggregate(aggregation, "t_dybk_trnsctn",
				OwnedTransactionTTDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<TTApproveDto> fetchDayBookTransactionToApproveOwned(Date fromDate, Date toDate) {

		final List<Criteria> andCriterias = new ArrayList<>();
		andCriterias.add(Criteria.where("paymentApprove").is(Constants.DAYBOOK_TRANSACTION_APPROVED));
		boolean isValid = false;
		if (!AppUtil.isObjectEmpty(fromDate)) {
			isValid = true;
			andCriterias
					.add(new Criteria().andOperator(Criteria.where("createdDate").gte(AppUtil.atStartOfDay(fromDate)),
							Criteria.where("createdDate").lte(AppUtil.atEndOfDay(toDate))));
		}

		if (!isValid) {
			return new ArrayList<>();
		}

		final Criteria matchCriteria = new Criteria();
		matchCriteria.andOperator(andCriterias.toArray(new Criteria[0]));
		final MatchOperation match = Aggregation.match(matchCriteria);

		LookupOperation lookupDayBook = LookupOperation.newLookup().from("t_day_book").localField("daybookId")
				.foreignField("daybookId").as("dayBookDetail");
		LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockId")
				.foreignField("stockNo").as("stockDetails");

		LookupOperation lookupCustomer = LookupOperation.newLookup().from("t_cstmr").localField("customerId")
				.foreignField("code").as("customerDetails");

		LookupOperation lookupUser = LookupOperation.newLookup().from("m_usr").localField("salesPersonId")
				.foreignField("code").as("userDetails");

		LookupOperation lookupBank = LookupOperation.newLookup().from("m_bank").localField("$dayBookDetail.bank")
				.foreignField("bankSeq").as("bankDetails");

		LookupOperation lookupCurrency = LookupOperation.newLookup().from("m_currency")
				.localField("$dayBookDetail.currency").foreignField("currencySeq").as("currency_details");

		ProjectionOperation project = Aggregation.project()
				.andInclude("_id", "daybookId", "salesInvoiceId", "amount", "allocationType", "createdDate",
						"salesPersonId", "customerId")
				.and("$customerDetails.firstName").as("customerFn").and("$stockDetails.stockNo").as("stockNo")
				.and("$stockDetails.chassisNo").as("chassisNo").and("$dayBookDetail.coaNo").as("coaCode")
				.and("$bankDetails.bankSeq").as("bankSeq").and("$bankDetails.bankName").as("bank")
				.and("$dayBookDetail.remitter").as("remitter").and("$userDetails.fullname").as("salesPerson")
				.and("$currency_details.currencySeq").as("currency").and("$currency_details.symbol")
				.as("currencySymbol");

		Aggregation aggregation = Aggregation.newAggregation(match, lookupDayBook,
				Aggregation.unwind("$dayBookDetail", true), lookupStock, Aggregation.unwind("$stockDetails", true),
				lookupCustomer, Aggregation.unwind("$customerDetails", true), lookupUser,
				Aggregation.unwind("$userDetails", true), lookupBank, Aggregation.unwind("$bankDetails", true),
				lookupCurrency, Aggregation.unwind("$currency_details", true), project);

		AggregationResults<TTApproveDto> result = this.mongoTemplate.aggregate(aggregation, "t_dybk_trnsctn",
				TTApproveDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<TTApproveDto> findAllByAllocationType(Integer type) {
		final MatchOperation match = Aggregation
				.match(new Criteria().andOperator(Criteria.where("allocationType").is(type),
						Criteria.where("paymentApprove").is(Constants.PAYMENT_APPROVED),
						Criteria.where("amountRefund").is(Constants.ADVANCE_AMOUNT_NOT_REFUNDED)));

		LookupOperation lookupDayBook = LookupOperation.newLookup().from("t_day_book").localField("daybookId")
				.foreignField("daybookId").as("dayBookDetail");
		LookupOperation lookupStock = LookupOperation.newLookup().from("t_stck").localField("stockId")
				.foreignField("stockNo").as("stockDetails");

		LookupOperation lookupCustomer = LookupOperation.newLookup().from("t_cstmr").localField("customerId")
				.foreignField("code").as("customerDetails");

		LookupOperation lookupUser = LookupOperation.newLookup().from("m_usr").localField("salesPersonId")
				.foreignField("code").as("userDetails");

		LookupOperation lookupBank = LookupOperation.newLookup().from("m_bank").localField("$dayBookDetail.bank")
				.foreignField("bankSeq").as("bankDetails");

		LookupOperation lookupCurrency = LookupOperation.newLookup().from("m_currency")
				.localField("$dayBookDetail.currency").foreignField("currencySeq").as("currency_details");

		LookupOperation lookupCustomerCurrency = LookupOperation.newLookup().from("m_currency")
				.localField("customerCurrency").foreignField("currencySeq").as("customer_currency_details");

		ProjectionOperation project = Aggregation.project()
				.andInclude("_id", "daybookId", "salesInvoiceId", "amount", "allocatedAmount", "allocationType",
						"createdDate", "salesPersonId", "customerId", "customerCurrency", "advanceOwned")
				.and("$customerDetails.firstName").as("customerFn").and("$stockDetails.stockNo").as("stockNo")
				.and("$stockDetails.chassisNo").as("chassisNo").and("$dayBookDetail.coaNo").as("coaCode")
				.and("$bankDetails.bankSeq").as("bankSeq").and("$bankDetails.bankName").as("bank")
				.and("$dayBookDetail.remitter").as("remitter").and("$userDetails.fullname").as("salesPerson")
				.and("$currency_details.currencySeq").as("currency").and("$currency_details.symbol")
				.as("currencySymbol").and("$customer_currency_details.symbol").as("customerCurrencySymbol");

		Aggregation aggregation = Aggregation.newAggregation(match, lookupDayBook,
				Aggregation.unwind("$dayBookDetail", true), lookupStock, Aggregation.unwind("$stockDetails", true),
				lookupCustomer, Aggregation.unwind("$customerDetails", true), lookupUser,
				Aggregation.unwind("$userDetails", true), lookupBank, Aggregation.unwind("$bankDetails", true),
				lookupCurrency, Aggregation.unwind("$currency_details", true), lookupCustomerCurrency,
				Aggregation.unwind("$customer_currency_details", true), project);

		AggregationResults<TTApproveDto> result = this.mongoTemplate.aggregate(aggregation, "t_dybk_trnsctn",
				TTApproveDto.class);
		return result.getMappedResults();
	}

	@Override
	public List<DayBookListDto> findCarTaxClaimedList() {
		MatchOperation match = Aggregation
				.match(new Criteria().andOperator(Criteria.where("status").is(Constants.DAYBOOK_ENTRY_APPROVED),
						Criteria.where("remitType").is(Constants.DAYBOOK_TRANSACTION_TYPE_CAR_TAX_CLAIMED),
						Criteria.where("cartaxClaimedStatus").is(Constants.TPURCHASEINVOICE_CARTAX_NOT_CLAIMED)));

		LookupOperation lookupBank = LookupOperation.newLookup().from("m_bank").localField("bank")
				.foreignField("bankSeq").as("bankDetails");
		LookupOperation lookupCurrency = LookupOperation.newLookup().from("m_currency").localField("currency")
				.foreignField("currencySeq").as("currency_details");
		LookupOperation lookupCustomer = LookupOperation.newLookup().from("t_cstmr").localField("customer")
				.foreignField("code").as("customer_details");
		LookupOperation lookupRemitType = LookupOperation.newLookup().from("m_remit_type").localField("remitType")
				.foreignField("remitSeq").as("remit_type");
		ProjectionOperation project = Aggregation.project()
				.andInclude("remitDate", "daybookId", "remitter", "bank", "coaNo", "transactionType", "amount",
						"amountWithOutBankCharge", "currency", "bankCharges", "billOfExchange", "lcNo", "remarks", "id",
						"status", "clearingAccount", "exchangeRate", "isCustomerBankCharge")
				.and("$bankDetails.bankName").as("bankName").and("$currency_details.symbol").as("currencySymbol")
				.and("customer_details.firstName").as("customer").and("customer_details.code").as("customerId")
				.and("$remit_type.remitType").as("remitType").and("$remit_type.remitSeq").as("remitTypeId")
				.and("salesPerson").as("staff").and("daybookId")
				.concat("/", "$remit_type.remitType", "/", "$bankDetails.bankName").as("carTaxApprove");

		Aggregation aggregation = Aggregation.newAggregation(match, lookupBank,
				Aggregation.unwind("$bankDetails", true), lookupCurrency, Aggregation.unwind("$currency_details", true),
				lookupRemitType, Aggregation.unwind("$remit_type", true), lookupCustomer,
				Aggregation.unwind("$customer_details", true), project);

		AggregationResults<DayBookListDto> result = this.mongoTemplate.aggregate(aggregation, "t_day_book",
				DayBookListDto.class);
		return result.getMappedResults();
	}
}
