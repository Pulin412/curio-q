package com.app.curioq.userservice.userservice.model;

import com.app.curioq.userservice.userservice.enums.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseDTO {
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String role;
}
