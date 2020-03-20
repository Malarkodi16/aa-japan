package com.nexware.aajapan.dto;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.utils.AppUtil;

public class TInvoicePaymentTransactionDto {

	private String code;
	private String paymentVoucherNo;
	private String invoiceType;
	private String invoiceNo;
	private String bankId;
	private Double amount;
	private String remarks;
	private String bankStatementAttachmentFilename;
	private String bankStatementAttachmentDiskFilename;
	private String sApprovedDate;
	private String attachmentDirectory;
	private String bankName;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
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

	public String getsApprovedDate() {
		return sApprovedDate;
	}

	public void setsApprovedDate(String sApprovedDate) {
		this.sApprovedDate = sApprovedDate;
	}

	public String getAttachmentDirectory() {
		if ((AppUtil.ifNull(this.invoiceType, "")).equals(Constants.PURCHASE_INVOICE_ITEM_TYPE_PURCHASE)) {
			this.attachmentDirectory = Constants.ATTACHMENT_DIRECTORY_AUCTION_BANK_STATEMENT;
		} else if ((AppUtil.ifNull(this.invoiceType, "")).equals(Constants.INVOICE_TYPE_TRANSPORT)) {
			this.attachmentDirectory = Constants.ATTACHMENT_DIRECTORY_TRANSPORT_BANK_STATEMENT;
		} else if ((AppUtil.ifNull(this.invoiceType, "")).equals(Constants.INVOICE_TYPE_FORWARDER)) {
			this.attachmentDirectory = Constants.ATTACHMENT_DIRECTORY_FORWARDER_BANK_STATEMENT;
		} else if ((AppUtil.ifNull(this.invoiceType, "")).equals(Constants.INVOICE_TYPE_OTHERS)) {
			this.attachmentDirectory = Constants.ATTACHMENT_DIRECTORY_OTHER_BANK_STATEMENT;
		} else if ((AppUtil.ifNull(this.invoiceType, "")).equals(Constants.INVOICE_TYPE_FREIGHT)) {
			this.attachmentDirectory = Constants.ATTACHMENT_DIRECTORY_FREIGHT_BANK_STATEMENT;
		} else if ((AppUtil.ifNull(this.invoiceType, "")).equals(Constants.INVOICE_TYPE_INSPECTION)) {
			this.attachmentDirectory = Constants.ATTACHMENT_DIRECTORY_INSPECTION_BANK_STATEMENT;
		}
		return attachmentDirectory;
	}

	public void setAttachmentDirectory(String attachmentDirectory) {
		this.attachmentDirectory = attachmentDirectory;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getPaymentVoucherNo() {
		return paymentVoucherNo;
	}

	public void setPaymentVoucherNo(String paymentVoucherNo) {
		this.paymentVoucherNo = paymentVoucherNo;
	}

}
