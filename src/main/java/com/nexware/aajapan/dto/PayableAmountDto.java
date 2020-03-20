package com.nexware.aajapan.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nexware.aajapan.utils.AppUtil;

public class PayableAmountDto {

	private String remitter;
	private Double grandTotal;
	private Double invoiceAmountReceived;
	private Double receivableAmount;
	private String sequenceId;
	private String stockNo;
	private List<Map<String, String>> paymentItems = new ArrayList<>();

	public String getRemitter() {
		return this.remitter;
	}

	public void setRemitter(String remitter) {
		this.remitter = remitter;
	}

	public String getSequenceId() {
		return this.sequenceId;
	}

	public void setSequenceId(String sequenceId) {
		this.sequenceId = sequenceId;
	}

	public String getStockNo() {
		return this.stockNo;
	}

	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}

	public List<Map<String, String>> getPaymentItems() {
		return this.paymentItems;
	}

	public void setPaymentItems(List<Map<String, String>> paymentItems) {
		this.paymentItems = paymentItems;
	}

	public Double getGrandTotal() {
		return this.grandTotal;
	}

	public void setGrandTotal(Double grandTotal) {
		this.grandTotal = grandTotal;
	}

	public Double getInvoiceAmountReceived() {
		return this.invoiceAmountReceived;
	}

	public void setInvoiceAmountReceived(Double invoiceAmountReceived) {
		this.invoiceAmountReceived = invoiceAmountReceived;
	}

	public Double getReceivableAmount() {
		this.receivableAmount = AppUtil.ifNull(this.grandTotal, 0.0) - AppUtil.ifNull(this.invoiceAmountReceived, 0.0);
		return this.receivableAmount;
	}

	public void setReceivableAmount(Double receivableAmount) {
		this.receivableAmount = receivableAmount;
	}

}
