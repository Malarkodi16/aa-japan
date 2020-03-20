package com.nexware.aajapan.dto;

import java.util.List;

public class GlReportDetails {
	private String name;
	private Integer order;
	private Double total;
	private List<CoaDetails> coa_details;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public List<CoaDetails> getCoa_details() {
		return coa_details;
	}
	public void setCoa_details(List<CoaDetails> coa_details) {
		this.coa_details = coa_details;
	}

}
