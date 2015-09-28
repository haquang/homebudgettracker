/**
 * Manage list of Expense Categories
 * This is the singleton
 * @author ngapham
 * @date 19/7/2015
 * @modified 26/7/2015 - Setting up the ArrayList of ExpenseCategory
 */
package com.pulsardev.homebudgettracker.model;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;

import com.pulsardev.homebudgettracker.control.JSONSerializer;

import android.content.Context;
import android.util.Log;

public class ExpenseCategoryLab {
	
	private ArrayList<Category> mListExpCategories;
	private static final int NUMBER_CATEGORIES = 6;
	private static final String CATEGORY_HOUSING = "Housing";
	private static final String CATEGORY_FOOD = "Food";
	private static final String CATEGORY_TRANSPORTATION = "Transportation";
	private static final String CATEGORY_MEDICAL = "Medical";
	private static final String CATEGORY_ENTERTAINMENT = "Entertainment";
	private static final String CATEGORY_OTHER = "Others";
	
	private JSONSerializer mSerializer;
	// file name in database, which stores List of Expense Categories
	private static final String JSON_FILENAME = "expense_categories.json";
	
	// To create singleton
	private static ExpenseCategoryLab mExpCatLab;
	private Context mAppContext;
	
	// TAG
	private static final String TAG = "ExpenseCategoryLab";
	
	/**
	 * Constructor
	 * @param appContext
	 */
	public ExpenseCategoryLab(Context appContext) {
		mAppContext = appContext;
		// Create Serializer to load ExpCategory List from JSON file
		mSerializer = new JSONSerializer(mAppContext, JSON_FILENAME);
		try {
			mListExpCategories = mSerializer.loadListCategories();
		} catch (Exception e) {
			// initial list of Expense Categories when loading app for the first time
			// There are always 6 categories
			mListExpCategories = new ArrayList<Category>();
			for (int i = 0; i < NUMBER_CATEGORIES; i++) {
				Category item = new Category();
				item.setId(i);
				mListExpCategories.add(item);
			}
			mListExpCategories.get(0).setName(CATEGORY_FOOD);
			mListExpCategories.get(1).setName(CATEGORY_TRANSPORTATION);
			mListExpCategories.get(2).setName(CATEGORY_HOUSING);
			mListExpCategories.get(3).setName(CATEGORY_MEDICAL);
			mListExpCategories.get(4).setName(CATEGORY_ENTERTAINMENT);
			mListExpCategories.get(5).setName(CATEGORY_OTHER);
			
			Log.e(TAG, "Error Loading new list of Expense Categories", e);
		}
		
	}
	/**
	 * To create singleton
	 * @param appContext
	 * @return singleton of ExpenseCategoryLab
	 */
	public static ExpenseCategoryLab get(Context appContext) {
		if (mExpCatLab == null) {
			mExpCatLab = new ExpenseCategoryLab(appContext.getApplicationContext());
		}
		return mExpCatLab;
	}
	
	public ArrayList<Category> getListExpCategories() {
		return mListExpCategories;
	}
	
	/**
	 * Get the specific Expense Category from list by id
	 * @param id
	 * @return the specific Expense Category
	 */
	public Category getExpCategory(int id) {
		for (Category item : mListExpCategories) {
			if (item.getId() == id) {
				return item;
			}
		}
		return null;
	}
	
	public void addNewCatAmount(int catId, double addedAmount) {
		for (Category item : mListExpCategories) {
			if (item.getId() == catId) {
				item.setAmount(item.getAmount() + addedAmount);
			}
		}
	}
	
	public void updateCatAmount(int catId, double oldAmount, double newAmount) {
		for (Category item : mListExpCategories) {
			if (item.getId() == catId) {
				item.setAmount(item.getAmount() - oldAmount + newAmount);
			}
		}
	}
	
	public boolean saveListCat() {
		try {
			mSerializer.saveListCategories(mListExpCategories);
			Log.i(TAG, "List saved to file.");
			return true;
		} catch (JSONException e) {
			Log.e(TAG, "Error saving list: " + e);
			return false;
		} catch (IOException e) {
			Log.e(TAG, "Error saving list: " + e);
			return false;
		}
	}
}
