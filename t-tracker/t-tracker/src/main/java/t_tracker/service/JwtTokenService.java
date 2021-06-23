package t_tracker.service;

import java.util.Date;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtTokenService {

    private final static int TOKEN_EXPIRATION_TIME = 30 * 60 * 1000;
    private final static String TOKEN_KEY = "ut1FfO9sSPjG1OKxVh";

    private JwtTokenService() {}

    public static String generateToken(String username, Claims claims) {
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, TOKEN_KEY)
                .compact();
    }

    public static void verifyToken(String token) throws JwtException {
        Jwts.parser()
                .setSigningKey(TOKEN_KEY)
                .parse(token.substring(7));
    }

    public static Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(TOKEN_KEY)
                .parseClaimsJws(token.substring(7))
                .getBody();
    }

    public static String updateExpirationDateToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, TOKEN_KEY)
                .compact();
    }
}
