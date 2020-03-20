package com.nexware.aajapan.services.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.BulkOperations.BulkMode;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.bulk.BulkWriteResult;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.models.Attachment;
import com.nexware.aajapan.models.AuctionInfo;
import com.nexware.aajapan.models.MSupplier;
import com.nexware.aajapan.models.PurchaseInfo;
import com.nexware.aajapan.models.SupplierLocation;
import com.nexware.aajapan.models.TPurchaseInvoice;
import com.nexware.aajapan.models.TStock;
import com.nexware.aajapan.repositories.MasterSupplierRepository;
import com.nexware.aajapan.repositories.MasterVechicleMakerRepository;
import com.nexware.aajapan.repositories.StockRepository;
import com.nexware.aajapan.repositories.TPurchaseInvoiceRepository;
import com.nexware.aajapan.services.OpeningStockService;
import com.nexware.aajapan.services.SequenceService;
import com.nexware.aajapan.utils.AppUtil;
import com.nexware.aajapan.utils.ExcelUtil;

@Service
@Transactional
public class OpeningStockServiceImpl implements OpeningStockService {
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private MasterSupplierRepository mSupplierRepo;
	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private TPurchaseInvoiceRepository tPurchaseInvoiceRepository;
	@Autowired
	private StockRepository stockRepository;
	@Autowired
	private MasterVechicleMakerRepository masterVechicleMakerRepository;

	@Override
	public int openingStockUpload(MultipartFile file) throws IOException {
		// TODO Auto-generated method stub
		boolean performUpdate = false;
		List<TStock> tskList = new ArrayList<TStock>();
		List<TPurchaseInvoice> purchaseInvoiceList = new ArrayList<TPurchaseInvoice>();
		List<Attachment> attachments;
		int rowNo = 0;
		BulkOperations ops = this.mongoTemplate.bulkOps(BulkMode.UNORDERED, TStock.class);
		BulkOperations ops1 = this.mongoTemplate.bulkOps(BulkMode.UNORDERED, TPurchaseInvoice.class);
		try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			// skip header row
			rowIterator.next();
			while (rowIterator.hasNext()) {
				TPurchaseInvoice purchaseInvoice = new TPurchaseInvoice();
				TStock stock = new TStock();

				Row row = rowIterator.next();
				if (row.getRowNum() != 1) {
					String purType = ExcelUtil.getCellValue(row, 2, String.class);
					if (purType.equalsIgnoreCase("update")) {
						String chasisNo = row.getCell(1).toString().equalsIgnoreCase("null")
								|| row.getCell(1).getCellTypeEnum() == CellType.BLANK ? ""
										: ExcelUtil.getCellValue(row, 1, String.class);
						System.out.println(row.getRowNum() + " - chassisNo " + chasisNo);
						String modelType = row.getCell(4).toString().equalsIgnoreCase("null")
								|| row.getCell(4).getCellTypeEnum() == CellType.BLANK ? ""
										: ExcelUtil.getCellValue(row, 4, String.class);
						String category = row.getCell(5).toString().equalsIgnoreCase("null")
								|| row.getCell(5).getCellTypeEnum() == CellType.BLANK ? ""
										: ExcelUtil.getCellValue(row, 5, String.class);
						String maker = row.getCell(6).toString().equalsIgnoreCase("null")
								|| row.getCell(6).getCellTypeEnum() == CellType.BLANK ? ""
										: ExcelUtil.getCellValue(row, 6, String.class);
						String model = row.getCell(7).toString().equalsIgnoreCase("null")
								|| row.getCell(7).getCellTypeEnum() == CellType.BLANK ? ""
										: ExcelUtil.getCellValue(row, 7, String.class);
//					String transporter = ExcelUtil.getCellValue(row, 8, String.class);
//					String locationFrom = ExcelUtil.getCellValue(row, 9, String.class);
//					String transportTo = ExcelUtil.getCellValue(row, 10, String.class);
//					double transportCharge = ExcelUtil.getCellValue(row, 11, Double.class);
						String company = row.getCell(15).toString().equalsIgnoreCase("null")
								|| row.getCell(15).getCellTypeEnum() == CellType.BLANK ? ""
										: ExcelUtil.getCellValue(row, 15, String.class);
						System.out.println(company);

						String auctionHouse = null;
						if (purType.equalsIgnoreCase("update")) {
							auctionHouse = row.getCell(16).toString().equalsIgnoreCase("null")
									|| row.getCell(16).getCellTypeEnum() == CellType.BLANK ? ""
											: ExcelUtil.getCellValue(row, 16, String.class);
						}
						String fuel = row.getCell(19).toString().equalsIgnoreCase("null")
								|| row.getCell(19).getCellTypeEnum() == CellType.BLANK ? ""
										: ExcelUtil.getCellValue(row, 19, String.class);
						String transmission = row.getCell(20).toString().equalsIgnoreCase("null")
								|| row.getCell(20).getCellTypeEnum() == CellType.BLANK ? ""
										: ExcelUtil.getCellValue(row, 20, String.class);
						String oldNumberPlate = row.getCell(24).toString().equalsIgnoreCase("null")
								|| row.getCell(24).getCellTypeEnum() == CellType.BLANK ? ""
										: ExcelUtil.getCellValue(row, 24, String.class);
						Double shuppinNo = Double
								.parseDouble(row.getCell(30).toString().replaceAll("-", "").replaceAll("[a-zA-Z]", ""));
						double purchaseCost = row.getCell(31).toString().equalsIgnoreCase("null")
								|| row.getCell(31).getCellTypeEnum() == CellType.BLANK ? 0.0
										: ExcelUtil.getCellValue(row, 31, Double.class);
						double commissionCost = row.getCell(32).toString().equalsIgnoreCase("null")
								|| row.getCell(32).getCellTypeEnum() == CellType.BLANK ? 0.0
										: ExcelUtil.getCellValue(row, 32, Double.class);
						Date purchaseDate = row.getCell(54).toString().equalsIgnoreCase("null")
								|| row.getCell(54).getCellTypeEnum() == CellType.BLANK ? null
										: ExcelUtil.getCellValue(row, 54, Date.class);
						double purchaseCostTax = row.getCell(48).toString().equalsIgnoreCase("null")
								|| row.getCell(48).getCellTypeEnum() == CellType.BLANK ? 0.0
										: ExcelUtil.getCellValue(row, 48, Double.class);
						double commCostTax = purchaseCost * 0.08;
						double roadTax = row.getCell(44).toString().equalsIgnoreCase("null")
								|| row.getCell(44).getCellTypeEnum() == CellType.BLANK ? 0.0
										: ExcelUtil.getCellValue(row, 44, Double.class);
						double purchaseTax = purchaseCost / purchaseCostTax;
						double commTax = 8.0;
						double recycle = row.getCell(38).toString().equalsIgnoreCase("null")
								|| row.getCell(38).getCellTypeEnum() == CellType.BLANK ? 0.0
										: ExcelUtil.getCellValue(row, 38, Double.class);
						double otherCharges = row.getCell(33).toString().equalsIgnoreCase("null")
								|| row.getCell(33).getCellTypeEnum() == CellType.BLANK ? 0.0
										: ExcelUtil.getCellValue(row, 33, Double.class);

						// EQUIPMENTS
						String ac = ExcelUtil.getCellValue(row, 99, String.class).equalsIgnoreCase("-") ? ""
								: Constants.EQUIP_AC;
						String ps = ExcelUtil.getCellValue(row, 100, String.class).equalsIgnoreCase("-") ? ""
								: Constants.EQUIP_PS;
						String pw = ExcelUtil.getCellValue(row, 101, String.class).equalsIgnoreCase("-") ? ""
								: Constants.EQUIP_PW;
						String sr = ExcelUtil.getCellValue(row, 102, String.class).equalsIgnoreCase("-") ? ""
								: Constants.EQUIP_SR;
						String aw = ExcelUtil.getCellValue(row, 103, String.class).equalsIgnoreCase("-") ? ""
								: Constants.EQUIP_AW;
						String abs = ExcelUtil.getCellValue(row, 104, String.class).equalsIgnoreCase("-") ? ""
								: Constants.EQUIP_ABS;
						String airbag = ExcelUtil.getCellValue(row, 105, String.class).equalsIgnoreCase("-") ? ""
								: Constants.EQUIP_AIRBAG;
						String fourwd = ExcelUtil.getCellValue(row, 106, String.class).equalsIgnoreCase("-") ? ""
								: Constants.EQUIP_4WD;
						String pm = ExcelUtil.getCellValue(row, 107, String.class).equalsIgnoreCase("-") ? ""
								: Constants.EQUIP_PM;
						String tv = ExcelUtil.getCellValue(row, 108, String.class).equalsIgnoreCase("-") ? ""
								: Constants.EQUIP_TV;
						String cd = ExcelUtil.getCellValue(row, 109, String.class).equalsIgnoreCase("-") ? ""
								: Constants.EQUIP_CD;
						String nv = ExcelUtil.getCellValue(row, 110, String.class).equalsIgnoreCase("-") ? ""
								: Constants.EQUIP_NV;
						String rs = ExcelUtil.getCellValue(row, 111, String.class).equalsIgnoreCase("-") ? ""
								: Constants.EQUIP_RS;
						String flamp = ExcelUtil.getCellValue(row, 112, String.class).equalsIgnoreCase("-") ? ""
								: Constants.EQUIP_FLAMP;
						List<String> equipments = new ArrayList<>();
						if (!StringUtils.isEmpty(ac))
							equipments.add(ac);
						if (!StringUtils.isEmpty(ps))
							equipments.add(ps);
						if (!StringUtils.isEmpty(pw))
							equipments.add(pw);
						if (!StringUtils.isEmpty(sr))
							equipments.add(sr);
						if (!StringUtils.isEmpty(aw))
							equipments.add(aw);
						if (!StringUtils.isEmpty(abs))
							equipments.add(abs);
						if (!StringUtils.isEmpty(airbag))
							equipments.add(airbag);
						if (!StringUtils.isEmpty(fourwd))
							equipments.add(fourwd);
						if (!StringUtils.isEmpty(pm))
							equipments.add(pm);
						if (!StringUtils.isEmpty(tv))
							equipments.add(tv);
						if (!StringUtils.isEmpty(cd))
							equipments.add(cd);
						if (!StringUtils.isEmpty(nv))
							equipments.add(nv);
						if (!StringUtils.isEmpty(rs))
							equipments.add(rs);
						if (!StringUtils.isEmpty(flamp))
							equipments.add(flamp);

						// EQUIPMENTS END

						String destCountry = row.getCell(18).toString().equalsIgnoreCase("null")
								|| row.getCell(18).getCellTypeEnum() == CellType.BLANK ? ""
										: ExcelUtil.getCellValue(row, 18, String.class);
						String destPort = row.getCell(51).toString().equalsIgnoreCase("null")
								|| row.getCell(51).getCellTypeEnum() == CellType.BLANK ? ""
										: ExcelUtil.getCellValue(row, 51, String.class);
						String addOption = row.getCell(94).toString().equalsIgnoreCase("null")
								|| row.getCell(94).getCellTypeEnum() == CellType.BLANK ? ""
										: ExcelUtil.getCellValue(row, 94, String.class);

						String freg = row.getCell(95).toString().equalsIgnoreCase("null")
								|| row.getCell(95).getCellTypeEnum() == CellType.BLANK ? ""
										: ExcelUtil.getCellValue(row, 95, String.class);
						Integer door = ExcelUtil.getCellValue(row, 96, Integer.class);
						String seat = row.getCell(97).toString().equalsIgnoreCase("null")
								|| row.getCell(97).getCellTypeEnum() == CellType.BLANK ? ""
										: ExcelUtil.getCellValue(row, 97, String.class);
						String recycleFlag = row.getCell(114).toString().equalsIgnoreCase("null")
								|| row.getCell(114).getCellTypeEnum() == CellType.BLANK ? ""
										: ExcelUtil.getCellValue(row, 114, String.class);
						String numPlate = row.getCell(115).toString().equalsIgnoreCase("null")
								|| row.getCell(115).getCellTypeEnum() == CellType.BLANK ? ""
										: ExcelUtil.getCellValue(row, 115, String.class);
						String driven = row.getCell(116).toString().equalsIgnoreCase("null")
								|| row.getCell(116).getCellTypeEnum() == CellType.BLANK ? ""
										: ExcelUtil.getCellValue(row, 116, String.class);
						String origin = row.getCell(118).toString().equalsIgnoreCase("null")
								|| row.getCell(118).getCellTypeEnum() == CellType.BLANK ? ""
										: ExcelUtil.getCellValue(row, 118, String.class);
						String grade = row.getCell(123).toString().equalsIgnoreCase("null")
								|| row.getCell(123).getCellTypeEnum() == CellType.BLANK ? ""
										: ExcelUtil.getCellValue(row, 123, String.class);
						Double cc = row.getCell(128).toString().equalsIgnoreCase("null")
								|| row.getCell(128).getCellTypeEnum() == CellType.BLANK ? 0.0
										: ExcelUtil.getCellValue(row, 128, Double.class);
						Long mileage = ExcelUtil.getCellValue(row, 139, Long.class);
						String color = row.getCell(140).toString().equalsIgnoreCase("null")
								|| row.getCell(140).getCellTypeEnum() == CellType.BLANK ? ""
										: ExcelUtil.getCellValue(row, 140, String.class);
						String remarks = row.getCell(142).toString().equalsIgnoreCase("null")
								|| row.getCell(142).getCellTypeEnum() == CellType.BLANK ? ""
										: ExcelUtil.getCellValue(row, 142, String.class);
						String stkType = row.getCell(169).toString().equalsIgnoreCase("null")
								|| row.getCell(169).getCellTypeEnum() == CellType.BLANK ? ""
										: ExcelUtil.getCellValue(row, 169, String.class);

						stock.setEquipment(equipments);
						stock.setDestinationCountry(destCountry);
						stock.setDestinationPort(destPort);
						stock.setOptionDescription(addOption);
						stock.setsFirstRegDate(freg);
						stock.setNoOfDoors(door);
						stock.setNoOfSeat(seat);
						stock.setRecycle(recycleFlag);
						stock.setNumberPlate(numPlate);
						if (numPlate == "Yes") {
							stock.setOldNumberPlate(oldNumberPlate);
						} else {
							stock.setOldNumberPlate("");
						}
						stock.setDriven(driven);
						stock.setGrade(grade);
						stock.setCc(cc);
						stock.setMileage(mileage);
						stock.setColor(color);
						stock.setRemarks(remarks);
						stock.setIsBidding(StringUtils.equalsIgnoreCase(stkType, "STOCK") ? 0 : 1);
						stock.setOrgin(StringUtils.equalsIgnoreCase(origin, "JAPAN") ? origin : "FOREIGN COUNTRY");
						stock.setFuel(fuel);
						stock.setTransmission(transmission);

						if (!AppUtil.isObjectEmpty(chasisNo) && !AppUtil.isObjectEmpty(modelType)
								&& !AppUtil.isObjectEmpty(maker) && !AppUtil.isObjectEmpty(model)
								&& !AppUtil.isObjectEmpty(shuppinNo)) {
							stock.setChassisNo(AppUtil.isObjectEmpty(chasisNo) ? "" : chasisNo);
							stock.setMaker(maker);
							stock.setModel(model);
							stock.setModelType(modelType);
							stock.setCategory(category);

							/*
							 * Model dataModel = masterVechicleMakerRepository.getModelDataName(maker,
							 * model, category); System.out.println("maker" + maker + " model " + model +
							 * " category " + category); stock.setModelId(dataModel.getModelId());
							 */

							stock.setStockNo(sequenceService.getNextSequence(Constants.SEQUENCE_KEY_OLD_STOCK));
							AuctionInfo aucInfo = new AuctionInfo();
							aucInfo.setLotNo(Math.round(shuppinNo));
							PurchaseInfo purInfo = new PurchaseInfo();
							purInfo.setType(
									purType.equalsIgnoreCase("update") ? Constants.ACCOUNTS_AUCTION_PAYMENT : "local");
							purInfo.setAuctionInfo(aucInfo);
							purInfo.setDate(purchaseDate);
//						TransportInfo transportInfo = new TransportInfo();
//						transportInfo.setTransporter(transporter);
//						transportInfo.setPickupLocation(locationFrom);
//						transportInfo.setDropLocation(transportTo);
//						transportInfo.setCharge(transportCharge);
//						stock.setTransportInfo(transportInfo);
							stock.setPurchaseInfo(purInfo);
							MSupplier msup = mSupplierRepo.findOneByCompany(company);
							System.out.println(msup.getSupplierCode());
							purInfo.setSupplier(msup.getSupplierCode());
							List<SupplierLocation> supplierLocations = msup.getSupplierLocations();
							if (purType.equalsIgnoreCase("update")) {
								for (SupplierLocation supplierLocation : supplierLocations) {
									if (supplierLocation.getAuctionHouse().equalsIgnoreCase(auctionHouse)) {
										aucInfo.setAuctionHouse(new ObjectId(supplierLocation.getId()));
									}

								}
							}
							final PurchaseInfo purchaseInfo = stock.getPurchaseInfo();
							final AuctionInfo auctionInfo = purchaseInfo.getAuctionInfo();

							// create purchase invoice
							purchaseInvoice.setStockNo(stock.getStockNo());
							purchaseInvoice.setChassisNo(stock.getChassisNo());
							purchaseInvoice
									.setCode(sequenceService.getNextSequence(Constants.SEQUENCE_KEY_OLD_PRCHSINVC));
							purchaseInvoice.setStatus(Constants.INV_STATUS_NEW);
							purchaseInvoice.setType(Constants.PURCHASE_INVOICE_ITEM_TYPE_PURCHASE);
							purchaseInvoice.setCarTaxClaimStatus(Constants.TPURCHASEINVOICE_CARTAX_NOT_CLAIMED);
							purchaseInvoice.setRecycleClaimStatus(Constants.TPURCHASEINVOICE_RECYCLE_NOT_CLAIMED);
							purchaseInvoice
									.setPurchaseTaxClaimStatus(Constants.TPURCHASEINVOICE_PURCHASETAX_NOT_CLAIMED);
							purchaseInvoice
									.setCommisionTaxClaimStatus(Constants.TPURCHASEINVOICE_COMMISSIONTAX_NOT_CLAIMED);
							purchaseInvoice.setPaymentApprove(Constants.PAYMENT_NOT_APPROVED);
							purchaseInvoice.setSupplierId(purchaseInfo.getSupplier());
							purchaseInvoice.setAuctionHouseId(auctionInfo.getAuctionHouse());
							purchaseInvoice.setInvoiceDate(purchaseInfo.getDate());
							stock.setPurchaseInvoiceCode(purchaseInvoice.getCode());
							purchaseInvoice.setPurchaseType(purchaseInfo.getType());
							purchaseInvoice.setPurchaseCost(purchaseCost);
							purchaseInvoice.setCommision(commissionCost);
							purchaseInvoice.setCommisionTaxAmount(commCostTax);
							purchaseInvoice.setCommisionTax(commTax);
							purchaseInvoice.setPurchaseCostTax(purchaseTax);
							purchaseInvoice.setPurchaseCostTaxAmount(purchaseCostTax);
							purchaseInvoice.setRecycle(recycle);
							purchaseInvoice.setRoadTax(roadTax);
							purchaseInvoice.setOtherCharges(otherCharges);
							purchaseInvoice.setPurchaseCostFlag(Constants.FLAG_NO);
							purchaseInvoice.setCommissionFlag(Constants.FLAG_NO);
							purchaseInvoice.setRoadTaxFlag(Constants.FLAG_NO);
							purchaseInvoice.setRecycleFlag(Constants.FLAG_NO);
							// save stock
							attachments = new ArrayList<>();
							stock.setAttachments(attachments);
							TStock duplicateEntryCheck = stockRepository.findOneByChassisNo(stock.getChassisNo());
							if (AppUtil.isObjectEmpty(duplicateEntryCheck)) {
								tskList.add(stock);
								purchaseInvoiceList.add(purchaseInvoice);
								// stockRepository.saveAll(tskList);
								// tPurchaseInvoiceRepository.saveAll(purchaseInvoiceList);
								System.out.println("stock Count " + tskList.size() + " purchasecount "
										+ purchaseInvoiceList.size());
							}

							if (row.getRowNum() == sheet.getPhysicalNumberOfRows()) {
								ops.insert(tskList);
								ops1.insert(purchaseInvoiceList);
								performUpdate = true;
							}

							// save purchase invoice

							// purInfo.setAuctionInfo(auctionInfo);
							// set claim status
							System.out.println(
									stock.getStockNo() + " stockno - purchase code " + purchaseInvoice.getCode());

							rowNo += 1;
						}
					}

				}
			}
		}
		if (performUpdate) {
			ops.execute();
			ops1.execute();
		}

		return performUpdate ? tskList.size() : null;
	}

}
