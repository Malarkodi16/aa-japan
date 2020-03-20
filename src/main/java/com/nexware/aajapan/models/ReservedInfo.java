package com.nexware.aajapan.models;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ReservedInfo {
	private String customerId;
	private String salesPersonId;
	private String selesPersonName;
	private Integer currency;
	private Double price;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date date;
	private Double exchangeRate;// value updated while updating price by special user

	public ReservedInfo(String customerId, String salesPersonId, String selesPersonName, Double price, Integer currency,
			Date date) {
		super();
		this.customerId = customerId;
		this.salesPersonId = salesPersonId;
		this.selesPersonName = selesPersonName;
		this.price = price;
		this.date = date;
		this.currency = currency;
	}

	public ReservedInfo() {

	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getSalesPersonId() {
		return salesPersonId;
	}

	public void setSalesPersonId(String salesPersonId) {
		this.salesPersonId = salesPersonId;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getCurrency() {
		return currency;
	}

	public void setCurrency(Integer currency) {
		this.currency = currency;
	}

	public String getSelesPersonName() {
		return selesPersonName;
	}

	public void setSelesPersonName(String selesPersonName) {
		this.selesPersonName = selesPersonName;
	}

	public Double getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

}
