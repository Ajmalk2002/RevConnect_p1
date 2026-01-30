package com.revconnect.service;

import java.util.Scanner;

import com.revconnect.dao.UserDao;
import com.revconnect.dao.UserDaoImpl;
import com.revconnect.model.User;
import com.revconnect.model.UserType;

public class AuthService {

    private UserDao dao = new UserDaoImpl();

    // ================= REGISTER =================
    public User register(Scanner sc) {

        System.out.print("Email: ");
        String email = sc.nextLine().trim();

        System.out.print("Password: ");
        String password = sc.nextLine().trim();

        if (email.length() == 0 || password.length() == 0) {
            System.out.println("❌ Email and password cannot be empty");
            return null;
        }

        // -------- User Type --------
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
                System.out.println("❌ Invalid choice");
            }
        }

        // -------- Security Question --------
        System.out.println("Select Security Question:");
        System.out.println("1. What is your pet name?");
        System.out.println("2. What is your birth city?");
        System.out.println("3. What is your favorite color?");
        System.out.print("Enter choice (1-3): ");

        String qChoice = sc.nextLine().trim();
        String question = null;

        if ("1".equals(qChoice))
            question = "What is your pet name?";
        else if ("2".equals(qChoice))
            question = "What is your birth city?";
        else if ("3".equals(qChoice))
            question = "What is your favorite color?";
        else {
            System.out.println("❌ Invalid security question choice");
            return null;
        }

        System.out.print("Security Answer: ");
        String answer = sc.nextLine().trim();

        if (answer.length() == 0) {
            System.out.println("❌ Security answer cannot be empty");
            return null;
        }

        // -------- Create User --------
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setUserType(userType);
        user.setSecurityQuestion(question);
        user.setSecurityAnswer(answer);

        int result = dao.register(user);

        if (result == 1) {
            System.out.println("✅ Registration successful");
            return user;
        } else {
            System.out.println("❌ Registration failed (email may already exist)");
            return null;
        }
    }

    // ================= LOGIN =================
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

    // ================= FORGOT PASSWORD =================
    public boolean forgotPassword(Scanner sc) {

        System.out.print("Enter registered email: ");
        String email = sc.nextLine().trim();

        String question = dao.getSecurityQuestion(email);

        if (question == null) {
            System.out.println("❌ Email not found");
            return false;
        }

        System.out.println("Security Question: " + question);
        System.out.print("Answer: ");
        String answer = sc.nextLine().trim();

        System.out.print("Enter new password: ");
        String newPassword = sc.nextLine().trim();

        if (newPassword.length() == 0) {
            System.out.println("❌ Password cannot be empty");
            return false;
        }

        boolean reset = dao.resetPassword(email, answer, newPassword);

        if (reset) {
            System.out.println("✅ Password reset successful");
            return true;
        } else {
            System.out.println("❌ Incorrect security answer");
            return false;
        }
    }
}
