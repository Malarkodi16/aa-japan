package com.nexware.aajapan.models;

import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.nexware.aajapan.listeners.EntityModelBase;

@Document(collection = "t_cstmr")
public class TCustomer extends EntityModelBase {
	@Id
	private String id;
	@NotBlank
	@Indexed(unique = true)
	private String code;
	private String firstName;
	private String lastName;
	private String nickName;
	private String email;
	private String skypeId;
	private String mobileNo;
	private String companyName;
	private String address;
	private String city;
	private String country;
	private String port;
	private String yard;
	private String comments;
	private boolean isLcCustomer;
	private String bank;
	private String accountNo;
	private Integer currencyType;
	private String paymentType;
	private Integer flag;
	private Integer approveCustomerflag;
	private Integer checkCreditLimit;
	private Double creditBalance;
	private Double balance;
	private Double depositAmount;
	private Double advanceAmount;
	private List<ConsigneeNotifyparty> consigneeNotifyparties;
	private String salesPerson;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getNickName() {
		return this.nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSkypeId() {
		return this.skypeId;
	}

	public void setSkypeId(String skypeId) {
		this.skypeId = skypeId;
	}

	public String getMobileNo() {
		return this.mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getCompanyName() {
		return this.companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPort() {
		return this.port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public boolean isLcCustomer() {
		return this.isLcCustomer;
	}

	public void setLcCustomer(boolean isLcCustomer) {
		this.isLcCustomer = isLcCustomer;
	}

	public List<ConsigneeNotifyparty> getConsigneeNotifyparties() {
		return this.consigneeNotifyparties;
	}

	public void setConsigneeNotifyparties(List<ConsigneeNotifyparty> consigneeNotifyparties) {
		this.consigneeNotifyparties = consigneeNotifyparties;
	}

	public String getBank() {
		return this.bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getAccountNo() {
		return this.accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public Integer getCurrencyType() {
		return this.currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public String getPaymentType() {
		return this.paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getYard() {
		return this.yard;
	}

	public void setYard(String yard) {
		this.yard = yard;
	}

	public Integer getCheckCreditLimit() {
		return this.checkCreditLimit;
	}

	public void setCheckCreditLimit(Integer checkCreditLimit) {
		this.checkCreditLimit = checkCreditLimit;
	}

	public Double getCreditBalance() {
		return this.creditBalance;
	}

	public void setCreditBalance(Double creditBalance) {
		this.creditBalance = creditBalance;
	}

	public Double getBalance() {
		return this.balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public String getSalesPerson() {
		return this.salesPerson;
	}

	public void setSalesPerson(String salesPerson) {
		this.salesPerson = salesPerson;
	}

	public Double getDepositAmount() {
		return this.depositAmount;
	}

	public void setDepositAmount(Double depositAmount) {
		this.depositAmount = depositAmount;
	}

	public Integer getApproveCustomerflag() {
		return this.approveCustomerflag;
	}

	public void setApproveCustomerflag(Integer approveCustomerflag) {
		this.approveCustomerflag = approveCustomerflag;
	}

	public Double getAdvanceAmount() {
		return advanceAmount;
	}

	public void setAdvanceAmount(Double advanceAmount) {
		this.advanceAmount = advanceAmount;
	}

}
