package com.airtribe.lms.notifcation.service;

import com.airtribe.lms.model.Book;
import com.airtribe.lms.model.Patron;

public class EmailNotificationSystem implements NotificationSystem {

	@Override
	public void notifyUser(Patron patron, Book book) {
		{
			System.out.println(patron.getChosenalertType() + ": Dear " + patron.getName()
					+ " you can checkout the book you reserved on priority: " + book.getTitle());

		}
	}

}
