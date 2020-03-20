package com.nexware.aajapan.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.nexware.aajapan.listeners.EntityModelBase;

@Document(collection = "t_bnk_trsnsctn")
public class TBankTransaction extends EntityModelBase {

	@Id
	private String id;
	@Indexed
	private String paymentVoucherNo;
	@Indexed
	private String bankId;
	private int currency;
	private String refNo;
	private String remitTo;
	private String description;
	private Integer transactionType;
	private Double amount;
	private Double balance;
	private Double closingBalance;
	private Integer clearingAccount;

	public TBankTransaction(String paymentVoucherNo, String bankId, int currency, String refNo, String description,
			Integer transactionType, Double amount, Double balance, Double closingBalance) {
		super();
		this.bankId = bankId;
		this.currency = currency;
		this.refNo = refNo;
		this.description = description;
		this.transactionType = transactionType;
		this.amount = amount;
		this.balance = balance;
		this.closingBalance = closingBalance;
		this.paymentVoucherNo = paymentVoucherNo;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBankId() {
		return this.bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public int getCurrency() {
		return this.currency;
	}

	public void setCurrency(int currency) {
		this.currency = currency;
	}

	public String getRefNo() {
		return this.refNo;
	}

	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}

	public Integer getTransactionType() {
		return this.transactionType;
	}

	public void setTransactionType(Integer transactionType) {
		this.transactionType = transactionType;
	}

	public Double getAmount() {
		return this.amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
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

	public String getRemitTo() {
		return this.remitTo;
	}

	public void setRemitTo(String remitTo) {
		this.remitTo = remitTo;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getClosingBalance() {
		return this.closingBalance;
	}

	public void setClosingBalance(Double closingBalance) {
		this.closingBalance = closingBalance;
	}

	public String getPaymentVoucherNo() {
		return paymentVoucherNo;
	}

	public void setPaymentVoucherNo(String paymentVoucherNo) {
		this.paymentVoucherNo = paymentVoucherNo;
	}

}
