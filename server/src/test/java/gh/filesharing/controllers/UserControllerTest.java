package gh.filesharing.controllers;

import gh.filesharing.db.UserDAO;
import gh.filesharing.models.User;
import io.javalin.http.Context;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

class UserControllerTest {

    private UserController userController;
    private UserDAO userDAO;
    private Context ctx;

    @BeforeEach
    void setUp() {
        userDAO = mock(UserDAO.class);
        ctx = mock(Context.class);
        userController = new UserController(userDAO);
    }

    @Test
    void getAllUsers_ReturnsListOfUsers() {
        List<User> users = Arrays.asList(new User(1, "Alice", "hash1", "alice@example.com", false), new User(2, "Bob", "hash2", "bob@example.com", false));
        when(userDAO.getAllUsers()).thenReturn(users);

        userController.getAll(ctx);

        verify(ctx).json(users);
    }

    @Test
    void getOneUser_ReturnsUser() {
        User user = new User(1, "Alice", "hash1", "alice@example.com", false);
        when(ctx.pathParam("userId")).thenReturn("1");
        when(userDAO.getUserById(1)).thenReturn(user);

        userController.getOne(ctx, "1");

        verify(ctx).json(user);
    }

    @Test
    void createUser_CreatesUserAndReturns201() {
        User user = new User("Alice", "hash1", "alice@example.com", false);
        when(ctx.bodyAsClass(User.class)).thenReturn(user);

        when(ctx.status(201)).thenReturn(ctx);

        userController.create(ctx);

        verify(userDAO).createUser(user);
        verify(ctx).status(201);
        verify(ctx).json(user);
    }

    @Test
    void updateUser_UpdatesUserAndReturns200() {
        User user = new User(1, "UpdatedAlice", "hash1", "alice@example.com", false);
        when(ctx.bodyAsClass(User.class)).thenReturn(user);
        when(ctx.pathParam("userId")).thenReturn("1");

        when(ctx.status(200)).thenReturn(ctx);
        userController.update(ctx, "1");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userDAO).updateUser(userCaptor.capture());
        assertEquals(1, userCaptor.getValue().getId());
        verify(ctx).status(200);
        verify(ctx).json(user);
    }

    @Test
    void deleteUser_DeletesUserAndReturns204() {
        when(ctx.pathParam("userId")).thenReturn("1");

        userController.delete(ctx, "1");

        verify(userDAO).deleteUser(1);
        verify(ctx).status(204);
    }

    @Test
    void getUserFiles_ReturnsUserFiles() {
        when(ctx.pathParam("userId")).thenReturn("1");

        UserController.getUserFiles(ctx);

        verify(ctx).json("Files for user 1");
    }
}
