package com.nexware.aajapan.dto;

import java.util.List;

import com.nexware.aajapan.models.TAccountsTransaction;

public class GLDto {

	private String id;
	private String stockNo;
	private List<TAccountsTransaction> items;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStockNo() {
		return this.stockNo;
	}

	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}

	public List<TAccountsTransaction> getItems() {
		return this.items;
	}

	public void setItems(List<TAccountsTransaction> items) {
		this.items = items;
	}

}
