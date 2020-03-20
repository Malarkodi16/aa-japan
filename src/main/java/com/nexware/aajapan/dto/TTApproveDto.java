package com.nexware.aajapan.dto;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TTApproveDto {

	private String id;
	private String stockNo;
	private String salesInvoiceId;
	private String daybookId;
	private String chassisNo;
	private String coaCode;
	private String bank;
	private String remitter;
	private String amount;
	private Double allocatedAmount;
	private Double advanceOwned;
	private String customerId;
	private String customer;
	private String customerFn;
	private String customerLn;
	private String allocationType;
	private String salesPerson;
	@CreatedDate
	@JsonFormat(pattern = "dd-MM-yyyy")
	protected Date createdDate;
	private Integer currency;
	private String currencySymbol;
	private Integer customerCurrency;
	private String customerCurrencySymbol;
	private Integer clearingAccount;
	private Double exchangeRate;
	private String bankSeq;
	private String salesPersonId;

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

	public String getSalesInvoiceId() {
		return this.salesInvoiceId;
	}

	public String getDaybookId() {
		return daybookId;
	}

	public void setDaybookId(String daybookId) {
		this.daybookId = daybookId;
	}

	public void setSalesInvoiceId(String salesInvoiceId) {
		this.salesInvoiceId = salesInvoiceId;
	}

	public String getChassisNo() {
		return this.chassisNo;
	}

	public void setChassisNo(String chassisNo) {
		this.chassisNo = chassisNo;
	}

	public String getCoaCode() {
		return this.coaCode;
	}

	public void setCoaCode(String coaCode) {
		this.coaCode = coaCode;
	}

	public String getBank() {
		return this.bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getRemitter() {
		return this.remitter;
	}

	public void setRemitter(String remitter) {
		this.remitter = remitter;
	}

	public String getAmount() {
		return this.amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCustomer() {
		this.customer = this.customerFn + " " + this.customerLn;
		return this.customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getCustomerFn() {
		return this.customerFn;
	}

	public void setCustomerFn(String customerFn) {
		this.customerFn = customerFn;
	}

	public String getCustomerLn() {
		return this.customerLn;
	}

	public void setCustomerLn(String customerLn) {
		this.customerLn = customerLn;
	}

	public String getAllocationType() {
		return this.allocationType;
	}

	public void setAllocationType(String allocationType) {
		this.allocationType = allocationType;
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

	public Integer getCurrency() {
		return this.currency;
	}

	public void setCurrency(Integer currency) {
		this.currency = currency;
	}

	public Integer getClearingAccount() {
		return this.clearingAccount;
	}

	public void setClearingAccount(Integer clearingAccount) {
		this.clearingAccount = clearingAccount;
	}

	public Double getExchangeRate() {
		return this.exchangeRate;
	}

	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public String getCurrencySymbol() {
		return this.currencySymbol;
	}

	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}

	public String getBankSeq() {
		return bankSeq;
	}

	public void setBankSeq(String bankSeq) {
		this.bankSeq = bankSeq;
	}

	public String getSalesPersonId() {
		return salesPersonId;
	}

	public void setSalesPersonId(String salesPersonId) {
		this.salesPersonId = salesPersonId;
	}

	public Double getAllocatedAmount() {
		return allocatedAmount;
	}

	public void setAllocatedAmount(Double allocatedAmount) {
		this.allocatedAmount = allocatedAmount;
	}

	public String getCustomerCurrencySymbol() {
		return customerCurrencySymbol;
	}

	public void setCustomerCurrencySymbol(String customerCurrencySymbol) {
		this.customerCurrencySymbol = customerCurrencySymbol;
	}

	public Integer getCustomerCurrency() {
		return customerCurrency;
	}

	public void setCustomerCurrency(Integer customerCurrency) {
		this.customerCurrency = customerCurrency;
	}

	public Double getAdvanceOwned() {
		return advanceOwned;
	}

	public void setAdvanceOwned(Double advanceOwned) {
		this.advanceOwned = advanceOwned;
	}

}
