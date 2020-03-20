package com.nexware.aajapan.models;

import java.util.List;

import org.springframework.data.annotation.Id;

public class SupplierLocation {
	@Id
	private String id;
	private String code;
	private String auctionHouse;
	private String address;
	private String phone;
	private String email;
	private String fax;
	private Integer deleteStatus;
	private List<String> posNos;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAuctionHouse() {
		return this.auctionHouse;
	}

	public void setAuctionHouse(String auctionHouse) {
		this.auctionHouse = auctionHouse;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFax() {
		return this.fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public Integer getDeleteStatus() {
		return this.deleteStatus;
	}

	public void setDeleteStatus(Integer deleteStatus) {
		this.deleteStatus = deleteStatus;
	}

	public List<String> getPosNos() {
		return this.posNos;
	}

	public void setPosNos(List<String> posNos) {
		this.posNos = posNos;
	}

}
