package com.nexware.aajapan.models;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.listeners.EntityModelBase;

@Document(collection = "t_frwd_bkng")
public class TForwardBooking extends EntityModelBase{
	@Id
	private String id;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date bookingDate;
	@Indexed
	private String bank;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date closingDate;
	private Integer currency;
	private Double amount;
	private Double currentExchangeRate;
	private Double bookingExchangeRate;

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

	public Integer getCurrency() {
		return this.currency;
	}

	public void setCurrency(Integer currency) {
		this.currency = currency;
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

}
