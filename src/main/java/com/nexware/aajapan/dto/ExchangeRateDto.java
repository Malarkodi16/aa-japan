package com.nexware.aajapan.dto;

public class ExchangeRateDto {

	private int currency;
	private Double exchangeRate;
	private Double salesExchangeRate;
	private Double specialExchangeRate;

	public int getCurrency() {
		return this.currency;
	}

	public void setCurrency(int currency) {
		this.currency = currency;
	}

	public Double getExchangeRate() {
		return this.exchangeRate;
	}

	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public Double getSalesExchangeRate() {
		return this.salesExchangeRate;
	}

	public void setSalesExchangeRate(Double salesExchangeRate) {
		this.salesExchangeRate = salesExchangeRate;
	}

	public Double getSpecialExchangeRate() {
		return this.specialExchangeRate;
	}

	public void setSpecialExchangeRate(Double specialExchangeRate) {
		this.specialExchangeRate = specialExchangeRate;
	}

}
