package com.nexware.aajapan.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nexware.aajapan.models.MCurrency;
import com.nexware.aajapan.utils.AppUtil;

public class ReceivableAmountDto {

	private String code;
	private String firstName;
	private String lastName;
	private String fullName;
	private Double creditBalance;
	private Double balance;
	private Double total;
	private Double amountReceived;
	private Double receivableAmount;
	private List<Map<String, String>> salesDetails = new ArrayList<>();
	private MCurrency currencyDetails;

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFullName() {
		this.fullName = AppUtil.ifNull(this.firstName, "") + " " + AppUtil.ifNull(this.lastName, "");
		return this.fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Double getCreditBalance() {
		return this.creditBalance;
	}

	public void setCreditBalance(Double creditBalance) {
		this.creditBalance = creditBalance;
	}

	public Double getBalance() {
		return this.balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Double getReceivableAmount() {
		this.receivableAmount = this.total - this.amountReceived;
		return this.receivableAmount;
	}

	public void setReceivableAmount(Double receivableAmount) {
		this.receivableAmount = receivableAmount;
	}

	public List<Map<String, String>> getSalesDetails() {
		return this.salesDetails;
	}

	public void setSalesDetails(List<Map<String, String>> salesDetails) {
		this.salesDetails = salesDetails;
	}

	public MCurrency getCurrencyDetails() {
		return this.currencyDetails;
	}

	public void setCurrencyDetails(MCurrency currencyDetails) {
		this.currencyDetails = currencyDetails;
	}

	public Double getTotal() {
		return this.total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Double getAmountReceived() {
		return this.amountReceived;
	}

	public void setAmountReceived(Double amountReceived) {
		this.amountReceived = amountReceived;
	}

}
