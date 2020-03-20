package com.nexware.aajapan.models;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.nexware.aajapan.listeners.EntityModelBase;

@Document(collection = "m_spcl_exch_rate")
public class MSpecialExchangeRate extends EntityModelBase {
	@Id
	private String id;
	@NotBlank
	@Indexed(unique = true)
	private double usDlSalesExchange;
	private double usDlSpclExchange;
	private double ausDlSalesExchange;
	private double ausDlSpclExchange;
	private double poundSalesExchange;
	private double poundSpclExchange;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public double getUsDlSalesExchange() {
		return usDlSalesExchange;
	}
	public void setUsDlSalesExchange(double usDlSalesExchange) {
		this.usDlSalesExchange = usDlSalesExchange;
	}
	public double getUsDlSpclExchange() {
		return usDlSpclExchange;
	}
	public void setUsDlSpclExchange(double usDlSpclExchange) {
		this.usDlSpclExchange = usDlSpclExchange;
	}
	public double getAusDlSalesExchange() {
		return ausDlSalesExchange;
	}
	public void setAusDlSalesExchange(double ausDlSalesExchange) {
		this.ausDlSalesExchange = ausDlSalesExchange;
	}
	public double getAusDlSpclExchange() {
		return ausDlSpclExchange;
	}
	public void setAusDlSpclExchange(double ausDlSpclExchange) {
		this.ausDlSpclExchange = ausDlSpclExchange;
	}
	public double getPoundSalesExchange() {
		return poundSalesExchange;
	}
	public void setPoundSalesExchange(double poundSalesExchange) {
		this.poundSalesExchange = poundSalesExchange;
	}
	public double getPoundSpclExchange() {
		return poundSpclExchange;
	}
	public void setPoundSpclExchange(double poundSpclExchange) {
		this.poundSpclExchange = poundSpclExchange;
	}
	
	
	

}
