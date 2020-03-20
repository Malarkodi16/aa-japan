package com.nexware.aajapan.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TExchangeRateDto {

	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date createdDate;
	private String createdBy;
	private List<Map<String, String>> groupItems = new ArrayList<>();

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public List<Map<String, String>> getGroupItems() {
		return groupItems;
	}

	public void setGroupItems(List<Map<String, String>> groupItems) {
		this.groupItems = groupItems;
	}

}
