package com.nexware.aajapan.dto;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.models.InspectionCompanyLocation;

public class TStockInspectionInfoDto {

	private String stockNo;
	private String inspectionCountry;
	private Integer inspectionStatus;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date inspectionSentDate;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date inspectionDate;
	private Integer inspectionDocStatus;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date inspectionDocSentDate;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date dateOfIssue;
	private String certificateNo;
	private String inspectionCompany;
	private InspectionCompanyLocation inspectionLocation;
	private List<TStockInspectionGroupInfoDto> inspectionGroupInfo;
	public String getStockNo() {
		return stockNo;
	}
	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}
	public String getInspectionCountry() {
		return inspectionCountry;
	}
	public void setInspectionCountry(String inspectionCountry) {
		this.inspectionCountry = inspectionCountry;
	}
	public Integer getInspectionStatus() {
		return inspectionStatus;
	}
	public void setInspectionStatus(Integer inspectionStatus) {
		this.inspectionStatus = inspectionStatus;
	}
	
	public Integer getInspectionDocStatus() {
		return inspectionDocStatus;
	}
	public void setInspectionDocStatus(Integer inspectionDocStatus) {
		this.inspectionDocStatus = inspectionDocStatus;
	}
	
	public String getCertificateNo() {
		return certificateNo;
	}
	public void setCertificateNo(String certificateNo) {
		this.certificateNo = certificateNo;
	}
	public String getInspectionCompany() {
		return inspectionCompany;
	}
	public void setInspectionCompany(String inspectionCompany) {
		this.inspectionCompany = inspectionCompany;
	}
	public InspectionCompanyLocation getInspectionLocation() {
		return inspectionLocation;
	}
	public void setInspectionLocation(InspectionCompanyLocation inspectionLocation) {
		this.inspectionLocation = inspectionLocation;
	}
	public List<TStockInspectionGroupInfoDto> getInspectionGroupInfo() {
		return inspectionGroupInfo;
	}
	public void setInspectionGroupInfo(List<TStockInspectionGroupInfoDto> inspectionGroupInfo) {
		this.inspectionGroupInfo = inspectionGroupInfo;
	}
	public Date getInspectionSentDate() {
		return inspectionSentDate;
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
	public Date getInspectionDocSentDate() {
		return inspectionDocSentDate;
	}
	public void setInspectionDocSentDate(Date inspectionDocSentDate) {
		this.inspectionDocSentDate = inspectionDocSentDate;
	}
	public Date getDateOfIssue() {
		return dateOfIssue;
	}
	public void setDateOfIssue(Date dateOfIssue) {
		this.dateOfIssue = dateOfIssue;
	}
	
	

}
