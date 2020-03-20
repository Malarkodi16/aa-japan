package com.nexware.aajapan.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TStockInspectionGroupInfoDto {
	
	private String inspectionCompany;
	private int inspectionCompanyFlag;
	private String destCountry;
	private String forwarder;
	private String status;
	private String invoiceStatus;
	private Double estimatedAmount;
	private Double actualAmount;
	
	public String getInspectionCompany() {
		return inspectionCompany;
	}
	public void setInspectionCompany(String inspectionCompany) {
		this.inspectionCompany = inspectionCompany;
	}
	public String getDestCountry() {
		return destCountry;
	}
	public void setDestCountry(String destCountry) {
		this.destCountry = destCountry;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getInvoiceStatus() {
		return invoiceStatus;
	}
	public void setInvoiceStatus(String invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}
	public Double getEstimatedAmount() {
		return estimatedAmount;
	}
	public void setEstimatedAmount(Double estimatedAmount) {
		this.estimatedAmount = estimatedAmount;
	}
	public Double getActualAmount() {
		return actualAmount;
	}
	public void setActualAmount(Double actualAmount) {
		this.actualAmount = actualAmount;
	}
	public String getForwarder() {
		return forwarder;
	}
	public void setForwarder(String forwarder) {
		this.forwarder = forwarder;
	}
	public int getInspectionCompanyFlag() {
		return inspectionCompanyFlag;
	}
	public void setInspectionCompanyFlag(int inspectionCompanyFlag) {
		this.inspectionCompanyFlag = inspectionCompanyFlag;
	}

}
