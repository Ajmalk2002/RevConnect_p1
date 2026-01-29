package com.revconnect.dao;

import static org.junit.Assert.*;

import org.junit.Test;

import com.revconnect.core.User;
import com.revconnect.core.UserType;

public class UserDaoTest {

    @Test
    public void testRegisterSuccess() {

        UserDao dao = new UserDaoImpl();

        User user = new User();
        user.setEmail("user_" + System.currentTimeMillis() + "@rev.com");
        user.setPassword("12345");
        user.setUserType(UserType.PERSONAL);

        int result = dao.register(user);

        assertEquals(1, result);
    }

    @Test
    public void testRegisterInvalidEmail() {

        UserDao dao = new UserDaoImpl();

        User user = new User();
        user.setEmail("invalid-email");
        user.setPassword("12345");
        user.setUserType(UserType.PERSONAL);

        int result = dao.register(user);

        assertEquals(0, result);
    }

        @Test
    public void testRegisterDuplicateEmail() {

        UserDao dao = new UserDaoImpl();

        String email = "dup_" + System.currentTimeMillis() + "@rev.com";

        User u1 = new User();
        u1.setEmail(email);
        u1.setPassword("12345");
        u1.setUserType(UserType.PERSONAL);
        dao.register(u1);

        User u2 = new User();
        u2.setEmail(email);
        u2.setPassword("12345");
        u2.setUserType(UserType.PERSONAL);

        int result = dao.register(u2);

        assertEquals(0, result);
    }
}
