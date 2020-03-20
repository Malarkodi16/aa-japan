package com.nexware.aajapan.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TCancelledInvoicesDto {

	private String id;
	private String invoiceNo;
	private String refNo;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date invoiceDate;
	private String supplierCode;
	private Object auctionHouse;
	private Double invoiceAmount;
	private String remarks;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date cancelledDate;
	private String company;
	private String auctionHouseName;
	private String cancellationRemarks;
	private String transporter;
	private String invoiceType;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getRefNo() {
		return refNo;
	}
	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}
	public Date getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public String getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	public Object getAuctionHouse() {
		return auctionHouse;
	}
	public void setAuctionHouse(Object auctionHouse) {
		this.auctionHouse = auctionHouse;
	}
	public Double getInvoiceAmount() {
		return invoiceAmount;
	}
	public void setInvoiceAmount(Double invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public Date getCancelledDate() {
		return cancelledDate;
	}
	public void setCancelledDate(Date cancelledDate) {
		this.cancelledDate = cancelledDate;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getAuctionHouseName() {
		return auctionHouseName;
	}
	public void setAuctionHouseName(String auctionHouseName) {
		this.auctionHouseName = auctionHouseName;
	}
	public String getCancellationRemarks() {
		return cancellationRemarks;
	}
	public void setCancellationRemarks(String cancellationRemarks) {
		this.cancellationRemarks = cancellationRemarks;
	}
	public String getTransporter() {
		return transporter;
	}
	public void setTransporter(String transporter) {
		this.transporter = transporter;
	}
	public String getInvoiceType() {
		return invoiceType;
	}
	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}
	
	
	
	
}
