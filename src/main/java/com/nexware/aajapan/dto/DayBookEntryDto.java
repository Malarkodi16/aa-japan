package com.nexware.aajapan.dto;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class DayBookEntryDto {
	@Id
	private String id;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date remitDate;
	private String bank;
	private Integer clearingAccount;
	private Integer currency;
	private List<DayBookEntryItemsDto> items;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getRemitDate() {
		return remitDate;
	}

	public void setRemitDate(Date remitDate) {
		this.remitDate = remitDate;
	}

	public String getBank() {
		return this.bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public Integer getClearingAccount() {
		return this.clearingAccount;
	}

	public void setClearingAccount(Integer clearingAccount) {
		this.clearingAccount = clearingAccount;
	}

	public List<DayBookEntryItemsDto> getItems() {
		return this.items;
	}

	public void setItems(List<DayBookEntryItemsDto> items) {
		this.items = items;
	}

	public Integer getCurrency() {
		return currency;
	}

	public void setCurrency(Integer currency) {
		this.currency = currency;
	}
}
