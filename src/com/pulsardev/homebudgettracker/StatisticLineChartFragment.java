package com.pulsardev.homebudgettracker;

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

import com.pulsardev.homebudgettracker.util.StaticString;

import android.app.Fragment;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StatisticLineChartFragment extends Fragment {

	// menu
	TextView txtTitle;
	ImageButton btnMenu;

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

	LinearLayout linearLayout;
	public HashMap<Date, Double> expenseData = new HashMap<Date, Double>();
	public HashMap<Date, Double> incomeData = new HashMap<Date, Double>();

	// Static String for savedInstanceState
	private static final String MULTI_SERIES_DATASET = "dataset";
	private static final String MULTI_SERIES_RENDERER = "renderer";
	private static final String EXPENSE_SERIES = "expense_series";
	private static final String INCOME_SERIES = "income_series";
	private static final String EXPENSE_RENDERER = "expense_renderer";
	private static final String INCOME_RENDERER = "income_renderer";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_statistic_linechart,
				container, false);

		initControls(rootView);
		setNavMenu();
		setTitleName();

		initChart();
		initDataChart();

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

		createChart();
	}

	private void initControls(View v) {
		txtTitle = (TextView) v.findViewById(R.id.txt_header);
		btnMenu = (ImageButton) v.findViewById(R.id.btnImg_Menu);

		linearLayout = (LinearLayout) v.findViewById(R.id.linechart);
	}

	private void initDataChart() {
		/**
		 * Add Expense Data
		 */
		expenseData = (HashMap<Date, Double>) getArguments().getSerializable(
				NavDrawerActivity.LINE_EXPENSE_DATA);

		// add a new data point to the current series
		Iterator<Map.Entry<Date, Double>> expIterator = expenseData
				.entrySet().iterator();
		while (expIterator.hasNext()) {
			Map.Entry pair = (Map.Entry) expIterator.next();
			mExpenseSeries.add((Date) pair.getKey(),
					((Double) pair.getValue()).doubleValue());
			expIterator.remove(); // avoids a ConcurrentModificationException
		}

		/**
		 * Add Income Data
		 */
		incomeData = (HashMap<Date, Double>) getArguments().getSerializable(
				NavDrawerActivity.LINE_INCOME_DATA);
		// add a new data point to the current series
		Iterator<Map.Entry<Date, Double>> inIterator = incomeData.entrySet()
				.iterator();
		while (inIterator.hasNext()) {
			Map.Entry pair = (Map.Entry) inIterator.next();
			mIncomeSeries.add((Date) pair.getKey(),
					((Double) pair.getValue()).doubleValue());
			inIterator.remove(); // avoids a ConcurrentModificationException
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(MULTI_SERIES_DATASET, mDataset);
		outState.putSerializable(MULTI_SERIES_RENDERER, mRenderer);
		outState.putSerializable(EXPENSE_SERIES, mExpenseSeries);
		outState.putSerializable(INCOME_SERIES, mIncomeSeries);
		outState.putSerializable(EXPENSE_RENDERER, mExpenseRenderer);
		outState.putSerializable(INCOME_RENDERER, mIncomeRenderer);
	}

	@Override
	public void onResume() {
		super.onResume();

		createChart();
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
				.getDimensionPixelSize(R.dimen.tex_size_xxxlarge));
		
		mRenderer.setChartTitleTextSize(mResources
				.getDimensionPixelSize(R.dimen.tex_size_large));
		// X, Y values
		mRenderer.setLabelsTextSize(mResources
				.getDimensionPixelSize(R.dimen.tex_size_large));
		// line titles
		mRenderer.setLegendTextSize(mResources
				.getDimensionPixelSize(R.dimen.tex_size_largest));
		
		// Set title name
		mRenderer.setXTitle("Date");
		mRenderer.setYTitle("Amount");

		mRenderer.setMargins(new int[] { 40, 40, 30, 10 });

		mRenderer.setZoomButtonsVisible(true);
		mRenderer.setPointSize(5);
		mRenderer.setPanEnabled(true, false);
		mRenderer.setZoomEnabled(false, false);

		// enable the chart click events
		mRenderer.setClickEnabled(true);
		mRenderer.setSelectableBuffer(10);

		// create a new renderer for the new series
		mExpenseRenderer = new XYSeriesRenderer();
		mIncomeRenderer = new XYSeriesRenderer();

		// set some renderer properties
		mExpenseRenderer.setPointStyle(PointStyle.CIRCLE);
		mExpenseRenderer.setFillPoints(true);
		mExpenseRenderer.setDisplayChartValues(true);
		mExpenseRenderer.setDisplayChartValuesDistance(10);
		mExpenseRenderer.setColor(mResources.getColor(R.color.red));
		
		mIncomeRenderer.setPointStyle(PointStyle.POINT);
		mIncomeRenderer.setFillPoints(true);
		mIncomeRenderer.setDisplayChartValues(true);
		mIncomeRenderer.setDisplayChartValuesDistance(10);
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

	private void createChart() {
		if (mChartView == null) {
			// Create a line chart
			mChartView = ChartFactory.getTimeChartView(this.getActivity(),
					mDataset, mRenderer, StaticString.DATE_FORMAT);

			// Add chart to layout
			linearLayout.addView(mChartView, new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		} else {
			mChartView.repaint();
		}
	}
}
