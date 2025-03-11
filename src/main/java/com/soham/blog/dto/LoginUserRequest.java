package com.soham.blog.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
public class LoginUserRequest {

    @NotBlank(message = "Username required parameter.")
    private String username;
    @NotBlank(message = "Password required parameter.")
    private String password;

    private int isSocialRegister;

    @Override
    public String toString() {
        return "LoginUserRequest{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", isSocialRegister=" + isSocialRegister +
                '}';
    }

}
