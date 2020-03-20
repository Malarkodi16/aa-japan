package com.nexware.aajapan.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.nexware.aajapan.listeners.EntityModelBase;

@Document(collection = "m_coa_type")
public class MCoaType extends EntityModelBase{
	@Id
	private String id;
	@Indexed
	private String coaTypeSeq;
	private String coaType;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCoaTypeSeq() {
		return this.coaTypeSeq;
	}

	public void setCoaTypeSeq(String coaTypeSeq) {
		this.coaTypeSeq = coaTypeSeq;
	}

	public String getCoaType() {
		return this.coaType;
	}

	public void setCoaType(String coaType) {
		this.coaType = coaType;
	}
}
