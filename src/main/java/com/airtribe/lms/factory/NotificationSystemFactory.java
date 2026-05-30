package com.airtribe.lms.factory;

import com.airtribe.lms.enums.AlertType;
import com.airtribe.lms.notifcation.service.EmailNotificationSystem;
import com.airtribe.lms.notifcation.service.NotificationSystem;
import com.airtribe.lms.notifcation.service.SMSNotificationSystem;

/**
 * @author Priyanka
 *
 */
public class NotificationSystemFactory {


	/**
	 * Factory method to create specific notification system to send notifications
	 * 
	 * @param notificationType
	 * @return
	 */
	public static NotificationSystem createNotificationEngine(AlertType notificationType) {
		NotificationSystem notificationSystem = null;
		if (notificationType.equals(AlertType.SMS)) {
			notificationSystem = new SMSNotificationSystem();
		}
		if (notificationType.equals(AlertType.EMAIL)) {
			notificationSystem = new EmailNotificationSystem();
		}
		return notificationSystem;

	}

}
