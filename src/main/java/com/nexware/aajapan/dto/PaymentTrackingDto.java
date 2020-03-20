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

public class PaymentTrackingDto {

	private Integer transactionNo;
	private String invoiceNo;
	private String refNo;
	private Double totalAmount;
	private String invoiceName;
	private Double paidAmount;
	private Double balance;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date approvedDate;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date invoiceDate;
	private String approvedBy;
	private Integer paymentApproveStatus;
	private Integer invoiceUpload;
	private List<Map<String, Object>> approvePaymentItems = new ArrayList<>();
	private String type;
	private String supplierName;
	private String auctionHouseName;
	private ObjectId auctionHouseId;
	private String sAuctionHouseId;
	private String invoiceAttachmentDiskFilename;
	private String bankStatementAttachmentFilename;
	private String attachmentDirectory;
	private Double totalAmountUsd;
	private Double paidAmountUsd;
	private Double balanceUsd;
	private Integer currency;
	private String currencySymbol;

	public String getInvoiceName() {
		if ((AppUtil.ifNull(this.type, "")).equals(Constants.PURCHASE_INVOICE_ITEM_TYPE_PURCHASE)) {
			this.invoiceName = this.supplierName;
			if (!AppUtil.isObjectEmpty(this.auctionHouseName)) {
				this.invoiceName += "/" + this.auctionHouseName;
			}
		} else {
			return this.invoiceName;
		}

		return this.invoiceName;
	}

	public void setInvoiceName(String invoiceName) {
		this.invoiceName = invoiceName;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public List<Map<String, Object>> getApprovePaymentItems() {
		return approvePaymentItems;
	}

	public void setApprovePaymentItems(List<Map<String, Object>> approvePaymentItems) {
		this.approvePaymentItems = approvePaymentItems;
	}

	public Integer getPaymentApproveStatus() {
		return paymentApproveStatus;
	}

	public void setPaymentApproveStatus(Integer paymentApproveStatus) {
		this.paymentApproveStatus = paymentApproveStatus;
	}

	public ObjectId getAuctionHouseId() {
		return auctionHouseId;
	}

	public void setAuctionHouseId(ObjectId auctionHouseId) {
		this.auctionHouseId = auctionHouseId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getsAuctionHouseId() {
		sAuctionHouseId = !AppUtil.isObjectEmpty(this.getAuctionHouseId()) ? this.getAuctionHouseId().toString() : "";
		return sAuctionHouseId;

	}

	public void setsAuctionHouseId(String sAuctionHouseId) {
		this.sAuctionHouseId = sAuctionHouseId;
	}

	public Integer getTransactionNo() {
		return transactionNo;
	}

	public void setTransactionNo(Integer transactionNo) {
		this.transactionNo = transactionNo;
	}

	public Double getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(Double paidAmount) {
		this.paidAmount = paidAmount;
	}

	public Double getBalance() {
		balance = AppUtil.ifNull(totalAmount, 0.0) - AppUtil.ifNull(paidAmount, 0.0);
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Date getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
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
		} else if ((AppUtil.ifNull(type, "")).equals(Constants.INVOICE_TYPE_INSPECTION)) {
			attachmentDirectory = Constants.ATTACHMENT_DIRECTORY_INSPECTION_INVOICE;
		}
		return attachmentDirectory;
	}

	public void setAttachmentDirectory(String attachmentDirectory) {
		this.attachmentDirectory = attachmentDirectory;
	}

	public Double getTotalAmountUsd() {
		return totalAmountUsd;
	}

	public void setTotalAmountUsd(Double totalAmountUsd) {
		this.totalAmountUsd = totalAmountUsd;
	}

	public Double getPaidAmountUsd() {
		return paidAmountUsd;
	}

	public void setPaidAmountUsd(Double paidAmountUsd) {
		this.paidAmountUsd = paidAmountUsd;
	}

	public Double getBalanceUsd() {
		balanceUsd = AppUtil.ifNull(totalAmountUsd, 0.0) - AppUtil.ifNull(paidAmountUsd, 0.0);
		return balanceUsd;
	}

	public void setBalanceUsd(Double balanceUsd) {
		this.balanceUsd = balanceUsd;
	}

	public Integer getInvoiceUpload() {
		return invoiceUpload;
	}

	public void setInvoiceUpload(Integer invoiceUpload) {
		this.invoiceUpload = invoiceUpload;
	}

	public String getRefNo() {
		return refNo;
	}

	public void setRefNo(String refNo) {
		this.refNo = refNo;
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
}
