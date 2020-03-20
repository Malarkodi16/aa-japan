package com.nexware.aajapan.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "m_tre_sze")
public class MTyreSize {

	@Id
	private String id;
	@Indexed
	private Double tyreSize;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Double getTyreSize() {
		return tyreSize;
	}

	public void setTyreSize(Double tyreSize) {
		this.tyreSize = tyreSize;
	}

}
