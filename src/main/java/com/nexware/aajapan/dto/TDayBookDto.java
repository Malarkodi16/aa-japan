package com.nexware.aajapan.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.listeners.EntityModelBase;

public class TDayBookDto extends EntityModelBase {

	private String id;
	private String daybookId;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date remitDate;
	private Integer remitType;
	private String remitTypeName;
	private Long coaNo;
	private String remitter;
	private String bank;
	private String bankName;
	private Integer currency;
	private String currencySymbol;
	private Double amount;
	private Double bankCharges;
	private String billOfExchange;
	private String lcNo;
	private String remarks;
	private String transactionType;
	private Integer status;
	private Double balance;
	private Integer clearingAccount;
	private Double exchangeRate;
	private Double salesExchangeRate;
	private Double SpecialExchangeRate;
	private String customer;
	private String salesPerson;
	private Double ownedAmount;
	private Double balanceAmount;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDaybookId() {
		return this.daybookId;
	}

	public void setDaybookId(String daybookId) {
		this.daybookId = daybookId;
	}

	public Date getRemitDate() {
		return this.remitDate;
	}

	public void setRemitDate(Date remitDate) {
		this.remitDate = remitDate;
	}

	public Integer getRemitType() {
		return this.remitType;
	}

	public void setRemitType(Integer remitType) {
		this.remitType = remitType;
	}

	public Long getCoaNo() {
		return this.coaNo;
	}

	public void setCoaNo(Long coaNo) {
		this.coaNo = coaNo;
	}

	public String getRemitter() {
		return this.remitter;
	}

	public void setRemitter(String remitter) {
		this.remitter = remitter;
	}

	public String getBank() {
		return this.bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getBankName() {
		return this.bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
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

	public Double getAmount() {
		return this.amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getBankCharges() {
		return this.bankCharges;
	}

	public void setBankCharges(Double bankCharges) {
		this.bankCharges = bankCharges;
	}

	public String getBillOfExchange() {
		return this.billOfExchange;
	}

	public void setBillOfExchange(String billOfExchange) {
		this.billOfExchange = billOfExchange;
	}

	public String getLcNo() {
		return this.lcNo;
	}

	public void setLcNo(String lcNo) {
		this.lcNo = lcNo;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getTransactionType() {
		return this.transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Double getBalance() {
		return this.balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
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

	public String getCustomer() {
		return this.customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getSalesPerson() {
		return this.salesPerson;
	}

	public void setSalesPerson(String salesPerson) {
		this.salesPerson = salesPerson;
	}

	public Double getOwnedAmount() {
		return this.ownedAmount;
	}

	public void setOwnedAmount(Double ownedAmount) {
		this.ownedAmount = ownedAmount;
	}

	public Double getBalanceAmount() {
		this.balanceAmount = this.amount - this.ownedAmount;
		return this.balanceAmount;
	}

	public void setBalanceAmount(Double balanceAmount) {
		this.balanceAmount = balanceAmount;
	}

	public String getRemitTypeName() {
		return this.remitTypeName;
	}

	public void setRemitTypeName(String remitTypeName) {
		this.remitTypeName = remitTypeName;
	}

	public Double getSalesExchangeRate() {
		return salesExchangeRate;
	}

	public void setSalesExchangeRate(Double salesExchangeRate) {
		this.salesExchangeRate = salesExchangeRate;
	}

	public Double getSpecialExchangeRate() {
		return SpecialExchangeRate;
	}

	public void setSpecialExchangeRate(Double specialExchangeRate) {
		SpecialExchangeRate = specialExchangeRate;
	}

}
