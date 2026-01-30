package com.revconnect.dao;

import java.util.List;

import com.revconnect.model.Post;

public interface FeedDao {

	List<Post> getPersonalizedFeed(int userId);

	List<Post> getTrendingPosts();

	List<Post> searchByHashtag(String hashtag);

	List<Post> filterFeed(int userId, String userType);
}
