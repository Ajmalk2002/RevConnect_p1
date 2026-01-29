package com.revconnect.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.revconnect.config.DBConnection;
import com.revconnect.core.Post;

public class FeedDaoImpl implements FeedDao {

    // ================= 1. Personalized Feed =================
    public List<Post> getPersonalizedFeed(int userId) {

        List<Post> list = new ArrayList<Post>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql =
            "SELECT post_id, user_id, content, hashtags " +
            "FROM posts WHERE user_id = ? " +
            "OR user_id IN ( " +
            "   SELECT CASE " +
            "       WHEN requester_id = ? THEN receiver_id " +
            "       ELSE requester_id " +
            "   END " +
            "   FROM connections " +
            "   WHERE status = 'ACCEPTED' " +
            "   AND (requester_id = ? OR receiver_id = ?) " +
            ") " +
            "OR user_id IN ( " +
            "   SELECT followee_id FROM followers WHERE follower_id = ? " +
            ") " +
            "ORDER BY post_id DESC";

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);

            ps.setInt(1, userId);
            ps.setInt(2, userId);
            ps.setInt(3, userId);
            ps.setInt(4, userId);
            ps.setInt(5, userId);

            rs = ps.executeQuery();

            while (rs.next()) {
                Post p = new Post();
                p.setPostId(rs.getInt("POST_ID"));
                p.setUserId(rs.getInt("USER_ID"));
                p.setContent(rs.getString("CONTENT"));
                p.setHashtags(rs.getString("HASHTAGS"));
                list.add(p);
            }

        } catch (Exception e) {
            System.out.println("⚠ Unable to load personalized feed");
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }

        return list;
    }

    // ================= 2. Trending Posts =================
    public List<Post> getTrendingPosts() {

        List<Post> list = new ArrayList<Post>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        // ✅ Oracle-safe trending query
        String sql =
            "SELECT p.post_id, p.user_id, p.content, p.hashtags " +
            "FROM posts p " +
            "LEFT JOIN ( " +
            "   SELECT post_id, COUNT(*) cnt " +
            "   FROM likes " +
            "   GROUP BY post_id " +
            ") l ON p.post_id = l.post_id " +
            "ORDER BY NVL(l.cnt, 0) DESC, p.post_id DESC";

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Post p = new Post();
                p.setPostId(rs.getInt("POST_ID"));
                p.setUserId(rs.getInt("USER_ID"));
                p.setContent(rs.getString("CONTENT"));
                p.setHashtags(rs.getString("HASHTAGS"));
                list.add(p);
            }

        } catch (Exception e) {
            System.out.println("⚠ Unable to load trending posts");
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }

        return list;
    }

    // ================= 3. Search by Hashtag =================
    public List<Post> searchByHashtag(String hashtag) {

        List<Post> list = new ArrayList<Post>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(
                "SELECT post_id, user_id, content, hashtags " +
                "FROM posts WHERE hashtags LIKE ?"
            );
            ps.setString(1, "%" + hashtag + "%");

            rs = ps.executeQuery();

            while (rs.next()) {
                Post p = new Post();
                p.setPostId(rs.getInt("POST_ID"));
                p.setUserId(rs.getInt("USER_ID"));
                p.setContent(rs.getString("CONTENT"));
                p.setHashtags(rs.getString("HASHTAGS"));
                list.add(p);
            }

        } catch (Exception e) {
            System.out.println("⚠ Error searching hashtag");
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }

        return list;
    }

    // ================= 4. Filter Feed by User Type =================
    public List<Post> filterFeed(int userId, String userType) {

        List<Post> list = new ArrayList<Post>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql =
        	    "SELECT p.post_id, p.user_id, p.content, p.hashtags " +
        	    "FROM posts p " +
        	    "JOIN users u ON p.user_id = u.user_id " +
        	    "WHERE UPPER(u.user_type) = UPPER(?) " +
        	    "ORDER BY p.post_id DESC";


        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, userType);
            rs = ps.executeQuery();

            while (rs.next()) {
                Post p = new Post();
                p.setPostId(rs.getInt("POST_ID"));
                p.setUserId(rs.getInt("USER_ID"));
                p.setContent(rs.getString("CONTENT"));
                p.setHashtags(rs.getString("HASHTAGS"));
                list.add(p);
            }

        } catch (Exception e) {
            System.out.println("⚠ Error filtering feed");
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }

        return list;
    }
}
