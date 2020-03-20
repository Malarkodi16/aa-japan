package com.nexware.aajapan.dto;

import java.util.Date;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.annotation.JsonFormat;

public class CustomerTransactionReportDto {

	private String id;
	@JsonFormat(pattern = "MMMM yyyy")
	private Date date;
	private String invoiceNo;
	private String customerId;
	private ObjectId consigneeId;
	private ObjectId notifypartyId;
	private String paymentType;
	private Double fobTotal;
	private Double shippingTotal;
	private Double freightTotal;
	private Double insuranceTotal;
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date etd;
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date eta;
	private String firstName;
	private String lastName;
	private String fName;
	private String stockNo;
	private String chassisNo;
	private String lotNo;
	private String auction;
	private String lcNumber;
	private Double commision;
	private Double purchaseCost;
	private Double roadTax;
	private Double recycleCost;
	private Double otherCharges;
	private Double additionalCharges;
	private Double totalPrice;
	private Double dollarPrice;
	private Double price;
	private String dhlNo;
	private Double shippingStatus;
	private String vesselId;
	private String vesselName;
	private String containerNo;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getInvoiceNo() {
		return this.invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public ObjectId getConsigneeId() {
		return this.consigneeId;
	}

	public void setConsigneeId(ObjectId consigneeId) {
		this.consigneeId = consigneeId;
	}

	public ObjectId getNotifypartyId() {
		return this.notifypartyId;
	}

	public void setNotifypartyId(ObjectId notifypartyId) {
		this.notifypartyId = notifypartyId;
	}

	public String getPaymentType() {
		return this.paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public Double getFobTotal() {
		return this.fobTotal;
	}

	public void setFobTotal(Double fobTotal) {
		this.fobTotal = fobTotal;
	}

	public Double getShippingTotal() {
		return this.shippingTotal;
	}

	public void setShippingTotal(Double shippingTotal) {
		this.shippingTotal = shippingTotal;
	}

	public Double getFreightTotal() {
		return this.freightTotal;
	}

	public void setFreightTotal(Double freightTotal) {
		this.freightTotal = freightTotal;
	}

	public Double getInsuranceTotal() {
		return this.insuranceTotal;
	}

	public void setInsuranceTotal(Double insuranceTotal) {
		this.insuranceTotal = insuranceTotal;
	}

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

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getfName() {
		return this.fName;
	}

	public void setfName(String fName) {
		this.fName = fName;
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

	public String getLotNo() {
		return this.lotNo;
	}

	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}

	public String getAuction() {
		return this.auction;
	}

	public void setAuction(String auction) {
		this.auction = auction;
	}

	public String getLcNumber() {
		return this.lcNumber;
	}

	public void setLcNumber(String lcNumber) {
		this.lcNumber = lcNumber;
	}

	public Double getCommision() {
		return this.commision;
	}

	public void setCommision(Double commision) {
		this.commision = commision;
	}

	public Double getPurchaseCost() {
		return this.purchaseCost;
	}

	public void setPurchaseCost(Double purchaseCost) {
		this.purchaseCost = purchaseCost;
	}

	public Double getRoadTax() {
		return this.roadTax;
	}

	public void setRoadTax(Double roadTax) {
		this.roadTax = roadTax;
	}

	public Double getRecycleCost() {
		return this.recycleCost;
	}

	public void setRecycleCost(Double recycleCost) {
		this.recycleCost = recycleCost;
	}

	public Double getOtherCharges() {
		return this.otherCharges;
	}

	public void setOtherCharges(Double otherCharges) {
		this.otherCharges = otherCharges;
	}

	public Double getAdditionalCharges() {
		return this.additionalCharges;
	}

	public void setAdditionalCharges(Double additionalCharges) {
		this.additionalCharges = additionalCharges;
	}

	public Double getTotalPrice() {
		return this.totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Double getDollarPrice() {
		return this.dollarPrice;
	}

	public void setDollarPrice(Double dollarPrice) {
		this.dollarPrice = dollarPrice;
	}

	public Double getPrice() {
		return this.price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getDhlNo() {
		return this.dhlNo;
	}

	public void setDhlNo(String dhlNo) {
		this.dhlNo = dhlNo;
	}

	public Double getShippingStatus() {
		return this.shippingStatus;
	}

	public void setShippingStatus(Double shippingStatus) {
		this.shippingStatus = shippingStatus;
	}

	public String getVesselId() {
		return this.vesselId;
	}

	public void setVesselId(String vesselId) {
		this.vesselId = vesselId;
	}

	public String getVesselName() {
		return this.vesselName;
	}

	public void setVesselName(String vesselName) {
		this.vesselName = vesselName;
	}

	public String getContainerNo() {
		return this.containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}

}
