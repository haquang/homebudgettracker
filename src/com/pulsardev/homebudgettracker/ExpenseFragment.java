package com.pulsardev.homebudgettracker;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

public class ExpenseFragment extends Fragment implements OnClickListener {
	
	// controls
	ImageButton btnHouse, btnFood, btnTransportation, btnMedical, btnEntertainment, btnOther;
	// key of value that will be passed to FragmentAddActivity
	public static final String INTENT_EXTRA_ADD_EXPENSE = "Add Expense";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_expense,
				container, false);
		
		initControls(rootView);
		
		return rootView;
	}
	
	private void initControls(View v) {
		btnHouse = (ImageButton) v.findViewById(R.id.btnImg_AddHouse);
		btnFood = (ImageButton) v.findViewById(R.id.btnImg_AddFood);
		btnTransportation = (ImageButton) v.findViewById(R.id.btnImg_AddTransport);
		btnMedical = (ImageButton) v.findViewById(R.id.btnImg_AddMedical);
		btnEntertainment = (ImageButton) v.findViewById(R.id.btnImg_AddLeisure);
		btnOther = (ImageButton) v.findViewById(R.id.btnImg_AddOther);
		
		btnHouse.setOnClickListener(this);
		btnFood.setOnClickListener(this);
		btnTransportation.setOnClickListener(this);
		btnMedical.setOnClickListener(this);
		btnEntertainment.setOnClickListener(this);
		btnOther.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		String categoryName;
		switch (v.getId()) {
		case R.id.btnImg_AddHouse:
			categoryName = getResources().getString(R.string.txt_category_house);
			callAddActivity(categoryName);
			break;
		case R.id.btnImg_AddFood:
			categoryName = getResources().getString(R.string.txt_category_food);
			callAddActivity(categoryName);
			break;
		case R.id.btnImg_AddTransport:
			categoryName = getResources().getString(R.string.txt_category_transport);
			callAddActivity(categoryName);
			break;
		case R.id.btnImg_AddMedical:
			categoryName = getResources().getString(R.string.txt_category_medical);
			callAddActivity(categoryName);
			break;
		case R.id.btnImg_AddLeisure:
			categoryName = getResources().getString(R.string.txt_category_leisure);
			callAddActivity(categoryName);
			break;
		case R.id.btnImg_AddOther:
			categoryName = getResources().getString(R.string.txt_category_other);
			callAddActivity(categoryName);
			break;
		default:
			break;
		}
	}
	
	public void callAddActivity(String categoryName) {
		Intent i = new Intent(this.getActivity(), ExpenseAddActivity.class);
		i.putExtra(INTENT_EXTRA_ADD_EXPENSE, categoryName);
		startActivity(i);
	}
}
