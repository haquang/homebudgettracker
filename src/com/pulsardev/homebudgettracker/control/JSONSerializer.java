/**
 * Parsing model objects as JSON type
 * @author ngapham
 * @date 12/7/05
 */
package com.pulsardev.homebudgettracker.control;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import android.content.Context;
import android.util.Log;

import com.pulsardev.homebudgettracker.model.ExpenseDateReport;
import com.pulsardev.homebudgettracker.model.IncomeDateReport;

public class JSONSerializer {

	private Context mContext;
	private String mFileName;

	// UTF-8 CHARSET
	private static final String CHARSET_UTF8 = "UTF-8";

	private static final String TAG = "JSONSerializer.java";

	/**
	 * constructor from specific context and filename
	 */
	public JSONSerializer(Context mContext, String mFileName) {
		this.mContext = mContext;
		this.mFileName = mFileName;
	}

	/**
	 * Save List of Exp Date Reports as JSON objects into JSON file
	 * @param listDateReport
	 * @throws JSONException
	 * @throws IOException
	 */	
	public void saveListExpDateReports(ArrayList<ExpenseDateReport> listDateReport) throws JSONException, IOException {

		// Build an array in JSON
		JSONArray array = new JSONArray();
		for (ExpenseDateReport item : listDateReport) {
			array.put(item.toJSON());
		}
		// Write the new JSON Object to disk
		Writer writer = null;
		try {
			OutputStream out = mContext.openFileOutput(mFileName, Context.MODE_PRIVATE);
			writer = new BufferedWriter(new OutputStreamWriter(out, CHARSET_UTF8));
			writer.write(array.toString());
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

	/**
	 * Load list of Exp Date Reports from JSON file
	 * @return list of Exp Date Report objects
	 * @throws IOException
	 * @throws JSONException
	 */
	public ArrayList<ExpenseDateReport> loadListExpDateReports() throws IOException, JSONException {
		ArrayList<ExpenseDateReport> list = new ArrayList<ExpenseDateReport>();
		BufferedReader reader = null;

		try {
			// Open and read the file into a StringBuilder
			InputStream in = mContext.openFileInput(mFileName);
			reader = new BufferedReader(new InputStreamReader(in));
			StringBuilder jsonString = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				// Line breaks are omitted and irrelevant
				jsonString.append(line);
			}

			// Parse the JSON using JSONTokener
			JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
			// Build the array of Exp Date Reports from JSONObjects
			for (int i = 0; i < array.length(); i++) {
				list.add(new ExpenseDateReport(array.getJSONObject(i)));
				Log.i(TAG, list.get(i).toString());
			}
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
		return list;
	}

	/**
	 * Load list of Exp Date Reports with specific categoryID
	 * @param fullList
	 * @param categoryId
	 * @return
	 */
	public ArrayList<ExpenseDateReport> loadListExpDateReportByCatId(ArrayList<ExpenseDateReport> fullList, int categoryId) {
		ArrayList<ExpenseDateReport> list = new ArrayList<ExpenseDateReport>();
		for (ExpenseDateReport item : fullList) {
			if (item.getCategoryID() == categoryId) {
				list.add(item);
			}
		}
		return list;
	}
	
	/**
	 * Save List of Income Date Reports as JSON objects into JSON file
	 * @param listDateReport
	 * @throws JSONException
	 * @throws IOException
	 */	
	public void saveListInDateReports(ArrayList<IncomeDateReport> listDateReport) throws JSONException, IOException {
		// Build an array in JSON
		JSONArray array = new JSONArray();
		for (IncomeDateReport item : listDateReport) {
			array.put(item.toJSON());
		}
		// Write the new JSON Object to disk
		Writer writer = null;
		try {
			OutputStream out = mContext.openFileOutput(mFileName, Context.MODE_PRIVATE);
			writer = new BufferedWriter(new OutputStreamWriter(out, CHARSET_UTF8));
			writer.write(array.toString());
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}
	
	/**
	 * Load list of Income Date Reports from JSON file
	 * @return list of Income Date Report objects
	 * @throws IOException
	 * @throws JSONException
	 */
	public ArrayList<IncomeDateReport> loadListInDateReports() throws IOException, JSONException {
		ArrayList<IncomeDateReport> list = new ArrayList<IncomeDateReport>();
		BufferedReader reader = null;

		try {
			// Open and read the file into a StringBuilder
			InputStream in = mContext.openFileInput(mFileName);
			reader = new BufferedReader(new InputStreamReader(in));
			StringBuilder jsonString = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				// Line breaks are omitted and irrelevant
				jsonString.append(line);
			}

			// Parse the JSON using JSONTokener
			JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
			// Build the array of Exp Date Reports from JSONObjects
			for (int i = 0; i < array.length(); i++) {
				list.add(new IncomeDateReport(array.getJSONObject(i)));
				Log.i(TAG, list.get(i).toString());
			}
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
		return list;
	}
}
