package com.nexware.aajapan.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.utils.AppUtil;

public class InventoryValueDto {

	private String stockNo;
	private String chassisNo;
	private String maker;
	private String model;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date purchaseDate;
	private Integer reserve;
	private Double purchaseCost;
	private String reservedPersonId;
	private String reservedPersonName;
	private Double commisionCost;
	private Double otherCharges;
	private Double inventoryCost;
	private String lastLocation;

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

	public Date getPurchaseDate() {
		return this.purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public Integer getReserve() {
		return this.reserve;
	}

	public void setReserve(Integer reserve) {
		this.reserve = reserve;
	}

	public Double getPurchaseCost() {
		return this.purchaseCost;
	}

	public void setPurchaseCost(Double purchaseCost) {
		this.purchaseCost = purchaseCost;
	}

	public Double getCommisionCost() {
		return this.commisionCost;
	}

	public void setCommisionCost(Double commisionCost) {
		this.commisionCost = commisionCost;
	}

	public Double getOtherCharges() {
		return this.otherCharges;
	}

	public void setOtherCharges(Double otherCharges) {
		this.otherCharges = otherCharges;
	}

	public Double getInventoryCost() {
		this.inventoryCost = AppUtil.ifNull(this.purchaseCost, 0.0) + AppUtil.ifNull(this.commisionCost, 0.0)
				+ AppUtil.ifNull(this.otherCharges, 0.0);
		return this.inventoryCost;
	}

	public void setInventoryCost(Double inventoryCost) {
		this.inventoryCost = inventoryCost;
	}

	public String getLastLocation() {
		return this.lastLocation;
	}

	public void setLastLocation(String lastLocation) {
		this.lastLocation = lastLocation;
	}

	public String getReservedPersonId() {
		return reservedPersonId;
	}

	public void setReservedPersonId(String reservedPersonId) {
		this.reservedPersonId = reservedPersonId;
	}

	public String getReservedPersonName() {
		return reservedPersonName;
	}

	public void setReservedPersonName(String reservedPersonName) {
		this.reservedPersonName = reservedPersonName;
	}

}
