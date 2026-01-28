package com.revconnect.service;

import java.util.Scanner;

import com.revconnect.core.User;
import com.revconnect.core.UserType;
import com.revconnect.dao.UserDao;
import com.revconnect.dao.UserDaoImpl;

public class AuthService {

    private UserDao dao = new UserDaoImpl();

    public User register(Scanner sc) {

        System.out.print("Email: ");
        String email = sc.nextLine();

        System.out.print("Password: ");
        String password = sc.nextLine();

        UserType userType = null;
        while (userType == null) {
            System.out.println("Select Account Type:");
            System.out.println("1. Personal");
            System.out.println("2. Business");
            System.out.println("3. Creator");
            System.out.print("Enter choice (1-3): ");

            String choice = sc.nextLine();

            if ("1".equals(choice))
                userType = UserType.PERSONAL;
            else if ("2".equals(choice))
                userType = UserType.BUSINESS;
            else if ("3".equals(choice))
                userType = UserType.CREATOR;
            else
                System.out.println("Invalid choice.");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setUserType(userType); // ENUM

        int result = dao.register(user); // ✅ FIXED

        return result == 1 ? user : null;
    }

    public User login(Scanner sc) {

        System.out.print("Email: ");
        String e = sc.nextLine();

        System.out.print("Password: ");
        String p = sc.nextLine();

        return dao.login(e, p);
    }
}
