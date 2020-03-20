package com.nexware.aajapan.dto;

public class BalanceAndPlDto {

	private String account;
	private Double totalPtdAmount;
	private Double totalYtdAmount;
	private Long code;
	private String subAccount;
	private Double ptdAmount;
	private Double ytdAmount;
	private Double plYtdAmount;

	public String getAccount() {
		return this.account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public Double getTotalPtdAmount() {
		return this.totalPtdAmount;
	}

	public void setTotalPtdAmount(Double totalPtdAmount) {
		this.totalPtdAmount = totalPtdAmount;
	}

	public Double getTotalYtdAmount() {
		return this.totalYtdAmount;
	}

	public void setTotalYtdAmount(Double totalYtdAmount) {
		this.totalYtdAmount = totalYtdAmount;
	}

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

	public Double getPlYtdAmount() {
		return this.plYtdAmount;
	}

	public void setPlYtdAmount(Double plYtdAmount) {
		this.plYtdAmount = plYtdAmount;
	}

}
