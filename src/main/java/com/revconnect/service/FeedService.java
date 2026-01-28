package com.revconnect.service;

import java.util.List;
import com.revconnect.core.Post;
import com.revconnect.dao.FeedDao;
import com.revconnect.dao.FeedDaoImpl;

public class FeedService {

    private FeedDao dao = new FeedDaoImpl();

    // 1️⃣ View Personalized Feed
    public void viewFeed(int userId) {

        List<Post> posts = dao.getPersonalizedFeed(userId);

        if (posts.isEmpty()) {
            System.out.println("No posts in your feed.");
            return;
        }

        for (Post p : posts) {
            System.out.println(
                p.getPostId() + " | " + p.getContent()
            );
        }
    }

    // 2️⃣ View Trending Posts
    public void viewTrending() {

        List<Post> posts = dao.getTrendingPosts();

        if (posts.isEmpty()) {
            System.out.println("No trending posts.");
            return;
        }

        for (Post p : posts) {
            System.out.println(
                p.getPostId() + " | " + p.getContent()
            );
        }
    }

    // 3️⃣ Search Posts by Hashtag
    public void searchHashtag(String tag) {

        List<Post> posts = dao.searchByHashtag(tag);

        if (posts.isEmpty()) {
            System.out.println("No posts found for #" + tag);
            return;
        }

        for (Post p : posts) {
            System.out.println(
                p.getPostId() + " | " + p.getContent()
            );
        }
    }

    // 4️⃣ Filter Feed by User Type
    public void filterByUserType(int userId, String type) {

        List<Post> posts = dao.filterFeed(userId, type);

        if (posts.isEmpty()) {
            System.out.println("No posts found for user type: " + type);
            return;
        }

        for (Post p : posts) {
            System.out.println(
                p.getPostId() + " | " + p.getContent()
            );
        }
    }
}
