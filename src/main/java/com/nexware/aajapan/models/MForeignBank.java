package com.nexware.aajapan.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.nexware.aajapan.listeners.EntityModelBase;

@Document(collection = "m_frgn_bnks")
public class MForeignBank extends EntityModelBase{
	@Id
	private String id;
	@Indexed
	private String bankId;
	private String country;
	private String bank;
	private String beneficiaryCertify;
	private String licenseDoc;
	private Integer deleteFlag;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBankId() {
		return this.bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getBank() {
		return this.bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getBeneficiaryCertify() {
		return this.beneficiaryCertify;
	}

	public void setBeneficiaryCertify(String beneficiaryCertify) {
		this.beneficiaryCertify = beneficiaryCertify;
	}

	public String getLicenseDoc() {
		return this.licenseDoc;
	}

	public void setLicenseDoc(String licenseDoc) {
		this.licenseDoc = licenseDoc;
	}

	public Integer getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

}
