/**
 * main fragment: manage Expense total and Expense Category
 * @author ngapham
 */

package com.pulsardev.homebudgettracker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class ExpenseFragment extends Fragment implements OnClickListener {
	
	// controls
	ImageButton btnHouse, btnFood, btnTransportation, btnMedical, btnEntertainment, btnOther;
	// key of value that will be passed to FragmentAddActivity
	public static final String INTENT_EXTRA_ADD_EXPENSE = "Add Expense";
	// main folder in sdcard
	public static final String DATA_PATH = Environment
			.getExternalStorageDirectory().toString() + "/HomeBudgetTracker/";
	// tag
	private static final String TAG = "ExpenseFragment";
	// utf-8 encoding String
	private static final String UTF8 = "utf-8";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_expense,
				container, false);
		
		initControls(rootView);
		try {
			saveDataToSdCard();
		} catch (IOException e) {
			Log.i(TAG, "Failed to copy expense_date.xml to sdcard: " + e);
		}
		
		return rootView;
	}
	
	private void initControls(View v) {
		btnHouse = (ImageButton) v.findViewById(R.id.btnImg_AddHouse);
		btnFood = (ImageButton) v.findViewById(R.id.btnImg_AddFood);
		btnTransportation = (ImageButton) v.findViewById(R.id.btnImg_AddTransport);
		btnMedical = (ImageButton) v.findViewById(R.id.btnImg_AddMedical);
		btnEntertainment = (ImageButton) v.findViewById(R.id.btnImg_AddLeisure);
		btnOther = (ImageButton) v.findViewById(R.id.btnImg_AddOther);
		
		btnHouse.setOnClickListener(this);
		btnFood.setOnClickListener(this);
		btnTransportation.setOnClickListener(this);
		btnMedical.setOnClickListener(this);
		btnEntertainment.setOnClickListener(this);
		btnOther.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		String categoryName;
		switch (v.getId()) {
		case R.id.btnImg_AddHouse:
			categoryName = getResources().getString(R.string.txt_category_house);
			callAddActivity(categoryName);
			break;
		case R.id.btnImg_AddFood:
			categoryName = getResources().getString(R.string.txt_category_food);
			callAddActivity(categoryName);
			break;
		case R.id.btnImg_AddTransport:
			categoryName = getResources().getString(R.string.txt_category_transport);
			callAddActivity(categoryName);
			break;
		case R.id.btnImg_AddMedical:
			categoryName = getResources().getString(R.string.txt_category_medical);
			callAddActivity(categoryName);
			break;
		case R.id.btnImg_AddLeisure:
			categoryName = getResources().getString(R.string.txt_category_leisure);
			callAddActivity(categoryName);
			break;
		case R.id.btnImg_AddOther:
			categoryName = getResources().getString(R.string.txt_category_other);
			callAddActivity(categoryName);
			break;
		default:
			break;
		}
	}
	
	public void callAddActivity(String categoryName) {
		Intent i = new Intent(this.getActivity(), ExpenseAddActivity.class);
		i.putExtra(INTENT_EXTRA_ADD_EXPENSE, categoryName);
		startActivity(i);
	}
	
	/**
	 * Save Expense Date Report data (xml file) to sdcard
	 * @throws IOException 
	 * @author ngapham
	 * @date 20/7/2015
	 */
	protected void saveDataToSdCard() throws IOException {
		// Create directory
		String path = new String(DATA_PATH);
		File dir = new File(path);
		if (!dir.exists()) {
			if (!dir.mkdirs()) {
				Log.i(TAG, "Creation of directory " + path + " on sdcard failed");
				return;
			} else {
				Log.i(TAG, "Created directory " + path + " on sdcard");
			}
		}
		//Copy file
		InputStream in = getActivity().getResources().openRawResource(R.xml.expense_date);
		InputStreamReader inReader = new InputStreamReader(in, UTF8);
		BufferedReader buffReader = new BufferedReader(inReader);
		
		OutputStream out = new FileOutputStream(DATA_PATH + "expense_date.xml");
		BufferedWriter buffWriter = new BufferedWriter(new OutputStreamWriter(out, UTF8));
		char[] buff = new char[1024];
		int i;
		while ((i = buffReader.read(buff)) != -1) {
			buffWriter.write(buff, 0, i);
		}
		Log.i(TAG, "File expense_date.xml copied");
		in.close();
		out.close();
	}
}
