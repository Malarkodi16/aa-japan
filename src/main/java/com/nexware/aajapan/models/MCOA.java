package com.nexware.aajapan.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.nexware.aajapan.listeners.EntityModelBase;

@Document(collection = "m_coa")
public class MCOA extends EntityModelBase {
	@Id
	private String id;
	@Indexed(unique = true)
	private Long code;
	private String account;
	private String subAccount;
	private String reportingCategory;
	private Integer status;
	private Double balance;
	private Integer balanceSheet;
	private Integer plOrder;
	private String reportFlag;
	private int generalLedger;
	private Long tkcCode;
	private String tkcDescription;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getCode() {
		return this.code;
	}

	public void setCode(Long code) {
		this.code = code;
	}

	public String getAccount() {
		return this.account;
	}

	public void setAccount(String object) {
		this.account = object;
	}

	public String getSubAccount() {
		return this.subAccount;
	}

	public void setSubAccount(String subAccount) {
		this.subAccount = subAccount;
	}

	public String getReportingCategory() {
		return this.reportingCategory;
	}

	public void setReportingCategory(String reportingCategory) {
		this.reportingCategory = reportingCategory;
	}

	public Double getBalance() {
		return this.balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getBalanceSheet() {
		return this.balanceSheet;
	}

	public void setBalanceSheet(Integer balanceSheet) {
		this.balanceSheet = balanceSheet;
	}

	public Integer getPlOrder() {
		return this.plOrder;
	}

	public void setPlOrder(Integer plOrder) {
		this.plOrder = plOrder;
	}

	public String getReportFlag() {
		return this.reportFlag;
	}

	public void setReportFlag(String reportFlag) {
		this.reportFlag = reportFlag;
	}

	public int getGeneralLedger() {
		return this.generalLedger;
	}

	public void setGeneralLedger(int generalLedger) {
		this.generalLedger = generalLedger;
	}

	public Long getTkcCode() {
		return tkcCode;
	}

	public void setTkcCode(Long tkcCode) {
		this.tkcCode = tkcCode;
	}

	public String getTkcDescription() {
		return tkcDescription;
	}

	public void setTkcDescription(String tkcDescription) {
		this.tkcDescription = tkcDescription;
	}

}
