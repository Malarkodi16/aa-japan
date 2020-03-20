package com.nexware.aajapan.dto;

import java.util.List;

public class StockInfoDto {

	private String stockNo;
	private StockBaseInfoDto baseInfo;
	private StockInfoDocumentDto documentInfo;
	private TStockPurchaseInfoDto purchaseDetail;
	private TStockTransportInfoDto transportDetail;
	private TStockInspectionInfoDto inspectionDetail;
	private TStockShippingInfoDto shippingDetail;
	private Integer noOfAttachments;
	private List<String> imageUrls;

	
	public Integer getNoOfAttachments() {
		return noOfAttachments;
	}

	public void setNoOfAttachments(Integer noOfAttachments) {
		this.noOfAttachments = noOfAttachments;
	}

	public List<String> getImageUrls() {
		return imageUrls;
	}

	public void setImageUrls(List<String> imageUrls) {
		this.imageUrls = imageUrls;
	}

	public StockBaseInfoDto getBaseInfo() {
		return baseInfo;
	}

	public void setBaseInfo(StockBaseInfoDto baseInfo) {
		this.baseInfo = baseInfo;
	}

	public StockInfoDocumentDto getDocumentInfo() {
		return documentInfo;
	}

	public void setDocumentInfo(StockInfoDocumentDto documentInfo) {
		this.documentInfo = documentInfo;
	}

	public String getStockNo() {
		return stockNo;
	}

	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}

	public TStockPurchaseInfoDto getPurchaseDetail() {
		return purchaseDetail;
	}

	public void setPurchaseDetail(TStockPurchaseInfoDto purchaseDetail) {
		this.purchaseDetail = purchaseDetail;
	}

	public TStockTransportInfoDto getTransportDetail() {
		return transportDetail;
	}

	public void setTransportDetail(TStockTransportInfoDto transportDetail) {
		this.transportDetail = transportDetail;
	}

	public TStockInspectionInfoDto getInspectionDetail() {
		return inspectionDetail;
	}

	public void setInspectionDetail(TStockInspectionInfoDto inspectionDetail) {
		this.inspectionDetail = inspectionDetail;
	}

	public TStockShippingInfoDto getShippingDetail() {
		return shippingDetail;
	}

	public void setShippingDetail(TStockShippingInfoDto shippingDetail) {
		this.shippingDetail = shippingDetail;
	}

}
