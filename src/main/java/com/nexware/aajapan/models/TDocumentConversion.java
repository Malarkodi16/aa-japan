package com.nexware.aajapan.models;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.listeners.EntityModelBase;

@Document(collection = "t_dcmnt_cnvrsn")
public class TDocumentConversion extends EntityModelBase {
	@Id
	private String id;
	@Indexed
	private String stockNo;
	private Integer docConvertTo;
	private Integer exportCertificateStatus;
	private String shippingCompanyId;// refer m_shppng_cmpny
	private String inspectionCompanyId;// refer m_shppng_cmpny
	private Integer docOriginalSent;
	private Integer docEmailSent;
	private Integer status;
	private Integer docReceivedStatus;
	private Integer handoverStatus;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date docSendDate;
	private Integer isExpired;
	private String supplier;
	private ObjectId auctionHouse;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date reauctionDate;

	public TDocumentConversion(String stockNo, Integer docConvertTo) {
		this.stockNo = stockNo;
		this.docConvertTo = docConvertTo;
		this.exportCertificateStatus = Constants.EXPORT_CERTIFICATE_INALAIN;
		this.docOriginalSent = Constants.EXPORT_CERTIFICATE_DOCUMENT_ORIGINAL_NOT_SENT;
		this.docEmailSent = Constants.EXPORT_CERTIFICATE_DOCUMENT_EMAIL_NOT_SENT;
		this.docReceivedStatus = Constants.EXPORT_CERTIFICATE_ORIGINAL_NOT_RECEIVED;
		this.handoverStatus = Constants.EXPORT_CERTIFICATE_NOT_HANDOVER;
	}

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

	public Integer getExportCertificateStatus() {
		return this.exportCertificateStatus;
	}

	public void setExportCertificateStatus(Integer exportCertificateStatus) {
		this.exportCertificateStatus = exportCertificateStatus;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getDocConvertTo() {
		return this.docConvertTo;
	}

	public void setDocConvertTo(Integer docConvertTo) {
		this.docConvertTo = docConvertTo;
	}

	public Date getDocSendDate() {
		return this.docSendDate;
	}

	public void setDocSendDate(Date docSendDate) {
		this.docSendDate = docSendDate;
	}

	public String getShippingCompanyId() {
		return this.shippingCompanyId;
	}

	public void setShippingCompanyId(String shippingCompanyId) {
		this.shippingCompanyId = shippingCompanyId;
	}

	public String getInspectionCompanyId() {
		return this.inspectionCompanyId;
	}

	public void setInspectionCompanyId(String inspectionCompanyId) {
		this.inspectionCompanyId = inspectionCompanyId;
	}

	public Integer getDocReceivedStatus() {
		return this.docReceivedStatus;
	}

	public void setDocReceivedStatus(Integer docReceivedStatus) {
		this.docReceivedStatus = docReceivedStatus;
	}

	public Integer getHandoverStatus() {
		return this.handoverStatus;
	}

	public void setHandoverStatus(Integer handoverStatus) {
		this.handoverStatus = handoverStatus;
	}

	public Integer getDocOriginalSent() {
		return this.docOriginalSent;
	}

	public void setDocOriginalSent(Integer docOriginalSent) {
		this.docOriginalSent = docOriginalSent;
	}

	public Integer getDocEmailSent() {
		return this.docEmailSent;
	}

	public void setDocEmailSent(Integer docEmailSent) {
		this.docEmailSent = docEmailSent;
	}

	public Integer getIsExpired() {
		return this.isExpired;
	}

	public void setIsExpired(Integer isExpired) {
		this.isExpired = isExpired;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public ObjectId getAuctionHouse() {
		return auctionHouse;
	}

	public void setAuctionHouse(ObjectId auctionHouse) {
		this.auctionHouse = auctionHouse;
	}

	public Date getReauctionDate() {
		return reauctionDate;
	}

	public void setReauctionDate(Date reauctionDate) {
		this.reauctionDate = reauctionDate;
	}

}
