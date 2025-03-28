package gh.filesharing.client.utils;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

public class SimpleApiRequest {
    private static final String BASE_URL = "http://localhost:8080";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String get(String endpoint) throws IOException {
        String url = BASE_URL + endpoint;
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            request.setHeader("Accept", "application/json");

            return client.execute(request, response ->
                    response.getEntity() != null ? response.getEntity().getContent().toString() : ""
            );
        }
    }

    public static String post(String endpoint, Map<String, String> data) throws IOException {
        String url = BASE_URL + endpoint;
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(url);
            request.setHeader("Content-Type", "application/json");

            String json = objectMapper.writeValueAsString(data);
            request.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));

            return client.execute(request, response ->
                    response.getEntity() != null ? response.getEntity().getContent().toString() : ""
            );
        }
    }
}
