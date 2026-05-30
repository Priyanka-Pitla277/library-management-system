package com.airtribe.lms.enums;

public enum AlertType {
    // 1. Define constants with parameters
    SMS("SMS"),
    EMAIL("EMAIL");
    // 2. Define final instance fields
    private final String alertType;

    // 3. Define a private constructor (Public constructors are forbidden)
    private AlertType(String alertType) {
        this.alertType = alertType;
    }

    // 4. Add getter methods to retrieve the data

	public String getAlertType() {
		return alertType;
	}
}
