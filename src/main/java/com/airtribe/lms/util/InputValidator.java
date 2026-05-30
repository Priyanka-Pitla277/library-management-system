package com.airtribe.lms.util;

import com.airtribe.lms.constants.AppConstants;
import com.airtribe.lms.exception.InvalidFieldException;
import com.airtribe.lms.exception.InvalidInputException;
import com.airtribe.lms.exception.ResourceNotFoundException;

public class InputValidator {
	

	public static void invalidInput() {
		try {
			throw new InvalidInputException(AppConstants.INVALID_SELECTION_TRY_AGAIN);
		} catch (InvalidInputException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void emptyData(String message) {
		try {
			throw new ResourceNotFoundException(message);
		} catch (ResourceNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void invalidFieldFound(String message) {
		try {
			throw new InvalidFieldException(message);
		} catch (InvalidFieldException e) {
			System.out.println(e.getMessage());
		}
	}	
	
	public static void invalidRequest(String message) {
		try {
			throw new InvalidFieldException(message);
		} catch (InvalidFieldException e) {
			System.out.println(e.getMessage());
		}
	
	}
}