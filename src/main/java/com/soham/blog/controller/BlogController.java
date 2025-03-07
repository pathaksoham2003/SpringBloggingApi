package com.soham.blog.controller;

import com.soham.blog.dto.CommonPaginationRequest;
import com.soham.blog.dto.CreateBlogRequest;
import com.soham.blog.dto.DBSResponseEntity;
import com.soham.blog.dto.UpdateBlogRequest;
import com.soham.blog.exception.RecordNotFoundException;
import com.soham.blog.model.Blog;
import com.soham.blog.service.BlogService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping("/v1")
@Slf4j
public class BlogController {
    @Autowired
    private BlogService blogService;

    @PostMapping("/blogs")
    public ResponseEntity
    createBlog(@Valid @RequestBody CreateBlogRequest createBlogRequest) {
        DBSResponseEntity dbsResponseEntity = new DBSResponseEntity();
        log.info("BlogController:createBlog request recieved with body : {}", createBlogRequest.toString());
        try{
            Blog createdBlog = blogService.createBlog(createBlogRequest);
            log.info("BlogService:updateBlog record save successfully with blogId : {}", createdBlog.getBlogId());
            dbsResponseEntity.setData(createdBlog);
            dbsResponseEntity.setMessage("The Blog was successfully created");
            return ResponseEntity.ok(dbsResponseEntity);
        }catch (Exception e) {
            log.debug("BlogController:createBlog error occured while creating blog : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PutMapping("/blogs")
    public ResponseEntity<DBSResponseEntity<Blog>> updateBlog(@Valid @RequestBody UpdateBlogRequest updateBlogRequest) throws Exception{
        DBSResponseEntity<Blog> dbsResponseEntity = new DBSResponseEntity<Blog>();
        log.info("BlogController:updateBlog request recieved with body : {}", updateBlogRequest.toString());
        try{
            Blog foundBlog = blogService.getBlog(updateBlogRequest.getBlogId());
            if(foundBlog == null) throw new RecordNotFoundException("Blog not found");
            log.info("BlogController:updateBlog blog found with blogId : {}", foundBlog.getBlogId());
            Blog updatedBlog = blogService.updateBlog(updateBlogRequest);
            log.info("BlogController:updateBlog blog updated successfully with blogId : {}", updatedBlog.getBlogId());
            dbsResponseEntity.setMessage("Blog Updated Successfully");
            dbsResponseEntity.setData(updatedBlog);
            return ResponseEntity.ok(dbsResponseEntity);
        }
        catch (RecordNotFoundException exception) {
            log.debug("BlogController:updateBlog blog with the blogId not found : {}", exception.getMessage());
            throw exception;
        }
        catch(Exception e) {
            log.debug("BlogController:updateBlog error occured while updating blog : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/blogs/{blogId}")
    public ResponseEntity getSpecificBlog(@PathVariable String blogId) {
        try{
            log.info("BlogController:getSpecificBlog request recieved with blogId : {}", blogId);
            DBSResponseEntity dbsResponseEntity = new DBSResponseEntity();
            Blog blog = blogService.getBlog(blogId);
            if (blog == null) throw new RecordNotFoundException("Blog not found");
            dbsResponseEntity.setData(blog);
            dbsResponseEntity.setMessage("Blog was successfully fetched");
            return ResponseEntity.ok(dbsResponseEntity);
        } catch(RecordNotFoundException exception) {
            log.debug("BlogController:getSpecificBlog blog with the blogId not found : {}", exception.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.debug("BlogController:getSpecificBlog error occured while fetching blog : {}", e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/blogs/{blogId}")
    public ResponseEntity<DBSResponseEntity> deleteBlog(@PathVariable String blogId) {
        DBSResponseEntity dbsResponseEntity = new DBSResponseEntity();
        try{
            Blog blog = blogService.deleteBlog(blogId);
            dbsResponseEntity.setMessage("Blog successfully deleted");
            dbsResponseEntity.setData(blog);
            return ResponseEntity.ok(dbsResponseEntity);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/blogs")
    public ResponseEntity<DBSResponseEntity<List<Blog>>> getBlogs(
            @RequestParam(defaultValue = "0") Integer pageNo
            , @RequestParam(defaultValue = "10") Integer pageSize
            , @RequestParam(defaultValue = "id") String sortBy
            , @RequestParam(defaultValue = "1") String userId) {
        CommonPaginationRequest commonPaginationRequest = new CommonPaginationRequest();
        commonPaginationRequest.setPageNo(pageNo);
        commonPaginationRequest.setPageSize(pageSize);
        commonPaginationRequest.setSortBy(sortBy);
        commonPaginationRequest.setValue(userId);
        DBSResponseEntity<List<Blog>> dbsResponseEntity = new DBSResponseEntity<List<Blog>>();
        try{
            List<Blog> blogs = blogService.getBlogsByUserId(commonPaginationRequest);
            dbsResponseEntity.setData(blogs);
            dbsResponseEntity.setMessage("Blogs fetched successfully");
            return ResponseEntity.ok(dbsResponseEntity);
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
