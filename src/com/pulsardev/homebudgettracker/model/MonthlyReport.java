/**
 * List Date Report grouped by month
 * @author ngapham
 * Date: 12/9/2015
 */

package com.pulsardev.homebudgettracker.model;

import java.util.ArrayList;

public class MonthlyReport {
	private String month;
	private double monthlyAmount;
	// List of Date Report in specific month
	private ArrayList<DateReport> listDateReport = new ArrayList<DateReport>();

	/**
	 * Constructor with fields
	 * @param month
	 * @param monthlyAmount
	 * @param arrayList
	 */
	public MonthlyReport(String month, double monthlyAmount,
			ArrayList<DateReport> arrayList) {
		this.month = month;
		this.monthlyAmount = monthlyAmount;
		this.listDateReport = arrayList;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public double getMonthlyAmount() {
		return monthlyAmount;
	}

	public void setMonthlyAmount(double monthlyAmount) {
		this.monthlyAmount = monthlyAmount;
	}

	public ArrayList<DateReport> getListDateReport() {
		return listDateReport;
	}

	public void setListDateReport(ArrayList<DateReport> listDateReport) {
		this.listDateReport = listDateReport;
	}
}
