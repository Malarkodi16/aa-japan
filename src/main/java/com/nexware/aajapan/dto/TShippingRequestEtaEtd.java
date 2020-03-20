package com.nexware.aajapan.dto;

import java.util.Date;

public class TShippingRequestEtaEtd {

	private String shipmentRequestId;
	private Date eta;
	private Date etd;

	public String getShipmentRequestId() {
		return this.shipmentRequestId;
	}

	public void setShipmentRequestId(String shipmentRequestId) {
		this.shipmentRequestId = shipmentRequestId;
	}

	public Date getEta() {
		return this.eta;
	}

	public void setEta(Date eta) {
		this.eta = eta;
	}

	public Date getEtd() {
		return this.etd;
	}

	public void setEtd(Date etd) {
		this.etd = etd;
	}

}
