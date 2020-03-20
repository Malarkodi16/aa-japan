package com.nexware.aajapan.models;

import java.util.Date;

import javax.persistence.Transient;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.listeners.EntityModelBase;

@Document(collection = "t_day_book")
public class TDayBook extends EntityModelBase {
	@Id
	private String id;
	@Indexed(unique = true)
	private String daybookId;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date remitDate;
	private Integer remitType;
	private Long coaNo;
	private String remitter;
	private String bank;
	private Integer currency;
	private Double amount;
	private Double amountWithOutBankCharge;
	private Double bankCharges;
	private String billOfExchange;
	private String lcNo;
	private String remarks;
	private String transactionType;
	private Integer status;
	private Integer cartaxClaimedStatus;

	private Integer clearingAccount;
	private Double exchangeRate;
	private Double salesExchangeRate;
	private Double SpecialExchangeRate;
	private String customer;
	private String customerId;
	private String salesPerson;
	private Double ownedAmount;
	@Transient
	private Double balanceAmount;
	private Integer isCustomerBankCharge;
	private String attachmentFilename;
	private Integer slipUpload;
	private Integer attachementViewed;
	private String attachmentDiskFilename;
	private String dollarRate;
	private String ausDollarRate;
	private String poundRate;

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

	public Long getCoaNo() {
		return this.coaNo;
	}

	public void setCoaNo(Long coaNo) {
		this.coaNo = coaNo;
	}

	public Integer getRemitType() {
		return this.remitType;
	}

	public void setRemitType(Integer remitType) {
		this.remitType = remitType;
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

	public Double getAmount() {
		return this.amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getBankCharges() {
		return this.bankCharges;
	}

	public void setBankCharges(Double bankCharges) {
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

	public String getDaybookId() {
		return this.daybookId;
	}

	public void setDaybookId(String daybookId) {
		this.daybookId = daybookId;
	}

	public String getCustomer() {
		return this.customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getSalesPerson() {
		return this.salesPerson;
	}

	public void setSalesPerson(String salesPerson) {
		this.salesPerson = salesPerson;
	}

	public Double getOwnedAmount() {
		return this.ownedAmount;
	}

	public void setOwnedAmount(Double ownedAmount) {
		this.ownedAmount = ownedAmount;
	}

	public Double getBalanceAmount() {
		this.balanceAmount = this.amount - this.ownedAmount;
		return this.balanceAmount;
	}

	public void setBalanceAmount(Double balanceAmount) {
		this.balanceAmount = balanceAmount;
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
	
	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public Integer getCartaxClaimedStatus() {
		return cartaxClaimedStatus;
	}

	public void setCartaxClaimedStatus(Integer cartaxClaimedStatus) {
		this.cartaxClaimedStatus = cartaxClaimedStatus;
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

	public Double getSalesExchangeRate() {
		return salesExchangeRate;
	}

	public void setSalesExchangeRate(Double salesExchangeRate) {
		this.salesExchangeRate = salesExchangeRate;
	}

	public Double getSpecialExchangeRate() {
		return SpecialExchangeRate;
	}

	public void setSpecialExchangeRate(Double specialExchangeRate) {
		SpecialExchangeRate = specialExchangeRate;
	}

	public String getDollarRate() {
		return dollarRate;
	}

	public void setDollarRate(String dollarRate) {
		this.dollarRate = dollarRate;
	}

	public String getAusDollarRate() {
		return ausDollarRate;
	}

	public void setAusDollarRate(String ausDollarRate) {
		this.ausDollarRate = ausDollarRate;
	}

	public String getPoundRate() {
		return poundRate;
	}

	public void setPoundRate(String poundRate) {
		this.poundRate = poundRate;
	}

	
}
