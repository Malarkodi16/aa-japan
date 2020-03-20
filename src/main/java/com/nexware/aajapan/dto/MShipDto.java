package com.nexware.aajapan.dto;

import java.util.List;

public class MShipDto {

	private String id;
	private String shippingCompanyNo;// refer m_shppng_cmpny
	private String shippingCompanyName;// refer m_shppng_cmpn
	private List<MShipDetailsDto> items;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
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

	public List<MShipDetailsDto> getItems() {
		return this.items;
	}

	public void setItems(List<MShipDetailsDto> items) {
		this.items = items;
	}

}
