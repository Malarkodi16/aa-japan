package com.nexware.aajapan.models;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.nexware.aajapan.listeners.EntityModelBase;

@Document(collection = "t_ln_dtls")
public class TLoanDetails extends EntityModelBase {
	
	@Id
	private String id;
	@Indexed(unique = true)
	private String loanDtlId;
	private String loanId;
	private Date date;
	private Double principalAmt;
	private Double interestAmount;
	private Double amount;
	private Double openingBalance;
	private Double closingBalance;
	private Integer status;

	

	public TLoanDetails( String loanId, Date date, Double principalAmt,
			Double interestAmount, Double amount, Double openingBalance, Double closingBalance, Integer status) {
		super();
		
		this.loanId = loanId;
		this.date = date;
		this.principalAmt = principalAmt;
		this.interestAmount = interestAmount;
		this.amount = amount;
		this.openingBalance = openingBalance;
		this.closingBalance = closingBalance;
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getOpeningBalance() {
		return openingBalance;
	}

	public void setOpeningBalance(Double openingBalance) {
		this.openingBalance = openingBalance;
	}

	public Double getClosingBalance() {
		return closingBalance;
	}

	public void setClosingBalance(Double closingBalance) {
		this.closingBalance = closingBalance;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Double getPrincipalAmt() {
		return principalAmt;
	}

	public void setPrincipalAmt(Double principalAmt) {
		this.principalAmt = principalAmt;
	}

	public Double getInterestAmount() {
		return interestAmount;
	}

	public void setInterestAmount(Double interestAmount) {
		this.interestAmount = interestAmount;
	}

}
