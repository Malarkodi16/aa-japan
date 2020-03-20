package com.nexware.aajapan.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.nexware.aajapan.listeners.EntityModelBase;

@Document(collection = "m_remit_type")
public class MRemitType extends EntityModelBase{
	@Id
	private String id;
	@Indexed
	private Integer remitSeq;
	private String remitType;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getRemitSeq() {
		return this.remitSeq;
	}

	public void setRemitSeq(Integer remitSeq) {
		this.remitSeq = remitSeq;
	}

	public String getRemitType() {
		return this.remitType;
	}

	public void setRemitType(String remitType) {
		this.remitType = remitType;
	}

}
