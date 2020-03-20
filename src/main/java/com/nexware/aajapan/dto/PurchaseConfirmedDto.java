package com.nexware.aajapan.dto;

import java.util.Date;

public class PurchaseConfirmedDto {
	private String id;
	private String stockNo;
	private String chassisNo;
	private Date purchaseDate;
	private String model;
	private String lotNo;
	private String posNo;
	private String auction;
	private String pickupLocation;
	private String transportTo;
	private String forwarderAddress;
	private String finalDestination;
	private String numberPlate;
	private String remarks;

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

	public String getModel() {
		return this.model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getLotNo() {
		return this.lotNo;
	}

	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}

	public String getPosNo() {
		return this.posNo;
	}

	public void setPosNo(String posNo) {
		this.posNo = posNo;
	}

	public String getAuction() {
		return this.auction;
	}

	public void setAuction(String auction) {
		this.auction = auction;
	}

	public String getPickupLocation() {
		return this.pickupLocation;
	}

	public void setPickupLocation(String pickupLocation) {
		this.pickupLocation = pickupLocation;
	}

	public String getTransportTo() {
		return this.transportTo;
	}

	public void setTransportTo(String transportTo) {
		this.transportTo = transportTo;
	}

	public String getForwarderAddress() {
		return this.forwarderAddress;
	}

	public void setForwarderAddress(String forwarderAddress) {
		this.forwarderAddress = forwarderAddress;
	}

	public String getFinalDestination() {
		return this.finalDestination;
	}

	public void setFinalDestination(String finalDestination) {
		this.finalDestination = finalDestination;
	}

	public String getNumberPlate() {
		return this.numberPlate;
	}

	public void setNumberPlate(String numberPlate) {
		this.numberPlate = numberPlate;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Date getPurchaseDate() {
		return this.purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

}
