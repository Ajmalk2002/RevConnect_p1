package com.revconnect.dao;

import com.revconnect.model.User;

public interface UserDao {
	int register(User user);

	User login(String email, String password);

	void searchUsers(String key);

	boolean changePassword(int userId, String oldPassword, String newPassword);

}
