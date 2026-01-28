package com.revconnect.dao;

import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.revconnect.config.DBConnection;
import com.revconnect.core.Profile;

public class ProfileDaoImpl implements ProfileDao {

    // Uses stored procedure: sp_save_profile
    public void saveOrUpdate(Profile p) {

        Connection con = null;
        CallableStatement cs = null;

        try {
            con = DBConnection.getConnection();
            cs = con.prepareCall("{ call sp_save_profile(?,?,?,?,?,?) }");

            cs.setInt(1, p.getUserId());
            cs.setString(2, p.getName());
            cs.setString(3, p.getBio());
            cs.setString(4, p.getLocation());
            cs.setString(5, p.getWebsite());
            cs.setString(6, p.getProfilePic());

            cs.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (cs != null) cs.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }

    // View profile from PROFILES table
    public void viewProfile(int userId) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(
                "SELECT * FROM profiles WHERE user_id=?"
            );

            ps.setInt(1, userId);
            rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("Name     : " + rs.getString("name"));
                System.out.println("Bio      : " + rs.getString("bio"));
                System.out.println("Location : " + rs.getString("location"));
                System.out.println("Website  : " + rs.getString("website"));
                System.out.println("Pic Path : " + rs.getString("profile_pic"));
            } else {
                System.out.println("Profile not found.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }
}
