/**
 * Manage list of Expense Categories
 * This is the singleton
 * @author ngapham
 * Date: 6/9/2015
 */

package com.pulsardev.homebudgettracker.model;

import java.util.ArrayList;

import android.content.Context;

public class IncomeCategoryLab {
	/**
	 * Properties
	 */
	private ArrayList<Category> mListInCategories;
	private static final int NUMBER_CATEGORIES = 2;
	private static final String MONTHLY_INCOME = "Monthly Income";
	private static final String OTHER_INCOME = "Other Income";
	
	// To create singleton
	private static IncomeCategoryLab mInCatLab;
	private Context mAppContext;
	
	/**
	 * Constructor
	 * @param appContext
	 */
	public IncomeCategoryLab(Context appContext) {
		mAppContext = appContext;
		// initial list of Income Categories when loading app for the first time
		// There are always 2 categories
		mListInCategories = new ArrayList<Category>();
		for (int i = 0; i < NUMBER_CATEGORIES; i++) {
			Category item = new Category();
			item.setID(i);
			mListInCategories.add(item);
		}
		mListInCategories.get(0).setName(MONTHLY_INCOME);
		mListInCategories.get(1).setName(OTHER_INCOME);
	}
	
	/**
	 * To create singleton
	 * @param appContext
	 * @return singleton of CategoryLab
	 */
	public static IncomeCategoryLab get(Context appContext) {
		if (mInCatLab == null) {
			mInCatLab = new IncomeCategoryLab(appContext.getApplicationContext());
		}
		return mInCatLab;
	}

	public ArrayList<Category> getListInCategories() {
		return mListInCategories;
	}
	
	/**
	 * Get the specific Income Category from list by id
	 * @param id
	 * @return the specific Income Category
	 */
	public Category getInCategory(int id) {
		for (Category item : mListInCategories) {
			if (item.getID() == id) {
				return item;
			}
		}
		return null;
	}
}
