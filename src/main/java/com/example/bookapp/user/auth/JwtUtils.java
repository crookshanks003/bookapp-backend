package com.example.bookapp.user.auth;

import io.jsonwebtoken.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtils {
    private final String jwtSecrete = "wkjeckwjebkejvwkenvkwejkwrjvbejkrvbekrnvlwenvlbjwehbweDJKNKJEBLWJbjwjebjheb";
    private final Key key = new SecretKeySpec(Base64.getDecoder().decode(jwtSecrete), SignatureAlgorithm.HS256.getJcaName());

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder().setSubject(userDetails.getUsername()).setIssuedAt(Date.from(Instant.now())).signWith(key).compact();
    }

    public String getUserEmail(String token) {
        Claims claim = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

        return claim.getSubject();
    }

    public boolean validateToken(String token, UserDetails userDetails) throws MalformedJwtException {
        String email = getUserEmail(token);
        if (email.equals(userDetails.getUsername())){
            return true;
        }else{
            throw new MalformedJwtException("Invalid jwt");
        }
    }
}
