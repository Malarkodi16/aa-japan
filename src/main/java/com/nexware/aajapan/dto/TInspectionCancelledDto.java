package com.nexware.aajapan.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.models.InspectionCompanyLocation;
import com.nexware.aajapan.utils.AppUtil;

public class TInspectionCancelledDto {

	private String code;
	private String stockNo;
	private String chassisNo;
	private String maker;
	private String model;
	private String destinationCountry;
	private String forwarder;
	private String forwarderId;
	private String inspectionCompany;
	private String inspectionCompanyId;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date inspectionSentDate;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date documentSentDate;
	private String cancelRemark;
	private String remark;
	private String comment;
	private Integer status;
	private Integer inspectionCompanyFlag;
	private InspectionCompanyLocation location;
	@JsonFormat(pattern = "MM/yyyy")
	@DateTimeFormat(pattern = "MM/yyyy")
	private Date estimatedDeparture;
	private Integer shippingInstructionStatus;
	private String bookingDetails;
	private String salesPerson;
	private String customerName;
	private String lastTransportLocationId;
	private String lastTransportLocationName;
	private String lastTransportLocationCustom;
	private String instructionId;
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public String getDestinationCountry() {
		return destinationCountry;
	}

	public void setDestinationCountry(String destinationCountry) {
		this.destinationCountry = destinationCountry;
	}

	public String getForwarder() {
		return forwarder;
	}

	public void setForwarder(String forwarder) {
		this.forwarder = forwarder;
	}

	public String getInspectionCompany() {
		return inspectionCompany;
	}

	public void setInspectionCompany(String inspectionCompany) {
		this.inspectionCompany = inspectionCompany;
	}

	public Date getInspectionSentDate() {
		return inspectionSentDate;
	}

	public void setInspectionSentDate(Date inspectionSentDate) {
		this.inspectionSentDate = inspectionSentDate;
	}

	public Date getDocumentSentDate() {
		return documentSentDate;
	}

	public void setDocumentSentDate(Date documentSentDate) {
		this.documentSentDate = documentSentDate;
	}

	public String getCancelRemark() {
		return cancelRemark;
	}

	public void setCancelRemark(String cancelRemark) {
		this.cancelRemark = cancelRemark;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getInspectionCompanyFlag() {
		return inspectionCompanyFlag;
	}

	public void setInspectionCompanyFlag(Integer inspectionCompanyFlag) {
		this.inspectionCompanyFlag = inspectionCompanyFlag;
	}

	public InspectionCompanyLocation getLocation() {
		return location;
	}

	public void setLocation(InspectionCompanyLocation location) {
		this.location = location;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getInspectionCompanyId() {
		return inspectionCompanyId;
	}

	public void setInspectionCompanyId(String inspectionCompanyId) {
		this.inspectionCompanyId = inspectionCompanyId;
	}

	public String getForwarderId() {
		return forwarderId;
	}

	public void setForwarderId(String forwarderId) {
		this.forwarderId = forwarderId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getEstimatedDeparture() {
		return estimatedDeparture;
	}

	public void setEstimatedDeparture(Date estimatedDeparture) {
		this.estimatedDeparture = estimatedDeparture;
	}

	public Integer getShippingInstructionStatus() {
		return shippingInstructionStatus;
	}

	public void setShippingInstructionStatus(Integer shippingInstructionStatus) {
		this.shippingInstructionStatus = shippingInstructionStatus;
	}

	public String getBookingDetails() {
		if (!AppUtil.isObjectEmpty(this.customerName) && !AppUtil.isObjectEmpty(this.salesPerson)) {
			bookingDetails = AppUtil.ifNull(this.customerName, "") + " - " + AppUtil.ifNull(this.salesPerson, "");
		} else if (!AppUtil.isObjectEmpty(this.customerName)) {
			bookingDetails = AppUtil.ifNull(this.customerName, "");
		} else {
			bookingDetails = AppUtil.ifNull(this.salesPerson, "");
		}
		return bookingDetails;
	}

	public void setBookingDetails(String bookingDetails) {
		this.bookingDetails = bookingDetails;
	}

	public String getSalesPerson() {
		return salesPerson;
	}

	public void setSalesPerson(String salesPerson) {
		this.salesPerson = salesPerson;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getLastTransportLocationId() {
		return lastTransportLocationId;
	}

	public void setLastTransportLocationId(String lastTransportLocationId) {
		this.lastTransportLocationId = lastTransportLocationId;
	}

	public String getLastTransportLocationName() {
		return lastTransportLocationName;
	}

	public void setLastTransportLocationName(String lastTransportLocationName) {
		this.lastTransportLocationName = lastTransportLocationName;
	}

	public String getLastTransportLocationCustom() {
		return lastTransportLocationCustom;
	}

	public void setLastTransportLocationCustom(String lastTransportLocationCustom) {
		this.lastTransportLocationCustom = lastTransportLocationCustom;
	}

	public String getInstructionId() {
		return instructionId;
	}

	public void setInstructionId(String instructionId) {
		this.instructionId = instructionId;
	}

}
