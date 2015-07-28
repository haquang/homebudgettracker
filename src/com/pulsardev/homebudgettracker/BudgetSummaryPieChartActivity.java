package com.pulsardev.homebudgettracker;

import com.pulsardev.homebudgettracker.view.BudgetSummaryPieChartView;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class BudgetSummaryPieChartActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
			.add(R.id.container, new BudgetSummaryPieChartFragment()).commit();
		}
		//        BudgetSummaryPieChartView mView = new BudgetSummaryPieChartView(this);
		//        requestWindowFeature(Window.FEATURE_NO_TITLE);
		//        setContentView(mView);
	}
}
