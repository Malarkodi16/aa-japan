package com.nexware.aajapan.dto;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

public class AddPaymentDto {
	private String stockNo;
	private String invoiceType;
	private String remitTo;
	private String invoiceNo;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date invoiceDate;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date dueDate;

	private List<AddPaymentItemsDto> items;

	public String getInvoiceType() {
		return this.invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public String getRemitTo() {
		return this.remitTo;
	}

	public void setRemitTo(String remitTo) {
		this.remitTo = remitTo;
	}

	public String getInvoiceNo() {
		return this.invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public Date getInvoiceDate() {
		return this.invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public Date getDueDate() {
		return this.dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public List<AddPaymentItemsDto> getItems() {
		return this.items;
	}

	public void setItems(List<AddPaymentItemsDto> items) {
		this.items = items;
	}

	public String getStockNo() {
		return this.stockNo;
	}

	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}

}
