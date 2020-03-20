package com.nexware.aajapan.models;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ApprovePaymentDetails {

	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date approvedDate;
	private String type;
	private String remarks;
	private String bank;

	public ApprovePaymentDetails(String bank, Date approvedDate, String remarks) {
		super();
		this.approvedDate = approvedDate;
		this.remarks = remarks;
		this.bank = bank;
	}

	public ApprovePaymentDetails() {

	}

	public Date getApprovedDate() {
		return this.approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getBank() {
		return this.bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

}