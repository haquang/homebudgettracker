package com.pulsardev.homebudgettracker.util;

import java.util.InputMismatchException;

public class Validator {
	public static void validateNullAmount(String amount) {
		if (amount == null) {
			throw new InputMismatchException();
		}
		return;
	}
}
