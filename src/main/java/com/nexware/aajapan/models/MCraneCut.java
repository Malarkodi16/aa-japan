package com.nexware.aajapan.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "m_crane_cut")
public class MCraneCut {

	@Id
	private String id;
	@Indexed
	private Integer craneCut;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getCraneCut() {
		return craneCut;
	}

	public void setCraneCut(Integer craneCut) {
		this.craneCut = craneCut;
	}

}
