package com.nexware.aajapan.dto;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.utils.AppUtil;

public class TSalesInvoiceDto {

	private String id;
	private String invoiceNo;
	private String customerId;
	private String consigneeId;
	private String notifypartyId;
	private String customerFN;
	private String customerLN;
	private String nickName;
	private String fCustomerName;
	private String companyName;
	private boolean isLcCustomer;
	private String cFirstName;
	private String cLastName;
	private String fConsigneeName;
	private String cAddress;
	private String cMobileNo;
	private String npFirstName;
	private String npLastName;
	private String fNotifyName;
	private String npAddress;
	private String npMobileNo;
	private String paymentType;
	private Long allTtotal;
	private Long fobTotal;
	private Long freightTotal;
	private Long shippingTotal;
	private Long insuranceTotal;
	private String salesPerson;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date createdDate;
	private List<TSalesInvoiceItemDto> salesInvoiceDetails;

	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date expiryDate;
	private String country;
	private String port;
	private String orderedBy;
	private String currencySymbol;
	private Integer currencyType;

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

	public String getConsigneeId() {
		return consigneeId;
	}

	public void setConsigneeId(String consigneeId) {
		this.consigneeId = consigneeId;
	}

	public String getNotifypartyId() {
		return notifypartyId;
	}

	public void setNotifypartyId(String notifypartyId) {
		this.notifypartyId = notifypartyId;
	}

	public String getCustomerFN() {
		return this.customerFN;
	}

	public void setCustomerFN(String customerFN) {
		this.customerFN = customerFN;
	}

	public String getCustomerLN() {
		return this.customerLN;
	}

	public void setCustomerLN(String customerLN) {
		this.customerLN = customerLN;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getfCustomerName() {
		this.fCustomerName = this.customerFN;
		return this.fCustomerName;
	}

	public void setfCustomerName(String fCustomerName) {
		this.fCustomerName = fCustomerName;
	}

	public String getcFirstName() {
		return this.cFirstName;
	}

	public void setcFirstName(String cFirstName) {
		this.cFirstName = cFirstName;
	}

	public String getcLastName() {
		return this.cLastName;
	}

	public void setcLastName(String cLastName) {
		this.cLastName = cLastName;
	}

	public String getfConsigneeName() {
		this.fConsigneeName = this.cFirstName;
		return this.fConsigneeName;
	}

	public void setfConsigneeName(String fConsigneeName) {
		this.fConsigneeName = fConsigneeName;
	}

	public String getNpFirstName() {
		return this.npFirstName;
	}

	public void setNpFirstName(String npFirstName) {
		this.npFirstName = npFirstName;
	}

	public String getNpLastName() {
		return this.npLastName;
	}

	public void setNpLastName(String npLastName) {
		this.npLastName = npLastName;
	}

	public String getfNotifyName() {
		this.fNotifyName = this.npFirstName;
		return this.fNotifyName;
	}

	public void setfNotifyName(String fNotifyName) {
		this.fNotifyName = fNotifyName;
	}

	public String getPaymentType() {
		return this.paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public String getSalesPerson() {
		return this.salesPerson;
	}

	public void setSalesPerson(String salesPerson) {
		this.salesPerson = salesPerson;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public List<TSalesInvoiceItemDto> getSalesInvoiceDetails() {
		return salesInvoiceDetails;
	}

	public void setSalesInvoiceDetails(List<TSalesInvoiceItemDto> salesInvoiceDetails) {
		this.salesInvoiceDetails = salesInvoiceDetails;
	}

	public Long getFobTotal() {
		this.fobTotal = AppUtil.ifNull(fobTotal, 0L);
		return (long) Math.round(fobTotal);
	}

	public void setFobTotal(Long fobTotal) {
		this.fobTotal = fobTotal;
	}

	public Long getFreightTotal() {
		this.freightTotal = AppUtil.ifNull(this.freightTotal, 0L);
		return (long) Math.round(this.freightTotal);
	}

	public void setFreightTotal(Long freightTotal) {
		this.freightTotal = freightTotal;
	}

	public Long getShippingTotal() {
		this.shippingTotal = AppUtil.ifNull(this.shippingTotal, 0L);
		return (long) Math.round(this.shippingTotal);
	}

	public void setShippingTotal(Long shippingTotal) {
		this.shippingTotal = shippingTotal;
	}

	public Long getInsuranceTotal() {
		this.insuranceTotal = AppUtil.ifNull(this.insuranceTotal, 0L);
		return (long) Math.round(this.insuranceTotal);
	}

	public void setInsuranceTotal(Long insuranceTotal) {
		this.insuranceTotal = insuranceTotal;
	}

	public Long getAllTtotal() {
		Long allTtotals = this.getFobTotal() + this.getFreightTotal() + this.getShippingTotal()
				+ this.getInsuranceTotal();
		AppUtil.ifNullOrEmpty(allTtotals, allTtotal);
		return allTtotal;
	}

	public void setAllTtotal(Long allTtotal) {
		this.allTtotal = allTtotal;
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

	public String getNpAddress() {
		return this.npAddress;
	}

	public void setNpAddress(String npAddress) {
		this.npAddress = npAddress;
	}

	public String getNpMobileNo() {
		return this.npMobileNo;
	}

	public void setNpMobileNo(String npMobileNo) {
		this.npMobileNo = npMobileNo;
	}

	public Date getExpiryDate() {
		this.expiryDate = AppUtil.addDays(this.createdDate, 10);
		return this.expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPort() {
		return this.port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getOrderedBy() {
		return orderedBy;
	}

	public void setOrderedBy(String orderedBy) {
		this.orderedBy = orderedBy;
	}

	public String getCurrencySymbol() {
		return currencySymbol;
	}

	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

}
