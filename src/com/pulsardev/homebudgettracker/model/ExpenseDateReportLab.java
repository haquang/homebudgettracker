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
	/**
	 * Properties
	 */
	// List of Expense Date Reports, which is stored in database
	private ArrayList<ExpenseDateReport> mListExpDateReport;
	
	private JSONSerializer mSerializer;
	// file name in database, which stores List of Expense Date Reports
	private static final String JSON_FILENAME = "expense_date_reports.json";
	
	// To create singleton
	private static ExpenseDateReportLab mExpDateReportLab;
	private Context mAppContext;
	
	// TAG
	private static final String TAG = "ExpenseDateReportLab";
	
	/**
	 * @param appContext
	 */
	public ExpenseDateReportLab(Context appContext) {
		mAppContext = appContext;
		// Create Serializer to save ExpDateReport List to JSON file
		mSerializer = new JSONSerializer(mAppContext, JSON_FILENAME);
		// initial list of Expense Date Reports when loading app for the first time
		try {
			mListExpDateReport = mSerializer.loadListExpDateReports();
		} catch (Exception e) {
			mListExpDateReport = new ArrayList<ExpenseDateReport>();
			Log.e(TAG, "Error Loading new list of Expense Date Reports", e);
		}
	}
	
	/**
	 * To create singleton
	 * @param appContext
	 * @return singleton of ExpenseDateReportLab
	 */
	public static ExpenseDateReportLab get(Context appContext) {
		if (mExpDateReportLab == null) {
			mExpDateReportLab = new ExpenseDateReportLab(appContext.getApplicationContext());
		}
		return mExpDateReportLab;
	}

	public ArrayList<ExpenseDateReport> getListExpDateReport() {
		return mListExpDateReport;
	}
	
	/**
	 * Save List of Exp Date Reports into JSON file
	 * @return success in save List data or not
	 */	
	public boolean saveListExpDateReport() {
		try {
			mSerializer.saveListExpDateReports(mListExpDateReport);
			Log.i(TAG, "List saved to file.");
			return true;
		} catch (JSONException e) {
			Log.e(TAG, "Error saving list: " + e);
			return false;
		} catch (IOException e) {
			Log.e(TAG, "Error saving list: " + e);
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
	 * add new Expense Date Report to List
	 * @param newExpDateReport
	 */
	public void addExpDateReport(ExpenseDateReport newExpDateReport) {
		mListExpDateReport.add(newExpDateReport);
	}
	
	
}
