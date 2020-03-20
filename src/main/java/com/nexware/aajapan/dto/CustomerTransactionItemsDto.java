package com.nexware.aajapan.dto;

import java.util.Date;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.nexware.aajapan.utils.AppUtil;

public class CustomerTransactionItemsDto {

	private String id;
	private String invoiceNo;
	private String customerId;
	private ObjectId consigneeId;
	private ObjectId notifypartyId;
	private String paymentType;
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date date;
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
	private String currencySymbol;
	private Double receivedAmount;
	private String sPurchasedDate;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
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
		return AppUtil.ifNull(this.commision, 0.0);
	}

	public void setCommision(Double commision) {
		this.commision = commision;
	}

	public Double getPurchaseCost() {
		return AppUtil.ifNull(this.purchaseCost, 0.0);
	}

	public void setPurchaseCost(Double purchaseCost) {
		this.purchaseCost = purchaseCost;
	}

	public Double getRoadTax() {
		return AppUtil.ifNull(this.roadTax, 0.0);
	}

	public void setRoadTax(Double roadTax) {
		this.roadTax = roadTax;
	}

	public Double getRecycleCost() {
		return AppUtil.ifNull(this.recycleCost, 0.0);
	}

	public void setRecycleCost(Double recycleCost) {
		this.recycleCost = recycleCost;
	}

	public Double getOtherCharges() {
		return AppUtil.ifNull(this.otherCharges, 0.0);
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
		return AppUtil.ifNull(this.price, 0.0);
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

	public String getCurrencySymbol() {
		return this.currencySymbol;
	}

	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}

	public Double getReceivedAmount() {
		return AppUtil.ifNull(this.receivedAmount, 0.0);
	}

	public void setReceivedAmount(Double receivedAmount) {
		this.receivedAmount = receivedAmount;
	}

	public String getsPurchasedDate() {
		String formattedDate;
		if (!AppUtil.isObjectEmpty(this.getDate())) {
			formattedDate = new java.text.SimpleDateFormat("MM/dd/yyyy").format(this.getDate());
		} else {
			formattedDate = "";
		}
		return formattedDate;
	}

	public void setsPurchasedDate(String sPurchasedDate) {
		this.sPurchasedDate = sPurchasedDate;
	}

}
