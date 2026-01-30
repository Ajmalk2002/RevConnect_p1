package com.revconnect.dao;

import java.util.List;

import com.revconnect.model.Notification;

public interface NotificationDao {

	void addNotification(int userId, String message);

	List<Notification> getUnreadNotifications(int userId);

	List<Notification> getAllNotifications(int userId);

	int getUnreadCount(int userId);

	void markAsRead(int notificationId);
}
