package com.nexware.aajapan.models;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "m_cstm_flds")
public class MCustomListFields {
	@Id
	private String id;
	@Indexed(unique = true)
	private int fieldId;
	private int groupId;
	private String tableName;
	private String fieldName;
	private String projectAs;
	private String displayName;
	private ArrayList<org.bson.Document> aggregation;
	private String resultToProject;
	private List<Integer> access;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getProjectAs() {
		return projectAs;
	}

	public void setProjectAs(String projectAs) {
		this.projectAs = projectAs;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public ArrayList<org.bson.Document> getAggregation() {
		return aggregation;
	}

	public void setAggregation(ArrayList<org.bson.Document> aggregation) {
		this.aggregation = aggregation;
	}

	public String getResultToProject() {
		return resultToProject;
	}

	public void setResultToProject(String resultToProject) {
		this.resultToProject = resultToProject;
	}

	public int getFieldId() {
		return fieldId;
	}

	public void setFieldId(int fieldId) {
		this.fieldId = fieldId;
	}

	public List<Integer> getAccess() {
		return access;
	}

	public void setAccess(List<Integer> access) {
		this.access = access;
	}

}
