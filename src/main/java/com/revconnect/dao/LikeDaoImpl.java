package com.revconnect.dao;

import java.sql.*;
import com.revconnect.config.DBConnection;

public class LikeDaoImpl implements LikeDao {

 public void likePost(int userId,int postId){
  Connection c=null; PreparedStatement ps=null;
  try{
   c=DBConnection.getConnection();
   ps=c.prepareStatement(
    "INSERT INTO likes VALUES(like_seq.NEXTVAL,?,?)");
   ps.setInt(1,postId);
   ps.setInt(2,userId);
   ps.executeUpdate();
  }catch(Exception e){
   System.out.println("Already liked or error");
  }finally{
   try{if(ps!=null)ps.close();}catch(Exception e){}
   try{if(c!=null)c.close();}catch(Exception e){}
  }
 }

 public void unlikePost(int userId,int postId){
  Connection c=null; PreparedStatement ps=null;
  try{
   c=DBConnection.getConnection();
   ps=c.prepareStatement(
    "DELETE FROM likes WHERE post_id=? AND user_id=?");
   ps.setInt(1,postId);
   ps.setInt(2,userId);
   ps.executeUpdate();
  }catch(Exception e){e.printStackTrace();}
  finally{
   try{if(ps!=null)ps.close();}catch(Exception e){}
   try{if(c!=null)c.close();}catch(Exception e){}
  }
 }
}
