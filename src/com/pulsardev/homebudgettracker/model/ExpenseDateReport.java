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
	private Date date;
	private double amount;
	private int categoryID;
	private String description;
	
	/**
	 * Constructor with no specific ID and Name
	 */
	public ExpenseDateReport() {
		// Generate unique id
		ID = UUID.randomUUID();
	}

	/**
	 * @param date
	 * @param amount
	 * @param categoryID
	 * @param description
	 */
	public ExpenseDateReport(Date date, double amount, int categoryID,
			String description) {
		ID = UUID.randomUUID();
		this.date = date;
		this.amount = amount;
		this.categoryID = categoryID;
		this.description = description;
	}

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
		return ID;
	}
}
