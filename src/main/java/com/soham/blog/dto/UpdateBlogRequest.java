package com.soham.blog.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UpdateBlogRequest {
    @NotBlank(message = "blogId is required parameter.")
    private String blogId;
    @NotBlank(message = "userId is required parameter.")
    private String userId;
    @NotBlank(message = "title is required parameter.")
    private String title;
    @NotBlank(message = "description is required parameter.")
    private String description;
    @NotNull(message = "publish is a required parameter.")
    private Boolean publish;
}
