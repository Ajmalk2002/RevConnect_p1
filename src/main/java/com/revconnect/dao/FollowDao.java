package com.revconnect.dao;

import java.util.List;

public interface FollowDao {
    void follow(int followerId, int followeeId);
    void unfollow(int followerId, int followeeId);
    List<Integer> getFollowers(int userId);
    List<Integer> getFollowing(int userId);
}
