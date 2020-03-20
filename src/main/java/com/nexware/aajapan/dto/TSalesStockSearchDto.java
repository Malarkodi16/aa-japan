package com.nexware.aajapan.dto;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.models.Attachment;
import com.nexware.aajapan.models.PurchaseInfo;
import com.nexware.aajapan.models.ReservedInfo;
import com.nexware.aajapan.models.TransportInfo;
import com.nexware.aajapan.utils.AppUtil;

public class TSalesStockSearchDto {

	private String id;
	private String stockNo;
	private String chassisNo;
	@JsonFormat(pattern = "yyyy/MM")
	@DateTimeFormat(pattern = "yyyy/MM")
	private Date firstRegDate;
	private String sFirstRegDate;
	private String model;
	private String subModel;
	private String hsCode;
	private String category;
	private String subcategory;
	private String maker;
	private String modelType;
	private String grade;
	private String auctionGrade;
	private String transmission;
	private Integer noOfDoors;
	private String noOfSeat;
	private String fuel;
	private String driven;
	private Long mileage;
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
	private List<String> equipment;
	private List<String> extraAccessories;
	private List<Attachment> attachments;
	private String purchaseInvoiceCode;
	private PurchaseInfo purchaseInfo;
	private TransportInfo transportInfo;
	private ReservedInfo reservedInfo;
	private Integer reserve;
	private Integer inspectionStatus;
	private Integer isLocked;
	private String lockedBy;
	private String lockedBySalesPersonName;
	private Integer status;
	private Integer showForSales;
	private Integer account;
	private Integer isBidding;
	private Double fob;
	private Double thresholdRange;
	private String engineNo;
	private Integer transportationStatus;
	private Double buyingPrice;
	private Double purchaseCost;
	private Double purchaseCostTax;
	private Double total;
	private Double commision;
	private Double commisionTax;
	private Double roadTax;
	private Double totalTaxIncluded;
	private Double otherCharges;
	private String shipmentType;
	private String customerName;
	private String userName;
	

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

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

	public String getGrade() {
		return this.grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
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

	public Integer getInspectionStatus() {
		return this.inspectionStatus;
	}

	public void setInspectionStatus(Integer inspectionStatus) {
		this.inspectionStatus = inspectionStatus;
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

	public Double getBuyingPrice() {
		return this.buyingPrice;
	}

	public void setBuyingPrice(Double buyingPrice) {
		this.buyingPrice = buyingPrice;
	}

	public Double getPurchaseCost() {
		return this.purchaseCost;
	}

	public void setPurchaseCost(Double purchaseCost) {
		this.purchaseCost = purchaseCost;
	}

	public Double getPurchaseCostTax() {
		return this.purchaseCostTax;
	}

	public void setPurchaseCostTax(Double purchaseCostTax) {
		this.purchaseCostTax = purchaseCostTax;
	}

	public Double getTotal() {
		return this.total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Double getCommision() {
		return this.commision;
	}

	public void setCommision(Double commision) {
		this.commision = commision;
	}

	public Double getCommisionTax() {
		return this.commisionTax;
	}

	public void setCommisionTax(Double commisionTax) {
		this.commisionTax = commisionTax;
	}

	public Double getRoadTax() {
		return this.roadTax;
	}

	public void setRoadTax(Double roadTax) {
		this.roadTax = roadTax;
	}

	public Double getTotalTaxIncluded() {
		return this.totalTaxIncluded;
	}

	public void setTotalTaxIncluded(Double totalTaxIncluded) {
		this.totalTaxIncluded = totalTaxIncluded;
	}

	public Double getOtherCharges() {
		return this.otherCharges;
	}

	public void setOtherCharges(Double otherCharges) {
		this.otherCharges = otherCharges;
	}

	public String getAuctionGrade() {
		return this.auctionGrade;
	}

	public void setAuctionGrade(String auctionGrade) {
		this.auctionGrade = auctionGrade;
	}

	public String getNoOfSeat() {
		return this.noOfSeat;
	}

	public void setNoOfSeat(String noOfSeat) {
		this.noOfSeat = noOfSeat;
	}

	public String getLockedBySalesPersonName() {
		return this.lockedBySalesPersonName;
	}

	public void setLockedBySalesPersonName(String lockedBySalesPersonName) {
		this.lockedBySalesPersonName = lockedBySalesPersonName;
	}

	public String getSubModel() {
		return this.subModel;
	}

	public void setSubModel(String subModel) {
		this.subModel = subModel;
	}

	public String getsFirstRegDate() {
		return this.sFirstRegDate;
	}

	public void setsFirstRegDate(String sFirstRegDate) {
		// set first reg. date
		this.setFirstRegDate(AppUtil.parseDate(sFirstRegDate.replaceAll("_+", "")));
		this.sFirstRegDate = sFirstRegDate;
	}

	public String getShipmentType() {
		return shipmentType;
	}

	public void setShipmentType(String shipmentType) {
		this.shipmentType = shipmentType;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

}
