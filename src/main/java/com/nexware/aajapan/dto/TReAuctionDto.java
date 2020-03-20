package com.nexware.aajapan.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TReAuctionDto {

	private String id;
	private String stockNo;
	private String chassisNo;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "yyyy/MM")
	private Date reauctionDate;
	private String auctionCompanyId;
	private String auctionCompany;
	private String auctionHouseId;
	private String auctionHouse;
	private Integer status;
	private Double soldPrice;
	private Double tax;
	private Double commission;
	private Double recycleAmount;
	private Double nagareCharge;
	private Double auctionShippingCharge;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStockNo() {
		return stockNo;
	}

	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}

	public String getChassisNo() {
		return chassisNo;
	}

	public void setChassisNo(String chassisNo) {
		this.chassisNo = chassisNo;
	}

	public Date getReauctionDate() {
		return reauctionDate;
	}

	public void setReauctionDate(Date reauctionDate) {
		this.reauctionDate = reauctionDate;
	}

	public String getAuctionCompanyId() {
		return auctionCompanyId;
	}

	public void setAuctionCompanyId(String auctionCompanyId) {
		this.auctionCompanyId = auctionCompanyId;
	}

	public String getAuctionCompany() {
		return auctionCompany;
	}

	public void setAuctionCompany(String auctionCompany) {
		this.auctionCompany = auctionCompany;
	}

	public String getAuctionHouseId() {
		return auctionHouseId;
	}

	public void setAuctionHouseId(String auctionHouseId) {
		this.auctionHouseId = auctionHouseId;
	}

	public String getAuctionHouse() {
		return auctionHouse;
	}

	public void setAuctionHouse(String auctionHouse) {
		this.auctionHouse = auctionHouse;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Double getSoldPrice() {
		return soldPrice;
	}

	public void setSoldPrice(Double soldPrice) {
		this.soldPrice = soldPrice;
	}

	public Double getTax() {
		return tax;
	}

	public void setTax(Double tax) {
		this.tax = tax;
	}

	public Double getCommission() {
		return commission;
	}

	public void setCommission(Double commission) {
		this.commission = commission;
	}

	public Double getRecycleAmount() {
		return recycleAmount;
	}

	public void setRecycleAmount(Double recycleAmount) {
		this.recycleAmount = recycleAmount;
	}

	public Double getNagareCharge() {
		return nagareCharge;
	}

	public void setNagareCharge(Double nagareCharge) {
		this.nagareCharge = nagareCharge;
	}

	public Double getAuctionShippingCharge() {
		return auctionShippingCharge;
	}

	public void setAuctionShippingCharge(Double auctionShippingCharge) {
		this.auctionShippingCharge = auctionShippingCharge;
	}

}
