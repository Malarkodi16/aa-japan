package com.nexware.aajapan.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

public class YardDetails {
	@Id
	private String id;
	@Indexed
	private String yardName;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getYardName() {
		return this.yardName;
	}

	public void setYardName(String yardName) {
		this.yardName = yardName;
	}

}
