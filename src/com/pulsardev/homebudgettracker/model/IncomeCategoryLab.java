/**
 * Manage list of Expense Categories
 * This is the singleton
 * @author ngapham
 * Date: 6/9/2015
 */

package com.pulsardev.homebudgettracker.model;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;

import com.pulsardev.homebudgettracker.control.JSONSerializer;

import android.content.Context;
import android.util.Log;

public class IncomeCategoryLab {
	/**
	 * Properties
	 */
	private ArrayList<Category> mListInCategories;
	private static final int NUMBER_CATEGORIES = 2;
	private static final String MONTHLY_INCOME = "Monthly Income";
	private static final String OTHER_INCOME = "Other Income";
	
	private JSONSerializer mSerializer;
	// file name in database, which stores List of Expense Categories
	private static final String JSON_FILENAME = "income_categories.json";
	
	// To create singleton
	private static IncomeCategoryLab mInCatLab;
	private Context mAppContext;
	
	// TAG
	private static final String TAG = "IncomeCategoryLab";
	
	/**
	 * Constructor
	 * @param appContext
	 */
	public IncomeCategoryLab(Context appContext) {
		mAppContext = appContext;
		// Create Serializer to load ExpCategory List from JSON file
		mSerializer = new JSONSerializer(mAppContext, JSON_FILENAME);
		try {
			mListInCategories = mSerializer.loadListCategories();
		} catch (Exception e) {
			// initial list of Income Categories when loading app for the first time
			// There are always 2 categories
			mListInCategories = new ArrayList<Category>();
			for (int i = 0; i < NUMBER_CATEGORIES; i++) {
				Category item = new Category();
				item.setId(i);
				mListInCategories.add(item);
			}
			mListInCategories.get(0).setName(MONTHLY_INCOME);
			mListInCategories.get(1).setName(OTHER_INCOME);
			
			Log.e(TAG, "Error Loading new list of Income Categories", e);
		}
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
			if (item.getId() == id) {
				return item;
			}
		}
		return null;
	}
	
	public void addNewCatAmount(int id, double addedAmount) {
		for (Category item : mListInCategories) {
			if (item.getId() == id) {
				item.setAmount(item.getAmount() + addedAmount);
			}
		}
	}
	
	public void updateCatAmount(int catId, double oldAmount, double newAmount) {
		for (Category item : mListInCategories) {
			if (item.getId() == catId) {
				item.setAmount(item.getAmount() - oldAmount + newAmount);
			}
		}
	}
	
	public boolean saveListCat() {
		try {
			mSerializer.saveListCategories(mListInCategories);
			return true;
		} catch (JSONException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
	}
}
