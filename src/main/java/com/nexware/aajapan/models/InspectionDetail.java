package com.nexware.aajapan.models;

public class InspectionDetail {
	private String country;
	private String inspectionRequestId;

	public InspectionDetail(String country, String inspectionRequestId) {
		this.country = country;
		this.inspectionRequestId = inspectionRequestId;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getInspectionRequestId() {
		return inspectionRequestId;
	}

	public void setInspectionRequestId(String inspectionRequestId) {
		this.inspectionRequestId = inspectionRequestId;
	}

}
