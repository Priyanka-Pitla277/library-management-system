package com.airtribe.lms.notifcation.service;

import com.airtribe.lms.model.Book;
import com.airtribe.lms.model.Patron;

public interface NotificationSystem {
	
	public void notifyUser(Patron patron, Book book);

}
