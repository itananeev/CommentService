package com.bezkoder.springjwt.repository;

import com.bezkoder.springjwt.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentsRepo extends JpaRepository<Comment, Long> {
    List<Comment> findAll();
    Optional<Comment> findById(Long id);
    List<Comment> findCommentsByUsername(String username);
    List<Comment> findCommentsByBlogId(Long blogId);

    @Override
    <S extends Comment> S save(S entity);
}
