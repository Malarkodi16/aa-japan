package com.nexware.aajapan.models;

import java.util.Date;
import java.util.List;

import javax.persistence.Embedded;
import javax.validation.constraints.NotBlank;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.listeners.EntityModelBase;
import com.nexware.aajapan.utils.AppUtil;

@Document(collection = "t_stck")
public class TStock extends EntityModelBase {
	@Id
	private String id;
	@NotBlank
	@Indexed(unique = true)
	private String stockNo;
	private String chassisNo;
	@JsonFormat(pattern = "yyyy/MM")
	@DateTimeFormat(pattern = "yyyy/MM")
	private Date firstRegDate;
	private String sFirstRegDate;
	private String model;
	private String category;
	private String subcategory;
	private String maker;
	private String modelType;
	private List<String> extraEquipments;
	private Double tyreSize;
	private String craneType;
	private Integer craneCut;
	private Integer exel;
	private Integer tankKiloLitre;
	// private String subModel
	private String grade;
	private String auctionGrade;
	private String auctionGradeExt;
	private String transmission;
	private Integer noOfDoors;
	private String noOfSeat;
	private String fuel;
	private String driven;
	private Long mileage;
	private String color;
	private String orgin;
	private String destinationCountry;
	private String destinationPort;
	private Double cc;
	private String recycle;
	private String numberPlate;
	private String oldNumberPlate;
	private String optionDescription;
	private String remarks;
	private String auctionRemarks;
	private List<String> equipment;
	private List<String> extraAccessories;
	private List<Attachment> attachments;
	private String purchaseInvoiceCode;
	@Embedded
	private PurchaseInfo purchaseInfo;
	private TransportInfo transportInfo;
	private String lastTransportLocation;
	private String lastTransportLocationCustom;
	private ReservedInfo reservedInfo;
	private Double exchangeRate;
	private Integer reserve;
	private ObjectId salesInvoiceId;
	private Integer lcStatus;
	private Integer isLocked;
	private String lockedBy;
	private String lockedBySalesPersonName;
	private Integer status;
	private Integer showForSales;
	private Integer account;
	private Integer isMovable;
	private Integer isBidding;
	private Double fob;
	private Double thresholdRange;
	private Double buyingPrice;
	private String engineNo;
	private Integer transportationStatus;
	private Integer transportationCount;
	private ShippingInstructionInfo shippingInstructionInfo;
	private Integer shippingInstructionStatus;
	private Integer shippingStatus;
	private Integer inspectionStatus;
	List<InspectionDetail> inspectionDetails;
	private String manualTypes;
	private Integer shipmentType;
	private String unit;
	private Integer documentStatus;
	private Integer documentType;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date documentReceivedDate;
	private Integer documentConvertTo;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date documentConvertedDate;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date plateNoReceivedDate;
	private Integer rikujiStatus;
	private String documentRemarks;
	private Integer handoverTo;
	private String handoverToUserId;// refer
	private Integer inventoryStatus;
	private String shipmentRequestId;
	@JsonFormat(pattern = "yyyy")
	@DateTimeFormat(pattern = "yyyy")
	private Date manufactureYear;
	private Double m3;
	private Double length;
	private Double width;
	private Double height;
	private Double weight;
	private Double documentFob;
	private String transportCategory;
	private String forwarder;
	private String modelId;
	private Double offerPrice;
	// private Double minSellingPriceInYen;// saved as fob
	private Double minSellingPriceInDollar;
	private Double shippingCharge;
	private Double inspectionCharge;
	private Double radiationCharge;
	private Double freightPerM3;
	private Double freightCharge;
	private Double courierCharge;
	private Double profit;
	private Double excludingProfit;
	private Double otherCharge;
	private Double transportCharge;
	private Integer showStock;
	private String shippingUser;
	private String shippingId;
	private String shippingTel;
	private String hsCode;
	private String transportInvoiceNo;
	private Integer isPhotoUploaded; // available inspection
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date mashoCopyReceivedDate;// inspection available
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date takeToRikujiDate; // set as @converted date in exp cert.
	private Integer inspectionFlag;
	private String oldChassisNo;
	private String vehicleRemarks;

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

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getHsCode() {
		return hsCode;
	}

	public void setHsCode(String hsCode) {
		this.hsCode = hsCode;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getMaker() {
		return maker;
	}

	public void setMaker(String maker) {
		this.maker = maker;
	}

	public String getModelType() {
		return modelType;
	}

	public void setModelType(String modelType) {
		this.modelType = modelType;
	}

	public List<String> getExtraEquipments() {
		return extraEquipments;
	}

	public void setExtraEquipments(List<String> extraEquipments) {
		this.extraEquipments = extraEquipments;
	}

	public Double getTyreSize() {
		return tyreSize;
	}

	public void setTyreSize(Double tyreSize) {
		this.tyreSize = tyreSize;
	}

	public String getCraneType() {
		return craneType;
	}

	public void setCraneType(String craneType) {
		this.craneType = craneType;
	}

	public Integer getCraneCut() {
		return craneCut;
	}

	public void setCraneCut(Integer craneCut) {
		this.craneCut = craneCut;
	}

	public Integer getExel() {
		return exel;
	}

	public void setExel(Integer exel) {
		this.exel = exel;
	}

	public Integer getTankKiloLitre() {
		return tankKiloLitre;
	}

	public void setTankKiloLitre(Integer tankKiloLitre) {
		this.tankKiloLitre = tankKiloLitre;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getTransmission() {
		return transmission;
	}

	public void setTransmission(String transmission) {
		this.transmission = transmission;
	}

	public Integer getNoOfDoors() {
		return noOfDoors;
	}

	public void setNoOfDoors(Integer noOfDoors) {
		this.noOfDoors = noOfDoors;
	}

	public String getNoOfSeat() {
		return noOfSeat;
	}

	public void setNoOfSeat(String noOfSeat) {
		this.noOfSeat = noOfSeat;
	}

	public String getFuel() {
		return fuel;
	}

	public void setFuel(String fuel) {
		this.fuel = fuel;
	}

	public String getDriven() {
		return driven;
	}

	public void setDriven(String driven) {
		this.driven = driven;
	}

	public Long getMileage() {
		return mileage;
	}

	public void setMileage(Long mileage) {
		this.mileage = mileage;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getOrgin() {
		return orgin;
	}

	public void setOrgin(String orgin) {
		this.orgin = orgin;
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

	public Double getCc() {
		return cc;
	}

	public void setCc(Double cc) {
		this.cc = cc;
	}

	public String getRecycle() {
		return recycle;
	}

	public void setRecycle(String recycle) {
		this.recycle = recycle;
	}

	public String getNumberPlate() {
		return numberPlate;
	}

	public void setNumberPlate(String numberPlate) {
		this.numberPlate = numberPlate;
	}

	public String getOldNumberPlate() {
		return oldNumberPlate;
	}

	public void setOldNumberPlate(String oldNumberPlate) {
		this.oldNumberPlate = oldNumberPlate;
	}

	public Double getBuyingPrice() {
		return buyingPrice;
	}

	public void setBuyingPrice(Double buyingPrice) {
		this.buyingPrice = buyingPrice;
	}

	public String getOptionDescription() {
		return optionDescription;
	}

	public void setOptionDescription(String optionDescription) {
		this.optionDescription = optionDescription;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public List<String> getEquipment() {
		return equipment;
	}

	public void setEquipment(List<String> equipment) {
		this.equipment = equipment;
	}

	public List<String> getExtraAccessories() {
		return extraAccessories;
	}

	public void setExtraAccessories(List<String> extraAccessories) {
		this.extraAccessories = extraAccessories;
	}

	public List<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}

	public String getPurchaseInvoiceCode() {
		return purchaseInvoiceCode;
	}

	public void setPurchaseInvoiceCode(String purchaseInvoiceCode) {
		this.purchaseInvoiceCode = purchaseInvoiceCode;
	}

	public PurchaseInfo getPurchaseInfo() {
		return purchaseInfo;
	}

	public void setPurchaseInfo(PurchaseInfo purchaseInfo) {
		this.purchaseInfo = purchaseInfo;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public TransportInfo getTransportInfo() {
		return transportInfo;
	}

	public void setTransportInfo(TransportInfo transportInfo) {
		this.transportInfo = transportInfo;
	}

	public ReservedInfo getReservedInfo() {
		return reservedInfo;
	}

	public void setReservedInfo(ReservedInfo reservedInfo) {
		this.reservedInfo = reservedInfo;
	}

	public Integer getIsLocked() {
		return isLocked;
	}

	public void setIsLocked(Integer isLocked) {
		this.isLocked = isLocked;
	}

	public String getLockedBy() {
		return lockedBy;
	}

	public void setLockedBy(String lockedBy) {
		this.lockedBy = lockedBy;
	}

	public Integer getShowForSales() {
		return showForSales;
	}

	public void setShowForSales(Integer showForSales) {
		this.showForSales = showForSales;
	}

	public Integer getAccount() {
		return account;
	}

	public void setAccount(Integer account) {
		this.account = account;
	}

	public Integer getIsMovable() {
		return isMovable;
	}

	public void setIsMovable(Integer isMovable) {
		this.isMovable = isMovable;
	}

	public Integer getReserve() {
		return reserve;
	}

	public void setReserve(Integer reserve) {
		this.reserve = reserve;
	}

	public ObjectId getSalesInvoiceId() {
		return salesInvoiceId;
	}

	public void setSalesInvoiceId(ObjectId slaesInvoiceId) {
		this.salesInvoiceId = slaesInvoiceId;
	}

	public Integer getInspectionStatus() {
		return inspectionStatus;
	}

	public void setInspectionStatus(Integer inspectionStatus) {
		this.inspectionStatus = inspectionStatus;
	}

	public Integer getIsBidding() {
		return isBidding;
	}

	public void setIsBidding(Integer isBidding) {
		this.isBidding = isBidding;
	}

	public Double getFob() {
		return fob;
	}

	public void setFob(Double fob) {
		this.fob = fob;
	}

	public Double getThresholdRange() {
		return thresholdRange;
	}

	public void setThresholdRange(Double thresholdRange) {
		this.thresholdRange = thresholdRange;
	}

	public String getEngineNo() {
		return engineNo;
	}

	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}

	public String getSubcategory() {
		return subcategory;
	}

	public void setSubcategory(String subcategory) {
		this.subcategory = subcategory;
	}

	public Integer getTransportationStatus() {
		return transportationStatus;
	}

	public void setTransportationStatus(Integer transportationStatus) {
		this.transportationStatus = transportationStatus;
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

	public String getAuctionGrade() {
		return auctionGrade;
	}

	public void setAuctionGrade(String auctionGrade) {
		this.auctionGrade = auctionGrade;
	}

	public String getAuctionGradeExt() {
		return auctionGradeExt;
	}

	public void setAuctionGradeExt(String auctionGradeExt) {
		this.auctionGradeExt = auctionGradeExt;
	}

	public ShippingInstructionInfo getShippingInstructionInfo() {
		return shippingInstructionInfo;
	}

	public void setShippingInstructionInfo(ShippingInstructionInfo shippingInstructionInfo) {
		this.shippingInstructionInfo = shippingInstructionInfo;
	}

	public Integer getShippingInstructionStatus() {
		return shippingInstructionStatus;
	}

	public void setShippingInstructionStatus(Integer shippingInstructionStatus) {
		this.shippingInstructionStatus = shippingInstructionStatus;
	}

	public Integer getShippingStatus() {
		return shippingStatus;
	}

	public void setShippingStatus(Integer shippingStatus) {
		this.shippingStatus = shippingStatus;
	}

	public List<InspectionDetail> getInspectionDetails() {
		return inspectionDetails;
	}

	public void setInspectionDetails(List<InspectionDetail> inspectionDetails) {
		this.inspectionDetails = inspectionDetails;
	}

	public String getLockedBySalesPersonName() {
		return lockedBySalesPersonName;
	}

	public void setLockedBySalesPersonName(String lockedBySalesPersonName) {
		this.lockedBySalesPersonName = lockedBySalesPersonName;
	}

	public String getManualTypes() {
		return manualTypes;
	}

	public void setManualTypes(String manualTypes) {
		this.manualTypes = manualTypes;
	}

	public Integer getTransportationCount() {
		return AppUtil.isObjectEmpty(transportationCount) ? 0 : transportationCount;
	}

	public void setTransportationCount(Integer transportationCount) {
		this.transportationCount = transportationCount;
	}

	public String getsFirstRegDate() {
		return sFirstRegDate;
	}

	public void setsFirstRegDate(String sFirstRegDate) {
		// set first reg. date
		this.setFirstRegDate(AppUtil.parseDate(sFirstRegDate.replaceAll("_+", "")));
		this.sFirstRegDate = sFirstRegDate;
	}

	public Integer getDocumentStatus() {
		return documentStatus;
	}

	public void setDocumentStatus(Integer documentStatus) {
		this.documentStatus = documentStatus;
	}

	public Integer getDocumentType() {
		return documentType;
	}

	public void setDocumentType(Integer documentType) {
		this.documentType = documentType;
	}

	public String getAuctionRemarks() {
		return auctionRemarks;
	}

	public void setAuctionRemarks(String auctionRemarks) {
		this.auctionRemarks = auctionRemarks;
	}

	public Date getDocumentReceivedDate() {
		return documentReceivedDate;
	}

	public void setDocumentReceivedDate(Date documentReceivedDate) {
		this.documentReceivedDate = documentReceivedDate;
	}

	public Integer getDocumentConvertTo() {
		return documentConvertTo;
	}

	public void setDocumentConvertTo(Integer documentConvertTo) {
		this.documentConvertTo = documentConvertTo;
	}

	public Date getDocumentConvertedDate() {
		return documentConvertedDate;
	}

	public void setDocumentConvertedDate(Date documentConvertedDate) {
		this.documentConvertedDate = documentConvertedDate;
	}

	public Date getPlateNoReceivedDate() {
		return plateNoReceivedDate;
	}

	public void setPlateNoReceivedDate(Date plateNoReceivedDate) {
		this.plateNoReceivedDate = plateNoReceivedDate;
	}

	public Integer getRikujiStatus() {
		return rikujiStatus;
	}

	public void setRikujiStatus(Integer rikujiStatus) {
		this.rikujiStatus = rikujiStatus;
	}

	public String getDocumentRemarks() {
		return documentRemarks;
	}

	public void setDocumentRemarks(String documentRemarks) {
		this.documentRemarks = documentRemarks;
	}

	public Integer getShipmentType() {
		return shipmentType;
	}

	public void setShipmentType(Integer shipmentType) {
		this.shipmentType = shipmentType;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Integer getHandoverTo() {
		return handoverTo;
	}

	public void setHandoverTo(Integer handoverTo) {
		this.handoverTo = handoverTo;
	}

	public String getHandoverToUserId() {
		return handoverToUserId;
	}

	public void setHandoverToUserId(String handoverToUserId) {
		this.handoverToUserId = handoverToUserId;
	}

	public Integer getInventoryStatus() {
		return inventoryStatus;
	}

	public void setInventoryStatus(Integer inventoryStatus) {
		this.inventoryStatus = inventoryStatus;
	}

	public String getShipmentRequestId() {
		return shipmentRequestId;
	}

	public void setShipmentRequestId(String shipmentRequestId) {
		this.shipmentRequestId = shipmentRequestId;
	}

	public Double getM3() {
		return m3;
	}

	public void setM3(Double m3) {
		this.m3 = m3;
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

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Double getDocumentFob() {
		return documentFob;
	}

	public void setDocumentFob(Double documentFob) {
		this.documentFob = documentFob;
	}

	public Date getManufactureYear() {
		return manufactureYear;
	}

	public void setManufactureYear(Date manufactureYear) {
		this.manufactureYear = manufactureYear;
	}

	public Integer getLcStatus() {
		return lcStatus;
	}

	public void setLcStatus(Integer lcStatus) {
		this.lcStatus = lcStatus;
	}

	public String getForwarder() {
		return forwarder;
	}

	public void setForwarder(String forwarder) {
		this.forwarder = forwarder;
	}

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public Double getOfferPrice() {
		return offerPrice;
	}

	public void setOfferPrice(Double offerPrice) {
		this.offerPrice = offerPrice;
	}

	public Integer getShowStock() {
		return showStock;
	}

	public void setShowStock(Integer showStock) {
		this.showStock = showStock;
	}

	public String getShippingUser() {
		return shippingUser;
	}

	public void setShippingUser(String shippingUser) {
		this.shippingUser = shippingUser;
	}

	public String getShippingId() {
		return shippingId;
	}

	public void setShippingId(String shippingId) {
		this.shippingId = shippingId;
	}

	public String getShippingTel() {
		return shippingTel;
	}

	public void setShippingTel(String shippingTel) {
		this.shippingTel = shippingTel;
	}

	public String getTransportInvoiceNo() {
		return transportInvoiceNo;
	}

	public void setTransportInvoiceNo(String transportInvoiceNo) {
		this.transportInvoiceNo = transportInvoiceNo;
	}

	public Integer getIsPhotoUploaded() {
		return isPhotoUploaded;
	}

	public void setIsPhotoUploaded(Integer isPhotoUploaded) {
		this.isPhotoUploaded = isPhotoUploaded;
	}

	public Date getMashoCopyReceivedDate() {
		return mashoCopyReceivedDate;
	}

	public void setMashoCopyReceivedDate(Date mashoCopyReceivedDate) {
		this.mashoCopyReceivedDate = mashoCopyReceivedDate;
	}

	public Date getTakeToRikujiDate() {
		return takeToRikujiDate;
	}

	public void setTakeToRikujiDate(Date takeToRikujiDate) {
		this.takeToRikujiDate = takeToRikujiDate;
	}

	public Double getMinSellingPriceInDollar() {
		return minSellingPriceInDollar;
	}

	public void setMinSellingPriceInDollar(Double minSellingPriceInDollar) {
		this.minSellingPriceInDollar = minSellingPriceInDollar;
	}

	public Double getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
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

	public Double getFreightPerM3() {
		return freightPerM3;
	}

	public void setFreightPerM3(Double freightPerM3) {
		this.freightPerM3 = freightPerM3;
	}

	public Double getFreightCharge() {
		return freightCharge;
	}

	public void setFreightCharge(Double freightCharge) {
		this.freightCharge = freightCharge;
	}

	public Double getCourierCharge() {
		return courierCharge;
	}

	public void setCourierCharge(Double courierCharge) {
		this.courierCharge = courierCharge;
	}

	public Double getProfit() {
		return profit;
	}

	public void setProfit(Double profit) {
		this.profit = profit;
	}

	public Double getExcludingProfit() {
		return excludingProfit;
	}

	public void setExcludingProfit(Double excludingProfit) {
		this.excludingProfit = excludingProfit;
	}

	public Double getOtherCharge() {
		return otherCharge;
	}

	public void setOtherCharge(Double otherCharge) {
		this.otherCharge = otherCharge;
	}

	public Double getTransportCharge() {
		return transportCharge;
	}

	public void setTransportCharge(Double transportCharge) {
		this.transportCharge = transportCharge;
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

	public String getOldChassisNo() {
		return oldChassisNo;
	}

	public void setOldChassisNo(String oldChassisNo) {
		this.oldChassisNo = oldChassisNo;
	}

	public String getVehicleRemarks() {
		return vehicleRemarks;
	}

	public void setVehicleRemarks(String vehicleRemarks) {
		this.vehicleRemarks = vehicleRemarks;
	}

	public void calcM3() {
		m3 = AppUtil.ifNull(length, 0.0) * AppUtil.ifNull(width, 0.0) * AppUtil.ifNull(height, 0.0);
		m3 = m3 / 1000000;
	}
}
