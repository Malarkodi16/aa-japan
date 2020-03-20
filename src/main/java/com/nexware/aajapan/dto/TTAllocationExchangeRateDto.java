package com.nexware.aajapan.dto;

public class TTAllocationExchangeRateDto {

	private String currencySymbol;
	private Double actualAmount;
	private Double exchangeRate1;
	private Double exchangeRate2;
	
	public String getCurrencySymbol() {
		return currencySymbol;
	}
	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}
	public Double getActualAmount() {
		return actualAmount;
	}
	public void setActualAmount(Double actualAmount) {
		this.actualAmount = actualAmount;
	}
	public Double getExchangeRate1() {
		return exchangeRate1;
	}
	public void setExchangeRate1(Double exchangeRate1) {
		this.exchangeRate1 = exchangeRate1;
	}
	public Double getExchangeRate2() {
		return exchangeRate2;
	}
	public void setExchangeRate2(Double exchangeRate2) {
		this.exchangeRate2 = exchangeRate2;
	}
	
}
