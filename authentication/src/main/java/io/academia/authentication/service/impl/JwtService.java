package io.academia.authentication.service.impl;

import io.academia.authentication.model.Account;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {

    public static final String SECRET_KEY = "464CC3302A0EF7CDBB1509B5ABA5C29488CD1B8A8B4BEFE2070B81CBA9C8F089";

    public String generatorToken(UserDetails userDetails) {
        Map<String, Object> additionalClaims = new HashMap<>();

        additionalClaims.put("id", ((Account) userDetails).getId());
        additionalClaims.put("verified", ((Account) userDetails).isVerified());
        additionalClaims.put("email", ((Account) userDetails).getEmail());
        additionalClaims.put("dateOfBirth", ((Account) userDetails).getDateOfBirth());
        additionalClaims.put("firstName", ((Account) userDetails).getFirstName());
        additionalClaims.put("lastName", ((Account) userDetails).getLastName());
        additionalClaims.put("gender", ((Account) userDetails).getGender());

        Set<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        additionalClaims.put("roles", roles);

        return generatorToken(additionalClaims, userDetails);
    }

    public String generatorToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getSignInKey())
                .compact();
    }

    private Key getSignInKey() {
        byte[] keyByte = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyByte);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }
    
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return (Claims) Jwts
                .parser()
                .verifyWith((SecretKey) getSignInKey())
                .build()
                .parseSignedClaims(token);
    }

    public boolean validateToken(final String token) {
        try {
            Jwts.parser().verifyWith((SecretKey) getSignInKey()).build().parseSignedClaims(token);
            return true;
        } catch (SignatureException e) {
            return false;
        }
    }

}
