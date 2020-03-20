package com.nexware.aajapan.dto;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.utils.AppUtil;

public class TInspectionInvoiceDto {

	private String id;
	private String invoiceNo;
	private String paymentVoucherNo;
	private String refNo;
	private String inspectionCompanyId;
	private String inspectionCompany;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	protected Date dueDate;
	private Double invoiceTotal;
	private List<TInspectionInvoiceStockDto> items;
	private Integer paymentStatus;
	private String invoiceAttachmentFilename;
	private String invoiceAttachmentDiskFilename;
	private String attachmentDirectory;
	private String type;
	private Integer invoiceUpload;
	private Integer attachementViewed;
	@JsonFormat(pattern = "dd-MM-yyyy")
	protected Date invoiceDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getPaymentVoucherNo() {
		return paymentVoucherNo;
	}

	public void setPaymentVoucherNo(String paymentVoucherNo) {
		this.paymentVoucherNo = paymentVoucherNo;
	}

	public String getRefNo() {
		return refNo;
	}

	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}

	public String getInspectionCompanyId() {
		return inspectionCompanyId;
	}

	public void setInspectionCompanyId(String inspectionCompanyId) {
		this.inspectionCompanyId = inspectionCompanyId;
	}

	public String getInspectionCompany() {
		return inspectionCompany;
	}

	public void setInspectionCompany(String inspectionCompany) {
		this.inspectionCompany = inspectionCompany;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public Double getInvoiceTotal() {
		return invoiceTotal;
	}

	public void setInvoiceTotal(Double invoiceTotal) {
		this.invoiceTotal = invoiceTotal;
	}

	public List<TInspectionInvoiceStockDto> getItems() {
		return items;
	}

	public void setItems(List<TInspectionInvoiceStockDto> items) {
		this.items = items;
	}

	public Integer getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(Integer paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getInvoiceAttachmentFilename() {
		return invoiceAttachmentFilename;
	}

	public void setInvoiceAttachmentFilename(String invoiceAttachmentFilename) {
		this.invoiceAttachmentFilename = invoiceAttachmentFilename;
	}

	public String getInvoiceAttachmentDiskFilename() {
		return invoiceAttachmentDiskFilename;
	}

	public void setInvoiceAttachmentDiskFilename(String invoiceAttachmentDiskFilename) {
		this.invoiceAttachmentDiskFilename = invoiceAttachmentDiskFilename;
	}

	public String getAttachmentDirectory() {
		if ((AppUtil.ifNull(this.type, "")).equals(Constants.INVOICE_TYPE_INSPECTION)) {
			this.attachmentDirectory = Constants.ATTACHMENT_DIRECTORY_INSPECTION_INVOICE;
		} else {
			this.attachmentDirectory = "";
		}
		return this.attachmentDirectory;
	}

	public void setAttachmentDirectory(String attachmentDirectory) {
		this.attachmentDirectory = attachmentDirectory;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getInvoiceUpload() {
		return invoiceUpload;
	}

	public void setInvoiceUpload(Integer invoiceUpload) {
		this.invoiceUpload = invoiceUpload;
	}

	public Integer getAttachementViewed() {
		return attachementViewed;
	}

	public void setAttachementViewed(Integer attachementViewed) {
		this.attachementViewed = attachementViewed;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

}
