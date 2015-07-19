/**
 * Create and manage Expense Category objects
 * @author ngapham
 * @date 12/7/15
 */
package com.pulsardev.homebudgettracker.model;

import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

public class ExpenseCategory {
	
	// properties of Crime object
	private int ID;
	private String name;
	
	// keys of JSON Object
	private static final String JSON_ID = "id";
	private static final String JSON_NAME = "name";
	
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

	public int getID() {
		return ID;
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
		return jsonExCatObj;
	}
}