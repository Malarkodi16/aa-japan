package com.nexware.aajapan.models;



import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "t_cancelled_Invoices")
public class TCancelledInvoices {
	
	@Id
	private String id;
	private String invoiceNo;
	private String invoiceRefNo;
	private String invoiceType;
	private String refNo;
	private Date invoiceDate;
	@Indexed
	private String supplierCode;
	@Indexed
	private String transporterCode;
	private Object auctionHouse;
	private Double invoiceAmount;
	private String remarks;
	private Date cancelledDate;
	private String cancellationRemarks;
	
	
	public String getCancellationRemarks() {
		return cancellationRemarks;
	}
	public void setCancellationRemarks(String cancellationRemarks) {
		this.cancellationRemarks = cancellationRemarks;
	}
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
	public String getRefNo() {
		return refNo;
	}
	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}
	public Date getCancelledDate() {
		return cancelledDate;
	}
	public void setCancelledDate(Date cancelledDate) {
		this.cancelledDate = cancelledDate;
	}
	public String getInvoiceRefNo() {
		return invoiceRefNo;
	}
	public void setInvoiceRefNo(String invoiceRefNo) {
		this.invoiceRefNo = invoiceRefNo;
	}
	public String getInvoiceType() {
		return invoiceType;
	}
	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}
	public String getTransporterCode() {
		return transporterCode;
	}
	public void setTransporterCode(String transporterCode) {
		this.transporterCode = transporterCode;
	}
	
	
	

}
