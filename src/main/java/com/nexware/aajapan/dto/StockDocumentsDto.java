package com.nexware.aajapan.dto;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.models.Attachment;
import com.nexware.aajapan.models.InspectionDetail;
import com.nexware.aajapan.models.PurchaseInfo;
import com.nexware.aajapan.models.ReservedInfo;
import com.nexware.aajapan.models.ShippingInstructionInfo;
import com.nexware.aajapan.models.TransportInfo;

public class StockDocumentsDto {

	private String id;
	private String stockNo;
	private String chassisNo;
	@JsonFormat(pattern = "yyyy/MM")
	@DateTimeFormat(pattern = "yyyy/MM")
	private Date firstRegDate;
	private String sFirstRegDate;
	private String model;
	private String hsCode;
	private String category;
	private String subcategory;
	private String maker;
	private String modelType;
	private String subModel;
	private String grade;
	private String supplier;
	private String supplierCode;
	private String supplierName;
	private String auctionHouseId;
	private String auctionHouse;
	private String auctionGrade;
	private String auctionGradeExt;
	private String transmission;
	private Integer noOfDoors;
	private String noOfSeat;
	private String fuel;
	private String driven;
	private Long mileage;
	private Long shuppinNo;
	private String color;
	private String orgin;
	private String destinationCountry;
	private String destinationPort;
	private Double cc;
	private String recycle;
	private String numberPlate;
	private String oldNumberPlate;
	private String optionDescription;
	private String remarks;
	private String documentRemarks;
	private String purchaseType;
	private String auctionRemarks;
	private List<String> equipment;
	private List<String> extraAccessories;
	private List<Attachment> attachments;
	private String purchaseInvoiceCode;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date purchaseInfoDate;
	private PurchaseInfo purchaseInfo;
	private TransportInfo transportInfo;
	private String lastTransportLocation;
	private String lastTransportLocationCustom;
	private ReservedInfo reservedInfo;
	private Integer reserve;
	@CreatedDate
	@JsonFormat(pattern = "dd-MM-yyyy")
	protected Date createdDate;
	private Integer isLocked;
	private String lockedBy;
	private String lockedBySalesPersonName;
	private Integer status;
	private Integer showForSales;
	private Integer account;
	private Integer isMovable;
	private Integer isBidding;
	private Double fob;
	private Double thresholdRange;
	private Double buyingPrice;
	private String engineNo;
	private Integer transportationStatus;
	private Integer transportationCount;
	private ShippingInstructionInfo shippingInstructionInfo;
	private Integer shippingInstructionStatus;
	private Integer shippingStatus;
	private Integer inspectionStatus;
	List<InspectionDetail> inspectionDetails;
	private String manualTypes;
	private Integer documentStatus;
	private Integer documentType;
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date documentReceivedDate;

	private Integer documentConvertTo;
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date documentConvertedDate;
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date plateNoReceivedDate;
	private Integer handoverTo;
	private String handOverPerson;// refer
	private Double documentFob;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStockNo() {
		return this.stockNo;
	}

	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}

	public String getChassisNo() {
		return this.chassisNo;
	}

	public void setChassisNo(String chassisNo) {
		this.chassisNo = chassisNo;
	}

	public Date getFirstRegDate() {
		return this.firstRegDate;
	}

	public void setFirstRegDate(Date firstRegDate) {
		this.firstRegDate = firstRegDate;
	}

	public String getsFirstRegDate() {
		return this.sFirstRegDate;
	}

	public void setsFirstRegDate(String sFirstRegDate) {
		this.sFirstRegDate = sFirstRegDate;
	}

	public String getModel() {
		return this.model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getHsCode() {
		return this.hsCode;
	}

	public void setHsCode(String hsCode) {
		this.hsCode = hsCode;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubcategory() {
		return this.subcategory;
	}

	public void setSubcategory(String subcategory) {
		this.subcategory = subcategory;
	}

	public String getMaker() {
		return this.maker;
	}

	public void setMaker(String maker) {
		this.maker = maker;
	}

	public String getModelType() {
		return this.modelType;
	}

	public void setModelType(String modelType) {
		this.modelType = modelType;
	}

	public String getSubModel() {
		return this.subModel;
	}

	public void setSubModel(String subModel) {
		this.subModel = subModel;
	}

	public String getGrade() {
		return this.grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getSupplierCode() {
		return this.supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public String getSupplierName() {
		return this.supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getAuctionHouseId() {
		return this.auctionHouseId;
	}

	public void setAuctionHouseId(String auctionHouseId) {
		this.auctionHouseId = auctionHouseId;
	}

	public String getAuctionHouse() {
		return this.auctionHouse;
	}

	public void setAuctionHouse(String auctionHouse) {
		this.auctionHouse = auctionHouse;
	}

	public String getAuctionGrade() {
		return this.auctionGrade;
	}

	public void setAuctionGrade(String auctionGrade) {
		this.auctionGrade = auctionGrade;
	}

	public String getAuctionGradeExt() {
		return this.auctionGradeExt;
	}

	public void setAuctionGradeExt(String auctionGradeExt) {
		this.auctionGradeExt = auctionGradeExt;
	}

	public String getTransmission() {
		return this.transmission;
	}

	public void setTransmission(String transmission) {
		this.transmission = transmission;
	}

	public Integer getNoOfDoors() {
		return this.noOfDoors;
	}

	public void setNoOfDoors(Integer noOfDoors) {
		this.noOfDoors = noOfDoors;
	}

	public String getNoOfSeat() {
		return this.noOfSeat;
	}

	public void setNoOfSeat(String noOfSeat) {
		this.noOfSeat = noOfSeat;
	}

	public String getFuel() {
		return this.fuel;
	}

	public void setFuel(String fuel) {
		this.fuel = fuel;
	}

	public String getDriven() {
		return this.driven;
	}

	public void setDriven(String driven) {
		this.driven = driven;
	}

	public Long getMileage() {
		return this.mileage;
	}

	public void setMileage(Long mileage) {
		this.mileage = mileage;
	}

	public String getColor() {
		return this.color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getOrgin() {
		return this.orgin;
	}

	public void setOrgin(String orgin) {
		this.orgin = orgin;
	}

	public String getDestinationCountry() {
		return this.destinationCountry;
	}

	public void setDestinationCountry(String destinationCountry) {
		this.destinationCountry = destinationCountry;
	}

	public String getDestinationPort() {
		return this.destinationPort;
	}

	public void setDestinationPort(String destinationPort) {
		this.destinationPort = destinationPort;
	}

	public Double getCc() {
		return this.cc;
	}

	public void setCc(Double cc) {
		this.cc = cc;
	}

	public String getRecycle() {
		return this.recycle;
	}

	public void setRecycle(String recycle) {
		this.recycle = recycle;
	}

	public String getNumberPlate() {
		return this.numberPlate;
	}

	public void setNumberPlate(String numberPlate) {
		this.numberPlate = numberPlate;
	}

	public String getOldNumberPlate() {
		return this.oldNumberPlate;
	}

	public void setOldNumberPlate(String oldNumberPlate) {
		this.oldNumberPlate = oldNumberPlate;
	}

	public String getOptionDescription() {
		return this.optionDescription;
	}

	public void setOptionDescription(String optionDescription) {
		this.optionDescription = optionDescription;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getDocumentRemarks() {
		return this.documentRemarks;
	}

	public void setDocumentRemarks(String documentRemarks) {
		this.documentRemarks = documentRemarks;
	}

	public String getAuctionRemarks() {
		return this.auctionRemarks;
	}

	public void setAuctionRemarks(String auctionRemarks) {
		this.auctionRemarks = auctionRemarks;
	}

	public List<String> getEquipment() {
		return this.equipment;
	}

	public void setEquipment(List<String> equipment) {
		this.equipment = equipment;
	}

	public List<String> getExtraAccessories() {
		return this.extraAccessories;
	}

	public void setExtraAccessories(List<String> extraAccessories) {
		this.extraAccessories = extraAccessories;
	}

	public List<Attachment> getAttachments() {
		return this.attachments;
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}

	public String getPurchaseInvoiceCode() {
		return this.purchaseInvoiceCode;
	}

	public void setPurchaseInvoiceCode(String purchaseInvoiceCode) {
		this.purchaseInvoiceCode = purchaseInvoiceCode;
	}

	public PurchaseInfo getPurchaseInfo() {
		return this.purchaseInfo;
	}

	public void setPurchaseInfo(PurchaseInfo purchaseInfo) {
		this.purchaseInfo = purchaseInfo;
	}

	public TransportInfo getTransportInfo() {
		return this.transportInfo;
	}

	public void setTransportInfo(TransportInfo transportInfo) {
		this.transportInfo = transportInfo;
	}

	public String getLastTransportLocation() {
		return this.lastTransportLocation;
	}

	public void setLastTransportLocation(String lastTransportLocation) {
		this.lastTransportLocation = lastTransportLocation;
	}

	public String getLastTransportLocationCustom() {
		return this.lastTransportLocationCustom;
	}

	public void setLastTransportLocationCustom(String lastTransportLocationCustom) {
		this.lastTransportLocationCustom = lastTransportLocationCustom;
	}

	public ReservedInfo getReservedInfo() {
		return this.reservedInfo;
	}

	public void setReservedInfo(ReservedInfo reservedInfo) {
		this.reservedInfo = reservedInfo;
	}

	public Integer getReserve() {
		return this.reserve;
	}

	public void setReserve(Integer reserve) {
		this.reserve = reserve;
	}

	public Integer getIsLocked() {
		return this.isLocked;
	}

	public void setIsLocked(Integer isLocked) {
		this.isLocked = isLocked;
	}

	public String getLockedBy() {
		return this.lockedBy;
	}

	public void setLockedBy(String lockedBy) {
		this.lockedBy = lockedBy;
	}

	public String getLockedBySalesPersonName() {
		return this.lockedBySalesPersonName;
	}

	public void setLockedBySalesPersonName(String lockedBySalesPersonName) {
		this.lockedBySalesPersonName = lockedBySalesPersonName;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getShowForSales() {
		return this.showForSales;
	}

	public void setShowForSales(Integer showForSales) {
		this.showForSales = showForSales;
	}

	public Integer getAccount() {
		return this.account;
	}

	public void setAccount(Integer account) {
		this.account = account;
	}

	public Integer getIsMovable() {
		return this.isMovable;
	}

	public void setIsMovable(Integer isMovable) {
		this.isMovable = isMovable;
	}

	public Integer getIsBidding() {
		return this.isBidding;
	}

	public void setIsBidding(Integer isBidding) {
		this.isBidding = isBidding;
	}

	public Double getFob() {
		return this.fob;
	}

	public void setFob(Double fob) {
		this.fob = fob;
	}

	public Double getThresholdRange() {
		return this.thresholdRange;
	}

	public void setThresholdRange(Double thresholdRange) {
		this.thresholdRange = thresholdRange;
	}

	public Double getBuyingPrice() {
		return this.buyingPrice;
	}

	public void setBuyingPrice(Double buyingPrice) {
		this.buyingPrice = buyingPrice;
	}

	public String getEngineNo() {
		return this.engineNo;
	}

	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}

	public Integer getTransportationStatus() {
		return this.transportationStatus;
	}

	public void setTransportationStatus(Integer transportationStatus) {
		this.transportationStatus = transportationStatus;
	}

	public Integer getTransportationCount() {
		return this.transportationCount;
	}

	public void setTransportationCount(Integer transportationCount) {
		this.transportationCount = transportationCount;
	}

	public ShippingInstructionInfo getShippingInstructionInfo() {
		return this.shippingInstructionInfo;
	}

	public void setShippingInstructionInfo(ShippingInstructionInfo shippingInstructionInfo) {
		this.shippingInstructionInfo = shippingInstructionInfo;
	}

	public Integer getShippingInstructionStatus() {
		return this.shippingInstructionStatus;
	}

	public void setShippingInstructionStatus(Integer shippingInstructionStatus) {
		this.shippingInstructionStatus = shippingInstructionStatus;
	}

	public Integer getShippingStatus() {
		return this.shippingStatus;
	}

	public void setShippingStatus(Integer shippingStatus) {
		this.shippingStatus = shippingStatus;
	}

	public Integer getInspectionStatus() {
		return this.inspectionStatus;
	}

	public void setInspectionStatus(Integer inspectionStatus) {
		this.inspectionStatus = inspectionStatus;
	}

	public List<InspectionDetail> getInspectionDetails() {
		return this.inspectionDetails;
	}

	public void setInspectionDetails(List<InspectionDetail> inspectionDetails) {
		this.inspectionDetails = inspectionDetails;
	}

	public String getManualTypes() {
		return this.manualTypes;
	}

	public void setManualTypes(String manualTypes) {
		this.manualTypes = manualTypes;
	}

	public Integer getDocumentStatus() {
		return this.documentStatus;
	}

	public void setDocumentStatus(Integer documentStatus) {
		this.documentStatus = documentStatus;
	}

	public Integer getDocumentType() {
		return this.documentType;
	}

	public void setDocumentType(Integer documentType) {
		this.documentType = documentType;
	}

	public Date getDocumentReceivedDate() {
		return this.documentReceivedDate;
	}

	public void setDocumentReceivedDate(Date documentReceivedDate) {
		this.documentReceivedDate = documentReceivedDate;
	}

	public Integer getDocumentConvertTo() {
		return this.documentConvertTo;
	}

	public void setDocumentConvertTo(Integer documentConvertTo) {
		this.documentConvertTo = documentConvertTo;
	}

	public Date getDocumentConvertedDate() {
		return this.documentConvertedDate;
	}

	public void setDocumentConvertedDate(Date documentConvertedDate) {
		this.documentConvertedDate = documentConvertedDate;
	}

	public Date getPurchaseInfoDate() {
		return this.purchaseInfoDate;
	}

	public void setPurchaseInfoDate(Date purchaseInfoDate) {
		this.purchaseInfoDate = purchaseInfoDate;
	}

	public Long getShuppinNo() {
		return this.shuppinNo;
	}

	public void setShuppinNo(Long shuppinNo) {
		this.shuppinNo = shuppinNo;
	}

	public String getPurchaseType() {
		return this.purchaseType;
	}

	public void setPurchaseType(String purchaseType) {
		this.purchaseType = purchaseType;
	}

	public String getSupplier() {
		return this.supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public Date getPlateNoReceivedDate() {
		return this.plateNoReceivedDate;
	}

	public void setPlateNoReceivedDate(Date plateNoReceivedDate) {
		this.plateNoReceivedDate = plateNoReceivedDate;
	}

	public Integer getHandoverTo() {
		return this.handoverTo;
	}

	public void setHandoverTo(Integer handoverTo) {
		this.handoverTo = handoverTo;
	}

	public String getHandOverPerson() {
		return this.handOverPerson;
	}

	public void setHandOverPerson(String handOverPerson) {
		this.handOverPerson = handOverPerson;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Double getDocumentFob() {
		return this.documentFob;
	}

	public void setDocumentFob(Double documentFob) {
		this.documentFob = documentFob;
	}

}
