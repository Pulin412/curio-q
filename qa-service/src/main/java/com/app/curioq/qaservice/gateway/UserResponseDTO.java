package com.app.curioq.qaservice.gateway;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseDTO {
    private String response;

    private Long userId;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String role;
    private Set<String> followers;
    private Set<String> following;
}
