package com.nexware.aajapan.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.utils.AppUtil;

public class TShippingRoroExcelItemsDto {

	private String stockNo;
	private String shipmentRequestId;
	private String chassisNo;
	private String destCountry;
	private String destPort;
	private String lastLap;
	@JsonFormat(pattern = "dd/MM")
	private Date inspCreatedDate;
	private String inspCompany;
	private String inspectionDate;
	private String maker;
	private String model;
	private String shippingInstructionGivenBy;
	private String sFirstRegDate;
	private Double length;
	private Double width;
	private Double height;
	private String weight;
	private Double m3;
	private String docDetails;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date docSendDate;
	private Integer docOriginalSent;
	private Integer docEmailSent;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date expiryDate;
	@JsonFormat(pattern = "yyyy/MM")
	private Date firstRegDate;
	private String consigneeAddress;
	private String consigneeName;
	private String customerDetails;
	private String notifypartyAddress;
	private String notifypartyName;
	private String notifypartyDetails;
	private Double grossWeight;
	private String locationName;
	private String locationId;
	private String yard;
	private String paymentType;
	private String voyageNo;
	private String shipName;
	private long purchaseValue;
	private Integer inspectionStatus;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date inspectionDateOfIssue;
	
	public String getStockNo() {
		return stockNo;
	}

	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}

	public String getShipmentRequestId() {
		return shipmentRequestId;
	}

	public void setShipmentRequestId(String shipmentRequestId) {
		this.shipmentRequestId = shipmentRequestId;
	}

	public String getChassisNo() {
		return chassisNo;
	}

	public void setChassisNo(String chassisNo) {
		this.chassisNo = chassisNo;
	}

	public String getDestCountry() {
		return destCountry;
	}

	public void setDestCountry(String destCountry) {
		this.destCountry = destCountry;
	}

	public String getDestPort() {
		return destPort;
	}

	public void setDestPort(String destPort) {
		this.destPort = destPort;
	}

	public String getLastLap() {
		if (new Date().after(AppUtil.ifNull(expiryDate, new Date()))) {
			this.lastLap = "LAST LAP";
		}
		return AppUtil.ifNull(this.lastLap, "");
	}

	public void setLastLap(String lastLap) {
		this.lastLap = lastLap;
	}

	public Date getInspCreatedDate() {
		return inspCreatedDate;
	}

	public void setInspCreatedDate(Date inspCreatedDate) {
		this.inspCreatedDate = inspCreatedDate;
	}

	public String getInspCompany() {
		return inspCompany;
	}

	public void setInspCompany(String inspCompany) {
		this.inspCompany = inspCompany;
	}

	public String getInspectionDate() {
		String formattedDate;
		if (!AppUtil.isObjectEmpty(this.getInspCreatedDate())) {
			formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(this.getInspCreatedDate());
		} else {
			formattedDate = "";
		}
		inspectionDate = AppUtil.ifNull(this.inspCompany, "") + " " + formattedDate;
		return inspectionDate;
	}

	public void setInspectionDate(String inspectionDate) {
		this.inspectionDate = inspectionDate;
	}

	public String getMaker() {
		return maker;
	}

	public void setMaker(String maker) {
		this.maker = maker;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getShippingInstructionGivenBy() {
		return shippingInstructionGivenBy;
	}

	public void setShippingInstructionGivenBy(String shippingInstructionGivenBy) {
		this.shippingInstructionGivenBy = shippingInstructionGivenBy;
	}

	public String getsFirstRegDate() {
		return sFirstRegDate;
	}

	public void setsFirstRegDate(String sFirstRegDate) {
		this.sFirstRegDate = sFirstRegDate;
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

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public Double getM3() {
		m3 = AppUtil.ifNull(length, 0.0) * AppUtil.ifNull(width, 0.0) * AppUtil.ifNull(height, 0.0);
		m3 = m3 / 1000000;
		return m3;
	}

	public void setM3(Double m3) {
		this.m3 = m3;
	}

	public Date getDocSendDate() {
		return docSendDate;
	}

	public void setDocSendDate(Date docSendDate) {
		this.docSendDate = docSendDate;
	}

	public Integer getDocOriginalSent() {
		return docOriginalSent;
	}

	public void setDocOriginalSent(Integer docOriginalSent) {
		this.docOriginalSent = docOriginalSent;
	}

	public Integer getDocEmailSent() {
		return docEmailSent;
	}

	public void setDocEmailSent(Integer docEmailSent) {
		this.docEmailSent = docEmailSent;
	}

	public String getDocDetails() {
		String formattedDate;
		if (!AppUtil.isObjectEmpty(this.getDocSendDate())) {
			formattedDate = new SimpleDateFormat("dd/MM").format(this.getDocSendDate());
		} else {
			formattedDate = "";
		}
		String docSent = null;
		if (!AppUtil.isObjectEmpty(this.getDocOriginalSent()) || !AppUtil.isObjectEmpty(this.getDocEmailSent())) {
			if (this.getDocOriginalSent().equals(Constants.EXPORT_CERTIFICATE_DOCUMENT_ORIGINAL_SENT)) {
				docSent = "ORIGINAL";
			} else if (this.getDocEmailSent().equals(Constants.EXPORT_CERTIFICATE_DOCUMENT_EMAIL_SENT)) {
				docSent = "EMAIL";
			}
		}
		docDetails = formattedDate + " " + AppUtil.ifNull(docSent, "");
		return docDetails;
	}

	public void setDocDetails(String docDetails) {
		this.docDetails = docDetails;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Date getFirstRegDate() {
		return firstRegDate;
	}

	public void setFirstRegDate(Date firstRegDate) {
		this.firstRegDate = firstRegDate;
	}

	public String getConsigneeAddress() {
		return consigneeAddress;
	}

	public void setConsigneeAddress(String consigneeAddress) {
		this.consigneeAddress = consigneeAddress;
	}

	public String getConsigneeName() {
		return consigneeName;
	}

	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}

	public String getCustomerDetails() {
		customerDetails = AppUtil.ifNull(this.consigneeName, "") + "\n" + AppUtil.ifNull(this.consigneeAddress, "");
		return customerDetails;
	}

	public void setCustomerDetails(String customerDetails) {
		this.customerDetails = customerDetails;
	}

	public Double getGrossWeight() {
		return grossWeight;
	}

	public void setGrossWeight(Double grossWeight) {
		this.grossWeight = grossWeight;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public String getNotifypartyAddress() {
		return notifypartyAddress;
	}

	public void setNotifypartyAddress(String notifypartyAddress) {
		this.notifypartyAddress = notifypartyAddress;
	}

	public String getNotifypartyName() {
		return notifypartyName;
	}

	public void setNotifypartyName(String notifypartyName) {
		this.notifypartyName = notifypartyName;
	}

	public String getNotifypartyDetails() {
		this.notifypartyDetails = AppUtil.ifNull(this.notifypartyName, "") + "\n" + AppUtil.ifNull(this.notifypartyAddress, "");
		return notifypartyDetails;
	}

	public void setNotifypartyDetails(String notifypartyDetails) {
		this.notifypartyDetails = notifypartyDetails;
		
	}

	public String getYard() {
		return yard;
	}

	public void setYard(String yard) {
		this.yard = yard;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getVoyageNo() {
		return voyageNo;
	}

	public void setVoyageNo(String voyageNo) {
		this.voyageNo = voyageNo;
	}

	public String getShipName() {
		return shipName;
	}

	public void setShipName(String shipName) {
		this.shipName = shipName;
	}

	public Long getPurchaseValue() {
		purchaseValue=Math.round(AppUtil.ifNull(purchaseValue, 0L)+100000);
		return purchaseValue;
	}

	public void setPurchaseValue(Long purchaseValue) {
		this.purchaseValue = purchaseValue;
	}

	public String getInspectionStatus() {
		if(!AppUtil.isObjectEmpty(inspectionStatus)) {
			if(inspectionStatus.equals(Constants.INSPECTION_ORDER_REQUEST_FAILED)) {
				return "FAILED";
			}else if(inspectionStatus.equals(Constants.INSPECTION_ORDER_REQUEST_PASSED)||inspectionStatus.equals(Constants.INSPECTION_ORDER_REQUEST_COMPLETE)) {
				return "PASSED";
			}
		}
		return "";
	}

	public void setInspectionStatus(Integer inspectionStatus) {
		this.inspectionStatus = inspectionStatus;
	}

	public void setPurchaseValue(long purchaseValue) {
		this.purchaseValue = purchaseValue;
	}

	public String getInspectionDateOfIssue() {
		String formattedDate;
		if (!AppUtil.isObjectEmpty(inspectionDateOfIssue)) {
			formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(inspectionDateOfIssue);
		} else {
			formattedDate = "";
		}
		return formattedDate;
	}

	public void setInspectionDateOfIssue(Date inspectionDateOfIssue) {
		this.inspectionDateOfIssue = inspectionDateOfIssue;
	}

}
