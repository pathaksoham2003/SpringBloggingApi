package com.soham.blog.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
public class RegisterUserRequest {
    @NotBlank(message = "Full name required parameter.")
    private String fullName;
    @Email(message = "Username should be valid.")
    private String username;
    @NotNull(message = "Password required parameter.")
    private String password;
    @NotNull(message = "Role required parameter.")
    private Integer role;
    @Override
    public String toString() {
        return "RegisterUserRequest{" +
                "fullName='" + fullName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }
}
