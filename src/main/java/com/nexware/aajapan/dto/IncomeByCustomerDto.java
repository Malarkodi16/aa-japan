package com.nexware.aajapan.dto;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.utils.AppUtil;

public class IncomeByCustomerDto {

	private String customerId;
	private String customerName;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date salesDate;
	private Double totalPurchasePrice;
	private Double totalSellingPrice;
	private Double totalMargin;
	private Integer totalStock;
	private List<StockDetailsItemsDto> stockDetails;

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

	public Date getSalesDate() {
		return this.salesDate;
	}

	public void setSalesDate(Date salesDate) {
		this.salesDate = salesDate;
	}

	public Double getTotalPurchasePrice() {
		return this.totalPurchasePrice;
	}

	public void setTotalPurchasePrice(Double totalPurchasePrice) {
		this.totalPurchasePrice = totalPurchasePrice;
	}

	public Double getTotalSellingPrice() {
		return this.totalSellingPrice;
	}

	public void setTotalSellingPrice(Double totalSellingPrice) {
		this.totalSellingPrice = totalSellingPrice;
	}

	public Double getTotalMargin() {
		this.totalMargin = AppUtil.ifNull(this.getTotalSellingPrice(), 0.0)
				- AppUtil.ifNull(this.getTotalPurchasePrice(), 0.0);
		return this.totalMargin;
	}

	public void setTotalMargin(Double totalMargin) {
		this.totalMargin = totalMargin;
	}

	public Integer getTotalStock() {
		return this.totalStock;
	}

	public void setTotalStock(Integer totalStock) {
		this.totalStock = totalStock;
	}

	public List<StockDetailsItemsDto> getStockDetails() {
		return this.stockDetails;
	}

	public void setStockDetails(List<StockDetailsItemsDto> stockDetails) {
		this.stockDetails = stockDetails;
	}

}
