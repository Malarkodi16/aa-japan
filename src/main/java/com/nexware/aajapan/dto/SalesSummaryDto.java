package com.nexware.aajapan.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.utils.AppUtil;

public class SalesSummaryDto {

	private String id;
	private String stockNo;
	private String chassisNo;
	private String customerId;
	private String salesPerson;
	private String customerName;
	private String salesPersonName;
	private String maker;
	private String model;
	private Double exchangeRate;
	private Double total;
	private Integer currency;
	private String currencySymbol;
	private String type;
	private Double sellingPrice;
	private Double exchangeRateSellingPrice;
	private Double costOfGoods;
	private Integer status;
	@JsonFormat(pattern = "dd/MM/yyyy")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date purchaseDate;
	@JsonFormat(pattern = "dd/MM/yyyy")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date soldDate;
	private Double margin;
	private Double marginPercentage;
	private String destinationCountry;
	private String locationId;
	private String location;
	private String destinationPort;
	private Integer isBidding;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date eta;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date etd;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStockNo() {
		return this.stockNo;
	}

	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}

	public String getChassisNo() {
		return this.chassisNo;
	}

	public void setChassisNo(String chassisNo) {
		this.chassisNo = chassisNo;
	}

	public String getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return this.customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getSalesPersonName() {
		return this.salesPersonName;
	}

	public void setSalesPersonName(String salesPersonName) {
		this.salesPersonName = salesPersonName;
	}

	public String getMaker() {
		return this.maker;
	}

	public void setMaker(String maker) {
		this.maker = maker;
	}

	public String getModel() {
		return this.model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Double getTotal() {
		return this.total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getPurchaseDate() {
		return this.purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public Date getSoldDate() {
		return this.soldDate;
	}

	public void setSoldDate(Date soldDate) {
		this.soldDate = soldDate;
	}

	public Double getMargin() {
		this.margin = AppUtil.ifNull(this.getExchangeRateSellingPrice(), 0.0)
				- AppUtil.ifNull(this.getCostOfGoods(), 0.0);
		return this.margin;
	}

	public void setMargin(Double margin) {
		this.margin = margin;
	}

	public Double getMarginPercentage() {
		this.marginPercentage = (AppUtil.ifNull(this.getMargin(), 0.0)
				/ AppUtil.ifNull(this.getExchangeRateSellingPrice(), 0.0)) * 100;
		return this.marginPercentage;
	}

	public void setMarginPercentage(Double marginPercentage) {
		this.marginPercentage = marginPercentage;
	}

	public String getDestinationCountry() {
		return this.destinationCountry;
	}

	public void setDestinationCountry(String destinationCountry) {
		this.destinationCountry = destinationCountry;
	}

	public String getDestinationPort() {
		return this.destinationPort;
	}

	public void setDestinationPort(String destinationPort) {
		this.destinationPort = destinationPort;
	}

	public Double getSellingPrice() {
		this.sellingPrice = AppUtil.ifNull(this.getTotal(), 0.0);
		return this.sellingPrice;
	}

	public void setSellingPrice(Double sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	public Double getCostOfGoods() {
		return this.costOfGoods;
	}

	public void setCostOfGoods(Double costOfGoods) {
		this.costOfGoods = costOfGoods;
	}

	public String getSalesPerson() {
		return this.salesPerson;
	}

	public void setSalesPerson(String salesPerson) {
		this.salesPerson = salesPerson;
	}

	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLocationId() {
		return this.locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public Integer getCurrency() {
		return this.currency;
	}

	public void setCurrency(Integer currency) {
		this.currency = currency;
	}

	public String getCurrencySymbol() {
		return this.currencySymbol;
	}

	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}

	public Double getExchangeRate() {
		return this.exchangeRate;
	}

	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public Double getExchangeRateSellingPrice() {
		this.exchangeRateSellingPrice = AppUtil.ifNull(this.getSellingPrice(), 0.0)
				* AppUtil.ifNull(this.getExchangeRate(), 0.0);
		return this.exchangeRateSellingPrice;
	}

	public void setExchangeRateSellingPrice(Double exchangeRateSellingPrice) {

		this.exchangeRateSellingPrice = exchangeRateSellingPrice;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getIsBidding() {
		return isBidding;
	}

	public void setIsBidding(Integer isBidding) {
		this.isBidding = isBidding;
	}

}
