/**
 * Create and manage Expense Category objects
 * @author ngapham
 * @date 12/7/15
 */
package com.pulsardev.homebudgettracker.model;

import org.json.JSONException;
import org.json.JSONObject;

public class ExpenseCategory {
	
	// properties of Crime object
	private int ID;
	private String name;
	private float amount;
	
	// keys of JSON Object
	private static final String JSON_ID = "id";
	private static final String JSON_NAME = "name";
	private static final String JSON_AMOUNT = "amount";
	
	/**
	 * Constructor with no specific ID and Name
	 */
	public ExpenseCategory() {
		super();
	}
	
	/**
	 * Convert JSON Object to Expense Category to load Category List
	 * @param jsonExCatObj
	 * @throws JSONException
	 */
	public ExpenseCategory(JSONObject jsonExCatObj) throws JSONException {
		ID = Integer.parseInt(jsonExCatObj.getString(JSON_ID));
		if (jsonExCatObj.has(JSON_NAME)) {
			name = jsonExCatObj.getString(JSON_NAME);
		}
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
	
	/**
	 * Convert Expense Category Object into JSON Object
	 * @return JSON Object
	 * @throws JSONException
	 */
	public JSONObject toJSON() throws JSONException {
		JSONObject jsonExCatObj = new JSONObject();
		jsonExCatObj.put(JSON_ID, ID);
		jsonExCatObj.put(JSON_NAME, name);
		jsonExCatObj.put(JSON_AMOUNT, amount);
		return jsonExCatObj;
	}
}
