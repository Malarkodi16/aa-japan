package com.nexware.aajapan.models;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.nexware.aajapan.listeners.EntityModelBase;

@Document(collection = "m_inspctn_cmpny")
public class MInspectionCompany extends EntityModelBase {
	@Id
	private String id;
	@Indexed(unique = true)
	private String code;
	private String name;
	private List<InspectionCompanyLocation> locations;
	private Integer deleteFlag;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<InspectionCompanyLocation> getLocations() {
		return this.locations;
	}

	public void setLocations(List<InspectionCompanyLocation> locations) {
		this.locations = locations;
	}

	public Integer getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

}
