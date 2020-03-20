package com.nexware.aajapan.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class CancelledStockDto {
	private String id;
	private String stockNo;
	private String chassisNo;
	private String supplierId;
	private String supplierName;
	private String auctionHouseId;
	private String auctionHouseName;
	private String status;
	private Double cancellationCharge;
	@JsonFormat(pattern = "dd-MM-yyyy")
	protected Date purchaseDate;

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

	public String getSupplierId() {
		return this.supplierId;
	}

	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
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

	public String getAuctionHouseName() {
		return this.auctionHouseName;
	}

	public void setAuctionHouseName(String auctionHouseName) {
		this.auctionHouseName = auctionHouseName;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getCancellationCharge() {
		return this.cancellationCharge;
	}

	public void setCancellationCharge(Double cancellationCharge) {
		this.cancellationCharge = cancellationCharge;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

}
