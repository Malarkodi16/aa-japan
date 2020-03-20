package com.nexware.aajapan.dto;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.listeners.EntityModelBase;
import com.nexware.aajapan.models.InspectionDetail;
import com.nexware.aajapan.utils.AppUtil;

public class TLastLapVehiclesDto extends EntityModelBase {
	@Id
	private String id;
	@NotBlank
	@Indexed(unique = true)
	private String stockNo;
	private String chassisNo;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date firstRegDate;
	private String model;
	private String hsCode;
	private String maker;
	private String destinationCountry;
	private int noOfMonths;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date warningDate;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date expiryDate;
	private Integer shippingStatus;
	private List<InspectionDetail> inspectionDetails;
	private Integer inspectionStatus;
	private Integer lastlapStatus;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStockNo() {
		return this.stockNo;
	}

	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}

	public String getChassisNo() {
		return this.chassisNo;
	}

	public void setChassisNo(String chassisNo) {
		this.chassisNo = chassisNo;
	}

	public Date getFirstRegDate() {
		return this.firstRegDate;
	}

	public void setFirstRegDate(Date firstRegDate) {
		this.firstRegDate = firstRegDate;
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

	public String getMaker() {
		return this.maker;
	}

	public void setMaker(String maker) {
		this.maker = maker;
	}

	public String getDestinationCountry() {
		return this.destinationCountry;
	}

	public void setDestinationCountry(String destinationCountry) {
		this.destinationCountry = destinationCountry;
	}

	public int getNoOfMonths() {
		return this.noOfMonths;
	}

	public void setNoOfMonths(int noOfMonths) {
		this.noOfMonths = noOfMonths;
	}

	public Date getWarningDate() {
		return this.warningDate;
	}

	public void setWarningDate(Date warningDate) {
		this.warningDate = warningDate;
	}

	public Date getExpiryDate() {
		return this.expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Integer getShippingStatus() {
		return this.shippingStatus;
	}

	public void setShippingStatus(Integer shippingStatus) {
		this.shippingStatus = shippingStatus;
	}

	public List<InspectionDetail> getInspectionDetails() {
		return this.inspectionDetails;
	}

	public void setInspectionDetails(List<InspectionDetail> inspectionDetails) {
		this.inspectionDetails = inspectionDetails;
	}

	public Integer getInspectionStatus() {

		if (!AppUtil.isObjectEmpty(this.inspectionDetails)) {
			this.inspectionStatus = this.inspectionDetails.stream()
					.anyMatch(country -> country.getCountry().equalsIgnoreCase(this.destinationCountry))
							? Constants.INSPECTION_DONE
							: Constants.INSPECTION_NOT_ARRANGED;
		}
		return this.inspectionStatus;
	}

	public void setInspectionStatus(Integer inspectionStatus) {
		this.inspectionStatus = inspectionStatus;
	}

	public Integer getLastlapStatus() {
		if (!AppUtil.isObjectEmpty(this.expiryDate)) {
			if (new Date().after(this.expiryDate)) {
				this.lastlapStatus = Constants.LAST_LAP_STATUS_2;
			} else {
				this.lastlapStatus = Constants.LAST_LAP_STATUS_1;
			}
		}
		return this.lastlapStatus;
	}

	public void setLastlapStatus(Integer lastlapStatus) {
		this.lastlapStatus = lastlapStatus;
	}

}
