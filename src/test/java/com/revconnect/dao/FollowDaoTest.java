package com.revconnect.dao;

import static org.junit.Assert.*;

import org.junit.Test;

public class FollowDaoTest {

    @Test
    public void testFollowUnfollow() {

        FollowDao dao = new FollowDaoImpl();

        assertTrue(dao.follow(1, 2));
        assertTrue(dao.unfollow(1, 2));
    }

    @Test
    public void testFollowSelf() {

        FollowDao dao = new FollowDaoImpl();
        assertFalse(dao.follow(1, 1));
    }
}
