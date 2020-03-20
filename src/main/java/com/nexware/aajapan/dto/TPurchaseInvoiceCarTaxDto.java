package com.nexware.aajapan.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TPurchaseInvoiceCarTaxDto {

	private String id;
	private String stockNo;
	private String chassisNo;
	private Double roadTax;
	private Integer carTaxClaimStatus;
	private Double carTaxClaimReceivedAmount;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date purchaseDate;
	private String purchaseInfoType;
	private String supplierCode;
	private String auctionHouseId;
	private String auctionInfoPosNo;
	private Long auctionInfoLotNo;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date receivedDate;

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

	public Double getRoadTax() {
		return this.roadTax;
	}

	public void setRoadTax(Double roadTax) {
		this.roadTax = roadTax;
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

	public Integer getCarTaxClaimStatus() {
		return this.carTaxClaimStatus;
	}

	public void setCarTaxClaimStatus(Integer carTaxClaimStatus) {
		this.carTaxClaimStatus = carTaxClaimStatus;
	}

	public Double getCarTaxClaimReceivedAmount() {
		return this.carTaxClaimReceivedAmount;
	}

	public void setCarTaxClaimReceivedAmount(Double carTaxClaimReceivedAmount) {
		this.carTaxClaimReceivedAmount = carTaxClaimReceivedAmount;
	}

	public Date getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}

}
