package com.bezkoder.springjwt.services;

import com.bezkoder.springjwt.models.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bezkoder.springjwt.repository.CommentsRepo;

import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImplementation implements CommentService {
    @Autowired
    private final CommentsRepo commentsRepo;

    public CommentServiceImplementation(CommentsRepo commentsRepo){
        this.commentsRepo = commentsRepo;
    }

    @Override
    public List<Comment> findAll(){
        return commentsRepo.findAll();
    }

    @Override
    public Optional<Comment> findById(Long id) {
        return commentsRepo.findById(id); }

    @Override
    public List<Comment> findCommentsByUsername(String username) {
        return commentsRepo.findCommentsByUsername(username); }

    @Override
    public List<Comment> findCommentsByBlogId(Long blogId){
        return commentsRepo.findCommentsByBlogId(blogId);
    }

    @Override
    public Comment createComment(Comment c){
        return commentsRepo.save(c);
    }

    @Override
    public Comment updateComment(Long id, Comment c){
        Optional<Comment> blog = findById(id);
        if(blog.isPresent()){
            blog.get().setBody(c.getBody());
            blog.get().setUsername(c.getUsername());
            blog.get().setBlogId(c.getBlogId());
            return commentsRepo.save(blog.get());
        }
        else{
            return null;
        }
    }

    @Override
    public void deleteComment(Long id){
        commentsRepo.deleteById(id);
    }

    @Override
    public boolean exists(Long id){
        return commentsRepo.existsById(id);
    }
}
