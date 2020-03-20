package com.nexware.aajapan.dto;

import java.util.List;

public class MCOAProfitLossDto {

	private String account;
	private Double ptdAmount;
	private Double ytdAmount;

	private List<SubProfitLossDto> items;

	public String getAccount() {
		return this.account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public Double getPtdAmount() {
		this.ptdAmount = this.ptdAmount * -1;
		return this.ptdAmount;
	}

	public void setPtdAmount(Double ptdAmount) {
		this.ptdAmount = ptdAmount;
	}

	public Double getYtdAmount() {
		this.ytdAmount = this.ytdAmount * -1;
		return this.ytdAmount;
	}

	public void setYtdAmount(Double ytdAmount) {
		this.ytdAmount = ytdAmount;
	}

	public List<SubProfitLossDto> getItems() {
		return this.items;
	}

	public void setItems(List<SubProfitLossDto> items) {
		this.items = items;
	}

}
