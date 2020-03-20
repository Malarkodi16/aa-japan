package com.nexware.aajapan.models;

import com.nexware.aajapan.utils.AppUtil;

public class Address {

	private String addressLine1;
	private String addressLine2;
	private String city;
	private String state;
	private String country;
	private String zip;
	private String formattedAddress;

	public String getAddressLine1() {
		return this.addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return this.addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getZip() {
		return this.zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getFormattedAddress() {
		this.formattedAddress = this.addressLine1
				+ (!AppUtil.isObjectEmpty(this.addressLine2) ? ", " + this.addressLine2 : "")
				+ (!AppUtil.isObjectEmpty(this.city) ? ", " + this.city : "")
				+ (!AppUtil.isObjectEmpty(this.state) ? ", " + this.state : "")
				+ (!AppUtil.isObjectEmpty(this.country) ? ", " + this.country : "")
				+ (!AppUtil.isObjectEmpty(this.zip) ? ", " + this.zip : "");
		return this.formattedAddress;
	}

	public void setFormattedAddress(String formattedAddress) {
		this.formattedAddress = formattedAddress;
	}

}
