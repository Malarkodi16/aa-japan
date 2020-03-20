
package com.nexware.aajapan.dto;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.models.MBank;
import com.nexware.aajapan.models.MCurrency;
import com.nexware.aajapan.models.ProformaInvoiceItem;
import com.nexware.aajapan.utils.AppUtil;

public class ProformaInvoiceDto {

	private String invoiceNo;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date date;
	private String customerId;
	private String firstName;
	private String lastName;
	private String mobileNo;
	private boolean isLcCustomer;
	private String fCustName;
	private String consigneeId;
	private String cFirstName;
	private String cLastName;
	private String fConsigneeName;
	private String cAddress;
	private String cMobileNo;

	private String notifypartyId;
	private String npFirstName;
	private String npLastName;
	private String fNotifypartyName;
	private String npAddress;
	private String npMobileNo;
	private String paymentType;
	private List<ProformaInvoiceItem> items;
	private Double fobTotal;
	private Double freightTotal;
	private Double insuranceTotal;
	private Double shippingTotal;
	private Long total;
	private MCurrency currencyDetails;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date issueDate;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date expiryDate;
	private String country;
	private String port;
	private MBank bank;
	private MCurrency currency;
	private Integer currencyType;
	private String invoiceBy;
	private String customerAddress;
	private String formattedCustomerNameAddress;

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
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

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMobileNo() {
		return this.mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getPaymentType() {
		return this.paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public List<ProformaInvoiceItem> getItems() {
		return this.items;
	}

	public void setItems(List<ProformaInvoiceItem> items) {
		this.items = items;
	}

	public Long getFobTotal() {
		this.fobTotal = AppUtil.ifNull(this.fobTotal, 0.0);
		return Math.round(this.fobTotal);
	}

	public void setFobTotal(Double fobTotal) {
		this.fobTotal = fobTotal;
	}

	public Long getFreightTotal() {
		this.freightTotal = AppUtil.ifNull(this.freightTotal, 0.0);
		return Math.round(this.freightTotal);
	}

	public void setFreightTotal(Double freightTotal) {
		this.freightTotal = freightTotal;
	}

	public Long getInsuranceTotal() {
		this.insuranceTotal = AppUtil.ifNull(this.insuranceTotal, 0.0);
		return Math.round(this.insuranceTotal);
	}

	public void setInsuranceTotal(Double insuranceTotal) {
		this.insuranceTotal = insuranceTotal;
	}

	public Long getShippingTotal() {
		this.shippingTotal = AppUtil.ifNull(this.shippingTotal, 0.0);
		return Math.round(this.shippingTotal);
	}

	public void setShippingTotal(Double shippingTotal) {
		this.shippingTotal = shippingTotal;
	}

	public Long getTotal() {
		this.total = this.getFobTotal() + this.getFreightTotal() + this.getShippingTotal() + this.getInsuranceTotal();
		return this.total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public String getConsigneeId() {
		return this.consigneeId;
	}

	public void setConsigneeId(String consigneeId) {
		this.consigneeId = consigneeId;
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

	public String getNotifypartyId() {
		return this.notifypartyId;
	}

	public void setNotifypartyId(String notifypartyId) {
		this.notifypartyId = notifypartyId;
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

	public String getfCustName() {
		this.fCustName = this.firstName + " " + this.lastName;
		return this.fCustName;
	}

	public void setfCustName(String fCustName) {
		this.fCustName = fCustName;
	}

	public String getfConsigneeName() {
		this.fConsigneeName = this.cLastName + " " + this.cLastName;
		return this.fConsigneeName;
	}

	public void setfConsigneeName(String fConsigneeName) {
		this.fConsigneeName = fConsigneeName;
	}

	public String getfNotifypartyName() {
		this.fNotifypartyName = this.npLastName + " " + this.npLastName;
		return this.fNotifypartyName;
	}

	public void setfNotifypartyName(String fNotifypartyName) {
		this.fNotifypartyName = fNotifypartyName;
	}

	public boolean isLcCustomer() {
		return this.isLcCustomer;
	}

	public void setLcCustomer(boolean isLcCustomer) {
		this.isLcCustomer = isLcCustomer;
	}

	public MCurrency getCurrencyDetails() {
		return this.currencyDetails;
	}

	public void setCurrencyDetails(MCurrency currencyDetails) {
		this.currencyDetails = currencyDetails;
	}

	public String getcMobileNo() {
		return this.cMobileNo;
	}

	public void setcMobileNo(String cMobileNo) {
		this.cMobileNo = cMobileNo;
	}

	public String getNpMobileNo() {
		return this.npMobileNo;
	}

	public void setNpMobileNo(String npMobileNo) {
		this.npMobileNo = npMobileNo;
	}

	public Date getIssueDate() {
		return this.issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public Date getExpiryDate() {
		this.expiryDate = AppUtil.addDays(this.issueDate, 10);
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

	public MBank getBank() {
		return this.bank;
	}

	public void setBank(MBank bank) {
		this.bank = bank;
	}

	public MCurrency getCurrency() {
		return this.currency;
	}

	public void setCurrency(MCurrency currency) {
		this.currency = currency;
	}

	public String getInvoiceBy() {
		return invoiceBy;
	}

	public void setInvoiceBy(String invoiceBy) {
		this.invoiceBy = invoiceBy;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public String getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}

	public String getFormattedCustomerNameAddress() {
		formattedCustomerNameAddress = AppUtil.ifNull(firstName, "") + ", " + AppUtil.ifNull(customerAddress, "");
		return formattedCustomerNameAddress;
	}

	public void setFormattedCustomerNameAddress(String formattedCustomerNameAddress) {
		this.formattedCustomerNameAddress = formattedCustomerNameAddress;
	}

}
