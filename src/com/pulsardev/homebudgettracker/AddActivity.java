package com.pulsardev.homebudgettracker;

import java.io.Serializable;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

public class AddActivity extends android.support.v4.app.FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (savedInstanceState == null) {
			Intent i = getIntent();
			FragmentTransaction fTransaction = getSupportFragmentManager().beginTransaction();
			Serializable temp = i.getSerializableExtra(ExpenseFragment.INTENT_EXTRA_ADD_EXPENSE_CATID);
			if (temp != null) {
				// Add Expense
				fTransaction.add(R.id.add_container, new ExpenseAddFragment());
			} else {
				temp = i.getSerializableExtra(IncomeFragment.INTENT_EXTRA_ADD_INCOME_CATID);
				if (temp != null) {
					// Add Income
					fTransaction.add(R.id.add_container, new IncomeAddFragment());
				} else {
					temp = i.getSerializableExtra(ExpenseDetailFragment.INTENT_EXTRA_EDIT_EXPENSE);
					if (temp != null) {
						// Edit Expense
						fTransaction.add(R.id.add_container, new ExpenseAddFragment());
					}
				}
			}
			fTransaction.commit();
		}
	}
}
