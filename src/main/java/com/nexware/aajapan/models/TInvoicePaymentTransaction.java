package com.nexware.aajapan.models;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.listeners.EntityModelBase;

@Document(collection = "t_invc_pymnt_trnsctn")
public class TInvoicePaymentTransaction extends EntityModelBase {

	@Id
	private String id;
	private String paymentVoucherNo;
	@NotBlank
	@Indexed(unique = true)
	private String code;
	private String invoiceType;
	private String invoiceNo;
	@Indexed
	private String bankId;
	private Double amount;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date approvedDate; // @payment date
	private String remarks;
	private String bankStatementAttachmentFilename;
	private String bankStatementAttachmentDiskFilename;
	private Integer status;

	public TInvoicePaymentTransaction(String invoiceType, String invoiceNo, String bankId, Double amount,
			Date approvedDate, String remarks, Integer status) {
		super();
		this.invoiceType = invoiceType;
		this.invoiceNo = invoiceNo;
		this.bankId = bankId;
		this.amount = amount;
		this.approvedDate = approvedDate;
		this.remarks = remarks;
		this.status = status;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getInvoiceType() {
		return this.invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public String getInvoiceNo() {
		return this.invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getBankId() {
		return this.bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public Double getAmount() {
		return this.amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Date getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getPaymentVoucherNo() {
		return paymentVoucherNo;
	}

	public void setPaymentVoucherNo(String paymentVoucherNo) {
		this.paymentVoucherNo = paymentVoucherNo;
	}

}
