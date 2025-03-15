package gh.filesharing.config;

import io.github.cdimascio.dotenv.Dotenv;

public class ConfigLoader {
    private static final Dotenv dotenv = Dotenv.load();

    public static final String DB_URL = dotenv.get("DB_URL");
    public static final String DB_USER = dotenv.get("DB_USER");
    public static final String DB_PASSWORD = dotenv.get("DB_PASSWORD");
    public static final String JWT_SECRET = dotenv.get("JWT_SECRET");
}
