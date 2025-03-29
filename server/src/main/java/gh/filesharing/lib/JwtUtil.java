package gh.filesharing.lib;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import gh.filesharing.App;

import java.util.Date;

import gh.filesharing.config.ConfigLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JwtUtil {
    private static final Logger log = LoggerFactory.getLogger(App.class);
    private static final String SECRET_KEY = "cXNn8SlN84eaTwyB16WbOsjGY4oF2f6x";

    public static String generateToken(String username) {
        long expirationTime = 60 * 60 * 1000; // 1 hour
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .sign(Algorithm.HMAC512(SECRET_KEY));
    }

    public static String extractUsername(String token) {
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            return decodedJWT.getSubject();
        } catch (JWTVerificationException e) {
            log.error("Invalid token: {}", e.getMessage());
            return null;
        }
    }

    private static Date extractExpiration(String token) {
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            return decodedJWT.getExpiresAt();
        } catch (JWTVerificationException e) {
            log.error("Invalid token: {}", e.getMessage());
            return null;
        }
    }

    public static boolean isTokenExpired(String token) {
        if (extractExpiration(token) == null) {
            return true;
        }
        return extractExpiration(token).before(new Date());
    }


    public static boolean validateToken(String token, String username) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC512(SECRET_KEY)).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT.getSubject().equals(username);
        } catch (JWTVerificationException e) {
            log.error("Invalid token: {}", e.getMessage());
            return false;
        }
    }
}
