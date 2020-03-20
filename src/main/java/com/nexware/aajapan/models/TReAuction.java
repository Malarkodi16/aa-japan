package com.nexware.aajapan.models;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.listeners.EntityModelBase;

@Document(collection = "t_re_actn")
public class TReAuction extends EntityModelBase {
	@Id
	private String id;
	private String stockNo;
	private String chassisNo;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date reauctionDate;
	private int invoiceStatus;
	private String auctionCompany;
	private ObjectId auctionHouse;
	private Integer status;
	private Double soldPrice;
	private Double tax;
	private Double shuppinCommission;
	private Double shuppinTax;
	private Double soldCommission;
	private Double soldCommTax;
	// private Double commission;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date invoiceDate;
	private Double recycleAmount;
	private Double nagareCharge;
	private Double auctionShippingCharge;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStockNo() {
		return stockNo;
	}

	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}

	public String getChassisNo() {
		return chassisNo;
	}

	public void setChassisNo(String chassisNo) {
		this.chassisNo = chassisNo;
	}

	public Date getReauctionDate() {
		return reauctionDate;
	}

	public void setReauctionDate(Date reauctionDate) {
		this.reauctionDate = reauctionDate;
	}

	public int getInvoiceStatus() {
		return invoiceStatus;
	}

	public void setInvoiceStatus(int invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}

	public String getAuctionCompany() {
		return auctionCompany;
	}

	public void setAuctionCompany(String auctionCompany) {
		this.auctionCompany = auctionCompany;
	}

	public ObjectId getAuctionHouse() {
		return auctionHouse;
	}

	public void setAuctionHouse(ObjectId auctionHouse) {
		this.auctionHouse = auctionHouse;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Double getSoldPrice() {
		return soldPrice;
	}

	public void setSoldPrice(Double soldPrice) {
		this.soldPrice = soldPrice;
	}

	public Double getTax() {
		return tax;
	}

	public void setTax(Double tax) {
		this.tax = tax;
	}

	public Double getShuppinCommission() {
		return shuppinCommission;
	}

	public void setShuppinCommission(Double shuppinCommission) {
		this.shuppinCommission = shuppinCommission;
	}

	public Double getShuppinTax() {
		return shuppinTax;
	}

	public void setShuppinTax(Double shuppinTax) {
		this.shuppinTax = shuppinTax;
	}

	public Double getSoldCommission() {
		return soldCommission;
	}

	public void setSoldCommission(Double soldCommission) {
		this.soldCommission = soldCommission;
	}

	public Double getSoldCommTax() {
		return soldCommTax;
	}

	public void setSoldCommTax(Double soldCommTax) {
		this.soldCommTax = soldCommTax;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;

	}

	public Double getRecycleAmount() {
		return recycleAmount;
	}

	public void setRecycleAmount(Double recycleAmount) {
		this.recycleAmount = recycleAmount;
	}

	public Double getNagareCharge() {
		return nagareCharge;
	}

	public void setNagareCharge(Double nagareCharge) {
		this.nagareCharge = nagareCharge;
	}

	public Double getAuctionShippingCharge() {
		return auctionShippingCharge;
	}

	public void setAuctionShippingCharge(Double auctionShippingCharge) {
		this.auctionShippingCharge = auctionShippingCharge;
	}

}
