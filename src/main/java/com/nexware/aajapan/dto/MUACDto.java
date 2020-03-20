package com.nexware.aajapan.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nexware.aajapan.payload.JSTreeState;

public class MUACDto {
	private Integer id;
	private String text;
	private Integer department;
	private Integer parentId;
	@JsonIgnore
	private MUACDto parent;
	private List<MUACDto> children;
	private JSTreeState state;

	public MUACDto() {
		super();
		children = new ArrayList<>();
	}

	public MUACDto(Integer id, String text, Integer parentId) {
		super();
		this.id = id;
		this.text = text;
		this.parentId = parentId;
		this.children = new ArrayList<>();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Integer getDepartment() {
		return department;
	}

	public void setDepartment(Integer department) {
		this.department = department;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public MUACDto getParent() {
		return parent;
	}

	public void setParent(MUACDto parent) {
		this.parent = parent;
	}

	public List<MUACDto> getChildren() {
		return children;
	}

	public void setChildren(List<MUACDto> children) {
		this.children = children;
	}

	public void addChild(MUACDto child) {
		if (!this.children.contains(child) && child != null)
			this.children.add(child);
	}

	public JSTreeState getState() {
		return state;
	}

	public void setState(JSTreeState state) {
		this.state = state;
	}

}
