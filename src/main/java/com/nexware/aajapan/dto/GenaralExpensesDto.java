package com.nexware.aajapan.dto;

import java.util.Date;

public class GenaralExpensesDto {
	private Date date;
	private String invoiceNo;
	private String remitTo;
	private Date dueDate;
	private String formattedCategory;
	private String category;
	private String tkcCode;
	private String tkcDescription;
	private String description;
	private Double amountInYen;
	private Double taxAmount;
	private Double totalAmount;
	private String sourceCurrency;
	private Double amount;
	private Double exchangeRate;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getRemitTo() {
		return remitTo;
	}

	public void setRemitTo(String remitTo) {
		this.remitTo = remitTo;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getSourceCurrency() {
		return sourceCurrency;
	}

	public void setSourceCurrency(String sourceCurrency) {
		this.sourceCurrency = sourceCurrency;
	}

	public Double getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public String getFormattedCategory() {
		this.formattedCategory = category + "[" + tkcCode + "] - " + tkcDescription;
		return formattedCategory;
	}

	public void setFormattedCategory(String formattedCategory) {
		this.formattedCategory = formattedCategory;
	}

	public String getTkcCode() {
		return tkcCode;
	}

	public void setTkcCode(String tkcCode) {
		this.tkcCode = tkcCode;
	}

	public String getTkcDescription() {
		return tkcDescription;
	}

	public void setTkcDescription(String tkcDescription) {
		this.tkcDescription = tkcDescription;
	}

	public Double getAmountInYen() {
		return amountInYen;
	}

	public void setAmountInYen(Double amountInYen) {
		this.amountInYen = amountInYen;
	}

}
