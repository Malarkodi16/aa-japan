package com.nexware.aajapan.dto;

import org.bson.types.ObjectId;

public class MBankDto {

	private ObjectId id;
	private String bankSeq;
	private String bankName;
	private Integer currencyType;
	private Double yenBalance;// this is total balance
	private Double clearingBalance;
	private Integer accountType;
	private Long coaCode;
	private String currency;

	public ObjectId getId() {
		return this.id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getBankSeq() {
		return this.bankSeq;
	}

	public void setBankSeq(String bankSeq) {
		this.bankSeq = bankSeq;
	}

	public String getBankName() {
		return this.bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public Integer getCurrencyType() {
		return this.currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public Double getYenBalance() {
		return this.yenBalance;
	}

	public void setYenBalance(Double yenBalance) {
		this.yenBalance = yenBalance;
	}

	public Double getClearingBalance() {
		return this.clearingBalance;
	}

	public void setClearingBalance(Double clearingBalance) {
		this.clearingBalance = clearingBalance;
	}

	public Integer getAccountType() {
		return this.accountType;
	}

	public void setAccountType(Integer accountType) {
		this.accountType = accountType;
	}

	public Long getCoaCode() {
		return this.coaCode;
	}

	public void setCoaCode(Long coaCode) {
		this.coaCode = coaCode;
	}

	public String getCurrency() {
		return this.currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

}
