package com.nexware.aajapan.models;

import org.springframework.data.annotation.Id;

import com.nexware.aajapan.core.Constants;

public class ConsigneeNotifyparty {
	@Id
	private String id;
	private String cFirstName;
	private String cLastName;
	private String cEmail;
	private String cMobileNo;
	private String cAddress;
	private String npFirstName;
	private String npLastName;
	private String npEmail;
	private String npMobileNo;
	private String npAddress;
	private int deleteFlag = Constants.DELETE_FLAG_0;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getcFirstName() {
		return this.cFirstName;
	}

	public void setcFirstName(String cFirstName) {
		this.cFirstName = cFirstName;
	}

	public String getcLastName() {
		return this.cLastName;
	}

	public void setcLastName(String cLastName) {
		this.cLastName = cLastName;
	}

	public String getcEmail() {
		return this.cEmail;
	}

	public void setcEmail(String cEmail) {
		this.cEmail = cEmail;
	}

	public String getcMobileNo() {
		return this.cMobileNo;
	}

	public void setcMobileNo(String cMobileNo) {
		this.cMobileNo = cMobileNo;
	}

	public String getcAddress() {
		return this.cAddress;
	}

	public void setcAddress(String cAddress) {
		this.cAddress = cAddress;
	}

	public String getNpFirstName() {
		return this.npFirstName;
	}

	public void setNpFirstName(String npFirstName) {
		this.npFirstName = npFirstName;
	}

	public String getNpLastName() {
		return this.npLastName;
	}

	public void setNpLastName(String npLastName) {
		this.npLastName = npLastName;
	}

	public String getNpEmail() {
		return this.npEmail;
	}

	public void setNpEmail(String npEmail) {
		this.npEmail = npEmail;
	}

	public String getNpMobileNo() {
		return this.npMobileNo;
	}

	public void setNpMobileNo(String npMobileNo) {
		this.npMobileNo = npMobileNo;
	}

	public String getNpAddress() {
		return this.npAddress;
	}

	public void setNpAddress(String npAddress) {
		this.npAddress = npAddress;
	}

	public int getDeleteFlag() {
		return this.deleteFlag;
	}

	public void setDeleteFlag(int deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

}