package com.nexware.aajapan.models;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.nexware.aajapan.listeners.EntityModelBase;

@Document(collection = "t_cstmr_stck_trnsctn")
public class TCustomerTransaction extends EntityModelBase {
	@Id
	private String id;
	@NotBlank
	@Indexed
	private String stockNo;
	@NotBlank
	@Indexed
	private String customerId;
	private Integer currency;
	private Integer transactionType;
	private Double amount;
	private Double closingBalance;
	private String transactionId;

	public TCustomerTransaction(@NotBlank String customerId, @NotBlank String stockNo, Integer currency,
			Integer transactionType, Double amount) {
		super();
		this.customerId = customerId;
		this.currency = currency;
		this.transactionType = transactionType;
		this.amount = amount;
		this.stockNo = stockNo;
	}

	public String getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public Integer getCurrency() {
		return this.currency;
	}

	public void setCurrency(Integer currency) {
		this.currency = currency;
	}

	public Integer getTransactionType() {
		return this.transactionType;
	}

	public void setTransactionType(Integer transactionType) {
		this.transactionType = transactionType;
	}

	public Double getAmount() {
		return this.amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Double getClosingBalance() {
		return this.closingBalance;
	}

	public void setClosingBalance(Double closingBalance) {
		this.closingBalance = closingBalance;
	}

	public String getStockNo() {
		return this.stockNo;
	}

	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

}
