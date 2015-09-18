package com.pulsardev.homebudgettracker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pulsardev.homebudgettracker.control.DetailListAdapter;
import com.pulsardev.homebudgettracker.model.Category;
import com.pulsardev.homebudgettracker.model.DateReport;
import com.pulsardev.homebudgettracker.model.ExpenseCategoryLab;
import com.pulsardev.homebudgettracker.model.ExpenseDateReportLab;
import com.pulsardev.homebudgettracker.model.MonthlyReport;
import com.pulsardev.homebudgettracker.util.StaticString;

public class ExpenseDetailFragment extends Fragment {

	/**
	 * Properties
	 */
	// controls
	TextView txtTitle;
	ImageButton btnMenu;

	ExpandableListView listViewExpenseDetail;

	// List of Expense Date Reports
	ArrayList<DateReport> listExpDateReports;

	// Specific Adapter for Expandable ListView
	DetailListAdapter expDateReportAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// load List of Expense Date Report, which is singleton
		listExpDateReports = ExpenseDateReportLab.get(
				this.getActivity().getApplicationContext())
				.getListExpDateReport();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_detail, container,
				false);

		initControls(rootView);
		setNavMenu();
		setTitleName();
		setDataListView();

		return rootView;
	}

	private void initControls(View v) {
		txtTitle = (TextView) v.findViewById(R.id.txt_header);
		btnMenu = (ImageButton) v.findViewById(R.id.btnImg_Menu);
		listViewExpenseDetail = (ExpandableListView) v
				.findViewById(R.id.list_detail);
	}

	private void setNavMenu() {
		btnMenu.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				((NavDrawerActivity) getActivity()).openDrawer();
			}
		});
	}

	private void setTitleName() {
		Resources res = getActivity().getResources();
		txtTitle.setText(res.getString(R.string.txt_expense_header) + " "
				+ res.getString(R.string.txt_detail_header));
	}

	private void setDataListView() {
		Intent intent = getActivity().getIntent();
		ArrayList<MonthlyReport> groups;
		// if show detail of specific category
		if (intent.hasExtra(ExpenseFragment.INTENT_EXTRA_EXPENSE_DETAIL_CATID)) {
			// load the default Category
			int default_cat_id = (Integer) getActivity().getIntent()
					.getSerializableExtra(
							ExpenseFragment.INTENT_EXTRA_EXPENSE_DETAIL_CATID);
			Category defaultCat = ExpenseCategoryLab.get(
					getActivity().getApplicationContext()).getExpCategory(
					default_cat_id);
			ArrayList<DateReport> listByCat = new ArrayList<DateReport>();
			for (DateReport item : listExpDateReports) {
				if (item.getCategoryID() == default_cat_id) {
					listByCat.add(item);
				}
			}
			groups = monthlyReport(listByCat);
		} else { // if show detail of all categories
			groups = monthlyReport(listExpDateReports);
		}

		// Set data for ListView
		expDateReportAdapter = new DetailListAdapter(groups, this.getActivity());
		listViewExpenseDetail.setAdapter(expDateReportAdapter);
	}

	/**
	 * group Date Reports by month
	 * 
	 * @param listDateReport
	 * @return
	 */
	private ArrayList<MonthlyReport> monthlyReport(
			ArrayList<DateReport> listDateReport) {
		ArrayList<MonthlyReport> groups = new ArrayList<MonthlyReport>();
		SimpleDateFormat fullMonthFormat = new SimpleDateFormat(
				StaticString.FULL_MONTH_FORMAT);
		Map<String, ArrayList<DateReport>> map = new HashMap<String, ArrayList<DateReport>>();
		for (DateReport item : listDateReport) {
			String fullMonth = fullMonthFormat.format(item.getDate());
			if (map.get(fullMonth) == null) {
				map.put(fullMonth, new ArrayList<DateReport>());
			}
			map.get(fullMonth).add(item);
		}
		for (Map.Entry<String, ArrayList<DateReport>> entry : map.entrySet()) {
			MonthlyReport group = new MonthlyReport(entry.getKey(),
					totalAmount(entry.getValue()), entry.getValue());
			groups.add(group);
		}
		return groups;
	}

	private double totalAmount(ArrayList<DateReport> list) {
		double amount = 0.0;
		for (DateReport item : list) {
			amount += item.getAmount();
		}
		return amount;
	}

	@Override
	public void onResume() {
		super.onResume();
		expDateReportAdapter.notifyDataSetChanged();
	}
}
