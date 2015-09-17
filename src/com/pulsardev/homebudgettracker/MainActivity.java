/**
 * Manage main Fragments and Navigation Drawer
 * @author ngapham
 * Update 5/9/2015: Manage item click event of Nav Drawer
 */

package com.pulsardev.homebudgettracker;

import android.app.Fragment;
import android.os.Bundle;

public class MainActivity extends NavDrawerActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// call the main Fragments
		hostFragmentInContainer(savedInstanceState, R.id.main_container,
				new ExpenseFragment());
	}

	protected void hostFragmentInContainer(Bundle savedInstanceState,
			int containerId, Fragment newFragment) {
		if (savedInstanceState == null) {
			this.mFragmentManager = getFragmentManager();
			this.mFragmentManager.beginTransaction()
					.add(R.id.main_container, newFragment).commit();
		}
	}
}
