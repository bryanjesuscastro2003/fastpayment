package com.auth.authserviceV2.security;

import com.auth.authserviceV2.models.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtProvider {
    @Value("${jwt.secret}")
    private String secret;

    @PostConstruct
    protected void init(){
       secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public String createToken(User user){
          Map<String, Object> claims = new HashMap<>();
          claims = Jwts.claims().setSubject(user.getUsername());
          claims.put("id", user.getId());
          Date now = new Date();
          Date exp = new Date(now.getTime() + 3600000);
          return Jwts.builder()
                  .setClaims(claims)
                  .setIssuedAt(now)
                  .setExpiration(exp)
                  .signWith(SignatureAlgorithm.HS256, secret)
                  .compact();
    }

    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJwt(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public String getUsernameFromToken(String token){
          try{
              return Jwts.parser().setSigningKey(secret).parseClaimsJwt(token).getBody().getSubject();
          }catch (Exception e){
              return "Bad token";
          }
    }

}
