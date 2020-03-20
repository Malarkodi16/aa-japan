package com.nexware.aajapan.dto;

import java.util.List;

public class TTransporterFeeDto {

	private String id;
	private String transporterCode;
	private String transporter;
	private String fromCode;
	private String from;
	private String toCode;
	private String to;
	private List<String> categories;
	private List<String> transportCategory;

	private Double amount;
	private Integer deleteFlag;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTransporterCode() {
		return this.transporterCode;
	}

	public void setTransporterCode(String transporterCode) {
		this.transporterCode = transporterCode;
	}

	public String getTransporter() {
		return this.transporter;
	}

	public void setTransporter(String transporter) {
		this.transporter = transporter;
	}

	public String getFromCode() {
		return this.fromCode;
	}

	public void setFromCode(String fromCode) {
		this.fromCode = fromCode;
	}

	public String getFrom() {
		return this.from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getToCode() {
		return this.toCode;
	}

	public void setToCode(String toCode) {
		this.toCode = toCode;
	}

	public String getTo() {
		return this.to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public Double getAmount() {
		return this.amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Integer getDeleteFlag() {
		return this.deleteFlag;
	}

	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public List<String> getCategories() {
		return this.categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	public List<String> getTransportCategory() {
		return transportCategory;
	}

	public void setTransportCategory(List<String> transportCategory) {
		this.transportCategory = transportCategory;
	}

}
