package com.nexware.aajapan.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TShippingRequestContainerDto {
	private String shipId;
	private String shippingCompanyNo;
	private String shippingCompanyName;
	private String vessel;
	private String voyageNo;
	private String scheduleId;
	private String orginCountry;
	private String orginPort;
	private String destCountry;
	private String destPort;
	private String forwarder;
	private String allocationId;
	private String status;
	private Integer shippingStatus;
	private String forwarderName;
	private List<TShippingRequestedDto> items;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date createdDate;
	private Integer stockCount;

	public String getShipId() {
		return this.shipId;
	}

	public void setShipId(String shipId) {
		this.shipId = shipId;
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

	public String getVessel() {
		return this.vessel;
	}

	public void setVessel(String vessel) {
		this.vessel = vessel;
	}

	public String getVoyageNo() {
		return this.voyageNo;
	}

	public void setVoyageNo(String voyageNo) {
		this.voyageNo = voyageNo;
	}

	public String getScheduleId() {
		return this.scheduleId;
	}

	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
	}

	public List<TShippingRequestedDto> getItems() {
		return this.items;
	}

	public void setItems(List<TShippingRequestedDto> items) {
		this.items = items;
	}

	public String getOrginCountry() {
		return this.orginCountry;
	}

	public void setOrginCountry(String orginCountry) {
		this.orginCountry = orginCountry;
	}

	public String getOrginPort() {
		return this.orginPort;
	}

	public void setOrginPort(String orginPort) {
		this.orginPort = orginPort;
	}

	public String getDestCountry() {
		return this.destCountry;
	}

	public void setDestCountry(String destCountry) {
		this.destCountry = destCountry;
	}

	public String getDestPort() {
		return this.destPort;
	}

	public void setDestPort(String destPort) {
		this.destPort = destPort;
	}

	public String getForwarder() {
		return this.forwarder;
	}

	public void setForwarder(String forwarder) {
		this.forwarder = forwarder;
	}

	public String getAllocationId() {
		return this.allocationId;
	}

	public void setAllocationId(String allocationId) {
		this.allocationId = allocationId;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getShippingStatus() {
		return this.shippingStatus;
	}

	public void setShippingStatus(Integer shippingStatus) {
		this.shippingStatus = shippingStatus;
	}

	public String getForwarderName() {
		return forwarderName;
	}

	public void setForwarderName(String forwarderName) {
		this.forwarderName = forwarderName;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getStockCount() {
		return stockCount;
	}

	public void setStockCount(Integer stockCount) {
		this.stockCount = stockCount;
	}

}
