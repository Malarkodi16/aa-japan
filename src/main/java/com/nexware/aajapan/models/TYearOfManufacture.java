package com.nexware.aajapan.models;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.listeners.EntityModelBase;

@Document(collection = "t_yr_mnfctr")
public class TYearOfManufacture extends EntityModelBase {

	@Id
	private String id;
	@Indexed
	private String code;
	private String maker;
	private String model;
	private String modelNo;
	private String frame;
	private Long serialNoFrom;
	private Long serialNoTo;
	private String formatedSerialNoFrom;
	private String formatedSerialNoTo;
	@DateTimeFormat(pattern = "yyyy")
	@JsonFormat(pattern = "yyyy")
	private Date manufactureYear;
	private Integer deleteFlag;

	
	public Integer getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public String getFormatedSerialNoFrom() {
		return formatedSerialNoFrom;
	}

	public void setFormatedSerialNoFrom(String formaltedSerialNoFrom) {
		this.formatedSerialNoFrom = formaltedSerialNoFrom;
	}

	public String getFormatedSerialNoTo() {
		return formatedSerialNoTo;
	}

	public void setFormatedSerialNoTo(String formatedSerialNoTo) {
		this.formatedSerialNoTo = formatedSerialNoTo;
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

	public String getMaker() {
		return this.maker;
	}

	public void setMaker(String maker) {
		this.maker = maker;
	}

	public String getModel() {
		return this.model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getModelNo() {
		return this.modelNo;
	}

	public void setModelNo(String modelNo) {
		this.modelNo = modelNo;
	}

	public String getFrame() {
		return this.frame;
	}

	public void setFrame(String frame) {
		this.frame = frame;
	}

	public Long getSerialNoFrom() {
		return this.serialNoFrom;
	}

	public void setSerialNoFrom(Long serialNoFrom) {
		this.serialNoFrom = serialNoFrom;
	}

	public Long getSerialNoTo() {
		return this.serialNoTo;
	}

	public void setSerialNoTo(Long serialNoTo) {
		this.serialNoTo = serialNoTo;
	}

	public Date getManufactureYear() {
		return this.manufactureYear;
	}

	public void setManufactureYear(Date manufactureYear) {
		this.manufactureYear = manufactureYear;
	}

}
