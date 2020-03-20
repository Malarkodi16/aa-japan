package com.nexware.aajapan.dto;

import java.util.Date;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.models.InspectionCompanyLocation;

public class TInspectionRearrangeDto {
	
	private String code;
	private String stockNo;
	private String country;
	private String forwarder;
	private String comment;
	private Integer status;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date inspectionDate;
	private Integer inspectionCompanyFlag;
	private String inspectionCompany;
	private String inspectionCompanyValue;
	private String locationValue;
	private String forwarderValue;
	
	private InspectionCompanyLocation location;
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
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getForwarder() {
		return forwarder;
	}
	public void setForwarder(String forwarder) {
		this.forwarder = forwarder;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getInspectionDate() {
		return inspectionDate;
	}
	public void setInspectionDate(Date inspectionDate) {
		this.inspectionDate = inspectionDate;
	}
	public Integer getInspectionCompanyFlag() {
		return inspectionCompanyFlag;
	}
	public void setInspectionCompanyFlag(Integer inspectionCompanyFlag) {
		this.inspectionCompanyFlag = inspectionCompanyFlag;
	}
	public String getInspectionCompany() {
		return inspectionCompany;
	}
	public void setInspectionCompany(String inspectionCompany) {
		this.inspectionCompany = inspectionCompany;
	}
	public String getInspectionCompanyValue() {
		return inspectionCompanyValue;
	}
	public void setInspectionCompanyValue(String inspectionCompanyValue) {
		this.inspectionCompanyValue = inspectionCompanyValue;
	}
	public String getLocationValue() {
		return locationValue;
	}
	public void setLocationValue(String locationValue) {
		this.locationValue = locationValue;
	}
	public InspectionCompanyLocation getLocation() {
		return location;
	}
	public void setLocation(InspectionCompanyLocation location) {
		this.location = location;
	}
	public String getForwarderValue() {
		return forwarderValue;
	}
	public void setForwarderValue(String forwarderValue) {
		this.forwarderValue = forwarderValue;
	}

}
