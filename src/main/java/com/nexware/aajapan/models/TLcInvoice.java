package com.nexware.aajapan.models;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.listeners.EntityModelBase;

/**
 * @author Karthik
 *
 */
@Document(collection = "t_lc_invc")
public class TLcInvoice extends EntityModelBase {
	@Id
	private String id;
	@Indexed
	private String lcDtlId;
	private String stockNo;
	private String chassisNo;
	private String maker;
	private String model;
	private String hsCode;
	private String customerId;
	private Double fob;
	private Double freight;
	private Double insurance;
	private Double amount;
	private Double amountAllocatted;
	private String proformaInvoiceId;
	private String proformaInvoiceNo;
	private String lcInvoiceNo;
	private String schedule;
	private Integer isBolUpdated;
	private String shippingMarksId;
	private String shippingMarks;
	private String perVessel;
	private String to;
	private String from;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date sailingDate;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date bankSentDate;
	private Integer status;
	public TLcInvoice(String lcDtlId, String stockNo, String chassisNo, String maker, String model, String hsCode,
			String customerId, Double amount, String proformaInvoiceId, String lcInvoiceNo, String schedule,
			Integer isBolUpdated
	// String perVessel,String from, String to, Date sailingDate
	) {
		super();
		this.lcDtlId = lcDtlId;
		this.stockNo = stockNo;
		this.chassisNo = chassisNo;
		this.maker = maker;
		this.model = model;
		this.hsCode = hsCode;
		this.customerId = customerId;
		this.amount = amount;
		this.proformaInvoiceId = proformaInvoiceId;
		this.lcInvoiceNo = lcInvoiceNo;
		this.schedule = schedule;
		this.isBolUpdated = isBolUpdated;
	}

	public TLcInvoice() {
		
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLcDtlId() {
		return this.lcDtlId;
	}

	public void setLcDtlId(String lcDtlId) {
		this.lcDtlId = lcDtlId;
	}

	public String getStockNo() {
		return this.stockNo;
	}

	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}

	public String getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public Double getAmount() {
		return this.amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getProformaInvoiceId() {
		return this.proformaInvoiceId;
	}

	public void setProformaInvoiceId(String proformaInvoiceId) {
		this.proformaInvoiceId = proformaInvoiceId;
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

	public String getHsCode() {
		return this.hsCode;
	}

	public void setHsCode(String hsCode) {
		this.hsCode = hsCode;
	}

	public String getChassisNo() {
		return this.chassisNo;
	}

	public void setChassisNo(String chassisNo) {
		this.chassisNo = chassisNo;
	}

	public String getLcInvoiceNo() {
		return this.lcInvoiceNo;
	}

	public void setLcInvoiceNo(String lcInvoiceNo) {
		this.lcInvoiceNo = lcInvoiceNo;
	}

	public String getSchedule() {
		return schedule;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}

	public Integer getIsBolUpdated() {
		return isBolUpdated;
	}

	public void setIsBolUpdated(Integer isBolUpdated) {
		this.isBolUpdated = isBolUpdated;
	}

	public String getShippingMarks() {
		return shippingMarks;
	}

	public void setShippingMarks(String shippingMarks) {
		this.shippingMarks = shippingMarks;
	}

	public String getPerVessel() {
		return perVessel;
	}

	public void setPerVessel(String perVessel) {
		this.perVessel = perVessel;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public Date getSailingDate() {
		return sailingDate;
	}

	public void setSailingDate(Date sailingDate) {
		this.sailingDate = sailingDate;
	}

	public Date getBankSentDate() {
		return bankSentDate;
	}

	public void setBankSentDate(Date bankSentDate) {
		this.bankSentDate = bankSentDate;
	}

	public String getShippingMarksId() {
		return shippingMarksId;
	}

	public void setShippingMarksId(String shippingMarksId) {
		this.shippingMarksId = shippingMarksId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getProformaInvoiceNo() {
		return proformaInvoiceNo;
	}

	public void setProformaInvoiceNo(String proformaInvoiceNo) {
		this.proformaInvoiceNo = proformaInvoiceNo;
	}

	public Double getFob() {
		return fob;
	}

	public void setFob(Double fob) {
		this.fob = fob;
	}

	public Double getFreight() {
		return freight;
	}

	public void setFreight(Double freight) {
		this.freight = freight;
	}

	public Double getInsurance() {
		return insurance;
	}

	public void setInsurance(Double insurance) {
		this.insurance = insurance;
	}

	public Double getAmountAllocatted() {
		return amountAllocatted;
	}

	public void setAmountAllocatted(Double amountAllocatted) {
		this.amountAllocatted = amountAllocatted;
	}
	
}
