package com.nexware.aajapan.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class FreightVesselItemsDto {

	private String shipmentRequestId;
	private String stockNo;
	private String chassisNo;
	private Double m3;
	private Double length;
	private Double width;
	private Double height;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date etd;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date eta;
	private String customerFirstName;
	private String customerLastName;
	private String consigneeFirstName;
	private String consigneeLastName;
	private Double freightUSD;
	private Double jpy;
	private Double freightCharge;
	private Double shippingCharge;
	private Double inspectionCharge;
	private Double radiationCharge;
	private String orginPort;
	private String destPort;
	private String forwarderId;

	public Date getEtd() {
		return this.etd;
	}

	public void setEtd(Date etd) {
		this.etd = etd;
	}

	public Date getEta() {
		return this.eta;
	}

	public void setEta(Date eta) {
		this.eta = eta;
	}

	public String getShipmentRequestId() {
		return this.shipmentRequestId;
	}

	public void setShipmentRequestId(String shipmentRequestId) {
		this.shipmentRequestId = shipmentRequestId;
	}

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

	public String getCustomerFirstName() {
		return this.customerFirstName;
	}

	public void setCustomerFirstName(String customerFirstName) {
		this.customerFirstName = customerFirstName;
	}

	public String getCustomerLastName() {
		return this.customerLastName;
	}

	public void setCustomerLastName(String customerLastName) {
		this.customerLastName = customerLastName;
	}

	public String getConsigneeFirstName() {
		return this.consigneeFirstName;
	}

	public void setConsigneeFirstName(String consigneeFirstName) {
		this.consigneeFirstName = consigneeFirstName;
	}

	public String getConsigneeLastName() {
		return this.consigneeLastName;
	}

	public void setConsigneeLastName(String consigneeLastName) {
		this.consigneeLastName = consigneeLastName;
	}

	public Double getFreightCharge() {
		return this.freightCharge;
	}

	public void setFreightCharge(Double freightCharge) {
		this.freightCharge = freightCharge;
	}

	public Double getShippingCharge() {
		return this.shippingCharge;
	}

	public void setShippingCharge(Double shippingCharge) {
		this.shippingCharge = shippingCharge;
	}

	public Double getInspectionCharge() {
		return this.inspectionCharge;
	}

	public void setInspectionCharge(Double inspectionCharge) {
		this.inspectionCharge = inspectionCharge;
	}

	public Double getRadiationCharge() {
		return this.radiationCharge;
	}

	public void setRadiationCharge(Double radiationCharge) {
		this.radiationCharge = radiationCharge;
	}

	public Double getFreightUSD() {
		return this.freightUSD;
	}

	public void setFreightUSD(Double freightUSD) {
		this.freightUSD = freightUSD;
	}

	public Double getJpy() {
		return this.jpy;
	}

	public void setJpy(Double jpy) {
		this.jpy = jpy;
	}

	public String getOrginPort() {
		return this.orginPort;
	}

	public void setOrginPort(String orginPort) {
		this.orginPort = orginPort;
	}

	public String getDestPort() {
		return this.destPort;
	}

	public void setDestPort(String destPort) {
		this.destPort = destPort;
	}

	public String getForwarderId() {
		return this.forwarderId;
	}

	public void setForwarderId(String forwarderId) {
		this.forwarderId = forwarderId;
	}

	public Double getM3() {
		return m3;
	}

	public void setM3(Double m3) {
		this.m3 = m3;
	}

	public Double getLength() {
		return length;
	}

	public void setLength(Double length) {
		this.length = length;
	}

	public Double getWidth() {
		return width;
	}

	public void setWidth(Double width) {
		this.width = width;
	}

	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

}
