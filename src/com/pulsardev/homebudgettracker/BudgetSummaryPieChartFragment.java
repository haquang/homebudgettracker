package com.pulsardev.homebudgettracker;

import com.pulsardev.homebudgettracker.view.BudgetSummaryPieChartView;

import android.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class BudgetSummaryPieChartFragment extends Fragment {

	BudgetSummaryPieChartView pieChartView;
	FrameLayout frameLayout;
	View rootView;
	// Tag
	private static final String TAG = "BudgetSummaryPieChart";

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		addPieView();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub


		rootView = inflater.inflate(R.layout.framegraph, container, false);
		initControls(rootView);
		return rootView;
	}

	private void initControls(View v) {
		frameLayout = (FrameLayout) rootView.findViewById(R.id.pielayout);
	}

	private void addPieView() {
		pieChartView = new BudgetSummaryPieChartView(this.getActivity());
		
		FrameLayout.LayoutParams params_layout_frame = new FrameLayout.LayoutParams(
		            LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);		 
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(params_layout_frame);		
		params.gravity = Gravity.CENTER;		
		frameLayout.addView(pieChartView);

	}

}
