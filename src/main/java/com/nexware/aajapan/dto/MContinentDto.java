package com.nexware.aajapan.dto;

import java.util.List;

import com.nexware.aajapan.models.MCountryPort;

public class MContinentDto {
	private String id;
	private String code;// refer m_shppng_cmpny
	private String name;// refer m_shppng_cmpn
	private List<MCountryPort> items;

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

	public List<MCountryPort> getItems() {
		return this.items;
	}

	public void setItems(List<MCountryPort> items) {
		this.items = items;
	}

}
