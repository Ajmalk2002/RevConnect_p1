package com.revconnect.dao;

import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.revconnect.config.DBConnection;
import com.revconnect.core.Profile;

public class ProfileDaoImpl implements ProfileDao {

    // Uses stored procedure: SP_SAVE_PROFILE
    @Override
    public void saveOrUpdate(Profile p) {

        Connection con = null;
        CallableStatement cs = null;

        try {
            con = DBConnection.getConnection();
            cs = con.prepareCall("{ call SP_SAVE_PROFILE(?,?,?,?,?,?,?,?,?) }");

            cs.setInt(1, p.getUserId());
            cs.setString(2, p.getName());
            cs.setString(3, p.getBio());
            cs.setString(4, p.getLocation());
            cs.setString(5, p.getWebsite());
            cs.setString(6, p.getProfilePic());
            cs.setString(7, p.getCategory());
            cs.setString(8, p.getBusinessAddress());
            cs.setString(9, p.getContactInfo());

            cs.execute();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error saving profile", e); // 🔥 IMPORTANT
        } finally {
            try { if (cs != null) cs.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }

    // View profile from PROFILES table
    @Override
    public void viewProfile(int userId) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(
                "SELECT * FROM PROFILES WHERE USER_ID = ?"
            );

            ps.setInt(1, userId);
            rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("Name     : " + rs.getString("NAME"));
                System.out.println("Bio      : " + rs.getString("BIO"));
                System.out.println("Location : " + rs.getString("LOCATION"));
                System.out.println("Website  : " + rs.getString("WEBSITE"));
                System.out.println("Pic Path : " + rs.getString("PROFILE_PIC"));

                // Optional (Business / Creator)
                if (rs.getString("CATEGORY") != null)
                    System.out.println("Category : " + rs.getString("CATEGORY"));

                if (rs.getString("BUSINESS_ADDRESS") != null)
                    System.out.println("Address  : " + rs.getString("BUSINESS_ADDRESS"));

                if (rs.getString("CONTACT_INFO") != null)
                    System.out.println("Contact  : " + rs.getString("CONTACT_INFO"));
            } else {
                System.out.println("Profile not found.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error viewing profile", e);
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }
}
