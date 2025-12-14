package com.microbank.authservice.auth;

import com.microbank.authservice.entities.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank
    @Size(min = 4, max = 25, message = "The username must be 5 to 25 characters long.")
    private String username;

    @Size(min = 4, max = 30, message = "The surname must be 5 to 30 characters long.")
    private String lastname;

    @Size(min = 4, max = 30, message = "The name must be 5 to 30 characters long.")
    private String firstname;

    @Size(max = 25, message = "The country cannot exceed 25 characters.")
    private String country;

    @NotBlank
    @Size(min = 5, max = 30, message = "The password must be 5 to 30 characters long.")
    private String password;

    private Role role;
}