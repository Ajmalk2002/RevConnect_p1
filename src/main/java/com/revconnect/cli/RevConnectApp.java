package com.revconnect.cli;

import java.util.List;
import java.util.Scanner;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.revconnect.core.Comment;
import com.revconnect.core.Notification;
import com.revconnect.core.Post;
import com.revconnect.core.User;
import com.revconnect.dao.*;
import com.revconnect.service.AuthService;
import com.revconnect.service.ProfileService;
import com.revconnect.service.NotificationService;

public class RevConnectApp {

	private static final Logger log = Logger.getLogger(RevConnectApp.class);
	private static PostDaoImpl postDao = new PostDaoImpl();
	private static LikeDaoImpl likeDao = new LikeDaoImpl();
	private static CommentDaoImpl commentDao = new CommentDaoImpl();
	private static ShareDaoImpl shareDao = new ShareDaoImpl();
	private static ConnectionDaoImpl connectionDao = new ConnectionDaoImpl();
	private static FeedDaoImpl feedDao = new FeedDaoImpl();
	private static NotificationService notificationService = new NotificationService();

	public static void main(String[] args) {

		log.info("RevConnect application started");
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
				log.info("Application exit");
				break;
			}

			switch (ch) {
			case 1:
				user = authService.register(sc);
				if (user == null) {
					log.warn("Registration failed");
					continue;
				}
				log.info("User registered: " + user.getEmail());
				break;

			case 2:
				user = authService.login(sc);
				if (user == null) {
					log.warn("Login failed");
					continue;
				}
				log.info("User logged in: " + user.getEmail());
				break;

			default:
				continue;
			}

			System.out.println("🔔 You have "
					+ notificationService.getUnreadCount(user.getUserId())
					+ " unread notifications");

			while (true) {

				System.out.println("\n==== Main Menu ====");
				System.out.println("1. Profile Management");
				System.out.println("2. Post Management");
				System.out.println("3. Social Interactions");
				System.out.println("4. Network Building");
				System.out.println("5. Feed & Discovery");
				System.out.println("6. Notifications ("
						+ notificationService.getUnreadCount(user.getUserId())
						+ " unread)");
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

				if (choice == 7)
					break;
			}
		}

		sc.close();
	}

	private static void profileMenu(Scanner sc, User user,
			ProfileService profileService) {

		UserDaoImpl userDao = new UserDaoImpl();

		while (true) {

			System.out.println("\n=== Profile Menu ===");
			System.out.println("1. Create / Edit Profile");
			System.out.println("2. View My Profile");
			System.out.println("3. Search User");
			System.out.println("4. Change Password");
			System.out.println("5. Back");
			System.out.print("Enter choice: ");

			int opt = readInt(sc);

			switch (opt) {

			case 1: {
				log.info("Profile create/edit selected by userId="
						+ user.getUserId());

				boolean success = profileService.createOrEditProfile(
						user.getUserId(), user.getUserType(), sc);

				if (success) {
					log.info("Profile saved successfully for userId="
							+ user.getUserId());
					System.out.println("✅ Profile saved");
				} else {
					log.warn("Profile save failed for userId="
							+ user.getUserId());
					System.out.println("❌ Profile save failed");
				}
				break;
			}

			case 2:
				log.info("View profile requested for userId="
						+ user.getUserId());
				profileService.viewProfile(user.getUserId());
				break;

			case 3:
				System.out.print("Enter name or email: ");
				String keyword = sc.nextLine();

				log.info("User search triggered. keyword=" + keyword
						+ ", by userId=" + user.getUserId());

				userDao.searchUsers(keyword);
				break;

			case 4: {
				log.info("Change password initiated for userId="
						+ user.getUserId());

				System.out.print("Enter old password: ");
				String oldPass = sc.nextLine();

				System.out.print("Enter new password: ");
				String newPass = sc.nextLine();

				boolean changed = userDao.changePassword(user.getUserId(),
						oldPass, newPass);

				if (changed) {
					log.info("Password changed successfully for userId="
							+ user.getUserId());
					System.out.println("✅ Password changed successfully");
				} else {
					log.warn("Password change failed (wrong old password) for userId="
							+ user.getUserId());
					System.out.println("❌ Old password incorrect");
				}
				break;
			}

			case 5:
				log.info("Exiting Profile Menu for userId=" + user.getUserId());
				return;

			default:
				log.warn("Invalid profile menu option selected: opt=" + opt
						+ ", userId=" + user.getUserId());
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
				log.info("Create Post option selected by userId="
						+ user.getUserId());

				System.out.print("Content: ");
				String content = readNonEmpty(sc);

				System.out.print("Hashtags: ");
				String hashtags = sc.nextLine();

				Post p = new Post();
				p.setUserId(user.getUserId());
				p.setContent(content);
				p.setHashtags(hashtags);

				postDao.createPost(p);

				log.info("Post created successfully by userId="
						+ user.getUserId() + ", contentLength="
						+ content.length());

				System.out.println("✅ Post created");
				break;

			case 2:
				log.info("View My Posts selected by userId=" + user.getUserId());

				List<Post> posts = postDao.getMyPosts(user.getUserId());

				if (posts.isEmpty()) {
					log.warn("No posts found for userId=" + user.getUserId());
					System.out.println("ℹ No posts found");
				} else {
					log.info("Found " + posts.size() + " posts for userId="
							+ user.getUserId());
					for (Post post : posts) {
						System.out.println(post.getPostId() + " | "
								+ post.getContent());
					}
				}
				break;

			case 3:
				log.info("Edit Post option selected by userId="
						+ user.getUserId());

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

				log.info("Post updated successfully. postId=" + postId
						+ ", userId=" + user.getUserId());

				System.out.println("✅ Post updated");
				break;

			case 4:
				log.warn("Delete Post option selected by userId="
						+ user.getUserId());

				System.out.print("Post ID: ");
				int deletePostId = readInt(sc);

				postDao.deletePost(deletePostId, user.getUserId());

				log.warn("Post deleted. postId=" + deletePostId + ", userId="
						+ user.getUserId());

				System.out.println("🗑 Post deleted");
				break;

			case 5:
				log.info("Exiting Post Management menu for userId="
						+ user.getUserId());
				return;

			default:
				log.error("Invalid post menu option selected: " + choice
						+ " by userId=" + user.getUserId());
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
				log.info("Like Post selected by userId=" + user.getUserId());

				System.out.print("Post ID: ");
				int likePostId = readInt(sc);

				likeDao.likePost(user.getUserId(), likePostId);

				log.info("Post liked successfully. postId=" + likePostId
						+ ", userId=" + user.getUserId());

				System.out.println("✅ Post liked");
				break;

			case 2:
				log.info("Unlike Post selected by userId=" + user.getUserId());

				System.out.print("Post ID: ");
				int unlikePostId = readInt(sc);

				likeDao.unlikePost(user.getUserId(), unlikePostId);

				log.info("Post unliked successfully. postId=" + unlikePostId
						+ ", userId=" + user.getUserId());

				System.out.println("✅ Post unliked");
				break;

			case 3:
				log.info("Add Comment selected by userId=" + user.getUserId());

				System.out.print("Post ID: ");
				int pid = readInt(sc);

				System.out.print("Comment: ");
				String txt = readNonEmpty(sc);

				Comment c = new Comment();
				c.setPostId(pid);
				c.setUserId(user.getUserId());
				c.setContent(txt);

				commentDao.addComment(c);

				log.info("Comment added successfully. postId=" + pid
						+ ", userId=" + user.getUserId() + ", contentLength="
						+ txt.length());

				System.out.println("✅ Comment added");
				break;

			case 4:
				log.info("View Comments selected by userId=" + user.getUserId());

				System.out.print("Post ID: ");
				int viewPostId = readInt(sc);

				List<Comment> list = commentDao.getCommentsByPost(viewPostId);

				if (list.isEmpty()) {
					log.warn("No comments found for postId=" + viewPostId);
					System.out.println("ℹ No comments found");
				} else {
					log.info("Found " + list.size() + " comments for postId="
							+ viewPostId);
					for (Comment cm : list) {
						System.out.println(cm.getCommentId() + " | "
								+ cm.getContent());
					}
				}
				break;

			case 5:
				log.warn("Delete Comment selected by userId="
						+ user.getUserId());

				System.out.print("Comment ID: ");
				int commentId = readInt(sc);

				commentDao.deleteComment(commentId, user.getUserId());

				log.warn("Comment deleted. commentId=" + commentId
						+ ", userId=" + user.getUserId());

				System.out.println("🗑 Comment deleted");
				break;

			case 6:
				log.info("Share Post selected by userId=" + user.getUserId());

				System.out.print("Post ID: ");
				int sharePostId = readInt(sc);

				shareDao.sharePost(user.getUserId(), sharePostId);

				log.info("Post shared successfully. postId=" + sharePostId
						+ ", userId=" + user.getUserId());

				System.out.println("🔁 Post shared");
				break;

			case 7:
				log.info("Exiting Social menu for userId=" + user.getUserId());
				return;

			default:
				log.error("Invalid social menu option: " + choice
						+ " selected by userId=" + user.getUserId());
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
		        log.info("Send Connection Request selected by userId=" + user.getUserId());

		        System.out.print("Enter User ID: ");
		        int target = readInt(sc);

		        boolean sent = connectionDao.sendRequest(
		                user.getUserId(), target);

		        if (sent) {
		            notificationService.notify(
		                    target, "New connection request");

		            log.info("Connection request sent successfully. fromUser="
		                    + user.getUserId() + ", toUser=" + target);

		            System.out.println("📨 Request sent successfully");
		        } else {
		            log.warn("Failed to send connection request. fromUser="
		                    + user.getUserId() + ", toUser=" + target);

		            System.out.println("❌ Unable to send request");
		        }
		        break;
		    }

		    case 2: {
		        log.info("View Pending Requests selected by userId=" + user.getUserId());

		        List<String> pending =
		                connectionDao.getPendingRequests(user.getUserId());

		        if (pending.isEmpty()) {
		            log.info("No pending connection requests for userId="
		                    + user.getUserId());

		            System.out.println("ℹ No pending requests");
		        } else {
		            log.info("Found " + pending.size()
		                    + " pending requests for userId=" + user.getUserId());

		            System.out.println("=== Pending Requests ===");
		            for (String p : pending) {
		                System.out.println(p);
		            }
		        }
		        break;
		    }

		    case 3: {
		        log.info("View My Connections selected by userId=" + user.getUserId());

		        List<Integer> connections =
		                connectionDao.getConnections(user.getUserId());

		        if (connections.isEmpty()) {
		            log.info("User has no connections. userId=" + user.getUserId());

		            System.out.println("ℹ No connections yet");
		        } else {
		            log.info("User has " + connections.size()
		                    + " connections. userId=" + user.getUserId());

		            System.out.println("=== My Connections ===");
		            for (Integer id : connections) {
		                System.out.println("Connected User ID: " + id);
		            }
		        }
		        break;
		    }

		    case 4: {
		        log.info("Accept Connection Request selected by userId=" + user.getUserId());

		        System.out.print("Enter Requester ID: ");
		        int req = readInt(sc);

		        boolean accepted =
		                connectionDao.acceptRequest(req, user.getUserId());

		        if (accepted) {
		            notificationService.notify(
		                    req, "Your connection request was accepted");

		            log.info("Connection request accepted. fromUser="
		                    + req + ", toUser=" + user.getUserId());

		            System.out.println("✅ Connection accepted");
		        } else {
		            log.warn("Failed to accept connection request. fromUser="
		                    + req + ", toUser=" + user.getUserId());

		            System.out.println("❌ Unable to accept request");
		        }
		        break;
		    }

		    case 5: {
		        log.warn("Reject Connection Request selected by userId=" + user.getUserId());

		        System.out.print("Enter Requester ID: ");
		        int rid = readInt(sc);

		        boolean rejected =
		                connectionDao.rejectRequest(rid, user.getUserId());

		        if (rejected) {
		            log.warn("Connection request rejected. fromUser="
		                    + rid + ", toUser=" + user.getUserId());

		            System.out.println("❌ Connection rejected");
		        } else {
		            log.warn("No connection request found to reject. fromUser="
		                    + rid + ", toUser=" + user.getUserId());

		            System.out.println("⚠ No such request found");
		        }
		        break;
		    }

		    case 6:
		        log.info("Exiting Network menu for userId=" + user.getUserId());
		        return;

		    default:
		        log.error("Invalid network menu option selected: " + choice
		                + ", userId=" + user.getUserId());
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
		        log.info("View Personalized Feed selected by userId=" + user.getUserId());

		        List<Post> feed =
		                feedDao.getPersonalizedFeed(user.getUserId());

		        if (feed.isEmpty()) {
		            log.info("Personalized feed is empty for userId="
		                    + user.getUserId());
		            System.out.println("ℹ Feed is empty");
		        } else {
		            log.info("Loaded " + feed.size()
		                    + " feed posts for userId=" + user.getUserId());
		            for (Post p : feed) {
		                System.out.println(p.getPostId() + " | "
		                        + p.getContent());
		            }
		        }
		        break;

		    case 2:
		        log.info("View Trending Posts selected by userId=" + user.getUserId());

		        List<Post> trending =
		                feedDao.getTrendingPosts();

		        if (trending.isEmpty()) {
		            log.warn("No trending posts available");
		        } else {
		            log.info("Loaded " + trending.size() + " trending posts");
		            for (Post p : trending) {
		                System.out.println(p.getPostId() + " | "
		                        + p.getContent());
		            }
		        }
		        break;

		    case 3:
		        log.info("Search Posts by Hashtag selected by userId="
		                + user.getUserId());

		        System.out.print("Hashtag: ");
		        String hashtag = sc.nextLine();

		        List<Post> hashtagPosts =
		                feedDao.searchByHashtag(hashtag);

		        if (hashtagPosts.isEmpty()) {
		            log.warn("No posts found for hashtag #" + hashtag);
		        } else {
		            log.info("Found " + hashtagPosts.size()
		                    + " posts for hashtag #" + hashtag);
		            for (Post p : hashtagPosts) {
		                System.out.println(p.getPostId() + " | "
		                        + p.getContent());
		            }
		        }
		        break;

		    case 4:
		        log.info("Filter Feed by User Type selected by userId="
		                + user.getUserId());

		        System.out.print("Type: ");
		        String type = sc.nextLine();

		        List<Post> filtered =
		                feedDao.filterFeed(user.getUserId(), type);

		        if (filtered.isEmpty()) {
		            log.warn("No feed posts found for type=" + type
		                    + ", userId=" + user.getUserId());
		        } else {
		            log.info("Found " + filtered.size()
		                    + " feed posts for type=" + type
		                    + ", userId=" + user.getUserId());
		            for (Post p : filtered) {
		                System.out.println(p.getPostId() + " | "
		                        + p.getContent());
		            }
		        }
		        break;

		    case 5:
		        log.info("Exiting Feed & Discovery menu for userId="
		                + user.getUserId());
		        return;

		    default:
		        log.error("Invalid Feed menu option selected: " + choice
		                + ", userId=" + user.getUserId());
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
		        log.info("View UNREAD notifications selected by userId="
		                + user.getUserId());

		        List<Notification> unread =
		                notificationService.getUnread(user.getUserId());

		        if (unread.isEmpty()) {
		            log.info("No unread notifications for userId="
		                    + user.getUserId());
		            System.out.println("ℹ No unread notifications");
		        } else {
		            log.info("Unread notifications count="
		                    + unread.size() + " for userId="
		                    + user.getUserId());

		            for (Notification n : unread) {
		                System.out.println(n.getNotificationId() + " | "
		                        + n.getMessage());
		            }
		        }
		        break;

		    case 2:
		        log.info("View ALL notifications selected by userId="
		                + user.getUserId());

		        List<Notification> all =
		                notificationService.getAll(user.getUserId());

		        if (all.isEmpty()) {
		            log.info("No notifications found for userId="
		                    + user.getUserId());
		            System.out.println("ℹ No notifications");
		        } else {
		            log.info("Total notifications count="
		                    + all.size() + " for userId="
		                    + user.getUserId());

		            for (Notification n : all) {
		                System.out.println(n.getNotificationId() + " | "
		                        + n.getMessage() + " | "
		                        + (n.isRead() ? "READ" : "UNREAD"));
		            }
		        }
		        break;

		    case 3:
		        log.info("Mark notification as READ selected by userId="
		                + user.getUserId());

		        System.out.print("Notification ID: ");
		        int nid = readInt(sc);

		        try {
		            notificationService.markRead(nid);

		            log.info("Notification marked as READ. notificationId="
		                    + nid + ", userId=" + user.getUserId());

		            System.out.println("✅ Marked as read");

		        } catch (Exception e) {

		            log.error("Error marking notification as READ. notificationId="
		                    + nid + ", userId=" + user.getUserId(), e);

		            System.out.println("❌ Unable to mark as read");
		        }
		        break;


		    case 4:
		        log.info("Exiting Notifications menu for userId="
		                + user.getUserId());
		        return;

		    default:
		        log.error("Invalid Notifications menu option selected: "
		                + choice + ", userId=" + user.getUserId());
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
			if (!input.isEmpty())
				return input;
			System.out.print("❌ Input cannot be empty: ");
		}
	}
}
