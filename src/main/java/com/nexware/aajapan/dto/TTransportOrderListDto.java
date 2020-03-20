package com.nexware.aajapan.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TTransportOrderListDto {

	private String transporterCode;
	private String transporterName;
	private String supplierCode;
	private String supplierName;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date createdDate;
	private String invoiceNo;
	private String pickupLocation;
	private String sPickupLocation;
	private String sPickupLocationCustom;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date purchaseDate;
	private List<TTransportOrderItemDto> orderItem;


	public String getTransporterCode() {
		return this.transporterCode;
	}

	public void setTransporterCode(String transporterCode) {
		this.transporterCode = transporterCode;
	}

	public String getTransporterName() {
		return this.transporterName;
	}

	public void setTransporterName(String transporterName) {
		this.transporterName = transporterName;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public List<TTransportOrderItemDto> getOrderItem() {
		return this.orderItem;
	}

	public void setOrderItem(List<TTransportOrderItemDto> orderItem) {
		this.orderItem = orderItem;
	}

	public String getInvoiceNo() {
		return this.invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getPickupLocation() {
		return this.pickupLocation;
	}

	public void setPickupLocation(String pickupLocation) {
		this.pickupLocation = pickupLocation;
	}

	public String getsPickupLocation() {
		return this.sPickupLocation;
	}

	public void setsPickupLocation(String sPickupLocation) {
		this.sPickupLocation = sPickupLocation;
	}

	public String getsPickupLocationCustom() {
		return this.sPickupLocationCustom;
	}

	public void setsPickupLocationCustom(String sPickupLocationCustom) {
		this.sPickupLocationCustom = sPickupLocationCustom;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

}
