package com.nexware.aajapan.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.nexware.aajapan.listeners.EntityModelBase;

@Document(collection = "m_shp_marks")
public class MShippingMarks extends EntityModelBase{
	@Id
	private String id;
	@Indexed(unique = true)
	private String marksId;
	private String name;
	private String shippingMarks;
	private Integer deleteFlag;
	public MShippingMarks(String id, String marksId, String name, String shippingMarks, Integer deleteFlag) {
		super();
		this.id = id;
		this.marksId = marksId;
		this.name = name;
		this.shippingMarks = shippingMarks;
		this.deleteFlag = deleteFlag;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMarksId() {
		return marksId;
	}
	public void setMarksId(String marksId) {
		this.marksId = marksId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getShippingMarks() {
		return shippingMarks;
	}
	public void setShippingMarks(String shippingMarks) {
		this.shippingMarks = shippingMarks;
	}
	public Integer getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	
	
}
