package com.soham.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soham.blog.dto.UpdateBlogRequest;
import com.soham.blog.model.Blog;
import com.soham.blog.service.BlogService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

// LOMBOK ERROR https://youtube.com/shorts/mNs0yKf6K0Q?si=AnP33YB5DqrkpKb9
@ExtendWith(MockitoExtension.class)
@WebMvcTest(value = BlogController.class)
public class BlogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BlogService blogService;

//    @Test
    public void testUpdateBlogValidationFailed() throws Exception {

        UpdateBlogRequest updateBlogRequest = new UpdateBlogRequest();
        updateBlogRequest.setBlogId("");
        updateBlogRequest.setTitle("Spring Boot Blog Title.");
        updateBlogRequest.setDescription("Spring Boot Blog Description.");
        updateBlogRequest.setUserId("1001");
        updateBlogRequest.setPublish(true);


        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/v1/blogs")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateBlogRequest))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals("{\"message\":\"blogId is required parameter.\"}", response.getContentAsString());

    }

//    @Test
    public void testUpdateBlogRecordNotFound() throws Exception {

        UpdateBlogRequest updateBlogRequest = new UpdateBlogRequest();
        updateBlogRequest.setBlogId("101");
        updateBlogRequest.setTitle("Spring Boot Blog Title.");
        updateBlogRequest.setDescription("Spring Boot Blog Description.");
        updateBlogRequest.setUserId("1001");
        updateBlogRequest.setPublish(true);

        when(blogService.updateBlog(any(UpdateBlogRequest.class))).thenReturn(null);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/v1/blogs")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateBlogRequest))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
//        assertEquals("{\"message\":\"Record not present in database.\"}", response.getContentAsString());

    }


//    @Test
    public void testUpdateBlogRecordSuccess() throws Exception {
        UpdateBlogRequest updateBlogRequest = new UpdateBlogRequest();
        updateBlogRequest.setBlogId("101");
        updateBlogRequest.setTitle("Spring Boot Blog Title.");
        updateBlogRequest.setDescription("Spring Boot Blog Description.");
        updateBlogRequest.setUserId("1001");
        updateBlogRequest.setPublish(true);
        when(blogService.updateBlog(any(UpdateBlogRequest.class))).thenReturn(new Blog());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/v1/blogs")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateBlogRequest))
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }
}