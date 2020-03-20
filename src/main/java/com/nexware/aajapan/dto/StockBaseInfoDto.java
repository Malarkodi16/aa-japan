package com.nexware.aajapan.dto;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.models.Attachment;
import com.nexware.aajapan.models.AuctionInfo;
import com.nexware.aajapan.models.MLocation;
import com.nexware.aajapan.models.PurchaseInfo;
import com.nexware.aajapan.models.TStock;
import com.nexware.aajapan.utils.AppUtil;

public class StockBaseInfoDto {
	private String stockNo;
	private String chassisNo;
	@JsonFormat(pattern = "yyyy/MM")
	@DateTimeFormat(pattern = "yyyy/MM")
	private Date firstRegDate;
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
	private String purchaseType;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date purchaseDate;
	private Long lotNo;
	private String posNo;
	private Double fob;
	private Double thresholdRange;
	private Double buyingPrice;
	private String engineNo;
	@JsonFormat(pattern = "yyyy")
	@DateTimeFormat(pattern = "yyyy")
	private Date manufactureYear;
	private Double m3;
	private Double length;
	private Double width;
	private Double height;
	private Double weight;
	private Double offerPrice;
	private Double minSellingPriceInDollar;
	private Double exchangeRate;
	private String unit;
	private String manualTypes;
	private Integer transportationCount;
	private Integer reserve;
	private String reserveTextValue;
	private Integer status;
	private String statusTextValue;
	private Integer lcStatus;
	private String lcStatusTextValue;
	private Integer isLocked;
	private String isLockedTextValue;
	private Integer showForSales;
	private String showForSalesTextValue;
	private Integer account;
	private String accountTextValue;
	private Integer isMovable;
	private String isMovableTextValue;
	private Integer isBidding;
	private String isBiddingTextValues;
	private Integer shippingStatus;
	private String shippingStatusTextValue;
	private Integer inspectionStatus;
	private String inspectionStatusTextValue;
	private Integer shipmentType;
	private String shipmentTypeTextValue;
	private Integer isPhotoUploaded;
	private String isPhotoUploadedTextValue;
	private Integer transportationStatus;
	private String transportationStatusTextValue;
	private Integer shippingInstructionStatus;
	private String shippingInstructionStatusTextValue;
	private String lockedBy;
	private String lockedBySalesPersonName;
	private String lastTransportLocation;
	private String lastTransportLocationCustom;

	private StockInfoReservedInfoDto reservedInfo;

	public StockBaseInfoDto() {

	}

	public StockBaseInfoDto(TStock stock, MLocation lastTransportLocation, StockInfoReservedInfoDto reservedInfo) {
		prepareData(stock, lastTransportLocation, reservedInfo);
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

	public String getAuctionRemarks() {
		return auctionRemarks;
	}

	public void setAuctionRemarks(String auctionRemarks) {
		this.auctionRemarks = auctionRemarks;
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

	public String getPurchaseType() {
		return purchaseType;
	}

	public void setPurchaseType(String purchaseType) {
		this.purchaseType = purchaseType;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public Long getLotNo() {
		return lotNo;
	}

	public void setLotNo(Long lotNo) {
		this.lotNo = lotNo;
	}

	public String getPosNo() {
		return posNo;
	}

	public void setPosNo(String posNo) {
		this.posNo = posNo;
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

	public StockInfoReservedInfoDto getReservedInfo() {
		return reservedInfo;
	}

	public void setReservedInfo(StockInfoReservedInfoDto reservedInfo) {
		this.reservedInfo = reservedInfo;
	}

	public Double getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public Integer getReserve() {
		return reserve;
	}

	public void setReserve(Integer reserve) {
		if (!AppUtil.isObjectEmpty(reserve)) {
			if (reserve.equals(Constants.NOT_RESERVED)) {
				reserveTextValue = "NOT RESERVED";
			} else if (reserve.equals(Constants.RESERVED)) {
				reserveTextValue = "RESERVED";
			}
		}
		this.reserve = reserve;
	}

	public String getReserveTextValue() {
		return reserveTextValue;
	}

	public void setReserveTextValue(String reserveTextValue) {
		this.reserveTextValue = reserveTextValue;
	}

	public String getStatusTextValue() {
		return statusTextValue;
	}

	public void setStatusTextValue(String statusTextValue) {
		this.statusTextValue = statusTextValue;
	}

	public Integer getLcStatus() {
		return lcStatus;
	}

	public void setLcStatus(Integer lcStatus) {
		if (!AppUtil.isObjectEmpty(lcStatus)) {
			if (lcStatus.equals(Constants.STOCK_LC_NOT_APPLIED)) {
				lcStatusTextValue = "LC NOT APPLIED";
			} else if (lcStatus.equals(Constants.STOCK_LC_APPLIED)) {
				lcStatusTextValue = "LC APPLIED";
			}
		}
		this.lcStatus = lcStatus;
	}

	public Integer getIsLocked() {
		return isLocked;
	}

	public void setIsLocked(Integer isLocked) {
		if (!AppUtil.isObjectEmpty(isLocked)) {
			if (isLocked.equals(Constants.IS_NOT_LOCKED)) {
				isLockedTextValue = "NOT LOCKED";
			} else if (isLocked.equals(Constants.IS_LOCKED)) {
				isLockedTextValue = "LOCKED";
			}
		}
		this.isLocked = isLocked;
	}

	public String getLockedBy() {
		return lockedBy;
	}

	public void setLockedBy(String lockedBy) {
		this.lockedBy = lockedBy;
	}

	public String getLockedBySalesPersonName() {
		return lockedBySalesPersonName;
	}

	public void setLockedBySalesPersonName(String lockedBySalesPersonName) {
		this.lockedBySalesPersonName = lockedBySalesPersonName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {

		if (!AppUtil.isObjectEmpty(status)) {
			if (status.equals(Constants.STOCK_STATUS_NEW)) {
				this.statusTextValue = "NEW";
			} else if (status.equals(Constants.STOCK_STATUS_PURCHASED_CONFIRMED)) {
				this.statusTextValue = "PURCHASED CONFIRMED";
			} else if (status.equals(Constants.STOCK_STATUS_RE_AUCTION)) {
				this.statusTextValue = "REAUCTION";
			} else if (status.equals(Constants.STOCK_STATUS_SOLD)) {
				this.statusTextValue = "SOLD";
			} else if (status.equals(Constants.STOCK_STATUS_CANCEL)) {
				this.statusTextValue = "CANCELLED";
			}
		}
		this.status = status;
	}

	public Integer getShowForSales() {
		return showForSales;
	}

	public void setShowForSales(Integer showForSales) {
		if (!AppUtil.isObjectEmpty(showForSales)) {
			if (showForSales.equals(Constants.NOT_SHOW_FOR_SALES)) {
				showForSalesTextValue = "NOT SHOWING TO SALES";
			} else if (showForSales.equals(Constants.SHOW_FOR_SALES)) {
				showForSalesTextValue = "SHOWING TO SALES";
			}
		}
		this.showForSales = showForSales;
	}

	public Integer getAccount() {
		return account;
	}

	public void setAccount(Integer account) {
		if (!AppUtil.isObjectEmpty(account)) {
			if (account.equals(Constants.ACCOUNT_AAJ)) {
				accountTextValue = "AAJ";
			} else if (account.equals(Constants.ACCOUNT_SOMO)) {
				accountTextValue = "SOMO";
			}
		}
		this.account = account;
	}

	public Integer getIsMovable() {
		return isMovable;
	}

	public void setIsMovable(Integer isMovable) {
		if (!AppUtil.isObjectEmpty(isMovable)) {
			if (isMovable.equals(0)) {
				isMovableTextValue = "MOVABLE";
			} else if (isMovable.equals(1)) {
				isMovableTextValue = "IMMOVABLE";
			}
		}
		this.isMovable = isMovable;
	}

	public Integer getIsBidding() {
		return isBidding;
	}

	public void setIsBidding(Integer isBidding) {
		if (!AppUtil.isObjectEmpty(isBidding)) {
			if (isBidding.equals(Constants.IS_BIDDING)) {
				isBiddingTextValues = "BIDDING";
			} else if (isBidding.equals(Constants.IS_NOT_BIDDING)) {
				isBiddingTextValues = "STOCK";
			}
		}
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

	public Double getBuyingPrice() {
		return buyingPrice;
	}

	public void setBuyingPrice(Double buyingPrice) {
		this.buyingPrice = buyingPrice;
	}

	public String getEngineNo() {
		return engineNo;
	}

	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}

	public Integer getTransportationStatus() {
		return transportationStatus;
	}

	public void setTransportationStatus(Integer transportationStatus) {
		if (!AppUtil.isObjectEmpty(transportationStatus)) {
			if (transportationStatus.equals(Constants.TRANSPORT_IDLE)) {
				transportationStatusTextValue = "IDLE";
			} else if (transportationStatus.equals(Constants.TRANSPORT_INTRANSIT)) {
				transportationStatusTextValue = "IN TRANSIT";
			} else if (transportationStatus.equals(Constants.TRANSPORT_COMPLETED)) {
				transportationStatusTextValue = "COMPLETED";
			}
		}
		this.transportationStatus = transportationStatus;
	}

	public Integer getTransportationCount() {
		return transportationCount;
	}

	public void setTransportationCount(Integer transportationCount) {
		this.transportationCount = transportationCount;
	}

	public Integer getShippingInstructionStatus() {
		return shippingInstructionStatus;
	}

	public void setShippingInstructionStatus(Integer shippingInstructionStatus) {
		if (!AppUtil.isObjectEmpty(shippingInstructionStatus)) {
			if (shippingInstructionStatus.equals(Constants.STOCK_SHIPPING_INSTRUCTION_STATUS_IDLE)) {
				shippingInstructionStatusTextValue = "SHIPPING INSTRUCTION STATUS IDLE";
			} else if (shippingInstructionStatus.equals(Constants.STOCK_SHIPPING_INSTRUCTION_STATUS_ARRANGED)) {
				shippingInstructionStatusTextValue = "SHIPPING INSTRUCTION GIVEN";
			}
		}
		this.shippingInstructionStatus = shippingInstructionStatus;
	}

	public Integer getShippingStatus() {
		return shippingStatus;
	}

	public void setShippingStatus(Integer shippingStatus) {
		if (!AppUtil.isObjectEmpty(shippingStatus)) {
			if (shippingStatus.equals(Constants.STOCK_SHIPPING_STATUS_IDLE)) {
				this.shippingStatusTextValue = "SHIPPING NOT ARRANGED";
			} else if (shippingStatus.equals(Constants.STOCK_SHIPPING_STATUS_SHIPPINGARRANGED)) {
				this.shippingStatusTextValue = "SHIPPING ARRANGED";
			}
		}
		this.shippingStatus = shippingStatus;
	}

	public Integer getInspectionStatus() {
		return inspectionStatus;
	}

	public void setInspectionStatus(Integer inspectionStatus) {
		if (!AppUtil.isObjectEmpty(inspectionStatus)) {
			if (inspectionStatus.equals(Constants.STOCK_AVAILABLE_FOR_INSPECTION)) {
				inspectionStatusTextValue = "AVAILABLE FOR INSPECTION";
			} else if (inspectionStatus.equals(Constants.STOCK_NOT_AVAILABLE_FOR_INSPECTION)) {
				inspectionStatusTextValue = "NOT AVAILABLE FOR INSPECTION";
			}
		}
		this.inspectionStatus = inspectionStatus;
	}

	public String getManualTypes() {
		return manualTypes;
	}

	public void setManualTypes(String manualTypes) {
		this.manualTypes = manualTypes;
	}

	public Integer getShipmentType() {
		return shipmentType;
	}

	public void setShipmentType(Integer shipmentType) {
		if (!AppUtil.isObjectEmpty(shipmentType)) {
			if (shipmentType.equals(Constants.STOCK_SHIPPING_TYPE_RORO)) {
				shipmentTypeTextValue = "RORO";
			} else if (shipmentType.equals(Constants.STOCK_SHIPPING_TYPE_CONTAINER)) {
				shipmentTypeTextValue = "CONTAINER";
			}
		}
		this.shipmentType = shipmentType;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Date getManufactureYear() {
		return manufactureYear;
	}

	public void setManufactureYear(Date manufactureYear) {
		this.manufactureYear = manufactureYear;
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

	public Double getOfferPrice() {
		return offerPrice;
	}

	public void setOfferPrice(Double offerPrice) {
		this.offerPrice = offerPrice;
	}

	public Double getMinSellingPriceInDollar() {
		return minSellingPriceInDollar;
	}

	public void setMinSellingPriceInDollar(Double minSellingPriceInDollar) {
		this.minSellingPriceInDollar = minSellingPriceInDollar;
	}

	public Integer getIsPhotoUploaded() {
		return isPhotoUploaded;
	}

	public void setIsPhotoUploaded(Integer isPhotoUploaded) {
		if (!AppUtil.isObjectEmpty(isPhotoUploaded)) {
			if (isPhotoUploaded.equals(Constants.STOCK_PHOTOS_UPLOADED)) {
				this.isPhotoUploadedTextValue = "PHOTOS UPLOADED";
			} else if (isPhotoUploaded.equals(Constants.STOCK_PHOTOS_NOT_UPLOADED)) {
				this.isPhotoUploadedTextValue = "PHOTOS NOT UPLOADED";
			}

		}
		this.isPhotoUploaded = isPhotoUploaded;
	}

	public String getLcStatusTextValue() {
		return lcStatusTextValue;
	}

	public void setLcStatusTextValue(String lcStatusTextValue) {
		this.lcStatusTextValue = lcStatusTextValue;
	}

	public String getIsLockedTextValue() {
		return isLockedTextValue;
	}

	public void setIsLockedTextValue(String isLockedTextValue) {
		this.isLockedTextValue = isLockedTextValue;
	}

	public String getShowForSalesTextValue() {
		return showForSalesTextValue;
	}

	public void setShowForSalesTextValue(String showForSalesTextValue) {
		this.showForSalesTextValue = showForSalesTextValue;
	}

	public String getAccountTextValue() {
		return accountTextValue;
	}

	public void setAccountTextValue(String accountTextValue) {
		this.accountTextValue = accountTextValue;
	}

	public String getIsMovableTextValue() {
		return isMovableTextValue;
	}

	public void setIsMovableTextValue(String isMovableTextValue) {
		this.isMovableTextValue = isMovableTextValue;
	}

	public String getIsBiddingTextValues() {
		return isBiddingTextValues;
	}

	public void setIsBiddingTextValues(String isBiddingTextValues) {
		this.isBiddingTextValues = isBiddingTextValues;
	}

	public String getShippingStatusTextValue() {
		return shippingStatusTextValue;
	}

	public void setShippingStatusTextValue(String shippingStatusTextValue) {
		this.shippingStatusTextValue = shippingStatusTextValue;
	}

	public String getInspectionStatusTextValue() {
		return inspectionStatusTextValue;
	}

	public void setInspectionStatusTextValue(String inspectionStatusTextValue) {
		this.inspectionStatusTextValue = inspectionStatusTextValue;
	}

	public String getShipmentTypeTextValue() {
		return shipmentTypeTextValue;
	}

	public void setShipmentTypeTextValue(String shipmentTypeTextValue) {
		this.shipmentTypeTextValue = shipmentTypeTextValue;
	}

	public String getIsPhotoUploadedTextValue() {
		return isPhotoUploadedTextValue;
	}

	public void setIsPhotoUploadedTextValue(String isPhotoUploadedTextValue) {
		this.isPhotoUploadedTextValue = isPhotoUploadedTextValue;
	}

	public String getTransportationStatusTextValue() {
		return transportationStatusTextValue;
	}

	public void setTransportationStatusTextValue(String transportationStatusTextValue) {
		this.transportationStatusTextValue = transportationStatusTextValue;
	}

	public String getShippingInstructionStatusTextValue() {
		return shippingInstructionStatusTextValue;
	}

	public void setShippingInstructionStatusTextValue(String shippingInstructionStatusTextValue) {
		this.shippingInstructionStatusTextValue = shippingInstructionStatusTextValue;
	}

	private void prepareData(TStock stock, MLocation oLastTransportLocation, StockInfoReservedInfoDto reservedInfo) {
		this.setStockNo(stock.getStockNo());
		this.setChassisNo(stock.getChassisNo());
		this.setFirstRegDate(stock.getFirstRegDate());
		this.setModel(stock.getModel());
		this.setCategory(stock.getCategory());
		this.setSubcategory(stock.getSubcategory());
		this.setMaker(stock.getMaker());
		this.setModelType(stock.getModelType());
		this.setExtraEquipments(stock.getExtraEquipments());
		this.setTyreSize(stock.getTyreSize());
		this.setCraneType(stock.getCraneType());
		this.setCraneCut(stock.getCraneCut());
		this.setExel(stock.getExel());
		this.setTankKiloLitre(stock.getTankKiloLitre());
		this.setGrade(stock.getGrade());
		this.setAuctionGrade(stock.getAuctionGrade());
		this.setAuctionGradeExt(stock.getAuctionGradeExt());
		this.setTransmission(stock.getTransmission());
		this.setNoOfDoors(stock.getNoOfDoors());
		this.setNoOfSeat(stock.getNoOfSeat());
		this.setFuel(stock.getFuel());
		this.setDriven(stock.getDriven());
		this.setMileage(stock.getMileage());
		this.setColor(stock.getColor());
		this.setOrgin(stock.getOrgin());
		this.setDestinationCountry(stock.getDestinationCountry());
		this.setDestinationPort(stock.getDestinationPort());
		this.setCc(stock.getCc());
		this.setRecycle(stock.getRecycle());
		this.setNumberPlate(stock.getNumberPlate());
		this.setOldNumberPlate(stock.getOldNumberPlate());
		this.setOptionDescription(stock.getOptionDescription());
		this.setRemarks(stock.getRemarks());
		this.setAuctionRemarks(stock.getAuctionRemarks());
		this.setEquipment(stock.getEquipment());
		this.setExtraAccessories(stock.getExtraAccessories());
		this.setAttachments(stock.getAttachments());

		this.setFob(stock.getFob());
		this.setThresholdRange(stock.getThresholdRange());
		this.setBuyingPrice(stock.getBuyingPrice());
		this.setEngineNo(stock.getEngineNo());
		this.setManufactureYear(stock.getManufactureYear());
		this.setM3(stock.getM3());
		this.setLength(stock.getLength());
		this.setWidth(stock.getWidth());
		this.setHeight(stock.getHeight());
		this.setWeight(stock.getWeight());
		this.setOfferPrice(stock.getOfferPrice());
		this.setMinSellingPriceInDollar(stock.getMinSellingPriceInDollar());
		this.setExchangeRate(stock.getExchangeRate());
		this.setUnit(stock.getUnit());
		this.setManualTypes(stock.getManualTypes());

		PurchaseInfo purchaseInfo = stock.getPurchaseInfo();
		this.setPurchaseType(purchaseInfo.getType());

		AuctionInfo auctionInfo = purchaseInfo.getAuctionInfo();
		if (!AppUtil.isObjectEmpty(auctionInfo)) {
			this.setLotNo(auctionInfo.getLotNo());
			this.setPosNo(auctionInfo.getPosNo());
		}

		this.setReserve(stock.getReserve());
		this.setStatus(stock.getStatus());
		this.setLcStatus(stock.getLcStatus());
		this.setIsLocked(stock.getIsLocked());
		this.setShowForSales(stock.getShowForSales());
		this.setAccount(stock.getAccount());
		this.setIsMovable(stock.getIsMovable());

		this.setIsBidding(stock.getIsBidding());
		this.setShippingStatus(stock.getShippingStatus());
		this.setInspectionStatus(stock.getInspectionStatus());
		this.setShipmentType(stock.getShipmentType());
		this.setIsPhotoUploaded(stock.getIsPhotoUploaded());

		this.setTransportationStatus(stock.getTransportationStatus());
		this.setShippingInstructionStatus(stock.getShippingInstructionStatus());
		this.setLockedBy(stock.getLockedBy());
		this.setLockedBySalesPersonName(stock.getLockedBySalesPersonName());
		if (!AppUtil.isObjectEmpty(oLastTransportLocation)) {
			this.setLastTransportLocation(oLastTransportLocation.getDisplayName());
		}
		this.setLastTransportLocationCustom(stock.getLastTransportLocationCustom());
		this.setReservedInfo(reservedInfo);
	}
}
