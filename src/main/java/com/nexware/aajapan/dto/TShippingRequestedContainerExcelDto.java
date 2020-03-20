package com.nexware.aajapan.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TShippingRequestedContainerExcelDto {

	private String destCountry;
	private String destPort;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date allocationDate;
	private List<TShippingRequestedContainerExcelItemDto> items;

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

	public Date getAllocationDate() {
		return allocationDate;
	}

	public void setAllocationDate(Date allocationDate) {
		this.allocationDate = allocationDate;
	}

	public List<TShippingRequestedContainerExcelItemDto> getItems() {
		return items;
	}

	public void setItems(List<TShippingRequestedContainerExcelItemDto> items) {
		this.items = items;
	}

}
