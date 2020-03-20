package com.nexware.aajapan.models;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.nexware.aajapan.listeners.EntityModelBase;

@Document(collection = "t_trnsprtr_fee")
public class TTransporterFee extends EntityModelBase {
	@Id
	private String id;
	@Indexed
	private String transporter;// m_trnsprtr
	@Indexed
	private String from;// m_lctn
	@Indexed
	private String to;// m_lctn
	private List<String> categories;// m_vchcl_mkr
	private Double amount;
	private Integer deleteFlag;
	private List<String> transportCategory;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTransporter() {
		return this.transporter;
	}

	public void setTransporter(String transporter) {
		this.transporter = transporter;
	}

	public String getFrom() {
		return this.from;
	}

	public void setFrom(String from) {
		this.from = from;
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
