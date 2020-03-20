package com.nexware.aajapan.dto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.utils.AppUtil;

public class TTransportOrderInvoiceDto {
	private String transporterCode;
	private String transporterName;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date auctionDate;// refer t_stck purchase date
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date pickupDate;
	private String spickupDate;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date deliveryDate;
	private String sDeliveryDate;
	private String deliveryDateNote;
	private Integer selectedDate;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date dueDate;// refer t_prchs_invc
	private Integer scheduleType;// refer trnsprt_ordr_items
	private String sDueDate;// urgent or asap
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date createdDate;
	private String invoiceNo;
	private List<TTransportOrderInvoiceItemDto> orderItem;
	private String pickupTime;
	private String deliveryTime;
	private String commentAppended;
	private List<String> comment;

	public String getTransporterCode() {
		return this.transporterCode;
	}

	public void setTransporterCode(String transporterCode) {
		this.transporterCode = transporterCode;
	}

	public String getTransporterName() {
		return this.transporterName;
	}

	public void setTransporterName(String transporterName) {
		this.transporterName = transporterName;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public List<TTransportOrderInvoiceItemDto> getOrderItem() {
		return this.orderItem;
	}

	public void setOrderItem(List<TTransportOrderInvoiceItemDto> orderItem) {
		this.orderItem = orderItem;
	}

	public Date getAuctionDate() {
		return this.auctionDate;
	}

	public void setAuctionDate(Date auctionDate) {
		this.auctionDate = auctionDate;
	}

	public Date getPickupDate() {
		return this.pickupDate;
	}

	public void setPickupDate(Date pickupDate) {
		this.pickupDate = pickupDate;
	}

	public String getSpickupDate() {
		if (this.selectedDate == Constants.TRANSPORT_PICKUP_BEFORE_PAYMENT) {
			this.spickupDate = Constants.TRANSPORT_REQUEST_PICKUP_DATE_NOTE;
		} else if (this.selectedDate == Constants.TRANSPORT_PICKUP_AFTER_PAYMENT) {
			this.spickupDate = new SimpleDateFormat("yyyy/MM/dd").format(this.pickupDate);
		} else {
			this.spickupDate = "";
		}
		return this.spickupDate;
	}

	public void setSpickupDate(String spickupDate) {
		this.spickupDate = spickupDate;
	}

	public String getDueDate() {
		return AppUtil.isObjectEmpty(this.dueDate) ? "" : new SimpleDateFormat("yyyy/MM/dd").format(this.dueDate);
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public Integer getScheduleType() {
		return this.scheduleType;
	}

	public void setScheduleType(Integer scheduleType) {
		this.scheduleType = scheduleType;
	}

	public String getsDueDate() {
		if (this.selectedDate == Constants.TRANSPORT_PICKUP_BEFORE_PAYMENT) {
			this.sDueDate = Constants.TRANSPORT_REQUEST_DUE_DATE_NOTE;
		} else if (this.selectedDate == Constants.TRANSPORT_PICKUP_AFTER_PAYMENT) {
			this.sDueDate = new SimpleDateFormat("yyyy/MM/dd").format(this.dueDate);
		} else {
			this.sDueDate = "";
		}
		return this.sDueDate;
	}

	public void setsDueDate(String sDueDate) {
		this.sDueDate = sDueDate;
	}

	public Date getDeliveryDate() {
		return this.deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getsDeliveryDate() {

		this.sDeliveryDate = new SimpleDateFormat("yyyy/MM/dd").format(this.deliveryDate);

		return this.sDeliveryDate;
	}

	public String getDeliveryDateNote() {
		if (this.scheduleType == Constants.TTRANSPORT_SCHEDULE_TYPE_ASAP) {
			this.deliveryDateNote = Constants.TRANSPORT_REQUEST_DELIVERY_NOTE_ASAP;
		} else if (this.scheduleType == Constants.TTRANSPORT_SCHEDULE_TYPE_URGENT) {
			this.deliveryDateNote = Constants.TRANSPORT_REQUEST_DELIVERY_NOTE_URGENT;
		} else {
			this.deliveryDateNote = new SimpleDateFormat("yyyy/MM/dd").format(this.deliveryDate);
		}
		return this.deliveryDateNote;

	}

	public void setDeliveryDateNote(String deliveryDateNote) {
		this.deliveryDateNote = deliveryDateNote;
	}

	public void setsDeliveryDate(String sDeliveryDate) {
		this.sDeliveryDate = sDeliveryDate;
	}

	public String getInvoiceNo() {
		return this.invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public Integer getSelectedDate() {
		return this.selectedDate;
	}

	public void setSelectedDate(Integer selectedDate) {
		this.selectedDate = selectedDate;
	}

	public String getPickupTime() {
		return pickupTime;
	}

	public void setPickupTime(String pickupTime) {
		this.pickupTime = pickupTime;
	}

	public String getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(String deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public String getCommentAppended() {
		return commentAppended;
	}

	public void setCommentAppended(String commentAppended) {
		this.commentAppended = commentAppended;
	}

	public List<String> getComment() {
		return comment;
	}

	public void setComment(List<String> comment) {
		this.comment = comment;
	}

}
