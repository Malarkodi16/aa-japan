package com.nexware.aajapan.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.listeners.EntityModelBase;

public class TTransportOrderItemDto extends EntityModelBase {
	private String invoiceId;
	private String invoiceNo;
	private String stockNo;
	private String chassisNo;
	private String maker;
	private String model;
	private String finalDestination;
	private String lotNo;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date dueDate;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date pickupDate;
	private String pickupTime;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date deliveryDate;
	private String deliveryTime;
	private String transporter;
	private String destinationCountry;
	private String destinationPort;
	private String pickupLocation;
	private String sPickupLocation;
	private String pickupLocationCustom;
	private String dropLocation;
	private String sDropLocation;
	private String dropLocationCustom;
	private Integer transportationCount;
	private Double charge;
	private String remarks;
	private Integer status;
	private Integer stockStatus;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date etd;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date purchaseDate;
	private String category;
	private String subcategory;
	private Integer scheduleType;
	private Integer selectedDate;
	private String comment;
	private String forwarder;
	private String supplierName;
	private String auctionHouse;
	private String auctionHouseId;
	private String supplierCode;
	private List<String> posNos;
	private String auctionInfoPosNo;
	private String transportCategory;

	public String getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getStockNo() {
		return stockNo;
	}

	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public Date getPickupDate() {
		return pickupDate;
	}

	public void setPickupDate(Date pickupDate) {
		this.pickupDate = pickupDate;
	}

	public String getPickupTime() {
		return pickupTime;
	}

	public void setPickupTime(String pickupTime) {
		this.pickupTime = pickupTime;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(String deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public String getTransporter() {
		return transporter;
	}

	public void setTransporter(String transporter) {
		this.transporter = transporter;
	}

	public String getDestinationCountry() {
		return destinationCountry;
	}

	public void setDestinationCountry(String destinationCountry) {
		this.destinationCountry = destinationCountry;
	}

	public String getDestinationPort() {
		return destinationPort;
	}

	public void setDestinationPort(String destinationPort) {
		this.destinationPort = destinationPort;
	}

	public String getPickupLocation() {
		return pickupLocation;
	}

	public void setPickupLocation(String pickupLocation) {
		this.pickupLocation = pickupLocation;
	}

	public String getPickupLocationCustom() {
		return pickupLocationCustom;
	}

	public void setPickupLocationCustom(String pickupLocationCustom) {
		this.pickupLocationCustom = pickupLocationCustom;
	}

	public String getDropLocation() {
		return dropLocation;
	}

	public void setDropLocation(String dropLocation) {
		this.dropLocation = dropLocation;
	}

	public String getDropLocationCustom() {
		return dropLocationCustom;
	}

	public void setDropLocationCustom(String dropLocationCustom) {
		this.dropLocationCustom = dropLocationCustom;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getEtd() {
		return etd;
	}

	public void setEtd(Date etd) {
		this.etd = etd;
	}

	public String getChassisNo() {
		return chassisNo;
	}

	public void setChassisNo(String chassisNo) {
		this.chassisNo = chassisNo;
	}

	public Double getCharge() {
		return charge;
	}

	public void setCharge(Double charge) {
		this.charge = charge;
	}

	public String getsPickupLocation() {
		return sPickupLocation;
	}

	public void setsPickupLocation(String sPickupLocation) {
		this.sPickupLocation = sPickupLocation;
	}

	public String getsDropLocation() {
		return sDropLocation;
	}

	public void setsDropLocation(String sDropLocation) {
		this.sDropLocation = sDropLocation;
	}

	public String getMaker() {
		return maker;
	}

	public void setMaker(String maker) {
		this.maker = maker;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getFinalDestination() {
		return finalDestination;
	}

	public void setFinalDestination(String finalDestination) {
		this.finalDestination = finalDestination;
	}

	public String getLotNo() {
		return lotNo;
	}

	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}

	public Integer getStockStatus() {
		return stockStatus;
	}

	public void setStockStatus(Integer stockStatus) {
		this.stockStatus = stockStatus;
	}

	public Integer getTransportationCount() {
		return transportationCount;
	}

	public void setTransportationCount(Integer transportationCount) {
		this.transportationCount = transportationCount;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubcategory() {
		return subcategory;
	}

	public void setSubcategory(String subcategory) {
		this.subcategory = subcategory;
	}

	public Integer getScheduleType() {
		return scheduleType;
	}

	public void setScheduleType(Integer scheduleType) {
		this.scheduleType = scheduleType;
	}

	public Integer getSelectedDate() {
		return selectedDate;
	}

	public void setSelectedDate(Integer selectedDate) {
		this.selectedDate = selectedDate;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getForwarder() {
		return forwarder;
	}

	public void setForwarder(String forwarder) {
		this.forwarder = forwarder;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getAuctionHouse() {
		return auctionHouse;
	}

	public void setAuctionHouse(String auctionHouse) {
		this.auctionHouse = auctionHouse;
	}

	public String getAuctionHouseId() {
		return auctionHouseId;
	}

	public void setAuctionHouseId(String auctionHouseId) {
		this.auctionHouseId = auctionHouseId;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public List<String> getPosNos() {
		return posNos;
	}

	public void setPosNos(List<String> posNos) {
		this.posNos = posNos;
	}

	public String getAuctionInfoPosNo() {
		return auctionInfoPosNo;
	}

	public void setAuctionInfoPosNo(String auctionInfoPosNo) {
		this.auctionInfoPosNo = auctionInfoPosNo;
	}

	public String getTransportCategory() {
		return transportCategory;
	}

	public void setTransportCategory(String transportCategory) {
		this.transportCategory = transportCategory;
	}

}
