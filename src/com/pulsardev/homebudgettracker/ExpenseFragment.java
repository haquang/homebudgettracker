/**
 * main fragment: manage Expense total and Expense Category
 * @author ngapham
 */

package com.pulsardev.homebudgettracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
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
	
	// Navigation Drawer
	private DrawerLayout mDrawerLayout;

	// controls
	ImageButton btnHouse, btnFood, btnTransportation, btnMedical,
			btnEntertainment, btnOther;
	TextView txtCatFood, txtCatTransportation, txtCatHousing, txtCatMedical,
			txtCatEntertainment, txtCatOther;
	TextView txtFoodAmount, txtTransportAmount, txtHousingAmount,
			txtMedicalAmount, txtEntertainmentAmount, txtOtherAmount;
	ImageButton btnMenu; // Quang - 28.7.2015
	TextView txtTotalAmount;

	// key of value that will be passed to Add activity and Detail activity 
	public static final String INTENT_EXTRA_ADD_EXPENSE_CATID = "Add_ExpCatId";
	public static final String INTENT_EXTRA_EXPENSE_DETAIL_CATID = "ExpDetail_CatId";
	
	// key of value that will be passed to ChartActivity
	public static final String INTENT_EXTRA_DATA_LINE = "Statistic Data Line";
	public static final String INTENT_EXTRA_DATA_PIE = "Statistic Data Pie";
	
	// storage for Statistic Chart
	public static final HashMap<Double, Double> statistic_data = new HashMap<Double, Double>();
	public static final HashMap<String, Double> statistic_data_pie = new HashMap<String, Double>();

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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_expense, container,
				false);

		initControls(rootView);
		setTitleName();
		showTotalAmount();
		showCatAmount();
		
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
	 * Update amount when add new and return to this fragment
	 * @author ngapham
	 * @date 4/8/2015
	 */
	@Override
	public void onResume() {
		super.onResume();
		showTotalAmount();
		showCatAmount();
	}

	private void initControls(View v) {
		mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
		btnMenu = (ImageButton) v.findViewById(R.id.btnImg_Menu);
		
		txtCatFood = (TextView) v.findViewById(R.id.txt_category_food);
		txtCatTransportation = (TextView) v
				.findViewById(R.id.txt_category_transport);
		txtCatHousing = (TextView) v.findViewById(R.id.txt_category_house);
		txtCatMedical = (TextView) v.findViewById(R.id.txt_category_medical);
		txtCatEntertainment = (TextView) v
				.findViewById(R.id.txt_category_leisure);
		txtCatOther = (TextView) v.findViewById(R.id.txt_category_other);
		
		txtTotalAmount = (TextView) v
				.findViewById(R.id.edt_total_expense_amount);
		txtFoodAmount = (TextView) v.findViewById(R.id.txt_food_amount);
		txtTransportAmount = (TextView) v.findViewById(R.id.txt_transport_amount);
		txtHousingAmount = (TextView) v.findViewById(R.id.txt_house_amount);
		txtMedicalAmount = (TextView) v.findViewById(R.id.txt_medical_amount);
		txtEntertainmentAmount = (TextView) v.findViewById(R.id.txt_leisure_amount);
		txtOtherAmount = (TextView) v.findViewById(R.id.txt_other_amount);

		btnMenu.setOnClickListener(this); // 28.7.2015: QuangHV
		
		btnHouse = (ImageButton) v.findViewById(R.id.btnImg_AddHouse);
		btnFood = (ImageButton) v.findViewById(R.id.btnImg_AddFood);
		btnTransportation = (ImageButton) v
				.findViewById(R.id.btnImg_AddTransport);
		btnMedical = (ImageButton) v.findViewById(R.id.btnImg_AddMedical);
		btnEntertainment = (ImageButton) v.findViewById(R.id.btnImg_AddLeisure);
		btnOther = (ImageButton) v.findViewById(R.id.btnImg_AddOther);
		
		// show detail
		txtCatFood.setOnClickListener(this);
		txtCatTransportation.setOnClickListener(this);
		txtCatHousing.setOnClickListener(this);
		txtCatMedical.setOnClickListener(this);
		txtCatEntertainment.setOnClickListener(this);
		txtCatOther.setOnClickListener(this);
		
		// add new
		btnHouse.setOnClickListener(this);
		btnFood.setOnClickListener(this);
		btnTransportation.setOnClickListener(this);
		btnMedical.setOnClickListener(this);
		btnEntertainment.setOnClickListener(this);
		btnOther.setOnClickListener(this);
	}

	/**
	 * @author ngapham 
	 * update: 2/8/2015
	 */
	private void showTotalAmount() {
		Double amount = 0.0;
		for (ExpenseCategory item : listExpCategories) {
			amount += item.getAmount();
		}
		txtTotalAmount.setText(String.valueOf(amount) + " USD");
	}

	/**
	 * @author ngapham
	 * update 9/8/2015
	 * update 23/8/2015
	 */
	private void showCatAmount() {
		txtFoodAmount.setText(listExpCategories.get(0).getAmount() + " USD");
		txtTransportAmount.setText(listExpCategories.get(1).getAmount() + " USD");
		txtHousingAmount.setText(listExpCategories.get(2).getAmount() + " USD");
		txtMedicalAmount.setText(listExpCategories.get(3).getAmount() + " USD");
		txtEntertainmentAmount.setText(listExpCategories.get(4).getAmount() + " USD");
		txtOtherAmount.setText(listExpCategories.get(5).getAmount() + " USD");
		
	}
	
	@Override
	public void onClick(View v) {
		ExpenseCategory currentCategory;
		switch (v.getId()) {
		case R.id.btnImg_Menu:
//			callChartActivity(); // Quang: Temporary function to test graph
			mDrawerLayout.openDrawer(Gravity.LEFT);
			break;
		// click button Add to add new Exp Date Report
		case R.id.btnImg_AddFood:
			currentCategory = listExpCategories.get(0);
			callAddActivity(currentCategory);
			break;
		case R.id.btnImg_AddTransport:
			currentCategory = listExpCategories.get(1);
			callAddActivity(currentCategory);
			break;
		case R.id.btnImg_AddHouse:
			currentCategory = listExpCategories.get(2);
			callAddActivity(currentCategory);
			break;
		case R.id.btnImg_AddMedical:
			currentCategory = listExpCategories.get(3);
			callAddActivity(currentCategory);
			break;
		case R.id.btnImg_AddLeisure:
			currentCategory = listExpCategories.get(4);
			callAddActivity(currentCategory);
			break;
		case R.id.btnImg_AddOther:
			currentCategory = listExpCategories.get(5);
			callAddActivity(currentCategory);
			break;
			
		// click to show Detail
		case R.id.txt_total_expense_amount:
			callDetailActivity();
			break;
		case R.id.txt_category_food:
			currentCategory = listExpCategories.get(0);
			callDetailActivity(currentCategory);
			break;
		case R.id.txt_category_transport:
			currentCategory = listExpCategories.get(1);
			callDetailActivity(currentCategory);
			break;
		case R.id.txt_category_house:
			currentCategory = listExpCategories.get(2);
			callDetailActivity(currentCategory);
			break;
		case R.id.txt_category_medical:
			currentCategory = listExpCategories.get(3);
			callDetailActivity(currentCategory);
			break;
		case R.id.txt_category_leisure:
			currentCategory = listExpCategories.get(4);
			callDetailActivity(currentCategory);
			break;
		case R.id.txt_category_other:
			currentCategory = listExpCategories.get(5);
			callDetailActivity(currentCategory);
			break;
		default:
			break;
		}
	}

	private void callDetailActivity(ExpenseCategory currentCategory) {
		Intent i = new Intent(this.getActivity(), ExpenseDetailActivity.class);
		i.putExtra(INTENT_EXTRA_EXPENSE_DETAIL_CATID, currentCategory.getID());
		startActivity(i);
	}

	private void callDetailActivity() {
		Intent i = new Intent(this.getActivity(), ExpenseDetailActivity.class);
		startActivity(i);
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
	
	/**
	 * update real data
	 * @author ngapham
	 * @date 23/8/2015
	 */
	public void pieData() {
		// Dummy data for pie chart
		for (int i = 0; i < listExpCategories.size(); i++) {
			ExpenseCategory currentExpCategory = listExpCategories.get(i);
			statistic_data_pie.put(currentExpCategory.getName(), currentExpCategory.getAmount());
		}
	}

	public void callChartActivity() {
		Intent i = new Intent(this.getActivity(),
				StatisticPieChartActivity.class);
		pieData();
		i.putExtra(INTENT_EXTRA_DATA_PIE, statistic_data_pie);
		startActivity(i);
	}

	public void callAddActivity(ExpenseCategory currentCat) {
		Intent i = new Intent(this.getActivity(), ExpenseAddActivity.class);
		i.putExtra(INTENT_EXTRA_ADD_EXPENSE_CATID, currentCat.getID());
		startActivity(i);
	}
}
