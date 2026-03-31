package co.istad.y2.quizzy.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;



import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    // SECRET must be at least 32 chars for HS256
    private final String SECRET = "my-super-secret-key-which-is-32chars!!"; // exactly 32+
    // Convert secret string to SecretKey
    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }
    // Generate token
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Extract email from token
    public String extractEmail(String token) {
        Claims claims = Jwts.parser()        // create a parser builder
                .setSigningKey(getKey())     // set signing key
                .build()                     // build the parser
                .parseClaimsJws(token)       // parse the JWT
                .getBody();

        return claims.getSubject();
    }
}