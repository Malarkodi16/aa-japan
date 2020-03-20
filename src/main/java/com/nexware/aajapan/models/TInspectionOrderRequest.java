package com.nexware.aajapan.models;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.listeners.EntityModelBase;

@Document(collection = "t_inspctn_odr_rqst")
public class TInspectionOrderRequest extends EntityModelBase {

	@Id
	private String id;
	@Indexed(unique = true)
	private String code;
	@Indexed
	private String stockNo;
	private String instructionId;
	private String country;
	private String forwarder;
	private String comment;
	private Integer status;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date inspectionDate;
	private Integer doumentSentStatus;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date documentSentDate;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date inspectionSentDate;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date dateOfIssue;
	private String certificateNo;
	private String cancelRemark;
	private String remark;
	private Integer inspectionCompanyFlag;
	private String inspectionCompany;
	private InspectionCompanyLocation location;
	private Integer bookingStatus;

	public String getId() {
		return this.id;
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

	public String getStockNo() {
		return this.stockNo;
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

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getForwarder() {
		return this.forwarder;
	}

	public void setForwarder(String forwarder) {
		this.forwarder = forwarder;
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getDocumentSentDate() {
		return this.documentSentDate;
	}

	public void setDocumentSentDate(Date documentSentDate) {
		this.documentSentDate = documentSentDate;
	}

	public Date getInspectionSentDate() {
		return this.inspectionSentDate;
	}

	public void setInspectionSentDate(Date inspectionSentDate) {
		this.inspectionSentDate = inspectionSentDate;
	}

	public String getCancelRemark() {
		return this.cancelRemark;
	}

	public void setCancelRemark(String cancelRemark) {
		this.cancelRemark = cancelRemark;
	}

	public Date getDateOfIssue() {
		return this.dateOfIssue;
	}

	public void setDateOfIssue(Date dateOfIssue) {
		this.dateOfIssue = dateOfIssue;
	}

	public String getCertificateNo() {
		return this.certificateNo;
	}

	public void setCertificateNo(String certificateNo) {
		this.certificateNo = certificateNo;
	}

	public String getInspectionCompany() {
		return this.inspectionCompany;
	}

	public void setInspectionCompany(String inspectionCompany) {
		this.inspectionCompany = inspectionCompany;
	}

	public InspectionCompanyLocation getLocation() {
		return this.location;
	}

	public void setLocation(InspectionCompanyLocation location) {
		this.location = location;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getDoumentSentStatus() {
		return this.doumentSentStatus;
	}

	public void setDoumentSentStatus(Integer doumentSentStatus) {
		this.doumentSentStatus = doumentSentStatus;
	}

	public Integer getInspectionCompanyFlag() {
		return this.inspectionCompanyFlag;
	}

	public void setInspectionCompanyFlag(Integer inspectionCompanyFlag) {
		this.inspectionCompanyFlag = inspectionCompanyFlag;
	}

	public Date getInspectionDate() {
		return inspectionDate;
	}

	public void setInspectionDate(Date inspectionDate) {
		this.inspectionDate = inspectionDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getBookingStatus() {
		return bookingStatus;
	}

	public void setBookingStatus(Integer bookingStatus) {
		this.bookingStatus = bookingStatus;
	}

}
