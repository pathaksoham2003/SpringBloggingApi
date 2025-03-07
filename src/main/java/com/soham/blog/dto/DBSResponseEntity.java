package com.soham.blog.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
// The below annotation will not give the null values containing key-value pairs in the response to frontend.
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DBSResponseEntity<T> {
    private T data;
    private String message;

}
