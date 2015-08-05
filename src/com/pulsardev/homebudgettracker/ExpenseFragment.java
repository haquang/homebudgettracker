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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

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
import android.widget.TextView;

import com.pulsardev.homebudgettracker.model.ExpenseCategory;
import com.pulsardev.homebudgettracker.model.ExpenseCategoryLab;
import com.pulsardev.homebudgettracker.model.ExpenseDateReport;
import com.pulsardev.homebudgettracker.model.ExpenseDateReportLab;

public class ExpenseFragment extends Fragment implements OnClickListener {

	// controls
	ImageButton btnHouse, btnFood, btnTransportation, btnMedical,
			btnEntertainment, btnOther;
	TextView txtCatFood, txtCatTransportation, txtCatHousing, txtCatMedical,
			txtCatEntertainment, txtCatOther;
	ImageButton btnMenu; // Quang - 28.7.2015
	TextView txtTotalAmount;

	// key of value that will be passed to FragmentAddActivity
	public static final String INTENT_EXTRA_ADD_EXPENSE = "Add Expense";
	public static final String INTENT_EXTRA_DATA_LINE = "Statistic Data Line";
	public static final String INTENT_EXTRA_DATA_PIE = "Statistic Data Pie";

	public static final HashMap<Double, Double> statistic_data = new HashMap<Double, Double>();
	public static final HashMap<String, Double> statistic_data_pie = new HashMap<String, Double>();

	// main folder in sdcard
	public static final String DATA_PATH = Environment
			.getExternalStorageDirectory().toString() + "/HomeBudgetTracker/";

	// TAG
	private static final String TAG = "ExpenseFragment";

	// utf-8 encoding String
	private static final String UTF8 = "utf-8";

	// List of Expense Categories
	ArrayList<ExpenseCategory> listExpCategories;
	// List of Expense Date Reports
	ArrayList<ExpenseDateReport> listExpDateReports;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// load List of Expense Categories, which is singleton
		listExpCategories = ExpenseCategoryLab.get(this.getActivity())
				.getListExpCategories();
		listExpDateReports = ExpenseDateReportLab.get(this.getActivity())
				.getListExpDateReport();
	}

	/**
	 * @author ngapham update: 2/8/2015
	 */
	private void showTotalAmount() {
		Double amount = 0.0;
		for (ExpenseDateReport item : listExpDateReports) {
			amount += item.getAmount();
		}
		txtTotalAmount.setText(String.valueOf(amount));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_expense, container,
				false);

		initControls(rootView);
		setTitleName();
		showTotalAmount();
		return rootView;
	}
	
	/**
	 * Set title for Category Name by code to unify Model data and View
	 * @author ngapham
	 * @date 5/8/2015
	 */
	private void setTitleName() {
		// Food
		txtCatFood.setText(listExpCategories.get(0).getName());
		// Transportation
		txtCatTransportation.setText(listExpCategories.get(1).getName());
		// Housing
		txtCatHousing.setText(listExpCategories.get(2).getName());
		// Medical
		txtCatMedical.setText(listExpCategories.get(3).getName());
		// Entertainment
		txtCatEntertainment.setText(listExpCategories.get(4).getName());
		// Others
		txtCatOther.setText(listExpCategories.get(5).getName());
	}

	/**
	 * Update total amount when add new and return to this fragment
	 * @author ngapham
	 * @date 4/8/2015
	 */
	@Override
	public void onResume() {
		super.onResume();
		showTotalAmount();
	}

	private void initControls(View v) {
		txtTotalAmount = (TextView) v
				.findViewById(R.id.edt_total_expense_amount);
		txtCatFood = (TextView) v.findViewById(R.id.txt_category_food);
		txtCatTransportation = (TextView) v.findViewById(R.id.txt_category_transport);
		txtCatHousing = (TextView) v.findViewById(R.id.txt_category_house);
		txtCatMedical = (TextView) v.findViewById(R.id.txt_category_medical);
		txtCatEntertainment = (TextView) v.findViewById(R.id.txt_category_leisure);
		txtCatOther = (TextView) v.findViewById(R.id.txt_category_other);
		
		btnHouse = (ImageButton) v.findViewById(R.id.btnImg_AddHouse);
		btnFood = (ImageButton) v.findViewById(R.id.btnImg_AddFood);
		btnTransportation = (ImageButton) v
				.findViewById(R.id.btnImg_AddTransport);
		btnMedical = (ImageButton) v.findViewById(R.id.btnImg_AddMedical);
		btnEntertainment = (ImageButton) v.findViewById(R.id.btnImg_AddLeisure);
		btnOther = (ImageButton) v.findViewById(R.id.btnImg_AddOther);
		btnMenu = (ImageButton) v.findViewById(R.id.btnImg_Menu);

		btnHouse.setOnClickListener(this);
		btnFood.setOnClickListener(this);
		btnTransportation.setOnClickListener(this);
		btnMedical.setOnClickListener(this);
		btnEntertainment.setOnClickListener(this);
		btnOther.setOnClickListener(this);
		btnMenu.setOnClickListener(this); // 28.7.2015: QuangHV
	}

	@Override
	public void onClick(View v) {
		String categoryName;
		switch (v.getId()) {
		case R.id.btnImg_AddHouse:
			categoryName = getResources()
					.getString(R.string.txt_category_house);
			callAddActivity(categoryName);
			break;
		case R.id.btnImg_AddFood:
			categoryName = getResources().getString(R.string.txt_category_food);
			callAddActivity(categoryName);
			break;
		case R.id.btnImg_AddTransport:
			categoryName = getResources().getString(
					R.string.txt_category_transport);
			callAddActivity(categoryName);
			break;
		case R.id.btnImg_AddMedical:
			categoryName = getResources().getString(
					R.string.txt_category_medical);
			callAddActivity(categoryName);
			break;
		case R.id.btnImg_AddLeisure:
			categoryName = getResources().getString(
					R.string.txt_category_leisure);
			callAddActivity(categoryName);
			break;
		case R.id.btnImg_AddOther:
			categoryName = getResources()
					.getString(R.string.txt_category_other);
			callAddActivity(categoryName);
			break;
		case R.id.btnImg_Menu:
			callChartActivity(); // Quang: Temporary function to test graph
			break;
		default:
			break;
		}
	}

	/*
	 * QuangHV: Add temporary function to display chart
	 */

	public void dummyData() {
		// Dummy data for line chart
		Random rand = new Random();
		for (int i = 0; i < 10; i++) {
			statistic_data.put(Double.valueOf(i),
					Double.valueOf(rand.nextInt(10)));
		}
	}

	public void pieDummyData() {
		// Dummy data for pie chart
		statistic_data_pie.put("Food", Double.valueOf(20));
		statistic_data_pie.put("Transportation", Double.valueOf(15));
		statistic_data_pie.put("House", Double.valueOf(40));
		statistic_data_pie.put("Medical", Double.valueOf(5));
		statistic_data_pie.put("Leisure", Double.valueOf(5));
		statistic_data_pie.put("Other", Double.valueOf(5));
	}

	public void callChartActivity() {

		// show line chart
		// Intent i = new
		// Intent(this.getActivity(),StatisticLineChartActivity.class);
		//
		// dummyData();
		// i.putExtra(INTENT_EXTRA_DATA_LINE, statistic_data);
		// startActivity(i);
		// show pie chart
		Intent i = new Intent(this.getActivity(),
				StatisticPieChartActivity.class);
		pieDummyData();
		i.putExtra(INTENT_EXTRA_DATA_PIE, statistic_data_pie);
		startActivity(i);
	}

	public void callAddActivity(String categoryName) {
		Intent i = new Intent(this.getActivity(), ExpenseAddActivity.class);
		i.putExtra(INTENT_EXTRA_ADD_EXPENSE, categoryName);
		startActivity(i);
	}

	/**
	 * not used Save Expense Date Report data (xml file) to sdcard
	 * 
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
				Log.i(TAG, "Creation of directory " + path
						+ " on sdcard failed");
				return;
			} else {
				Log.i(TAG, "Created directory " + path + " on sdcard");
			}
		}
		// Copy file
		InputStream in = getActivity().getResources().openRawResource(
				R.xml.expense_date);
		InputStreamReader inReader = new InputStreamReader(in, UTF8);
		BufferedReader buffReader = new BufferedReader(inReader);

		OutputStream out = new FileOutputStream(DATA_PATH + "expense_date.xml");
		BufferedWriter buffWriter = new BufferedWriter(new OutputStreamWriter(
				out, UTF8));
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
