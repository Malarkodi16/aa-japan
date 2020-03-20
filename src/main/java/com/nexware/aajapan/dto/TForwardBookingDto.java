package com.nexware.aajapan.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TForwardBookingDto {

	private String id;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date bookingDate;
	private String bank;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date closingDate;
	private String currency;
	private String symbol;
	private Double amount;
	private Double currentExchangeRate;
	private Double bookingExchangeRate;
	private String bankId;
	private Integer currencySeq;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getBookingDate() {
		return this.bookingDate;
	}

	public void setBookingDate(Date bookingDate) {
		this.bookingDate = bookingDate;
	}

	public String getBank() {
		return this.bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public Date getClosingDate() {
		return this.closingDate;
	}

	public void setClosingDate(Date closingDate) {
		this.closingDate = closingDate;
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

	public Double getAmount() {
		return this.amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getCurrentExchangeRate() {
		return this.currentExchangeRate;
	}

	public void setCurrentExchangeRate(Double currentExchangeRate) {
		this.currentExchangeRate = currentExchangeRate;
	}

	public Double getBookingExchangeRate() {
		return this.bookingExchangeRate;
	}

	public void setBookingExchangeRate(Double bookingExchangeRate) {
		this.bookingExchangeRate = bookingExchangeRate;
	}

	public String getBankId() {
		return this.bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public Integer getCurrencySeq() {
		return this.currencySeq;
	}

	public void setCurrencySeq(Integer currencySeq) {
		this.currencySeq = currencySeq;
	}

}
