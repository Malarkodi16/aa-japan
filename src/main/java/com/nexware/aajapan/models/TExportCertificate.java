package com.nexware.aajapan.models;

import java.util.Date;

import javax.persistence.Transient;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.listeners.EntityModelBase;

@Document(collection = "t_exprt_crtfct")
public class TExportCertificate extends EntityModelBase {
	@Id
	private String id;
	@Indexed(unique = true)
	private String stockNo;
	private String registrationNo1;
	private String registrationNo2;
	private String registrationNo3;
	private String registrationNo4;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date registrationDate;
	@JsonFormat(pattern = "yyyy/MM")
	@DateTimeFormat(pattern = "yyyy/MM")
	private Date firstRegDate;
	private String sFirstRegDate;
	private String makerSerialNo;
	private String trademarkVehicle;
	private String modelType;
	private String engineModel;
	private String classificationOfVehicle;
	private String use;
	private String purpose;
	private String typeOfBody;
	private String fixedNumber;
	private String maximCarry;
	private Double weight;
	private String grossWeight;
	private Double engineCapacity;
	private String unit;
	private String fuel;
	private String specificationNo;
	private String classificationNo;
	private Double length;
	private Double width;
	private Double height;
	private String ffWeight;
	private String frWeight;
	private String rfWeight;
	private String rrWeight;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date exportScheduleDate;
	private String remark;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date convertedDate;
	private String serialNo;
	private String referenceNo;
	private String nameOfOwner;
	private String addressOfOwner;
	private String nameOfUser;
	private String addressOfUser;
	private String localityOfPrincipalOfUse;
	@Transient
	private String lcNo;
	@Transient
	private String lcBank;

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

	public String getRegistrationNo1() {
		return registrationNo1;
	}

	public void setRegistrationNo1(String registrationNo1) {
		this.registrationNo1 = registrationNo1;
	}

	public String getRegistrationNo2() {
		return registrationNo2;
	}

	public void setRegistrationNo2(String registrationNo2) {
		this.registrationNo2 = registrationNo2;
	}

	public String getRegistrationNo3() {
		return registrationNo3;
	}

	public void setRegistrationNo3(String registrationNo3) {
		this.registrationNo3 = registrationNo3;
	}

	public String getRegistrationNo4() {
		return registrationNo4;
	}

	public void setRegistrationNo4(String registrationNo4) {
		this.registrationNo4 = registrationNo4;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public Date getFirstRegDate() {
		return firstRegDate;
	}

	public void setFirstRegDate(Date firstRegDate) {
		this.firstRegDate = firstRegDate;
	}

	public String getMakerSerialNo() {
		return makerSerialNo;
	}

	public void setMakerSerialNo(String makerSerialNo) {
		this.makerSerialNo = makerSerialNo;
	}

	public String getTrademarkVehicle() {
		return trademarkVehicle;
	}

	public void setTrademarkVehicle(String trademarkVehicle) {
		this.trademarkVehicle = trademarkVehicle;
	}

	public String getModelType() {
		return modelType;
	}

	public void setModelType(String modelType) {
		this.modelType = modelType;
	}

	public String getEngineModel() {
		return engineModel;
	}

	public void setEngineModel(String engineModel) {
		this.engineModel = engineModel;
	}

	public String getClassificationOfVehicle() {
		return classificationOfVehicle;
	}

	public void setClassificationOfVehicle(String classificationOfVehicle) {
		this.classificationOfVehicle = classificationOfVehicle;
	}

	public String getUse() {
		return use;
	}

	public void setUse(String use) {
		this.use = use;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getTypeOfBody() {
		return typeOfBody;
	}

	public void setTypeOfBody(String typeOfBody) {
		this.typeOfBody = typeOfBody;
	}

	public String getFixedNumber() {
		return fixedNumber;
	}

	public void setFixedNumber(String fixedNumber) {
		this.fixedNumber = fixedNumber;
	}

	public String getMaximCarry() {
		return maximCarry;
	}

	public void setMaximCarry(String maximCarry) {
		this.maximCarry = maximCarry;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public String getGrossWeight() {
		return grossWeight;
	}

	public void setGrossWeight(String grossWeight) {
		this.grossWeight = grossWeight;
	}

	public Double getEngineCapacity() {
		return engineCapacity;
	}

	public void setEngineCapacity(Double engineCapacity) {
		this.engineCapacity = engineCapacity;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getFuel() {
		return fuel;
	}

	public void setFuel(String fuel) {
		this.fuel = fuel;
	}

	public String getSpecificationNo() {
		return specificationNo;
	}

	public void setSpecificationNo(String specificationNo) {
		this.specificationNo = specificationNo;
	}

	public String getClassificationNo() {
		return classificationNo;
	}

	public void setClassificationNo(String classificationNo) {
		this.classificationNo = classificationNo;
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

	public String getFfWeight() {
		return ffWeight;
	}

	public void setFfWeight(String ffWeight) {
		this.ffWeight = ffWeight;
	}

	public String getFrWeight() {
		return frWeight;
	}

	public void setFrWeight(String frWeight) {
		this.frWeight = frWeight;
	}

	public String getRfWeight() {
		return rfWeight;
	}

	public void setRfWeight(String rfWeight) {
		this.rfWeight = rfWeight;
	}

	public String getRrWeight() {
		return rrWeight;
	}

	public void setRrWeight(String rrWeight) {
		this.rrWeight = rrWeight;
	}

	public Date getExportScheduleDate() {
		return exportScheduleDate;
	}

	public void setExportScheduleDate(Date exportScheduleDate) {
		this.exportScheduleDate = exportScheduleDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getConvertedDate() {
		return convertedDate;
	}

	public void setConvertedDate(Date convertedDate) {
		this.convertedDate = convertedDate;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}

	public String getNameOfOwner() {
		return nameOfOwner;
	}

	public void setNameOfOwner(String nameOfOwner) {
		this.nameOfOwner = nameOfOwner;
	}

	public String getAddressOfOwner() {
		return addressOfOwner;
	}

	public void setAddressOfOwner(String addressOfOwner) {
		this.addressOfOwner = addressOfOwner;
	}

	public String getNameOfUser() {
		return nameOfUser;
	}

	public void setNameOfUser(String nameOfUser) {
		this.nameOfUser = nameOfUser;
	}

	public String getAddressOfUser() {
		return addressOfUser;
	}

	public void setAddressOfUser(String addressOfUser) {
		this.addressOfUser = addressOfUser;
	}

	public String getLocalityOfPrincipalOfUse() {
		return localityOfPrincipalOfUse;
	}

	public void setLocalityOfPrincipalOfUse(String localityOfPrincipalOfUse) {
		this.localityOfPrincipalOfUse = localityOfPrincipalOfUse;
	}

	public String getsFirstRegDate() {
		return sFirstRegDate;
	}

	public void setsFirstRegDate(String sFirstRegDate) {
		this.sFirstRegDate = sFirstRegDate;
	}

	public String getLcNo() {
		return lcNo;
	}

	public void setLcNo(String lcNo) {
		this.lcNo = lcNo;
	}

	public String getLcBank() {
		return lcBank;
	}

	public void setLcBank(String lcBank) {
		this.lcBank = lcBank;
	}

}
