package com.nexware.aajapan.models;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.listeners.EntityModelBase;

@Document(collection = "t_invc")
public class TInvoice extends EntityModelBase {
	@Id
	private String id;
	@Indexed
	private String invoiceNo;
	private String invoiceType;// refer m_invc_typ
	private String otherInvoiceType;
	private String category;// refer m_pymnt_ctgry
	private String categoryOthers;
	private String remitter;// refer m_spplr
	private String remitterOthers;
	private String refNo;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date invoiceDate; // @purchase date
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date dueDate;
	private String stockNo;// refer t_stck
	private Double amountInYen;
	private String sourceCurrency;
	private Double exchangeRate;
	private Double amount;
	private String description;
	private String remarks;
	private String status;
	private Integer paymentApprove = 0;
	private ApprovePaymentDetails approvePaymentDetails;
	private String bank;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date approvedDate;
	private String cancelledRemarks;
	private String invoiceAttachmentFilename;
	private String invoiceAttachmentDiskFilename;
	private String bankStatementAttachmentFilename;
	private String bankStatementAttachmentDiskFilename;
	private Integer invoiceUpload;
	private Integer attachementViewed;
	private Double invoiceAmountReceived;
	private boolean taxInclusive;
	private Double taxPercentage;
	private Double taxAmount;
	private Double taxIncludedAmount;
	private String approvedBy;
	private Integer deleteFlag;

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

	public String getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public String getOtherInvoiceType() {
		return otherInvoiceType;
	}

	public void setOtherInvoiceType(String otherInvoiceType) {
		this.otherInvoiceType = otherInvoiceType;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCategoryOthers() {
		return categoryOthers;
	}

	public void setCategoryOthers(String categoryOthers) {
		this.categoryOthers = categoryOthers;
	}

	public String getRemitter() {
		return remitter;
	}

	public void setRemitter(String remitter) {
		this.remitter = remitter;
	}

	public String getRemitterOthers() {
		return remitterOthers;
	}

	public void setRemitterOthers(String remitterOthers) {
		this.remitterOthers = remitterOthers;
	}

	public String getRefNo() {
		return refNo;
	}

	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public String getStockNo() {
		return stockNo;
	}

	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ApprovePaymentDetails getApprovePaymentDetails() {
		return approvePaymentDetails;
	}

	public void setApprovePaymentDetails(ApprovePaymentDetails approvePaymentDetails) {
		this.approvePaymentDetails = approvePaymentDetails;
	}

	public Integer getPaymentApprove() {
		return paymentApprove;
	}

	public void setPaymentApprove(Integer paymentApprove) {
		this.paymentApprove = paymentApprove;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public Date getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	public String getCancelledRemarks() {
		return cancelledRemarks;
	}

	public void setCancelledRemarks(String cancelledRemarks) {
		this.cancelledRemarks = cancelledRemarks;
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

	public Double getInvoiceAmountReceived() {
		return invoiceAmountReceived;
	}

	public void setInvoiceAmountReceived(Double invoiceAmountReceived) {
		this.invoiceAmountReceived = invoiceAmountReceived;
	}

	public boolean isTaxInclusive() {
		return taxInclusive;
	}

	public void setTaxInclusive(boolean taxInclusive) {
		this.taxInclusive = taxInclusive;
	}

	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Double getAmountInYen() {
		return amountInYen;
	}

	public void setAmountInYen(Double amountInYen) {
		this.amountInYen = amountInYen;
	}

	public String getSourceCurrency() {
		return sourceCurrency;
	}

	public void setSourceCurrency(String sourceCurrency) {
		this.sourceCurrency = sourceCurrency;
	}

	public Double getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getTaxPercentage() {
		return taxPercentage;
	}

	public void setTaxPercentage(Double taxPercentage) {
		this.taxPercentage = taxPercentage;
	}

	public Double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}

//	public Double calcTaxAmount() {
//		return (AppUtil.ifNull(amountInYen, 0.0) * (AppUtil.ifNull(taxPercentage, 0.0) / 100));
//	}

	public Double getTaxIncludedAmount() {
		return taxIncludedAmount;
	}

	public void setTaxIncludedAmount(Double taxIncludedAmount) {
		this.taxIncludedAmount = taxIncludedAmount;
	}

	public Integer getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

}
