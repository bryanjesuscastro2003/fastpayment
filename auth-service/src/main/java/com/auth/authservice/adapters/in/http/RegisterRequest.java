package com.auth.authservice.adapters.in.http;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RegisterRequest {
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private String password;
}
