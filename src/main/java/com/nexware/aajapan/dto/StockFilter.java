
package com.nexware.aajapan.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.nexware.aajapan.utils.AppUtil;

public class StockFilter {

	private String[] stockNos;
	private String driven;
	private String destinationCountry;
	private String[] transmissions;
	private String[] makers;
	private String[] options;
	private String[] models;
	private String[] subModels;
	private String[] lotNos;
	private String[] modelTypes;
	private String[] keywords;
	private Double mileageMin;
	private Double mileageMax;
	private Double ccMin;
	private Double ccMax;
	private Double priceMin;
	private Double priceMax;
	private Double fobMin;
	private Double fobMax;
	private Date yearMin;
	private Date yearMax;
	private Date purchaseDateFrom;
	private Date purchaseDateTo;
	private String[] colors;
	private String[] grades;
	private String[] auctionGrades;
	private String[] vehicleCategories;
	private Integer flag;
	private Integer account;

	public StockFilter(HttpServletRequest request) throws ParseException {
		this.prepareDataTableRequest(request);
	}

	public String[] getStockNos() {
		return this.stockNos;
	}

	public void setStockNos(String[] stockNos) {
		this.stockNos = stockNos;
	}

	public String[] getOptions() {
		return this.options;
	}

	public void setOptions(String[] options) {
		this.options = options;
	}

	public String[] getSubModels() {
		return this.subModels;
	}

	public void setSubModels(String[] subModels) {
		this.subModels = subModels;
	}

	public String getDestinationCountry() {
		return this.destinationCountry;
	}

	public void setDestinationCountry(String destinationCountry) {
		this.destinationCountry = destinationCountry;
	}

	public String getDriven() {
		return this.driven;
	}

	public void setDriven(String driven) {
		this.driven = driven;
	}

	public String[] getTransmissions() {
		return this.transmissions;
	}

	public Double getFobMin() {
		return this.fobMin;
	}

	public void setFobMin(Double fobMin) {
		this.fobMin = fobMin;
	}

	public Double getFobMax() {
		return this.fobMax;
	}

	public void setFobMax(Double fobMax) {
		this.fobMax = fobMax;
	}

	public void setTransmissions(String[] transmissions) {
		this.transmissions = transmissions;
	}

	public String[] getMakers() {
		return this.makers;
	}

	public void setMakers(String[] makers) {
		this.makers = makers;
	}

	public Date getPurchaseDateFrom() {
		return this.purchaseDateFrom;
	}

	public void setPurchaseDateFrom(Date purchaseDateFrom) {
		this.purchaseDateFrom = purchaseDateFrom;
	}

	public Date getPurchaseDateTo() {
		return this.purchaseDateTo;
	}

	public void setPurchaseDateTo(Date purchaseDateTo) {
		this.purchaseDateTo = purchaseDateTo;
	}

	public String[] getModels() {
		return this.models;
	}

	public void setModels(String[] models) {
		this.models = models;
	}

	public String[] getLotNos() {
		return this.lotNos;
	}

	public void setLotNos(String[] lotNos) {
		this.lotNos = lotNos;
	}

	public String[] getModelTypes() {
		return this.modelTypes;
	}

	public void setModelTypes(String[] modelTypes) {
		this.modelTypes = modelTypes;
	}

	public String[] getKeywords() {
		return this.keywords;
	}

	public void setKeywords(String[] keywords) {
		this.keywords = keywords;
	}

	public Double getCcMin() {
		return this.ccMin;
	}

	public void setCcMin(Double ccMin) {
		this.ccMin = ccMin;
	}

	public Double getCcMax() {
		return this.ccMax;
	}

	public void setCcMax(Double ccMax) {
		this.ccMax = ccMax;
	}

	public Double getPriceMin() {
		return this.priceMin;
	}

	public void setPriceMin(Double priceMin) {
		this.priceMin = priceMin;
	}

	public Double getPriceMax() {
		return this.priceMax;
	}

	public void setPriceMax(Double priceMax) {
		this.priceMax = priceMax;
	}

	public Date getYearMin() {
		return this.yearMin;
	}

	public void setYearMin(Date yearMin) {
		this.yearMin = yearMin;
	}

	public Date getYearMax() {
		return this.yearMax;
	}

	public void setYearMax(Date yearMax) {
		this.yearMax = yearMax;
	}

	public Double getMileageMin() {
		return this.mileageMin;
	}

	public void setMileageMin(Double mileageMin) {
		this.mileageMin = mileageMin;
	}

	public Double getMileageMax() {
		return this.mileageMax;
	}

	public void setMileageMax(Double mileageMax) {
		this.mileageMax = mileageMax;
	}

	public String[] getColors() {
		return this.colors;
	}

	public void setColors(String[] colors) {
		this.colors = colors;
	}

	public String[] getGrades() {
		return this.grades;
	}

	public String[] getAuctionGrades() {
		return this.auctionGrades;
	}

	public void setAuctionGrades(String[] auctionGrades) {
		this.auctionGrades = auctionGrades;
	}

	public void setGrades(String[] grades) {
		this.grades = grades;
	}

	public String[] getVehicleCategories() {
		return this.vehicleCategories;
	}

	public void setVehicleCategories(String[] vehicleCategories) {
		this.vehicleCategories = vehicleCategories;
	}

	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	private void prepareDataTableRequest(HttpServletRequest request) throws ParseException {

		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		String tmp = "";
		this.setDriven(request.getParameter("criteria[driven]"));
		this.setTransmissions(request.getParameterValues("criteria[transmissions][]"));
		this.setMakers(request.getParameterValues("criteria[makers][]"));
		this.setOptions(request.getParameterValues("criteria[options][]"));
		this.setModels(request.getParameterValues("criteria[models][]"));
		this.setSubModels(request.getParameterValues("criteria[subModels][]"));
		this.setLotNos(request.getParameterValues("criteria[lotNos][]"));
		this.setModelTypes(request.getParameterValues("criteria[modelTypes][]"));
		this.setKeywords(request.getParameterValues("criteria[keywords][]"));
		this.setColors(request.getParameterValues("criteria[colors][]"));
		this.setGrades(request.getParameterValues("criteria[grades][]"));
		this.setStockNos(request.getParameterValues("criteria[stockNos][]"));
		this.setAuctionGrades(request.getParameterValues("criteria[auctionGrades][]"));
		this.setVehicleCategories(request.getParameterValues("criteria[vehicleCategories][]"));
		this.setDestinationCountry(request.getParameter("criteria[destinationCountry]"));

		tmp = request.getParameter("criteria[flag]");
		if (!AppUtil.isObjectEmpty(tmp)) {
			this.setFlag(Integer.parseInt(tmp));
		} else {
			this.setFlag(0);
		}
		tmp = request.getParameter("criteria[account]");
		if (!AppUtil.isObjectEmpty(tmp)) {
			this.setAccount(Integer.parseInt(tmp));
		} else {
			this.setAccount(0);
		}
		tmp = request.getParameter("criteria[mileageMin]");
		if (!AppUtil.isObjectEmpty(tmp)) {
			this.setMileageMin(Double.parseDouble(tmp));
		}
		tmp = request.getParameter("criteria[mileageMax]");
		if (!AppUtil.isObjectEmpty(tmp)) {
			this.setMileageMax(Double.parseDouble(tmp));
		}
		tmp = request.getParameter("criteria[ccMin]");
		if (!AppUtil.isObjectEmpty(tmp)) {
			this.setCcMin(Double.parseDouble(tmp));
		}
		tmp = request.getParameter("criteria[ccMax]");
		if (!AppUtil.isObjectEmpty(tmp)) {
			this.setCcMax(Double.parseDouble(tmp));
		}
		tmp = request.getParameter("criteria[ccMax]");
		if (!AppUtil.isObjectEmpty(tmp)) {
			this.setCcMax(Double.parseDouble(tmp));
		}
		tmp = request.getParameter("criteria[priceMin]");
		if (!AppUtil.isObjectEmpty(tmp)) {
			this.setPriceMin(Double.parseDouble(tmp));
		}
		tmp = request.getParameter("criteria[priceMax]");
		if (!AppUtil.isObjectEmpty(tmp)) {
			this.setPriceMax(Double.parseDouble(tmp));
		}
		tmp = request.getParameter("criteria[yearMin]");
		if (!AppUtil.isObjectEmpty(tmp)) {
			this.setYearMin(new SimpleDateFormat("yyyy").parse(tmp));
		}
		tmp = request.getParameter("criteria[yearMax]");
		if (!AppUtil.isObjectEmpty(tmp)) {
			this.setYearMax(new SimpleDateFormat("yyyy").parse(tmp));
		}

		tmp = request.getParameter("criteria[purchaseDateFrom]");
		if (!AppUtil.isObjectEmpty(tmp)) {
			this.setPurchaseDateFrom(formatter.parse(tmp));
		}
		tmp = request.getParameter("criteria[purchaseDateTo]");
		if (!AppUtil.isObjectEmpty(tmp)) {
			this.setPurchaseDateTo(formatter.parse(tmp));
		}
		tmp = request.getParameter("criteria[fobMin]");
		if (!AppUtil.isObjectEmpty(tmp)) {
			this.setFobMin(Double.parseDouble(tmp));
		}
		tmp = request.getParameter("criteria[fobMax]");
		if (!AppUtil.isObjectEmpty(tmp)) {
			this.setFobMax(Double.parseDouble(tmp));
		}
	}

	public Integer getAccount() {
		return this.account;
	}

	public void setAccount(Integer account) {
		this.account = account;
	}

}
