package com.nexware.aajapan.dto;

import java.util.List;

public class TStockShippingInfoDto {

	private String stockNo;
//	private Double freightCharge;
//	private Double shippingCharge;
//	private Double inspectionCharge;
//	private Double radiationCharge;
//	private Double othersCharge;
	private List<TStockShippingGroupInfoDto> shippingGroupInfo;

	public String getStockNo() {
		return stockNo;
	}

	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}

	public List<TStockShippingGroupInfoDto> getShippingGroupInfo() {
		return shippingGroupInfo;
	}

	public void setShippingGroupInfo(List<TStockShippingGroupInfoDto> shippingGroupInfo) {
		this.shippingGroupInfo = shippingGroupInfo;
	}

}
