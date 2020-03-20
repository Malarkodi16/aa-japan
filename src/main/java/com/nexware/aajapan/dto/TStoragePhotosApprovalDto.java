package com.nexware.aajapan.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.utils.AppUtil;

public class TStoragePhotosApprovalDto {

	// approval values
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date invoiceDate;

	private String supplierId;
	private String forwarderName;
	private String remitter;
	private String refNo;
	private String remarks;
	private String invoiceNo;
	private String paymentVoucherNo;
	private String invoiceAttachmentFilename;
	private String invoiceAttachmentDiskFilename;
	private String attachmentDirectory;
	private Integer invoiceUpload;
	private Integer attachementViewed;
	private Integer invoiceType;
	private Integer currency;
	private String type;
	private String currencySymbol;
	private Double amount;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date dueDate;
	private List<Map<String, Object>> approvePaymentItems = new ArrayList<>();
	// processing values
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date createdDate;
	private String invoiceName;
	private Double totalAmount;
	private Double invoiceAmountReceived;
	private Double invoiceBalanceAmount;
	// approval screen
	private Integer approvePaymentStatus;
	private org.bson.Document chargesList;
	private List<ColumnDefDto> metaData;
	private Double exchangeRate;
	
	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getForwarderName() {
		return forwarderName;
	}

	public void setForwarderName(String forwarderName) {
		this.forwarderName = forwarderName;
	}

	public String getRemitter() {
		return remitter;
	}

	public void setRemitter(String remitter) {
		this.remitter = remitter;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
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
		if ((AppUtil.ifNull(type, "")).equals(Constants.PURCHASE_INVOICE_ITEM_TYPE_PURCHASE)) {
			attachmentDirectory = Constants.ATTACHMENT_DIRECTORY_AUCTION_INVOICE;
		} else if ((AppUtil.ifNull(type, "")).equals(Constants.INVOICE_TYPE_TRANSPORT)) {
			attachmentDirectory = Constants.ATTACHMENT_DIRECTORY_TRANSPORT_INVOICE;
		} else if ((AppUtil.ifNull(type, "")).equals(Constants.INVOICE_TYPE_FORWARDER)) {
			attachmentDirectory = Constants.ATTACHMENT_DIRECTORY_FORWARDER_INVOICE;
		} else if ((AppUtil.ifNull(type, "")).equals(Constants.INVOICE_TYPE_OTHERS)) {
			attachmentDirectory = Constants.ATTACHMENT_DIRECTORY_OTHER_INVOICE;
		} else if ((AppUtil.ifNull(type, "")).equals(Constants.INVOICE_TYPE_FREIGHT)) {
			attachmentDirectory = Constants.ATTACHMENT_DIRECTORY_SHIPPING_INVOICE;
		}
		return attachmentDirectory;
	}

	public void setAttachmentDirectory(String attachmentDirectory) {
		this.attachmentDirectory = attachmentDirectory;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public List<Map<String, Object>> getApprovePaymentItems() {
		return approvePaymentItems;
	}

	public void setApprovePaymentItems(List<Map<String, Object>> approvePaymentItems) {
		this.approvePaymentItems = approvePaymentItems;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getInvoiceName() {
		return invoiceName;
	}

	public void setInvoiceName(String invoiceName) {
		this.invoiceName = invoiceName;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getInvoiceAmountReceived() {
		return invoiceAmountReceived;
	}

	public void setInvoiceAmountReceived(Double invoiceAmountReceived) {
		this.invoiceAmountReceived = invoiceAmountReceived;
	}

	public Double getInvoiceBalanceAmount() {
		invoiceBalanceAmount = AppUtil.ifNull(totalAmount, 0.0) - AppUtil.ifNull(invoiceAmountReceived, 0.0);
		return invoiceBalanceAmount;
	}

	public void setInvoiceBalanceAmount(Double invoiceBalanceAmount) {
		this.invoiceBalanceAmount = invoiceBalanceAmount;
	}

	public Integer getApprovePaymentStatus() {
		return approvePaymentStatus;
	}

	public void setApprovePaymentStatus(Integer approvePaymentStatus) {
		this.approvePaymentStatus = approvePaymentStatus;
	}

	public String getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getCurrency() {
		return currency;
	}

	public void setCurrency(Integer currency) {
		this.currency = currency;
	}

	public String getCurrencySymbol() {
		return currencySymbol;
	}

	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}

	public org.bson.Document getChargesList() {
		return chargesList;
	}

	public void setChargesList(org.bson.Document chargesList) {
		this.chargesList = chargesList;
	}

	public List<ColumnDefDto> getMetaData() {
		return metaData;
	}

	public void setMetaData(List<ColumnDefDto> metaData) {
		this.metaData = metaData;
	}

	public Integer getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(Integer invoiceType) {
		this.invoiceType = invoiceType;
	}

	public Double getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

}
