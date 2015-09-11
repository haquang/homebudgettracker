/**
 * main fragment: manage Income total and Income Category
 * @author ngapham
 * @date 8/9/2015
 */
package com.pulsardev.homebudgettracker;

import java.util.ArrayList;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pulsardev.homebudgettracker.model.IncomeCategory;
import com.pulsardev.homebudgettracker.model.IncomeCategoryLab;
import com.pulsardev.homebudgettracker.model.IncomeDateReport;
import com.pulsardev.homebudgettracker.model.IncomeDateReportLab;

public class IncomeFragment extends Fragment implements OnClickListener {

	/**
	 * Properties
	 */
	// controls
	ImageButton btnMenu;
	TextView txtTitle;
	
	TextView txtTotal, txtTotalAmount;
	TextView txtCatMonthly, txtCatOther;
	TextView txtMonthlyAmount, txtOtherAmount;
	ImageButton btnAddMonthly, btnAddOther;

	// key of value that will be passed to Add activity and Detail activity 
	public static final String INTENT_EXTRA_ADD_INCOME_CATID = "Add_InCatId";
	public static final String INTENT_EXTRA_INCOME_DETAIL_CATID = "InDetail_CatId";

	// TAG
	private static final String TAG = "IncomeFragment";
	
	// List of Income Categories
	ArrayList<IncomeCategory> listInCategories;
	// List of Income Date Reports
	ArrayList<IncomeDateReport> listInDateReports;
	
	/**
	 * Functions
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Load list of Income Categories, which is singleton
		listInCategories = IncomeCategoryLab.get(getActivity())
				.getListInCategories();
		// Load list of Income Date Reports, which is singleton
		listInDateReports = IncomeDateReportLab.get(getActivity())
				.getListInDateReport();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_income, container, false);
		
		initControls(rootView);
		setTitleName();
		setCatName();
		showTotalAmount();
		showCatAmount();
		
		return rootView;
	}

	private void setTitleName() {
		txtTitle.setText(getResources().getString(R.string.txt_income_header));
	}

	private void setCatName() {
		// Monthly Income
		txtCatMonthly.setText(listInCategories.get(0).getName());
		// Other Income
		txtCatOther.setText(listInCategories.get(1).getName());
	}

	private void showTotalAmount() {
		Double amount = 0.0;
		for (IncomeCategory item : listInCategories) {
			amount += item.getAmount();
		}
		txtTotalAmount.setText(String.valueOf(amount) + " USD");
	}

	private void showCatAmount() {
		txtMonthlyAmount.setText(listInCategories.get(0).getAmount() + " USD");
		txtOtherAmount.setText(listInCategories.get(1).getAmount() + " USD");
	}

	private void initControls(View v) {
		btnMenu = (ImageButton) v.findViewById(R.id.btnImg_Menu);
		txtTitle = (TextView) v.findViewById(R.id.txt_header);
		
		txtTotal = (TextView) v.findViewById(R.id.txt_total);
		txtCatMonthly = (TextView) v.findViewById(R.id.txt_monthly_income_category);
		txtCatOther = (TextView) v.findViewById(R.id.txt_other_income_category);
		
		txtTotalAmount = (TextView) v.findViewById(R.id.txt_total_amount);
		txtMonthlyAmount = (TextView) v.findViewById(R.id.txt_monthly_income_amount);
		txtOtherAmount = (TextView) v.findViewById(R.id.txt_other_income_amount);
		
		btnAddMonthly = (ImageButton) v.findViewById(R.id.btnImg_AddMonthlyIncome);
		btnAddOther = (ImageButton) v.findViewById(R.id.btnImg_AddOtherIncome);
		
		btnMenu.setOnClickListener(this);
		
		// Show detail
		txtTotal.setOnClickListener(this);
		txtCatMonthly.setOnClickListener(this);
		txtCatOther.setOnClickListener(this);
		
		// Add new
		btnAddMonthly.setOnClickListener(this);
		btnAddOther.setOnClickListener(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		showTotalAmount();
		showCatAmount();
	}

	@Override
	public void onClick(View v) {
		IncomeCategory currentCategory;
		switch (v.getId()) {
		// Click button Menu to show menu
		case R.id.btnImg_Menu:
			((MainActivity) this.getActivity()).openDrawer();
			break;
			
		// Click button Add to add new Income Date Report
		case R.id.btnImg_AddMonthlyIncome:
			currentCategory = listInCategories.get(0);
			callAddActivity(currentCategory);
			break;
		case R.id.btnImg_AddOtherIncome:
			currentCategory = listInCategories.get(1);
			callAddActivity(currentCategory);
			break;
			
		// Click TextView to show Detail
		case R.id.txt_total:
			callDetailActivity();
			break;
		case R.id.txt_monthly_income_category:
			currentCategory = listInCategories.get(0);
			callDetailActivity(currentCategory);
			break;
		case R.id.txt_other_income_category:
			currentCategory = listInCategories.get(1);
			callDetailActivity(currentCategory);
			break;
		
		default:
			break;
		}
	}

	private void callAddActivity(IncomeCategory currentCat) {
		Intent i = new Intent(this.getActivity(), ExpenseAddActivity.class);
		i.putExtra(INTENT_EXTRA_ADD_INCOME_CATID, currentCat.getID());
		startActivity(i);
	}

	private void callDetailActivity() {
		// TODO Auto-generated method stub
		
	}

	private void callDetailActivity(IncomeCategory currentCategory) {
		// TODO Auto-generated method stub
		
	}
}
