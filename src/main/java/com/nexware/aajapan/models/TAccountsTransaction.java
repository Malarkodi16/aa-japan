package com.nexware.aajapan.models;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.nexware.aajapan.listeners.EntityModelBase;

@Document(collection = "t_acnt_trnsctn")
public class TAccountsTransaction extends EntityModelBase {
	@Id
	private String id;
	@Indexed
	private String transactionId;// auto
	private String refInvoiceNo;// invoice no
	private String remitTo;//
	private Long code;
	private String source;
	private int currency;
	private Integer type;
	private Double amount;
	private String remarks;
	private Double exchangeRate;
	private String category;
	private String stockNo;
	private Integer flag;
	private String key;
	private Double balance;
	private Double closingBalance;
	private Integer clearingAccount;
	private String description;
	@NotBlank
	private Date transactionDate;
	@NotBlank
	private Date invoiceDate;

	public TAccountsTransaction(String refInvoiceNo, String remitTo, Long code, int currency, Integer type,
			Double amount, String source, Date invoiceDate) {
		super();
		this.refInvoiceNo = refInvoiceNo;
		this.remitTo = remitTo;
		this.code = code;
		this.currency = currency;
		this.type = type;
		this.amount = amount;
		this.source = source;
		this.invoiceDate = invoiceDate;

	}

	public TAccountsTransaction(String refInvoiceNo, String remitTo, Long code, int currency, Integer type,
			Double amount, String source, String description, Date invoiceDate) {
		super();
		this.refInvoiceNo = refInvoiceNo;
		this.remitTo = remitTo;
		this.code = code;
		this.currency = currency;
		this.type = type;
		this.amount = amount;
		this.source = source;
		this.description = description;
		this.invoiceDate = invoiceDate;
	}

	public TAccountsTransaction() {

	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getCode() {
		return this.code;
	}

	public void setCode(Long code) {
		this.code = code;
	}

	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public int getCurrency() {
		return this.currency;
	}

	public void setCurrency(int currency) {
		this.currency = currency;
	}

	public Double getAmount() {

		return this.amount;
	}

	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Double getExchangeRate() {
		return this.exchangeRate;
	}

	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getStockNo() {
		return this.stockNo;
	}

	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}

	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Double getBalance() {
		return this.balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Integer getClearingAccount() {
		return this.clearingAccount;
	}

	public void setClearingAccount(Integer clearingAccount) {
		this.clearingAccount = clearingAccount;
	}

	public String getTransactionId() {
		return this.transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getRefInvoiceNo() {
		return this.refInvoiceNo;
	}

	public void setRefInvoiceNo(String refInvoiceNo) {
		this.refInvoiceNo = refInvoiceNo;
	}

	public String getRemitTo() {
		return this.remitTo;
	}

	public void setRemitTo(String remitTo) {
		this.remitTo = remitTo;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getClosingBalance() {
		return this.closingBalance;
	}

	public void setClosingBalance(Double closingBalance) {
		this.closingBalance = closingBalance;
	}

	public Date getTransactionDate() {
		return this.transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

}
