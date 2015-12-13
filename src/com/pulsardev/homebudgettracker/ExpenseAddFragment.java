/**
 * Add new Expense Date Report or Edit current Expense Date Report
 * @author ngapham
 * update 10/9/2015
 * update 28/9/2015
 */
package com.pulsardev.homebudgettracker;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

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
import com.pulsardev.homebudgettracker.model.ExpenseCategoryLab;
import com.pulsardev.homebudgettracker.model.ExpenseDateReportLab;
import com.pulsardev.homebudgettracker.util.MoneyValueFilter;
import com.pulsardev.homebudgettracker.util.StaticString;
import com.pulsardev.homebudgettracker.util.Validator;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

public class ExpenseAddFragment extends android.support.v4.app.Fragment {

	// controls
	TextView txtTitle; // title appears in menu bar
	EditText edtAmount, edtDate, edtDescription;
	Button btnSave, btnCancel;
	Spinner spCategory;

	// date properties of new/editing Date Report
	private Date defaultDate;

	// current Date Report; if adding new Expense, this is new Date Report
	private DateReport defaultDateReport;

	// static String
	private static final String CALDROID_FRAGMENT_TITLE = "Select a date";

	// Tag
	private static final String TAG = "ExpenseAddFragment";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// to know if Add or Edit Date Report
		Serializable add_edit_flag = getActivity().getIntent()
				.getSerializableExtra(
						ExpenseFragment.INTENT_EXTRA_ADD_EXPENSE_CATID);
		if (add_edit_flag != null) { // Add new Expense
			defaultDateReport = new DateReport();
		} else { // Edit current Expense
			add_edit_flag = getActivity().getIntent().getSerializableExtra(
					ExpenseDetailFragment.INTENT_EXTRA_EDIT_EXPENSE);
			if (add_edit_flag != null) {
				UUID currentReportId = (UUID) add_edit_flag;
				defaultDateReport = ExpenseDateReportLab.get(
						getActivity().getApplicationContext()).getDateReport(
						currentReportId);
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_add, container,
				false);

		initControls(rootView);
		setTitleName();
		setDefaultData();

		// handleSpinnerChanged();
		handleEdtDate();
		handleBtnCancel();
		handleBtnSave();

		return rootView;
	}

	/**
	 * Change the default txtCategory when spinner changed
	 */
	private void handleSpinnerChanged() {
		spCategory
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View arg1, int position, long arg3) {
						String selectedItem = (String) parent
								.getItemAtPosition(position);

						txtTitle.setText(getResources().getString(
								R.string.txt_expense_header)
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
	 * @author ngapham Date: 3/9/2015
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
								defaultDate = date;
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

	/**
	 * Save date report to JSON file
	 * 
	 * @author ngapham
	 * @date 20/7/2015
	 * @update 21/7/2015 1/8/2015: Save new Exp Date Report to JSON file
	 *         4/8/2015: Change "save new" into "Save list"
	 *         29/9/2015: Change to 2 cases: save new and edit function
	 */
	private void handleBtnSave() {
		btnSave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					// Validate the amount
					Validator.validateNullAmount(String.valueOf(edtAmount
							.getText()));

					double oldAmount = defaultDateReport.getAmount();
					defaultDateReport.setAmount(Double.valueOf(String
							.valueOf(edtAmount.getText())));
					defaultDateReport.setDate(defaultDate);
					defaultDateReport.setCategoryID(spCategory
							.getSelectedItemPosition());
					defaultDateReport.setDescription(String
							.valueOf(edtDescription.getText()));

					ExpenseDateReportLab dateReportLab = ExpenseDateReportLab
							.get(getActivity().getApplicationContext());
					ExpenseCategoryLab catLab = ExpenseCategoryLab
							.get(getActivity().getApplicationContext());

					if (dateReportLab.getDateReport(defaultDateReport.getID()) == null) {
						// in case of Add new Expense
						// Save new Date Report
						dateReportLab.addExpDateReport(defaultDateReport);
						// And update the amount of this category
						catLab.addNewReportAmount(
								defaultDateReport.getCategoryID(),
								defaultDateReport.getAmount());
					} else {
						// In case of Edit current Expense
						// update the amount of this category
						catLab.updateCatAmount(
								defaultDateReport.getCategoryID(), oldAmount,
								defaultDateReport.getAmount());
					}

					dateReportLab.saveListExpDateReport();
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

	private void initControls(View v) {
		txtTitle = (TextView) v.findViewById(R.id.txt_add_edit_title);
		edtAmount = (EditText) v.findViewById(R.id.edt_amount);
		edtDate = (EditText) v.findViewById(R.id.edt_date);
		edtDescription = (EditText) v.findViewById(R.id.edt_desc);
		btnSave = (Button) v.findViewById(R.id.btn_save);
		btnCancel = (Button) v.findViewById(R.id.btn_cancel);
		spCategory = (Spinner) v.findViewById(R.id.spinner_category);
	}

	private void setTitleName() {
		txtTitle.setText(getResources().getString(
				R.string.txt_add_expense_header));
	}

	private void setDefaultData() {
		setSpinner();
		setDefaultCategory();
		setDefaultDate();
		setDefaultAmount();
		setAmountFilter();
		setDefaultDesc();
	}

	private void setSpinner() {
		// Spinner initialization
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				getActivity(), R.array.exp_categories_array,
				R.layout.spinner_item);

		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// Apply the adapter to the spinner
		spCategory.setAdapter(adapter);
	}

	private void setDefaultCategory() {
		// get default Category
		int defaultCatId = 0;
		if (getActivity().getIntent().getSerializableExtra(
				ExpenseFragment.INTENT_EXTRA_ADD_EXPENSE_CATID) != null) {
			// Add new Expense
			defaultCatId = (Integer) getActivity().getIntent()
					.getSerializableExtra(
							ExpenseFragment.INTENT_EXTRA_ADD_EXPENSE_CATID);
		} else if (getActivity().getIntent().getSerializableExtra(
				ExpenseDetailFragment.INTENT_EXTRA_EDIT_EXPENSE) != null) {
			// Edit current Expense
			defaultCatId = defaultDateReport.getCategoryID();
		}
		
		// set default category for spCategory (the order of list Exp Categories
		// matches
		// Category Arrays)
		int spDefaultPosition = defaultCatId;
		spCategory.setSelection(spDefaultPosition);
	}

	private void setDefaultDate() {
		defaultDate = defaultDateReport.getDate();
		if (defaultDate == null) {
			defaultDate = new Date(System.currentTimeMillis());
		}
		String dateFormat = String.valueOf(DateFormat.format(
				StaticString.DATE_FORMAT, defaultDate));
		edtDate.setText(dateFormat);
	}

	/**
	 * In case of Edit current Expense
	 */
	private void setDefaultAmount() {
		double currentAmount = defaultDateReport.getAmount();
		if (currentAmount != 0.0) {
			edtAmount.setText(String.valueOf(currentAmount));
		}
	}

	private void setAmountFilter() {
		// Limit the number of digits after decimal point in edtAmount
		edtAmount.setFilters(new InputFilter[] { new MoneyValueFilter(2) });
	}

	private void setDefaultDesc() {
		String currentDesc = defaultDateReport.getDescription();
		edtDescription.setText(currentDesc);
	}
}
