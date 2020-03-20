package com.nexware.aajapan.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class ScheduleSaveDto {
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date date;
	private String country;

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPortName() {
		return this.portName;
	}

	public void setPortName(String portName) {
		this.portName = portName;
	}

	public String getSubVessel() {
		return this.subVessel;
	}

	public void setSubVessel(String subVessel) {
		this.subVessel = subVessel;
	}

	private String portName;
	private String subVessel;
}
