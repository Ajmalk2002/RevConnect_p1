package com.revconnect.service;

import java.util.List;
import com.revconnect.dao.FeedDao;
import com.revconnect.dao.FeedDaoImpl;
import com.revconnect.model.Post;

public class FeedService {

	private FeedDao dao = new FeedDaoImpl();

	public void viewFeed(int userId) {

		List<Post> posts = dao.getPersonalizedFeed(userId);

		if (posts.isEmpty()) {
			System.out.println("No posts in your feed.");
			return;
		}

		for (Post p : posts) {
			System.out.println(p.getPostId() + " | " + p.getContent());
		}
	}

	public void viewTrending() {

		List<Post> posts = dao.getTrendingPosts();

		if (posts.isEmpty()) {
			System.out.println("No trending posts.");
			return;
		}

		for (Post p : posts) {
			System.out.println(p.getPostId() + " | " + p.getContent());
		}
	}

	public void searchHashtag(String tag) {

		List<Post> posts = dao.searchByHashtag(tag);

		if (posts.isEmpty()) {
			System.out.println("No posts found for #" + tag);
			return;
		}

		for (Post p : posts) {
			System.out.println(p.getPostId() + " | " + p.getContent());
		}
	}

	public void filterByUserType(int userId, String type) {

		List<Post> posts = dao.filterFeed(userId, type);

		if (posts.isEmpty()) {
			System.out.println("No posts found for user type: " + type);
			return;
		}

		for (Post p : posts) {
			System.out.println(p.getPostId() + " | " + p.getContent());
		}
	}
}
