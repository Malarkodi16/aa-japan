package com.nexware.aajapan.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.utils.AppUtil;

public class TFreightShippingContainerInvoiceDto {

	private String invoiceNo;
	private String remitTo;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date createdDate;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date dueDate;
	private Double total;
	private Integer invoiceUpload;
	private Integer attachementViewed;
	private String invoiceAttachmentDiskFilename;
	private String attachmentDirectory;
	private String type;
	private String forwarderId;
	private List<TFreightShippingContainerInvoiceItemDto> items;

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getRemitTo() {
		return remitTo;
	}

	public void setRemitTo(String remitTo) {
		this.remitTo = remitTo;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public List<TFreightShippingContainerInvoiceItemDto> getItems() {
		return items;
	}

	public void setItems(List<TFreightShippingContainerInvoiceItemDto> items) {
		this.items = items;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
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

	public String getInvoiceAttachmentDiskFilename() {
		return invoiceAttachmentDiskFilename;
	}

	public void setInvoiceAttachmentDiskFilename(String invoiceAttachmentDiskFilename) {
		this.invoiceAttachmentDiskFilename = invoiceAttachmentDiskFilename;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAttachmentDirectory() {
		if ((AppUtil.ifNull(type, "")).equals(Constants.INVOICE_TYPE_FREIGHT)) {
			attachmentDirectory = Constants.ATTACHMENT_DIRECTORY_SHIPPING_INVOICE;
		}
		return attachmentDirectory;
	}

	public void setAttachmentDirectory(String attachmentDirectory) {
		this.attachmentDirectory = attachmentDirectory;
	}

	public String getForwarderId() {
		return forwarderId;
	}

	public void setForwarderId(String forwarderId) {
		this.forwarderId = forwarderId;
	}

}
