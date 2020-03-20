package com.nexware.aajapan.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "m_fuel_tnk_lit")
public class MFuelTankKiloLitre {

	@Id
	private String id;
	@Indexed
	private Integer tankKiloLitre;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getTankKiloLitre() {
		return tankKiloLitre;
	}

	public void setTankKiloLitre(Integer tankKiloLitre) {
		this.tankKiloLitre = tankKiloLitre;
	}

}
