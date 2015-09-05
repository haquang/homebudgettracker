package com.pulsardev.homebudgettracker;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.Toast;

public class StatisticLineChartFragment extends Fragment {

	/** The main dataset that includes all the series that go into a chart. */
	private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
	/** The main renderer that includes all the renderers customizing a chart. */
	private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
	/** The most recently added series. */
	private XYSeries mCurrentSeries;
	/** The most recently created renderer, customizing the current series. */
	private XYSeriesRenderer mCurrentRenderer;
	/** The chart view that displays the data. */
	private GraphicalView mChartView;

	LinearLayout linearLayout;
	public HashMap<Double, Double> data = new HashMap<Double, Double>();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_statistic_linechart,
				container, false);

		initControls(rootView);
		createChart();

		return rootView;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		if (savedInstanceState != null) {
			// Restore last state for checked position.
			mDataset = (XYMultipleSeriesDataset) savedInstanceState.getSerializable("dataset");
			mRenderer = (XYMultipleSeriesRenderer) savedInstanceState.getSerializable("renderer");
			mCurrentSeries = (XYSeries) savedInstanceState.getSerializable("current_series");
			mCurrentRenderer = (XYSeriesRenderer) savedInstanceState.getSerializable("current_renderer");
		}

		if (mChartView == null) {

			mChartView = ChartFactory.getLineChartView(this.getActivity(), mDataset, mRenderer);
			// enable the chart click events
			mRenderer.setClickEnabled(true);
			mRenderer.setSelectableBuffer(10);

			linearLayout.addView(mChartView, new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT));
		} else {
			mChartView.repaint();
		}
	}

	private void initControls(View v) {
		linearLayout = (LinearLayout) v.findViewById(R.id.linechart);
//		data = (HashMap<Double, Double>) getActivity().getIntent().getSerializableExtra(ExpenseFragment.INTENT_EXTRA_DATA_LINE);
		data = (HashMap<Double, Double>) getArguments().getSerializable(MainActivity.INTENT_EXTRA_DATA_LINE);

	}
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putSerializable("dataset", mDataset);
		outState.putSerializable("renderer", mRenderer);
		outState.putSerializable("current_series", mCurrentSeries);
		outState.putSerializable("current_renderer", mCurrentRenderer);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (mChartView == null) {

			mChartView = ChartFactory.getLineChartView(getActivity(), mDataset, mRenderer);
			// enable the chart click events
			mRenderer.setClickEnabled(true);
			mRenderer.setSelectableBuffer(10);

			linearLayout.addView(mChartView, new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT));
		} else {
			mChartView.repaint();
		}
	}
	private void createChart(){
		// set some properties on the main renderer
		mRenderer.setApplyBackgroundColor(true);
		mRenderer.setBackgroundColor(Color.argb(100, 50, 50, 50));

		mRenderer.setAxisTitleTextSize(80);
		mRenderer.setChartTitleTextSize(80);
		mRenderer.setLabelsTextSize(50);
		mRenderer.setLegendTextSize(50);
		mRenderer.setMargins(new int[] { 40, 60, 30, 10 });
		mRenderer.setZoomButtonsVisible(true);
		mRenderer.setPointSize(5);
		mRenderer.setPanEnabled(true,false);
		mRenderer.setZoomEnabled(false,false);
		String seriesTitle = "";
		// create a new series of data
		XYSeries series = new XYSeries(seriesTitle);
		mDataset.addSeries(series);
		mCurrentSeries = series;
		// create a new renderer for the new series
		XYSeriesRenderer renderer = new XYSeriesRenderer();
		mRenderer.addSeriesRenderer(renderer);
		// set some renderer properties
		renderer.setPointStyle(PointStyle.CIRCLE);
		renderer.setFillPoints(true);
		renderer.setDisplayChartValues(true);
		renderer.setDisplayChartValuesDistance(10);
		mCurrentRenderer = renderer;
		mCurrentRenderer.setColor(Color.RED);

		// add a new data point to the current series

		Iterator<Map.Entry<Double, Double>> iterator = data.entrySet().iterator() ;
		while (iterator.hasNext()) {
			Map.Entry pair = (Map.Entry)iterator.next();
			mCurrentSeries.add(((Double)pair.getKey()).doubleValue(),((Double)pair.getValue()).doubleValue());
			iterator.remove(); // avoids a ConcurrentModificationException
		}
	}

}
