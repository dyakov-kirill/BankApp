package ru.dyakov.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.dyakov.entities.User;
import ru.dyakov.requests.SignInRequest;

import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${security.jwt.secret}")
    private String SECRET_KEY;

    public String generateToken(User user) {
        return Jwts.builder().setSubject(user.getPhoneNumber())
                .claim("Id", user.getId())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    public Boolean validateToken(String token, SignInRequest userDetails) {
        final String username = extractPhoneNumber(token);
        return (username.equals(userDetails.getPhoneNumber()) && !isTokenExpired(token));
    }

    public String extractPhoneNumber(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJwt(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
