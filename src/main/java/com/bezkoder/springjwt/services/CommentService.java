package com.bezkoder.springjwt.services;

import com.bezkoder.springjwt.models.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    List<Comment> findAll();
    Optional<Comment> findById(Long id);
    List<Comment> findCommentsByUsername(String username);
    List<Comment> findCommentsByBlogId(Long blogId);

    Comment createComment(Comment c);
    Comment updateComment(Long id, Comment c);
    void deleteComment(Long id);
    boolean exists(Long id);
}
