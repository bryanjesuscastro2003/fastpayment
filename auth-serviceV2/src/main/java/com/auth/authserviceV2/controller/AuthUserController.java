package com.auth.authserviceV2.controller;

import com.auth.authserviceV2.dto.AuthUserDto;
import com.auth.authserviceV2.dto.TokenDto;
import com.auth.authserviceV2.models.User;
import com.auth.authserviceV2.services.AuthUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class AuthUserController {
    @Autowired
    private AuthUserService authUserService;

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody AuthUserDto dto){
         TokenDto tokenDto = authUserService.login(dto);
         if(tokenDto == null)
               return ResponseEntity.badRequest().build();
         return ResponseEntity.ok(tokenDto);
    }
    @PostMapping("/validate")
    public ResponseEntity<TokenDto> validate(@RequestParam String token){
         TokenDto tokenDto = authUserService.validate(token);
         if(tokenDto == null)
             return ResponseEntity.badRequest().build();
         return ResponseEntity.ok(tokenDto);
    }

    @PostMapping("/logup")
    public ResponseEntity<User> create(@RequestBody AuthUserDto dto){
         User authUser = authUserService.save(dto);
         if(authUser == null)
             return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(authUser);
    }

}


