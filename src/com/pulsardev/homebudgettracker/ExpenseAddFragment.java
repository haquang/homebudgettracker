package com.pulsardev.homebudgettracker;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pulsardev.homebudgettracker.model.ExpenseCategory;
import com.pulsardev.homebudgettracker.model.ExpenseCategoryLab;
import com.pulsardev.homebudgettracker.model.ExpenseDateReport;
import com.pulsardev.homebudgettracker.model.ExpenseDateReportLab;

public class ExpenseAddFragment extends Fragment {

	// controls
	EditText edtAmount, edtDate, edtDescription;
	Button btnSave, btnCancel;
	TextView txtCategory;
	Spinner spCategory;

	// current Exp Category
	private ExpenseCategory defaultCat;

	// Tag
	private static final String TAG = "ExpenseAddFragment";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_expense_add,
				container, false);

		initControls(rootView);

		// change the default txtCategory when spinner changed
		spCategory
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View arg1, int position, long arg3) {
						String selectedItem = (String) parent
								.getItemAtPosition(position);
						txtCategory.setText(getResources().getString(
								R.string.txt_main_header)
								+ ": " + selectedItem);
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// no code here
					}

				});

		// not save file and return to Main Screen
		btnCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				getActivity().finish();
			}
		});

		/**
		 * Save date report to XML file
		 * 
		 * @author ngapham
		 * @date 20/7/2015
		 * @update 21/7/2015 1/8/2015: Save new Exp Date Report to JSON file
		 *         4/8/2015: Change "save new" into "Save list"
		 */
		btnSave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (String.valueOf(edtAmount.getText()) == null) {
					Toast.makeText(getActivity(), "Please input amount",
							Toast.LENGTH_SHORT).show();
				} else {
					// Save new Date Report
					ExpenseDateReport newDateReport = new ExpenseDateReport();
					newDateReport.setAmount(Double.valueOf(String
							.valueOf(edtAmount.getText())));
					// must be changed later
					newDateReport.setDate(new java.util.Date(System
							.currentTimeMillis()));
					newDateReport.setCategoryID(spCategory
							.getSelectedItemPosition());
					newDateReport.setDescription(String.valueOf(edtDescription
							.getText()));

					ExpenseDateReportLab.get(getActivity()).addExpDateReport(
							newDateReport);
					ExpenseDateReportLab.get(getActivity())
							.saveListExpDateReport();

					// And update the amount of this category
					ExpenseCategory currentExpCategory = ExpenseCategoryLab
							.get(getActivity()).getExpCategory(
									newDateReport.getCategoryID());
					currentExpCategory.setAmount(currentExpCategory.getAmount() + newDateReport.getAmount());

					// finish
					getActivity().finish();
				}
			}
		});

		return rootView;
	}

	private void initControls(View v) {
		edtAmount = (EditText) v.findViewById(R.id.edt_expense_amount);
		edtDescription = (EditText) v.findViewById(R.id.edt_expense_desc);
		btnSave = (Button) v.findViewById(R.id.btn_save);
		btnCancel = (Button) v.findViewById(R.id.btn_cancel);
		txtCategory = (TextView) v.findViewById(R.id.txt_default_category);
		spCategory = (Spinner) v.findViewById(R.id.spinner_category);

		// get the default category
		int defaultCatId = (Integer) getActivity().getIntent()
				.getSerializableExtra(
						ExpenseFragment.INTENT_EXTRA_ADD_EXPENSE_CATID);
		defaultCat = ExpenseCategoryLab.get(getActivity())
				.getListExpCategories().get(defaultCatId);
		txtCategory.setText(getResources().getString(R.string.txt_main_header)
				+ ": " + defaultCat.getName());

		// Spinner initialization
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				getActivity(), R.array.category_arrays, R.layout.spinner_item);

		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// Apply the adapter to the spinner
		spCategory.setAdapter(adapter);
		// set default category (the order of list Exp Categories matches
		// Category Arrays)
		int spDefaultPosition = defaultCatId;
		spCategory.setSelection(spDefaultPosition);
	}
}
