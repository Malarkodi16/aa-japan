package com.nexware.aajapan.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.core.Constants;

public class TFreightShippingInvoiceDto {

	private String invoiceNo;
	private List<TFreightShippingInvoiceItemsDto> items;
	private List<Map<String, String>> invoiceItems = new ArrayList<>();
	private Double shippingCharge;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date createdDate;
	private Integer noOfContainers;
	private Integer paymentApprove;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date dueDate;
	private String invoiceName;
	private String supplierId;
	private Double totalAmount;
	private Double totalAmountUsd;
	private Integer invoiceType;
	private String invoiceAttachmentDiskFilename;
	private String attachmentDirectory;
	private String paymentVoucherNo;

	public String getInvoiceNo() {
		return this.invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public List<TFreightShippingInvoiceItemsDto> getItems() {
		return this.items;
	}

	public void setItems(List<TFreightShippingInvoiceItemsDto> items) {
		this.items = items;
	}

	public Double getShippingCharge() {
		return this.shippingCharge;
	}

	public void setShippingCharge(Double shippingCharge) {
		this.shippingCharge = shippingCharge;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getNoOfContainers() {
		return this.noOfContainers;
	}

	public void setNoOfContainers(Integer noOfContainers) {
		this.noOfContainers = noOfContainers;
	}

	public Integer getPaymentApprove() {
		return this.paymentApprove;
	}

	public void setPaymentApprove(Integer paymentApprove) {
		this.paymentApprove = paymentApprove;
	}

	public Date getDueDate() {
		return this.dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public String getInvoiceName() {
		return this.invoiceName;
	}

	public void setInvoiceName(String invoiceName) {
		this.invoiceName = invoiceName;
	}

	public String getSupplierId() {
		return this.supplierId;
	}

	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}

	public Double getTotalAmount() {
		return this.totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getTotalAmountUsd() {
		return totalAmountUsd;
	}

	public void setTotalAmountUsd(Double totalAmountUsd) {
		this.totalAmountUsd = totalAmountUsd;
	}

	public List<Map<String, String>> getInvoiceItems() {
		return invoiceItems;
	}

	public void setInvoiceItems(List<Map<String, String>> invoiceItems) {
		this.invoiceItems = invoiceItems;
	}

	public Integer getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(Integer invoiceType) {
		this.invoiceType = invoiceType;
	}

	public String getInvoiceAttachmentDiskFilename() {
		return invoiceAttachmentDiskFilename;
	}

	public void setInvoiceAttachmentDiskFilename(String invoiceAttachmentDiskFilename) {
		this.invoiceAttachmentDiskFilename = invoiceAttachmentDiskFilename;
	}

	public String getAttachmentDirectory() {
		attachmentDirectory = Constants.ATTACHMENT_DIRECTORY_SHIPPING_INVOICE;
		return attachmentDirectory;
	}

	public void setAttachmentDirectory(String attachmentDirectory) {
		this.attachmentDirectory = attachmentDirectory;
	}

	public String getPaymentVoucherNo() {
		return paymentVoucherNo;
	}

	public void setPaymentVoucherNo(String paymentVoucherNo) {
		this.paymentVoucherNo = paymentVoucherNo;
	}

}
