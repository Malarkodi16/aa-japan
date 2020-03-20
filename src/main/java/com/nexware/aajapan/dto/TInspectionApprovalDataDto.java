package com.nexware.aajapan.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.utils.AppUtil;

public class TInspectionApprovalDataDto {

	private String invoiceNo;
	private String refNo;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date dueDate;
	private String company;
	private String companyId;
	private Double totalAmount;
	private List<TInspectionApprovalDataItemsDto> items;
	private Integer invoiceUpload;
	private Integer attachmentViewed;
	private String invoiceAttachmentFilename;
	private String invoiceAttachmentDiskFilename;
	private String bankStatementAttachmentFilename;
	private String bankStatementAttachmentDiskFilename;
	private String attachmentDirectory;
	private String type;

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getRefNo() {
		return refNo;
	}

	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public List<TInspectionApprovalDataItemsDto> getItems() {
		return items;
	}

	public void setItems(List<TInspectionApprovalDataItemsDto> items) {
		this.items = items;
	}

	public Integer getInvoiceUpload() {
		return invoiceUpload;
	}

	public void setInvoiceUpload(Integer invoiceUpload) {
		this.invoiceUpload = invoiceUpload;
	}

	public Integer getAttachmentViewed() {
		return attachmentViewed;
	}

	public void setAttachmentViewed(Integer attachmentViewed) {
		this.attachmentViewed = attachmentViewed;
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

	public String getBankStatementAttachmentFilename() {
		return bankStatementAttachmentFilename;
	}

	public void setBankStatementAttachmentFilename(String bankStatementAttachmentFilename) {
		this.bankStatementAttachmentFilename = bankStatementAttachmentFilename;
	}

	public String getBankStatementAttachmentDiskFilename() {
		return bankStatementAttachmentDiskFilename;
	}

	public void setBankStatementAttachmentDiskFilename(String bankStatementAttachmentDiskFilename) {
		this.bankStatementAttachmentDiskFilename = bankStatementAttachmentDiskFilename;
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

}
