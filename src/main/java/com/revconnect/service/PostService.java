package com.revconnect.service;

import java.util.List;
import java.util.Scanner;
import com.revconnect.core.Post;
import com.revconnect.dao.PostDaoImpl;

public class PostService {

    private PostDaoImpl dao = new PostDaoImpl();

    public void createPost(int userId, Scanner sc) {
        Post p = new Post();
        p.setUserId(userId);

        System.out.print("Post Content: ");
        p.setContent(sc.nextLine());

        System.out.print("Hashtags (optional): ");
        p.setHashtags(sc.nextLine());

        dao.createPost(p);
        System.out.println("Post created successfully!");
    }

    public void viewMyPosts(int userId) {
        List<Post> posts = dao.getMyPosts(userId);
        for (Post p : posts) {
            System.out.println(p.getPostId() + " | " + p.getContent() +
                    " | #" + p.getHashtags());
        }
    }

    public void editPost(int userId, Scanner sc) {
        System.out.print("Enter Post ID: ");
        int pid = Integer.parseInt(sc.nextLine());

        Post p = new Post();
        p.setPostId(pid);
        p.setUserId(userId);

        System.out.print("New Content: ");
        p.setContent(sc.nextLine());

        System.out.print("New Hashtags: ");
        p.setHashtags(sc.nextLine());

        dao.updatePost(p);
        System.out.println("Post updated!");
    }

    public void deletePost(int userId, Scanner sc) {
        System.out.print("Enter Post ID to delete: ");
        int pid = Integer.parseInt(sc.nextLine());
        dao.deletePost(pid, userId);
        System.out.println("Post deleted!");
    }
}
