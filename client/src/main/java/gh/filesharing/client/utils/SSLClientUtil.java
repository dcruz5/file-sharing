package gh.filesharing.client.utils;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase utilitaria para manejar conexiones SSL y enviar peticiones HTTP.
 */
public class SSLClientUtil {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_SSL_PORT = 8443;
    private static final String SERVER_PORT = "8080";

    private SSLSocket sslSocket;
    private PrintWriter out;
    private BufferedReader in;

    /**
     * Establece una conexión SSL con el servidor.
     *
     * @return true si la conexión se establece correctamente, false en caso contrario.
     */
    public boolean conectar() {
        try {
//                    TrustManager[] trustManagers = null;
//                    KeyManager[] keyManagers = null;
//
//                    SSLContext sc = SSLContext.getInstance("SSL");
//                    sc.init(keyManagers, trustManagers, null);
//                    // Crear el socket SSL con el TrustManager personalizado
//                    SSLSocketFactory factory = sc.getSocketFactory();


            // Crear el socket SSL con el TrustManager personalizado
            SSLSocketFactory factory = (SSLSocketFactory) TrustAllCertsSSL.getSSLContext().getSocketFactory();
            sslSocket = (SSLSocket) factory.createSocket(SERVER_HOST, SERVER_SSL_PORT);

            // Configurar los protocolos SSL/TLS permitidos
            sslSocket.setEnabledProtocols(new String[] {"TLSv1.2", "TLSv1.3"});

            // Configurar los streams de entrada/salida
            out = new PrintWriter(sslSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));

            System.out.println("Conexión SSL establecida con el servidor en puerto " + SERVER_SSL_PORT);
            return true;
        } catch (Exception e) {
            System.err.println("Error al conectar con SSL: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cierra la conexión SSL con el servidor.
     */
    public void cerrarConexion() {
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (sslSocket != null) sslSocket.close();
            System.out.println("Conexión SSL cerrada");
        } catch (IOException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Construye una cadena de datos de formulario a partir de un mapa de parámetros.
     *
     * @param params Mapa de parámetros.
     * @return Cadena de datos de formulario codificada.
     */
    private String buildFormData(Map<String, String> params) {
        StringBuilder formData = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (formData.length() > 0) {
                formData.append("&");
            }
            formData.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8))
                    .append("=")
                    .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
        }
        return formData.toString();
    }

    /**
     * Envía una petición HTTP al servidor a través de la conexión SSL.
     *
     * @param method Metodo HTTP (e.g., "GET", "POST").
     * @param path   Ruta del recurso en el servidor.
     * @param body   Cuerpo de la petición.
     * @return Respuesta del servidor.
     */
    public String enviarPeticion(String method, String path, String body) {
        try {
            if (sslSocket == null || sslSocket.isClosed()) {
                if (!conectar()) {
                    return "ERROR: No se pudo establecer la conexión SSL";
                }
            }

            // Construir la petición HTTP
            StringBuilder request = new StringBuilder();
            request.append(method).append(" ").append(path).append(" HTTP/1.1\r\n");
            request.append("Host: ").append(SERVER_HOST).append(":").append(SERVER_SSL_PORT).append("\r\n");
            request.append("Content-Type: application/x-www-form-urlencoded\r\n");
            request.append("Content-Length: ").append(body.length()).append("\r\n");
            request.append("Connection: keep-alive\r\n");
            request.append("\r\n");
            request.append(body);

            // Enviar la petición
            out.print(request.toString());
            out.flush();

            // Leer la respuesta
            StringBuilder respuesta = new StringBuilder();
            String linea;
            boolean headerEnded = false;
            int contentLength = 0;

            // Leer headers
            while ((linea = in.readLine()) != null && !linea.isEmpty()) {
                if (linea.toLowerCase().startsWith("content-length:")) {
                    contentLength = Integer.parseInt(linea.substring(15).trim());
                }
                System.out.println("Header recibido: " + linea);
            }

            // Leer body
            if (contentLength > 0) {
                char[] buffer = new char[contentLength];
                in.read(buffer, 0, contentLength);
                respuesta.append(buffer);
            } else {
                while ((linea = in.readLine()) != null) {
                    respuesta.append(linea).append("\n");
                }
            }

            return respuesta.toString().trim();
        } catch (IOException e) {
            System.err.println("Error al enviar petición: " + e.getMessage());
            e.printStackTrace();
            return "ERROR: " + e.getMessage();
        }
    }

    /**
     * Autentica un usuario enviando una petición de login al servidor.
     *
     * @param username Nombre de usuario.
     * @param password Contraseña del usuario.
     * @return true si la autenticación es exitosa, false en caso contrario.
     */
    public boolean autenticarUsuario(String username, String password) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("username", username);
            params.put("password", password);

            String body = buildFormData(params);
            System.out.println("Enviando petición de login con body: " + body);

            String respuesta = enviarPeticion("POST", "/login", body);
            System.out.println("Respuesta completa del servidor: " + respuesta);

            // Procesar la respuesta
            if (respuesta.contains("success") || respuesta.contains("200")) {
                System.out.println("Autenticación exitosa");
                return true;
            } else {
                System.out.println("Autenticación fallida: " + respuesta);
                return false;
            }
        } catch (Exception e) {
            System.err.println("Error en la autenticación: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Registra un nuevo usuario enviando una petición de registro al servidor.
     *
     * @param username Nombre de usuario.
     * @param email    Correo electrónico del usuario.
     * @param password Contraseña del usuario.
     * @return true si el registro es exitoso, false en caso contrario.
     */
    public boolean registrarUsuario(String username, String email, String password) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("username", username);
            params.put("email", email);
            params.put("password", password);
            params.put("isAdmin", "false");

            String body = buildFormData(params);
            System.out.println("Enviando petición de registro con body: " + body);

            String respuesta = enviarPeticion("POST", "/register", body);
            System.out.println("Respuesta completa del servidor: " + respuesta);

            if (respuesta.contains("success") || respuesta.contains("200")) {
                System.out.println("Registro exitoso");
                return true;
            } else {
                System.out.println("Registro fallido: " + respuesta);
                return false;
            }
        } catch (Exception e) {
            System.err.println("Error en el registro: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}