/**
 * Parsing model objects as JSON type
 * @author ngapham
 * @date 12/7/05
 */
package com.pulsardev.homebudgettracker.control;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import com.pulsardev.homebudgettracker.model.ExpenseCategory;

import android.content.Context;

public class JSONSerializer {
	
	private Context mContext;
	private String mFilename;
	
	/**
	 * constructor from specific context and filename
	 */
	public JSONSerializer(Context mContext, String mFilename) {
		this.mContext = mContext;
		this.mFilename = mFilename;
	}
	
	public void saveExpenseCategory(ArrayList<ExpenseCategory> exCatList) throws JSONException {
		// Build an array in JSON
		JSONArray mArray = new JSONArray();
		for (ExpenseCategory item : exCatList) {
			mArray.put(item.toJSON());
		}
	}
}
