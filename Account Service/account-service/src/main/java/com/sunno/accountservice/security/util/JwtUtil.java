package com.sunno.accountservice.security.util;

import com.github.openjson.JSONObject;
import com.sunno.accountservice.models.persistence.User;
import com.sunno.accountservice.service.MyUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtUtil {

    private static final String SECRET_KEY = "";    // TODO: change this!

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        System.out.println(extractExpiration(token).getTime());
        System.out.println(Date.from(Instant.now()));
        return extractExpiration(token).before(Date.from(Instant.now()));
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername(),10);
    }

    public String generateToken(MyUserDetailsService userDetailsService,long  exp) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetailsService.toString(), exp);
    }

    public String generateToken(String accessType, long exp) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, accessType, exp);
    }

    private String createToken(Map<String, Object> claims, String subject, long exp) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(exp))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public Boolean validateToken(String token ){
        return !isTokenExpired(token);
    }

    public Boolean validateToken(String token , String email){
        JSONObject jsonObject = new JSONObject(extractUsername(token));
        final String email2 = jsonObject.getString("email");
        System.out.println(Instant.now());
        System.out.println(!isTokenExpired(token));
        return (email.equals(email2) && !isTokenExpired(token));
    }

    public static String generateToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getEmail());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuer("https://devglan.com")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }


    public String generateRefreshToken() {
        // generate a random UUID as refresh token
        return UUID.randomUUID().toString();
    }
}