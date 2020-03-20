package com.nexware.aajapan.dto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.models.PurchaseInfo;
import com.nexware.aajapan.models.TransportInfo;
import com.nexware.aajapan.utils.AppUtil;

public class TInspectionOrderRequestItemDto {

	private String inspectionId;
	private String inspectionCode;
	private String stockNo;
	private String chassisNo;
	private String model;
	private String maker;
	private String country;
	private Double cc;
	@JsonFormat(pattern = "yyyy/MM")
	@DateTimeFormat(pattern = "yyyy/MM")
	private Date firstRegDate;
	private String sFirstRegDate;
	private Long mileage;
	private String remarks;
	private String destinationCountry;
	private String destinationPort;
	private String forwarderId;
	private String forwarder;
	private String category;
	private String subcategory;
	private Integer status;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date date;
	private TransportInfo transportInfo;
	private String dropLocation;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date documentSentDate;
	private String sDocumentSentDate;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date inspectionSentDate;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date inspectedDate;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date inspectionDate;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date mashoCopyReceivedDate;// inspection available
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date inspectionRcvdDate;
	private String engineNo;
	private String color;
	private List<String> equipments;
	private Integer transportationStatus;
	private String lastTransportLocation;
	private String lastTransportLocationCustom;
	private PurchaseInfo purchaseInfo;
	private Double charge;
	private Integer doumentSentStatus;
	private String cancelRemark;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date dateOfIssue;
	private String certificateNo;
	private Integer inspectionCompanyFlag;
	private String inspectionCompany;
	private String inspectionCompanyId;
	private String sLastTransportLocation;
	private String transporterName;
	private String salesPerson;
	private String customerName;
	private Integer isPhotoUploaded;
	private String bookingDetails;
	private String supplierName;
	@JsonFormat(pattern = "MM/yyyy")
	@DateTimeFormat(pattern = "MM/yyyy")
	private Date estimatedDeparture;
	private Integer shippingInstructionStatus;

	public String getInspectionId() {
		return this.inspectionId;
	}

	public void setInspectionId(String inspectionId) {
		this.inspectionId = inspectionId;
	}

	public String getInspectionCode() {
		return inspectionCode;
	}

	public void setInspectionCode(String inspectionCode) {
		this.inspectionCode = inspectionCode;
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

	public String getModel() {
		return this.model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getMaker() {
		return this.maker;
	}

	public void setMaker(String maker) {
		this.maker = maker;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getDestinationCountry() {
		return this.destinationCountry;
	}

	public void setDestinationCountry(String destinationCountry) {
		this.destinationCountry = destinationCountry;
	}

	public String getDestinationPort() {
		return this.destinationPort;
	}

	public void setDestinationPort(String destinationPort) {
		this.destinationPort = destinationPort;
	}

	public String getForwarder() {
		return this.forwarder;
	}

	public void setForwarder(String forwarder) {
		this.forwarder = forwarder;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubcategory() {
		return this.subcategory;
	}

	public void setSubcategory(String subcategory) {
		this.subcategory = subcategory;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public TransportInfo getTransportInfo() {
		return this.transportInfo;
	}

	public void setTransportInfo(TransportInfo transportInfo) {
		this.transportInfo = transportInfo;
	}

	public String getDropLocation() {
		return this.dropLocation;
	}

	public void setDropLocation(String dropLocation) {
		this.dropLocation = dropLocation;
	}

	public Date getDocumentSentDate() {
		return this.documentSentDate;
	}

	public void setDocumentSentDate(Date documentSentDate) {
		this.documentSentDate = documentSentDate;
	}

	public String getsDocumentSentDate() {
		this.sDocumentSentDate = AppUtil.isObjectEmpty(this.documentSentDate) ? ""
				: new SimpleDateFormat("dd-MM-yyyy").format(this.documentSentDate);
		return this.sDocumentSentDate;
	}

	public void setsDocumentSentDate(String sDocumentSentDate) {
		this.sDocumentSentDate = sDocumentSentDate;
	}

	public Date getInspectionSentDate() {
		return this.inspectionSentDate;
	}

	public void setInspectionSentDate(Date inspectionSentDate) {
		this.inspectionSentDate = inspectionSentDate;
	}

	public Date getInspectedDate() {
		return this.inspectedDate;
	}

	public void setInspectedDate(Date inspectedDate) {
		this.inspectedDate = inspectedDate;
	}

	public Date getInspectionRcvdDate() {
		return this.inspectionRcvdDate;
	}

	public void setInspectionRcvdDate(Date inspectionRcvdDate) {
		this.inspectionRcvdDate = inspectionRcvdDate;
	}

	public String getEngineNo() {
		return this.engineNo;
	}

	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}

	public String getColor() {
		return this.color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public List<String> getEquipments() {
		return this.equipments;
	}

	public void setEquipments(List<String> equipments) {
		this.equipments = equipments;
	}

	public Integer getTransportationStatus() {
		return this.transportationStatus;
	}

	public void setTransportationStatus(Integer transportationStatus) {
		this.transportationStatus = transportationStatus;
	}

	public String getLastTransportLocation() {
		return this.lastTransportLocation;
	}

	public void setLastTransportLocation(String lastTransportLocation) {
		this.lastTransportLocation = lastTransportLocation;
	}

	public String getLastTransportLocationCustom() {
		return this.lastTransportLocationCustom;
	}

	public void setLastTransportLocationCustom(String lastTransportLocationCustom) {
		this.lastTransportLocationCustom = lastTransportLocationCustom;
	}

	public PurchaseInfo getPurchaseInfo() {
		return this.purchaseInfo;
	}

	public void setPurchaseInfo(PurchaseInfo purchaseInfo) {
		this.purchaseInfo = purchaseInfo;
	}

	public Double getCharge() {
		return this.charge;
	}

	public void setCharge(Double charge) {
		this.charge = charge;
	}

	public Integer getDoumentSentStatus() {
		return this.doumentSentStatus;
	}

	public void setDoumentSentStatus(Integer doumentSentStatus) {
		this.doumentSentStatus = doumentSentStatus;
	}

	public String getCancelRemark() {
		return this.cancelRemark;
	}

	public void setCancelRemark(String cancelRemark) {
		this.cancelRemark = cancelRemark;
	}

	public Double getCc() {
		return this.cc;
	}

	public void setCc(Double cc) {
		this.cc = cc;
	}

	public Date getFirstRegDate() {
		return this.firstRegDate;
	}

	public void setFirstRegDate(Date firstRegDate) {
		this.firstRegDate = firstRegDate;
	}

	public String getsFirstRegDate() {
		return this.sFirstRegDate;
	}

	public void setsFirstRegDate(String sFirstRegDate) {
		this.sFirstRegDate = sFirstRegDate;
	}

	public Long getMileage() {
		return this.mileage;
	}

	public void setMileage(Long mileage) {
		this.mileage = mileage;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Date getDateOfIssue() {
		return this.dateOfIssue;
	}

	public void setDateOfIssue(Date dateOfIssue) {
		this.dateOfIssue = dateOfIssue;
	}

	public String getCertificateNo() {
		return this.certificateNo;
	}

	public void setCertificateNo(String certificateNo) {
		this.certificateNo = certificateNo;
	}

	public Integer getInspectionCompanyFlag() {
		return this.inspectionCompanyFlag;
	}

	public void setInspectionCompanyFlag(Integer inspectionCompanyFlag) {
		this.inspectionCompanyFlag = inspectionCompanyFlag;
	}

	public String getInspectionCompany() {
		return this.inspectionCompany;
	}

	public void setInspectionCompany(String inspectionCompany) {
		this.inspectionCompany = inspectionCompany;
	}

	public String getForwarderId() {
		return this.forwarderId;
	}

	public void setForwarderId(String forwarderId) {
		this.forwarderId = forwarderId;
	}

	public String getInspectionCompanyId() {
		return this.inspectionCompanyId;
	}

	public void setInspectionCompanyId(String inspectionCompanyId) {
		this.inspectionCompanyId = inspectionCompanyId;
	}

	public String getsLastTransportLocation() {
		return sLastTransportLocation;
	}

	public void setsLastTransportLocation(String sLastTransportLocation) {
		this.sLastTransportLocation = sLastTransportLocation;
	}

	public String getTransporterName() {
		return transporterName;
	}

	public void setTransporterName(String transporterName) {
		this.transporterName = transporterName;
	}

	public Integer getIsPhotoUploaded() {
		return isPhotoUploaded;
	}

	public void setIsPhotoUploaded(Integer isPhotoUploaded) {
		this.isPhotoUploaded = isPhotoUploaded;
	}

	public String getBookingDetails() {
		if (!AppUtil.isObjectEmpty(this.customerName) && !AppUtil.isObjectEmpty(this.salesPerson)) {
			bookingDetails = AppUtil.ifNull(this.customerName, "") + " - " + AppUtil.ifNull(this.salesPerson, "");
		} else if (!AppUtil.isObjectEmpty(this.customerName)) {
			bookingDetails = AppUtil.ifNull(this.customerName, "");
		} else {
			bookingDetails = AppUtil.ifNull(this.salesPerson, "");
		}

		return bookingDetails;
	}

	public String getSalesPerson() {
		return salesPerson;
	}

	public void setSalesPerson(String salesPerson) {
		this.salesPerson = salesPerson;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public void setBookingDetails(String bookingDetails) {
		this.bookingDetails = bookingDetails;
	}

	public Date getEstimatedDeparture() {
		return estimatedDeparture;
	}

	public void setEstimatedDeparture(Date estimatedDeparture) {
		this.estimatedDeparture = estimatedDeparture;
	}

	public Integer getShippingInstructionStatus() {
		return shippingInstructionStatus;
	}

	public void setShippingInstructionStatus(Integer shippingInstructionStatus) {
		this.shippingInstructionStatus = shippingInstructionStatus;
	}

	public Date getMashoCopyReceivedDate() {
		return mashoCopyReceivedDate;
	}

	public void setMashoCopyReceivedDate(Date mashoCopyReceivedDate) {
		this.mashoCopyReceivedDate = mashoCopyReceivedDate;
	}

	public Date getInspectionDate() {
		return inspectionDate;
	}

	public void setInspectionDate(Date inspectionDate) {
		this.inspectionDate = inspectionDate;
	}

	
	

}