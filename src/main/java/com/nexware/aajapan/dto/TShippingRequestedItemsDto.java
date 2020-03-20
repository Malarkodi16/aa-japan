package com.nexware.aajapan.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.utils.AppUtil;

public class TShippingRequestedItemsDto {
	private String shipmentRequestId;
	private String allocationId;
	private String stockNo;
	private String chassisNo;
	private String maker;
	private String model;
	private String shippingType;
	private String vesselId;
	private String scheduleId;
	private String forwarder;
	private String forwarderId;
	private String orginCountry;
	private String orginPort;
	private String destCountry;
	private String destPort;
	private String yard;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date etd;
	private String sEtd;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date eta;
	private String sEta;
	private Integer status;
	private String customerId;// refer t_cstmr
	private String consigneeId;// refer t_cstmr
	private String notifypartyId;// refer t_cstmr
	private String containerNo;
	private String shipId;
	private String shippingCompanyNo;
	private String shippingCompanyName;
	private String vessel;
	private String voyageNo;
	private String blNo;
	private String remarks;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date purchaseDate;
	private String locationName;
	private String locationId;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date documentConvertedDate;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date inspectionDate;
	private Integer inspectionStatus;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date inspectionDateOfIssue;
	public String getOrginCountry() {
		return orginCountry;
	}

	public void setOrginCountry(String orginCountry) {
		this.orginCountry = orginCountry;
	}

	public String getOrginPort() {
		return orginPort;
	}

	public void setOrginPort(String orginPort) {
		this.orginPort = orginPort;
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

	public String getShipmentRequestId() {
		return shipmentRequestId;
	}

	public void setShipmentRequestId(String shipmentRequestId) {
		this.shipmentRequestId = shipmentRequestId;
	}

	public String getStockNo() {
		return stockNo;
	}

	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}

	public String getChassisNo() {
		return chassisNo;
	}

	public void setChassisNo(String chassisNo) {
		this.chassisNo = chassisNo;
	}

	public String getShippingType() {
		return shippingType;
	}

	public void setShippingType(String shippingType) {
		this.shippingType = shippingType;
	}

	public String getForwarder() {
		return forwarder;
	}

	public void setForwarder(String forwarder) {
		this.forwarder = forwarder;
	}

	public Date getEtd() {
		return etd;
	}

	public void setEtd(Date etd) {
		this.etd = etd;
	}

	public Date getEta() {
		return eta;
	}

	public void setEta(Date eta) {
		this.eta = eta;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getYard() {
		return yard;
	}

	public void setYard(String yard) {
		this.yard = yard;
	}

	public String getsEtd() {
		sEtd = !AppUtil.isObjectEmpty(etd) ? new SimpleDateFormat("dd-MM-yyyy").format(etd) : "";
		return sEtd;
	}

	public void setsEtd(String sEtd) {
		this.sEtd = sEtd;
	}

	public String getsEta() {
		sEta = !AppUtil.isObjectEmpty(eta) ? new SimpleDateFormat("dd-MM-yyyy").format(eta) : "";
		return sEta;
	}

	public void setsEta(String sEta) {
		this.sEta = sEta;
	}

	public String getForwarderId() {
		return forwarderId;
	}

	public void setForwarderId(String forwarderId) {
		this.forwarderId = forwarderId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getConsigneeId() {
		return consigneeId;
	}

	public void setConsigneeId(String consigneeId) {
		this.consigneeId = consigneeId;
	}

	public String getNotifypartyId() {
		return notifypartyId;
	}

	public void setNotifypartyId(String notifypartyId) {
		this.notifypartyId = notifypartyId;
	}

	public String getVesselId() {
		return vesselId;
	}

	public void setVesselId(String vesselId) {
		this.vesselId = vesselId;
	}

	public String getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
	}

	public String getContainerNo() {
		return containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
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

	public String getAllocationId() {
		return allocationId;
	}

	public void setAllocationId(String allocationId) {
		this.allocationId = allocationId;
	}

	public String getShipId() {
		return shipId;
	}

	public void setShipId(String shipId) {
		this.shipId = shipId;
	}

	public String getShippingCompanyNo() {
		return shippingCompanyNo;
	}

	public void setShippingCompanyNo(String shippingCompanyNo) {
		this.shippingCompanyNo = shippingCompanyNo;
	}

	public String getShippingCompanyName() {
		return shippingCompanyName;
	}

	public void setShippingCompanyName(String shippingCompanyName) {
		this.shippingCompanyName = shippingCompanyName;
	}

	public String getVessel() {
		return vessel;
	}

	public void setVessel(String vessel) {
		this.vessel = vessel;
	}

	public String getVoyageNo() {
		return voyageNo;
	}

	public void setVoyageNo(String voyageNo) {
		this.voyageNo = voyageNo;
	}

	public String getBlNo() {
		return blNo;
	}

	public void setBlNo(String blNo) {
		this.blNo = blNo;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
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

	public Date getDocumentConvertedDate() {
		return documentConvertedDate;
	}

	public void setDocumentConvertedDate(Date documentConvertedDate) {
		this.documentConvertedDate = documentConvertedDate;
	}

	

	public Date getInspectionDate() {
		return inspectionDate;
	}

	public void setInspectionDate(Date inspectionDate) {
		this.inspectionDate = inspectionDate;
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

	public Date getInspectionDateOfIssue() {
		return inspectionDateOfIssue;
	}

	public void setInspectionDateOfIssue(Date inspectionDateOfIssue) {
		this.inspectionDateOfIssue = inspectionDateOfIssue;
	}

}
