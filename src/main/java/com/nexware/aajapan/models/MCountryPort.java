package com.nexware.aajapan.models;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.nexware.aajapan.listeners.EntityModelBase;

//
@Document(collection = "m_cntry_prt")
public class MCountryPort extends EntityModelBase {

	@Id
	private String id;
	@Indexed
	private String code;
	@Indexed(unique = true)
	private String country;
	private Integer inspectionFlag;
	private List<YardDetails> yardDetails;
	private List<String> port;
	private String continent;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public List<String> getPort() {
		return port;
	}

	public void setPort(List<String> port) {
		this.port = port;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getInspectionFlag() {
		return inspectionFlag;
	}

	public void setInspectionFlag(Integer inspectionFlag) {
		this.inspectionFlag = inspectionFlag;
	}

	public List<YardDetails> getYardDetails() {
		return yardDetails;
	}

	public void setYardDetails(List<YardDetails> yardDetails) {
		this.yardDetails = yardDetails;
	}

	public String getContinent() {
		return continent;
	}

	public void setContinent(String continent) {
		this.continent = continent;
	}

}
