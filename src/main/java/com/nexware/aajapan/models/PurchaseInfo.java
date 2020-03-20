package com.nexware.aajapan.models;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Embeddable;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;


@Embeddable
public class PurchaseInfo {
	private String type;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date date;
	private String sDate;
	@Indexed
	private String supplier;
	private AuctionInfo auctionInfo;

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getDate() {
		return this.date;

	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getsDate() {
		this.sDate = new SimpleDateFormat("dd-MM-yyyy").format(this.date);
		return this.sDate;
	}

	public void setsDate(String sDate) {
		this.sDate = sDate;
	}

	public String getSupplier() {
		return this.supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public AuctionInfo getAuctionInfo() {
		return this.auctionInfo;
	}

	public void setAuctionInfo(AuctionInfo auctionInfo) {
		this.auctionInfo = auctionInfo;
	}

}
