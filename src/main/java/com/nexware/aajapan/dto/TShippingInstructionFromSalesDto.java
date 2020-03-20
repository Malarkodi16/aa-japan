package com.nexware.aajapan.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.utils.AppUtil;

public class TShippingInstructionFromSalesDto {
	private String id;
	private String stockNo;
	private String chassisNo;
	private String customerId;
	private String customerFN;
	private String customerLN;
	private String fCustName;
	private String consigneeId;
	private String consigneeFN;
	private String consigneeLN;
	private String fConsigneeName;
	private String notifyPartyId;
	private String notifyPartyFN;
	private String notifyPartyLN;
	private String fNotifyPartyName;
	private String destinationCountry;
	private String destinationPort;
	private String yard;
	private String originCountry;
	private String originPort;
	private String salesPersonId;
	private String salesPersonName;
	private String lastTransportLocationDisplayname;
	private String lastTransportLocation;
	private String lastTransportLocationCustom;
	private String remarks;
	private Integer scheduleType;
	private Integer shipmentType;
	private String maker;
	private String model;
	private Double m3;
	private Double length;
	private Double width;
	private Double height;
	private String forwarder;
	private String forwarderName;
	private Integer status;
	@JsonFormat(pattern = "MM/yyyy")
	@DateTimeFormat(pattern = "MM/yyyy")
	private Date estimatedDeparture;
	@JsonFormat(pattern = "yyyy/MM")
	private Date firstRegDate;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date purchaseDate;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date createdDate;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date documentConvertedDate;

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

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

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCustomerFN() {
		return customerFN;
	}

	public void setCustomerFN(String customerFN) {
		this.customerFN = customerFN;
	}

	public String getCustomerLN() {
		return customerLN;
	}

	public void setCustomerLN(String customerLN) {
		this.customerLN = customerLN;
	}

	public String getfCustName() {
		fCustName = customerFN + " " + customerLN;
		return fCustName;
	}

	public void setfCustName(String fCustName) {
		this.fCustName = fCustName;
	}

	public String getConsigneeId() {
		return consigneeId;
	}

	public void setConsigneeId(String consigneeId) {
		this.consigneeId = consigneeId;
	}

	public String getConsigneeFN() {
		return consigneeFN;
	}

	public void setConsigneeFN(String consigneeFN) {
		this.consigneeFN = consigneeFN;
	}

	public String getConsigneeLN() {
		return consigneeLN;
	}

	public void setConsigneeLN(String consigneeLN) {
		this.consigneeLN = consigneeLN;
	}

	public String getfConsigneeName() {
		fConsigneeName = consigneeFN + " " + consigneeLN;
		return fConsigneeName;
	}

	public void setfConsigneeName(String fConsigneeName) {
		this.fConsigneeName = fConsigneeName;
	}

	public String getNotifyPartyId() {
		return notifyPartyId;
	}

	public void setNotifyPartyId(String notifyPartyId) {
		this.notifyPartyId = notifyPartyId;
	}

	public String getNotifyPartyFN() {
		return notifyPartyFN;
	}

	public void setNotifyPartyFN(String notifyPartyFN) {
		this.notifyPartyFN = notifyPartyFN;
	}

	public String getNotifyPartyLN() {
		return notifyPartyLN;
	}

	public void setNotifyPartyLN(String notifyPartyLN) {
		this.notifyPartyLN = notifyPartyLN;
	}

	public String getfNotifyPartyName() {
		fNotifyPartyName = AppUtil.ifNull(notifyPartyFN, "") + " " + AppUtil.ifNull(notifyPartyLN, "");
		return fNotifyPartyName;
	}

	public void setfNotifyPartyName(String fNotifyPartyName) {
		this.fNotifyPartyName = fNotifyPartyName;
	}

	public String getSalesPersonId() {
		return salesPersonId;
	}

	public String getSalesPersonName() {
		return salesPersonName;
	}

	public void setSalesPersonName(String salesPersonName) {
		this.salesPersonName = salesPersonName;
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

	public void setSalesPersonId(String salesPersonId) {
		this.salesPersonId = salesPersonId;
	}

	public String getYard() {
		return yard;
	}

	public void setYard(String yard) {
		this.yard = yard;
	}

	public String getOriginCountry() {
		return originCountry;
	}

	public void setOriginCountry(String originCountry) {
		this.originCountry = originCountry;
	}

	public String getOriginPort() {
		return originPort;
	}

	public void setOriginPort(String originPort) {
		this.originPort = originPort;
	}

	public String getLastTransportLocation() {
		return lastTransportLocation;
	}

	public void setLastTransportLocation(String lastTransportLocation) {
		this.lastTransportLocation = lastTransportLocation;
	}

	public String getLastTransportLocationCustom() {
		return lastTransportLocationCustom;
	}

	public void setLastTransportLocationCustom(String lastTransportLocationCustom) {
		this.lastTransportLocationCustom = lastTransportLocationCustom;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getLastTransportLocationDisplayname() {
		return lastTransportLocationDisplayname;
	}

	public void setLastTransportLocationDisplayname(String lastTransportLocationDisplayname) {
		this.lastTransportLocationDisplayname = lastTransportLocationDisplayname;
	}

	public Integer getScheduleType() {
		return scheduleType;
	}

	public void setScheduleType(Integer scheduleType) {
		this.scheduleType = scheduleType;
	}

	public Integer getShipmentType() {
		return shipmentType;
	}

	public void setShipmentType(Integer shipmentType) {
		this.shipmentType = shipmentType;
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

	public String getForwarder() {
		return forwarder;
	}

	public void setForwarder(String forwarder) {
		this.forwarder = forwarder;
	}

	public String getForwarderName() {
		return forwarderName;
	}

	public void setForwarderName(String forwarderName) {
		this.forwarderName = forwarderName;
	}

	public Date getFirstRegDate() {
		return firstRegDate;
	}

	public void setFirstRegDate(Date firstRegDate) {
		this.firstRegDate = firstRegDate;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getEstimatedDeparture() {
		return estimatedDeparture;
	}

	public void setEstimatedDeparture(Date estimatedDeparture) {
		this.estimatedDeparture = estimatedDeparture;
	}

	public Date getDocumentConvertedDate() {
		return documentConvertedDate;
	}

	public void setDocumentConvertedDate(Date documentConvertedDate) {
		this.documentConvertedDate = documentConvertedDate;
	}

}
