package com.auth.authservice.adapters.out.services;

import com.auth.authservice.adapters.in.http.AuthRequest;
import com.auth.authservice.adapters.in.http.RegisterRequest;
import com.auth.authservice.adapters.out.http.AuthResponse;
import com.auth.authservice.adapters.out.models.User;
import com.auth.authservice.adapters.out.repository.UserRepository;
import com.auth.authservice.adapters.out.types.Role;
import com.netflix.discovery.converters.Auto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request){
        String jwt = null;
        try{
            if(request.getEmail()==null || request.getUsername()==null || request.getPassword()==null)
                return AuthResponse.builder()
                        .ok(false)
                        .message("Invalid data")
                        .token(null)
                        .build();
            var user = User.builder() // TODO
                    .firstname(request.getFirstname())
                    .lastname(request.getLastname())
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.USER)
                    .build();
            userRepository.insert(user); // TODO
            jwt = jwtService.generateJwt(user);
        }catch (Exception e){}
        return AuthResponse.builder()
                .ok(!Objects.equals(jwt, null))
                .message(!Objects.equals(jwt, null) ? "Account created successfully" : "Unexpected error try again later")
                .token(jwt)
                .build();
    }

    public AuthResponse login(AuthRequest request){
        String jwt = null;
        String message;
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            var user = userRepository.findByUsername(request.getUsername());
            if(user.isPresent()){
                jwt = jwtService.generateJwt(user.get());
                message = "Login ok";
            }
            else message = "User not found";
        }catch (Exception e){
            message = null;
        }
        return AuthResponse.builder()
                .ok(!Objects.equals(jwt, null))
                .message(!Objects.equals(message, null) ? message : "Unexpected error try again later")
                .token(jwt)
                .build();
    }
}
