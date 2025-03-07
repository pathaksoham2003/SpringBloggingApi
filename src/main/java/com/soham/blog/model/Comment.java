package com.soham.blog.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "comments")
public class Comment {
    @Id
    private String commentId;
    private String title;
    private String description;
    private String blogId;
    private String userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
