import gh.filesharing.client.controllers.UploadController;
import javafx.application.Platform;

import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UploadControllerTest {

    private UploadController uploadController;
    private VBox dropArea;
    private Dragboard dragboard;

    @BeforeEach
    public void setUp() throws Exception {
        // Initialize the JavaFX platform
        Platform.startup(() -> {
        });

        uploadController = new UploadController();
        dropArea = new VBox();
        dragboard = mock(Dragboard.class);

        Field dropAreaField = UploadController.class.getDeclaredField("dropArea");
        dropAreaField.setAccessible(true);
        dropAreaField.set(uploadController, dropArea);

        uploadController.initialize();
    }

    @Test
    void dragOverWithFile() {
        var event = mock(javafx.scene.input.DragEvent.class);
        when(event.getGestureSource()).thenReturn(new Object());
        when(event.getDragboard()).thenReturn(dragboard);
        when(dragboard.hasFiles()).thenReturn(true);

        dropArea.getOnDragOver().handle(event);

        verify(event).acceptTransferModes(TransferMode.COPY);
        verify(event).consume();
    }

    @Test
    void dragOverWithoutFile() {
        var event = mock(javafx.scene.input.DragEvent.class);
        when(event.getGestureSource()).thenReturn(new Object());
        when(event.getDragboard()).thenReturn(dragboard);
        when(dragboard.hasFiles()).thenReturn(false);

        dropArea.getOnDragOver().handle(event);

        verify(event, never()).acceptTransferModes(any());
        verify(event).consume();
    }

    @Test
    void dragDroppedWithFile() throws Exception {
        var event = mock(javafx.scene.input.DragEvent.class);
        when(event.getDragboard()).thenReturn(dragboard);
        File file = new File("test.txt");
        when(dragboard.hasFiles()).thenReturn(true);
        when(dragboard.getFiles()).thenReturn(Collections.singletonList(file));

        dropArea.getOnDragDropped().handle(event);

        Field selectedFileField = UploadController.class.getDeclaredField("selectedFile");
        selectedFileField.setAccessible(true);
        assertEquals(file, selectedFileField.get(uploadController));

        verify(event).setDropCompleted(true);
        verify(event).consume();
    }

    @Test
    void dragDroppedWithoutFile() {
        var event = mock(javafx.scene.input.DragEvent.class);
        when(event.getDragboard()).thenReturn(dragboard);
        when(dragboard.hasFiles()).thenReturn(false);

        dropArea.getOnDragDropped().handle(event);

        verify(event).setDropCompleted(true);
        verify(event).consume();
    }
}