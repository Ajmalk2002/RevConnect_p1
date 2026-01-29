package com.revconnect.dao;

import static org.junit.Assert.*;

import org.junit.Test;

import com.revconnect.core.Post;

public class PostDaoTest {

    @Test
    public void testCreatePost() {

        PostDao dao = new PostDaoImpl();

        Post p = new Post();
        p.setUserId(1);
        p.setContent("JUnit Test Post");
        p.setHashtags("junit,test");

        dao.createPost(p);

        assertTrue(true);
    }

    @Test
    public void testCreatePostInvalidUser() {

        PostDao dao = new PostDaoImpl();

        Post p = new Post();
        p.setUserId(-1);
        p.setContent("Invalid post");

        dao.createPost(p);

        assertTrue(true); 
    }

    @Test
    public void testGetMyPosts() {

        PostDao dao = new PostDaoImpl();
        assertNotNull(dao.getMyPosts(1));
    }
}
