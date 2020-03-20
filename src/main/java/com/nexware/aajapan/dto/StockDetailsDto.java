package com.nexware.aajapan.dto;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.models.Attachment;
import com.nexware.aajapan.models.TInspectionOrderRequest;
import com.nexware.aajapan.models.TransportInfo;
import com.nexware.aajapan.services.S3Factory;
import com.nexware.aajapan.utils.AppUtil;

public class StockDetailsDto {

	private String id;
	private String stockNo;
	private String chassisNo;
	private String maker;
	private String model;
	private String supplierCode;// lookup
	private String supplierName;// lookup
	@JsonFormat(pattern = "yyyy/MM")
	@DateTimeFormat(pattern = "yyyy/MM")
	private Date firstRegDate;
	private String sFirstRegDate;
	private String transmission;
	private String manualTypes;
	private Integer noOfDoors;
	private Integer isBidding;
	private String noOfSeat;
	private String grade;
	private String auctionGrade;
	private String auctionGradeExt;
	private String fuel;
	private String driven;
	private Long mileage;
	private String color;
	private Double cc;
	private String recycle;
	private String numberPlate;
	private String exportSerial;
	private String exportReference;
	private String oldNumberPlate;
	private List<String> equipment;
	private List<String> extraAccessories;
	private List<Attachment> attachments;
	private Integer noOfAttachments;
	private List<String> imageUrls;
	private String destinationCountry;
	private String destinationPort;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date purchasedDate;
	private Long shuppinNo;
	private String auctionHouse;
	private String auctionHouseId;

	private String reservedById;
	private String reservedByName;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date reservedDate;

	private String reservedCustomerID;
	private String reservedCustomerName;
	private String reservedPrice;
	private String minSellPrice;
	// purchase info

	private Double purchaseCost;
	private Double purchaseCostTax;
	private Double commision;
	private Double commisionTax;
	private Double roadTax;
	private Double recycleAmount;
	private Double otherCharges;
	private Double total;
	private Double totalTax;
	private Double totalTaxIncluded;
	private List<TransportInfo> transportInfos;
	private List<TInspectionOrderRequest> inspectionInfos;
	private String currentLocation;
	private Integer shipmentType;
	@JsonFormat(pattern = "yyyy")
	@DateTimeFormat(pattern = "yyyy")
	private Date manufactureYear;
	private String remarks;
	private String auctionRemarks;

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

	public String getMaker() {
		return this.maker;
	}

	public void setMaker(String maker) {
		this.maker = maker;
	}

	public String getModel() {
		return this.model;
	}

	public void setModel(String model) {
		this.model = model;
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

	public String getTransmission() {
		return this.transmission;
	}

	public void setTransmission(String transmission) {
		this.transmission = transmission;
	}

	public String getManualTypes() {
		return this.manualTypes;
	}

	public void setManualTypes(String manualTypes) {
		this.manualTypes = manualTypes;
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

	public String getGrade() {
		return this.grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
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

	public Date getPurchasedDate() {
		return purchasedDate;
	}

	public void setPurchasedDate(Date purchasedDate) {
		this.purchasedDate = purchasedDate;
	}

	public Long getShuppinNo() {
		return shuppinNo;
	}

	public void setShuppinNo(Long shuppinNo) {
		this.shuppinNo = shuppinNo;
	}

	public String getReservedById() {
		return this.reservedById;
	}

	public void setReservedById(String reservedById) {
		this.reservedById = reservedById;
	}

	public String getReservedByName() {
		return this.reservedByName;
	}

	public void setReservedByName(String reservedByName) {
		this.reservedByName = reservedByName;
	}

	public String getReservedCustomerID() {
		return this.reservedCustomerID;
	}

	public void setReservedCustomerID(String reservedCustomerID) {
		this.reservedCustomerID = reservedCustomerID;
	}

	public String getReservedCustomerName() {
		return this.reservedCustomerName;
	}

	public void setReservedCustomerName(String reservedCustomerName) {
		this.reservedCustomerName = reservedCustomerName;
	}

	public List<Attachment> getAttachments() {
		return this.attachments;
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}

	public Integer getNoOfAttachments() {
		return noOfAttachments;
	}

	public List<String> getImageUrls() {
		return imageUrls;
	}

	public void setImageUrls(List<String> imageUrls) {
		this.imageUrls = imageUrls;
	}

	public void setNoOfAttachments(Integer noOfAttachments) {
		this.noOfAttachments = noOfAttachments;
	}

	public Date getReservedDate() {
		return this.reservedDate;
	}

	public void setReservedDate(Date reservedDate) {
		this.reservedDate = reservedDate;
	}

	public Double getRecycleAmount() {
		return this.recycleAmount;
	}

	public void setRecycleAmount(Double recycleAmount) {
		this.recycleAmount = recycleAmount;
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
		this.total = AppUtil.ifNull(this.recycleAmount, 0.0) + AppUtil.ifNull(this.commision, 0.0)
				+ AppUtil.ifNull(this.roadTax, 0.0) + AppUtil.ifNull(this.purchaseCost, 0.0)
				+ AppUtil.ifNull(this.otherCharges, 0.0);
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
		this.totalTaxIncluded = this.getTotal() + this.getTotalTax();
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

	public Double getTotalTax() {
		this.totalTax = (((AppUtil.isObjectEmpty(this.purchaseCost) ? 0.0 : this.purchaseCost)
				* (AppUtil.isObjectEmpty(this.purchaseCostTax) ? 0.0 : this.purchaseCostTax)) / 100)
				+ (((AppUtil.isObjectEmpty(this.commision) ? 0.0 : this.commision)
						* (AppUtil.isObjectEmpty(this.commisionTax) ? 0.0 : this.commisionTax)) / 100);
		return this.totalTax;
	}

	public void setTotalTax(Double totalTax) {
		this.totalTax = totalTax;
	}

	public List<TransportInfo> getTransportInfos() {
		return this.transportInfos;
	}

	public void setTransportInfos(List<TransportInfo> transportInfos) {
		this.transportInfos = transportInfos;
	}

	public List<TInspectionOrderRequest> getInspectionInfos() {
		return this.inspectionInfos;
	}

	public void setInspectionInfos(List<TInspectionOrderRequest> inspectionInfos) {
		this.inspectionInfos = inspectionInfos;
	}

	public String getCurrentLocation() {
		return this.currentLocation;
	}

	public void setCurrentLocation(String currentLocation) {
		this.currentLocation = currentLocation;
	}

	public String getReservedPrice() {
		return this.reservedPrice;
	}

	public void setReservedPrice(String reservedPrice) {
		this.reservedPrice = reservedPrice;
	}

	public String getMinSellPrice() {
		return this.minSellPrice;
	}

	public void setMinSellPrice(String minSellPrice) {
		this.minSellPrice = minSellPrice;
	}

	public Integer getShipmentType() {
		return shipmentType;
	}

	public void setShipmentType(Integer shipmentType) {
		this.shipmentType = shipmentType;
	}

	public Date getManufactureYear() {
		return manufactureYear;
	}

	public void setManufactureYear(Date manufactureYear) {
		this.manufactureYear = manufactureYear;
	}

	public Integer getIsBidding() {
		return isBidding;
	}

	public void setIsBidding(Integer isBidding) {
		this.isBidding = isBidding;
	}

	public String getExportSerial() {
		return exportSerial;
	}

	public void setExportSerial(String exportSerial) {
		this.exportSerial = exportSerial;
	}

	public String getExportReference() {
		return exportReference;
	}

	public void setExportReference(String exportReference) {
		this.exportReference = exportReference;
	}

	public String getAuctionHouse() {
		return auctionHouse;
	}

	public void setAuctionHouse(String auctionHouse) {
		this.auctionHouse = auctionHouse;
	}

	public String getAuctionHouseId() {
		return auctionHouseId;
	}

	public void setAuctionHouseId(String auctionHouseId) {
		this.auctionHouseId = auctionHouseId;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getAuctionRemarks() {
		return auctionRemarks;
	}

	public void setAuctionRemarks(String auctionRemarks) {
		this.auctionRemarks = auctionRemarks;
	}

	

	
}
