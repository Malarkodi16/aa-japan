package com.nexware.aajapan.dto;

public class PaymentCountDto {
	private Integer auction = 0;
	private Integer transport = 0;
	private Integer shipping = 0;
	private Integer others = 0;
	private Integer freight = 0;

	public Integer getAuction() {
		return this.auction;
	}

	public Integer getTransport() {
		return this.transport;
	}

	public Integer getShipping() {
		return this.shipping;
	}

	public Integer getOthers() {
		return this.others;
	}

	public Integer getFreight() {
		return this.freight;
	}

	public void setAuction(Integer auction) {
		this.auction = auction;
	}

	public void setTransport(Integer transport) {
		this.transport = transport;
	}

	public void setShipping(Integer shipping) {
		this.shipping = shipping;
	}

	public void setOthers(Integer others) {
		this.others = others;
	}

	public void setFreight(Integer freight) {
		this.freight = freight;
	}

}
