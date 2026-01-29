package com.revconnect.cli;

import java.util.List;
import java.util.Scanner;

import com.revconnect.core.Comment;
import com.revconnect.core.Notification;
import com.revconnect.core.Post;
import com.revconnect.core.User;
import com.revconnect.dao.*;
import com.revconnect.service.AuthService;
import com.revconnect.service.ProfileService;
import com.revconnect.service.NotificationService;

public class RevConnectApp {

    private static PostDaoImpl postDao = new PostDaoImpl();
    private static LikeDaoImpl likeDao = new LikeDaoImpl();
    private static CommentDaoImpl commentDao = new CommentDaoImpl();
    private static ShareDaoImpl shareDao = new ShareDaoImpl();
    private static ConnectionDaoImpl connectionDao = new ConnectionDaoImpl();
    private static FeedDaoImpl feedDao = new FeedDaoImpl();
    private static NotificationService notificationService = new NotificationService();

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        AuthService authService = new AuthService();
        ProfileService profileService = new ProfileService();

        while (true) {

            User user = null;

            System.out.println("\n==== RevConnect ====");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");

            int ch = readInt(sc);

            if (ch == 0) {
                System.out.println("👋 Exiting RevConnect");
                break;
            }

            switch (ch) {
                case 1:
                    user = authService.register(sc);
                    if (user == null) {
                        System.out.println("❌ Registration failed");
                        continue;
                    }
                    System.out.println("✅ Registration successful");
                    break;

                case 2:
                    user = authService.login(sc);
                    if (user == null) {
                        System.out.println("❌ Invalid credentials");
                        continue;
                    }
                    System.out.println("✅ Login successful");
                    break;

                default:
                    System.out.println("❌ Invalid option");
                    continue;
            }

            System.out.println("🔔 You have " +
                    notificationService.getUnreadCount(user.getUserId()) +
                    " unread notifications");

            while (true) {

                System.out.println("\n==== Main Menu ====");
                System.out.println("1. Profile Management");
                System.out.println("2. Post Management");
                System.out.println("3. Social Interactions");
                System.out.println("4. Network Building");
                System.out.println("5. Feed & Discovery");
                System.out.println("6. Notifications (" +
                        notificationService.getUnreadCount(user.getUserId()) +
                        " unread)");
                System.out.println("7. Logout");
                System.out.println("0. Exit");
                System.out.print("Enter choice: ");

                int choice = readInt(sc);

                switch (choice) {

                    case 0:
                        System.out.println("👋 Exiting RevConnect");
                        sc.close();
                        return;

                    case 7:
                        System.out.println("👋 Logged out");
                        break;

                    case 1:
                        profileMenu(sc, user, profileService);
                        break;

                    case 2:
                        postMenu(sc, user);
                        break;

                    case 3:
                        socialMenu(sc, user);
                        break;

                    case 4:
                        networkMenu(sc, user);
                        break;

                    case 5:
                        feedMenu(sc, user);
                        break;

                    case 6:
                        notificationMenu(sc, user);
                        break;

                    default:
                        System.out.println("❌ Invalid option");
                        continue;
                }

                if (choice == 7) break;
            }
        }

        sc.close();
    }

    private static void profileMenu(Scanner sc, User user, ProfileService profileService) {

        while (true) {
            System.out.println("\n=== Profile Menu ===");
            System.out.println("1. Create / Edit Profile");
            System.out.println("2. View My Profile");
            System.out.println("3. Search User");
            System.out.println("4. Back");
            System.out.print("Enter choice: ");

            int opt = readInt(sc);

            switch (opt) {
                case 1:
                    boolean success = profileService.createOrEditProfile(
                            user.getUserId(), user.getUserType(), sc);
                    System.out.println(success ? "✅ Profile saved" : "❌ Save failed");
                    break;

                case 2:
                    profileService.viewProfile(user.getUserId());
                    break;

                case 3:
                    System.out.print("Enter name or email: ");
                    new UserDaoImpl().searchUsers(sc.nextLine());
                    break;

                case 4:
                    return;

                default:
                    System.out.println("❌ Invalid option");
            }
        }
    }

    private static void postMenu(Scanner sc, User user) {

        while (true) {
            System.out.println("\n=== Post Management ===");
            System.out.println("1. Create Post");
            System.out.println("2. View My Posts");
            System.out.println("3. Edit Post");
            System.out.println("4. Delete Post");
            System.out.println("5. Back");
            System.out.print("Enter choice: ");

            int choice = readInt(sc);

            switch (choice) {
                case 1:
                    System.out.print("Content: ");
                    String content = readNonEmpty(sc);
                    System.out.print("Hashtags: ");
                    String hashtags = sc.nextLine();
                    Post p = new Post();
                    p.setUserId(user.getUserId());
                    p.setContent(content);
                    p.setHashtags(hashtags);
                    postDao.createPost(p);
                    System.out.println("✅ Post created");
                    break;

                case 2:
                    List<Post> posts = postDao.getMyPosts(user.getUserId());
                    if (posts.isEmpty())
                        System.out.println("ℹ No posts found");
                    else
                        for (Post post : posts)
                            System.out.println(post.getPostId() + " | " + post.getContent());
                    break;

                case 3:
                    System.out.print("Post ID: ");
                    int postId = readInt(sc);
                    System.out.print("New Content: ");
                    content = readNonEmpty(sc);
                    System.out.print("New Hashtags: ");
                    hashtags = sc.nextLine();
                    Post edit = new Post();
                    edit.setPostId(postId);
                    edit.setUserId(user.getUserId());
                    edit.setContent(content);
                    edit.setHashtags(hashtags);
                    postDao.updatePost(edit);
                    System.out.println("✅ Post updated");
                    break;

                case 4:
                    System.out.print("Post ID: ");
                    postDao.deletePost(readInt(sc), user.getUserId());
                    System.out.println("🗑 Post deleted");
                    break;

                case 5:
                    return;

                default:
                    System.out.println("❌ Invalid option");
            }
        }
    }

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
            System.out.print("Enter choice: ");

            int choice = readInt(sc);

            switch (choice) {
                case 1:
                    System.out.print("Post ID: ");
                    likeDao.likePost(user.getUserId(), readInt(sc));
                    System.out.println("✅ Post liked");
                    break;

                case 2:
                    System.out.print("Post ID: ");
                    likeDao.unlikePost(user.getUserId(), readInt(sc));
                    System.out.println("✅ Post unliked");
                    break;

                case 3:
                    System.out.print("Post ID: ");
                    int pid = readInt(sc);
                    System.out.print("Comment: ");
                    String txt = readNonEmpty(sc);
                    Comment c = new Comment();
                    c.setPostId(pid);
                    c.setUserId(user.getUserId());
                    c.setContent(txt);
                    commentDao.addComment(c);
                    System.out.println("✅ Comment added");
                    break;

                case 4:
                    System.out.print("Post ID: ");
                    List<Comment> list = commentDao.getCommentsByPost(readInt(sc));
                    if (list.isEmpty())
                        System.out.println("ℹ No comments found");
                    else
                        for (Comment cm : list)
                            System.out.println(cm.getCommentId() + " | " + cm.getContent());
                    break;

                case 5:
                    System.out.print("Comment ID: ");
                    commentDao.deleteComment(readInt(sc), user.getUserId());
                    System.out.println("🗑 Comment deleted");
                    break;

                case 6:
                    System.out.print("Post ID: ");
                    shareDao.sharePost(user.getUserId(), readInt(sc));
                    System.out.println("🔁 Post shared");
                    break;

                case 7:
                    return;

                default:
                    System.out.println("❌ Invalid option");
            }
        }
    }

    private static void networkMenu(Scanner sc, User user) {

        while (true) {

            System.out.println("\n=== Network ===");
            System.out.println("1. Send Request");
            System.out.println("2. View Pending Requests");
            System.out.println("3. View My Connections");
            System.out.println("4. Accept Request");
            System.out.println("5. Reject Request");
            System.out.println("6. Back");
            System.out.print("Enter choice: ");

            int choice = readInt(sc);

            switch (choice) {

                case 1: {
                    System.out.print("Enter User ID: ");
                    int target = readInt(sc);

                    boolean sent =
                        connectionDao.sendRequest(user.getUserId(), target);

                    if (sent) {
                        notificationService.notify(
                            target, "New connection request");
                        System.out.println("📨 Request sent successfully");
                    } else {
                        System.out.println("❌ Unable to send request");
                    }
                    break;
                }

                case 2: {
                    List<String> pending =
                        connectionDao.getPendingRequests(user.getUserId());

                    if (pending.isEmpty()) {
                        System.out.println("ℹ No pending requests");
                    } else {
                        System.out.println("=== Pending Requests ===");
                        for (String p : pending) {
                            System.out.println(p);
                        }
                    }
                    break;
                }

                case 3: {
                    List<Integer> connections =
                        connectionDao.getConnections(user.getUserId());

                    if (connections.isEmpty()) {
                        System.out.println("ℹ No connections yet");
                    } else {
                        System.out.println("=== My Connections ===");
                        for (Integer id : connections) {
                            System.out.println("Connected User ID: " + id);
                        }
                    }
                    break;
                }

                case 4: {
                    System.out.print("Enter Requester ID: ");
                    int req = readInt(sc);

                    boolean accepted =
                        connectionDao.acceptRequest(req, user.getUserId());

                    if (accepted) {
                        notificationService.notify(
                            req, "Your connection request was accepted");
                        System.out.println("✅ Connection accepted");
                    } else {
                        System.out.println("❌ Unable to accept request");
                    }
                    break;
                }

                case 5: {
                    System.out.print("Enter Requester ID: ");
                    int rid = readInt(sc);

                    boolean rejected =
                        connectionDao.rejectRequest(rid, user.getUserId());

                    if (rejected) {
                        System.out.println("❌ Connection rejected");
                    } else {
                        System.out.println("⚠ No such request found");
                    }
                    break;
                }

                case 6:
                    return;

                default:
                    System.out.println("❌ Invalid option");
            }
        }
    }


    private static void feedMenu(Scanner sc, User user) {

        while (true) {
            System.out.println("\n=== Feed & Discovery ===");
            System.out.println("1. View My Feed");
            System.out.println("2. Trending Posts");
            System.out.println("3. Search Hashtag");
            System.out.println("4. Filter by User Type");
            System.out.println("5. Back");
            System.out.print("Enter choice: ");

            int choice = readInt(sc);

            switch (choice) {
                case 1:
                    List<Post> feed = feedDao.getPersonalizedFeed(user.getUserId());
                    if (feed.isEmpty())
                        System.out.println("ℹ Feed is empty");
                    else
                        for (Post p : feed)
                            System.out.println(p.getPostId() + " | " + p.getContent());
                    break;

                case 2:
                    for (Post p : feedDao.getTrendingPosts())
                        System.out.println(p.getPostId() + " | " + p.getContent());
                    break;

                case 3:
                    System.out.print("Hashtag: ");
                    for (Post p : feedDao.searchByHashtag(sc.nextLine()))
                        System.out.println(p.getPostId() + " | " + p.getContent());
                    break;

                case 4:
                    System.out.print("Type: ");
                    for (Post p : feedDao.filterFeed(user.getUserId(), sc.nextLine()))
                        System.out.println(p.getPostId() + " | " + p.getContent());
                    break;

                case 5:
                    return;

                default:
                    System.out.println("❌ Invalid option");
            }
        }
    }

    private static void notificationMenu(Scanner sc, User user) {

        while (true) {
            System.out.println("\n=== Notifications ===");
            System.out.println("1. View Unread");
            System.out.println("2. View All");
            System.out.println("3. Mark as Read");
            System.out.println("4. Back");
            System.out.print("Enter choice: ");

            int choice = readInt(sc);

            switch (choice) {
                case 1:
                    List<Notification> unread = notificationService.getUnread(user.getUserId());
                    if (unread.isEmpty())
                        System.out.println("ℹ No unread notifications");
                    else
                        for (Notification n : unread)
                            System.out.println(n.getNotificationId() + " | " + n.getMessage());
                    break;

                case 2:
                    List<Notification> all = notificationService.getAll(user.getUserId());
                    if (all.isEmpty())
                        System.out.println("ℹ No notifications");
                    else
                        for (Notification n : all)
                            System.out.println(n.getNotificationId() + " | " +
                                    n.getMessage() + " | " +
                                    (n.isRead() ? "READ" : "UNREAD"));
                    break;

                case 3:
                    System.out.print("Notification ID: ");
                    notificationService.markRead(readInt(sc));
                    System.out.println("✅ Marked as read");
                    break;

                case 4:
                    return;

                default:
                    System.out.println("❌ Invalid option");
            }
        }
    }

    private static int readInt(Scanner sc) {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("❌ Enter a valid number: ");
            }
        }
    }

    private static String readNonEmpty(Scanner sc) {
        while (true) {
            String input = sc.nextLine().trim();
            if (!input.isEmpty()) return input;
            System.out.print("❌ Input cannot be empty: ");
        }
    }
}
