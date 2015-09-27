/**
 * Create and manage Expense or Income Category objects
 * @author ngapham
 * @date 12/7/15
 */
package com.pulsardev.homebudgettracker.model;

import org.json.JSONException;
import org.json.JSONObject;


public class Category {
	
	/**
	 * Properties
	 */
	private int id;
	private String name;
	private double amount;
	
	/**
	 * keys of JSON Object
	 */
	private static final String ID = "id";
	private static final String NAME = "name";
	private static final String AMOUNT = "amount";
	
	/**
	 * Constructor with no specific ID and Name
	 */
	public Category() {
		super();
		this.amount = 0.0;
	}
	
	public Category(JSONObject jsonObject) throws JSONException {
		if (jsonObject.has(ID)) {
			this.id = jsonObject.getInt(ID);
		}
		if (jsonObject.has(NAME)) {
			this.name = jsonObject.getString(NAME);
		}
		if (jsonObject.has(AMOUNT)) {
			this.amount = jsonObject.getDouble(AMOUNT);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return name;
	}
	
	/**
	 * Convert Category Object into JSON Object
	 * @return JSON Object
	 * @throws JSONException
	 */
	public JSONObject toJSON() throws JSONException {
		JSONObject jsonCatObj = new JSONObject();
		jsonCatObj.put(ID, this.id);
		jsonCatObj.put(NAME, this.name);
		jsonCatObj.put(AMOUNT, this.amount);
		return jsonCatObj;
	}
}
