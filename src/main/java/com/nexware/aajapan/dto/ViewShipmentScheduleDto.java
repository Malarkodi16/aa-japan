package com.nexware.aajapan.dto;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.text.SimpleDateFormat;
import com.nexware.aajapan.models.Schedule;
import com.nexware.aajapan.utils.AppUtil;

public class ViewShipmentScheduleDto {
	private String id;
	private String scheduleId;
	private String shipmentCompany;
	private String vesselName;
	private String voyageNo;
	private String deckHeight;
	private String startCountry;
	private String startPort;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date startDate;
	private String sStartDate;
	private String destinationCountry;
	private String destinationPort;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date endDate;
	private String sEndDate;
	private List<Schedule> schedule;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getShipmentCompany() {
		return this.shipmentCompany;
	}

	public void setShipmentCompany(String shipmentCompany) {
		this.shipmentCompany = shipmentCompany;
	}

	public String getVesselName() {
		return this.vesselName;
	}

	public void setVesselName(String vesselName) {
		this.vesselName = vesselName;
	}

	public String getVoyageNo() {
		return this.voyageNo;
	}

	public void setVoyageNo(String voyageNo) {
		this.voyageNo = voyageNo;
	}

	public String getDeckHeight() {
		return this.deckHeight;
	}

	public void setDeckHeight(String deckHeight) {
		this.deckHeight = deckHeight;
	}

	public String getStartCountry() {
		return this.startCountry;
	}

	public void setStartCountry(String startCountry) {
		this.startCountry = startCountry;
	}

	public String getStartPort() {
		return this.startPort;
	}

	public void setStartPort(String startPort) {
		this.startPort = startPort;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getDestinationCountry() {
		return this.destinationCountry;
	}

	public void setDestinationCountry(String destinationCountry) {
		this.destinationCountry = destinationCountry;
	}

	public String getDestinationPort() {
		return this.destinationPort;
	}

	public void setDestinationPort(String destinationPort) {
		this.destinationPort = destinationPort;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public List<Schedule> getSchedule() {
		return this.schedule;
	}

	public void setSchedule(List<Schedule> schedule) {
		this.schedule = schedule;
	}

	public String getScheduleId() {
		return this.scheduleId;
	}

	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
	}

	public String getsStartDate() {
		this.sStartDate = !AppUtil.isObjectEmpty(this.startDate)
				? new SimpleDateFormat("dd-MM-yyyy").format(this.startDate)
				: "";
		return this.sStartDate;
	}

	public void setsStartDate(String sStartDate) {
		this.sStartDate = sStartDate;
	}

	public String getsEndDate() {
		this.sEndDate = !AppUtil.isObjectEmpty(this.endDate) ? new SimpleDateFormat("dd-MM-yyyy").format(this.endDate)
				: "";
		return this.sEndDate;
	}

	public void setsEndDate(String sEndDate) {
		this.sEndDate = sEndDate;
	}

}
