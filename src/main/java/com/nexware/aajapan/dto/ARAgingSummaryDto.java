package com.nexware.aajapan.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nexware.aajapan.models.MCurrency;
import com.nexware.aajapan.utils.AppUtil;

public class ARAgingSummaryDto {

	private String code;
	private String customerId;
	private String customerName;
	private Double amountOutstanding;
	private Double current;
	private Double amountReceived;
	private Double aged30;
	private Double aged60;
	private Double aged90;
	private Double agedAbove90;
	private List<Map<String, Object>> salesDetails = new ArrayList<>();
	private MCurrency currencyDetails;

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return this.customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Double getAmountOutstanding() {
		this.amountOutstanding = (AppUtil.ifNull(this.current, 0.0) + AppUtil.ifNull(this.aged30, 0.0)
				+ AppUtil.ifNull(this.aged60, 0.0) + AppUtil.ifNull(this.aged90, 0.0)
				+ AppUtil.ifNull(this.agedAbove90, 0.0)) - AppUtil.ifNull(this.amountReceived, 0.0);
		return this.amountOutstanding;
	}

	public void setAmountOutstanding(Double amountOutstanding) {
		this.amountOutstanding = amountOutstanding;
	}

	public Double getCurrent() {

		return this.current;
	}

	public void setCurrent(Double current) {
		this.current = current;
	}

	public Double getAmountReceived() {
		return this.amountReceived;
	}

	public void setAmountReceived(Double amountReceived) {
		this.amountReceived = amountReceived;
	}

	public Double getAged30() {
		return this.aged30;
	}

	public void setAged30(Double aged30) {
		this.aged30 = aged30;
	}

	public Double getAged60() {
		return this.aged60;
	}

	public void setAged60(Double aged60) {
		this.aged60 = aged60;
	}

	public Double getAged90() {
		return this.aged90;
	}

	public void setAged90(Double aged90) {
		this.aged90 = aged90;
	}

	public Double getAgedAbove90() {
		return this.agedAbove90;
	}

	public void setAgedAbove90(Double agedAbove90) {
		this.agedAbove90 = agedAbove90;
	}

	public List<Map<String, Object>> getSalesDetails() {
		return this.salesDetails;
	}

	public void setSalesDetails(List<Map<String, Object>> salesDetails) {
		this.salesDetails = salesDetails;
	}

	public MCurrency getCurrencyDetails() {
		return this.currencyDetails;
	}

	public void setCurrencyDetails(MCurrency currencyDetails) {
		this.currencyDetails = currencyDetails;
	}

}
