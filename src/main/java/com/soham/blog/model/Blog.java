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
@Document(collection = "blogs")
public class Blog {
    @Id
    private String blogId;
    private String userId;
    private String title;
    private String description;
    private boolean publish;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
