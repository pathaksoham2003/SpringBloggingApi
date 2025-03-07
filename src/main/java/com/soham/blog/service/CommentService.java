package com.soham.blog.service;


import com.soham.blog.dto.CreateCommentRequest;
import com.soham.blog.jpa.CommentRepository;
import com.soham.blog.model.Comment;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public Comment createComment(CreateCommentRequest createCommentRequest) {
        Comment comment = new Comment();
        BeanUtils.copyProperties(createCommentRequest,comment);
        return commentRepository.save(comment);
    }

    public Comment updateComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public Comment deleteComment(String commentId) {
        return commentRepository.deleteByCommentId(commentId);
    }

    public List<Comment> getCommentsByBlogId(String blogId, Pageable pageable) {
        return commentRepository.findByBlogId(blogId, pageable);
    }
}
