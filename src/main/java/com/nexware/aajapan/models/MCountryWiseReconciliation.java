package com.nexware.aajapan.models;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "m_cnt_recn")
public class MCountryWiseReconciliation {

	@Id
	private String id;
	@Indexed
	private String country;
	private List<CountryReconciliationItems> countryReconciliationItems;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public List<CountryReconciliationItems> getCountryReconciliationItems() {
		return this.countryReconciliationItems;
	}

	public void setCountryReconciliationItems(List<CountryReconciliationItems> countryReconciliationItems) {
		this.countryReconciliationItems = countryReconciliationItems;
	}

}
