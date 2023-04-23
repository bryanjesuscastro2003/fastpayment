package com.auth.authserviceV2.services;

import com.auth.authserviceV2.dto.AuthUserDto;
import com.auth.authserviceV2.dto.TokenDto;
import com.auth.authserviceV2.models.User;
import com.auth.authserviceV2.repository.UserRepository;
import com.auth.authserviceV2.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class AuthUserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtProvider jwtProvider;

    public User save(AuthUserDto dto){
        Optional<User> user = userRepository.findByUsername(dto.getUsername());
        if(user.isPresent())
             return null;
        String password = passwordEncoder.encode(dto.getPassword());
        User authUser = User.builder()
                .username(dto.getUsername())
                .password(password)
                .build();
        return userRepository.save(authUser);
    }

    public TokenDto login(AuthUserDto dto){
        Optional<User> user = userRepository.findByUsername(dto.getUsername());
        if(user.isEmpty())
            return null;
        if(passwordEncoder.matches(dto.getPassword(), user.get().getPassword()))
            return new TokenDto(jwtProvider.createToken(user.get()));
        return null;
    }

    public TokenDto validate(String token){
        if(!jwtProvider.validateToken(token))
            return null;
        String username = jwtProvider.getUsernameFromToken(token);
        if(userRepository.findByUsername(username).isEmpty())
            return null;
        return new TokenDto(token);
    }

}
