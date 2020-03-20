package com.nexware.aajapan.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TInspectionBookingDataDto {

	private String code;
	private String stockNo;
	private String instructionId;
	private String destinationCountry;
	private String destinationPort;
	private String comment;
	private Integer status;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date inspectionDate;
	private Integer inspectionCompanyId;
	private String inspectionCompany;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date inspectionSentDate;// created Date
	private String chassisNo;
	private String maker;
	private String model;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date purchaseDate;
	private Integer doumentSentStatus;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date documentSentDate;
	private String certificateNo;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date dateOfIssue;

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

	public String getInstructionId() {
		return instructionId;
	}

	public void setInstructionId(String instructionId) {
		this.instructionId = instructionId;
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

	public Integer getInspectionCompanyId() {
		return inspectionCompanyId;
	}

	public void setInspectionCompanyId(Integer inspectionCompanyId) {
		this.inspectionCompanyId = inspectionCompanyId;
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

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public Integer getDoumentSentStatus() {
		return doumentSentStatus;
	}

	public void setDoumentSentStatus(Integer doumentSentStatus) {
		this.doumentSentStatus = doumentSentStatus;
	}

	public Date getDocumentSentDate() {
		return documentSentDate;
	}

	public void setDocumentSentDate(Date documentSentDate) {
		this.documentSentDate = documentSentDate;
	}

	public String getCertificateNo() {
		return certificateNo;
	}

	public void setCertificateNo(String certificateNo) {
		this.certificateNo = certificateNo;
	}

	public Date getDateOfIssue() {
		return dateOfIssue;
	}

	public void setDateOfIssue(Date dateOfIssue) {
		this.dateOfIssue = dateOfIssue;
	}

}
