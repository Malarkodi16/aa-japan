package com.nexware.aajapan.dto;

import org.bson.types.ObjectId;

public class MForeignBankDto {
	private ObjectId id;
	private String bankName;
	private String benificiaryCertify;
	private String licenseDoc;
	private Integer bankId;

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBenificiaryCertify() {
		return benificiaryCertify;
	}

	public void setBenificiaryCertify(String benificiaryCertify) {
		this.benificiaryCertify = benificiaryCertify;
	}

	public String getLicenseDoc() {
		return licenseDoc;
	}

	public void setLicenseDoc(String licenseDoc) {
		this.licenseDoc = licenseDoc;
	}

	public Integer getBankId() {
		return bankId;
	}

	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}

}
