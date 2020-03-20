package com.nexware.aajapan.models;

import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.nexware.aajapan.listeners.EntityModelBase;

@Document(collection = "m_spplr")
public class MSupplier extends EntityModelBase {
	@Id
	private String id;
	@NotBlank
	@Indexed(unique = true)
	private String supplierCode;
	private String type;
	private String company;
	private Integer deleteStatus;
	private Integer maxDueDays;
	private Double maxCreditAmount;
	private Double balance;
	private List<SupplierLocation> supplierLocations;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSupplierCode() {
		return this.supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCompany() {
		return this.company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public Integer getDeleteStatus() {
		return this.deleteStatus;
	}

	public void setDeleteStatus(Integer deleteStatus) {
		this.deleteStatus = deleteStatus;
	}

	public List<SupplierLocation> getSupplierLocations() {
		return this.supplierLocations;
	}

	public void setSupplierLocations(List<SupplierLocation> supplierLocations) {
		this.supplierLocations = supplierLocations;
	}

	public Integer getMaxDueDays() {
		return this.maxDueDays;
	}

	public void setMaxDueDays(Integer maxDueDays) {
		this.maxDueDays = maxDueDays;
	}

	public Double getMaxCreditAmount() {
		return this.maxCreditAmount;
	}

	public void setMaxCreditAmount(Double maxCreditAmount) {
		this.maxCreditAmount = maxCreditAmount;
	}

	public Double getBalance() {
		return this.balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

}
