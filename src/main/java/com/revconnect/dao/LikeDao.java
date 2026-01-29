package com.revconnect.dao;

public interface LikeDao {

    boolean likePost(int userId, int postId);

    boolean unlikePost(int userId, int postId);
}
