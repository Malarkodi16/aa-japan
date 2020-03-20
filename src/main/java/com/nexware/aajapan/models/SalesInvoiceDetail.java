package com.nexware.aajapan.models;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.utils.AppUtil;

//t_sls_inv_dtl
public class SalesInvoiceDetail {

	private String id;
	private String stockNo;
	private String chassisNo;
	private String maker;
	private String transmission;
	private String fuel;
	private String driven;
	private Double cc;
	private String type;
	private String model;
	private String color;
	@JsonFormat(pattern = "yyyy/MM")
	@DateTimeFormat(pattern = "yyyy/MM")
	private Date firstRegDate;
	private Double fob;
	private Double insurance;
	private Double shipping;
	private Double freight;
	private Double m3;
	private Double profit;
	private Double total;
	private Integer status;
	private String currencySymbol;;
	private List<TSalesInvoiceDetailsTransaction> transactions;

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

	public Double getFob() {
		return AppUtil.ifNull(this.fob, 0.0);
	}

	public void setFob(Double fob) {
		this.fob = fob;
	}

	public Double getInsurance() {
		return AppUtil.ifNull(this.insurance, 0.0);
	}

	public void setInsurance(Double insurance) {
		this.insurance = insurance;
	}

	public Double getShipping() {
		return AppUtil.ifNull(this.shipping, 0.0);
	}

	public void setShipping(Double shipping) {
		this.shipping = shipping;
	}

	public Double getFreight() {
		return AppUtil.ifNull(this.freight, 0.0);
	}

	public void setFreight(Double freight) {
		this.freight = freight;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public List<TSalesInvoiceDetailsTransaction> getTransactions() {
		return this.transactions;
	}

	public void setTransactions(List<TSalesInvoiceDetailsTransaction> transactions) {
		this.transactions = transactions;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getFirstRegDate() {
		return this.firstRegDate;
	}

	public void setFirstRegDate(Date firstRegDate) {
		this.firstRegDate = firstRegDate;
	}

	public String getTransmission() {
		return this.transmission;
	}

	public void setTransmission(String transmission) {
		this.transmission = transmission;
	}

	public String getFuel() {
		return this.fuel;
	}

	public void setFuel(String fuel) {
		this.fuel = fuel;
	}

	public String getDriven() {
		return this.driven;
	}

	public void setDriven(String driven) {
		this.driven = driven;
	}

	public Double getCc() {
		return this.cc;
	}

	public void setCc(Double cc) {
		this.cc = cc;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCurrencySymbol() {
		return this.currencySymbol;
	}

	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}

	public Double getM3() {
		return this.m3;
	}

	public void setM3(Double m3) {
		this.m3 = m3;
	}

	public String getColor() {
		return this.color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Double getProfit() {
		return this.profit;
	}

	public void setProfit(Double profit) {
		this.profit = profit;
	}

}
