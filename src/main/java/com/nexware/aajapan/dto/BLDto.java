package com.nexware.aajapan.dto;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.utils.AppUtil;

public class BLDto {

	private String id;
	private String chassisNo;
	private String customer;
	private String staff;
	private String destinationCountry;
	private String destinationPortName;
	private String vessalName;
	private String vessalNo;
	private Double soldAmount;
	private Double amountReceived;
	private Double balanceAmount;
	private String lotNo;
	private String blNo;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date eta;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date etd;
	private String status;
	private Integer showBlAmendBtn;
	private String shippingInstructionId;
	private String customerId;
	private String consigneeId;
	private String shipmentRequestId;
	private String staffId;
	private String blTransactionNo;
	private Integer recSurStatus;
	private Integer blDocumentStatus;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getChassisNo() {
		return this.chassisNo;
	}

	public void setChassisNo(String chassisNo) {
		this.chassisNo = chassisNo;
	}

	public String getCustomer() {
		return this.customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getStaff() {
		return this.staff;
	}

	public void setStaff(String staff) {
		this.staff = staff;
	}

	public String getDestinationCountry() {
		return destinationCountry;
	}

	public void setDestinationCountry(String destinationCountry) {
		this.destinationCountry = destinationCountry;
	}

	public String getDestinationPortName() {
		return this.destinationPortName;
	}

	public void setDestinationPortName(String destinationPortName) {
		this.destinationPortName = destinationPortName;
	}

	public String getVessalName() {
		return this.vessalName;
	}

	public void setVessalName(String vessalName) {
		this.vessalName = vessalName;
	}

	public String getVessalNo() {
		return this.vessalNo;
	}

	public void setVessalNo(String vessalNo) {
		this.vessalNo = vessalNo;
	}

	public Double getSoldAmount() {
		return this.soldAmount;
	}

	public void setSoldAmount(Double soldAmount) {
		this.soldAmount = soldAmount;
	}

	public Double getAmountReceived() {
		return this.amountReceived;
	}

	public void setAmountReceived(Double amountReceived) {
		this.amountReceived = amountReceived;
	}

	public Double getBalanceAmount() {
		this.balanceAmount = AppUtil.ifNull(this.soldAmount, 0.0) - AppUtil.ifNull(this.amountReceived, 0.0);
		return this.balanceAmount;
	}

	public void setBalanceAmount(Double balanceAmount) {
		this.balanceAmount = balanceAmount;
	}

	public String getLotNo() {
		return this.lotNo;
	}

	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}

	public String getBlNo() {
		return blNo;
	}

	public void setBlNo(String blNo) {
		this.blNo = blNo;
	}

	public Date getEta() {
		return this.eta;
	}

	public void setEta(Date eta) {
		this.eta = eta;
	}

	public Date getEtd() {
		return this.etd;
	}

	public void setEtd(Date etd) {
		this.etd = etd;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getShowBlAmendBtn() {
		Date today = new Date();
		Date eta = this.getEta();
		long duration = eta.getTime() - today.getTime();
//		long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);
//		long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
//		long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);
		long diffInDays = TimeUnit.MILLISECONDS.toDays(duration);
		if (diffInDays >= 15) {
			showBlAmendBtn = 1;
		} else {
			showBlAmendBtn = 0;
		}
		return showBlAmendBtn;
	}

	public void setShowBlAmendBtn(Integer showBlAmendBtn) {
		this.showBlAmendBtn = showBlAmendBtn;
	}

	public String getShippingInstructionId() {
		return shippingInstructionId;
	}

	public void setShippingInstructionId(String shippingInstructionId) {
		this.shippingInstructionId = shippingInstructionId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getConsigneeId() {
		return consigneeId;
	}

	public void setConsigneeId(String consigneeId) {
		this.consigneeId = consigneeId;
	}

	public String getShipmentRequestId() {
		return shipmentRequestId;
	}

	public void setShipmentRequestId(String shipmentRequestId) {
		this.shipmentRequestId = shipmentRequestId;
	}

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	public String getBlTransactionNo() {
		return blTransactionNo;
	}

	public void setBlTransactionNo(String blTransactionNo) {
		this.blTransactionNo = blTransactionNo;
	}

	public Integer getRecSurStatus() {
		return recSurStatus;
	}

	public void setRecSurStatus(Integer recSurStatus) {
		this.recSurStatus = recSurStatus;
	}

	public Integer getBlDocumentStatus() {
		return blDocumentStatus;
	}

	public void setBlDocumentStatus(Integer blDocumentStatus) {
		this.blDocumentStatus = blDocumentStatus;
	}

}
