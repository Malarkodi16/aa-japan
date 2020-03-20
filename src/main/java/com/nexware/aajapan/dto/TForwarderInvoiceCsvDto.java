package com.nexware.aajapan.dto;

public class TForwarderInvoiceCsvDto {

	private String chassisNo;
	private Double amount;
	private String remarks;

	public TForwarderInvoiceCsvDto() {
		// this empty constructor is required
	}

	public TForwarderInvoiceCsvDto(String chassisNo, Double amount, String remarks) {
		this.chassisNo = chassisNo;
		this.amount = amount;
		this.remarks = remarks;
	}

	public String getChassisNo() {
		return chassisNo;
	}

	public void setChassisNo(String chassisNo) {
		this.chassisNo = chassisNo;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
