import gh.filesharing.client.Utils.SSLClientUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class SSLClientUtilTest {

    private SSLClientUtil sslClientUtil;
    private SSLSocketFactory mockSocketFactory;
    private SSLSocket mockSocket;

    @BeforeEach
    public void setUp() {
        sslClientUtil = new SSLClientUtil();
        mockSocketFactory = mock(SSLSocketFactory.class);
        mockSocket = mock(SSLSocket.class);
    }

    @Test
    void connectadoSuccessfully() throws IOException {
        // Create mock output and input streams
        OutputStream mockOutputStream = mock(OutputStream.class);
        ByteArrayInputStream mockInputStream = new ByteArrayInputStream("".getBytes());

        // Configure the mock socket behavior
        when(mockSocket.getOutputStream()).thenReturn(mockOutputStream);
        when(mockSocket.getInputStream()).thenReturn(mockInputStream);

        // Configure the mock factory behavior
        when(mockSocketFactory.createSocket("localhost", 8443)).thenReturn(mockSocket);

        // Use a static mock for SSLSocketFactory.getDefault()
        try (MockedStatic<SSLSocketFactory> mockedStatic = mockStatic(SSLSocketFactory.class)) {
            // When getDefault() is called, return our mock factory
            mockedStatic.when(SSLSocketFactory::getDefault).thenReturn(mockSocketFactory);

            // Now test the method
            boolean result = sslClientUtil.connectado();

            // Verify the behavior
            verify(mockSocket).startHandshake();
            assertTrue(result);
        }
    }

    @Test
    void connectadoFails() throws IOException {
        // Configure the mock factory behavior to throw an exception
        when(mockSocketFactory.createSocket("localhost", 8443)).thenThrow(new IOException("Connection failed"));

        // Use a static mock for SSLSocketFactory.getDefault()
        try (MockedStatic<SSLSocketFactory> mockedStatic = mockStatic(SSLSocketFactory.class)) {
            // When getDefault() is called, return our mock factory that will throw exception
            mockedStatic.when(SSLSocketFactory::getDefault).thenReturn(mockSocketFactory);

            // Now test the method
            boolean result = sslClientUtil.connectado();

            // Verify the behavior
            assertFalse(result);
        }
    }
}