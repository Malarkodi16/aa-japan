package com.nexware.aajapan.dto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.models.InspectionDetail;
import com.nexware.aajapan.utils.AppUtil;

/**
 * @author Karthik
 *
 */
public class InspectionDto {

	private String stockNo;
	private String chassisNo;
	private String model;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date purchaseDate;
	private String sPurchaseDate;
	private String destinationCountry;
	private String destinationPort;
	private String lastTransportLocation;
	private String sLastTransportLocation;
	private String lastTransportLocationCustom;
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
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date warningDate;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date expiryDate;
	private Integer lastlapStatus;
	private String voyageNo;
	private String shipName;
	private String shippingCompanyName;
	private String vessalName;
	private Integer shippingStatus;
	private Integer shippingInstructionStatus;
	@JsonFormat(pattern = "MM/yyyy")
	@DateTimeFormat(pattern = "MM/yyyy")
	private Date estimatedDeparture;
	@JsonFormat(pattern = "MM/yyyy")
	@DateTimeFormat(pattern = "MM/yyyy")
	private Date estimatedArrival;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date transportDeliveryDate;
	private Integer transportStatus;
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

	public String getSupplier() {
		return this.supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public List<InspectionDetail> getInspectionDetails() {
		return this.inspectionDetails;
	}

	public void setInspectionDetails(List<InspectionDetail> inspectionDetails) {
		this.inspectionDetails = inspectionDetails;
	}

	public Integer getInspectionStatus() {
		if (!AppUtil.isObjectEmpty(this.inspectionDetails)) {
			this.inspectionStatus = this.inspectionDetails.stream()
					.anyMatch(country -> country.getCountry().equalsIgnoreCase(this.destinationCountry))
							? Constants.INSPECTION_DONE
							: Constants.INSPECTION_NOT_ARRANGED;
		}
		return this.inspectionStatus;
	}

	public void setInspectionStatus(Integer inspectionStatus) {
		this.inspectionStatus = inspectionStatus;
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

	public String getsLastTransportLocation() {
		return this.sLastTransportLocation;
	}

	public void setsLastTransportLocation(String sLastTransportLocation) {
		this.sLastTransportLocation = sLastTransportLocation;
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
		if (!AppUtil.isObjectEmpty(this.etd)) {
			sEtd = new SimpleDateFormat("yyyy-mm-dd").format(this.etd);
		}
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

	public Date getWarningDate() {
		return warningDate;
	}

	public void setWarningDate(Date warningDate) {
		this.warningDate = warningDate;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Integer getLastlapStatus() {
		if (new Date().after(AppUtil.ifNull(expiryDate, new Date()))) {
			lastlapStatus = Constants.LAST_LAP_STATUS_2;
		} else if (new Date().after(AppUtil.ifNull(warningDate, new Date()))
				&& new Date().before(AppUtil.ifNull(expiryDate, new Date()))) {
			lastlapStatus = Constants.LAST_LAP_STATUS_1;
		} else {
			lastlapStatus = Constants.LAST_LAP_STATUS_0;
		}
		return lastlapStatus;
	}

	public void setLastlapStatus(Integer lastlapStatus) {
		this.lastlapStatus = lastlapStatus;
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

	public Integer getShippingInstructionStatus() {
		return shippingInstructionStatus;
	}

	public void setShippingInstructionStatus(Integer shippingInstructionStatus) {
		this.shippingInstructionStatus = shippingInstructionStatus;
	}

	public Date getEstimatedDeparture() {
		return estimatedDeparture;
	}

	public void setEstimatedDeparture(Date estimatedDeparture) {
		this.estimatedDeparture = estimatedDeparture;
	}

	public Date getEstimatedArrival() {
		return estimatedArrival;
	}

	public void setEstimatedArrival(Date estimatedArrival) {
		this.estimatedArrival = estimatedArrival;
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
