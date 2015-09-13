/**
 * List Date Report grouped by month
 * @author ngapham
 * Date: 12/9/2015
 */

package com.pulsardev.homebudgettracker.model;

import java.util.ArrayList;
import java.util.List;

public class DetailGroup {
	private String month;
	private double monthlyAmount;
	// List of Date Report in specific month
	private ArrayList<ExpenseDateReport> listDateReport = new ArrayList<ExpenseDateReport>();

	/**
	 * Constructor with fields
	 * @param month
	 * @param monthlyAmount
	 * @param list
	 */
	public DetailGroup(String month, double monthlyAmount,
			ArrayList<ExpenseDateReport> list) {
		this.month = month;
		this.monthlyAmount = monthlyAmount;
		this.listDateReport = list;
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

	public ArrayList<ExpenseDateReport> getListDateReport() {
		return listDateReport;
	}

	public void setListDateReport(ArrayList<ExpenseDateReport> listDateReport) {
		this.listDateReport = listDateReport;
	}
}
