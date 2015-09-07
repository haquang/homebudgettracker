/**
 * Create and manage Income Category objects
 * @author ngapham
 * @date 6/9/2015
 */
package com.pulsardev.homebudgettracker.model;

public class IncomeCategory {
	/**
	 * Properties
	 */
	private int ID;
	private String name;
	private double amount;

	/**
	 * Constructor with no specific ID and Name
	 */
	public IncomeCategory() {
		super();
		this.amount = 0.0;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getID() {
		return ID;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return name;
	}
}
