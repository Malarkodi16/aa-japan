package com.nexware.aajapan.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.utils.AppUtil;

public class DocumentStatusDto {

	private String id;
	private String shipmentRequestId;// auto generate
	private String stockNo;// refer t_stck
	private String customerId;// refer t_cstmr
	private String forwarderId;// refer m_frwdr
	private String orginCountry;// refer m_cntry_prt
	private String orginPort;// refer m_cntry_prt
	private String destCountry;// refer m_cntry_prt
	private String destPort;// refer m_cntry_prt
	private String vesselId;
	private String voyageNo;
	private String firstName;
	private String lastName;
	private String fName;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date etd;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date eta;
	private String sEtd;
	private String sEta;
	private Integer blDraftStatus;
	private Integer blOriginalStatus;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getShipmentRequestId() {
		return this.shipmentRequestId;
	}

	public void setShipmentRequestId(String shipmentRequestId) {
		this.shipmentRequestId = shipmentRequestId;
	}

	public String getStockNo() {
		return this.stockNo;
	}

	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}

	public String getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getForwarderId() {
		return this.forwarderId;
	}

	public void setForwarderId(String forwarderId) {
		this.forwarderId = forwarderId;
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

	public String getVesselId() {
		return this.vesselId;
	}

	public void setVesselId(String vesselId) {
		this.vesselId = vesselId;
	}

	public String getVoyageNo() {
		return this.voyageNo;
	}

	public void setVoyageNo(String voyageNo) {
		this.voyageNo = voyageNo;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getfName() {
		this.fName = this.firstName + " " + this.lastName;
		return this.fName;
	}

	public void setfName(String fName) {
		this.fName = fName;
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

	public Integer getBlDraftStatus() {
		return this.blDraftStatus;
	}

	public void setBlDraftStatus(Integer blDraftStatus) {
		this.blDraftStatus = blDraftStatus;
	}

	public Integer getBlOriginalStatus() {
		return this.blOriginalStatus;
	}

	public void setBlOriginalStatus(Integer blOriginalStatus) {
		this.blOriginalStatus = blOriginalStatus;
	}

	public String getsEtd() {
		this.sEtd = AppUtil.isObjectEmpty(this.etd) ? "" : new SimpleDateFormat("dd-MM-yyyy").format(this.etd);
		return this.sEtd;
	}

	public void setsEtd(String sEtd) {
		this.sEtd = sEtd;
	}

	public String getsEta() {
		this.sEta = AppUtil.isObjectEmpty(this.eta) ? "" : new SimpleDateFormat("dd-MM-yyyy").format(this.eta);
		return this.sEta;
	}

	public void setsEta(String sEta) {
		this.sEta = sEta;
	}

}
