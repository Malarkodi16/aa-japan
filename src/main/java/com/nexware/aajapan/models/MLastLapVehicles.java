package com.nexware.aajapan.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.nexware.aajapan.listeners.EntityModelBase;

@Document(collection = "m_lt_lp_vhcls")
public class MLastLapVehicles extends EntityModelBase{
	@Id
	private String id;
	private int expiryDays;
	private int warningDays;
	private long expiryMilliSeconds;
	private long warningDaysMilliSeconds;
	@Indexed
	private String destinationCountry;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getExpiryDays() {
		return this.expiryDays;
	}

	public void setExpiryDays(int expiryDays) {
		this.expiryDays = expiryDays;
	}

	public int getWarningDays() {
		return this.warningDays;
	}

	public void setWarningDays(int warningDays) {
		this.warningDays = warningDays;
	}

	public long getExpiryMilliSeconds() {
		return this.expiryMilliSeconds;
	}

	public void setExpiryMilliSeconds(long expiryMilliSeconds) {
		this.expiryMilliSeconds = expiryMilliSeconds;
	}

	public long getWarningDaysMilliSeconds() {
		return this.warningDaysMilliSeconds;
	}

	public void setWarningDaysMilliSeconds(long warningDaysMilliSeconds) {
		this.warningDaysMilliSeconds = warningDaysMilliSeconds;
	}

	public String getDestinationCountry() {
		return this.destinationCountry;
	}

	public void setDestinationCountry(String destinationCountry) {
		this.destinationCountry = destinationCountry;
	}

}
