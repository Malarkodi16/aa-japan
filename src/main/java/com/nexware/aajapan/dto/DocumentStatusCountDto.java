package com.nexware.aajapan.dto;

public class DocumentStatusCountDto {

	private Long draftBL = 0L;
	private Long originalBL = 0L;
	private Long reAuction = 0L;
	private Long cancelStock = 0L;

	public Long getDraftBL() {
		return this.draftBL;
	}

	public void setDraftBL(Long draftBL) {
		this.draftBL = draftBL;
	}

	public Long getOriginalBL() {
		return this.originalBL;
	}

	public void setOriginalBL(Long originalBL) {
		this.originalBL = originalBL;
	}

	public Long getReAuction() {
		return this.reAuction;
	}

	public void setReAuction(Long reAuction) {
		this.reAuction = reAuction;
	}

	public Long getCancelStock() {
		return this.cancelStock;
	}

	public void setCancelStock(Long cancelStock) {
		this.cancelStock = cancelStock;
	}

}
