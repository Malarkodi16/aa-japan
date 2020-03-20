package com.nexware.aajapan.dto;

import java.util.Date;

public class TShippingRequestDto {

	private String id;
	private String shipmentRequestId;
	private String vessel;
	private String voyageNo;
	private String orginPort;
	private String destPort;
	private Double freightRate;
	private Double shippingCharge;
	private Double inspection;
	private Double radiation;
	private Double otherCharge;
	private Double blNo;
	private String stockNo;
	private String lotNo;
	private String eta;
	private String etd;
	private String amountReceived;
	private String balanceAmount;
	private String status;
	private String invoiceNo;
	private Date dueDate;
	private String exchangeRate;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getShipmentRequestId() {
		return this.shipmentRequestId;
	}

	public void setShipmentRequestId(String shipmentRequestId) {
		this.shipmentRequestId = shipmentRequestId;
	}

	public String getVessel() {
		return this.vessel;
	}

	public void setVessel(String vessel) {
		this.vessel = vessel;
	}

	public String getVoyageNo() {
		return this.voyageNo;
	}

	public void setVoyageNo(String voyageNo) {
		this.voyageNo = voyageNo;
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

	public Double getFreightRate() {
		return this.freightRate;
	}

	public void setFreightRate(Double freightRate) {
		this.freightRate = freightRate;
	}

	public Double getInspection() {
		return this.inspection;
	}

	public void setInspection(Double inspection) {
		this.inspection = inspection;
	}

	public Double getRadiation() {
		return this.radiation;
	}

	public void setRadiation(Double radiation) {
		this.radiation = radiation;
	}

	public Double getBlNo() {
		return this.blNo;
	}

	public void setBlNo(Double blNo) {
		this.blNo = blNo;
	}

	public Double getShippingCharge() {
		return this.shippingCharge;
	}

	public void setShippingCharge(Double shippingCharge) {
		this.shippingCharge = shippingCharge;
	}

	public Double getOtherCharge() {
		return this.otherCharge;
	}

	public void setOtherCharge(Double otherCharge) {
		this.otherCharge = otherCharge;
	}

	public String getStockNo() {
		return this.stockNo;
	}

	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}

	public String getLotNo() {
		return this.lotNo;
	}

	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}

	public String getEta() {
		return this.eta;
	}

	public void setEta(String eta) {
		this.eta = eta;
	}

	public String getEtd() {
		return this.etd;
	}

	public void setEtd(String etd) {
		this.etd = etd;
	}

	public String getAmountReceived() {
		return this.amountReceived;
	}

	public void setAmountReceived(String amountReceived) {
		this.amountReceived = amountReceived;
	}

	public String getBalanceAmount() {
		return this.balanceAmount;
	}

	public void setBalanceAmount(String balanceAmount) {
		this.balanceAmount = balanceAmount;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getInvoiceNo() {
		return this.invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public Date getDueDate() {
		return this.dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public String getExchangeRate() {
		return this.exchangeRate;
	}

	public void setExchangeRate(String exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

}
