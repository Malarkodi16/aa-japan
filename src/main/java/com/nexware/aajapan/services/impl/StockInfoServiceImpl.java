package com.nexware.aajapan.services.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators.Cond;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.mongodb.BasicDBObject;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.StockBaseInfoDto;
import com.nexware.aajapan.dto.StockInfoDocumentConversionDto;
import com.nexware.aajapan.dto.StockInfoDocumentDto;
import com.nexware.aajapan.dto.StockInfoDto;
import com.nexware.aajapan.dto.StockInfoReservedInfoDto;
import com.nexware.aajapan.dto.StockInfoSalesDto;
import com.nexware.aajapan.dto.TStockInspectionInfoDto;
import com.nexware.aajapan.dto.TStockPurchaseInfoDto;
import com.nexware.aajapan.dto.TStockShippingInfoDto;
import com.nexware.aajapan.dto.TStockTransportInfoDto;
import com.nexware.aajapan.models.MInspectionCompany;
import com.nexware.aajapan.models.MLocation;
import com.nexware.aajapan.models.MShippingCompany;
import com.nexware.aajapan.models.MUser;
import com.nexware.aajapan.models.TCustomer;
import com.nexware.aajapan.models.TDocumentConversion;
import com.nexware.aajapan.models.TDocumentReceived;
import com.nexware.aajapan.models.TSalesInvoice;
import com.nexware.aajapan.models.TStock;
import com.nexware.aajapan.repositories.LocationRepository;
import com.nexware.aajapan.repositories.MInspectionCompanyRepository;
import com.nexware.aajapan.repositories.MShippingCompanyRepository;
import com.nexware.aajapan.repositories.StockRepository;
import com.nexware.aajapan.repositories.TCustomerRepository;
import com.nexware.aajapan.repositories.TDocumentConversionRepository;
import com.nexware.aajapan.repositories.TDocumentReceivedRepository;
import com.nexware.aajapan.repositories.TSalesInvoiceRepository;
import com.nexware.aajapan.repositories.UserRepository;
import com.nexware.aajapan.services.S3Factory;
import com.nexware.aajapan.services.StockInfoService;
import com.nexware.aajapan.utils.AppUtil;

import constants.FbFeedConstants;

@Service
public class StockInfoServiceImpl implements StockInfoService {

	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private StockRepository stockRepository;
	@Autowired
	private TDocumentReceivedRepository documentReceivedRepository;
	@Autowired
	private TDocumentConversionRepository documentConversionRepository;
	@Autowired
	private MShippingCompanyRepository shippingCompanyRepository;
	@Autowired
	private MInspectionCompanyRepository inspectionCompanyRepository;
	@Autowired
	private TSalesInvoiceRepository salesInvoiceRepository;
	@Autowired
	private TCustomerRepository customerRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private LocationRepository locationRepository;
	@Autowired
	private S3Factory s3FactoryService;

	@Override
	public TStockPurchaseInfoDto findPurchaseInfoForStockNo(String stockNo) {

		final MatchOperation match = Aggregation.match(Criteria.where("stockNo").is(stockNo));
		final LookupOperation lookupPurchase = LookupOperation.newLookup().from("t_prchs_invc").localField("stockNo")
				.foreignField("stockNo").as("purchase");
		final LookupOperation lookupSupplier = LookupOperation.newLookup().from("m_spplr")
				.localField("purchase.supplierId").foreignField("supplierCode").as("supplier");

		AggregationOperation addAuctionHouse = context -> new Document("$addFields",
				new Document("auctionHouse", new Document("$filter",
						new Document("input", "$supplier.supplierLocations").append("as", "result").append("cond",
								new Document("$eq", Arrays.asList("$$result._id", "$purchase.auctionHouseId"))))));

		Cond rikusoPayment = ConditionalOperators
				.when(Criteria.where("$purchase.type").is(Constants.PURCHASE_INVOICE_ITEM_TYPE_RIKUSO_PAYMENT))
				.thenValueOf("$purchase.otherCharges").otherwise("0.0");

		Cond rikusoPaymentTax = ConditionalOperators
				.when(Criteria.where("$purchase.type").is(Constants.PURCHASE_INVOICE_ITEM_TYPE_RIKUSO_PAYMENT))
				.thenValueOf("$purchase.othersCostTaxAmount").otherwise("0.0");

		Cond cancellationCharge = ConditionalOperators
				.when(Criteria.where("$purchase.type").is(Constants.PURCHASE_INVOICE_ITEM_TYPE_CANCELLATION_CHARGES))
				.thenValueOf("$purchase.otherCharges").otherwise("0.0");

		Cond cancellationChargeTax = ConditionalOperators
				.when(Criteria.where("$purchase.type").is(Constants.PURCHASE_INVOICE_ITEM_TYPE_CANCELLATION_CHARGES))
				.thenValueOf("$purchase.othersCostTaxAmount").otherwise("0.0");

		Cond unsoldAuctionCharge = ConditionalOperators
				.when(Criteria.where("$purchase.type").is(Constants.PURCHASE_INVOICE_ITEM_TYPE_UNSOLD_AUCTION_CHARGE))
				.thenValueOf("$purchase.otherCharges").otherwise("0.0");

		Cond unsoldAuctionChargeTax = ConditionalOperators
				.when(Criteria.where("$purchase.type").is(Constants.PURCHASE_INVOICE_ITEM_TYPE_UNSOLD_AUCTION_CHARGE))
				.thenValueOf("$purchase.othersCostTaxAmount").otherwise("0.0");

		Cond cancellationPenalityCharge = ConditionalOperators
				.when(Criteria.where("$purchase.type")
						.is(Constants.PURCHASE_INVOICE_ITEM_TYPE_CANCELLATION_PENALTY_CHARGES))
				.thenValueOf("$purchase.otherCharges").otherwise("0.0");

		Cond cancellationPenalityChargeTax = ConditionalOperators
				.when(Criteria.where("$purchase.type")
						.is(Constants.PURCHASE_INVOICE_ITEM_TYPE_CANCELLATION_PENALTY_CHARGES))
				.thenValueOf("$purchase.othersCostTaxAmount").otherwise("0.0");

		Cond takeOutCharge = ConditionalOperators
				.when(Criteria.where("$purchase.type").is(Constants.PURCHASE_INVOICE_ITEM_TYPE_TAKE_OUT_STOCK))
				.thenValueOf("$purchase.otherCharges").otherwise("0.0");

		Cond takeOutChargeTax = ConditionalOperators
				.when(Criteria.where("$purchase.type").is(Constants.PURCHASE_INVOICE_ITEM_TYPE_TAKE_OUT_STOCK))
				.thenValueOf("$purchase.othersCostTaxAmount").otherwise("0.0");

		Cond recyclepaid = ConditionalOperators
				.when(Criteria.where("$purchase.type").is(Constants.PURCHASE_INVOICE_ITEM_TYPE_RECYCLE_PAID))
				.thenValueOf("$purchase.otherCharges").otherwise("0.0");

		Cond recyclepaidTax = ConditionalOperators
				.when(Criteria.where("$purchase.type").is(Constants.PURCHASE_INVOICE_ITEM_TYPE_RECYCLE_PAID))
				.thenValueOf("$purchase.othersCostTaxAmount").otherwise("0.0");

		ProjectionOperation project = Aggregation.project().andInclude("stockNo", "chassisNo")
				// purchase details
				.and("supplier.company").as("auctionCompany").and("auctionHouse.auctionHouse").as("auctionHouse")
				.and("purchaseInfo.date").as("purchaseDate").and("purchase.status").as("purchaseStatus")
				.and("purchase.paymentApprove").as("paymentStatus").and("purchase.invoiceNo").as("invoiceNo")
				.and("purchase.type").as("invoicetype").and("purchase.auctionRefNo").as("auctionRefNo")
				.and("purchase.invoiceDate").as("invoiceDate").and("purchase.dueDate").as("dueDate")
				.and("purchase.invoiceAttachmentFilename").as("invoiceAttachmentFilename")
				.and("purchase.invoiceAttachmentDiskFilename").as("invoiceAttachmentDiskFilename")
				// TODO : attachmentDirectory need to be added
				.and("purchase.invoiceUpload").as("invoiceUpload").and("purchase.attachementViewed")
				.as("attachementViewed").and("purchase.purchaseCost").as("purchaseCost")
				.and("purchase.purchaseCostTaxAmount").as("purchaseCostTax").and("purchase.commision").as("commision")
				.and("purchase.commisionTaxAmount").as("commisionTax").and("purchase.recycle").as("recycle")
				.and("purchase.roadTax").as("roadTax").and("purchase.otherCharges").as("others")
				.and("purchase.othersCostTaxAmount").as("othersTax").and(rikusoPayment).as("rikusoPayment")
				.and(rikusoPaymentTax).as("rikusoPaymentTax").and(cancellationCharge).as("cancellationCharge")
				.and(cancellationChargeTax).as("cancellationChargeTax").and(unsoldAuctionCharge)
				.as("unsoldAuctionCharge").and(unsoldAuctionChargeTax).as("unsoldAuctionChargeTax")
				.and(cancellationPenalityCharge).as("cancellationPenalityCharge").and(cancellationPenalityChargeTax)
				.as("cancellationPenalityChargeTax").and(takeOutCharge).as("takeOutCharge").and(takeOutChargeTax)
				.as("takeOutChargeTax").and(recyclepaid).as("recyclepaid").and(recyclepaidTax).as("recyclepaidTax");

		GroupOperation group = Aggregation.group("$stockNo").first("stockNo").as("stockNo").first("chassisNo")
				.as("chassisNo").first("auctionCompany").as("auctionCompany").first("auctionHouse").as("auctionHouse")
				.first("purchaseDate").as("purchaseDate").first("purchaseStatus").as("purchaseStatus")
				.first("paymentStatus").as("paymentStatus").first("invoiceNo").as("invoiceNo").first("invoicetype")
				.as("invoicetype").first("auctionRefNo").as("auctionRefNo").first("invoiceDate").as("invoiceDate")
				.first("dueDate").as("dueDate").first("invoiceAttachmentFilename").as("invoiceAttachmentFilename")
				.first("invoiceAttachmentDiskFilename").as("invoiceAttachmentDiskFilename").first("invoiceUpload")
				.as("invoiceUpload").first("attachementViewed").as("attachementViewed").first("purchaseCost")
				.as("purchaseCost").first("purchaseCostTax").as("purchaseCostTax").first("commision").as("commision")
				.first("commisionTax").as("commisionTax").first("recycle").as("recycle").first("roadTax").as("roadTax")
				.first("others").as("others").first("othersTax").as("othersTax").first("rikusoPayment")
				.as("rikusoPayment").first("rikusoPaymentTax").as("rikusoPaymentTax").first("cancellationCharge")
				.as("cancellationCharge").first("cancellationChargeTax").as("cancellationChargeTax")
				.first("unsoldAuctionCharge").as("unsoldAuctionCharge").first("unsoldAuctionChargeTax")
				.as("unsoldAuctionChargeTax").first("cancellationPenalityCharge").as("cancellationPenalityCharge")
				.first("cancellationPenalityChargeTax").as("cancellationPenalityChargeTax").first("takeOutCharge")
				.as("takeOutCharge").first("takeOutChargeTax").as("takeOutChargeTax").first("recyclepaid")
				.as("recyclepaid").first("recyclepaidTax").as("recyclepaidTax");

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupPurchase,
				Aggregation.unwind("$purchase", true), lookupSupplier, Aggregation.unwind("$supplier", true),
				addAuctionHouse, Aggregation.unwind("$auctionHouse", true), project, group);

		final AggregationResults<TStockPurchaseInfoDto> result = mongoTemplate.aggregate(aggregation, "t_stck",
				TStockPurchaseInfoDto.class);
		return result.getUniqueMappedResult();

	}

	@Override
	public TStockTransportInfoDto findTransportInfoForStockNo(String stockNo) {

		final MatchOperation match = Aggregation.match(Criteria.where("stockNo").is(stockNo));

		final LookupOperation lookupTransportOrder = LookupOperation.newLookup().from("trnsprt_ordr_items")
				.localField("stockNo").foreignField("stockNo").as("transportOrder");
		final LookupOperation lookupTransportForwarder = LookupOperation.newLookup().from("m_frwrdr")
				.localField("transportOrder.forwarder").foreignField("code").as("forwarder");
		final LookupOperation lookupTransportInvoice = LookupOperation.newLookup().from("trnsprt_invc")
				.localField("transportOrder.invoiceNo").foreignField("orderId").as("transportInvoice");

		final MatchOperation matchTransport = Aggregation.match(Criteria.where("transportInvoice.stockNo").is(stockNo));

		final LookupOperation lookupMasterTransport = LookupOperation.newLookup().from("m_trnsprtr")
				.localField("transportInvoice.transporterId").foreignField("code").as("masterTransporter");
		final LookupOperation lookupPickupLocation = LookupOperation.newLookup().from("m_lctn")
				.localField("transportInvoice.pickupLocation").foreignField("code").as("pickupLocation");
		final LookupOperation lookupDropLocation = LookupOperation.newLookup().from("m_lctn")
				.localField("transportInvoice.dropLocation").foreignField("code").as("dropLocation");

		GroupOperation group = Aggregation.group("$stockNo").first("stockNo").as("stockNo")
				.push(new BasicDBObject("fromLocation", "$pickupLocation.displayName")
						.append("fromLocationCustom", "$transportInvoice.pickupLocationCustom")
						.append("toLocation", "$dropLocation.displayName")
						.append("toLocationCustom", "$transportInvoice.dropLocationCustom")
						.append("forwarder", "$forwarder.name").append("transporter", "$masterTransporter.name")
						.append("transportOrdertatus", "$transportOrder.status")
						.append("estimatedAmount", "$transportOrder.charge")
						.append("actualAmount", "$transportInvoice.amount")
						.append("dueDate", "$transportInvoice.dueDate")
						.append("transportInvoiceStatus", "$transportInvoice.status")
						.append("invoiceAttachmentFilename", "$transportInvoice.invoiceAttachmentFilename")
						.append("invoiceAttachmentDiskFilename", "$transportInvoice.invoiceAttachmentDiskFilename"))
				.as("transGroupInfo").last("dropLocation.displayName").as("currentLocation")
				.last("transportInvoice.dropLocationCustom").as("currrentLocationCustom").last("transportOrder.status")
				.as("transportOrdertatus").last("transportationCount").as("transportationCount")
				.last("transportInvoice.status").as("transportInvoiceStatus").last("transportInvoice.invoiceRefNo")
				.as("invoiceRefNo").last("transportInvoice.refNo").as("refNo").last("transportInvoice.invoiceDate")
				.as("invoiceDate").last("transportInvoice.dueDate").as("dueDate").last("masterTransporter.name")
				.as("transporter");

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupTransportOrder,
				Aggregation.unwind("$transportOrder", true), lookupTransportInvoice,
				Aggregation.unwind("$transportInvoice", true), matchTransport, lookupMasterTransport,
				Aggregation.unwind("$masterTransporter", true), lookupTransportForwarder,
				Aggregation.unwind("$transportForwarder", true), lookupPickupLocation,
				Aggregation.unwind("$pickupLocation", true), lookupDropLocation,
				Aggregation.unwind("$dropLocation", true), group);

		final AggregationResults<TStockTransportInfoDto> result = mongoTemplate.aggregate(aggregation, "t_stck",
				TStockTransportInfoDto.class);

		return result.getUniqueMappedResult();
	}

	@Override
	public TStockInspectionInfoDto findInspectionInfoForStockNo(String stockNo) {
		final MatchOperation match = Aggregation.match(Criteria.where("stockNo").is(stockNo));

		final LookupOperation lookupInspection = LookupOperation.newLookup().from("t_inspctn_instructn")
				.localField("stockNo").foreignField("stockNo").as("inspection");
		final LookupOperation lookupInspectionOrder = LookupOperation.newLookup().from("t_inspctn_odr_rqst")
				.localField("inspection.stockNo").foreignField("stockNo").as("inspectionOrder");
		final LookupOperation lookupInspectionCompany = LookupOperation.newLookup().from("m_inspctn_cmpny")
				.localField("inspectionOrder.inspectionCompany").foreignField("code").as("inspectionCompany");
		final LookupOperation lookupInspectionForwarder = LookupOperation.newLookup().from("m_frwrdr")
				.localField("inspectionOrder.forwarder").foreignField("code").as("inspectionForwarder");

		Cond inspectionCompany = ConditionalOperators
				.when(Criteria.where("$inspectionOrder.inspectionCompanyFlag").is(0))
				.thenValueOf("$inspectionCompany.name").otherwise("$inspectionForwarder.name");

		GroupOperation group = Aggregation.group("$stockNo").first("stockNo").as("stockNo")
				.push(new BasicDBObject("inspectionCompany", "$inspectionCompany.name")
						.append("destCountry", "$inspection.destCountry")
						.append("inspectionCompanyFlag", "$inspectionOrder.inspectionCompanyFlag")
						.append("status", "$inspection.status").append("forwarder", "$inspectionForwarder.name")
						.append("invoiceStatus", "$inspectionOrder.status"))
				.as("inspectionGroupInfo").last("inspectionOrder.country").as("inspectionCountry")
				.last("inspectionOrder.status").as("inspectionStatus").last("inspectionOrder.createdDate")
				.as("inspectionSentDate").last("inspectionOrder.inspectionDate").as("inspectionDate")
				.last("inspectionOrder.doumentSentStatus").as("inspectionDocStatus")
				.last("inspectionOrder.documentSentDate").as("inspectionDocSentDate")
				.last("inspectionOrder.dateOfIssue").as("dateOfIssue").last("inspectionOrder.certificateNo")
				.as("certificateNo").last(inspectionCompany).as("inspectionCompany").last("inspectionOrder.location")
				.as("inspectionLocation");

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupInspection,
				Aggregation.unwind("$inspection", true), lookupInspectionOrder,
				Aggregation.unwind("$inspectionOrder", true), lookupInspectionCompany,
				Aggregation.unwind("$inspectionCompany", true), lookupInspectionForwarder,
				Aggregation.unwind("$inspectionForwarder", true), group);

		final AggregationResults<TStockInspectionInfoDto> result = mongoTemplate.aggregate(aggregation, "t_stck",
				TStockInspectionInfoDto.class);

		return result.getUniqueMappedResult();
	}

	@Override
	public TStockShippingInfoDto findShippingInfoForStockNo(String stockNo) {
		final MatchOperation match = Aggregation.match(Criteria.where("stockNo").is(stockNo));

		final LookupOperation lookupShippingInstruction = LookupOperation.newLookup().from("t_shppng_instructn")
				.localField("stockNo").foreignField("stockNo").as("shippingInstruction");
		final LookupOperation lookupShippingRequest = LookupOperation.newLookup().from("t_shppng_rqust")
				.localField("shippingInstruction.shippingInstructionId").foreignField("shippingInstructionId")
				.as("shippingRequest");
		final LookupOperation lookupShippingForwarder = LookupOperation.newLookup().from("m_frwrdr")
				.localField("shippingRequest.forwarderId").foreignField("code").as("forwarder");
		final LookupOperation lookupShippingSchedule = LookupOperation.newLookup().from("t_shpmnt_schdl")
				.localField("shippingRequest.scheduleId").foreignField("scheduleId").as("schedule");
		final LookupOperation lookupShippingCompany = LookupOperation.newLookup().from("m_shppng_cmpny")
				.localField("schedule.shippingCompanyNo").foreignField("shippingCompanyNo").as("shippingCompany");

		final LookupOperation lookupShip = LookupOperation.newLookup().from("m_ship").localField("schedule.shipId")
				.foreignField("shipId").as("ship");
		final LookupOperation lookupFreightShippingInvoice = LookupOperation.newLookup().from("t_frght_shpng_invc")
				.localField("shipmentRequestId").foreignField("shipmentRequestId").as("freightShippingInvoice");
		final LookupOperation lookupFreightContainerInvoice = LookupOperation.newLookup()
				.from("t_shipping_invoice_items").localField("invoiceNo").foreignField("invoiceNo")
				.as("freightShippingContainerInvoice");

		GroupOperation group = Aggregation.group("$stockNo").first("stockNo").as("stockNo")
				.push(new BasicDBObject("forwarder", "$forwarder.name")
						.append("originPort", "$shippingRequest.orginPort")
						.append("destinationCountry", "$shippingRequest.destCountry")
						.append("destinationPort", "$shippingRequest.destPort")
						.append("destinationYard", "$shippingRequest.yard")
						.append("shippingType", "$shippingRequest.shippingType")
						.append("shippingStatus", "$shippingRequest.status")
						.append("invoiceStatus", "$shippingRequest.invoiceStatus")
						.append("containerNo", "$shippingRequest.containerNo")
						.append("shippingBlNo", "$shippingRequest.blNo")
						.append("containerName", "$shippingRequest.containerName")
						.append("slaNo", "$shippingRequest.slaNo").append("shippingCompany", "$shippingCompany.name")
						.append("shipName", "$ship.name"))
				.as("shippingGroupInfo");

//				.first("freightShippingInvoice.freightCharge").as("freightCharge")
//				.first("freightShippingInvoice.shippingCharge").as("shippingCharge")
//				.first("freightShippingInvoice.inspectionCharge").as("inspectionCharge")
//				.first("freightShippingInvoice.radiationCharge").as("radiationCharge")
//				.first("freightShippingInvoice.otherCharges").as("othersCharge");

		final Aggregation aggregation = Aggregation.newAggregation(match, lookupShippingInstruction,
				Aggregation.unwind("$shippingInstruction", true), lookupShippingRequest,
				Aggregation.unwind("$shippingRequest", true), lookupShippingForwarder,
				Aggregation.unwind("$forwarder", true), lookupShippingSchedule, Aggregation.unwind("$schedule", true),
				lookupShippingCompany, Aggregation.unwind("$shippingCompany", true), lookupShip,
				Aggregation.unwind("$ship", true), lookupFreightShippingInvoice,
				Aggregation.unwind("$freightShippingInvoice", true), lookupFreightContainerInvoice,
				Aggregation.unwind("$freightShippingContainerInvoice", true), group);

		final AggregationResults<TStockShippingInfoDto> result = mongoTemplate.aggregate(aggregation, "t_stck",
				TStockShippingInfoDto.class);

		return result.getUniqueMappedResult();
	}

	@Override
	public StockInfoDto fetchStockData(String stockNo) {
		TStock stock = stockRepository.findOneByStockNo(stockNo);
		TDocumentReceived documentReceived = documentReceivedRepository.findOneByStockNo(stockNo);
		List<TDocumentConversion> documentConversions = documentConversionRepository.findAllByStockNo(stockNo);
		List<TSalesInvoice> salesInvoices = salesInvoiceRepository.findAllByStockNo(stockNo);
		TCustomer reservedCustomer = null;

		MLocation lastTransportLocation = null;
		if (!AppUtil.isObjectEmpty(stock.getLastTransportLocation())) {
			lastTransportLocation = locationRepository.findOneByCode(stock.getLastTransportLocation());
		}
		// prepare reservedInfo
		StockInfoReservedInfoDto reservedInfoDto = null;
		if (!AppUtil.isObjectEmpty(stock.getReservedInfo())) {
			if (AppUtil.isObjectEmpty(stock.getReservedInfo().getCustomerId())) {
				reservedCustomer = customerRepository.findOneByCode(stock.getReservedInfo().getCustomerId());
			}
			reservedInfoDto = new StockInfoReservedInfoDto(stock.getReservedInfo(), reservedCustomer);
		}
		// prepare stock base info dto
		StockBaseInfoDto baseInfoDto = new StockBaseInfoDto(stock, lastTransportLocation, reservedInfoDto);
		// prepare sales invoice dto
		List<StockInfoSalesDto> salesDtos = new ArrayList<>();
		for (TSalesInvoice salesInvoice : salesInvoices) {
			TCustomer customer = customerRepository.findOneByCode(salesInvoice.getCustomerId());
			MUser salesPerson = userRepository.findOneByCode(salesInvoice.getSalesPerson());
			StockInfoSalesDto salesDto = new StockInfoSalesDto(salesInvoice, customer, salesPerson);
			salesDtos.add(salesDto);
		}
		// prepare document conversion dto
		List<StockInfoDocumentConversionDto> documentConversionDtos = new ArrayList<>();
		for (TDocumentConversion documentConversion : documentConversions) {
			MShippingCompany shippingCompany = null;
			MInspectionCompany inspectionCompany = null;
			if (!AppUtil.isObjectEmpty(documentConversion.getShippingCompanyId())) {
				shippingCompany = shippingCompanyRepository
						.findOneByShippingCompanyNo(documentConversion.getShippingCompanyId());
			}
			if (!AppUtil.isObjectEmpty(documentConversion.getInspectionCompanyId())) {
				inspectionCompany = inspectionCompanyRepository
						.findOneByCode(documentConversion.getInspectionCompanyId());
			}
			StockInfoDocumentConversionDto conversionDto = new StockInfoDocumentConversionDto(documentConversion,
					shippingCompany, inspectionCompany);
			documentConversionDtos.add(conversionDto);
		}
		// prepare document info
		StockInfoDocumentDto documentInfo = new StockInfoDocumentDto(stock, documentReceived);
		documentInfo.setDocumentConversionDtos(documentConversionDtos);
		documentInfo.setSalesInvoices(salesDtos);
		StockInfoDto stockInfo = new StockInfoDto();
		stockInfo.setBaseInfo(baseInfoDto);
		stockInfo.setDocumentInfo(documentInfo);
		stockInfo.setStockNo(stockNo);
		stockInfo.setPurchaseDetail(findPurchaseInfoForStockNo(stockNo));
		stockInfo.setTransportDetail(findTransportInfoForStockNo(stockNo));
		stockInfo.setInspectionDetail(findInspectionInfoForStockNo(stockNo));
		stockInfo.setShippingDetail(findShippingInfoForStockNo(stockNo));
		
		TStockPurchaseInfoDto purchaseInfo = stockInfo.getPurchaseDetail();
		List<S3ObjectSummary> summary = s3FactoryService.getNoOfAttachment(baseInfoDto.getChassisNo(),
				baseInfoDto.getDestinationCountry(), purchaseInfo.getAuctionCompany());
		
		final String imageBaseURl = FbFeedConstants.IMAGE_URL;
		List<String> urls = new ArrayList<String>();
		summary.stream().forEach(data -> {
			String url = imageBaseURl + data.getKey();
			urls.add(url);
		});
		stockInfo.setNoOfAttachments(summary.size());
		stockInfo.setImageUrls(urls);
		
		return stockInfo;
	}

}
