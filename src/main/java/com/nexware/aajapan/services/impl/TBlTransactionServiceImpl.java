package com.nexware.aajapan.services.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.BulkOperations.BulkMode;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AccumulatorOperators;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mongodb.BasicDBObject;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.BlTransactionListDto;
import com.nexware.aajapan.dto.CRDto;
import com.nexware.aajapan.dto.MLoginDto;
import com.nexware.aajapan.models.ShippingInstructionInfo;
import com.nexware.aajapan.models.TBlTransaction;
import com.nexware.aajapan.models.TShippingInstruction;
import com.nexware.aajapan.models.TShippingRequest;
import com.nexware.aajapan.models.TStock;
import com.nexware.aajapan.repositories.TBlTransactionRepository;
import com.nexware.aajapan.repositories.TShippingInstructionRepository;
import com.nexware.aajapan.repositories.TShippingRequestRepository;
import com.nexware.aajapan.services.SecurityService;
import com.nexware.aajapan.services.SequenceService;
import com.nexware.aajapan.services.TBlTransactionService;
import com.nexware.aajapan.utils.AppUtil;

@Service
@Transactional
public class TBlTransactionServiceImpl implements TBlTransactionService {

	@Autowired
	private TBlTransactionRepository blTransactionRepository;
	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private TShippingInstructionRepository shippingInstructionRepository;
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private SecurityService securityService;
	@Autowired
	private TShippingRequestRepository shippingRequestRepository;

	@Override
	public void saveBlTransaction(List<TShippingInstruction> shippingInstruction) {
		shippingInstruction.forEach(instruction -> {
			TBlTransaction blTrans = new TBlTransaction();
			blTrans.setCode(sequenceService.getNextSequence(Constants.SEQUENCE_KEY_BL_TRANSACTION));
			blTrans.setCustomerId(instruction.getCustomerId());
			blTrans.setConsigneeId(instruction.getConsigneeId());
			blTrans.setShippingInstructionId(instruction.getShippingInstructionId());
			blTransactionRepository.save(blTrans);
		});

	}

	@Override
	public void updateSingleBlTransaction(String shippingInstructionId, String customerId, String consigneeId) {
		TBlTransaction blTrans = new TBlTransaction();
		blTrans.setCode(sequenceService.getNextSequence(Constants.SEQUENCE_KEY_BL_TRANSACTION));
		blTrans.setCustomerId(customerId);
		blTrans.setConsigneeId(new ObjectId(consigneeId));
		blTrans.setShippingInstructionId(shippingInstructionId);
		blTransactionRepository.save(blTrans);

		TShippingInstruction shippingInstruction = shippingInstructionRepository
				.findOneByShippingInstructionId(shippingInstructionId);
		shippingInstruction.setCustomerId(customerId);
		shippingInstruction.setConsigneeId(new ObjectId(consigneeId));
		shippingInstructionRepository.save(shippingInstruction);

		// update stock table
		final MLoginDto loggedInUser = securityService.findLoggedInUser();

		final BulkOperations ops = mongoTemplate.bulkOps(BulkMode.UNORDERED, TStock.class);
		// update shipping instruction status
		final ShippingInstructionInfo shippingInstructionInfo = new ShippingInstructionInfo(customerId,
				loggedInUser.getUserId(), shippingInstructionId);
		final Update update = new Update();
		update.set("shippingInstructionInfo", shippingInstructionInfo);
		ops.updateOne(Query.query(Criteria.where("stockNo").is(shippingInstruction.getStockNo())), update);
		ops.execute();

	}

	@Override
	public void updateMultipleBlTransaction(List<Map<String, Object>> data) {

		final MLoginDto loggedInUser = securityService.findLoggedInUser();
		final BulkOperations ops = mongoTemplate.bulkOps(BulkMode.UNORDERED, TStock.class);
		data.forEach(trans -> {
			String shippingInstructionId = trans.get("shippingInstructionId").toString();
			String customerId = trans.get("customerId").toString();
			String consigneeId = trans.get("consigneeId").toString();

			TBlTransaction blTrans = new TBlTransaction();
			blTrans.setCode(sequenceService.getNextSequence(Constants.SEQUENCE_KEY_BL_TRANSACTION));
			blTrans.setCustomerId(customerId);
			blTrans.setConsigneeId(new ObjectId(consigneeId));
			blTrans.setShippingInstructionId(shippingInstructionId);
			blTransactionRepository.save(blTrans);

			TShippingInstruction shippingInstruction = shippingInstructionRepository
					.findOneByShippingInstructionId(shippingInstructionId);
			shippingInstruction.setCustomerId(customerId);
			shippingInstruction.setConsigneeId(new ObjectId(consigneeId));
			shippingInstructionRepository.save(shippingInstruction);

			// update shipping instruction status
			final ShippingInstructionInfo shippingInstructionInfo = new ShippingInstructionInfo(customerId,
					loggedInUser.getUserId(), shippingInstructionId);
			final Update update = new Update();
			update.set("shippingInstructionInfo", shippingInstructionInfo);
			ops.updateOne(Query.query(Criteria.where("stockNo").is(shippingInstruction.getStockNo())), update);

		});
		ops.execute();
	}

	@Override
	public void updateSingleBlTransactionNo(String blNo, String shipmentRequestId) {
		TShippingRequest request = shippingRequestRepository.findOneByShipmentRequestId(shipmentRequestId);
		request.setBlNo(blNo);
		shippingRequestRepository.save(request);

	}

	@Override
	public void updateMultipleBlTransactionNo(List<Map<String, Object>> data) {
		data.forEach(trans -> {

			String shipmentRequestId = trans.get("shipmentRequestId").toString();
			String blNo = trans.get("blNo").toString();

			TShippingRequest request = shippingRequestRepository.findOneByShipmentRequestId(shipmentRequestId);
			request.setBlNo(blNo);
			shippingRequestRepository.save(request);
		});

	}

	@Override
	public List<BlTransactionListDto> getBLListTransaction(String shippingInstructionId) {
		// TODO Auto-generated method stub
		return blTransactionRepository.getBLListTransaction(shippingInstructionId);
	}

	@Override
	public void updateReceiveOrSurrenderStatus(String shipmentRequestId, Integer recSurStatus) {
		TShippingRequest request = shippingRequestRepository.findOneByShipmentRequestId(shipmentRequestId);
		request.setRecSurStatus(recSurStatus);
		request.setRecSurDate(new Date());
		if (recSurStatus.equals(Constants.SHIPIING_REQUEST_REC_SUR_STATUS_SURRENDER)) {
			request.setBlDocumentStatus(Constants.SHIPIING_REQUEST_BL_DOC_STATUS_SURRENDER);
			request.setBlDocSurrenderedDate(new Date());
		} else {
			request.setBlDocumentStatus(request.getBlDocumentStatus());
		}
		shippingRequestRepository.save(request);
	}

	@Override
	public void updateBlDocStatus(String shipmentRequestId, Integer status) {
		TShippingRequest request = shippingRequestRepository.findOneByShipmentRequestId(shipmentRequestId);
		request.setBlDocumentStatus(status);
		if (status.equals(Constants.SHIPIING_REQUEST_BL_DOC_STATUS_RECEIVE)) {
			request.setBlDocReceivedDate(new Date());
		} else if (status.equals(Constants.SHIPIING_REQUEST_BL_DOC_STATUS_ISSUED)) {
			request.setBlDocIssuedDate(new Date());
		} else if (status.equals(Constants.SHIPIING_REQUEST_BL_DOC_STATUS_DISPATCHED)) {
			request.setBlDocDispatchedDate(new Date());
		}
		shippingRequestRepository.save(request);

	}

	@Override
	public List<CRDto> getCrList() {

		MatchOperation match = Aggregation.match(new Criteria().andOperator(Criteria.where("scheduleId").ne(null),
				Criteria.where("status").ne(Constants.SHIPIING_REQUEST_INITIATED),
				Criteria.where("destCountry").in("AUSTRALIA", "RUSSIA", "GEORGIA", "NEW ZEALAND")));

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
		final AggregationResults<CRDto> result = mongoTemplate.aggregate(aggregation, "t_shppng_rqust", CRDto.class);
		return result.getMappedResults();
	}

	@Override
	public void updateSingleCrTransaction(String shippingInstructionId, String customerId, String consigneeId) {
		TBlTransaction blTrans = new TBlTransaction();
		blTrans.setCode(sequenceService.getNextSequence(Constants.SEQUENCE_KEY_BL_TRANSACTION));
		blTrans.setCustomerId(customerId);
		blTrans.setConsigneeId(new ObjectId(consigneeId));
		blTrans.setShippingInstructionId(shippingInstructionId);
		blTransactionRepository.save(blTrans);

		TShippingInstruction shippingInstruction = shippingInstructionRepository
				.findOneByShippingInstructionId(shippingInstructionId);
		shippingInstruction.setCustomerId(customerId);
		shippingInstruction.setConsigneeId(new ObjectId(consigneeId));
		shippingInstructionRepository.save(shippingInstruction);

		// update stock table
		final MLoginDto loggedInUser = securityService.findLoggedInUser();

		final BulkOperations ops = mongoTemplate.bulkOps(BulkMode.UNORDERED, TStock.class);
		// update shipping instruction status
		final ShippingInstructionInfo shippingInstructionInfo = new ShippingInstructionInfo(customerId,
				loggedInUser.getUserId(), shippingInstructionId);
		final Update update = new Update();
		update.set("shippingInstructionInfo", shippingInstructionInfo);
		ops.updateOne(Query.query(Criteria.where("stockNo").is(shippingInstruction.getStockNo())), update);
		ops.execute();

	}

	@Override
	public void updateMultipleCrTransaction(List<Map<String, Object>> data) {

		final MLoginDto loggedInUser = securityService.findLoggedInUser();
		final BulkOperations ops = mongoTemplate.bulkOps(BulkMode.UNORDERED, TStock.class);
		data.forEach(trans -> {
			String shippingInstructionId = trans.get("shippingInstructionId").toString();
			String customerId = trans.get("customerId").toString();
			String consigneeId = trans.get("consigneeId").toString();

			TBlTransaction blTrans = new TBlTransaction();
			blTrans.setCode(sequenceService.getNextSequence(Constants.SEQUENCE_KEY_BL_TRANSACTION));
			blTrans.setCustomerId(customerId);
			blTrans.setConsigneeId(new ObjectId(consigneeId));
			blTrans.setShippingInstructionId(shippingInstructionId);
			blTransactionRepository.save(blTrans);

			TShippingInstruction shippingInstruction = shippingInstructionRepository
					.findOneByShippingInstructionId(shippingInstructionId);
			shippingInstruction.setCustomerId(customerId);
			shippingInstruction.setConsigneeId(new ObjectId(consigneeId));
			shippingInstructionRepository.save(shippingInstruction);

			// update shipping instruction status
			final ShippingInstructionInfo shippingInstructionInfo = new ShippingInstructionInfo(customerId,
					loggedInUser.getUserId(), shippingInstructionId);
			final Update update = new Update();
			update.set("shippingInstructionInfo", shippingInstructionInfo);
			ops.updateOne(Query.query(Criteria.where("stockNo").is(shippingInstruction.getStockNo())), update);

		});
		ops.execute();
	}

}
