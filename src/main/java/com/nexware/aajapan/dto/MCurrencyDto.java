package com.nexware.aajapan.dto;

import java.util.List;

public class MCurrencyDto {
	private int currencySeq;
	private String currency;
	private String symbol;
	private List<MBankDto> bankDetails;
	private Double totalAmount;

	public int getCurrencySeq() {
		return currencySeq;
	}

	public void setCurrencySeq(int currencySeq) {
		this.currencySeq = currencySeq;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public List<MBankDto> getBankDetails() {
		return bankDetails;
	}

	public void setBankDetails(List<MBankDto> bankDetails) {
		this.bankDetails = bankDetails;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

}
