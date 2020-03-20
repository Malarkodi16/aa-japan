package com.nexware.aajapan.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.nexware.aajapan.listeners.EntityModelBase;

@Document(collection = "t_exhg_rate")
public class TExchangeRate extends EntityModelBase {
	@Id
	private String id;
	@Indexed
	private int currency;
	private Double exchangeRate;
	private Double salesExchangeRate;
	private Double specialExchangeRate;

	public TExchangeRate(int currency, Double exchangeRate, Double salesExchangeRate, Double specialExchangeRate) {
		this.currency = currency;
		this.exchangeRate = exchangeRate;
		this.salesExchangeRate = salesExchangeRate;
		this.specialExchangeRate = specialExchangeRate;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

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
