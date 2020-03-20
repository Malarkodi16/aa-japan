package com.nexware.aajapan.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.models.InspectionCompanyLocation;

public class TInspectionOrderRequestDto {
	private String country;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date inspectionSentDate;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date inspectionDate;
	private Integer inspectionCompanyFlag;
	private String inspectionCompany;
	private String inspectionCompanyId;
	private String forwarderId;
	private String forwarder;
	private InspectionCompanyLocation location;
	private List<TInspectionOrderRequestItemDto> items;

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Date getInspectionSentDate() {
		return this.inspectionSentDate;
	}

	public void setInspectionSentDate(Date inspectionSentDate) {
		this.inspectionSentDate = inspectionSentDate;
	}

	public Date getInspectionDate() {
		return inspectionDate;
	}

	public void setInspectionDate(Date inspectionDate) {
		this.inspectionDate = inspectionDate;
	}

	public Integer getInspectionCompanyFlag() {
		return this.inspectionCompanyFlag;
	}

	public void setInspectionCompanyFlag(Integer inspectionCompanyFlag) {
		this.inspectionCompanyFlag = inspectionCompanyFlag;
	}

	public String getInspectionCompany() {
		return this.inspectionCompany;
	}

	public void setInspectionCompany(String inspectionCompany) {
		this.inspectionCompany = inspectionCompany;
	}

	public List<TInspectionOrderRequestItemDto> getItems() {
		return this.items;
	}

	public void setItems(List<TInspectionOrderRequestItemDto> items) {
		this.items = items;
	}

	public String getInspectionCompanyId() {
		return this.inspectionCompanyId;
	}

	public void setInspectionCompanyId(String inspectionCompanyId) {
		this.inspectionCompanyId = inspectionCompanyId;
	}

	public String getForwarderId() {
		return this.forwarderId;
	}

	public void setForwarderId(String forwarderId) {
		this.forwarderId = forwarderId;
	}

	public String getForwarder() {
		return this.forwarder;
	}

	public void setForwarder(String forwarder) {
		this.forwarder = forwarder;
	}

	public InspectionCompanyLocation getLocation() {
		return this.location;
	}

	public void setLocation(InspectionCompanyLocation location) {
		this.location = location;
	}

}