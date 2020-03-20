package com.nexware.aajapan.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.nexware.aajapan.listeners.EntityModelBase;

@Document(collection = "m_hs_code")
public class MHSCode extends EntityModelBase {

	@Id
	private String id;
	@Indexed(unique = true)
	private String code;
	private Double cc;
	private String category;
	private String subCategory;
	private String hsCode;
	private Integer deleteFlag;

	public String getId() {
		return id;
	}

	public MHSCode() {

	}

	public MHSCode(String id, String code, Double cc, String category, String hsCode) {
		super();
		this.id = id;
		this.code = code;
		this.cc = cc;
		this.category = category;
		this.hsCode = hsCode;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Double getCc() {
		return cc;
	}

	public void setCc(Double cc) {
		this.cc = cc;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getHsCode() {
		return hsCode;
	}

	public void setHsCode(String hsCode) {
		this.hsCode = hsCode;
	}

	public Integer getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public String getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

	
}
