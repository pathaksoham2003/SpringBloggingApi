package com.soham.blog.service;

import com.soham.blog.dto.CommonPaginationRequest;
import com.soham.blog.dto.CreateBlogRequest;
import com.soham.blog.dto.UpdateBlogRequest;
import com.soham.blog.jpa.BlogRepository;
import com.soham.blog.model.Blog;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@CacheConfig(cacheNames = "blogs")
public class BlogService {

    @Autowired
    private BlogRepository blogRepository;

    public Blog createBlog(CreateBlogRequest createBlogRequest) {
        Blog blog = new Blog();
        BeanUtils.copyProperties(createBlogRequest,blog);
        blog.setCreatedAt(LocalDateTime.now());
        blog.setUpdatedAt(LocalDateTime.now());
        return blogRepository.save(blog);
    }

    @CachePut(key = "#updateBlogRequest.blogId")
    public Blog updateBlog(UpdateBlogRequest updateBlogRequest) {
        Blog blog = new Blog();
        BeanUtils.copyProperties(updateBlogRequest,blog);
        blog.setUpdatedAt(LocalDateTime.now());
        return blogRepository.save(blog);
    }

    @CacheEvict(key = "#blogId")
    public Blog deleteBlog(String blogId) {
        return blogRepository.deleteByBlogId(blogId);
    }

    @Cacheable(key = "#blogId")
    public Blog getBlog(String blogId) {

        return blogRepository.findByBlogId(blogId);
    }

    public List<Blog> getBlogsByUserId(CommonPaginationRequest commonPaginationRequest) throws Exception {
        Pageable pageable = PageRequest.of(commonPaginationRequest.getPageNo(),
                commonPaginationRequest.getPageSize(),
                Sort.by(commonPaginationRequest.getSortBy())
                        .descending());
        return blogRepository.findByUserId(commonPaginationRequest.getValue(), pageable);
    }
}
