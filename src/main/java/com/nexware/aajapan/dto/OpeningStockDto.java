package com.nexware.aajapan.dto;

import java.util.Date;

import com.nexware.aajapan.utils.ExcelUtil;

public class OpeningStockDto {
	private String chasisNo;
	private String modelType;
	private String maker;
	private String model;
	private String fuel;
	private String transmission;
	private String shuppinNo;
	private String company;
	private String auctionHouse;
	private Date purchaseDate;
	private String purchaseCost;
	private String commision;
	private String carTax;
	private String recycleAmt;

	public String getChasisNo() {
		return chasisNo;
	}

	public void setChasisNo(String chasisNo) {
		this.chasisNo = chasisNo;
	}

	public String getModelType() {
		return modelType;
	}

	public void setModelType(String modelType) {
		this.modelType = modelType;
	}

	public String getMaker() {
		return maker;
	}

	public void setMaker(String maker) {
		this.maker = maker;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getFuel() {
		return fuel;
	}

	public void setFuel(String fuel) {
		this.fuel = fuel;
	}

	public String getTransmission() {
		return transmission;
	}

	public void setTransmission(String transmission) {
		this.transmission = transmission;
	}

	public String getShuppinNo() {
		return shuppinNo;
	}

	public void setShuppinNo(String shuppinNo) {
		this.shuppinNo = shuppinNo;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getAuctionHouse() {
		return auctionHouse;
	}

	public void setAuctionHouse(String auctionHouse) {
		this.auctionHouse = auctionHouse;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public String getPurchaseCost() {
		return purchaseCost;
	}

	public void setPurchaseCost(String purchaseCost) {
		this.purchaseCost = purchaseCost;
	}

	public String getCommision() {
		return commision;
	}

	public void setCommision(String commision) {
		this.commision = commision;
	}

	public String getCarTax() {
		return carTax;
	}

	public void setCarTax(String carTax) {
		this.carTax = carTax;
	}

	public String getRecycleAmt() {
		return recycleAmt;
	}

	public void setRecycleAmt(String recycleAmt) {
		this.recycleAmt = recycleAmt;
	}

}
