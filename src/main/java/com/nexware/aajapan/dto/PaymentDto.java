package com.nexware.aajapan.dto;

public class PaymentDto {
	private String invoiceType;
	private Integer count;

	public String getInvoiceType() {
		return this.invoiceType;
	}

	public Integer getCount() {
		return this.count;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

}
