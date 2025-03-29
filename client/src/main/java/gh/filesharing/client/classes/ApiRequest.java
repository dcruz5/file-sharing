package gh.filesharing.client.classes;

import com.fasterxml.jackson.databind.ObjectMapper;
import gh.filesharing.client.utils.HttpsConnectionManager;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.Map;

public class ApiRequest {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8080;
    private static final int SERVER_SSL_PORT = 8443;
    private static final String BASE_URL = "https://" + SERVER_HOST + ":" + SERVER_SSL_PORT;

    public static String get(String endpoint) {
        return makeHttpRequest("GET", endpoint, null);
    }

    public static String post(String endpoint, Map<String, String> data) {
        return makeHttpRequest("POST", endpoint, data);
    }

    public static String put(String endpoint, Map<String, String> data) {
        return makeHttpRequest("PUT", endpoint, data);
    }

    public static String delete(String endpoint) {
        return makeHttpRequest("DELETE", endpoint, null);
    }

    private static String makeHttpRequest(String method, String endpoint, Map<String, String> data) {
        String url = BASE_URL + endpoint;
        try {
            URL urlObj = new URL(url);
            HttpsURLConnection connection = (HttpsURLConnection) urlObj.openConnection();
            connection.setSSLSocketFactory(HttpsConnectionManager.getSocketFactory());

            connection.setRequestMethod(method);

            if ((method.equals("POST") || method.equals("PUT")) && data != null) {
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonInputString = objectMapper.writeValueAsString(data);
                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                return reader.lines().reduce("", (acc, line) -> acc + line);
            }

        } catch (Exception e) {
            // TODO: configure logger
            e.printStackTrace();
            return null;
        }
    }
}