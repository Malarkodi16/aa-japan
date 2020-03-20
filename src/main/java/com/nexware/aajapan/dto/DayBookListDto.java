package com.nexware.aajapan.dto;

import java.time.LocalDate;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.core.Constants;

public class DayBookListDto {

	private String id;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@JsonFormat(pattern = "dd-MM-yyyy")
	@Temporal(TemporalType.DATE)
	private LocalDate remitDate;
	private String daybookId;
	private String remitType;
	private Integer remitTypeId;
	private Long coaNo;
	private String remitter;
	private String bank;
	private String bankName;
	private Integer currency;
	private String currencySymbol;
	private double amount;
	private Double amountWithOutBankCharge;
	private double bankCharges;
	private String billOfExchange;
	private String lcNo;
	private String staff;
	private String customer;
	private String customerId;
	private String remarks;
	private String transactionType; // Debit or Credit
	private Integer status;
	private Double balance;
	private Integer clearingAccount;
	private Integer isCustomerBankCharge;
	private Double exchangeRate;
	private String carTaxApprove;
	private String attachmentFilename;
	private Integer slipUpload;
	private Integer attachementViewed;
	private String attachmentDiskFilename;
	private String attachmentDirectory;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LocalDate getRemitDate() {
		return this.remitDate;
	}

	public void setRemitDate(LocalDate remitDate) {
		this.remitDate = remitDate;
	}

	public String getDaybookId() {
		return daybookId;
	}

	public void setDaybookId(String daybookId) {
		this.daybookId = daybookId;
	}

	public String getRemitType() {
		return this.remitType;
	}

	public void setRemitType(String remitType) {
		this.remitType = remitType;
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

	public String getBank() {
		return this.bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public Integer getCurrency() {
		return this.currency;
	}

	public void setCurrency(Integer currency) {
		this.currency = currency;
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

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getTransactionType() {
		return this.transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Double getBalance() {
		return this.balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Integer getClearingAccount() {
		return this.clearingAccount;
	}

	public void setClearingAccount(Integer clearingAccount) {
		this.clearingAccount = clearingAccount;
	}

	public Double getExchangeRate() {
		return this.exchangeRate;
	}

	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public String getCurrencySymbol() {
		return this.currencySymbol;
	}

	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}

	public String getBankName() {
		return this.bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public Double getAmountWithOutBankCharge() {
		return amountWithOutBankCharge;
	}

	public void setAmountWithOutBankCharge(Double amountWithOutBankCharge) {
		this.amountWithOutBankCharge = amountWithOutBankCharge;
	}

	public Integer getIsCustomerBankCharge() {
		return isCustomerBankCharge;
	}

	public void setIsCustomerBankCharge(Integer isCustomerBankCharge) {
		this.isCustomerBankCharge = isCustomerBankCharge;
	}

	public Integer getRemitTypeId() {
		return remitTypeId;
	}

	public void setRemitTypeId(Integer remitTypeId) {
		this.remitTypeId = remitTypeId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCarTaxApprove() {
		return carTaxApprove;
	}

	public void setCarTaxApprove(String carTaxApprove) {
		this.carTaxApprove = carTaxApprove;
	}

	public String getAttachmentFilename() {
		return attachmentFilename;
	}

	public void setAttachmentFilename(String attachmentFilename) {
		this.attachmentFilename = attachmentFilename;
	}

	public Integer getSlipUpload() {
		return slipUpload;
	}

	public void setSlipUpload(Integer slipUpload) {
		this.slipUpload = slipUpload;
	}

	public Integer getAttachementViewed() {
		return attachementViewed;
	}

	public void setAttachementViewed(Integer attachementViewed) {
		this.attachementViewed = attachementViewed;
	}

	public String getAttachmentDiskFilename() {
		return attachmentDiskFilename;
	}

	public void setAttachmentDiskFilename(String attachmentDiskFilename) {
		this.attachmentDiskFilename = attachmentDiskFilename;
	}

	public String getAttachmentDirectory() {
		attachmentDirectory = Constants.ATTACHMENT_DIRECTORY_DAYBOOK;
		return attachmentDirectory;
	}

	public void setAttachmentDirectory(String attachmentDirectory) {
		this.attachmentDirectory = attachmentDirectory;
	}

}
