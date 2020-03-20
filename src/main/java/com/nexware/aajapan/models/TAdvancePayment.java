package com.nexware.aajapan.models;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.listeners.EntityModelBase;

@Document(collection = "t_adv_pymnt")
public class TAdvancePayment extends EntityModelBase {

	@Id
	private String id;
	@Indexed
	private Integer paymentType;
	private String remitterType;
	private String remitterId;
	private String remitTo;
	private Double amount;
	private Integer paymentApprove;
	private String bank;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date approvedDate;
	private String remarks;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getPaymentType() {
		return this.paymentType;
	}

	public void setPaymentType(Integer paymentType) {
		this.paymentType = paymentType;
	}

	public String getRemitterType() {
		return this.remitterType;
	}

	public void setRemitterType(String remitterType) {
		this.remitterType = remitterType;
	}

	public String getRemitterId() {
		return this.remitterId;
	}

	public void setRemitterId(String remitterId) {
		this.remitterId = remitterId;
	}

	public String getRemitTo() {
		return this.remitTo;
	}

	public void setRemitTo(String remitTo) {
		this.remitTo = remitTo;
	}

	public Double getAmount() {
		return this.amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Integer getPaymentApprove() {
		return this.paymentApprove;
	}

	public void setPaymentApprove(Integer paymentApprove) {
		this.paymentApprove = paymentApprove;
	}

	public String getBank() {
		return this.bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public Date getApprovedDate() {
		return this.approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
