package com.nexware.aajapan.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.nexware.aajapan.listeners.EntityModelBase;
import com.nexware.aajapan.utils.AppUtil;

@Document(collection = "t_dybk_trnsctn")
public class TDayBookTransaction extends EntityModelBase {
	@Id
	private String id;

	private String transactionId;// auto
	@Indexed
	private String daybookId;// refer t_day_book
	private String salesInvoiceId;// refer t_sls_inv
	private String stockId;// refer t_sls_inv
	private Double amount;
	private Integer transactionType;
	private Integer allocationType;
	private Integer paymentApprove;
	private Double exchangeRate;
	private Double exchangeRate1;
	private Double exchangeRate2;
	private Double soExchRate;
	private String customerId;
	private String salesPersonId;
	private Integer currency;
	private Integer customerCurrency;
	private Double advanceOwned;
	private Double balance;
	private Integer amountRefund;
	private Double allocatedAmount;

	public TDayBookTransaction(String daybookId, String salesInvoiceId, String stockId, Double amount,
			Integer transactionType, Integer allocationType, Integer paymentApprove, Double exchangeRate,
			Double soExchRate) {
		super();
		this.daybookId = daybookId;
		this.salesInvoiceId = salesInvoiceId;
		this.stockId = stockId;
		this.amount = amount;
		this.transactionType = transactionType;
		this.allocationType = allocationType;
		this.paymentApprove = paymentApprove;
		this.exchangeRate = exchangeRate;
		this.soExchRate = soExchRate;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTransactionId() {
		return this.transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public Double getSoExchRate() {
		return this.soExchRate;
	}

	public void setSoExchRate(Double soExchRate) {
		this.soExchRate = soExchRate;
	}

	public Double getExchangeRate() {
		return this.exchangeRate;
	}

	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public String getDaybookId() {
		return this.daybookId;
	}

	public void setDaybookId(String daybookId) {
		this.daybookId = daybookId;
	}

	public String getSalesInvoiceId() {
		return this.salesInvoiceId;
	}

	public void setSalesInvoiceId(String salesInvoiceId) {
		this.salesInvoiceId = salesInvoiceId;
	}

	public String getStockId() {
		return this.stockId;
	}

	public void setStockId(String stockId) {
		this.stockId = stockId;
	}

	public Double getAmount() {
		return this.amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Integer getTransactionType() {
		return this.transactionType;
	}

	public void setTransactionType(Integer transactionType) {
		this.transactionType = transactionType;
	}

	public Integer getAllocationType() {
		return this.allocationType;
	}

	public void setAllocationType(Integer allocationType) {
		this.allocationType = allocationType;
	}

	public Integer getPaymentApprove() {
		return this.paymentApprove;
	}

	public void setPaymentApprove(Integer paymentApprove) {
		this.paymentApprove = paymentApprove;
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

	public Integer getCurrency() {
		return this.currency;
	}

	public void setCurrency(Integer currency) {
		this.currency = currency;
	}

	public Double getAdvanceOwned() {
		return advanceOwned;
	}

	public void setAdvanceOwned(Double advanceOwned) {
		this.advanceOwned = advanceOwned;
	}

	public Double getBalance() {
		balance = this.amount - AppUtil.ifNull(this.advanceOwned, 0.0);
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Integer getAmountRefund() {
		return amountRefund;
	}

	public void setAmountRefund(Integer amountRefund) {
		this.amountRefund = amountRefund;
	}

	public Double getAllocatedAmount() {
		return allocatedAmount;
	}

	public void setAllocatedAmount(Double allocatedAmount) {
		this.allocatedAmount = allocatedAmount;
	}

	public Double getExchangeRate1() {
		return exchangeRate1;
	}

	public void setExchangeRate1(Double exchangeRate1) {
		this.exchangeRate1 = exchangeRate1;
	}

	public Double getExchangeRate2() {
		return exchangeRate2;
	}

	public void setExchangeRate2(Double exchangeRate2) {
		this.exchangeRate2 = exchangeRate2;
	}

	public Integer getCustomerCurrency() {
		return customerCurrency;
	}

	public void setCustomerCurrency(Integer customerCurrency) {
		this.customerCurrency = customerCurrency;
	}

}
