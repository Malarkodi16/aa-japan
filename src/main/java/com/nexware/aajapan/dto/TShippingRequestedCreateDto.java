package com.nexware.aajapan.dto;

import java.util.List;

public class TShippingRequestedCreateDto {

	private String scheduleId;
	private String orginCountry;
	private String orginPort;
	private String destCountry;
	private String destPort;
	private String forwarderId;
	private Integer shippingType;
	private String yard;
	private String yardId;
	private List<TStockShippingInstructionDto> stockShippingInstructionArray;

	public String getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
	}

	public String getOrginCountry() {
		return orginCountry;
	}

	public void setOrginCountry(String orginCountry) {
		this.orginCountry = orginCountry;
	}

	public String getOrginPort() {
		return orginPort;
	}

	public void setOrginPort(String orginPort) {
		this.orginPort = orginPort;
	}

	public String getDestCountry() {
		return destCountry;
	}

	public void setDestCountry(String destCountry) {
		this.destCountry = destCountry;
	}

	public String getDestPort() {
		return destPort;
	}

	public void setDestPort(String destPort) {
		this.destPort = destPort;
	}

	public String getForwarderId() {
		return forwarderId;
	}

	public void setForwarderId(String forwarderId) {
		this.forwarderId = forwarderId;
	}

	public Integer getShippingType() {
		return shippingType;
	}

	public void setShippingType(Integer shippingType) {
		this.shippingType = shippingType;
	}

	public String getYard() {
		return yard;
	}

	public void setYard(String yard) {
		this.yard = yard;
	}

	public List<TStockShippingInstructionDto> getStockShippingInstructionArray() {
		return stockShippingInstructionArray;
	}

	public void setStockShippingInstructionArray(List<TStockShippingInstructionDto> stockShippingInstructionArray) {
		this.stockShippingInstructionArray = stockShippingInstructionArray;
	}

	public String getYardId() {
		return yardId;
	}

	public void setYardId(String yardId) {
		this.yardId = yardId;
	}

}
