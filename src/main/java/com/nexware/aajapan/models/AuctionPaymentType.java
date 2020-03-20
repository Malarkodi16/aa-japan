package com.nexware.aajapan.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.nexware.aajapan.listeners.EntityModelBase;

@Document(collection = "auction_paymt_type")
public class AuctionPaymentType extends EntityModelBase {
	
	@Id
	private String id;
	@Indexed(unique = true)
	private String code;
	private String type;
	private Long coaCode;
	private Integer stockView;
	private Integer claimed;
	
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public Integer getClaimed() {
		return claimed;
	}
	public void setClaimed(Integer claimed) {
		this.claimed = claimed;
	}

}
