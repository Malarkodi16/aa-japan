package com.nexware.aajapan.dto;

import org.springframework.web.multipart.MultipartFile;

import com.nexware.aajapan.models.Attachment;

public class DayBookEntryItemsDto {

	private String transactionType; // Debit or Credit
	private Long coaNo;	
	private String remitter;
	private double amount;
	private double amountWithOutBankCharge;
	private double bankCharges;
	private String billOfExchange;
	private String lcNo;
	private String staff;
	private String customer;
	private String remarks;
	private Integer remitType;
	private Integer customerBankCharge;
	private MultipartFile attachment;

	public String getTransactionType() {
		return this.transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public Long getCoaNo() {
		return this.coaNo;
	}

	public void setCoaNo(Long coaNo) {
		this.coaNo = coaNo;
	}

	public String getRemitter() {
		return this.remitter;
	}

	public void setRemitter(String remitter) {
		this.remitter = remitter;
	}

	public double getAmount() {
		return this.amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getBankCharges() {
		return this.bankCharges;
	}

	public void setBankCharges(double bankCharges) {
		this.bankCharges = bankCharges;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getBillOfExchange() {
		return this.billOfExchange;
	}

	public void setBillOfExchange(String billOfExchange) {
		this.billOfExchange = billOfExchange;
	}

	public String getLcNo() {
		return this.lcNo;
	}

	public void setLcNo(String lcNo) {
		this.lcNo = lcNo;
	}

	public String getStaff() {
		return this.staff;
	}

	public void setStaff(String staff) {
		this.staff = staff;
	}

	public String getCustomer() {
		return this.customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public Integer getRemitType() {
		return this.remitType;
	}

	public void setRemitType(Integer remitType) {
		this.remitType = remitType;
	}

	public double getAmountWithOutBankCharge() {
		return amountWithOutBankCharge;
	}

	public void setAmountWithOutBankCharge(double amountWithOutBankCharge) {
		this.amountWithOutBankCharge = amountWithOutBankCharge;
	}

	public Integer getCustomerBankCharge() {
		return customerBankCharge;
	}

	public void setCustomerBankCharge(Integer customerBankCharge) {
		this.customerBankCharge = customerBankCharge;
	}

	public MultipartFile getAttachment() {
		return attachment;
	}

	public void setAttachment(MultipartFile attachment) {
		this.attachment = attachment;
	}	

}
