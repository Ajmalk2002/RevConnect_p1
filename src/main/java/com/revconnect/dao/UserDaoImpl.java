package com.revconnect.dao;

import java.sql.*;

import com.revconnect.config.DBConnection;
import com.revconnect.model.User;
import com.revconnect.util.PasswordUtil;

public class UserDaoImpl implements UserDao {

    // ================= REGISTER =================
    @Override
    public int register(User user) {

        Connection con = null;
        CallableStatement cs = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getConnection();

            String hashedPassword =
                    PasswordUtil.hashPassword(user.getPassword());

            String hashedAnswer =
                    PasswordUtil.hashPassword(user.getSecurityAnswer());

            cs = con.prepareCall("{ call sp_register_user(?, ?, ?, ?, ?) }");

            cs.setString(1, user.getEmail());
            cs.setString(2, hashedPassword);
            cs.setString(3, user.getUserType().name());
            cs.setString(4, user.getSecurityQuestion());
            cs.setString(5, hashedAnswer);

            cs.execute();
            cs.close();

            ps = con.prepareStatement(
                "SELECT user_id FROM users WHERE email=?"
            );
            ps.setString(1, user.getEmail());
            rs = ps.executeQuery();

            if (rs.next()) {
                user.setUserId(rs.getInt("USER_ID"));
                return 1; // ✅ SUCCESS
            }

        } catch (SQLException e) {

            // ORA-00001 → duplicate email
            if (e.getErrorCode() == 1) {
                System.out.println("❌ Email already registered");
                return 0;
            }

            System.out.println("❌ Registration failed");
            e.printStackTrace();
            return 0;

        } catch (Exception e) {
            System.out.println("❌ Unexpected registration error");
            e.printStackTrace();
            return 0;

        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (cs != null) cs.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }

        return 0;
    }

    // ================= LOGIN =================
    @Override
    public User login(String email, String password) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getConnection();

            String hashedPassword =
                    PasswordUtil.hashPassword(password);

            ps = con.prepareStatement(
                "SELECT user_id, email, user_type FROM users " +
                "WHERE email=? AND password=?"
            );
            ps.setString(1, email);
            ps.setString(2, hashedPassword);

            rs = ps.executeQuery();

            if (rs.next()) {
                User u = new User();
                u.setUserId(rs.getInt("USER_ID"));
                u.setEmail(rs.getString("EMAIL"));
                u.setUserType(
                    Enum.valueOf(
                        com.revconnect.model.UserType.class,
                        rs.getString("USER_TYPE")
                    )
                );
                return u;
            }

        } catch (Exception e) {
            System.out.println("❌ Login failed");
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
        return null;
    }

    // ================= SEARCH USERS =================
    @Override
    public void searchUsers(String key) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(
                "SELECT user_id, email, user_type FROM users " +
                "WHERE LOWER(email) LIKE ?"
            );

            ps.setString(1, "%" + key.toLowerCase() + "%");
            rs = ps.executeQuery();

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println(
                    "User ID: " + rs.getInt("USER_ID") +
                    " | Email: " + rs.getString("EMAIL") +
                    " | Type: " + rs.getString("USER_TYPE")
                );
            }

            if (!found)
                System.out.println("No users found.");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }

    // ================= CHANGE PASSWORD (LOGGED-IN) =================
    @Override
    public boolean changePassword(
            int userId, String oldPassword, String newPassword) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getConnection();

            ps = con.prepareStatement(
                "SELECT password FROM users WHERE user_id=?"
            );
            ps.setInt(1, userId);
            rs = ps.executeQuery();

            if (!rs.next())
                return false;

            String dbHash = rs.getString("password");
            String oldHash =
                    PasswordUtil.hashPassword(oldPassword);

            if (!oldHash.equals(dbHash))
                return false;

            rs.close();
            ps.close();

            ps = con.prepareStatement(
                "UPDATE users SET password=? WHERE user_id=?"
            );
            ps.setString(1,
                PasswordUtil.hashPassword(newPassword));
            ps.setInt(2, userId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }

    // ================= FORGOT PASSWORD =================
    @Override
    public String getSecurityQuestion(String email) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(
                "SELECT security_question FROM users WHERE email=?"
            );
            ps.setString(1, email);
            rs = ps.executeQuery();

            if (rs.next())
                return rs.getString("security_question");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
        return null;
    }

    @Override
    public boolean resetPassword(
            String email, String securityAnswer, String newPassword) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getConnection();

            ps = con.prepareStatement(
                "SELECT security_answer FROM users WHERE email=?"
            );
            ps.setString(1, email);
            rs = ps.executeQuery();

            if (!rs.next())
                return false;

            String dbAnswer = rs.getString("security_answer");
            String inputHash =
                    PasswordUtil.hashPassword(securityAnswer);

            if (!inputHash.equals(dbAnswer))
                return false;

            rs.close();
            ps.close();

            ps = con.prepareStatement(
                "UPDATE users SET password=? WHERE email=?"
            );
            ps.setString(1,
                PasswordUtil.hashPassword(newPassword));
            ps.setString(2, email);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }
}
