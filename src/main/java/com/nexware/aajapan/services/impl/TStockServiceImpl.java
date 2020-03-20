package com.nexware.aajapan.services.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.TShippingRequestEtaEtd;
import com.nexware.aajapan.dto.TStockEditHistoryDto;
import com.nexware.aajapan.exceptions.AAJRuntimeException;
import com.nexware.aajapan.form.StockForm;
import com.nexware.aajapan.models.Attachment;
import com.nexware.aajapan.models.AuctionInfo;
import com.nexware.aajapan.models.MCountryPort;
import com.nexware.aajapan.models.MLocation;
import com.nexware.aajapan.models.MLogin;
import com.nexware.aajapan.models.MSupplier;
import com.nexware.aajapan.models.MUser;
import com.nexware.aajapan.models.Model;
import com.nexware.aajapan.models.PurchaseInfo;
import com.nexware.aajapan.models.ReservedInfo;
import com.nexware.aajapan.models.TCustomer;
import com.nexware.aajapan.models.TPurchaseInvoice;
import com.nexware.aajapan.models.TReAuction;
import com.nexware.aajapan.models.TStock;
import com.nexware.aajapan.models.TransportInfo;
import com.nexware.aajapan.property.FileStorageProperties;
import com.nexware.aajapan.repositories.LocationRepository;
import com.nexware.aajapan.repositories.LoginRepository;
import com.nexware.aajapan.repositories.MasterCountryPortRepository;
import com.nexware.aajapan.repositories.MasterSupplierRepository;
import com.nexware.aajapan.repositories.StockRepository;
import com.nexware.aajapan.repositories.TCustomerRepository;
import com.nexware.aajapan.repositories.TPurchaseInvoiceRepository;
import com.nexware.aajapan.repositories.TReAuctionRepository;
import com.nexware.aajapan.repositories.TShippingRequestRepository;
import com.nexware.aajapan.repositories.TStockEditHistoryRepository;
import com.nexware.aajapan.repositories.UserRepository;
import com.nexware.aajapan.services.MVechicleMakerService;
import com.nexware.aajapan.services.SequenceService;
import com.nexware.aajapan.services.TCustomerService;
import com.nexware.aajapan.services.TCustomerTransactionService;
import com.nexware.aajapan.services.TStockEditHistoryService;
import com.nexware.aajapan.services.TStockModelTypeService;
import com.nexware.aajapan.services.TStockService;
import com.nexware.aajapan.services.YearOfManufactureService;
import com.nexware.aajapan.utils.AppUtil;

@Service
@Transactional
public class TStockServiceImpl implements TStockService {
	@Autowired
	private StockRepository stockRepository;
	@Autowired
	private TShippingRequestRepository shippingRequestRepository;
	@Autowired
	private TReAuctionRepository reAuctionRepository;
	@Autowired
	private LocationRepository locationRepository;
	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private FileStorageProperties fileStorageProperties;
	@Autowired
	private TPurchaseInvoiceRepository tPurchaseInvoiceRepository;
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private YearOfManufactureService yearOfManufactureService;
	@Autowired
	private MVechicleMakerService makerModelService;
	@Autowired
	private TCustomerRepository customerRepository;
	@Autowired
	private TCustomerTransactionService customerTransactionService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private LoginRepository loginRepository;
	@Autowired
	private TCustomerService customerService;
	@Autowired
	private TStockModelTypeService stockModelTypeService;
	@Autowired
	private MasterCountryPortRepository countryPortRepository;
	@Autowired
	private TStockEditHistoryService stockEditHistoryService;
	@Autowired
	private TStockEditHistoryRepository stockEditHistoryRepository;
	@Autowired
	private MasterSupplierRepository supplierRepository;

	@Override
	public Integer getStockInventoryStatus(String stockNo) {
		final TStock stock = stockRepository.findOneByStockNo(stockNo);
		boolean shippingArranged = false;
		boolean salesOrderCreated = false;
		if ((stock.getShippingStatus() != Constants.STOCK_SHIPPING_STATUS_IDLE)
				&& !AppUtil.isObjectEmpty(stock.getShipmentRequestId())) {
			shippingArranged = true;
		}
		if (stock.getInventoryStatus() == Constants.STOCK_INVENTORY_STATUS_STAGING) {
			salesOrderCreated = true;
		}
		if ((stock.getInventoryStatus() == Constants.STOCK_INVENTORY_STATUS_GENERAL) && !shippingArranged) {
			return Constants.STOCK_INVENTORY_STATUS_GENERAL;
		} else if (salesOrderCreated && !shippingArranged) {
			return Constants.STOCK_INVENTORY_STATUS_STAGING;
		} else if (shippingArranged) {

			final TShippingRequestEtaEtd etaEtd = shippingRequestRepository
					.findEtaEtdByShipmentRequestId(stock.getShipmentRequestId());
			if (AppUtil.isObjectEmpty(etaEtd)) {
				return null;
			}
			final Date currentDate = AppUtil.atStartOfDay(new Date());
			final Date etd = AppUtil.isObjectEmpty(etaEtd.getEtd()) ? null : AppUtil.atStartOfDay(etaEtd.getEtd());
			final Date eta = AppUtil.isObjectEmpty(etaEtd.getEta()) ? null : AppUtil.atStartOfDay(etaEtd.getEta());
			if (AppUtil.isObjectEmpty(eta) || AppUtil.isObjectEmpty(etd)) {
				return null;
			} else if (currentDate.equals(eta) || currentDate.after(eta)) {
				return null;
			} else if ((currentDate.equals(etd) || currentDate.after(etd)) && salesOrderCreated) {
				return Constants.STOCK_INVENTORY_STATUS_IN_TRANSIT;
			} else if ((currentDate.equals(etd) || currentDate.after(etd)) && !salesOrderCreated) {
				return Constants.STOCK_INVENTORY_STATUS_IN_CONSIGNMENT;
			} else if (currentDate.before(etd) && !salesOrderCreated) {
				return Constants.STOCK_INVENTORY_STATUS_GENERAL;
			} else if (currentDate.before(etd) && salesOrderCreated) {
				return Constants.STOCK_INVENTORY_STATUS_STAGING;
			}
		}
		return null;
	}

	@Override
	public void saveReAuctionAndChangeStockStatus(List<TReAuction> reauction) {
		final List<String> stockNos = reauction.stream().map(TReAuction::getStockNo).collect(Collectors.toList());
		final List<TStock> stockList = stockRepository.findAllByStockNoIn(stockNos);
		MUser user = userRepository.findOneByCode(Constants.DEFAULT_RESERVE_SALESPERSON_ID);
		stockList.stream().forEach(stock -> {
			final ReservedInfo reservedInfo = new ReservedInfo(null, Constants.DEFAULT_RESERVE_SALESPERSON_ID,
					user.getFullname(), null, null, new Date());
			stock.setReservedInfo(reservedInfo);
			stock.setReserve(Constants.RESERVED);
			stock.setIsLocked(Constants.IS_NOT_LOCKED);
			stock.setLockedBy("");
			stock.setStatus(Constants.STOCK_STATUS_RE_AUCTION);
		});
		stockRepository.saveAll(stockList);
		final List<TReAuction> auctionList = new ArrayList<>();
		reauction.stream().forEach(r -> {
			r.setStatus(Constants.REAUCTION_STATUS_INITIATED);
			auctionList.add(r);
		});
		reAuctionRepository.saveAll(auctionList);
	}

	@Override
	public void createStock(StockForm stockForm, String attachmentTempDirectory) throws IOException {
		final TStock stock = stockForm.getStock();
		final TPurchaseInvoice purchaseInvoice = stockForm.getPurchaseInvoice();
		List<Attachment> attachments;
		attachments = stock.getAttachments();
		if (attachments != null) {
			attachments = attachments.stream().filter(attachment -> attachment.getFilename() != null)
					.collect(Collectors.toList());
		} else {
			attachments = new ArrayList<>();
		}
		final TransportInfo transportInfo = stock.getTransportInfo();
		transportInfo.setStatus(Constants.TRANSPORT_ITEM_NEW);
		if (!AppUtil.isObjectEmpty(stock.getReservedInfo().getSalesPersonId())) {
//			MLogin salesPerson = loginRepository.findOneByUserId(stock.getReservedInfo().getSalesPersonId());
//			stock.getReservedInfo().setSelesPersonName(salesPerson.getUsername());
			stock.setReserve(Constants.RESERVED);

		} else {
			stock.setReserve(Constants.NOT_RESERVED);
		}
		if (AppUtil.isObjectEmpty(transportInfo.getPickupLocation())) {
			final MLocation mLocation = locationRepository.findOneBySupplierCodeAndAuctionHouseId(
					stock.getPurchaseInfo().getSupplier(), stock.getPurchaseInfo().getAuctionInfo().getAuctionHouse());
			if (stock.getPurchaseInfo().getType().equalsIgnoreCase("auction")) {
				stock.setLastTransportLocation(mLocation.getCode());
			}

		} else if (!AppUtil.isObjectEmpty(transportInfo.getPickupLocation())
				&& !transportInfo.getPickupLocation().equalsIgnoreCase("Others")) {
			stock.setLastTransportLocation(transportInfo.getPickupLocation());
		} else if (!AppUtil.isObjectEmpty(transportInfo.getPickupLocation())
				&& transportInfo.getPickupLocation().equalsIgnoreCase("Others")) {
			stock.setLastTransportLocation("others");
			stock.setLastTransportLocationCustom(transportInfo.getPickupLocationCustom());
		}

		stock.setId(null);
		stock.setStockNo(sequenceService.getNextSequence(Constants.SEQUENCE_KEY_STOCK));
		stock.setStatus(Constants.STOCK_STATUS_NEW);
		stock.setInventoryStatus(Constants.STOCK_INVENTORY_STATUS_INITIAL);
		stock.setShowForSales(Constants.SHOW_FOR_SALES);
		stock.setTransportationStatus(Constants.TRANSPORT_IDLE);
		stock.setInspectionStatus(Constants.STOCK_AVAILABLE_FOR_INSPECTION);
		stock.setTransportInfo(transportInfo);
		stock.setShippingInstructionStatus(Constants.STOCK_SHIPPING_INSTRUCTION_STATUS_IDLE);
		stock.setShippingStatus(Constants.STOCK_SHIPPING_STATUS_IDLE);
		stock.setLcStatus(Constants.STOCK_LC_NOT_APPLIED);
		stock.setTransportationCount(0);
		stock.setDocumentStatus(Constants.STOCK_DOCUMENT_NOT_RECEIVED);
		stock.setRikujiStatus(Constants.STOCK_RIKUJI_STATUS_0);
		if (!AppUtil.isObjectEmpty(stock.getDestinationCountry())) {
			MCountryPort countryPort = this.countryPortRepository.findOneByCountry(stock.getDestinationCountry());
			stock.setInspectionFlag(countryPort.getInspectionFlag());
		}

		// move files to upload directory
		final Path tempPath = Paths
				.get(fileStorageProperties.getTempDirectory() + File.separator + attachmentTempDirectory)
				.toAbsolutePath().normalize();

		if ((tempPath.toFile().listFiles() != null) && (tempPath.toFile().listFiles().length > 0)) {
			final Path uploadPath = Paths
					.get(fileStorageProperties.getDirectory() + File.separator + stock.getStockNo()).toAbsolutePath()
					.normalize();
			FileUtils.moveDirectory(tempPath.toFile(), uploadPath.toFile());
		}
		attachments.forEach(attachment -> attachment.setDiskDirectory(stock.getStockNo()));

		final Date manufactureYear = yearOfManufactureService.yearOfManufacture(stock.getChassisNo(),
				stock.getModelType());
		if (!AppUtil.isObjectEmpty(manufactureYear)) {
			stock.setManufactureYear(manufactureYear);
		}

		stock.setAttachments(attachments);
		// set maker model details to stock
		final Model model = makerModelService.getModelData(stock.getMaker(), stock.getModel());
		stock.setModelId(model.getModelId());
		stock.setModel(model.getModelName());
		stock.setLength(model.getLength());
		stock.setWidth(model.getWidth());
		stock.setHeight(model.getHeight());
		stock.calcM3();
		stock.setTransportCategory(model.getTransportCategory());

		if (stock.getPurchaseInfo().getType().equalsIgnoreCase("local")
				|| stock.getPurchaseInfo().getType().equalsIgnoreCase("overseas")) {
			stock.getPurchaseInfo().getAuctionInfo().setPosNo(stock.getPurchaseInfo().getSupplier());
		}

		// prchs variable declaration
		final PurchaseInfo purchaseInfo = stock.getPurchaseInfo();
		final AuctionInfo auctionInfo = purchaseInfo.getAuctionInfo();
		// create purchase invoice
		purchaseInvoice.setStockNo(stock.getStockNo());
		purchaseInvoice.setChassisNo(stock.getChassisNo());
		purchaseInvoice.setCode(sequenceService.getNextSequence(Constants.SEQUENCE_KEY_PRCHSINVC));
		purchaseInvoice.setStatus(Constants.INV_STATUS_NEW);
		purchaseInvoice.setType(Constants.PURCHASE_INVOICE_ITEM_TYPE_PURCHASE);
		purchaseInvoice.setCarTaxClaimStatus(Constants.TPURCHASEINVOICE_CARTAX_NOT_CLAIMED);
		purchaseInvoice.setRecycleClaimStatus(Constants.TPURCHASEINVOICE_RECYCLE_NOT_CLAIMED);
		purchaseInvoice.setPurchaseTaxClaimStatus(Constants.TPURCHASEINVOICE_PURCHASETAX_NOT_CLAIMED);
		purchaseInvoice.setCommisionTaxClaimStatus(Constants.TPURCHASEINVOICE_COMMISSIONTAX_NOT_CLAIMED);
		purchaseInvoice.setPaymentApprove(Constants.PAYMENT_NOT_APPROVED);
		purchaseInvoice.setSupplierId(purchaseInfo.getSupplier());
		purchaseInvoice.setAuctionHouseId(auctionInfo.getAuctionHouse());
		purchaseInvoice.setInvoiceDate(purchaseInfo.getDate());
		stock.setPurchaseInvoiceCode(purchaseInvoice.getCode());
		purchaseInvoice.setPurchaseType(purchaseInfo.getType());

		// save stock
		stockRepository.save(stock);
		// save purchase invoice
		tPurchaseInvoiceRepository.save(purchaseInvoice);
		// save stock model Type
		if (!AppUtil.isObjectEmpty(stock.getModelType()) && stock.getModelType().contains("-")) {
			stockModelTypeService.saveStockModelType(stock);
		}

	}

	// for editing existing stock set created date and created by as same
	@Override
	public void editStock(StockForm stockForm) {
		final TStock stock = stockForm.getStock();
		final TPurchaseInvoice purchaseInvoice = stockForm.getPurchaseInvoice();
		List<Attachment> attachments;
		attachments = stock.getAttachments();
		if (attachments != null) {
			attachments = attachments.stream().filter(attachment -> attachment.getFilename() != null)
					.collect(Collectors.toList());

		} else {
			attachments = new ArrayList<>();
		}
		final TStock newStock = stockRepository.findOneById(stock.getId());
		final TStock existingStock = stockRepository.findOneById(stock.getId());
		attachments.forEach(attachment -> attachment.setDiskDirectory(newStock.getStockNo()));
		attachments.addAll(newStock.getAttachments());
		newStock.setAttachments(attachments);
		// set maker model details to stock
		final Model model = makerModelService.getModelData(stock.getMaker(), stock.getModel());
		if (AppUtil.isObjectEmpty(newStock.getOldChassisNo())) {
			newStock.setOldChassisNo(newStock.getChassisNo());
		}
		newStock.setChassisNo(stock.getChassisNo());
		newStock.setsFirstRegDate(stock.getsFirstRegDate());
		newStock.setModel(model.getModelName());
		newStock.setModelId(model.getModelId());
		newStock.setMaker(stock.getMaker());
		newStock.setHsCode(stock.getHsCode());
		newStock.setCategory(stock.getCategory());
		newStock.setSubcategory(stock.getSubcategory());
		newStock.setExtraEquipments(stock.getExtraEquipments());
		newStock.setModelType(stock.getModelType());
		newStock.setGrade(stock.getGrade());
		newStock.setTransmission(stock.getTransmission());
		newStock.setManualTypes(stock.getManualTypes());
		newStock.setAuctionGrade(stock.getAuctionGrade());
		newStock.setAuctionGradeExt(stock.getAuctionGradeExt());
		newStock.setNoOfDoors(stock.getNoOfDoors());
		newStock.setNoOfSeat(stock.getNoOfSeat());
		newStock.setFuel(stock.getFuel());
		newStock.setDriven(stock.getDriven());
		newStock.setColor(stock.getColor());
		newStock.setMileage(stock.getMileage());
		newStock.setOrgin(stock.getOrgin());
		newStock.setDestinationCountry(stock.getDestinationCountry());
		newStock.setDestinationPort(stock.getDestinationPort());
		newStock.setCc(stock.getCc());
		newStock.setRecycle(stock.getRecycle());
		newStock.setNumberPlate(stock.getNumberPlate());
		newStock.setOldNumberPlate(stock.getOldNumberPlate());
		newStock.setOptionDescription(stock.getOptionDescription());
		newStock.setRemarks(stock.getRemarks());
		newStock.setEquipment(stock.getEquipment());
		newStock.setExtraAccessories(stock.getExtraAccessories());
		newStock.setAccount(stock.getAccount());
		newStock.setIsMovable(stock.getIsMovable());
		newStock.setAttachments(attachments);
		newStock.setAuctionRemarks(stock.getAuctionRemarks());
		newStock.setShipmentType(stock.getShipmentType());
		newStock.setTransportCategory(model.getTransportCategory());
//		newStock.setLength(model.getLength());
//		newStock.setWidth(model.getWidth());
//		newStock.setHeight(model.getHeight());
//		newStock.setM3(newStock.getM3());
		newStock.setTyreSize(stock.getTyreSize());
		newStock.setCraneType(stock.getCraneType());
		newStock.setCraneCut(stock.getCraneCut());
		newStock.setExel(stock.getExel());
		newStock.setInspectionFlag(stock.getInspectionFlag());
		newStock.setTankKiloLitre(stock.getTankKiloLitre());
		newStock.setForwarder(stock.getForwarder());

		// purchase info only updated before purchase info confirm
		if (newStock.getStatus() == Constants.STOCK_STATUS_NEW) {
			newStock.setPurchaseInfo(stock.getPurchaseInfo());
		}
		if (!AppUtil.isObjectEmpty(stock.getReservedInfo().getSalesPersonId())) {
			newStock.setReserve(Constants.RESERVED);
			newStock.setIsBidding(stock.getIsBidding());
			ReservedInfo reservedInfo = new ReservedInfo();
			reservedInfo.setCustomerId(stock.getReservedInfo().getCustomerId());
			reservedInfo.setSalesPersonId(stock.getReservedInfo().getSalesPersonId());
			newStock.setReservedInfo(reservedInfo);
		} else {
			newStock.setReserve(Constants.NOT_RESERVED);
			newStock.setIsBidding(stock.getIsBidding());
			ReservedInfo reservedInfo = new ReservedInfo();
			reservedInfo.setCustomerId(null);
			reservedInfo.setSalesPersonId(null);
			newStock.setReservedInfo(reservedInfo);
		}

		if (!AppUtil.isObjectEmpty(stock.getTransportInfo().getPickupLocation())) {
			TransportInfo transportInfo = newStock.getTransportInfo();
			transportInfo.setPickupLocation(stock.getTransportInfo().getPickupLocation());
			newStock.setLastTransportLocation(stock.getTransportInfo().getPickupLocation());
			if (stock.getTransportInfo().getPickupLocation().equalsIgnoreCase("others")) {
				transportInfo.setPickupLocationCustom(stock.getTransportInfo().getPickupLocationCustom());
				newStock.setLastTransportLocationCustom(stock.getTransportInfo().getPickupLocationCustom());
			}
			newStock.setTransportInfo(transportInfo);

		}

		// save updated stock
		stockRepository.save(newStock);

		// compare stock before save
		try {
			stockEditHistoryService.compareStockObject(existingStock, newStock);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}

		// edit invoice info
		final List<TPurchaseInvoice> purchaseInvoiceExisting = tPurchaseInvoiceRepository
				.findAllByStockNoAndType(newStock.getStockNo(), Constants.PURCHASE_INVOICE_ITEM_TYPE_PURCHASE);
		if (!AppUtil.isObjectEmpty(purchaseInvoiceExisting)) {
			purchaseInvoiceExisting.stream().forEach(invoice -> {
				invoice.setChassisNo(stock.getChassisNo());
				invoice.setPurchaseType(stock.getPurchaseInfo().getType());
				if (newStock.getStatus().equals(Constants.STOCK_STATUS_NEW)) {
					final PurchaseInfo purchaseInfo = stock.getPurchaseInfo();
					final AuctionInfo auctionInfo = purchaseInfo.getAuctionInfo();
					invoice.setSupplierId(purchaseInfo.getSupplier());
					invoice.setAuctionHouseId(auctionInfo.getAuctionHouse());
					invoice.setInvoiceDate(purchaseInfo.getDate());
				}
			});

		} else {
			throw new AAJRuntimeException("purchase invoice not found for stock::" + stock.getStockNo());
		}
//		 save purchase invoice
		tPurchaseInvoiceRepository.saveAll(purchaseInvoiceExisting);

	}

	@Override
	public void reserveStock(String stockNo, String custId, String userId, Double price) {
		final TCustomer customer = customerRepository.findOneByCode(custId);
		final boolean isValidAmount = customerService.checkReserveAmountIsValid(customer, price);
		if (!isValidAmount) {
			throw new AAJRuntimeException("Reserved price is not in credit range.");
		}

		final MUser user = userRepository.findOneByCode(userId);
		final MLogin login = loginRepository.findOneByUserId(user.getCode());
		customerTransactionService.updateCustomerBalance(customer.getCode(), Constants.TRANSACTION_DEBIT, price);
		final TStock stock = stockRepository.findOneByStockNo(stockNo);
		final ReservedInfo reservedInfo = new ReservedInfo(custId, user.getCode(), login.getUsername(), price,
				customer.getCurrencyType(), new Date());
		stock.setReservedInfo(reservedInfo);
		stock.setReserve(Constants.RESERVED);
		stock.setIsLocked(Constants.IS_NOT_LOCKED);
		stock.setLockedBy("");
		stockRepository.save(stock);
	}

	@Override
	public List<TStockEditHistoryDto> findAllByStockNo(String stockNo) {
		List<TStockEditHistoryDto> stockEditHistoryList = stockEditHistoryRepository.findAllByStockNo(stockNo);
		stockEditHistoryList.forEach(stock -> {
			if (stock.getColumnName().equalsIgnoreCase("supplier")) {
				MSupplier originalSupplier = this.supplierRepository.findOneBySupplierCode(stock.getOriginalValue());
				MSupplier newSupplier = this.supplierRepository.findOneBySupplierCode(stock.getNewValue());
				stock.setOriginalValue(originalSupplier.getCompany());
				stock.setNewValue(newSupplier.getCompany());
			}
			if (stock.getColumnName().equalsIgnoreCase("auctionHouse")) {
				List<MSupplier> originalSupplier = this.supplierRepository.getListWithoutDeletedSuppliers();
				for (MSupplier supplier : originalSupplier) {
					supplier.getSupplierLocations().forEach(location -> {
						if (stock.getOriginalValue().equals(location.getId())) {
							stock.setOriginalValue(location.getAuctionHouse());
						}
						if (stock.getNewValue().equals(location.getId())) {
							stock.setNewValue(location.getAuctionHouse());
						}
					});
				}

			}

			if (stock.getColumnName().equalsIgnoreCase("reservedCustomer")) {
				TCustomer originalCustomer = this.customerRepository.findOneByCode(stock.getOriginalValue());
				TCustomer newCustomer = this.customerRepository.findOneByCode(stock.getNewValue());
				stock.setOriginalValue(originalCustomer.getFirstName());
				stock.setNewValue(newCustomer.getFirstName());
			}

			if (stock.getColumnName().equalsIgnoreCase("shippingCustomer")) {
				TCustomer originalCustomer = this.customerRepository.findOneByCode(stock.getOriginalValue());
				TCustomer newCustomer = this.customerRepository.findOneByCode(stock.getNewValue());
				stock.setOriginalValue(originalCustomer.getFirstName());
				stock.setNewValue(newCustomer.getFirstName());
			}
			if (stock.getColumnName().equalsIgnoreCase("shippingSalesPerson")) {
				MLogin originalUser = this.loginRepository.findOneByUserId(stock.getOriginalValue());
				MLogin newUser = this.loginRepository.findOneByUserId(stock.getNewValue());
				stock.setOriginalValue(originalUser.getUsername());
				stock.setNewValue(newUser.getUsername());
			}
			if (stock.getColumnName().equalsIgnoreCase("pickupLocation")) {
				if (!stock.getOriginalValue().equalsIgnoreCase("others")) {
					MLocation originalLocation = this.locationRepository.findOneByCode(stock.getOriginalValue());
					stock.setOriginalValue(originalLocation.getDisplayName());
				}
				if (!stock.getNewValue().equalsIgnoreCase("others")) {
					MLocation newLocation = this.locationRepository.findOneByCode(stock.getNewValue());
					stock.setNewValue(newLocation.getDisplayName());
				}

			}
		});
		return stockEditHistoryList;
	}

}
