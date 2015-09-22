package com.pulsardev.homebudgettracker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.pulsardev.homebudgettracker.model.Category;
import com.pulsardev.homebudgettracker.model.DateReport;
import com.pulsardev.homebudgettracker.model.ExpenseDateReportLab;
import com.pulsardev.homebudgettracker.model.IncomeDateReportLab;
import com.pulsardev.homebudgettracker.model.MonthlyReport;
import com.pulsardev.homebudgettracker.util.StaticString;

import android.app.Fragment;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class StatisticLineChartFragment extends Fragment {

	// menu
	TextView txtTitle;
	ImageButton btnMenu;

	/** spinner */
	TextView txtFilter;
	Spinner spFilter;

	/** The main dataset that includes all the series that go into a chart. */
	private XYMultipleSeriesDataset mDataset;
	/** The main renderer that includes all the renderers customizing a chart. */
	private XYMultipleSeriesRenderer mRenderer;
	/** The most recently added series (Expense data). */
	private TimeSeries mExpenseSeries;
	/** The most recently added series (Income data). */
	private TimeSeries mIncomeSeries;
	/** The most recently created renderer (Expense */
	private XYSeriesRenderer mExpenseRenderer, mIncomeRenderer;
	/** The chart view that displays the data. */
	private GraphicalView mChartView;
	/** Layout that contains the chart */
	LinearLayout linearLayout;

	/** data */
	// List of Expense and Income Date Reports
	private ArrayList<DateReport> listExpDateReports, listInDateReports;
	private HashMap<Date, Double> expenseDataByDate;
	private HashMap<Date, Double> incomeDataByDate;
	private HashMap<String, Double> expenseDataByMonth;
	private HashMap<String, Double> incomeDataByMonth;

	// Static String for savedInstanceState
	private static final String MULTI_SERIES_DATASET = "dataset";
	private static final String MULTI_SERIES_RENDERER = "renderer";
	private static final String EXPENSE_SERIES = "expense_series";
	private static final String INCOME_SERIES = "income_series";
	private static final String EXPENSE_RENDERER = "expense_renderer";
	private static final String INCOME_RENDERER = "income_renderer";
	
	// Number of report display in chart
	private static final int NUMBER = 10;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		expenseDataByDate = new HashMap<Date, Double>();
		incomeDataByDate = new HashMap<Date, Double>();
		expenseDataByMonth = new HashMap<String, Double>();
		incomeDataByMonth = new HashMap<String, Double>();

		listExpDateReports = ExpenseDateReportLab.get(
				getActivity().getApplicationContext()).getListExpDateReport();
		listInDateReports = IncomeDateReportLab.get(
				getActivity().getApplicationContext()).getListInDateReport();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_statistic_linechart,
				container, false);

		initControls(rootView);
		setNavMenu();
		setTitleName();
		setSpinner();

		initChart();
		passDataByDate(NUMBER);
		initDataChart();

		handleSpinnerChanged();

		return rootView;
	}

	private void setNavMenu() {
		btnMenu.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				((NavDrawerActivity) getActivity()).openDrawer();
			}
		});
	}

	private void setTitleName() {
		Resources res = getActivity().getResources();
		txtTitle.setText(res.getString(R.string.txt_report_header));
	}

	private void setSpinner() {
		// Spinner initialization
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				getActivity(), R.array.spinner_line_filter,
				R.layout.spinner_item);

		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// Apply the adapter to the spinner
		spFilter.setAdapter(adapter);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		if (savedInstanceState != null) {
			// Restore last state for checked position.
			mDataset = (XYMultipleSeriesDataset) savedInstanceState
					.getSerializable(MULTI_SERIES_DATASET);
			mRenderer = (XYMultipleSeriesRenderer) savedInstanceState
					.getSerializable(MULTI_SERIES_RENDERER);
			mExpenseSeries = (TimeSeries) savedInstanceState
					.getSerializable(EXPENSE_SERIES);
			mIncomeSeries = (TimeSeries) savedInstanceState
					.getSerializable(INCOME_SERIES);
			mExpenseRenderer = (XYSeriesRenderer) savedInstanceState
					.getSerializable(EXPENSE_RENDERER);
			mIncomeRenderer = (XYSeriesRenderer) savedInstanceState
					.getSerializable(INCOME_RENDERER);
		}

		createChart(StaticString.DATE_FORMAT);
	}

	private void initControls(View v) {
		txtTitle = (TextView) v.findViewById(R.id.txt_header);
		btnMenu = (ImageButton) v.findViewById(R.id.btnImg_Menu);

		txtFilter = (TextView) v.findViewById(R.id.txt_line_filter);
		spFilter = (Spinner) v.findViewById(R.id.spinner_line_filter);

		linearLayout = (LinearLayout) v.findViewById(R.id.linechart);
	}

	private void passDataByDate(int number) {
		passExpenseDataByDate(number); 
		passIncomeDataByDate(number);
		// Test passing Data
		Iterator<Map.Entry<Date, Double>> expIterator = expenseDataByDate.entrySet()
				.iterator();
		while (expIterator.hasNext()) {
			Map.Entry pair = (Map.Entry) expIterator.next();
			SimpleDateFormat mFormat = new SimpleDateFormat(StaticString.DATE_FORMAT);
			Log.i("By date", "key=" + mFormat.format((Date) pair.getKey()) + ", value="
					+ String.valueOf((Double) pair.getValue()));
			expIterator.remove(); // avoids a ConcurrentModificationException
		}
	}
	
	/**
	 * Add Expense data
	 * @param number
	 */
	private void passExpenseDataByDate(int number) {
		Date[] dt = new Date[number];
		int exp_size = listExpDateReports.size();
		
		if (exp_size >= number) {
			int j = 0;
			for (int i = exp_size - 1; i > exp_size - number - 1; i--) {
				dt[j] = listExpDateReports.get(i).getDate();
				expenseDataByDate.put(dt[j], listExpDateReports.get(i).getAmount());
				j++;
			}
		} else {	// If there are not enough reports to display
			int j = 0;
			for (int i = exp_size - 1; i >= 0; i--) {
				dt[j] = listExpDateReports.get(i).getDate();
				expenseDataByDate.put(dt[j], listExpDateReports.get(i).getAmount());
				j++;
			}
		}
	}
	
	/**
	 * Add Income data
	 * @param number
	 */
	private void passIncomeDataByDate(int number) {
		Date[] dt = new Date[number];
		int in_size = listInDateReports.size();
		
		if (in_size >= number) {
			int j = 0;
			for (int i = in_size - 1; i < in_size - number - 1; i--) {
				dt[j] = listInDateReports.get(i).getDate();
				incomeDataByDate.put(dt[j], listInDateReports.get(i).getAmount());
				j++;
			}
		} else {	// If there are not enough reports to display
			int j = 0;
			for (int i = in_size - 1; i >= 0; i--) {
				dt[j] = listInDateReports.get(i).getDate();
				incomeDataByDate.put(dt[j], listInDateReports.get(i).getAmount());
				j++;
			}
		}
	}
	
	private void passDataByMonth(int number) {
		passExpenseDataByMonth(number);
//		passIncomeDataByMonth(number);

		// Test passing Data
		Iterator<Map.Entry<Date, Double>> expIterator = expenseDataByDate.entrySet()
				.iterator();
		while (expIterator.hasNext()) {
			Map.Entry pair = (Map.Entry) expIterator.next();
			SimpleDateFormat mFormat = new SimpleDateFormat(StaticString.FULL_MONTH_FORMAT);
			Log.i("By month", "key=" + mFormat.format((Date) pair.getKey()) + ", value="
					+ String.valueOf((Double) pair.getValue()));
			expIterator.remove(); // avoids a ConcurrentModificationException
		}
	}

	private void passIncomeDataByMonth(int number) {
		String[] s = new String[number];
		ArrayList<MonthlyReport> listInMonReports = monthlyReport(listInDateReports);
		int in_size = listInMonReports.size();
		// Convert string to Date
		SimpleDateFormat mFormat = new SimpleDateFormat(StaticString.FULL_MONTH_FORMAT); 
		
		if (in_size >= number) {
			int j = 0;
			for (int i = in_size - 1; i < in_size - number - 1; i--) {
				s[j] = listInMonReports.get(i).getMonth();
				try {
					incomeDataByDate.put(mFormat.parse(s[j]), listInMonReports.get(i).getMonthlyAmount());
				} catch (ParseException e) {
					Log.e("Line chart", "ParseException");
				}
				j++;
			}
		} else {	// If there are not enough reports to display
			int j = 0;
			for (int i = in_size - 1; i >= 0; i--) {
				s[j] = listInMonReports.get(i).getMonth();
				try {
					incomeDataByDate.put(mFormat.parse(s[j]), listInMonReports.get(i).getMonthlyAmount());
				} catch (ParseException e) {
					Log.e("Line chart", "ParseException");
				}
				j++;
			}
		}
	}

	private void passExpenseDataByMonth(int number) {
		String[] s = new String[number];
		ArrayList<MonthlyReport> listExpMonReports = monthlyReport(listExpDateReports);
		int in_size = listExpMonReports.size();
		// Convert string to Date
		SimpleDateFormat mFormat = new SimpleDateFormat(StaticString.FULL_MONTH_FORMAT); 
		
		if (in_size >= number) {
			int j = 0;
			for (int i = in_size - 1; i < in_size - number - 1; i--) {
				s[j] = listExpMonReports.get(i).getMonth();
				try {
					expenseDataByDate.put(mFormat.parse(s[j]), listExpMonReports.get(i).getMonthlyAmount());
				} catch (ParseException e) {
					Log.e("Line chart", "ParseException");
				}
				j++;
			}
		} else {	// If there are not enough reports to display
			int j = 0;
			for (int i = in_size - 1; i >= 0; i--) {
				s[j] = listExpMonReports.get(i).getMonth();
				try {
					expenseDataByDate.put(mFormat.parse(s[j]), listExpMonReports.get(i).getMonthlyAmount());
				} catch (ParseException e) {
					Log.e("Line chart", "ParseException");
				}
				j++;
			}
		}
	}
	
	/**
	 * group Date Reports by month
	 * 
	 * @param listDateReport
	 * @return
	 */
	private ArrayList<MonthlyReport> monthlyReport(
			ArrayList<DateReport> listDateReport) {
		ArrayList<MonthlyReport> groups = new ArrayList<MonthlyReport>();
		SimpleDateFormat fullMonthFormat = new SimpleDateFormat(
				StaticString.FULL_MONTH_FORMAT);
		Map<String, ArrayList<DateReport>> map = new HashMap<String, ArrayList<DateReport>>();
		for (DateReport item : listDateReport) {
			String fullMonth = fullMonthFormat.format(item.getDate());
			if (map.get(fullMonth) == null) {
				map.put(fullMonth, new ArrayList<DateReport>());
			}
			map.get(fullMonth).add(item);
		}
		for (Map.Entry<String, ArrayList<DateReport>> entry : map.entrySet()) {
			MonthlyReport group = new MonthlyReport(entry.getKey(),
					totalAmount(entry.getValue()), entry.getValue());
			groups.add(group);
		}
		return groups;
	}
	
	private double totalAmount(ArrayList<DateReport> list) {
		double amount = 0.0;
		for (DateReport item : list) {
			amount += item.getAmount();
		}
		return amount;
	}

	private void initDataChart() {
		/**
		 * Add Expense Data
		 */
		mExpenseSeries.clear();
		// add a new data point to the current series
		Iterator<Map.Entry<Date, Double>> expIterator = expenseDataByDate.entrySet()
				.iterator();
		while (expIterator.hasNext()) {
			Map.Entry pair = (Map.Entry) expIterator.next();
			mExpenseSeries.add((Date) pair.getKey(),
					((Double) pair.getValue()).doubleValue());
			expIterator.remove(); // avoids a ConcurrentModificationException
		}

		/**
		 * Add Income Data
		 */
		mIncomeSeries.clear();
		// add a new data point to the current series
		Iterator<Map.Entry<Date, Double>> inIterator = incomeDataByDate.entrySet()
				.iterator();
		while (inIterator.hasNext()) {
			Map.Entry pair = (Map.Entry) inIterator.next();
			mIncomeSeries.add((Date) pair.getKey(),
					((Double) pair.getValue()).doubleValue());
			inIterator.remove(); // avoids a ConcurrentModificationException
		}
	}

	private void initChart() {
		Resources mResources = getResources();

		// Initialize new Multi Renderer to hold each renderer
		mRenderer = new XYMultipleSeriesRenderer();

		// set some properties on the main renderer
		mRenderer.setApplyBackgroundColor(true);
		mRenderer.setBackgroundColor(mResources.getColor(R.color.white));

		// X, Y axis titles
		mRenderer.setAxisTitleTextSize(mResources
				.getDimensionPixelSize(R.dimen.tex_size_large));

		mRenderer.setChartTitleTextSize(mResources
				.getDimensionPixelSize(R.dimen.tex_size_large));
		// X, Y values
		mRenderer.setLabelsTextSize(mResources
				.getDimensionPixelSize(R.dimen.tex_size_large));
		// line titles
		mRenderer.setLegendTextSize(mResources
				.getDimensionPixelSize(R.dimen.tex_size_xxxlarge));

		// Set title name
		mRenderer.setXTitle("Date");
		mRenderer.setYTitle("Amount");

		mRenderer.setMargins(new int[] { 40, 60, 30, 10 });

		mRenderer.setZoomButtonsVisible(true);
		mRenderer.setPointSize(5);
		// mRenderer.setPanEnabled(true, false);
		// mRenderer.setZoomEnabled(false, false);

		// enable the chart click events
		mRenderer.setClickEnabled(true);
		// Sets the selectable radius value around clickable points.
		mRenderer.setSelectableBuffer(20);

		// create a new renderer for the new series
		mExpenseRenderer = new XYSeriesRenderer();
		mIncomeRenderer = new XYSeriesRenderer();

		// set some renderer properties
		mExpenseRenderer.setPointStyle(PointStyle.CIRCLE);
		mExpenseRenderer.setFillPoints(true);
		mExpenseRenderer.setDisplayChartValues(false); // prevent ArrayList
		// IndexOutOfBoundsException
		mExpenseRenderer.setDisplayChartValuesDistance(5);
		mExpenseRenderer.setColor(mResources.getColor(R.color.red));

		mIncomeRenderer.setPointStyle(PointStyle.POINT);
		mIncomeRenderer.setFillPoints(true);
		mIncomeRenderer.setDisplayChartValues(false);
		mIncomeRenderer.setDisplayChartValuesDistance(5);
		mIncomeRenderer.setColor(mResources.getColor(R.color.blue));

		// Add this renderer to Multi Renderer
		mRenderer.addSeriesRenderer(mExpenseRenderer);
		mRenderer.addSeriesRenderer(mIncomeRenderer);

		// Initialize new Multi Series DataSet to hold each series
		mDataset = new XYMultipleSeriesDataset();

		// create a new series of data
		String seriesTitle = getResources().getString(
				R.string.txt_expense_header);
		mExpenseSeries = new TimeSeries(seriesTitle);
		seriesTitle = getResources().getString(R.string.txt_income_header);
		mIncomeSeries = new TimeSeries(seriesTitle);

		// Add this series to Multi Series DataSet
		mDataset.addSeries(mExpenseSeries);
		mDataset.addSeries(mIncomeSeries);
	}

	private void createChart(String dateFormat) {
		if (mChartView == null) {
			// Create a line chart
			mChartView = ChartFactory.getTimeChartView(this.getActivity(),
					mDataset, mRenderer, dateFormat);

			// Add chart to layout
			linearLayout.addView(mChartView, new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		} else {
			mChartView.repaint();
		}
	}

	private void handleSpinnerChanged() {
		spFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View arg1,
					int position, long arg3) {
				switch (position) {
				case 0:		// Filter by Date
					passDataByDate(NUMBER);
					initDataChart();
					createChart(StaticString.DATE_FORMAT);
					break;
				case 1:		// Filter by Month
					passDataByMonth(NUMBER);
					initDataChart();
					createChart(StaticString.FULL_MONTH_FORMAT);
					break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// No code here
			}
		});
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(MULTI_SERIES_DATASET, mDataset);
		outState.putSerializable(MULTI_SERIES_RENDERER, mRenderer);
		outState.putSerializable(EXPENSE_SERIES, mExpenseSeries);
		outState.putSerializable(INCOME_SERIES
				, mIncomeSeries);
		outState.putSerializable(EXPENSE_RENDERER, mExpenseRenderer);
		outState.putSerializable(INCOME_RENDERER, mIncomeRenderer);
	}

	@Override
	public void onResume() {
		super.onResume();

//		createChart();
	}
}
