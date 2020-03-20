package com.nexware.aajapan.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.utils.AppUtil;

public class CostofSalesDto {

	private String id;
	private String stockNo; // refer t_stck table
	private String chassisNo;
	private String type;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date purchaseDate; // refer t_prchs_invc table
	private Date eta;
	private Date etd;
	private Double sellingPrice;
	private Double purchasePrice;
	private Double exchangeRate;
	private Double margin;
	private Double purchaseCost; // refer t_prchs_invc table
	private Double commision; // refer t_prchs_invc table
	private Double otherCharge;
	private Double roadTax; // refer t_prchs_invc table
	private Double recycle; // refer t_prchs_invc table
	private Double freight; // refer t_frght_invc table
	private Double inspection; // refer t_frght_invc table
	private Double shipping;
	private Double fob;
	private Double insurance;// refer t_frght_invc table
	private Double transport;
	private String customerId;
	private String customerName;
	private String consigneeId;
	private Integer currency;
	private String currencySymbol;
	private String cName;
	private String notifyId;
	private String nName;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date salesDate;
	private Double exchangeRateSellingPrice;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStockNo() {
		return stockNo;
	}

	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}

	public String getChassisNo() {
		return chassisNo;
	}

	public void setChassisNo(String chassisNo) {
		this.chassisNo = chassisNo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public Double getOtherCharge() {
		return otherCharge;
	}

	public void setOtherCharge(Double otherCharge) {
		this.otherCharge = otherCharge;
	}

	public Date getEta() {
		return eta;
	}

	public void setEta(Date eta) {
		this.eta = eta;
	}

	public Date getEtd() {
		return etd;
	}

	public void setEtd(Date etd) {
		this.etd = etd;
	}

	public Double getSellingPrice() {
		sellingPrice = AppUtil.ifNull(this.getFob(), 0.0) + AppUtil.ifNull(this.getInsurance(), 0.0)
				+ AppUtil.ifNull(this.getFreight(), 0.0) + AppUtil.ifNull(this.getShipping(), 0.0);
		return sellingPrice;
	}

	public void setSellingPrice(Double sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	public Double getPurchasePrice() {
		final Double pc = (AppUtil.ifNull(this.getPurchaseCost(), 0.0) * Constants.COMMON_TAX) / 100;
		final Double commission = (AppUtil.ifNull(this.getCommision(), 0.0) * Constants.COMMON_TAX) / 100;
		purchasePrice = AppUtil.ifNull(this.getPurchaseCost(), 0.0) + AppUtil.ifNull(this.getCommision(), 0.0) + pc
				+ commission + AppUtil.ifNull(this.getRecycle(), 0.0) + AppUtil.ifNull(this.getOtherCharge(), 0.0)
				+ AppUtil.ifNull(this.getRoadTax(), 0.0);
		return purchasePrice;
	}

	public void setPurchasePrice(Double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public Double getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public Double getMargin() {
		margin = AppUtil.ifNull(this.getExchangeRateSellingPrice(), 0.0) - AppUtil.ifNull(this.getPurchasePrice(), 0.0);
		return margin;
	}

	public void setMargin(Double margin) {
		this.margin = margin;
	}

	public Double getPurchaseCost() {
		return purchaseCost;
	}

	public void setPurchaseCost(Double purchaseCost) {
		this.purchaseCost = purchaseCost;
	}

	public Double getCommision() {
		return commision;
	}

	public void setCommision(Double commision) {
		this.commision = commision;
	}

	public Double getRoadTax() {
		return roadTax;
	}

	public void setRoadTax(Double roadTax) {
		this.roadTax = roadTax;
	}

	public Double getRecycle() {
		return recycle;
	}

	public void setRecycle(Double recycle) {
		this.recycle = recycle;
	}

	public Double getInspection() {
		return inspection;
	}

	public void setInspection(Double inspection) {
		this.inspection = inspection;
	}

	public Double getShipping() {
		return shipping;
	}

	public void setShipping(Double shipping) {
		this.shipping = shipping;
	}

	public Double getTransport() {
		return transport;
	}

	public void setTransport(Double transport) {
		this.transport = transport;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getConsigneeId() {
		return consigneeId;
	}

	public void setConsigneeId(String consigneeId) {
		this.consigneeId = consigneeId;
	}

	public String getcName() {
		return cName;
	}

	public void setcName(String cName) {
		this.cName = cName;
	}

	public String getNotifyId() {
		return notifyId;
	}

	public void setNotifyId(String notifyId) {
		this.notifyId = notifyId;
	}

	public String getnName() {
		return nName;
	}

	public void setnName(String nName) {
		this.nName = nName;
	}

	public Date getSalesDate() {
		return salesDate;
	}

	public void setSalesDate(Date salesDate) {
		this.salesDate = salesDate;
	}

	public Double getFreight() {
		return freight;
	}

	public void setFreight(Double freight) {
		this.freight = freight;
	}

	public Double getFob() {
		return fob;
	}

	public void setFob(Double fob) {
		this.fob = fob;
	}

	public Double getInsurance() {
		return insurance;
	}

	public void setInsurance(Double insurance) {
		this.insurance = insurance;
	}

	public Integer getCurrency() {
		return currency;
	}

	public void setCurrency(Integer currency) {
		this.currency = currency;
	}

	public String getCurrencySymbol() {
		return currencySymbol;
	}

	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}

	public Double getExchangeRateSellingPrice() {
		exchangeRateSellingPrice = AppUtil.ifNull(this.getSellingPrice(), 0.0)
				* AppUtil.ifNull(this.getExchangeRate(), 0.0);
		return exchangeRateSellingPrice;
	}

	public void setExchangeRateSellingPrice(Double exchangeRateSellingPrice) {
		this.exchangeRateSellingPrice = exchangeRateSellingPrice;
	}

}
