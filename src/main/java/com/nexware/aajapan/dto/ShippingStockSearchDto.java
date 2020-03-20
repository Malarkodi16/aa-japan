package com.nexware.aajapan.dto;

import java.util.List;

public class ShippingStockSearchDto {
	private int recordsTotal;
	private List<TStockShippingListDto> listOfDataObjects;

	public int getRecordsTotal() {
		return this.recordsTotal;
	}

	public void setRecordsTotal(int recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

	public List<TStockShippingListDto> getListOfDataObjects() {
		return this.listOfDataObjects;
	}

	public void setListOfDataObjects(List<TStockShippingListDto> listOfDataObjects) {
		this.listOfDataObjects = listOfDataObjects;
	}

}
