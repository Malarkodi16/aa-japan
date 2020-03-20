package com.nexware.aajapan.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class CustomReportDto {
	private String chassisNo;
	private String stockNo;
	private String modelType;
	private String category;
	private String maker;
	private String stockModelType;
	private String destinationCountry;
	private String transmission;
	private String fuel;
	private String engineNo;
	private String oldNumberPlate;
	private String lotNo;
	private String status;
	private String destinationPort;
	private String destinationPortCode;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date purchaseDate;
	private String fob;
	private String destinationCountryCode;
	private String referenceNo;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date auctionDate;
	@JsonFormat(pattern = "yyyy")
	private Date firstRegDate;
	private String noOfSeat;
	private String webStockNo;
	private String equipment;
	private String optionDescription;
	private String numberPlate;
	private String noOfDoors;
	private String orgin;
	private String fuelCode;
	private String weight;
	private String recycle;
	private String width;
	private String driven;
	private String cc;
	private String grade;
	private String color;
	private String remarks;
	private String length;
	private String height;
	private String isBidding;
	private String mileage;
	private String ffWeight;
	private String rfWeight;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date regDate;
	private String webMileage;
	private String specificationNo;
	private String usedFor;
	private String classificationOfVehicle;
	private String typeOfBody;
	private String frWeight;
	private String grossWeight;
	private String rrWeight;
	private String supplierName;
	private String classificationNo;
	private String purpose;
	private String shippingStatus;
	private String maximumCarry;
	private String specification;
	private String customerName;
	private String transporterName;
	private String documentType;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date documentReceivedDate;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date plateNoReceivedDate;
	private String offerPrice;
	private String shipmentType;
	private String showStock;
	private String transportCategory;
	private String m3;
	private String reservedSalesPersonName;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date reservedDate;
	private String containerNo;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date blDocIssuedDate;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date blDocReceivedDate;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date blDocDispatchedDate;
	private String orginPort;
	private String trnsprt_invc_amount;
	private String blNo;
	private String recyclepaidAmount;
	private String recycleProcessAmount;
	private String recycleClaimReceivedAmount;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date recycleClaimReceivedDate;
	private Double soldPriceTotal;
	private String roadTax;
	private String purchaseCostTaxAmount;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date dueDate;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date approvedDate;
	private String lcAmount;
	private String lcNo;
	private String freightjpy;
	private String shippingCharge;
	private String inspectionCharge;
	private String radiationCharge;
	private String purchasePriceTotal;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date createdDate;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date convertedDate;
	private String cFirstName;
	private String npFirstName;
	private String vessalName;
	private String etd;
	private String eta;
	private String forwarder;
	private Double purchaseCostAmount;

	public String getChassisNo() {
		return chassisNo;
	}

	public void setChassisNo(String chassisNo) {
		this.chassisNo = chassisNo;
	}

	public String getStockNo() {
		return stockNo;
	}

	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}

	public String getModelType() {
		return modelType;
	}

	public void setModelType(String modelType) {
		this.modelType = modelType;
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

	public String getStockModelType() {
		return stockModelType;
	}

	public void setStockModelType(String stockModelType) {
		this.stockModelType = stockModelType;
	}

	public String getDestinationCountry() {
		return destinationCountry;
	}

	public void setDestinationCountry(String destinationCountry) {
		this.destinationCountry = destinationCountry;
	}

	public String getTransmission() {
		return transmission;
	}

	public void setTransmission(String transmission) {
		this.transmission = transmission;
	}

	public String getFuel() {
		return fuel;
	}

	public void setFuel(String fuel) {
		this.fuel = fuel;
	}

	public String getEngineNo() {
		return engineNo;
	}

	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}

	public String getOldNumberPlate() {
		return oldNumberPlate;
	}

	public void setOldNumberPlate(String oldNumberPlate) {
		this.oldNumberPlate = oldNumberPlate;
	}

	public String getLotNo() {
		return lotNo;
	}

	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDestinationPort() {
		return destinationPort;
	}

	public void setDestinationPort(String destinationPort) {
		this.destinationPort = destinationPort;
	}

	public String getDestinationPortCode() {
		return destinationPortCode;
	}

	public void setDestinationPortCode(String destinationPortCode) {
		this.destinationPortCode = destinationPortCode;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public String getFob() {
		return fob;
	}

	public void setFob(String fob) {
		this.fob = fob;
	}

	public String getDestinationCountryCode() {
		return destinationCountryCode;
	}

	public void setDestinationCountryCode(String destinationCountryCode) {
		this.destinationCountryCode = destinationCountryCode;
	}

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}

	public Date getAuctionDate() {
		return auctionDate;
	}

	public void setAuctionDate(Date auctionDate) {
		this.auctionDate = auctionDate;
	}

	public Date getFirstRegDate() {
		return firstRegDate;
	}

	public void setFirstRegDate(Date firstRegDate) {
		this.firstRegDate = firstRegDate;
	}

	public String getNoOfSeat() {
		return noOfSeat;
	}

	public void setNoOfSeat(String noOfSeat) {
		this.noOfSeat = noOfSeat;
	}

	public String getWebStockNo() {
		return webStockNo;
	}

	public void setWebStockNo(String webStockNo) {
		this.webStockNo = webStockNo;
	}

	public String getEquipment() {
		return equipment;
	}

	public void setEquipment(String equipment) {
		this.equipment = equipment;
	}

	public String getOptionDescription() {
		return optionDescription;
	}

	public void setOptionDescription(String optionDescription) {
		this.optionDescription = optionDescription;
	}

	public String getNumberPlate() {
		return numberPlate;
	}

	public void setNumberPlate(String numberPlate) {
		this.numberPlate = numberPlate;
	}

	public String getNoOfDoors() {
		return noOfDoors;
	}

	public void setNoOfDoors(String noOfDoors) {
		this.noOfDoors = noOfDoors;
	}

	public String getOrgin() {
		return orgin;
	}

	public void setOrgin(String orgin) {
		this.orgin = orgin;
	}

	public String getFuelCode() {
		return fuelCode;
	}

	public void setFuelCode(String fuelCode) {
		this.fuelCode = fuelCode;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getRecycle() {
		return recycle;
	}

	public void setRecycle(String recycle) {
		this.recycle = recycle;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getDriven() {
		return driven;
	}

	public void setDriven(String driven) {
		this.driven = driven;
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getIsBidding() {
		return isBidding;
	}

	public void setIsBidding(String isBidding) {
		this.isBidding = isBidding;
	}

	public String getMileage() {
		return mileage;
	}

	public void setMileage(String mileage) {
		this.mileage = mileage;
	}

	public String getFfWeight() {
		return ffWeight;
	}

	public void setFfWeight(String ffWeight) {
		this.ffWeight = ffWeight;
	}

	public String getRfWeight() {
		return rfWeight;
	}

	public void setRfWeight(String rfWeight) {
		this.rfWeight = rfWeight;
	}

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	public String getWebMileage() {
		return webMileage;
	}

	public void setWebMileage(String webMileage) {
		this.webMileage = webMileage;
	}

	public String getSpecificationNo() {
		return specificationNo;
	}

	public void setSpecificationNo(String specificationNo) {
		this.specificationNo = specificationNo;
	}

	public String getUsedFor() {
		return usedFor;
	}

	public void setUsedFor(String usedFor) {
		this.usedFor = usedFor;
	}

	public String getClassificationOfVehicle() {
		return classificationOfVehicle;
	}

	public void setClassificationOfVehicle(String classificationOfVehicle) {
		this.classificationOfVehicle = classificationOfVehicle;
	}

	public String getTypeOfBody() {
		return typeOfBody;
	}

	public void setTypeOfBody(String typeOfBody) {
		this.typeOfBody = typeOfBody;
	}

	public String getFrWeight() {
		return frWeight;
	}

	public void setFrWeight(String frWeight) {
		this.frWeight = frWeight;
	}

	public String getGrossWeight() {
		return grossWeight;
	}

	public void setGrossWeight(String grossWeight) {
		this.grossWeight = grossWeight;
	}

	public String getRrWeight() {
		return rrWeight;
	}

	public void setRrWeight(String rrWeight) {
		this.rrWeight = rrWeight;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getClassificationNo() {
		return classificationNo;
	}

	public void setClassificationNo(String classificationNo) {
		this.classificationNo = classificationNo;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getShippingStatus() {
		return shippingStatus;
	}

	public void setShippingStatus(String shippingStatus) {
		this.shippingStatus = shippingStatus;
	}

	public String getMaximumCarry() {
		return maximumCarry;
	}

	public void setMaximumCarry(String maximumCarry) {
		this.maximumCarry = maximumCarry;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getTransporterName() {
		return transporterName;
	}

	public void setTransporterName(String transporterName) {
		this.transporterName = transporterName;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public Date getDocumentReceivedDate() {
		return documentReceivedDate;
	}

	public void setDocumentReceivedDate(Date documentReceivedDate) {
		this.documentReceivedDate = documentReceivedDate;
	}

	public Date getPlateNoReceivedDate() {
		return plateNoReceivedDate;
	}

	public void setPlateNoReceivedDate(Date plateNoReceivedDate) {
		this.plateNoReceivedDate = plateNoReceivedDate;
	}

	public String getOfferPrice() {
		return offerPrice;
	}

	public void setOfferPrice(String offerPrice) {
		this.offerPrice = offerPrice;
	}

	public String getShipmentType() {
		return shipmentType;
	}

	public void setShipmentType(String shipmentType) {
		this.shipmentType = shipmentType;
	}

	public String getShowStock() {
		return showStock;
	}

	public void setShowStock(String showStock) {
		this.showStock = showStock;
	}

	public String getTransportCategory() {
		return transportCategory;
	}

	public void setTransportCategory(String transportCategory) {
		this.transportCategory = transportCategory;
	}

	public String getM3() {
		return m3;
	}

	public void setM3(String m3) {
		this.m3 = m3;
	}

	public String getReservedSalesPersonName() {
		return reservedSalesPersonName;
	}

	public void setReservedSalesPersonName(String reservedSalesPersonName) {
		this.reservedSalesPersonName = reservedSalesPersonName;
	}

	public Date getReservedDate() {
		return reservedDate;
	}

	public void setReservedDate(Date reservedDate) {
		this.reservedDate = reservedDate;
	}

	public String getContainerNo() {
		return containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}

	public Date getBlDocIssuedDate() {
		return blDocIssuedDate;
	}

	public void setBlDocIssuedDate(Date blDocIssuedDate) {
		this.blDocIssuedDate = blDocIssuedDate;
	}

	public Date getBlDocReceivedDate() {
		return blDocReceivedDate;
	}

	public void setBlDocReceivedDate(Date blDocReceivedDate) {
		this.blDocReceivedDate = blDocReceivedDate;
	}

	public Date getBlDocDispatchedDate() {
		return blDocDispatchedDate;
	}

	public void setBlDocDispatchedDate(Date blDocDispatchedDate) {
		this.blDocDispatchedDate = blDocDispatchedDate;
	}

	public String getOrginPort() {
		return orginPort;
	}

	public void setOrginPort(String orginPort) {
		this.orginPort = orginPort;
	}

	public String getTrnsprt_invc_amount() {
		return trnsprt_invc_amount;
	}

	public void setTrnsprt_invc_amount(String trnsprt_invc_amount) {
		this.trnsprt_invc_amount = trnsprt_invc_amount;
	}

	public String getBlNo() {
		return blNo;
	}

	public void setBlNo(String blNo) {
		this.blNo = blNo;
	}

	public String getRecyclepaidAmount() {
		return recyclepaidAmount;
	}

	public void setRecyclepaidAmount(String recyclepaidAmount) {
		this.recyclepaidAmount = recyclepaidAmount;
	}

	public String getRecycleProcessAmount() {
		return recycleProcessAmount;
	}

	public void setRecycleProcessAmount(String recycleProcessAmount) {
		this.recycleProcessAmount = recycleProcessAmount;
	}

	public String getRecycleClaimReceivedAmount() {
		return recycleClaimReceivedAmount;
	}

	public void setRecycleClaimReceivedAmount(String recycleClaimReceivedAmount) {
		this.recycleClaimReceivedAmount = recycleClaimReceivedAmount;
	}

	public Date getRecycleClaimReceivedDate() {
		return recycleClaimReceivedDate;
	}

	public void setRecycleClaimReceivedDate(Date recycleClaimReceivedDate) {
		this.recycleClaimReceivedDate = recycleClaimReceivedDate;
	}

	public Double getSoldPriceTotal() {
		return soldPriceTotal;
	}

	public void setSoldPriceTotal(Double soldPriceTotal) {
		this.soldPriceTotal = soldPriceTotal;
	}

	public String getRoadTax() {
		return roadTax;
	}

	public void setRoadTax(String roadTax) {
		this.roadTax = roadTax;
	}

	public String getPurchaseCostTaxAmount() {
		return purchaseCostTaxAmount;
	}

	public void setPurchaseCostTaxAmount(String purchaseCostTaxAmount) {
		this.purchaseCostTaxAmount = purchaseCostTaxAmount;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public Date getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	public String getLcAmount() {
		return lcAmount;
	}

	public void setLcAmount(String lcAmount) {
		this.lcAmount = lcAmount;
	}

	public String getLcNo() {
		return lcNo;
	}

	public void setLcNo(String lcNo) {
		this.lcNo = lcNo;
	}

	public String getFreightjpy() {
		return freightjpy;
	}

	public void setFreightjpy(String freightjpy) {
		this.freightjpy = freightjpy;
	}

	public String getShippingCharge() {
		return shippingCharge;
	}

	public void setShippingCharge(String shippingCharge) {
		this.shippingCharge = shippingCharge;
	}

	public String getInspectionCharge() {
		return inspectionCharge;
	}

	public void setInspectionCharge(String inspectionCharge) {
		this.inspectionCharge = inspectionCharge;
	}

	public String getRadiationCharge() {
		return radiationCharge;
	}

	public void setRadiationCharge(String radiationCharge) {
		this.radiationCharge = radiationCharge;
	}

	public String getPurchasePriceTotal() {
		return purchasePriceTotal;
	}

	public void setPurchasePriceTotal(String purchasePriceTotal) {
		this.purchasePriceTotal = purchasePriceTotal;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getConvertedDate() {
		return convertedDate;
	}

	public void setConvertedDate(Date convertedDate) {
		this.convertedDate = convertedDate;
	}

	public String getcFirstName() {
		return cFirstName;
	}

	public void setcFirstName(String cFirstName) {
		this.cFirstName = cFirstName;
	}

	public String getNpFirstName() {
		return npFirstName;
	}

	public void setNpFirstName(String npFirstName) {
		this.npFirstName = npFirstName;
	}

	public String getVessalName() {
		return vessalName;
	}

	public void setVessalName(String vessalName) {
		this.vessalName = vessalName;
	}

	public String getEtd() {
		return etd;
	}

	public void setEtd(String etd) {
		this.etd = etd;
	}

	public String getEta() {
		return eta;
	}

	public void setEta(String eta) {
		this.eta = eta;
	}

	public String getForwarder() {
		return forwarder;
	}

	public void setForwarder(String forwarder) {
		this.forwarder = forwarder;
	}

	public Double getPurchaseCostAmount() {
		return purchaseCostAmount;
	}

	public void setPurchaseCostAmount(Double purchaseCostAmount) {
		this.purchaseCostAmount = purchaseCostAmount;
	}

}
