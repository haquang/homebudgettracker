package com.pulsardev.homebudgettracker;

import java.util.Calendar;
import java.util.Date;
import java.util.InputMismatchException;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView.Validator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pulsardev.homebudgettracker.model.ExpenseCategory;
import com.pulsardev.homebudgettracker.model.ExpenseCategoryLab;
import com.pulsardev.homebudgettracker.model.ExpenseDateReport;
import com.pulsardev.homebudgettracker.model.ExpenseDateReportLab;
import com.pulsardev.homebudgettracker.util.MoneyValueFilter;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

public class ExpenseAddFragment extends android.support.v4.app.Fragment {

	// controls
	EditText edtAmount, edtDate, edtDescription;
	Button btnSave, btnCancel;
	TextView txtCategory;
	Spinner spCategory;

	// current Exp Category
	private ExpenseCategory defaultCat;
	
	// date properties of new Date Report
	private Date newDate;
	
	// date format for edtDate
	private static final String DATE_FORMAT = "yyyy-MM-dd";

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

		/**
		 * set the Calendar View for edtDate
		 * @author ngapham
		 * Date: 3/9/2015
		 */
		edtDate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bundle args = new Bundle();
				Calendar cal = Calendar.getInstance();
				final CaldroidFragment dialogCaldroidFragment = CaldroidFragment.newInstance("Select a date", cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR));
				
//				args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
//				args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
				args.putInt(CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.MONDAY);
				dialogCaldroidFragment.setArguments(args);
				
				// Show the CaldroidFragment as a dialog
				final android.support.v4.app.FragmentTransaction t = getActivity().getSupportFragmentManager().beginTransaction();
				t.addToBackStack(null);
				dialogCaldroidFragment.show(t, "TAG");
				
				// Handle when user click a date
				dialogCaldroidFragment.setCaldroidListener(new CaldroidListener() {
					
					@Override
					public void onSelectDate(Date date, View view) {
						newDate = date;
						String dateFormat = String.valueOf(DateFormat.format(DATE_FORMAT, date));
						edtDate.setText(dateFormat);
						dialogCaldroidFragment.dismiss();
					}
				});
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
					try {
						// Save new Date Report
						ExpenseDateReport newDateReport = new ExpenseDateReport();
						
						// Validate the amount
						com.pulsardev.homebudgettracker.util.Validator.validateNullAmount(String
								.valueOf(edtAmount.getText()));
						
						newDateReport.setAmount(Double.valueOf(String
								.valueOf(edtAmount.getText())));
						newDateReport.setDate(newDate);
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
					} catch (Exception e) {
						Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.toast_amount_null),
					            Toast.LENGTH_LONG).show();
					}
				}
			}
		});

		return rootView;
	}

	private void initControls(View v) {
		edtAmount = (EditText) v.findViewById(R.id.edt_expense_amount);
		edtDate = (EditText) v.findViewById(R.id.edt_expense_date);
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
				getActivity(), R.array.categories_array, R.layout.spinner_item);

		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// Apply the adapter to the spinner
		spCategory.setAdapter(adapter);
		
		// set default category (the order of list Exp Categories matches
		// Category Arrays)
		int spDefaultPosition = defaultCatId;
		spCategory.setSelection(spDefaultPosition);
		
		// set default date 
		newDate = new Date(java.lang.System.currentTimeMillis());
		String dateFormat = String.valueOf(DateFormat.format(DATE_FORMAT, newDate));
		edtDate.setText(dateFormat);
		
		// Limit the number of digits after decimal point in edtAmount
		edtAmount.setFilters(new InputFilter[] { new MoneyValueFilter(2)});
	}
}
