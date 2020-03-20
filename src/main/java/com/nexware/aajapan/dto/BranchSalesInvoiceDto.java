package com.nexware.aajapan.dto;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.models.SalesInvoiceDetail;

public class BranchSalesInvoiceDto {

	private String id;
	private String invoiceNo;
	private String customerId;
	private String customerFN;
	private boolean isLcCustomer;
	private String cAddress;
	private String cMobileNo;
	private String cCity;
	private String cCompanyName;
	private String paymentType;
	private Double total;
	private Double fobTotal;
	private Double freightTotal;
	private Double shippingTotal;
	private Double insuranceTotal;
	private String salesPerson;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date createdDate;
	private List<SalesInvoiceDetail> salesInvoiceDetails;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getInvoiceNo() {
		return this.invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCustomerFN() {
		return this.customerFN;
	}

	public void setCustomerFN(String customerFN) {
		this.customerFN = customerFN;
	}

	public boolean isLcCustomer() {
		return this.isLcCustomer;
	}

	public void setLcCustomer(boolean isLcCustomer) {
		this.isLcCustomer = isLcCustomer;
	}

	public String getcAddress() {
		return this.cAddress;
	}

	public void setcAddress(String cAddress) {
		this.cAddress = cAddress;
	}

	public String getcMobileNo() {
		return this.cMobileNo;
	}

	public void setcMobileNo(String cMobileNo) {
		this.cMobileNo = cMobileNo;
	}

	public String getPaymentType() {
		return this.paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public Double getTotal() {
		return this.total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Double getFobTotal() {
		return this.fobTotal;
	}

	public void setFobTotal(Double fobTotal) {
		this.fobTotal = fobTotal;
	}

	public Double getFreightTotal() {
		return this.freightTotal;
	}

	public void setFreightTotal(Double freightTotal) {
		this.freightTotal = freightTotal;
	}

	public Double getShippingTotal() {
		return this.shippingTotal;
	}

	public void setShippingTotal(Double shippingTotal) {
		this.shippingTotal = shippingTotal;
	}

	public Double getInsuranceTotal() {
		return this.insuranceTotal;
	}

	public void setInsuranceTotal(Double insuranceTotal) {
		this.insuranceTotal = insuranceTotal;
	}

	public String getSalesPerson() {
		return this.salesPerson;
	}

	public void setSalesPerson(String salesPerson) {
		this.salesPerson = salesPerson;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public List<SalesInvoiceDetail> getSalesInvoiceDetails() {
		return this.salesInvoiceDetails;
	}

	public void setSalesInvoiceDetails(List<SalesInvoiceDetail> salesInvoiceDetails) {
		this.salesInvoiceDetails = salesInvoiceDetails;
	}

	public String getcCity() {
		return this.cCity;
	}

	public void setcCity(String cCity) {
		this.cCity = cCity;
	}

	public String getcCompanyName() {
		return this.cCompanyName;
	}

	public void setcCompanyName(String cCompanyName) {
		this.cCompanyName = cCompanyName;
	}

}
