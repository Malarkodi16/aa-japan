package com.nexware.aajapan.dto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.utils.AppUtil;

public class TLcInvoiceDto {

	private ObjectId lcDtlId;
	private String notifyParty;
	private String npAddress;
	private String shippingTerms;
	private String shippingMarks;
	private String maker;
	private String model;
	private String type;
	private String chassisNo;
	private Double stockAmount;
	private String fuel;
	private List<String> equipment;
	@JsonFormat(pattern = "yyyy/MM")
	@DateTimeFormat(pattern = "yyyy/MM")
	private Date firstRegDate;
	private String sFirstRegDate;
	private String proformaInvoiceNo;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date proformaInvoiceDate;
	private String bank;
	private String hsCode;
	private String lcNo;
	private String beneficiaryCertify;
	private String licenseDoc;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date lcExpiryDate;
	private Double fob;
	private Double insurance;
	private Double inspection;
	private Double freight;
	private Double total;
	private String lcInvoiceNo;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date createdDate;
	private String perVessel;
	private String to;
	private String from;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date sailingDate;
	private String inspectionCertificateNo;
	private String exportCertificateNo;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date issueDate;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date expiryDate;
	private String billOfExchangeNo;

	public String getLcDtlId() {
		return this.lcDtlId.toString();
	}

	public void setLcDtlId(String lcDtlId) {
		this.lcDtlId = new ObjectId(lcDtlId);
	}

	public String getNotifyParty() {
		return this.notifyParty;
	}

	public void setNotifyParty(String notifyParty) {
		this.notifyParty = notifyParty;
	}

	public String getNpAddress() {
		return this.npAddress;
	}

	public void setNpAddress(String npAddress) {
		this.npAddress = npAddress;
	}

	public String getShippingTerms() {
		return this.shippingTerms;
	}

	public void setShippingTerms(String shippingTerms) {
		this.shippingTerms = shippingTerms;
	}

	public String getShippingMarks() {
		return this.shippingMarks;
	}

	public void setShippingMarks(String shippingMarks) {
		this.shippingMarks = shippingMarks;
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

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getChassisNo() {
		return this.chassisNo;
	}

	public void setChassisNo(String chassisNo) {
		this.chassisNo = chassisNo;
	}

	public Double getStockAmount() {
		stockAmount = AppUtil.ifNull(stockAmount, 0.0);
		return stockAmount;
	} 

	public void setStockAmount(Double stockAmount) {
		this.stockAmount = stockAmount;
	}

	public String getFuel() {
		return !AppUtil.isObjectEmpty(this.fuel)?this.fuel.toUpperCase():"";
	}

	public void setFuel(String fuel) {
		this.fuel = fuel;
	}

	public String getEquipment() {
		if (AppUtil.isObjectEmpty(equipment)) {
			return "";
		} else {
			return StringUtils.join(this.equipment, ",");
		}

	}

	public void setEquipment(List<String> equipment) {
		this.equipment = equipment;
	}

	public Date getFirstRegDate() {
		return this.firstRegDate;
	}

	public void setFirstRegDate(Date firstRegDate) {
		this.firstRegDate = firstRegDate;
	}

	public String getsFirstRegDate() {
		this.sFirstRegDate = AppUtil.isObjectEmpty(this.firstRegDate) ? ""
				: new SimpleDateFormat("yyyy/MM").format(this.firstRegDate);
		return this.sFirstRegDate;
	}

	public void setsFirstRegDate(String sFirstRegDate) {
		this.sFirstRegDate = sFirstRegDate;
	}

	public String getProformaInvoiceNo() {
		return this.proformaInvoiceNo;
	}

	public void setProformaInvoiceNo(String proformaInvoiceNo) {
		this.proformaInvoiceNo = proformaInvoiceNo;
	}

	public Date getProformaInvoiceDate() {
		return this.proformaInvoiceDate;
	}

	public void setProformaInvoiceDate(Date proformaInvoiceDate) {
		this.proformaInvoiceDate = proformaInvoiceDate;
	}

	public String getBank() {
		return this.bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getHsCode() {
		return this.hsCode;
	}

	public void setHsCode(String hsCode) {
		this.hsCode = hsCode;
	}

	public String getLcNo() {
		return this.lcNo;
	}

	public void setLcNo(String lcNo) {
		this.lcNo = lcNo;
	}

	public String getBeneficiaryCertify() {
		return this.beneficiaryCertify;
	}

	public void setBeneficiaryCertify(String beneficiaryCertify) {
		this.beneficiaryCertify = beneficiaryCertify;
	}

	public String getLicenseDoc() {
		return this.licenseDoc;
	}

	public void setLicenseDoc(String licenseDoc) {
		this.licenseDoc = licenseDoc;
	}

	public Date getLcExpiryDate() {
		return this.lcExpiryDate;
	}

	public void setLcExpiryDate(Date lcExpiryDate) {
		this.lcExpiryDate = lcExpiryDate;
	}

	public Double getFob() {
		fob = AppUtil.ifNull(fob, 0.0);
		return this.fob;
	}

	public void setFob(Double fob) {
		this.fob = fob;
	}

	public Double getInsurance() {
		insurance = AppUtil.ifNull(insurance, 0.0);
		return this.insurance;
	}

	public void setInsurance(Double insurance) {
		this.insurance = insurance;
	}

	public Double getFreight() {
		freight = AppUtil.ifNull(freight, 0.0);
		return this.freight;
	}

	public void setFreight(Double freight) {
		this.freight = freight;
	}

	public Double getTotal() {
		total = AppUtil.ifNull(total, 0.0);
		return this.total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public String getLcInvoiceNo() {
		return this.lcInvoiceNo;
	}

	public void setLcInvoiceNo(String lcInvoiceNo) {
		this.lcInvoiceNo = lcInvoiceNo;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getPerVessel() {
		return this.perVessel;
	}

	public void setPerVessel(String perVessel) {
		this.perVessel = perVessel;
	}

	public String getTo() {
		return this.to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getFrom() {
		return this.from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public Date getSailingDate() {
		return this.sailingDate;
	}

	public void setSailingDate(Date sailingDate) {
		this.sailingDate = sailingDate;
	}

	public String getInspectionCertificateNo() {
		return inspectionCertificateNo;
	}

	public void setInspectionCertificateNo(String inspectionCertificateNo) {
		this.inspectionCertificateNo = inspectionCertificateNo;
	}

	public void setLcDtlId(ObjectId lcDtlId) {
		this.lcDtlId = lcDtlId;
	}

	public String getExportCertificateNo() {
		return exportCertificateNo;
	}

	public void setExportCertificateNo(String exportCertificateNo) {
		this.exportCertificateNo = exportCertificateNo;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Double getInspection() {
		return inspection;
	}

	public void setInspection(Double inspection) {
		this.inspection = inspection;
	}

	public String getBillOfExchangeNo() {
		return billOfExchangeNo;
	}

	public void setBillOfExchangeNo(String billOfExchangeNo) {
		this.billOfExchangeNo = billOfExchangeNo;
	}

}
