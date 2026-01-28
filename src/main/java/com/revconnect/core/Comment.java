package com.revconnect.core;

import java.util.Date;

public class Comment {
 private int commentId;
 private int postId;
 private int userId;
 private String content;
 private Date createdAt;

 public int getCommentId(){ return commentId; }
 public void setCommentId(int id){ this.commentId=id; }

 public int getPostId(){ return postId; }
 public void setPostId(int id){ this.postId=id; }

 public int getUserId(){ return userId; }
 public void setUserId(int id){ this.userId=id; }

 public String getContent(){ return content; }
 public void setContent(String c){ this.content=c; }

 public Date getCreatedAt(){ return createdAt; }
 public void setCreatedAt(Date d){ this.createdAt=d; }
}
