package com.nexware.aajapan.dto;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.models.TSalesInvoiceDetailsTransaction;
import com.nexware.aajapan.utils.AppUtil;

public class TSalesInvoiceItemDto {

	private String id;
	private String stockNo;
	private String invoiceNo;
	private String chassisNo;
	private String maker;
	private String transmission;
	private String fuel;
	private String driven;
	private Double cc;
	private String type;
	private String model;
	private String color;

	@JsonFormat(pattern = "yyyy/MM")
	@DateTimeFormat(pattern = "yyyy/MM")
	private Date firstRegDate;
	private Long fob;
	private Long insurance;
	private Long shipping;
	private Long freight;
	private Double m3;
	private Double profit;
	private Long total;
	private Integer status;
	private String currencySymbol;
	private String currencyType;
	private List<TSalesInvoiceDetailsTransaction> transactions;
	private Integer shippingInstructionStatus;
	private String customerFN;
	private String customerLN;
	private String companyName;
	private String nickName;
	private String fCustomerName;
	private String cFirstName;
	private String cLastName;
	private String fConsigneeName;
	private String npFirstName;
	private String npLastName;
	private String fNotifyName;
	private String orderedBy;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date etd;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date eta;
	private Integer shippingStatus;
	private String shipName;
	private String shippingCompanyName;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date createdDate;
	private String customerId;
	

	
	public String getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCustomerFN() {
		return customerFN;
	}

	public void setCustomerFN(String customerFN) {
		this.customerFN = customerFN;
	}

	public String getCustomerLN() {
		return customerLN;
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
		return fCustomerName;
	}

	public void setfCustomerName(String fCustomerName) {
		this.fCustomerName = fCustomerName;
	}

	public String getcFirstName() {
		return cFirstName;
	}

	public void setcFirstName(String cFirstName) {
		this.cFirstName = cFirstName;
	}

	public String getcLastName() {
		return cLastName;
	}

	public void setcLastName(String cLastName) {
		this.cLastName = cLastName;
	}

	public String getfConsigneeName() {
		return fConsigneeName;
	}

	public void setfConsigneeName(String fConsigneeName) {
		this.fConsigneeName = fConsigneeName;
	}

	public String getNpFirstName() {
		return npFirstName;
	}

	public void setNpFirstName(String npFirstName) {
		this.npFirstName = npFirstName;
	}

	public String getNpLastName() {
		return npLastName;
	}

	public void setNpLastName(String npLastName) {
		this.npLastName = npLastName;
	}

	public String getfNotifyName() {
		return fNotifyName;
	}

	public void setfNotifyName(String fNotifyName) {
		this.fNotifyName = fNotifyName;
	}

	public String getOrderedBy() {
		return orderedBy;
	}

	public void setOrderedBy(String orderedBy) {
		this.orderedBy = orderedBy;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Long getFob() {
		this.fob = AppUtil.ifNull(fob, 0L);
		return (long) Math.round(fob);
	}

	public void setFob(Long fob) {
		this.fob = fob;
	}

	public Long getInsurance() {
		this.insurance = AppUtil.ifNull(insurance, 0L);
		return (long) Math.round(insurance);
	}

	public void setInsurance(Long insurance) {
		this.insurance = insurance;
	}

	public Long getShipping() {
		this.shipping = AppUtil.ifNull(shipping, 0L);
		return (long) Math.round(shipping);
	}

	public void setShipping(Long shipping) {
		this.shipping = shipping;
	}

	public Long getFreight() {
		this.freight = AppUtil.ifNull(freight, 0L);
		return (long) Math.round(freight);
	}

	public void setFreight(Long freight) {
		this.freight = freight;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public Long getTotal() {
		// this.total = (this.getFob() + this.getShipping() + this.getInsurance() +
		// this.getFreight())
		return AppUtil.ifNull(this.total, 0L);
	}

	public List<TSalesInvoiceDetailsTransaction> getTransactions() {
		return this.transactions;
	}

	public void setTransactions(List<TSalesInvoiceDetailsTransaction> transactions) {
		this.transactions = transactions;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getFirstRegDate() {
		return this.firstRegDate;
	}

	public void setFirstRegDate(Date firstRegDate) {
		this.firstRegDate = firstRegDate;
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

	public String getCurrencySymbol() {
		return this.currencySymbol;
	}

	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}

	public Double getM3() {
		return this.m3;
	}

	public void setM3(Double m3) {
		this.m3 = m3;
	}

	public String getColor() {
		return this.color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Double getProfit() {
		return this.profit;
	}

	public void setProfit(Double profit) {
		this.profit = profit;
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

	public Date getEta() {
		return eta;
	}

	public void setEta(Date eta) {
		this.eta = eta;
	}

	public Integer getShippingStatus() {
		return shippingStatus;
	}

	public void setShippingStatus(Integer shippingStatus) {
		this.shippingStatus = shippingStatus;
	}

	public String getShipName() {
		return shipName;
	}

	public void setShipName(String shipName) {
		this.shipName = shipName;
	}

	public String getShippingCompanyName() {
		return shippingCompanyName;
	}

	public void setShippingCompanyName(String shippingCompanyName) {
		this.shippingCompanyName = shippingCompanyName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

}
