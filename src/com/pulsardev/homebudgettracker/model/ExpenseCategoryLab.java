/**
 * Manage list of Expense Categories
 * This is the singleton
 * @author ngapham
 * @date 19/7/2015
 * @modified 26/7/2015 - Setting up the ArrayList of ExpenseCategory
 */
package com.pulsardev.homebudgettracker.model;

import java.util.ArrayList;

import android.content.Context;

public class ExpenseCategoryLab {
	
	private ArrayList<ExpenseCategory> mListExpCategories;
	private static final int NUMBER_CATEGORIES = 6;
	private static final String CATEGORY_HOUSING = "House";
	private static final String CATEGORY_FOOD = "Food";
	private static final String CATEGORY_TRANSPORTATION = "Transportation";
	private static final String CATEGORY_MEDICAL = "Medical";
	private static final String CATEGORY_ENTERTAINMENT = "Leisure";
	private static final String CATEGORY_OTHER = "Other";
	
	// To create singleton
	private static ExpenseCategoryLab mExpCatLab;
	private Context mAppContext;
	
	/**
	 * @param appContext
	 */
	public ExpenseCategoryLab(Context appContext) {
		mAppContext = appContext;
		// initial list of Expense Categories when loading app for the first time
		// There are always 6 categories
		mListExpCategories = new ArrayList<ExpenseCategory>();
		for (int i = 0; i < NUMBER_CATEGORIES; i++) {
			ExpenseCategory item = new ExpenseCategory();
			item.setID(i);
			mListExpCategories.add(item);
		}
		mListExpCategories.get(0).setName(CATEGORY_FOOD);
		mListExpCategories.get(1).setName(CATEGORY_TRANSPORTATION);
		mListExpCategories.get(2).setName(CATEGORY_ENTERTAINMENT);
		mListExpCategories.get(3).setName(CATEGORY_MEDICAL);
		mListExpCategories.get(4).setName(CATEGORY_HOUSING);
		mListExpCategories.get(5).setName(CATEGORY_OTHER);
	}
	/**
	 * To create singleton
	 * @param c
	 * @return singleton of ExpenseCategoryLab
	 */
	public static ExpenseCategoryLab get(Context c) {
		if (mExpCatLab == null) {
			mExpCatLab = new ExpenseCategoryLab(c.getApplicationContext());
		}
		return mExpCatLab;
	}
	
	public ArrayList<ExpenseCategory> getListExpCategories() {
		return mListExpCategories;
	}
	
	/**
	 * Get the specific Expense Category from list by id
	 * @param id
	 * @return the specific Expense Category
	 */
	public ExpenseCategory getExpCategory(int id) {
		for (ExpenseCategory item : mListExpCategories) {
			if (item.getID() == id) {
				return item;
			}
		}
		return null;
	}
}
