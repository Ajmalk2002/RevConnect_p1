package com.revconnect.dao;

import java.sql.*;
import com.revconnect.config.DBConnection;

public class ShareDaoImpl implements ShareDao {

	public void sharePost(int userId, int postId) {
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement("INSERT INTO shares VALUES(share_seq.NEXTVAL,?,?,SYSDATE)");
			ps.setInt(1, postId);
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
