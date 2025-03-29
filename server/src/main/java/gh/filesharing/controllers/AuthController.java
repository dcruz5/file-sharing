package gh.filesharing.controllers;

import at.favre.lib.crypto.bcrypt.BCrypt;
import gh.filesharing.db.FileDAO;
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

    private final UserDAO userDAO;

    public AuthController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void login(Context ctx) {
        try {
            Map<String, String> requestBody = ctx.bodyAsClass(Map.class);

            String username = requestBody.get("username");
            String password = requestBody.get("password");

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
        } catch (Exception e) {
            log.error("Error parsing login request body", e);
            ctx.status(400).result("Invalid request format");
        }
    }

    public void register(Context ctx) {
        User user = ctx.bodyAsClass(User.class);

        if (user.getUsername() == null || user.getPasswordHash() == null || user.getEmail() == null) {
            ctx.status(400).json("Missing username, password, or email");
            return;
        }

        log.info("Received user registration: " + user.getUsername());

        userDAO.createUser(user);

        log.info("User successfully saved: " + user.getUsername());
        ctx.status(201).json("User " + user.getUsername() + " created successfully");
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
