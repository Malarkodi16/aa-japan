package com.nexware.aajapan.models;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.nexware.aajapan.listeners.EntityModelBase;

@Document(collection = "m_shppng_cmpny")
public class MShippingCompany extends EntityModelBase{
	@Id
	private String id;
	@NotBlank
	@Indexed(unique = true)
	private String shippingCompanyNo;
	
	public MShippingCompany( @NotBlank String shippingCompanyNo, String name, String shipCompAddr,
			String shipCompMail, String mobileNo) {
		super();
		
		this.shippingCompanyNo = shippingCompanyNo;
		this.name = name;
		this.shipCompAddr = shipCompAddr;
		this.shipCompMail = shipCompMail;
		this.mobileNo = mobileNo;
	}

	private String name;
	private String shipCompAddr;
	private String shipCompMail;
	private String mobileNo;
	private Integer deleteFlag;

	public Integer getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShippingCompanyNo() {
		return this.shippingCompanyNo;
	}

	public void setShippingCompanyNo(String shippingCompanyNo) {
		this.shippingCompanyNo = shippingCompanyNo;
	}

	public String getShipCompAddr() {
		return shipCompAddr;
	}

	public void setShipCompAddr(String shipCompAddr) {
		this.shipCompAddr = shipCompAddr;
	}

	public String getShipCompMail() {
		return shipCompMail;
	}

	public void setShipCompMail(String shipCompMail) {
		this.shipCompMail = shipCompMail;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

}
