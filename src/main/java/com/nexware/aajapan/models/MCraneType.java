package com.nexware.aajapan.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "m_crane_type")
public class MCraneType {
	
	@Id
	private String id;
	@Indexed
	private String craneType;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCraneType() {
		return craneType;
	}
	public void setCraneType(String craneType) {
		this.craneType = craneType;
	}
	
	

}
