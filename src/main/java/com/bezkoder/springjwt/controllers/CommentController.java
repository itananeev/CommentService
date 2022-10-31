package com.bezkoder.springjwt.controllers;

import com.bezkoder.springjwt.models.Comment;
import com.bezkoder.springjwt.payload.ResponseWithMessage;
import com.bezkoder.springjwt.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor

@RequestMapping("/api/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;


    @GetMapping
    public ResponseEntity<ResponseWithMessage<List<Comment>>> getAll(){
        List<Comment> comments;
        try {
            comments = commentService.findAll();
        } catch (DataAccessException e) {
            return new ResponseEntity<>(new ResponseWithMessage<>(null, "Posts repository not responding"), HttpStatus.SERVICE_UNAVAILABLE);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ResponseWithMessage<>(null, "Something went wrong..."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new ResponseWithMessage<>(comments, null), HttpStatus.OK);
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<ResponseWithMessage<Optional<Comment>>> getById(@PathVariable Long id){
        Optional<Comment> result;
        try {
            result = commentService.findById(id);
        } catch (DataAccessException e) {
            return new ResponseEntity<>(new ResponseWithMessage<>(null, "Blog repository not responding"), HttpStatus.SERVICE_UNAVAILABLE);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ResponseWithMessage<>(null, "Something went wrong..."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new ResponseWithMessage<>(result, null), HttpStatus.OK);
    }

    @GetMapping(params = "username")
    public ResponseEntity<ResponseWithMessage<List<Comment>>> findCommentsByUsername(@RequestParam String username){
        // TODO: Change this request to take the username from the cookie (case: corresponding user) or user has role admin
        List<Comment> results;
        try {
            results = commentService.findCommentsByUsername(username);
        } catch (DataAccessException e) {
            return new ResponseEntity<>(new ResponseWithMessage<>(null, "Comment repository not responding"), HttpStatus.SERVICE_UNAVAILABLE);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ResponseWithMessage<>(null, "Something went wrong..."), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(new ResponseWithMessage<>(results, null), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResponseWithMessage<Comment>> postComment(@CookieValue(name="bezkoder") String cookie, @RequestBody Comment comment){
        try {
            Comment newComment = commentService.createComment(comment);
            return new ResponseEntity<>(new ResponseWithMessage<>(newComment, "Comment successfully created"), HttpStatus.OK);
        } catch (DataAccessException e) {
            return new ResponseEntity<>(new ResponseWithMessage<>(null, "Comment repository not responding"), HttpStatus.SERVICE_UNAVAILABLE);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseWithMessage<>(null, "Something went wrong..."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(path="{id}/{b}")
    public ResponseEntity<ResponseWithMessage<Comment>> editComment(@PathVariable Long id, @RequestBody Comment c){
        // TODO: Authorize the user who created the answer is the same as this one requesting this action & exists in the database
        try {
            if(commentService.exists(id)) {
                Comment updatedComment = commentService.updateComment(id, c);
                return new ResponseEntity<>(new ResponseWithMessage<>(updatedComment, "Comment successfully updated"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ResponseWithMessage<>(null, "Comment not found"), HttpStatus.NOT_FOUND);
            }
        } catch (DataAccessException e) {
            return new ResponseEntity<>(new ResponseWithMessage<>(null, "Comment repository not responding"), HttpStatus.SERVICE_UNAVAILABLE);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new ResponseWithMessage<>(null, "Something went wrong..."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(path="{id}")
    public ResponseEntity<ResponseWithMessage<Comment>> deleteComment(@PathVariable Long id){
        // TODO: Authorize the user who created the answer is the same as this one requesting this action & exists in the database
        try {
            if(commentService.exists(id)) {
                commentService.deleteComment(id);
                return new ResponseEntity<>(new ResponseWithMessage<>(null, "Comment successfully deleted"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ResponseWithMessage<>(null, "Comment not found"), HttpStatus.NOT_FOUND);
            }
        } catch (DataAccessException e) {
            return new ResponseEntity<>(new ResponseWithMessage<>(null, "Comment repository not responding"), HttpStatus.SERVICE_UNAVAILABLE);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ResponseWithMessage<>(null, "Something went wrong..."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}