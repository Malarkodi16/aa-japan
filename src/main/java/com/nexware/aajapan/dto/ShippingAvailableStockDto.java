
package com.nexware.aajapan.dto;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.models.InspectionDetail;
import com.nexware.aajapan.utils.AppUtil;

public class ShippingAvailableStockDto {

	private String id;
	private String stockNo;
	private String chassisNo;
	@JsonFormat(pattern = "yyyy/MM")
	private Date firstRegDate;
	private String category;
	private String subcategory;
	private String maker;
	private String model;
	private Double length;
	private Double width;
	private Double height;
	private Integer transportationStatus;
	private String destinationCountry;
	private String destinationPort;
	private Integer inspectionStatus;
	private String pickupLocation;
	private String pickupLocationCustom;
	private String dropLocation;
	private String dropLocationCustom;
	private String transporter;
	private String lastTransportLocation;
	private String lastTransportLocationCustom;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date warningDate;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date expiryDate;
	private Integer status;
	private String lotNo;
	private String auctionInfoPosNo;
	private String auctionHouse;
	private String auctionHouseId;
	private List<InspectionDetail> inspectionDetails;
	private Integer destinationCountryInspectionStatus;
	private Double charge;
	// location details look up
	private String currentLocation;
	private String shipmentOriginCountry;
	private String shipmentOriginPort;
	private String forwarder;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date purchaseDate;
	private String shipmentType;
	private List<String> posNos;
	private String transportCategory;
	private Integer inspectionFlag;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date documentConvertedDate;
	private String bookingDetails;
	private String customerFN;
	private String instructedBy;
	private String salesPersonId;

	
	public String getSalesPersonId() {
		return salesPersonId;
	}

	public void setSalesPersonId(String salesPersonId) {
		this.salesPersonId = salesPersonId;
	}

	public String getInstructedBy() {
		return instructedBy;
	}

	public void setInstructedBy(String instructedBy) {
		this.instructedBy = instructedBy;
	}

	public String getCustomerFN() {
		return customerFN;
	}

	public void setCustomerFN(String customerFN) {
		this.customerFN = customerFN;
	}

	public String getBookingDetails() {
		if (!AppUtil.isObjectEmpty(this.customerFN) && !AppUtil.isObjectEmpty(this.instructedBy)) {
			bookingDetails = AppUtil.ifNull(this.customerFN, "") + " - " + AppUtil.ifNull(this.instructedBy, "");
		} else if (!AppUtil.isObjectEmpty(this.customerFN)) {
			bookingDetails = AppUtil.ifNull(this.customerFN, "");
		} else {
			bookingDetails = AppUtil.ifNull(this.instructedBy, "");
		}
		return bookingDetails;
	}

	public void setBookingDetails(String bookingDetails) {
		this.bookingDetails = bookingDetails;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Date getFirstRegDate() {
		return firstRegDate;
	}

	public void setFirstRegDate(Date firstRegDate) {
		this.firstRegDate = firstRegDate;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubcategory() {
		return subcategory;
	}

	public void setSubcategory(String subcategory) {
		this.subcategory = subcategory;
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

	public String getDestinationCountry() {
		return destinationCountry;
	}

	public void setDestinationCountry(String destinationCountry) {
		this.destinationCountry = destinationCountry;
	}

	public String getDestinationPort() {
		return destinationPort;
	}

	public void setDestinationPort(String destinationPort) {
		this.destinationPort = destinationPort;
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

	public Integer getTransportationStatus() {
		return transportationStatus;
	}

	public void setTransportationStatus(Integer transportationStatus) {
		this.transportationStatus = transportationStatus;
	}

	public Integer getInspectionStatus() {
		return inspectionStatus;
	}

	public void setInspectionStatus(Integer inspectionStatus) {
		this.inspectionStatus = inspectionStatus;
	}

	public Integer getDestinationCountryInspectionStatus() {
		if (!AppUtil.isObjectEmpty(inspectionDetails)) {
			destinationCountryInspectionStatus = inspectionDetails.stream().anyMatch(
					country -> country.getCountry().equalsIgnoreCase(destinationCountry)) ? Constants.INSPECTION_DONE
							: Constants.INSPECTION_NOT_ARRANGED;
		}
		return destinationCountryInspectionStatus;
	}

	public void setDestinationCountryInspectionStatus(Integer destinationCountryInspectionStatus) {
		this.destinationCountryInspectionStatus = destinationCountryInspectionStatus;
	}

	public String getPickupLocation() {
		return pickupLocation;
	}

	public void setPickupLocation(String pickupLocation) {
		this.pickupLocation = pickupLocation;
	}

	public String getPickupLocationCustom() {
		return pickupLocationCustom;
	}

	public void setPickupLocationCustom(String pickupLocationCustom) {
		this.pickupLocationCustom = pickupLocationCustom;
	}

	public String getDropLocation() {
		return dropLocation;
	}

	public void setDropLocation(String dropLocation) {
		this.dropLocation = dropLocation;
	}

	public String getDropLocationCustom() {
		return dropLocationCustom;
	}

	public void setDropLocationCustom(String dropLocationCustom) {
		this.dropLocationCustom = dropLocationCustom;
	}

	public String getTransporter() {
		return transporter;
	}

	public void setTransporter(String transporter) {
		this.transporter = transporter;
	}

	public String getLastTransportLocation() {
		return lastTransportLocation;
	}

	public void setLastTransportLocation(String lastTransportLocation) {
		this.lastTransportLocation = lastTransportLocation;
	}

	public String getLastTransportLocationCustom() {
		return lastTransportLocationCustom;
	}

	public void setLastTransportLocationCustom(String lastTransportLocationCustom) {
		this.lastTransportLocationCustom = lastTransportLocationCustom;
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

	public Integer getStatus() {

		if (new Date().after(AppUtil.ifNull(expiryDate, new Date()))) {
			status = Constants.LAST_LAP_STATUS_2;
		} else if (new Date().after(AppUtil.ifNull(warningDate, new Date()))
				&& new Date().before(AppUtil.ifNull(expiryDate, new Date()))) {
			status = Constants.LAST_LAP_STATUS_1;
		} else {
			status = Constants.LAST_LAP_STATUS_0;
		}
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getLotNo() {
		return lotNo;
	}

	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}

	public List<InspectionDetail> getInspectionDetails() {
		return inspectionDetails;
	}

	public void setInspectionDetails(List<InspectionDetail> inspectionDetails) {
		this.inspectionDetails = inspectionDetails;
	}

	public Double getCharge() {
		return charge;
	}

	public void setCharge(Double charge) {
		this.charge = charge;
	}

	public String getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(String currentLocation) {
		this.currentLocation = currentLocation;
	}

	public String getShipmentOriginCountry() {
		return shipmentOriginCountry;
	}

	public void setShipmentOriginCountry(String shipmentOriginCountry) {
		this.shipmentOriginCountry = shipmentOriginCountry;
	}

	public String getShipmentOriginPort() {
		return shipmentOriginPort;
	}

	public void setShipmentOriginPort(String shipmentOriginPort) {
		this.shipmentOriginPort = shipmentOriginPort;
	}

	public String getForwarder() {
		return forwarder;
	}

	public void setForwarder(String forwarder) {
		this.forwarder = forwarder;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public String getShipmentType() {
		return shipmentType;
	}

	public void setShipmentType(String shipmentType) {
		this.shipmentType = shipmentType;
	}

	public String getAuctionInfoPosNo() {
		return auctionInfoPosNo;
	}

	public void setAuctionInfoPosNo(String auctionInfoPosNo) {
		this.auctionInfoPosNo = auctionInfoPosNo;
	}

	public String getAuctionHouse() {
		return auctionHouse;
	}

	public void setAuctionHouse(String auctionHouse) {
		this.auctionHouse = auctionHouse;
	}

	public String getAuctionHouseId() {
		return auctionHouseId;
	}

	public void setAuctionHouseId(String auctionHouseId) {
		this.auctionHouseId = auctionHouseId;
	}

	public List<String> getPosNos() {
		return posNos;
	}

	public void setPosNos(List<String> posNos) {
		this.posNos = posNos;
	}

	public String getTransportCategory() {
		return transportCategory;
	}

	public void setTransportCategory(String transportCategory) {
		this.transportCategory = transportCategory;
	}

	public Integer getInspectionFlag() {
		return inspectionFlag;
	}

	public void setInspectionFlag(Integer inspectionFlag) {
		this.inspectionFlag = inspectionFlag;
	}

	public Date getDocumentConvertedDate() {
		return documentConvertedDate;
	}

	public void setDocumentConvertedDate(Date documentConvertedDate) {
		this.documentConvertedDate = documentConvertedDate;
	}

}
