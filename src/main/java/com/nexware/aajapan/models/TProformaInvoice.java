package com.nexware.aajapan.models;

import java.util.Date;
import java.util.List;

import javax.persistence.Transient;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.listeners.EntityModelBase;
import com.nexware.aajapan.utils.AppUtil;

@Document(collection = "t_prfrm_invc")
public class TProformaInvoice extends EntityModelBase {
	@Id
	private String id;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date date;
	@Indexed(unique = true)
	private String invoiceNo;
	private String customerId;
	private ObjectId consigneeId;
	private ObjectId notifypartyId;
	private String paymentType;
	private Integer currencyType;
	private Double exchangeRate;
	private List<ProformaInvoiceItem> items;
	@Transient
	private Double fobTotal;
	@Transient
	private Double freightTotal;
	@Transient
	private Double insuranceTotal;
	@Transient
	private Double shippingTotal;
	@Transient
	private Double total;
	private String salesPerson;

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
		return this.consigneeId.toString();
	}

	public void setConsigneeId(String consigneeId) {
		this.consigneeId = new ObjectId(consigneeId);
	}

	public String getNotifypartyId() {
		return !AppUtil.isObjectEmpty(this.notifypartyId) ? this.notifypartyId.toString() : null;
	}

	public void setNotifypartyId(String notifypartyId) {
		this.notifypartyId = new ObjectId(notifypartyId);

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

	public Double getInsuranceTotal() {
		return this.insuranceTotal;
	}

	public void setInsuranceTotal(Double insuranceTotal) {
		this.insuranceTotal = insuranceTotal;
	}

	public Double getTotal() {
		return this.total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Double getShippingTotal() {
		return this.shippingTotal;
	}

	public void setShippingTotal(Double shippingTotal) {
		this.shippingTotal = shippingTotal;
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

	public void setConsigneeId(ObjectId consigneeId) {
		this.consigneeId = consigneeId;
	}

	public void setNotifypartyId(ObjectId notifypartyId) {
		this.notifypartyId = notifypartyId;
	}

	public String getSalesPerson() {
		return this.salesPerson;
	}

	public void setSalesPerson(String salesPerson) {
		this.salesPerson = salesPerson;
	}

}
