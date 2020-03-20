package com.nexware.aajapan.models;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.listeners.EntityModelBase;

@Document(collection = "t_loan")
public class TLoan extends EntityModelBase {
	@Id
	private String id;
	@Indexed(unique = true)
	private String loanId;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date date;
	private String bank;
	private String reference;
	private String loanType;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date dueDate;
	private int savingAccount;
	private Double savingsAccountAmount;
	private String savingsBankAccount;
	private Integer principalPaymentFrequency;
	private Integer interestPaymentFrequency;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date firstPaymentDate;
	private Double rateOfInterest;
	private int loanTerm;
	private String description;
	private Double loanAmount;
	private Double totalInterest;
	private Double totalPayable;
	private Double closingBalance;
	private Double installmentAmount;
	private String leaveDay;

	public TLoan() {

	}

	public TLoan(String loanType, String bank, Double rateOfInterest, int loanTerm, Double loanAmount) {
		this.bank = bank;
		this.loanType = loanType;
		this.rateOfInterest = rateOfInterest;
		this.loanTerm = loanTerm;
		this.loanAmount = loanAmount;

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLoanId() {
		return loanId;
	}

	public void setLoanId(String loanId) {
		this.loanId = loanId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getLoanType() {
		return loanType;
	}

	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
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

	public Integer getPrincipalPaymentFrequency() {
		return principalPaymentFrequency;
	}

	public void setPrincipalPaymentFrequency(Integer principalPaymentFrequency) {
		this.principalPaymentFrequency = principalPaymentFrequency;
	}

	public Integer getInterestPaymentFrequency() {
		return interestPaymentFrequency;
	}

	public void setInterestPaymentFrequency(Integer interestPaymentFrequency) {
		this.interestPaymentFrequency = interestPaymentFrequency;
	}

	public Date getFirstPaymentDate() {
		return firstPaymentDate;
	}

	public void setFirstPaymentDate(Date firstPaymentDate) {
		this.firstPaymentDate = firstPaymentDate;
	}

	public Double getRateOfInterest() {
		return rateOfInterest;
	}

	public void setRateOfInterest(Double rateOfInterest) {
		this.rateOfInterest = rateOfInterest;
	}

	public int getLoanTerm() {
		return loanTerm;
	}

	public void setLoanTerm(int loanTerm) {
		this.loanTerm = loanTerm;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(Double loanAmount) {
		this.loanAmount = loanAmount;
	}

	public Double getTotalInterest() {
		return totalInterest;
	}

	public void setTotalInterest(Double totalInterest) {
		this.totalInterest = totalInterest;
	}

	public Double getTotalPayable() {
		return totalPayable;
	}

	public void setTotalPayable(Double totalPayable) {
		this.totalPayable = totalPayable;
	}

	public Double getClosingBalance() {
		return closingBalance;
	}

	public void setClosingBalance(Double closingBalance) {
		this.closingBalance = closingBalance;
	}

	public Double getInstallmentAmount() {
		return installmentAmount;
	}

	public void setInstallmentAmount(Double installmentAmount) {
		this.installmentAmount = installmentAmount;
	}

	public String getLeaveDay() {
		return leaveDay;
	}

	public void setLeaveDay(String leaveDay) {
		this.leaveDay = leaveDay;
	}

}
