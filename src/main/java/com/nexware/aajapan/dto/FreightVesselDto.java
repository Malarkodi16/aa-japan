package com.nexware.aajapan.dto;

import java.util.List;

public class FreightVesselDto {
	private String shipId;
	private String shipName;
	private String shippingCompanyNo;
	private String shippingCompanyName;
	private String voyageNo;
	private List<FreightVesselItemsDto> items;

	public String getShipId() {
		return this.shipId;
	}

	public void setShipId(String shipId) {
		this.shipId = shipId;
	}

	public String getShipName() {
		return this.shipName;
	}

	public void setShipName(String shipName) {
		this.shipName = shipName;
	}

	public String getShippingCompanyNo() {
		return this.shippingCompanyNo;
	}

	public void setShippingCompanyNo(String shippingCompanyNo) {
		this.shippingCompanyNo = shippingCompanyNo;
	}

	public String getShippingCompanyName() {
		return this.shippingCompanyName;
	}

	public void setShippingCompanyName(String shippingCompanyName) {
		this.shippingCompanyName = shippingCompanyName;
	}

	public String getVoyageNo() {
		return this.voyageNo;
	}

	public void setVoyageNo(String voyageNo) {
		this.voyageNo = voyageNo;
	}

	public List<FreightVesselItemsDto> getItems() {
		return this.items;
	}

	public void setItems(List<FreightVesselItemsDto> items) {
		this.items = items;
	}

}
