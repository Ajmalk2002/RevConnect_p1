package com.revconnect.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.revconnect.config.DBConnection;
import com.revconnect.core.Post;

public class FeedDaoImpl implements FeedDao {

    // 1️⃣ Personalized Feed
    public List<Post> getPersonalizedFeed(int userId) {

        List<Post> list = new ArrayList<Post>();

        String sql =
            "SELECT * FROM posts WHERE user_id = ? " +
            "OR user_id IN (SELECT connected_user FROM connections WHERE user_id = ?) " +
            "OR user_id IN (SELECT following_id FROM follows WHERE user_id = ?) " +
            "ORDER BY post_id DESC";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);

            ps.setInt(1, userId);
            ps.setInt(2, userId);
            ps.setInt(3, userId);

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
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }

        return list;
    }

    // 2️⃣ Trending Posts
    public List<Post> getTrendingPosts() {

        List<Post> list = new ArrayList<Post>();

        String sql =
            "SELECT p.* FROM posts p " +
            "LEFT JOIN likes l ON p.post_id = l.post_id " +
            "GROUP BY p.post_id, p.user_id, p.content, p.hashtags " +
            "ORDER BY COUNT(l.like_id) DESC";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

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
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }

        return list;
    }

    // 3️⃣ Search by hashtag
    public List<Post> searchByHashtag(String hashtag) {

        List<Post> list = new ArrayList<Post>();

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(
                "SELECT * FROM posts WHERE hashtags LIKE ?"
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
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }

        return list;
    }

    // 4️⃣ Filter feed by user type
    public List<Post> filterFeed(int userId, String userType) {

        List<Post> list = new ArrayList<Post>();

        String sql =
            "SELECT p.* FROM posts p JOIN users u ON p.user_id = u.user_id " +
            "WHERE u.user_type = ?";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

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
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }

        return list;
    }
}
