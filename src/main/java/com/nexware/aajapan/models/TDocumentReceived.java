package com.nexware.aajapan.models;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.listeners.EntityModelBase;

@Document(collection = "t_doc_recvd")
public class TDocumentReceived extends EntityModelBase {
	@Id
	private String id;
	@Indexed(unique = true)
	private String stockNo;
	private Integer documentType;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date documentReceivedDate;
	private Integer documentConvertTo;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date documentConvertedDate;
	private Integer documentStatus;
	private Integer rikujiStatus;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date rikujiUpdateToOneDate;
	private String rikujiRemarks;
	private String remarks;

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

	public Integer getDocumentType() {
		return this.documentType;
	}

	public void setDocumentType(Integer documentType) {
		this.documentType = documentType;
	}

	public Date getDocumentReceivedDate() {
		return this.documentReceivedDate;
	}

	public void setDocumentReceivedDate(Date documentReceivedDate) {
		this.documentReceivedDate = documentReceivedDate;
	}

	public Integer getDocumentConvertTo() {
		return this.documentConvertTo;
	}

	public void setDocumentConvertTo(Integer documentConvertTo) {
		this.documentConvertTo = documentConvertTo;
	}

	public Date getDocumentConvertedDate() {
		return this.documentConvertedDate;
	}

	public void setDocumentConvertedDate(Date documentConvertedDate) {
		this.documentConvertedDate = documentConvertedDate;
	}

	public Integer getDocumentStatus() {
		return this.documentStatus;
	}

	public void setDocumentStatus(Integer documentStatus) {
		this.documentStatus = documentStatus;
	}

	public Integer getRikujiStatus() {
		return this.rikujiStatus;
	}

	public void setRikujiStatus(Integer rikujiStatus) {
		this.rikujiStatus = rikujiStatus;
	}

	public Date getRikujiUpdateToOneDate() {
		return this.rikujiUpdateToOneDate;
	}

	public void setRikujiUpdateToOneDate(Date rikujiUpdateToOneDate) {
		this.rikujiUpdateToOneDate = rikujiUpdateToOneDate;
	}

	public String getRikujiRemarks() {
		return this.rikujiRemarks;
	}

	public void setRikujiRemarks(String rikujiRemarks) {
		this.rikujiRemarks = rikujiRemarks;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
