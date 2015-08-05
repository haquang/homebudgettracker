/**
 * Create and manage Expense Category objects
 * @author ngapham
 * @date 12/7/15
 */
package com.pulsardev.homebudgettracker.model;


public class ExpenseCategory {
	
	// properties of Crime object
	private int ID;
	private String name;
	private float amount;
	
	/**
	 * Constructor with no specific ID and Name
	 */
	public ExpenseCategory() {
		super();
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

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return name;
	}
}
