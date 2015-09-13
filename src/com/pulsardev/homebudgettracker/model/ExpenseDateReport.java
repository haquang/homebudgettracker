/**
 * Manage objects of Expense Reporting by Date
 * @author ngapham
 * @date 12/7/15
 */

package com.pulsardev.homebudgettracker.model;

import java.util.Date;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;


public class ExpenseDateReport extends DateReport {
	/**
	 * Constructor with no specific ID and Name
	 */
	public ExpenseDateReport() {
		super();
	}

	/**
	 * Constructor with fields
	 * @param date
	 * @param amount
	 * @param categoryID
	 * @param description
	 */
	public ExpenseDateReport(Date date, double amount, int categoryID,
			String description) {
		super(date, amount, categoryID, description);
	}
	
	/**
	 * Create new item from JSON Object
	 * @param jsonObject
	 * @throws JSONException
	 */
	public ExpenseDateReport(JSONObject jsonObject) throws JSONException {
		super(jsonObject);
	}
	
}
