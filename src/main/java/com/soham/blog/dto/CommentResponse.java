package com.soham.blog.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CommentResponse {
    @Id
    private ObjectId commentId;
    private String title;
    private String description;
    private ObjectId blogId;
    private ObjectId userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
