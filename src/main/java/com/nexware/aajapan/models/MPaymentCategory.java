package com.nexware.aajapan.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.nexware.aajapan.listeners.EntityModelBase;

@Document(collection = "m_pymnt_ctgry")
public class MPaymentCategory extends EntityModelBase {
	@Id
	private String id;
	@Indexed(unique = true)
	private String categoryCode;
	private String category;
	private Long coaCode;
	private Long tkcCode;
	private String tkcDescription;

	public MPaymentCategory() {

	}

	public MPaymentCategory(String categoryCode, Long coaCode, String category, Long tkcCode, String tkcDescription) {
		super();
		this.categoryCode = categoryCode;
		this.coaCode = coaCode;
		this.category = category;
		this.tkcCode = tkcCode;
		this.tkcDescription = tkcDescription;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCategoryCode() {
		return this.categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Long getCoaCode() {
		return this.coaCode;
	}

	public void setCoaCode(Long coaCode) {
		this.coaCode = coaCode;
	}

	public Long getTkcCode() {
		return tkcCode;
	}

	public void setTkcCode(Long tkcCode) {
		this.tkcCode = tkcCode;
	}

	public String getTkcDescription() {
		return tkcDescription;
	}

	public void setTkcDescription(String tkcDescription) {
		this.tkcDescription = tkcDescription;
	}

}
