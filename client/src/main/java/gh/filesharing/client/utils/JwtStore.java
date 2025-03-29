package gh.filesharing.client.utils;
import java.util.prefs.Preferences;

public class JwtStore {

    private static final Preferences prefs = Preferences.userRoot().node("jwt-token-store");

    public static void saveToken(String token) {
        prefs.put("jwt_token", token);
    }

    public static String loadToken() {
        return prefs.get("jwt_token", null);
    }

    public static void clearToken() {
        prefs.remove("jwt_token");
    }
}
