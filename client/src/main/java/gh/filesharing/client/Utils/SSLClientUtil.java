package gh.filesharing.client.Utils;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.UnknownHostException;

public class SSLClientUtil {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8443;
    private static SSLSocket socket;
    private static PrintWriter out;
    private static BufferedReader in;


    public boolean connectado() {
        try {
            SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            socket = (SSLSocket) factory.createSocket(SERVER_HOST, SERVER_PORT);
            socket.startHandshake();

            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            return true;
        } catch (IOException e) {
            AlertManager.showError( "No se pudo conectar al servidor");
            System.out.println(e.getMessage());
            return false;
        }
    }


}
