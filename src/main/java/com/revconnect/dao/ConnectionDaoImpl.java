package com.revconnect.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.revconnect.config.DBConnection;

public class ConnectionDaoImpl implements ConnectionDao {

    @Override
    public boolean sendRequest(int fromUser, int toUser) {

        if (fromUser == toUser) return false;

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getConnection();

            ps = con.prepareStatement(
                "SELECT status FROM connections WHERE requester_id=? AND receiver_id=?");
            ps.setInt(1, toUser);
            ps.setInt(2, fromUser);
            rs = ps.executeQuery();

            if (rs.next()) {
                if ("PENDING".equals(rs.getString("status"))) {
                    ps.close();
                    ps = con.prepareStatement(
                        "UPDATE connections SET status='ACCEPTED' WHERE requester_id=? AND receiver_id=?");
                    ps.setInt(1, toUser);
                    ps.setInt(2, fromUser);
                    ps.executeUpdate();
                    return true;
                }
                return false;
            }

            rs.close();
            ps.close();

            ps = con.prepareStatement(
                "SELECT 1 FROM connections WHERE requester_id=? AND receiver_id=?");
            ps.setInt(1, fromUser);
            ps.setInt(2, toUser);
            rs = ps.executeQuery();

            if (rs.next()) return false;

            rs.close();
            ps.close();

            ps = con.prepareStatement(
                "INSERT INTO connections (requester_id, receiver_id, status) VALUES (?, ?, 'PENDING')");
            ps.setInt(1, fromUser);
            ps.setInt(2, toUser);
            ps.executeUpdate();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }

    @Override
    public boolean acceptRequest(int fromUser, int toUser) {

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(
                "UPDATE connections SET status='ACCEPTED' WHERE requester_id=? AND receiver_id=?");
            ps.setInt(1, fromUser);
            ps.setInt(2, toUser);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            return false;
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }

    @Override
    public boolean rejectRequest(int fromUser, int toUser) {

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(
                "DELETE FROM connections WHERE requester_id=? AND receiver_id=?");
            ps.setInt(1, fromUser);
            ps.setInt(2, toUser);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            return false;
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }

    @Override
    public boolean removeConnection(int userId, int otherUser) {

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(
                "DELETE FROM connections WHERE (requester_id=? AND receiver_id=?) OR (requester_id=? AND receiver_id=?)");

            ps.setInt(1, userId);
            ps.setInt(2, otherUser);
            ps.setInt(3, otherUser);
            ps.setInt(4, userId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            return false;
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }

    @Override
    public List<String> getPendingRequests(int userId) {

        List<String> list = new ArrayList<String>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(
                "SELECT u.user_id, u.email FROM connections c JOIN users u ON c.requester_id=u.user_id WHERE c.receiver_id=? AND c.status='PENDING'");
            ps.setInt(1, userId);
            rs = ps.executeQuery();

            while (rs.next()) {
                list.add("User ID: " + rs.getInt(1) +
                         " | Email: " + rs.getString(2));
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

    @Override
    public List<Integer> getConnections(int userId) {

        List<Integer> list = new ArrayList<Integer>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(
                "SELECT CASE WHEN requester_id=? THEN receiver_id ELSE requester_id END FROM connections WHERE status='ACCEPTED' AND (requester_id=? OR receiver_id=?)");

            ps.setInt(1, userId);
            ps.setInt(2, userId);
            ps.setInt(3, userId);

            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(rs.getInt(1));
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

    @Override
    public List<String> getAllConnections(int userId) {

        List<String> list = new ArrayList<String>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(
                "SELECT u.user_id, u.email FROM connections c JOIN users u ON ((c.requester_id=? AND u.user_id=c.receiver_id) OR (c.receiver_id=? AND u.user_id=c.requester_id)) WHERE c.status='ACCEPTED'");
            ps.setInt(1, userId);
            ps.setInt(2, userId);

            rs = ps.executeQuery();
            while (rs.next()) {
                list.add("User ID: " + rs.getInt(1) +
                         " | Email: " + rs.getString(2));
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
