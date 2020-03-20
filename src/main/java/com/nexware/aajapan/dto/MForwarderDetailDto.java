package com.nexware.aajapan.dto;

public class MForwarderDetailDto {
	private String id;
	private String forwarderId;
	private String forwarderName;
	private String orginCountry;// refer m_cntry_prt
	private String orginPort;// refer m_cntry_prt
	private String destCountry;// refer m_cntry_prt
	private String destPort;// refer m_cntry_prt
	private Double freightUSD;
	private Double freightCharge;
	private Double shippingCharge;
	private Double inspectionCharge;
	private Double radiationCharge;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getForwarderId() {
		return this.forwarderId;
	}

	public void setForwarderId(String forwarderId) {
		this.forwarderId = forwarderId;
	}

	public String getForwarderName() {
		return this.forwarderName;
	}

	public void setForwarderName(String forwarderName) {
		this.forwarderName = forwarderName;
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

	public Double getFreightUSD() {
		return this.freightUSD;
	}

	public void setFreightUSD(Double freightUSD) {
		this.freightUSD = freightUSD;
	}

	public Double getFreightCharge() {
		return this.freightCharge;
	}

	public void setFreightCharge(Double freightCharge) {
		this.freightCharge = freightCharge;
	}

	public Double getShippingCharge() {
		return this.shippingCharge;
	}

	public void setShippingCharge(Double shippingCharge) {
		this.shippingCharge = shippingCharge;
	}

	public Double getInspectionCharge() {
		return this.inspectionCharge;
	}

	public void setInspectionCharge(Double inspectionCharge) {
		this.inspectionCharge = inspectionCharge;
	}

	public Double getRadiationCharge() {
		return this.radiationCharge;
	}

	public void setRadiationCharge(Double radiationCharge) {
		this.radiationCharge = radiationCharge;
	}

}
