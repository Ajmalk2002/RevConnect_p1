package com.revconnect.dao;

import org.junit.Test;
import static org.junit.Assert.*;

import com.revconnect.core.User;
import com.revconnect.core.UserType;

public class UserDaoTest {

    /*
     * POSITIVE TEST CASE
     * Valid email registration
     */
    @Test
    public void testRegisterWithValidEmail() {

        UserDao dao = new UserDaoImpl();

        User user = new User();
        user.setEmail("valid_" + System.currentTimeMillis() + "@rev.com");
        user.setPassword("12345");
        user.setUserType(UserType.PERSONAL);

        int result = dao.register(user);

        assertEquals(1, result);
        
        System.out.println("Register Success");
    }

    /*
     * NEGATIVE TEST CASE
     * Invalid email registration
     */
    @Test
    public void testRegisterWithInvalidEmail() {

        UserDao dao = new UserDaoImpl();

        User user = new User();
        user.setEmail("invalid-email");   
        user.setPassword("12345");
        user.setUserType(UserType.PERSONAL);

        int result = dao.register(user);

        assertEquals(0, result);
        
    }
}
