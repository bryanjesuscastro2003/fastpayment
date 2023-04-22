package com.auth.authservice.adapters.in.http;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthRequest {
    private String username;
    private String password;
}
