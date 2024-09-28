package com.mainhub.homebanking.servicesSecurity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtilService{

    private static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();

    private static final long EXPIRATION_TOKEN = 1000 * 60 * 60;

    public static Claims extractAllClaims(String token){
        return Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload();
    }

    public static <T> T extractClaim(String token, Function<Claims, T> claimsTFunction){
        final Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    public static String extractUsername(String token){return extractClaim(token, Claims::getSubject);}

    public static Date extractExpiration(String token){return extractClaim(token, Claims::getExpiration);}

    public static Boolean isTokenExpired(String token){return extractExpiration(token).before(new Date());}

    public static String createToken(Map<String, Object> claims, String username){
        return  Jwts
                .builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TOKEN ))
                .signWith(SECRET_KEY)
                .compact();
    }

    public static String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        String rol = userDetails.getAuthorities().iterator().next().getAuthority();
        claims.put("rol", rol);
        return createToken(claims, userDetails.getUsername());
    }
}