package com.nexware.aajapan.models;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.listeners.EntityModelBase;
import com.nexware.aajapan.utils.AppUtil;

@Document(collection = "t_frght_shpng_invc")
public class TFreightShippingInvoice extends EntityModelBase {
	@Id
	private String id;
	@Indexed
	private String shipmentRequestId;
	private String invoiceNo;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date date;
	private String forwarder;
	private String stockNo;
	private Double freightCharge;
	private Double shippingCharge;
	private Double inspectionCharge;
	private Double radiationCharge;
	private Double exchangeRate;
	private Double zarExchangeRate;
	private Double otherCharges;
	private Double perM3Usd;
	private Double freightChargeUsd;
	private String blNo;
	private Double usd;
	private Double jpy;
	private Double length;
	private Double width;
	private Double m3;
	private Double height;
	private Double noOfContainers;
	private Integer paymentApprove = 0;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date dueDate;
	private ApprovePaymentDetails approvePaymentDetails;
	private Integer status;
	private String cancelledRemarks;
	private Double invoiceAmountReceived;
	private Double totalAmount;
	private String invoiceAttachmentFilename;
	private Integer invoiceUpload;
	private Integer attachementViewed;
	private String invoiceAttachmentDiskFilename;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date approvedDate;
	private String approvedBy;
	private Double totalAmountUsd;
	private Integer paymentType;
	private Double invoiceAmountReceivedUsd;
	private Integer currencyType;
	private Integer freightPaymentStatus;
	private Integer invoiceType;
	@Transient
	private Double totalWithoutFreight;

	public TFreightShippingInvoice() {

	}

	public TFreightShippingInvoice(String shipmentRequestId, String invoiceNo, Date date, String forwarder,
			String stockNo) {
		super();
		this.shipmentRequestId = shipmentRequestId;
		this.invoiceNo = invoiceNo;
		this.date = date;
		this.forwarder = forwarder;
		this.stockNo = stockNo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getShipmentRequestId() {
		return shipmentRequestId;
	}

	public void setShipmentRequestId(String shipmentRequestId) {
		this.shipmentRequestId = shipmentRequestId;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getForwarder() {
		return forwarder;
	}

	public void setForwarder(String forwarder) {
		this.forwarder = forwarder;
	}

	public String getStockNo() {
		return stockNo;
	}

	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}

	public Double getFreightCharge() {
		return freightCharge;
	}

	public void setFreightCharge(Double freightCharge) {
		this.freightCharge = freightCharge;
	}

	public Double getShippingCharge() {
		return shippingCharge;
	}

	public void setShippingCharge(Double shippingCharge) {
		this.shippingCharge = shippingCharge;
	}

	public Double getInspectionCharge() {
		return inspectionCharge;
	}

	public void setInspectionCharge(Double inspectionCharge) {
		this.inspectionCharge = inspectionCharge;
	}

	public Double getRadiationCharge() {
		return radiationCharge;
	}

	public void setRadiationCharge(Double radiationCharge) {
		this.radiationCharge = radiationCharge;
	}

	public Double getUsd() {
		return usd;
	}

	public void setUsd(Double usd) {
		this.usd = usd;
	}

	public Double getJpy() {
		return jpy;
	}

	public void setJpy(Double jpy) {
		this.jpy = jpy;
	}

	public Double getOtherCharges() {
		return otherCharges;
	}

	public void setOtherCharges(Double otherCharges) {
		this.otherCharges = otherCharges;
	}

	public Double getPerM3Usd() {
		return perM3Usd;
	}

	public void setPerM3Usd(Double perM3Usd) {
		this.perM3Usd = perM3Usd;
	}

	public Double getFreightChargeUsd() {
		return freightChargeUsd;
	}

	public void setFreightChargeUsd(Double freightChargeUsd) {
		this.freightChargeUsd = freightChargeUsd;
	}

	public String getBlNo() {
		return blNo;
	}

	public void setBlNo(String blNo) {
		this.blNo = blNo;
	}

	public Double getLength() {
		return length;
	}

	public void setLength(Double length) {
		this.length = length;
	}

	public Double getWidth() {
		return width;
	}

	public void setWidth(Double width) {
		this.width = width;
	}

	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	public Double getNoOfContainers() {
		return noOfContainers;
	}

	public void setNoOfContainers(Double noOfContainers) {
		this.noOfContainers = noOfContainers;
	}

	public ApprovePaymentDetails getApprovePaymentDetails() {
		return approvePaymentDetails;
	}

	public void setApprovePaymentDetails(ApprovePaymentDetails approvePaymentDetails) {
		this.approvePaymentDetails = approvePaymentDetails;
	}

	public Integer getPaymentApprove() {
		return paymentApprove;
	}

	public void setPaymentApprove(Integer paymentApprove) {
		this.paymentApprove = paymentApprove;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public String getCancelledRemarks() {
		return cancelledRemarks;
	}

	public void setCancelledRemarks(String cancelledRemarks) {
		this.cancelledRemarks = cancelledRemarks;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getInvoiceAmountReceived() {
		return invoiceAmountReceived;
	}

	public void setInvoiceAmountReceived(Double invoiceAmountReceived) {
		this.invoiceAmountReceived = invoiceAmountReceived;
	}

	public String getInvoiceAttachmentFilename() {
		return invoiceAttachmentFilename;
	}

	public void setInvoiceAttachmentFilename(String invoiceAttachmentFilename) {
		this.invoiceAttachmentFilename = invoiceAttachmentFilename;
	}

	public Integer getInvoiceUpload() {
		return invoiceUpload;
	}

	public void setInvoiceUpload(Integer invoiceUpload) {
		this.invoiceUpload = invoiceUpload;
	}

	public Integer getAttachementViewed() {
		return attachementViewed;
	}

	public void setAttachementViewed(Integer attachementViewed) {
		this.attachementViewed = attachementViewed;
	}

	public String getInvoiceAttachmentDiskFilename() {
		return invoiceAttachmentDiskFilename;
	}

	public void setInvoiceAttachmentDiskFilename(String invoiceAttachmentDiskFilename) {
		this.invoiceAttachmentDiskFilename = invoiceAttachmentDiskFilename;
	}

	public Date getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Double getTotalAmountUsd() {
		return totalAmountUsd;
	}

	public void setTotalAmountUsd(Double totalAmountUsd) {
		this.totalAmountUsd = totalAmountUsd;
	}

	public Integer getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(Integer paymentType) {
		this.paymentType = paymentType;
	}

	public Double getInvoiceAmountReceivedUsd() {
		return invoiceAmountReceivedUsd;
	}

	public void setInvoiceAmountReceivedUsd(Double invoiceAmountReceivedUsd) {
		this.invoiceAmountReceivedUsd = invoiceAmountReceivedUsd;
	}

	public Double getM3() {
		return m3;
	}

	public void setM3(Double m3) {
		this.m3 = m3;
	}

	public Double getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public Integer getFreightPaymentStatus() {
		return freightPaymentStatus;
	}

	public void setFreightPaymentStatus(Integer freightPaymentStatus) {
		this.freightPaymentStatus = freightPaymentStatus;
	}

	public Double getTotalWithoutFreight() {
		this.totalWithoutFreight = AppUtil.ifNullOrEmpty(this.shippingCharge, 0.0);
		this.totalWithoutFreight += AppUtil.ifNullOrEmpty(this.radiationCharge, 0.0);
		this.totalWithoutFreight += AppUtil.ifNullOrEmpty(this.inspectionCharge, 0.0);
		this.totalWithoutFreight += AppUtil.ifNullOrEmpty(this.otherCharges, 0.0);
		return totalWithoutFreight;
	}

	public void setTotalWithoutFreight(Double totalWithoutFreight) {
		this.totalWithoutFreight = totalWithoutFreight;
	}

	public Integer getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(Integer invoiceType) {
		this.invoiceType = invoiceType;
	}

	public Double getZarExchangeRate() {
		return zarExchangeRate;
	}

	public void setZarExchangeRate(Double zarExchangeRate) {
		this.zarExchangeRate = zarExchangeRate;
	}

}
