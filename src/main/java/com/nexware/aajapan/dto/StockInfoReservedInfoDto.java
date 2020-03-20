package com.nexware.aajapan.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.models.ReservedInfo;
import com.nexware.aajapan.models.TCustomer;
import com.nexware.aajapan.utils.AppUtil;

public class StockInfoReservedInfoDto {
	private String customerId;
	private String customerName;
	private String salesPersonId;
	private String salesPersonName;
	private Integer currency;
	private Double price;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date date;
	private Double exchangeRate;// value updated while updating price by special user

	public StockInfoReservedInfoDto() {

	}

	public StockInfoReservedInfoDto(ReservedInfo reservedInfo, TCustomer reservedCustomer) {
		prepareData(reservedInfo, reservedCustomer);
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
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

	public String getSalesPersonName() {
		return salesPersonName;
	}

	public void setSalesPersonName(String salesPersonName) {
		this.salesPersonName = salesPersonName;
	}

	public Double getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	private void prepareData(ReservedInfo reservedInfo, TCustomer reservedCustomer) {
		if (!AppUtil.isObjectEmpty(reservedInfo)) {
			this.currency = reservedInfo.getCurrency();
			this.date = reservedInfo.getDate();
			this.exchangeRate = reservedInfo.getExchangeRate();
			this.price = reservedInfo.getPrice();
			this.salesPersonId = reservedInfo.getSalesPersonId();
			this.salesPersonName = reservedInfo.getSelesPersonName();
		}
		if (!AppUtil.isObjectEmpty(reservedCustomer)) {
			this.customerId = reservedCustomer.getCode();
			this.customerName = reservedCustomer.getFirstName();
		}
	}
}
