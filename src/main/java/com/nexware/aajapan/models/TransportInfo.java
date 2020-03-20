package com.nexware.aajapan.models;

import java.util.Date;

import org.springframework.data.mongodb.core.index.Indexed;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TransportInfo {
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date pickupDate;
	private String pickupTime;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date deliveryDate;
	private String deliveryTime;
	private String transporter;
	private String remarks;
	@Indexed
	private String pickupLocation;
	private String sPickupLocation;
	private String pickupLocationCustom;
	@Indexed
	private String dropLocation;
	private String sDropLocation;
	private String dropLocationCustom;
	private Double charge;
	private String reasonForCancel;
	private Integer status;
	@JsonFormat(pattern = "dd-MM-YYYY")
	private Date etd;

	public Date getPickupDate() {
		return this.pickupDate;
	}

	public void setPickupDate(Date pickupDate) {
		this.pickupDate = pickupDate;
	}

	public String getPickupTime() {
		return this.pickupTime;
	}

	public void setPickupTime(String pickupTime) {
		this.pickupTime = pickupTime;
	}

	public Date getDeliveryDate() {
		return this.deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getDeliveryTime() {
		return this.deliveryTime;
	}

	public void setDeliveryTime(String deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public String getTransporter() {
		return this.transporter;
	}

	public void setTransporter(String transporter) {
		this.transporter = transporter;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getPickupLocation() {
		return this.pickupLocation;
	}

	public void setPickupLocation(String pickupLocation) {
		this.pickupLocation = pickupLocation;
	}

	public String getPickupLocationCustom() {
		return this.pickupLocationCustom;
	}

	public void setPickupLocationCustom(String pickupLocationCustom) {
		this.pickupLocationCustom = pickupLocationCustom;
	}

	public String getDropLocation() {
		return this.dropLocation;
	}

	public void setDropLocation(String dropLocation) {
		this.dropLocation = dropLocation;
	}

	public String getDropLocationCustom() {
		return this.dropLocationCustom;
	}

	public void setDropLocationCustom(String dropLocationCustom) {
		this.dropLocationCustom = dropLocationCustom;
	}

	public Double getCharge() {
		return this.charge;
	}

	public void setCharge(Double charge) {
		this.charge = charge;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getReasonForCancel() {
		return this.reasonForCancel;
	}

	public void setReasonForCancel(String reasonForCancel) {
		this.reasonForCancel = reasonForCancel;
	}

	public Date getEtd() {
		return this.etd;
	}

	public void setEtd(Date etd) {
		this.etd = etd;
	}

	public String getsPickupLocation() {
		return this.sPickupLocation;
	}

	public void setsPickupLocation(String sPickupLocation) {
		this.sPickupLocation = sPickupLocation;
	}

	public String getsDropLocation() {
		return this.sDropLocation;
	}

	public void setsDropLocation(String sDropLocation) {
		this.sDropLocation = sDropLocation;
	}

}
