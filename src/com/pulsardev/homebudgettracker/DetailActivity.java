package com.pulsardev.homebudgettracker;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import com.pulsardev.homebudgettracker.util.StaticString;

public class DetailActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (savedInstanceState == null) {
			Intent i = getIntent();
			FragmentTransaction fTransaction = getFragmentManager().beginTransaction();
			String temp = i.getExtras().getString(StaticString.DETAIL_FRAGMENT);
			if (temp.equalsIgnoreCase(ExpenseFragment.INTENT_EXTRA_EXPENSE)) {
				fTransaction.add(R.id.container, new ExpenseDetailFragment());
			} else if (temp.equalsIgnoreCase(IncomeFragment.INTENT_EXTRA_INCOME)) {
				fTransaction.add(R.id.container, new IncomeDetailFragment());
			}
			fTransaction.commit();
		}
	}

}
