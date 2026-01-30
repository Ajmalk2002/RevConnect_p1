package com.revconnect.dao;

import java.sql.*;
import java.util.*;
import com.revconnect.config.DBConnection;
import com.revconnect.model.Comment;

public class CommentDaoImpl implements CommentDao {

	// for adding comment

	public void addComment(Comment comment) {
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement("INSERT INTO comments VALUES(comment_seq.NEXTVAL,?,?,?,SYSDATE)");
			ps.setInt(1, comment.getPostId());
			ps.setInt(2, comment.getUserId());
			ps.setString(3, comment.getContent());
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps != null)
					ps.close();
			} catch (Exception e) {
			}
			try {
				if (con != null)
					con.close();
			} catch (Exception e) {
			}
		}
	}

	// to see the comments
	public List<Comment> getCommentsByPost(int postId) {
		List<Comment> list = new ArrayList<Comment>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement("SELECT * FROM comments WHERE post_id=?");
			ps.setInt(1, postId);
			rs = ps.executeQuery();
			while (rs.next()) {
				Comment c = new Comment();
				c.setCommentId(rs.getInt("COMMENT_ID"));
				c.setPostId(postId);
				c.setUserId(rs.getInt("USER_ID"));
				c.setContent(rs.getString("CONTENT"));
				c.setCreatedAt(rs.getDate("CREATED_AT"));
				list.add(c);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (ps != null)
					ps.close();
			} catch (Exception e) {
			}
			try {
				if (con != null)
					con.close();
			} catch (Exception e) {
			}
		}
		return list;
	}

	// for deleting comment
	public void deleteComment(int commentId, int userId) {
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement("DELETE FROM comments WHERE comment_id=? AND user_id=?");
			ps.setInt(1, commentId);
			ps.setInt(2, userId);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps != null)
					ps.close();
			} catch (Exception e) {
			}
			try {
				if (con != null)
					con.close();
			} catch (Exception e) {
			}
		}
	}
}
