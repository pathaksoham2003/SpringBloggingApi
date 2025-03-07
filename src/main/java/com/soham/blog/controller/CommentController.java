package com.soham.blog.controller;

import com.soham.blog.dto.CommonPaginationRequest;
import com.soham.blog.dto.CreateCommentRequest;
import com.soham.blog.dto.DBSResponseEntity;
import com.soham.blog.model.Comment;
import com.soham.blog.service.CommentService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/comments")
    public ResponseEntity getComments(@RequestParam String blogId,
                                      @RequestParam(defaultValue = "10") Integer pageSize,
                                      @RequestParam(defaultValue = "0") Integer pageNumber,
                                      @RequestParam(defaultValue = "id") String sortBy) {
        DBSResponseEntity dbsResponseEntity = new DBSResponseEntity();
        CommonPaginationRequest commonPaginationRequest = new CommonPaginationRequest();
//        commonPaginationRequest.setPage(0);
//        commonPaginationRequest.setSize(10);

        try {
//            List<Comment> comments = commentService.getCommentsByBlogId(blogId);
            return ResponseEntity.ok(dbsResponseEntity);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    @PostMapping("/comments")
    public ResponseEntity createComment(@RequestBody CreateCommentRequest createCommentRequest) {
        DBSResponseEntity dbsResponseEntity = new DBSResponseEntity();
        try{
            Comment comment = commentService.createComment(createCommentRequest);
            dbsResponseEntity.setData(comment);
            dbsResponseEntity.setMessage("The Comment was successfully created");
            return ResponseEntity.ok(dbsResponseEntity);
        }catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
