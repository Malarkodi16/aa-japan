package com.nexware.aajapan.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.nexware.aajapan.listeners.EntityModelBase;

@Document(collection = "t_bll_of_exchng")
public class TBillOfExchange extends EntityModelBase{
	@Id
	private String id;
	@Indexed
	private String billOfExchange;
	private String lcNo;
	private String staff;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBillOfExchange() {
		return this.billOfExchange;
	}

	public void setBillOfExchange(String billOfExchange) {
		this.billOfExchange = billOfExchange;
	}

	public String getLcNo() {
		return this.lcNo;
	}

	public void setLcNo(String lcNo) {
		this.lcNo = lcNo;
	}

	public String getStaff() {
		return this.staff;
	}

	public void setStaff(String staff) {
		this.staff = staff;
	}

	public String getCustomer() {
		return this.customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	private String customer;

}
