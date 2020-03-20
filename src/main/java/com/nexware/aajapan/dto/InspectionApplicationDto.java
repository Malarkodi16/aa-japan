package com.nexware.aajapan.dto;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.amazonaws.services.amplify.model.App;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.models.InspectionCompanyLocation;
import com.nexware.aajapan.utils.AppUtil;

public class InspectionApplicationDto {
	private String inspectionCompanyId;
	private String inspectionCompanyName;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date createdDate;
	private String country;
	private String forwarderId;
	private String forwarderName;
	private int inspectionCompanyFlag;
	private String locationNameJrxml;
	private InspectionCompanyLocation inspectionCompanylocation;
	private List<InspectionStockDetailsDto> items;
	private Integer totalCount;

	public String getInspectionCompanyId() {
		return this.inspectionCompanyId;
	}

	public void setInspectionCompanyId(String inspectionCompanyId) {
		this.inspectionCompanyId = inspectionCompanyId;
	}

	public String getInspectionCompanyName() {
		return this.inspectionCompanyName;
	}

	public void setInspectionCompanyName(String inspectionCompanyName) {
		this.inspectionCompanyName = inspectionCompanyName;
	}

	public InspectionCompanyLocation getInspectionCompanylocation() {
		return this.inspectionCompanylocation;
	}

	public void setInspectionCompanylocation(InspectionCompanyLocation inspectionCompanylocation) {
		this.inspectionCompanylocation = inspectionCompanylocation;
	}

	public List<InspectionStockDetailsDto> getItems() {
		return this.items;
	}

	public void setItems(List<InspectionStockDetailsDto> items) {
		this.items = items;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getForwarderId() {
		return this.forwarderId;
	}

	public void setForwarderId(String forwarderId) {
		this.forwarderId = forwarderId;
	}

	public String getForwarderName() {
		return this.forwarderName;
	}

	public void setForwarderName(String forwarderName) {
		this.forwarderName = forwarderName;
	}

	public int getInspectionCompanyFlag() {
		return this.inspectionCompanyFlag;
	}

	public void setInspectionCompanyFlag(int inspectionCompanyFlag) {
		this.inspectionCompanyFlag = inspectionCompanyFlag;
	}

	public String getLocationNameJrxml() {

		if (!AppUtil.isObjectEmpty(this.locationNameJrxml)) {
			this.locationNameJrxml = this.locationNameJrxml.equals("ECL KAWASAKI")
					? "日本国際輸送㈱  " + this.locationNameJrxml
					: this.locationNameJrxml;
		}
		return this.locationNameJrxml;
	}

	public void setLocationNameJrxml(String locationNameJrxml) {
		this.locationNameJrxml = locationNameJrxml;
	}

	public Integer getTotalCount() {
		totalCount = 0;
		for (InspectionStockDetailsDto stk : items) {
			if (!AppUtil.isObjectEmpty(stk)) {
				totalCount++;
			}
		}
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

}
