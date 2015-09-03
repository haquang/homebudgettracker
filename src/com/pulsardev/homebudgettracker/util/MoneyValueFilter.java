/**
 * Limit the number of digits after decimal point of Money's value
 * @author ngapham
 * Date: 3/9/2015
 */
package com.pulsardev.homebudgettracker.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.InputFilter;
import android.text.Spanned;

public class MoneyValueFilter implements InputFilter {

	Pattern mPattern;

	public MoneyValueFilter(int digitsAfterZero) {
		mPattern = Pattern.compile("[0-9]*+((\\.[0-9]{0," + (digitsAfterZero-1) + "})?)||(\\.)?");
	}

	@Override
	public CharSequence filter(CharSequence source, int start, int end,
			Spanned dest, int dStart, int dEnd) {
		Matcher matcher = mPattern.matcher(dest);       
		if(!matcher.matches()) {
			return "";
		}
		return null;
	}
}
