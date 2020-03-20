package com.nexware.aajapan.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.nexware.aajapan.listeners.EntityModelBase;

@Document(collection = "trnsprt_paymt_ctry")
public class TransportPaymentCategory extends EntityModelBase  {
	
	@Id
	private String id;
	@Indexed(unique = true)
	private String categoryCode;
	private String category;
	private Long coaCode;
	private Integer stockView;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCategoryCode() {
		return categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public Long getCoaCode() {
		return coaCode;
	}
	public void setCoaCode(Long coaCode) {
		this.coaCode = coaCode;
	}
	public Integer getStockView() {
		return stockView;
	}
	public void setStockView(Integer stockView) {
		this.stockView = stockView;
	}

}
