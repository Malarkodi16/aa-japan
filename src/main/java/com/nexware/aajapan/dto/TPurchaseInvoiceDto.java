package com.nexware.aajapan.dto;

import java.util.Date;
import java.util.List;

public class TPurchaseInvoiceDto {

	private String invoiceNo;
	private String remitTo;
	private Date dueDate;
	List<TPurchaseInvoiceItemDto> items;

	public String getInvoiceNo() {
		return this.invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getRemitTo() {
		return this.remitTo;
	}

	public void setRemitTo(String remitTo) {
		this.remitTo = remitTo;
	}

	public Date getDueDate() {
		return this.dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public List<TPurchaseInvoiceItemDto> getItems() {
		return this.items;
	}

	public void setItems(List<TPurchaseInvoiceItemDto> items) {
		this.items = items;
	}

}
