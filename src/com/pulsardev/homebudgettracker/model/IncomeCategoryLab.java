package com.pulsardev.homebudgettracker.model;

import java.util.ArrayList;

import android.content.Context;

public class IncomeCategoryLab {
	/**
	 * Properties
	 */
	private ArrayList<IncomeCategory> mListInCategories;
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
		mListInCategories = new ArrayList<IncomeCategory>();
		for (int i = 0; i < NUMBER_CATEGORIES; i++) {
			IncomeCategory item = new IncomeCategory();
			item.setID(i);
			mListInCategories.add(item);
		}
		mListInCategories.get(0).setName(MONTHLY_INCOME);
		mListInCategories.get(1).setName(OTHER_INCOME);
	}
	
	/**
	 * To create singleton
	 * @param appContext
	 * @return singleton of IncomeCategoryLab
	 */
	public static IncomeCategoryLab get(Context appContext) {
		if (mInCatLab == null) {
			mInCatLab = new IncomeCategoryLab(appContext.getApplicationContext());
		}
		return mInCatLab;
	}

	public ArrayList<IncomeCategory> getListInCategories() {
		return mListInCategories;
	}
	
	/**
	 * Get the specific Income Category from list by id
	 * @param id
	 * @return the specific Income Category
	 */
	public IncomeCategory getInCategory(int id) {
		for (IncomeCategory item : mListInCategories) {
			if (item.getID() == id) {
				return item;
			}
		}
		return null;
	}
}
