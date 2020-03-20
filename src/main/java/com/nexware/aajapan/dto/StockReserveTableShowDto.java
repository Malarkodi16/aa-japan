package com.nexware.aajapan.dto;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.utils.AppUtil;

public class StockReserveTableShowDto {
	@NotBlank
	@Indexed(unique = true)
	private String stockNo;
	private String chassisNo;
	private String model;
	private String category;
	private String maker;
	private String customerId;
	private String firstName;
	private String hsCode;
	private String lastName;
	private String nickName;
	private String companyName;
	private String salesPersonId;
	private Integer currency;	
	private String currencySymbol;
	private Double price;
	@JsonFormat(pattern = "yyyy/MM")
	@DateTimeFormat(pattern = "yyyy/MM")
	private Date firstRegDate;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date date;
	private Integer shippingInstructionStatus;
	private Double fob;
	private String reserveBy;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date warningDate;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date expiryDate;
	private Integer status;
	private Double exchangeRate;
	private Double minSellingPriceInDollar;
	private Integer inspectionFlag;
	private String destinationCountry;
	private String destinationPort;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date purchaseDate;
	private Integer isBidding;
	private Double cc;

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

	public String getModel() {
		return this.model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getMaker() {
		return this.maker;
	}

	public void setMaker(String maker) {
		this.maker = maker;
	}

	public String getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getSalesPersonId() {
		return this.salesPersonId;
	}

	public void setSalesPersonId(String salesPersonId) {
		this.salesPersonId = salesPersonId;
	}

	public Double getPrice() {
		return this.price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
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

	public String getNickName() {
		return this.nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Integer getShippingInstructionStatus() {
		return this.shippingInstructionStatus;
	}

	public void setShippingInstructionStatus(Integer shippingInstructionStatus) {
		this.shippingInstructionStatus = shippingInstructionStatus;
	}

	public Date getFirstRegDate() {
		return this.firstRegDate;
	}

	public void setFirstRegDate(Date firstRegDate) {
		this.firstRegDate = firstRegDate;
	}

	public Integer getCurrency() {
		return this.currency;
	}

	public void setCurrency(Integer currency) {
		this.currency = currency;
	}

	public String getCurrencySymbol() {
		return this.currencySymbol;
	}

	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}

	public Double getFob() {
		/*
		 * Double fobVal = AppUtil.isObjectEmpty(this.fob) ? 0.0 : this.fob; Double
		 * minSellingPriceInDollarVal =
		 * AppUtil.isObjectEmpty(this.minSellingPriceInDollar) ? 0.0 :
		 * this.minSellingPriceInDollar; if (!AppUtil.isObjectEmpty(this.currency)) {
		 * return this.currency.equals(Constants.CURRENCY_YEN) ? fobVal :
		 * minSellingPriceInDollarVal; }
		 */
		return fob;
	}

	public void setFob(Double fob) {
		this.fob = fob;
	}

	public String getReserveBy() {
		return reserveBy;
	}

	public void setReserveBy(String reserveBy) {
		this.reserveBy = reserveBy;
	}

	public Date getWarningDate() {
		return warningDate;
	}

	public void setWarningDate(Date warningDate) {
		this.warningDate = warningDate;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Integer getStatus() {
		if (new Date().after(AppUtil.ifNull(expiryDate, new Date()))) {
			status = Constants.LAST_LAP_STATUS_2;
		} else if (new Date().after(AppUtil.ifNull(warningDate, new Date()))
				&& new Date().before(AppUtil.ifNull(expiryDate, new Date()))) {
			status = Constants.LAST_LAP_STATUS_1;
		} else {
			status = Constants.LAST_LAP_STATUS_0;
		}
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Double getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public Double getMinSellingPriceInDollar() {
		return minSellingPriceInDollar;
	}

	public void setMinSellingPriceInDollar(Double minSellingPriceInDollar) {
		this.minSellingPriceInDollar = minSellingPriceInDollar;
	}

	public Integer getInspectionFlag() {
		return inspectionFlag;
	}

	public void setInspectionFlag(Integer inspectionFlag) {
		this.inspectionFlag = inspectionFlag;
	}

	public String getDestinationCountry() {
		return destinationCountry;
	}

	public void setDestinationCountry(String destinationCountry) {
		this.destinationCountry = destinationCountry;
	}

	public String getDestinationPort() {
		return destinationPort;
	}

	public void setDestinationPort(String destinationPort) {
		this.destinationPort = destinationPort;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public String getHsCode() {
		return hsCode;
	}

	public void setHsCode(String hsCode) {
		this.hsCode = hsCode;
	}

	public Integer getIsBidding() {
		return isBidding;
	}

	public void setIsBidding(Integer isBidding) {
		this.isBidding = isBidding;
	}

	public Double getCc() {
		return cc;
	}

	public void setCc(Double cc) {
		this.cc = cc;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

}
