package com.nexware.aajapan.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.utils.AppUtil;

public class RecentSalesDto {

	private String id;
	private String invoiceNo;
	private String customerId;
	private String stockNo;
	private String customerFN;
	private String fCustomerName;
	private String fConsigneeName;
	private String fNotifyName;
	private String paymentType;
	private Double salesAmount;
	private Double purchaseCostTotal;
	private String salesPersonId;
	private String salesPerson;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date createdDate;
	private String maker;
	private String model;
	private String currencySymbol;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getInvoiceNo() {
		return this.invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getStockNo() {
		return this.stockNo;
	}

	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}

	public String getCustomerFN() {
		return this.customerFN;
	}

	public void setCustomerFN(String customerFN) {
		this.customerFN = customerFN;
	}

	public String getfCustomerName() {
		return this.fCustomerName;
	}

	public void setfCustomerName(String fCustomerName) {
		this.fCustomerName = fCustomerName;
	}

	public String getfConsigneeName() {
		return this.fConsigneeName;
	}

	public void setfConsigneeName(String fConsigneeName) {
		this.fConsigneeName = fConsigneeName;
	}

	public String getfNotifyName() {
		return this.fNotifyName;
	}

	public void setfNotifyName(String fNotifyName) {
		this.fNotifyName = fNotifyName;
	}

	public String getPaymentType() {
		return this.paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public Double getSalesAmount() {
		return this.salesAmount;
	}

	public void setSalesAmount(Double salesAmount) {
		this.salesAmount = salesAmount;
	}

	public Double getPurchaseCostTotal() {
		return this.purchaseCostTotal;
	}

	public void setPurchaseCostTotal(Double purchaseCostTotal) {
		this.purchaseCostTotal = purchaseCostTotal;
	}

	public String getSalesPersonId() {
		return this.salesPersonId;
	}

	public void setSalesPersonId(String salesPersonId) {
		this.salesPersonId = salesPersonId;
	}

	public String getSalesPerson() {
		return this.salesPerson;
	}

	public void setSalesPerson(String salesPerson) {
		this.salesPerson = salesPerson;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
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

	public String getCurrencySymbol() {
		return this.currencySymbol;
	}

	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}

}
