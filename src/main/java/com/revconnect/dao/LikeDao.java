package com.revconnect.dao;

public interface LikeDao {
 void likePost(int userId,int postId);
 void unlikePost(int userId,int postId);
}
