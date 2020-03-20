package com.nexware.aajapan.dto;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TLcDetailsDto {
	private String id;
	private String lcNo;
	@NotBlank
	@Indexed(unique = true)
	private String lcInvoiceNo;
	private String bankId;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date validity;
	private String consignee;
	private String customerName;
	private String notifyParty;
	private String cAddress;
	private String npAddress;
	private Double amount;
	private String proformaInvoiceId;
	private String salesPerson;
	private String salesPersonName;
	private Integer status;
	private String shippingTerms;
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

	public String getLcInvoiceNo() {
		return this.lcInvoiceNo;
	}

	public void setLcInvoiceNo(String lcInvoiceNo) {
		this.lcInvoiceNo = lcInvoiceNo;
	}

	public String getBankId() {
		return this.bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public Date getValidity() {
		return this.validity;
	}

	public void setValidity(Date validity) {
		this.validity = validity;
	}

	public String getConsignee() {
		return this.consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getCustomerName() {
		return this.customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getNotifyParty() {
		return this.notifyParty;
	}

	public void setNotifyParty(String notifyParty) {
		this.notifyParty = notifyParty;
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

	public String getSalesPerson() {
		return this.salesPerson;
	}

	public void setSalesPerson(String salesPerson) {
		this.salesPerson = salesPerson;
	}

	public String getSalesPersonName() {
		return this.salesPersonName;
	}

	public void setSalesPersonName(String salesPersonName) {
		this.salesPersonName = salesPersonName;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

}
