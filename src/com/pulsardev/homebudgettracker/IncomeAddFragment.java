/**
 * Add fragment: add new Income Date Report
 * @author ngapham
 * @date 10/9/2015
 */
package com.pulsardev.homebudgettracker;

import java.util.Calendar;
import java.util.Date;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.format.DateFormat;
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

import com.pulsardev.homebudgettracker.model.Category;
import com.pulsardev.homebudgettracker.model.DateReport;
import com.pulsardev.homebudgettracker.model.IncomeCategoryLab;
import com.pulsardev.homebudgettracker.model.IncomeDateReportLab;
import com.pulsardev.homebudgettracker.util.MoneyValueFilter;
import com.pulsardev.homebudgettracker.util.StaticString;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

public class IncomeAddFragment extends android.support.v4.app.Fragment {
	// controls
	EditText edtAmount, edtDate, edtDescription;
	Button btnSave, btnCancel;
	TextView txtCategory;
	Spinner spCategory;

	// current Income Category
	private Category defaultCat;

	// date properties of new Date Report
	private Date newDate;

	// static String
	private static final String CALDROID_FRAGMENT_TITLE = "Select a date";

	// Tag
	private static final String TAG = "IncomeAddFragment";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_add, container,
				false);

		initControls(rootView);
		handleSpinnerChanged();
		handleEdtDate();
		handleBtnCancel();
		handleBtnSave();

		return rootView;
	}

	private void initControls(View v) {
		edtAmount = (EditText) v.findViewById(R.id.edt_amount);
		edtDate = (EditText) v.findViewById(R.id.edt_date);
		edtDescription = (EditText) v.findViewById(R.id.edt_desc);
		btnSave = (Button) v.findViewById(R.id.btn_save);
		btnCancel = (Button) v.findViewById(R.id.btn_cancel);
		txtCategory = (TextView) v.findViewById(R.id.txt_default_category);
		spCategory = (Spinner) v.findViewById(R.id.spinner_category);

		setSpinner();
		setDefaultCategory();
		setDefaultDate();
		setAmountFilter();
	}

	private void setSpinner() {
		// Spinner initialization
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				getActivity(), R.array.in_categories_array,
				R.layout.spinner_item);

		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// Apply the adapter to the spinner
		spCategory.setAdapter(adapter);
	}

	private void setDefaultCategory() {
		// get the default category
		int defaultCatId = (Integer) getActivity().getIntent()
				.getSerializableExtra(
						IncomeFragment.INTENT_EXTRA_ADD_INCOME_CATID);
		defaultCat = IncomeCategoryLab.get(
				getActivity().getApplicationContext()).getInCategory(
				defaultCatId);
		txtCategory.setText(getResources()
				.getString(R.string.txt_income_header)
				+ ": "
				+ defaultCat.getName());

		// set default category for spCategory (the order of list Exp Categories
		// matches
		// Category Arrays)
		int spDefaultPosition = defaultCatId;
		spCategory.setSelection(spDefaultPosition);
	}

	private void setDefaultDate() {
		// set default date
		newDate = new Date(java.lang.System.currentTimeMillis());
		String dateFormat = String.valueOf(DateFormat.format(
				StaticString.DATE_FORMAT, newDate));
		edtDate.setText(dateFormat);
	}

	private void setAmountFilter() {
		// Limit the number of digits after decimal point in edtAmount
		edtAmount.setFilters(new InputFilter[] { new MoneyValueFilter(2) });
	}

	/**
	 * Change the default txtCategory when spinner changed
	 * 
	 * @date: 10/9/2015
	 */
	private void handleSpinnerChanged() {

		spCategory
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View arg1, int position, long arg3) {
						String selectedItem = (String) parent
								.getItemAtPosition(position);
						txtCategory.setText(getResources().getString(
								R.string.txt_income_header)
								+ ": " + selectedItem);
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// no code here
					}
				});
	}

	/**
	 * set the Calendar View for edtDate
	 * 
	 * @author ngapham Date: 10/9/2015
	 */
	private void handleEdtDate() {
		edtDate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Create and pass data for Calendar view
				Bundle args = new Bundle();
				Calendar cal = Calendar.getInstance();
				final CaldroidFragment dialogCaldroidFragment = CaldroidFragment
						.newInstance(CALDROID_FRAGMENT_TITLE,
								cal.get(Calendar.MONTH) + 1,
								cal.get(Calendar.YEAR));
				args.putInt(CaldroidFragment.START_DAY_OF_WEEK,
						CaldroidFragment.MONDAY);
				dialogCaldroidFragment.setArguments(args);

				// Show the CaldroidFragment as a dialog
				final android.support.v4.app.FragmentTransaction t = getActivity()
						.getSupportFragmentManager().beginTransaction();
				t.addToBackStack(null);
				dialogCaldroidFragment.show(t, TAG);

				// Handle when user click a date
				dialogCaldroidFragment
						.setCaldroidListener(new CaldroidListener() {

							@Override
							public void onSelectDate(Date date, View view) {
								newDate = date;
								String dateFormat = String.valueOf(DateFormat
										.format(StaticString.DATE_FORMAT, date));
								edtDate.setText(dateFormat);
								dialogCaldroidFragment.dismiss();
							}
						});
			}
		});
	}

	/**
	 * Save date report to JSON file
	 * 
	 * @author ngapham
	 * @date 10/9/2015
	 */
	private void handleBtnSave() {
		btnSave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					// Save new Date Report
					DateReport newDateReport = new DateReport();

					// Validate the amount
					com.pulsardev.homebudgettracker.util.Validator
							.validateNullAmount(String.valueOf(edtAmount
									.getText()));

					newDateReport.setAmount(Double.valueOf(String
							.valueOf(edtAmount.getText())));
					newDateReport.setDate(newDate);
					newDateReport.setCategoryID(spCategory
							.getSelectedItemPosition());
					newDateReport.setDescription(String.valueOf(edtDescription
							.getText()));

					IncomeDateReportLab dateReportLab = IncomeDateReportLab
							.get(getActivity().getApplicationContext());
					dateReportLab.addInDateReport(newDateReport);
					dateReportLab.saveListInDateReport();

					// And update the amount of this category
					IncomeCategoryLab catLab = IncomeCategoryLab
							.get(getActivity().getApplicationContext());
					catLab.updateCatAmount(newDateReport.getCategoryID(),
							newDateReport.getAmount());
					catLab.saveListCat();

					// finish
					getActivity().finish();
				} catch (Exception e) {
					Toast.makeText(
							getActivity().getApplicationContext(),
							getResources()
									.getString(R.string.toast_amount_null),
							Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	/**
	 * not save file and return to Main Screen
	 */
	private void handleBtnCancel() {
		btnCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				getActivity().finish();
			}
		});
	}
}
