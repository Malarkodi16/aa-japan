package com.nexware.aajapan.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.nexware.aajapan.listeners.EntityModelBase;

@Document(collection = "othr_drct_expense")
public class OtherDirectExpense extends EntityModelBase {
	
	@Id
	private String id;
	@Indexed(unique = true)
	private String code;
	private String type;
	private Long coaCode;
	private Long tkcCode;
	private String tkcDescription;
	
	public OtherDirectExpense(String code, Long coaCode, String type) {
		super();
		this.code = code;
		this.coaCode = coaCode;
		this.type = type;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Long getCoaCode() {
		return coaCode;
	}
	public void setCoaCode(Long coaCode) {
		this.coaCode = coaCode;
	}
	public Long getTkcCode() {
		return tkcCode;
	}
	public void setTkcCode(Long tkcCode) {
		this.tkcCode = tkcCode;
	}
	public String getTkcDescription() {
		return tkcDescription;
	}
	public void setTkcDescription(String tkcDescription) {
		this.tkcDescription = tkcDescription;
	}


}
