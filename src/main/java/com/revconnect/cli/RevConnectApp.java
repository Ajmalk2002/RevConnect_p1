package com.revconnect.cli;

import java.util.List;
import java.util.Scanner;

import com.revconnect.core.Comment;
import com.revconnect.core.Post;
import com.revconnect.core.User;
import com.revconnect.dao.*;
import com.revconnect.service.AuthService;
import com.revconnect.service.ProfileService;

public class RevConnectApp {

    private static PostDaoImpl postDao = new PostDaoImpl();
    private static LikeDaoImpl likeDao = new LikeDaoImpl();
    private static CommentDaoImpl commentDao = new CommentDaoImpl();
    private static ShareDaoImpl shareDao = new ShareDaoImpl();
    private static ConnectionDaoImpl connectionDao = new ConnectionDaoImpl();
    private static FollowDaoImpl followDao = new FollowDaoImpl();
    private static FeedDaoImpl feedDao = new FeedDaoImpl();

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        AuthService authService = new AuthService();
        ProfileService profileService = new ProfileService();
        User user = null;

        System.out.println("==== RevConnect ====");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.print("Enter choice: ");

        int ch = Integer.parseInt(sc.nextLine());

        if (ch == 1) {
            user = authService.register(sc);
            if (user == null) {
                System.out.println("❌ Registration failed");
                return;
            }
            System.out.println("✅ Registration successful");
        }
        else if (ch == 2) {
            user = authService.login(sc);
            if (user == null) {
                System.out.println("❌ Invalid credentials");
                return;
            }
            System.out.println("✅ Login successful");
        }
        else {
            System.out.println("❌ Invalid option");
            return;
        }

        while (true) {

            System.out.println("\n==== Main Menu ====");
            System.out.println("1. Profile Management");
            System.out.println("2. Post Management");
            System.out.println("3. Social Interactions");
            System.out.println("4. Network Building");
            System.out.println("5. Feed & Discovery");
            System.out.println("6. Logout");
            System.out.print("Enter choice: ");

            int choice = Integer.parseInt(sc.nextLine());

            if (choice == 1)
                profileMenu(sc, user, profileService);
            else if (choice == 2)
                postMenu(sc, user);
            else if (choice == 3)
                socialMenu(sc, user);
            else if (choice == 4)
                networkMenu(sc, user);
            else if (choice == 5)
                feedMenu(sc, user);
            else if (choice == 6) {
                System.out.println("👋 Logged out");
                break;
            }
            else
                System.out.println("❌ Invalid option");
        }

        sc.close();
    }

    // ================= PROFILE MENU =================
    private static void profileMenu(Scanner sc, User user, ProfileService profileService) {

        System.out.println("\n=== Profile Menu ===");
        System.out.println("1. Create / Edit Profile");
        System.out.println("2. View My Profile");
        System.out.println("3. Search User");
        System.out.println("4. Back");
        System.out.print("Enter choice: ");

        int opt = Integer.parseInt(sc.nextLine());

        if (opt == 1) {
            boolean success = profileService.createOrEditProfile(
                    user.getUserId(),
                    user.getUserType(),   // ✅ PASS USER TYPE
                    sc
            );
            System.out.println(success ? "✅ Profile saved" : "❌ Save failed");
        }
        else if (opt == 2) {
            profileService.viewProfile(user.getUserId());
        }
        else if (opt == 3) {
            System.out.print("Enter name or email: ");
            new UserDaoImpl().searchUsers(sc.nextLine());
        }
    }

    // ================= POST MENU =================
    private static void postMenu(Scanner sc, User user) {

        while (true) {

            System.out.println("\n=== Post Management ===");
            System.out.println("1. Create Post");
            System.out.println("2. View My Posts");
            System.out.println("3. Edit Post");
            System.out.println("4. Delete Post");
            System.out.println("5. Back");
            System.out.print("Enter choice: ");

            int choice = Integer.parseInt(sc.nextLine());
            if (choice == 5) return;

            if (choice == 1) {
                System.out.print("Content: ");
                String content = sc.nextLine();
                System.out.print("Hashtags: ");
                String hashtags = sc.nextLine();

                Post p = new Post();
                p.setUserId(user.getUserId());
                p.setContent(content);
                p.setHashtags(hashtags);

                postDao.createPost(p);
                System.out.println("✅ Post created");
            }
            else if (choice == 2) {
                List<Post> posts = postDao.getMyPosts(user.getUserId());
                if (posts.isEmpty())
                    System.out.println("No posts found");
                else
                    for (Post p : posts)
                        System.out.println(p.getPostId() + " | " + p.getContent());
            }
            else if (choice == 3) {
                System.out.print("Post ID: ");
                int postId = Integer.parseInt(sc.nextLine());

                System.out.print("New Content: ");
                String content = sc.nextLine();

                System.out.print("New Hashtags: ");
                String hashtags = sc.nextLine();

                Post p = new Post();
                p.setPostId(postId);
                p.setUserId(user.getUserId());
                p.setContent(content);
                p.setHashtags(hashtags);

                postDao.updatePost(p);
                System.out.println("✅ Post updated");
            }
            else if (choice == 4) {
                System.out.print("Post ID: ");
                postDao.deletePost(Integer.parseInt(sc.nextLine()), user.getUserId());
                System.out.println("🗑 Post deleted");
            }
        }
    }

    // ================= SOCIAL MENU =================
    private static void socialMenu(Scanner sc, User user) {

        while (true) {

            System.out.println("\n=== Social ===");
            System.out.println("1. Like");
            System.out.println("2. Unlike");
            System.out.println("3. Comment");
            System.out.println("4. View Comments");
            System.out.println("5. Delete Comment");
            System.out.println("6. Share");
            System.out.println("7. Back");

            int choice = Integer.parseInt(sc.nextLine());
            if (choice == 7) return;

            if (choice == 1) {
                System.out.print("Post ID: ");
                likeDao.likePost(user.getUserId(), Integer.parseInt(sc.nextLine()));
            }
            else if (choice == 2) {
                System.out.print("Post ID: ");
                likeDao.unlikePost(user.getUserId(), Integer.parseInt(sc.nextLine()));
            }
            else if (choice == 3) {
                System.out.print("Post ID: ");
                int postId = Integer.parseInt(sc.nextLine());
                System.out.print("Comment: ");
                String content = sc.nextLine();

                Comment c = new Comment();
                c.setPostId(postId);
                c.setUserId(user.getUserId());
                c.setContent(content);

                commentDao.addComment(c);
            }
            else if (choice == 4) {
                System.out.print("Post ID: ");
                for (Comment c : commentDao.getCommentsByPost(Integer.parseInt(sc.nextLine())))
                    System.out.println(c.getContent());
            }
            else if (choice == 5) {
                System.out.print("Comment ID: ");
                commentDao.deleteComment(Integer.parseInt(sc.nextLine()), user.getUserId());
            }
            else if (choice == 6) {
                System.out.print("Post ID: ");
                shareDao.sharePost(user.getUserId(), Integer.parseInt(sc.nextLine()));
            }
        }
    }

    // ================= NETWORK MENU =================
    private static void networkMenu(Scanner sc, User user) {

        while (true) {

            System.out.println("\n=== Network ===");
            System.out.println("1. Send Request");
            System.out.println("2. View Pending");
            System.out.println("3. Accept");
            System.out.println("4. Reject");
            System.out.println("5. Back");

            int choice = Integer.parseInt(sc.nextLine());
            if (choice == 5) return;

            if (choice == 1) {
                System.out.print("User ID: ");
                connectionDao.sendRequest(user.getUserId(), Integer.parseInt(sc.nextLine()));
            }
            else if (choice == 2) {
                System.out.println(connectionDao.getPendingRequests(user.getUserId()));
            }
            else if (choice == 3) {
                System.out.print("Requester ID: ");
                connectionDao.acceptRequest(Integer.parseInt(sc.nextLine()), user.getUserId());
            }
            else if (choice == 4) {
                System.out.print("Requester ID: ");
                connectionDao.rejectRequest(Integer.parseInt(sc.nextLine()), user.getUserId());
            }
        }
    }

    // ================= FEED MENU =================
    private static void feedMenu(Scanner sc, User user) {

        while (true) {

            System.out.println("\n=== Feed & Discovery ===");
            System.out.println("1. View My Feed");
            System.out.println("2. Trending Posts");
            System.out.println("3. Search Hashtag");
            System.out.println("4. Filter by User Type");
            System.out.println("5. Back");

            int choice = Integer.parseInt(sc.nextLine());
            if (choice == 5) return;

            if (choice == 1) {
                for (Post p : feedDao.getPersonalizedFeed(user.getUserId()))
                    System.out.println(p.getPostId() + " | " + p.getContent());
            }
            else if (choice == 2) {
                for (Post p : feedDao.getTrendingPosts())
                    System.out.println(p.getPostId() + " | " + p.getContent());
            }
            else if (choice == 3) {
                System.out.print("Hashtag: ");
                for (Post p : feedDao.searchByHashtag(sc.nextLine()))
                    System.out.println(p.getPostId() + " | " + p.getContent());
            }
            else if (choice == 4) {
                System.out.print("Type (PERSONAL/BUSINESS/CREATOR): ");
                for (Post p : feedDao.filterFeed(user.getUserId(), sc.nextLine()))
                    System.out.println(p.getPostId() + " | " + p.getContent());
            }
        }
    }
}
