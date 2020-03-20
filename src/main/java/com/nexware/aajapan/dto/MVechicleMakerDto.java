package com.nexware.aajapan.dto;

import java.util.List;

public class MVechicleMakerDto {

	private String id;
	private String code;
	private String name;
	private List<String> models;

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

	public List<String> getModels() {
		return this.models;
	}

	public void setModels(List<String> models) {
		this.models = models;
	}

}
