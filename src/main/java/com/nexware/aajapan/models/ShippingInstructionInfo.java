package com.nexware.aajapan.models;

public class ShippingInstructionInfo {

	private final String shippingInstructionId;
	private String customerId;
	private String salesPersonId;

	public ShippingInstructionInfo(String customerId, String salesPersonId, String shippingInstructionId) {
		super();
		this.customerId = customerId;
		this.salesPersonId = salesPersonId;
		this.shippingInstructionId = shippingInstructionId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getSalesPersonId() {
		return salesPersonId;
	}

	public void setSalesPersonId(String salesPersonId) {
		this.salesPersonId = salesPersonId;
	}

	public String getShippingInstructionId() {
		return shippingInstructionId;
	}

}
