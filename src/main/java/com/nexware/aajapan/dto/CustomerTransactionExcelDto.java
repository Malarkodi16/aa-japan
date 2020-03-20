package com.nexware.aajapan.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class CustomerTransactionExcelDto {

	private String id;
	@JsonFormat(pattern = "MMMM yyyy")
	private Date date;
	private String customerId;
	private String currencySymbol;
	private CustomerTransactionExcelItemDto dataItems;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCurrencySymbol() {
		return currencySymbol;
	}

	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}

	public CustomerTransactionExcelItemDto getDataItems() {
		return dataItems;
	}

	public void setDataItems(CustomerTransactionExcelItemDto dataItems) {
		this.dataItems = dataItems;
	}

}
