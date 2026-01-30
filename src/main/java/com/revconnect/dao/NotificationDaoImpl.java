package com.revconnect.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.revconnect.config.DBConnection;
import com.revconnect.model.Notification;

public class NotificationDaoImpl implements NotificationDao {

	// add notification
	@Override
	public void addNotification(int userId, String message) {

		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement("INSERT INTO notifications "
					+ "(notification_id, user_id, message, is_read, created_at) "
					+ "VALUES (notification_seq.NEXTVAL, ?, ?, 'N', SYSDATE)");

			ps.setInt(1, userId);
			ps.setString(2, message);
			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps != null)
					ps.close();
			} catch (Exception e) {
			}
			try {
				if (con != null)
					con.close();
			} catch (Exception e) {
			}
		}
	}

	// get all notifications unread
	@Override
	public List<Notification> getUnreadNotifications(int userId) {

		List<Notification> list = new ArrayList<Notification>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement("SELECT * FROM notifications "
					+ "WHERE user_id=? AND is_read='N' "
					+ "ORDER BY created_at DESC");

			ps.setInt(1, userId);
			rs = ps.executeQuery();

			while (rs.next()) {
				Notification n = new Notification();
				n.setNotificationId(rs.getInt("NOTIFICATION_ID"));
				n.setUserId(rs.getInt("USER_ID"));
				n.setMessage(rs.getString("MESSAGE"));
				n.setRead(false);
				n.setCreatedAt(rs.getDate("CREATED_AT"));
				list.add(n);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (ps != null)
					ps.close();
			} catch (Exception e) {
			}
			try {
				if (con != null)
					con.close();
			} catch (Exception e) {
			}
		}

		return list;
	}

	// to see all the notification
	@Override
	public List<Notification> getAllNotifications(int userId) {

		List<Notification> list = new ArrayList<Notification>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement("SELECT * FROM notifications "
					+ "WHERE user_id=? ORDER BY created_at DESC");

			ps.setInt(1, userId);
			rs = ps.executeQuery();

			while (rs.next()) {
				Notification n = new Notification();
				n.setNotificationId(rs.getInt("NOTIFICATION_ID"));
				n.setUserId(rs.getInt("USER_ID"));
				n.setMessage(rs.getString("MESSAGE"));
				n.setRead("Y".equals(rs.getString("IS_READ")));
				n.setCreatedAt(rs.getDate("CREATED_AT"));
				list.add(n);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (ps != null)
					ps.close();
			} catch (Exception e) {
			}
			try {
				if (con != null)
					con.close();
			} catch (Exception e) {
			}
		}

		return list;
	}

	// to get unread count

	@Override
	public int getUnreadCount(int userId) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement("SELECT COUNT(*) FROM notifications "
					+ "WHERE user_id=? AND is_read='N'");

			ps.setInt(1, userId);
			rs = ps.executeQuery();

			if (rs.next()) {
				return rs.getInt(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (ps != null)
					ps.close();
			} catch (Exception e) {
			}
			try {
				if (con != null)
					con.close();
			} catch (Exception e) {
			}
		}

		return 0;
	}

	// to marking as read
	@Override
	public void markAsRead(int notificationId) {

		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement("UPDATE notifications SET is_read='Y' "
					+ "WHERE notification_id=?");

			ps.setInt(1, notificationId);
			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps != null)
					ps.close();
			} catch (Exception e) {
			}
			try {
				if (con != null)
					con.close();
			} catch (Exception e) {
			}
		}
	}
}
