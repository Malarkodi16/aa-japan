package com.nexware.aajapan.dto;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.models.TransportInfo;

public class PurchasedDto {

	private String id;
	private String code;
	private String type;
	private String stockNo;
	private String sRecycle;
	private String supplierCode;
	private String supplierName;
	private String auctionHouseId;
	private String auctionHouse;
	private String invoiceNo;
	private Double purchaseCost;
	private Double purchaseCostTax;
	private Double purchaseCostTaxAmount;
	private Double recycle;
	private Double commision;
	private Double commisionTax;
	private Double commisionTaxAmount;
	private Double roadTax;
	private Double otherCharges;
	private Double otherChargesTax;
	private Double othersCostTaxAmount;
	private String chassisNo;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date purchaseDate;
	private String purchaseInfoType;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date purchaseInfoDate;
	private String purchaseInfoSupplier;
	private ObjectId auctionInfoAuctionHouse;
	private Long auctionInfoLotNo;
	private String auctionInfoPosNo;
	private TransportInfo transportInfo;
	private String maker;
	private String model;
	private String category;
	private String subcategory;
	private String transportCategory;
	private String destinationCountry;
	private String destinationPort;
	private Integer transportationStatus;
	private String lastTransportLocation;
	private String charge;
	private String lastTransportLocationCustom;
	private String numberPlate;
	private Integer purchaseCostFlag;
	private Integer CommissionFlag;
	private Integer recycleFlag;
	private Integer roadTaxFlag;
	private List<String> posNos;
	private Integer inspectionFlag;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getChassisNo() {
		return this.chassisNo;
	}

	public void setChassisNo(String chassisNo) {
		this.chassisNo = chassisNo;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getStockNo() {
		return this.stockNo;
	}

	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}

	public String getInvoiceNo() {
		return this.invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public Double getPurchaseCost() {
		return this.purchaseCost;
	}

	public void setPurchaseCost(Double purchaseCost) {
		this.purchaseCost = purchaseCost;
	}

	public Double getRecycle() {
		return this.recycle;
	}

	public void setRecycle(Double recycle) {
		this.recycle = recycle;
	}

	public Double getCommision() {
		return this.commision;
	}

	public void setCommision(Double commision) {
		this.commision = commision;
	}

	public Double getRoadTax() {
		return this.roadTax;
	}

	public void setRoadTax(Double roadTax) {
		this.roadTax = roadTax;
	}

	public Double getOtherCharges() {
		return this.otherCharges;
	}

	public void setOtherCharges(Double otherCharges) {
		this.otherCharges = otherCharges;
	}

	public String getPurchaseInfoType() {
		return purchaseInfoType;
	}

	public void setPurchaseInfoType(String purchaseInfoType) {
		this.purchaseInfoType = purchaseInfoType;
	}

	public Date getPurchaseInfoDate() {
		return purchaseInfoDate;
	}

	public void setPurchaseInfoDate(Date purchaseInfoDate) {
		this.purchaseInfoDate = purchaseInfoDate;
	}

	public String getPurchaseInfoSupplier() {
		return purchaseInfoSupplier;
	}

	public void setPurchaseInfoSupplier(String purchaseInfoSupplier) {
		this.purchaseInfoSupplier = purchaseInfoSupplier;
	}

	public ObjectId getAuctionInfoAuctionHouse() {
		return auctionInfoAuctionHouse;
	}

	public void setAuctionInfoAuctionHouse(ObjectId auctionInfoAuctionHouse) {
		this.auctionInfoAuctionHouse = auctionInfoAuctionHouse;
	}

	public Long getAuctionInfoLotNo() {
		return auctionInfoLotNo;
	}

	public void setAuctionInfoLotNo(Long auctionInfoLotNo) {
		this.auctionInfoLotNo = auctionInfoLotNo;
	}

	public String getAuctionInfoPosNo() {
		return auctionInfoPosNo;
	}

	public void setAuctionInfoPosNo(String auctionInfoPosNo) {
		this.auctionInfoPosNo = auctionInfoPosNo;
	}

	public TransportInfo getTransportInfo() {
		return this.transportInfo;
	}

	public void setTransportInfo(TransportInfo transportInfo) {
		this.transportInfo = transportInfo;
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

	public Double getPurchaseCostTax() {
		return this.purchaseCostTax;
	}

	public void setPurchaseCostTax(Double purchaseCostTax) {
		this.purchaseCostTax = purchaseCostTax;
	}

	public Double getCommisionTax() {
		return this.commisionTax;
	}

	public void setCommisionTax(Double commisionTax) {
		this.commisionTax = commisionTax;
	}

	public String getsRecycle() {
		return this.sRecycle;
	}

	public void setsRecycle(String sRecycle) {
		this.sRecycle = sRecycle;
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

	public String getDestinationCountry() {
		return this.destinationCountry;
	}

	public String getDestinationPort() {
		return this.destinationPort;
	}

	public void setDestinationPort(String destinationPort) {
		this.destinationPort = destinationPort;
	}

	public void setDestinationCountry(String destinationCountry) {
		this.destinationCountry = destinationCountry;
	}

	public Integer getTransportationStatus() {
		return this.transportationStatus;
	}

	public void setTransportationStatus(Integer transportationStatus) {
		this.transportationStatus = transportationStatus;
	}

	public String getLastTransportLocation() {
		return this.lastTransportLocation;
	}

	public void setLastTransportLocation(String lastTransportLocation) {
		this.lastTransportLocation = lastTransportLocation;
	}

	public String getCharge() {
		return this.charge;
	}

	public void setCharge(String charge) {
		this.charge = charge;
	}

	public String getLastTransportLocationCustom() {
		return this.lastTransportLocationCustom;
	}

	public void setLastTransportLocationCustom(String lastTransportLocationCustom) {
		this.lastTransportLocationCustom = lastTransportLocationCustom;
	}

	public String getNumberPlate() {
		return this.numberPlate;
	}

	public void setNumberPlate(String numberPlate) {
		this.numberPlate = numberPlate;
	}

	public Date getPurchaseDate() {
		return this.purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public Integer getPurchaseCostFlag() {
		return purchaseCostFlag;
	}

	public void setPurchaseCostFlag(Integer purchaseCostFlag) {
		this.purchaseCostFlag = purchaseCostFlag;
	}

	public Integer getCommissionFlag() {
		return CommissionFlag;
	}

	public void setCommissionFlag(Integer commissionFlag) {
		CommissionFlag = commissionFlag;
	}

	public Integer getRecycleFlag() {
		return recycleFlag;
	}

	public void setRecycleFlag(Integer recycleFlag) {
		this.recycleFlag = recycleFlag;
	}

	public Integer getRoadTaxFlag() {
		return roadTaxFlag;
	}

	public void setRoadTaxFlag(Integer roadTaxFlag) {
		this.roadTaxFlag = roadTaxFlag;
	}

	public Double getPurchaseCostTaxAmount() {
		return purchaseCostTaxAmount;
	}

	public void setPurchaseCostTaxAmount(Double purchaseCostTaxAmount) {
		this.purchaseCostTaxAmount = purchaseCostTaxAmount;
	}

	public Double getCommisionTaxAmount() {
		return commisionTaxAmount;
	}

	public void setCommisionTaxAmount(Double commisionTaxAmount) {
		this.commisionTaxAmount = commisionTaxAmount;
	}

	public Double getOtherChargesTax() {
		return otherChargesTax;
	}

	public void setOtherChargesTax(Double otherChargesTax) {
		this.otherChargesTax = otherChargesTax;
	}

	public Double getOthersCostTaxAmount() {
		return othersCostTaxAmount;
	}

	public void setOthersCostTaxAmount(Double othersCostTaxAmount) {
		this.othersCostTaxAmount = othersCostTaxAmount;
	}

	public List<String> getPosNos() {
		return posNos;
	}

	public void setPosNos(List<String> posNos) {
		this.posNos = posNos;
	}

	public String getTransportCategory() {
		return transportCategory;
	}

	public void setTransportCategory(String transportCategory) {
		this.transportCategory = transportCategory;
	}

	public Integer getInspectionFlag() {
		return inspectionFlag;
	}

	public void setInspectionFlag(Integer inspectionFlag) {
		this.inspectionFlag = inspectionFlag;
	}

}
