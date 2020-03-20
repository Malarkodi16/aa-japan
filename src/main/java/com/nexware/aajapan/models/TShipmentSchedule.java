package com.nexware.aajapan.models;

import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.nexware.aajapan.listeners.EntityModelBase;

@Document(collection = "t_shpmnt_schdl")
public class TShipmentSchedule extends EntityModelBase{
	@Id
	private String id;
	@NotBlank
	@Indexed(unique = true)
	private String scheduleId;
	private String shippingCompanyNo;
	private String shipId;
	private String deckHeight;
	private String voyageNo;
	private List<String> continents;
	private List<Schedule> schedule;
	private Integer deleteFlag;

	public String getShipId() {
		return this.shipId;
	}

	public void setShipId(String shipId) {
		this.shipId = shipId;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getScheduleId() {
		return this.scheduleId;
	}

	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
	}

	public String getShippingCompanyNo() {
		return this.shippingCompanyNo;
	}

	public void setShippingCompanyNo(String shippingCompanyNo) {
		this.shippingCompanyNo = shippingCompanyNo;
	}

	public String getDeckHeight() {
		return this.deckHeight;
	}

	public void setDeckHeight(String deckHeight) {
		this.deckHeight = deckHeight;
	}

	public String getVoyageNo() {
		return this.voyageNo;
	}

	public void setVoyageNo(String voyageNo) {
		this.voyageNo = voyageNo;
	}

	public List<String> getContinents() {
		return continents;
	}

	public void setContinents(List<String> continents) {
		this.continents = continents;
	}

	public List<Schedule> getSchedule() {
		return this.schedule;
	}

	public void setSchedule(List<Schedule> schedule) {
		this.schedule = schedule;
	}

	public Integer getDeleteFlag() {
		return this.deleteFlag;
	}

	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

}
