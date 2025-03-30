package gh.filesharing.client.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class JwtUtil {
    public static Claims decodeToken(String token) {
//        try {
//            return Jwts.parser()
//                    .parseSignedClaims(token)
//                    .getPayload();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
        return null;
    }

    public static boolean isTokenExpired(String token) {
        return false;
    }
}
