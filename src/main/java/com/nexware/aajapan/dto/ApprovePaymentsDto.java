package com.nexware.aajapan.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.utils.AppUtil;

public class ApprovePaymentsDto {

	// necessary fields
	private String remitter;
	private String paymentVoucherNo;

	private String remitterOthers;// refer m_frwrdr

	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date invoiceDate;
	private ObjectId auctionHouseId;
	private String type;
	private String supplierId;
	private String transporterId;
	private String invoiceNo;
	private String invoiceName;
	private Integer lotNo;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date dueDate;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date createdDate;
	private Double totalAmount;
	private List<Map<String, String>> approvePaymentItems = new ArrayList<>();
	private List<Map<String, String>> invoiceItems = new ArrayList<>();
	private List<Map<String, String>> invoiceTransaction = new ArrayList<>();

	private String invoiceAttachmentFilename;
	private String invoiceAttachmentDiskFilename;
	private String bankStatementAttachmentFilename;
	private String bankStatementAttachmentDiskFilename;
	private String attachmentDirectory;
	private Double invoiceBalanceAmount;
	private Double invoiceAmountReceived;
	private Integer invoiceUpload;
	private Integer attachementViewed;
	// unnecessary fields
	private String id;
	private Integer invoiceType;
	private String supplierName;
	private String auctionHouseName;

	private String bank;
	private Integer approvePaymentStatus;
	private String category;
	private String refNo;
	private Double amount;
	private String remarks;
	private Integer status;
	private String currency;
	private Integer currencyType;
	private Double exchangeRate;

	private String forwarderName;
	private Double totalAmountUsd;
	private Double invoiceBalanceAmountUsd;
	private Double invoiceAmountReceivedUsd;
	private Integer paymentType;
	private Double totalFreightAmount;
	private Double otherTotalAmount;
	private Double containerUsd;
	private String supplier;
	private String auctionHouse;
	private String auctionRefNo;

	
	public String getTransporterId() {
		return transporterId;
	}

	public void setTransporterId(String transporterId) {
		this.transporterId = transporterId;
	}

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getInvoiceName() {
		return invoiceName;
	}

	public void setInvoiceName(String invoiceName) {
		this.invoiceName = invoiceName;
	}

	public Double getTotalAmount() {
		totalAmount = AppUtil.ifNull(totalAmount, 0.0);
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {

		this.totalAmount = totalAmount;
	}

	public List<Map<String, String>> getApprovePaymentItems() {
		return approvePaymentItems;
	}

	public void setApprovePaymentItems(List<Map<String, String>> approvePaymentItems) {
		this.approvePaymentItems = approvePaymentItems;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}

	public ObjectId getAuctionHouseId() {
		return auctionHouseId;
	}

	public void setAuctionHouseId(ObjectId auctionHouseId) {
		this.auctionHouseId = auctionHouseId;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getAuctionHouseName() {
		return auctionHouseName;
	}

	public void setAuctionHouseName(String auctionHouseName) {
		this.auctionHouseName = auctionHouseName;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public Integer getApprovePaymentStatus() {
		return approvePaymentStatus;
	}

	public void setApprovePaymentStatus(Integer approvePaymentStatus) {
		this.approvePaymentStatus = approvePaymentStatus;
	}

	public String getRemitter() {
		return remitter;
	}

	public void setRemitter(String remitter) {
		this.remitter = remitter;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getRefNo() {
		return refNo;
	}

	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getForwarderName() {
		return forwarderName;
	}

	public void setForwarderName(String forwarderName) {
		this.forwarderName = forwarderName;
	}

	public String getAttachmentDirectory() {
		if (AppUtil.ifNull(type, "").equals(Constants.PURCHASE_INVOICE_ITEM_TYPE_PURCHASE)) {
			attachmentDirectory = Constants.ATTACHMENT_DIRECTORY_AUCTION_INVOICE;
		} else if ((AppUtil.ifNull(type, "")).equals(Constants.INVOICE_TYPE_TRANSPORT)) {
			attachmentDirectory = Constants.ATTACHMENT_DIRECTORY_TRANSPORT_INVOICE;
		} else if ((AppUtil.ifNull(type, "")).equals(Constants.INVOICE_TYPE_FORWARDER)) {
			attachmentDirectory = Constants.ATTACHMENT_DIRECTORY_FORWARDER_INVOICE;
		} else if ((AppUtil.ifNull(type, "")).equals(Constants.INVOICE_TYPE_OTHERS)) {
			attachmentDirectory = Constants.ATTACHMENT_DIRECTORY_OTHER_INVOICE;
		} else if ((AppUtil.ifNull(type, "")).equals(Constants.INVOICE_TYPE_FREIGHT)) {
			attachmentDirectory = Constants.ATTACHMENT_DIRECTORY_SHIPPING_INVOICE;
		} else if ((AppUtil.ifNull(type, "")).equals(Constants.INVOICE_TYPE_INSPECTION)) {
			attachmentDirectory = Constants.ATTACHMENT_DIRECTORY_INSPECTION_INVOICE;
		}
		return attachmentDirectory;
	}

	public void setAttachmentDirectory(String attachmentDirectory) {
		this.attachmentDirectory = attachmentDirectory;
	}

	public String getRemitterOthers() {
		return remitterOthers;
	}

	public void setRemitterOthers(String remitterOthers) {
		this.remitterOthers = remitterOthers;
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

	public Double getInvoiceBalanceAmount() {
		invoiceBalanceAmount = AppUtil.ifNull(totalAmount, 0.0) - AppUtil.ifNull(invoiceAmountReceived, 0.0);
		return invoiceBalanceAmount;
	}

	public void setInvoiceBalanceAmount(Double invoiceBalanceAmount) {
		this.invoiceBalanceAmount = invoiceBalanceAmount;
	}

	public Double getInvoiceAmountReceived() {
		return invoiceAmountReceived;
	}

	public void setInvoiceAmountReceived(Double invoiceAmountReceived) {
		this.invoiceAmountReceived = invoiceAmountReceived;
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

	public Double getTotalAmountUsd() {
		return totalAmountUsd;
	}

	public void setTotalAmountUsd(Double totalAmountUsd) {
		this.totalAmountUsd = totalAmountUsd;
	}

	public Double getInvoiceBalanceAmountUsd() {
		invoiceBalanceAmountUsd = AppUtil.ifNull(totalAmountUsd, 0.0) - AppUtil.ifNull(invoiceAmountReceivedUsd, 0.0);
		return invoiceBalanceAmountUsd;
	}

	public void setInvoiceBalanceAmountUsd(Double invoiceBalanceAmountUsd) {
		this.invoiceBalanceAmountUsd = invoiceBalanceAmountUsd;
	}

	public Double getInvoiceAmountReceivedUsd() {
		return invoiceAmountReceivedUsd;
	}

	public void setInvoiceAmountReceivedUsd(Double invoiceAmountReceivedUsd) {
		this.invoiceAmountReceivedUsd = invoiceAmountReceivedUsd;
	}

	public Integer getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(Integer paymentType) {
		this.paymentType = paymentType;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public Double getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public Double getTotalFreightAmount() {
		return totalFreightAmount;
	}

	public void setTotalFreightAmount(Double totalFreightAmount) {
		this.totalFreightAmount = totalFreightAmount;
	}

	public Double getOtherTotalAmount() {
		return otherTotalAmount;
	}

	public void setOtherTotalAmount(Double otherTotalAmount) {
		this.otherTotalAmount = otherTotalAmount;
	}

	public Integer getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(Integer invoiceType) {
		this.invoiceType = invoiceType;
	}

	public List<Map<String, String>> getInvoiceItems() {
		return invoiceItems;
	}

	public void setInvoiceItems(List<Map<String, String>> invoiceItems) {
		this.invoiceItems = invoiceItems;
	}

	public Double getContainerUsd() {
		return containerUsd;
	}

	public void setContainerUsd(Double containerUsd) {
		this.containerUsd = containerUsd;
	}

	public String getPaymentVoucherNo() {
		return paymentVoucherNo;
	}

	public void setPaymentVoucherNo(String paymentVoucherNo) {
		this.paymentVoucherNo = paymentVoucherNo;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getAuctionHouse() {
		return auctionHouse;
	}

	public void setAuctionHouse(String auctionHouse) {
		this.auctionHouse = auctionHouse;
	}

	public Integer getLotNo() {
		return lotNo;
	}

	public void setLotNo(Integer lotNo) {
		this.lotNo = lotNo;
	}

	public String getAuctionRefNo() {
		return auctionRefNo;
	}

	public void setAuctionRefNo(String auctionRefNo) {
		this.auctionRefNo = auctionRefNo;
	}

	public List<Map<String, String>> getInvoiceTransaction() {
		return invoiceTransaction;
	}

	public void setInvoiceTransaction(List<Map<String, String>> invoiceTransaction) {
		this.invoiceTransaction = invoiceTransaction;
	}

}
