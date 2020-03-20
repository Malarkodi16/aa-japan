package com.nexware.aajapan.dto;

import java.util.List;

import com.nexware.aajapan.models.SubModel;

public class MModelDto {

	private String code;
	private String modelName;
	private String category;
	private String subcategory;
	private String transportCategory;
	private Double m3;
	private Double length;
	private Double width;
	private Double height;
	private List<SubModel> subModel;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubcategory() {
		return subcategory;
	}

	public void setSubcategory(String subcategory) {
		this.subcategory = subcategory;
	}

	public Double getM3() {
		return m3;
	}

	public void setM3(Double m3) {
		this.m3 = m3;
	}

	public Double getLength() {
		return length;
	}

	public void setLength(Double length) {
		this.length = length;
	}

	public Double getWidth() {
		return width;
	}

	public void setWidth(Double width) {
		this.width = width;
	}

	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	public List<SubModel> getSubModel() {
		return subModel;
	}

	public void setSubModel(List<SubModel> subModel) {
		this.subModel = subModel;
	}

	public String getTransportCategory() {
		return transportCategory;
	}

	public void setTransportCategory(String transportCategory) {
		this.transportCategory = transportCategory;
	}

}
