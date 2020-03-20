package com.nexware.aajapan.dto;

import java.util.List;

public class SalesStockSearchDto {
	private int recordsTotal;
	private List<TSalesStockSearchDto> listOfDataObjects;

	public int getRecordsTotal() {
		return this.recordsTotal;
	}

	public void setRecordsTotal(int recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

	public List<TSalesStockSearchDto> getListOfDataObjects() {
		return this.listOfDataObjects;
	}

	public void setListOfDataObjects(List<TSalesStockSearchDto> listOfDataObjects) {
		this.listOfDataObjects = listOfDataObjects;
	}

}
