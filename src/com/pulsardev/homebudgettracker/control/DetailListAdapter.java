package com.pulsardev.homebudgettracker.control;

import java.util.ArrayList;

import com.pulsardev.homebudgettracker.R;
import com.pulsardev.homebudgettracker.model.DateReport;
import com.pulsardev.homebudgettracker.model.MonthlyReport;
import com.pulsardev.homebudgettracker.util.StaticString;

import android.app.Activity;
import android.text.format.DateFormat;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class DetailListAdapter extends BaseExpandableListAdapter {

	private final ArrayList<MonthlyReport> groups;
	public LayoutInflater inflater;
	public Activity activity;

	/**
	 * @param groups
	 * @param activity
	 */
	public DetailListAdapter(ArrayList<MonthlyReport> groups, Activity activity) {
		this.groups = groups;
		this.activity = activity;
		this.inflater = activity.getLayoutInflater();
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return groups.get(groupPosition).getListDateReport().get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.listrow_item, null);
		}
		
		DateReport child = (DateReport) getChild(groupPosition, childPosition);
//		TextView txtDetailDate = null, txtDetailDesc = null, txtDetailAmount = null;
		TextView txtDetailDate = (TextView) convertView.findViewById(R.id.txt_detail_date);
		TextView txtDetailDesc = (TextView) convertView.findViewById(R.id.txt_detail_desc);
		TextView txtDetailAmount = (TextView) convertView.findViewById(R.id.txt_detail_amount);
		
		String dateFormat = String.valueOf(DateFormat
				.format(StaticString.DATE_FORMAT,
						child.getDate()));
		txtDetailDate.setText(dateFormat);
		txtDetailDesc.setText(child.getDescription());
		txtDetailAmount.setText(String.valueOf(child.getAmount()) + " $");
		
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return groups.get(groupPosition).getListDateReport().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return groups.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.listrow_group, null);
		}
		
		MonthlyReport month = (MonthlyReport) getGroup(groupPosition);
		TextView txtMonth = (TextView) convertView.findViewById(R.id.txt_month);
		TextView txtMonthlyAmount = (TextView) convertView.findViewById(R.id.txt_monthly_amount);
		txtMonth.setText(month.getMonth());
		txtMonthlyAmount.setText(String.valueOf(month.getMonthlyAmount()) +" $");
		
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	public void deleteChild(int groupPosition, DateReport deletedDateReport) {
		groups.get(groupPosition).getListDateReport().remove(deletedDateReport);
	}
}
