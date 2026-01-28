package com.revconnect.dao;

import java.sql.*;
import java.util.*;

import com.revconnect.config.DBConnection;

public class FollowDaoImpl implements FollowDao {

    public void follow(int followerId, int followeeId) {
        Connection c=null; PreparedStatement ps=null;
        try {
            c=DBConnection.getConnection();
            ps=c.prepareStatement(
              "INSERT INTO followers VALUES(?,?)");
            ps.setInt(1, followerId);
            ps.setInt(2, followeeId);
            ps.executeUpdate();
        } catch(Exception e){ e.printStackTrace(); }
        finally {
            try{ if(ps!=null) ps.close(); }catch(Exception e){}
            try{ if(c!=null) c.close(); }catch(Exception e){}
        }
    }

    public void unfollow(int followerId, int followeeId) {
        Connection c=null; PreparedStatement ps=null;
        try {
            c=DBConnection.getConnection();
            ps=c.prepareStatement(
              "DELETE FROM followers WHERE follower_id=? AND followee_id=?");
            ps.setInt(1, followerId);
            ps.setInt(2, followeeId);
            ps.executeUpdate();
        } catch(Exception e){ e.printStackTrace(); }
        finally {
            try{ if(ps!=null) ps.close(); }catch(Exception e){}
            try{ if(c!=null) c.close(); }catch(Exception e){}
        }
    }

    public List<Integer> getFollowers(int userId) {
        return fetchList(
            "SELECT follower_id FROM followers WHERE followee_id=?", userId);
    }

    public List<Integer> getFollowing(int userId) {
        return fetchList(
            "SELECT followee_id FROM followers WHERE follower_id=?", userId);
    }

    private List<Integer> fetchList(String sql, int id) {
        List<Integer> list=new ArrayList<Integer>();
        Connection c=null; PreparedStatement ps=null; ResultSet rs=null;
        try {
            c=DBConnection.getConnection();
            ps=c.prepareStatement(sql);
            ps.setInt(1, id);
            rs=ps.executeQuery();
            while(rs.next())
                list.add(rs.getInt(1));
        } catch(Exception e){ e.printStackTrace(); }
        finally {
            try{ if(rs!=null) rs.close(); }catch(Exception e){}
            try{ if(ps!=null) ps.close(); }catch(Exception e){}
            try{ if(c!=null) c.close(); }catch(Exception e){}
        }
        return list;
    }
}
