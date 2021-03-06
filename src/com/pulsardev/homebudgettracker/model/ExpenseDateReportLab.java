/**
 * Manage list of Expense Date Report
 * This is the singleton
 * @author ngapham
 * @date 26/7/2015
 */

package com.pulsardev.homebudgettracker.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
	private ArrayList<DateReport> mListExpDateReport;
	
	private JSONSerializer mSerializer;
	// file name in database, which stores List of Expense Date Reports
	private static final String JSON_FILENAME = "expense_date_reports.json";
	
	// To create singleton
	private static ExpenseDateReportLab mExpDateReportLab;
	private Context mAppContext;
	
	// TAG
	private static final String TAG = "ExpenseDateReportLab";
	
	/**
	 * Constructor
	 * @param appContext
	 */
	public ExpenseDateReportLab(Context appContext) {
		mAppContext = appContext;
		// Create Serializer to load ExpDateReport List from JSON file
		mSerializer = new JSONSerializer(mAppContext, JSON_FILENAME);
		// initial list of Expense Date Reports when loading app for the first time
		try {
			mListExpDateReport = mSerializer.loadListDateReports();
		} catch (Exception e) {
			mListExpDateReport = new ArrayList<DateReport>();
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

	public ArrayList<DateReport> getListExpDateReport() {
		return mListExpDateReport;
	}
	
	/**
	 * Save List of Exp Date Reports into JSON file
	 * @return success in save List data or not
	 */	
	@SuppressWarnings("unchecked")
	public boolean saveListExpDateReport() {
		try {
			Collections.sort(mListExpDateReport);
			mSerializer.saveListDateReports(mListExpDateReport);
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
	 * add new Expense Date Report to List
	 * @param newExpDateReport
	 */
	public void addExpDateReport(DateReport newExpDateReport) {
		mListExpDateReport.add(newExpDateReport);
	}
	
	public DateReport getDateReport (UUID id) {
		for (DateReport item : mListExpDateReport) {
			if (item.getID().equals(id)) {
				return item;
			}
		}
		return null;
	}
	
	/**
	 * delete a Exp Date Report from list
	 * @param Date Report need to be deleted
	 */
	public void deleteExpDateReport(DateReport report) {
		mListExpDateReport.remove(report);
	}
}
