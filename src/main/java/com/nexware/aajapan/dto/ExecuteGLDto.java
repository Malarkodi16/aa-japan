package com.nexware.aajapan.dto;

public class ExecuteGLDto {

	private String id;
	private String stockNo;
	private String fieldName;
	private String remark;

	public ExecuteGLDto(String stockNo, String fieldName, String remark) {
		super();
		this.stockNo = stockNo;
		this.fieldName = fieldName;
		this.remark = remark;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStockNo() {
		return this.stockNo;
	}

	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}

	public String getFieldName() {
		return this.fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
