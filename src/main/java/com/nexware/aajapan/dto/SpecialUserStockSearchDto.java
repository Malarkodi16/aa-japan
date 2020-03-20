package com.nexware.aajapan.dto;

import java.util.List;

public class SpecialUserStockSearchDto {
	private int recordsTotal;
	private List<SpecialUserDto> listOfDataObjects;

	public int getRecordsTotal() {
		return this.recordsTotal;
	}

	public void setRecordsTotal(int recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

	public List<SpecialUserDto> getListOfDataObjects() {
		return this.listOfDataObjects;
	}

	public void setListOfDataObjects(List<SpecialUserDto> listOfDataObjects) {
		this.listOfDataObjects = listOfDataObjects;
	}

}
