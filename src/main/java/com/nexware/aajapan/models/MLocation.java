package com.nexware.aajapan.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.nexware.aajapan.listeners.EntityModelBase;
import com.nexware.aajapan.utils.AppUtil;

@Document(collection = "m_lctn")
public class MLocation extends EntityModelBase {
	@Id
	private String id;
	@Indexed(unique = true)
	private String code;
	private String type;
	private String displayName;
	private String address;
	private String phone;
	private String fax;
	private String personInCharge;
	private String supplierCode;
	private ObjectId auctionHouseId;
	@Transient
	private String sAuctionHouseId;
	private Integer deleteFlag;
	private Integer shipmentType;
	private String shipmentOriginCountry;
	private String shipmentOriginPort;
	private String atsukai;
	private String tantousha;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getPersonInCharge() {
		return personInCharge;
	}

	public void setPersonInCharge(String personInCharge) {
		this.personInCharge = personInCharge;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public ObjectId getAuctionHouseId() {
		return auctionHouseId;
	}

	public void setAuctionHouseId(String auctionHouseId) {
		this.auctionHouseId = new ObjectId(auctionHouseId);
	}

	public void setAuctionHouseId(ObjectId auctionHouseId) {
		this.auctionHouseId = auctionHouseId;
	}

	public String getsAuctionHouseId() {
		return AppUtil.isObjectEmpty(auctionHouseId) ? "" : auctionHouseId.toString();
	}

	public void setsAuctionHouseId(String sAuctionHouseId) {
		this.sAuctionHouseId = sAuctionHouseId;
	}

	public Integer getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public Integer getShipmentType() {
		return shipmentType;
	}

	public void setShipmentType(Integer shipmentType) {
		this.shipmentType = shipmentType;
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

	public String getAtsukai() {
		return atsukai;
	}

	public void setAtsukai(String atsukai) {
		this.atsukai = atsukai;
	}

	public String getTantousha() {
		return tantousha;
	}

	public void setTantousha(String tantousha) {
		this.tantousha = tantousha;
	}

}
