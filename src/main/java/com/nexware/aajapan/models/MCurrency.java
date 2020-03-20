package com.nexware.aajapan.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.nexware.aajapan.listeners.EntityModelBase;

@Document(collection = "m_currency")
public class MCurrency extends EntityModelBase {
	@Id
	private String id;
	@Indexed(unique = true)
	private int currencySeq;// mapped in Constants.java
	private String currency;
	private String symbol;
	private String symbolHtml;
	private Double exchangeRate;
	private Double salesExchangeRate;
	private Double specialExchangeRate;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getCurrencySeq() {
		return this.currencySeq;
	}

	public void setCurrencySeq(int currencySeq) {
		this.currencySeq = currencySeq;
	}

	public String getCurrency() {
		return this.currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getSymbol() {
		return this.symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getSymbolHtml() {
		return this.symbolHtml;
	}

	public void setSymbolHtml(String symbolHtml) {
		this.symbolHtml = symbolHtml;
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
