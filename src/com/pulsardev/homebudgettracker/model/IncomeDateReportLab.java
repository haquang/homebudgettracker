package com.pulsardev.homebudgettracker.model;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;

import android.content.Context;
import android.util.Log;

import com.pulsardev.homebudgettracker.control.JSONSerializer;

public class IncomeDateReportLab {

	/**
	 * Properties
	 */
	// List of Income Date Reports, which is stored in database
	private ArrayList<IncomeDateReport> mListInDateReport;

	private JSONSerializer mSerializer;
	// file name in database, which stores List of Income Date Reports
	private static final String JSON_FILENAME = "income_date_reports.json";

	// To create singleton
	private static IncomeDateReportLab mInDateReportLab;
	private Context mAppContext;

	// TAG
	private static final String TAG = "IncomeDateReportLab";

	public IncomeDateReportLab(Context appContext) {
		mAppContext = appContext;
		// Create Serializer to save InDateReport List to JSON file
		mSerializer = new JSONSerializer(mAppContext, JSON_FILENAME);
		// initial list of Income Date Reports when loading app for the first time
		try {
			mListInDateReport = mSerializer.loadListInDateReports();
		} catch (Exception e) {
			mListInDateReport = new ArrayList<IncomeDateReport>();
			Log.e(TAG, "Error Loading new list of Expense Date Reports", e);
		}
	}
	
	/**
	 * To create singleton
	 * @param appContext
	 * @return singleton of IncomeDateReportLab
	 */
	public IncomeDateReportLab get(Context appContext) {
		if (mInDateReportLab == null) {
			mInDateReportLab = new IncomeDateReportLab(appContext.getApplicationContext());
		}
		return mInDateReportLab;
	}

	public ArrayList<IncomeDateReport> getListInDateReport() {
		return mListInDateReport;
	}
	
	/**
	 * Save List of Income Date Reports into JSON file
	 * @return success in save List data or not
	 */	
	public boolean saveListInDateReport() {
		try {
			mSerializer.saveListInDateReports(mListInDateReport);
			Log.i(TAG, "List saved to file.");
			return true;
		} catch (Exception e) {
			Log.e(TAG, "Error saving list: " + e);
			return false;
		}
	}
	
	/**
	 * add new Income Date Report to List
	 * @param newInDateReport
	 */
	public void addInDateReport(IncomeDateReport newInDateReport) {
		mListInDateReport.add(newInDateReport);
	}
}
