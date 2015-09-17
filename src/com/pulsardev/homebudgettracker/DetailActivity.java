package com.pulsardev.homebudgettracker;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import com.pulsardev.homebudgettracker.util.StaticString;

public class DetailActivity extends NavDrawerActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (savedInstanceState == null) {
			Intent i = getIntent();
			FragmentTransaction fTransaction = getFragmentManager().beginTransaction();
			String temp = i.getExtras().getString(StaticString.DETAIL_FRAGMENT);
			if (temp.equalsIgnoreCase(ExpenseFragment.INTENT_EXTRA_EXPENSE)) {
				fTransaction.replace(R.id.main_container, new ExpenseDetailFragment());
			} else if (temp.equalsIgnoreCase(IncomeFragment.INTENT_EXTRA_INCOME)) {
				fTransaction.replace(R.id.main_container, new IncomeDetailFragment());
			}
			fTransaction.commit();
		}
	}

}
