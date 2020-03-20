
package com.nexware.aajapan.dto;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.utils.AppUtil;

public class LcInvoiceItemDto {
	private String invoiceId;
	private String stockNo;
	private String chassisNo;
	private String maker;
	private String model;
	private String hsCode;
	private Double fob;
	private Double insurance;
	private Double amount;
	private Double freight;
	private String customerId;
	private String customerName;
	private String scheduleId;
	private ObjectId proConsigneeId;
	private ObjectId proNotifypartyId;
	private String consigneeName;
	private String notifypartyName;
	private String consigneeAddress;
	private String notifypartyAddress;
	private String shipmentRequestId;
	private String schedule;
	private String to;
	private String from;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date sailingDate;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date bankSentDate;
	private String shippingMarksId;
	private String shippingMarks;
	private String perVessel;
	private String proformaInvoiceId;
	private String proformaInvoiceNo;
	private String inspectionCertificateNo;
	private String exportCertificateNo;	
	public String getInvoiceId() {
		return this.invoiceId;
	}

	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}

	public String getStockNo() {
		return this.stockNo;
	}

	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}

	public String getChassisNo() {
		return this.chassisNo;
	}

	public void setChassisNo(String chassisNo) {
		this.chassisNo = chassisNo;
	}

	public String getMaker() {
		return this.maker;
	}

	public void setMaker(String maker) {
		this.maker = maker;
	}

	public String getModel() {
		return this.model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Double getAmount() {
		return this.amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getHsCode() {
		return this.hsCode;
	}

	public void setHsCode(String hsCode) {
		this.hsCode = hsCode;
	}

	public String getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
	}

	public String getProConsigneeId() {
		return !AppUtil.isObjectEmpty(this.proConsigneeId) ? this.proConsigneeId.toString() : null;
	}

	public void setProConsigneeId(ObjectId proConsigneeId) {
		this.proConsigneeId = proConsigneeId;
	}

	public String getProNotifypartyId() {
		return !AppUtil.isObjectEmpty(this.proNotifypartyId) ? this.proNotifypartyId.toString() : null;
	}

	public void setProNotifypartyId(ObjectId proNotifypartyId) {
		this.proNotifypartyId = proNotifypartyId;
	}

	public String getConsigneeName() {
		return consigneeName;
	}

	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}

	public String getNotifypartyName() {
		return notifypartyName;
	}

	public void setNotifypartyName(String notifypartyName) {
		this.notifypartyName = notifypartyName;
	}

	public String getConsigneeAddress() {
		return consigneeAddress;
	}

	public void setConsigneeAddress(String consigneeAddress) {
		this.consigneeAddress = consigneeAddress;
	}

	public String getNotifypartyAddress() {
		return notifypartyAddress;
	}

	public void setNotifypartyAddress(String notifypartyAddress) {
		this.notifypartyAddress = notifypartyAddress;
	}

	public String getShipmentRequestId() {
		return shipmentRequestId;
	}

	public void setShipmentRequestId(String shipmentRequestId) {
		this.shipmentRequestId = shipmentRequestId;
	}

	public String getSchedule() {
		return schedule;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public Date getSailingDate() {
		return sailingDate;
	}

	public void setSailingDate(Date sailingDate) {
		this.sailingDate = sailingDate;
	}

	public Date getBankSentDate() {
		return bankSentDate;
	}

	public void setBankSentDate(Date bankSentDate) {
		this.bankSentDate = bankSentDate;
	}

	public String getShippingMarks() {
		return shippingMarks;
	}

	public void setShippingMarks(String shippingMarks) {
		this.shippingMarks = shippingMarks;
	}

	public String getPerVessel() {
		return perVessel;
	}

	public void setPerVessel(String perVessel) {
		this.perVessel = perVessel;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getShippingMarksId() {
		return shippingMarksId;
	}

	public void setShippingMarksId(String shippingMarksId) {
		this.shippingMarksId = shippingMarksId;
	}

	public String getProformaInvoiceId() {
		return proformaInvoiceId;
	}

	public void setProformaInvoiceId(String proformaInvoiceId) {
		this.proformaInvoiceId = proformaInvoiceId;
	}

	public String getProformaInvoiceNo() {
		return proformaInvoiceNo;
	}

	public void setProformaInvoiceNo(String proformaInvoiceNo) {
		this.proformaInvoiceNo = proformaInvoiceNo;
	}

	public Double getFob() {
		return fob;
	}

	public void setFob(Double fob) {
		this.fob = fob;
	}

	public Double getInsurance() {
		return insurance;
	}

	public void setInsurance(Double insurance) {
		this.insurance = insurance;
	}

	public Double getFreight() {
		return freight;
	}

	public void setFreight(Double freight) {
		this.freight = freight;
	}

	public String getInspectionCertificateNo() {
		return inspectionCertificateNo;
	}

	public void setInspectionCertificateNo(String inspectionCertificateNo) {
		this.inspectionCertificateNo = inspectionCertificateNo;
	}

	public String getExportCertificateNo() {
		return exportCertificateNo;
	}

	public void setExportCertificateNo(String exportCertificateNo) {
		this.exportCertificateNo = exportCertificateNo;
	}

}
