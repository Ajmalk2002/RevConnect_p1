package com.revconnect.dao;

import org.junit.Test;

public class LikeDaoTest {

 @Test
 public void testLikeAndUnlike(){
  LikeDao dao=new LikeDaoImpl();
  dao.likePost(1,1);
  dao.unlikePost(1,1);
 }
}
