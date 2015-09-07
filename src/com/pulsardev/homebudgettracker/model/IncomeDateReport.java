/**
 * Manage objects of Income Reporting by Date
 * @author ngapham
 * @date 6/9/2015
 */
package com.pulsardev.homebudgettracker.model;

import java.util.Date;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

public class IncomeDateReport {
	/**
	 * Properties
	 */
	private UUID id;
	private Date date;
	private double amount;
	private int categoryID;
	private String description;
	
	/**
	 * keys of JSON Object
	 */
	private static final String ID = "id";
	private static final String DATE = "date";
	private static final String AMOUNT = "amount";
	private static final String CATEGORY_ID = "category_id";
	private static final String DESCRIPTION = "description";
	
	/**
	 * Constructor with no specific ID and Name
	 */
	public IncomeDateReport() {
		// Generate unique id
		id = UUID.randomUUID();
	}

	/**
	 * Constructor with fields
	 * @param date
	 * @param amount
	 * @param categoryID
	 * @param description
	 */
	public IncomeDateReport(Date date, double amount, int categoryID,
			String description) {
		id = UUID.randomUUID();
		this.date = date;
		this.amount = amount;
		this.categoryID = categoryID;
		this.description = description;
	}
	
	/**
	 * Create new item from JSON Object
	 * @param jsonObject
	 * @throws JSONException
	 */
	public IncomeDateReport(JSONObject jsonObject) throws JSONException {
		id = UUID.fromString(jsonObject.getString(ID));
		if (jsonObject.has(DATE)) {
			this.date = new Date(jsonObject.getLong(DATE));
		}
		if (jsonObject.has(AMOUNT)) {
			this.amount = jsonObject.getDouble(AMOUNT);
		}
		if (jsonObject.has(CATEGORY_ID)) {
			this.categoryID = jsonObject.getInt(CATEGORY_ID);
		}
		if (jsonObject.has(DESCRIPTION)) {
			this.description = jsonObject.getString(DESCRIPTION);
		}
	}
	
	/**
	 * getter and setter
	 */
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public int getCategoryID() {
		return categoryID;
	}

	public void setCategoryID(int categoryID) {
		this.categoryID = categoryID;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public UUID getId() {
		return id;
	}
	
	/**
	 * Convert Income Date Report Object into JSON Object
	 * @return JSON Object
	 * @throws JSONException
	 */
	public JSONObject toJSON() throws JSONException {
		JSONObject jsonInDateReportObj = new JSONObject();
		jsonInDateReportObj.put(ID, this.id.toString());
		jsonInDateReportObj.put(DATE, this.date.getTime());
		jsonInDateReportObj.put(AMOUNT, this.amount);
		jsonInDateReportObj.put(CATEGORY_ID, this.categoryID);
		jsonInDateReportObj.put(DESCRIPTION, this.description);
		return jsonInDateReportObj;
	}

	@Override
	public String toString() {
		String line = "id = " + this.ID
				+ ", amount = " + this.amount
				+ ", category id = " + this.categoryID;
		return line;
	}
}
