package com.nexware.aajapan.models;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.listeners.EntityModelBase;

@Document(collection = "t_insurance")
public class TInsurance extends EntityModelBase {
	@Id
	private String id;
	@Indexed
	private String chassisNo;
	private String company;// mapped in Constants.java
	private String insuranceNo;
	private String ownerAddress;
	private String ownerName;
	private String fromYear;
	private String fromMonth;
	private String fromDate;
	private String toYear;
	private String toMonth;
	private String toDate;
	private String period;
	private Integer insuranceApplied;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date insuranceApplyDate;

	public TInsurance(String chassisNo, String company, String insuranceNo, String ownerAddress, String ownerName) {
		super();
		this.chassisNo = chassisNo;
		this.company = company;
		this.insuranceNo = insuranceNo;
		this.ownerAddress = ownerAddress;
		this.ownerName = ownerName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getChassisNo() {
		return chassisNo;
	}

	public void setChassisNo(String chassisNo) {
		this.chassisNo = chassisNo;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getInsuranceNo() {
		return insuranceNo;
	}

	public void setInsuranceNo(String insuranceNo) {
		this.insuranceNo = insuranceNo;
	}

	public String getOwnerAddress() {
		return ownerAddress;
	}

	public void setOwnerAddress(String ownerAddress) {
		this.ownerAddress = ownerAddress;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getFromYear() {
		return fromYear;
	}

	public void setFromYear(String fromYear) {
		this.fromYear = fromYear;
	}

	public String getFromMonth() {
		return fromMonth;
	}

	public void setFromMonth(String fromMonth) {
		this.fromMonth = fromMonth;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToYear() {
		return toYear;
	}

	public void setToYear(String toYear) {
		this.toYear = toYear;
	}

	public String getToMonth() {
		return toMonth;
	}

	public void setToMonth(String toMonth) {
		this.toMonth = toMonth;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public Integer getInsuranceApplied() {
		return insuranceApplied;
	}

	public void setInsuranceApplied(Integer insuranceApplied) {
		this.insuranceApplied = insuranceApplied;
	}

	public Date getInsuranceApplyDate() {
		return insuranceApplyDate;
	}

	public void setInsuranceApplyDate(Date insuranceApplyDate) {
		this.insuranceApplyDate = insuranceApplyDate;
	}

}
