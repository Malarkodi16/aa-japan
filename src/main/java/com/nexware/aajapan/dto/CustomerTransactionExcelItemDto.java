package com.nexware.aajapan.dto;

import java.util.List;

public class CustomerTransactionExcelItemDto {

	private Double broughtForward;
	private List<CustomerTransactionItemsDto> stockItems;
	private Double paidAmount;

	public Double getBroughtForward() {
		return broughtForward;
	}

	public void setBroughtForward(Double broughtForward) {
		this.broughtForward = broughtForward;
	}

	public List<CustomerTransactionItemsDto> getStockItems() {
		return stockItems;
	}

	public void setStockItems(List<CustomerTransactionItemsDto> stockItems) {
		this.stockItems = stockItems;
	}

	public Double getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(Double paidAmount) {
		this.paidAmount = paidAmount;
	}

}
