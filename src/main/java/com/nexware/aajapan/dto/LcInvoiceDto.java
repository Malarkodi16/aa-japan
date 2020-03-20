
package com.nexware.aajapan.dto;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.models.ConsigneeNotifyparty;
import com.nexware.aajapan.utils.AppUtil;

public class LcInvoiceDto {
	private String id;
	private String bankId;
	private String customerId;
	private String lcInvoiceNo;
	private String lcNo;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date validity;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date issueDate;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date expiryDate;
	private String bank;
	private Double amount;
	private String proformaInvoiceId;
	private String customerName;
	private String consignee;
	private String cAddress;
	private String npAddress;
	private String notifyParty;
	private List<LcInvoiceItemDto> items;
	private String shippingTerms;
	private String beneficiaryCertify;
	private String licenseDoc;
	private String shippingMarks;
	private String perVessel;
	private String to;
	private String from;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date sailingDate;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date bankSentDate;
	private String billOfExchangeNo;
	private List<ConsigneeNotifyparty> consigneeNotifyparties;

	private ObjectId consigneeId;
	private ObjectId notifypartyId;
	private String shippingTermsName;
	
	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLcInvoiceNo() {
		return lcInvoiceNo;
	}

	public void setLcInvoiceNo(String lcInvoiceNo) {
		this.lcInvoiceNo = lcInvoiceNo;
	}

	public String getLcNo() {
		return this.lcNo;
	}

	public void setLcNo(String lcNo) {
		this.lcNo = lcNo;
	}

	public Date getValidity() {
		return this.validity;
	}

	public void setValidity(Date validity) {
		this.validity = validity;
	}

	public String getBank() {
		return this.bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public Double getAmount() {
		return this.amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getProformaInvoiceId() {
		return this.proformaInvoiceId;
	}

	public void setProformaInvoiceId(String proformaInvoiceId) {
		this.proformaInvoiceId = proformaInvoiceId;
	}

	public String getCustomerName() {
		return this.customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getConsignee() {
		return this.consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getcAddress() {
		return cAddress;
	}

	public void setcAddress(String cAddress) {
		this.cAddress = cAddress;
	}

	public String getNpAddress() {
		return npAddress;
	}

	public void setNpAddress(String npAddress) {
		this.npAddress = npAddress;
	}

	public String getNotifyParty() {
		return this.notifyParty;
	}

	public void setNotifyParty(String notifyParty) {
		this.notifyParty = notifyParty;
	}

	public List<LcInvoiceItemDto> getItems() {
		return this.items;
	}

	public void setItems(List<LcInvoiceItemDto> items) {
		this.items = items;
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

	public Date getBankSentDate() {
		return this.bankSentDate;
	}

	public void setBankSentDate(Date bankSentDate) {
		this.bankSentDate = bankSentDate;
	}

	public String getBillOfExchangeNo() {
		return this.billOfExchangeNo;
	}

	public void setBillOfExchangeNo(String billOfExchangeNo) {
		this.billOfExchangeNo = billOfExchangeNo;
	}

	public List<ConsigneeNotifyparty> getConsigneeNotifyparties() {
		return consigneeNotifyparties;
	}

	public void setConsigneeNotifyparties(List<ConsigneeNotifyparty> consigneeNotifyparties) {
		this.consigneeNotifyparties = consigneeNotifyparties;
	}

	public String getConsigneeId() {
		return !AppUtil.isObjectEmpty(this.consigneeId) ? this.consigneeId.toString() : null;
	}

	public void setConsigneeId(ObjectId consigneeId) {
		this.consigneeId = consigneeId;
	}

	public String getNotifypartyId() {
		return !AppUtil.isObjectEmpty(this.notifypartyId) ? this.notifypartyId.toString() : null;
	}

	public void setNotifypartyId(ObjectId notifypartyId) {
		this.notifypartyId = notifypartyId;
	}

	public String getBeneficiaryCertify() {
		return beneficiaryCertify;
	}

	public void setBeneficiaryCertify(String beneficiaryCertify) {
		this.beneficiaryCertify = beneficiaryCertify;
	}

	public String getLicenseDoc() {
		return licenseDoc;
	}

	public void setLicenseDoc(String licenseDoc) {
		this.licenseDoc = licenseDoc;
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

	public String getShippingTermsName() {
		return shippingTermsName;
	}

	public void setShippingTermsName(String shippingTermsName) {
		this.shippingTermsName = shippingTermsName;
	}


}
