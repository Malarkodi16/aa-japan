package com.nexware.aajapan.services.impl;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.BulkOperations.BulkMode;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.bulk.BulkWriteResult;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.models.TInsurance;
import com.nexware.aajapan.models.TPurchaseInvoice;
import com.nexware.aajapan.repositories.TInsuranceRepository;
import com.nexware.aajapan.services.ClaimService;
import com.nexware.aajapan.utils.AppUtil;
import com.nexware.aajapan.utils.ExcelUtil;

@Service
@Transactional
public class ClaimServiceImpl implements ClaimService {
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private TInsuranceRepository insuranceRepository;

	@Override
	public BulkWriteResult applyRecycleClaimByExcel(MultipartFile file) throws IOException {
		boolean executeUpdate = false;
		BulkOperations ops = this.mongoTemplate.bulkOps(BulkMode.UNORDERED, TPurchaseInvoice.class);
		try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			// skip header row
			rowIterator.next();
			while (rowIterator.hasNext()) {
				boolean performUpdate = false;
				Row row = rowIterator.next();
				Date appliedDate = ExcelUtil.getCellValue(row, 1, Date.class);
				String chassisNo = ExcelUtil.getCellValue(row, 2, String.class);
				Update update = new Update();
				// set claim status
				if (!AppUtil.isObjectEmpty(chassisNo)) {
					update.set("recycleClaimStatus", Constants.TPURCHASEINVOICE_RECYCLE_CLAIM_APPLIED);
					performUpdate = true;
				}
				// set applied date
				if (!AppUtil.isObjectEmpty(appliedDate)) {
//						Date appliedDate = new SimpleDateFormat("dd-MM-yyyy").parse(dateStr);
					update.set("recycleClaimAppliedDate", appliedDate);

				}
				if (performUpdate) {
					ops.updateOne(Query.query(Criteria.where("chassisNo").is(chassisNo).and("recycleClaimStatus")
							.is(Constants.TPURCHASEINVOICE_RECYCLE_NOT_CLAIMED)), update);
					executeUpdate = true;
				}
			}

		}
		return executeUpdate ? ops.execute() : null;

	}

	@Override
	public BulkWriteResult receiveRecycleClaimByExcel(MultipartFile file) throws IOException {
		boolean executeUpdate = false;
		BulkOperations ops = this.mongoTemplate.bulkOps(BulkMode.UNORDERED, TPurchaseInvoice.class);
		try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			// skip header row
//			AppUtil.skip(rowIterator, 1);
			// get received date
			Row row = rowIterator.next();
			Date receivedDate = ExcelUtil.getCellValue(row, 1, Date.class);
			AppUtil.skip(rowIterator, 1);
			while (rowIterator.hasNext()) {
				boolean performUpdate = false;
				row = rowIterator.next();
				String chassisNo = ExcelUtil.getCellValue(row, 1, String.class);
				Double charges = ExcelUtil.getCellValue(row, 2, Double.class);
				Double interest = ExcelUtil.getCellValue(row, 3, Double.class);
				Double receivedAmount = ExcelUtil.getCellValue(row, 4, Double.class);
				Update update = new Update();
				// set claim status
				if (!AppUtil.isObjectEmpty(chassisNo) && !AppUtil.isObjectEmpty(receivedAmount)) {
					update.set("recycleClaimStatus", Constants.TPURCHASEINVOICE_RECYCLE_RECEIVED);
					update.set("recycleClaimReceivedAmount", receivedAmount);
					performUpdate = true;
				}
				if (!AppUtil.isObjectEmpty(charges)) {
					update.set("recycleClaimCharge", charges);
				}
				if (!AppUtil.isObjectEmpty(charges)) {
					update.set("recycleClaimInterest", interest);
				}
				// set received date
				if (!AppUtil.isObjectEmpty(receivedDate)) {
					update.set("recycleClaimReceivedDate", receivedDate);
				}
				if (performUpdate) {
					ops.updateOne(Query.query(Criteria.where("chassisNo").is(chassisNo).and("recycleClaimStatus")
							.is(Constants.TPURCHASEINVOICE_RECYCLE_CLAIM_APPLIED)), update);
					executeUpdate = true;
				}
			}

		}
		return executeUpdate ? ops.execute() : null;

	}

	@Override
	public String applyInsuranceByExcel(MultipartFile file, Date insuranceApplyDate) throws IOException {
		int successCount = 0;
		int failureCount = 0;
		try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			// skip header row
			AppUtil.skip(rowIterator, 1);
			// get received date
			Row row;
			while (rowIterator.hasNext()) {
				row = rowIterator.next();
				if (ExcelUtil.isRowEmpty(row)) {
					continue;
				}
				String company = ExcelUtil.getCellValue(row, 0, String.class);
				String insuranceNo = ExcelUtil.getCellValue(row, 1, String.class);
				String ownerAddress = ExcelUtil.getCellValue(row, 2, String.class);
				String ownerName = ExcelUtil.getCellValue(row, 3, String.class);
				String fromYear = ExcelUtil.getCellValue(row, 4, String.class);
				String fromMonth = ExcelUtil.getCellValue(row, 5, String.class);
				String fromDate = ExcelUtil.getCellValue(row, 6, String.class);
				String toYear = ExcelUtil.getCellValue(row, 7, String.class);
				String toMonth = ExcelUtil.getCellValue(row, 8, String.class);
				String toDate = ExcelUtil.getCellValue(row, 9, String.class);
				String period = ExcelUtil.getCellValue(row, 10, String.class);
				String chassisNo = ExcelUtil.getCellValue(row, 11, String.class);
				TInsurance tInsurance = insuranceRepository.findOneByChassisNo(chassisNo);
				if (AppUtil.isObjectEmpty(tInsurance)) {
					TInsurance insurance = new TInsurance(chassisNo, company, insuranceNo, ownerAddress, ownerName);
					insurance.setFromYear(fromYear);
					insurance.setFromMonth(fromMonth);
					insurance.setFromDate(fromDate);
					insurance.setToYear(toYear);
					insurance.setToMonth(toMonth);
					insurance.setToDate(toDate);
					insurance.setPeriod(period);
					insurance.setInsuranceApplied(Constants.INVOICE_APPLIED);
					insurance.setInsuranceApplyDate(insuranceApplyDate);
					insuranceRepository.insert(insurance);
					successCount += 1;
				} else {
					failureCount += 1;
				}
			}

		}

		return "Success : " + successCount + " failure : " + failureCount;

	}

	@Override
	public BulkWriteResult receiveRecycleClaim(List<Document> data) throws IOException {

		boolean executeUpdate = true;
		BulkOperations ops = this.mongoTemplate.bulkOps(BulkMode.UNORDERED, TPurchaseInvoice.class);
		data.stream().forEach(p -> {

			Date receivedDate = null;
			try {
				receivedDate = new SimpleDateFormat("dd-MM-yyyy").parse(p.getString("recycleClaimReceivedDate"));
			} catch (ParseException e) {
				e.printStackTrace();
			}

			boolean performUpdate = false;

			String chassisNo = p.getString("chassisNo");
			Double charges = Double.valueOf(AppUtil.ifNullOrEmpty(p.getString("claimCharges"), "0.0"));
			Double interest = Double.valueOf(AppUtil.ifNullOrEmpty(p.getString("claimInterest"), "0.0"));
			Double receivedAmount = Double.valueOf(AppUtil.ifNullOrEmpty(p.getString("recycleReceivedCost"), "0.0"));
			Update update = new Update();
			// set claim status
			if (!AppUtil.isObjectEmpty(chassisNo) && !AppUtil.isObjectEmpty(receivedAmount)) {
				update.set("recycleClaimStatus", Constants.TPURCHASEINVOICE_RECYCLE_RECEIVED);
				update.set("recycleClaimReceivedAmount", receivedAmount);
				performUpdate = true;
			}
			if (!AppUtil.isObjectEmpty(charges)) {
				update.set("recycleClaimCharge", charges);
			}
			if (!AppUtil.isObjectEmpty(charges)) {
				update.set("recycleClaimInterest", interest);
			}
			// set received date
			if (!AppUtil.isObjectEmpty(receivedDate)) {
				update.set("recycleClaimReceivedDate", receivedDate);
			}
			if (performUpdate) {
				ops.updateOne(Query.query(Criteria.where("chassisNo").is(chassisNo).and("recycleClaimStatus")
						.is(Constants.TPURCHASEINVOICE_RECYCLE_CLAIM_APPLIED)), update);

			}

		});
		return executeUpdate ? ops.execute() : null;
	}

}