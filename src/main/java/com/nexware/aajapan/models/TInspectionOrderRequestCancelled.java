package com.nexware.aajapan.models;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.listeners.EntityModelBase;

@Document(collection = "t_inspctn_odr_rqst_cancld")
public class TInspectionOrderRequestCancelled extends EntityModelBase {

	@Id
	private String id;
	private String stockNo;
	@Indexed(unique = true)
	private String code;
	private String inspectionCode;
	private String inspectionCountry;
	private String inspectionDestCounrtry;
	private String inspectionCompany;
	private String location;
	private String inspectionFlag;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date inspectionDate;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date inspectionCreatedDate;
	private String remarks;

	public TInspectionOrderRequestCancelled(String code, String stockNo, String inspectionCode,
			String inspectionCountry, String inspectionCompany, String location, String inspectionDestCounrtry,
			Date inspectionDate, Date inspectionCreatedDate, String remarks, String inspectionFlag) {
		super();
		this.code = code;
		this.stockNo = stockNo;
		this.inspectionCode = inspectionCode;
		this.inspectionCountry = inspectionCountry;
		this.inspectionCompany = inspectionCompany;
		this.location = location;
		this.inspectionDestCounrtry = inspectionDestCounrtry;
		this.inspectionDate = inspectionDate;
		this.inspectionCreatedDate = inspectionCreatedDate;
		this.remarks = remarks;
		this.inspectionFlag = inspectionFlag;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getInspectionCode() {
		return inspectionCode;
	}

	public void setInspectionCode(String inspectionCode) {
		this.inspectionCode = inspectionCode;
	}

	public String getInspectionCountry() {
		return inspectionCountry;
	}

	public void setInspectionCountry(String inspectionCountry) {
		this.inspectionCountry = inspectionCountry;
	}

	public String getInspectionDestCounrtry() {
		return inspectionDestCounrtry;
	}

	public void setInspectionDestCounrtry(String inspectionDestCounrtry) {
		this.inspectionDestCounrtry = inspectionDestCounrtry;
	}

	public Date getInspectionDate() {
		return inspectionDate;
	}

	public void setInspectionDate(Date inspectionDate) {
		this.inspectionDate = inspectionDate;
	}

	public Date getInspectionCreatedDate() {
		return inspectionCreatedDate;
	}

	public void setInspectionCreatedDate(Date inspectionCreatedDate) {
		this.inspectionCreatedDate = inspectionCreatedDate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getStockNo() {
		return stockNo;
	}

	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}

	public String getInspectionCompany() {
		return inspectionCompany;
	}

	public void setInspectionCompany(String inspectionCompany) {
		this.inspectionCompany = inspectionCompany;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getInspectionFlag() {
		return inspectionFlag;
	}

	public void setInspectionFlag(String inspectionFlag) {
		this.inspectionFlag = inspectionFlag;
	}

}
