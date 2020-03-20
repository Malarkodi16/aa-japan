package com.nexware.aajapan.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TStockTransportInfoDto {

	private String stockNo;
	private String currentLocation;
	private String currrentLocationCustom;
	private String transporter;
	private String invoiceRefNo;
	private String refNo;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date invoiceDate;
	private Integer transportOrdertatus;
	private Integer transportationCount;
	private Integer transportInvoiceStatus;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date dueDate;
	private List<TStockTransportGroupInfoDto> transGroupInfo;

	public String getStockNo() {
		return stockNo;
	}

	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}

	public String getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(String currentLocation) {
		this.currentLocation = currentLocation;
	}

	public String getCurrrentLocationCustom() {
		return currrentLocationCustom;
	}

	public void setCurrrentLocationCustom(String currrentLocationCustom) {
		this.currrentLocationCustom = currrentLocationCustom;
	}

	public String getTransporter() {
		return transporter;
	}

	public void setTransporter(String transporter) {
		this.transporter = transporter;
	}

	public String getInvoiceRefNo() {
		return invoiceRefNo;
	}

	public void setInvoiceRefNo(String invoiceRefNo) {
		this.invoiceRefNo = invoiceRefNo;
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

	public Integer getTransportOrdertatus() {
		return transportOrdertatus;
	}

	public void setTransportOrdertatus(Integer transportOrdertatus) {
		this.transportOrdertatus = transportOrdertatus;
	}

	public Integer getTransportationCount() {
		return transportationCount;
	}

	public void setTransportationCount(Integer transportationCount) {
		this.transportationCount = transportationCount;
	}

	public Integer getTransportInvoiceStatus() {
		return transportInvoiceStatus;
	}

	public void setTransportInvoiceStatus(Integer transportInvoiceStatus) {
		this.transportInvoiceStatus = transportInvoiceStatus;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public List<TStockTransportGroupInfoDto> getTransGroupInfo() {
		return transGroupInfo;
	}

	public void setTransGroupInfo(List<TStockTransportGroupInfoDto> transGroupInfo) {
		this.transGroupInfo = transGroupInfo;
	}

}
