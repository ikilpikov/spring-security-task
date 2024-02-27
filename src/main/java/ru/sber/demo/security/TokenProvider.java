package ru.sber.demo.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;


@Component
public class TokenProvider {

/*
    @Value("${spring.security.authentication.jwt.validity}")
    private long tokenLifetime;
*/

    @Value("${custom-properties.jwt.secret}")
    private String secretKey;

    @Value("${custom-properties.jwt.issuer}")
    private String issuer;

    private final UserDetailsService userDetailsService;

    public TokenProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public String createToken(String username) {
        var tokenExpirationDate = Date.from(ZonedDateTime.now().plusMinutes(2).toInstant());

        return Jwts.builder()
                .setSubject(username)
                .setIssuer(issuer)
                .signWith(SignatureAlgorithm.HS512, this.secretKey)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(tokenExpirationDate)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public String getClaim(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public UserDetails getUserDetails(String token) {
        String username = getClaim(token);
        return userDetailsService.loadUserByUsername(username);
    }

}
