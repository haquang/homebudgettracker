/**
 * Manage list of Expense Date Report
 * This is the singleton
 * @author ngapham
 * @date 26/7/2015
 */

package com.pulsardev.homebudgettracker.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.json.JSONException;

import com.pulsardev.homebudgettracker.control.JSONSerializer;

import android.content.Context;
import android.util.Log;

public class ExpenseDateReportLab {
	// List of Expense Date Reports, which is stored in database
	private ArrayList<ExpenseDateReport> mListExpDateReport;
	// 
	private JSONSerializer mSerializer;
	// file name in database, which stores List of Expense Date Reports
	private static final String JSON_FILENAME = "expense_date_reports.json";
	
	// To create singleton
	private static ExpenseDateReportLab mExpDateReportLab;
	private Context mAppContext;
	
	// TAG
	private static final String TAG = "ExpenseDateReportLab.java";
	
	/**
	 * @param appContext
	 */
	public ExpenseDateReportLab(Context appContext) {
		mAppContext = appContext;
		// Create Serializer to save ExpDateReport List to JSON file
		mSerializer = new JSONSerializer(mAppContext, JSON_FILENAME);
		// initial list of Expense Date Reports when loading app for the first time
		try {
			mListExpDateReport = mSerializer.loadListExpDateReport();
		} catch (Exception e) {
			mListExpDateReport = new ArrayList<ExpenseDateReport>();
			Log.i(TAG, "Loading new list of Expense Date Reports", e);
		}
	}
	
	/**
	 * To create singleton
	 * @param c
	 * @return singleton of ExpenseCategoryLab
	 */
	public static ExpenseDateReportLab get(Context c) {
		if (mExpDateReportLab == null) {
			mExpDateReportLab = new ExpenseDateReportLab(c.getApplicationContext());
		}
		return mExpDateReportLab;
	}

	public ArrayList<ExpenseDateReport> getListExpDateReport() {
		return mListExpDateReport;
	}
	
	/**
	 * Save list of Exp Date Report into JSON file
	 * @return success in save List data or not
	 */
	public boolean saveExpDateReport(ExpenseDateReport newExpDateReport) {
		try {
			mSerializer.saveExpenseDateReport(newExpDateReport);
			Log.i(TAG, "New Date Report saved to file.");
			return true;
		} catch (JSONException e) {
			Log.e(TAG, "Error saving new Date Report: " + e);
			return false;
		} catch (IOException e) {
			Log.e(TAG, "Error saving new Date Report: " + e);
			return false;
		}
	}
	
	/**
	 * not used
	 * Get the specific Expense Date Report from list by id
	 * @param id
	 * @return the specific Expense Date Report
	 */
	public ExpenseDateReport getExpDateReport(UUID id) {
		for (ExpenseDateReport item : mListExpDateReport) {
			if (item.getID().equals(id)) {
				return item;
			}
		}
		return null;
	}
	
	/**
	 * not used
	 * add new Expense Date Report to List
	 * @param e
	 */
	public void addExpDateReport(ExpenseDateReport e) {
		mListExpDateReport.add(e);
	}
	
	
}
