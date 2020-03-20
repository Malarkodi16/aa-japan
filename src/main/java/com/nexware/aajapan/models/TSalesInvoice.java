package com.nexware.aajapan.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.nexware.aajapan.listeners.EntityModelBase;

@Document(collection = "t_sls_inv")
public class TSalesInvoice extends EntityModelBase {

	@Id
	private String id;
	@Indexed
	private String invoiceNo;
	private String customerId;
	private Integer customerFlag;
	private ObjectId consigneeId;
	private ObjectId notifypartyId;
	private Integer currencyType;
	private Double exchangeRate;
	private String paymentType;
	private String stockNo;
	private Double total;
	private Integer status;
	private Double dollarRate;
	private String salesPerson;
	private Double amountAllocatted;
	private Double amountReceived;
	@Transient
	private Double balance;

	public TSalesInvoice() {

	}

	public TSalesInvoice(String customerId, Integer customerFlag, ObjectId consigneeId, ObjectId notifypartyId,
			Integer currencyType, String paymentType, String stockNo) {
		super();
		this.customerId = customerId;
		this.customerFlag = customerFlag;
		this.consigneeId = consigneeId;
		this.notifypartyId = notifypartyId;
		this.currencyType = currencyType;
		this.paymentType = paymentType;
		this.stockNo = stockNo;
	}

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

	public Integer getCustomerFlag() {
		return this.customerFlag;
	}

	public void setCustomerFlag(Integer customerFlag) {
		this.customerFlag = customerFlag;
	}

	public ObjectId getConsigneeId() {
		return this.consigneeId;
	}

	public void setConsigneeId(ObjectId consigneeId) {
		this.consigneeId = consigneeId;
	}

	public ObjectId getNotifypartyId() {
		return this.notifypartyId;
	}

	public void setNotifypartyId(ObjectId notifypartyId) {
		this.notifypartyId = notifypartyId;
	}

	public String getPaymentType() {
		return this.paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getStockNo() {
		return this.stockNo;
	}

	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}

	/*
	 * public Double getTotal() { this.fob = AppUtil.ifNull(this.fob, 0.0);
	 * this.freight = AppUtil.ifNull(this.freight, 0.0); this.insurance =
	 * AppUtil.ifNull(this.insurance, 0.0); this.shipping =
	 * AppUtil.ifNull(this.shipping, 0.0); this.total = this.fob + this.freight +
	 * this.insurance + this.shipping; return this.total; }
	 * 
	 * public void setTotal(Double total) { this.total = total; }
	 */

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getCurrencyType() {
		return this.currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public Double getExchangeRate() {
		return this.exchangeRate;
	}

	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public Double getDollarRate() {
		return this.dollarRate;
	}

	public void setDollarRate(Double dollarRate) {
		this.dollarRate = dollarRate;
	}

	public String getSalesPerson() {
		return this.salesPerson;
	}

	public void setSalesPerson(String salesPerson) {
		this.salesPerson = salesPerson;
	}

	public Double getAmountReceived() {
		return this.amountReceived;
	}

	public void setAmountReceived(Double amountReceived) {
		this.amountReceived = amountReceived;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Double getBalance() {
		this.balance = this.getTotal() - this.amountReceived;
		return this.balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Double getAmountAllocatted() {
		return this.amountAllocatted;
	}

	public void setAmountAllocatted(Double amountAllocatted) {
		this.amountAllocatted = amountAllocatted;
	}

}
