package com.nexware.aajapan.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.utils.AppUtil;

public class ProfitLossDto {

	private String id;
	private String stockNo;
	private String chassisNo;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date soldDate;
	private Integer stockType;
	private Integer status;
	private String firstName;
	private String lastName;
	private Double purchaseCost;
	private Double purchaseCostTaxAmount;
	private Double commision;
	private Double commisionTaxAmount;
	private Double roadTax;
	private Double otherCharges;
	private Double othersCostTaxAmount;
	private Double soldPrice;
	private Double recycle;
	private Double recycleClaimed;
	private Double shuppinCommission;
	private Double shuppinTax;
	private Double shuppinTaxAmount;
	private Double soldCommission;
	private Double soldCommTax;
	private Double soldTaxAmount;
	private Double tax;
	private Double taxAmount;
	private Double others;
	private Double total;
	private Double soldTotal;
	private Double profitLossAmount;
	private Double profitLossPercentage;

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

	public Date getSoldDate() {
		return soldDate;
	}

	public void setSoldDate(Date soldDate) {
		this.soldDate = soldDate;
	}

	public Integer getStockType() {
		return stockType;
	}

	public void setStockType(Integer stockType) {
		this.stockType = stockType;
	}

	public Double getPurchaseCost() {
		return purchaseCost;
	}

	public void setPurchaseCost(Double purchaseCost) {
		this.purchaseCost = purchaseCost;
	}

	public Double getPurchaseCostTaxAmount() {
		return purchaseCostTaxAmount;
	}

	public void setPurchaseCostTaxAmount(Double purchaseCostTaxAmount) {
		this.purchaseCostTaxAmount = purchaseCostTaxAmount;
	}

	public Double getCommision() {
		return commision;
	}

	public void setCommision(Double commision) {
		this.commision = commision;
	}

	public Double getCommisionTaxAmount() {
		return commisionTaxAmount;
	}

	public void setCommisionTaxAmount(Double commisionTaxAmount) {
		this.commisionTaxAmount = commisionTaxAmount;
	}

	public Double getRoadTax() {
		return roadTax;
	}

	public void setRoadTax(Double roadTax) {
		this.roadTax = roadTax;
	}

	public Double getOtherCharges() {
		return otherCharges;
	}

	public void setOtherCharges(Double otherCharges) {
		this.otherCharges = otherCharges;
	}

	public Double getOthersCostTaxAmount() {
		return othersCostTaxAmount;
	}

	public void setOthersCostTaxAmount(Double othersCostTaxAmount) {
		this.othersCostTaxAmount = othersCostTaxAmount;
	}

	public Double getSoldPrice() {
		return soldPrice;
	}

	public void setSoldPrice(Double soldPrice) {
		this.soldPrice = soldPrice;
	}

	public Double getRecycle() {
		return recycle;
	}

	public void setRecycle(Double recycle) {
		this.recycle = recycle;
	}

	public Double getRecycleClaimed() {
		return recycleClaimed;
	}

	public void setRecycleClaimed(Double recycleClaimed) {
		this.recycleClaimed = recycleClaimed;
	}

	public Double getShuppinCommission() {
		return shuppinCommission;
	}

	public void setShuppinCommission(Double shuppinCommission) {
		this.shuppinCommission = shuppinCommission;
	}

	public Double getShuppinTax() {
		return shuppinTax;
	}

	public void setShuppinTax(Double shuppinTax) {
		this.shuppinTax = shuppinTax;
	}

	public Double getSoldCommission() {
		return soldCommission;
	}

	public void setSoldCommission(Double soldCommission) {
		this.soldCommission = soldCommission;
	}

	public Double getSoldCommTax() {
		return soldCommTax;
	}

	

	public Double getProfitLossAmount() {
		return profitLossAmount;
	}

	public void setProfitLossAmount(Double profitLossAmount) {
		this.profitLossAmount = profitLossAmount;
	}

	public Double getProfitLossPercentage() {
		return profitLossPercentage;
	}

	public void setProfitLossPercentage(Double profitLossPercentage) {
		this.profitLossPercentage = profitLossPercentage;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Double getTax() {
		return tax;
	}

	public void setTax(Double tax) {
		this.tax = tax;
	}

	public Double getShuppinTaxAmount() {
		this.shuppinTaxAmount = (AppUtil.ifNull(this.shuppinCommission, 0.0)
				* (AppUtil.ifNull(this.shuppinTax, 0.0) / 100));
		return shuppinTaxAmount;
	}

	public void setShuppinTaxAmount(Double shuppinTaxAmount) {
		this.shuppinTaxAmount = shuppinTaxAmount;
	}

	public Double getSoldTaxAmount() {
		this.soldTaxAmount = (AppUtil.ifNull(this.soldCommission, 0.0) * (AppUtil.ifNull(this.soldCommTax, 0.0) / 100));
		return soldTaxAmount;
	}

	public void setSoldTaxAmount(Double soldTaxAmount) {
		this.soldTaxAmount = soldTaxAmount;
	}

	public Double getTaxAmount() {
		this.taxAmount = (AppUtil.ifNull(this.soldPrice, 0.0) * (AppUtil.ifNull(this.tax, 0.0) / 100));
		return taxAmount;
	}

	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Double getOthers() {
		return others;
	}

	public void setOthers(Double others) {
		this.others = others;
	}
	
	public void setSoldCommTax(Double soldCommTax) {
		this.soldCommTax = soldCommTax;
	}

	public Double getTotal() {
		Double p = AppUtil.ifNull(this.purchaseCost, 0.0) + AppUtil.ifNull(this.purchaseCostTaxAmount, 0.0);
		Double c = AppUtil.ifNull(this.commision, 0.0) + AppUtil.ifNull(this.commisionTaxAmount, 0.0);
		Double o = AppUtil.ifNull(this.otherCharges, 0.0) + AppUtil.ifNull(this.othersCostTaxAmount, 0.0);
		Double s = AppUtil.ifNull(this.shuppinCommission, 0.0) + AppUtil.ifNull(this.shuppinTaxAmount, 0.0);
		Double sold = AppUtil.ifNull(this.soldCommission, 0.0) + AppUtil.ifNull(this.soldTaxAmount, 0.0);
		this.total = AppUtil.ifNull(this.recycle, 0.0) + AppUtil.ifNull(this.roadTax, 0.0)
				 + AppUtil.ifNull(this.others, 0.0) + p + c + o + s + sold;
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Double getSoldTotal() {
		this.soldTotal = AppUtil.ifNull(this.soldPrice, 0.0) + AppUtil.ifNull(this.taxAmount, 0.0);
		return soldTotal;
	}

	public void setSoldTotal(Double soldTotal) {
		this.soldTotal = soldTotal;
	}

}
