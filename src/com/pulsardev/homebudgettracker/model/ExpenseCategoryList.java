/**
 * Manage list of Expense Categories
 * @author ngapham
 * @date 19/7/2015
 */
package com.pulsardev.homebudgettracker.model;

import android.content.Context;

public class ExpenseCategoryList {
	
	private static ExpenseCategoryList mExpCatList;
	private Context mAppContext;
	
	/**
	 * @param appContext
	 */
	public ExpenseCategoryList(Context appContext) {
		mAppContext = appContext;
	}
	
	public static ExpenseCategoryList get(Context appContext) {
		if (mExpCatList == null) {
			mExpCatList = new ExpenseCategoryList(appContext);
		}
		return mExpCatList;
	}
}
