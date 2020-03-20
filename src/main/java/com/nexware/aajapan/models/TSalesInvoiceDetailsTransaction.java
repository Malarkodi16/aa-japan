package com.nexware.aajapan.models;

import java.util.Date;

import com.nexware.aajapan.listeners.EntityModelBase;

public class TSalesInvoiceDetailsTransaction extends EntityModelBase{

	private Date date;
	private Double amount;
	private String allocatedBy;
	private String allocationType;
	private String refNo;

	public TSalesInvoiceDetailsTransaction(Date date, Double amount, String allocatedBy, String allocationType,
			String refNo) {
		super();
		this.date = date;
		this.amount = amount;
		this.allocatedBy = allocatedBy;
		this.allocationType = allocationType;
		this.refNo = refNo;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Double getAmount() {
		return this.amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getAllocatedBy() {
		return this.allocatedBy;
	}

	public void setAllocatedBy(String allocatedBy) {
		this.allocatedBy = allocatedBy;
	}

	public String getAllocationType() {
		return this.allocationType;
	}

	public void setAllocationType(String allocationType) {
		this.allocationType = allocationType;
	}

	public String getRefNo() {
		return this.refNo;
	}

	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}

}
