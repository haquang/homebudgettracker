package com.pulsardev.homebudgettracker;

import android.app.Activity;
import android.os.Bundle;

public class ExpenseAddActivity extends android.support.v4.app.FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new ExpenseAddFragment()).commit();
		}
	}

	
}
