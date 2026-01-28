package com.revconnect.dao;

import java.util.List;
import com.revconnect.core.Post;

public interface PostDao {
    void createPost(Post post);
    List<Post> getMyPosts(int userId);
    void updatePost(Post post);
    void deletePost(int postId, int userId);
}
