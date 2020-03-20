//package com.nexware.aajapan.services.impl;
//
//import java.util.Arrays;
//
//import org.bson.Document;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.aggregation.Aggregation;
//import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
//import org.springframework.data.mongodb.core.aggregation.AggregationResults;
//import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;
//import org.springframework.data.mongodb.core.aggregation.ConditionalOperators.Cond;
//import org.springframework.data.mongodb.core.aggregation.GroupOperation;
//import org.springframework.data.mongodb.core.aggregation.LookupOperation;
//import org.springframework.data.mongodb.core.aggregation.MatchOperation;
//import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.stereotype.Service;
//
//import com.mongodb.BasicDBObject;
//import com.nexware.aajapan.core.Constants;
//import com.nexware.aajapan.dto.StockInfoDto;
//import com.nexware.aajapan.services.StockInfoService;
//
//public class StockInfoServiceImpl2 {
//
//	@Autowired
//	private MongoTemplate mongoTemplate;
//
//	@Override
//	public StockInfoDto findOneStockInfoByStockNo(String stockNo) {
//
//		final MatchOperation match = Aggregation.match(Criteria.where("stockNo").is(stockNo));
//		final LookupOperation lookupPurchase = LookupOperation.newLookup().from("t_prchs_invc").localField("stockNo")
//				.foreignField("stockNo").as("purchase");
//		final LookupOperation lookupSupplier = LookupOperation.newLookup().from("m_spplr")
//				.localField("purchase.supplierId").foreignField("supplierCode").as("supplier");
//		final LookupOperation lookupTransportOrder = LookupOperation.newLookup().from("trnsprt_ordr_items")
//				.localField("stockNo").foreignField("stockNo").as("transportOrder");
//		final LookupOperation lookupTransportInvoice = LookupOperation.newLookup().from("trnsprt_invc")
//				.localField("stockNo").foreignField("stockNo").as("transportInvoice");
//		final LookupOperation lookupMasterTransport = LookupOperation.newLookup().from("m_trnsprtr")
//				.localField("transportInvoice.transporterId").foreignField("code").as("masterTransporter");
//		final LookupOperation lookupTransportForwarder = LookupOperation.newLookup().from("m_frwrdr")
//				.localField("transportOrder.forwarder").foreignField("code").as("transportForwarder");
//		final LookupOperation lookupPickupLocation = LookupOperation.newLookup().from("m_lctn")
//				.localField("transportInfo.pickupLocation").foreignField("code").as("pickupLocation");
//		final LookupOperation lookupDropLocation = LookupOperation.newLookup().from("m_lctn")
//				.localField("transportInfo.dropLocation").foreignField("code").as("dropLocation");
//		final LookupOperation lookupInspection = LookupOperation.newLookup().from("t_inspctn_instructn")
//				.localField("stockNo").foreignField("stockNo").as("inspection");
//		final LookupOperation lookupInspectionOrder = LookupOperation.newLookup().from("t_inspctn_odr_rqst")
//				.localField("inspection.code").foreignField("instructionId").as("inspectionOrder");
//		final LookupOperation lookupInspectionCompany = LookupOperation.newLookup().from("m_inspctn_cmpny")
//				.localField("inspectionOrder.inspectionCompany").foreignField("code").as("inspectionCompany");
//		final LookupOperation lookupInspectionForwarder = LookupOperation.newLookup().from("m_frwrdr")
//				.localField("inspectionOrder.forwarder").foreignField("code").as("inspectionForwarder");
//		final LookupOperation lookupShippingInstruction = LookupOperation.newLookup().from("t_shppng_instructn")
//				.localField("stockNo").foreignField("stockNo").as("shippingInstruction");
//		final LookupOperation lookupShippingRequest = LookupOperation.newLookup().from("t_shppng_rqust")
//				.localField("shippingInstruction.shippingInstructionId").foreignField("shippingInstructionId")
//				.as("shippingRequest");
//		final LookupOperation lookupShippingForwarder = LookupOperation.newLookup().from("m_frwrdr")
//				.localField("shippingRequest.forwarderId").foreignField("code").as("shippingForwarder");
//		final LookupOperation lookupFreightShippingInvoice = LookupOperation.newLookup().from("t_frght_shpng_invc")
//				.localField("shipmentRequestId").foreignField("shipmentRequestId").as("freightShippingInvoice");
//		final LookupOperation lookupFreightContainerInvoice = LookupOperation.newLookup()
//				.from("t_shipping_invoice_items").localField("invoiceNo").foreignField("invoiceNo")
//				.as("freightShippingContainerInvoice");
//
//		AggregationOperation addAuctionHouse = context -> new Document("$addFields",
//				new Document("auctionHouse", new Document("$filter",
//						new Document("input", "$supplier.supplierLocations").append("as", "result").append("cond",
//								new Document("$eq", Arrays.asList("$$result._id", "$purchase.auctionHouseId"))))));
//
//		Cond rikusoPayment = ConditionalOperators
//				.when(Criteria.where("$purchase.type").is(Constants.PURCHASE_INVOICE_ITEM_TYPE_RIKUSO_PAYMENT))
//				.thenValueOf("$purchase.otherCharges").otherwise("0.0");
//
//		Cond rikusoPaymentTax = ConditionalOperators
//				.when(Criteria.where("$purchase.type").is(Constants.PURCHASE_INVOICE_ITEM_TYPE_RIKUSO_PAYMENT))
//				.thenValueOf("$purchase.othersCostTaxAmount").otherwise("0.0");
//
//		Cond cancellationCharge = ConditionalOperators
//				.when(Criteria.where("$purchase.type").is(Constants.PURCHASE_INVOICE_ITEM_TYPE_CANCELLATION_CHARGES))
//				.thenValueOf("$purchase.otherCharges").otherwise("0.0");
//
//		Cond cancellationChargeTax = ConditionalOperators
//				.when(Criteria.where("$purchase.type").is(Constants.PURCHASE_INVOICE_ITEM_TYPE_CANCELLATION_CHARGES))
//				.thenValueOf("$purchase.othersCostTaxAmount").otherwise("0.0");
//
//		Cond unsoldAuctionCharge = ConditionalOperators
//				.when(Criteria.where("$purchase.type").is(Constants.PURCHASE_INVOICE_ITEM_TYPE_UNSOLD_AUCTION_CHARGE))
//				.thenValueOf("$purchase.otherCharges").otherwise("0.0");
//
//		Cond unsoldAuctionChargeTax = ConditionalOperators
//				.when(Criteria.where("$purchase.type").is(Constants.PURCHASE_INVOICE_ITEM_TYPE_UNSOLD_AUCTION_CHARGE))
//				.thenValueOf("$purchase.othersCostTaxAmount").otherwise("0.0");
//
//		Cond cancellationPenalityCharge = ConditionalOperators
//				.when(Criteria.where("$purchase.type")
//						.is(Constants.PURCHASE_INVOICE_ITEM_TYPE_CANCELLATION_PENALTY_CHARGES))
//				.thenValueOf("$purchase.otherCharges").otherwise("0.0");
//
//		Cond cancellationPenalityChargeTax = ConditionalOperators
//				.when(Criteria.where("$purchase.type")
//						.is(Constants.PURCHASE_INVOICE_ITEM_TYPE_CANCELLATION_PENALTY_CHARGES))
//				.thenValueOf("$purchase.othersCostTaxAmount").otherwise("0.0");
//
//		Cond takeOutCharge = ConditionalOperators
//				.when(Criteria.where("$purchase.type").is(Constants.PURCHASE_INVOICE_ITEM_TYPE_TAKE_OUT_STOCK))
//				.thenValueOf("$purchase.otherCharges").otherwise("0.0");
//
//		Cond takeOutChargeTax = ConditionalOperators
//				.when(Criteria.where("$purchase.type").is(Constants.PURCHASE_INVOICE_ITEM_TYPE_TAKE_OUT_STOCK))
//				.thenValueOf("$purchase.othersCostTaxAmount").otherwise("0.0");
//
//		Cond recyclepaid = ConditionalOperators
//				.when(Criteria.where("$purchase.type").is(Constants.PURCHASE_INVOICE_ITEM_TYPE_RECYCLE_PAID))
//				.thenValueOf("$purchase.otherCharges").otherwise("0.0");
//
//		Cond recyclepaidTax = ConditionalOperators
//				.when(Criteria.where("$purchase.type").is(Constants.PURCHASE_INVOICE_ITEM_TYPE_RECYCLE_PAID))
//				.thenValueOf("$purchase.othersCostTaxAmount").otherwise("0.0");
//
//		Cond inspectionCompany = ConditionalOperators
//				.when(Criteria.where("$inspectionOrder.inspectionCompanyFlag").is(0))
//				.thenValueOf("$inspectionCompany.name").otherwise("$inspectionForwarder.name");
//
////		List<ConditionalOperators.Switch.CaseOperator> cases = new ArrayList<>();
////
////		ConditionalOperators.Switch.CaseOperator cond1 = ConditionalOperators.Switch.CaseOperator
////				.when(ConditionalOperators
////						.when(Criteria.where("purchase.type").is(Constants.PURCHASE_INVOICE_ITEM_TYPE_RIKUSO_PAYMENT))
////						.thenValueOf("purchase.otherCharges").otherwise(""))
////				.then("");
////
////		cases.add(cond1);
//
//		ProjectionOperation project = Aggregation.project()
//				.andInclude("stockNo", "chassisNo", "lastTransportLocation", "transportInfo", "transportationCount")
//				// purchase details
//				.and("supplier.company").as("auctionCompany").and("auctionHouse.auctionHouse").as("auctionHouse")
//				.and("purchaseInfo.date").as("purchaseDate").and("purchase.status").as("purchaseInvoiceStatus")
//				.and("purchase.invoiceNo").as("purchaseInvoiceNo").and("purchase.type").as("purchaseInvoicetype")
//				.and("purchase.auctionRefNo").as("auctionRefNo").and("purchase.invoiceDate").as("purchaseInvoiceDate")
//				.and("purchase.dueDate").as("purchaseDueDate").and("purchase.invoiceAttachmentFilename")
//				.as("purchaseInvoiceAttachmentFilename").and("purchase.invoiceAttachmentDiskFilename")
//				.as("purchaseInvoiceAttachmentDiskFilename")
//				// TODO : attachmentDirectory need to be added
//				.and("purchase.invoiceUpload").as("purchaseInvoiceUpload").and("purchase.attachementViewed")
//				.as("purchaseAttachementViewed").and("purchase.purchaseCost").as("purchaseCost")
//				.and("purchase.purchaseCostTaxAmount").as("purchaseCostTax").and("purchase.commision").as("commision")
//				.and("purchase.commisionTaxAmount").as("commisionTax").and("purchase.recycle").as("recycle")
//				.and("purchase.roadTax").as("roadTax").and("purchase.otherCharges").as("others")
//				.and("purchase.othersCostTaxAmount").as("othersTax").and(rikusoPayment).as("rikusoPayment")
//				.and(rikusoPaymentTax).as("rikusoPaymentTax").and(cancellationCharge).as("cancellationCharge")
//				.and(cancellationChargeTax).as("cancellationChargeTax").and(unsoldAuctionCharge)
//				.as("unsoldAuctionCharge").and(unsoldAuctionChargeTax).as("unsoldAuctionChargeTax")
//				.and(cancellationPenalityCharge).as("cancellationPenalityCharge").and(cancellationPenalityChargeTax)
//				.as("cancellationPenalityChargeTax").and(takeOutCharge).as("takeOutCharge").and(takeOutChargeTax)
//				.as("takeOutChargeTax").and(recyclepaid).as("recyclepaid").and(recyclepaidTax).as("recyclepaidTax")
//				// transport details
//				.and("pickupLocation.displayName").as("fromLocation").and("dropLocation.displayName").as("toLocation")
//				.and("transportForwarder.name").as("transportForwarder").and("masterTransporter.name").as("transporter")
//				.and("transportOrder.status").as("transportOrdertatus").and("transportInvoice.dueDate")
//				.as("transportDueDate").and("transportInvoice.status").as("transportInvoiceStatus")
//				.and("transportInvoice.invoiceAttachmentFilename").as("transportInvoiceAttachmentFilename")
//				.and("transportInvoice.invoiceAttachmentDiskFilename").as("transportInvoiceAttachmentDiskFilename")
//				// TODO : transport attachmentDirectory need to be added
//				// Inspection Details
//				.and("inspectionOrder.country").as("inspectionCountry").and("inspectionOrder.status")
//				.as("inspectionStatus").and("inspectionOrder.createdDate").as("inspectionSentDate")
//				.and("inspectionOrder.inspectionDate").as("inspectionDate").and("inspectionOrder.doumentSentStatus")
//				.as("inspectionDocStatus").and("inspectionOrder.documentSentDate").as("inspectionDocSentDate")
//				.and("inspectionOrder.dateOfIssue").as("inspectionDateOfIssue").and("inspectionOrder.certificateNo")
//				.as("inspectionCertNo").and(inspectionCompany).as("inspectionCompany").and("inspectionOrder.location")
//				.as("inspectionLocation")
//				// Shipping Details
//				.and("shippingForwarder.name").as("shippingForwarder").and("shippingRequest.orginPort")
//				.as("shippingOriginPort").and("shippingRequest.destCountry").as("shippingDestinationCountry")
//				.and("shippingRequest.destPort").as("shippingDestinationPort").and("shippingRequest.yard")
//				.as("shippingDestinationYard").and("shippingRequest.shippingType").as("shippingType")
//				.and("shippingRequest.status").as("shippingStatus").and("shippingRequest.invoiceStatus")
//				.as("shippingInvoiceStatus").and("shippingRequest.containerNo").as("shippingContainerNo")
//				.and("shippingRequest.blNo").as("shippingBlNo").and("shippingRequest.containerName")
//				.as("shippingContainerName").and("shippingRequest.slaNo").as("shippingSlaNo")
//				.and("freightShippingInvoice.freightCharge").as("shippingFreightCharge")
//				.and("freightShippingInvoice.shippingCharge").as("shippingShippingCharge")
//				.and("freightShippingInvoice.inspectionCharge").as("shippingInspectionCharge")
//				.and("freightShippingInvoice.radiationCharge").as("shippingRadiationCharge")
//				.and("freightShippingInvoice.otherCharges").as("shippingOthersCharge");
//
////		final LookupOperation lookupPurchaseInvoice = LookupOperation.newLookup().from("t_invc_pymnt_trnsctn")
////				.localField("invoiceNo").foreignField("invoiceNo").as("invoicePayment");
////		final LookupOperation lookupBankPurchase = LookupOperation.newLookup().from("t_invc_pymnt_trnsctn")
////				.localField("invoicePayment.bankId").foreignField("bankSeq").as("purchasePaymentBank");
////		final LookupOperation lookupTransportInvoicePayment = LookupOperation.newLookup().from("t_invc_pymnt_trnsctn")
////				.localField("transportInvoice.invoiceRefNo").foreignField("invoiceNo").as("transportInvoicePayment");
////		final LookupOperation lookupBankTransport = LookupOperation.newLookup().from("t_invc_pymnt_trnsctn")
////				.localField("transportInvoicePayment.bankId").foreignField("bankSeq").as("transportPaymentBank");
//
//		GroupOperation stockGroup = Aggregation.group("$stockNo").first("stockNo").as("stockNo").first("chassisNo")
//				.as("chassisNo").first("lastTransportLocation").as("lastTransportLocation").first("transportationCount")
//				.as("transportationCount").first("transportInfo").as("transportInfo")
//				// PurchaseInfo
//				.push(new BasicDBObject("auctionCompany", "$auctionCompany").append("auctionHouse", "$auctionHouse")
//						.append("purchaseDate", "$purchaseDate")
//						.append("purchaseInvoiceStatus", "$purchaseInvoiceStatus")
//						.append("purchaseInvoiceNo", "$purchaseInvoiceNo")
//						.append("purchaseInvoicetype", "$purchaseInvoicetype").append("auctionRefNo", "$auctionRefNo")
//						.append("purchaseInvoiceDate", "$purchaseInvoiceDate")
//						.append("purchaseDueDate", "$purchaseDueDate")
//						.append("purchaseInvoiceAttachmentFilename", "$purchaseInvoiceAttachmentFilename")
//						.append("purchaseInvoiceAttachmentDiskFilename", "$purchaseInvoiceAttachmentDiskFilename")
//						.append("purchaseInvoiceUpload", "$purchaseInvoiceUpload")
//						.append("purchaseAttachementViewed", "$purchaseAttachementViewed")
//						.append("purchaseCost", "$purchaseCost").append("purchaseCostTax", "$purchaseCostTax")
//						.append("commision", "$commision").append("commisionTax", "$commisionTax")
//						.append("recycle", "$recycle").append("roadTax", "$roadTax").append("others", "$others")
//						.append("othersTax", "$othersTax").append("rikusoPayment", "$rikusoPayment")
//						.append("rikusoPaymentTax", "$rikusoPaymentTax")
//						.append("cancellationCharge", "$cancellationCharge")
//						.append("cancellationChargeTax", "$cancellationChargeTax")
//						.append("unsoldAuctionCharge", "$unsoldAuctionCharge")
//						.append("unsoldAuctionChargeTax", "$unsoldAuctionChargeTax")
//						.append("cancellationPenalityCharge", "$cancellationPenalityCharge")
//						.append("cancellationPenalityChargeTax", "$cancellationPenalityChargeTax")
//						.append("takeOutCharge", "$takeOutCharge").append("takeOutChargeTax", "$takeOutChargeTax")
//						.append("recyclepaid", "$recyclepaid").append("recyclepaidTax", "$recyclepaidTax"))
//				.as("purchaseDetails")
//				// transportationInfo
//				.push(new BasicDBObject("fromLocation", "$fromLocation").append("toLocation", "$toLocation")
//						.append("transportForwarder", "$transportForwarder")
//						.append("transportOrdertatus", "$transportOrdertatus").append("transporter", "$transporter")
//						.append("transportForwarder", "$transportForwarder")
//						.append("transportActualAmount", "$transportActualAmount")
//						.append("transportTaxAmount", "$transportTaxAmount")
//						.append("transportInvoiceStatus", "$transportInvoiceStatus")
//						.append("transportInvoiceAttachmentFilename", "$transportInvoiceAttachmentFilename")
//						.append("transportInvoiceAttachmentDiskFilename", "$transportInvoiceAttachmentDiskFilename"))
//				.as("transportDetails")
//				// inspectionInfo
//				.push(new BasicDBObject("inspectionCountry", "$inspectionCountry")
//						.append("inspectionStatus", "$inspectionStatus")
//						.append("inspectionSentDate", "$inspectionSentDate").append("inspectionDate", "$inspectionDate")
//						.append("inspectionDocStatus", "$inspectionDocStatus")
//						.append("inspectionDocSentDate", "$inspectionDocSentDate")
//						.append("inspectionDateOfIssue", "$inspectionDateOfIssue")
//						.append("inspectionCertNo", "$inspectionCertNo")
//						.append("inspectionCompany", "$inspectionCompany")
//						.append("inspectionLocation", "$inspectionLocation"))
//				.as("inspectionDetails")
//				// shipping Details
//				.push(new BasicDBObject("shippingForwarder", "$shippingForwarder")
//						.append("shippingOriginPort", "$shippingOriginPort")
//						.append("shippingDestinationCountry", "$shippingDestinationCountry")
//						.append("shippingDestinationPort", "$shippingDestinationPort")
//						.append("shippingDestinationYard", "$shippingDestinationYard")
//						.append("shippingType", "$shippingType").append("shippingStatus", "$shippingStatus")
//						.append("shippingInvoiceStatus", "$shippingInvoiceStatus")
//						.append("shippingContainerNo", "$shippingContainerNo").append("shippingBlNo", "$shippingBlNo")
//						.append("shippingContainerName", "$shippingContainerName")
//						.append("shippingSlaNo", "$shippingSlaNo").append("amount",
//								new Document("$sum",
//										Arrays.asList("$shippingFreightCharge", "$shippingShippingCharge",
//												"$shippingInspectionCharge", "$shippingRadiationCharge",
//												"$shippingOthersCharge"))))
//				.as("shippingDetails");
//
////				.push(new BasicDBObject("paymentVoucherNo", "$invoicePayment.paymentVoucherNo")
////						.append("bank", "$purchasePaymentBank.bankName")
////						.append("paidDate", "$invoicePayment.approvedDate")
////						.append("invoiceType", "$invoicePayment.invoiceType")
////						.append("bankStatementAttachmentFilename", "$invoicePayment.bankStatementAttachmentFilename")
////						.append("bankStatementAttachmentDiskFilename",
////								"$invoicePayment.bankStatementAttachmentDiskFilename"))
////				// TODO : attachmentDirectory need to be added
////				.as("auctionPaymentInfo")
////				.push(new BasicDBObject("paymentVoucherNo", "$transportInvoicePayment.paymentVoucherNo")
////						.append("bank", "$transportPaymentBank.bankName")
////						.append("paidDate", "$transportInvoicePayment.approvedDate")
////						.append("invoiceType", "$transportInvoicePayment.invoiceType")
////						.append("bankStatementAttachmentFilename",
////								"$transportInvoicePayment.bankStatementAttachmentFilename")
////						.append("bankStatementAttachmentDiskFilename",
////								"$transportInvoicePayment.bankStatementAttachmentDiskFilename"))
////				// TODO : attachmentDirectory need to be added
////				.as("transportPaymentInfo");
//
//		final Aggregation aggregation = Aggregation.newAggregation(match, lookupPurchase,
//				Aggregation.unwind("$purchase", true), lookupTransportOrder,
//				Aggregation.unwind("$transportOrder", true), lookupTransportForwarder,
//				Aggregation.unwind("$transportForwarder", true), lookupTransportInvoice,
//				Aggregation.unwind("$transportInvoice", true), lookupMasterTransport,
//				Aggregation.unwind("$masterTransporter", true), lookupSupplier, Aggregation.unwind("$supplier", true),
//				addAuctionHouse, Aggregation.unwind("$auctionHouse", true), lookupPickupLocation,
//				Aggregation.unwind("$pickupLocation", true), lookupDropLocation,
//				Aggregation.unwind("$dropLocation", true), lookupInspection, Aggregation.unwind("$inspection", true),
//				lookupInspectionOrder, Aggregation.unwind("$inspectionOrder", true), lookupInspectionCompany,
//				Aggregation.unwind("$inspectionCompany", true), lookupInspectionForwarder,
//				Aggregation.unwind("$inspectionForwarder", true), lookupShippingInstruction,
//				Aggregation.unwind("$shippingInstruction", true), lookupShippingRequest,
//				Aggregation.unwind("$shippingRequest", true), lookupShippingForwarder,
//				Aggregation.unwind("$shippingForwarder", true), lookupFreightShippingInvoice,
//				Aggregation.unwind("$freightShippingInvoice", true), lookupFreightContainerInvoice,
//				Aggregation.unwind("$freightShippingContainerInvoice", true), project,
////				lookupPurchaseInvoice,
////				Aggregation.unwind("$invoicePayment", true), lookupTransportInvoicePayment,
////				Aggregation.unwind("$transportInvoicePayment", true), lookupBankPurchase,
////				Aggregation.unwind("$purchasePaymentBank", true), lookupBankTransport,
////				Aggregation.unwind("$transportPaymentBank", true), 
//				stockGroup).withOptions(Aggregation.newAggregationOptions().allowDiskUse(true).build());
//		System.out.println(aggregation);
//
//		final AggregationResults<StockInfoDto> result = mongoTemplate.aggregate(aggregation, "t_stck",
//				StockInfoDto.class);
//		System.out.println(result);
//
//		return result.getUniqueMappedResult();
//
//	}
//
//}
