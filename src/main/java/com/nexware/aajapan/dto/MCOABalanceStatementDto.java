package com.nexware.aajapan.dto;

import java.util.List;

public class MCOABalanceStatementDto {

	private String account;
	private Double ptdAmount;
	private Double ytdAmount;
	private List<SubBalanceStatementDto> items;
	private Double plPtdAmount;
	private Double plYtdAmount;

	public String getAccount() {
		return this.account;
	}

	public void setAccount(String account) {
		this.account = account;
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

	public List<SubBalanceStatementDto> getItems() {
		return this.items;
	}

	public void setItems(List<SubBalanceStatementDto> items) {
		this.items = items;
	}

	public Double getPlPtdAmount() {
		return this.plPtdAmount;
	}

	public void setPlPtdAmount(Double plPtdAmount) {
		this.plPtdAmount = plPtdAmount;
	}

	public Double getPlYtdAmount() {
		return this.plYtdAmount;
	}

	public void setPlYtdAmount(Double plYtdAmount) {
		this.plYtdAmount = plYtdAmount;
	}

}
