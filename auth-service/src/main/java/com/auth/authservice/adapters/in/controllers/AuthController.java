package com.auth.authservice.adapters.in.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @PostMapping("/login")
    public String login(){
        return "login";
    }

    @PostMapping("/logup")
    public String logup(){
        return "logup";
    }
}
