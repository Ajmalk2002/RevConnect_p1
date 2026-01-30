package com.revconnect.service;

import java.util.Scanner;

import com.revconnect.dao.UserDao;
import com.revconnect.dao.UserDaoImpl;
import com.revconnect.model.User;
import com.revconnect.model.UserType;

public class AuthService {

	private UserDao dao = new UserDaoImpl();

	public User register(Scanner sc) {

		System.out.print("Email: ");
		String email = sc.nextLine().trim();

		System.out.print("Password: ");
		String password = sc.nextLine().trim();

		if (email.length() == 0 || password.length() == 0) {
			System.out.println("❌ Email and password cannot be empty");
			return null;
		}

		UserType userType = null;

		while (userType == null) {
			System.out.println("Select Account Type:");
			System.out.println("1. Personal");
			System.out.println("2. Business");
			System.out.println("3. Creator");
			System.out.print("Enter choice (1-3): ");

			String choice = sc.nextLine().trim();

			if ("1".equals(choice)) {
				userType = UserType.PERSONAL;
			} else if ("2".equals(choice)) {
				userType = UserType.BUSINESS;
			} else if ("3".equals(choice)) {
				userType = UserType.CREATOR;
			} else {
				System.out
						.println("❌ Invalid choice. Please select 1, 2, or 3.");
			}
		}

		User user = new User();
		user.setEmail(email);
		user.setPassword(password);
		user.setUserType(userType);

		int result = dao.register(user);

		if (result == 1) {
			System.out.println("✅ Registration successful");
			return user;
		} else {
			System.out
					.println("❌ Registration failed (email may already exist or invalid)");
			return null;
		}
	}

	public User login(Scanner sc) {

		System.out.print("Email: ");
		String email = sc.nextLine().trim();

		System.out.print("Password: ");
		String password = sc.nextLine().trim();

		if (email.length() == 0 || password.length() == 0) {
			System.out.println("❌ Email and password cannot be empty");
			return null;
		}

		User user = dao.login(email, password);

		if (user == null) {
			System.out.println("❌ Invalid email or password");
		} else {
			System.out.println("✅ Login successful");
		}

		return user;
	}
}
