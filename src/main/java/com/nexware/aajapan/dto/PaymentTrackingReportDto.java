package com.nexware.aajapan.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.nexware.aajapan.utils.AppUtil;

public class PaymentTrackingReportDto {

	private String paymentVoucherNo;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date invoiceDate;
	private String invoiceNo;
	private String invoiceName;
	private Double totalAmount;
	private Double paidAmount;
	private Double balance;
	private Double totalAmountUsd;
	private Double paidAmountUsd;
	private Double balanceAmountUsd;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date approvedDate;
	private String approvedBy;
	private Double paymentAmount;
	private Integer paymentApproveStatus;
	private String supplierName;
	private String auctionHouseName;
	private String remitter;
	private String remitterOthers;

	public String getPaymentVoucherNo() {
		return paymentVoucherNo;
	}

	public void setPaymentVoucherNo(String paymentVoucherNo) {
		this.paymentVoucherNo = paymentVoucherNo;
	}

	public String getInvoiceDate() {
		String invDateStr = "";
		if (!AppUtil.isObjectEmpty(invoiceDate)) {
			invDateStr = new SimpleDateFormat("dd-MM-yyyy").format(invoiceDate);
		}
		return invDateStr;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getInvoiceName() {
		if (!AppUtil.isObjectEmpty(supplierName)) {
			invoiceName = this.supplierName + "/" + this.auctionHouseName;
		} else if (!AppUtil.isObjectEmpty(remitter) && remitter.toLowerCase() == "others") {
			invoiceName = this.remitterOthers;
		} else {
			invoiceName = this.invoiceName;
		}

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

	public Double getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(Double paidAmount) {
		this.paidAmount = paidAmount;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public String getApprovedDate() {
		String dateStr = "";
		if (!AppUtil.isObjectEmpty(approvedDate)) {
			dateStr = new SimpleDateFormat("dd-MM-yyyy").format(approvedDate);
		}
		return dateStr;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Integer getPaymentApproveStatus() {
		return paymentApproveStatus;
	}

	public void setPaymentApproveStatus(Integer paymentApproveStatus) {
		this.paymentApproveStatus = paymentApproveStatus;
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

	public Double getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(Double paymentAmount) {
		this.paymentAmount = paymentAmount;
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

	public Double getBalanceAmountUsd() {
		return balanceAmountUsd;
	}

	public void setBalanceAmountUsd(Double balanceAmountUsd) {
		this.balanceAmountUsd = balanceAmountUsd;
	}

}
