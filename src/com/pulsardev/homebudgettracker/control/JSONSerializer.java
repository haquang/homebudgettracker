/**
 * Parsing model objects as JSON type
 * @author ngapham
 * @date 12/7/05
 */
package com.pulsardev.homebudgettracker.control;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;

import com.pulsardev.homebudgettracker.model.ExpenseDateReport;

public class JSONSerializer {
	
	private Context mContext;
	private String mFileName;
	
	/**
	 * constructor from specific context and filename
	 */
	public JSONSerializer(Context mContext, String mFileName) {
		this.mContext = mContext;
		this.mFileName = mFileName;
	}
	
	public void saveListExpenseDateReport(ArrayList<ExpenseDateReport> exDateReportList) throws JSONException, IOException {
		// Build an array in JSON
		JSONArray mArray = new JSONArray();
		for (ExpenseDateReport item : exDateReportList) {
			mArray.put(item.toJSON());
		}
		// Write the file to disk
		Writer writer = null;
		try {
			OutputStream out = mContext.openFileOutput(mFileName, Context.MODE_PRIVATE);
			writer = new OutputStreamWriter(out);
			writer.write(mArray.toString());
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}
}
