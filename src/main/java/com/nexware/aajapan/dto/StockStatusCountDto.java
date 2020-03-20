package com.nexware.aajapan.dto;

public class StockStatusCountDto {

	private Long purchased = 0L;
	private Long purchasedConfirm = 0L;
	private String inTransit;
	private String inspection;
	private Long shippingRoro = 0L;
	private Long shippingContainer = 0L;
	private Long shippingStatus = 0L;

	public Long getPurchased() {
		return this.purchased;
	}

	public void setPurchased(Long purchased) {
		this.purchased = purchased;
	}

	public Long getPurchasedConfirm() {
		return this.purchasedConfirm;
	}

	public void setPurchasedConfirm(Long purchasedConfirm) {
		this.purchasedConfirm = purchasedConfirm;
	}

	public String getInTransit() {
		return this.inTransit;
	}

	public void setInTransit(String inTransit) {
		this.inTransit = inTransit;
	}

	public String getInspection() {
		return inspection;
	}

	public void setInspection(String inspection) {
		this.inspection = inspection;
	}

	public Long getShippingRoro() {
		return shippingRoro;
	}

	public void setShippingRoro(Long shippingRoro) {
		this.shippingRoro = shippingRoro;
	}

	public Long getShippingContainer() {
		return shippingContainer;
	}

	public void setShippingContainer(Long shippingContainer) {
		this.shippingContainer = shippingContainer;
	}

	public Long getShippingStatus() {
		return this.shippingStatus;
	}

	public void setShippingStatus(Long shippingStatus) {
		this.shippingStatus = shippingStatus;
	}

}
