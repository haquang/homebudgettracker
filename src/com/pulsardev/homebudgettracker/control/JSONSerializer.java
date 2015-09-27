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

import com.pulsardev.homebudgettracker.model.Category;
import com.pulsardev.homebudgettracker.model.DateReport;
import com.pulsardev.homebudgettracker.util.StaticString;

public class JSONSerializer {

	private Context mContext;
	private String mFileName;

	private static final String TAG = "JSONSerializer.java";

	/**
	 * constructor from specific context and filename
	 */
	public JSONSerializer(Context mContext, String mFileName) {
		this.mContext = mContext;
		this.mFileName = mFileName;
	}

	/**
	 * Save List of Date Reports as JSON objects into JSON file
	 * @param listDateReport
	 * @throws JSONException
	 * @throws IOException
	 */	
	public void saveListDateReports(ArrayList<DateReport> listDateReport) throws JSONException, IOException {
		// Build an array in JSON
		JSONArray array = new JSONArray();
		for (DateReport item : listDateReport) {
			array.put(item.toJSON());
		}
		// Write the new JSON Object to disk
		Writer writer = null;
		try {
			OutputStream out = mContext.openFileOutput(mFileName, Context.MODE_PRIVATE);
			writer = new BufferedWriter(new OutputStreamWriter(out, StaticString.UTF8));
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
	public ArrayList<DateReport> loadListDateReports() throws IOException, JSONException {
		ArrayList<DateReport> list = new ArrayList<DateReport>();
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
				list.add(new DateReport(array.getJSONObject(i)));
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
	public ArrayList<DateReport> loadListExpDateReportByCatId(ArrayList<DateReport> fullList, int categoryId) {
		ArrayList<DateReport> list = new ArrayList<DateReport>();
		for (DateReport item : fullList) {
			if (item.getCategoryID() == categoryId) {
				list.add(item);
			}
		}
		return list;
	}
	
	/**
	 * Save List of Categories as JSON objects into JSON file
	 * @param listCategory
	 * @throws JSONException
	 * @throws IOException
	 */
	public void saveListCategories(ArrayList<Category> listCategory) throws JSONException, IOException {
		// Build an array in JSON
		JSONArray array = new JSONArray();
		for (Category item : listCategory) {
			array.put(item.toJSON());
		}
		// Write the new JSON Object to disk
		Writer writer = null;
		try {
			OutputStream out = mContext.openFileOutput(mFileName, Context.MODE_PRIVATE);
			writer = new BufferedWriter(new OutputStreamWriter(out, StaticString.UTF8));
			writer.write(array.toString());
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}
	
	public ArrayList<Category> loadListCategories() throws IOException, JSONException {
		ArrayList<Category> list = new ArrayList<Category>();
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
				list.add(new Category(array.getJSONObject(i)));
			}
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
		return list;
	}
}
