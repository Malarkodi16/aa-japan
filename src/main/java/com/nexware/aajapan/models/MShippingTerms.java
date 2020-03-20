package com.nexware.aajapan.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.nexware.aajapan.listeners.EntityModelBase;

@Document(collection = "m_shp_terms")
public class MShippingTerms extends EntityModelBase{
	@Id
	private String id;
	@Indexed(unique = true)
	private String termsId;
	private String name;
	private String shippingTerms;
	private Integer deleteFlag;
	
	public MShippingTerms(String id, String termsId, String name, String shippingTerms) {
		super();
		this.id = id;
		this.termsId = termsId;
		this.name = name;
		this.shippingTerms = shippingTerms;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTermsId() {
		return termsId;
	}

	public void setTermsId(String termsId) {
		this.termsId = termsId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShippingTerms() {
		return shippingTerms;
	}

	public void setShippingTerms(String shippingTerms) {
		this.shippingTerms = shippingTerms;
	}

	public Integer getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}	
	

	
}
