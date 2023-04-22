package com.auth.authservice.adapters.out.http;

import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse{
    private Boolean ok;
    private String message;
    private String token;

}
