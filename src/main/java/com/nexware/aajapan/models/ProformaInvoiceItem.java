package com.nexware.aajapan.models;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.utils.AppUtil;

public class ProformaInvoiceItem {

	private String invoiceNo;
	private String stockNo;
	private String chassisNo;
	private String maker;
	private String transmission;
	private String fuel;
	private String driven;
	private Double cc;
	private String type;
	private String model;
	private String hsCode;
	@JsonFormat(pattern = "yyyy/MM")
	@DateTimeFormat(pattern = "yyyy/MM")
	private Date firstRegDate;
	private Double fob;
	private Double insurance;
	private Double freight;
	private Double shipping;// @inspection
	private Double total;
	private Integer lcStatus;
	private Integer reserve;
	private Integer status;
	private String lcCustomerId;
	private String lcCustFirstName;
	private String lcCustLastName;
	private String lcCustNickName;
	private ObjectId consigneeId;
	private String consigneeName;
	private String consigneeAddress;
	private ObjectId notifypartyId;
	private String notifypartyName;
	private String notifypartyAddress;
	private Double lcAmount;
	private String lcNo;
	private String formattedDetails;
	private ReservedInfo reservedInfo;
	private List<ConsigneeNotifyparty> consigneeNotifyparties;
	private String destCountry;
	private String destPort;
	private String scheduleId;
	private String shipmentRequestId;
	private String currencySymbol;
	private Integer shippingInstructionStatus;
	private Date etd;
	private String shipName;
	private String inspectionCertificateNo;
	private String exportCertificateNo;
	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
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

	public String getMaker() {
		return this.maker;
	}

	public void setMaker(String maker) {
		this.maker = maker;
	}

	public String getModel() {
		return this.model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getHsCode() {
		return this.hsCode;
	}

	public void setHsCode(String hsCode) {
		this.hsCode = hsCode;
	}

	public Date getFirstRegDate() {
		return this.firstRegDate;
	}

	public void setFirstRegDate(Date firstRegDate) {
		this.firstRegDate = firstRegDate;
	}

	public Double getFob() {
		return this.fob;
	}

	public void setFob(Double fob) {
		this.fob = fob;
	}

	public Double getInsurance() {
		return this.insurance;
	}

	public void setInsurance(Double insurance) {
		this.insurance = insurance;
	}

	public Double getFreight() {
		return this.freight;
	}

	public void setFreight(Double freight) {
		this.freight = freight;
	}

	public Double getTotal() {
		this.total = (AppUtil.ifNull(this.fob, 0.0) + AppUtil.ifNull(this.insurance, 0.0)
				+ AppUtil.ifNull(this.shipping, 0.0) + AppUtil.ifNull(this.freight, 0.0));
		return this.total;

	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Integer getReserve() {
		return this.reserve;
	}

	public void setReserve(Integer reserve) {
		this.reserve = reserve;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getLcNo() {
		return this.lcNo;
	}

	public void setLcNo(String lcNo) {
		this.lcNo = lcNo;
	}

	public String getLcCustomerId() {
		return this.lcCustomerId;
	}

	public void setLcCustomerId(String lcCustomerId) {
		this.lcCustomerId = lcCustomerId;
	}

	public String getLcCustFirstName() {
		return this.lcCustFirstName;
	}

	public void setLcCustFirstName(String lcCustFirstName) {
		this.lcCustFirstName = lcCustFirstName;
	}

	public String getLcCustLastName() {
		return this.lcCustLastName;
	}

	public void setLcCustLastName(String lcCustLastName) {
		this.lcCustLastName = lcCustLastName;
	}

	public Double getLcAmount() {
		return this.lcAmount;
	}

	public void setLcAmount(Double lcAmount) {
		this.lcAmount = lcAmount;
	}

	public Double getShipping() {
		return this.shipping;
	}

	public void setShipping(Double shipping) {
		this.shipping = shipping;
	}

	public String getTransmission() {
		return this.transmission;
	}

	public void setTransmission(String transmission) {
		this.transmission = transmission;
	}

	public String getFuel() {
		return this.fuel;
	}

	public void setFuel(String fuel) {
		this.fuel = fuel;
	}

	public String getDriven() {
		return this.driven;
	}

	public void setDriven(String driven) {
		this.driven = driven;
	}

	public Double getCc() {
		return this.cc;
	}

	public void setCc(Double cc) {
		this.cc = cc;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFormattedDetails() {
		this.formattedDetails = this.stockNo + "," + this.chassisNo + "," + this.maker + "," + this.model + ","
				+ this.transmission + "," + this.fuel + "," + this.driven + "," + this.cc + "," + this.type;

		return this.formattedDetails;
	}

	public void setFormattedDetails(String formattedDetails) {
		this.formattedDetails = formattedDetails;
	}

	public ReservedInfo getReservedInfo() {
		return this.reservedInfo;
	}

	public void setReservedInfo(ReservedInfo reservedInfo) {
		this.reservedInfo = reservedInfo;
	}

	public Integer getLcStatus() {
		return this.lcStatus;
	}

	public void setLcStatus(Integer lcStatus) {
		this.lcStatus = lcStatus;
	}

	public String getLcCustNickName() {
		return lcCustNickName;
	}

	public void setLcCustNickName(String lcCustNickName) {
		this.lcCustNickName = lcCustNickName;
	}

	public String getConsigneeId() {
		return !AppUtil.isObjectEmpty(this.consigneeId) ? this.consigneeId.toString() : null;
	}

	public void setConsigneeId(ObjectId consigneeId) {
		this.consigneeId = consigneeId;
	}

	public String getConsigneeName() {
		return consigneeName;
	}

	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}

	public String getConsigneeAddress() {
		return consigneeAddress;
	}

	public void setConsigneeAddress(String consigneeAddress) {
		this.consigneeAddress = consigneeAddress;
	}

	public String getNotifypartyId() {
		return !AppUtil.isObjectEmpty(this.notifypartyId) ? this.notifypartyId.toString() : null;
	}

	public void setNotifypartyId(ObjectId notifypartyId) {
		this.notifypartyId = notifypartyId;
	}

	public String getNotifypartyName() {
		return notifypartyName;
	}

	public void setNotifypartyName(String notifypartyName) {
		this.notifypartyName = notifypartyName;
	}

	public String getNotifypartyAddress() {
		return notifypartyAddress;
	}

	public void setNotifypartyAddress(String notifypartyAddress) {
		this.notifypartyAddress = notifypartyAddress;
	}

	public List<ConsigneeNotifyparty> getConsigneeNotifyparties() {
		return consigneeNotifyparties;
	}

	public void setConsigneeNotifyparties(List<ConsigneeNotifyparty> consigneeNotifyparties) {
		this.consigneeNotifyparties = consigneeNotifyparties;
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

	public String getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
	}

	public String getShipmentRequestId() {
		return shipmentRequestId;
	}

	public void setShipmentRequestId(String shipmentRequestId) {
		this.shipmentRequestId = shipmentRequestId;
	}

	public String getCurrencySymbol() {
		return currencySymbol;
	}

	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}

	public Integer getShippingInstructionStatus() {
		return shippingInstructionStatus;
	}

	public void setShippingInstructionStatus(Integer shippingInstructionStatus) {
		this.shippingInstructionStatus = shippingInstructionStatus;
	}

	public Date getEtd() {
		return etd;
	}

	public void setEtd(Date etd) {
		this.etd = etd;
	}

	public String getShipName() {
		return shipName;
	}

	public void setShipName(String shipName) {
		this.shipName = shipName;
	}

	public String getInspectionCertificateNo() {
		return inspectionCertificateNo;
	}

	public void setInspectionCertificateNo(String inspectionCertificateNo) {
		this.inspectionCertificateNo = inspectionCertificateNo;
	}

	public String getExportCertificateNo() {
		return exportCertificateNo;
	}

	public void setExportCertificateNo(String exportCertificateNo) {
		this.exportCertificateNo = exportCertificateNo;
	}

}
