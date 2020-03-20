package com.nexware.aajapan.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TPurchaseInvoiceDocumentationRecylceDto {

	private String id;
	private String stockNo;
	private String chassisNo;
	private String destCountry;
	private String destPort;
	private String vehicleType;
	private Double recycle;
	private Double recycleClaimReceivedAmount;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date recycleClaimAppliedDate;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date recycleClaimReceivedDate;
	private Double recycleClaimCharge;
	private Double recycleClaimInterest;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date purchaseDate;
	private Integer recycleClaimStatus;
	private String purchaseInfoType;
	private String supplierCode;
	private String auctionHouseId;
	private String auctionInfoPosNo;

	public String getDestPort() {
		return destPort;
	}

	public void setDestPort(String destPort) {
		this.destPort = destPort;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	private Long auctionInfoLotNo;

	public String getDestCountry() {
		return destCountry;
	}

	public void setDestCountry(String destCountry) {
		this.destCountry = destCountry;
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

	public Double getRecycle() {
		return this.recycle;
	}

	public void setRecycle(Double recycle) {
		this.recycle = recycle;
	}

	public Integer getRecycleClaimStatus() {
		return this.recycleClaimStatus;
	}

	public void setRecycleClaimStatus(Integer recycleClaimStatus) {
		this.recycleClaimStatus = recycleClaimStatus;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Double getRecycleClaimReceivedAmount() {
		return this.recycleClaimReceivedAmount;
	}

	public void setRecycleClaimReceivedAmount(Double recycleClaimReceivedAmount) {
		this.recycleClaimReceivedAmount = recycleClaimReceivedAmount;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public String getPurchaseInfoType() {
		return purchaseInfoType;
	}

	public void setPurchaseInfoType(String purchaseInfoType) {
		this.purchaseInfoType = purchaseInfoType;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public String getAuctionHouseId() {
		return auctionHouseId;
	}

	public void setAuctionHouseId(String auctionHouseId) {
		this.auctionHouseId = auctionHouseId;
	}

	public String getAuctionInfoPosNo() {
		return auctionInfoPosNo;
	}

	public void setAuctionInfoPosNo(String auctionInfoPosNo) {
		this.auctionInfoPosNo = auctionInfoPosNo;
	}

	public Long getAuctionInfoLotNo() {
		return auctionInfoLotNo;
	}

	public void setAuctionInfoLotNo(Long auctionInfoLotNo) {
		this.auctionInfoLotNo = auctionInfoLotNo;
	}

	public Date getRecycleClaimAppliedDate() {
		return recycleClaimAppliedDate;
	}

	public void setRecycleClaimAppliedDate(Date recycleClaimAppliedDate) {
		this.recycleClaimAppliedDate = recycleClaimAppliedDate;
	}

	public Date getRecycleClaimReceivedDate() {
		return recycleClaimReceivedDate;
	}

	public void setRecycleClaimReceivedDate(Date recycleClaimReceivedDate) {
		this.recycleClaimReceivedDate = recycleClaimReceivedDate;
	}

	public Double getRecycleClaimCharge() {
		return recycleClaimCharge;
	}

	public void setRecycleClaimCharge(Double recycleClaimCharge) {
		this.recycleClaimCharge = recycleClaimCharge;
	}

	public Double getRecycleClaimInterest() {
		return recycleClaimInterest;
	}

	public void setRecycleClaimInterest(Double recycleClaimInterest) {
		this.recycleClaimInterest = recycleClaimInterest;
	}

}
