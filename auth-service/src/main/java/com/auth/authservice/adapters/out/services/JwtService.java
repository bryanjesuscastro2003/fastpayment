package com.auth.authservice.adapters.out.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
@Service
public class JwtService {
    private static final String finalKey = "1E82D3E1BD1C72ACBA498AB58C4551E82D3E1BD1C72ACBA498AB58C4551E82D3E1BD1C72ACBA498AB58C4551E82D3E1BD1C72ACBA498AB58C4551E82D3E1BD1C72ACBA498AB58C45";
    public String extractUsername(String jwt){
        try {
            return extractClaim(jwt, Claims::getSubject);
        }catch (Exception e){
            return null;
        }
    }
    public <T> T extractClaim(String jwt, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(jwt);
        return claimsResolver.apply(claims);
    }
    public String generateJwt(UserDetails userDetails){
        return generateJwt(new HashMap<>(), userDetails);
    }

    public String generateJwt(
            Map<String, Object> extraClaims,
            UserDetails userDetails
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

    public boolean isJwtValid(String jwt, UserDetails userDetails){
        final String username = extractUsername(jwt);
        return (username.equals(userDetails.getUsername())) && !isJwtExpired(jwt);
    }

    public boolean isJwtExpired(String jwt){
        return extractExpiration(jwt).before(new Date());
    }

    public Date extractExpiration(String jwt){
        return extractClaim(jwt, Claims::getExpiration);
    }

    public Claims extractAllClaims(String jwt){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    public Key getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(finalKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


}
