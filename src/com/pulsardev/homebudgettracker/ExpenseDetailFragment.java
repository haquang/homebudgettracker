package com.pulsardev.homebudgettracker;

import java.util.ArrayList;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.pulsardev.homebudgettracker.model.ExpenseCategory;
import com.pulsardev.homebudgettracker.model.ExpenseCategoryLab;
import com.pulsardev.homebudgettracker.model.ExpenseDateReport;
import com.pulsardev.homebudgettracker.model.ExpenseDateReportLab;

public class ExpenseDetailFragment extends Fragment {

	// controls
	TextView txtLabel;
	ListView listExpenseDetail;

	// current Exp Category
//	private ExpenseCategory defaultCat;

	// List of Expense Date Reports
	ArrayList<ExpenseDateReport> listExpDateReports;

	ArrayAdapter<String> expDateReportAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// load List of Expense Date Report, which is singleton
		listExpDateReports = ExpenseDateReportLab.get(this.getActivity())
				.getListExpDateReport();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_detail,
				container, false);

		initControls(rootView);

		setDataListView();

		return rootView;
	}

	private void setDataListView() {
		Intent i = getActivity().getIntent();
		ArrayList<String> list = new ArrayList<String>();
		
		// if show detail of specific category
		if (i.hasExtra(ExpenseFragment.INTENT_EXTRA_EXPENSE_DETAIL_CATID)) {
			// load the default Category
			int default_cat_id = (Integer) getActivity().getIntent()
					.getSerializableExtra(
							ExpenseFragment.INTENT_EXTRA_EXPENSE_DETAIL_CATID);
			ExpenseCategory defaultCat = ExpenseCategoryLab.get(getActivity())
					.getExpCategory(default_cat_id);
			for (ExpenseDateReport item : listExpDateReports) {
				if (item.getCategoryID() == default_cat_id) {
					list.add("amount: " + item.getAmount() + ", category = " + defaultCat.getName());
				}
			}
		} else { // if show detail of all categories
			for (ExpenseDateReport item : listExpDateReports) {
				list.add("amount: " + item.getAmount());
			}
		}

		// Set data for ListView
		expDateReportAdapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, list);
		listExpenseDetail.setAdapter(expDateReportAdapter);
	}

	private void initControls(View v) {
		txtLabel = (TextView) v.findViewById(R.id.txt_label);
		listExpenseDetail = (ListView) v.findViewById(R.id.list_expense_detail);

	}

	@Override
	public void onResume() {
		super.onResume();
		expDateReportAdapter.notifyDataSetChanged();
	}
}
