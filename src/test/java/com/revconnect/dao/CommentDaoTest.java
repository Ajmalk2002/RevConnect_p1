package com.revconnect.dao;

import org.junit.Test;
import com.revconnect.core.Comment;

public class CommentDaoTest {

 @Test
 public void testAddComment(){
  CommentDao dao=new CommentDaoImpl();
  Comment c=new Comment();
  c.setPostId(1);
  c.setUserId(1);
  c.setContent("JUnit Comment");
  dao.addComment(c);
 }
}
