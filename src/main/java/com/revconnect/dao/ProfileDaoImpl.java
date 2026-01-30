package com.revconnect.dao;

import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.revconnect.config.DBConnection;
import com.revconnect.model.Profile;

public class ProfileDaoImpl implements ProfileDao {

	
	// save or update profile
	@Override
	public void saveOrUpdate(Profile profile) {

		Connection con = null;
		CallableStatement cs = null;

		try {
			con = DBConnection.getConnection();
			cs = con.prepareCall("{ call SP_SAVE_PROFILE(?,?,?,?,?,?,?,?,?) }");

			cs.setInt(1, profile.getUserId());
			cs.setString(2, profile.getName());
			cs.setString(3, profile.getBio());
			cs.setString(4, profile.getLocation());
			cs.setString(5, profile.getWebsite());
			cs.setString(6, profile.getProfilePic());
			cs.setString(7, profile.getCategory());
			cs.setString(8, profile.getBusinessAddress());
			cs.setString(9, profile.getContactInfo());

			cs.execute();

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error saving profile", e);
		} finally {
			try {
				if (cs != null)
					cs.close();
			} catch (Exception e) {
			}
			try {
				if (con != null)
					con.close();
			} catch (Exception e) {
			}
		}
	}
	
	// view profile

	@Override
	public void viewProfile(int userId) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement("SELECT * FROM PROFILES WHERE USER_ID = ?");
			ps.setInt(1, userId);
			rs = ps.executeQuery();

			if (rs.next()) {
				System.out.println("Name     : " + rs.getString("NAME"));
				System.out.println("Bio      : " + rs.getString("BIO"));
				System.out.println("Location : " + rs.getString("LOCATION"));
				System.out.println("Website  : " + rs.getString("WEBSITE"));

				if (rs.getString("CATEGORY") != null)
					System.out
							.println("Category : " + rs.getString("CATEGORY"));

				if (rs.getString("BUSINESS_ADDRESS") != null)
					System.out.println("Address  : "
							+ rs.getString("BUSINESS_ADDRESS"));

				if (rs.getString("CONTACT_INFO") != null)
					System.out.println("Contact  : "
							+ rs.getString("CONTACT_INFO"));
			} else {
				System.out.println("Profile not found.");
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error viewing profile", e);
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
	}

}
