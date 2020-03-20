package com.nexware.aajapan.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.nexware.aajapan.listeners.EntityModelBase;

@Document(collection = "m_frght")
public class MFreight extends EntityModelBase {
	@Id
	private String id;
	@Indexed
	private String freightId;// auto genarate
	private String fromPort;
	private String toPort;
	private String currency;
	private Double freightRate;
	private Double shippingCharge;
	private Double inspectionCharge;
	private Double radiationCharge;
	private Double otherCharge;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFreightId() {
		return this.freightId;
	}

	public void setFreightId(String freightId) {
		this.freightId = freightId;
	}

	public String getFromPort() {
		return this.fromPort;
	}

	public void setFromPort(String fromPort) {
		this.fromPort = fromPort;
	}

	public String getToPort() {
		return this.toPort;
	}

	public void setToPort(String toPort) {
		this.toPort = toPort;
	}

	public String getCurrency() {
		return this.currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Double getFreightRate() {
		return this.freightRate;
	}

	public void setFreightRate(Double freightRate) {
		this.freightRate = freightRate;
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

	public Double getOtherCharge() {
		return this.otherCharge;
	}

	public void setOtherCharge(Double otherCharge) {
		this.otherCharge = otherCharge;
	}

}
