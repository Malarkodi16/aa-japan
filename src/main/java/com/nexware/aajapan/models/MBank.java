package com.nexware.aajapan.models;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.nexware.aajapan.listeners.EntityModelBase;

@Document(collection = "m_bank")
public class MBank extends EntityModelBase{
	@Id
	private String id;
	@Indexed(unique = true)
	private String bankSeq;
	private String bankName;
	private Integer currencyType;
	private Double yenBalance;// this is total balance
	private Double clearingBalance;
	private Integer accountType;
	@NotBlank
	private Long coaCode;
	private String address;
	private String cityState;
	private String shiftCode;
	private Long branchNumber;
	private Long accountNo;
	private String accouontName;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBankSeq() {
		return this.bankSeq;
	}

	public void setBankSeq(String bankSeq) {
		this.bankSeq = bankSeq;
	}

	public String getBankName() {
		return this.bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public Integer getCurrencyType() {
		return this.currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public Double getYenBalance() {
		return this.yenBalance;
	}

	public void setYenBalance(Double yenBalance) {
		this.yenBalance = yenBalance;
	}

	public Double getClearingBalance() {
		return this.clearingBalance;
	}

	public void setClearingBalance(Double clearingBalance) {
		this.clearingBalance = clearingBalance;
	}

	public Long getCoaCode() {
		return this.coaCode;
	}

	public void setCoaCode(Long coaCode) {
		this.coaCode = coaCode;
	}

	public Integer getAccountType() {
		return this.accountType;
	}

	public void setAccountType(Integer accountType) {
		this.accountType = accountType;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCityState() {
		return this.cityState;
	}

	public void setCityState(String cityState) {
		this.cityState = cityState;
	}

	public String getShiftCode() {
		return this.shiftCode;
	}

	public void setShiftCode(String shiftCode) {
		this.shiftCode = shiftCode;
	}

	public Long getBranchNumber() {
		return this.branchNumber;
	}

	public void setBranchNumber(Long branchNumber) {
		this.branchNumber = branchNumber;
	}

	public Long getAccountNo() {
		return this.accountNo;
	}

	public void setAccountNo(Long accountNo) {
		this.accountNo = accountNo;
	}

	public String getAccouontName() {
		return this.accouontName;
	}

	public void setAccouontName(String accouontName) {
		this.accouontName = accouontName;
	}

}
