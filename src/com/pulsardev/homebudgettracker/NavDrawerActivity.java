package com.pulsardev.homebudgettracker;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Random;

import com.pulsardev.homebudgettracker.model.Category;
import com.pulsardev.homebudgettracker.model.ExpenseCategoryLab;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class NavDrawerActivity extends Activity {

	// controls for Navigation Drawer
	private String[] mDrawerTitles;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;

	// Manage all the main Fragments
	protected FragmentManager mFragmentManager;

	// List of Expense Categories
	ArrayList<Category> listExpCategories;

	// key of value that will be passed to ChartActivity
	public static final String LINE_EXPENSE_DATA = "Statistic Expense Data Line";
	public static final String LINE_INCOME_DATA = "Statistic Income Data Line";
	public static final String PIE_EXPENSE_DATA = "Statistic Expense Data Pie";
	public static final String PIE_INCOME_DATA = "Statistic Income Data Pie";

	// storage for Statistic Chart
	public static HashMap<Date, Double> line_expense_data;
	public static HashMap<Date, Double> line_income_data;
	public static HashMap<String, Double> pie_expense_data;
	public static HashMap<String, Double> pie_income_data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		mFragmentManager = getFragmentManager();

		// create items for Navigation Drawer list
		mDrawerTitles = getResources().getStringArray(
				R.array.drawler_items_array);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_item, R.id.drawer_title, mDrawerTitles));

		mDrawerList
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						FragmentTransaction mFragmentTransaction = mFragmentManager
								.beginTransaction();
						Fragment newFragment = null; // Fragment to replace
						switch (position) {
						case 0: // Manage Expenses
							newFragment = new ExpenseFragment();
							break;
						case 1: // Manage Income
							newFragment = new IncomeFragment();
							break;
						case 2: // Distribution
							// load List of Expense Categories, which is
							// singleton
							listExpCategories = ExpenseCategoryLab.get(
									getApplicationContext())
									.getListExpCategories();
							// Add data to storage of Pie Char
							pie_expense_data = new HashMap<String, Double>();
							pieData();
							Bundle pieArgs = new Bundle();
							pieArgs.putSerializable(PIE_EXPENSE_DATA,
									pie_expense_data);
							newFragment = new StatisticPieChartFragment();
							newFragment.setArguments(pieArgs);

							break;
						case 3: // Report
							// Add data to storage of Line Chart
							line_expense_data = new HashMap<Date, Double>();
							line_income_data = new HashMap<Date, Double>();
							dummyLineData();
							Bundle lineArgs = new Bundle();
							lineArgs.putSerializable(LINE_EXPENSE_DATA,
									line_expense_data);
							lineArgs.putSerializable(LINE_INCOME_DATA,
									line_income_data);
							newFragment = new StatisticLineChartFragment();
							newFragment.setArguments(lineArgs);

							break;
						default:
							newFragment = new ExpenseFragment();
							break;
						}
						if (newFragment != null) {
							// Call new fragment
							mFragmentTransaction.replace(R.id.main_container,
									newFragment);
							mFragmentTransaction.addToBackStack(null);
							mFragmentTransaction.commit();

							// Highlight the selected item of Nav Drawer, and
							// close the drawer
							mDrawerList.setItemChecked(position, true);
							mDrawerLayout.closeDrawer(mDrawerList);
						}
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		return super.onOptionsItemSelected(item);
	}

	public void openDrawer() {
		mDrawerLayout.openDrawer(Gravity.LEFT);
	}

	/**
	 * update real data
	 * 
	 * @author ngapham
	 * @date 23/8/2015
	 */
	public void pieData() {
		// Dummy data for pie chart
		for (int i = 0; i < listExpCategories.size(); i++) {
			Category currentExpCategory = listExpCategories.get(i);
			pie_expense_data.put(currentExpCategory.getName(),
					currentExpCategory.getAmount());
		}
	}

	/*
	 * QuangHV: Add temporary function to display chart
	 */

	public void dummyLineData() {
		// Dummy data for line chart
		Date[] dt = new Date[10];
		for (int i = 0; i < 10; i++) {
			GregorianCalendar gc = new GregorianCalendar(2012, 10, i + 1);
			dt[i] = gc.getTime();
		}
		Random rand = new Random();
		for (int i = 0; i < 10; i++) {
			line_expense_data.put(dt[i], Double.valueOf(rand.nextInt(10)));
			line_income_data.put(dt[i], Double.valueOf(rand.nextInt(10)));
		}
	}
}
