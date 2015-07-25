/**
 * Manage list of Expense Categories
 * @author ngapham
 * @date 19/7/2015
 */
package com.pulsardev.homebudgettracker.model;

import android.content.Context;

public class ExpenseCategoryLab {
	
	private static ExpenseCategoryLab mExpCatList;
	private Context mAppContext;
	
	/**
	 * @param appContext
	 */
	public ExpenseCategoryLab(Context appContext) {
		mAppContext = appContext;
	}
	
	public static ExpenseCategoryLab get(Context appContext) {
		if (mExpCatList == null) {
			mExpCatList = new ExpenseCategoryLab(appContext);
		}
		return mExpCatList;
	}
}
