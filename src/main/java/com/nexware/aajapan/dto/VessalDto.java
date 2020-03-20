package com.nexware.aajapan.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.text.SimpleDateFormat;
import com.nexware.aajapan.utils.AppUtil;

public class VessalDto {
	private String scheduleId;
	private String shippingCompanyNo;
	private String shippingCompanyName;
	private String shipId;
	private String shipName;
	private String orginCountry;
	private String orginPort;
	private String destinationCountry;
	private String destinationPort;
	private String voyageNo;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date etd;
	private String sEtd;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date eta;
	private String sEta;

	public String getScheduleId() {
		return this.scheduleId;
	}

	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
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

	public String getVoyageNo() {
		return this.voyageNo;
	}

	public void setVoyageNo(String voyageNo) {
		this.voyageNo = voyageNo;
	}

	public Date getEtd() {
		return this.etd;
	}

	public void setEtd(Date etd) {
		this.etd = etd;
	}

	public Date getEta() {
		return this.eta;
	}

	public void setEta(Date eta) {
		this.eta = eta;
	}

	public String getsEtd() {
		this.sEtd = !AppUtil.isObjectEmpty(this.etd) ? new SimpleDateFormat("dd-MM-yyyy").format(this.etd) : "";
		return this.sEtd;
	}

	public void setsEtd(String sEtd) {
		this.sEtd = sEtd;
	}

	public String getsEta() {
		this.sEta = !AppUtil.isObjectEmpty(this.eta) ? new SimpleDateFormat("dd-MM-yyyy").format(this.eta) : "";
		return this.sEta;
	}

	public void setsEta(String sEta) {

		this.sEta = sEta;
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

	public String getDestinationCountry() {
		return destinationCountry;
	}

	public void setDestinationCountry(String destinationCountry) {
		this.destinationCountry = destinationCountry;
	}

	public String getDestinationPort() {
		return destinationPort;
	}

	public void setDestinationPort(String destinationPort) {
		this.destinationPort = destinationPort;
	}

}
