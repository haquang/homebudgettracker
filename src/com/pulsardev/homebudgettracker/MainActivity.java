/**
 * Manage main Fragments and Navigation Drawer
 * @author ngapham
 * Update 5/9/2015: Manage item click event of Nav Drawer
 */

package com.pulsardev.homebudgettracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.pulsardev.homebudgettracker.model.ExpenseCategory;
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

public class MainActivity extends Activity {

	// controls for Navigation Drawer
	private String[] mDrawerTitles;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;

	// Manage all the main Fragments
	private FragmentManager mFragmentManager;

	// List of Expense Categories
	ArrayList<ExpenseCategory> listExpCategories;

	// key of value that will be passed to ChartActivity
	public static final String FRAGMENT_DATA_LINE = "Statistic Data Line";
	public static final String FRAGMENT_DATA_PIE = "Statistic Data Pie";

	// storage for Statistic Chart
	public static HashMap<Double, Double> statistic_data_line;
	public static HashMap<String, Double> statistic_data_pie;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			mFragmentManager = getFragmentManager();
			mFragmentManager.beginTransaction()
			.add(R.id.container, new ExpenseFragment()).commit();
		}

		// create items for Navigation Drawer list
		mDrawerTitles = getResources().getStringArray(R.array.drawler_items_array);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_item, R.id.drawer_title, mDrawerTitles));

		mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
				Fragment newFragment = null;	//Fragment to replace
				switch (position) {
				case 0:	//Manage Expenses
					newFragment = new ExpenseFragment();
					break;
				case 1: //Manage Income
					newFragment = new IncomeFragment();
					break;
				case 2:	//Distribution
					// load List of Expense Categories, which is singleton
					listExpCategories = ExpenseCategoryLab.get(MainActivity.this)
					.getListExpCategories();
					// Add data to storage of Pie Chart
					statistic_data_pie = new HashMap<String, Double>();
					pieData();
					Bundle pieArgs = new Bundle();
					pieArgs.putSerializable(FRAGMENT_DATA_PIE, statistic_data_pie);
					newFragment = new StatisticPieChartFragment();
					newFragment.setArguments(pieArgs);

					break;
				case 3:	// Report
					// Add data to storage of Line Chart
					statistic_data_line = new HashMap<Double, Double>();
					dummyLineData();
					Bundle lineArgs = new Bundle();
					lineArgs.putSerializable(FRAGMENT_DATA_LINE, statistic_data_line);
					newFragment = new StatisticLineChartFragment();
					newFragment.setArguments(lineArgs);

					break;
				default:
					newFragment = new ExpenseFragment();
					break;
				}
				if (newFragment != null) {
					//Call new fragment
					mFragmentTransaction.replace(R.id.container, newFragment);
					mFragmentTransaction.addToBackStack(null);
					mFragmentTransaction.commit();

					// Highlight the selected item of Nav Drawer, and close the drawer
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
	/*
	 * QuangHV: Add temporary function to display chart
	 */

	public void dummyLineData() {
		// Dummy data for line chart
		Random rand = new Random();
		for (int i = 0; i < 10; i++) {
			statistic_data_line.put(Double.valueOf(i),
					Double.valueOf(rand.nextInt(10)));
		}
	}
}
