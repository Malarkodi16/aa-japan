package com.nexware.aajapan.dto;

public class PurchasedSaveDto {

	private String stockNo;
	private String supplierCode;
	private String invoiceNo;
	private String invoiceName;
	private String invoiceType;
	private Double purchaseCost;
	private Double purchaseCostTax;
	private Double purchaseCostTaxAmount;
	private Double commisionTaxAmount;
	private Double commision;
	private Double commisionTax;
	private Double recycle;

	private Double roadTax;
	private Double otherCharges;
	private Double otherChargesTax;
	private Double othersCostTaxAmount;
	private String maker;
	private String model;
	private String remarks;

	public String getStockNo() {
		return this.stockNo;
	}

	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getSupplierCode() {
		return this.supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
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

	public Double getRecycle() {
		return this.recycle;
	}

	public void setRecycle(Double recycle) {
		this.recycle = recycle;
	}

	public Double getRoadTax() {
		return this.roadTax;
	}

	public void setRoadTax(Double roadTax) {
		this.roadTax = roadTax;
	}

	public Double getOtherCharges() {
		return this.otherCharges;
	}

	public void setOtherCharges(Double otherCharges) {
		this.otherCharges = otherCharges;
	}

	public Double getPurchaseCostTax() {
		return this.purchaseCostTax;
	}

	public void setPurchaseCostTax(Double purchaseCostTax) {
		this.purchaseCostTax = purchaseCostTax;
	}

	public Double getCommisionTax() {
		return this.commisionTax;
	}

	public void setCommisionTax(Double commisionTax) {
		this.commisionTax = commisionTax;
	}

	public String getInvoiceName() {
		return this.invoiceName;
	}

	public void setInvoiceName(String invoiceName) {
		this.invoiceName = invoiceName;
	}

	public String getInvoiceType() {
		return this.invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
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

	public Double getPurchaseCostTaxAmount() {
		return purchaseCostTaxAmount;
	}

	public void setPurchaseCostTaxAmount(Double purchaseCostTaxAmount) {
		this.purchaseCostTaxAmount = purchaseCostTaxAmount;
	}

	public Double getCommisionTaxAmount() {
		return commisionTaxAmount;
	}

	public void setCommisionTaxAmount(Double commisionTaxAmount) {
		this.commisionTaxAmount = commisionTaxAmount;
	}

	public Double getOtherChargesTax() {
		return otherChargesTax;
	}

	public void setOtherChargesTax(Double otherChargesTax) {
		this.otherChargesTax = otherChargesTax;
	}

	public Double getOthersCostTaxAmount() {
		return othersCostTaxAmount;
	}

	public void setOthersCostTaxAmount(Double othersCostTaxAmount) {
		this.othersCostTaxAmount = othersCostTaxAmount;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
