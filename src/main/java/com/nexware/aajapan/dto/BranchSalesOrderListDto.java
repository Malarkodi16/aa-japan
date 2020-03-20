package com.nexware.aajapan.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nexware.aajapan.utils.AppUtil;

public class BranchSalesOrderListDto {

	private String customerId;
	private String fName;
	private String invoiceNo;
	private String lName;
	private String fullName;
	private String companyName;
	private String email;
	private String mobileNo;
	private String country;
	private String port;
	private List<Map<String, String>> salesDetails = new ArrayList<>();

	public String getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getfName() {
		return this.fName;
	}

	public void setfName(String fName) {
		this.fName = fName;
	}

	public String getlName() {
		return this.lName;
	}

	public void setlName(String lName) {
		this.lName = lName;
	}

	public String getFullName() {
		this.fullName = AppUtil.ifNull(this.fName, "") + " " + AppUtil.ifNull(this.lName, "");
		return this.fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getCompanyName() {
		return this.companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobileNo() {
		return this.mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPort() {
		return this.port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public List<Map<String, String>> getSalesDetails() {
		return this.salesDetails;
	}

	public void setSalesDetails(List<Map<String, String>> salesDetails) {
		this.salesDetails = salesDetails;
	}

	public String getInvoiceNo() {
		return this.invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

}
