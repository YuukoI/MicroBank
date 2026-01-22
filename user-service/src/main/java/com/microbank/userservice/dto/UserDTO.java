package com.microbank.userservice.dtos;

import com.microbank.userservice.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private String username;

    private String firstName;

    private String lastName;

    private String country;

    private Role role;

}