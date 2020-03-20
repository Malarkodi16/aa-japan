package com.nexware.aajapan.dto;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.models.TDocumentReceived;
import com.nexware.aajapan.models.TStock;
import com.nexware.aajapan.utils.AppUtil;

public class StockInfoDocumentDto {

	private Integer documentRecivedStatus;// 1
	private String documentRecivedStatusTextValue;// 1
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date plateNoReceivedDate;// 1
	private Integer documentType;// 1
	private String documentTypeTextValue;// 1
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date documentReceivedDate;// 1
	private Integer documentConvertTo;// 1
	private String documentConvertToTextValue;
	private Double documentFob;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date documentConvertedDate;
	private Integer handoverTo;
	private String handoverToTextValue;
	private String handoverToUserId;
	private String handoverToUserName;
	private Integer rikujiStatus;
	private String rikujiStatusValueText;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date rikujiUpdateToOneDate;
	private String rikujiRemarks;
	List<StockInfoDocumentConversionDto> documentConversionDtos;
	List<StockInfoSalesDto> salesInvoices;

	public StockInfoDocumentDto() {

	}

	public StockInfoDocumentDto(TStock stock, TDocumentReceived documentReceived) {
		prepareData(stock, documentReceived);
	}

	public Integer getDocumentRecivedStatus() {
		return documentRecivedStatus;
	}

	public void setDocumentRecivedStatus(Integer documentRecivedStatus) {
		if (documentRecivedStatus.equals(Constants.STOCK_DOCUMENT_NOT_RECEIVED)) {
			documentRecivedStatusTextValue = "Not Received";
		} else if (documentRecivedStatus.equals(Constants.STOCK_DOCUMENT_RECEIVED)) {
			documentRecivedStatusTextValue = "Received";
		} else if (documentRecivedStatus.equals(Constants.STOCK_DOCUMENT_RECEIVED_AND_CANCELLED)) {
			documentRecivedStatusTextValue = "Received And Cancelled";
		} else if (documentRecivedStatus.equals(Constants.STOCK_DOCUMENT_CONVERT)) {
			documentRecivedStatusTextValue = "Documetn Converted";
		}

		this.documentRecivedStatus = documentRecivedStatus;
	}

	public Date getPlateNoReceivedDate() {
		return plateNoReceivedDate;
	}

	public void setPlateNoReceivedDate(Date plateNoReceivedDate) {
		this.plateNoReceivedDate = plateNoReceivedDate;
	}

	public Integer getDocumentType() {
		return documentType;
	}

	public void setDocumentType(Integer documentType) {
		switch (documentType) {
		case 0:
			documentTypeTextValue = "MASHO";
			break;
		case 1:
			documentTypeTextValue = "SHAKEN";
			break;
		default:
			break;
		}
		this.documentType = documentType;
	}

	public Date getDocumentReceivedDate() {
		return documentReceivedDate;
	}

	public void setDocumentReceivedDate(Date documentReceivedDate) {
		this.documentReceivedDate = documentReceivedDate;
	}

	public Integer getDocumentConvertTo() {
		return documentConvertTo;
	}

	public void setDocumentConvertTo(Integer documentConvertTo) {
		if (documentConvertTo.equals(Constants.STOCK_DOCUMENT_TYPE_CONVERT_TO_EXPORT_CERTIFICATE)) {
			documentConvertToTextValue = "EXPORT CERTIFICATE";
		} else if (documentConvertTo.equals(Constants.STOCK_DOCUMENT_TYPE_CONVERT_TO_NAME_TRANSFER)) {
			documentConvertToTextValue = "NAME TRANSFER";
		} else if (documentConvertTo.equals(Constants.STOCK_DOCUMENT_TYPE_CONVERT_TO_DOMESTIC)) {
			documentConvertToTextValue = "DOMESTIC";
		} else if (documentConvertTo.equals(Constants.STOCK_DOCUMENT_TYPE_CONVERT_TO_MASHO)) {
			documentConvertToTextValue = "MASHO";
		} else if (documentConvertTo.equals(Constants.STOCK_DOCUMENT_TYPE_CONVERT_TO_PARTS)) {
			documentConvertToTextValue = "PARTS";
		} else if (documentConvertTo.equals(Constants.STOCK_DOCUMENT_TYPE_CONVERT_TO_SHUPPIN)) {
			documentConvertToTextValue = "SHUPPIN";
		} else if (documentConvertTo.equals(Constants.STOCK_DOCUMENT_TYPE_EXPIRED)) {
			documentConvertToTextValue = "EXPIRED";
		}
		this.documentConvertTo = documentConvertTo;
	}

	public String getDocumentRecivedStatusTextValue() {
		return documentRecivedStatusTextValue;
	}

	public void setDocumentRecivedStatusTextValue(String documentRecivedStatusTextValue) {
		this.documentRecivedStatusTextValue = documentRecivedStatusTextValue;
	}

	public String getDocumentTypeTextValue() {
		return documentTypeTextValue;
	}

	public void setDocumentTypeTextValue(String documentTypeTextValue) {
		this.documentTypeTextValue = documentTypeTextValue;
	}

	public String getDocumentConvertToTextValue() {
		return documentConvertToTextValue;
	}

	public void setDocumentConvertToTextValue(String documentConvertToTextValue) {
		this.documentConvertToTextValue = documentConvertToTextValue;
	}

	public Double getDocumentFob() {
		return documentFob;
	}

	public void setDocumentFob(Double documentFob) {
		this.documentFob = documentFob;
	}

	public Date getDocumentConvertedDate() {
		return documentConvertedDate;
	}

	public void setDocumentConvertedDate(Date documentConvertedDate) {
		this.documentConvertedDate = documentConvertedDate;
	}

	public Integer getHandoverTo() {
		return handoverTo;
	}

	public void setHandoverTo(Integer handoverTo) {
		if (handoverTo.equals(Constants.DOCUMENTS_HANDOVER_TO_ACCOUNTS)) {
			this.handoverToTextValue = "ACCOUNTS TEAM";
		} else if (handoverTo.equals(Constants.DOCUMENTS_HANDOVER_TO_SALES)) {
			this.handoverToTextValue = "SALES TEAM";
		} else if (handoverTo.equals(Constants.DOCUMENTS_HANDOVER_TO_RECYCLE_PURPOSE)) {
			this.handoverToTextValue = "RECYCLE PURPOSE";
		} else if (handoverTo.equals(Constants.DOCUMENTS_HANDOVER_TO_RIKUJI)) {
			this.handoverToTextValue = "RIKUJI";
		} else if (handoverTo.equals(Constants.DOCUMENTS_HANDOVER_TO_SHIPPING)) {
			this.handoverToTextValue = "SHIPPING TEAM";
		} else if (handoverTo.equals(Constants.DOCUMENTS_HANDOVER_TO_INSPECTION)) {
			this.handoverToTextValue = "INSPECTION";
		} else if (handoverTo.equals(Constants.DOCUMENTS_HANDOVER_TO_DOCUMENTS_TEAM)) {
			this.handoverToTextValue = "DOCUMENTS TEAM";
		}
		this.handoverTo = handoverTo;
	}

	public String getHandoverToTextValue() {
		return handoverToTextValue;
	}

	public void setHandoverToTextValue(String handoverToTextValue) {
		this.handoverToTextValue = handoverToTextValue;
	}

	public Integer getRikujiStatus() {
		return rikujiStatus;
	}

	public void setRikujiStatus(Integer rikujiStatus) {
		if (rikujiStatus == Constants.STOCK_RIKUJI_STATUS_0) {
			rikujiStatusValueText = "NOT SEND TO RIKUJI";
		} else if (rikujiStatus == Constants.STOCK_RIKUJI_STATUS_1) {
			rikujiStatusValueText = "SENT TO RIKUJI";
		}
		this.rikujiStatus = rikujiStatus;
	}

	public String getRikujiStatusValueText() {
		return rikujiStatusValueText;
	}

	public void setRikujiStatusValueText(String rikujiStatusValueText) {
		this.rikujiStatusValueText = rikujiStatusValueText;
	}

	public Date getRikujiUpdateToOneDate() {
		return rikujiUpdateToOneDate;
	}

	public void setRikujiUpdateToOneDate(Date rikujiUpdateToOneDate) {
		this.rikujiUpdateToOneDate = rikujiUpdateToOneDate;
	}

	public String getRikujiRemarks() {
		return rikujiRemarks;
	}

	public void setRikujiRemarks(String rikujiRemarks) {
		this.rikujiRemarks = rikujiRemarks;
	}

	public List<StockInfoDocumentConversionDto> getDocumentConversionDtos() {
		return documentConversionDtos;
	}

	public void setDocumentConversionDtos(List<StockInfoDocumentConversionDto> documentConversionDtos) {
		this.documentConversionDtos = documentConversionDtos;
	}

	public String getHandoverToUserId() {
		return handoverToUserId;
	}

	public void setHandoverToUserId(String handoverToUserId) {
		this.handoverToUserId = handoverToUserId;
	}

	public String getHandoverToUserName() {
		return handoverToUserName;
	}

	public void setHandoverToUserName(String handoverToUserName) {
		this.handoverToUserName = handoverToUserName;
	}

	public List<StockInfoSalesDto> getSalesInvoices() {
		return salesInvoices;
	}

	public void setSalesInvoices(List<StockInfoSalesDto> salesInvoices) {
		this.salesInvoices = salesInvoices;
	}

	private void prepareData(TStock stock, TDocumentReceived documentReceived) {
		if (!AppUtil.isObjectEmpty(documentReceived)) {
			this.setDocumentRecivedStatus(AppUtil.ifNullOrEmpty(stock.getDocumentStatus(), -1));
			this.setPlateNoReceivedDate(stock.getPlateNoReceivedDate());
			this.setDocumentType(AppUtil.ifNullOrEmpty(documentReceived.getDocumentType(), -1));
			this.setDocumentReceivedDate(documentReceived.getDocumentReceivedDate());
			this.setDocumentConvertTo(AppUtil.ifNullOrEmpty(documentReceived.getDocumentConvertTo(), -1));
			this.setDocumentFob(stock.getDocumentFob());
			this.setHandoverTo(AppUtil.ifNullOrEmpty(stock.getHandoverTo(), -1));
			this.setHandoverToUserId(stock.getHandoverToUserId());
			if (!AppUtil.isObjectEmpty(documentReceived)) {
				this.setDocumentConvertedDate(documentReceived.getDocumentConvertedDate());
				this.setRikujiStatus(AppUtil.ifNullOrEmpty(documentReceived.getRikujiStatus(), -1));
				this.setRikujiUpdateToOneDate(documentReceived.getRikujiUpdateToOneDate());
				this.setRikujiRemarks(documentReceived.getRikujiRemarks());
			}
		}
	}
}
