package gh.filesharing.controllers;

import gh.filesharing.db.FileDAO;
import gh.filesharing.models.FileMetadata;
import io.javalin.http.Context;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class FileControllerTest {
    private FileController fileController;
    private FileDAO fileDAO;
    private Context ctx;

    @BeforeEach
    void setUp() {
        fileDAO = mock(FileDAO.class);
        ctx = mock(Context.class);
        fileController = new FileController(fileDAO);

    }

    @Test
    void getAllFiles_ReturnsListOfFiles() {
        when(fileDAO.getAllFiles()).thenReturn(java.util.Collections.emptyList());

        fileController.getAll(ctx);

        verify(ctx).json(java.util.Collections.emptyList());
    }

    @Test
    void getFileById_ReturnsFile() {
        FileMetadata file = new FileMetadata();
        file.setId(1);
        when(fileDAO.getFileById(1)).thenReturn(file);
        when(ctx.pathParam("fileId")).thenReturn("1");

        fileController.getOne(ctx, "1");

        verify(ctx).json(file);
    }

    @Test
    void createFile_Success() {
        FileMetadata file = new FileMetadata();
        when(ctx.bodyAsClass(FileMetadata.class)).thenReturn(file);
        when(ctx.status(201)).thenReturn(ctx);

        fileController.create(ctx);

        verify(fileDAO).createFile(file);
        verify(ctx).status(201);
        verify(ctx).json(file);
    }

    @Test
    void updateFile_Success() {
        FileMetadata file = new FileMetadata();
        when(ctx.bodyAsClass(FileMetadata.class)).thenReturn(file);
        when(ctx.pathParam("fileId")).thenReturn("1");
        when(ctx.status(200)).thenReturn(ctx);

        fileController.update(ctx, "1");

        verify(fileDAO).updateFile(file);
        verify(ctx).status(200);
        verify(ctx).json(file);
    }

    @Test
    void deleteFile_Success() {
        when(ctx.pathParam("fileId")).thenReturn("1");

        fileController.delete(ctx, "1");

        verify(fileDAO).deleteFile(1);
        verify(ctx).status(204);
    }

    @Test
    void uploadFile_Unauthorized() {
        when(ctx.attribute("username")).thenReturn(null);

        when(ctx.status(401)).thenReturn(ctx);

        FileController.upload(ctx);

        verify(ctx).status(401);
        verify(ctx).result("Unauthorized");
    }

    @Test
    void downloadFile_Unauthorized() {
        when(ctx.attribute("username")).thenReturn(null);
        when(ctx.pathParam("fileId")).thenReturn("1");
        when(ctx.status(401)).thenReturn(ctx);

        FileController.download(ctx);

        verify(ctx).status(401);
        verify(ctx).result("Unauthorized");
    }

    @Test
    void downloadFile_Success() {
        when(ctx.attribute("username")).thenReturn("Alice");
        when(ctx.pathParam("fileId")).thenReturn("1");
        when(ctx.status(200)).thenReturn(ctx);

        FileController.download(ctx);

        verify(ctx).result("File downloaded successfully by user: Alice");
        verify(ctx).status(200);
    }

    @Test
    void shareFile_Success() {
        when(ctx.attribute("username")).thenReturn("Alice");
        when(ctx.status(200)).thenReturn(ctx);

        FileController.share(ctx);

        verify(ctx).status(200);
    }
}
