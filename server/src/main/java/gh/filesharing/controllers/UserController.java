package gh.filesharing.controllers;

import io.javalin.apibuilder.CrudHandler;
import io.javalin.http.Context;
import gh.filesharing.models.User;
import gh.filesharing.db.UserDAO;
import org.jetbrains.annotations.NotNull;

public class UserController implements CrudHandler {

    private final UserDAO userDAO;

    // Dependency injection
    public UserController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public void getAll(Context ctx) {
        ctx.json(userDAO.getAllUsers());
    }

    @Override
    public void getOne(Context ctx, @NotNull String userId) {
        ctx.json(userDAO.getUserById(Integer.parseInt(userId)));
    }

    @Override
    public void create(Context ctx) {
        User user = ctx.bodyAsClass(User.class);
        userDAO.createUser(user);
        ctx.status(201).json(user);
    }

    @Override
    public void update(Context ctx, @NotNull String userId) {
        User user = ctx.bodyAsClass(User.class);
        user.setId(Integer.parseInt(userId));
        userDAO.updateUser(user);
        ctx.status(200).json(user);
    }

    @Override
    public void delete(Context ctx, @NotNull String userId) {
        userDAO.deleteUser(Integer.parseInt(userId));
        ctx.status(204);
    }


    public static void getUserFiles(Context context) {
        String userId = context.pathParam("userId");
        // Get user files from DB
        context.json("Files for user " + userId);
    }
}
