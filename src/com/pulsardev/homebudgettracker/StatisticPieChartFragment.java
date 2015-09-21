package com.pulsardev.homebudgettracker;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

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
import android.widget.Toast;

public class StatisticPieChartFragment extends Fragment {
	// menu
	TextView txtTitle;
	ImageButton btnMenu;

	/** The main dataset that includes all the series that go into a chart. */
	private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
	/** Colors to be used for the pie slices. */
	private static int[] COLORS = new int[] { Color.GREEN, Color.BLUE, Color.MAGENTA, Color.CYAN , Color.RED,Color.YELLOW };
	/** The main series that will include all the data. */
	private CategorySeries mSeries = new CategorySeries("");
	/** The main renderer for the main dataset. */
	private DefaultRenderer mRenderer = new DefaultRenderer();
	/** The chart view that displays the data. */
	private GraphicalView mChartView;

	LinearLayout linearLayout;
	public HashMap<String, Double> data = new HashMap<String, Double>();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_statistic_piechart,
				container, false);

		initControls(rootView);
		setNavMenu();
		setTitleName();
		createChart();

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
		txtTitle.setText(res.getString(R.string.txt_distribution_header));
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		if (savedInstanceState != null) {
			// Restore last state for checked position.
			mSeries = (CategorySeries) savedInstanceState.getSerializable("current_series");
			mRenderer = (DefaultRenderer) savedInstanceState.getSerializable("current_renderer");
		}

		if (mChartView == null) {
			mChartView = ChartFactory.getPieChartView(getActivity(), mSeries, mRenderer);

			linearLayout.addView(mChartView, new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT));
		} else {
			mChartView.repaint();
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putSerializable("current_series", mSeries);
		outState.putSerializable("current_renderer", mRenderer);
	}

	private void initControls(View v) {
		txtTitle = (TextView) v.findViewById(R.id.txt_header);
		btnMenu = (ImageButton) v.findViewById(R.id.btnImg_Menu);
		
		linearLayout = (LinearLayout) v.findViewById(R.id.piechart);
		data = (HashMap<String, Double>) getArguments().getSerializable(MainActivity.PIE_EXPENSE_DATA);
	}

	private void createChart() {
		mRenderer.setZoomButtonsVisible(false);
		mRenderer.setPanEnabled(false);
		mRenderer.setZoomEnabled(false);
		mRenderer.setStartAngle(180);
		mRenderer.setDisplayValues(true);
		mRenderer.setChartTitleTextSize(80);
		mRenderer.setLabelsTextSize(50);
		mRenderer.setLegendTextSize(50);
		mRenderer.setMargins(new int[] { 40, 60, 30, 10 });

		// add a new data point to the current series

		Iterator<Map.Entry<String, Double>> iterator = data.entrySet().iterator() ;
		int i = 0;
		while (iterator.hasNext()) {
			Map.Entry pair = (Map.Entry)iterator.next();
			SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
			mSeries.add((String) pair.getKey(), ((Double)pair.getValue()).doubleValue());
			renderer.setColor(COLORS[i]);
			mRenderer.addSeriesRenderer(renderer);
			i++;
			iterator.remove(); // avoids a ConcurrentModificationException
		}

	}
}
