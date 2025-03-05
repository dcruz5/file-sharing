package gh.filesharing.controllers;

import gh.filesharing.db.UserDAO;
import io.javalin.apibuilder.CrudHandler;
import io.javalin.http.Context;
import gh.filesharing.models.FileMetadata;
import gh.filesharing.db.FileDAO;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class FileController implements CrudHandler {
    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    private final FileDAO fileDAO;

    public FileController(FileDAO fileDAO) {
        this.fileDAO = fileDAO;
    }

    @Override
    public void getAll(Context ctx) {
        ctx.json(fileDAO.getAllFiles());
    }

    @Override
    public void getOne(Context ctx, @NotNull String fileId) {
        ctx.json(fileDAO.getFileById(Integer.parseInt(fileId)));
    }

    @Override
    public void create(Context ctx) {
        FileMetadata file = ctx.bodyAsClass(FileMetadata.class);
        fileDAO.createFile(file);
        ctx.status(201).json(file);
    }

    @Override
    public void update(Context ctx, @NotNull String fileId) {
        FileMetadata file = ctx.bodyAsClass(FileMetadata.class);
        file.setId(Integer.parseInt(fileId));
        fileDAO.updateFile(file);
        ctx.status(200).json(file);
    }

    public static void upload(Context ctx) {
        String username = ctx.attribute("username");

        if (username == null) {
            ctx.status(401).result("Unauthorized");
            return;
        }

        // implement logic
        ctx.status(200).result("File uploaded successfully by user: " + username);
    }

    @Override
    public void delete(Context ctx, @NotNull String fileId) {
        fileDAO.deleteFile(Integer.parseInt(fileId));
        ctx.status(204);
    }

    public static void download(Context ctx) {
        String fileId = ctx.pathParam("fileId");
        String username = ctx.attribute("username");

        if (username == null) {
            ctx.status(401).result("Unauthorized");
            return;
        }

        ctx.result("File downloaded successfully by user: " + username);
        log.info("User {} downloaded a file", username);
        ctx.status(200);
    }

    public static void share(Context ctx) {
        log.info("User {} shared a file", Optional.ofNullable(ctx.attribute("username")));
        ctx.status(200);
    }
}
