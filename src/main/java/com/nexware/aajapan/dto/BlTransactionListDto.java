package com.nexware.aajapan.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class BlTransactionListDto {

	private String shippingInstructionId;
	private String customer;
	private String consignee;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date createdDate;
	private String createdBy;
	private String transactionNo;

	public String getShippingInstructionId() {
		return shippingInstructionId;
	}

	public void setShippingInstructionId(String shippingInstructionId) {
		this.shippingInstructionId = shippingInstructionId;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getTransactionNo() {
		return transactionNo;
	}

	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}

}
