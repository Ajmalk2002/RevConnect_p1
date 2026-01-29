package com.revconnect.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.revconnect.config.DBConnection;
import com.revconnect.core.Post;

public class PostDaoImpl implements PostDao {

    public void createPost(Post post) {

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(
                "INSERT INTO POSTS (POST_ID, USER_ID, CONTENT, HASHTAGS) " +
                "VALUES (POST_SEQ.NEXTVAL, ?, ?, ?)"
            );

            ps.setInt(1, post.getUserId());
            ps.setString(2, post.getContent());
            ps.setString(3, post.getHashtags());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }

    public List<Post> getMyPosts(int userId) {

        List<Post> list = new ArrayList<Post>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(
                "SELECT POST_ID, CONTENT, HASHTAGS FROM POSTS WHERE USER_ID=?"
            );
            ps.setInt(1, userId);

            rs = ps.executeQuery();

            while (rs.next()) {
                Post p = new Post();
                p.setPostId(rs.getInt("POST_ID"));
                p.setUserId(userId);
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

    public void updatePost(Post post) {

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(
                "UPDATE POSTS SET CONTENT=?, HASHTAGS=? WHERE POST_ID=? AND USER_ID=?"
            );

            ps.setString(1, post.getContent());
            ps.setString(2, post.getHashtags());
            ps.setInt(3, post.getPostId());
            ps.setInt(4, post.getUserId());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }

    public void deletePost(int postId, int userId) {

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(
                "DELETE FROM POSTS WHERE POST_ID=? AND USER_ID=?"
            );

            ps.setInt(1, postId);
            ps.setInt(2, userId);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }

    public int getPostOwner(int postId) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(
                "SELECT USER_ID FROM POSTS WHERE POST_ID=?"
            );
            ps.setInt(1, postId);
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("USER_ID");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }

        return -1;
    }
}
