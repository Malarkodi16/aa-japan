package com.nexware.aajapan.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TStockTransportGroupInfoDto {

	private String fromLocation;
	private String fromLocationCustom;
	private String toLocation;
	private String toLocationCustom;
	private String forwarder;
	private String transporter;
	private Integer transportOrdertatus;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private String dueDate;
	private Integer transportInvoiceStatus;
	private String invoiceAttachmentFilename;
	private String invoiceAttachmentDiskFilename;
	private Double estimatedAmount;
	private Double actualAmount;

	public String getFromLocation() {
		return fromLocation;
	}

	public void setFromLocation(String fromLocation) {
		this.fromLocation = fromLocation;
	}

	public String getFromLocationCustom() {
		return fromLocationCustom;
	}

	public void setFromLocationCustom(String fromLocationCustom) {
		this.fromLocationCustom = fromLocationCustom;
	}

	public String getToLocation() {
		return toLocation;
	}

	public void setToLocation(String toLocation) {
		this.toLocation = toLocation;
	}

	public String getToLocationCustom() {
		return toLocationCustom;
	}

	public void setToLocationCustom(String toLocationCustom) {
		this.toLocationCustom = toLocationCustom;
	}

	public String getForwarder() {
		return forwarder;
	}

	public void setForwarder(String forwarder) {
		this.forwarder = forwarder;
	}

	public String getTransporter() {
		return transporter;
	}

	public void setTransporter(String transporter) {
		this.transporter = transporter;
	}

	public Integer getTransportOrdertatus() {
		return transportOrdertatus;
	}

	public void setTransportOrdertatus(Integer transportOrdertatus) {
		this.transportOrdertatus = transportOrdertatus;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public Integer getTransportInvoiceStatus() {
		return transportInvoiceStatus;
	}

	public void setTransportInvoiceStatus(Integer transportInvoiceStatus) {
		this.transportInvoiceStatus = transportInvoiceStatus;
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

	public Double getEstimatedAmount() {
		return estimatedAmount;
	}

	public void setEstimatedAmount(Double estimatedAmount) {
		this.estimatedAmount = estimatedAmount;
	}

	public Double getActualAmount() {
		return actualAmount;
	}

	public void setActualAmount(Double actualAmount) {
		this.actualAmount = actualAmount;
	}

}
