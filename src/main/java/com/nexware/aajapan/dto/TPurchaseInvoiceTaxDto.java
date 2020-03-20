package com.nexware.aajapan.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.utils.AppUtil;

public class TPurchaseInvoiceTaxDto {

	private String code;
	private String stockNo;
	private String chassisNo;
	private Double purchaseCost;
	private Double purchaseCostTax;
	private Double commision;
	private Double commisionTax;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date date;
	private Integer purchaseTaxClaimStatus;
	private Integer commisionTaxClaimStatus;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date purchaseDate;
	private String purchaseInfoType;
	private String supplierCode;
	private String auctionHouseId;
	private String auctionInfoPosNo;
	private Long auctionInfoLotNo;

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

	public String getChassisNo() {
		return this.chassisNo;
	}

	public void setChassisNo(String chassisNo) {
		this.chassisNo = chassisNo;
	}

	public Double getPurchaseCost() {
		return this.purchaseCost;
	}

	public void setPurchaseCost(Double purchaseCost) {
		this.purchaseCost = purchaseCost;
	}

	public Double getPurchaseCostTax() {
//		this.purchaseCostTax = (AppUtil.ifNull(this.purchaseCost, 0.0) * Constants.COMMON_TAX) / 100;
		return this.purchaseCostTax;
	}

	public void setPurchaseCostTax(Double purchaseCostTax) {
		this.purchaseCostTax = purchaseCostTax;
	}

	public Double getCommision() {
		return this.commision;
	}

	public void setCommision(Double commision) {
		this.commision = commision;
	}

	public Double getCommisionTax() {
//		this.commisionTax = (AppUtil.ifNull(this.commision, 0.0) * Constants.COMMON_TAX) / 100;
		return this.commisionTax;
	}

	public void setCommisionTax(Double commisionTax) {
		this.commisionTax = commisionTax;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getPurchaseTaxClaimStatus() {
		return this.purchaseTaxClaimStatus;
	}

	public void setPurchaseTaxClaimStatus(Integer purchaseTaxClaimStatus) {
		this.purchaseTaxClaimStatus = purchaseTaxClaimStatus;
	}

	public Integer getCommisionTaxClaimStatus() {
		return this.commisionTaxClaimStatus;
	}

	public void setCommisionTaxClaimStatus(Integer commisionTaxClaimStatus) {
		this.commisionTaxClaimStatus = commisionTaxClaimStatus;
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

}
