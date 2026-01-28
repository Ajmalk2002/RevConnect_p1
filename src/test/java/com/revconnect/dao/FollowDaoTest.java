package com.revconnect.dao;

import org.junit.Test;

public class FollowDaoTest {

 @Test
 public void testFollowUnfollow(){
  FollowDao dao=new FollowDaoImpl();
  dao.follow(1,3);
  dao.unfollow(1,3);
 }
}
