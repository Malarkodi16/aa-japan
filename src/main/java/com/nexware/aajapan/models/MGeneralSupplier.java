package com.nexware.aajapan.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.nexware.aajapan.listeners.EntityModelBase;

@Document(collection = "m_gen_supplier")
public class MGeneralSupplier extends EntityModelBase {

	@Id
	private String id;
	@Indexed(unique = true)
	private String code;
	private String name;
	private Integer deleteFlag;	

	public MGeneralSupplier(String code, String name, Integer deleteFlag) {
		super();
		this.code = code;
		this.name = name;
		this.deleteFlag = deleteFlag;
	}

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

	public Integer getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

}
