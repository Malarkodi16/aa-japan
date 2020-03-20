package com.nexware.aajapan.models;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import com.nexware.aajapan.listeners.EntityModelBase;

@Document(collection = "t_loan_repmt")
public class TLoanRepayment extends EntityModelBase {

	@Id
	private String id;
	@Indexed
	private String loanDtlId;
	@Indexed
	private String loanId;
	private String bank;
	private Double installmentAmount;
	private String loanType;
	private int savingAccount;
	private Double savingsAccountAmount;
	private String savingsBankAccount;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date paymentDate;

	public TLoanRepayment(String loanDtlId, String loanId, String bank, Double installmentAmount, String loanType,
			int savingAccount, Double savingsAccountAmount, String savingsBankAccount, Date paymentDate) {
		super();
		this.loanDtlId = loanDtlId;
		this.loanId = loanId;
		this.bank = bank;
		this.installmentAmount = installmentAmount;
		this.loanType = loanType;
		this.savingAccount = savingAccount;
		this.savingsAccountAmount = savingsAccountAmount;
		this.savingsBankAccount = savingsBankAccount;
		this.paymentDate = paymentDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public Double getInstallmentAmount() {
		return installmentAmount;
	}

	public void setInstallmentAmount(Double installmentAmount) {
		this.installmentAmount = installmentAmount;
	}

	public String getLoanType() {
		return loanType;
	}

	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}

	public int getSavingAccount() {
		return savingAccount;
	}

	public void setSavingAccount(int savingAccount) {
		this.savingAccount = savingAccount;
	}

	public Double getSavingsAccountAmount() {
		return savingsAccountAmount;
	}

	public void setSavingsAccountAmount(Double savingsAccountAmount) {
		this.savingsAccountAmount = savingsAccountAmount;
	}

	public String getSavingsBankAccount() {
		return savingsBankAccount;
	}

	public void setSavingsBankAccount(String savingsBankAccount) {
		this.savingsBankAccount = savingsBankAccount;
	}

	public String getLoanDtlId() {
		return loanDtlId;
	}

	public void setLoanDtlId(String loanDtlId) {
		this.loanDtlId = loanDtlId;
	}

	public String getLoanId() {
		return loanId;
	}

	public void setLoanId(String loanId) {
		this.loanId = loanId;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

}
