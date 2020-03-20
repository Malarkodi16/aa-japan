package com.nexware.aajapan.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TFreightShippingRadiationDto {

	private String id;
	private String stockNo;
	private String chassisNo;
	private Double radiationCharge;
	private String forwarder;
	private String shippingCompany;
	private Double radiationClaimReceivedAmount;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date createdDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStockNo() {
		return stockNo;
	}

	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}

	public String getChassisNo() {
		return chassisNo;
	}

	public void setChassisNo(String chassisNo) {
		this.chassisNo = chassisNo;
	}

	public Double getRadiationCharge() {
		return radiationCharge;
	}

	public void setRadiationCharge(Double radiationCharge) {
		this.radiationCharge = radiationCharge;
	}

	public String getForwarder() {
		return forwarder;
	}

	public void setForwarder(String forwarder) {
		this.forwarder = forwarder;
	}

	public String getShippingCompany() {
		return shippingCompany;
	}

	public void setShippingCompany(String shippingCompany) {
		this.shippingCompany = shippingCompany;
	}

	public Double getRadiationClaimReceivedAmount() {
		return radiationClaimReceivedAmount;
	}

	public void setRadiationClaimReceivedAmount(Double radiationClaimReceivedAmount) {
		this.radiationClaimReceivedAmount = radiationClaimReceivedAmount;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

}
