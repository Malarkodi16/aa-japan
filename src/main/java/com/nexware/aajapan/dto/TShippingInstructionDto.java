package com.nexware.aajapan.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.utils.AppUtil;

public class TShippingInstructionDto {

	private String id;
	private String stockNo;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date date = new Date();
	private String chassisNo;
	private String customerId;
	private String customerFN;
	private String fCustName;
	private String consigneeId;
	private String consigneeFN;
	private String consigneeLN;
	private String fConsigneeName;
	private String notifypartyId;
	private String notifyPartyFN;
	private String notifyPartyLN;
	private String fNotifyPartyName;
	private String destCountry;
	private String destPort;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date eta;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date etd;
	private String status;
	private String remarks;
	private String vesselId;
	private String dhlNo;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date reserveDate;
	private String lcNo;
	private Integer scheduleType;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date createdDate;
	private String shippingUser;
	private String shippingId;
    private String shippingTel;
	private String hsCode;
	private String instructedBy;
	private String shippingInstructionId;
	private Integer inspectionFlag;
	private String paymentType;
	private String yard;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date estimatedArrival;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date estimatedDeparture;
	private String bookingDetails;
	private Double purchasePrice;
	private String salesPersonId;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCustomerFN() {
		return this.customerFN;
	}

	public void setCustomerFN(String customerFN) {
		this.customerFN = customerFN;
	}

	public String getfCustName() {
		return this.fCustName;
	}

	public void setfCustName(String fCustName) {
		this.fCustName = fCustName;
	}

	public String getConsigneeId() {
		return this.consigneeId;
	}

	public void setConsigneeId(String consigneeId) {
		this.consigneeId = consigneeId;
	}

	public String getConsigneeFN() {
		return this.consigneeFN;
	}

	public void setConsigneeFN(String consigneeFN) {
		this.consigneeFN = consigneeFN;
	}

	public String getConsigneeLN() {
		return this.consigneeLN;
	}

	public void setConsigneeLN(String consigneeLN) {
		this.consigneeLN = consigneeLN;
	}

	public String getfConsigneeName() {
		return this.fConsigneeName;
	}

	public void setfConsigneeName(String fConsigneeName) {
		this.fConsigneeName = fConsigneeName;
	}

	public String getNotifypartyId() {
		return this.notifypartyId;
	}

	public void setNotifypartyId(String notifyPartyId) {
		this.notifypartyId = notifyPartyId;
	}

	public String getNotifyPartyFN() {
		return this.notifyPartyFN;
	}

	public void setNotifyPartyFN(String notifyPartyFN) {
		this.notifyPartyFN = notifyPartyFN;
	}

	public String getNotifyPartyLN() {
		return this.notifyPartyLN;
	}

	public void setNotifyPartyLN(String notifyPartyLN) {
		this.notifyPartyLN = notifyPartyLN;
	}

	public String getfNotifyPartyName() {
		return this.fNotifyPartyName;
	}

	public void setfNotifyPartyName(String fNotifyPartyName) {
		this.fNotifyPartyName = fNotifyPartyName;
	}

	public Date getEta() {
		return this.eta;
	}

	public void setEta(Date eta) {
		this.eta = eta;
	}

	public Date getEtd() {
		return this.etd;
	}

	public void setEtd(Date etd) {
		this.etd = etd;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getVesselId() {
		return this.vesselId;
	}

	public void setVesselId(String vesselId) {
		this.vesselId = vesselId;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDestCountry() {
		return this.destCountry;
	}

	public void setDestCountry(String destCountry) {
		this.destCountry = destCountry;
	}

	public String getDestPort() {
		return this.destPort;
	}

	public void setDestPort(String destPort) {
		this.destPort = destPort;
	}

	public String getDhlNo() {
		return this.dhlNo;
	}

	public void setDhlNo(String dhlNo) {
		this.dhlNo = dhlNo;
	}

	public Date getReserveDate() {
		return this.reserveDate;
	}

	public void setReserveDate(Date reserveDate) {
		this.reserveDate = reserveDate;
	}

	public String getLcNo() {
		return this.lcNo;
	}

	public void setLcNo(String lcNo) {
		this.lcNo = lcNo;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getScheduleType() {
		return this.scheduleType;
	}

	public void setScheduleType(Integer scheduleType) {
		this.scheduleType = scheduleType;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getShippingUser() {
		return shippingUser;
	}

	public void setShippingUser(String shippingUser) {
		this.shippingUser = shippingUser;
	}

	public String getShippingId() {
		return shippingId;
	}

	public void setShippingId(String shippingId) {
		this.shippingId = shippingId;
	}

	public String getShippingTel() {
		return shippingTel;
	}

	public void setShippingTel(String shippingTel) {
		this.shippingTel = shippingTel;
	}

	public String getHsCode() {
		return hsCode;
	}

	public void setHsCode(String hsCode) {
		this.hsCode = hsCode;
	}

	public String getInstructedBy() {
		return instructedBy;
	}

	public void setInstructedBy(String instructedBy) {
		this.instructedBy = instructedBy;
	}

	public String getShippingInstructionId() {
		return shippingInstructionId;
	}

	public void setShippingInstructionId(String shippingInstructionId) {
		this.shippingInstructionId = shippingInstructionId;
	}

	public Integer getInspectionFlag() {
		return inspectionFlag;
	}

	public void setInspectionFlag(Integer inspectionFlag) {
		this.inspectionFlag = inspectionFlag;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getYard() {
		return yard;
	}

	public void setYard(String yard) {
		this.yard = yard;
	}

	public Date getEstimatedArrival() {
		return estimatedArrival;
	}

	public void setEstimatedArrival(Date estimatedArrival) {
		this.estimatedArrival = estimatedArrival;
	}

	public Date getEstimatedDeparture() {
		return estimatedDeparture;
	}

	public void setEstimatedDeparture(Date estimatedDeparture) {
		this.estimatedDeparture = estimatedDeparture;
	}

	public String getBookingDetails() {
		if (!AppUtil.isObjectEmpty(this.customerFN) && !AppUtil.isObjectEmpty(this.instructedBy)) {
			bookingDetails = AppUtil.ifNull(this.customerFN, "") + " - " + AppUtil.ifNull(this.instructedBy, "");
		} else if (!AppUtil.isObjectEmpty(this.customerFN)) {
			bookingDetails = AppUtil.ifNull(this.customerFN, "");
		} else {
			bookingDetails = AppUtil.ifNull(this.instructedBy, "");
		}
		return bookingDetails;
	}

	public void setBookingDetails(String bookingDetails) {
		this.bookingDetails = bookingDetails;
	}

	public Double getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(Double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public String getSalesPersonId() {
		return salesPersonId;
	}

	public void setSalesPersonId(String salesPersonId) {
		this.salesPersonId = salesPersonId;
	}

}
