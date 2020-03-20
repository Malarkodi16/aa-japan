package com.nexware.aajapan.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.nexware.aajapan.listeners.EntityModelBase;

@Document(collection = "m_frwrdr")
public class MForwarder extends EntityModelBase {
	@Id
	private String id;
	@Indexed(unique = true)
	private String code;
	private String name;
	private Double perM3Usd;
	private String type;
	private Integer deleteFlag;

	public MForwarder(String code, String name, String type) {
		super();
		this.code = code;
		this.name = name;
		this.type = type;
	}

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

	public Double getPerM3Usd() {
		return this.perM3Usd;
	}

	public void setPerM3Usd(Double perM3Usd) {
		this.perM3Usd = perM3Usd;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

}
