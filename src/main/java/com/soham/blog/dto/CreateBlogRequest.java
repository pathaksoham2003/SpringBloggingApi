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
public class CreateBlogRequest {
    @NotBlank(message = "The userId is required")
    private String userId;
    @NotBlank(message = "The title is required")
    private String title;
    @NotBlank(message = "The description is required")
    private String description;
    @NotNull(message = "The publish is required")
    private boolean publish;
}
