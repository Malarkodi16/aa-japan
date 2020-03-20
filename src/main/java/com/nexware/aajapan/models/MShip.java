package com.nexware.aajapan.models;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.nexware.aajapan.listeners.EntityModelBase;

//shipNo
@Document(collection = "m_ship")
public class MShip extends EntityModelBase{
	@Id
	private String id;
	@NotBlank
	@Indexed(unique = true)
	private String shipId;
	private String name;
	private Integer deleteFlag;

	public MShip(@NotBlank String shipId, String name) {
		super();
		this.shipId = shipId;
		this.name = name;
	}

	
	public Integer getDeleteFlag() {
		return deleteFlag;
	}


	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}


	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getShipId() {
		return this.shipId;
	}

	public void setShipId(String shipId) {
		this.shipId = shipId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
