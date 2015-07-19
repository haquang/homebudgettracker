package com.pulsardev.homebudgettracker;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class ExpenseAddFragment extends Fragment {
	
	// controls
	EditText txtAmount, txtDescription;
	Button btnSave, btnCancel;
	TextView lbCategory;
	Spinner spCategory;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_expense_add,
				container, false);
		
		initControls(rootView);
		// get the default category
		String defaultCat = (String) getActivity().getIntent().getSerializableExtra(ExpenseFragment.INTENT_EXTRA_ADD_EXPENSE);
		lbCategory.setText(lbCategory.getText() + ": " + defaultCat);
		
		btnCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getActivity().finish();
			}
		});
		
		btnSave.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		
		return rootView;
	}

	private void initControls(View v) {
		txtAmount = (EditText) v.findViewById(R.id.edt_espense_amount);
		txtDescription = (EditText) v.findViewById(R.id.edt_espense_desc);
		btnSave = (Button) v.findViewById(R.id.btn_save);
		btnCancel = (Button) v.findViewById(R.id.btn_cancel);
		lbCategory = (TextView) v.findViewById(R.id.txt_default_category);
		spCategory = (Spinner) v.findViewById(R.id.spinner_category);
		
		// Spinner initialization
		
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.category_arrays, R.layout.spinner_item);
		
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		// Apply the adapter to the spinner
		spCategory.setAdapter(adapter);
	}
}
