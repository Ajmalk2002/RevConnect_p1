package com.revconnect.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.revconnect.config.DBConnection;

public class LikeDaoImpl implements LikeDao {

    @Override
    public boolean likePost(int userId, int postId) {

        Connection c = null;
        PreparedStatement ps = null;

        try {
            c = DBConnection.getConnection();
            ps = c.prepareStatement(
                "INSERT INTO likes (like_id, post_id, user_id) " +
                "VALUES (like_seq.NEXTVAL, ?, ?)"
            );

            ps.setInt(1, postId);
            ps.setInt(2, userId);

            ps.executeUpdate();
            return true; // ✅ like successful

        } catch (SQLException e) {

            // ORA-00001 → UNIQUE constraint violated (already liked)
            if (e.getErrorCode() == 1) {
                return false; // ❌ duplicate like
            }

            throw new RuntimeException("Error while liking post", e);

        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (c != null) c.close(); } catch (Exception e) {}
        }
    }

    @Override
    public boolean unlikePost(int userId, int postId) {

        Connection c = null;
        PreparedStatement ps = null;

        try {
            c = DBConnection.getConnection();
            ps = c.prepareStatement(
                "DELETE FROM likes WHERE post_id=? AND user_id=?"
            );

            ps.setInt(1, postId);
            ps.setInt(2, userId);

            int rows = ps.executeUpdate();
            return rows > 0; // ✅ true if something deleted

        } catch (Exception e) {
            throw new RuntimeException("Error while unliking post", e);

        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (c != null) c.close(); } catch (Exception e) {}
        }
    }
}
