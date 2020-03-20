package com.nexware.aajapan.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.nexware.aajapan.listeners.EntityModelBase;

@Document(collection = "t_adv_dybk_trnsctn")
public class TAdvanceDayBookAllocation extends EntityModelBase {

	@Id
	private String id;
	@Indexed
	private String code;
	private String daybookId;
	private ObjectId daybookTransId;
	private Integer allocationType;
	private Integer changedAllocationType;

	public TAdvanceDayBookAllocation(String code, String daybookId, ObjectId daybookTransId, Integer allocationType,
			Integer changedAllocationType) {
		super();
		this.code = code;
		this.daybookId = daybookId;
		this.daybookTransId = daybookTransId;
		this.allocationType = allocationType;
		this.changedAllocationType = changedAllocationType;
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

	public String getDaybookId() {
		return daybookId;
	}

	public void setDaybookId(String daybookId) {
		this.daybookId = daybookId;
	}

	public ObjectId getDaybookTransId() {
		return daybookTransId;
	}

	public void setDaybookTransId(ObjectId daybookTransId) {
		this.daybookTransId = daybookTransId;
	}

	public Integer getAllocationType() {
		return allocationType;
	}

	public void setAllocationType(Integer allocationType) {
		this.allocationType = allocationType;
	}

	public Integer getChangedAllocationType() {
		return changedAllocationType;
	}

	public void setChangedAllocationType(Integer changedAllocationType) {
		this.changedAllocationType = changedAllocationType;
	}

}
