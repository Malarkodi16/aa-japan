package com.nexware.aajapan.dto;

import java.util.List;

public class TShippingRoroExcelDto {

	private String originPort;
	private String originPortName;
	private List<TShippingRoroExcelItemsDto> items;

	public String getOriginPort() {
		return originPort;
	}

	public void setOriginPort(String originPort) {
		this.originPort = originPort;
	}

	public String getOriginPortName() {
		return originPortName;
	}

	public void setOriginPortName(String originPortName) {
		this.originPortName = originPortName;
	}

	public List<TShippingRoroExcelItemsDto> getItems() {
		return items;
	}

	public void setItems(List<TShippingRoroExcelItemsDto> items) {
		this.items = items;
	}

}
