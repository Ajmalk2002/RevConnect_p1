package com.revconnect.dao;

import java.util.List;
import com.revconnect.core.Comment;

public interface CommentDao {
 void addComment(Comment c);
 List<Comment> getCommentsByPost(int postId);
 void deleteComment(int commentId,int userId);
}
