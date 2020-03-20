package com.nexware.aajapan.models;

import javax.validation.constraints.NotNull;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.nexware.aajapan.listeners.EntityModelBase;

@Document(collection = "t_bl_trnsctn")
public class TBlTransaction extends EntityModelBase {

	@Id
	private String id;
	@Indexed(unique = true)
	private String code;
	@NotNull
	private String customerId;
	private ObjectId consigneeId;
	private String shippingInstructionId;

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

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public ObjectId getConsigneeId() {
		return consigneeId;
	}

	public void setConsigneeId(ObjectId consigneeId) {
		this.consigneeId = consigneeId;
	}

	public String getShippingInstructionId() {
		return shippingInstructionId;
	}

	public void setShippingInstructionId(String shippingInstructionId) {
		this.shippingInstructionId = shippingInstructionId;
	}

}
