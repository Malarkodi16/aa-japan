package com.nexware.aajapan.dto;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class OwnedTransactionTTDto {

	private String id;
	private Double amount;
	private String daybookId;// refer t_day_book
	private String salesInvoiceId;// refer t_sls_inv
	private String stockId;// refer t_sls_inv
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@JsonFormat(pattern = "dd-MM-yyyy")
	private LocalDate remitDate;
	private String remitter;
	private String bank;
	private String remarks;
	private Double balance;
	private String currency;
	private List<DayBookAllocationItemsDto> items;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Double getAmount() {
		return this.amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public LocalDate getRemitDate() {
		return this.remitDate;
	}

	public void setRemitDate(LocalDate remitDate) {
		this.remitDate = remitDate;
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

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public List<DayBookAllocationItemsDto> getItems() {
		return this.items;
	}

	public void setItems(List<DayBookAllocationItemsDto> items) {
		this.items = items;
	}

	public String getDaybookId() {
		return this.daybookId;
	}

	public void setDaybookId(String daybookId) {
		this.daybookId = daybookId;
	}

	public String getSalesInvoiceId() {
		return this.salesInvoiceId;
	}

	public void setSalesInvoiceId(String salesInvoiceId) {
		this.salesInvoiceId = salesInvoiceId;
	}

	public String getStockId() {
		return this.stockId;
	}

	public void setStockId(String stockId) {
		this.stockId = stockId;
	}

	public Double getBalance() {
		return this.balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public String getCurrency() {
		return this.currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

}
