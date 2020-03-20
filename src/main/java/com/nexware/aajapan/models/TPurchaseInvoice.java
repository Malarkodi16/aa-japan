package com.nexware.aajapan.models;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.listeners.EntityModelBase;
import com.nexware.aajapan.utils.AppUtil;

@Document(collection = "t_prchs_invc")
public class TPurchaseInvoice extends EntityModelBase {

	@Id
	private String id;
	@NotBlank
	@Indexed(unique = true)
	private String code;
	@NotBlank
	@Indexed
	private String stockNo;
	@NotBlank
	private String chassisNo;
	@Indexed
	private String invoiceNo;
	private String invoiceName;
	private String invoiceType;
	private String supplierId;
	private ObjectId auctionHouseId;
	private String paymentStatus;
	private String paymentType;
	private String description;
	private String remitTo;
	private Double recycle;
	private Double purchaseCost;
	private Double purchaseCostTax;
	private Double purchaseCostTaxAmount;
	private Double commision;
	private Double commisionTax;
	private Double commisionTaxAmount;
	private Double roadTax;
	private Double otherCharges;
	private Double otherChargesTax;
	private Double othersCostTaxAmount;
	@Transient
	private Double total;
	@Transient
	private Double totalTax;
	@Transient
	private Double totalTaxIncluded;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date invoiceDate;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date dueDate;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date transportDueDate;
	private Integer status;
	@Transient
	private Double totalAMount;
	private Double cancellationCharge;
	private String bank;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date approvedDate;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date carTaxReceivedDate;
	private String remarks;
	private String type;
	private Integer paymentApprove;
//	private ApprovePaymentDetails approvePaymentDetails;
	private Integer recycleClaimStatus;
	private Date recycleClaimAppliedDate;
	private Date recycleClaimReceivedDate;
	private Double recycleClaimReceivedAmount;
	private Double recycleClaimCharge;
	private Double recycleClaimInterest;
	private Integer carTaxClaimStatus;
	private Integer purchaseTaxClaimStatus;
	private Integer commisionTaxClaimStatus;
	private Double carTaxClaimReceivedAmount;
	private String cancelledRemarks;
	private String invoiceAttachmentFilename;
	private Integer invoiceUpload;
	private Integer attachementViewed;
	private String invoiceAttachmentDiskFilename;
	private String bankStatementAttachmentFilename;
	private String bankStatementAttachmentDiskFilename;
	private Double invoiceAmountReceived;
	@Transient
	private Double totalInventoryAmount;
	private String purchaseType;
	private String approvedBy;
	private Integer purchaseCostFlag;
	private Integer CommissionFlag;
	private Integer recycleFlag;
	private Integer roadTaxFlag;
	private String auctionRefNo;

	public Date getInvoiceDate() {
		return this.invoiceDate;
	}

	public Double getTotalAMount() {
		return AppUtil.ifNull(this.recycle, 0.0) + AppUtil.ifNull(this.commision, 0.0)
				+ AppUtil.ifNull(this.roadTax, 0.0) + AppUtil.ifNull(this.purchaseCost, 0.0);
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public void setTotalAMount(Double totalAMount) {
		this.totalAMount = totalAMount;
	}

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

	public Double getPurchaseCost() {
		this.purchaseCost = AppUtil.ifNull(this.purchaseCost, 0.0);
		return this.purchaseCost;
	}

	public void setPurchaseCost(Double purchaseCost) {
		this.purchaseCost = purchaseCost;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Double getCommision() {
		this.commision = AppUtil.ifNull(this.commision, 0.0);
		return this.commision;
	}

	public void setCommision(Double commision) {
		this.commision = commision;
	}

	public Double getRoadTax() {
		this.roadTax = AppUtil.ifNull(this.roadTax, 0.0);
		return this.roadTax;
	}

	public void setRoadTax(Double roadTax) {
		this.roadTax = roadTax;
	}

	public Double getTotalTaxIncluded() {
		Double pT = (AppUtil.ifNull(this.purchaseCost, 0.0) * (AppUtil.ifNull(this.purchaseCostTax, 0.0) / 100));
		Double cT = (AppUtil.ifNull(this.commision, 0.0) * (AppUtil.ifNull(this.commisionTax, 0.0) / 100));
		Double oT = (AppUtil.ifNull(this.otherCharges, 0.0) * (AppUtil.ifNull(this.otherChargesTax, 0.0) / 100));
		Double p = AppUtil.ifNull(this.purchaseCost, 0.0) + AppUtil.ifNull(this.purchaseCostTaxAmount, pT);
		Double c = AppUtil.ifNull(this.commision, 0.0) + AppUtil.ifNull(this.commisionTaxAmount, cT);
		Double o = AppUtil.ifNull(this.otherCharges, 0.0) + AppUtil.ifNull(this.othersCostTaxAmount, oT);
		this.totalTaxIncluded = AppUtil.ifNull(this.recycle, 0.0) + AppUtil.ifNull(this.roadTax, 0.0) + p + c + o;
		return this.totalTaxIncluded;
	}

	public void setTotalTaxIncluded(Double totalTaxIncluded) {
		this.totalTaxIncluded = totalTaxIncluded;
	}

	public Double getOtherCharges() {
		this.otherCharges = AppUtil.ifNull(this.otherCharges, 0.0);
		return this.otherCharges;
	}

	public void setOtherCharges(Double otherCharges) {
		this.otherCharges = otherCharges;
	}

	public Date getDueDate() {
		return this.dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public String getInvoiceName() {
		return this.invoiceName;
	}

	public String getInvoiceType() {
		return this.invoiceType;
	}

	public String getPaymentStatus() {
		return this.paymentStatus;
	}

	public void setInvoiceName(String invoiceName) {
		this.invoiceName = invoiceName;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getBank() {
		return this.bank;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Date getTransportDueDate() {
		return this.transportDueDate;
	}

	public void setTransportDueDate(Date transportDueDate) {
		this.transportDueDate = transportDueDate;
	}

	public Double getRecycle() {
		this.recycle = AppUtil.ifNull(this.recycle, 0.0);
		return this.recycle;
	}

	public void setRecycle(Double recycle) {
		this.recycle = recycle;
	}

	public String getInvoiceNo() {
		return this.invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRemitTo() {
		return this.remitTo;
	}

	public void setRemitTo(String remitTo) {
		this.remitTo = remitTo;
	}

	public String getPaymentType() {
		return this.paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getDescription() {
		return this.description;
	}

	/*
	 * public ApprovePaymentDetails getApprovePaymentDetails() { return
	 * this.approvePaymentDetails; }
	 * 
	 * public void setApprovePaymentDetails(ApprovePaymentDetails
	 * approvePaymentDetails) { this.approvePaymentDetails = approvePaymentDetails;
	 * }
	 */

	public Integer getPaymentApprove() {
		return this.paymentApprove;
	}

	public void setPaymentApprove(Integer paymentApprove) {
		this.paymentApprove = paymentApprove;
	}

	public Double getPurchaseCostTax() {
		this.purchaseCostTax = AppUtil.ifNull(this.purchaseCostTax, 0.0);
		return this.purchaseCostTax;
	}

	public void setPurchaseCostTax(Double purchaseCostTax) {
		this.purchaseCostTax = purchaseCostTax;
	}

	public Double getCommisionTax() {
		return this.commisionTax;
	}

	public void setCommisionTax(Double commisionTax) {
		this.commisionTax = AppUtil.ifNull(this.commisionTax, 0.0);
		this.commisionTax = commisionTax;
	}

	public Double getCancellationCharge() {
		return this.cancellationCharge;
	}

	public void setCancellationCharge(Double cancellationCharge) {
		this.cancellationCharge = cancellationCharge;
	}

	public Integer getRecycleClaimStatus() {
		return this.recycleClaimStatus;
	}

	public void setRecycleClaimStatus(Integer recycleClaimStatus) {
		this.recycleClaimStatus = recycleClaimStatus;
	}

	public Integer getCarTaxClaimStatus() {
		return this.carTaxClaimStatus;
	}

	public void setCarTaxClaimStatus(Integer carTaxClaimStatus) {
		this.carTaxClaimStatus = carTaxClaimStatus;
	}

	public Integer getPurchaseTaxClaimStatus() {
		return this.purchaseTaxClaimStatus;
	}

	public void setPurchaseTaxClaimStatus(Integer purchaseTaxClaimStatus) {
		this.purchaseTaxClaimStatus = purchaseTaxClaimStatus;
	}

	public Integer getCommisionTaxClaimStatus() {
		return this.commisionTaxClaimStatus;
	}

	public void setCommisionTaxClaimStatus(Integer commisionTaxClaimStatus) {
		this.commisionTaxClaimStatus = commisionTaxClaimStatus;
	}

	public Double getRecycleClaimReceivedAmount() {
		return this.recycleClaimReceivedAmount;
	}

	public void setRecycleClaimReceivedAmount(Double recycleClaimReceivedAmount) {
		this.recycleClaimReceivedAmount = recycleClaimReceivedAmount;
	}

	public Double getCarTaxClaimReceivedAmount() {
		return this.carTaxClaimReceivedAmount;
	}

	public void setCarTaxClaimReceivedAmount(Double carTaxClaimReceivedAmount) {
		this.carTaxClaimReceivedAmount = carTaxClaimReceivedAmount;
	}

	public String getSupplierId() {
		return this.supplierId;
	}

	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}

	public ObjectId getAuctionHouseId() {
		return this.auctionHouseId;
	}

	public void setAuctionHouseId(ObjectId auctionHouseId) {
		this.auctionHouseId = auctionHouseId;
	}

	public Date getApprovedDate() {
		return this.approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	public String getCancelledRemarks() {
		return this.cancelledRemarks;
	}

	public void setCancelledRemarks(String cancelledRemarks) {
		this.cancelledRemarks = cancelledRemarks;
	}

	public String getInvoiceAttachmentFilename() {
		return this.invoiceAttachmentFilename;
	}

	public void setInvoiceAttachmentFilename(String invoiceAttachmentFilename) {
		this.invoiceAttachmentFilename = invoiceAttachmentFilename;
	}

	public Integer getInvoiceUpload() {
		return this.invoiceUpload;
	}

	public void setInvoiceUpload(Integer invoiceUpload) {
		this.invoiceUpload = invoiceUpload;
	}

	public Integer getAttachementViewed() {
		return this.attachementViewed;
	}

	public void setAttachementViewed(Integer attachementViewed) {
		this.attachementViewed = attachementViewed;
	}

	public String getInvoiceAttachmentDiskFilename() {
		return this.invoiceAttachmentDiskFilename;
	}

	public void setInvoiceAttachmentDiskFilename(String invoiceAttachmentDiskFilename) {
		this.invoiceAttachmentDiskFilename = invoiceAttachmentDiskFilename;
	}

	public String getBankStatementAttachmentFilename() {
		return this.bankStatementAttachmentFilename;
	}

	public void setBankStatementAttachmentFilename(String bankStatementAttachmentFilename) {
		this.bankStatementAttachmentFilename = bankStatementAttachmentFilename;
	}

	public String getBankStatementAttachmentDiskFilename() {
		return this.bankStatementAttachmentDiskFilename;
	}

	public void setBankStatementAttachmentDiskFilename(String bankStatementAttachmentDiskFilename) {
		this.bankStatementAttachmentDiskFilename = bankStatementAttachmentDiskFilename;
	}

	public Double getPurchaseCostTaxAmount() {
		this.purchaseCostTaxAmount = AppUtil.ifNull(this.purchaseCostTaxAmount, 0.0);
		return this.purchaseCostTaxAmount;
	}

	public void setPurchaseCostTaxAmount(Double purchaseCostTaxAmount) {
		this.purchaseCostTaxAmount = purchaseCostTaxAmount;
	}

	public Double getCommisionTaxAmount() {
//		this.commisionTaxAmount = (AppUtil.ifNull(this.commision, 0.0)
//				* (AppUtil.ifNull(this.commisionTax, 0.0) / 100));
		this.commisionTaxAmount = AppUtil.ifNull(this.commisionTaxAmount, 0.0);
		return this.commisionTaxAmount;
	}

	public void setCommisionTaxAmount(Double commisionTaxAmount) {
		this.commisionTaxAmount = commisionTaxAmount;
	}

	public Double getInvoiceAmountReceived() {
		return this.invoiceAmountReceived;
	}

	public void setInvoiceAmountReceived(Double invoiceAmountReceived) {
		this.invoiceAmountReceived = invoiceAmountReceived;
	}

	public Double getTotalInventoryAmount() {
		Double p = AppUtil.ifNull(this.purchaseCost, 0.0);
		Double c = AppUtil.ifNull(this.commision, 0.0);
		Double pTax = AppUtil.ifNull(this.purchaseCostTaxAmount, 0.0);
		Double cTax = AppUtil.ifNull(this.commisionTaxAmount, 0.0);
		Double OTax = AppUtil.ifNull(this.othersCostTaxAmount, 0.0);
		Double recycle = AppUtil.ifNull(this.recycle, 0.0);
		Double roadTax = AppUtil.ifNull(this.roadTax, 0.0);
		this.totalInventoryAmount = p + c +pTax+cTax+OTax+ recycle+roadTax+AppUtil.ifNull(this.otherCharges, 0.0);
		return this.totalInventoryAmount;
	}

	public void setTotalInventoryAmount(Double totalInventoryAmount) {
		this.totalInventoryAmount = totalInventoryAmount;
	}

	public Date getRecycleClaimAppliedDate() {
		return recycleClaimAppliedDate;
	}

	public void setRecycleClaimAppliedDate(Date recycleClaimAppliedDate) {
		this.recycleClaimAppliedDate = recycleClaimAppliedDate;
	}

	public String getChassisNo() {
		return chassisNo;
	}

	public void setChassisNo(String chassisNo) {
		this.chassisNo = chassisNo;
	}

	public Date getRecycleClaimReceivedDate() {
		return recycleClaimReceivedDate;
	}

	public void setRecycleClaimReceivedDate(Date recycleClaimReceivedDate) {
		this.recycleClaimReceivedDate = recycleClaimReceivedDate;
	}

	public String getPurchaseType() {
		return purchaseType;
	}

	public void setPurchaseType(String purchaseType) {
		this.purchaseType = purchaseType;
	}

	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Integer getPurchaseCostFlag() {
		return purchaseCostFlag;
	}

	public void setPurchaseCostFlag(Integer purchaseCostFlag) {
		this.purchaseCostFlag = purchaseCostFlag;
	}

	public Integer getCommissionFlag() {
		return CommissionFlag;
	}

	public void setCommissionFlag(Integer commissionFlag) {
		CommissionFlag = commissionFlag;
	}

	public Integer getRecycleFlag() {
		return recycleFlag;
	}

	public void setRecycleFlag(Integer recycleFlag) {
		this.recycleFlag = recycleFlag;
	}

	public Integer getRoadTaxFlag() {
		return roadTaxFlag;
	}

	public void setRoadTaxFlag(Integer roadTaxFlag) {
		this.roadTaxFlag = roadTaxFlag;
	}

	public Double getRecycleClaimCharge() {
		return recycleClaimCharge;
	}

	public void setRecycleClaimCharge(Double recycleClaimCharge) {
		this.recycleClaimCharge = recycleClaimCharge;
	}

	public Double getRecycleClaimInterest() {
		return recycleClaimInterest;
	}

	public void setRecycleClaimInterest(Double recycleClaimInterest) {
		this.recycleClaimInterest = recycleClaimInterest;
	}

	public String getAuctionRefNo() {
		return auctionRefNo;
	}

	public void setAuctionRefNo(String auctionRefNo) {
		this.auctionRefNo = auctionRefNo;
	}

	public Double getOtherChargesTax() {
		return otherChargesTax;
	}

	public void setOtherChargesTax(Double otherChargesTax) {
		this.otherChargesTax = otherChargesTax;
	}

	public Double getOthersCostTaxAmount() {
		return othersCostTaxAmount;
	}

	public void setOthersCostTaxAmount(Double othersCostTaxAmount) {
		this.othersCostTaxAmount = othersCostTaxAmount;
	}

	public Date getCarTaxReceivedDate() {
		return carTaxReceivedDate;
	}

	public void setCarTaxReceivedDate(Date carTaxReceivedDate) {
		this.carTaxReceivedDate = carTaxReceivedDate;
	}

	public Double getTotalTax() {
		return totalTax;
	}

	public void setTotalTax(Double totalTax) {
		this.totalTax = totalTax;
	}

}