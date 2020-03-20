package com.nexware.aajapan.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TStockPurchaseInfoDto {
	private String stockNo;
	private String chassisNo;
	private String auctionCompany;
	private String auctionHouse;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date purchaseDate;
	private Integer purchaseStatus;
	private String invoiceNo;
	private String invoicetype;
	private String auctionRefNo;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date invoiceDate;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date dueDate;
	private String invoiceAttachmentFilename;
	private String invoiceAttachmentDiskFilename;
	private Integer invoiceUpload;
	private Integer attachementViewed;
	private Double purchaseCost;
	private Double purchaseCostTax;
	private Double commision;
	private Double commisionTax;
	private Double recycle;
	private Double roadTax;
	private Double others;
	private Double othersTax;
	private Double rikusoPayment;
	private Double rikusoPaymentTax;
	private Double cancellationCharge;
	private Double cancellationChargeTax;
	private Double unsoldAuctionCharge;
	private Double unsoldAuctionChargeTax;
	private Double cancellationPenalityCharge;
	private Double cancellationPenalityChargeTax;
	private Double takeOutCharge;
	private Double takeOutChargeTax;
	private Double recyclepaid;
	private Double recyclepaidTax;
	private Integer paymentStatus;

	public Integer getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(Integer paymentStatus) {
		this.paymentStatus = paymentStatus;
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

	public String getAuctionCompany() {
		return auctionCompany;
	}

	public void setAuctionCompany(String auctionCompany) {
		this.auctionCompany = auctionCompany;
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

	public Integer getPurchaseStatus() {
		return purchaseStatus;
	}

	public void setPurchaseStatus(Integer purchaseStatus) {
		this.purchaseStatus = purchaseStatus;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getInvoicetype() {
		return invoicetype;
	}

	public void setInvoicetype(String invoicetype) {
		this.invoicetype = invoicetype;
	}

	public String getAuctionRefNo() {
		return auctionRefNo;
	}

	public void setAuctionRefNo(String auctionRefNo) {
		this.auctionRefNo = auctionRefNo;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public String getInvoiceAttachmentFilename() {
		return invoiceAttachmentFilename;
	}

	public void setInvoiceAttachmentFilename(String invoiceAttachmentFilename) {
		this.invoiceAttachmentFilename = invoiceAttachmentFilename;
	}

	public String getInvoiceAttachmentDiskFilename() {
		return invoiceAttachmentDiskFilename;
	}

	public void setInvoiceAttachmentDiskFilename(String invoiceAttachmentDiskFilename) {
		this.invoiceAttachmentDiskFilename = invoiceAttachmentDiskFilename;
	}

	public Integer getInvoiceUpload() {
		return invoiceUpload;
	}

	public void setInvoiceUpload(Integer invoiceUpload) {
		this.invoiceUpload = invoiceUpload;
	}

	public Integer getAttachementViewed() {
		return attachementViewed;
	}

	public void setAttachementViewed(Integer attachementViewed) {
		this.attachementViewed = attachementViewed;
	}

	public Double getPurchaseCost() {
		return purchaseCost;
	}

	public void setPurchaseCost(Double purchaseCost) {
		this.purchaseCost = purchaseCost;
	}

	public Double getPurchaseCostTax() {
		return purchaseCostTax;
	}

	public void setPurchaseCostTax(Double purchaseCostTax) {
		this.purchaseCostTax = purchaseCostTax;
	}

	public Double getCommision() {
		return commision;
	}

	public void setCommision(Double commision) {
		this.commision = commision;
	}

	public Double getCommisionTax() {
		return commisionTax;
	}

	public void setCommisionTax(Double commisionTax) {
		this.commisionTax = commisionTax;
	}

	public Double getRecycle() {
		return recycle;
	}

	public void setRecycle(Double recycle) {
		this.recycle = recycle;
	}

	public Double getRoadTax() {
		return roadTax;
	}

	public void setRoadTax(Double roadTax) {
		this.roadTax = roadTax;
	}

	public Double getOthers() {
		return others;
	}

	public void setOthers(Double others) {
		this.others = others;
	}

	public Double getOthersTax() {
		return othersTax;
	}

	public void setOthersTax(Double othersTax) {
		this.othersTax = othersTax;
	}

	public Double getRikusoPayment() {
		return rikusoPayment;
	}

	public void setRikusoPayment(Double rikusoPayment) {
		this.rikusoPayment = rikusoPayment;
	}

	public Double getRikusoPaymentTax() {
		return rikusoPaymentTax;
	}

	public void setRikusoPaymentTax(Double rikusoPaymentTax) {
		this.rikusoPaymentTax = rikusoPaymentTax;
	}

	public Double getCancellationCharge() {
		return cancellationCharge;
	}

	public void setCancellationCharge(Double cancellationCharge) {
		this.cancellationCharge = cancellationCharge;
	}

	public Double getCancellationChargeTax() {
		return cancellationChargeTax;
	}

	public void setCancellationChargeTax(Double cancellationChargeTax) {
		this.cancellationChargeTax = cancellationChargeTax;
	}

	public Double getUnsoldAuctionCharge() {
		return unsoldAuctionCharge;
	}

	public void setUnsoldAuctionCharge(Double unsoldAuctionCharge) {
		this.unsoldAuctionCharge = unsoldAuctionCharge;
	}

	public Double getUnsoldAuctionChargeTax() {
		return unsoldAuctionChargeTax;
	}

	public void setUnsoldAuctionChargeTax(Double unsoldAuctionChargeTax) {
		this.unsoldAuctionChargeTax = unsoldAuctionChargeTax;
	}

	public Double getCancellationPenalityCharge() {
		return cancellationPenalityCharge;
	}

	public void setCancellationPenalityCharge(Double cancellationPenalityCharge) {
		this.cancellationPenalityCharge = cancellationPenalityCharge;
	}

	public Double getCancellationPenalityChargeTax() {
		return cancellationPenalityChargeTax;
	}

	public void setCancellationPenalityChargeTax(Double cancellationPenalityChargeTax) {
		this.cancellationPenalityChargeTax = cancellationPenalityChargeTax;
	}

	public Double getTakeOutCharge() {
		return takeOutCharge;
	}

	public void setTakeOutCharge(Double takeOutCharge) {
		this.takeOutCharge = takeOutCharge;
	}

	public Double getTakeOutChargeTax() {
		return takeOutChargeTax;
	}

	public void setTakeOutChargeTax(Double takeOutChargeTax) {
		this.takeOutChargeTax = takeOutChargeTax;
	}

	public Double getRecyclepaid() {
		return recyclepaid;
	}

	public void setRecyclepaid(Double recyclepaid) {
		this.recyclepaid = recyclepaid;
	}

	public Double getRecyclepaidTax() {
		return recyclepaidTax;
	}

	public void setRecyclepaidTax(Double recyclepaidTax) {
		this.recyclepaidTax = recyclepaidTax;
	}

}
