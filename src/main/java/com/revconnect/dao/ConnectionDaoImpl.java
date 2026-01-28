package com.revconnect.dao;

import java.sql.*;
import java.util.*;

import com.revconnect.config.DBConnection;

public class ConnectionDaoImpl implements ConnectionDao {

    public void sendRequest(int fromUser, int toUser) {
        Connection c=null; PreparedStatement ps=null;
        try {
            c=DBConnection.getConnection();
            ps=c.prepareStatement(
              "INSERT INTO connections VALUES(?, ?, 'PENDING')");
            ps.setInt(1, fromUser);
            ps.setInt(2, toUser);
            ps.executeUpdate();
        } catch(Exception e){ e.printStackTrace(); }
        finally {
            try{ if(ps!=null) ps.close(); }catch(Exception e){}
            try{ if(c!=null) c.close(); }catch(Exception e){}
        }
    }

    public void acceptRequest(int fromUser, int toUser) {
        Connection c=null; PreparedStatement ps=null;
        try {
            c=DBConnection.getConnection();
            ps=c.prepareStatement(
              "UPDATE connections SET status='ACCEPTED' WHERE requester_id=? AND receiver_id=?");
            ps.setInt(1, fromUser);
            ps.setInt(2, toUser);
            ps.executeUpdate();
        } catch(Exception e){ e.printStackTrace(); }
        finally {
            try{ if(ps!=null) ps.close(); }catch(Exception e){}
            try{ if(c!=null) c.close(); }catch(Exception e){}
        }
    }

    public void rejectRequest(int fromUser, int toUser) {
        Connection c=null; PreparedStatement ps=null;
        try {
            c=DBConnection.getConnection();
            ps=c.prepareStatement(
              "DELETE FROM connections WHERE requester_id=? AND receiver_id=?");
            ps.setInt(1, fromUser);
            ps.setInt(2, toUser);
            ps.executeUpdate();
        } catch(Exception e){ e.printStackTrace(); }
        finally {
            try{ if(ps!=null) ps.close(); }catch(Exception e){}
            try{ if(c!=null) c.close(); }catch(Exception e){}
        }
    }

    public List<Integer> getPendingRequests(int userId) {
        List<Integer> list=new ArrayList<Integer>();
        Connection c=null; PreparedStatement ps=null; ResultSet rs=null;
        try {
            c=DBConnection.getConnection();
            ps=c.prepareStatement(
              "SELECT requester_id FROM connections WHERE receiver_id=? AND status='PENDING'");
            ps.setInt(1, userId);
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

    public List<Integer> getConnections(int userId) {
        List<Integer> list=new ArrayList<Integer>();
        Connection c=null; PreparedStatement ps=null; ResultSet rs=null;
        try {
            c=DBConnection.getConnection();
            ps=c.prepareStatement(
              "SELECT requester_id FROM connections WHERE receiver_id=? AND status='ACCEPTED'");
            ps.setInt(1, userId);
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

    public void removeConnection(int userId, int otherUser) {
        Connection c=null; PreparedStatement ps=null;
        try {
            c=DBConnection.getConnection();
            ps=c.prepareStatement(
              "DELETE FROM connections WHERE (requester_id=? AND receiver_id=?) OR (requester_id=? AND receiver_id=?)");
            ps.setInt(1, userId);
            ps.setInt(2, otherUser);
            ps.setInt(3, otherUser);
            ps.setInt(4, userId);
            ps.executeUpdate();
        } catch(Exception e){ e.printStackTrace(); }
        finally {
            try{ if(ps!=null) ps.close(); }catch(Exception e){}
            try{ if(c!=null) c.close(); }catch(Exception e){}
        }
    }
}
