package com.airtribe.lms.constants;

public final class AppConstants {

	// 1. Private constructor prevents anyone from creating an instance of this
	// class
	private AppConstants() {
		throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}

	// 2. Constants are declared as public static final
	public static final String EMAIL_NOTIF_SYSTEM = "EMAIL";
	public static final String SMSL_NOTIF_SYSTEM = "SMS";

	public static final String IN_TRANSIT = "IN_TRANSIT";
	public static final String AVAILABLE = "AVAILABLE";
	public static final String INVALID_SELECTION_TRY_AGAIN = "invalid selection , please try again!!";
	public static final String NO_DATA_FOUND = "no data available";

}
