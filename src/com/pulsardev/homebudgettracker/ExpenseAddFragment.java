package com.pulsardev.homebudgettracker;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import com.pulsardev.homebudgettracker.control.XMLParser;
import com.pulsardev.homebudgettracker.model.ExpenseDateReport;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ExpenseAddFragment extends Fragment {
	
	// controls
	EditText edtAmount, edtDate, edtDescription;
	Button btnSave, btnCancel;
	TextView txtCategory;
	Spinner spCategory;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_expense_add,
				container, false);
		
		initControls(rootView);
		// get the default category
		String defaultCat = (String) getActivity().getIntent().getSerializableExtra(ExpenseFragment.INTENT_EXTRA_ADD_EXPENSE);
		txtCategory.setText(txtCategory.getText() + ": " + defaultCat);
		
		// not save file and return to Main Screen
		btnCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getActivity().finish();
			}
		});
		
		/**
		 * Save date report to XML file
		 * @author ngapham
		 * @date 20/7/2015
		 */
		btnSave.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (String.valueOf(edtAmount.getText()) == null) {
					Toast.makeText(getActivity(), "Please input amount", Toast.LENGTH_SHORT).show();
				} else {
					ExpenseDateReport newDateReport = new ExpenseDateReport();
					newDateReport.setAmount(Float.valueOf(String.valueOf(edtAmount.getText())));
					newDateReport.setDate(new java.util.Date(System.currentTimeMillis()));
					newDateReport.setCategoryID(1);
					newDateReport.setDescription(String.valueOf(edtDescription.getText()));
					String xmlFilePath = ExpenseFragment.DATA_PATH + "expense_date.xml";
					XMLParser mParser = new XMLParser();
					try {
						mParser.saveExpDateReport(xmlFilePath, newDateReport);
					} catch (ParserConfigurationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SAXException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (TransformerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		
		return rootView;
	}

	private void initControls(View v) {
		edtAmount = (EditText) v.findViewById(R.id.edt_espense_amount);
//		dtDate = v.findViewById(R.id.tx);
		edtDescription = (EditText) v.findViewById(R.id.edt_espense_desc);
		btnSave = (Button) v.findViewById(R.id.btn_save);
		btnCancel = (Button) v.findViewById(R.id.btn_cancel);
		txtCategory = (TextView) v.findViewById(R.id.txt_default_category);
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
