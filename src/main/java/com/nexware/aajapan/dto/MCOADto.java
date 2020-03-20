package com.nexware.aajapan.dto;

public class MCOADto {
	private String id;
	private Long code;
	private String account;
	private String subAccount;
	private String reportingCategory;
	private Integer status;
	private Double balance;
	private Integer balanceSheet;
	private Integer plOrder;
	private String reportFlag;

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

	public void setAccount(String account) {
		this.account = account;
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

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Double getBalance() {
		return this.balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
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

}
