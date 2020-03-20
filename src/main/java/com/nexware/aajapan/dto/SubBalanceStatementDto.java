package com.nexware.aajapan.dto;

public class SubBalanceStatementDto {

	private Long code;
	private String subAccount;
	private Double ptdAmount;
	private Double ytdAmount;
	private Integer balanceSheet;
	private Integer plOrder;
	private String reportFlag;

	public Long getCode() {
		return this.code;
	}

	public void setCode(Long code) {
		this.code = code;
	}

	public String getSubAccount() {
		return this.subAccount;
	}

	public void setSubAccount(String subAccount) {
		this.subAccount = subAccount;
	}

	public Double getPtdAmount() {
		return this.ptdAmount;
	}

	public void setPtdAmount(Double ptdAmount) {
		this.ptdAmount = ptdAmount;
	}

	public Double getYtdAmount() {
		return this.ytdAmount;
	}

	public void setYtdAmount(Double ytdAmount) {
		this.ytdAmount = ytdAmount;
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
