package com.revconnect.service;

import java.util.List;
import com.revconnect.dao.NotificationDao;
import com.revconnect.dao.NotificationDaoImpl;
import com.revconnect.model.Notification;

public class NotificationService {

	private NotificationDao dao = new NotificationDaoImpl();

	public void notify(int userId, String message) {
		dao.addNotification(userId, message);
	}

	public int getUnreadCount(int userId) {
		return dao.getUnreadCount(userId);
	}

	public List<Notification> getUnread(int userId) {
		return dao.getUnreadNotifications(userId);
	}

	public List<Notification> getAll(int userId) {
		return dao.getAllNotifications(userId);
	}

	public void markRead(int notificationId) {
		dao.markAsRead(notificationId);
	}
}
