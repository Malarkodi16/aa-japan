package com.nexware.aajapan.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "m_exel")
public class MExel {

	@Id
	private String id;
	@Indexed
	private Integer exel;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getExel() {
		return exel;
	}

	public void setExel(Integer exel) {
		this.exel = exel;
	}

}
