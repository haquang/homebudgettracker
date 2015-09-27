/**
 * Create and manage Expense or Income Date Report objects
 * @author ngapham
 */

package com.pulsardev.homebudgettracker.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import com.pulsardev.homebudgettracker.util.StaticString;

public class DateReport implements Comparable {

	/**
	 * properties
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
	public DateReport() {
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
	public DateReport(Date date, double amount, int categoryID,
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
	public DateReport(JSONObject jsonObject) throws JSONException {
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

	public UUID getID() {
		return id;
	}

	/**
	 * Convert Date Report Object into JSON Object
	 * @return JSON Object
	 * @throws JSONException
	 */
	public JSONObject toJSON() throws JSONException {
		JSONObject jsonExpDateReportObj = new JSONObject();
		jsonExpDateReportObj.put(ID, this.id.toString());
		jsonExpDateReportObj.put(DATE, this.date.getTime());
		jsonExpDateReportObj.put(AMOUNT, this.amount);
		jsonExpDateReportObj.put(CATEGORY_ID, this.categoryID);
		jsonExpDateReportObj.put(DESCRIPTION, this.description);
		return jsonExpDateReportObj;
	}

	@Override
	public String toString() {
		SimpleDateFormat format = new SimpleDateFormat(StaticString.DATE_FORMAT);
		String dateString = format.format(this.date);
		String line = "id = " + this.ID
				+ ", amount = " + this.amount
				+ ", category id = " + this.categoryID
				+ ", date = " + dateString;
		return line;
	}

	@Override
	public int compareTo(Object compareDateReport) {
		long currentDateInLong = this.getDate().getTime();
		long compareDateInLong = ((DateReport) compareDateReport).getDate().getTime();
		/* For Descending order */
		if (compareDateInLong == currentDateInLong) {
			return 0;
		} else if (compareDateInLong > currentDateInLong) {
			return 1;
		} else {
			return -1;
		}
	}
}
