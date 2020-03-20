package com.nexware.aajapan.dto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.models.InspectionDetail;
import com.nexware.aajapan.utils.AppUtil;

public class TInspectionInstructionDto {

	private String code;
	private String stockNo;
	private String chassisNo;
	private String maker;
	private String model;
	private String destinationPort;
	private String destinationCountry;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date purchaseDate;
	private String sPurchaseDate;
	private String lastTransportLocation;
	private String sLastTransportLocation;
	private String supplier;
	private List<InspectionDetail> inspectionDetails;
	private Integer inspectionStatus;
	@JsonFormat(pattern = "yyyy/MM")
	private Date firstRegDate;
	private String color;
	private String supplierName;
	private Long lotNo;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date documentReceivedDate;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date etd;
	private String sEtd;
	private String salesPerson;
	private String customerName;
	private String bookingDetails;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date shippingDate;
	private String transporterName;
	private Integer isPhotoUploaded;
	private String voyageNo;
	private String shipName;
	private String shippingCompanyName;
	private String vessalName;
	private Integer shippingStatus;
	@JsonFormat(pattern = "MM/yyyy")
	@DateTimeFormat(pattern = "MM/yyyy")
	private Date estimatedDeparture;
	private Integer shippingInstructionStatus;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date transportDeliveryDate;
	private Integer transportStatus;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public String getDestinationPort() {
		return destinationPort;
	}

	public void setDestinationPort(String destinationPort) {
		this.destinationPort = destinationPort;
	}

	public String getLastTransportLocation() {
		return lastTransportLocation;
	}

	public void setLastTransportLocation(String lastTransportLocation) {
		this.lastTransportLocation = lastTransportLocation;
	}

	public String getsLastTransportLocation() {
		return sLastTransportLocation;
	}

	public void setsLastTransportLocation(String sLastTransportLocation) {
		this.sLastTransportLocation = sLastTransportLocation;
	}

	public Date getPurchaseDate() {
		return this.purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public String getsPurchaseDate() {

		this.sPurchaseDate = AppUtil.isObjectEmpty(this.purchaseDate) ? ""
				: new SimpleDateFormat("dd-MM-yyyy").format(this.purchaseDate);
		return this.sPurchaseDate;
	}

	public void setsPurchaseDate(String sPurchaseDate) {
		this.sPurchaseDate = sPurchaseDate;
	}

	public String getDestinationCountry() {
		return destinationCountry;
	}

	public void setDestinationCountry(String destinationCountry) {
		this.destinationCountry = destinationCountry;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public List<InspectionDetail> getInspectionDetails() {
		return inspectionDetails;
	}

	public void setInspectionDetails(List<InspectionDetail> inspectionDetails) {
		this.inspectionDetails = inspectionDetails;
	}

	public Integer getInspectionStatus() {
		return inspectionStatus;
	}

	public void setInspectionStatus(Integer inspectionStatus) {
		this.inspectionStatus = inspectionStatus;
	}

	public Date getFirstRegDate() {
		return firstRegDate;
	}

	public void setFirstRegDate(Date firstRegDate) {
		this.firstRegDate = firstRegDate;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public Long getLotNo() {
		return lotNo;
	}

	public void setLotNo(Long lotNo) {
		this.lotNo = lotNo;
	}

	public Date getDocumentReceivedDate() {
		return documentReceivedDate;
	}

	public void setDocumentReceivedDate(Date documentReceivedDate) {
		this.documentReceivedDate = documentReceivedDate;
	}

	public Date getEtd() {
		return etd;
	}

	public void setEtd(Date etd) {
		this.etd = etd;
	}

	public String getsEtd() {
		return sEtd;
	}

	public void setsEtd(String sEtd) {
		this.sEtd = sEtd;
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

	public void setBookingDetails(String bookingDetails) {
		this.bookingDetails = bookingDetails;
	}

	public Date getShippingDate() {
		return shippingDate;
	}

	public void setShippingDate(Date shippingDate) {
		this.shippingDate = shippingDate;
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

	public String getShippingCompanyName() {
		return shippingCompanyName;
	}

	public void setShippingCompanyName(String shippingCompanyName) {
		this.shippingCompanyName = shippingCompanyName;
	}

	public String getVessalName() {
		if (!AppUtil.isObjectEmpty(this.shipName)) {
			vessalName = AppUtil.ifNull(this.shipName + " [ " + "", "")
					+ AppUtil.ifNull(this.shippingCompanyName + " ] " + "", "")
					+ AppUtil.ifNull(this.voyageNo + "  ", "");

		}
		return vessalName;
	}

	public void setVessalName(String vessalName) {
		this.vessalName = vessalName;
	}

	public Integer getShippingStatus() {
		return shippingStatus;
	}

	public void setShippingStatus(Integer shippingStatus) {
		this.shippingStatus = shippingStatus;
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

	public Date getTransportDeliveryDate() {
		return transportDeliveryDate;
	}

	public void setTransportDeliveryDate(Date transportDeliveryDate) {
		this.transportDeliveryDate = transportDeliveryDate;
	}

	public Integer getTransportStatus() {
		if(!AppUtil.isObjectEmpty(transportStatus)&&transportStatus.equals(Constants.TRANSPORT_ITEM_DELIVERY_CONFIRMED)) {
			if(!AppUtil.isObjectEmpty(transportDeliveryDate)&&transportDeliveryDate.after(AppUtil.startDateOfMonth(new Date()))) {
				transportStatus=Constants.TRANSPORT_ITEM_INTRANSIT;
			}
		}
		return transportStatus;
	}

	public void setTransportStatus(Integer transportStatus) {
		this.transportStatus = transportStatus;
	}

}
