package com.nexware.aajapan.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.nexware.aajapan.listeners.EntityModelBase;
@Document(collection = "m_ship_charge")
public class MShippingCharge extends EntityModelBase{
	@Id
	private String id;
	private String originCountry;
	private String destCountry;
	private Long m3From;
	private Long m3To;
	private Double amount;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOriginCountry() {
		return originCountry;
	}
	public void setOriginCountry(String originCountry) {
		this.originCountry = originCountry;
	}
	public String getDestCountry() {
		return destCountry;
	}
	public void setDestCountry(String destCountry) {
		this.destCountry = destCountry;
	}
	public Long getM3From() {
		return m3From;
	}
	public void setM3From(Long m3From) {
		this.m3From = m3From;
	}
	public Long getM3To() {
		return m3To;
	}
	public void setM3To(Long m3To) {
		this.m3To = m3To;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
}
