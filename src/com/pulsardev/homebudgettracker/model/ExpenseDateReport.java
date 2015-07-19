/**
 * Manage objects of Expense Reporting by Date
 * @author ngapham
 * @date 12/7/15
 */

package com.pulsardev.homebudgettracker.model;

import java.util.Date;
import java.util.UUID;

public class ExpenseDateReport {

	// properties
	private UUID ID;
	private Date mDate;
	private float mAmount;
	private int categoryID;
	private String description;
	
	/**
	 * Constructor with no specific ID and Name
	 */
	public ExpenseDateReport() {
		super();
		// Generate unique id
		ID = UUID.randomUUID();
	}

	/**
	 * @param iD
	 * @param date
	 * @param amount
	 * @param categoryID
	 * @param description
	 */
	public ExpenseDateReport(UUID iD, Date date, float amount, int categoryID,
			String description) {
		super();
		ID = iD;
		mDate = date;
		mAmount = amount;
		this.categoryID = categoryID;
		this.description = description;
	}

	public Date getDate() {
		return mDate;
	}

	public void setDate(Date date) {
		mDate = date;
	}

	public float getAmount() {
		return mAmount;
	}

	public void setAmount(float amount) {
		mAmount = amount;
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
		return ID;
	}
	
}
