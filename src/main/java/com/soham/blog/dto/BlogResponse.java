package com.soham.blog.dto;


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
public class BlogResponse {
    @Id
    private String blogId;
    private String userId;
    private String title;
    private String description;
    private boolean publish;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
