package com.revconnect.dao;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class LikeDaoTest {

    private LikeDao dao;

    private final int USER_ID = 1;
    private final int POST_ID = 1;

    @Before
    public void setup() {
        dao = new LikeDaoImpl();
        dao.unlikePost(USER_ID, POST_ID);
    }

    @Test
    public void testLikePost() {
        assertTrue(dao.likePost(USER_ID, POST_ID));
    }

    @Test
    public void testDuplicateLike() {
        dao.likePost(USER_ID, POST_ID);
        assertFalse(dao.likePost(USER_ID, POST_ID));
    }

    @Test
    public void testUnlike() {
        dao.likePost(USER_ID, POST_ID);
        assertTrue(dao.unlikePost(USER_ID, POST_ID));
    }

    @Test
    public void testUnlikeWithoutLike() {
        assertFalse(dao.unlikePost(USER_ID, POST_ID));
    }
}
