/**
 *
 */
package com.nexware.aajapan.datatable;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DataTableResults<T> {

	/** The draw. */
	private String draw;

	/** The records filtered. */
	private Integer recordsFiltered;

	/** The records total. */
	private Integer recordsTotal;

	/** The list of data objects. */
	@JsonProperty("data")
	List<T> listOfDataObjects;

	/**
	 * Gets the draw.
	 *
	 * @return the draw
	 */
	public String getDraw() {
		return this.draw;
	}

	/**
	 * Sets the draw.
	 *
	 * @param draw
	 *            the draw to set
	 */
	public void setDraw(String draw) {
		this.draw = draw;
	}

	/**
	 * Gets the records filtered.
	 *
	 * @return the recordsFiltered
	 */
	public Integer getRecordsFiltered() {
		return this.recordsFiltered;
	}

	/**
	 * Sets the records filtered.
	 *
	 * @param recordsFiltered
	 *            the recordsFiltered to set
	 */
	public void setRecordsFiltered(Integer recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}

	/**
	 * Gets the records total.
	 *
	 * @return the recordsTotal
	 */
	public Integer getRecordsTotal() {
		return this.recordsTotal;
	}

	/**
	 * Sets the records total.
	 *
	 * @param recordsTotal
	 *            the recordsTotal to set
	 */
	public void setRecordsTotal(Integer recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

	/**
	 * Gets the list of data objects.
	 *
	 * @return the listOfDataObjects
	 */
	public List<T> getListOfDataObjects() {
		return this.listOfDataObjects;
	}

	/**
	 * Sets the list of data objects.
	 *
	 * @param listOfDataObjects
	 *            the listOfDataObjects to set
	 */
	public void setListOfDataObjects(List<T> listOfDataObjects) {
		this.listOfDataObjects = listOfDataObjects;
	}

}
