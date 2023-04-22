package com.auth.authservice.adapters.out.config;

import com.auth.authservice.adapters.out.services.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final ObjectMapper mapper;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        System.out.println(authHeader + " meme");
        String jwt = null;
        String username = null;
        boolean ok;
        if(authHeader == null || !authHeader.startsWith("Bearer "))
            ok = false;
        else {
            jwt = authHeader.substring(7);
            username = jwtService.extractUsername(jwt);
            ok = true;
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null && ok){
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if(jwtService.isJwtValid(jwt, userDetails)){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
            else{
                System.out.println("Estamos mal ");
                HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                Map<String, Object> errorDetails = new HashMap<>();
                errorDetails.put("message", "Invalid token");
                httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
                httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
                mapper.writeValue(httpServletResponse.getWriter(), errorDetails);
            }
        }
        filterChain.doFilter(request, response);
    }
}
