package com.nexware.aajapan.models;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.nexware.aajapan.listeners.EntityModelBase;

@Document(collection = "m_vchcl_ctgry")
public class MVechicleCategory extends EntityModelBase {
	@Id
	private String id;
	@Indexed
	private String code;
	private String name;
	private List<SubCategory> subCategories;
	private List<String> extraEquipments;

	public String getId() {
		return id;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<SubCategory> getSubCategories() {
		return subCategories;
	}

	public void setSubCategories(List<SubCategory> subCategories) {
		this.subCategories = subCategories;
	}

	public List<String> getExtraEquipments() {
		return extraEquipments;
	}

	public void setExtraEquipments(List<String> extraEquipments) {
		this.extraEquipments = extraEquipments;
	}

}
