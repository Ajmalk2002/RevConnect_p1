package com.revconnect.dao;

import java.util.List;

public interface FollowDao {

    boolean follow(int followerId, int followeeId);

    boolean unfollow(int followerId, int followeeId);

    List<Integer> getFollowers(int userId);

    List<Integer> getFollowing(int userId);
}
