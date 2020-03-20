package com.nexware.aajapan.models;

import org.bson.types.ObjectId;

import com.nexware.aajapan.utils.AppUtil;

public class AuctionInfo {

	private ObjectId auctionHouse;
	private Long lotNo;
	private String posNo;

	public ObjectId getAuctionHouse() {
		return this.auctionHouse;
	}

	public void setAuctionHouse(ObjectId auctionHouse) {
		this.auctionHouse = auctionHouse;
	}

	public Long getLotNo() {
		return this.lotNo;
	}

	public void setLotNo(Long lotNo) {
		this.lotNo = lotNo;
	}

	public String getPosNo() {
		return this.posNo;
	}

	public void setPosNo(String posNo) {
		this.posNo = posNo;
	}

	public String getsAuctionHouse() {
		return AppUtil.isObjectEmpty(this.auctionHouse) ? "" : this.auctionHouse.toString();
	}
 
}
