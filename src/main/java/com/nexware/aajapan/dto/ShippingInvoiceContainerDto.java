package com.nexware.aajapan.dto;

import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ShippingInvoiceContainerDto {

	private List<TShippingInvoiceItemDto> invoiceItems;
	private List<Document> shipmentRequest;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date invoiceDate;
	private String forwarder;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date dueDate;
	private Double usdExchangeRate;
	private Double zarExchangeRate;

	public List<TShippingInvoiceItemDto> getInvoiceItems() {
		return invoiceItems;
	}

	public void setInvoiceItems(List<TShippingInvoiceItemDto> invoiceItems) {
		this.invoiceItems = invoiceItems;
	}

	public List<Document> getShipmentRequest() {
		return shipmentRequest;
	}

	public void setShipmentRequest(List<Document> shipmentRequest) {
		this.shipmentRequest = shipmentRequest;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getForwarder() {
		return forwarder;
	}

	public void setForwarder(String forwarder) {
		this.forwarder = forwarder;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public Double getUsdExchangeRate() {
		return usdExchangeRate;
	}

	public void setUsdExchangeRate(Double usdExchangeRate) {
		this.usdExchangeRate = usdExchangeRate;
	}

	public Double getZarExchangeRate() {
		return zarExchangeRate;
	}

	public void setZarExchangeRate(Double zarExchangeRate) {
		this.zarExchangeRate = zarExchangeRate;
	}

}
