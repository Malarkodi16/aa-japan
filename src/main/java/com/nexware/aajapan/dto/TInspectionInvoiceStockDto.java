package com.nexware.aajapan.dto;

public class TInspectionInvoiceStockDto {

	private String stockNo;
	private String maker;
	private String model;
	private String chassisNo;
	private Double amount;
	private Double taxAmount;
	private Double totalTaxIncluded;
	private String inspectionInvoiceId;

	public String getStockNo() {
		return stockNo;
	}

	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}

	public String getMaker() {
		return maker;
	}

	public void setMaker(String maker) {
		this.maker = maker;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getChassisNo() {
		return chassisNo;
	}

	public void setChassisNo(String chassisNo) {
		this.chassisNo = chassisNo;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public Double getTotalTaxIncluded() {
		return totalTaxIncluded;
	}

	public void setTotalTaxIncluded(Double totalTaxIncluded) {
		this.totalTaxIncluded = totalTaxIncluded;
	}

	public String getInspectionInvoiceId() {
		return inspectionInvoiceId;
	}

	public void setInspectionInvoiceId(String inspectionInvoiceId) {
		this.inspectionInvoiceId = inspectionInvoiceId;
	}

}
