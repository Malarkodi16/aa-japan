package com.nexware.aajapan.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.models.MInspectionCompany;
import com.nexware.aajapan.models.MShippingCompany;
import com.nexware.aajapan.models.TDocumentConversion;
import com.nexware.aajapan.utils.AppUtil;

public class StockInfoDocumentConversionDto {

	private Integer docConvertTo;// 1
	private String docConvertToTextValue;// 1
	private Integer exportCertificateStatus;// 1
	private String exportCertificateStatusTextValue;// 1
	private String shippingCompanyId;// 1
	private String shippingCompanyName;// 1
	private String inspectionCompanyId;// 1
	private String inspectionCompanyName;// 1
	private Integer docOriginalSent;// 1
	private String docOriginalSentTextValue;// 1
	private Integer docEmailSent;// 1
	private String docEmailSentTextValue;// 1
	private Integer docReceivedStatus;// 1
	private String docReceivedStatusTextValue;// 1
	private Integer handoverStatus;// 1
	private String handoverStatusTextValue;// 1
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date docSendDate;// 1
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date reauctionDate;// 1

	public StockInfoDocumentConversionDto() {

	}

	public StockInfoDocumentConversionDto(TDocumentConversion documentConversion, MShippingCompany shippingCompany,
			MInspectionCompany inspectionCompany) {
		prepareData(documentConversion, shippingCompany, inspectionCompany);
	}

	public Integer getDocConvertTo() {
		return docConvertTo;
	}

	public void setDocConvertTo(Integer docConvertTo) {
		if (!AppUtil.isObjectEmpty(docConvertTo)) {
			if (docConvertTo.equals(Constants.STOCK_DOCUMENT_TYPE_CONVERT_TO_EXPORT_CERTIFICATE)) {
				docConvertToTextValue = "EXPORT CERTIFICATE";
			} else if (docConvertTo.equals(Constants.STOCK_DOCUMENT_TYPE_CONVERT_TO_NAME_TRANSFER)) {
				docConvertToTextValue = "NAME TRANSFER";
			} else if (docConvertTo.equals(Constants.STOCK_DOCUMENT_TYPE_CONVERT_TO_DOMESTIC)) {
				docConvertToTextValue = "DOMESTIC";
			} else if (docConvertTo.equals(Constants.STOCK_DOCUMENT_TYPE_CONVERT_TO_MASHO)) {
				docConvertToTextValue = "MASHO";
			} else if (docConvertTo.equals(Constants.STOCK_DOCUMENT_TYPE_CONVERT_TO_PARTS)) {
				docConvertToTextValue = "PARTS";
			} else if (docConvertTo.equals(Constants.STOCK_DOCUMENT_TYPE_CONVERT_TO_SHUPPIN)) {
				docConvertToTextValue = "SHUPPIN";
			} else if (docConvertTo.equals(Constants.STOCK_DOCUMENT_TYPE_EXPIRED)) {
				docConvertToTextValue = "EXPIRED";
			}
		}
		this.docConvertTo = docConvertTo;
	}

	public String getDocConvertToTextValue() {
		return docConvertToTextValue;
	}

	public void setDocConvertToTextValue(String docConvertToTextValue) {
		this.docConvertToTextValue = docConvertToTextValue;
	}

	public Integer getExportCertificateStatus() {
		return exportCertificateStatus;
	}

	public void setExportCertificateStatus(Integer exportCertificateStatus) {
		if (!AppUtil.isObjectEmpty(exportCertificateStatus)) {
			if (exportCertificateStatus.equals(Constants.EXPORT_CERTIFICATE_INALAIN)) {
				this.exportCertificateStatusTextValue = "ALAIN";
			} else if (exportCertificateStatus.equals(Constants.EXPORT_CERTIFICATE_SHIPPING_COMPANY)) {
				this.exportCertificateStatusTextValue = "SHIPPING COMPANY";
			} else if (exportCertificateStatus.equals(Constants.EXPORT_CERTIFICATE_INSPECTION_COMPANY)) {
				this.exportCertificateStatusTextValue = "INSPECTION COMPANY";
			}
		}
		this.exportCertificateStatus = exportCertificateStatus;
	}

	public String getExportCertificateStatusTextValue() {
		return exportCertificateStatusTextValue;
	}

	public void setExportCertificateStatusTextValue(String exportCertificateStatusTextValue) {
		this.exportCertificateStatusTextValue = exportCertificateStatusTextValue;
	}

	public String getShippingCompanyId() {
		return shippingCompanyId;
	}

	public void setShippingCompanyId(String shippingCompanyId) {
		this.shippingCompanyId = shippingCompanyId;
	}

	public String getInspectionCompanyId() {
		return inspectionCompanyId;
	}

	public void setInspectionCompanyId(String inspectionCompanyId) {
		this.inspectionCompanyId = inspectionCompanyId;
	}

	public Integer getDocOriginalSent() {
		return docOriginalSent;
	}

	public void setDocOriginalSent(Integer docOriginalSent) {
		if (!AppUtil.isObjectEmpty(docOriginalSent)) {
			if (docOriginalSent.equals(Constants.EXPORT_CERTIFICATE_DOCUMENT_ORIGINAL_NOT_SENT)) {
				docOriginalSentTextValue = "Documents Not Sent";
			} else if (docOriginalSent.equals(Constants.EXPORT_CERTIFICATE_DOCUMENT_ORIGINAL_SENT)) {
				docOriginalSentTextValue = "Documents Sent";
			}
		}
		this.docOriginalSent = docOriginalSent;
	}

	public String getDocOriginalSentTextValue() {
		return docOriginalSentTextValue;
	}

	public void setDocOriginalSentTextValue(String docOriginalSentTextValue) {
		this.docOriginalSentTextValue = docOriginalSentTextValue;
	}

	public Integer getDocEmailSent() {
		return docEmailSent;
	}

	public void setDocEmailSent(Integer docEmailSent) {
		if (!AppUtil.isObjectEmpty(docEmailSent)) {
			if (docEmailSent.equals(Constants.EXPORT_CERTIFICATE_DOCUMENT_EMAIL_NOT_SENT)) {
				docEmailSentTextValue = "Email Not Sent";
			} else if (docEmailSent.equals(Constants.EXPORT_CERTIFICATE_DOCUMENT_EMAIL_SENT)) {
				docEmailSentTextValue = "Email Sent";
			}
		}
		this.docEmailSent = docEmailSent;
	}

	public String getDocEmailSentTextValue() {
		return docEmailSentTextValue;
	}

	public void setDocEmailSentTextValue(String docEmailSentTextValue) {
		this.docEmailSentTextValue = docEmailSentTextValue;
	}

	public Integer getDocReceivedStatus() {
		return docReceivedStatus;
	}

	public void setDocReceivedStatus(Integer docReceivedStatus) {
		if (!AppUtil.isObjectEmpty(docReceivedStatus)) {
			if (docReceivedStatus.equals(Constants.EXPORT_CERTIFICATE_ORIGINAL_NOT_RECEIVED)) {
				docReceivedStatusTextValue = "Document Not Yet Received";
			} else if (docReceivedStatus.equals(Constants.EXPORT_CERTIFICATE_ORIGINAL_RECEIVED)) {
				docReceivedStatusTextValue = "Document Received";
			}
		}
		this.docReceivedStatus = docReceivedStatus;
	}

	public String getDocReceivedStatusTextValue() {
		return docReceivedStatusTextValue;
	}

	public void setDocReceivedStatusTextValue(String docReceivedStatusTextValue) {
		this.docReceivedStatusTextValue = docReceivedStatusTextValue;
	}

	public Integer getHandoverStatus() {
		return handoverStatus;
	}

	public void setHandoverStatus(Integer handoverStatus) {
		if (!AppUtil.isObjectEmpty(handoverStatus)) {
			if (handoverStatus.equals(Constants.DOCUMENTS_HANDOVER_TO_ACCOUNTS)) {
				handoverStatusTextValue = "ACCOUNTS";
			} else if (handoverStatus.equals(Constants.DOCUMENTS_HANDOVER_TO_SALES)) {
				handoverStatusTextValue = "SALES";
			} else if (handoverStatus.equals(Constants.DOCUMENTS_HANDOVER_TO_RECYCLE_PURPOSE)) {
				handoverStatusTextValue = "RECYCLE PURPOSE";
			} else if (handoverStatus.equals(Constants.DOCUMENTS_HANDOVER_TO_RIKUJI)) {
				handoverStatusTextValue = "RIKUJI";
			} else if (handoverStatus.equals(Constants.DOCUMENTS_HANDOVER_TO_SHIPPING)) {
				handoverStatusTextValue = "SHIPPING";
			} else if (handoverStatus.equals(Constants.DOCUMENTS_HANDOVER_TO_INSPECTION)) {
				handoverStatusTextValue = "INSPECTION";
			} else if (handoverStatus.equals(Constants.DOCUMENTS_HANDOVER_TO_DOCUMENTS_TEAM)) {
				handoverStatusTextValue = "DOCUMENT";
			}
		}
		this.handoverStatus = handoverStatus;
	}

	public String getHandoverStatusTextValue() {
		return handoverStatusTextValue;
	}

	public void setHandoverStatusTextValue(String handoverStatusTextValue) {
		this.handoverStatusTextValue = handoverStatusTextValue;
	}

	public Date getDocSendDate() {
		return docSendDate;
	}

	public void setDocSendDate(Date docSendDate) {
		this.docSendDate = docSendDate;
	}

	public Date getReauctionDate() {
		return reauctionDate;
	}

	public void setReauctionDate(Date reauctionDate) {
		this.reauctionDate = reauctionDate;
	}

	public String getShippingCompanyName() {
		return shippingCompanyName;
	}

	public void setShippingCompanyName(String shippingCompanyName) {
		this.shippingCompanyName = shippingCompanyName;
	}

	public String getInspectionCompanyName() {
		return inspectionCompanyName;
	}

	public void setInspectionCompanyName(String inspectionCompanyName) {
		this.inspectionCompanyName = inspectionCompanyName;
	}

	private void prepareData(TDocumentConversion documentConversion, MShippingCompany shippingCompany,
			MInspectionCompany inspectionCompany) {
		if (!AppUtil.isObjectEmpty(documentConversion)) {
			this.setDocConvertTo(documentConversion.getDocConvertTo());
			this.setExportCertificateStatus(documentConversion.getExportCertificateStatus());
			this.setDocOriginalSent(documentConversion.getDocOriginalSent());
			this.setDocEmailSent(documentConversion.getDocEmailSent());
			this.setDocReceivedStatus(documentConversion.getDocReceivedStatus());
			this.setHandoverStatus(documentConversion.getHandoverStatus());
			this.setDocSendDate(documentConversion.getDocSendDate());
			this.setReauctionDate(documentConversion.getReauctionDate());
		}
		if (!AppUtil.isObjectEmpty(shippingCompany)) {
			this.setShippingCompanyId(shippingCompany.getShippingCompanyNo());
			this.setShippingCompanyName(shippingCompany.getName());
		}
		if (!AppUtil.isObjectEmpty(inspectionCompany)) {
			this.setInspectionCompanyId(inspectionCompany.getCode());
			this.setInspectionCompanyName(inspectionCompany.getName());
		}

	}
}
