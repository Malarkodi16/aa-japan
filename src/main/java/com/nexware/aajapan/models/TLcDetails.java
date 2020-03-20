package com.nexware.aajapan.models;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.listeners.EntityModelBase;

@Document(collection = "t_lc_dtls")
public class TLcDetails extends EntityModelBase {
	@Id
	private String id;
	@Indexed
	private String lcNo;
	@NotBlank
	@Indexed(unique = true)
	private String lcInvoiceNo;
	private String bankId;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date issueDate;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date expiryDate;
	private ObjectId consignee;
	private String consigneeName;
	private String customerName;
	private ObjectId notifyParty;
	private String notifyPartyName;
	private String cAddress;
	private String npAddress;
	private Double amount;
	private Double amountAllocated;
	private Double amountReceived;
	private String proformaInvoiceId;
	private String proformaInvoiceNo;
	private String customerId;
	private String salesPerson;
	private Integer status;
	private String shippingTermsName;
	private String shippingTerms;
	private String beneficiaryCertify;
	private String licenseDoc;
	private String billOfExchangeNo;
	private Integer billOfExchangeStatus;
	private String yenBankId;
	private Integer methodOfProcess;
	private String dhlNo;
	
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLcNo() {
		return this.lcNo;
	}

	public void setLcNo(String lcNo) {
		this.lcNo = lcNo;
	}

	public String getBankId() {
		return this.bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
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

	public Double getAmount() {
		return this.amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getConsignee() {
		return this.consignee.toString();
	}

	public void setConsignee(String consignee) {
		this.consignee = new ObjectId(consignee);
	}

	public String getCustomerName() {
		return this.customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getNotifyParty() {
		return this.notifyParty.toString();
	}

	public void setNotifyParty(String notifyParty) {
		this.notifyParty = new ObjectId(notifyParty);
	}

	public String getProformaInvoiceId() {
		return this.proformaInvoiceId;
	}

	public void setProformaInvoiceId(String proformaInvoiceId) {
		this.proformaInvoiceId = proformaInvoiceId;
	}

	public String getcAddress() {
		return this.cAddress;
	}

	public void setcAddress(String cAddress) {
		this.cAddress = cAddress;
	}

	public String getNpAddress() {
		return this.npAddress;
	}

	public void setNpAddress(String npAddress) {
		this.npAddress = npAddress;
	}

	public String getLcInvoiceNo() {
		return this.lcInvoiceNo;
	}

	public void setLcInvoiceNo(String lcInvoiceNo) {
		this.lcInvoiceNo = lcInvoiceNo;
	}

	public String getShippingTerms() {
		return this.shippingTerms;
	}

	public void setShippingTerms(String shippingTerms) {
		this.shippingTerms = shippingTerms;
	}

	public String getBillOfExchangeNo() {
		return this.billOfExchangeNo;
	}

	public void setBillOfExchangeNo(String billOfExchangeNo) {
		this.billOfExchangeNo = billOfExchangeNo;
	}

	public String getSalesPerson() {
		return this.salesPerson;
	}

	public void setSalesPerson(String salesPerson) {
		this.salesPerson = salesPerson;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getBillOfExchangeStatus() {
		return billOfExchangeStatus;
	}

	public void setBillOfExchangeStatus(Integer billOfExchangeStatus) {
		this.billOfExchangeStatus = billOfExchangeStatus;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public void setConsignee(ObjectId consignee) {
		this.consignee = consignee;
	}

	public void setNotifyParty(ObjectId notifyParty) {
		this.notifyParty = notifyParty;
	}

	public String getConsigneeName() {
		return consigneeName;
	}

	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}

	public String getNotifyPartyName() {
		return notifyPartyName;
	}

	public void setNotifyPartyName(String notifyPartyName) {
		this.notifyPartyName = notifyPartyName;
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

	public String getShippingTermsName() {
		return shippingTermsName;
	}

	public void setShippingTermsName(String shippingTermsName) {
		this.shippingTermsName = shippingTermsName;
	}

	public String getYenBankId() {
		return yenBankId;
	}

	public void setYenBankId(String yenBankId) {
		this.yenBankId = yenBankId;
	}

	public Integer getMethodOfProcess() {
		return methodOfProcess;
	}

	public void setMethodOfProcess(Integer methodOfProcess) {
		this.methodOfProcess = methodOfProcess;
	}

	public String getDhlNo() {
		return dhlNo;
	}

	public void setDhlNo(String dhlNo) {
		this.dhlNo = dhlNo;
	}

	public String getProformaInvoiceNo() {
		return proformaInvoiceNo;
	}

	public void setProformaInvoiceNo(String proformaInvoiceNo) {
		this.proformaInvoiceNo = proformaInvoiceNo;
	}

	public Double getAmountAllocated() {
		return amountAllocated;
	}

	public void setAmountAllocated(Double amountAllocated) {
		this.amountAllocated = amountAllocated;
	}

	public Double getAmountReceived() {
		return amountReceived;
	}

	public void setAmountReceived(Double amountReceived) {
		this.amountReceived = amountReceived;
	}


}
