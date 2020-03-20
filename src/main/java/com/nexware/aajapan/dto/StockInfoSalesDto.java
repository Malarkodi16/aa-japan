package com.nexware.aajapan.dto;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.models.ConsigneeNotifyparty;
import com.nexware.aajapan.models.MUser;
import com.nexware.aajapan.models.TCustomer;
import com.nexware.aajapan.models.TSalesInvoice;
import com.nexware.aajapan.utils.AppUtil;

public class StockInfoSalesDto {

	private String invoiceNo;
	private String customerId;
	private String customerName;
	private String customerType;// Constants.CUSTOMER_FLAG_1 // customer or branch
	private ObjectId consigneeId;
	private String consigneeName;
	private ObjectId notifypartyId;
	private String notifypartyName;
	private Integer currencyType;
	private String currencyTypeTextValue;
	private Double exchangeRate;
	private String paymentType;

	private Double total;
	private Integer status;
	private String statusTextValue;
	private String salesPersonId;
	private String salesPersonIdName;
	private Double amountAllocatted;
	private Double amountReceived;

	public StockInfoSalesDto(TSalesInvoice salesInvoice, TCustomer customer, MUser salesPerson) {
		prepareData(salesInvoice, customer, salesPerson);
	}

	public StockInfoSalesDto() {

	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public ObjectId getConsigneeId() {
		return consigneeId;
	}

	public void setConsigneeId(ObjectId consigneeId) {
		this.consigneeId = consigneeId;
	}

	public ObjectId getNotifypartyId() {
		return notifypartyId;
	}

	public void setNotifypartyId(ObjectId notifypartyId) {
		this.notifypartyId = notifypartyId;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		if (!AppUtil.isObjectEmpty(currencyType)) {
			if (currencyType.equals(Constants.CURRENCY_YEN)) {
				currencyTypeTextValue = "YEN";
			} else if (currencyType.equals(Constants.CURRENCY_USD)) {
				currencyTypeTextValue = "USD";
			} else if (currencyType.equals(Constants.CURRENCY_AUD)) {
				currencyTypeTextValue = "AUD";
			} else if (currencyType.equals(Constants.CURRENCY_POUND)) {
				currencyTypeTextValue = "POUND";
			}
		}
		this.currencyType = currencyType;
	}

	public Double getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		if (AppUtil.isObjectEmpty(status)) {
			if (status.equals(Constants.SALES_INV_PAYMENT_NOT_RECEIVED)) {
				statusTextValue = "PAYMENT NOT RECEIVED";
			} else if (status.equals(Constants.SALES_INV_PAYMENT_RECEIVED)) {
				statusTextValue = "PAYMENT RECEIVED";
			} else if (status.equals(Constants.SALES_INV_PAYMENT_RECEIVED_PARTIAL)) {
				statusTextValue = "PAYMENT PARTIALLY RECEIVED";
			} else if (status.equals(Constants.SALES_INV_CANCEL)) {
				statusTextValue = "INVOICE CANCELLED";
			}
		}
		this.status = status;
	}

	public String getCurrencyTypeTextValue() {
		return currencyTypeTextValue;
	}

	public void setCurrencyTypeTextValue(String currencyTypeTextValue) {
		this.currencyTypeTextValue = currencyTypeTextValue;
	}

	public String getSalesPersonId() {
		return salesPersonId;
	}

	public void setSalesPersonId(String salesPersonId) {
		this.salesPersonId = salesPersonId;
	}

	public String getSalesPersonIdName() {
		return salesPersonIdName;
	}

	public void setSalesPersonIdName(String salesPersonIdName) {
		this.salesPersonIdName = salesPersonIdName;
	}

	public Double getAmountAllocatted() {
		return amountAllocatted;
	}

	public void setAmountAllocatted(Double amountAllocatted) {
		this.amountAllocatted = amountAllocatted;
	}

	public Double getAmountReceived() {
		return amountReceived;
	}

	public void setAmountReceived(Double amountReceived) {
		this.amountReceived = amountReceived;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public String getConsigneeName() {
		return consigneeName;
	}

	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}

	public String getNotifypartyName() {
		return notifypartyName;
	}

	public void setNotifypartyName(String notifypartyName) {
		this.notifypartyName = notifypartyName;
	}

	public String getStatusTextValue() {
		return statusTextValue;
	}

	public void setStatusTextValue(String statusTextValue) {
		this.statusTextValue = statusTextValue;
	}

	private void prepareData(TSalesInvoice salesInvoice, TCustomer customer, MUser salesPerson) {
		if (!AppUtil.isObjectEmpty(salesInvoice)) {
			this.setInvoiceNo(salesInvoice.getInvoiceNo());
			this.setCustomerId(salesInvoice.getCustomerId());
			if (salesInvoice.getCustomerFlag().equals(Constants.CUSTOMER_FLAG_BRANCH)) {
				this.customerType = "BRANCH";
			} else if (salesInvoice.getCustomerFlag().equals(Constants.CUSTOMER_FLAG_CUSTOMER)) {
				this.customerType = "CUSTOMER";
			}
			this.setCurrencyType(salesInvoice.getCurrencyType());
			this.setExchangeRate(salesInvoice.getExchangeRate());
			this.setPaymentType(salesInvoice.getPaymentType());
			this.setTotal(salesInvoice.getTotal());
			this.setStatus(salesInvoice.getStatus());
			this.setAmountAllocatted(salesInvoice.getAmountAllocatted());
			this.setAmountReceived(salesInvoice.getAmountReceived());
			this.setSalesPersonId(salesInvoice.getSalesPerson());

		}
		if (!AppUtil.isObjectEmpty(customer)) {
			this.setCustomerName(customer.getFirstName());
			List<ConsigneeNotifyparty> consigneeNotifyparties = customer.getConsigneeNotifyparties();
			if (!AppUtil.isObjectEmpty(consigneeNotifyparties)) {
				Optional<ConsigneeNotifyparty> consignee = consigneeNotifyparties.stream()
						.filter(con -> con.getId().equals(salesInvoice.getConsigneeId())).findFirst();
				if (consignee.isPresent()) {
					this.setConsigneeName(consignee.get().getcFirstName());
				}
				Optional<ConsigneeNotifyparty> notifyParty = consigneeNotifyparties.stream()
						.filter(np -> np.getId().equals(salesInvoice.getNotifypartyId())).findFirst();
				if (notifyParty.isPresent()) {
					this.setNotifypartyName(notifyParty.get().getNpFirstName());
				}
			}
		}

		if (!AppUtil.isObjectEmpty(salesPerson)) {
			this.setSalesPersonIdName(salesPerson.getFullname());
		}

	}
}
