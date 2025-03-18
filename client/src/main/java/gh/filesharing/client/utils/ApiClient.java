package gh.filesharing.client.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import gh.filesharing.client.HelloApplication;
import org.apache.hc.client5.http.classic.methods.*;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.ssl.SSLContextBuilder;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;
import java.security.KeyStore;
import java.util.Map;

public class ApiClient {
    private static final String BASE_URL = "https://localhost:8080";
    private static final String TRUSTSTORE_PATH = HelloApplication.class.getClassLoader().getResource("keystores/client-truststore.jks").getPath();
    private static final String TRUSTSTORE_PASSWORD = "grupo8";
    private static final ObjectMapper objectMapper = new ObjectMapper();


    // instance HttpClient with SSL context
    private static CloseableHttpClient createHttpClient() throws Exception {
        KeyStore trustStore = KeyStore.getInstance("JKS");
        try (var trustStoreStream = new java.io.FileInputStream(new File(TRUSTSTORE_PATH))) {
            trustStore.load(trustStoreStream, TRUSTSTORE_PASSWORD.toCharArray());
        }

        SSLContext sslContext = SSLContextBuilder.create()
                .loadTrustMaterial(trustStore, null)
                .build();

        return HttpClients.custom()
                .setSSLContext(sslContext)
                .build();
    }


    public static String post(String endpoint, Map<String, String> params) throws IOException {
        String url = BASE_URL + endpoint;
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(url);

            // Convert parameters to JSON
            String json = objectMapper.writeValueAsString(params);
            post.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
            post.setHeader("Content-Type", "application/json");

            try (var response = client.execute(post)) {
                return response.getEntity() != null ? response.getEntity().getContent().toString() : "";
            }
        }
    }

    public static String get(String endpoint) throws IOException {
        String url = BASE_URL + endpoint;
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet get = new HttpGet(url);
            get.setHeader("Accept", "application/json");

            try (var response = client.execute(get)) {
                return response.getEntity() != null ? response.getEntity().getContent().toString() : "";
            }
        }
    }
}
