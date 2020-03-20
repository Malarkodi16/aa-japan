
package com.nexware.aajapan.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class StockSalesReportDto {
	private String stockNo;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date createdDate;
	private Double purchaseCost;
	private Double commision;
	private Double otherCharges;
	private Double pTotal;
	private Double sTotal;
	private Double exchangeRate;

	public String getStockNo() {
		return this.stockNo;
	}

	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Double getPurchaseCost() {
		return this.purchaseCost;
	}

	public void setPurchaseCost(Double purchaseCost) {
		this.purchaseCost = purchaseCost;
	}

	public Double getCommision() {
		return this.commision;
	}

	public void setCommision(Double commision) {
		this.commision = commision;
	}

	public Double getOtherCharges() {
		return this.otherCharges;
	}

	public void setOtherCharges(Double otherCharges) {
		this.otherCharges = otherCharges;
	}

	public Double getpTotal() {
		this.pTotal = this.commision + this.purchaseCost + this.otherCharges;
		return this.pTotal;
	}

	public void setpTotal(Double pTotal) {
		this.pTotal = pTotal;
	}

	public Double getsTotal() {
		this.sTotal = this.sTotal * this.exchangeRate;
		return this.sTotal;
	}

	public void setsTotal(Double sTotal) {
		this.sTotal = sTotal;
	}

	public Double getExchangeRate() {
		return this.exchangeRate;
	}

	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

}
