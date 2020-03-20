package com.nexware.aajapan.dto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.utils.AppUtil;

public class TStockDto {

	private String id;
	private String stockNo;
	private String chassisNo;
	private String model;
	private String maker;
	private String category;
	private String subcategory;
	private String oldNumberPlate;
	private String destinationCountry;
	private String destinationPort;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date purchaseInfoDate;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date etd;
	private String sPurchaseInfoDate;
	private String prchsInfoSupplier;
	private String prchsInfoSSupplier;
	private Integer transportationCount;

	private String trnsprtInfostatus;
	private String prchsInfoLotNo;
	private String prchsInfoPosNo;
	private String prchsInfoAuctionHouse;
	private String prchsInfoType;
	private String prchsInfoSAuctionHouse;
	private String transporter;
	private String trnsprtInfoPickupLocation;
	private String trnsprtInfoPickupLocationCustom;
	private String trnsprtInfoPickupLocationName;
	private String trnsprtInfoDropLocation;
	private String trnsprtInfoDropLocationCustom;
	private String trnsprtInfoDropLocationName;
	private Integer transportationStatus;
	private String lastTransportLocation;
	private String lastTransportLocationCustom;
	private Double charge;
	private List<String> posNos;
	private String transportCategory;
	private Integer inspectionFlag;

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

	public String getOldNumberPlate() {
		return this.oldNumberPlate;
	}

	public void setOldNumberPlate(String oldNumberPlate) {
		this.oldNumberPlate = oldNumberPlate;
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

	public Date getPurchaseInfoDate() {
		return this.purchaseInfoDate;
	}

	public void setPurchaseInfoDate(Date purchaseInfoDate) {
		this.purchaseInfoDate = purchaseInfoDate;
	}

	public String getTrnsprtInfostatus() {
		if (!AppUtil.isObjectEmpty((trnsprtInfostatus)) && !AppUtil.isObjectEmpty((etd))) {
			if (trnsprtInfostatus.equals("5"))
				trnsprtInfostatus = (etd.after(new Date())) ? "7" : "5";
		}
		return trnsprtInfostatus;
	}

	public void setTrnsprtInfostatus(String trnsprtInfostatus) {
		this.trnsprtInfostatus = trnsprtInfostatus;
	}

	public String getPrchsInfoLotNo() {
		return this.prchsInfoLotNo;
	}

	public void setPrchsInfoLotNo(String prchsInfoLotNo) {
		this.prchsInfoLotNo = prchsInfoLotNo;
	}

	public String getPrchsInfoPosNo() {
		return this.prchsInfoPosNo;
	}

	public void setPrchsInfoPosNo(String prchsInfoPosNo) {
		this.prchsInfoPosNo = prchsInfoPosNo;
	}

	public String getPrchsInfoAuctionHouse() {
		return this.prchsInfoAuctionHouse;
	}

	public void setPrchsInfoAuctionHouse(String prchsInfoAuctionHouse) {
		this.prchsInfoAuctionHouse = prchsInfoAuctionHouse;
	}

	public String getPrchsInfoType() {
		return this.prchsInfoType;
	}

	public void setPrchsInfoType(String prchsInfoType) {
		this.prchsInfoType = prchsInfoType;
	}

	public String getPrchsInfoSupplier() {
		return this.prchsInfoSupplier;
	}

	public void setPrchsInfoSupplier(String prchsInfoSupplier) {
		this.prchsInfoSupplier = prchsInfoSupplier;
	}

	public String getPrchsInfoSSupplier() {
		return this.prchsInfoSSupplier;
	}

	public void setPrchsInfoSSupplier(String prchsInfoSSupplier) {
		this.prchsInfoSSupplier = prchsInfoSSupplier;
	}

	public String getPrchsInfoSAuctionHouse() {
		return this.prchsInfoSAuctionHouse;
	}

	public void setPrchsInfoSAuctionHouse(String prchsInfoSAuctionHouse) {
		this.prchsInfoSAuctionHouse = prchsInfoSAuctionHouse;
	}

	public String getTransporter() {
		return this.transporter;
	}

	public void setTransporter(String transporter) {
		this.transporter = transporter;
	}

	public String getTrnsprtInfoPickupLocation() {
		return this.trnsprtInfoPickupLocation;
	}

	public void setTrnsprtInfoPickupLocation(String trnsprtInfoPickupLocation) {
		this.trnsprtInfoPickupLocation = trnsprtInfoPickupLocation;
	}

	public String getTrnsprtInfoPickupLocationCustom() {
		return this.trnsprtInfoPickupLocationCustom;
	}

	public void setTrnsprtInfoPickupLocationCustom(String trnsprtInfoPickupLocationCustom) {
		this.trnsprtInfoPickupLocationCustom = trnsprtInfoPickupLocationCustom;
	}

	public String getTrnsprtInfoPickupLocationName() {
		return this.trnsprtInfoPickupLocationName;
	}

	public void setTrnsprtInfoPickupLocationName(String trnsprtInfoPickupLocationName) {
		this.trnsprtInfoPickupLocationName = trnsprtInfoPickupLocationName;
	}

	public String getTrnsprtInfoDropLocation() {
		return this.trnsprtInfoDropLocation;
	}

	public void setTrnsprtInfoDropLocation(String trnsprtInfoDropLocation) {
		this.trnsprtInfoDropLocation = trnsprtInfoDropLocation;
	}

	public String getTrnsprtInfoDropLocationCustom() {
		return this.trnsprtInfoDropLocationCustom;
	}

	public void setTrnsprtInfoDropLocationCustom(String trnsprtInfoDropLocationCustom) {
		this.trnsprtInfoDropLocationCustom = trnsprtInfoDropLocationCustom;
	}

	public String getTrnsprtInfoDropLocationName() {
		return this.trnsprtInfoDropLocationName;
	}

	public void setTrnsprtInfoDropLocationName(String trnsprtInfoDropLocationName) {
		this.trnsprtInfoDropLocationName = trnsprtInfoDropLocationName;
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

	public String getsPurchaseInfoDate() {
		this.sPurchaseInfoDate = AppUtil.isObjectEmpty(this.purchaseInfoDate) ? ""
				: new SimpleDateFormat("dd-MM-yyyy").format(this.purchaseInfoDate);
		return this.sPurchaseInfoDate;
	}

	public void setsPurchaseInfoDate(String sPurchaseInfoDate) {
		this.sPurchaseInfoDate = sPurchaseInfoDate;
	}

	public Double getCharge() {
		return this.charge;
	}

	public void setCharge(Double charge) {
		this.charge = charge;
	}

	public Integer getTransportationCount() {
		return this.transportationCount;
	}

	public void setTransportationCount(Integer transportationCount) {
		this.transportationCount = transportationCount;
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

}