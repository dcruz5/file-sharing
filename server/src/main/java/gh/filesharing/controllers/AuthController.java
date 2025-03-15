package gh.filesharing.controllers;

import gh.filesharing.db.UserDAO;
import gh.filesharing.lib.AuthManager;
import gh.filesharing.lib.JwtUtil;

import gh.filesharing.models.User;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    public static void login(Context ctx) {
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");

        if (username == null || password == null) {
            ctx.status(400).result("Missing username or password");
            return;
        }

        String token = AuthManager.validateCredentials(username, password);
        if (token != null) {
            ctx.status(200).json(Map.of("token", token));
            log.info("User {} logged in successfully.", username);
        } else {
            ctx.status(401).result("Invalid credentials");
            log.warn("Failed login attempt for username: {}.", username);
        }
    }

    public static void register(Context ctx) {
        String username = ctx.formParam("username");
        String passwordHashed = ctx.formParam("password"); // password will already be hashed
        String email = ctx.formParam("email");
        boolean isAdmin = Boolean.parseBoolean(ctx.formParam("isAdmin"));

        if (username == null || passwordHashed == null || email == null) {
            ctx.status(400).result("Missing username, password, or email");
            return;
        }

        User user = new User(username, passwordHashed, email, isAdmin);




        ctx.json("User " + username + " created successfully");
    }

    // Middleware
    public static void authenticate(Context ctx) {
        String token = ctx.header("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            ctx.status(401).result("Unauthorized");
            return;
        }

        token = token.substring(7); // Remove "Bearer " prefix
        try {
            if (JwtUtil.validateToken(token, JwtUtil.extractUsername(token))) {
                ctx.attribute("username", JwtUtil.extractUsername(token));
            } else {
                ctx.status(401).result("Invalid or expired token");
            }
        } catch (Exception e) {
            ctx.status(401).result("Unauthorized");
        }
    }
}
