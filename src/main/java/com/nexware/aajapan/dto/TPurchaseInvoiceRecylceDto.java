package com.nexware.aajapan.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TPurchaseInvoiceRecylceDto {

	private String id;
	private String stockNo;
	private String chassisNo;
	private Double recycle;
	private Double recycleClaimReceivedAmount;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date date;
	private Integer recycleClaimStatus;

	public String getStockNo() {
		return this.stockNo;
	}

	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}

	public String getChassisNo() {
		return this.chassisNo;
	}

	public void setChassisNo(String chassisNo) {
		this.chassisNo = chassisNo;
	}

	public Double getRecycle() {
		return this.recycle;
	}

	public void setRecycle(Double recycle) {
		this.recycle = recycle;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getRecycleClaimStatus() {
		return this.recycleClaimStatus;
	}

	public void setRecycleClaimStatus(Integer recycleClaimStatus) {
		this.recycleClaimStatus = recycleClaimStatus;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Double getRecycleClaimReceivedAmount() {
		return this.recycleClaimReceivedAmount;
	}

	public void setRecycleClaimReceivedAmount(Double recycleClaimReceivedAmount) {
		this.recycleClaimReceivedAmount = recycleClaimReceivedAmount;
	}

}
