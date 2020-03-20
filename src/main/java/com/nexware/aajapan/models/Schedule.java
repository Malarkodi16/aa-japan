package com.nexware.aajapan.models;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Schedule {

	private ObjectId id;
	private String scheduleNo;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date date;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date soCutDate;
	private String portName;
	private String subVessel;
	private int route;
	private String portFlag;

	public Schedule(Date date, String portName, String subVessel, String portFlag) {
		super();
		this.date = date;
//		this.country = country;
		this.portName = portName;
		this.subVessel = subVessel;
		this.portFlag = portFlag;
	}

	public Schedule() {
		id = ObjectId.get();
	}

	public String getId() {
		return id.toString();
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getScheduleNo() {
		return scheduleNo;
	}

	public void setScheduleNo(String scheduleNo) {
		this.scheduleNo = scheduleNo;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

//	public String getCountry() {
//		return country;
//	}
//
//	public void setCountry(String country) {
//		this.country = country;
//	}

	public String getPortName() {
		return portName;
	}

	public void setPortName(String portName) {
		this.portName = portName;
	}

	public String getSubVessel() {
		return subVessel;
	}

	public void setSubVessel(String subVessel) {
		this.subVessel = subVessel;
	}

	public int getRoute() {
		return route;
	}

	public void setRoute(int route) {
		this.route = route;
	}

	public String getPortFlag() {
		return portFlag;
	}

	public void setPortFlag(String portFlag) {
		this.portFlag = portFlag;
	}

	public Date getSoCutDate() {
		return soCutDate;
	}

	public void setSoCutDate(Date soCutDate) {
		this.soCutDate = soCutDate;
	}

}
