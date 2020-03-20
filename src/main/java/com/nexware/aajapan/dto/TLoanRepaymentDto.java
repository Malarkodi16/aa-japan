package com.nexware.aajapan.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TLoanRepaymentDto {

	private String loanId;
	private String loanDtlId;
	private String bank;
	private Double installmentAmount;
	private String loanType;
	private int savingAccount;
	private Double savingsAccountAmount;
	private String savingsBankAccount;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date paymentDate;

	public String getLoanId() {
		return loanId;
	}

	public void setLoanId(String loanId) {
		this.loanId = loanId;
	}

	public String getLoanDtlId() {
		return loanDtlId;
	}

	public void setLoanDtlId(String loanDtlId) {
		this.loanDtlId = loanDtlId;
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

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

}
