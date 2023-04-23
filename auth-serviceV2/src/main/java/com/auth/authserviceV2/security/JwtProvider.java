package com.auth.authserviceV2.security;

import com.auth.authserviceV2.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import javax.annotation.PostConstruct;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtProvider {
    @Value("${jwt.secret}")
    private String secret;
    private static final String finalKey = "1E82D3E1BD1C72ACBA498AB58C4551E82D3E1BD1C72ACBA498AB58C4551E82D3E1BD1C72ACBA498AB58C4551E82D3E1BD1C72ACBA498AB58C4551E82D3E1BD1C72ACBA498AB58C45";

    @PostConstruct
    protected void init(){
       secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public String createToken(User user){
        try {
            Map<String, Object> claims;
            claims = Jwts.claims().setSubject(user.getUsername());
            claims.put("id", user.getId());
            Date now = new Date();
            Date exp = new Date(now.getTime() + 3600000);
            return Jwts.builder()
                    .setClaims(claims)
                    .setIssuedAt(now)
                    .setExpiration(exp)
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .compact();
        }catch (Exception e){
           return "test";
        }
    }

    public Key getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(finalKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(getSignInKey()).parseClaimsJws(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public String getUsernameFromToken(String token){
          try{
              return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
          }catch (Exception e){
              return "Bad token";
          }
    }

    public boolean isJwtExpired(String jwt){
        return extractExpiration(jwt).before(new Date());
    }

    public Date extractExpiration(String jwt){
        return extractClaim(jwt, Claims::getExpiration);
    }

    public <T> T extractClaim(String jwt, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(jwt);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String jwt){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }
    public String extractUsername(String jwt){
        try {
            return extractClaim(jwt, Claims::getSubject);
        }catch (Exception e){
            return null;
        }
    }
    public String generateJwt(User userDetails){
        return generateJwt(new HashMap<>(), userDetails);
    }
    public String generateJwt(
            Map<String, Object> extraClaims,
            User userDetails
    ){
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    public boolean isJwtValid(String jwt){
        final String username = extractUsername(jwt);
        return (!isJwtExpired(jwt));
    }




}
