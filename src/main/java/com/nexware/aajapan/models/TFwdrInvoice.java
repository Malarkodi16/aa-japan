package com.nexware.aajapan.models;

import java.util.Date;
import java.util.List;

import javax.persistence.Transient;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.ColumnDefDto;
import com.nexware.aajapan.listeners.EntityModelBase;
import com.nexware.aajapan.utils.AppUtil;

@Document(collection = "t_frwrdr_invc")
public class TFwdrInvoice extends EntityModelBase {
	@Id
	private String id;
	@Indexed
	private String invoiceNo;// auto genarate
	private String remitter;// refer m_frwrdr
	private String refNo;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date invoiceDate; // @purchase date
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date dueDate;
	private String stockNo;// refer t_stck
	private Double amount;// storageAmount
	private Double storageTax;
	private Double shippingCharges;
	private Double shippingTax;
	private Double photoCharges;
	private Double photoTax;
	private Double blAmendCombineCharges;
	private Double blAmendCombineTax;
	private Double radiationCharges;
	private Double radiationTax;
	private Double repairCharges;
	private Double repairTax;
	private Double yardHandlingCharges;
	private Double yardHandlingTax;
	private Double inspectionCharges;
	private Double inspectionTax;
	private Double transportCharges;
	private Double transportTax;
	private Double freightCharges;
	private Double freightTax;
	private String remarks;
	private Integer status;
	private Integer currency;
	private Integer paymentApprove = 0;
	private ApprovePaymentDetails approvePaymentDetails;
	private String forwarderName;
	private String cancelledRemarks;
	private String invoiceAttachmentFilename;
	private String invoiceAttachmentDiskFilename;
	private String bankStatementAttachmentFilename;
	private String bankStatementAttachmentDiskFilename;
	private Integer invoiceUpload;
	private Integer attachementViewed;
	private Double invoiceAmountReceived;
	private Double totalAmountInclusiveTax;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date approvedDate;
	private String approvedBy;
	@Transient
	private Double sumOfCharges;
	private org.bson.Document chargesList;
	private List<ColumnDefDto> metaData;
	private Double exchangeRate;
	private Double exchangeRateValue;
	private Integer invoiceType;
	private Double totalAmount;
	

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getRemitter() {
		return remitter;
	}

	public void setRemitter(String remitter) {
		this.remitter = remitter;
	}

	public String getRefNo() {
		return refNo;
	}

	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public String getStockNo() {
		return stockNo;
	}

	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getStorageTax() {
		return storageTax;
	}

	public void setStorageTax(Double storageTax) {
		this.storageTax = storageTax;
	}

	public Double getShippingCharges() {
		return shippingCharges;
	}

	public void setShippingCharges(Double shippingCharges) {
		this.shippingCharges = shippingCharges;
	}

	public Double getShippingTax() {
		return shippingTax;
	}

	public void setShippingTax(Double shippingTax) {
		this.shippingTax = shippingTax;
	}

	public Double getPhotoCharges() {
		return photoCharges;
	}

	public void setPhotoCharges(Double photoCharges) {
		this.photoCharges = photoCharges;
	}

	public Double getPhotoTax() {
		return photoTax;
	}

	public void setPhotoTax(Double photoTax) {
		this.photoTax = photoTax;
	}

	public Double getBlAmendCombineCharges() {
		return blAmendCombineCharges;
	}

	public void setBlAmendCombineCharges(Double blAmendCombineCharges) {
		this.blAmendCombineCharges = blAmendCombineCharges;
	}

	public Double getBlAmendCombineTax() {
		return blAmendCombineTax;
	}

	public void setBlAmendCombineTax(Double blAmendCombineTax) {
		this.blAmendCombineTax = blAmendCombineTax;
	}

	public Double getRadiationCharges() {
		return radiationCharges;
	}

	public void setRadiationCharges(Double radiationCharges) {
		this.radiationCharges = radiationCharges;
	}

	public Double getRadiationTax() {
		return radiationTax;
	}

	public void setRadiationTax(Double radiationTax) {
		this.radiationTax = radiationTax;
	}

	public Double getRepairCharges() {
		return repairCharges;
	}

	public void setRepairCharges(Double repairCharges) {
		this.repairCharges = repairCharges;
	}

	public Double getRepairTax() {
		return repairTax;
	}

	public void setRepairTax(Double repairTax) {
		this.repairTax = repairTax;
	}

	public Double getYardHandlingCharges() {
		return yardHandlingCharges;
	}

	public void setYardHandlingCharges(Double yardHandlingCharges) {
		this.yardHandlingCharges = yardHandlingCharges;
	}

	public Double getYardHandlingTax() {
		return yardHandlingTax;
	}

	public void setYardHandlingTax(Double yardHandlingTax) {
		this.yardHandlingTax = yardHandlingTax;
	}

	public Double getInspectionCharges() {
		return inspectionCharges;
	}

	public void setInspectionCharges(Double inspectionCharges) {
		this.inspectionCharges = inspectionCharges;
	}

	public Double getInspectionTax() {
		return inspectionTax;
	}

	public void setInspectionTax(Double inspectionTax) {
		this.inspectionTax = inspectionTax;
	}

	public Double getTransportCharges() {
		return transportCharges;
	}

	public void setTransportCharges(Double transportCharges) {
		this.transportCharges = transportCharges;
	}

	public Double getTransportTax() {
		return transportTax;
	}

	public void setTransportTax(Double transportTax) {
		this.transportTax = transportTax;
	}

	public Double getFreightCharges() {
		return freightCharges;
	}

	public void setFreightCharges(Double freightCharges) {
		this.freightCharges = freightCharges;
	}

	public Double getFreightTax() {
		return freightTax;
	}

	public void setFreightTax(Double freightTax) {
		this.freightTax = freightTax;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getCurrency() {
		return currency;
	}

	public void setCurrency(Integer currency) {
		this.currency = currency;
	}

	public Integer getPaymentApprove() {
		return paymentApprove;
	}

	public void setPaymentApprove(Integer paymentApprove) {
		this.paymentApprove = paymentApprove;
	}

	public ApprovePaymentDetails getApprovePaymentDetails() {
		return approvePaymentDetails;
	}

	public void setApprovePaymentDetails(ApprovePaymentDetails approvePaymentDetails) {
		this.approvePaymentDetails = approvePaymentDetails;
	}

	public String getForwarderName() {
		return forwarderName;
	}

	public void setForwarderName(String forwarderName) {
		this.forwarderName = forwarderName;
	}

	public String getCancelledRemarks() {
		return cancelledRemarks;
	}

	public void setCancelledRemarks(String cancelledRemarks) {
		this.cancelledRemarks = cancelledRemarks;
	}

	public String getInvoiceAttachmentFilename() {
		return invoiceAttachmentFilename;
	}

	public void setInvoiceAttachmentFilename(String invoiceAttachmentFilename) {
		this.invoiceAttachmentFilename = invoiceAttachmentFilename;
	}

	public String getInvoiceAttachmentDiskFilename() {
		return invoiceAttachmentDiskFilename;
	}

	public void setInvoiceAttachmentDiskFilename(String invoiceAttachmentDiskFilename) {
		this.invoiceAttachmentDiskFilename = invoiceAttachmentDiskFilename;
	}

	public String getBankStatementAttachmentFilename() {
		return bankStatementAttachmentFilename;
	}

	public void setBankStatementAttachmentFilename(String bankStatementAttachmentFilename) {
		this.bankStatementAttachmentFilename = bankStatementAttachmentFilename;
	}

	public String getBankStatementAttachmentDiskFilename() {
		return bankStatementAttachmentDiskFilename;
	}

	public void setBankStatementAttachmentDiskFilename(String bankStatementAttachmentDiskFilename) {
		this.bankStatementAttachmentDiskFilename = bankStatementAttachmentDiskFilename;
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

	public Double getInvoiceAmountReceived() {
		return invoiceAmountReceived;
	}

	public void setInvoiceAmountReceived(Double invoiceAmountReceived) {
		this.invoiceAmountReceived = invoiceAmountReceived;
	}

	public Double getTotalAmountInclusiveTax() {
		this.totalAmountInclusiveTax = AppUtil.ifNull(this.invoiceAmountReceived, 0.0)
				+ (AppUtil.ifNull(this.invoiceAmountReceived, 0.0) * (AppUtil.ifNull(Constants.COMMON_TAX, 0.0) / 100));
		return this.totalAmountInclusiveTax;
	}

	public void setTotalAmountInclusiveTax(Double totalAmountInclusiveTax) {
		this.totalAmountInclusiveTax = totalAmountInclusiveTax;
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

	public Double getSumOfCharges() {
		sumOfCharges = AppUtil.ifNull(this.getAmount(), 0.0) + AppUtil.ifNull(this.getStorageTax(), 0.0)
				+ AppUtil.ifNull(this.getShippingCharges(), 0.0) + AppUtil.ifNull(this.getShippingTax(), 0.0)
				+ AppUtil.ifNull(this.getPhotoCharges(), 0.0) + AppUtil.ifNull(this.getPhotoTax(), 0.0)
				+ AppUtil.ifNull(this.getBlAmendCombineCharges(), 0.0)
				+ AppUtil.ifNull(this.getBlAmendCombineTax(), 0.0) + AppUtil.ifNull(this.getRadiationCharges(), 0.0)
				+ AppUtil.ifNull(this.getRadiationTax(), 0.0) + AppUtil.ifNull(this.getRepairCharges(), 0.0)
				+ AppUtil.ifNull(this.getRepairTax(), 0.0) + AppUtil.ifNull(this.getYardHandlingCharges(), 0.0)
				+ AppUtil.ifNull(this.getYardHandlingTax(), 0.0) + AppUtil.ifNull(this.getInspectionCharges(), 0.0)
				+ AppUtil.ifNull(this.getInspectionTax(), 0.0) + AppUtil.ifNull(this.getTransportCharges(), 0.0)
				+ AppUtil.ifNull(this.getTransportTax(), 0.0) + AppUtil.ifNull(this.getFreightCharges(), 0.0)
				+ AppUtil.ifNull(this.getFreightTax(), 0.0);
		return sumOfCharges;
	}

	public void setSumOfCharges(Double sumOfCharges) {
		this.sumOfCharges = sumOfCharges;
	}

	public org.bson.Document getChargesList() {
		return chargesList;
	}

	public void setChargesList(org.bson.Document chargesList) {
		this.chargesList = chargesList;
	}

	public Integer getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(Integer invoiceType) {
		this.invoiceType = invoiceType;
	}

	public Double getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public List<ColumnDefDto> getMetaData() {
		return metaData;
	}

	public void setMetaData(List<ColumnDefDto> metaData) {
		this.metaData = metaData;
	}

	public Double getExchangeRateValue() {
		return exchangeRateValue;
	}

	public void setExchangeRateValue(Double exchangeRateValue) {
		this.exchangeRateValue = exchangeRateValue;
	}

}
