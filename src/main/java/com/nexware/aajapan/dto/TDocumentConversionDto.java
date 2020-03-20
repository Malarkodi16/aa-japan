package com.nexware.aajapan.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author Karthik
 *
 */
public class TDocumentConversionDto {

	private String id;
	private String stockNo;
	private int docType;
	private Integer docConvertTo;
	private int exportCertificateStatus;
	private int docSentStatus;
	private int documentConvertionStatus;
	private String chassisNo;
	@JsonFormat(pattern = "MM/yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date firstRegDate;
	private Integer receivedDocumentType;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date documentReceivedDate;
	private String oldNumberPlate;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date documentConvertedDate;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date purchaseInfoDate;
	private String purchaseType;
	private String supplier;
	private Long shuppinNo;
	private String supplierCode;
	private String supplierName;
	private String auctionHouseId;
	private String auctionHouse;
	private String handOverTo;
	private String handOverPerson;
	private Double documentFob;
	private String destinationPort;
	private Integer documentStatus;
	private String destinationCountry;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date plateNoReceivedDate;

	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date reauctionDate;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStockNo() {
		return this.stockNo;
	}

	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}

	public int getDocType() {
		return this.docType;
	}

	public void setDocType(int docType) {
		this.docType = docType;
	}

	public int getExportCertificateStatus() {
		return this.exportCertificateStatus;
	}

	public void setExportCertificateStatus(int exportCertificateStatus) {
		this.exportCertificateStatus = exportCertificateStatus;
	}

	public int getDocSentStatus() {
		return this.docSentStatus;
	}

	public void setDocSentStatus(int docSentStatus) {
		this.docSentStatus = docSentStatus;
	}

	public int getDocumentConvertionStatus() {
		return this.documentConvertionStatus;
	}

	public void setDocumentConvertionStatus(int documentConvertionStatus) {
		this.documentConvertionStatus = documentConvertionStatus;
	}

	public String getChassisNo() {
		return this.chassisNo;
	}

	public void setChassisNo(String chassisNo) {
		this.chassisNo = chassisNo;
	}

	public Date getFirstRegDate() {
		return this.firstRegDate;
	}

	public void setFirstRegDate(Date firstRegDate) {
		this.firstRegDate = firstRegDate;
	}

	public Integer getReceivedDocumentType() {
		return this.receivedDocumentType;
	}

	public void setReceivedDocumentType(Integer receivedDocumentType) {
		this.receivedDocumentType = receivedDocumentType;
	}

	public Date getDocumentReceivedDate() {
		return this.documentReceivedDate;
	}

	public void setDocumentReceivedDate(Date documentReceivedDate) {
		this.documentReceivedDate = documentReceivedDate;
	}

	public String getOldNumberPlate() {
		return this.oldNumberPlate;
	}

	public void setOldNumberPlate(String oldNumberPlate) {
		this.oldNumberPlate = oldNumberPlate;
	}

	public Date getDocumentConvertedDate() {
		return this.documentConvertedDate;
	}

	public void setDocumentConvertedDate(Date documentConvertedDate) {
		this.documentConvertedDate = documentConvertedDate;
	}

	public Date getPurchaseInfoDate() {
		return this.purchaseInfoDate;
	}

	public void setPurchaseInfoDate(Date purchaseInfoDate) {
		this.purchaseInfoDate = purchaseInfoDate;
	}

	public String getPurchaseType() {
		return this.purchaseType;
	}

	public void setPurchaseType(String purchaseType) {
		this.purchaseType = purchaseType;
	}

	public String getSupplier() {
		return this.supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public Long getShuppinNo() {
		return this.shuppinNo;
	}

	public void setShuppinNo(Long shuppinNo) {
		this.shuppinNo = shuppinNo;
	}

	public String getSupplierCode() {
		return this.supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public String getSupplierName() {
		return this.supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getAuctionHouseId() {
		return this.auctionHouseId;
	}

	public void setAuctionHouseId(String auctionHouseId) {
		this.auctionHouseId = auctionHouseId;
	}

	public String getAuctionHouse() {
		return this.auctionHouse;
	}

	public void setAuctionHouse(String auctionHouse) {
		this.auctionHouse = auctionHouse;
	}

	public String getHandOverTo() {
		return this.handOverTo;
	}

	public void setHandOverTo(String handOverTo) {
		this.handOverTo = handOverTo;
	}

	public String getHandOverPerson() {
		return this.handOverPerson;
	}

	public void setHandOverPerson(String handOverPerson) {
		this.handOverPerson = handOverPerson;
	}

	public Double getDocumentFob() {
		return this.documentFob;
	}

	public void setDocumentFob(Double documentFob) {
		this.documentFob = documentFob;
	}

	public String getDestinationPort() {
		return this.destinationPort;
	}

	public void setDestinationPort(String destinationPort) {
		this.destinationPort = destinationPort;
	}

	public Integer getDocumentStatus() {
		return this.documentStatus;
	}

	public void setDocumentStatus(Integer documentStatus) {
		this.documentStatus = documentStatus;
	}

	public String getDestinationCountry() {
		return this.destinationCountry;
	}

	public void setDestinationCountry(String destinationCountry) {
		this.destinationCountry = destinationCountry;
	}

	public Integer getDocConvertTo() {
		return this.docConvertTo;
	}

	public void setDocConvertTo(Integer docConvertTo) {
		this.docConvertTo = docConvertTo;
	}

	public Date getPlateNoReceivedDate() {
		return this.plateNoReceivedDate;
	}

	public void setPlateNoReceivedDate(Date plateNoReceivedDate) {
		this.plateNoReceivedDate = plateNoReceivedDate;
	}

	public Date getReauctionDate() {
		return reauctionDate;
	}

	public void setReauctionDate(Date reauctionDate) {
		this.reauctionDate = reauctionDate;
	}

}
