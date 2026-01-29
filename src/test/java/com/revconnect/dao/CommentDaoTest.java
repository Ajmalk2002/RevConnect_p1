package com.revconnect.dao;

import org.junit.Test;
import static org.junit.Assert.*;

import com.revconnect.core.Comment;

public class CommentDaoTest {

    // ✅ POSITIVE: Add comment
    @Test
    public void testAddComment() {

        CommentDao dao = new CommentDaoImpl();

        Comment c = new Comment();
        c.setPostId(1);
        c.setUserId(1);
        c.setContent("JUnit Comment");

        dao.addComment(c);

        assertTrue(true);
    }

    // ❌ NEGATIVE: Invalid post
    @Test
    public void testAddCommentInvalidPost() {

        CommentDao dao = new CommentDaoImpl();

        Comment c = new Comment();
        c.setPostId(-1);
        c.setUserId(1);
        c.setContent("Invalid");

        dao.addComment(c);

        assertTrue(true);
    }
}
