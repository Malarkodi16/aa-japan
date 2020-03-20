package com.nexware.aajapan.dto;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TPurchaseInvoiceAddPaymentDto {

	private Integer invoiceItemType;
	private Integer claimed;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date invoiceDate;
	private String supplier;
	private String purchasedAuctionHouse;
	private Double amount;
	private Double othersTaxValue;
	private Double othersTotal;
	private Double othersCostTaxAmount;
	private String stockNo;
	private List<String> stockNos;

	public Integer getInvoiceItemType() {
		return invoiceItemType;
	}

	public void setInvoiceItemType(Integer invoiceItemType) {
		this.invoiceItemType = invoiceItemType;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getPurchasedAuctionHouse() {
		return purchasedAuctionHouse;
	}

	public void setPurchasedAuctionHouse(String purchasedAuctionHouse) {
		this.purchasedAuctionHouse = purchasedAuctionHouse;
	}

	public String getStockNo() {
		return stockNo;
	}

	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}

	public List<String> getStockNos() {
		return stockNos;
	}

	public void setStockNos(List<String> stockNos) {
		this.stockNos = stockNos;
	}

	public Double getOthersCostTaxAmount() {
		return othersCostTaxAmount;
	}

	public void setOthersCostTaxAmount(Double othersCostTaxAmount) {
		this.othersCostTaxAmount = othersCostTaxAmount;
	}

	public Double getOthersTaxValue() {
		return othersTaxValue;
	}

	public void setOthersTaxValue(Double othersTaxValue) {
		this.othersTaxValue = othersTaxValue;
	}

	public Double getOthersTotal() {
		return othersTotal;
	}

	public void setOthersTotal(Double othersTotal) {
		this.othersTotal = othersTotal;
	}

	public Integer getClaimed() {
		return claimed;
	}

	public void setClaimed(Integer claimed) {
		this.claimed = claimed;
	}

}